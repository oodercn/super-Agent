# Intranet Network Test Scenario Documentation (Nexus)

## 1. Scenario Overview

This test scenario is designed to validate the Ooder Agent SDK functionality in an intranet environment for the Nexus version, where agents are running on different machines across multiple subnets within an enterprise network.

### 1.1 Test Objectives
- Validate service discovery across multiple subnets in an intranet (Nexus)
- Test agent communication between different network segments (Nexus)
- Verify task execution and scene management in a complex network environment (Nexus)
- Ensure proper error handling and fault tolerance in an intranet (Nexus)
- Test performance and reliability in a typical enterprise network (Nexus)

### 1.2 Test Environment

| Component | Type | Port | Description |
|-----------|------|------|-------------|
| route-agent | Route Agent | 8120 | Route control plane agent (Subnet A, Nexus) |
| end-agent-1 | End Agent | 8121 | End node agent (Subnet B, Nexus) |
| end-agent-2 | End Agent | 8122 | End node agent (Subnet C, Nexus) |
| UDP Communication | Network | 9040-9042 | Agent communication ports (Nexus) |
| Network | Intranet | - | Multi-subnet enterprise network |
| Firewall | Network | - | Between subnets |

## 2. Test Environment Setup

### 2.1 Prerequisites
- JDK 8+ on all machines
- Maven 3.6+ on all machines
- Spring Boot 2.7.0+ on all machines
- Agent SDK 0.6.5 Nexus
- Skill VFS 0.6.5 Nexus
- Intranet network connection with multiple subnets
- Multiple machines (minimum 3) in different subnets
- Network administrator access (for firewall configuration)

### 2.2 Deployment Steps

1. **Prepare All Machines**
   - Install JDK, Maven, and Spring Boot on all machines
   - Ensure machines are in different subnets within the intranet
   - Verify basic network connectivity between subnets

2. **Configure Firewall Rules**
   - Allow UDP ports 9040-9042 through firewall between subnets
   - Allow TCP ports 8120-8122 through firewall between subnets
   - Ensure ICMP (ping) is allowed for testing

3. **Build the Agent SDK on Machine 1 (Subnet A, Nexus)**
   ```bash
   cd ../../../../agent-sdk
   mvn clean package -DskipTests
   ```

4. **Build the Skill VFS on Machine 1 (Subnet A, Nexus)**
   ```bash
   cd ../../../../skill-vfs
   mvn clean package -DskipTests
   ```

5. **Copy Build Artifacts to Other Machines**
   ```bash
   # Copy agent-sdk-0.6.5-nexus.jar to Machine 2 (Subnet B) and 3 (Subnet C)
   # Copy skill-vfs-0.6.5-nexus.jar to Machine 2 and 3
   ```

6. **Build and Start Route Agent on Machine 1 (Subnet A, Nexus)**
   ```bash
   cd route-agent
   mvn clean package -DskipTests
   java -jar target\route-agent-intranet-1.0.0-test.jar
   ```

7. **Build and Start End Agent 1 on Machine 2 (Subnet B, Nexus)**
   ```bash
   cd end-agent-1
   mvn clean package -DskipTests
   java -jar target\end-agent-1-intranet-1.0.0-test.jar
   ```

8. **Build and Start End Agent 2 on Machine 3 (Subnet C, Nexus)**
   ```bash
   cd end-agent-2
   mvn clean package -DskipTests
   java -jar target\end-agent-2-intranet-1.0.0-test.jar
   ```

## 3. Test Cases

### 3.1 Service Discovery Test Cases

#### 3.1.1 Intranet Discovery Test
- **Test ID**: IND-001
- **Test Objective**: Verify service discovery across multiple subnets
- **Test Steps**: 
  1. Start Route Agent on Machine 1 (Subnet A)
  2. Start End Agent 1 on Machine 2 (Subnet B)
  3. Start End Agent 2 on Machine 3 (Subnet C)
  4. Check logs for discovery success messages
- **Expected Result**: All agents successfully discover each other across subnets

#### 3.1.2 Cross-Subnet Discovery Speed Test
- **Test ID**: IND-002
- **Test Objective**: Measure discovery speed across subnets
- **Test Steps**: 
  1. Start Route Agent on Machine 1 (Subnet A)
  2. Start End Agent on Machine 2 (Subnet B) and measure time to discovery
  3. Repeat multiple times and calculate average
- **Expected Result**: Discovery completes within 5 seconds

### 3.2 Communication Test Cases

#### 3.2.1 Intranet Communication Test
- **Test ID**: INC-001
- **Test Objective**: Verify communication between agents across different subnets
- **Test Steps**: 
  1. Start all agents
  2. Send commands between all agent pairs across subnets
  3. Check response times and success rates
- **Expected Result**: All communications succeed with acceptable latency

#### 3.2.2 Message Throughput Test
- **Test ID**: INC-002
- **Test Objective**: Measure message throughput across subnets
- **Test Steps**: 
  1. Start all agents
  2. Send multiple messages in rapid succession across subnets
  3. Measure messages per second
- **Expected Result**: Throughput > 30 messages/second

### 3.3 Task Execution Test Cases

#### 3.3.1 Intranet Task Execution Test
- **Test ID**: INT-001
- **Test Objective**: Verify task execution across different subnets
- **Test Steps**: 
  1. Start all agents
  2. Send tasks from Route Agent to End Agents on different subnets
  3. Check task execution success rate
- **Expected Result**: All tasks execute successfully across subnets

#### 3.3.2 Distributed Cross-Subnet Task Test
- **Test ID**: INT-002
- **Test Objective**: Test distributed task execution across subnets
- **Test Steps**: 
  1. Start all agents
  2. Send different tasks to different End Agents on different subnets simultaneously
  3. Check distributed execution and completion
- **Expected Result**: Tasks execute on respective machines without interference

### 3.4 Network Resilience Test Cases

#### 3.4.1 Intranet Network Stability Test
- **Test ID**: INR-001
- **Test Objective**: Verify system stability in an intranet environment
- **Test Steps**: 
  1. Start all agents
  2. Run system for extended period (12 hours)
  3. Periodically send tasks and check status
- **Expected Result**: System remains stable with no errors

#### 3.4.2 Firewall Rule Test
- **Test ID**: INR-002
- **Test Objective**: Verify system behavior with different firewall rules
- **Test Steps**: 
  1. Start all agents with minimal firewall rules
  2. Gradually tighten firewall rules
  3. Test discovery and communication at each step
- **Expected Result**: System adapts to firewall rules, fails gracefully when necessary

#### 3.4.3 Network Congestion Test
- **Test ID**: INR-003
- **Test Objective**: Test system behavior under network congestion
- **Test Steps**: 
  1. Start all agents
  2. Simulate network congestion between subnets
  3. Test discovery, communication, and task execution
- **Expected Result**: System maintains functionality with appropriate error handling

## 4. Test Execution

### 4.1 Test Commands

#### 4.1.1 Start All Agents (Nexus)
```bash
# Start Route Agent on Machine 1 (Subnet A, Nexus)
cd route-agent
java -jar target\route-agent-intranet-1.0.0-test.jar

# Start End Agent 1 on Machine 2 (Subnet B, Nexus)
cd end-agent-1
java -jar target\end-agent-1-intranet-1.0.0-test.jar

# Start End Agent 2 on Machine 3 (Subnet C, Nexus)
cd end-agent-2
java -jar target\end-agent-2-intranet-1.0.0-test.jar
```

#### 4.1.2 Test Service Discovery (Nexus)
```bash
# Check Route Agent logs for End Agent discovery
# Look for messages like: "End Agent discovered: end-agent-1 (10.0.1.x)" (Nexus)

# Check End Agent logs for Route Agent discovery
# Look for messages like: "Route Agent discovered: route-agent (10.0.0.x)" (Nexus)
```

#### 4.1.3 Test Task Execution (Nexus)
```bash
# Using curl to send task from Route Agent to End Agent 1 (Nexus)
curl -X POST http://<subnetA-ip>:8120/api/v1/task -H "Content-Type: application/json" -d '{"taskId":"test-task-001","targetAgent":"end-agent-1","action":"execute","data":"test data"}'

# Using curl to send task from Route Agent to End Agent 2 (Nexus)
curl -X POST http://<subnetA-ip>:8120/api/v1/task -H "Content-Type: application/json" -d '{"taskId":"test-task-002","targetAgent":"end-agent-2","action":"execute","data":"test data"}'
```

### 4.2 Monitoring

#### 4.2.1 Log Monitoring (Nexus)
- **Route Agent Logs**: `route-agent/logs/skill-vfs.log` on Machine 1 (Subnet A)
- **End Agent 1 Logs**: `end-agent-1/logs/skill-vfs.log` on Machine 2 (Subnet B)
- **End Agent 2 Logs**: `end-agent-2/logs/skill-vfs.log` on Machine 3 (Subnet C)

#### 4.2.2 Health Checks (Nexus)
```bash
# Check Route Agent health (Nexus)
curl http://<subnetA-ip>:8120/api/v1/health

# Check End Agent 1 health (Nexus)
curl http://<subnetB-ip>:8121/api/v1/health

# Check End Agent 2 health (Nexus)
curl http://<subnetC-ip>:8122/api/v1/health
```

### 4.3 Network Monitoring (Nexus)
- **Tool**: Wireshark or tcpdump on each subnet
- **Filter**: `udp port 9040 or udp port 9041 or udp port 9042`
- **Expected Traffic**: Broadcast messages, discovery packets, heartbeat packets (Nexus format)
- **Firewall Logs**: Monitor firewall logs for blocked packets

## 5. Test Results

### 5.1 Success Criteria (Nexus)
- All agents successfully discover each other across subnets (Nexus)
- Communication between agents across different network segments works reliably (Nexus)
- Tasks execute successfully on remote machines across subnets (Nexus)
- System remains stable over extended periods (Nexus)
- System adapts to different firewall rules and network conditions (Nexus)
- All test cases pass for Nexus version
- Heartbeat mechanism functions correctly (Nexus)
- Skill registration and discovery work properly (Nexus)

### 5.2 Failure Handling (Nexus)
- **Discovery Failure**: Check firewall rules, routing configuration, and network segmentation for Nexus
- **Communication Issues**: Verify cross-subnet connectivity, IP addressing, and port accessibility for Nexus
- **Task Execution Errors**: Check network latency, packet loss, and agent initialization for Nexus
- **Stability Issues**: Monitor network congestion, firewall performance, and system resources for Nexus
- **Heartbeat Issues**: Check network stability and service availability for Nexus

## 6. Test Cleanup

### 6.1 Stopping Agents
1. Press `Ctrl+C` in each terminal on all machines to stop the agents
2. Verify all processes have exited

### 6.2 Cleanup Steps (Nexus)
```bash
# Clean up build artifacts on all machines
# Machine 1 (Subnet A)
cd route-agent && mvn clean

# Machine 2 (Subnet B)
cd end-agent-1 && mvn clean

# Machine 3 (Subnet C)
cd end-agent-2 && mvn clean

# Remove log files (optional) on all machines
rm -rf route-agent/logs/*  # Machine 1
rm -rf end-agent-1/logs/*  # Machine 2
rm -rf end-agent-2/logs/*  # Machine 3

# Restore firewall rules to original state
```

## 7. Troubleshooting

### 7.1 Common Issues (Nexus)

| Issue | Possible Cause | Solution |
|-------|---------------|----------|
| Discovery fails | Firewall blocking UDP broadcast | Configure firewall to allow UDP broadcast across subnets for Nexus |
| Communication error | Routing issues between subnets | Verify routing configuration and network paths for Nexus |
| Task execution timeout | Network latency between subnets | Increase timeout settings in configuration for Nexus |
| Agent unreachable | Network address translation (NAT) | Use static IP addresses and proper port forwarding for Nexus |
| Firewall blocking | Strict firewall rules | Configure appropriate firewall exceptions for Nexus |
| Heartbeat lost | Network instability or service overload | Check network stability and reduce service load for Nexus |

### 7.2 Debug Commands (Nexus)

#### 7.2.1 Check Intranet Connectivity
```bash
# Test connectivity between subnets
ping <subnetB-ip>  # From Subnet A
ping <subnetC-ip>  # From Subnet A
ping <subnetA-ip>  # From Subnet B
ping <subnetC-ip>  # From Subnet B
ping <subnetA-ip>  # From Subnet C
ping <subnetB-ip>  # From Subnet C

# Check port accessibility
telnet <subnetA-ip> 8120  # From Subnet B
telnet <subnetB-ip> 8121  # From Subnet A
telnet <subnetC-ip> 8122  # From Subnet A
```

#### 7.2.2 Check Network Configuration
```bash
# Check IP addresses and subnets on all machines
ipconfig /all  # Windows
ifconfig -a    # Linux/Mac

# Check routing tables
route print  # Windows
netstat -r   # Linux/Mac

# Check firewall rules
# Windows: Windows Firewall with Advanced Security
# Linux: iptables -L
# Network firewall: Check device configuration

# Check UDP port availability
netstat -ano | findstr :9040  # Windows
netstat -an | grep 9040      # Linux/Mac
```

## 8. Conclusion

This test scenario provides a comprehensive validation of the Ooder Agent SDK functionality in an intranet environment for the Nexus version. By following the setup instructions and executing the test cases, you can verify that the system works correctly in a complex enterprise network with multiple subnets and firewalls with Nexus-specific features.

### 8.1 Test Coverage (Nexus)
- ✅ Service discovery across multiple subnets (Nexus)
- ✅ Agent communication between network segments (Nexus)
- ✅ Task execution in a complex network environment (Nexus)
- ✅ Network resilience and error handling (Nexus)
- ✅ Performance and reliability in an intranet (Nexus)
- ✅ Heartbeat mechanism (Nexus)
- ✅ Skill registration and discovery (Nexus)

### 8.2 Next Steps (Nexus)
After completing this test scenario, you can proceed to test other aspects of the system with Nexus features:
- **Performance Optimization (Nexus)**: Tuning the system for better intranet performance with Nexus optimizations
- **Security Hardening (Nexus)**: Enhancing security in a multi-subnet environment with Nexus features
- **Scalability Testing (Nexus)**: Testing with larger numbers of agents across more subnets with Nexus capabilities

The intranet network test provides valuable insights into how the Ooder Agent SDK Nexus performs in a real-world enterprise network environment with enhanced features and capabilities.

### 8.3 Nexus-Specific Features Tested
- ✅ Enhanced service discovery across multiple subnets
- ✅ Improved agent communication between network segments
- ✅ Advanced task execution in a complex network environment
- ✅ Robust heartbeat mechanism
- ✅ Seamless integration with Skill VFS 0.6.5 Nexus

The Nexus version of the intranet network test demonstrates the improved performance and reliability of the Ooder Agent SDK in a complex enterprise network environment with multiple subnets and firewalls.