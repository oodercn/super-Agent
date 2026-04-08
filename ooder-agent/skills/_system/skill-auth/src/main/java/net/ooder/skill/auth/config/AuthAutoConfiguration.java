package net.ooder.skill.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "net.ooder.skill.auth")
@ConditionalOnProperty(name = "skill.auth.enabled", havingValue = "true", matchIfMissing = true)
public class AuthAutoConfiguration {

}
