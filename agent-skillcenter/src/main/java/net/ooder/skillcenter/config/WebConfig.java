package net.ooder.skillcenter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于配置静态资源访问路径
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源访问路径
        registry.addResourceHandler("/console/**")
                .addResourceLocations("classpath:/static/console/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 配置欢迎页面，当访问/console/时，重定向到/console/index.html
        registry.addViewController("/console/").setViewName("forward:/console/index.html");
        // 配置根路径重定向，当访问/时，重定向到/console/
        registry.addViewController("/").setViewName("forward:/console/");
    }
}
