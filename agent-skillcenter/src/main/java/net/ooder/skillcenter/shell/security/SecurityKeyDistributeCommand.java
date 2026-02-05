package net.ooder.skillcenter.shell.security;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.security.service.KeyManagementService;
import net.ooder.skillcenter.security.service.KeyManagementService.KeyDistributionResult;
import net.ooder.skillcenter.security.service.impl.KeyManagementServiceImpl;

public class SecurityKeyDistributeCommand extends AbstractCommand {
    
    private KeyManagementService keyManagementService;
    
    public SecurityKeyDistributeCommand() {
        super();
        this.keyManagementService = new KeyManagementServiceImpl();
    }
    
    @Override
    public String getName() {
        return "security key distribute";
    }
    
    @Override
    public String getDescription() {
        return "将密钥分发给目标 Agent";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            error("用法: security key distribute <keyId> <targetAgentId>");
            return;
        }
        
        String keyId = args[0];
        String targetAgentId = args[1];
        
        KeyDistributionResult result = keyManagementService
            .distributeKeyToAgent(keyId, targetAgentId);
        
        if (result.isSuccess()) {
            success("密钥分发成功");
            println("密钥 ID: " + result.getKeyId());
            println("目标 Agent: " + result.getAgentId());
            println("分发 ID: " + result.getDistributionId());
        } else {
            error("密钥分发失败: " + result.getMessage());
        }
    }
}
