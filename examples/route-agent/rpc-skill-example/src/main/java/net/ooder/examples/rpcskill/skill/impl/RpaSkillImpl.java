package net.ooder.examples.rpaskill.skill.impl;

import net.ooder.examples.rpaskill.config.EndAgentConfig;
import net.ooder.examples.rpaskill.model.RpaRequest;
import net.ooder.examples.rpaskill.model.RpaResponse;
import net.ooder.examples.rpaskill.service.RpaOperationService;
import net.ooder.examples.rpaskill.service.SnAuthService;
import net.ooder.examples.rpaskill.skill.RpaSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RPA Skill Implementation
 * Provides RPA operation functionality with endAgent role
 * 作者：ooderAI agent team   V0.6.0
 */
@Service
public class RpaSkillImpl implements RpaSkill {

    private final RpaOperationService rpaOperationService;
    private final EndAgentConfig endAgentConfig;

    @Autowired
    public RpaSkillImpl(RpaOperationService rpaOperationService, EndAgentConfig endAgentConfig) {
        this.rpaOperationService = rpaOperationService;
        this.endAgentConfig = endAgentConfig;
    }

    @Override
    public RpaResponse executeRpa(RpaRequest request) {
        return rpaOperationService.performRpaOperation(request);
    }

    @Override
    public RpaResponse executeRpaWithAuth(RpaRequest request, String sn) throws SnAuthService.UnauthorizedException {
        return rpaOperationService.performRpaOperationWithAuth(request, sn);
    }

    @Override
    public String getRole() {
        return endAgentConfig.getRoleName();
    }

    @Override
    public String getDescription() {
        return endAgentConfig.getDescription();
    }
}
