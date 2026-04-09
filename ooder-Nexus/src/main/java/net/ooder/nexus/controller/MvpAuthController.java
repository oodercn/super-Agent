package net.ooder.nexus.controller;

import net.ooder.nexus.dto.auth.LoginRequest;
import net.ooder.nexus.dto.auth.UserSessionDTO;
import net.ooder.nexus.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api/v1/mvp-auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class MvpAuthController {

    private static final Logger log = LoggerFactory.getLogger(MvpAuthController.class);

    private static final Map<String, String> ROLE_DISPLAY_NAMES = new HashMap<>();
    static {
        ROLE_DISPLAY_NAMES.put("admin", "系统管理员");
        ROLE_DISPLAY_NAMES.put("installer", "系统安装者");
        ROLE_DISPLAY_NAMES.put("leader", "主导者");
        ROLE_DISPLAY_NAMES.put("collaborator", "协作者");
        ROLE_DISPLAY_NAMES.put("manager", "管理者");
        ROLE_DISPLAY_NAMES.put("employee", "员工");
        ROLE_DISPLAY_NAMES.put("hr", "HR");
        ROLE_DISPLAY_NAMES.put("developer", "开发者");
        ROLE_DISPLAY_NAMES.put("user", "普通用户");
    }

    @PostMapping("/login")
    public ResultModel<UserSessionDTO> login(@RequestBody LoginRequest request, HttpSession session) {
        log.info("[login] Login attempt: username={}, role={}", request.getUsername(), request.getRole());
        
        String role = request.getRole();
        if (role == null || role.isEmpty()) {
            role = "collaborator";
        }
        
        UserSessionDTO sessionDTO = new UserSessionDTO();
        sessionDTO.setUserId("user-" + role + "-001");
        sessionDTO.setUsername(request.getUsername() != null ? request.getUsername() : role);
        sessionDTO.setName(ROLE_DISPLAY_NAMES.getOrDefault(role, role));
        sessionDTO.setEmail(role + "@ooder.local");
        sessionDTO.setRole(role);
        sessionDTO.setRoleType(role);
        sessionDTO.setDepartmentId("dept-default");
        sessionDTO.setDepartmentName("默认部门");
        sessionDTO.setTitle(ROLE_DISPLAY_NAMES.getOrDefault(role, role));
        sessionDTO.setLoginTime(System.currentTimeMillis());
        sessionDTO.setToken(UUID.randomUUID().toString());
        sessionDTO.setPermissions(getDefaultPermissions(role));
        
        session.setAttribute("user", sessionDTO);
        
        log.info("[login] Login successful: userId={}, name={}, role={}", 
            sessionDTO.getUserId(), sessionDTO.getName(), sessionDTO.getRoleType());
        
        return ResultModel.success(sessionDTO);
    }
    
    @PostMapping("/logout")
    public ResultModel<Void> logout(HttpSession session) {
        log.info("[logout] User logging out");
        session.invalidate();
        return ResultModel.success(null);
    }
    
    @GetMapping("/my-session")
    public ResultModel<UserSessionDTO> getMySession(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        
        if (user == null) {
            log.info("[my-session] No active session, returning default admin session");
            
            user = new UserSessionDTO();
            user.setUserId("user-admin-001");
            user.setUsername("admin");
            user.setName("系统管理员");
            user.setEmail("admin@ooder.local");
            user.setRole("admin");
            user.setRoleType("admin");
            user.setDepartmentId("dept-it");
            user.setDepartmentName("信息技术部");
            user.setTitle("系统管理员");
            user.setLoginTime(System.currentTimeMillis());
            user.setToken(UUID.randomUUID().toString());
            user.setPermissions(getDefaultPermissions("admin"));
            
            session.setAttribute("user", user);
        }
        
        log.info("[my-session] Session user: userId={}, name={}, role={}", 
            user.getUserId(), user.getName(), user.getRoleType());
        
        return ResultModel.success(user);
    }
    
    private List<String> getDefaultPermissions(String role) {
        List<String> permissions = new ArrayList<>();
        permissions.add("read");
        
        if ("admin".equals(role) || "installer".equals(role)) {
            permissions.add("write");
            permissions.add("delete");
            permissions.add("manage");
        } else if ("leader".equals(role) || "manager".equals(role)) {
            permissions.add("write");
            permissions.add("manage");
        } else {
            permissions.add("write");
        }
        
        return permissions;
    }
}
