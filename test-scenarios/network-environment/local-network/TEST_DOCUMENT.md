# Local Network Test Scenario Documentation (Nexus)

## 1. Scenario Overview

This test scenario is designed to validate the Ooder Agent SDK functionality in a local network environment for the Nexus version, where all agents are running on the same machine or within the same localhost network.

### 1.1 Test Objectives
- Validate service discovery in a local network environment (Nexus)
- Test agent communication within the same machine (Nexus)
- Verify task execution and scene management (Nexus)
- Ensure proper error handling and fault tolerance (Nexus)
- Test performance and reliability in a low-latency environment (Nexus)

### 1.2 Test Environment

| Component | Type | Port | Description |
|-----------|------|------|-------------|
| route-agent | Route Agent | 8100 | Route control plane agent (Nexus) |
| end-agent-1 | End Agent | 8101 | End node agent 1 (Nexus) |
| end-agent-2 | End Agent | 8102 | End node agent 2 (Nexus) |
| UDP Communication | Network | 9020-9022 | Agent communication ports (Nexus) |

## 2. Test Environment Setup

### 2.1 Prerequisites
- JDK 8+
- Maven 3.6+
- Spring Boot 2.7.0+
- Agent SDK 0.6.5 Nexus
- Skill VFS 0.6.5 Nexus
- Local network connection (localhost)

### 2.2 Deployment Steps

1. **Build the Agent SDK (Nexus)**
   ```bash
   cd ../../../../agent-sdk
   mvn clean package -DskipTests
   ```

2. **Build the Skill VFS (Nexus)**
   ```bash
   cd ../../../../skill-vfs
   mvn clean package -DskipTests
   ```

3. **Build and Start Route Agent (Nexus)**
   ```bash
   cd route-agent
   mvn clean package -DskipTests
   java -jar target\route-agent-local-1.0.0-test.jar
   ```

4. **Build and Start End Agent 1 (Nexus)**
   ```bash
   cd ../end-agent-1
   mvn clean package -DskipTests
   java -jar target\end-agent-1-local-1.0.0-test.jar
   ```

5. **Build and Start End Agent 2 (Nexus)**
   ```bash
   cd ../end-agent-2
   mvn clean package -DskipTests
   java -jar target\end-agent-2-local-1.0.0-test.jar
   ```

## 3. Test Cases

### 3.1 Service Discovery Test Cases

#### 3.1.1 Local Discovery Test
- **Test ID**: LD-001
- **Test Objective**: Verify service discovery in local network
- **Test Steps**: 
  1. Start Route Agent
  2. Start End Agent 1 and End Agent 2
  3. Check logs for discovery success messages
- **Expected Result**: All agents successfully discover each other

#### 3.1.2 Discovery Speed Test
- **Test ID**: LD-002
- **Test Objective**: Measure discovery speed in local network
- **Test Steps**: 
  1. Start Route Agent
  2. Start End Agent and measure time to discovery
  3. Repeat multiple times and calculate average
- **Expected Result**: Discovery completes within 1 second

### 3.2 Communication Test Cases

#### 3.2.1 Local Communication Test
- **Test ID**: LC-001
- **Test Objective**: Verify communication between agents in local network
- **Test Steps**: 
  1. Start all agents
  2. Send commands between all agent pairs
  3. Check response times and success rates
- **Expected Result**: All communications succeed with low latency

#### 3.2.2 Message Throughput Test
- **Test ID**: LC-002
- **Test Objective**: Measure message throughput in local network
- **Test Steps**: 
  1. Start all agents
  2. Send multiple messages in rapid succession
  3. Measure messages per second
- **Expected Result**: Throughput > 100 messages/second

### 3.3 Task Execution Test Cases

#### 3.3.1 Local Task Execution Test
- **Test ID**: LT-001
- **Test Objective**: Verify task execution in local network
- **Test Steps**: 
  1. Start all agents
  2. Send tasks from Route Agent to End Agents
  3. Check task execution success rate
- **Expected Result**: All tasks execute successfully

#### 3.3.2 Parallel Task Test
- **Test ID**: LT-002
- **Test Objective**: Test parallel task execution
- **Test Steps**: 
  1. Start all agents
  2. Send multiple tasks simultaneously
  3. Check parallel execution and completion
- **Expected Result**: Tasks execute in parallel without interference

### 3.4 Reliability Test Cases

#### 3.4.1 Local Network Stability Test
- **Test ID**: LR-001
- **Test Objective**: Verify system stability in local network
- **Test Steps**: 
  1. Start all agents
  2. Run system for extended period (24 hours)
  3. Periodically send tasks and check status
- **Expected Result**: System remains stable with no errors

#### 3.4.2 Agent Restart Test
- **Test ID**: LR-002
- **Test Objective**: Verify agent restart behavior in local network
- **Test Steps**: 
  1. Start all agents
  2. Stop and restart End Agent
  3. Check re-discovery and re-registration
- **Expected Result**: Agent successfully restarts and rejoins the network

## 4. Test Execution

### 4.1 Test Commands

#### 4.1.1 Start All Agents (Nexus)
```bash
# Start Route Agent (Nexus)
cd route-agent
java -jar target\route-agent-local-1.0.0-test.jar

# Start End Agent 1 (Nexus)
cd ../end-agent-1
java -jar target\end-agent-1-local-1.0.0-test.jar

# Start End Agent 2 (Nexus)
cd ../end-agent-2
java -jar target\end-agent-2-local-1.0.0-test.jar
```

#### 4.1.2 Test Service Discovery (Nexus)
```bash
# Check Route Agent logs for End Agent discovery
# Look for messages like: "End Agent discovered: end-agent-1" (Nexus)

# Check End Agent logs for Route Agent discovery
# Look for messages like: "Route Agent discovered: route-agent" (Nexus)
```

#### 4.1.3 Test Task Execution (Nexus)
```bash
# Using curl to send task from Route Agent to End Agent 1 (Nexus)
curl -X POST http://localhost:8100/api/v1/task -H "Content-Type: application/json" -d '{"taskId":"test-task-001","targetAgent":"end-agent-1","action":"execute","data":"test data"}'

# Using curl to send task from Route Agent to End Agent 2 (Nexus)
curl -X POST http://localhost:8100/api/v1/task -H "Content-Type: application/json" -d '{"taskId":"test-task-002","targetAgent":"end-agent-2","action":"execute","data":"test data"}'
```

### 4.2 Monitoring

#### 4.2.1 Log Monitoring (Nexus)
- **Route Agent Logs**: `route-agent/logs/skill-vfs.log`
- **End Agent 1 Logs**: `end-agent-1/logs/skill-vfs.log`
- **End Agent 2 Logs**: `end-agent-2/logs/skill-vfs.log`

#### 4.2.2 Health Checks (Nexus)
```bash
# Check Route Agent health (Nexus)
curl http://localhost:8100/api/v1/health

# Check End Agent 1 health (Nexus)
curl http://localhost:8101/api/v1/health

# Check End Agent 2 health (Nexus)
curl http://localhost:8102/api/v1/health
```

## 5. Test Results

### 5.1 Success Criteria (Nexus)
- All agents successfully discover each other in local network (Nexus)
- Communication between agents is fast and reliable in Nexus
- Tasks execute successfully with low latency in Nexus
- System remains stable over extended periods in Nexus
- All test cases pass for Nexus version
- Heartbeat mechanism functions correctly in Nexus
- Skill registration and discovery work properly in Nexus

### 5.2 Failure Handling (Nexus)
- **Discovery Failure**: Check localhost connectivity and firewall settings for Nexus
- **Communication Issues**: Verify port configurations and network settings for Nexus
- **Task Execution Errors**: Check task parameters and agent capabilities for Nexus
- **Stability Issues**: Monitor system resources and check for memory leaks in Nexus
- **Heartbeat Issues**: Check network stability and service availability for Nexus

## 6. Test Cleanup

### 6.1 Stopping Agents
1. Press `Ctrl+C` in each terminal to stop the agents
2. Verify all processes have exited

### 6.2 Cleanup Steps (Nexus)
```bash
# Clean up build artifacts
cd route-agent && mvn clean
cd ../end-agent-1 && mvn clean
cd ../end-agent-2 && mvn clean

# Remove log files (optional)
rm -rf route-agent/logs/*
rm -rf end-agent-1/logs/*
rm -rf end-agent-2/logs/*
```

## 7. Troubleshooting

### 7.1 Common Issues (Nexus)

| Issue | Possible Cause | Solution |
|-------|---------------|----------|
| Discovery fails | Localhost firewall blocking | Allow localhost connections through firewall for Nexus |
| Port conflict | Other services using ports | Change service ports in application.yml for Nexus |
| Communication error | Network loopback issues | Check localhost network configuration for Nexus |
| Task execution failure | Agent initialization issues | Verify agent startup order and dependencies for Nexus |
| Heartbeat lost | Network instability or service overload | Check network stability and reduce service load for Nexus |

### 7.2 Debug Commands (Nexus)

#### 7.2.1 Check Local Network Connectivity
```bash
# Test localhost connectivity
ping localhost

# Check port availability
netstat -ano | findstr :8100
netstat -ano | findstr :8101
netstat -ano | findstr :8102
netstat -ano | findstr :9020
netstat -ano | findstr :9021
netstat -ano | findstr :9022
```

#### 7.2.2 Check Agent Status (Nexus)
```bash
# Check Route Agent status (Nexus)
curl http://localhost:8100/api/v1/info

# Check End Agent 1 status (Nexus)
curl http://localhost:8101/api/v1/info

# Check End Agent 2 status (Nexus)
curl http://localhost:8102/api/v1/info
```

## 8. Conclusion

This test scenario provides a comprehensive validation of the Ooder Agent SDK functionality in a local network environment for the Nexus version. By following the setup instructions and executing the test cases, you can verify that the system works correctly in a low-latency, local network setting with Nexus-specific features.

### 8.1 Test Coverage (Nexus)
- ✅ Service discovery in local network (Nexus)
- ✅ Agent communication within localhost (Nexus)
- ✅ Task execution and scene management (Nexus)
- ✅ Performance and throughput (Nexus)
- ✅ Reliability and stability (Nexus)
- ✅ Heartbeat mechanism (Nexus)
- ✅ Skill registration and discovery (Nexus)

### 8.2 Next Steps (Nexus)
After completing this test scenario, you can proceed to test other network environments with Nexus features:
- **LAN Network (Nexus)**: Testing across multiple machines in the same LAN with Nexus features
- **Intranet Network (Nexus)**: Testing across different subnets within an intranet with Nexus features

The local network test provides the baseline for understanding how the Ooder Agent SDK Nexus performs in optimal network conditions with enhanced features and capabilities.

### 8.3 Nexus-Specific Features Tested
- ✅ Enhanced service discovery in local network
- ✅ Improved agent communication within localhost
- ✅ Advanced task execution and scene management
- ✅ Robust heartbeat mechanism
- ✅ Seamless integration with Skill VFS 0.6.5 Nexus

The Nexus version of the local network test demonstrates the improved performance and reliability of the Ooder Agent SDK in optimal network conditions.