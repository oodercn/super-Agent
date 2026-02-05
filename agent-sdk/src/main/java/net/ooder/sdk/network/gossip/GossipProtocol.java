package net.ooder.sdk.network.gossip;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GossipProtocol {
    /**
     * 启动 Gossip 协议
     */
    void start();
    
    /**
     * 停止 Gossip 协议
     */
    void stop();
    
    /**
     * 发送 Gossip 消息
     */
    CompletableFuture<Boolean> sendGossip(Object message);
    
    /**
     * 处理接收到的 Gossip 消息
     */
    void handleGossipMessage(GossipMessage message);
    
    /**
     * 添加 Gossip 节点
     */
    void addNode(String nodeId);
    
    /**
     * 移除 Gossip 节点
     */
    void removeNode(String nodeId);
    
    /**
     * 获取所有 Gossip 节点
     */
    List<String> getNodes();
    
    /**
     * 检查 Gossip 协议是否正在运行
     */
    boolean isRunning();
}
