package net.ooder.nexus.controller;

import net.ooder.nexus.NexusSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContextTest {

    @Test
    public void testApplicationContextInitialization() {
        try {
            // 手动创建Spring Boot应用上下文
            ApplicationContext context = SpringApplication.run(NexusSpringApplication.class);
            assertNotNull(context, "ApplicationContext should be initialized");
            System.out.println("ApplicationContext initialized successfully");
            System.out.println("Application name: " + context.getApplicationName());
            // 关闭上下文
            SpringApplication.exit(context);
        } catch (Exception e) {
            System.err.println("Error initializing ApplicationContext:");
            e.printStackTrace();
            throw e;
        }
    }
}
