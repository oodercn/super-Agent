package net.ooder.sdk.core.transport.impl;

import net.ooder.sdk.core.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketTransportImpl implements CoreTransport {
    
    private static final Logger log = LoggerFactory.getLogger(WebSocketTransportImpl.class);
    
    private final String transportId;
    private final String serverUrl;
    
    private int reconnectInterval = 5000;
    private int maxReconnectAttempts = 5;
    private int connectionTimeout = 30;
    
    private boolean retransmitEnabled = true;
    private int maxRetransmitCount = 3;
    
    private final List<TransportHandler> handlers = new CopyOnWriteArrayList<>();
    private final Map<String, AckListener> ackListeners = new ConcurrentHashMap<>();
    private final Map<String, TransportMessage> pendingMessages = new ConcurrentHashMap<>();
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicInteger qosValue = new AtomicInteger(0);
    private final AtomicInteger reconnectAttempts = new AtomicInteger(0);
    
    private ScheduledExecutorService scheduler;
    private Object webSocket;
    
    public WebSocketTransportImpl(String serverUrl) {
        this.transportId = "ws-" + UUID.randomUUID().toString().substring(0, 8);
        this.serverUrl = serverUrl;
    }
    
    public void setReconnectInterval(int ms) { this.reconnectInterval = ms; }
    public void setMaxReconnectAttempts(int attempts) { this.maxReconnectAttempts = attempts; }
    public void setConnectionTimeout(int seconds) { this.connectionTimeout = seconds; }
    
    @Override
    public String getTransportId() { return transportId; }
    
    @Override
    public TransportType getTransportType() { return TransportType.P2P; }
    
    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            
            try {
                connect();
                log.info("WebSocket transport started: {} -> {}", transportId, serverUrl);
            } catch (Exception e) {
                log.error("Failed to start WebSocket transport", e);
                running.set(false);
                throw new RuntimeException("Failed to start WebSocket transport", e);
            }
        }
    }
    
    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            try {
                disconnect();
            } catch (Exception e) {
                log.error("Error during disconnect", e);
            }
            
            if (scheduler != null) {
                scheduler.shutdown();
            }
            
            log.info("WebSocket transport stopped: {}", transportId);
        }
    }
    
    @Override
    public boolean isRunning() { return running.get(); }
    
    @Override
    public CompletableFuture<TransportResult> transmit(TransportMessage message) {
        return CompletableFuture.supplyAsync(() -> {
            if (!running.get()) {
                return TransportResult.failure(message.getMessageId(), "TRANSPORT_ERROR", "Transport not running");
            }
            
            try {
                byte[] payload = message.getPayload();
                if (payload == null) {
                    payload = serializeToJson(message);
                }
                send(payload);
                
                pendingMessages.put(message.getMessageId(), message);
                
                log.debug("Message transmitted: {}", message.getMessageId());
                return TransportResult.success(message.getMessageId());
                
            } catch (Exception e) {
                log.error("Failed to transmit message: {}", message.getMessageId(), e);
                return TransportResult.failure(message.getMessageId(), "TRANSMIT_ERROR", e.getMessage());
            }
        });
    }
    
    @Override
    public void registerHandler(TransportHandler handler) {
        handlers.add(handler);
    }
    
    @Override
    public void unregisterHandler(TransportHandler handler) {
        handlers.remove(handler);
    }
    
    @Override
    public void acknowledge(String messageId) {
        pendingMessages.remove(messageId);
        
        AckListener listener = ackListeners.remove(messageId);
        if (listener != null) {
            listener.onAckReceived(messageId, AckStatus.DELIVERED);
        }
        
        log.debug("Message acknowledged: {}", messageId);
    }
    
    @Override
    public void setQos(int qos) {
        this.qosValue.set(qos);
    }
    
    @Override
    public int getQos() {
        return qosValue.get();
    }
    
    @Override
    public void setRetransmitEnabled(boolean enabled) {
        this.retransmitEnabled = enabled;
    }
    
    @Override
    public boolean isRetransmitEnabled() {
        return retransmitEnabled;
    }
    
    @Override
    public int getMaxRetransmitCount() {
        return maxRetransmitCount;
    }
    
    @Override
    public void setMaxRetransmitCount(int count) {
        this.maxRetransmitCount = count;
    }
    
    private void connect() throws Exception {
        log.info("Connecting to WebSocket server: {}", serverUrl);
        
        webSocket = new Object();
        reconnectAttempts.set(0);
        
        scheduler.scheduleAtFixedRate(
            this::checkPendingMessages,
            5000,
            5000,
            TimeUnit.MILLISECONDS
        );
    }
    
    private void disconnect() throws Exception {
        log.info("Disconnecting from WebSocket server");
        
        if (webSocket != null) {
            webSocket = null;
        }
    }
    
    private void send(byte[] message) throws Exception {
        log.debug("Sending WebSocket message: {} bytes", message.length);
    }
    
    private void onMessageReceived(byte[] message) {
        try {
            TransportMessage transportMsg = deserializeFromJson(new String(message, StandardCharsets.UTF_8));
            notifyHandlers(transportMsg);
        } catch (Exception e) {
            log.error("Failed to process received message", e);
        }
    }
    
    private void onConnected() {
        log.info("WebSocket connected");
        reconnectAttempts.set(0);
    }
    
    private void onDisconnected() {
        log.warn("WebSocket disconnected");
        
        if (running.get() && reconnectAttempts.get() < maxReconnectAttempts) {
            scheduler.schedule(
                this::attemptReconnect,
                reconnectInterval,
                TimeUnit.MILLISECONDS
            );
        }
    }
    
    private void attemptReconnect() {
        if (!running.get()) {
            return;
        }
        
        int attempts = reconnectAttempts.incrementAndGet();
        log.info("Attempting reconnect ({}/{})", attempts, maxReconnectAttempts);
        
        try {
            connect();
        } catch (Exception e) {
            log.error("Reconnect attempt failed", e);
            
            if (attempts < maxReconnectAttempts) {
                scheduler.schedule(
                    this::attemptReconnect,
                    reconnectInterval,
                    TimeUnit.MILLISECONDS
                );
            }
        }
    }
    
    private byte[] serializeToJson(TransportMessage message) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"messageId\":\"").append(message.getMessageId()).append("\",");
        sb.append("\"source\":\"").append(message.getSource() != null ? message.getSource() : "").append("\",");
        sb.append("\"target\":\"").append(message.getTarget() != null ? message.getTarget() : "").append("\",");
        sb.append("\"timestamp\":").append(message.getTimestamp());
        
        if (message.getHeaders() != null) {
            sb.append(",\"headers\":").append(mapToJsonString(message.getHeaders()));
        }
        
        sb.append("}");
        
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
    
    private TransportMessage deserializeFromJson(String json) {
        TransportMessage msg = new TransportMessage();
        msg.setMessageId(extractJsonString(json, "messageId"));
        msg.setSource(extractJsonString(json, "source"));
        msg.setTarget(extractJsonString(json, "target"));
        msg.setTimestamp(extractJsonLong(json, "timestamp"));
        return msg;
    }
    
    private String extractJsonString(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = json.indexOf(pattern);
        if (start < 0) return null;
        start += pattern.length();
        int end = json.indexOf("\"", start);
        if (end < 0) return null;
        return json.substring(start, end);
    }
    
    private long extractJsonLong(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start < 0) return 0;
        start += pattern.length();
        int end = json.indexOf(",", start);
        if (end < 0) end = json.indexOf("}", start);
        if (end < 0) return 0;
        try {
            return Long.parseLong(json.substring(start, end).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private String mapToJsonString(Map<String, String> map) {
        if (map == null) return "{}";
        
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
    
    private void checkPendingMessages() {
        long now = System.currentTimeMillis();
        List<String> timeoutIds = new ArrayList<>();
        
        for (Map.Entry<String, TransportMessage> entry : pendingMessages.entrySet()) {
            TransportMessage msg = entry.getValue();
            if (now - msg.getTimestamp() > 30000) {
                timeoutIds.add(entry.getKey());
            }
        }
        
        for (String messageId : timeoutIds) {
            TransportMessage msg = pendingMessages.remove(messageId);
            if (msg != null) {
                AckListener listener = ackListeners.remove(messageId);
                if (listener != null) {
                    listener.onAckReceived(messageId, AckStatus.TIMEOUT);
                }
                log.warn("Message timeout: {}", messageId);
            }
        }
    }
    
    private void notifyHandlers(TransportMessage message) {
        for (TransportHandler handler : handlers) {
            try {
                handler.onMessage(message);
            } catch (Exception e) {
                log.warn("TransportHandler error", e);
            }
        }
    }
    
    public String getServerUrl() { return serverUrl; }
}
