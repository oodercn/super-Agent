package net.ooder.skillcenter.shell.lifecycle;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.lifecycle.execution.SkillExecutionManager;
import net.ooder.skillcenter.lifecycle.execution.SkillExecutionManager.ExecutionResult;
import net.ooder.skillcenter.lifecycle.execution.impl.SkillExecutionManagerImpl;
import net.ooder.skillcenter.model.SkillContext;
import java.util.HashMap;
import java.util.Map;

public class SkillExecuteCommand extends AbstractCommand {
    
    private SkillExecutionManager executionManager;
    
    public SkillExecuteCommand() {
        super();
        this.executionManager = new SkillExecutionManagerImpl();
    }
    
    @Override
    public String getName() {
        return "skill execute";
    }
    
    @Override
    public String getDescription() {
        return "执行指定技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: skill execute <skillId> [options]");
            println("选项:");
            println("  --async                    - 异步执行");
            println("  --params <json>           - 执行参数 (JSON 格式)");
            println("  --timeout <seconds>       - 执行超时时间 (默认: 60)");
            return;
        }
        
        String skillId = args[0];
        boolean async = false;
        String paramsJson = "{}";
        Integer timeout = 60;
        
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--async")) {
                async = true;
            } else if (args[i].startsWith("--params=")) {
                paramsJson = args[i].substring(9);
            } else if (args[i].startsWith("--timeout=")) {
                timeout = Integer.parseInt(args[i].substring(10));
            }
        }
        
        Map<String, Object> params = parseJsonParams(paramsJson);
        
        SkillContext context = new SkillContext();
        context.setParams(params);
        context.setTimeout(timeout * 1000);
        
        println("正在执行技能: " + skillId);
        println("执行模式: " + (async ? "异步" : "同步"));
        if (!params.isEmpty()) {
            println("执行参数: " + paramsJson);
        }
        println("超时时间: " + timeout + " 秒");
        
        if (async) {
            executionManager.executeSkillAsync(skillId, context, new SkillExecutionManager.ExecutionCallback() {
                @Override
                public void onSuccess(ExecutionResult result) {
                    success("技能执行成功");
                    println("执行 ID: " + result.getExecutionId());
                    println("技能 ID: " + result.getSkillId());
                    println("开始时间: " + formatTimestamp(result.getStartTime()));
                    println("结束时间: " + formatTimestamp(result.getEndTime()));
                    println("执行时长: " + result.getDuration() + " ms");
                    
                    if (result.getResult() != null) {
                        println("执行结果: " + result.getResult().toString());
                    }
                }
                
                @Override
                public void onFailure(ExecutionResult result) {
                    error("技能执行失败");
                    println("执行 ID: " + result.getExecutionId());
                    println("技能 ID: " + result.getSkillId());
                    println("错误信息: " + result.getErrorMessage());
                }
                
                @Override
                public void onProgress(String executionId, double progress) {
                    println("执行进度: " + executionId + " - " + String.format("%.2f%%", progress * 100));
                }
            });
            
            println("异步执行已启动，执行 ID 将在完成时显示");
        } else {
            ExecutionResult result = executionManager.executeSkill(skillId, context);
            
            if (result.isSuccess()) {
                success("技能执行成功");
                println("执行 ID: " + result.getExecutionId());
                println("技能 ID: " + result.getSkillId());
                println("开始时间: " + formatTimestamp(result.getStartTime()));
                println("结束时间: " + formatTimestamp(result.getEndTime()));
                println("执行时长: " + result.getDuration() + " ms");
                
                if (result.getResult() != null) {
                    println("执行结果: " + result.getResult().toString());
                }
            } else {
                error("技能执行失败");
                println("执行 ID: " + result.getExecutionId());
                println("技能 ID: " + result.getSkillId());
                println("错误信息: " + result.getErrorMessage());
            }
        }
    }
    
    private Map<String, Object> parseJsonParams(String json) {
        Map<String, Object> params = new HashMap<>();
        if (json == null || json.trim().isEmpty() || json.equals("{}")) {
            return params;
        }
        
        try {
            String trimmed = json.trim();
            if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
                String content = trimmed.substring(1, trimmed.length() - 1);
                String[] pairs = content.split(",");
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim().replaceAll("\"", "");
                        String value = keyValue[1].trim().replaceAll("\"", "");
                        params.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("解析 JSON 参数失败: " + e.getMessage());
        }
        
        return params;
    }
    
    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
