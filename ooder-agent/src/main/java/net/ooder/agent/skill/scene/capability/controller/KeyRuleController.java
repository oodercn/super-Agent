package net.ooder.mvp.skill.scene.capability.controller;

import net.ooder.mvp.skill.scene.dto.key.KeyRuleDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/key-rules")
@CrossOrigin(origins = "*")
public class KeyRuleController {

    private static final Logger log = LoggerFactory.getLogger(KeyRuleController.class);

    @GetMapping
    public ResultModel<List<KeyRuleDTO>> getAllRules() {
        log.info("[getAllRules]");
        
        List<KeyRuleDTO> rules = new ArrayList<>();
        
        rules.add(createMockRule("rule-default", "默认规则", "适用于普通用户的默认密钥规则", 30, 1000));
        rules.add(createMockRule("rule-admin", "管理员规则", "适用于管理员的密钥规则", 90, -1));
        rules.add(createMockRule("rule-device", "设备规则", "适用于设备的密钥规则", 365, -1));
        rules.add(createMockRule("rule-temp", "临时规则", "适用于临时访问的密钥规则", 7, 100));
        
        return ResultModel.success(rules);
    }

    @GetMapping("/{ruleId}")
    public ResultModel<KeyRuleDTO> getRule(@PathVariable String ruleId) {
        log.info("[getRule] ruleId={}", ruleId);
        
        KeyRuleDTO rule = createMockRule(ruleId, "自定义规则", "自定义密钥规则", 30, 500);
        return ResultModel.success(rule);
    }

    @PostMapping
    public ResultModel<KeyRuleDTO> createRule(@RequestBody KeyRuleDTO request) {
        log.info("[createRule] request={}", request);
        
        KeyRuleDTO result = new KeyRuleDTO();
        result.setRuleId("rule-" + System.currentTimeMillis());
        result.setCreatedAt(System.currentTimeMillis());
        result.setRuleName(request.getRuleName());
        result.setDescription(request.getDescription());
        result.setValidityDays(request.getValidityDays());
        result.setMaxUseCount(request.getMaxUseCount());
        result.setAllowedScenes(request.getAllowedScenes());
        result.setAllowedOperations(request.getAllowedOperations());
        result.setAllowedRoles(request.getAllowedRoles());
        result.setRequireApproval(request.getRequireApproval());
        result.setEnableAudit(request.getEnableAudit());
        result.setEnableAlert(request.getEnableAlert());
        
        return ResultModel.success(result);
    }

    @PutMapping("/{ruleId}")
    public ResultModel<KeyRuleDTO> updateRule(
            @PathVariable String ruleId,
            @RequestBody KeyRuleDTO request) {
        
        log.info("[updateRule] ruleId={}, request={}", ruleId, request);
        
        KeyRuleDTO result = new KeyRuleDTO();
        result.setRuleId(ruleId);
        result.setUpdatedAt(System.currentTimeMillis());
        result.setRuleName(request.getRuleName());
        result.setDescription(request.getDescription());
        result.setValidityDays(request.getValidityDays());
        result.setMaxUseCount(request.getMaxUseCount());
        result.setAllowedScenes(request.getAllowedScenes());
        result.setAllowedOperations(request.getAllowedOperations());
        result.setAllowedRoles(request.getAllowedRoles());
        result.setRequireApproval(request.getRequireApproval());
        result.setEnableAudit(request.getEnableAudit());
        result.setEnableAlert(request.getEnableAlert());
        
        return ResultModel.success(result);
    }

    @DeleteMapping("/{ruleId}")
    public ResultModel<Boolean> deleteRule(@PathVariable String ruleId) {
        log.info("[deleteRule] ruleId={}", ruleId);
        return ResultModel.success(true);
    }

    @GetMapping("/recommended")
    public ResultModel<KeyRuleDTO> getRecommendedRule(
            @RequestParam(required = false) String requestType,
            @RequestParam(required = false) String sceneGroupId) {
        
        log.info("[getRecommendedRule] requestType={}, sceneGroupId={}", requestType, sceneGroupId);
        
        KeyRuleDTO rule = createMockRule("rule-default", "推荐规则", "根据申请类型自动推荐的规则", 30, 1000);
        
        if ("DEVICE_JOIN".equals(requestType)) {
            rule.setValidityDays(365);
            rule.setMaxUseCount(-1);
        } else if ("AGENT_JOIN".equals(requestType)) {
            rule.setValidityDays(90);
            rule.setMaxUseCount(-1);
        }
        
        return ResultModel.success(rule);
    }

    @PostMapping("/{ruleId}/apply/{keyId}")
    public ResultModel<RuleApplyResultDTO> applyRuleToKey(
            @PathVariable String ruleId,
            @PathVariable String keyId) {
        
        log.info("[applyRuleToKey] ruleId={}, keyId={}", ruleId, keyId);
        
        RuleApplyResultDTO result = new RuleApplyResultDTO();
        result.setKeyId(keyId);
        result.setRuleId(ruleId);
        result.setAppliedAt(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    private KeyRuleDTO createMockRule(String ruleId, String ruleName, String description,
            int validityDays, int maxUseCount) {
        
        KeyRuleDTO rule = new KeyRuleDTO();
        rule.setRuleId(ruleId);
        rule.setRuleName(ruleName);
        rule.setDescription(description);
        rule.setValidityDays(validityDays);
        rule.setAutoExpire(true);
        rule.setMaxUseCount(maxUseCount);
        rule.setDailyUseLimit(-1);
        
        List<String> allowedScenes = new ArrayList<>();
        allowedScenes.add("daily-log-scene");
        allowedScenes.add("report-scene");
        rule.setAllowedScenes(allowedScenes);
        
        List<String> allowedOperations = new ArrayList<>();
        allowedOperations.add("read");
        allowedOperations.add("write");
        rule.setAllowedOperations(allowedOperations);
        
        List<String> allowedRoles = new ArrayList<>();
        allowedRoles.add("employee");
        allowedRoles.add("manager");
        rule.setAllowedRoles(allowedRoles);
        
        rule.setRequireApproval(true);
        rule.setEnableAudit(true);
        rule.setEnableAlert(maxUseCount > 0);
        rule.setCreatedAt(System.currentTimeMillis() - 86400000);
        rule.setUpdatedAt(System.currentTimeMillis());
        
        return rule;
    }
    
    public static class RuleApplyResultDTO {
        private String keyId;
        private String ruleId;
        private Long appliedAt;
        
        public String getKeyId() { return keyId; }
        public void setKeyId(String keyId) { this.keyId = keyId; }
        public String getRuleId() { return ruleId; }
        public void setRuleId(String ruleId) { this.ruleId = ruleId; }
        public Long getAppliedAt() { return appliedAt; }
        public void setAppliedAt(Long appliedAt) { this.appliedAt = appliedAt; }
    }
}
