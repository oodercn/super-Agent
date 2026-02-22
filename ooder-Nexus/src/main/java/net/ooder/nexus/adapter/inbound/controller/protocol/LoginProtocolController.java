package net.ooder.nexus.adapter.inbound.controller.protocol;

import net.ooder.config.ResultModel;
import net.ooder.sdk.southbound.protocol.LoginProtocol;
import net.ooder.sdk.southbound.protocol.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/protocol/login")
public class LoginProtocolController {

    private static final Logger log = LoggerFactory.getLogger(LoginProtocolController.class);

    private final LoginProtocol loginProtocol;

    @Autowired
    public LoginProtocolController(@Autowired(required = false) LoginProtocol loginProtocol) {
        this.loginProtocol = loginProtocol;
        log.info("LoginProtocolController initialized: {}", 
            loginProtocol != null ? "SDK protocol available" : "using mock");
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultModel<LoginResult> login(@RequestBody Map<String, Object> params) {
        log.info("Login requested: {}", params);
        ResultModel<LoginResult> result = new ResultModel<>();

        try {
            if (loginProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                LoginRequest request = new LoginRequest();
                request.setUsername((String) params.get("username"));
                request.setPassword((String) params.get("password"));
                request.setDomain((String) params.get("domain"));
                
                CompletableFuture<LoginResult> future = loginProtocol.login(request);
                LoginResult loginResult = future.get();
                
                result.setData(loginResult);
                result.setRequestStatus(200);
                result.setMessage("Login successful");
            }
        } catch (Exception e) {
            log.error("Login failed", e);
            result.setRequestStatus(500);
            result.setMessage("Login failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResultModel<Void> logout(@RequestBody Map<String, Object> params) {
        log.info("Logout requested");
        ResultModel<Void> result = new ResultModel<>();

        try {
            String sessionId = (String) params.get("sessionId");
            
            if (loginProtocol != null) {
                loginProtocol.logout(sessionId);
            }
            result.setRequestStatus(200);
            result.setMessage("Logout successful");
        } catch (Exception e) {
            log.error("Logout failed", e);
            result.setRequestStatus(500);
            result.setMessage("Logout failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/session")
    @ResponseBody
    public ResultModel<SessionInfo> getSession(@RequestBody Map<String, Object> params) {
        log.info("Get session requested: {}", params);
        ResultModel<SessionInfo> result = new ResultModel<>();

        try {
            String sessionId = (String) params.get("sessionId");
            
            if (loginProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                CompletableFuture<SessionInfo> future = loginProtocol.getSession(sessionId);
                SessionInfo sessionInfo = future.get();
                result.setData(sessionInfo);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get session failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get session: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/session/validate")
    @ResponseBody
    public ResultModel<SessionInfo> validateSession(@RequestBody Map<String, Object> params) {
        log.info("Validate session requested: {}", params);
        ResultModel<SessionInfo> result = new ResultModel<>();

        try {
            String sessionId = (String) params.get("sessionId");
            
            if (loginProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                CompletableFuture<SessionInfo> future = loginProtocol.validateSession(sessionId);
                SessionInfo sessionInfo = future.get();
                result.setData(sessionInfo);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Validate session failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to validate session: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/session/refresh")
    @ResponseBody
    public ResultModel<Void> refreshSession(@RequestBody Map<String, Object> params) {
        log.info("Refresh session requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String sessionId = (String) params.get("sessionId");
            
            if (loginProtocol != null) {
                loginProtocol.refreshSession(sessionId);
            }
            result.setRequestStatus(200);
            result.setMessage("Session refreshed");
        } catch (Exception e) {
            log.error("Refresh session failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to refresh session: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/auto-login")
    @ResponseBody
    public ResultModel<LoginResult> autoLogin(@RequestBody(required = false) Map<String, Object> params) {
        log.info("Auto login requested");
        ResultModel<LoginResult> result = new ResultModel<>();

        try {
            if (loginProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                AutoLoginConfig config = new AutoLoginConfig();
                if (params != null) {
                    config.setDomain((String) params.get("domain"));
                }
                
                CompletableFuture<LoginResult> future = loginProtocol.autoLogin(config);
                LoginResult loginResult = future.get();
                
                result.setData(loginResult);
                result.setRequestStatus(200);
                result.setMessage("Auto login successful");
            }
        } catch (Exception e) {
            log.error("Auto login failed", e);
            result.setRequestStatus(500);
            result.setMessage("Auto login failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/domain-policy")
    @ResponseBody
    public ResultModel<DomainPolicy> getDomainPolicy(@RequestBody Map<String, Object> params) {
        log.info("Get domain policy requested: {}", params);
        ResultModel<DomainPolicy> result = new ResultModel<>();

        try {
            String userId = (String) params.get("userId");
            
            if (loginProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                CompletableFuture<DomainPolicy> future = loginProtocol.getDomainPolicy(userId);
                DomainPolicy policy = future.get();
                result.setData(policy);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get domain policy failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get domain policy: " + e.getMessage());
        }

        return result;
    }

    @GetMapping("/status")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatus() {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        Map<String, Object> status = new HashMap<>();
        status.put("sdkAvailable", loginProtocol != null);
        status.put("protocolType", "LoginProtocol");
        
        result.setData(status);
        result.setRequestStatus(200);
        return result;
    }
}
