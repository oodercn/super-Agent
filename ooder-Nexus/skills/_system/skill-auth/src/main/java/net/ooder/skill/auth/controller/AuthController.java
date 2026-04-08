package net.ooder.skill.auth.controller;

import net.ooder.skill.auth.dto.LoginDTO;
import net.ooder.skill.auth.dto.SessionDTO;
import net.ooder.skill.auth.dto.QrcodeDTO;
import net.ooder.skill.auth.dto.QrcodeStatusDTO;
import net.ooder.skill.auth.model.ResultModel;
import net.ooder.skill.auth.service.QrcodeLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    private Map<String, QrcodeStatusDTO> qrcodeCache = new ConcurrentHashMap<>();
    
    @Autowired(required = false)
    private Map<String, QrcodeLoginService> qrcodeLoginServices;

    @PostMapping("/login")
    public ResultModel<SessionDTO> login(@RequestBody LoginDTO loginDTO) {
        log.info("[AuthController] Login request - username: {}", loginDTO.getUsername());
        
        SessionDTO session = new SessionDTO();
        session.setToken("mock-token-" + System.currentTimeMillis());
        session.setUserId("user-001");
        session.setUsername(loginDTO.getUsername());
        session.setRole("admin");
        session.setExpireTime(java.time.LocalDateTime.now().plusHours(1));
        
        return ResultModel.success(session);
    }

    @GetMapping("/session")
    public ResultModel<SessionDTO> getSession() {
        log.info("[AuthController] Get session");
        
        SessionDTO session = new SessionDTO();
        session.setToken("mock-token");
        session.setUserId("user-001");
        session.setUsername("admin");
        session.setRole("admin");
        
        return ResultModel.success(session);
    }

    @PostMapping("/logout")
    public ResultModel<Boolean> logout() {
        log.info("[AuthController] Logout");
        return ResultModel.success(true);
    }
    
    @GetMapping("/qrcode/{platform}")
    public ResultModel<QrcodeDTO> getQrcode(@PathVariable String platform) {
        log.info("[AuthController] Get qrcode for platform: {}", platform);
        
        QrcodeLoginService service = getQrcodeLoginService(platform);
        if (service != null) {
            QrcodeDTO qrcode = service.getQrcode(platform);
            if (qrcode != null) {
                return ResultModel.success(qrcode);
            }
        }
        
        String qrcodeId = UUID.randomUUID().toString();
        
        QrcodeDTO qrcode = new QrcodeDTO();
        qrcode.setQrcodeId(qrcodeId);
        qrcode.setPlatform(platform);
        qrcode.setExpiresIn(300);
        
        String mockQrcodeUrl = "https://via.placeholder.com/240x240?text=" + platform.toUpperCase() + "+Login";
        qrcode.setQrcodeUrl(mockQrcodeUrl);
        
        QrcodeStatusDTO status = new QrcodeStatusDTO();
        status.setQrcodeId(qrcodeId);
        status.setStatus("waiting");
        status.setPlatform(platform);
        qrcodeCache.put(qrcodeId, status);
        
        return ResultModel.success(qrcode);
    }
    
    @GetMapping("/qrcode/check/{qrcodeId}")
    public ResultModel<QrcodeStatusDTO> checkQrcode(@PathVariable String qrcodeId) {
        log.info("[AuthController] Check qrcode status: {}", qrcodeId);
        
        QrcodeStatusDTO status = qrcodeCache.get(qrcodeId);
        if (status == null) {
            status = new QrcodeStatusDTO();
            status.setQrcodeId(qrcodeId);
            status.setStatus("expired");
            return ResultModel.success(status);
        }
        
        if ("waiting".equals(status.getStatus())) {
            long random = System.currentTimeMillis() % 10;
            if (random < 3) {
                status.setStatus("scanned");
            } else if (random < 5) {
                status.setStatus("confirmed");
                status.setToken("mock-token-" + System.currentTimeMillis());
                status.setUserId("user-qrcode-001");
                status.setUsername("qrcode_user");
                status.setRole("admin");
                
                SessionDTO user = new SessionDTO();
                user.setToken(status.getToken());
                user.setUserId(status.getUserId());
                user.setUsername(status.getUsername());
                user.setRole(status.getRole());
                status.setUser(user);
                
                qrcodeCache.remove(qrcodeId);
            }
        }
        
        return ResultModel.success(status);
    }
    
    @GetMapping("/callback/{platform}")
    public ResultModel<QrcodeStatusDTO> handleCallback(
            @PathVariable String platform,
            @RequestParam String code,
            @RequestParam String state) {
        log.info("[AuthController] Handle callback for platform: {}, code: {}, state: {}", platform, code, state);
        
        QrcodeLoginService service = getQrcodeLoginService(platform);
        if (service != null) {
            QrcodeStatusDTO status = service.handleCallback(platform, code);
            if (status != null) {
                return ResultModel.success(status);
            }
        }
        
        QrcodeStatusDTO status = new QrcodeStatusDTO();
        status.setStatus("failed");
        status.setQrcodeId(state);
        
        return ResultModel.error("Login failed");
    }
    
    private QrcodeLoginService getQrcodeLoginService(String platform) {
        if (qrcodeLoginServices == null) {
            return null;
        }
        
        String serviceName = platform + "LoginServiceImpl";
        return qrcodeLoginServices.values().stream()
            .filter(s -> s.getClass().getSimpleName().toLowerCase().startsWith(platform.toLowerCase()))
            .findFirst()
            .orElse(null);
    }
}
