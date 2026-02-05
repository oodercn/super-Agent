# Agent Hierarchy Test Scenario Documentation (Nexus)

## 1. Scenario Overview

This test scenario is designed to validate the complete agent hierarchy functionality of the Ooder Agent SDK for the Nexus version, including McpAgent (Master Control Plane), RouteAgent (Route Control Plane), and EndAgent (End Node) interactions with Nexus-specific features.

### 1.1 Test Objectives
- Validate the complete agent hierarchy setup in Nexus
- Test communication between different agent types in Nexus
- Verify agent registration and coordination in Nexus
- Ensure proper task execution and command routing in Nexus
- Test error handling and fault tolerance in Nexus

### 1.2 Test Environment

| Component | Type | Port | Description |
|-----------|------|------|-------------|
| mcp-agent | MCP Agent | 8090 | Master control plane agent (Nexus) |
| route-agent | Route Agent | 8091 | Route control plane agent (Nexus) |
| end-agent | End Agent | 8092 | End node agent (Nexus) |
| UDP Communication | Network | 9010-9012 | Agent communication ports (Nexus) |

## 2. Test Environment Setup

### 2.1 Prerequisites
- JDK 8+
- Maven 3.6+
- Spring Boot 2.7.0+
- Agent SDK 0.6.5 Nexus
- Skill VFS 0.6.5 Nexus

### 2.2 Deployment Steps

1. **Build the Agent SDK (Nexus)**
   ```bash
   cd ../../../agent-sdk
   mvn clean package -DskipTests
   ```

2. **Build the Skill VFS (Nexus)**
   ```bash
   cd ../../../skill-vfs
   mvn clean package -DskipTests
   ```

3. **Build and Start MCP Agent (Nexus)**
   ```bash
   cd mcp-agent
   mvn clean package -DskipTests
   java -jar target\mcp-agent-hierarchy-1.0.0-test.jar
   ```

4. **Build and Start Route Agent (Nexus)**
   ```bash
   cd ../route-agent
   mvn clean package -DskipTests
   java -jar target\route-agent-hierarchy-1.0.0-test.jar
   ```

5. **Build and Start End Agent (Nexus)**
   ```bash
   cd ../end-agent
   mvn clean package -DskipTests
   java -jar target\end-agent-hierarchy-1.0.0-test.jar
   ```

## 3. Test Cases

### 3.1 Agent Registration Test Cases

#### 3.1.1 Route Agent Registration Test
- **Test ID**: AR-001
- **Test Objective**: Verify Route Agent can register with MCP Agent
- **Test Steps**: 
  1. Start MCP Agent
  2. Start Route Agent
  3. Check logs for registration success messages
- **Expected Result**: Route Agent successfully registers with MCP Agent

#### 3.1.2 End Agent Registration Test
- **Test ID**: AR-002
- **Test Objective**: Verify End Agent can register with Route Agent
- **Test Steps**: 
  1. Start MCP Agent and Route Agent
  2. Start End Agent
  3. Check logs for registration success messages
- **Expected Result**: End Agent successfully registers with Route Agent

### 3.2 Communication Test Cases

#### 3.2.1 MCP to Route Agent Communication
- **Test ID**: CM-001
- **Test Objective**: Verify MCP Agent can communicate with Route Agent
- **Test Steps**: 
  1. Start both agents
  2. Send command from MCP Agent to Route Agent
  3. Check response and logs
- **Expected Result**: Command is successfully sent and executed

#### 3.2.2 Route to End Agent Communication
- **Test ID**: CM-002
- **Test Objective**: Verify Route Agent can communicate with End Agent
- **Test Steps**: 
  1. Start all agents
  2. Send command from Route Agent to End Agent
  3. Check response and logs
- **Expected Result**: Command is successfully sent and executed

#### 3.2.3 End to Route Agent Communication
- **Test ID**: CM-003
- **Test Objective**: Verify End Agent can communicate with Route Agent
- **Test Steps**: 
  1. Start all agents
  2. Send command from End Agent to Route Agent
  3. Check response and logs
- **Expected Result**: Command is successfully sent and executed

### 3.3 Task Execution Test Cases

#### 3.3.1 Task Routing Test
- **Test ID**: TE-001
- **Test Objective**: Verify task routing through the hierarchy
- **Test Steps**: 
  1. Start all agents
  2. Send task from MCP Agent to End Agent via Route Agent
  3. Check task execution and response
- **Expected Result**: Task is successfully routed and executed

#### 3.3.2 Multiple End Agent Test
- **Test ID**: TE-002
- **Test Objective**: Verify task distribution to multiple End Agents
- **Test Steps**: 
  1. Start MCP and Route Agents
  2. Start multiple End Agents
  3. Send tasks from MCP Agent
  4. Check task distribution and execution
- **Expected Result**: Tasks are evenly distributed and executed

### 3.4 Error Handling Test Cases

#### 3.4.1 Agent Failure Test
- **Test ID**: EH-001
- **Test Objective**: Verify system behavior when an agent fails
- **Test Steps**: 
  1. Start all agents
  2. Stop Route Agent
  3. Check MCP Agent's response to failure
  4. Restart Route Agent and check re-registration
- **Expected Result**: System detects failure and recovers when agent restarts

#### 3.4.2 Network Failure Test
- **Test ID**: EH-002
- **Test Objective**: Verify system behavior during network failures
- **Test Steps**: 
  1. Start all agents
  2. Simulate network disruption
  3. Check system response and recovery
- **Expected Result**: System detects network issues and recovers when network is restored

## 4. Test Execution

### 4.1 Test Commands

#### 4.1.1 Start All Agents (Nexus)
```bash
# Start MCP Agent (Nexus)
cd mcp-agent
java -jar target\mcp-agent-hierarchy-1.0.0-test.jar

# Start Route Agent (Nexus)
cd ../route-agent
java -jar target\route-agent-hierarchy-1.0.0-test.jar

# Start End Agent (Nexus)
cd ../end-agent
java -jar target\end-agent-hierarchy-1.0.0-test.jar
```

#### 4.1.2 Test Agent Registration (Nexus)
```bash
# Check MCP Agent logs for Route Agent registration
# Look for messages like: "Route Agent registered successfully: route-agent-hierarchy-001" (Nexus)

# Check Route Agent logs for End Agent registration
# Look for messages like: "End Agent registered successfully: end-agent-hierarchy-001" (Nexus)
```

#### 4.1.3 Test Task Execution (Nexus)
```bash
# Using curl to send task from MCP Agent (Nexus)
curl -X POST http://localhost:8090/api/v1/task -H "Content-Type: application/json" -d '{"taskId":"test-task-001","targetAgent":"end-agent-hierarchy-001","action":"execute","data":"test data"}'
```

### 4.2 Monitoring

#### 4.2.1 Log Monitoring (Nexus)
- **MCP Agent Logs**: `mcp-agent/logs/skill-vfs.log`
- **Route Agent Logs**: `route-agent/logs/skill-vfs.log`
- **End Agent Logs**: `end-agent/logs/skill-vfs.log`

#### 4.2.2 Health Checks (Nexus)
```bash
# Check MCP Agent health (Nexus)
curl http://localhost:8090/api/v1/health

# Check Route Agent health (Nexus)
curl http://localhost:8091/api/v1/health

# Check End Agent health (Nexus)
curl http://localhost:8092/api/v1/health
```

## 5. Test Results

### 5.1 Success Criteria (Nexus)
- All agents successfully start and register in Nexus
- Communication between all agent types works correctly in Nexus
- Tasks are successfully routed and executed in Nexus
- System recovers from agent failures in Nexus
- No network errors or timeouts in Nexus
- All test cases pass for Nexus version
- Agent hierarchy functions correctly with Nexus features

### 5.2 Failure Handling (Nexus)
- **Registration Failure**: Check network connectivity and agent configurations for Nexus
- **Communication Failure**: Verify firewall settings and port configurations for Nexus
- **Task Execution Failure**: Check task parameters and agent capabilities for Nexus
- **Recovery Failure**: Verify agent restart logic and network stability for Nexus
- **Heartbeat Issues**: Check network stability and service availability for Nexus

## 6. Test Cleanup

### 6.1 Stopping Agents
1. Press `Ctrl+C` in each terminal to stop the agents
2. Verify all processes have exited

### 6.2 Cleanup Steps (Nexus)
```bash
# Clean up build artifacts
cd mcp-agent && mvn clean
cd ../route-agent && mvn clean
cd ../end-agent && mvn clean

# Remove log files (optional)
rm -rf mcp-agent/logs/*
rm -rf route-agent/logs/*
rm -rf end-agent/logs/*
```

## 7. Troubleshooting

### 7.1 Common Issues (Nexus)

| Issue | Possible Cause | Solution |
|-------|---------------|----------|
| Agent registration fails | Network connectivity issues | Check network settings and firewall rules for Nexus |
| Port conflict | Other services using the same ports | Change service ports in application.yml for Nexus |
| Communication timeout | Network latency or agent overload | Increase timeout settings or reduce load for Nexus |
| Task execution failure | Invalid task parameters | Check task format and required parameters for Nexus |
| Agent restart failure | Improper shutdown | Ensure agents are properly stopped before restarting in Nexus |
| Heartbeat lost | Network instability or service overload | Check network stability and reduce service load for Nexus |

### 7.2 Debug Commands (Nexus)

#### 7.2.1 Check Agent Status (Nexus)
```bash
# Check MCP Agent status (Nexus)
curl http://localhost:8090/api/v1/info

# Check Route Agent status (Nexus)
curl http://localhost:8091/api/v1/info

# Check End Agent status (Nexus)
curl http://localhost:8092/api/v1/info
```

#### 7.2.2 Check Network Connectivity (Nexus)
```bash
# Test TCP connectivity between agents (Nexus)
telnet localhost 8090
telnet localhost 8091
telnet localhost 8092

# Test UDP connectivity (Nexus)
nc -u localhost 9010
nc -u localhost 9011
nc -u localhost 9012
```

## 8. Conclusion

This test scenario provides a comprehensive validation of the complete agent hierarchy functionality in the Ooder Agent SDK for the Nexus version. By following the setup instructions and executing the test cases, you can verify that the entire agent hierarchy works correctly in a controlled environment with Nexus-specific features.

### 8.1 Test Coverage (Nexus)
- ✅ Agent hierarchy setup (Nexus)
- ✅ Agent registration and discovery (Nexus)
- ✅ Communication between agent types (Nexus)
- ✅ Task routing and execution (Nexus)
- ✅ Error handling and fault tolerance (Nexus)
- ✅ System recovery from failures (Nexus)
- ✅ Heartbeat mechanism (Nexus)

### 8.2 Next Steps (Nexus)
After completing this test scenario, you can proceed to test other Nexus-specific scenarios such as:
- **Network Environment (Nexus)**: Testing across different network environments with Nexus features
- **Performance Test (Nexus)**: Testing performance under load with Nexus optimizations
- **Reliability Test (Nexus)**: Testing system reliability under various conditions with Nexus fault tolerance

The agent hierarchy test provides the foundation for understanding how the entire Ooder Agent SDK Nexus ecosystem works together to execute tasks and manage distributed systems with enhanced features and capabilities.

### 8.3 Nexus-Specific Features Tested
- ✅ Enhanced agent registration and discovery
- ✅ Improved task routing and execution
- ✅ Advanced error handling and fault tolerance
- ✅ Robust heartbeat mechanism
- ✅ Seamless integration with Skill VFS 0.6.5 Nexus

The Nexus version of the agent hierarchy test demonstrates the improved capabilities and reliability of the Ooder Agent SDK in managing complex distributed systems.