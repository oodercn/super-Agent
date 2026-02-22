package net.ooder.nexus.controller;

import net.ooder.nexus.NexusSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BasicTest {

    @Test
    public void testSpringBootApplicationStartup() {
        try {
            System.out.println("Starting Spring Boot application...");
            ApplicationContext context = SpringApplication.run(NexusSpringApplication.class);
            assertNotNull(context, "ApplicationContext should be initialized");
            System.out.println("Spring Boot application started successfully");
            System.out.println("Application name: " + context.getApplicationName());
            System.out.println("Number of beans: " + context.getBeanDefinitionCount());
            
            // 尝试获取一些关键Bean
            System.out.println("\nTrying to get key beans:");
            try {
                Object agentSDK = context.getBean("agentSDK");
                System.out.println("agentSDK bean: " + (agentSDK != null ? "found" : "not found"));
            } catch (Exception e) {
                System.out.println("Error getting agentSDK bean: " + e.getMessage());
            }
            
            try {
                Object nexusSkill = context.getBean("nexusSkill");
                System.out.println("nexusSkill bean: " + (nexusSkill != null ? "found" : "not found"));
            } catch (Exception e) {
                System.out.println("Error getting nexusSkill bean: " + e.getMessage());
            }
            
            try {
                Object nexusManager = context.getBean("nexusManager");
                System.out.println("nexusManager bean: " + (nexusManager != null ? "found" : "not found"));
            } catch (Exception e) {
                System.out.println("Error getting nexusManager bean: " + e.getMessage());
            }
            
            // 关闭上下文
            SpringApplication.exit(context);
            System.out.println("\nSpring Boot application stopped successfully");
        } catch (Exception e) {
            System.err.println("Error starting Spring Boot application:");
            e.printStackTrace();
            throw e;
        }
    }
}
