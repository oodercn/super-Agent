package net.ooder.skill.config.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "net.ooder.skill.config")
@ConditionalOnProperty(name = "skill.config.enabled", havingValue = "true", matchIfMissing = true)
public class ConfigAutoConfiguration {

}
