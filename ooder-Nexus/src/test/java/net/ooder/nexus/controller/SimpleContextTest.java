package net.ooder.nexus.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SimpleContextTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testApplicationContextInitialization() {
        assertNotNull(context, "ApplicationContext should be initialized");
        System.out.println("ApplicationContext initialized successfully");
        System.out.println("Application name: " + context.getApplicationName());
    }
}
