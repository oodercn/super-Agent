package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.LoginRequest;
import net.ooder.mvp.skill.scene.dto.UserSessionDTO;
import net.ooder.mvp.skill.scene.dto.OrgUserDTO;
import net.ooder.mvp.skill.scene.adapter.OrgWebAdapter;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api/v1/mvp-auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private OrgWebAdapter orgWebAdapter;

    @PostMapping("/login")
    public ResultModel<UserSessionDTO> login(@RequestBody LoginRequest request, HttpSession session) {
        log.info("[login] Login attempt: username={}, role={}", request.getUsername(), request.getRole());
        
        String role = request.getRole();
        if (role == null || role.isEmpty()) {
            role = "collaborator";
        }
        
        final String finalRole = role;
        
        List<OrgUserDTO> users = orgWebAdapter.getUsersByRole(finalRole);
        OrgUserDTO matchedUser = null;
        
        if (users != null && !users.isEmpty()) {
            matchedUser = users.get(0);
        }
        
        if (matchedUser == null) {
            matchedUser = orgWebAdapter.getAllUsers().stream()
                .filter(u -> finalRole.equals(u.getRole()))
                .findFirst()
                .orElse(null);
        }
        
        if (matchedUser == null) {
            matchedUser = new OrgUserDTO();
            matchedUser.setUserId("user-" + finalRole + "-001");
            matchedUser.setName(getRoleDisplayName(finalRole));
            matchedUser.setEmail(finalRole + "@ooder.local");
            matchedUser.setRole(finalRole);
            matchedUser.setActive(true);
        }
        
        UserSessionDTO sessionDTO = new UserSessionDTO();
        sessionDTO.setUserId(matchedUser.getUserId());
        sessionDTO.setUsername(request.getUsername());
        sessionDTO.setName(matchedUser.getName());
        sessionDTO.setEmail(matchedUser.getEmail());
        sessionDTO.setRole(role);
        sessionDTO.setRoleType(role);
        sessionDTO.setDepartmentId(matchedUser.getDepartmentId());
        sessionDTO.setDepartmentName(matchedUser.getDepartmentName());
        sessionDTO.setTitle(matchedUser.getTitle());
        sessionDTO.setLoginTime(System.currentTimeMillis());
        sessionDTO.setToken(UUID.randomUUID().toString());
        
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
            log.info("[getSession] No active session, creating default admin session");
            
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
            
            session.setAttribute("user", user);
        }
        
        log.info("[getSession] Session user: userId={}, name={}, role={}", 
            user.getUserId(), user.getName(), user.getRoleType());
        
        return ResultModel.success(user);
    }
    
    private String getRoleDisplayName(String role) {
        Map<String, String> roleNames = new HashMap<>();
        roleNames.put("admin", "系统管理员");
        roleNames.put("installer", "系统安装者");
        roleNames.put("leader", "主导者");
        roleNames.put("collaborator", "协作者");
        roleNames.put("manager", "管理者");
        roleNames.put("employee", "员工");
        roleNames.put("hr", "HR");
        roleNames.put("developer", "开发者");
        roleNames.put("user", "普通用户");
        
        return roleNames.getOrDefault(role, role);
    }
}
