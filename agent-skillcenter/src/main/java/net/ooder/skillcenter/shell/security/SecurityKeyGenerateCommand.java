package net.ooder.skillcenter.shell.security;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.security.service.KeyManagementService;
import net.ooder.skillcenter.security.service.KeyManagementService.KeyGenerationResult;
import net.ooder.skillcenter.security.service.KeyManagementService.KeyType;
import net.ooder.skillcenter.security.service.impl.KeyManagementServiceImpl;
import java.util.HashMap;
import java.util.Map;

public class SecurityKeyGenerateCommand extends AbstractCommand {
    
    private KeyManagementService keyManagementService;
    
    public SecurityKeyGenerateCommand() {
        super();
        this.keyManagementService = KeyManagementServiceImpl.getInstance();
    }
    @Override
    public String getName() {
        return "security key generate";
    }
    
    @Override
    public String getDescription() {
        return "为指定 Agent 生成密钥";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            error("用法: security key generate <agentId> <keyType> [options]");
            println("密钥类型: API_KEY, SESSION_KEY, ACCESS_TOKEN, ENCRYPTION_KEY_PAIR, SYMMETRIC_KEY");
            println("选项:");
            println("  --ttl <milliseconds>      - 密钥有效期 (默认: 86400000)");
            println("  --keySize <size>          - 密钥大小 (默认: 2048)");
            println("  --scope <scope>           - 密钥作用域 (默认: read:skills)");
            return;
        }
        
        String agentId = args[0];
        String keyTypeStr = args[1];
        
        try {
            KeyType keyType = KeyType.valueOf(keyTypeStr);
            
            Map<String, Object> options = new HashMap<>();
            for (int i = 2; i < args.length; i++) {
                if (args[i].startsWith("--ttl=")) {
                    options.put("ttl", Long.parseLong(args[i].substring(6)));
                } else if (args[i].startsWith("--keySize=")) {
                    options.put("keySize", Integer.parseInt(args[i].substring(10)));
                } else if (args[i].startsWith("--scope=")) {
                    options.put("scope", args[i].substring(8));
                }
            }
            
            KeyGenerationResult result = KeyManagementServiceImpl.getInstance()
                .generateKeyForAgent(agentId, keyType, options);
            
            if (result.isSuccess()) {
                success("密钥生成成功");
                println("密钥 ID: " + result.getKeyId());
                println("密钥类型: " + result.getKeyType());
                println("密钥值: " + result.getKeyValue());
                println("过期时间: " + result.getExpiresAt());
            } else {
                error("密钥生成失败: " + result.getMessage());
            }
            
        } catch (IllegalArgumentException e) {
            error("无效的密钥类型: " + keyTypeStr);
            println("有效的密钥类型: API_KEY, SESSION_KEY, ACCESS_TOKEN, ENCRYPTION_KEY_PAIR, SYMMETRIC_KEY");
        }
    }
}
