package net.ooder.examples.rpaskill.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Authentication Configuration
 * Reads SN authorization information from application.yml
 * 作者：ooderAI agent team   V0.6.0
 */
@Configuration
@ConfigurationProperties(prefix = "auth.sn")
public class AuthConfig {

    private List<String> authorizedSns;
    private boolean validationEnabled;

    public List<String> getAuthorizedSns() {
        return authorizedSns;
    }

    public void setAuthorizedSns(List<String> authorizedSns) {
        this.authorizedSns = authorizedSns;
    }

    public boolean isValidationEnabled() {
        return validationEnabled;
    }

    public void setValidationEnabled(boolean validationEnabled) {
        this.validationEnabled = validationEnabled;
    }
}
