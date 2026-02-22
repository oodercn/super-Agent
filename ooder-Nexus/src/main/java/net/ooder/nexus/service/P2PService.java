package net.ooder.nexus.service;

import net.ooder.sdk.api.OoderSDK;
import net.ooder.sdk.api.protocol.CommandPacket;

import java.util.List;
import java.util.Map;

public interface P2PService {
    
    /**
     * 初始化P2P服务
     * @param ooderSDK OoderSDK实例
     */
    void initialize(OoderSDK ooderSDK);
    
    /**
     * 发现网络中的其他节点
     * @return 节点列表
     */
    List<Map<String, Object>> discoverNodes();
    
    /**
     * 加入P2P网络
     * @param nodeInfo 节点信息
     * @return 加入结果
     */
    boolean joinNetwork(Map<String, Object> nodeInfo);
    
    /**
     * 获取网络节点列表
     * @return 节点列表
     */
    List<Map<String, Object>> getNodes();
    
    /**
     * 获取节点详情
     * @param nodeId 节点ID
     * @return 节点详情
     */
    Map<String, Object> getNodeDetails(String nodeId);
    
    /**
     * 移除节点
     * @param nodeId 节点ID
     * @return 移除结果
     */
    boolean removeNode(String nodeId);
    
    /**
     * 发布技能到网络
     * @param skillInfo 技能信息
     * @return 发布结果
     */
    boolean publishSkill(Map<String, Object> skillInfo);
    
    /**
     * 订阅网络中的技能
     * @param skillId 技能ID
     * @return 订阅结果
     */
    boolean subscribeSkill(String skillId);
    
    /**
     * 获取技能市场列表
     * @return 技能列表
     */
    List<Map<String, Object>> getSkillMarket();
    
    /**
     * 获取网络状态
     * @return 网络状态
     */
    Map<String, Object> getNetworkStatus();
    
    /**
     * 发送P2P消息
     * @param targetNodeId 目标节点ID
     * @param message 消息内容
     * @return 发送结果
     */
    boolean sendMessage(String targetNodeId, Map<String, Object> message);
    
    /**
     * 处理接收到的P2P消息
     * @param packet 消息包
     */
    void handleMessage(CommandPacket packet);
}
