package net.ooder.nexus.service.security.impl;

import net.ooder.nexus.service.security.AccessControlService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 访问控制服务实现
 * 提供基于内存的访问控制功能，生产环境应替换为持久化存储
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since 0.7.3
 */
@Service
public class AccessControlServiceImpl implements AccessControlService {

    private static final Logger log = LoggerFactory.getLogger(AccessControlServiceImpl.class);

    private final ConcurrentHashMap<String, Map<String, Object>> whitelist = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, Object>> blacklist = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, Object>> apiRules = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, Object>> roles = new ConcurrentHashMap<>();

    public AccessControlServiceImpl() {
        initDefaultData();
    }

    private void initDefaultData() {
        Map<String, Object> adminRole = new HashMap<String, Object>();
        adminRole.put("id", "role-admin");
        adminRole.put("name", "管理员");
        adminRole.put("type", "system");
        adminRole.put("permissions", new ArrayList<String>() {{
            add("read");
            add("write");
            add("delete");
            add("admin");
        }});
        roles.put("role-admin", adminRole);

        Map<String, Object> userRole = new HashMap<String, Object>();
        userRole.put("id", "role-user");
        userRole.put("name", "普通用户");
        userRole.put("type", "system");
        userRole.put("permissions", new ArrayList<String>() {{
            add("read");
            add("write");
        }});
        roles.put("role-user", userRole);

        Map<String, Object> guestRole = new HashMap<String, Object>();
        guestRole.put("id", "role-guest");
        guestRole.put("name", "访客");
        guestRole.put("type", "system");
        guestRole.put("permissions", new ArrayList<String>() {{
            add("read");
        }});
        roles.put("role-guest", guestRole);

        Map<String, Object> apiRule1 = new HashMap<String, Object>();
        apiRule1.put("id", "api-1");
        apiRule1.put("path", "/api/admin/*");
        apiRule1.put("roles", new ArrayList<String>() {{
            add("管理员");
        }});
        apiRule1.put("rateLimit", "100/min");
        apiRule1.put("enabled", true);
        apiRules.put("api-1", apiRule1);

        Map<String, Object> apiRule2 = new HashMap<String, Object>();
        apiRule2.put("id", "api-2");
        apiRule2.put("path", "/api/skill/*");
        apiRule2.put("roles", new ArrayList<String>() {{
            add("管理员");
            add("普通用户");
        }});
        apiRule2.put("rateLimit", "1000/min");
        apiRule2.put("enabled", true);
        apiRules.put("api-2", apiRule2);

        log.info("AccessControlService initialized with default data");
    }

    @Override
    public List<Map<String, Object>> getWhitelist() {
        log.info("Getting whitelist");
        return new ArrayList<>(whitelist.values());
    }

    @Override
    public Map<String, Object> addToWhitelist(Map<String, Object> ipData) {
        log.info("Adding to whitelist: {}", ipData.get("ip"));
        String id = UUID.randomUUID().toString();
        Map<String, Object> entry = new HashMap<String, Object>(ipData);
        entry.put("id", id);
        entry.put("createdAt", System.currentTimeMillis());
        whitelist.put(id, entry);
        return entry;
    }

    @Override
    public boolean removeFromWhitelist(String id) {
        log.info("Removing from whitelist: {}", id);
        return whitelist.remove(id) != null;
    }

    @Override
    public List<Map<String, Object>> getBlacklist() {
        log.info("Getting blacklist");
        return new ArrayList<>(blacklist.values());
    }

    @Override
    public Map<String, Object> addToBlacklist(Map<String, Object> ipData) {
        log.info("Adding to blacklist: {}", ipData.get("ip"));
        String id = UUID.randomUUID().toString();
        Map<String, Object> entry = new HashMap<String, Object>(ipData);
        entry.put("id", id);
        entry.put("bannedAt", System.currentTimeMillis());
        
        String expiry = (String) ipData.get("expiry");
        if (expiry != null && !"permanent".equals(expiry)) {
            long duration = parseExpiry(expiry);
            entry.put("expiresAt", System.currentTimeMillis() + duration);
        }
        
        blacklist.put(id, entry);
        return entry;
    }

    @Override
    public boolean unbanFromBlacklist(String id) {
        log.info("Unbanning from blacklist: {}", id);
        return blacklist.remove(id) != null;
    }

    @Override
    public List<Map<String, Object>> getApiRules() {
        log.info("Getting API rules");
        return new ArrayList<>(apiRules.values());
    }

    @Override
    public Map<String, Object> saveApiRule(Map<String, Object> ruleData) {
        log.info("Saving API rule: {}", ruleData.get("path"));
        String id = (String) ruleData.get("id");
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
        Map<String, Object> entry = new HashMap<String, Object>(ruleData);
        entry.put("id", id);
        apiRules.put(id, entry);
        return entry;
    }

    @Override
    public boolean deleteApiRule(String id) {
        log.info("Deleting API rule: {}", id);
        return apiRules.remove(id) != null;
    }

    @Override
    public List<Map<String, Object>> getRoles() {
        log.info("Getting roles");
        return new ArrayList<>(roles.values());
    }

    @Override
    public Map<String, Object> saveRole(Map<String, Object> roleData) {
        log.info("Saving role: {}", roleData.get("name"));
        String id = (String) roleData.get("id");
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
        Map<String, Object> entry = new HashMap<String, Object>(roleData);
        entry.put("id", id);
        roles.put(id, entry);
        return entry;
    }

    @Override
    public boolean deleteRole(String id) {
        log.info("Deleting role: {}", id);
        Map<String, Object> role = roles.get(id);
        if (role != null && "system".equals(role.get("type"))) {
            log.warn("Cannot delete system role: {}", id);
            return false;
        }
        return roles.remove(id) != null;
    }

    @Override
    public boolean isIpAllowed(String ip) {
        for (Map<String, Object> entry : blacklist.values()) {
            String bannedIp = (String) entry.get("ip");
            Long expiresAt = (Long) entry.get("expiresAt");
            
            if (expiresAt != null && System.currentTimeMillis() > expiresAt) {
                continue;
            }
            
            if (ipMatches(ip, bannedIp)) {
                return false;
            }
        }
        
        if (!whitelist.isEmpty()) {
            for (Map<String, Object> entry : whitelist.values()) {
                String allowedIp = (String) entry.get("ip");
                if (ipMatches(ip, allowedIp)) {
                    return true;
                }
            }
            return false;
        }
        
        return true;
    }

    @Override
    public boolean hasApiAccess(String userId, String apiPath) {
        log.debug("Checking API access for user {} to {}", userId, apiPath);
        
        for (Map<String, Object> rule : apiRules.values()) {
            String path = (String) rule.get("path");
            Boolean enabled = (Boolean) rule.get("enabled");
            
            if (enabled != null && enabled && pathMatches(apiPath, path)) {
                return true;
            }
        }
        
        return true;
    }

    private boolean ipMatches(String ip, String pattern) {
        if (pattern == null) return false;
        if (pattern.equals(ip)) return true;
        if (pattern.endsWith("*")) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            return ip.startsWith(prefix);
        }
        if (pattern.contains("/")) {
            return isInSubnet(ip, pattern);
        }
        return false;
    }

    private boolean isInSubnet(String ip, String cidr) {
        return ip.startsWith(cidr.split("/")[0].substring(0, cidr.split("/")[0].lastIndexOf(".")));
    }

    private boolean pathMatches(String path, String pattern) {
        if (pattern == null) return false;
        if (pattern.equals(path)) return true;
        if (pattern.endsWith("*")) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            return path.startsWith(prefix);
        }
        return false;
    }

    private long parseExpiry(String expiry) {
        if (expiry == null) return 0;
        
        if (expiry.equals("1h")) return 60 * 60 * 1000L;
        if (expiry.equals("24h")) return 24 * 60 * 60 * 1000L;
        if (expiry.equals("7d")) return 7 * 24 * 60 * 60 * 1000L;
        if (expiry.equals("30d")) return 30 * 24 * 60 * 60 * 1000L;
        
        return 0;
    }
}
