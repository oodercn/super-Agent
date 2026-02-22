package net.ooder.nexus.service.enterprise;

import net.ooder.nexus.model.Result;

import java.util.List;

/**
 * WeChat Work (Enterprise WeChat) service interface
 * Provides integration with WeChat Work platform
 */
public interface WechatWorkService {

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
     * Get user ID by email
     */
    Result<String> getUserIdByEmail(String email);

    /**
     * Department model
     */
    class Department {
        private String id;
        private String name;
        private String parentId;
        private int order;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getParentId() { return parentId; }
        public void setParentId(String parentId) { this.parentId = parentId; }
        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }
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
        private int status;

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
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
    }
}
