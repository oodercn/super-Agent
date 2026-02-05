package net.ooder.examples.rpaskill.config;

import org.springframework.context.annotation.Configuration;

/**
 * End Agent Configuration
 * Configures endAgent role information
 * 作者：ooderAI agent team   V0.6.0
 */
@Configuration
public class EndAgentConfig {

    private static final String ROLE_NAME = "endAgent";
    private static final String DESCRIPTION = "End Agent for RPA Operations";
    private static final String VERSION = "1.0.0";

    public String getRoleName() {
        return ROLE_NAME;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getVersion() {
        return VERSION;
    }
}
