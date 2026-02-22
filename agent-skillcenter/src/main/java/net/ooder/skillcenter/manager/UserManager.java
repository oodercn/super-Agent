/*
 * Copyright (c) 2024 Ooder Team
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.ooder.skillcenter.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * User Manager - Responsible for user CRUD operations
 */
public class UserManager {
    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    // 单例实例
    private static UserManager instance;
    
    // 用户映射，key为用户ID，value为用户实例
    private Map<String, User> userMap;

    /**
     * 私有构造方法
     */
    private UserManager() {
        this.userMap = new HashMap<>();
        this.loadSampleData();
    }

    /**
     * 获取实例
     * @return 用户管理器实例
     */
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * 创建新用户
     * @param user 用户实例
     * @return 创建的用户实例
     */
    public synchronized User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // 生成用户ID
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId("user-" + UUID.randomUUID().toString().substring(0, 8));
        }

        // 设置创建时间
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        // 添加到用户映射
        userMap.put(user.getId(), user);
        return user;
    }

    /**
     * 获取指定ID的用户
     * @param id 用户ID
     * @return 用户实例，不存在则返回null
     */
    public User getUser(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return userMap.get(id);
    }

    /**
     * 获取所有用户
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    /**
     * 更新用户信息
     * @param user 用户实例
     * @return 更新后的用户实例
     */
    public synchronized User updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getId() == null || user.getId().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        if (!userMap.containsKey(user.getId())) {
            throw new IllegalArgumentException("User not found: " + user.getId());
        }

        // 更新用户
        userMap.put(user.getId(), user);
        return user;
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除是否成功
     */
    public synchronized boolean deleteUser(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        return userMap.remove(id) != null;
    }

    /**
     * 搜索用户
     * @param keyword 关键词
     * @return 匹配的用户列表
     */
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllUsers();
        }

        List<User> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (User user : userMap.values()) {
            if (user.getName().toLowerCase().contains(lowerKeyword) || 
                user.getId().toLowerCase().contains(lowerKeyword) ||
                (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerKeyword))) {
                result.add(user);
            }
        }

        return result;
    }

    /**
     * 根据群组ID获取成员
     * @param groupId 群组ID
     * @return 用户列表
     */
    public List<User> getUsersByGroupId(String groupId) {
        if (groupId == null || groupId.isEmpty()) {
            return new ArrayList<>();
        }

        GroupManager groupManager = GroupManager.getInstance();
        GroupManager.Group group = groupManager.getGroup(groupId);
        
        if (group == null || group.getMembers() == null) {
            return new ArrayList<>();
        }

        List<User> result = new ArrayList<>();
        for (String userId : group.getMembers()) {
            User user = userMap.get(userId);
            if (user != null) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * 加载示例数据
     */
    private void loadSampleData() {
        // 创建示例用户数据
        User user1 = new User();
        user1.setId("User123");
        user1.setName("张三");
        user1.setEmail("zhangsan@example.com");
        user1.setRole("developer");
        user1.setStatus("active");
        user1.setCreatedAt(LocalDateTime.now().minusDays(30));
        userMap.put(user1.getId(), user1);

        User user2 = new User();
        user2.setId("User456");
        user2.setName("李四");
        user2.setEmail("lisi@example.com");
        user2.setRole("designer");
        user2.setStatus("active");
        user2.setCreatedAt(LocalDateTime.now().minusDays(25));
        userMap.put(user2.getId(), user2);

        User user3 = new User();
        user3.setId("User789");
        user3.setName("王五");
        user3.setEmail("wangwu@example.com");
        user3.setRole("developer");
        user3.setStatus("active");
        user3.setCreatedAt(LocalDateTime.now().minusDays(20));
        userMap.put(user3.getId(), user3);

        User user4 = new User();
        user4.setId("User101");
        user4.setName("赵六");
        user4.setEmail("zhaoliu@example.com");
        user4.setRole("manager");
        user4.setStatus("active");
        user4.setCreatedAt(LocalDateTime.now().minusDays(15));
        userMap.put(user4.getId(), user4);

        User user5 = new User();
        user5.setId("User202");
        user5.setName("孙七");
        user5.setEmail("sunqi@example.com");
        user5.setRole("developer");
        user5.setStatus("inactive");
        user5.setCreatedAt(LocalDateTime.now().minusDays(10));
        userMap.put(user5.getId(), user5);

        User user6 = new User();
        user6.setId("User303");
        user6.setName("周八");
        user6.setEmail("zhouba@example.com");
        user6.setRole("designer");
        user6.setStatus("active");
        user6.setCreatedAt(LocalDateTime.now().minusDays(18));
        userMap.put(user6.getId(), user6);

        User user7 = new User();
        user7.setId("User404");
        user7.setName("吴九");
        user7.setEmail("wujiu@example.com");
        user7.setRole("designer");
        user7.setStatus("active");
        user7.setCreatedAt(LocalDateTime.now().minusDays(12));
        userMap.put(user7.getId(), user7);

        User user8 = new User();
        user8.setId("User505");
        user8.setName("郑十");
        user8.setEmail("zhengshi@example.com");
        user8.setRole("marketing");
        user8.setStatus("active");
        user8.setCreatedAt(LocalDateTime.now().minusDays(8));
        userMap.put(user8.getId(), user8);

        User user9 = new User();
        user9.setId("User606");
        user9.setName("钱十一");
        user9.setEmail("qian11@example.com");
        user9.setRole("marketing");
        user9.setStatus("active");
        user9.setCreatedAt(LocalDateTime.now().minusDays(22));
        userMap.put(user9.getId(), user9);

        User user10 = new User();
        user10.setId("User707");
        user10.setName("冯十二");
        user10.setEmail("feng12@example.com");
        user10.setRole("marketing");
        user10.setStatus("active");
        user10.setCreatedAt(LocalDateTime.now().minusDays(16));
        userMap.put(user10.getId(), user10);

        logger.info("Loaded {} sample users", userMap.size());
    }

    /**
     * 用户类
     */
    public static class User {
        private String id;
        private String name;
        private String email;
        private String role;
        private String status;
        private LocalDateTime createdAt;

        // getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}
