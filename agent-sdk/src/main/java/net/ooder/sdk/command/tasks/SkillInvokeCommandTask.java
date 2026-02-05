package net.ooder.sdk.command.tasks;


import net.ooder.sdk.command.api.CommandAnnotation;
import net.ooder.sdk.command.impl.AbstractCommandTask;
import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.params.SkillInvokeParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@CommandAnnotation(
        id = "skill_invoke",
        name = "Skill Invoke Command",
        desc = "Invoke a skill",
        commandType = CommandType.SKILL_INVOKE,
        key = "skill.invoke"
)
public class SkillInvokeCommandTask extends AbstractCommandTask<SkillInvokeParams> {
    private static final Logger log = LoggerFactory.getLogger(SkillInvokeCommandTask.class);

    public SkillInvokeCommandTask() {
        super(CommandType.SKILL_INVOKE);
    }

    @Override
    protected CommandResult doExecute(CommandPacket<SkillInvokeParams> packet) throws Exception {
        log.info("Handling SKILL_INVOKE command with params: {}", packet.getPayload());
        
        Object payload = packet.getPayload();
        String skillId = null;
        Map<String, Object> skillParams = new HashMap<>();
        
        try {
            // 从参数中获取技能ID和参数
            if (payload instanceof SkillInvokeParams) {
                // 处理特定参数类的情况
                SkillInvokeParams params = (SkillInvokeParams) payload;
                skillId = params.getSkillId();
                skillParams = params.getParams();
            } else if (payload instanceof Map) {
                // 处理Map参数的情况（向后兼容）
                Map<String, Object> params = (Map<String, Object>) payload;
                skillId = (String) params.get("skillId");
                skillParams = (Map<String, Object>) params.getOrDefault("params", new HashMap<>());
            } else {
                throw new IllegalArgumentException("Invalid payload type: " + (payload != null ? payload.getClass().getName() : "null"));
            }
            
            if (skillId == null) {
                throw new IllegalArgumentException("Skill invoke parameters cannot be null");
            }
            
            // 参数验证
            if (skillId.trim().isEmpty()) {
                throw new IllegalArgumentException("Skill ID cannot be empty");
            }
            
            // 获取技能
            net.ooder.sdk.skill.Skill skill = getSkill(skillId);
            if (skill == null) {
                throw new IllegalArgumentException("Skill not found: " + skillId);
            }
            
            log.info("Invoking skill: {} ({}), params: {}", skillId, skill.getDescription(), skillParams);
            
            // 参数验证：检查是否提供了所有必需参数
            Map<String, String> requiredParams = skill.getParameters();
            for (Map.Entry<String, String> paramEntry : requiredParams.entrySet()) {
                if (!skillParams.containsKey(paramEntry.getKey())) {
                    throw new IllegalArgumentException("Missing required parameter: " + paramEntry.getKey() + 
                            " (" + paramEntry.getValue() + ")");
                }
            }
            
            // 实际调用技能
            net.ooder.sdk.skill.SkillResult skillResult = skill.execute(skillParams);
            
            // 构建技能调用结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("skillId", skillId);
            resultData.put("skillName", skill.getDescription());
            resultData.put("params", skillParams);
            resultData.put("result", skillResult.getData());
            resultData.put("success", skillResult.isSuccess());
            resultData.put("timestamp", System.currentTimeMillis());
            
            if (skillResult.isSuccess()) {
                return CommandResult.success(resultData, "Skill invoked successfully");
            } else {
                resultData.put("error", skillResult.getErrorMessage());
                return CommandResult.executionError(skillResult.getErrorMessage(), resultData);
            }
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid skill invocation parameters: {}", e.getMessage(), e);
            
            // 构建参数错误结果
            Map<String, Object> resultData = new HashMap<>();
            if (payload != null) {
                if (payload instanceof SkillInvokeParams) {
                    resultData.put("skillId", ((SkillInvokeParams) payload).getSkillId());
                    resultData.put("params", ((SkillInvokeParams) payload).getParams());
                } else if (payload instanceof Map) {
                    resultData.put("skillId", ((Map<String, Object>) payload).get("skillId"));
                    resultData.put("params", ((Map<String, Object>) payload).getOrDefault("params", new HashMap<>()));
                }
            }
            resultData.put("error", e.getMessage());
            resultData.put("timestamp", System.currentTimeMillis());
            
            return CommandResult.parameterError(e.getMessage(), resultData);
            
        } catch (Exception e) {
            log.error("Failed to invoke skill: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> resultData = new HashMap<>();
            if (payload != null) {
                if (payload instanceof SkillInvokeParams) {
                    resultData.put("skillId", ((SkillInvokeParams) payload).getSkillId());
                    resultData.put("params", ((SkillInvokeParams) payload).getParams());
                } else if (payload instanceof Map) {
                    resultData.put("skillId", ((Map<String, Object>) payload).get("skillId"));
                    resultData.put("params", ((Map<String, Object>) payload).getOrDefault("params", new HashMap<>()));
                }
            }
            resultData.put("error", e.getMessage());
            resultData.put("timestamp", System.currentTimeMillis());
            
            return CommandResult.executionError(e.getMessage(), resultData);
        }
    }
    
    /**
     * 获取技能
     */
    private net.ooder.sdk.skill.Skill getSkill(String skillId) {
        return agentSDK.getSkill(skillId);
    }
}