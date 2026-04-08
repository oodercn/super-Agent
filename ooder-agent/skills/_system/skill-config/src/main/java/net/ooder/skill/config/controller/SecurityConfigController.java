package net.ooder.skill.config.controller;

import net.ooder.skill.common.model.ResultModel;
import net.ooder.skill.config.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/config/security")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class SecurityConfigController {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfigController.class);

    private SecurityConfigDTO securityConfig = new SecurityConfigDTO();
    private final Map<String, SecurityPolicyDTO> policies = new HashMap<>();

    @GetMapping
    public ResultModel<SecurityConfigDTO> getSecurityConfig() {
        log.info("[SecurityConfigController] Get security config");
        return ResultModel.success(securityConfig);
    }

    @PostMapping
    public ResultModel<SecurityConfigDTO> saveSecurityConfig(@RequestBody SecurityConfigDTO config) {
        log.info("[SecurityConfigController] Save security config");
        this.securityConfig = config;
        return ResultModel.success(config);
    }

    @GetMapping("/stats")
    public ResultModel<SecurityStatsDTO> getSecurityStats() {
        log.info("[SecurityConfigController] Get security stats");
        
        SecurityStatsDTO stats = new SecurityStatsDTO();
        stats.setThreatCount(0);
        stats.setPolicyCount(policies.size());
        stats.setLastAuditTime(System.currentTimeMillis());
        stats.setBlockedAttempts(0);
        stats.setActiveSessions(0);
        
        return ResultModel.success(stats);
    }

    @GetMapping("/policies")
    public ResultModel<List<SecurityPolicyDTO>> listPolicies() {
        log.info("[SecurityConfigController] List security policies");
        return ResultModel.success(new ArrayList<>(policies.values()));
    }

    @GetMapping("/policies/{policyId}")
    public ResultModel<SecurityPolicyDTO> getPolicy(@PathVariable String policyId) {
        log.info("[SecurityConfigController] Get security policy: {}", policyId);
        SecurityPolicyDTO policy = policies.get(policyId);
        if (policy == null) {
            return ResultModel.notFound("Policy not found: " + policyId);
        }
        return ResultModel.success(policy);
    }

    @PostMapping("/policies")
    public ResultModel<SecurityPolicyDTO> createPolicy(@RequestBody CreatePolicyRequest request) {
        log.info("[SecurityConfigController] Create security policy: {}", request.getName());
        
        String policyId = UUID.randomUUID().toString();
        
        SecurityPolicyDTO policy = new SecurityPolicyDTO();
        policy.setPolicyId(policyId);
        policy.setName(request.getName());
        policy.setType(request.getType());
        policy.setEnabled(request.isEnabled());
        policy.setRules(request.getRules());
        policy.setCreateTime(System.currentTimeMillis());
        
        policies.put(policyId, policy);
        return ResultModel.success(policy);
    }

    @PutMapping("/policies/{policyId}")
    public ResultModel<SecurityPolicyDTO> updatePolicy(@PathVariable String policyId, @RequestBody CreatePolicyRequest request) {
        log.info("[SecurityConfigController] Update security policy: {}", policyId);
        
        SecurityPolicyDTO policy = policies.get(policyId);
        if (policy == null) {
            return ResultModel.notFound("Policy not found: " + policyId);
        }
        
        if (request.getName() != null) policy.setName(request.getName());
        if (request.getType() != null) policy.setType(request.getType());
        policy.setEnabled(request.isEnabled());
        if (request.getRules() != null) policy.setRules(request.getRules());
        
        return ResultModel.success(policy);
    }

    @DeleteMapping("/policies/{policyId}")
    public ResultModel<Boolean> deletePolicy(@PathVariable String policyId) {
        log.info("[SecurityConfigController] Delete security policy: {}", policyId);
        
        if (!policies.containsKey(policyId)) {
            return ResultModel.notFound("Policy not found: " + policyId);
        }
        
        policies.remove(policyId);
        return ResultModel.success(true);
    }

    @PostMapping("/policies/{policyId}/enable")
    public ResultModel<SecurityPolicyDTO> enablePolicy(@PathVariable String policyId) {
        log.info("[SecurityConfigController] Enable security policy: {}", policyId);
        
        SecurityPolicyDTO policy = policies.get(policyId);
        if (policy == null) {
            return ResultModel.notFound("Policy not found: " + policyId);
        }
        
        policy.setEnabled(true);
        return ResultModel.success(policy);
    }

    @PostMapping("/policies/{policyId}/disable")
    public ResultModel<SecurityPolicyDTO> disablePolicy(@PathVariable String policyId) {
        log.info("[SecurityConfigController] Disable security policy: {}", policyId);
        
        SecurityPolicyDTO policy = policies.get(policyId);
        if (policy == null) {
            return ResultModel.notFound("Policy not found: " + policyId);
        }
        
        policy.setEnabled(false);
        return ResultModel.success(policy);
    }

    @PostMapping("/reset")
    public ResultModel<SecurityConfigDTO> resetSecurityConfig() {
        log.info("[SecurityConfigController] Reset security config");
        this.securityConfig = new SecurityConfigDTO();
        return ResultModel.success(securityConfig);
    }
}
