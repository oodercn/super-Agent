package net.ooder.sdk.skill.factory;

import net.ooder.sdk.skill.Skill;
import net.ooder.sdk.skill.SkillExecutor;
import net.ooder.sdk.skill.SkillResult;
import net.ooder.sdk.skill.SkillStatus;
import net.ooder.sdk.skill.AbstractSkill;
import net.ooder.sdk.skill.factory.SkillFactory;

import java.util.Map;
import java.util.HashMap;

/**
 * 技能工厂实现类
 */
public class SkillFactoryImpl implements SkillFactory {
    
    private final Map<String, Class<? extends Skill>> skillTypeMap = new HashMap<>();
    
    @Override
    public Skill createSkill(String skillId, String skillName, String description, Map<String, String> parameters, String skillCode) {
        // 创建技能实现
        return new Skill() {
            @Override
            public String getSkillId() {
                return skillId;
            }
            
            @Override
            public String getName() {
                return skillName;
            }
            
            @Override
            public String getDescription() {
                return description;
            }
            
            @Override
            public Map<String, String> getParameters() {
                return parameters;
            }
            
            @Override
            public SkillResult execute(Map<String, Object> params) {
                // 默认实现，返回成功结果
                Map<String, Object> data = new HashMap<>();
                data.put("skillId", skillId);
                data.put("params", params);
                data.put("message", "Skill executed successfully");
                
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("skillId", skillId);
                metadata.put("executionTime", System.currentTimeMillis());
                
                return SkillResult.success(data, metadata);
            }
            
            @Override
            public void initialize() {
                // 默认初始化实现
            }
            
            @Override
            public void destroy() {
                // 默认销毁实现
            }
            
            @Override
            public SkillStatus getStatus() {
                return SkillStatus.READY;
            }
        };
    }
    
    @Override
    public SkillExecutor createSkillExecutor() {
        return new SkillExecutor();
    }
    
    @Override
    public void registerSkillType(String skillType, Class<? extends Skill> skillClass) {
        skillTypeMap.put(skillType, skillClass);
    }
}
