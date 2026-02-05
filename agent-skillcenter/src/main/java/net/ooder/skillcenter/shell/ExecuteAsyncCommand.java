package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.model.SkillContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行异步命令，异步执行技能
 */
public class ExecuteAsyncCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "execute async";
    }
    
    @Override
    public String getDescription() {
        return "异步执行技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            output.error("请指定技能ID");
            output.println("使用示例: execute async <skill-id> [param1=value1] [param2=value2]");
            return;
        }
        
        String skillId = args[0];
        
        // 解析参数
        Map<String, Object> params = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            if (arg.contains("=")) {
                String[] parts = arg.split("=", 2);
                params.put(parts[0], parts[1]);
            }
        }
        
        // 创建执行上下文
        SkillContext context = new SkillContext();
        context.setParameters(params);
        
        // 异步执行技能
        output.println("==============================================");
        output.println("正在异步执行技能: " + skillId);
        output.println("==============================================");
        
        // 模拟获取技能实例
        net.ooder.skillcenter.model.Skill skill = new net.ooder.skillcenter.model.Skill() {
            @Override
            public String getId() {
                return skillId;
            }
            
            @Override
            public String getName() {
                return "Test Skill";
            }
            
            @Override
            public String getDescription() {
                return "Test Skill Description";
            }
            
            @Override
            public boolean isAvailable() {
                return true;
            }
            
            @Override
            public java.util.Map<String, net.ooder.skillcenter.model.SkillParam> getParams() {
                return new java.util.HashMap<>();
            }
            
            @Override
            public net.ooder.skillcenter.model.SkillResult execute(SkillContext context) throws net.ooder.skillcenter.model.SkillException {
                net.ooder.skillcenter.model.SkillResult result = new net.ooder.skillcenter.model.SkillResult();
                result.setMessage("Skill executed successfully");
                result.addData("output", "Execution result for skill " + skillId);
                return result;
            }
        };
        
        // 异步执行技能
        executorEngine.executeSkillAsync(skill, context, new net.ooder.skillcenter.execution.SkillExecutorEngine.SkillExecutionCallback() {
            @Override
            public void onSuccess(net.ooder.skillcenter.model.SkillResult result) {
                System.out.println("异步执行成功: " + result.getMessage());
            }
            
            @Override
            public void onFailure(net.ooder.skillcenter.model.SkillException e) {
                System.out.println("异步执行失败: " + e.getMessage());
            }
        });
        
        output.println("==============================================");
        output.success("技能异步执行已启动!");
        output.println("技能ID: " + skillId);
        output.println("==============================================");

    }
}
