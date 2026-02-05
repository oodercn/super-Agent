package net.ooder.skillcenter.shell.security;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.security.service.KeyManagementService;
import net.ooder.skillcenter.security.service.KeyManagementService.KeyAuditLog;
import net.ooder.skillcenter.security.service.impl.KeyManagementServiceImpl;
import java.util.List;

public class SecurityKeyAuditCommand extends AbstractCommand {
    
    private KeyManagementService keyManagementService;
    
    public SecurityKeyAuditCommand() {
        super();
        this.keyManagementService = KeyManagementServiceImpl.getInstance();
    }
    
    @Override
    public String getName() {
        return "security key audit";
    }
    
    @Override
    public String getDescription() {
        return "显示密钥的审计日志";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: security key audit <keyId> [options]");
            println("选项:");
            println("  --limit <number>           - 限制返回条数 (默认: 50)");
            println("  --startTime <timestamp>    - 开始时间");
            println("  --endTime <timestamp>      - 结束时间");
            return;
        }
        
        String keyId = args[0];
        Integer limit = 50;
        Long startTime = null;
        Long endTime = null;
        
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--limit=")) {
                limit = Integer.parseInt(args[i].substring(8));
            } else if (args[i].startsWith("--startTime=")) {
                startTime = Long.parseLong(args[i].substring(12));
            } else if (args[i].startsWith("--endTime=")) {
                endTime = Long.parseLong(args[i].substring(10));
            }
        }
        
        KeyAuditLog auditLog = KeyManagementServiceImpl.getInstance().getKeyAuditLog(keyId);
        
        if (auditLog == null) {
            error("密钥不存在或没有审计日志: " + keyId);
            return;
        }
        
        println("==============================================");
        println("密钥审计日志: " + keyId);
        println("==============================================");
        println("日志 ID:       " + auditLog.getLogId());
        println("密钥 ID:       " + auditLog.getKeyId());
        println("Agent ID:       " + auditLog.getAgentId());
        println("操作:           " + auditLog.getAction());
        println("操作者:         " + auditLog.getActor());
        println("时间:           " + formatTimestamp(auditLog.getTimestamp()));
        println("详细信息:       " + auditLog.getDetails());
        println("==============================================");
    }
    
    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
