package net.ooder.skillcenter.p2p;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * P2P节点管理器，负责管理P2P网络中的节点
 */
public class P2PNodeManager {
    // 单例实例
    private static P2PNodeManager instance;
    
    // 本地节点信息
    private Node localNode;
    
    // 已发现的节点列表，key为节点ID，value为节点信息
    private Map<String, Node> discoveredNodes;
    
    // 共享技能映射，key为技能ID，value为提供该技能的节点列表
    private Map<String, List<Node>> sharedSkills;
    
    // 线程池，用于异步执行P2P操作
    private ExecutorService executorService;
    
    // P2P事件监听器列表
    private List<P2PEventListener> eventListeners;
    
    /**
     * 私有构造方法，初始化P2P节点管理器
     */
    private P2PNodeManager() {
        this.discoveredNodes = new ConcurrentHashMap<>();
        this.sharedSkills = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(10);
        this.eventListeners = Collections.synchronizedList(new ArrayList<>());
        this.localNode = createLocalNode();
    }
    
    /**
     * 获取P2P节点管理器实例
     * @return P2P节点管理器实例
     */
    public static synchronized P2PNodeManager getInstance() {
        if (instance == null) {
            instance = new P2PNodeManager();
        }
        return instance;
    }
    
    /**
     * 创建本地节点
     * @return 本地节点信息
     */
    private Node createLocalNode() {
        Node node = new Node();
        node.setId(UUID.randomUUID().toString());
        node.setName("SuperAgent-" + System.getProperty("user.name"));
        node.setType(NodeType.PERSONAL);
        node.setIp(getLocalIp());
        node.setPort(8080); // 默认端口
        node.setStatus(NodeStatus.ONLINE);
        node.setLastSeen(System.currentTimeMillis());
        
        return node;
    }
    
    /**
     * 获取本地IP地址
     * @return 本地IP地址
     */
    private String getLocalIp() {
        // 简单实现，返回本地IP地址
        return "127.0.0.1";
    }
    
    /**
     * 启动P2P服务
     */
    public void start() {
        // 启动P2P服务，开始监听和发现节点
        System.out.println("Starting P2P service for node: " + localNode.getName());
        
        // 启动节点发现线程
        executorService.submit(this::discoverNodes);
        
        // 启动心跳检测线程
        executorService.submit(this::heartbeat);
        
        notifyEventListeners(P2PEventType.SERVICE_STARTED, localNode);
    }
    
    /**
     * 停止P2P服务
     */
    public void stop() {
        // 停止P2P服务
        System.out.println("Stopping P2P service for node: " + localNode.getName());
        
        // 更新本地节点状态为离线
        localNode.setStatus(NodeStatus.OFFLINE);
        
        // 关闭线程池
        executorService.shutdown();
        
        notifyEventListeners(P2PEventType.SERVICE_STOPPED, localNode);
    }
    
    /**
     * 发现局域网内的节点
     */
    private void discoverNodes() {
        System.out.println("Discovering nodes in LAN...");
        
        // 实现UDP广播发现机制
        try {
            // 创建UDP套接字用于广播
            java.net.DatagramSocket socket = new java.net.DatagramSocket();
            socket.setBroadcast(true);
            
            // 构建发现消息
            String discoveryMessage = "SUPER_AGENT_DISCOVERY:" + localNode.getId() + ":" + localNode.getName();
            byte[] sendData = discoveryMessage.getBytes();
            
            // 发送广播到局域网
            java.net.InetAddress broadcastAddress = java.net.InetAddress.getByName("255.255.255.255");
            java.net.DatagramPacket sendPacket = new java.net.DatagramPacket(sendData, sendData.length, broadcastAddress, 8888);
            socket.send(sendPacket);
            System.out.println("Sent discovery broadcast: " + discoveryMessage);
            
            // 接收响应
            socket.setSoTimeout(5000); // 5秒超时
            byte[] receiveData = new byte[1024];
            
            while (true) {
                try {
                    java.net.DatagramPacket receivePacket = new java.net.DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
                    
                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Received discovery response: " + response);
                    
                    // 解析响应消息
                    if (response.startsWith("SUPER_AGENT_RESPONSE:")) {
                        String[] parts = response.split(":");
                        if (parts.length >= 4) {
                            String nodeId = parts[1];
                            String nodeName = parts[2];
                            String nodeIp = receivePacket.getAddress().getHostAddress();
                            int nodePort = Integer.parseInt(parts[3]);
                            
                            // 创建节点信息
                            Node node = new Node();
                            node.setId(nodeId);
                            node.setName(nodeName);
                            node.setType(NodeType.PERSONAL);
                            node.setIp(nodeIp);
                            node.setPort(nodePort);
                            node.setStatus(NodeStatus.ONLINE);
                            node.setLastSeen(System.currentTimeMillis());
                            
                            // 添加到已发现节点列表
                            addDiscoveredNode(node);
                        }
                    }
                } catch (java.net.SocketTimeoutException e) {
                    // 超时，结束接收
                    break;
                }
            }
            
            socket.close();
        } catch (Exception e) {
            System.err.println("Error in node discovery: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 如果没有发现节点，使用模拟数据
        if (discoveredNodes.isEmpty()) {
            System.out.println("No nodes discovered, using simulated data...");
            simulateNodeDiscovery();
        }
    }
    
    /**
     * 模拟节点发现
     */
    private void simulateNodeDiscovery() {
        // 模拟发现家庭网络中的其他设备
        Node node1 = new Node();
        node1.setId(UUID.randomUUID().toString());
        node1.setName("Home-Server");
        node1.setType(NodeType.HOME_SERVER);
        node1.setIp("192.168.1.100");
        node1.setPort(8080);
        node1.setStatus(NodeStatus.ONLINE);
        node1.setLastSeen(System.currentTimeMillis());
        
        Node node2 = new Node();
        node2.setId(UUID.randomUUID().toString());
        node2.setName("Family-Member-PC");
        node2.setType(NodeType.PERSONAL);
        node2.setIp("192.168.1.101");
        node2.setPort(8080);
        node2.setStatus(NodeStatus.ONLINE);
        node2.setLastSeen(System.currentTimeMillis());
        
        Node node3 = new Node();
        node3.setId(UUID.randomUUID().toString());
        node3.setName("Smart-TV");
        node3.setType(NodeType.IOT_DEVICE);
        node3.setIp("192.168.1.102");
        node3.setPort(8080);
        node3.setStatus(NodeStatus.ONLINE);
        node3.setLastSeen(System.currentTimeMillis());
        
        // 添加到已发现节点列表
        addDiscoveredNode(node1);
        addDiscoveredNode(node2);
        addDiscoveredNode(node3);
    }
    
    /**
     * 添加已发现的节点
     * @param node 节点信息
     */
    public void addDiscoveredNode(Node node) {
        if (node == null || node.getId() == null) {
            return;
        }
        
        discoveredNodes.put(node.getId(), node);
        notifyEventListeners(P2PEventType.NODE_DISCOVERED, node);
        
        // 获取该节点共享的技能
        fetchNodeSkills(node);
    }
    
    /**
     * 获取节点共享的技能
     * @param node 节点信息
     */
    private void fetchNodeSkills(Node node) {
        // 模拟获取节点共享的技能
        System.out.println("Fetching skills from node: " + node.getName());
        
        // 实际项目中，这里应该实现与远程节点的通信，获取其共享的技能列表
        
        // 模拟获取到的技能
        List<String> skills = simulateFetchSkills(node);
        
        // 更新共享技能映射
        for (String skillId : skills) {
            sharedSkills.computeIfAbsent(skillId, k -> new ArrayList<>()).add(node);
        }
        
        notifyEventListeners(P2PEventType.SKILLS_UPDATED, sharedSkills);
    }
    
    /**
     * 模拟获取节点共享的技能
     * @param node 节点信息
     * @return 技能ID列表
     */
    private List<String> simulateFetchSkills(Node node) {
        List<String> skills = new ArrayList<>();
        
        // 根据节点类型模拟不同的共享技能
        if (NodeType.HOME_SERVER.equals(node.getType())) {
            skills.add("media-streaming-skill");
            skills.add("file-storage-skill");
            skills.add("backup-skill");
        } else if (NodeType.PERSONAL.equals(node.getType())) {
            skills.add("code-generation-skill");
            skills.add("prototype-design-skill");
            skills.add("text-to-uppercase-skill");
        } else if (NodeType.IOT_DEVICE.equals(node.getType())) {
            skills.add("device-control-skill");
            skills.add("data-collection-skill");
        }
        
        return skills;
    }
    
    /**
     * 心跳检测，更新节点状态
     */
    private void heartbeat() {
        System.out.println("Starting heartbeat mechanism...");
        
        // 定期执行心跳检测
        while (true) {
            try {
                Thread.sleep(30000); // 每30秒执行一次心跳检测
                
                System.out.println("Performing heartbeat check...");
                
                // 检查已发现节点的状态
                long currentTime = System.currentTimeMillis();
                List<String> offlineNodes = new ArrayList<>();
                
                for (Map.Entry<String, Node> entry : discoveredNodes.entrySet()) {
                    Node node = entry.getValue();
                    
                    // 检查节点是否超时（超过60秒无响应）
                    if (currentTime - node.getLastSeen() > 60000) {
                        System.out.println("Node " + node.getName() + " is offline (timeout)");
                        node.setStatus(NodeStatus.OFFLINE);
                        offlineNodes.add(entry.getKey());
                        notifyEventListeners(P2PEventType.NODE_LOST, node);
                    } else {
                        // 向在线节点发送心跳请求
                        sendHeartbeat(node);
                    }
                }
                
                // 清理离线节点
                for (String nodeId : offlineNodes) {
                    discoveredNodes.remove(nodeId);
                    System.out.println("Removed offline node: " + nodeId);
                }
                
            } catch (InterruptedException e) {
                System.err.println("Heartbeat thread interrupted: " + e.getMessage());
                break;
            } catch (Exception e) {
                System.err.println("Error in heartbeat: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 向节点发送心跳请求
     * @param node 目标节点
     */
    private void sendHeartbeat(Node node) {
        try {
            // 创建UDP套接字
            java.net.DatagramSocket socket = new java.net.DatagramSocket();
            
            // 构建心跳消息
            String heartbeatMessage = "SUPER_AGENT_HEARTBEAT:" + localNode.getId();
            byte[] sendData = heartbeatMessage.getBytes();
            
            // 发送心跳请求
            java.net.InetAddress nodeAddress = java.net.InetAddress.getByName(node.getIp());
            java.net.DatagramPacket sendPacket = new java.net.DatagramPacket(sendData, sendData.length, nodeAddress, 8888);
            socket.send(sendPacket);
            
            // 接收心跳响应
            socket.setSoTimeout(2000); // 2秒超时
            byte[] receiveData = new byte[1024];
            
            try {
                java.net.DatagramPacket receivePacket = new java.net.DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (response.startsWith("SUPER_AGENT_HEARTBEAT_ACK:")) {
                    // 更新节点最后seen时间
                    node.setLastSeen(System.currentTimeMillis());
                    node.setStatus(NodeStatus.ONLINE);
                    System.out.println("Heartbeat successful for node: " + node.getName());
                }
            } catch (java.net.SocketTimeoutException e) {
                System.out.println("Heartbeat timeout for node: " + node.getName());
            }
            
            socket.close();
        } catch (Exception e) {
            System.err.println("Error sending heartbeat to node " + node.getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * 共享本地技能到P2P网络
     * @param skillId 技能ID
     */
    public void shareSkill(String skillId) {
        // 模拟共享技能
        System.out.println("Sharing skill: " + skillId + " to P2P network");
        
        // 实际项目中，这里应该实现将本地技能发布到P2P网络的逻辑
        
        notifyEventListeners(P2PEventType.SKILL_SHARED, skillId);
    }
    
    /**
     * 取消共享本地技能
     * @param skillId 技能ID
     */
    public void unshareSkill(String skillId) {
        // 模拟取消共享技能
        System.out.println("Unsharing skill: " + skillId + " from P2P network");
        
        // 实际项目中，这里应该实现将本地技能从P2P网络中移除的逻辑
        
        notifyEventListeners(P2PEventType.SKILL_UNSHARED, skillId);
    }
    
    /**
     * 查找提供指定技能的节点
     * @param skillId 技能ID
     * @return 提供该技能的节点列表
     */
    public List<Node> findNodesBySkill(String skillId) {
        return sharedSkills.getOrDefault(skillId, new ArrayList<>());
    }
    
    /**
     * 获取所有已发现的节点
     * @return 已发现的节点列表
     */
    public List<Node> getAllDiscoveredNodes() {
        return new ArrayList<>(discoveredNodes.values());
    }
    
    /**
     * 获取所有共享的技能
     * @return 共享的技能ID列表
     */
    public List<String> getAllSharedSkills() {
        return new ArrayList<>(sharedSkills.keySet());
    }
    
    /**
     * 添加P2P事件监听器
     * @param listener 事件监听器
     */
    public void addEventListener(P2PEventListener listener) {
        if (listener != null && !eventListeners.contains(listener)) {
            eventListeners.add(listener);
        }
    }
    
    /**
     * 移除P2P事件监听器
     * @param listener 事件监听器
     */
    public void removeEventListener(P2PEventListener listener) {
        eventListeners.remove(listener);
    }
    
    /**
     * 通知所有事件监听器
     * @param eventType 事件类型
     * @param data 事件数据
     */
    private void notifyEventListeners(P2PEventType eventType, Object data) {
        for (P2PEventListener listener : eventListeners) {
            listener.onEvent(eventType, data);
        }
    }
    
    /**
     * 获取本地节点信息
     * @return 本地节点信息
     */
    public Node getLocalNode() {
        return localNode;
    }
    
    /**
     * 设置本地节点信息
     * @param localNode 本地节点信息
     */
    public void setLocalNode(Node localNode) {
        this.localNode = localNode;
    }
}