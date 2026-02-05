package net.ooder.examples.skillb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class DiscoveryService {
    private static final Logger logger = LoggerFactory.getLogger(DiscoveryService.class);
    private static final int BUFFER_SIZE = 1024;
    private static final String DISCOVERY_PREFIX = "DISCOVER:SKILL:";
    private static final String JOIN_RESPONSE_PREFIX = "JOIN_RESPONSE:SKILL:";
    private static final int BROADCAST_INTERVAL = 5000;
    private static final int MAX_RETRIES = 10;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private DatagramSocket socket;
    private boolean isRunning = false;
    private boolean isJoined = false;
    private String assignedSceneId;
    private final String agentId = UUID.randomUUID().toString();

    @Value("${skill.discovery.port:5000}")
    private int discoveryPort;

    @Value("${skill.discovery.broadcast-address:255.255.255.255}")
    private String broadcastAddress;

    @Value("${server.port:9001}")
    private int serverPort;

    @Value("${skill.b.name:SkillB}")
    private String skillName;

    @Value("${skill.b.type:skill-b}")
    private String skillType;

    @PostConstruct
    public void initialize() {
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            isRunning = true;
            logger.info("Discovery service initialized for Skill B");
            startDiscoveryProcess();
        } catch (IOException e) {
            logger.error("Failed to initialize discovery service: {}", e.getMessage());
        }
    }

    private void startDiscoveryProcess() {
        // Start broadcast thread
        executorService.submit(this::sendDiscoveryBroadcasts);
        
        // Start listener thread
        executorService.submit(this::listenForResponses);
    }

    private void sendDiscoveryBroadcasts() {
        try {
            String agentEndpoint = String.format("http://localhost:%d", serverPort);
            String broadcastMessage = String.format("%s%s;%s;%s;%s", 
                    DISCOVERY_PREFIX, agentId, skillName, skillType, agentEndpoint);
            
            byte[] data = broadcastMessage.getBytes();
            InetAddress address = InetAddress.getByName(broadcastAddress);
            DatagramPacket packet = new DatagramPacket(data, data.length, address, discoveryPort);
            
            int attempts = 0;
            while (isRunning && !isJoined && attempts < MAX_RETRIES) {
                socket.send(packet);
                logger.info("Sent discovery broadcast (attempt {}): {}", attempts + 1, broadcastMessage);
                attempts++;
                Thread.sleep(BROADCAST_INTERVAL);
            }
            
            if (attempts >= MAX_RETRIES && !isJoined) {
                logger.warn("Max discovery attempts reached without response");
            }
        } catch (IOException | InterruptedException e) {
            if (isRunning) {
                logger.error("Error in discovery broadcast thread: {}", e.getMessage());
            }
        }
    }

    private void listenForResponses() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            while (isRunning) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                
                String message = new String(packet.getData(), 0, packet.getLength());
                if (message.startsWith(JOIN_RESPONSE_PREFIX)) {
                    processJoinResponse(message);
                }
            }
        } catch (IOException e) {
            if (isRunning) {
                logger.error("Error in response listener thread: {}", e.getMessage());
            }
        }
    }

    private void processJoinResponse(String message) {
        String[] parts = message.substring(JOIN_RESPONSE_PREFIX.length()).split(";");
        if (parts.length >= 3 && parts[0].equals(agentId)) {
            String sceneId = parts[1];
            String status = parts[2];
            
            if ("SUCCESS".equals(status)) {
                assignedSceneId = sceneId;
                isJoined = true;
                logger.info("Successfully joined scene: {}", sceneId);
            } else {
                logger.warn("Failed to join scene: {}", status);
            }
        }
    }

    public boolean isJoined() {
        return isJoined;
    }

    public String getSceneId() {
        return assignedSceneId;
    }

    public String getAgentId() {
        return agentId;
    }

    @PreDestroy
    public void shutdown() {
        isRunning = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        logger.info("Discovery service shutdown for Skill B");
    }
}