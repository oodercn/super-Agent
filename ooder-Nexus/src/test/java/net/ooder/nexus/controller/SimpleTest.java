package net.ooder.nexus.controller;

import net.ooder.nexus.NexusSpringApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = NexusSpringApplication.class)
@AutoConfigureMockMvc
public class SimpleTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMockMvcInitialization() {
        assertNotNull(mockMvc, "MockMvc should be initialized");
        System.out.println("MockMvc initialized successfully");
    }
}
