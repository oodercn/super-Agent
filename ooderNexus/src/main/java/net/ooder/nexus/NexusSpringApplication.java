package net.ooder.nexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Nexus Spring Boot 应用主类
 */
@SpringBootApplication
@ComponentScan(basePackages = "net.ooder.nexus")
public class NexusSpringApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NexusSpringApplication.class, args);
    }
}
