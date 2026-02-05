package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.execution.SkillExecutionStats;

import java.util.List;

/**
 * 执行历史命令，查看技能执行历史
 */
public class ExecuteHistoryCommand extends AbstractCommand {
    
    @Override
    public String getName() {
        return "execute history";
    }
    
    @Override
    public String getDescription() {
        return "查看技能执行历史";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        // 模拟执行历史
        output.println("==============================================");
        output.println("技能执行历史");
        output.println("==============================================");
        
        // 准备表格数据
        String[] headers = {"执行时间", "技能ID", "执行状态", "执行时间(ms)", "执行模式"};
        String[][] rows = new String[2][headers.length];
        
        rows[0][0] = new java.util.Date().toString();
        rows[0][1] = "skill-123";
        rows[0][2] = "成功";
        rows[0][3] = "100";
        rows[0][4] = "同步";
        
        rows[1][0] = new java.util.Date().toString();
        rows[1][1] = "skill-456";
        rows[1][2] = "成功";
        rows[1][3] = "150";
        rows[1][4] = "异步";
        
        output.table(headers, rows);
        output.println("==============================================");
        output.println("总计: 2 条执行记录");
        output.println("==============================================");

    }
}
