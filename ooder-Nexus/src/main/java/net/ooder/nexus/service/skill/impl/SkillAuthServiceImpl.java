package net.ooder.nexus.service.skill.impl;

import net.ooder.nexus.domain.skill.model.SkillAuthorization;
import net.ooder.nexus.domain.skill.model.SkillAuthorization.ResourcePermission;
import net.ooder.nexus.domain.skill.model.SkillAuthorization.ScenePermission;
import net.ooder.nexus.domain.skill.model.SkillResourceLog;
import net.ooder.nexus.service.skill.SkillAuthService;

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

@Service
public class SkillAuthServiceImpl implements SkillAuthService {

    private static final Logger log = LoggerFactory.getLogger(SkillAuthServiceImpl.class);

    private final ConcurrentHashMap<String, SkillAuthorization> authorizations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<SkillAuthorization>> userAuthorizations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<SkillResourceLog>> resourceLogs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<Map<String, Object>>> installRequests = new ConcurrentHashMap<>();

    @Override
    public List<SkillAuthorization> getPendingAuthorizations(String userId) {
        log.info("Getting pending authorizations for user: {}", userId);
        List<SkillAuthorization> userAuths = userAuthorizations.getOrDefault(userId, new ArrayList<>());
        return userAuths.stream()
                .filter(a -> a.getStatus() == SkillAuthorization.STATUS_PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillAuthorization> getSkillAuthorizations(String userId, String skillId) {
        log.info("Getting authorizations for skill: {}, user: {}", skillId, userId);
        List<SkillAuthorization> userAuths = userAuthorizations.getOrDefault(userId, new ArrayList<>());
        return userAuths.stream()
                .filter(a -> a.getSkillId().equals(skillId))
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillAuthorization> getAllAuthorizations(String userId) {
        log.info("Getting all authorizations for user: {}", userId);
        return userAuthorizations.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public SkillAuthorization createAuthorizationRequest(String userId, String skillId, String skillName,
            String authType, List<Map<String, Object>> resourcePermissions, 
            List<Map<String, Object>> scenePermissions, String authReason) {
        log.info("Creating authorization request: skill={}, user={}, type={}", skillId, userId, authType);
        
        SkillAuthorization auth = new SkillAuthorization();
        auth.setAuthId(UUID.randomUUID().toString());
        auth.setSkillId(skillId);
        auth.setSkillName(skillName);
        auth.setUserId(userId);
        auth.setAuthType(authType);
        auth.setAuthReason(authReason);
        
        if (resourcePermissions != null) {
            for (Map<String, Object> rp : resourcePermissions) {
                ResourcePermission perm = new ResourcePermission();
                perm.setResourceType((String) rp.get("resourceType"));
                perm.setResourceId((String) rp.get("resourceId"));
                perm.setResourceName((String) rp.get("resourceName"));
                perm.setAccessScope((String) rp.get("accessScope"));
                if (rp.get("permissions") != null) {
                    @SuppressWarnings("unchecked")
                    List<String> perms = (List<String>) rp.get("permissions");
                    perm.setPermissions(perms);
                }
                auth.getResourcePermissions().add(perm);
            }
        }
        
        if (scenePermissions != null) {
            for (Map<String, Object> sp : scenePermissions) {
                ScenePermission perm = new ScenePermission();
                perm.setSceneId((String) sp.get("sceneId"));
                perm.setSceneName((String) sp.get("sceneName"));
                perm.setSceneType((String) sp.get("sceneType"));
                perm.setJoinGroup(Boolean.TRUE.equals(sp.get("joinGroup")));
                perm.setGroupId((String) sp.get("groupId"));
                if (sp.get("capabilities") != null) {
                    @SuppressWarnings("unchecked")
                    List<String> caps = (List<String>) sp.get("capabilities");
                    perm.setCapabilities(caps);
                }
                auth.getScenePermissions().add(perm);
            }
        }
        
        authorizations.put(auth.getAuthId(), auth);
        userAuthorizations.computeIfAbsent(userId, k -> new ArrayList<>()).add(auth);
        
        return auth;
    }

    @Override
    public boolean approveAuthorization(String authId, String userId) {
        log.info("Approving authorization: authId={}, userId={}", authId, userId);
        SkillAuthorization auth = authorizations.get(authId);
        if (auth == null || !auth.getUserId().equals(userId)) {
            return false;
        }
        auth.setStatus(SkillAuthorization.STATUS_APPROVED);
        auth.setUpdatedAt(System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean rejectAuthorization(String authId, String userId, String reason) {
        log.info("Rejecting authorization: authId={}, userId={}", authId, userId);
        SkillAuthorization auth = authorizations.get(authId);
        if (auth == null || !auth.getUserId().equals(userId)) {
            return false;
        }
        auth.setStatus(SkillAuthorization.STATUS_REJECTED);
        auth.setAuthReason(reason);
        auth.setUpdatedAt(System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean revokeAuthorization(String authId, String userId) {
        log.info("Revoking authorization: authId={}, userId={}", authId, userId);
        SkillAuthorization auth = authorizations.get(authId);
        if (auth == null || !auth.getUserId().equals(userId)) {
            return false;
        }
        auth.setStatus(SkillAuthorization.STATUS_REVOKED);
        auth.setUpdatedAt(System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean checkPermission(String skillId, String userId, String resourceType, String resourceId, String action) {
        List<SkillAuthorization> userAuths = userAuthorizations.getOrDefault(userId, new ArrayList<>());
        for (SkillAuthorization auth : userAuths) {
            if (auth.getSkillId().equals(skillId) && auth.getStatus() == SkillAuthorization.STATUS_APPROVED) {
                for (ResourcePermission rp : auth.getResourcePermissions()) {
                    if ((rp.getResourceType().equals(resourceType) || rp.getResourceType().equals("*")) &&
                        (rp.getResourceId().equals(resourceId) || rp.getResourceId().equals("*"))) {
                        if (rp.getPermissions().contains(action) || rp.getPermissions().contains("*")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void logResourceAccess(String skillId, String skillName, String userId, String action,
            String resourceType, String resourceId, String resourceName,
            String sceneId, String sceneName, String groupId, String detail, int status) {
        log.info("Logging resource access: skill={}, action={}, resource={}", skillId, action, resourceId);
        
        SkillResourceLog logEntry = new SkillResourceLog();
        logEntry.setLogId(UUID.randomUUID().toString());
        logEntry.setSkillId(skillId);
        logEntry.setSkillName(skillName);
        logEntry.setUserId(userId);
        logEntry.setAction(action);
        logEntry.setResourceType(resourceType);
        logEntry.setResourceId(resourceId);
        logEntry.setResourceName(resourceName);
        logEntry.setSceneId(sceneId);
        logEntry.setSceneName(sceneName);
        logEntry.setGroupId(groupId);
        logEntry.setDetail(detail);
        logEntry.setStatus(status);
        
        resourceLogs.computeIfAbsent(userId, k -> new ArrayList<>()).add(logEntry);
    }

    @Override
    public List<SkillResourceLog> getResourceLogs(String userId, Map<String, Object> filters) {
        log.info("Getting resource logs for user: {}", userId);
        List<SkillResourceLog> logs = resourceLogs.getOrDefault(userId, new ArrayList<>());
        
        if (filters == null) {
            return logs;
        }
        
        return logs.stream()
                .filter(l -> {
                    if (filters.get("skillId") != null && !l.getSkillId().equals(filters.get("skillId"))) {
                        return false;
                    }
                    if (filters.get("action") != null && !l.getAction().equals(filters.get("action"))) {
                        return false;
                    }
                    if (filters.get("resourceType") != null && !l.getResourceType().equals(filters.get("resourceType"))) {
                        return false;
                    }
                    if (filters.get("status") != null && l.getStatus() != (Integer) filters.get("status")) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillResourceLog> getSkillResourceLogs(String skillId, String userId) {
        log.info("Getting resource logs for skill: {}, user: {}", skillId, userId);
        List<SkillResourceLog> logs = resourceLogs.getOrDefault(userId, new ArrayList<>());
        return logs.stream()
                .filter(l -> l.getSkillId().equals(skillId))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getResourceUsageStats(String userId, String skillId) {
        log.info("Getting resource usage stats for skill: {}, user: {}", skillId, userId);
        Map<String, Object> stats = new HashMap<>();
        
        List<SkillResourceLog> logs = resourceLogs.getOrDefault(userId, new ArrayList<>());
        List<SkillResourceLog> skillLogs = logs.stream()
                .filter(l -> skillId == null || l.getSkillId().equals(skillId))
                .collect(Collectors.toList());
        
        stats.put("totalAccess", skillLogs.size());
        
        Map<String, Long> actionCounts = new HashMap<>();
        Map<String, Long> resourceTypeCounts = new HashMap<>();
        int successCount = 0;
        int failedCount = 0;
        int deniedCount = 0;
        
        for (SkillResourceLog l : skillLogs) {
            actionCounts.merge(l.getAction(), 1L, Long::sum);
            resourceTypeCounts.merge(l.getResourceType(), 1L, Long::sum);
            
            if (l.getStatus() == SkillResourceLog.STATUS_SUCCESS) successCount++;
            else if (l.getStatus() == SkillResourceLog.STATUS_FAILED) failedCount++;
            else if (l.getStatus() == SkillResourceLog.STATUS_DENIED) deniedCount++;
        }
        
        stats.put("actionCounts", actionCounts);
        stats.put("resourceTypeCounts", resourceTypeCounts);
        stats.put("successCount", successCount);
        stats.put("failedCount", failedCount);
        stats.put("deniedCount", deniedCount);
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getInstallRequests(String userId) {
        log.info("Getting install requests for user: {}", userId);
        return installRequests.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public Map<String, Object> processInstallRequest(String requestId, String userId, boolean approved, String reason) {
        log.info("Processing install request: requestId={}, approved={}", requestId, approved);
        
        List<Map<String, Object>> requests = installRequests.getOrDefault(userId, new ArrayList<>());
        Map<String, Object> request = null;
        
        for (Map<String, Object> r : requests) {
            if (r.get("requestId").equals(requestId)) {
                request = r;
                break;
            }
        }
        
        if (request == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "请求不存在");
            return result;
        }
        
        request.put("status", approved ? "approved" : "rejected");
        request.put("processedAt", System.currentTimeMillis());
        request.put("reason", reason);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", approved ? "已批准安装" : "已拒绝安装");
        result.put("request", request);
        
        return result;
    }
}
