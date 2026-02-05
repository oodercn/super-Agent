package net.ooder.skillcenter.shell.security;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.security.service.KeyManagementService;
import net.ooder.skillcenter.security.service.KeyManagementService.KeyValidationResult;
import net.ooder.skillcenter.security.service.impl.KeyManagementServiceImpl;
import java.util.Map;

public class SecurityKeyValidateCommand extends AbstractCommand {
    
    private KeyManagementService keyManagementService;
    
    public SecurityKeyValidateCommand() {
        super();
        this.keyManagementService = KeyManagementServiceImpl.getInstance();
    }
    
    @Override
    public String getName() {
        return "security key validate";
    }
    
    @Override
    public String getDescription() {
        return "验证密钥的有效性";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            error("用法: security key validate <agentId> <key>");
            return;
        }
        
        String agentId = args[0];
        String key = args[1];
        
        KeyValidationResult result = KeyManagementServiceImpl.getInstance()
                .validateAgentKey(agentId, key);
        
        if (result.isValid()) {
            success("密钥验证成功");
            println("密钥 ID: " + result.getKeyId());
            println("Agent ID: " + result.getAgentId());
            println("过期时间: " + formatTimestamp(result.getExpiresAt()));
            
            if (result.getClaims() != null && !result.getClaims().isEmpty()) {
                println("权限声明:");
                for (Map.Entry<String, Object> entry : result.getClaims().entrySet()) {
                    println("  " + entry.getKey() + ": " + entry.getValue());
                }
            }
        } else {
            error("密钥验证失败: " + result.getMessage());
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
