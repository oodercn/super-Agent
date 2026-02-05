package net.ooder.skillcenter.p2p;

import net.ooder.skillcenter.distribution.SkillDistributionManager;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * P2P技能执行器，用于在P2P网络中执行技能
 */
public class P2PSkillExecutor {
    // 单例实例
    private static P2PSkillExecutor instance;
    
    // P2P节点管理器
    private P2PNodeManager nodeManager;
    
    // 技能分发管理器
    private SkillDistributionManager distributionManager;
    
    // 线程池，用于异步执行P2P技能
    private ExecutorService executorService;
    
    /**
     * 私有构造方法，初始化P2P技能执行器
     */
    private P2PSkillExecutor() {
        this.nodeManager = P2PNodeManager.getInstance();
        this.distributionManager = SkillDistributionManager.getInstance();
        this.executorService = Executors.newFixedThreadPool(10);
    }
    
    /**
     * 获取P2P技能执行器实例
     * @return P2P技能执行器实例
     */
    public static synchronized P2PSkillExecutor getInstance() {
        if (instance == null) {
            instance = new P2PSkillExecutor();
        }
        return instance;
    }
    
    /**
     * 在P2P网络中执行技能
     * @param skillId 技能ID
     * @param context 执行上下文
     * @return 技能执行结果
     * @throws SkillException 技能执行异常
     */
    public SkillResult executeSkill(String skillId, SkillContext context) throws SkillException {
        // 查找提供该技能的节点
        List<Node> nodes = nodeManager.findNodesBySkill(skillId);
        
        if (nodes == null || nodes.isEmpty()) {
            throw new SkillException(skillId, "No nodes found with skill: " + skillId, 
                                     SkillException.ErrorCode.SKILL_NOT_FOUND);
        }
        
        // 选择一个可用的节点（这里简单选择第一个节点）
        Node targetNode = selectTargetNode(nodes);
        
        if (targetNode == null) {
            throw new SkillException(skillId, "No available nodes found with skill: " + skillId, 
                                     SkillException.ErrorCode.SKILL_NOT_AVAILABLE);
        }
        
        // 执行远程技能调用
        return invokeRemoteSkill(targetNode, skillId, context);
    }
    
    /**
     * 异步在P2P网络中执行技能
     * @param skillId 技能ID
     * @param context 执行上下文
     * @return CompletableFuture，用于获取执行结果
     */
    public CompletableFuture<SkillResult> executeSkillAsync(String skillId, SkillContext context) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return executeSkill(skillId, context);
            } catch (SkillException e) {
                throw new RuntimeException(e);
            }
        }, executorService);
    }
    
    /**
     * 选择目标节点
     * @param nodes 提供技能的节点列表
     * @return 选中的目标节点
     */
    private Node selectTargetNode(List<Node> nodes) {
        // 简单实现：选择第一个在线节点
        for (Node node : nodes) {
            if (NodeStatus.ONLINE.equals(node.getStatus())) {
                return node;
            }
        }
        return null;
    }
    
    /**
     * 调用远程节点的技能
     * @param node 目标节点
     * @param skillId 技能ID
     * @param context 执行上下文
     * @return 技能执行结果
     * @throws SkillException 技能执行异常
     */
    private SkillResult invokeRemoteSkill(Node node, String skillId, SkillContext context) throws SkillException {
        // 模拟远程技能调用
        System.out.println("Invoking skill " + skillId + " on node " + node.getName() + " (" + node.getIp() + ":" + node.getPort() + ")");
        
        // 实际项目中，这里应该实现与远程节点的通信，调用其技能
        // 例如：使用REST API、gRPC或其他通信协议
        
        // 检查本地缓存中是否有该技能
        net.ooder.skillcenter.model.Skill cachedSkill = distributionManager.getSkillFromCache(skillId);
        
        SkillResult result;
        if (cachedSkill != null) {
            // 使用缓存的技能执行
            System.out.println("Using cached skill for execution");
            result = cachedSkill.execute(context);
        } else {
            // 模拟技能执行结果
            result = new SkillResult();
            result.setMessage("Skill executed successfully on remote node: " + node.getName());
            result.addData("skillId", skillId);
            result.addData("targetNode", node.getName());
            result.addData("targetNodeIp", node.getIp());
            result.addData("executionMode", "p2p");
            
            // 模拟根据技能ID返回不同的结果
            if ("media-streaming-skill".equals(skillId)) {
                result.addData("streamUrl", "http://" + node.getIp() + ":" + node.getPort() + "/stream");
                result.addData("mediaType", "video/mp4");
            } else if ("file-storage-skill".equals(skillId)) {
                result.addData("storageUrl", "http://" + node.getIp() + ":" + node.getPort() + "/storage");
                result.addData("availableSpace", "100GB");
            } else if ("device-control-skill".equals(skillId)) {
                result.addData("deviceStatus", "controlled");
                result.addData("controlResult", "success");
            }
        }
        
        return result;
    }
    
    /**
     * 关闭P2P技能执行器，释放资源
     */
    public void shutdown() {
        executorService.shutdown();
    }
}