package net.ooder.nexus.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BasicContextTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testApplicationContextInitialization() {
        assertNotNull(context, "ApplicationContext should be initialized");
        System.out.println("ApplicationContext initialized successfully");
        System.out.println("Application name: " + context.getApplicationName());
        // 打印所有已注册的Bean
        System.out.println("Registered beans:");
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanName.startsWith("net.ooder")) {
                System.out.println("  " + beanName);
            }
        }
    }
}
