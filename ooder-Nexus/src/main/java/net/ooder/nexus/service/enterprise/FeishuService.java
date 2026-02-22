package net.ooder.nexus.service.enterprise;

import net.ooder.nexus.model.Result;

import java.util.List;

/**
 * Feishu (Lark) service interface
 * Provides integration with Feishu/Lark platform
 */
public interface FeishuService {

    /**
     * Send text message to a user
     */
    Result<Boolean> sendTextMessage(String userId, String message);

    /**
     * Send interactive message (card) to a user
     */
    Result<Boolean> sendInteractiveMessage(String userId, String cardContent);

    /**
     * Send message to a chat group
     */
    Result<Boolean> sendGroupMessage(String chatId, String message);

    /**
     * Send message to a department
     */
    Result<Boolean> sendDepartmentMessage(String departmentId, String message);

    /**
     * Get tenant access token
     */
    Result<String> getTenantAccessToken();

    /**
     * Get app access token
     */
    Result<String> getAppAccessToken();

    /**
     * Get department list
     */
    Result<List<Department>> getDepartmentList();

    /**
     * Get department users
     */
    Result<List<User>> getDepartmentUsers(String departmentId);

    /**
     * Get user info by user ID
     */
    Result<User> getUserInfo(String userId);

    /**
     * Get user ID by email
     */
    Result<String> getUserIdByEmail(String email);

    /**
     * Get user ID by mobile
     */
    Result<String> getUserIdByMobile(String mobile);

    /**
     * Department model
     */
    class Department {
        private String departmentId;
        private String name;
        private String parentDepartmentId;
        private int order;
        private String leaderUserId;
        private boolean hasChild;

        public String getDepartmentId() { return departmentId; }
        public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getParentDepartmentId() { return parentDepartmentId; }
        public void setParentDepartmentId(String parentDepartmentId) { this.parentDepartmentId = parentDepartmentId; }
        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }
        public String getLeaderUserId() { return leaderUserId; }
        public void setLeaderUserId(String leaderUserId) { this.leaderUserId = leaderUserId; }
        public boolean isHasChild() { return hasChild; }
        public void setHasChild(boolean hasChild) { this.hasChild = hasChild; }
    }

    /**
     * User model
     */
    class User {
        private String userId;
        private String name;
        private String enName;
        private String departmentId;
        private String position;
        private String mobile;
        private String email;
        private String avatar;
        private int status;
        private String employeeType;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEnName() { return enName; }
        public void setEnName(String enName) { this.enName = enName; }
        public String getDepartmentId() { return departmentId; }
        public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getEmployeeType() { return employeeType; }
        public void setEmployeeType(String employeeType) { this.employeeType = employeeType; }
    }
}
