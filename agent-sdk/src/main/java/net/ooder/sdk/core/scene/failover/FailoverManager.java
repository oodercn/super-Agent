
package net.ooder.sdk.core.scene.failover;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.scene.SceneGroupManager.FailoverStatus;
import net.ooder.sdk.api.scene.SceneMember;
import net.ooder.sdk.common.constants.SDKConstants;

public class FailoverManager {
    
    private static final Logger log = LoggerFactory.getLogger(FailoverManager.class);
    
    private final Map<String, FailoverStatus> failoverStatuses = new ConcurrentHashMap<>();
    private final Map<String, ScheduledFuture<?>> heartbeatTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    
    private int heartbeatInterval = SDKConstants.FAILOVER_HEARTBEAT_INTERVAL_MS;
    private int heartbeatTimeout = SDKConstants.FAILOVER_TIMEOUT_MS;
    private int evaluationWindow = SDKConstants.FAILOVER_EVALUATION_WINDOW_MS;
    
    public SceneMember selectNewPrimary(List<SceneMember> backups) {
        if (backups == null || backups.isEmpty()) {
            return null;
        }
        
        SceneMember bestCandidate = null;
        double bestScore = -1;
        
        for (SceneMember backup : backups) {
            double score = evaluateCandidate(backup);
            if (score > bestScore) {
                bestScore = score;
                bestCandidate = backup;
            }
        }
        
        return bestCandidate;
    }
    
    public double evaluateCandidate(SceneMember member) {
        if (member == null || !member.isHealthy()) {
            return 0;
        }
        
        double score = 100.0;
        
        if (!member.isHealthy()) {
            score -= 50;
        }
        
        long timeSinceJoin = System.currentTimeMillis() - member.getJoinTime();
        score += Math.min(timeSinceJoin / 3600000.0, 10);
        
        int missedHeartbeats = member.getHeartbeatMissed();
        score -= missedHeartbeats * 5;
        
        return Math.max(0, score);
    }
    
    public void startHeartbeat(String sceneGroupId, Runnable heartbeatCallback) {
        if (heartbeatTasks.containsKey(sceneGroupId)) {
            return;
        }
        
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                if (heartbeatCallback != null) {
                    heartbeatCallback.run();
                }
            } catch (Exception e) {
                log.error("Heartbeat error for scene group: {}", sceneGroupId, e);
            }
        }, 0, heartbeatInterval, TimeUnit.MILLISECONDS);
        
        heartbeatTasks.put(sceneGroupId, future);
        log.info("Heartbeat started for scene group: {}", sceneGroupId);
    }
    
    public void stopHeartbeat(String sceneGroupId) {
        ScheduledFuture<?> future = heartbeatTasks.remove(sceneGroupId);
        if (future != null) {
            future.cancel(false);
            log.info("Heartbeat stopped for scene group: {}", sceneGroupId);
        }
    }
    
    public void startFailover(String sceneGroupId, String failedMemberId) {
        FailoverStatus status = new FailoverStatus();
        status.setSceneGroupId(sceneGroupId);
        status.setInProgress(true);
        status.setFailedMemberId(failedMemberId);
        status.setStartTime(System.currentTimeMillis());
        status.setPhase("detecting");
        
        failoverStatuses.put(sceneGroupId, status);
        
        log.info("Failover started for scene group: {}, failed member: {}", 
            sceneGroupId, failedMemberId);
    }
    
    public void completeFailover(String sceneGroupId, String newPrimaryId) {
        FailoverStatus status = failoverStatuses.get(sceneGroupId);
        if (status != null) {
            status.setInProgress(false);
            status.setNewPrimaryId(newPrimaryId);
            status.setPhase("completed");
        }
        
        log.info("Failover completed for scene group: {}, new primary: {}", 
            sceneGroupId, newPrimaryId);
    }
    
    public FailoverStatus getStatus(String sceneGroupId) {
        return failoverStatuses.get(sceneGroupId);
    }
    
    public void updateStatus(String sceneGroupId, FailoverStatus status) {
        failoverStatuses.put(sceneGroupId, status);
    }
    
    public boolean isFailoverInProgress(String sceneGroupId) {
        FailoverStatus status = failoverStatuses.get(sceneGroupId);
        return status != null && status.isInProgress();
    }
    
    public void setHeartbeatInterval(int interval) {
        this.heartbeatInterval = interval;
    }
    
    public void setHeartbeatTimeout(int timeout) {
        this.heartbeatTimeout = timeout;
    }
    
    public void setEvaluationWindow(int window) {
        this.evaluationWindow = window;
    }
    
    public void shutdown() {
        for (ScheduledFuture<?> future : heartbeatTasks.values()) {
            future.cancel(false);
        }
        heartbeatTasks.clear();
        scheduler.shutdown();
    }
}
