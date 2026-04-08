package net.ooder.skill.discovery.service;

import net.ooder.skill.discovery.model.SkillDirectory;
import org.springframework.stereotype.Component;

@Component
public class SkillDirectoryDetector {
    
    public SkillDirectory detect(String skillId) {
        if (skillId == null) {
            return SkillDirectory.BUSINESS;
        }
        
        if (isSystemSkill(skillId)) {
            return SkillDirectory.SYSTEM;
        }
        
        if (isDriverSkill(skillId)) {
            return SkillDirectory.DRIVERS;
        }
        
        if (isSceneSkill(skillId)) {
            return SkillDirectory.SCENES;
        }
        
        if (isCapabilitySkill(skillId)) {
            return SkillDirectory.CAPABILITIES;
        }
        
        if (isToolSkill(skillId)) {
            return SkillDirectory.TOOLS;
        }
        
        return SkillDirectory.BUSINESS;
    }
    
    private boolean isSystemSkill(String skillId) {
        return skillId.contains("skill-auth") ||
               skillId.contains("skill-capability") ||
               skillId.contains("skill-dict") ||
               skillId.contains("skill-menu") ||
               skillId.contains("skill-role") ||
               skillId.contains("skill-audit") ||
               skillId.contains("skill-discovery") ||
               skillId.contains("skill-install") ||
               skillId.contains("skill-key") ||
               skillId.contains("skill-history") ||
               skillId.contains("skill-template") ||
               skillId.contains("skill-dashboard") ||
               skillId.contains("skill-scene") ||
               skillId.contains("skill-management") ||
               skillId.contains("skill-config") ||
               skillId.contains("skill-notification") ||
               skillId.contains("skill-agent") ||
               skillId.contains("skill-llm-chat") ||
               skillId.contains("skill-health") ||
               skillId.contains("skill-knowledge") ||
               skillId.contains("skill-tenant") ||
               skillId.contains("skill-org") ||
               skillId.contains("skill-support") ||
               skillId.contains("skill-setup") ||
               skillId.contains("skill-workflow") ||
               skillId.contains("skill-vfs") ||
               skillId.contains("skill-protocol") ||
               skillId.contains("skill-messaging");
    }
    
    private boolean isDriverSkill(String skillId) {
        return isLlmDriver(skillId) ||
               isMessagingDriver(skillId) ||
               isStorageDriver(skillId) ||
               isOrgDriver(skillId) ||
               isRagDriver(skillId);
    }
    
    private boolean isLlmDriver(String skillId) {
        return skillId.contains("-openai") ||
               skillId.contains("-deepseek") ||
               skillId.contains("-qianwen") ||
               skillId.contains("-ollama") ||
               skillId.contains("-zhipu") ||
               skillId.contains("-baidu") ||
               skillId.contains("-minimax") ||
               skillId.contains("-claude") ||
               skillId.contains("skill-llm-");
    }
    
    private boolean isMessagingDriver(String skillId) {
        return skillId.contains("-dingtalk") ||
               skillId.contains("-dingding") ||
               skillId.contains("-feishu") ||
               skillId.contains("-wecom") ||
               skillId.contains("-email") ||
               skillId.contains("-sms") ||
               skillId.contains("-push") ||
               skillId.contains("skill-im-");
    }
    
    private boolean isStorageDriver(String skillId) {
        return skillId.contains("-s3") ||
               skillId.contains("-oss") ||
               skillId.contains("-minio") ||
               skillId.contains("-cos") ||
               skillId.contains("skill-vfs-");
    }
    
    private boolean isOrgDriver(String skillId) {
        return skillId.contains("skill-org-");
    }
    
    private boolean isRagDriver(String skillId) {
        return skillId.contains("skill-rag");
    }
    
    private boolean isSceneSkill(String skillId) {
        return skillId.contains("scene-") ||
               skillId.contains("skill-scenes");
    }
    
    private boolean isCapabilitySkill(String skillId) {
        return skillId.contains("capability-");
    }
    
    private boolean isToolSkill(String skillId) {
        return skillId.contains("tool-");
    }
}
