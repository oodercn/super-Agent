package net.ooder.examples.rpaskill.controller;

import net.ooder.examples.rpaskill.model.RpaRequest;
import net.ooder.examples.rpaskill.model.RpaResponse;
import net.ooder.examples.rpaskill.service.SnAuthService;
import net.ooder.examples.rpaskill.skill.RpaSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RPA Skill REST Controller
 * Exposes RPA skill operations as HTTP endpoints
 * 作者：ooderAI agent team   V0.6.0
 */
@RestController
@RequestMapping("/api/rpa")
public class RpaSkillController {

    private final RpaSkill rpaSkill;

    @Autowired
    public RpaSkillController(RpaSkill rpaSkill) {
        this.rpaSkill = rpaSkill;
    }

    /**
     * Endpoint to execute RPA operation without authorization
     * @param request RPA request parameters in request body
     * @return RPA response with the result
     */
    @PostMapping("/execute")
    public ResponseEntity<RpaResponse> executeRpa(@RequestBody RpaRequest request) {
        RpaResponse response = rpaSkill.executeRpa(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint to execute RPA operation with SN authorization
     * @param request RPA request parameters in request body
     * @param sn Serial Number for authorization (in header)
     * @return RPA response with the result
     */
    @PostMapping("/execute-with-auth")
    public ResponseEntity<RpaResponse> executeRpaWithAuth(
            @RequestBody RpaRequest request,
            @RequestHeader(value = "X-Auth-SN") String sn) {
        try {
            RpaResponse response = rpaSkill.executeRpaWithAuth(request, sn);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (SnAuthService.UnauthorizedException e) {
            RpaResponse errorResponse = RpaResponse.error(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            RpaResponse errorResponse = RpaResponse.error("Internal server error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to get skill information
     * @return Skill information including role and description
     */
    @GetMapping("/info")
    public ResponseEntity<SkillInfo> getSkillInfo() {
        SkillInfo skillInfo = new SkillInfo();
        skillInfo.setRole(rpaSkill.getRole());
        skillInfo.setDescription(rpaSkill.getDescription());
        return new ResponseEntity<>(skillInfo, HttpStatus.OK);
    }

    /**
     * Skill Info Model
     */
    public static class SkillInfo {
        private String role;
        private String description;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
