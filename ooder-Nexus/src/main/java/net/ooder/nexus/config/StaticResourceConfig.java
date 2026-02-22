package net.ooder.nexus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * 静态资源配置类
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源处理
        registry.addResourceHandler("/console/**")
                .addResourceLocations("classpath:/static/console/")
                .setCachePeriod(0);
        
        // 配置根路径下的CSS资源处理，映射到console/css目录
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/console/css/")
                .setCachePeriod(0);
        
        // 配置根路径下的JS资源处理，映射到console/js目录
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/console/js/")
                .setCachePeriod(0);
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 配置控制台根路径重定向到 index.html
        registry.addViewController("/console/")
                .setViewName("forward:/console/index.html");
        
        // 配置应用根路径重定向到控制台
        registry.addViewController("/")
                .setViewName("forward:/console/index.html");
        
        // 配置 index.html 路径重定向到控制台
        registry.addViewController("/index.html")
                .setViewName("forward:/console/index.html");
    }
}
