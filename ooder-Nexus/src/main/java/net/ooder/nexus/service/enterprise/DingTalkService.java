package net.ooder.nexus.service.enterprise;

import net.ooder.nexus.model.Result;

import java.util.List;

/**
 * DingTalk service interface
 * Provides integration with DingTalk platform
 */
public interface DingTalkService {

    /**
     * Send text message to a user
     */
    Result<Boolean> sendTextMessage(String userId, String message);

    /**
     * Send markdown message to a user
     */
    Result<Boolean> sendMarkdownMessage(String userId, String markdown);

    /**
     * Send message to a department
     */
    Result<Boolean> sendDepartmentMessage(String deptId, String message);

    /**
     * Send work notification
     */
    Result<Boolean> sendWorkNotification(String userId, String title, String content);

    /**
     * Get access token
     */
    Result<String> getAccessToken();

    /**
     * Get department list
     */
    Result<List<Department>> getDepartmentList();

    /**
     * Get department users
     */
    Result<List<User>> getDepartmentUsers(String deptId);

    /**
     * Get user info
     */
    Result<User> getUserInfo(String userId);

    /**
     * Get user ID by mobile
     */
    Result<String> getUserIdByMobile(String mobile);

    /**
     * Department model
     */
    class Department {
        private String id;
        private String name;
        private String parentId;
        private boolean createDeptGroup;
        private boolean autoAddUser;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getParentId() { return parentId; }
        public void setParentId(String parentId) { this.parentId = parentId; }
        public boolean isCreateDeptGroup() { return createDeptGroup; }
        public void setCreateDeptGroup(boolean createDeptGroup) { this.createDeptGroup = createDeptGroup; }
        public boolean isAutoAddUser() { return autoAddUser; }
        public void setAutoAddUser(boolean autoAddUser) { this.autoAddUser = autoAddUser; }
    }

    /**
     * User model
     */
    class User {
        private String userId;
        private String name;
        private String department;
        private String position;
        private String mobile;
        private String email;
        private String avatar;
        private boolean active;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
}
