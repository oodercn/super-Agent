package net.ooder.sdk.agent.impl;

import net.ooder.sdk.agent.EndAgent;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.network.packet.ResponsePacket;
import net.ooder.sdk.network.udp.UDPSDK;
import net.ooder.sdk.system.enums.EndAgentStatus;
import net.ooder.sdk.command.persistence.CommandBaseService;
import net.ooder.sdk.command.persistence.CommandBaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractEndAgent implements EndAgent {
    protected static final Logger log = LoggerFactory.getLogger(AbstractEndAgent.class);
    protected final UDPSDK udpSDK;
    protected final String agentId;
    protected final String agentName;
    protected final String agentType;
    protected final Map<String, Object> capabilities;
    protected volatile EndAgentStatus status;
    protected final CommandBaseService commandBaseService;
    protected final Queue<CommandPacket> commandCache;
    private final Map<String, Map<String, Object>> caps;
    private final Map<String, Map<String, Object>> skills;
    private final Map<String, Map<String, Map<String, Object>>> sceneSkills;
    private final Map<String, String> currentKeys;
    private final Map<String, String> keyStatus;
    private long keyRotationPeriod;
    private java.util.concurrent.TimeUnit keyRotationTimeUnit;
    private java.util.concurrent.ScheduledExecutorService keyRotationExecutor;
    private java.util.concurrent.ScheduledFuture<?> keyRotationTask;

    public AbstractEndAgent(UDPSDK udpSDK, String agentId, String agentName, String agentType, Map<String, Object> capabilities) {
        this.udpSDK = udpSDK;
        this.agentId = agentId;
        this.agentName = agentName;
        this.agentType = agentType;
        this.capabilities = capabilities;
        this.status = EndAgentStatus.OFFLINE;
        this.commandBaseService = new CommandBaseServiceImpl();
        this.commandCache = new ConcurrentLinkedQueue<>();
        this.caps = new ConcurrentHashMap<>();
        this.skills = new ConcurrentHashMap<>();
        this.sceneSkills = new ConcurrentHashMap<>();
        this.currentKeys = new ConcurrentHashMap<>();
        this.keyStatus = new ConcurrentHashMap<>();
        this.keyRotationPeriod = 24;
        this.keyRotationTimeUnit = java.util.concurrent.TimeUnit.HOURS;
        this.keyRotationExecutor = java.util.concurrent.Executors.newScheduledThreadPool(1);
        this.keyRotationTask = null;
        
        // 初始化密钥
        initializeKeys();
    }
    
    private void initializeKeys() {
        // 初始化密钥
        generateKeys();
        keyStatus.put("lastRotation", String.valueOf(System.currentTimeMillis()));
        keyStatus.put("nextRotation", String.valueOf(System.currentTimeMillis() + keyRotationTimeUnit.toMillis(keyRotationPeriod)));
        keyStatus.put("status", "active");
    }
    
    private void generateKeys() {
        // 生成公钥和私钥
        String publicKey = "public_key_" + agentId + "_" + java.util.UUID.randomUUID();
        String privateKey = "private_key_" + agentId + "_" + java.util.UUID.randomUUID();
        
        currentKeys.put("publicKey", publicKey);
        currentKeys.put("privateKey", privateKey);
    }

    @Override
    public String getAgentId() {
        return agentId;
    }
    
    @Override
    public String getAgentName() {
        return agentName;
    }
    
    @Override
    public String getAgentType() {
        return agentType;
    }
    
    @Override
    public EndAgentStatus getStatus() {
        return status;
    }
    
    @Override
    public Map<String, Object> getCapabilities() {
        return capabilities;
    }
    
    @Override
    public CompletableFuture<ResponsePacket> executeTask(TaskPacket taskPacket) {
        // 基础任务执行实现
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Executing task {} on EndAgent {}", taskPacket.getTaskId(), agentId);
                // 具体实现由子类提供
                return createResponse(taskPacket, true, "Task executed successfully");
            } catch (Exception e) {
                log.error("Error executing task: {}", e.getMessage());
                return createResponse(taskPacket, false, e.getMessage());
            }
        });
    }
    
    @Override
    public CompletableFuture<ResponsePacket> executeCommand(CommandPacket commandPacket) {
        log.info("Executing command: {}", commandPacket.getCommandId());
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 检查 Skills 状态
                if (!areSkillsAvailable()) {
                    // 缓存命令
                    cacheCommand(commandPacket);
                    log.info("Skills not available, cached command: {}", commandPacket.getCommandId());
                    return ResponsePacket.builder()
                            .commandId(commandPacket.getCommandId())
                            .status("cached")
                            .data("Command cached: Skills not available")
                            .build();
                }
                
                // 检查命令是否需要特殊处理
                String commandType = getCommandType(commandPacket);
                if ("sync_status".equals(commandType)) {
                    // 处理状态同步命令
                    syncSkillStatus();
                    return createCommandResponse(commandPacket, true, "Status synced successfully");
                }
                
                // 实际执行命令的逻辑
                ResponsePacket response = createCommandResponse(commandPacket, true, "Command executed successfully");
                log.info("Command executed successfully: {}", commandPacket.getCommandId());
                return response;
            } catch (Exception e) {
                log.error("Failed to execute command: {}", commandPacket.getCommandId(), e);
                return createCommandResponse(commandPacket, false, "Failed to execute command: " + e.getMessage());
            }
        });
    }
    
    private String getCommandType(CommandPacket commandPacket) {
        // 获取命令类型
        // 简化实现：从命令参数中获取类型
        Map<String, Object> params = commandPacket.getParams();
        if (params != null && params.containsKey("type")) {
            return params.get("type").toString();
        }
        return "unknown";
    }
    
    @Override
    public void cacheCommand(CommandPacket commandPacket) {
        commandCache.offer(commandPacket);
        log.info("Command cached: {}", commandPacket.getCommandId());
    }
    
    @Override
    public int getCommandCacheSize() {
        return commandCache.size();
    }
    
    @Override
    public void processCachedCommands() {
        log.info("Processing cached commands, size: {}", commandCache.size());
        while (!commandCache.isEmpty() && areSkillsAvailable()) {
            CommandPacket commandPacket = commandCache.poll();
            if (commandPacket != null) {
                log.info("Processing cached command: {}", commandPacket.getCommandId());
                executeCommand(commandPacket);
            }
        }
        log.info("Cached commands processing completed");
    }
    
    @Override
    public void syncSkillStatus() {
        log.info("Syncing skill status for End Agent: {}", agentId);
        // 同步 Skill 状态的逻辑
        // 1. 获取所有 Skill 的状态
        Map<String, String> skillStatuses = getAllSkillStatuses();
        
        // 2. 根据 Skill 状态更新 End Agent 的状态
        if (areAllSkillsAvailable(skillStatuses)) {
            updateStatus(EndAgentStatus.ONLINE);
        } else {
            updateStatus(EndAgentStatus.BUSY);
        }
        
        // 3. 同步技能和能力信息
        syncSkillsAndCaps();
        
        // 4. 同步后处理缓存的命令
        processCachedCommands();
        log.info("Skill status synced successfully for End Agent: {}", agentId);
    }
    
    private Map<String, String> getAllSkillStatuses() {
        // 获取所有 Skill 的状态
        // 简化实现：假设所有 Skill 状态正常
        Map<String, String> statuses = new ConcurrentHashMap<>();
        skills.keySet().forEach(skillId -> statuses.put(skillId, "online"));
        return statuses;
    }
    
    private boolean areAllSkillsAvailable(Map<String, String> skillStatuses) {
        // 检查所有 Skill 是否可用
        return skillStatuses.values().stream().allMatch(status -> "online".equals(status));
    }
    
    private void syncSkillsAndCaps() {
        // 从外部存储或 Skill 同步技能和能力信息
        // 简化实现：这里不做实际同步，假设信息已经一致
        log.debug("Syncing skills and caps for End Agent: {}", agentId);
    }
    
    @Override
    public CompletableFuture<Boolean> start() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Starting EndAgent: {}", agentId);
                syncSkillStatus();
                return true;
            } catch (Exception e) {
                log.error("Error starting EndAgent: {}", e.getMessage());
                status = EndAgentStatus.ERROR;
                return false;
            }
        });
    }
    
    @Override
    public CompletableFuture<Boolean> stop() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Stopping EndAgent: {}", agentId);
                status = EndAgentStatus.OFFLINE;
                return true;
            } catch (Exception e) {
                log.error("Error stopping EndAgent: {}", e.getMessage());
                return false;
            }
        });
    }
    
    @Override
    public boolean isOnline() {
        return status == EndAgentStatus.ONLINE;
    }
    
    @Override
    public void updateStatus(EndAgentStatus status) {
        this.status = status;
        log.info("EndAgent {} status updated to: {}", agentId, status);
    }
    
    protected abstract ResponsePacket createResponse(TaskPacket taskPacket, boolean success, String message);
    
    protected ResponsePacket createCommandResponse(CommandPacket commandPacket, boolean success, String message) {
        return ResponsePacket.builder()
                .commandId(commandPacket.getCommandId())
                .status(success ? "success" : "error")
                .data(message)
                .build();
    }
    
    protected boolean areSkillsAvailable() {
        // 检查 Skills 是否可用的逻辑
        // 简化实现：假设 Skills 始终可用
        return true;
    }
    
    protected CommandBaseService getCommandBaseService() {
        return commandBaseService;
    }
    
    @Override
    public CompletableFuture<Boolean> registerCAP(String capKey, Map<String, Object> capInfo) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Registering CAP for End Agent: {} - {}", agentId, capKey);
            
            // 验证 CAP Key 格式
            if (!validateCapKey(capKey)) {
                log.error("Invalid CAP Key format: {}", capKey);
                future.complete(false);
                return future;
            }
            
            // 检查 CAP 数量限制
            if (caps.size() >= 256) {
                log.error("CAP limit exceeded: maximum 256 CAPs allowed");
                future.complete(false);
                return future;
            }
            
            // 注册 CAP
            caps.put(capKey, capInfo);
            log.info("CAP registered successfully: {} - {}", agentId, capKey);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error registering CAP: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    private boolean validateCapKey(String capKey) {
        // 验证 CAP Key 格式（16进制，范围 0x00 到 0xFF）
        try {
            int key = Integer.parseInt(capKey, 16);
            return key >= 0 && key <= 255;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    @Override
    public CompletableFuture<Boolean> updateCAP(String capKey, Map<String, Object> capInfo) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Updating CAP for End Agent: {} - {}", agentId, capKey);
            
            // 验证 CAP Key 是否存在
            if (!caps.containsKey(capKey)) {
                log.error("CAP not found: {}", capKey);
                future.complete(false);
                return future;
            }
            
            // 更新 CAP
            caps.put(capKey, capInfo);
            log.info("CAP updated successfully: {} - {}", agentId, capKey);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error updating CAP: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getCAP(String capKey) {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        
        try {
            log.info("Getting CAP for End Agent: {} - {}", agentId, capKey);
            
            // 验证 CAP Key 格式
            if (!validateCapKey(capKey)) {
                log.error("Invalid CAP Key format: {}", capKey);
                future.complete(null);
                return future;
            }
            
            // 查询 CAP
            Map<String, Object> capInfo = caps.get(capKey);
            if (capInfo != null) {
                log.info("CAP found: {} - {}", agentId, capKey);
            } else {
                log.warn("CAP not found: {} - {}", agentId, capKey);
            }
            future.complete(capInfo);
        } catch (Exception e) {
            log.error("Error getting CAP: {}", e.getMessage());
            future.complete(null);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> deleteCAP(String capKey) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Deleting CAP for End Agent: {} - {}", agentId, capKey);
            
            // 验证 CAP Key 是否存在
            if (!caps.containsKey(capKey)) {
                log.error("CAP not found: {}", capKey);
                future.complete(false);
                return future;
            }
            
            // 删除 CAP
            caps.remove(capKey);
            log.info("CAP deleted successfully: {} - {}", agentId, capKey);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error deleting CAP: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public Map<String, Map<String, Object>> getAllCAPs() {
        return caps;
    }
    
    @Override
    public CompletableFuture<Boolean> registerSkill(String skillId, Map<String, Object> skillInfo) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Registering skill for End Agent: {} - {}", agentId, skillId);
            
            // 注册技能
            skills.put(skillId, skillInfo);
            log.info("Skill registered successfully: {} - {}", agentId, skillId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error registering skill: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> updateSkill(String skillId, Map<String, Object> skillInfo) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Updating skill for End Agent: {} - {}", agentId, skillId);
            
            // 验证技能是否存在
            if (!skills.containsKey(skillId)) {
                log.error("Skill not found: {}", skillId);
                future.complete(false);
                return future;
            }
            
            // 更新技能
            skills.put(skillId, skillInfo);
            log.info("Skill updated successfully: {} - {}", agentId, skillId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error updating skill: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getSkill(String skillId) {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        
        try {
            log.info("Getting skill for End Agent: {} - {}", agentId, skillId);
            
            // 查询技能
            Map<String, Object> skillInfo = skills.get(skillId);
            if (skillInfo != null) {
                log.info("Skill found: {} - {}", agentId, skillId);
            } else {
                log.warn("Skill not found: {} - {}", agentId, skillId);
            }
            future.complete(skillInfo);
        } catch (Exception e) {
            log.error("Error getting skill: {}", e.getMessage());
            future.complete(null);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> deleteSkill(String skillId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Deleting skill for End Agent: {} - {}", agentId, skillId);
            
            // 验证技能是否存在
            if (!skills.containsKey(skillId)) {
                log.error("Skill not found: {}", skillId);
                future.complete(false);
                return future;
            }
            
            // 删除技能
            skills.remove(skillId);
            log.info("Skill deleted successfully: {} - {}", agentId, skillId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error deleting skill: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public Map<String, Map<String, Object>> getAllSkills() {
        return skills;
    }
    
    @Override
    public CompletableFuture<Boolean> registerSceneSkill(String sceneId, String skillId, Map<String, Object> skillInfo) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Registering scene skill for End Agent: {} - {} - {}", agentId, sceneId, skillId);
            
            // 确保场景技能映射存在
            sceneSkills.computeIfAbsent(sceneId, k -> new ConcurrentHashMap<>());
            
            // 注册场景技能
            sceneSkills.get(sceneId).put(skillId, skillInfo);
            log.info("Scene skill registered successfully: {} - {} - {}", agentId, sceneId, skillId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error registering scene skill: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Map<String, Map<String, Object>>> getSceneSkills(String sceneId) {
        CompletableFuture<Map<String, Map<String, Object>>> future = new CompletableFuture<>();
        
        try {
            log.info("Getting scene skills for End Agent: {} - {}", agentId, sceneId);
            
            // 查询场景技能
            Map<String, Map<String, Object>> sceneSkillMap = sceneSkills.get(sceneId);
            if (sceneSkillMap != null) {
                log.info("Scene skills found: {} - {}", agentId, sceneId);
            } else {
                log.warn("Scene skills not found: {} - {}", agentId, sceneId);
                sceneSkillMap = new ConcurrentHashMap<>();
            }
            future.complete(sceneSkillMap);
        } catch (Exception e) {
            log.error("Error getting scene skills: {}", e.getMessage());
            future.complete(new ConcurrentHashMap<>());
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> validateSceneSkill(String sceneId, String skillId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Validating scene skill for End Agent: {} - {} - {}", agentId, sceneId, skillId);
            
            // 验证场景技能是否存在
            if (!sceneSkills.containsKey(sceneId) || !sceneSkills.get(sceneId).containsKey(skillId)) {
                log.error("Scene skill not found: {} - {}", sceneId, skillId);
                future.complete(false);
                return future;
            }
            
            // 验证技能是否符合场景要求
            // 简化实现：假设技能始终符合场景要求
            log.info("Scene skill validated successfully: {} - {} - {}", agentId, sceneId, skillId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error validating scene skill: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<ResponsePacket> executeSceneSkill(String sceneId, String skillId, Map<String, Object> params) {
        CompletableFuture<ResponsePacket> future = new CompletableFuture<>();
        
        try {
            log.info("Executing scene skill for End Agent: {} - {} - {}", agentId, sceneId, skillId);
            
            // 验证场景技能是否存在
            if (!sceneSkills.containsKey(sceneId) || !sceneSkills.get(sceneId).containsKey(skillId)) {
                log.error("Scene skill not found: {} - {}", sceneId, skillId);
                ResponsePacket errorResponse = ResponsePacket.builder()
                        .commandId("execute-scene-skill")
                        .status("error")
                        .data("Scene skill not found")
                        .build();
                future.complete(errorResponse);
                return future;
            }
            
            // 执行场景技能
            // 简化实现：假设技能执行成功
            log.info("Scene skill executed successfully: {} - {} - {}", agentId, sceneId, skillId);
            ResponsePacket successResponse = ResponsePacket.builder()
                    .commandId("execute-scene-skill")
                    .status("success")
                    .data("Scene skill executed successfully")
                    .build();
            future.complete(successResponse);
        } catch (Exception e) {
            log.error("Error executing scene skill: {}", e.getMessage());
            ResponsePacket errorResponse = ResponsePacket.builder()
                    .commandId("execute-scene-skill")
                    .status("error")
                    .data("Error executing scene skill: " + e.getMessage())
                    .build();
            future.complete(errorResponse);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Map<String, String>> generateNewKeys() {
        CompletableFuture<Map<String, String>> future = new CompletableFuture<>();
        
        try {
            log.info("Generating new keys for End Agent: {}", agentId);
            
            // 生成新密钥
            Map<String, String> newKeys = new java.util.HashMap<>();
            newKeys.put("publicKey", "public_key_" + agentId + "_" + java.util.UUID.randomUUID());
            newKeys.put("privateKey", "private_key_" + agentId + "_" + java.util.UUID.randomUUID());
            
            log.info("New keys generated successfully for End Agent: {}", agentId);
            future.complete(newKeys);
        } catch (Exception e) {
            log.error("Error generating new keys: {}", e.getMessage());
            future.completeExceptionally(e);
        }
        
        return future;
    }
    
    @Override
    public CompletableFuture<Boolean> rotateKeys() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Rotating keys for End Agent: {}", agentId);
            
            // 生成新密钥
            generateKeys();
            
            // 更新密钥状态
            keyStatus.put("lastRotation", String.valueOf(System.currentTimeMillis()));
            keyStatus.put("nextRotation", String.valueOf(System.currentTimeMillis() + keyRotationTimeUnit.toMillis(keyRotationPeriod)));
            keyStatus.put("status", "active");
            
            // 通知其他组件密钥已轮换
            notifyKeyRotation();
            
            log.info("Keys rotated successfully for End Agent: {}", agentId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error rotating keys: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    private void notifyKeyRotation() {
        // 通知其他组件密钥已轮换
        // 简化实现：这里不做实际通知，假设通知成功
        log.debug("Notifying key rotation for End Agent: {}", agentId);
    }
    
    @Override
    public Map<String, String> getKeyStatus() {
        return keyStatus;
    }
    
    @Override
    public void setKeyRotationPeriod(long period, java.util.concurrent.TimeUnit unit) {
        this.keyRotationPeriod = period;
        this.keyRotationTimeUnit = unit;
        log.info("Key rotation period set to {} {}", period, unit);
        
        // 更新下一次轮换时间
        keyStatus.put("nextRotation", String.valueOf(System.currentTimeMillis() + keyRotationTimeUnit.toMillis(keyRotationPeriod)));
    }
    
    @Override
    public CompletableFuture<Boolean> startAutoKeyRotation() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            log.info("Starting auto key rotation for End Agent: {}", agentId);
            
            // 停止现有的轮换任务
            if (keyRotationTask != null && !keyRotationTask.isCancelled()) {
                keyRotationTask.cancel(false);
            }
            
            // 启动新的轮换任务
            keyRotationTask = keyRotationExecutor.scheduleAtFixedRate(() -> {
                try {
                    rotateKeys().join();
                } catch (Exception e) {
                    log.error("Error in auto key rotation: {}", e.getMessage());
                }
            }, keyRotationPeriod, keyRotationPeriod, keyRotationTimeUnit);
            
            log.info("Auto key rotation started successfully for End Agent: {}", agentId);
            future.complete(true);
        } catch (Exception e) {
            log.error("Error starting auto key rotation: {}", e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    @Override
    public void stopAutoKeyRotation() {
        log.info("Stopping auto key rotation for End Agent: {}", agentId);
        
        // 停止轮换任务
        if (keyRotationTask != null && !keyRotationTask.isCancelled()) {
            keyRotationTask.cancel(false);
            log.info("Auto key rotation stopped successfully for End Agent: {}", agentId);
        } else {
            log.warn("Auto key rotation was not running for End Agent: {}", agentId);
        }
    }
}
