package net.ooder.skillcenter.shell.security;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.security.service.KeyManagementService;
import net.ooder.skillcenter.security.service.KeyManagementService.KeyInfo;
import net.ooder.skillcenter.security.service.impl.KeyManagementServiceImpl;
import java.util.Map;

public class SecurityKeyInfoCommand extends AbstractCommand {
    
    private KeyManagementService keyManagementService;
    
    public SecurityKeyInfoCommand() {
        super();
        this.keyManagementService = KeyManagementServiceImpl.getInstance();
    }
    
    @Override
    public String getName() {
        return "security key info";
    }
    
    @Override
    public String getDescription() {
        return "显示指定密钥的详细信息";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: security key info <keyId>");
            return;
        }
        
        String keyId = args[0];
        KeyInfo keyInfo = this.keyManagementService.getKeyInfo(keyId);
        
        if (keyInfo == null) {
            error("密钥不存在: " + keyId);
            return;
        }
        
        println("==============================================");
        println("密钥详细信息");
        println("==============================================");
        println("密钥 ID:       " + keyInfo.getKeyId());
        println("密钥类型:       " + keyInfo.getKeyType());
        println("Agent ID:       " + keyInfo.getAgentId());
        println("状态:           " + keyInfo.getStatus());
        println("创建时间:       " + formatTimestamp(keyInfo.getCreatedAt()));
        println("过期时间:       " + formatTimestamp(keyInfo.getExpiresAt()));
        println("最后使用时间:   " + formatTimestamp(keyInfo.getLastUsedAt()));
        
        if (keyInfo.getMetadata() != null && !keyInfo.getMetadata().isEmpty()) {
            println("元数据:");
            for (Map.Entry<String, Object> entry : keyInfo.getMetadata().entrySet()) {
                println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
        
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
