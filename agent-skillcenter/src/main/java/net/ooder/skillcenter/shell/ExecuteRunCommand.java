package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillResult;
import net.ooder.skillcenter.model.SkillException;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行运行命令，同步执行技能
 */
public class ExecuteRunCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "execute run";
    }
    
    @Override
    public String getDescription() {
        return "同步执行技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            output.error("请指定技能ID");
            output.println("使用示例: execute run <skill-id> [param1=value1] [param2=value2]");
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
        
        // 同步执行技能
        output.println("==============================================");
        output.println("正在执行技能: " + skillId);
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
            public SkillResult execute(SkillContext context) throws SkillException {
                SkillResult result = new SkillResult();
                result.setMessage("Skill executed successfully");
                result.addData("output", "Execution result for skill " + skillId);
                return result;
            }
        };
        
        SkillResult result = executorEngine.executeSkill(skill, context);
        
        output.println("==============================================");
        output.println("执行结果");
        output.println("==============================================");
        output.println("执行状态: " + (result.isSuccess() ? "成功" : "失败"));
        
        if (result.isSuccess()) {
            output.success("技能执行成功!");
            output.println("执行结果: " + result.getMessage());
            output.println("输出数据: " + result.getData("output"));
        } else {
            output.error("技能执行失败!");
            output.println("错误信息: " + result.getMessage());
        }
        
        output.println("==============================================");

    }
}
