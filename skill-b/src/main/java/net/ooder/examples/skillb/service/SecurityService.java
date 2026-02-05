package net.ooder.examples.skillb.service;

import net.ooder.examples.skillb.config.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Objects;

@Service
public class SecurityService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    private final SecurityConfig securityConfig;

    @Autowired
    public SecurityService(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    public boolean validateToken(String token) {
        logger.debug("Validating token: {}", token);
        
        // If security is disabled, always return true
        if (!securityConfig.isEnabled()) {
            logger.info("Security is disabled, token validation skipped");
            return true;
        }

        try {
            // Basic token validation logic - in real implementation this would be more complex
            // For now, we'll just check if the token matches our expected pattern
            if (token == null || token.isEmpty()) {
                logger.warn("Empty token provided");
                return false;
            }

            // In a real scenario, we might decode the token and validate its signature
            // For this example, we'll just check if it's not empty
            logger.info("Token validation successful");
            return true;
        } catch (Exception e) {
            logger.error("Error validating token: {}", e.getMessage(), e);
            return false;
        }
    }

    public String generateResponseSignature(String requestId, String responseData) {
        logger.debug("Generating response signature for requestId: {}", requestId);

        if (!securityConfig.isEnabled()) {
            logger.info("Security is disabled, signature generation skipped");
            return "";
        }

        try {
            // Simple signature generation logic - in real implementation this would be more secure
            String signatureInput = requestId + responseData + securityConfig.getKey();
            String signature = Base64.getEncoder().encodeToString(signatureInput.getBytes());
            
            logger.debug("Generated signature: {}", signature);
            return signature;
        } catch (Exception e) {
            logger.error("Error generating response signature: {}", e.getMessage(), e);
            return "";
        }
    }

    public boolean validateRequestSignature(String requestData, String signature) {
        logger.debug("Validating request signature");

        if (!securityConfig.isEnabled()) {
            logger.info("Security is disabled, signature validation skipped");
            return true;
        }

        try {
            String expectedSignature = Base64.getEncoder().encodeToString((requestData + securityConfig.getKey()).getBytes());
            boolean isValid = Objects.equals(signature, expectedSignature);
            
            logger.info("Request signature validation: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error validating request signature: {}", e.getMessage(), e);
            return false;
        }
    }

    public String getSecurityKey() {
        return securityConfig.getKey();
    }

    public boolean isSecurityEnabled() {
        return securityConfig.isEnabled();
    }
}
