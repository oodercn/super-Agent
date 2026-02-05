package net.ooder.sdk.network.udp;

import net.ooder.sdk.config.NetworkProperties;
import net.ooder.sdk.config.RetryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class PerformanceOptimizer {
    private static final Logger log = LoggerFactory.getLogger(PerformanceOptimizer.class);
    
    private final DatagramSocket socket;
    private final ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<String, Long> pendingAcks;
    private final NetworkProperties networkProperties;
    private final RetryProperties retryProperties;
    
    private boolean enabled = true;
    private int bufferSize;
    private int threadPoolSize;
    private boolean useNio = true;
    
    public PerformanceOptimizer(DatagramSocket socket, NetworkProperties networkProperties, 
                          RetryProperties retryProperties) {
        this.socket = socket;
        this.networkProperties = networkProperties;
        this.retryProperties = retryProperties;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.pendingAcks = new ConcurrentHashMap<>();
        
        this.bufferSize = networkProperties.getBufferSize();
        this.threadPoolSize = Runtime.getRuntime().availableProcessors();
        this.useNio = true;
    }
    
    public void init() {
        log.info("性能优化器初始化完成");
    }
    
    public void close() {
        scheduler.shutdown();
        log.info("性能优化器已关闭");
    }
    
    public byte[] optimizePacket(byte[] data) {
        if (!enabled) {
            return data;
        }
        
        if (shouldCompress(data)) {
            return compressData(data);
        }
        
        return data;
    }
    
    private boolean shouldCompress(byte[] data) {
        return data.length > 1024;
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
            
            log.debug("数据压缩: 原始大小={}, 压缩后大小={}", data.length, compressedSize);
            return result;
        } catch (Exception e) {
            log.warn("数据压缩失败，使用原始数据", e);
            return data;
        }
    }
    
    public <T> T executeWithRetry(Supplier<T> supplier, String operationName) throws Exception {
        int maxRetries = retryProperties.getMaxRetries();
        RetryProperties.RetryStrategy strategy = retryProperties.getStrategy();
        int retryCount = 0;
        
        while (retryCount < maxRetries) {
            try {
                return supplier.get();
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    log.error("操作{}失败，已达最大重试次数: {}", operationName, maxRetries);
                    throw e;
                }
                
                long delay = calculateDelay(strategy, retryCount, retryProperties.getDelayBase());
                log.warn("操作{}失败，第{}次重试，延迟{}ms", 
                    operationName, retryCount, delay);
                Thread.sleep(delay);
            }
        }
        
        return supplier.get();
    }
    
    private long calculateDelay(RetryProperties.RetryStrategy strategy, int retryCount, int baseDelay) {
        switch (strategy) {
            case FIXED:
                return baseDelay;
            case LINEAR:
                return baseDelay * (retryCount + 1);
            case EXPONENTIAL:
                return (long) (baseDelay * Math.pow(2, retryCount));
            case EXPONENTIAL_WITH_JITTER:
                long exponentialDelay = (long) (baseDelay * Math.pow(2, retryCount));
                long jitter = (long) (Math.random() * baseDelay * 0.1);
                return exponentialDelay + jitter;
            default:
                return baseDelay;
        }
    }
    
    public void checkAndReconnect() {
        if (!socket.isClosed()) {
            log.debug("Socket连接正常");
        }
    }
    
    public void registerPacketForAck(String messageId, int timeout) {
        pendingAcks.put(messageId, System.currentTimeMillis() + timeout);
        log.debug("注册ACK等待: 消息ID={}, 超时={}ms", messageId, timeout);
    }
    
    public void unregisterPacketForAck(String messageId) {
        Long expiryTime = pendingAcks.remove(messageId);
        if (expiryTime != null) {
            log.debug("取消注册ACK等待: 消息ID={}", messageId);
        }
    }
    
    public void cleanupExpiredAcks() {
        long currentTime = System.currentTimeMillis();
        pendingAcks.entrySet().removeIf(entry -> {
            if (entry.getValue() < currentTime) {
                log.warn("ACK超时，移除等待: 消息ID={}", entry.getKey());
                return true;
            }
            return false;
        });
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    
    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
    
    public void setUseNio(boolean useNio) {
        this.useNio = useNio;
    }
    
    public int getPendingAckCount() {
        return pendingAcks.size();
    }
}
