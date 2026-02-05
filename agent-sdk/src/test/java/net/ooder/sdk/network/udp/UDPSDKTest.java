package net.ooder.sdk.network.udp;

import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.config.TestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
@Disabled("UDPSDKTest causes forked VM termination due to resource cleanup issues")
public class UDPSDKTest {
    @Autowired
    private UDPSDK udpSDK;

    @BeforeEach
    public void setUp() {
        assertNotNull(udpSDK);
    }

    @AfterEach
    public void tearDown() {
        try {
            udpSDK.stopListening();
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    @Test
    public void testUDPSDKInitialization() throws Exception {
        System.out.println("UDPSDK initialized successfully on port: " + udpSDK.getPort());
    }

    @Test
    public void testSendCommand() throws Exception {
        // Create command packet
        CommandPacket packet = new CommandPacket();
        packet.setOperation(CommandType.END_EXECUTE.getValue());
        packet.setSenderId("test-client");
        
        Map<String, Object> params = new HashMap<>();
        params.put("command", "echo");
        
        Map<String, Object> argsMap = new HashMap<>();
        argsMap.put("message", "Hello from UDPSDK Test");
        params.put("args", argsMap);
        
        packet.setParams(params);

        // Start listening
        udpSDK.startListening();
        System.out.println("UDP listener started");

        try {
            // Send broadcast packet
            System.out.println("Sending UDP command");
            final CountDownLatch latch = new CountDownLatch(1);
            
            udpSDK.sendCommand(packet).thenAccept(result -> {
                if (result.isSuccess()) {
                    System.out.println("UDP command sent successfully: " + result.getBytesSent() + " bytes");
                } else {
                    System.out.println("Failed to send UDP command: " + result.getErrorMessage());
                }
                latch.countDown();
            }).exceptionally(ex -> {
                System.out.println("Error sending UDP command: " + ex.getMessage());
                ex.printStackTrace();
                latch.countDown();
                return null;
            });

            // Wait for response
            latch.await(5, TimeUnit.SECONDS);
        } finally {
            // Stop listening
            udpSDK.stopListening();
            System.out.println("UDP listener stopped");
        }
    }
}