package net.ooder.sdk.system.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
import net.ooder.sdk.network.udp.PortManager;

/**
 * 统一的服务发现服务，用于技能模块的自动发现和注册
 */
@Service
@ConfigurationProperties(prefix = "skill.discovery")
public class DiscoveryService {
    private static final Logger log = LoggerFactory.getLogger(DiscoveryService.class);
    private static final String DISCOVERY_PREFIX = "DISCOVER:SKILL:";
    private static final String JOIN_RESPONSE_PREFIX = "JOIN_RESPONSE:SKILL:";

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private DatagramSocket socket;
    private boolean isRunning = false;
    private boolean isJoined = false;
    private String assignedSceneId;
    private final String agentId = UUID.randomUUID().toString();

    // 配置属性
    private int bufferSize = 1024;
    private int broadcastInterval = 5000;
    private int maxRetries = 10;
    private int port = 5000;
    private String broadcastAddress = "255.255.255.255";
    private String skillName = "DefaultSkill";
    private String skillType = "skill";
    private int skillPort = 9000;
    
    @Autowired
    private PortManager portManager;
    
    @PostConstruct
    public void initialize() {
        try {
            // 使用端口管理器分配端口
            int discoveryPort = portManager.allocatePort(PortManager.ServiceType.DISCOVERY);
            if (discoveryPort != this.port) {
                log.info("Discovery port changed from {} to {}", this.port, discoveryPort);
                this.port = discoveryPort;
            }
            
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            isRunning = true;
            log.info("Discovery service initialized for Skill: {}", skillName);
            startDiscoveryProcess();
        } catch (Exception e) {
            log.error("Failed to initialize discovery service: {}", e.getMessage());
        }
    }

    private void startDiscoveryProcess() {
        // 启动广播线程
        executorService.submit(this::sendDiscoveryBroadcasts);
        // 启动监听线程
        executorService.submit(this::listenForResponses);
    }

    private void sendDiscoveryBroadcasts() {
        try {
            String agentEndpoint = String.format("http://localhost:%d", skillPort);
            String broadcastMessage = String.format("%s%s;%s;%s;%s", 
                    DISCOVERY_PREFIX, agentId, skillName, skillType, agentEndpoint);
            
            byte[] data = broadcastMessage.getBytes();
            InetAddress address = InetAddress.getByName(broadcastAddress);
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            
            int attempts = 0;
            while (isRunning && !isJoined && attempts < maxRetries) {
                socket.send(packet);
                log.info("Sent discovery broadcast (attempt {}): {}", attempts + 1, broadcastMessage);
                attempts++;
                Thread.sleep(broadcastInterval);
            }
            
            if (attempts >= maxRetries && !isJoined) {
                log.warn("Max discovery attempts reached without response");
            }
        } catch (IOException | InterruptedException e) {
            if (isRunning) {
                log.error("Error in discovery broadcast thread: {}", e.getMessage());
            }
        }
    }

    private void listenForResponses() {
        try {
            byte[] buffer = new byte[bufferSize];
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
                log.error("Error in response listener thread: {}", e.getMessage());
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
                log.info("Successfully joined scene: {}", sceneId);
            } else {
                log.warn("Failed to join scene: {}", status);
            }
        }
    }

    /**
     * 检查是否已成功加入场景
     */
    public boolean isJoined() {
        return isJoined;
    }

    /**
     * 获取分配的场景ID
     */
    public String getSceneId() {
        return assignedSceneId;
    }

    /**
     * 获取当前代理的ID
     */
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
        log.info("Discovery service shutdown for Skill: {}", skillName);
    }

    // getter和setter方法
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getBroadcastAddress() {
        return broadcastAddress;
    }

    public void setBroadcastAddress(String broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public int getSkillPort() {
        return skillPort;
    }

    public void setSkillPort(int skillPort) {
        this.skillPort = skillPort;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getBroadcastInterval() {
        return broadcastInterval;
    }

    public void setBroadcastInterval(int broadcastInterval) {
        this.broadcastInterval = broadcastInterval;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }
}
