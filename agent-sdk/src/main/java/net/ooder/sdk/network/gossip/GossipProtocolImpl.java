package net.ooder.sdk.network.gossip;

import net.ooder.sdk.network.udp.UDPSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GossipProtocolImpl implements GossipProtocol {
    private static final Logger log = LoggerFactory.getLogger(GossipProtocolImpl.class);
    private final UDPSDK udpSDK;
    private final String localNodeId;
    private final List<String> nodes;
    private final ScheduledExecutorService executor;
    private volatile boolean running;
    
    public GossipProtocolImpl(UDPSDK udpSDK, String localNodeId) {
        this.udpSDK = udpSDK;
        this.localNodeId = localNodeId;
        this.nodes = new CopyOnWriteArrayList<>();
        this.executor = Executors.newScheduledThreadPool(2);
        this.running = false;
    }
    
    @Override
    public void start() {
        if (!running) {
            running = true;
            executor.scheduleAtFixedRate(this::sendHeartbeat, 0, 5, TimeUnit.SECONDS);
            log.info("Gossip protocol started for node: {}", localNodeId);
        }
    }
    
    @Override
    public void stop() {
        if (running) {
            running = false;
            executor.shutdown();
            log.info("Gossip protocol stopped for node: {}", localNodeId);
        }
    }
    
    @Override
    public CompletableFuture<Boolean> sendGossip(Object message) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        if (!running || nodes.isEmpty()) {
            future.complete(false);
            return future;
        }
        
        try {
            // 随机选择几个节点发送消息（Gossip 协议的核心）
            int fanout = Math.min(3, nodes.size());
            java.util.Random random = new java.util.Random();
            java.util.concurrent.atomic.AtomicInteger successCount = new java.util.concurrent.atomic.AtomicInteger(0);
            int totalCount = 0;
            
            for (int i = 0; i < fanout; i++) {
                final int currentIndex = i;
                String targetNodeId = nodes.get(random.nextInt(nodes.size()));
                if (!targetNodeId.equals(localNodeId)) {
                    totalCount++;
                    final int finalTotalCount = totalCount;
                    GossipMessage gossipMessage = new GossipMessage(
                            localNodeId,
                            targetNodeId,
                            GossipMessageType.LINK_TABLE_UPDATE,
                            message
                    );
                    
                    // 发送 Gossip 消息
                    udpSDK.sendGossip(gossipMessage)
                            .thenAccept(result -> {
                                if (result.isSuccess()) {
                                    successCount.incrementAndGet();
                                } else {
                                    log.error("Failed to send gossip to node: {}", targetNodeId);
                                }
                                
                                if (successCount.get() + (finalTotalCount - currentIndex - 1) == 0) {
                                    future.complete(false);
                                } else if (successCount.get() > 0 && currentIndex == fanout - 1) {
                                    future.complete(true);
                                }
                            });
                }
            }
            
            if (totalCount == 0) {
                future.complete(false);
            }
        } catch (Exception e) {
            log.error("Error sending gossip: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public void handleGossipMessage(GossipMessage message) {
        if (!message.getSenderId().equals(localNodeId)) {
            log.info("Received gossip message from: {}", message.getSenderId());
            
            // 处理消息内容
            switch (message.getMessageType()) {
                case LINK_TABLE_UPDATE:
                    // 处理链路表更新
                    handleLinkTableUpdate(message);
                    break;
                case NODE_STATUS_UPDATE:
                    // 处理节点状态更新
                    handleNodeStatusUpdate(message);
                    break;
                case CPA_BUILD:
                    // 处理 CPA 构建
                    handleCpaBuild(message);
                    break;
                case HEARTBEAT:
                    // 处理心跳消息
                    handleHeartbeat(message);
                    break;
                default:
                    log.debug("Received unknown gossip message type: {}", message.getMessageType());
            }
            
            // 转发消息给其他节点（Gossip 协议的核心）
            forwardGossip(message);
        }
    }
    
    private void handleLinkTableUpdate(GossipMessage message) {
        // 处理链路表更新的逻辑
        log.debug("Handling link table update from: {}", message.getSenderId());
        // 这里应该更新本地链路表
    }
    
    private void handleNodeStatusUpdate(GossipMessage message) {
        // 处理节点状态更新的逻辑
        log.debug("Handling node status update from: {}", message.getSenderId());
    }
    
    private void handleCpaBuild(GossipMessage message) {
        // 处理 CPA 构建的逻辑
        log.debug("Handling CPA build from: {}", message.getSenderId());
    }
    
    private void handleHeartbeat(GossipMessage message) {
        // 处理心跳消息的逻辑
        log.debug("Handling heartbeat from: {}", message.getSenderId());
    }
    
    private void forwardGossip(GossipMessage message) {
        // 转发 Gossip 消息给其他节点
        if (running && !nodes.isEmpty()) {
            int fanout = Math.min(2, nodes.size());
            java.util.Random random = new java.util.Random();
            
            for (int i = 0; i < fanout; i++) {
                String targetNodeId = nodes.get(random.nextInt(nodes.size()));
                if (!targetNodeId.equals(localNodeId) && !targetNodeId.equals(message.getSenderId())) {
                    GossipMessageType messageType;
                    try {
                        messageType = GossipMessageType.valueOf(message.getType());
                    } catch (IllegalArgumentException e) {
                        messageType = GossipMessageType.OTHER;
                    }
                    
                    GossipMessage forwardedMessage = new GossipMessage(
                            localNodeId,
                            targetNodeId,
                            messageType,
                            message.getContent()
                    );
                    
                    udpSDK.sendGossip(forwardedMessage)
                            .thenAccept(result -> {
                                if (!result.isSuccess()) {
                                    log.error("Failed to forward gossip to node: {}", targetNodeId);
                                }
                            });
                }
            }
        }
    }
    
    private void sendHeartbeat() {
        if (running && !nodes.isEmpty()) {
            GossipMessage heartbeatMessage = new GossipMessage(
                    localNodeId,
                    "", // 广播
                    GossipMessageType.HEARTBEAT,
                    java.util.Collections.singletonMap("status", "online")
            );
            
            for (String nodeId : nodes) {
                if (!nodeId.equals(localNodeId)) {
                    GossipMessage nodeHeartbeat = new GossipMessage(
                            localNodeId,
                            nodeId,
                            GossipMessageType.HEARTBEAT,
                            java.util.Collections.singletonMap("status", "online")
                    );
                    
                    udpSDK.sendGossip(nodeHeartbeat)
                            .thenAccept(result -> {
                                if (!result.isSuccess()) {
                                    log.debug("Failed to send heartbeat to node: {}", nodeId);
                                }
                            });
                }
            }
        }
    }
    
    @Override
    public void addNode(String nodeId) {
        if (!nodes.contains(nodeId)) {
            nodes.add(nodeId);
            log.info("Added node to gossip protocol: {}", nodeId);
        }
    }
    
    @Override
    public void removeNode(String nodeId) {
        if (nodes.remove(nodeId)) {
            log.info("Removed node from gossip protocol: {}", nodeId);
        }
    }
    
    @Override
    public List<String> getNodes() {
        return nodes;
    }
    
    @Override
    public boolean isRunning() {
        return running;
    }
}
