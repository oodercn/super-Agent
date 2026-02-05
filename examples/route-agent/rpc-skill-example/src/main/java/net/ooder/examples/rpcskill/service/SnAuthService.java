package net.ooder.examples.rpaskill.service;

import net.ooder.examples.rpaskill.config.AuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SN Authorization Service
 * Validates if a SN is authorized to access the service
 * 作者：ooderAI agent team   V0.6.0
 */
@Service
public class SnAuthService {

    private final AuthConfig authConfig;

    @Autowired
    public SnAuthService(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    /**
     * Validates if the provided SN is authorized
     * @param sn Serial Number to validate
     * @return true if authorized, false otherwise
     */
    public boolean validateSn(String sn) {
        // If validation is disabled, return true
        if (!authConfig.isValidationEnabled()) {
            return true;
        }

        // Check if SN is null or empty
        if (sn == null || sn.trim().isEmpty()) {
            return false;
        }

        // Check if SN is in the authorized list
        return authConfig.getAuthorizedSns() != null && authConfig.getAuthorizedSns().contains(sn);
    }

    /**
     * Validates if the provided SN is authorized and throws exception if not
     * @param sn Serial Number to validate
     * @throws UnauthorizedException if SN is not authorized
     */
    public void validateSnWithException(String sn) throws UnauthorizedException {
        if (!validateSn(sn)) {
            throw new UnauthorizedException("Invalid or unauthorized SN: " + sn);
        }
    }

    /**
     * Unauthorized Exception Class
     */
    public static class UnauthorizedException extends Exception {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
