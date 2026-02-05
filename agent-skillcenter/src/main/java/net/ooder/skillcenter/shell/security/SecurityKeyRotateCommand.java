package net.ooder.skillcenter.shell.security;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.security.service.KeyManagementService;
import net.ooder.skillcenter.security.service.KeyManagementService.KeyRotationResult;
import net.ooder.skillcenter.security.service.impl.KeyManagementServiceImpl;

public class SecurityKeyRotateCommand extends AbstractCommand {
    
    private KeyManagementService keyManagementService;
    
    public SecurityKeyRotateCommand() {
        super();
        this.keyManagementService = KeyManagementServiceImpl.getInstance();
    }
    
    @Override
    public String getName() {
        return "security key rotate";
    }
    
    @Override
    public String getDescription() {
        return "轮换指定 Agent 的密钥";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            error("用法: security key rotate <keyId> <agentId> [options]");
            println("选项:");
            println("  --force                    - 强制轮换");
            println("  --newKeySize <size>        - 新密钥大小");
            return;
        }
        
        String keyId = args[0];
        String agentId = args[1];
        boolean force = false;
        Integer newKeySize = null;
        
        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("--force")) {
                force = true;
            } else if (args[i].startsWith("--newKeySize=")) {
                newKeySize = Integer.parseInt(args[i].substring(13));
            }
        }
        
        KeyRotationResult result = KeyManagementServiceImpl.getInstance()
            .rotateAgentKey(agentId, keyId);
        
        if (result.isSuccess()) {
            success("密钥轮换成功");
            println("旧密钥 ID: " + result.getOldKeyId());
            println("新密钥 ID: " + result.getNewKeyId());
            println("轮换时间: " + formatTimestamp(result.getTimestamp()));
        } else {
            error("密钥轮换失败: " + result.getMessage());
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
