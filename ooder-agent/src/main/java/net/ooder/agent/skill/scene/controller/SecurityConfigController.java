package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/security")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class SecurityConfigController extends BaseController {

    private Map<String, Object> securityConfig = new HashMap<>();
    private List<Map<String, Object>> securityPolicies = new ArrayList<>();

    public SecurityConfigController() {
        securityConfig.put("enableAuth", true);
        securityConfig.put("enableEncryption", true);
        securityConfig.put("enableAudit", true);
        securityConfig.put("sessionTimeout", 30);
        securityConfig.put("maxLoginAttempts", 5);
        securityConfig.put("keyRotationDays", 90);
        securityConfig.put("enableFirewall", false);
        securityConfig.put("firewallMode", "active");
        securityConfig.put("enableAgentAuth", true);
        securityConfig.put("enableAgentEncryption", true);
        securityConfig.put("enableAgentIsolation", false);
        securityConfig.put("llmRateLimit", 60);
        securityConfig.put("costAlertThreshold", 100);

        Map<String, Object> policy1 = new HashMap<>();
        policy1.put("policyId", "p1");
        policy1.put("name", "Default Access Control");
        policy1.put("type", "ACCESS_CONTROL");
        policy1.put("enabled", true);
        policy1.put("rules", Arrays.asList("User Authentication", "Permission Check"));
        policy1.put("createTime", System.currentTimeMillis());
        securityPolicies.add(policy1);

        Map<String, Object> policy2 = new HashMap<>();
        policy2.put("policyId", "p2");
        policy2.put("name", "Data Encryption Policy");
        policy2.put("type", "DATA_PROTECTION");
        policy2.put("enabled", true);
        policy2.put("rules", Arrays.asList("Transfer Encryption", "Storage Encryption"));
        policy2.put("createTime", System.currentTimeMillis());
        securityPolicies.add(policy2);
    }

    @GetMapping("/config")
    public ResultModel<Map<String, Object>> getConfig() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getConfig", "security");

        try {
            logRequestEnd("getConfig", securityConfig, System.currentTimeMillis() - startTime);
            return ResultModel.success(securityConfig);
        } catch (Exception e) {
            logRequestError("getConfig", e);
            return ResultModel.error(500, "Failed to get security config: " + e.getMessage());
        }
    }

    @PutMapping("/config")
    public ResultModel<Boolean> updateConfig(@RequestBody Map<String, Object> config) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateConfig", config);

        try {
            securityConfig.putAll(config);
            logRequestEnd("updateConfig", true, System.currentTimeMillis() - startTime);
            return ResultModel.success(true);
        } catch (Exception e) {
            logRequestError("updateConfig", e);
            return ResultModel.error(500, "Failed to update security config: " + e.getMessage());
        }
    }

    @GetMapping("/policies")
    public ResultModel<List<Map<String, Object>>> getPolicies() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getPolicies", "security");

        try {
            logRequestEnd("getPolicies", securityPolicies, System.currentTimeMillis() - startTime);
            return ResultModel.success(securityPolicies);
        } catch (Exception e) {
            logRequestError("getPolicies", e);
            return ResultModel.error(500, "Failed to get security policies: " + e.getMessage());
        }
    }

    @GetMapping("/policies/{policyId}")
    public ResultModel<Map<String, Object>> getPolicy(@PathVariable String policyId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getPolicy", policyId);

        try {
            Map<String, Object> policy = securityPolicies.stream()
                .filter(p -> policyId.equals(p.get("policyId")))
                .findFirst()
                .orElse(null);

            if (policy != null) {
                logRequestEnd("getPolicy", policy, System.currentTimeMillis() - startTime);
                return ResultModel.success(policy);
            } else {
                logRequestEnd("getPolicy", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("Policy not found");
            }
        } catch (Exception e) {
            logRequestError("getPolicy", e);
            return ResultModel.error(500, "Failed to get security policy: " + e.getMessage());
        }
    }

    @PostMapping("/policies")
    public ResultModel<Map<String, Object>> createPolicy(@RequestBody Map<String, Object> policy) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createPolicy", policy);

        try {
                policy.put("policyId", "p" + (securityPolicies.size() + 1));
                policy.put("createTime", System.currentTimeMillis());
                securityPolicies.add(policy);
                logRequestEnd("createPolicy", policy, System.currentTimeMillis() - startTime);
                return ResultModel.success(policy);
        } catch (Exception e) {
            logRequestError("createPolicy", e);
                return ResultModel.error(500, "Failed to create security policy: " + e.getMessage());
        }
    }

    @PutMapping("/policies/{policyId}")
    public ResultModel<Boolean> updatePolicy(@PathVariable String policyId, @RequestBody Map<String, Object> updates) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updatePolicy", policyId);

        try {
            for (int i = 0; i < securityPolicies.size(); i++) {
                if (policyId.equals(securityPolicies.get(i).get("policyId"))) {
                    Map<String, Object> policy = securityPolicies.get(i);
                    policy.putAll(updates);
                    logRequestEnd("updatePolicy", true, System.currentTimeMillis() - startTime);
                    return ResultModel.success(true);
                }
            }
            logRequestEnd("updatePolicy", "Not found", System.currentTimeMillis() - startTime);
            return ResultModel.notFound("Policy not found");
        } catch (Exception e) {
            logRequestError("updatePolicy", e);
                return ResultModel.error(500, "Failed to update security policy: " + e.getMessage());
        }
    }

    @DeleteMapping("/policies/{policyId}")
    public ResultModel<Boolean> deletePolicy(@PathVariable String policyId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deletePolicy", policyId);

        try {
            boolean removed = securityPolicies.removeIf(p -> policyId.equals(p.get("policyId")));
            logRequestEnd("deletePolicy", removed, System.currentTimeMillis() - startTime);
            return ResultModel.success(removed);
        } catch (Exception e) {
            logRequestError("deletePolicy", e);
                return ResultModel.error(500, "Failed to delete security policy: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResultModel<Map<String, Object>> getStats() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getStats", "security");

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("threatCount", 0);
            stats.put("policyCount", securityPolicies.size());
            stats.put("lastAuditTime", System.currentTimeMillis());
            stats.put("blockedAttempts", 0);
            stats.put("activeSessions", 1);
            
            logRequestEnd("getStats", stats, System.currentTimeMillis() - startTime);
            return ResultModel.success(stats);
        } catch (Exception e) {
            logRequestError("getStats", e);
                return ResultModel.error(500, "Failed to get security stats: " + e.getMessage());
        }
    }
}
