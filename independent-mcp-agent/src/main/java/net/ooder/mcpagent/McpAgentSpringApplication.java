package net.ooder.mcpagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * MCP Agent Spring Boot 应用程序
 */
@SpringBootApplication
@ComponentScan(basePackages = {"net.ooder.mcpagent", "net.ooder.mcp.agent"})
public class McpAgentSpringApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(McpAgentSpringApplication.class, args);
    }
}