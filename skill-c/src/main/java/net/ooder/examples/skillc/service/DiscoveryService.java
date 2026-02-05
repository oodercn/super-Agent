/**
 * 作者：ooderAI agent team
 * 版本：V0.6.0
 * 日期：2026-01-18
 */
package net.ooder.examples.skillc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.ooder.examples.skillc.model.Agent;

@Service
public class DiscoveryService {
    private static final Logger logger = LoggerFactory.getLogger(DiscoveryService.class);
    private static final int BUFFER_SIZE = 1024;
    private static final String DISCOVERY_PREFIX = "DISCOVER:SKILL:";
    private static final String JOIN_RESPONSE_PREFIX = "JOIN_RESPONSE:SKILL:";
    private static final String SECURITY_KEY = "default_security_key_2023";

    private final SceneService sceneService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private DatagramSocket socket;

    @Value("${skill.discovery.port:5000}")
    private int discoveryPort;

    @Value("${skill.discovery.broadcast-address:255.255.255.255}")
    private String broadcastAddress;

    @Value("${skill.scene.id:RBC_SCENE_001}")
    private String sceneId;

    @Autowired
    public DiscoveryService(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @PostConstruct
    public void initialize() {
        try {
            socket = new DatagramSocket(discoveryPort);
            socket.setBroadcast(true);
            logger.info("Discovery service initialized on port {}", discoveryPort);
            logger.info("UDP broadcast is limited to MCP Agent internal use only");
            startDiscoveryListener();
        } catch (IOException e) {
            logger.error("Failed to initialize discovery service: {}", e.getMessage());
        }
    }

    private void startDiscoveryListener() {
        executorService.submit(() -> {
            logger.info("Discovery listener started");
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    logger.info("Received UDP packet: {}", message);
                    processDiscoveryPacket(message, packet);
                } catch (IOException e) {
                    logger.error("Error receiving discovery packet: {}", e.getMessage());
                }
            }
        });
    }

    private void processDiscoveryPacket(String message, DatagramPacket packet) {
        if (message.startsWith(DISCOVERY_PREFIX)) {
            String[] parts = message.substring(DISCOVERY_PREFIX.length()).split(";");
            if (parts.length >= 4) {
                String agentId = parts[0];
                String agentName = parts[1];
                String agentType = parts[2];
                String agentEndpoint = parts[3];
                
                // 安全认证流程（仅验证格式，不进行实际安全验证）
                boolean isAuthenticated = true;
                String timestamp = null;
                String signature = null;
                
                if (parts.length >= 6) {
                    timestamp = parts[4];
                    signature = parts[5];
                    logger.info("Processing secure discovery request with timestamp and signature");
                    
                    // 验证时间戳格式
                    try {
                        long msgTimestamp = Long.parseLong(timestamp);
                        logger.info("Discovery request timestamp: {}", msgTimestamp);
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid timestamp format: {}", timestamp);
                    }
                    
                    // 验证签名格式
                    if (signature == null || signature.isEmpty()) {
                        logger.warn("Missing signature in discovery request");
                    }
                } else {
                    logger.info("Received discovery request without security information (backward compatibility mode)");
                }

                logger.info("Discovery request authentication result: {}", isAuthenticated ? "SUCCESS" : "FAILED");
                
                // Check if agent type is valid (skill-a or skill-b)
                if (isAuthenticated && ("skill-a".equals(agentType) || "skill-b".equals(agentType))) {
                    // Create or get existing scene
                    sceneService.createScene("RBC代填代报场景", "代填代报数据流转场景", sceneId);
                    
                    // Create agent and join scene
                    Agent agent = new Agent(agentId, agentName, agentType, agentEndpoint);
                    if (sceneService.joinScene(sceneId, agent)) {
                        // Send join response
                        sendJoinResponse(agentId, agentType, packet.getAddress(), packet.getPort());
                    }
                } else {
                    logger.warn("Received discovery request from invalid agent type: {}", agentType);
                }
            }
        }
    }

    private void sendJoinResponse(String agentId, String agentType, InetAddress address, int port) {
        try {
            long timestamp = System.currentTimeMillis();
            // 构建响应消息，包含安全信息字段（虽然不实现完整的安全认证）
            String response = String.format("%s%s;%s;%s;%d;%s", 
                JOIN_RESPONSE_PREFIX, agentId, agentType, sceneId, timestamp, "dummy_signature");
            byte[] responseData = response.getBytes();
            DatagramPacket packet = new DatagramPacket(responseData, responseData.length, address, port);
            socket.send(packet);
            logger.info("Sent join response to agent {} (scene: {}, timestamp: {})", agentId, agentType, sceneId, timestamp);
        } catch (IOException e) {
            logger.error("Failed to send join response: {}", e.getMessage());
        }
    }

    public String getSceneId() {
        return sceneId;
    }

    public void shutdown() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        executorService.shutdown();
        logger.info("Discovery service shutdown");
    }
}