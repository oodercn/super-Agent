package net.ooder.skill.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TravelNotificationSkillApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelNotificationSkillApplication.class, args);
    }

    @Configuration
    public static class StaticResourceConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // 配置静态资源处理
            registry.addResourceHandler("/static/**")
                    .addResourceLocations("classpath:/static/")
                    .setCachePeriod(0);
            
            // 配置根路径重定向到反馈表单
            registry.addResourceHandler("/")
                    .addResourceLocations("classpath:/static/feedback-form.html")
                    .setCachePeriod(0);
        }
    }

}
