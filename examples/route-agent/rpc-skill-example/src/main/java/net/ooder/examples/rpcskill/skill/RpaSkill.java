package net.ooder.examples.rpaskill.skill;

import net.ooder.examples.rpaskill.model.RpaRequest;
import net.ooder.examples.rpaskill.model.RpaResponse;
import net.ooder.examples.rpaskill.service.SnAuthService;

/**
 * RPA Skill Interface
 * Defines methods for RPA operations with endAgent role
 * 作者：ooderAI agent team   V0.6.0
 */
public interface RpaSkill {

    /**
     * Executes RPA operation with the given parameters
     * @param request RPA request parameters
     * @return RPA response with the result
     */
    RpaResponse executeRpa(RpaRequest request);

    /**
     * Executes RPA operation with SN authorization
     * @param request RPA request parameters
     * @param sn Serial Number for authorization
     * @return RPA response with the result
     * @throws SnAuthService.UnauthorizedException if SN is not authorized
     */
    RpaResponse executeRpaWithAuth(RpaRequest request, String sn) throws SnAuthService.UnauthorizedException;

    /**
     * Gets the role of the skill (endAgent)
     * @return Role name
     */
    String getRole();

    /**
     * Gets the skill description
     * @return Skill description
     */
    String getDescription();
}
