package net.ooder.skillcenter.shell.security;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.security.service.KeyManagementService;
import net.ooder.skillcenter.security.service.KeyManagementService.KeyInfo;
import net.ooder.skillcenter.security.service.impl.KeyManagementServiceImpl;
import java.util.List;

public class SecurityKeyListCommand extends AbstractCommand {
    
    private KeyManagementService keyManagementService;
    
    public SecurityKeyListCommand() {
        super();
        this.keyManagementService = KeyManagementServiceImpl.getInstance();
    }
    
    @Override
    public String getName() {
        return "security key list";
    }
    
    @Override
    public String getDescription() {
        return "列出指定 Agent 的所有密钥";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: security key list <agentId> [options]");
            println("选项:");
            println("  --status <status>         - 按状态筛选 (active, expired, revoked)");
            println("  --type <type>            - 按类型筛选 (API_KEY, SESSION_KEY, etc.)");
            return;
        }
        
        String agentId = args[0];
        String statusFilter = null;
        String typeFilter = null;
        
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--status=")) {
                statusFilter = args[i].substring(9);
            } else if (args[i].startsWith("--type=")) {
                typeFilter = args[i].substring(7);
            }
        }
        
        List<KeyInfo> keys = KeyManagementServiceImpl.getInstance().listAgentKeys(agentId);
        
        if (keys.isEmpty()) {
            println("Agent " + agentId + " 没有密钥");
            return;
        }
        
        println("==============================================");
        println("Agent " + agentId + " 的密钥列表");
        println("==============================================");
        println(String.format("%-20s %-20s %-15s %-15s %-15s", 
            "密钥ID", "密钥类型", "状态", "创建时间", "过期时间"));
        println("------------------------------------------------------------------------------");
        
        for (KeyInfo key : keys) {
            if (statusFilter != null && !key.getStatus().equalsIgnoreCase(statusFilter)) {
                continue;
            }
            if (typeFilter != null && !key.getKeyType().equalsIgnoreCase(typeFilter)) {
                continue;
            }
            
            println(String.format("%-20s %-20s %-15s %-15s %-15s",
                key.getKeyId(),
                key.getKeyType(),
                key.getStatus(),
                formatTimestamp(key.getCreatedAt()),
                formatTimestamp(key.getExpiresAt())));
        }
        
        println("==============================================");
        println("总计: " + keys.size() + " 个密钥");
    }
    
    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
