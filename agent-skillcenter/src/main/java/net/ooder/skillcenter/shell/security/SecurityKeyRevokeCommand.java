package net.ooder.skillcenter.shell.security;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.security.service.KeyManagementService;
import net.ooder.skillcenter.security.service.KeyManagementService.KeyRevocationResult;
import net.ooder.skillcenter.security.service.impl.KeyManagementServiceImpl;

public class SecurityKeyRevokeCommand extends AbstractCommand {
    
    private KeyManagementService keyManagementService;
    
    public SecurityKeyRevokeCommand() {
        super();
        this.keyManagementService = KeyManagementServiceImpl.getInstance();
    }
    
    @Override
    public String getName() {
        return "security key revoke";
    }
    
    @Override
    public String getDescription() {
        return "撤销指定 Agent 的密钥";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            error("用法: security key revoke <keyId> <agentId>");
            return;
        }
        
        String keyId = args[0];
        String agentId = args[1];
        
        KeyRevocationResult result = KeyManagementServiceImpl.getInstance()
            .revokeAgentKey(agentId, keyId);
        
        if (result.isSuccess()) {
            success("密钥撤销成功");
            println("密钥 ID: " + result.getKeyId());
            println("Agent ID: " + result.getAgentId());
            println("撤销时间: " + formatTimestamp(result.getRevokedAt()));
        } else {
            error("密钥撤销失败: " + result.getMessage());
        }
    }
    
    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
