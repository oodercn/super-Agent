package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.OrgUserDTO;
import net.ooder.mvp.skill.scene.dto.OrgDepartmentDTO;
import net.ooder.mvp.skill.scene.adapter.OrgWebAdapter;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/org")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrgController {

    private static final Logger log = LoggerFactory.getLogger(OrgController.class);

    @Autowired
    private OrgWebAdapter orgWebAdapter;

    @GetMapping("/users/current")
    public ResultModel<OrgUserDTO> getCurrentUser() {
        OrgUserDTO user = orgWebAdapter.getCurrentUser();
        if (user != null) {
            return ResultModel.success(user);
        }
        return ResultModel.success(createDefaultUser());
    }

    private OrgUserDTO createDefaultUser() {
        OrgUserDTO user = new OrgUserDTO();
        user.setUserId("default");
        user.setName("默认用户");
        user.setRole("user");
        user.setActive(true);
        return user;
    }

    @GetMapping("/users")
    public ResultModel<List<OrgUserDTO>> listUsers(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String role) {
        
        log.info("[listUsers] request start, departmentId: {}, role: {}", departmentId, role);
        
        List<OrgUserDTO> users;
        if (departmentId != null && !departmentId.isEmpty()) {
            users = orgWebAdapter.getUsersByDepartment(departmentId);
        } else if (role != null && !role.isEmpty()) {
            users = orgWebAdapter.getUsersByRole(role);
        } else {
            users = orgWebAdapter.getAllUsers();
        }
        
        return ResultModel.success(users);
    }

    @GetMapping("/users/{userId}")
    public ResultModel<OrgUserDTO> getUser(@PathVariable String userId) {
        OrgUserDTO user = orgWebAdapter.getUser(userId);
        if (user != null) {
            return ResultModel.success(user);
        }
        return ResultModel.error(404, "User not found: " + userId);
    }

    @PostMapping("/users")
    public ResultModel<OrgUserDTO> createUser(@RequestBody OrgUserDTO user) {
        log.info("[createUser] request start, userId: {}", user.getUserId());
        
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId("user-" + System.currentTimeMillis());
        }
        if (user.getCreateTime() == 0) {
            user.setCreateTime(System.currentTimeMillis());
        }
        user.setActive(true);
        
        orgWebAdapter.addUser(user);
        return ResultModel.success(user);
    }

    @PutMapping("/users/{userId}")
    public ResultModel<OrgUserDTO> updateUser(@PathVariable String userId, @RequestBody OrgUserDTO user) {
        log.info("[updateUser] request start, userId: {}", userId);
        
        OrgUserDTO existing = orgWebAdapter.getUser(userId);
        if (existing == null) {
            return ResultModel.error(404, "User not found: " + userId);
        }
        
        user.setUserId(userId);
        user.setCreateTime(existing.getCreateTime());
        user.setActive(existing.isActive());
        
        orgWebAdapter.updateUser(user);
        return ResultModel.success(user);
    }

    @DeleteMapping("/users/{userId}")
    public ResultModel<Boolean> deleteUser(@PathVariable String userId) {
        log.info("[deleteUser] request start, userId: {}", userId);
        
        boolean result = orgWebAdapter.deleteUser(userId);
        if (result) {
            return ResultModel.success(true);
        }
        return ResultModel.error(404, "User not found: " + userId);
    }

    @GetMapping("/departments")
    public ResultModel<List<OrgDepartmentDTO>> listDepartments() {
        log.info("[listDepartments] request start");
        List<OrgDepartmentDTO> departments = orgWebAdapter.getAllDepartments();
        return ResultModel.success(departments);
    }

    @GetMapping("/departments/{departmentId}")
    public ResultModel<OrgDepartmentDTO> getDepartment(@PathVariable String departmentId) {
        OrgDepartmentDTO department = orgWebAdapter.getDepartment(departmentId);
        if (department != null) {
            return ResultModel.success(department);
        }
        return ResultModel.error(404, "Department not found: " + departmentId);
    }

    @PostMapping("/departments")
    public ResultModel<OrgDepartmentDTO> createDepartment(@RequestBody OrgDepartmentDTO department) {
        log.info("[createDepartment] request start, departmentId: {}", department.getDepartmentId());
        
        if (department.getDepartmentId() == null || department.getDepartmentId().isEmpty()) {
            department.setDepartmentId("dept-" + System.currentTimeMillis());
        }
        if (department.getCreateTime() == 0) {
            department.setCreateTime(System.currentTimeMillis());
        }
        
        orgWebAdapter.addDepartment(department);
        return ResultModel.success(department);
    }

    @PutMapping("/departments/{departmentId}")
    public ResultModel<OrgDepartmentDTO> updateDepartment(@PathVariable String departmentId, @RequestBody OrgDepartmentDTO department) {
        log.info("[updateDepartment] request start, departmentId: {}", departmentId);
        
        OrgDepartmentDTO existing = orgWebAdapter.getDepartment(departmentId);
        if (existing == null) {
            return ResultModel.error(404, "Department not found: " + departmentId);
        }
        
        department.setDepartmentId(departmentId);
        department.setCreateTime(existing.getCreateTime());
        
        orgWebAdapter.updateDepartment(department);
        return ResultModel.success(department);
    }

    @DeleteMapping("/departments/{departmentId}")
    public ResultModel<Boolean> deleteDepartment(@PathVariable String departmentId) {
        log.info("[deleteDepartment] request start, departmentId: {}", departmentId);
        
        boolean result = orgWebAdapter.deleteDepartment(departmentId);
        if (result) {
            return ResultModel.success(true);
        }
        return ResultModel.error(404, "Department not found: " + departmentId);
    }

    @GetMapping("/departments/{departmentId}/members")
    public ResultModel<List<OrgUserDTO>> getDepartmentMembers(@PathVariable String departmentId) {
        List<OrgUserDTO> members = orgWebAdapter.getDepartmentMembers(departmentId);
        return ResultModel.success(members);
    }

    @GetMapping("/departments/{departmentId}/manager")
    public ResultModel<OrgUserDTO> getDepartmentManager(@PathVariable String departmentId) {
        OrgUserDTO manager = orgWebAdapter.getDepartmentManager(departmentId);
        if (manager != null) {
            return ResultModel.success(manager);
        }
        return ResultModel.error(404, "Manager not found for department: " + departmentId);
    }

    @PostMapping("/departments/{departmentId}/members/{userId}")
    public ResultModel<Boolean> addMemberToDepartment(@PathVariable String departmentId, @PathVariable String userId) {
        log.info("[addMemberToDepartment] departmentId: {}, userId: {}", departmentId, userId);
        boolean result = orgWebAdapter.addMemberToDepartment(departmentId, userId);
        return ResultModel.success(result);
    }

    @DeleteMapping("/departments/{departmentId}/members/{userId}")
    public ResultModel<Boolean> removeMemberFromDepartment(@PathVariable String departmentId, @PathVariable String userId) {
        log.info("[removeMemberFromDepartment] departmentId: {}, userId: {}", departmentId, userId);
        boolean result = orgWebAdapter.removeMemberFromDepartment(departmentId, userId);
        return ResultModel.success(result);
    }

    @GetMapping("/tree")
    public ResultModel<List<Map<String, Object>>> getOrgTree() {
        List<Map<String, Object>> tree = new ArrayList<>();
        
        List<OrgDepartmentDTO> departments = orgWebAdapter.getAllDepartments();
        
        for (OrgDepartmentDTO dept : departments) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", dept.getDepartmentId());
            node.put("name", dept.getName());
            node.put("type", "department");
            node.put("parentId", dept.getParentId());
            node.put("managerId", dept.getManagerId());
            
            List<Map<String, Object>> children = new ArrayList<>();
            List<OrgUserDTO> members = orgWebAdapter.getDepartmentMembers(dept.getDepartmentId());
            for (OrgUserDTO user : members) {
                Map<String, Object> userNode = new HashMap<>();
                userNode.put("id", user.getUserId());
                userNode.put("name", user.getName());
                userNode.put("type", "user");
                userNode.put("role", user.getRole());
                userNode.put("title", user.getTitle());
                userNode.put("email", user.getEmail());
                userNode.put("parentId", dept.getDepartmentId());
                children.add(userNode);
            }
            node.put("children", children);
            
            tree.add(node);
        }
        
        return ResultModel.success(tree);
    }

    @GetMapping("/roles")
    public ResultModel<List<Map<String, Object>>> listRoles() {
        List<Map<String, Object>> roles = new ArrayList<>();
        
        String[][] roleData = {
            {"manager", "管理者", "场景管理者，拥有完整管理权限"},
            {"employee", "员工", "普通员工，参与场景执行"},
            {"hr", "HR", "人力资源，管理人事相关"},
            {"admin", "管理员", "系统运维、能力管理、用户管理"},
            {"user", "普通用户", "场景参与、任务执行、业务流转"},
            {"developer", "开发者", "能力开发、测试、发布"},
            {"llm-assistant", "LLM助手", "AI分析助手"},
            {"coordinator", "协调Agent", "任务协调Agent"}
        };
        
        for (String[] data : roleData) {
            Map<String, Object> role = new HashMap<>();
            role.put("id", data[0]);
            role.put("name", data[1]);
            role.put("description", data[2]);
            roles.add(role);
        }
        
        return ResultModel.success(roles);
    }

    @PutMapping("/users/{userId}/role")
    public ResultModel<OrgUserDTO> updateUserRole(@PathVariable String userId, @RequestParam String role) {
        log.info("[updateUserRole] userId: {}, role: {}", userId, role);
        
        OrgUserDTO user = orgWebAdapter.getUser(userId);
        if (user == null) {
            return ResultModel.error(404, "User not found: " + userId);
        }
        
        if (role == null || role.isEmpty()) {
            return ResultModel.error(400, "Role is required");
        }
        
        user.setRole(role);
        orgWebAdapter.updateUser(user);
        
        return ResultModel.success(user);
    }
    
    @GetMapping("/users/current/stats")
    public ResultModel<Map<String, Object>> getCurrentUserStats() {
        Map<String, Object> stats = new HashMap<>();
        
        OrgUserDTO currentUser = orgWebAdapter.getCurrentUser();
        if (currentUser == null) {
            stats.put("scenesCount", 0);
            stats.put("capabilitiesCount", 0);
            return ResultModel.success(stats);
        }
        
        int scenesCount = orgWebAdapter.getUserSceneCount(currentUser.getUserId());
        int capabilitiesCount = orgWebAdapter.getUserCapabilityCount(currentUser.getUserId());
        
        stats.put("scenesCount", scenesCount);
        stats.put("capabilitiesCount", capabilitiesCount);
        
        return ResultModel.success(stats);
    }
}
