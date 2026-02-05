package net.ooder.sdk.system.heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.network.packet.RetryInfo;
import net.ooder.sdk.network.packet.SleepInfo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class HeartbeatManager {
    private static final Logger log = LoggerFactory.getLogger(HeartbeatManager.class);
    private final String agentId;
    private final long heartbeatInterval;
    private final long timeout;
    private final int heartbeatLossThreshold;
    private final ScheduledExecutorService scheduler;
    private final AtomicLong sequence = new AtomicLong(0);
    private final AtomicReference<Long> lastHeartbeatTime = new AtomicReference<>(System.currentTimeMillis());
    private final AtomicReference<Long> lastAckTime = new AtomicReference<>(System.currentTimeMillis());
    private final AtomicInteger missedHeartbeats = new AtomicInteger(0);
    private HeartbeatCallback callback;

    public interface HeartbeatCallback {
        void onHeartbeatSent(HeartbeatData data);
        void onHeartbeatAck(HeartbeatAckData data);
        void onHeartbeatTimeout();
        void onHeartbeatLost();
    }

    public static class HeartbeatData {
        private String agentId;
        private long sequence;
        private RetryInfo retryInfo;
        private SleepInfo sleepInfo;

        // Getter and setter methods
        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public long getSequence() {
            return sequence;
        }

        public void setSequence(long sequence) {
            this.sequence = sequence;
        }

        public RetryInfo getRetryInfo() {
            return retryInfo;
        }

        public void setRetryInfo(RetryInfo retryInfo) {
            this.retryInfo = retryInfo;
        }

        public SleepInfo getSleepInfo() {
            return sleepInfo;
        }

        public void setSleepInfo(SleepInfo sleepInfo) {
            this.sleepInfo = sleepInfo;
        }
    }

    public static class HeartbeatAckData {
        private String agentId;
        private long sequence;
        private String status;
        private RetryInfo retryInfo;
        private SleepInfo sleepInfo;
        private HealthInfo healthInfo;

        // Getter and setter methods
        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public long getSequence() {
            return sequence;
        }

        public void setSequence(long sequence) {
            this.sequence = sequence;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public RetryInfo getRetryInfo() {
            return retryInfo;
        }

        public void setRetryInfo(RetryInfo retryInfo) {
            this.retryInfo = retryInfo;
        }

        public SleepInfo getSleepInfo() {
            return sleepInfo;
        }

        public void setSleepInfo(SleepInfo sleepInfo) {
            this.sleepInfo = sleepInfo;
        }

        public HealthInfo getHealthInfo() {
            return healthInfo;
        }

        public void setHealthInfo(HealthInfo healthInfo) {
            this.healthInfo = healthInfo;
        }
    }

    public static class HealthInfo {
        private double cpuUsage;
        private long memoryUsage;
        private int activeConnections;
        private int pendingCommands;

        // Getter and setter methods
        public double getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(double cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public long getMemoryUsage() {
            return memoryUsage;
        }

        public void setMemoryUsage(long memoryUsage) {
            this.memoryUsage = memoryUsage;
        }

        public int getActiveConnections() {
            return activeConnections;
        }

        public void setActiveConnections(int activeConnections) {
            this.activeConnections = activeConnections;
        }

        public int getPendingCommands() {
            return pendingCommands;
        }

        public void setPendingCommands(int pendingCommands) {
            this.pendingCommands = pendingCommands;
        }
    }

    public HeartbeatManager(String agentId, long heartbeatInterval, long timeout, int heartbeatLossThreshold) {
        this.agentId = agentId;
        this.heartbeatInterval = heartbeatInterval;
        this.timeout = timeout;
        this.heartbeatLossThreshold = heartbeatLossThreshold;
        this.scheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread thread = new Thread(r, "heartbeat-manager");
            thread.setDaemon(true);
            return thread;
        });
    }

    public void start() {
        scheduleHeartbeat();
        scheduleTimeoutCheck();
        log.info("Heartbeat manager started for agent: {}", agentId);
    }

    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("Heartbeat manager stopped for agent: {}", agentId);
    }

    public void handleHeartbeatAck(long seq, String status, RetryInfo retryInfo, SleepInfo sleepInfo, HealthInfo healthInfo) {
        lastAckTime.set(System.currentTimeMillis());
        missedHeartbeats.set(0);
        log.debug("Heartbeat ack received for sequence: {}, status: {}", seq, status);

        if (callback != null) {
            HeartbeatAckData ackData = new HeartbeatAckData();
            ackData.setAgentId(agentId);
            ackData.setSequence(seq);
            ackData.setStatus(status);
            ackData.setRetryInfo(retryInfo);
            ackData.setSleepInfo(sleepInfo);
            ackData.setHealthInfo(healthInfo);
            callback.onHeartbeatAck(ackData);
        }
    }

    private void scheduleHeartbeat() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                sendHeartbeat();
            } catch (Exception e) {
                log.error("Error sending heartbeat", e);
            }
        }, 0, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    private void sendHeartbeat() {
        long seq = sequence.incrementAndGet();
        lastHeartbeatTime.set(System.currentTimeMillis());

        HeartbeatData data = new HeartbeatData();
        data.setAgentId(agentId);
        data.setSequence(seq);

        log.debug("Sending heartbeat with sequence: {}", seq);

        if (callback != null) {
            callback.onHeartbeatSent(data);
        }
    }

    private void scheduleTimeoutCheck() {
        scheduler.scheduleAtFixedRate(() -> {
            long timeSinceLastAck = System.currentTimeMillis() - lastAckTime.get();
            if (timeSinceLastAck > timeout) {
                missedHeartbeats.incrementAndGet();
                log.warn("Heartbeat timeout detected, missed count: {}", missedHeartbeats.get());

                if (missedHeartbeats.get() >= heartbeatLossThreshold) {
                    log.error("Heartbeat lost threshold reached: {}", heartbeatLossThreshold);
                    if (callback != null) {
                        callback.onHeartbeatLost();
                    }
                } else if (callback != null) {
                    callback.onHeartbeatTimeout();
                }
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    public void setCallback(HeartbeatCallback callback) {
        this.callback = callback;
    }

    public long getLastHeartbeatTime() {
        return lastHeartbeatTime.get();
    }

    public long getLastAckTime() {
        return lastAckTime.get();
    }

    public int getMissedHeartbeats() {
        return missedHeartbeats.get();
    }

    public boolean isHealthy() {
        return missedHeartbeats.get() < heartbeatLossThreshold;
    }

    /**
     * 重置心跳管理器状态
     */
    public void reset() {
        sequence.set(0);
        lastHeartbeatTime.set(System.currentTimeMillis());
        lastAckTime.set(System.currentTimeMillis());
        missedHeartbeats.set(0);
        log.info("Heartbeat manager reset for agent: {}", agentId);
    }

    // Getter methods for final fields
    public String getAgentId() {
        return agentId;
    }

    public long getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public long getTimeout() {
        return timeout;
    }

    public int getHeartbeatLossThreshold() {
        return heartbeatLossThreshold;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    // Getter method for callback
    public HeartbeatCallback getCallback() {
        return callback;
    }
}
