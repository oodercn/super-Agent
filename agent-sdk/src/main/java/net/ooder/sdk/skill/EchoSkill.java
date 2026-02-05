package net.ooder.sdk.skill;

import java.util.HashMap;
import java.util.Map;

/**
 * Echo技能示例，回显输入参数
 */
public class EchoSkill extends AbstractSkill {
    
    @Override
    public boolean start() {
        // 启动逻辑
        System.out.println("EchoSkill started");
        return true;
    }
    
    @Override
    public boolean stop() {
        // 停止逻辑
        System.out.println("EchoSkill stopped");
        return true;
    }
    
    @Override
    public Object execute(Object task) {
        // 处理任务
        if (task instanceof Map) {
            Map<String, Object> params = (Map<String, Object>) task;
            // 获取输入参数
            String message = (String) params.get("message");
            int repeat = params.containsKey("repeat") ? (Integer) params.get("repeat") : 1;
            
            // 验证参数
            if (message == null || message.isEmpty()) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "Message parameter is required");
                return errorResult;
            }
            
            // 构建回显结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("success", true);
            resultData.put("originalMessage", message);
            resultData.put("repeatCount", repeat);
            
            // 构建回显消息
            StringBuilder echoBuilder = new StringBuilder();
            for (int i = 0; i < repeat; i++) {
                echoBuilder.append(message);
                if (i < repeat - 1) {
                    echoBuilder.append("\n");
                }
            }
            resultData.put("echoMessage", echoBuilder.toString());
            
            // 构建元数据
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("skillId", getSkillId());
            metadata.put("skillName", getSkillName());
            metadata.put("executionTime", System.currentTimeMillis());
            resultData.put("metadata", metadata);
            
            return resultData;
        }
        
        // 处理无效任务
        Map<String, Object> errorResult = new HashMap<>();
        errorResult.put("success", false);
        errorResult.put("message", "Invalid task format");
        return errorResult;
    }
}