package net.ooder.skill.org;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OrgWebServiceImpl implements OrgService {

    private static final Logger log = LoggerFactory.getLogger(OrgWebServiceImpl.class);

    private final Map<String, OrgUserDTO> users = new ConcurrentHashMap<>();

    public OrgWebServiceImpl() {
        initDefaultUsers();
        log.info("[OrgWebService] Initialized with {} users", users.size());
    }

    private void initDefaultUsers() {
        long now = System.currentTimeMillis();
        
        createUser("user-manager-001", "张经理", "zhang.manager@example.com", 
            "dept-rd", "manager", "研发经理", now);
        createUser("user-employee-001", "李员工", "li.employee@example.com", 
            "dept-rd", "employee", "高级工程师", now);
        createUser("user-hr-001", "刘HR", "liu.hr@example.com", 
            "dept-hr", "hr", "HR经理", now);
        createUser("user-admin-001", "系统管理员", "admin@example.com", 
            "dept-it", "admin", "系统管理员", now);
        createUser("user-installer-001", "安装者", "installer@example.com", 
            "dept-it", "installer", "系统安装者", now);
        createUser("user-leader-001", "主导者", "leader@example.com", 
            "dept-rd", "leader", "项目主导", now);
        createUser("user-collaborator-001", "协作者", "collaborator@example.com", 
            "dept-rd", "collaborator", "项目协作", now);
    }

    private void createUser(String userId, String name, String email, 
                           String deptId, String role, String title, long now) {
        OrgUserDTO user = new OrgUserDTO();
        user.setUserId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setDepartmentId(deptId);
        user.setRole(role);
        user.setTitle(title);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setActive(true);
        users.put(userId, user);
    }

    @Override
    public String getSkillId() {
        return "skill-org-web";
    }

    @Override
    public OrgUserDTO getUser(String userId) {
        return users.get(userId);
    }

    @Override
    public OrgUserDTO getCurrentUser() {
        if (!users.isEmpty()) {
            OrgUserDTO admin = users.get("user-admin-001");
            if (admin != null) {
                return admin;
            }
            return users.values().iterator().next();
        }
        OrgUserDTO defaultUser = new OrgUserDTO();
        defaultUser.setUserId("default");
        defaultUser.setName("默认用户");
        defaultUser.setRole("user");
        defaultUser.setActive(true);
        return defaultUser;
    }

    @Override
    public List<OrgUserDTO> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<OrgUserDTO> getUsersByRole(String role) {
        List<OrgUserDTO> result = new ArrayList<>();
        for (OrgUserDTO user : users.values()) {
            if (role.equals(user.getRole())) {
                result.add(user);
            }
        }
        return result;
    }

    @Override
    public List<OrgUserDTO> getUsersByDepartment(String departmentId) {
        List<OrgUserDTO> result = new ArrayList<>();
        for (OrgUserDTO user : users.values()) {
            if (departmentId.equals(user.getDepartmentId())) {
                result.add(user);
            }
        }
        return result;
    }

    @Override
    public boolean isUserInRole(String userId, String role) {
        OrgUserDTO user = users.get(userId);
        return user != null && role.equals(user.getRole());
    }

    @Override
    public void addUser(OrgUserDTO user) {
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(System.currentTimeMillis());
        users.put(user.getUserId(), user);
        log.info("[OrgWebService] Added user: {}", user.getUserId());
    }

    @Override
    public void updateUser(OrgUserDTO user) {
        user.setUpdateTime(System.currentTimeMillis());
        users.put(user.getUserId(), user);
        log.info("[OrgWebService] Updated user: {}", user.getUserId());
    }

    @Override
    public boolean deleteUser(String userId) {
        OrgUserDTO removed = users.remove(userId);
        if (removed != null) {
            log.info("[OrgWebService] Deleted user: {}", userId);
            return true;
        }
        return false;
    }
}
