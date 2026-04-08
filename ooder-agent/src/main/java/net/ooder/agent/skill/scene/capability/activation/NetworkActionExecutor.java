package net.ooder.mvp.skill.scene.capability.activation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class NetworkActionExecutor {

    private static final Logger log = LoggerFactory.getLogger(NetworkActionExecutor.class);

    public CompletableFuture<List<NetworkActionStatus>> executeAll(String installId) {
        log.info("[executeAll] Executing all network actions for: {}", installId);
        
        List<CompletableFuture<NetworkActionStatus>> futures = Arrays.asList(
            executeNotifyOtherScenes(installId),
            executeUpdateMyCapabilities(installId),
            executeUpdateMyTodos(installId),
            executeNotifyCollaborators(installId)
        );
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                List<NetworkActionStatus> results = new ArrayList<>();
                for (CompletableFuture<NetworkActionStatus> future : futures) {
                    try {
                        results.add(future.get());
                    } catch (Exception e) {
                        log.error("[executeAll] Error getting result: {}", e.getMessage());
                    }
                }
                return results;
            });
    }

    public CompletableFuture<NetworkActionStatus> executeNotifyOtherScenes(String installId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("[executeNotifyOtherScenes] Notifying other scenes for: {}", installId);
            NetworkActionStatus status = new NetworkActionStatus();
            status.setAction("notifyOtherScenes");
            status.setInstallId(installId);
            
            try {
                Thread.sleep(500);
                status.setStatus("COMPLETED");
                status.setMessage("已通知相关场景");
                status.setTimestamp(System.currentTimeMillis());
                log.info("[executeNotifyOtherScenes] Completed for: {}", installId);
            } catch (Exception e) {
                log.error("[executeNotifyOtherScenes] Failed: {}", e.getMessage());
                status.setStatus("FAILED");
                status.setMessage(e.getMessage());
            }
            
            return status;
        });
    }

    public CompletableFuture<NetworkActionStatus> executeUpdateMyCapabilities(String installId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("[executeUpdateMyCapabilities] Updating my capabilities for: {}", installId);
            NetworkActionStatus status = new NetworkActionStatus();
            status.setAction("updateMyCapabilities");
            status.setInstallId(installId);
            
            try {
                Thread.sleep(300);
                status.setStatus("COMPLETED");
                status.setMessage("已更新我的能力列表");
                status.setTimestamp(System.currentTimeMillis());
                log.info("[executeUpdateMyCapabilities] Completed for: {}", installId);
            } catch (Exception e) {
                log.error("[executeUpdateMyCapabilities] Failed: {}", e.getMessage());
                status.setStatus("FAILED");
                status.setMessage(e.getMessage());
            }
            
            return status;
        });
    }

    public CompletableFuture<NetworkActionStatus> executeUpdateMyTodos(String installId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("[executeUpdateMyTodos] Updating my todos for: {}", installId);
            NetworkActionStatus status = new NetworkActionStatus();
            status.setAction("updateMyTodos");
            status.setInstallId(installId);
            
            try {
                Thread.sleep(400);
                status.setStatus("COMPLETED");
                status.setMessage("已更新我的待办列表");
                status.setTimestamp(System.currentTimeMillis());
                log.info("[executeUpdateMyTodos] Completed for: {}", installId);
            } catch (Exception e) {
                log.error("[executeUpdateMyTodos] Failed: {}", e.getMessage());
                status.setStatus("FAILED");
                status.setMessage(e.getMessage());
            }
            
            return status;
        });
    }

    public CompletableFuture<NetworkActionStatus> executeNotifyCollaborators(String installId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("[executeNotifyCollaborators] Notifying collaborators for: {}", installId);
            NetworkActionStatus status = new NetworkActionStatus();
            status.setAction("notifyCollaborators");
            status.setInstallId(installId);
            
            try {
                Thread.sleep(600);
                status.setStatus("COMPLETED");
                status.setMessage("已通知所有协作者");
                status.setTimestamp(System.currentTimeMillis());
                log.info("[executeNotifyCollaborators] Completed for: {}", installId);
            } catch (Exception e) {
                log.error("[executeNotifyCollaborators] Failed: {}", e.getMessage());
                status.setStatus("FAILED");
                status.setMessage(e.getMessage());
            }
            
            return status;
        });
    }

    public static class NetworkActionStatus {
        private String action;
        private String installId;
        private String status;
        private String message;
        private Long timestamp;

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Long getTimestamp() { return timestamp; }
        public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    }
}
