package net.ooder.nexus.adapter.inbound.controller.enterprise;

import net.ooder.nexus.domain.org.Department;
import net.ooder.nexus.domain.org.OrgUser;
import net.ooder.nexus.dto.common.OrgTreeDTO;
import net.ooder.nexus.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Organization controller
 * Provides organization tree view and management APIs
 */
@RestController
@RequestMapping("/api/org")
public class OrganizationController {

    private static final Logger log = LoggerFactory.getLogger(OrganizationController.class);

    /**
     * Get organization tree
     */
    @PostMapping("/tree")
    public Result<List<OrgTreeDTO>> getOrgTree(@RequestBody(required = false) Map<String, Object> request) {
        log.info("Getting organization tree");
        try {
            List<OrgTreeDTO> tree = buildMockOrgTree();
            return Result.success("获取成功", tree);
        } catch (Exception e) {
            log.error("Failed to get organization tree", e);
            return Result.error("获取组织树失败: " + e.getMessage());
        }
    }

    /**
     * Get department tree
     */
    @PostMapping("/department/tree")
    public Result<List<Department>> getDepartmentTree() {
        log.info("Getting department tree");
        try {
            List<Department> tree = buildMockDepartmentTree();
            return Result.success("获取成功", tree);
        } catch (Exception e) {
            log.error("Failed to get department tree", e);
            return Result.error("获取部门树失败: " + e.getMessage());
        }
    }

    /**
     * Get department users
     */
    @PostMapping("/user/list")
    public Result<List<OrgUser>> getDepartmentUsers(@RequestBody Map<String, String> request) {
        String departmentId = request.get("departmentId");
        log.info("Getting users for department: {}", departmentId);
        try {
            List<OrgUser> users = getMockUsers(departmentId);
            return Result.success("获取成功", users);
        } catch (Exception e) {
            log.error("Failed to get department users", e);
            return Result.error("获取部门用户失败: " + e.getMessage());
        }
    }

    /**
     * Search users
     */
    @PostMapping("/user/search")
    public Result<List<OrgUser>> searchUsers(@RequestBody Map<String, String> request) {
        String keyword = request.get("keyword");
        log.info("Searching users with keyword: {}", keyword);
        try {
            List<OrgUser> users = searchMockUsers(keyword);
            return Result.success("搜索成功", users);
        } catch (Exception e) {
            log.error("Failed to search users", e);
            return Result.error("搜索用户失败: " + e.getMessage());
        }
    }

    /**
     * Get organization statistics
     */
    @PostMapping("/stats")
    public Result<OrgStats> getOrgStats() {
        log.info("Getting organization statistics");
        try {
            OrgStats stats = new OrgStats();
            stats.setTotalDepartments(5);
            stats.setTotalUsers(12);
            stats.setTotalGroups(3);
            return Result.success("获取成功", stats);
        } catch (Exception e) {
            log.error("Failed to get organization statistics", e);
            return Result.error("获取统计信息失败: " + e.getMessage());
        }
    }

    /**
     * Sync organization
     */
    @PostMapping("/sync")
    public Result<Void> syncOrganization() {
        log.info("Syncing organization");
        return Result.success("同步成功", null);
    }

    // ==================== Mock Data Methods ====================

    private List<OrgTreeDTO> buildMockOrgTree() {
        List<OrgTreeDTO> tree = new ArrayList<>();

        OrgTreeDTO root = createNode("dept-001", "Ooder Technology", "department", null, 1);
        root.setExpanded(true);
        root.setHasChildren(true);

        OrgTreeDTO devDept = createNode("dept-002", "Development Department", "department", "dept-001", 1);
        devDept.setHasChildren(true);

        OrgTreeDTO testDept = createNode("dept-003", "Testing Department", "department", "dept-001", 2);
        testDept.setHasChildren(true);

        OrgTreeDTO productDept = createNode("dept-004", "Product Department", "department", "dept-001", 3);
        productDept.setHasChildren(true);

        OrgTreeDTO user1 = createUserNode("user-001", "Zhang San", "dept-002", "Senior Developer", "zhangsan@ooder.net", "13800138001");
        OrgTreeDTO user2 = createUserNode("user-002", "Li Si", "dept-002", "Developer", "lisi@ooder.net", "13800138002");
        OrgTreeDTO user3 = createUserNode("user-003", "Wang Wu", "dept-003", "Test Engineer", "wangwu@ooder.net", "13800138003");
        OrgTreeDTO user4 = createUserNode("user-004", "Zhao Liu", "dept-004", "Product Manager", "zhaoliu@ooder.net", "13800138004");

        devDept.setChildren(new ArrayList<>());
        devDept.getChildren().add(user1);
        devDept.getChildren().add(user2);

        testDept.setChildren(new ArrayList<>());
        testDept.getChildren().add(user3);

        productDept.setChildren(new ArrayList<>());
        productDept.getChildren().add(user4);

        root.setChildren(new ArrayList<>());
        root.getChildren().add(devDept);
        root.getChildren().add(testDept);
        root.getChildren().add(productDept);

        tree.add(root);
        return tree;
    }

    private List<Department> buildMockDepartmentTree() {
        List<Department> tree = new ArrayList<>();

        Department root = new Department("dept-001", "Ooder Technology");
        root.setLevel(1);
        root.setPath("/Ooder Technology");

        Department devDept = new Department("dept-002", "Development Department");
        devDept.setParentId("dept-001");
        devDept.setLevel(2);
        devDept.setPath("/Ooder Technology/Development Department");
        devDept.setMemberCount(2);

        Department testDept = new Department("dept-003", "Testing Department");
        testDept.setParentId("dept-001");
        testDept.setLevel(2);
        testDept.setPath("/Ooder Technology/Testing Department");
        testDept.setMemberCount(1);

        Department productDept = new Department("dept-004", "Product Department");
        productDept.setParentId("dept-001");
        productDept.setLevel(2);
        productDept.setPath("/Ooder Technology/Product Department");
        productDept.setMemberCount(1);

        root.addChild(devDept);
        root.addChild(testDept);
        root.addChild(productDept);
        root.setMemberCount(4);

        tree.add(root);
        return tree;
    }

    private List<OrgUser> getMockUsers(String departmentId) {
        List<OrgUser> users = new ArrayList<>();
        
        if ("dept-002".equals(departmentId)) {
            users.add(createMockUser("user-001", "Zhang San", "Senior Developer", "zhangsan@ooder.net", "13800138001"));
            users.add(createMockUser("user-002", "Li Si", "Developer", "lisi@ooder.net", "13800138002"));
        } else if ("dept-003".equals(departmentId)) {
            users.add(createMockUser("user-003", "Wang Wu", "Test Engineer", "wangwu@ooder.net", "13800138003"));
        } else if ("dept-004".equals(departmentId)) {
            users.add(createMockUser("user-004", "Zhao Liu", "Product Manager", "zhaoliu@ooder.net", "13800138004"));
        }
        
        return users;
    }

    private List<OrgUser> searchMockUsers(String keyword) {
        List<OrgUser> allUsers = new ArrayList<>();
        allUsers.add(createMockUser("user-001", "Zhang San", "Senior Developer", "zhangsan@ooder.net", "13800138001"));
        allUsers.add(createMockUser("user-002", "Li Si", "Developer", "lisi@ooder.net", "13800138002"));
        allUsers.add(createMockUser("user-003", "Wang Wu", "Test Engineer", "wangwu@ooder.net", "13800138003"));
        allUsers.add(createMockUser("user-004", "Zhao Liu", "Product Manager", "zhaoliu@ooder.net", "13800138004"));

        List<OrgUser> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (OrgUser user : allUsers) {
            if (user.getName().toLowerCase().contains(lowerKeyword) ||
                (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerKeyword)) ||
                (user.getMobile() != null && user.getMobile().contains(lowerKeyword))) {
                results.add(user);
            }
        }
        return results;
    }

    private OrgUser createMockUser(String id, String name, String position, String email, String mobile) {
        OrgUser user = new OrgUser(id, name);
        user.setPosition(position);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setStatus(1);
        return user;
    }

    private OrgTreeDTO createNode(String id, String name, String type, String parentId, int order) {
        OrgTreeDTO node = new OrgTreeDTO();
        node.setId(id);
        node.setName(name);
        node.setType(type);
        node.setParentId(parentId);
        node.setOrder(order);
        node.setExpanded(false);
        node.setSelected(false);
        node.setHasChildren(false);
        node.setIcon("department".equals(type) ? "folder" : "user");
        return node;
    }

    private OrgTreeDTO createUserNode(String id, String name, String parentId, String position, String email, String mobile) {
        OrgTreeDTO node = createNode(id, name, "user", parentId, 0);
        OrgTreeDTO.OrgNodeData data = new OrgTreeDTO.OrgNodeData();
        data.setPosition(position);
        data.setEmail(email);
        data.setMobile(mobile);
        data.setStatus(1);
        node.setData(data);
        return node;
    }

    /**
     * Organization statistics
     */
    public static class OrgStats {
        private int totalDepartments;
        private int totalUsers;
        private int totalGroups;

        public int getTotalDepartments() { return totalDepartments; }
        public void setTotalDepartments(int totalDepartments) { this.totalDepartments = totalDepartments; }
        public int getTotalUsers() { return totalUsers; }
        public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
        public int getTotalGroups() { return totalGroups; }
        public void setTotalGroups(int totalGroups) { this.totalGroups = totalGroups; }
    }
}
