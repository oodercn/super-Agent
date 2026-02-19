
package net.ooder.sdk.service.skillcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.common.enums.DiscoveryMethod;
import net.ooder.sdk.common.enums.SkillStatus;
import net.ooder.sdk.infra.exception.SkillException;

public class RemoteDeploymentService {
    
    private static final Logger log = LoggerFactory.getLogger(RemoteDeploymentService.class);
    
    private final SkillCenterClientImpl skillCenterClient;
    private final Map<String, DeploymentStatus> deploymentStatuses = new HashMap<>();
    
    public RemoteDeploymentService(SkillCenterClientImpl skillCenterClient) {
        this.skillCenterClient = skillCenterClient;
    }
    
    public CompletableFuture<DeploymentResult> deploySkill(String skillId, String targetAgentId) {
        log.info("Deploying skill {} to agent {}", skillId, targetAgentId);
        
        String deploymentId = generateDeploymentId(skillId, targetAgentId);
        DeploymentStatus status = new DeploymentStatus();
        status.setDeploymentId(deploymentId);
        status.setSkillId(skillId);
        status.setTargetAgentId(targetAgentId);
        status.setStatus("in_progress");
        status.setStartTime(System.currentTimeMillis());
        
        deploymentStatuses.put(deploymentId, status);
        
        return skillCenterClient.getSkill(skillId)
            .thenCompose(pkg -> {
                if (pkg == null) {
                    throw new SkillException(skillId, "Skill not found in SkillCenter");
                }
                
                return skillCenterClient.getDownloadUrl(skillId, pkg.getVersion())
                    .thenApply(url -> {
                        DeploymentResult result = new DeploymentResult();
                        result.setDeploymentId(deploymentId);
                        result.setSkillId(skillId);
                        result.setTargetAgentId(targetAgentId);
                        result.setDownloadUrl(url);
                        result.setSuccess(true);
                        
                        status.setStatus("completed");
                        status.setEndTime(System.currentTimeMillis());
                        
                        log.info("Skill {} deployed to agent {}", skillId, targetAgentId);
                        
                        return result;
                    });
            })
            .exceptionally(e -> {
                DeploymentResult result = new DeploymentResult();
                result.setDeploymentId(deploymentId);
                result.setSkillId(skillId);
                result.setTargetAgentId(targetAgentId);
                result.setSuccess(false);
                result.setError(e.getMessage());
                
                status.setStatus("failed");
                status.setEndTime(System.currentTimeMillis());
                status.setError(e.getMessage());
                
                log.error("Failed to deploy skill {} to agent {}", skillId, targetAgentId, e);
                
                return result;
            });
    }
    
    public CompletableFuture<List<DeploymentResult>> deployToMultipleAgents(
            String skillId, List<String> targetAgentIds) {
        log.info("Deploying skill {} to {} agents", skillId, targetAgentIds.size());
        
        List<CompletableFuture<DeploymentResult>> futures = new ArrayList<>();
        for (String agentId : targetAgentIds) {
            futures.add(deploySkill(skillId, agentId));
        }
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> {
                List<DeploymentResult> results = new ArrayList<>();
                for (CompletableFuture<DeploymentResult> future : futures) {
                    results.add(future.join());
                }
                return results;
            });
    }
    
    public DeploymentStatus getDeploymentStatus(String deploymentId) {
        return deploymentStatuses.get(deploymentId);
    }
    
    public List<DeploymentStatus> getDeploymentsBySkill(String skillId) {
        List<DeploymentStatus> results = new ArrayList<>();
        for (DeploymentStatus status : deploymentStatuses.values()) {
            if (skillId.equals(status.getSkillId())) {
                results.add(status);
            }
        }
        return results;
    }
    
    public List<DeploymentStatus> getDeploymentsByAgent(String agentId) {
        List<DeploymentStatus> results = new ArrayList<>();
        for (DeploymentStatus status : deploymentStatuses.values()) {
            if (agentId.equals(status.getTargetAgentId())) {
                results.add(status);
            }
        }
        return results;
    }
    
    public CompletableFuture<Void> cancelDeployment(String deploymentId) {
        return CompletableFuture.runAsync(() -> {
            DeploymentStatus status = deploymentStatuses.get(deploymentId);
            if (status != null && "in_progress".equals(status.getStatus())) {
                status.setStatus("cancelled");
                status.setEndTime(System.currentTimeMillis());
                log.info("Deployment cancelled: {}", deploymentId);
            }
        });
    }
    
    private String generateDeploymentId(String skillId, String agentId) {
        return skillId + "-" + agentId + "-" + System.currentTimeMillis();
    }
    
    public static class DeploymentStatus {
        private String deploymentId;
        private String skillId;
        private String targetAgentId;
        private String status;
        private long startTime;
        private long endTime;
        private String error;
        
        public String getDeploymentId() { return deploymentId; }
        public void setDeploymentId(String deploymentId) { this.deploymentId = deploymentId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getTargetAgentId() { return targetAgentId; }
        public void setTargetAgentId(String targetAgentId) { this.targetAgentId = targetAgentId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }
        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        
        public long getDuration() {
            if (endTime > 0 && startTime > 0) {
                return endTime - startTime;
            }
            return 0;
        }
    }
    
    public static class DeploymentResult {
        private String deploymentId;
        private String skillId;
        private String targetAgentId;
        private String downloadUrl;
        private boolean success;
        private String error;
        
        public String getDeploymentId() { return deploymentId; }
        public void setDeploymentId(String deploymentId) { this.deploymentId = deploymentId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getTargetAgentId() { return targetAgentId; }
        public void setTargetAgentId(String targetAgentId) { this.targetAgentId = targetAgentId; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}
