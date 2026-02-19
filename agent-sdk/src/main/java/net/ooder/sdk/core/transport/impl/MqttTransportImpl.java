package net.ooder.sdk.core.transport.impl;

import net.ooder.sdk.core.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MqttTransportImpl implements CoreTransport {
    
    private static final Logger log = LoggerFactory.getLogger(MqttTransportImpl.class);
    
    private final String transportId;
    private final String brokerUrl;
    private final String clientId;
    
    private String username;
    private String password;
    private int qos = 1;
    private boolean cleanSession = true;
    private int keepAliveInterval = 60;
    private int connectionTimeout = 30;
    
    private boolean retransmitEnabled = true;
    private int maxRetransmitCount = 3;
    
    private final List<TransportHandler> handlers = new CopyOnWriteArrayList<>();
    private final Map<String, AckListener> ackListeners = new ConcurrentHashMap<>();
    private final Map<String, TransportMessage> pendingAcks = new ConcurrentHashMap<>();
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicInteger qosValue = new AtomicInteger(1);
    
    private ScheduledExecutorService scheduler;
    private Object mqttClient;
    
    public MqttTransportImpl(String brokerUrl, String clientId) {
        this.transportId = "mqtt-" + UUID.randomUUID().toString().substring(0, 8);
        this.brokerUrl = brokerUrl;
        this.clientId = clientId;
    }
    
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setCleanSession(boolean cleanSession) { this.cleanSession = cleanSession; }
    public void setKeepAliveInterval(int seconds) { this.keepAliveInterval = seconds; }
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
                log.info("MQTT transport started: {} -> {}", transportId, brokerUrl);
            } catch (Exception e) {
                log.error("Failed to start MQTT transport", e);
                running.set(false);
                throw new RuntimeException("Failed to start MQTT transport", e);
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
            
            log.info("MQTT transport stopped: {}", transportId);
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
                String topic = buildTopic(message);
                byte[] payload = message.getPayload();
                if (payload == null) {
                    payload = new byte[0];
                }
                
                publish(topic, payload, qosValue.get());
                
                Map<String, String> headers = message.getHeaders();
                if (headers != null && "true".equals(headers.get("requiresAck"))) {
                    pendingAcks.put(message.getMessageId(), message);
                }
                
                log.debug("Message transmitted: {} -> {}", message.getMessageId(), topic);
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
        pendingAcks.remove(messageId);
        
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
    
    public void subscribe(String topic) {
        if (!running.get()) {
            throw new IllegalStateException("Transport not running");
        }
        
        try {
            doSubscribe(topic, qosValue.get());
            log.info("Subscribed to topic: {}", topic);
        } catch (Exception e) {
            log.error("Failed to subscribe to topic: {}", topic, e);
            throw new RuntimeException("Failed to subscribe", e);
        }
    }
    
    public void unsubscribe(String topic) {
        if (!running.get()) {
            return;
        }
        
        try {
            doUnsubscribe(topic);
            log.info("Unsubscribed from topic: {}", topic);
        } catch (Exception e) {
            log.error("Failed to unsubscribe from topic: {}", topic, e);
        }
    }
    
    private void connect() throws Exception {
        log.info("Connecting to MQTT broker: {}", brokerUrl);
        
        mqttClient = new Object();
        
        scheduler.scheduleAtFixedRate(
            this::checkPendingAcks,
            5000,
            5000,
            TimeUnit.MILLISECONDS
        );
    }
    
    private void disconnect() throws Exception {
        log.info("Disconnecting from MQTT broker");
        
        if (mqttClient != null) {
            mqttClient = null;
        }
    }
    
    private void publish(String topic, byte[] payload, int qos) throws Exception {
        log.debug("Publishing to topic: {} (qos={})", topic, qos);
    }
    
    private void doSubscribe(String topic, int qos) throws Exception {
        log.debug("Subscribing to topic: {} (qos={})", topic, qos);
    }
    
    private void doUnsubscribe(String topic) throws Exception {
        log.debug("Unsubscribing from topic: {}", topic);
    }
    
    private String buildTopic(TransportMessage message) {
        String target = message.getTarget();
        if (target == null || target.isEmpty()) {
            target = "default";
        }
        
        return "ooder/msg/" + target;
    }
    
    private void checkPendingAcks() {
        long now = System.currentTimeMillis();
        List<String> timeoutIds = new ArrayList<>();
        
        for (Map.Entry<String, TransportMessage> entry : pendingAcks.entrySet()) {
            TransportMessage msg = entry.getValue();
            if (now - msg.getTimestamp() > 30000) {
                timeoutIds.add(entry.getKey());
            }
        }
        
        for (String messageId : timeoutIds) {
            TransportMessage msg = pendingAcks.remove(messageId);
            if (msg != null) {
                AckListener listener = ackListeners.remove(messageId);
                if (listener != null) {
                    listener.onAckReceived(messageId, AckStatus.TIMEOUT);
                }
                log.warn("Message ack timeout: {}", messageId);
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
    
    public String getBrokerUrl() { return brokerUrl; }
    public String getClientId() { return clientId; }
}
