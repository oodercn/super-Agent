package net.ooder.sdk.core.msg.impl;

import net.ooder.sdk.api.msg.*;
import net.ooder.sdk.core.transport.CoreTransport;
import net.ooder.sdk.core.transport.TransportMessage;
import net.ooder.sdk.core.transport.TransportResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MsgClientProxyImpl<V> implements MsgClientProxy<V> {
    
    private static final Logger log = LoggerFactory.getLogger(MsgClientProxyImpl.class);
    
    private final String clientId;
    private final String sceneId;
    private final String groupId;
    private final Class<V> msgClass;
    
    private MsgClientConfig config;
    private CoreTransport transport;
    private LocalMsgCache<V> cache;
    
    private final Map<String, V> sentMessages = new ConcurrentHashMap<>();
    private final Map<String, V> receivedMessages = new ConcurrentHashMap<>();
    private final List<MessageListener<V>> listeners = new CopyOnWriteArrayList<>();
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    public MsgClientProxyImpl(String clientId, String sceneId, Class<V> msgClass) {
        this.clientId = clientId;
        this.sceneId = sceneId;
        this.groupId = sceneId + ":default";
        this.msgClass = msgClass;
    }
    
    public void setTransport(CoreTransport transport) {
        this.transport = transport;
    }
    
    @Override
    public String getClientId() { return clientId; }
    
    @Override
    public String getSceneId() { return sceneId; }
    
    @Override
    public String getGroupId() { return groupId; }
    
    @Override
    public void initialize(MsgClientConfig config) {
        this.config = config;
        
        if (config.isCacheEnabled()) {
            this.cache = new LocalMsgCache<>(
                config.getCacheMaxSize(),
                config.getCacheExpireTime()
            );
        }
        
        log.info("MsgClientProxy initialized: {} for scene: {}", clientId, sceneId);
    }
    
    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            if (transport != null) {
                transport.start();
            }
            
            scheduler.scheduleAtFixedRate(
                this::cleanupExpiredMessages,
                60000,
                60000,
                TimeUnit.MILLISECONDS
            );
            
            log.info("MsgClientProxy started: {}", clientId);
        }
    }
    
    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            scheduler.shutdown();
            
            if (transport != null) {
                transport.stop();
            }
            
            log.info("MsgClientProxy stopped: {}", clientId);
        }
    }
    
    @Override
    public boolean isRunning() { return running.get(); }
    
    @Override
    public CompletableFuture<V> getMsgById(String msgId) {
        return CompletableFuture.supplyAsync(() -> {
            if (cache != null) {
                V cached = cache.get(msgId);
                if (cached != null) {
                    return cached;
                }
            }
            
            V sent = sentMessages.get(msgId);
            if (sent != null) {
                return sent;
            }
            
            return receivedMessages.get(msgId);
        });
    }
    
    @Override
    public CompletableFuture<List<V>> getAllSendMsg() {
        return CompletableFuture.supplyAsync(() -> 
            new ArrayList<>(sentMessages.values())
        );
    }
    
    @Override
    public CompletableFuture<List<V>> getAllReceiveMsg() {
        return CompletableFuture.supplyAsync(() -> 
            new ArrayList<>(receivedMessages.values())
        );
    }
    
    @Override
    public CompletableFuture<List<V>> getMsgList(Map<String, Object> condition) {
        return CompletableFuture.supplyAsync(() -> {
            List<V> result = new ArrayList<>();
            
            String status = condition != null ? (String) condition.get("status") : null;
            String fromPersonId = condition != null ? (String) condition.get("fromPersonId") : null;
            String toPersonId = condition != null ? (String) condition.get("toPersonId") : null;
            
            for (V msg : sentMessages.values()) {
                if (matchesCondition(msg, status, fromPersonId, toPersonId)) {
                    result.add(msg);
                }
            }
            
            for (V msg : receivedMessages.values()) {
                if (matchesCondition(msg, status, fromPersonId, toPersonId)) {
                    result.add(msg);
                }
            }
            
            return result;
        });
    }
    
    private boolean matchesCondition(V msg, String status, String fromPersonId, String toPersonId) {
        if (msg instanceof MsgInfo) {
            MsgInfo msgInfo = (MsgInfo) msg;
            if (status != null && !status.equals(msgInfo.getStatus())) {
                return false;
            }
            if (fromPersonId != null && !fromPersonId.equals(msgInfo.getFromPersonId())) {
                return false;
            }
            if (toPersonId != null && !toPersonId.equals(msgInfo.getToPersonId())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public CompletableFuture<V> createMsg() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                V msg = msgClass.newInstance();
                if (msg instanceof MsgInfo) {
                    MsgInfo msgInfo = (MsgInfo) msg;
                    msgInfo.setMsgId(generateMsgId());
                    msgInfo.setCreateTime(System.currentTimeMillis());
                    msgInfo.setStatus("draft");
                }
                return msg;
            } catch (Exception e) {
                throw new RuntimeException("Failed to create message", e);
            }
        });
    }
    
    @Override
    public CompletableFuture<V> createMsg(String toPersonId) {
        return createMsg().thenApply(msg -> {
            if (msg instanceof MsgInfo) {
                ((MsgInfo) msg).setToPersonId(toPersonId);
            }
            return msg;
        });
    }
    
    @Override
    public CompletableFuture<V> createMsg(String toPersonId, Map<String, Object> content) {
        return createMsg(toPersonId).thenApply(msg -> {
            if (msg instanceof MsgInfo && content != null) {
                MsgInfo msgInfo = (MsgInfo) msg;
                if (content.containsKey("title")) {
                    msgInfo.setTitle(content.get("title").toString());
                }
                if (content.containsKey("body")) {
                    msgInfo.setBody(content.get("body").toString());
                }
            }
            return msg;
        });
    }
    
    @Override
    public CompletableFuture<V> updateMsg(V msg) {
        return CompletableFuture.supplyAsync(() -> {
            if (msg instanceof MsgInfo) {
                MsgInfo msgInfo = (MsgInfo) msg;
                msgInfo.setStatus("updated");
            }
            
            String msgId = getMsgId(msg);
            if (msgId != null) {
                sentMessages.put(msgId, msg);
                if (cache != null) {
                    cache.put(msgId, msg);
                }
            }
            
            return msg;
        });
    }
    
    @Override
    public CompletableFuture<Void> sendMsg(V msg) {
        return CompletableFuture.runAsync(() -> {
            if (transport == null) {
                throw new IllegalStateException("Transport not configured");
            }
            
            if (msg instanceof MsgInfo) {
                MsgInfo msgInfo = (MsgInfo) msg;
                msgInfo.setStatus("sent");
                msgInfo.setSendTime(System.currentTimeMillis());
            }
            
            TransportMessage transportMsg = toTransportMessage(msg);
            TransportResult result = transport.transmit(transportMsg).join();
            
            if (!result.isSuccess()) {
                throw new RuntimeException("Failed to send message: " + result.getErrorMessage());
            }
            
            String msgId = getMsgId(msg);
            if (msgId != null) {
                sentMessages.put(msgId, msg);
                if (cache != null) {
                    cache.put(msgId, msg);
                }
            }
            
            notifyMessageSent(msg);
            
            log.info("Message sent: {}", msgId);
        });
    }
    
    @Override
    public CompletableFuture<Map<String, Boolean>> sendMassMsg(V msg, List<String> personIds) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Boolean> results = new ConcurrentHashMap<>();
            
            for (String personId : personIds) {
                try {
                    V clonedMsg = cloneMsg(msg, personId);
                    sendMsg(clonedMsg).join();
                    results.put(personId, true);
                } catch (Exception e) {
                    log.error("Failed to send message to: {}", personId, e);
                    results.put(personId, false);
                }
            }
            
            return results;
        });
    }
    
    @Override
    public CompletableFuture<Void> deleteMsg(String msgId) {
        return CompletableFuture.runAsync(() -> {
            sentMessages.remove(msgId);
            receivedMessages.remove(msgId);
            
            if (cache != null) {
                cache.remove(msgId);
            }
            
            notifyMessageDeleted(msgId);
            
            log.info("Message deleted: {}", msgId);
        });
    }
    
    @Override
    public void addMessageListener(MessageListener<V> listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeMessageListener(MessageListener<V> listener) {
        listeners.remove(listener);
    }
    
    public void receiveMessage(V msg) {
        String msgId = getMsgId(msg);
        if (msgId != null) {
            receivedMessages.put(msgId, msg);
            
            if (cache != null) {
                cache.put(msgId, msg);
            }
        }
        
        notifyMessageReceived(msg);
    }
    
    private String generateMsgId() {
        return "msg_" + System.currentTimeMillis() + "_" + 
               UUID.randomUUID().toString().substring(0, 8);
    }
    
    private String getMsgId(V msg) {
        if (msg instanceof MsgInfo) {
            return ((MsgInfo) msg).getMsgId();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private V cloneMsg(V msg, String toPersonId) {
        try {
            V cloned = (V) msg.getClass().newInstance();
            if (cloned instanceof MsgInfo && msg instanceof MsgInfo) {
                MsgInfo src = (MsgInfo) msg;
                MsgInfo dst = (MsgInfo) cloned;
                dst.setMsgId(generateMsgId());
                dst.setFromPersonId(src.getFromPersonId());
                dst.setToPersonId(toPersonId);
                dst.setTitle(src.getTitle());
                dst.setBody(src.getBody());
                dst.setMsgType(src.getMsgType());
                dst.setCreateTime(System.currentTimeMillis());
                dst.setStatus("draft");
            }
            return cloned;
        } catch (Exception e) {
            throw new RuntimeException("Failed to clone message", e);
        }
    }
    
    private TransportMessage toTransportMessage(V msg) {
        TransportMessage transportMsg = new TransportMessage();
        transportMsg.setMessageId(getMsgId(msg));
        transportMsg.setSource(clientId);
        
        if (msg instanceof MsgInfo) {
            MsgInfo msgInfo = (MsgInfo) msg;
            transportMsg.setTarget(msgInfo.getToPersonId());
            
            StringBuilder payloadBuilder = new StringBuilder();
            payloadBuilder.append("{");
            payloadBuilder.append("\"title\":\"").append(escapeJson(msgInfo.getTitle())).append("\",");
            payloadBuilder.append("\"body\":\"").append(escapeJson(msgInfo.getBody())).append("\",");
            payloadBuilder.append("\"msgType\":\"").append(msgInfo.getMsgType() != null ? msgInfo.getMsgType() : "").append("\"");
            payloadBuilder.append("}");
            
            transportMsg.setPayload(payloadBuilder.toString().getBytes(StandardCharsets.UTF_8));
        }
        
        transportMsg.setTimestamp(System.currentTimeMillis());
        
        return transportMsg;
    }
    
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
    
    private void cleanupExpiredMessages() {
        if (cache != null) {
            cache.cleanupExpired();
        }
    }
    
    private void notifyMessageReceived(V msg) {
        for (MessageListener<V> listener : listeners) {
            try {
                listener.onMessageReceived(msg);
            } catch (Exception e) {
                log.warn("MessageListener error", e);
            }
        }
    }
    
    private void notifyMessageSent(V msg) {
        for (MessageListener<V> listener : listeners) {
            try {
                listener.onMessageSent(msg);
            } catch (Exception e) {
                log.warn("MessageListener error", e);
            }
        }
    }
    
    private void notifyMessageDeleted(String msgId) {
        for (MessageListener<V> listener : listeners) {
            try {
                listener.onMessageDeleted(msgId);
            } catch (Exception e) {
                log.warn("MessageListener error", e);
            }
        }
    }
    
    public int getSentCount() { return sentMessages.size(); }
    public int getReceivedCount() { return receivedMessages.size(); }
}
