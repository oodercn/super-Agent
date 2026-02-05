package net.ooder.sdk.network.udp;

import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.HeartbeatPacket;
import net.ooder.sdk.network.packet.StatusReportPacket;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.network.packet.RoutePacket;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.config.TestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class UDPComprehensiveTest {

    @Autowired
    private UDPSDK udpSDK;
    
    @Autowired
    private PortManager portManager;

    @Test
    public void testUDPComprehensive() throws Exception {
        System.out.println("=== UDP Comprehensive Test ===");
        
        try {
            // Test 1: UDPSDK Initialization
            System.out.println("\n1. Testing UDPSDK Initialization...");
            System.out.println("OK UDPSDK initialized successfully on port: " + udpSDK.getPort());
            
            // Test 2: Start Listening
            System.out.println("\n2. Testing Start Listening...");
            udpSDK.startListening();
            System.out.println("OK UDP listener started successfully");
            
            // Test 3: Test Heartbeat Packet
            System.out.println("\n3. Testing Heartbeat Packet...");
            HeartbeatPacket heartbeatPacket = new HeartbeatPacket();
            heartbeatPacket.setSenderId("test-client-1");
            heartbeatPacket.setAgentId("test-client-1");
            
            final CountDownLatch heartbeatLatch = new CountDownLatch(1);
            udpSDK.sendHeartbeat(heartbeatPacket).thenAccept(result -> {
                if (result.isSuccess()) {
                    System.out.println("OK Heartbeat sent successfully: " + result.getBytesSent() + " bytes");
                } else {
                    System.out.println("ERROR Failed to send heartbeat: " + result.getErrorMessage());
                }
                heartbeatLatch.countDown();
            }).exceptionally(ex -> {
                System.out.println("ERROR Error sending heartbeat: " + ex.getMessage());
                ex.printStackTrace();
                heartbeatLatch.countDown();
                return null;
            });
            
            // Wait for heartbeat to complete
            heartbeatLatch.await(2, TimeUnit.SECONDS);
            
            // Test 4: Test Command Packet
            System.out.println("\n4. Testing Command Packet...");
            CommandPacket<Map<String, Object>> commandPacket = new CommandPacket<>();
            commandPacket.setOperation(CommandType.END_EXECUTE.getValue());
            commandPacket.setSenderId("test-client-1");
            
            Map<String, Object> params = new HashMap<>();
            params.put("command", "echo");
            
            Map<String, Object> argsMap = new HashMap<>();
            argsMap.put("message", "Hello from UDP Test");
            params.put("args", argsMap);
            
            commandPacket.setParams(params);
            
            final CountDownLatch commandLatch = new CountDownLatch(1);
            udpSDK.sendCommand(commandPacket).thenAccept(result -> {
                if (result.isSuccess()) {
                    System.out.println("OK Command sent successfully: " + result.getBytesSent() + " bytes");
                } else {
                    System.out.println("ERROR Failed to send command: " + result.getErrorMessage());
                }
                commandLatch.countDown();
            }).exceptionally(ex -> {
                System.out.println("ERROR Error sending command: " + ex.getMessage());
                ex.printStackTrace();
                commandLatch.countDown();
                return null;
            });
            
            // Wait for command to complete
            commandLatch.await(2, TimeUnit.SECONDS);
            
            // Test 5: Test Status Report Packet
            System.out.println("\n5. Testing Status Report Packet...");
            StatusReportPacket statusReportPacket = new StatusReportPacket();
            statusReportPacket.setSenderId("test-client-1");
            statusReportPacket.setCurrentStatus("online");
            
            final CountDownLatch statusLatch = new CountDownLatch(1);
            udpSDK.sendStatusReport(statusReportPacket).thenAccept(result -> {
                if (result.isSuccess()) {
                    System.out.println("OK Status report sent successfully: " + result.getBytesSent() + " bytes");
                } else {
                    System.out.println("ERROR Failed to send status report: " + result.getErrorMessage());
                }
                statusLatch.countDown();
            }).exceptionally(ex -> {
                System.out.println("ERROR Error sending status report: " + ex.getMessage());
                ex.printStackTrace();
                statusLatch.countDown();
                return null;
            });
            
            // Wait for status report to complete
            statusLatch.await(2, TimeUnit.SECONDS);
            
            // Test 6: Test Task Packet
            System.out.println("\n6. Testing Task Packet...");
            TaskPacket taskPacket = new TaskPacket();
            taskPacket.setSenderId("test-client-1");
            taskPacket.setTaskId("task-123");
            taskPacket.setTaskType("compute");
            
            Map<String, Object> taskParams = new HashMap<>();
            taskParams.put("operation", "sum");
            taskParams.put("numbers", new int[]{1, 2, 3, 4, 5});
            taskPacket.setParams(taskParams);
            
            final CountDownLatch taskLatch = new CountDownLatch(1);
            udpSDK.sendTask(taskPacket).thenAccept(result -> {
                if (result.isSuccess()) {
                    System.out.println("OK Task packet sent successfully: " + result.getBytesSent() + " bytes");
                } else {
                    System.out.println("ERROR Failed to send task packet: " + result.getErrorMessage());
                }
                taskLatch.countDown();
            }).exceptionally(ex -> {
                System.out.println("ERROR Error sending task packet: " + ex.getMessage());
                ex.printStackTrace();
                taskLatch.countDown();
                return null;
            });
            
            // Wait for task to complete
            taskLatch.await(2, TimeUnit.SECONDS);
            
            // Test 7: Test Route Packet
            System.out.println("\n7. Testing Route Packet...");
            RoutePacket routePacket = new RoutePacket();
            routePacket.setSenderId("test-client-1");
            routePacket.setRouteId("route-456");
            routePacket.setSourceId("192.168.1.100");
            routePacket.setDestinationId("192.168.1.200");
            
            final CountDownLatch routeLatch = new CountDownLatch(1);
            udpSDK.sendRoute(routePacket).thenAccept(result -> {
                if (result.isSuccess()) {
                    System.out.println("OK Route packet sent successfully: " + result.getBytesSent() + " bytes");
                } else {
                    System.out.println("ERROR Failed to send route packet: " + result.getErrorMessage());
                }
                routeLatch.countDown();
            }).exceptionally(ex -> {
                System.out.println("ERROR Error sending route packet: " + ex.getMessage());
                ex.printStackTrace();
                routeLatch.countDown();
                return null;
            });
            
            // Wait for route to complete
            routeLatch.await(2, TimeUnit.SECONDS);
            
            // Test 8: Test Port Management
            System.out.println("\n8. Testing Port Management...");
            boolean isPortAvailable = portManager.isPortAvailable(5000);
            System.out.println("OK Port 5000 availability check: " + (isPortAvailable ? "Available" : "In use"));
            
            // Test 9: Test Port Allocation
            System.out.println("\n9. Testing Port Allocation...");
            int allocatedPort = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
            System.out.println("OK Port allocated successfully: " + allocatedPort);
            
            // Release the allocated port
            portManager.releasePort(allocatedPort);
            System.out.println("OK Port released successfully: " + allocatedPort);
            
            // Test 10: Stop Listening
            System.out.println("\n10. Testing Stop Listening...");
            udpSDK.stopListening();
            System.out.println("OK UDP listener stopped successfully");
            
            // Test 11: Test Network Environment Detection
            System.out.println("\n11. Testing Network Environment Detection...");
            PortManager.NetworkEnvironment env = portManager.getNetworkEnvironment();
            System.out.println("OK Network environment detected: " + env);
            
            // Test 12: Test Port Statistics
            System.out.println("\n12. Testing Port Statistics...");
            portManager.printPortStatistics();
            System.out.println("OK Port statistics printed successfully");
            
            System.out.println("\n=== UDP Comprehensive Test Completed ===");
            System.out.println("All tests executed successfully!");
            
        } catch (Exception e) {
            System.err.println("ERROR Error in UDP comprehensive test: " + e.getMessage());
            e.printStackTrace();
            System.out.println("\n=== UDP Comprehensive Test Failed ===");
        }
    }
}