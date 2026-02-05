# Independent MCP Agent (Simple Version)

This is a simple implementation of an independent MCP Agent using the Ooder SDK with the skill approach.

## Project Structure

```
independent-mcp-agent-simple/
├── src/
│   └── main/
│       └── java/
│           └── net/
│               └── ooder/
│                   └── mcpagent/
│                       ├── McpAgentApplication.java    # Main application class
│                       ├── skill/
│                       │   └── McpAgentSkill.java     # MCP Agent skill interface
│                       └── skill/impl/
│                           └── McpAgentSkillImpl.java  # MCP Agent skill implementation
├── lib/                                                # Dependencies directory
│   └── agent-sdk-0.6.2.jar                             # Ooder Agent SDK jar
└── README.md                                           # This file
```

## Core Concepts

### 1. McpAgentSkill Interface

This interface defines the core functionality that an MCP Agent should provide:

```java
public interface McpAgentSkill {
    void initialize(AgentSDK sdk);
    void handleMcpRegisterCommand(CommandPacket packet);
    void handleMcpDeregisterCommand(CommandPacket packet);
    void handleMcpHeartbeatCommand(CommandPacket packet);
    void handleMcpStatusCommand(CommandPacket packet);
    void handleMcpDiscoverCommand(CommandPacket packet);
    void handleRouteQueryCommand(CommandPacket packet);
    void handleRouteUpdateCommand(CommandPacket packet);
    void handleEndagentDiscoverCommand(CommandPacket packet);
    void handleEndagentStatusCommand(CommandPacket packet);
    void handleTaskRequestCommand(CommandPacket packet);
    void handleTaskResponseCommand(CommandPacket packet);
    void handleAuthenticateCommand(CommandPacket packet);
    void handleAuthResponseCommand(CommandPacket packet);
    void start();
    void stop();
}
```

### 2. McpAgentSkillImpl Implementation

This class implements the McpAgentSkill interface and provides the actual functionality:

```java
public class McpAgentSkillImpl implements McpAgentSkill, UDPMessageHandler {
    
    private final Map<String, Map<String, Object>> routeAgents = new HashMap<>();
    private final Map<String, Map<String, Object>> endAgents = new HashMap<>();
    
    @Override
    public void initialize(AgentSDK sdk) {
        // Register command handlers
        sdk.registerCommandHandler(CommandType.MCP_REGISTER, this::handleMcpRegisterCommand);
        sdk.registerCommandHandler(CommandType.MCP_DEREGISTER, this::handleMcpDeregisterCommand);
        // Register other command handlers...
    }
    
    @Override
    public void handleMcpRegisterCommand(CommandPacket packet) {
        // Handle MCP register command
        Map<String, Object> params = (Map<String, Object>) packet.getParams();
        String agentId = (String) params.get("agentId");
        String agentType = (String) params.get("agentType");
        
        if ("route".equals(agentType)) {
            routeAgents.put(agentId, params);
        }
        // Send response...
    }
    
    @Override
    public void start() {
        // Start the MCP Agent
    }
    
    @Override
    public void stop() {
        // Stop the MCP Agent
    }
    
    // Implement other methods...
}
```

### 3. Main Application

```java
public class McpAgentApplication {
    public static void main(String[] args) {
        // Configure UDP
        UDPConfig udpConfig = new UDPConfig();
        udpConfig.setHost("0.0.0.0");
        udpConfig.setPort(9876);
        
        // Create AgentSDK
        AgentSDK agentSDK = new AgentSDK("mcp-agent-001", "Independent MCP Agent", "mcp");
        agentSDK.setUdpConfig(udpConfig);
        
        // Initialize and start skill
        McpAgentSkill skill = new McpAgentSkillImpl();
        skill.initialize(agentSDK);
        skill.start();
        
        // Keep application running
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            skill.stop();
        }));
        
        synchronized (McpAgentApplication.class) {
            try {
                McpAgentApplication.class.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
```

## How to Run

1. Place the `agent-sdk-0.6.2.jar` file in the `lib/` directory
2. Compile the Java files:
   ```bash
   javac -cp "lib/*" -d out src/main/java/net/ooder/mcpagent/**/*.java
   ```
3. Run the application:
   ```bash
   java -cp "out:lib/*" net.ooder.mcpagent.McpAgentApplication
   ```

## Key Features

1. **Command-driven architecture**: Uses the command pattern to handle MCP Agent commands
2. **Skill-based approach**: Implements MCP functionality as a skill that can be initialized and started
3. **UDP communication**: Uses the Ooder SDK's UDP capabilities for communication
4. **Thread-safe**: Properly handles multiple commands and concurrent requests
5. **Graceful shutdown**: Cleanly stops the agent when the application is shut down

## MCP Agent Responsibilities

- **Route Management**: Register, deregister, and query route agents
- **End Agent Management**: Discover and monitor end agents
- **Task Management**: Forward and process task requests
- **Heartbeat Monitoring**: Maintain connections with other agents
- **Status Reporting**: Provide status information to other agents
- **Authentication**: Handle authentication requests from other agents

## Command Reference

| Command Type | Description |
|--------------|-------------|
| MCP_REGISTER | Register a new agent with the MCP |
| MCP_DEREGISTER | Deregister an existing agent |
| MCP_HEARTBEAT | Maintain connection with the MCP |
| MCP_STATUS | Query the status of the MCP |
| MCP_DISCOVER | Discover the MCP |
| MCP_ROUTE_QUERY | Query route information |
| MCP_ROUTE_UPDATE | Update route information |
| MCP_ENDAGENT_DISCOVER | Discover end agents |
| MCP_ENDAGENT_STATUS | Query end agent status |
| MCP_TASK_REQUEST | Request a task to be performed |
| MCP_TASK_RESPONSE | Respond to a task request |
| MCP_AUTHENTICATE | Authenticate with the MCP |
| MCP_AUTH_RESPONSE | Respond to an authentication request |

## Implementation Notes

1. **AgentSDK Initialization**: The AgentSDK is the core component that handles UDP communication and command processing.

2. **Command Handlers**: Each command type is handled by a specific method, making the code modular and easy to extend.

3. **State Management**: The MCP Agent maintains state information about route agents and end agents.

4. **Error Handling**: Proper error handling ensures that the agent continues to function even when encountering errors.

5. **Concurrency**: The implementation is designed to handle multiple commands concurrently.

## Extending the MCP Agent

To extend the MCP Agent:

1. Implement additional command handlers in the `McpAgentSkillImpl` class
2. Add new methods to the `McpAgentSkill` interface as needed
3. Add new functionality to the main application class
4. Configure the UDP settings based on your network environment

## Troubleshooting

### Common Issues

1. **UDP Port Already in Use**: Change the UDP port in the application configuration
2. **AgentSDK Not Found**: Ensure the agent-sdk jar is in the lib directory
3. **Command Not Handled**: Ensure the command handler is properly registered
4. **Connection Issues**: Check network settings and firewall configurations

## Conclusion

This simple implementation demonstrates the core concepts of building an independent MCP Agent using the Ooder SDK with the skill approach. By following this structure, you can create a fully functional MCP Agent that can communicate with other agents in the Ooder ecosystem.

For a more complete implementation, refer to the `examples/mcp-agent` directory in the Ooder SDK repository.
