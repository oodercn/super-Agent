package net.ooder.examples.skillb.skill;

import com.alibaba.fastjson.JSON;
import net.ooder.examples.skillb.model.DataSubmissionRequest;
import net.ooder.examples.skillb.model.DataSubmissionResponse;
import net.ooder.examples.skillb.service.DiscoveryService;
import net.ooder.examples.skillb.service.NetworkService;
import net.ooder.examples.skillb.service.SecurityService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSubmissionSkill {

    private static final Logger logger = LoggerFactory.getLogger(DataSubmissionSkill.class);
    private static final String SKILL_NAME = "DataSubmissionSkill";
    private static final String SKILL_VERSION = "1.0.0";

    private final NetworkService networkService;
    private final SecurityService securityService;
    private final DiscoveryService discoveryService;

    @Autowired
    public DataSubmissionSkill(NetworkService networkService, SecurityService securityService, DiscoveryService discoveryService) {
        this.networkService = networkService;
        this.securityService = securityService;
        this.discoveryService = discoveryService;
    }

    public String executeSkill(String requestJson) {
        logger.info("Executing {}", SKILL_NAME);

        try {
            // Parse the request JSON
            DataSubmissionRequest request = JSON.parseObject(requestJson, DataSubmissionRequest.class);
            logger.debug("Parsed request: {}", request);

            // Validate the token
            if (!securityService.validateToken(request.getToken())) {
                DataSubmissionResponse response = new DataSubmissionResponse();
                response.setSuccess(false);
                response.setMessage("Invalid token");
                return JSON.toJSONString(response);
            }

            // Submit data to Network B
            Map<String, Object> result = networkService.submitData(request.getData());
            DataSubmissionResponse response = new DataSubmissionResponse();
            response.setSuccess("SUCCESS".equals(result.get("status")));
            response.setMessage((String) result.get("message"));
            response.setResult(result);
            response.setRequestId(java.util.UUID.randomUUID().toString());

            // Add signature to response if security is enabled
            if (securityService.isSecurityEnabled() && response.getResult() != null) {
                String signature = securityService.generateResponseSignature(response.getRequestId(), JSON.toJSONString(response.getResult()));
                response.getResult().put("signature", signature);
            }

            // Check if we need to leave the scene after completion
            if (response.isSuccess()) {
                logger.info("Skill execution completed successfully");
                // In a real scenario, we might trigger auto-leave here based on scene configuration
                // For now, we'll just log it
            }

            return JSON.toJSONString(response);

        } catch (Exception e) {
            logger.error("Error executing {}: {}", SKILL_NAME, e.getMessage(), e);
            DataSubmissionResponse errorResponse = new DataSubmissionResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Skill execution failed: " + e.getMessage());
            return JSON.toJSONString(errorResponse);
        }
    }

    public String getSkillInfo() {
        return JSON.toJSONString(new SkillInfo());
    }

    // Inner class to hold skill information
    private class SkillInfo {
        private String name = SKILL_NAME;
        private String version = SKILL_VERSION;
        private String type = "endAgent";
        private String function = "Data Submission";
        private String description = "Skill for submitting data to Network B";
        private String status = discoveryService.isJoined() ? "connected" : "disconnected";

        // Getters - needed for JSON serialization
        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getType() { return type; }
        public String getFunction() { return function; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
    }
}
