package net.ooder.examples.mcpagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class McpAgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpAgentApplication.class, args);
        System.out.println("MCP Agent started successfully");
    }
}
