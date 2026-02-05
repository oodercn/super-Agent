package net.ooder.sdk.network.udp;

import net.ooder.sdk.async.AsyncExecutorService;
import net.ooder.sdk.config.NetworkProperties;
import net.ooder.sdk.config.PerformanceProperties;
import net.ooder.sdk.config.RetryProperties;
import net.ooder.sdk.network.udp.monitoring.UDPMetricsCollector;
import net.ooder.sdk.network.packet.*;
import net.ooder.sdk.network.gossip.GossipMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class UDPSDK {
    private static final Logger log = LoggerFactory.getLogger(UDPSDK.class);
    
    private final DatagramSocket socket;
    private final int port;
    private final int bufferSize;
    private final int maxPacketSize;
    private final boolean strongTypeEnforcement;
    private final boolean allowCustomFormat;
    private final PacketValidation packetValidation;
    private final ConcurrentHashMap<String, Consumer<UDPPacket>> messageHandlers;
    private final PerformanceOptimizer performanceOptimizer;
    private UDPMessageHandler handler;
    private volatile boolean listening;
    
    private final NetworkProperties networkProperties;
    private final RetryProperties retryProperties;
    private final PerformanceProperties performanceProperties;
    private final UDPMetricsCollector metricsCollector;
    private final AsyncExecutorService asyncExecutorService;
    private final PortManager portManager;
    
    @Autowired
    public UDPSDK(NetworkProperties networkProperties,
                  RetryProperties retryProperties,
                  PerformanceProperties performanceProperties,
                  UDPMetricsCollector metricsCollector,
                  AsyncExecutorService asyncExecutorService,
                  PortManager portManager) throws Exception {
        this.networkProperties = networkProperties;
        this.retryProperties = retryProperties;
        this.performanceProperties = performanceProperties;
        this.metricsCollector = metricsCollector;
        this.asyncExecutorService = asyncExecutorService;
        this.portManager = portManager;
        
        int requestedPort = networkProperties.getDefaultPort();
        if (!portManager.isPortAvailable(requestedPort)) {
            requestedPort = portManager.allocatePort(PortManager.ServiceType.UDPSDK);
            log.info("端口{}被占用，分配替代端口: {}", networkProperties.getDefaultPort(), requestedPort);
        }
        
        this.port = requestedPort;
        this.bufferSize = networkProperties.getBufferSize();
        this.maxPacketSize = networkProperties.getMaxPacketSize();
        this.strongTypeEnforcement = true;
        this.allowCustomFormat = true;
        this.packetValidation = new DefaultPacketValidation();
        
        this.socket = new DatagramSocket(port);
        this.socket.setReuseAddress(networkProperties.isSocketReuse());
        this.socket.setBroadcast(networkProperties.isSocketBroadcast());
        this.socket.setSendBufferSize(bufferSize);
        this.socket.setReceiveBufferSize(bufferSize);
        this.socket.setSoTimeout(networkProperties.getTimeout());
        
        this.messageHandlers = new ConcurrentHashMap<>();
        this.performanceOptimizer = new PerformanceOptimizer(socket, networkProperties, retryProperties);
        this.performanceOptimizer.init();
        
        log.info("UDPSDK初始化完成，端口: {}, 缓冲区大小: {}", port, bufferSize);
    }
    
    public CompletableFuture<SendResult> sendHeartbeat(HeartbeatPacket packet) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                InetAddress broadcastAddress = InetAddress.getByName(networkProperties.getBroadcastAddress());
                int targetPort = networkProperties.getDefaultPort();
                return sendPacketInternal(packet, broadcastAddress, targetPort);
            } catch (Exception e) {
                log.error("发送心跳包失败", e);
                metricsCollector.recordError("HEARTBEAT_SEND", "sendHeartbeat", e.getMessage());
                throw new RuntimeException("发送心跳包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendHeartbeat(HeartbeatPacket packet, InetAddress address, int port) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                return sendPacketInternal(packet, address, port);
            } catch (Exception e) {
                log.error("发送心跳包失败", e);
                metricsCollector.recordError("HEARTBEAT_SEND", "sendHeartbeat", e.getMessage());
                throw new RuntimeException("发送心跳包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendCommand(CommandPacket packet) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                InetAddress broadcastAddress = InetAddress.getByName(networkProperties.getBroadcastAddress());
                int targetPort = networkProperties.getDefaultPort();
                return sendPacketInternal(packet, broadcastAddress, targetPort);
            } catch (Exception e) {
                log.error("发送命令包失败", e);
                metricsCollector.recordError("COMMAND_SEND", "sendCommand", e.getMessage());
                throw new RuntimeException("发送命令包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendCommand(CommandPacket packet, InetAddress address, int port) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                return sendPacketInternal(packet, address, port);
            } catch (Exception e) {
                log.error("发送命令包失败", e);
                metricsCollector.recordError("COMMAND_SEND", "sendCommand", e.getMessage());
                throw new RuntimeException("发送命令包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendStatusReport(StatusReportPacket packet) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                InetAddress broadcastAddress = InetAddress.getByName(networkProperties.getBroadcastAddress());
                int targetPort = networkProperties.getDefaultPort();
                return sendPacketInternal(packet, broadcastAddress, targetPort);
            } catch (Exception e) {
                log.error("发送状态报告包失败", e);
                metricsCollector.recordError("STATUS_REPORT_SEND", "sendStatusReport", e.getMessage());
                throw new RuntimeException("发送状态报告包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendStatusReport(StatusReportPacket packet, InetAddress address, int port) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                return sendPacketInternal(packet, address, port);
            } catch (Exception e) {
                log.error("发送状态报告包失败", e);
                metricsCollector.recordError("STATUS_REPORT_SEND", "sendStatusReport", e.getMessage());
                throw new RuntimeException("发送状态报告包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendTask(TaskPacket packet) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                InetAddress broadcastAddress = InetAddress.getByName(networkProperties.getBroadcastAddress());
                int targetPort = networkProperties.getDefaultPort();
                return sendPacketInternal(packet, broadcastAddress, targetPort);
            } catch (Exception e) {
                log.error("发送任务包失败", e);
                metricsCollector.recordError("TASK_SEND", "sendTask", e.getMessage());
                throw new RuntimeException("发送任务包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendTask(TaskPacket packet, InetAddress address, int port) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                return sendPacketInternal(packet, address, port);
            } catch (Exception e) {
                log.error("发送任务包失败", e);
                metricsCollector.recordError("TASK_SEND", "sendTask", e.getMessage());
                throw new RuntimeException("发送任务包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendAuth(AuthPacket packet) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                InetAddress broadcastAddress = InetAddress.getByName(networkProperties.getBroadcastAddress());
                int targetPort = networkProperties.getDefaultPort();
                return sendPacketInternal(packet, broadcastAddress, targetPort);
            } catch (Exception e) {
                log.error("发送认证包失败", e);
                metricsCollector.recordError("AUTH_SEND", "sendAuth", e.getMessage());
                throw new RuntimeException("发送认证包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendAuth(AuthPacket packet, InetAddress address, int port) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                return sendPacketInternal(packet, address, port);
            } catch (Exception e) {
                log.error("发送认证包失败", e);
                metricsCollector.recordError("AUTH_SEND", "sendAuth", e.getMessage());
                throw new RuntimeException("发送认证包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendRoute(RoutePacket packet) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                InetAddress broadcastAddress = InetAddress.getByName(networkProperties.getBroadcastAddress());
                int targetPort = networkProperties.getDefaultPort();
                return sendPacketInternal(packet, broadcastAddress, targetPort);
            } catch (Exception e) {
                log.error("发送路由包失败", e);
                metricsCollector.recordError("ROUTE_SEND", "sendRoute", e.getMessage());
                throw new RuntimeException("发送路由包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendRoute(RoutePacket packet, InetAddress address, int port) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                return sendPacketInternal(packet, address, port);
            } catch (Exception e) {
                log.error("发送路由包失败", e);
                metricsCollector.recordError("ROUTE_SEND", "sendRoute", e.getMessage());
                throw new RuntimeException("发送路由包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendResponse(ResponsePacket packet) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                InetAddress broadcastAddress = InetAddress.getByName(networkProperties.getBroadcastAddress());
                int targetPort = networkProperties.getDefaultPort();
                return sendPacketInternal(packet, broadcastAddress, targetPort);
            } catch (Exception e) {
                log.error("发送响应包失败", e);
                metricsCollector.recordError("RESPONSE_SEND", "sendResponse", e.getMessage());
                throw new RuntimeException("发送响应包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendResponse(ResponsePacket packet, InetAddress address, int port) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                return sendPacketInternal(packet, address, port);
            } catch (Exception e) {
                log.error("发送响应包失败", e);
                metricsCollector.recordError("RESPONSE_SEND", "sendResponse", e.getMessage());
                throw new RuntimeException("发送响应包失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendGossip(GossipMessage packet) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                InetAddress broadcastAddress = InetAddress.getByName(networkProperties.getBroadcastAddress());
                int targetPort = networkProperties.getDefaultPort();
                return sendPacketInternal(packet, broadcastAddress, targetPort);
            } catch (Exception e) {
                log.error("发送Gossip消息失败", e);
                metricsCollector.recordError("GOSSIP_SEND", "sendGossip", e.getMessage());
                throw new RuntimeException("发送Gossip消息失败", e);
            }
        });
    }
    
    public CompletableFuture<SendResult> sendGossip(GossipMessage packet, InetAddress address, int port) {
        return asyncExecutorService.executeAsync(() -> {
            try {
                return sendPacketInternal(packet, address, port);
            } catch (Exception e) {
                log.error("发送Gossip消息失败", e);
                metricsCollector.recordError("GOSSIP_SEND", "sendGossip", e.getMessage());
                throw new RuntimeException("发送Gossip消息失败", e);
            }
        });
    }
    
    private SendResult sendPacketInternal(UDPPacket packet, InetAddress address, int port) {
        final long startTime = System.currentTimeMillis();
        final byte[] originalData = packet.toJson().getBytes();
        final int originalLength = originalData.length;
        
        metricsCollector.recordPacketSent(originalLength, "sendPacket");
        
        byte[] data = originalData;
        if (performanceProperties.isCompressionEnabled() && originalLength > performanceProperties.getCompressionThreshold()) {
            data = compressData(originalData);
        }
        
        final byte[] sendData = data;
        final DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, address, port);
        
        try {
            return performanceOptimizer.executeWithRetry(() -> {
                try {
                    socket.send(datagramPacket);
                } catch (java.io.IOException e) {
                    throw new RuntimeException("发送数据包失败", e);
                }
                long latency = System.currentTimeMillis() - startTime;
                metricsCollector.recordLatency("sendPacket", latency);
                return SendResult.success(sendData.length);
            }, "sendPacket");
        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;
            metricsCollector.recordError("SEND_FAILURE", "sendPacket", e.getMessage());
            return SendResult.failure("发送失败: " + e.getMessage());
        }
    }
    
    private byte[] compressData(byte[] data) {
        try {
            java.util.zip.Deflater deflater = new java.util.zip.Deflater();
            deflater.setInput(data);
            deflater.finish();
            byte[] compressed = new byte[data.length];
            int compressedSize = deflater.deflate(compressed);
            deflater.end();
            
            byte[] result = new byte[compressedSize];
            System.arraycopy(compressed, 0, result, 0, compressedSize);
            
            metricsCollector.recordPacketSent(data.length - compressedSize, "compression");
            log.debug("数据压缩: 原始大小={}, 压缩后大小={}", data.length, compressedSize);
            return result;
        } catch (Exception e) {
            log.warn("数据压缩失败，使用原始数据", e);
            return data;
        }
    }
    
    public void registerMessageHandler(String messageType, Consumer<UDPPacket> handler) {
        messageHandlers.put(messageType, handler);
        log.debug("注册消息处理器: {}", messageType);
    }
    
    public void registerMessageHandler(UDPMessageHandler handler) {
        messageHandlers.put("heartbeat", packet -> handler.onHeartbeat((HeartbeatPacket) packet));
        messageHandlers.put("command", packet -> handler.onCommand((CommandPacket) packet));
        messageHandlers.put("status_report", packet -> handler.onStatusReport((StatusReportPacket) packet));
        messageHandlers.put("task", packet -> handler.onTask((TaskPacket) packet));
        messageHandlers.put("auth", packet -> handler.onAuth((AuthPacket) packet));
        messageHandlers.put("route", packet -> handler.onRoute((RoutePacket) packet));
        log.debug("注册UDPMessageHandler: {}", handler.getClass().getSimpleName());
    }
    
    public void unregisterMessageHandler(String messageType) {
        messageHandlers.remove(messageType);
        log.debug("取消注册消息处理器: {}", messageType);
    }
    
    public void startListening() {
        if (listening) {
            log.warn("UDP监听器已启动");
            return;
        }
        
        listening = true;
        asyncExecutorService.executeAsync(() -> {
            byte[] buffer = new byte[bufferSize];
            while (listening) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    
                    String jsonData = new String(packet.getData(), 0, packet.getLength());
                    UDPPacket udpPacket = parsePacket(jsonData);
                    
                    if (udpPacket != null) {
                        metricsCollector.recordPacketReceived(packet.getLength(), "receivePacket");
                        Consumer<UDPPacket> handler = messageHandlers.get(udpPacket.getType());
                        if (handler != null) {
                            handler.accept(udpPacket);
                        }
                    }
                } catch (java.net.SocketTimeoutException e) {
                    // 超时是正常的，继续监听
                } catch (Exception e) {
                    log.error("接收数据包失败", e);
                    metricsCollector.recordError("RECEIVE_FAILURE", "receivePacket", e.getMessage());
                }
            }
        });
        
        log.info("UDP监听器已启动，端口: {}", port);
    }
    
    public void stopListening() {
        listening = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        log.info("UDP监听器已停止");
    }
    
    private UDPPacket parsePacket(String jsonData) {
        try {
            if (!packetValidation.validate(jsonData)) {
                log.warn("数据包验证失败: {}", jsonData);
                metricsCollector.recordError("VALIDATION_FAILURE", "parsePacket", "数据包格式无效");
                return null;
            }
            
            String type = extractType(jsonData);
            switch (type) {
                case "heartbeat":
                    return HeartbeatPacket.fromJson(jsonData);
                case "command":
                    return CommandPacket.fromJson(jsonData);
                case "status_report":
                    return StatusReportPacket.fromJson(jsonData);
                case "task":
                    return TaskPacket.fromJson(jsonData);
                case "auth":
                    return AuthPacket.fromJson(jsonData);
                case "route":
                    return RoutePacket.fromJson(jsonData);
                case "response":
                    return ResponsePacket.fromJson(jsonData);
                case "gossip":
                    return GossipMessage.fromJson(jsonData);
                default:
                    log.warn("未知的数据包类型: {}", type);
                    return null;
            }
        } catch (Exception e) {
            log.error("解析数据包失败", e);
            metricsCollector.recordError("PARSE_FAILURE", "parsePacket", e.getMessage());
            return null;
        }
    }
    
    private String extractType(String jsonData) {
        int typeIndex = jsonData.indexOf("\"type\"");
        if (typeIndex == -1) {
            return "unknown";
        }
        
        int valueStart = jsonData.indexOf("\"", typeIndex + 6);
        int valueEnd = jsonData.indexOf("\"", valueStart + 1);
        if (valueStart == -1 || valueEnd == -1) {
            return "unknown";
        }
        
        return jsonData.substring(valueStart + 1, valueEnd);
    }
    
    public UDPMetricsCollector getMetricsCollector() {
        return metricsCollector;
    }
    
    public int getPort() {
        return port;
    }
    
    public boolean isListening() {
        return listening;
    }
}
