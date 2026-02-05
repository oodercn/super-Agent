# Service Discovery Test Scenario Documentation (Nexus)

## 1. Scenario Overview

This test scenario is designed to validate the service discovery functionality of the Ooder Agent SDK in a P2P network environment for the Nexus version. It focuses on testing the discovery mechanism, scene management, and agent coordination with Nexus-specific features.

### 1.1 Test Objectives
- Validate the service discovery process between End Agents and Route Agents in Nexus
- Test scene creation and management functionality with Nexus features
- Verify agent registration and coordination in the Nexus P2P network
- Ensure proper network communication and error handling in Nexus

### 1.2 Test Environment

| Component | Type | Port | Description |
|-----------|------|------|-------------|
| skill-a | End Agent | 8081 | Information retrieval service (Nexus) |
| skill-b | End Agent | 8082 | Data submission service (Nexus) |
| skill-c | Route Agent | 8083 | Scene management service (Nexus) |
| UDP Discovery | Network | 9001-9003 | Service discovery ports (Nexus) |

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

3. **Build and Start Skill C (Route Agent - Nexus)**
   ```bash
   cd skill-c
   mvn clean package -DskipTests
   java -jar target\skill-c-service-discovery-1.0.0-test.jar
   ```

4. **Build and Start Skill A (End Agent - Nexus)**
   ```bash
   cd skill-a
   mvn clean package -DskipTests
   java -jar target\skill-a-service-discovery-1.0.0-test.jar
   ```

5. **Build and Start Skill B (End Agent - Nexus)**
   ```bash
   cd skill-b
   mvn clean package -DskipTests
   java -jar target\skill-b-service-discovery-1.0.0-test.jar
   ```

## 3. Test Cases

### 3.1 Service Discovery Test Cases

#### 3.1.1 DiscoveryService Initialization Test
- **Test ID**: DS-001
- **Test Objective**: Verify that DiscoveryService initializes successfully
- **Test Steps**: 
  1. Start Skill C (Route Agent)
  2. Start Skill A (End Agent)
  3. Check logs for DiscoveryService initialization messages
- **Expected Result**: DiscoveryService initializes successfully, UDP socket created

#### 3.1.2 Broadcast Message Test
- **Test ID**: DS-002
- **Test Objective**: Verify that End Agents send broadcast messages
- **Test Steps**: 
  1. Start Wireshark with filter `udp port 5000`
  2. Start Skill C and Skill A
  3. Monitor network packets
- **Expected Result**: Broadcast messages are sent with correct format

#### 3.1.3 Scene Join Test
- **Test ID**: DS-003
- **Test Objective**: Verify that End Agents can join scenes
- **Test Steps**: 
  1. Start Skill C (creates scene)
  2. Start Skill A and Skill B
  3. Check logs for join success messages
- **Expected Result**: Both End Agents successfully join the scene

### 3.2 Scene Management Test Cases

#### 3.2.1 Scene Creation Test
- **Test ID**: SM-001
- **Test Objective**: Verify scene creation functionality
- **Test Steps**: 
  1. Start Skill C
  2. Send create scene command
  3. Check response and logs
- **Expected Result**: Scene is created successfully

#### 3.2.2 Agent Join/Leave Test
- **Test ID**: SM-002
- **Test Objective**: Verify agent join/leave functionality
- **Test Steps**: 
  1. Create a scene
  2. Send join command from End Agent
  3. Send leave command from End Agent
  4. Check membership status
- **Expected Result**: Agent successfully joins and leaves the scene

#### 3.2.3 Scene List Test
- **Test ID**: SM-003
- **Test Objective**: Verify scene listing functionality
- **Test Steps**: 
  1. Create multiple scenes
  2. Send list scenes command
  3. Check response
- **Expected Result**: All scenes are listed correctly

## 4. Test Execution

### 4.1 Test Commands

#### 4.1.1 Start All Services (Nexus)
```bash
# Start Skill C (Route Agent - Nexus)
cd skill-c
java -jar target\skill-c-service-discovery-1.0.0-test.jar

# Start Skill A (End Agent - Nexus)
cd ../skill-a
java -jar target\skill-a-service-discovery-1.0.0-test.jar

# Start Skill B (End Agent - Nexus)
cd ../skill-b
java -jar target\skill-b-service-discovery-1.0.0-test.jar
```

#### 4.1.2 Test Scene Creation (Nexus)
```bash
# Using curl to send create scene command
curl -X POST http://localhost:8083/api/v1/coordinate -H "Content-Type: application/json" -d '{"sceneId":"RBC_SCENE_001"}'
```

#### 4.1.3 Test Agent Join (Nexus)
```bash
# Using curl to send join scene command
curl -X POST http://localhost:8083/api/v1/coordinate -H "Content-Type: application/json" -d '{"sceneId":"RBC_SCENE_001","agentId":"skill-a-service-discovery-001"}'
```

### 4.2 Monitoring

#### 4.2.1 Log Monitoring (Nexus)
- **Skill A Logs**: `skill-a/logs/skill-vfs.log`
- **Skill B Logs**: `skill-b/logs/skill-vfs.log`
- **Skill C Logs**: `skill-c/logs/skill-vfs.log`

#### 4.2.2 Network Monitoring (Nexus)
- **Tool**: Wireshark
- **Filter**: `udp port 9001 or udp port 9002 or udp port 9003 or tcp port 8081 or tcp port 8082 or tcp port 8083`
- **Expected Traffic**: Broadcast messages, join requests, heartbeat packets (Nexus format)

## 5. Test Results

### 5.1 Success Criteria (Nexus)
- All End Agents successfully discover and join the Route Agent in Nexus
- Scene creation and management operations complete successfully with Nexus features
- No network errors or timeouts in Nexus P2P network
- All test cases pass for Nexus version
- Skills are successfully registered and discoverable in Nexus
- Heartbeat mechanism functions correctly in Nexus

### 5.2 Failure Handling (Nexus)
- **Network Issues**: Check firewall settings, network connectivity for UDP ports 9001-9003
- **Port Conflicts**: Verify no other services are using ports 8081-8083 or 9001-9003
- **SDK Errors**: Check Agent SDK 0.6.5 Nexus version compatibility, configuration settings
- **Skill Initialization**: Verify skill dependencies and initialization order for Nexus
- **Heartbeat Issues**: Check network stability and service availability

## 6. Test Cleanup

### 6.1 Stopping Services
1. Press `Ctrl+C` in each terminal to stop the services
2. Verify all processes have exited

### 6.2 Cleanup Steps (Nexus)
```bash
# Clean up build artifacts
cd skill-a && mvn clean
cd ../skill-b && mvn clean
cd ../skill-c && mvn clean

# Remove log files (optional)
rm -rf skill-a/logs/*
rm -rf skill-b/logs/*
rm -rf skill-c/logs/*
```

## 7. Troubleshooting

### 7.1 Common Issues (Nexus)

| Issue | Possible Cause | Solution |
|-------|---------------|----------|
| Service discovery fails | Firewall blocking UDP ports 9001-9003 | Allow UDP ports 9001-9003 through firewall |
| Port conflict | Other services using the same ports | Change service ports in application.yml for Nexus |
| Skill initialization error | Missing dependencies for Nexus | Check pom.xml dependencies for Nexus compatibility |
| Scene join timeout | Network latency or Route Agent not running | Ensure Route Agent is started first in Nexus |
| Heartbeat lost | Network instability or service overload | Check network stability and reduce service load |

### 7.2 Debug Commands (Nexus)

#### 7.2.1 Check Port Availability
```bash
# Check if ports are in use
netstat -ano | findstr :8081
netstat -ano | findstr :8082
netstat -ano | findstr :8083
netstat -ano | findstr :9001
netstat -ano | findstr :9002
netstat -ano | findstr :9003
```

#### 7.2.2 Test Network Connectivity
```bash
# Test TCP connectivity
telnet localhost 8081
telnet localhost 8082
telnet localhost 8083

# Test UDP connectivity
nc -u localhost 9001
nc -u localhost 9002
nc -u localhost 9003
```

## 8. Conclusion

This test scenario provides a comprehensive validation of the service discovery functionality in the Ooder Agent SDK for the Nexus version. By following the setup instructions and executing the test cases, you can verify that the P2P network discovery mechanism works correctly in a controlled environment with Nexus-specific features.

### 8.1 Test Coverage (Nexus)
- ✅ Service discovery initialization (Nexus)
- ✅ Broadcast message transmission (Nexus format)
- ✅ Agent registration and discovery (Nexus)
- ✅ Scene creation and management (Nexus features)
- ✅ Agent join/leave operations (Nexus)
- ✅ Network communication (UDP ports 9001-9003)
- ✅ Error handling (Nexus)
- ✅ Skill registration and discovery (Nexus)
- ✅ Heartbeat mechanism (Nexus)

### 8.2 Next Steps (Nexus)
After completing this test scenario, you can proceed to test other Nexus-specific scenarios such as:
- **Agent Hierarchy (Nexus)**: Testing the full agent hierarchy with McpAgent, RouteAgent, and EndAgent in Nexus
- **Network Environment (Nexus)**: Testing across different network environments with Nexus features
- **Performance Test (Nexus)**: Testing performance under load with Nexus optimizations
- **Reliability Test (Nexus)**: Testing system reliability under various conditions with Nexus fault tolerance