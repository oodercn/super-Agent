# LAN Network Test Scenario Documentation (Nexus)

## 1. Scenario Overview

This test scenario is designed to validate the Ooder Agent SDK functionality in a Local Area Network (LAN) environment for the Nexus version, where agents are running on different machines within the same physical network.

### 1.1 Test Objectives
- Validate service discovery across multiple machines in a LAN (Nexus)
- Test agent communication between different hosts (Nexus)
- Verify task execution and scene management in a distributed environment (Nexus)
- Ensure proper error handling and fault tolerance in a LAN (Nexus)
- Test performance and reliability in a typical office/home network (Nexus)

### 1.2 Test Environment

| Component | Type | Port | Description |
|-----------|------|------|-------------|
| route-agent | Route Agent | 8110 | Route control plane agent (Machine 1, Nexus) |
| end-agent-1 | End Agent | 8111 | End node agent (Machine 2, Nexus) |
| end-agent-2 | End Agent | 8112 | End node agent (Machine 3, Nexus) |
| UDP Communication | Network | 9030-9032 | Agent communication ports (Nexus) |
| Network | LAN | - | WIFI or Ethernet LAN |

## 2. Test Environment Setup

### 2.1 Prerequisites
- JDK 8+ on all machines
- Maven 3.6+ on all machines
- Spring Boot 2.7.0+ on all machines
- Agent SDK 0.6.5 Nexus
- Skill VFS 0.6.5 Nexus
- LAN network connection (WIFI or Ethernet)
- Multiple machines (minimum 3) in the same LAN

### 2.2 Deployment Steps

1. **Prepare All Machines**
   - Install JDK, Maven, and Spring Boot on all machines
   - Ensure all machines are connected to the same LAN
   - Verify network connectivity between machines

2. **Build the Agent SDK on Machine 1 (Nexus)**
   ```bash
   cd ../../../../agent-sdk
   mvn clean package -DskipTests
   ```

3. **Build the Skill VFS on Machine 1 (Nexus)**
   ```bash
   cd ../../../../skill-vfs
   mvn clean package -DskipTests
   ```

4. **Copy Build Artifacts to Other Machines**
   ```bash
   # Copy agent-sdk-0.6.5-nexus.jar to Machine 2 and 3
   # Copy skill-vfs-0.6.5-nexus.jar to Machine 2 and 3
   ```

5. **Build and Start Route Agent on Machine 1 (Nexus)**
   ```bash
   cd route-agent
   mvn clean package -DskipTests
   java -jar target\route-agent-lan-1.0.0-test.jar
   ```

6. **Build and Start End Agent 1 on Machine 2 (Nexus)**
   ```bash
   cd end-agent-1
   mvn clean package -DskipTests
   java -jar target\end-agent-1-lan-1.0.0-test.jar
   ```

7. **Build and Start End Agent 2 on Machine 3 (Nexus)**
   ```bash
   cd end-agent-2
   mvn clean package -DskipTests
   java -jar target\end-agent-2-lan-1.0.0-test.jar
   ```

## 3. Test Cases

### 3.1 Service Discovery Test Cases

#### 3.1.1 LAN Discovery Test
- **Test ID**: LAD-001
- **Test Objective**: Verify service discovery across multiple machines in a LAN
- **Test Steps**: 
  1. Start Route Agent on Machine 1
  2. Start End Agent 1 on Machine 2
  3. Start End Agent 2 on Machine 3
  4. Check logs for discovery success messages
- **Expected Result**: All agents successfully discover each other across the LAN

#### 3.1.2 Discovery Speed Test
- **Test ID**: LAD-002
- **Test Objective**: Measure discovery speed in a LAN environment
- **Test Steps**: 
  1. Start Route Agent on Machine 1
  2. Start End Agent on Machine 2 and measure time to discovery
  3. Repeat multiple times and calculate average
- **Expected Result**: Discovery completes within 3 seconds

### 3.2 Communication Test Cases

#### 3.2.1 LAN Communication Test
- **Test ID**: LAC-001
- **Test Objective**: Verify communication between agents across different machines
- **Test Steps**: 
  1. Start all agents
  2. Send commands between all agent pairs
  3. Check response times and success rates
- **Expected Result**: All communications succeed with reasonable latency

#### 3.2.2 Message Throughput Test
- **Test ID**: LAC-002
- **Test Objective**: Measure message throughput in a LAN environment
- **Test Steps**: 
  1. Start all agents
  2. Send multiple messages in rapid succession
  3. Measure messages per second
- **Expected Result**: Throughput > 50 messages/second

### 3.3 Task Execution Test Cases

#### 3.3.1 LAN Task Execution Test
- **Test ID**: LAT-001
- **Test Objective**: Verify task execution across different machines
- **Test Steps**: 
  1. Start all agents
  2. Send tasks from Route Agent to End Agents on different machines
  3. Check task execution success rate
- **Expected Result**: All tasks execute successfully

#### 3.3.2 Distributed Task Test
- **Test ID**: LAT-002
- **Test Objective**: Test distributed task execution
- **Test Steps**: 
  1. Start all agents
  2. Send different tasks to different End Agents simultaneously
  3. Check distributed execution and completion
- **Expected Result**: Tasks execute on respective machines without interference

### 3.4 Network Resilience Test Cases

#### 3.4.1 LAN Network Stability Test
- **Test ID**: LAR-001
- **Test Objective**: Verify system stability in a LAN environment
- **Test Steps**: 
  1. Start all agents
  2. Run system for extended period (8 hours)
  3. Periodically send tasks and check status
- **Expected Result**: System remains stable with no errors

#### 3.4.2 Network Interruption Test
- **Test ID**: LAR-002
- **Test Objective**: Verify system behavior during temporary network interruptions
- **Test Steps**: 
  1. Start all agents
  2. Temporarily disconnect one End Agent from the network
  3. Reconnect the End Agent and check recovery
- **Expected Result**: Agent successfully reconnects and rejoins the network

#### 3.4.3 WIFI Signal Strength Test
- **Test ID**: LAR-003
- **Test Objective**: Test system behavior under different WIFI signal strengths
- **Test Steps**: 
  1. Start all agents
  2. Test at different distances from the WIFI router
  3. Measure performance and reliability at different signal strengths
- **Expected Result**: System adapts to different signal strengths with appropriate error handling

## 4. Test Execution

### 4.1 Test Commands

#### 4.1.1 Start All Agents (Nexus)
```bash
# Start Route Agent on Machine 1 (Nexus)
cd route-agent
java -jar target\route-agent-lan-1.0.0-test.jar

# Start End Agent 1 on Machine 2 (Nexus)
cd end-agent-1
java -jar target\end-agent-1-lan-1.0.0-test.jar

# Start End Agent 2 on Machine 3 (Nexus)
cd end-agent-2
java -jar target\end-agent-2-lan-1.0.0-test.jar
```

#### 4.1.2 Test Service Discovery (Nexus)
```bash
# Check Route Agent logs for End Agent discovery
# Look for messages like: "End Agent discovered: end-agent-1 (192.168.1.x)" (Nexus)

# Check End Agent logs for Route Agent discovery
# Look for messages like: "Route Agent discovered: route-agent (192.168.1.x)" (Nexus)
```

#### 4.1.3 Test Task Execution (Nexus)
```bash
# Using curl to send task from Route Agent to End Agent 1 (Nexus)
curl -X POST http://<machine1-ip>:8110/api/v1/task -H "Content-Type: application/json" -d '{"taskId":"test-task-001","targetAgent":"end-agent-1","action":"execute","data":"test data"}'

# Using curl to send task from Route Agent to End Agent 2 (Nexus)
curl -X POST http://<machine1-ip>:8110/api/v1/task -H "Content-Type: application/json" -d '{"taskId":"test-task-002","targetAgent":"end-agent-2","action":"execute","data":"test data"}'
```

### 4.2 Monitoring

#### 4.2.1 Log Monitoring (Nexus)
- **Route Agent Logs**: `route-agent/logs/skill-vfs.log` on Machine 1
- **End Agent 1 Logs**: `end-agent-1/logs/skill-vfs.log` on Machine 2
- **End Agent 2 Logs**: `end-agent-2/logs/skill-vfs.log` on Machine 3

#### 4.2.2 Health Checks (Nexus)
```bash
# Check Route Agent health (Nexus)
curl http://<machine1-ip>:8110/api/v1/health

# Check End Agent 1 health (Nexus)
curl http://<machine2-ip>:8111/api/v1/health

# Check End Agent 2 health (Nexus)
curl http://<machine3-ip>:8112/api/v1/health
```

### 4.3 Network Monitoring (Nexus)
- **Tool**: Wireshark or tcpdump
- **Filter**: `udp port 9030 or udp port 9031 or udp port 9032`
- **Expected Traffic**: Broadcast messages, discovery packets, heartbeat packets (Nexus format)

## 5. Test Results

### 5.1 Success Criteria (Nexus)
- All agents successfully discover each other across the LAN (Nexus)
- Communication between agents across different machines works reliably (Nexus)
- Tasks execute successfully on remote machines (Nexus)
- System remains stable over extended periods (Nexus)
- System recovers from network interruptions (Nexus)
- All test cases pass for Nexus version
- Heartbeat mechanism functions correctly (Nexus)
- Skill registration and discovery work properly (Nexus)

### 5.2 Failure Handling (Nexus)
- **Discovery Failure**: Check LAN connectivity, firewall settings, and network discovery settings for Nexus
- **Communication Issues**: Verify network configuration, IP addresses, and port accessibility for Nexus
- **Task Execution Errors**: Check agent initialization and network latency for Nexus
- **Stability Issues**: Monitor network stability and system resources for Nexus
- **Heartbeat Issues**: Check network stability and service availability for Nexus

## 6. Test Cleanup

### 6.1 Stopping Agents
1. Press `Ctrl+C` in each terminal on all machines to stop the agents
2. Verify all processes have exited

### 6.2 Cleanup Steps (Nexus)
```bash
# Clean up build artifacts on all machines
# Machine 1
cd route-agent && mvn clean

# Machine 2
cd end-agent-1 && mvn clean

# Machine 3
cd end-agent-2 && mvn clean

# Remove log files (optional) on all machines
rm -rf route-agent/logs/*  # Machine 1
rm -rf end-agent-1/logs/*  # Machine 2
rm -rf end-agent-2/logs/*  # Machine 3
```

## 7. Troubleshooting

### 7.1 Common Issues (Nexus)

| Issue | Possible Cause | Solution |
|-------|---------------|----------|
| Discovery fails | LAN firewall blocking UDP | Allow UDP port 9030-9032 through firewall on all machines for Nexus |
| Communication error | Network segmentation | Ensure all machines are in the same subnet for Nexus |
| Task execution timeout | Network latency | Increase timeout settings in configuration for Nexus |
| Agent unreachable | IP address changes | Use static IP addresses or DHCP reservations for Nexus |
| WIFI interference | Signal interference | Test on different channels or use Ethernet for Nexus |
| Heartbeat lost | Network instability or service overload | Check network stability and reduce service load for Nexus |

### 7.2 Debug Commands (Nexus)

#### 7.2.1 Check LAN Connectivity
```bash
# Test connectivity between machines
ping <machine2-ip>  # From Machine 1
ping <machine3-ip>  # From Machine 1
ping <machine1-ip>  # From Machine 2
ping <machine3-ip>  # From Machine 2
ping <machine1-ip>  # From Machine 3
ping <machine2-ip>  # From Machine 3

# Check port accessibility
telnet <machine1-ip> 8110  # From Machine 2
telnet <machine2-ip> 8111  # From Machine 1
telnet <machine3-ip> 8112  # From Machine 1
```

#### 7.2.2 Check Network Configuration
```bash
# Check IP addresses on all machines
ipconfig  # Windows
ifconfig  # Linux/Mac

# Check subnet mask and gateway
ipconfig /all  # Windows
ifconfig -a    # Linux/Mac

# Check UDP port availability
netstat -ano | findstr :9030  # Windows
netstat -an | grep 9030      # Linux/Mac
```

## 8. Conclusion

This test scenario provides a comprehensive validation of the Ooder Agent SDK functionality in a LAN environment for the Nexus version. By following the setup instructions and executing the test cases, you can verify that the system works correctly in a typical office or home network setting with multiple machines with Nexus-specific features.

### 8.1 Test Coverage (Nexus)
- ✅ Service discovery across multiple machines (Nexus)
- ✅ Agent communication between different hosts (Nexus)
- ✅ Task execution in a distributed environment (Nexus)
- ✅ Network resilience and error handling (Nexus)
- ✅ Performance and reliability in a LAN (Nexus)
- ✅ Heartbeat mechanism (Nexus)
- ✅ Skill registration and discovery (Nexus)

### 8.2 Next Steps (Nexus)
After completing this test scenario, you can proceed to test other network environments with Nexus features:
- **Intranet Network (Nexus)**: Testing across different subnets within an enterprise network with Nexus features
- **Internet Network (Nexus)**: Testing across wide area networks with Nexus features (optional)

The LAN network test provides valuable insights into how the Ooder Agent SDK Nexus performs in a typical distributed environment with multiple machines with enhanced features and capabilities.

### 8.3 Nexus-Specific Features Tested
- ✅ Enhanced service discovery across multiple machines
- ✅ Improved agent communication between different hosts
- ✅ Advanced task execution in a distributed environment
- ✅ Robust heartbeat mechanism
- ✅ Seamless integration with Skill VFS 0.6.5 Nexus

The Nexus version of the LAN network test demonstrates the improved performance and reliability of the Ooder Agent SDK in a typical office or home network setting with multiple machines.