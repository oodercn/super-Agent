package net.ooder.nexus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * API 文档配置
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@Configuration
public class ApiDocConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/api-docs", "/api-docs.html");
        registry.addRedirectViewController("/swagger", "/api-docs.html");
        registry.addRedirectViewController("/doc", "/api-docs.html");
        registry.addRedirectViewController("/docs", "/api-docs.html");
    }
}
