package net.ooder.sdk.api.security.impl;

import net.ooder.sdk.api.security.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class EncryptionServiceImpl implements EncryptionService {
    
    private static final Logger log = LoggerFactory.getLogger(EncryptionServiceImpl.class);
    
    private final Map<String, SessionKey> sessionKeys;
    private final ExecutorService executor;
    private final ScheduledExecutorService cleanupScheduler;
    
    public EncryptionServiceImpl() {
        this.sessionKeys = new ConcurrentHashMap<String, SessionKey>();
        this.executor = Executors.newCachedThreadPool();
        this.cleanupScheduler = Executors.newSingleThreadScheduledExecutor();
        
        cleanupScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                cleanupExpiredKeys();
            }
        }, 60, 60, TimeUnit.SECONDS);
        
        log.info("EncryptionServiceImpl initialized");
    }
    
    @Override
    public CompletableFuture<SessionKey> generateSessionKey(String targetPeerId) {
        return CompletableFuture.supplyAsync(new java.util.function.Supplier<SessionKey>() {
            @Override
            public SessionKey get() {
                log.debug("Generating session key for peer: {}", targetPeerId);
                
                SessionKey sessionKey = new SessionKey();
                sessionKey.setSessionId("session-" + UUID.randomUUID().toString().substring(0, 8));
                sessionKey.setPeerId(targetPeerId);
                sessionKey.setKey(generateRandomKey());
                sessionKey.setCreatedAt(System.currentTimeMillis());
                sessionKey.setExpiresAt(System.currentTimeMillis() + 3600000);
                sessionKey.setStatus(SessionKeyStatus.ACTIVE);
                
                sessionKeys.put(sessionKey.getSessionId(), sessionKey);
                
                log.debug("Session key generated: {}", sessionKey.getSessionId());
                return sessionKey;
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<byte[]> encryptWithSessionKey(String sessionId, byte[] data) {
        return CompletableFuture.supplyAsync(new java.util.function.Supplier<byte[]>() {
            @Override
            public byte[] get() {
                SessionKey sessionKey = sessionKeys.get(sessionId);
                if (sessionKey == null) {
                    throw new RuntimeException("Session key not found: " + sessionId);
                }
                
                if (!sessionKey.isValid()) {
                    throw new RuntimeException("Session key is not valid: " + sessionId);
                }
                
                return xorEncrypt(data, sessionKey.getKey());
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<byte[]> decryptWithSessionKey(String sessionId, byte[] data) {
        return CompletableFuture.supplyAsync(new java.util.function.Supplier<byte[]>() {
            @Override
            public byte[] get() {
                SessionKey sessionKey = sessionKeys.get(sessionId);
                if (sessionKey == null) {
                    throw new RuntimeException("Session key not found: " + sessionId);
                }
                
                if (!sessionKey.isValid()) {
                    throw new RuntimeException("Session key is not valid: " + sessionId);
                }
                
                return xorEncrypt(data, sessionKey.getKey());
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> destroySessionKey(String sessionId) {
        return CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                SessionKey sessionKey = sessionKeys.remove(sessionId);
                if (sessionKey != null) {
                    sessionKey.setStatus(SessionKeyStatus.DESTROYED);
                    log.debug("Session key destroyed: {}", sessionId);
                }
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<SessionKeyStatus> getSessionKeyStatus(String sessionId) {
        return CompletableFuture.supplyAsync(new java.util.function.Supplier<SessionKeyStatus>() {
            @Override
            public SessionKeyStatus get() {
                SessionKey sessionKey = sessionKeys.get(sessionId);
                if (sessionKey == null) {
                    return null;
                }
                
                if (sessionKey.isExpired()) {
                    sessionKey.setStatus(SessionKeyStatus.EXPIRED);
                }
                
                return sessionKey.getStatus();
            }
        }, executor);
    }
    
    @Override
    public void shutdown() {
        log.info("Shutting down EncryptionService");
        executor.shutdown();
        cleanupScheduler.shutdown();
        sessionKeys.clear();
        log.info("EncryptionService shutdown complete");
    }
    
    private String generateRandomKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
    
    private byte[] xorEncrypt(byte[] data, String key) {
        byte[] result = new byte[data.length];
        byte[] keyBytes = key.getBytes();
        
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ keyBytes[i % keyBytes.length]);
        }
        
        return result;
    }
    
    private void cleanupExpiredKeys() {
        log.debug("Running session key cleanup");
        Iterator<Map.Entry<String, SessionKey>> it = sessionKeys.entrySet().iterator();
        
        while (it.hasNext()) {
            Map.Entry<String, SessionKey> entry = it.next();
            SessionKey sessionKey = entry.getValue();
            
            if (sessionKey.isExpired()) {
                sessionKey.setStatus(SessionKeyStatus.EXPIRED);
                it.remove();
                log.debug("Removed expired session key: {}", entry.getKey());
            }
        }
    }
    
    public int getActiveSessionCount() {
        int count = 0;
        for (SessionKey key : sessionKeys.values()) {
            if (key.isValid()) {
                count++;
            }
        }
        return count;
    }
}
