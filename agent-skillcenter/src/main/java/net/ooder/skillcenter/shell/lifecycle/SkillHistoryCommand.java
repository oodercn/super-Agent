package net.ooder.skillcenter.shell.lifecycle;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.lifecycle.execution.SkillExecutionManager;
import net.ooder.skillcenter.lifecycle.execution.SkillExecutionManager.ExecutionRecord;
import net.ooder.skillcenter.lifecycle.execution.impl.SkillExecutionManagerImpl;
import java.util.List;

public class SkillHistoryCommand extends AbstractCommand {
    
    private SkillExecutionManager executionManager;
    
    public SkillHistoryCommand() {
        super();
        this.executionManager = new SkillExecutionManagerImpl();
    }
    
    @Override
    public String getName() {
        return "skill history";
    }
    
    @Override
    public String getDescription() {
        return "显示技能执行历史";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: skill history <skillId> [options]");
            println("选项:");
            println("  --limit <number>           - 限制返回条数 (默认: 50)");
            println("  --startTime <timestamp>    - 开始时间");
            println("  --endTime <timestamp>      - 结束时间");
            println("  --status <status>         - 按状态筛选 (success, failed, running)");
            return;
        }
        
        String skillId = args[0];
        Integer limit = 50;
        Long startTime = null;
        Long endTime = null;
        String statusFilter = null;
        
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--limit=")) {
                limit = Integer.parseInt(args[i].substring(8));
            } else if (args[i].startsWith("--startTime=")) {
                startTime = Long.parseLong(args[i].substring(12));
            } else if (args[i].startsWith("--endTime=")) {
                endTime = Long.parseLong(args[i].substring(10));
            } else if (args[i].startsWith("--status=")) {
                statusFilter = args[i].substring(9);
            }
        }
        
        List<ExecutionRecord> records;
        if (startTime != null && endTime != null) {
            records = executionManager.getExecutionHistory(skillId, startTime, endTime);
        } else {
            records = executionManager.getExecutionHistory(skillId);
        }
        
        if (records.isEmpty()) {
            println("技能 " + skillId + " 没有执行历史");
            return;
        }
        
        if (statusFilter != null) {
            records.removeIf(record -> !record.getStatus().equalsIgnoreCase(statusFilter));
        }
        
        if (records.size() > limit) {
            records = records.subList(0, limit);
        }
        
        println("==============================================");
        println("技能执行历史: " + skillId);
        println("==============================================");
        
        if (statusFilter != null) {
            println("状态筛选: " + statusFilter);
        }
        
        println(String.format("%-20s %-15s %-15s %-15s %-10s", 
            "执行 ID", "状态", "开始时间", "结束时间", "时长(ms)", "结果"));
        println("------------------------------------------------------------------------------------");
        
        for (ExecutionRecord record : records) {
            println(String.format("%-20s %-15s %-15s %-15s %-10s",
                record.getExecutionId(),
                record.getStatus(),
                formatTimestamp(record.getStartTime()),
                formatTimestamp(record.getEndTime()),
                String.valueOf(record.getDuration()),
                record.isSuccess() ? "成功" : "失败"));
        }
        
        println("==============================================");
        println("总计: " + records.size() + " 条记录");
    }
    
    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
