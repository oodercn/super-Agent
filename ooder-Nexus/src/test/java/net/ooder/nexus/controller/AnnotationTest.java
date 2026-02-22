package net.ooder.nexus.controller;

import net.ooder.nexus.NexusSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = NexusSpringApplication.class)
public class AnnotationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testApplicationContextInjection() {
        System.out.println("Testing ApplicationContext injection with @Autowired...");
        if (context == null) {
            System.out.println("ERROR: ApplicationContext is null");
        } else {
            System.out.println("SUCCESS: ApplicationContext is injected");
            System.out.println("Application name: " + context.getApplicationName());
            System.out.println("Number of beans: " + context.getBeanDefinitionCount());
        }
        assertNotNull(context, "ApplicationContext should be injected");
    }
}
