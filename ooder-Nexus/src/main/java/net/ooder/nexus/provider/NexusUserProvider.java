package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * UserProvider 实现
 *
 * <p>实现用户管理 Provider 接口</p>
 */
@Component
public class NexusUserProvider implements UserProvider {

    private static final Logger log = LoggerFactory.getLogger(NexusUserProvider.class);
    private static final String PROVIDER_NAME = "NexusUserProvider";
    private static final String VERSION = "1.0.0";

    private SceneEngine sceneEngine;
    private boolean initialized = false;
    private boolean running = false;

    private final Map<String, UserInfo> users = new ConcurrentHashMap<String, UserInfo>();
    private final Map<String, Permission> permissions = new ConcurrentHashMap<String, Permission>();
    private final Map<String, List<String>> userPermissions = new ConcurrentHashMap<String, List<String>>();
    private final List<SecurityLog> securityLogs = new ArrayList<SecurityLog>();
    
    private final AtomicLong logIdCounter = new AtomicLong(0);

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public void initialize(SceneEngine engine) {
        this.sceneEngine = engine;
        this.initialized = true;
        
        initDefaultData();
        
        log.info("NexusUserProvider initialized");
    }

    @Override
    public void start() {
        this.running = true;
        log.info("NexusUserProvider started");
    }

    @Override
    public void stop() {
        this.running = false;
        log.info("NexusUserProvider stopped");
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void initDefaultData() {
        UserInfo admin = new UserInfo();
        admin.setUserId("user-admin");
        admin.setUsername("admin");
        admin.setEmail("admin@ooder.net");
        admin.setStatus("active");
        admin.setRoles(Arrays.asList("admin", "user"));
        admin.setCreatedAt(System.currentTimeMillis());
        admin.setUpdatedAt(System.currentTimeMillis());
        users.put(admin.getUserId(), admin);
        
        UserInfo user = new UserInfo();
        user.setUserId("user-001");
        user.setUsername("user");
        user.setEmail("user@ooder.net");
        user.setStatus("active");
        user.setRoles(Arrays.asList("user"));
        user.setCreatedAt(System.currentTimeMillis());
        user.setUpdatedAt(System.currentTimeMillis());
        users.put(user.getUserId(), user);
        
        Permission perm1 = new Permission();
        perm1.setPermissionId("perm-admin");
        perm1.setName("管理员权限");
        perm1.setResource("*");
        perm1.setAction("*");
        perm1.setDescription("完全管理权限");
        permissions.put(perm1.getPermissionId(), perm1);
        
        Permission perm2 = new Permission();
        perm2.setPermissionId("perm-read");
        perm2.setName("读取权限");
        perm2.setResource("*");
        perm2.setAction("read");
        perm2.setDescription("只读权限");
        permissions.put(perm2.getPermissionId(), perm2);
        
        userPermissions.put("user-admin", Arrays.asList("perm-admin", "perm-read"));
        userPermissions.put("user-001", Arrays.asList("perm-read"));
    }

    @Override
    public Result<UserStatus> getStatus() {
        log.debug("Getting user status");
        
        UserStatus status = new UserStatus();
        status.setTotalUsers(users.size());
        status.setActiveUsers((int) users.values().stream()
            .filter(u -> "active".equals(u.getStatus())).count());
        status.setDisabledUsers((int) users.values().stream()
            .filter(u -> "disabled".equals(u.getStatus())).count());
        status.setLastUpdated(System.currentTimeMillis());
        
        return Result.success(status);
    }

    @Override
    public Result<PageResult<UserInfo>> listUsers(int page, int size) {
        log.debug("Listing users: page={}, size={}", page, size);
        
        List<UserInfo> allUsers = new ArrayList<UserInfo>(users.values());
        
        int start = (page - 1) * size;
        int end = Math.min(start + size, allUsers.size());
        
        List<UserInfo> pageData = new ArrayList<UserInfo>();
        if (start < allUsers.size()) {
            pageData = allUsers.subList(start, end);
        }
        
        PageResult<UserInfo> pageResult = new PageResult<UserInfo>();
        pageResult.setItems(pageData);
        pageResult.setTotal(allUsers.size());
        pageResult.setPageNum(page);
        pageResult.setPageSize(size);
        
        return Result.success(pageResult);
    }

    @Override
    public Result<UserInfo> getUser(String userId) {
        log.debug("Getting user: {}", userId);
        
        UserInfo user = users.get(userId);
        if (user == null) {
            return Result.error("User not found: " + userId);
        }
        
        return Result.success(user);
    }

    @Override
    public Result<UserInfo> createUser(Map<String, Object> userData) {
        log.info("Creating user: {}", userData);
        
        try {
            String userId = "user-" + UUID.randomUUID().toString().substring(0, 8);
            
            UserInfo user = new UserInfo();
            user.setUserId(userId);
            user.setUsername((String) userData.get("username"));
            user.setEmail((String) userData.get("email"));
            user.setStatus("active");
            
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) userData.getOrDefault("roles", Arrays.asList("user"));
            user.setRoles(roles);
            
            user.setCreatedAt(System.currentTimeMillis());
            user.setUpdatedAt(System.currentTimeMillis());
            
            users.put(userId, user);
            userPermissions.put(userId, new ArrayList<String>());
            
            addSecurityLog(userId, "create", "user", "success");
            
            return Result.success(user);
        } catch (Exception e) {
            log.error("Failed to create user", e);
            return Result.error("创建用户失败: " + e.getMessage());
        }
    }

    @Override
    public Result<UserInfo> updateUser(String userId, Map<String, Object> userData) {
        log.info("Updating user: {}", userId);
        
        UserInfo user = users.get(userId);
        if (user == null) {
            return Result.error("User not found: " + userId);
        }
        
        if (userData.containsKey("username")) {
            user.setUsername((String) userData.get("username"));
        }
        if (userData.containsKey("email")) {
            user.setEmail((String) userData.get("email"));
        }
        if (userData.containsKey("roles")) {
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) userData.get("roles");
            user.setRoles(roles);
        }
        
        user.setUpdatedAt(System.currentTimeMillis());
        
        addSecurityLog(userId, "update", "user", "success");
        
        return Result.success(user);
    }

    @Override
    public Result<Boolean> deleteUser(String userId) {
        log.info("Deleting user: {}", userId);
        
        UserInfo removed = users.remove(userId);
        if (removed == null) {
            return Result.error("User not found: " + userId);
        }
        
        userPermissions.remove(userId);
        
        addSecurityLog(userId, "delete", "user", "success");
        
        return Result.success(true);
    }

    @Override
    public Result<UserInfo> enableUser(String userId) {
        log.info("Enabling user: {}", userId);
        
        UserInfo user = users.get(userId);
        if (user == null) {
            return Result.error("User not found: " + userId);
        }
        
        user.setStatus("active");
        user.setUpdatedAt(System.currentTimeMillis());
        
        addSecurityLog(userId, "enable", "user", "success");
        
        return Result.success(user);
    }

    @Override
    public Result<UserInfo> disableUser(String userId) {
        log.info("Disabling user: {}", userId);
        
        UserInfo user = users.get(userId);
        if (user == null) {
            return Result.error("User not found: " + userId);
        }
        
        user.setStatus("disabled");
        user.setUpdatedAt(System.currentTimeMillis());
        
        addSecurityLog(userId, "disable", "user", "success");
        
        return Result.success(user);
    }

    @Override
    public Result<PageResult<Permission>> listPermissions(int page, int size) {
        log.debug("Listing permissions: page={}, size={}", page, size);
        
        List<Permission> allPerms = new ArrayList<Permission>(permissions.values());
        
        int start = (page - 1) * size;
        int end = Math.min(start + size, allPerms.size());
        
        List<Permission> pageData = new ArrayList<Permission>();
        if (start < allPerms.size()) {
            pageData = allPerms.subList(start, end);
        }
        
        PageResult<Permission> pageResult = new PageResult<Permission>();
        pageResult.setItems(pageData);
        pageResult.setTotal(allPerms.size());
        pageResult.setPageNum(page);
        pageResult.setPageSize(size);
        
        return Result.success(pageResult);
    }

    @Override
    public Result<Boolean> savePermissions(String userId, List<String> permissionIds) {
        log.info("Saving permissions for user: {}, permissions: {}", userId, permissionIds);
        
        if (!users.containsKey(userId)) {
            return Result.error("User not found: " + userId);
        }
        
        userPermissions.put(userId, new ArrayList<String>(permissionIds));
        
        addSecurityLog(userId, "save_permissions", "permissions", "success");
        
        return Result.success(true);
    }

    @Override
    public Result<List<Permission>> getUserPermissions(String userId) {
        log.debug("Getting permissions for user: {}", userId);
        
        if (!users.containsKey(userId)) {
            return Result.error("User not found: " + userId);
        }
        
        List<String> permIds = userPermissions.get(userId);
        if (permIds == null) {
            return Result.success(new ArrayList<Permission>());
        }
        
        List<Permission> userPerms = new ArrayList<Permission>();
        for (String permId : permIds) {
            Permission perm = permissions.get(permId);
            if (perm != null) {
                userPerms.add(perm);
            }
        }
        
        return Result.success(userPerms);
    }

    @Override
    public Result<PageResult<SecurityLog>> listSecurityLogs(int page, int size) {
        log.debug("Listing security logs: page={}, size={}", page, size);
        
        List<SecurityLog> allLogs = new ArrayList<SecurityLog>(securityLogs);
        Collections.reverse(allLogs);
        
        int start = (page - 1) * size;
        int end = Math.min(start + size, allLogs.size());
        
        List<SecurityLog> pageData = new ArrayList<SecurityLog>();
        if (start < allLogs.size()) {
            pageData = allLogs.subList(start, end);
        }
        
        PageResult<SecurityLog> pageResult = new PageResult<SecurityLog>();
        pageResult.setItems(pageData);
        pageResult.setTotal(allLogs.size());
        pageResult.setPageNum(page);
        pageResult.setPageSize(size);
        
        return Result.success(pageResult);
    }

    private void addSecurityLog(String userId, String action, String resource, String result) {
        SecurityLog logEntry = new SecurityLog();
        logEntry.setLogId("log-" + logIdCounter.incrementAndGet());
        logEntry.setUserId(userId);
        logEntry.setAction(action);
        logEntry.setResource(resource);
        logEntry.setResult(result);
        logEntry.setIpAddress("127.0.0.1");
        logEntry.setTimestamp(System.currentTimeMillis());
        
        securityLogs.add(logEntry);
        
        while (securityLogs.size() > 1000) {
            securityLogs.remove(0);
        }
    }
}
