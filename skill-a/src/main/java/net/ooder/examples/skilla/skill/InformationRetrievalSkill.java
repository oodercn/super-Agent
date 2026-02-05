package net.ooder.examples.skilla.skill;

import com.alibaba.fastjson.JSON;
import net.ooder.examples.skilla.model.InformationRequest;
import net.ooder.examples.skilla.model.InformationResponse;
import net.ooder.examples.skilla.service.DiscoveryService;
import net.ooder.examples.skilla.service.NetworkService;
import net.ooder.examples.skilla.service.SecurityService;
import net.ooder.sdk.skill.Skill;
import net.ooder.sdk.skill.SkillResult;
import net.ooder.sdk.skill.SkillStatus;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InformationRetrievalSkill implements Skill {

    private static final Logger logger = LoggerFactory.getLogger(InformationRetrievalSkill.class);
    private static final String SKILL_ID = "information_retrieval";
    private static final String SKILL_NAME = "InformationRetrievalSkill";
    private static final String SKILL_VERSION = "1.0.0";
    private static final String SKILL_DESCRIPTION = "Skill for retrieving information from Network A";

    private final NetworkService networkService;
    private final SecurityService securityService;
    private final DiscoveryService discoveryService;
    private SkillStatus status = SkillStatus.READY;

    @Autowired
    public InformationRetrievalSkill(NetworkService networkService, SecurityService securityService, DiscoveryService discoveryService) {
        this.networkService = networkService;
        this.securityService = securityService;
        this.discoveryService = discoveryService;
    }

    @Override
    public String getSkillId() {
        return SKILL_ID;
    }

    @Override
    public String getName() {
        return SKILL_NAME;
    }

    @Override
    public String getDescription() {
        return SKILL_DESCRIPTION;
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query", "Information query string");
        parameters.put("token", "Security token for authentication");
        return parameters;
    }

    @Override
    public SkillResult execute(Map<String, Object> params) {
        logger.info("Executing {}", SKILL_NAME);
        status = SkillStatus.EXECUTING;

        try {
            // Validate the token
            String token = (String) params.get("token");
            if (token == null || !securityService.validateToken(token)) {
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("success", false);
                resultData.put("message", "Invalid token");
                status = SkillStatus.ERROR;
                return SkillResult.failure("Invalid token", resultData);
            }

            // Get information from Network A
            Map<String, Object> data = networkService.retrieveInformation();
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("success", true);
            resultData.put("message", "Information retrieved successfully");
            resultData.put("data", data);
            resultData.put("requestId", java.util.UUID.randomUUID().toString());

            // Add signature to response if security is enabled
            if (securityService.isSecurityEnabled()) {
                String signature = securityService.generateResponseSignature(
                        (String) resultData.get("requestId"), 
                        JSON.toJSONString(data)
                );
                data.put("signature", signature);
            }

            // Check if we need to leave the scene after completion
            if ((boolean) resultData.get("success")) {
                logger.info("Skill execution completed successfully");
                // In a real scenario, we might trigger auto-leave here based on scene configuration
                // For now, we'll just log it
            }

            status = SkillStatus.READY;
            return SkillResult.success(resultData, null);

        } catch (Exception e) {
            logger.error("Error executing {}: {}", SKILL_NAME, e.getMessage(), e);
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("success", false);
            resultData.put("message", "Skill execution failed: " + e.getMessage());
            status = SkillStatus.ERROR;
            return SkillResult.failure(e.getMessage(), resultData);
        }
    }

    @Override
    public void initialize() {
        logger.info("Initializing {}", SKILL_NAME);
        status = SkillStatus.READY;
        // Additional initialization logic if needed
    }

    @Override
    public void destroy() {
        logger.info("Destroying {}", SKILL_NAME);
        status = SkillStatus.DESTROYED;
        // Cleanup logic if needed
    }

    @Override
    public SkillStatus getStatus() {
        return status;
    }

    public String getSkillInfo() {
        Map<String, Object> skillInfo = new HashMap<>();
        skillInfo.put("name", SKILL_NAME);
        skillInfo.put("version", SKILL_VERSION);
        skillInfo.put("type", "endAgent");
        skillInfo.put("function", "Information Retrieval");
        skillInfo.put("description", SKILL_DESCRIPTION);
        skillInfo.put("status", discoveryService.isJoined() ? "connected" : "disconnected");
        return JSON.toJSONString(skillInfo);
    }
}
