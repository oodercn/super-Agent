package net.ooder.examples.skillb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.command.handler.AbstractSkillCommandHandler;
import net.ooder.sdk.command.handler.CommandHandler;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.examples.skillb.vfs.StorageService;
import net.ooder.skillvfs.VfsConfig;
import net.ooder.skillvfs.VfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SkillCommandHandler extends AbstractSkillCommandHandler {
    private static final Logger log = LoggerFactory.getLogger(SkillCommandHandler.class);
    
    private final StorageService storageService;
    
    @Autowired
    public SkillCommandHandler(
            @Value("${vfs.serverUrl}") String vfsServerUrl,
            @Value("${vfs.groupName}") String groupName,
            @Value("${vfs.connectionTimeout}") long connectionTimeout,
            @Value("${vfs.retryCount}") int retryCount,
            @Value("${vfs.enabled}") boolean enabled) {
        // Create VFS configuration from Spring properties
        VfsConfig vfsConfig = VfsConfig.builder()
                .vfsServerUrl(vfsServerUrl)
                .groupName(groupName)
                .vfsConnectionTimeout(connectionTimeout)
                .vfsRetryCount(retryCount)
                .enableVfs(enabled)
                .build();
        
        // Initialize storage service
        this.storageService = new StorageService(vfsConfig);
        this.storageService.start();
    }

    @CommandHandler(CommandType.SKILL_SUBMIT)
    public void handleSkillSubmit(CommandPacket packet) {
        log.info("Handling skill.submit command");
        log.info("Params: {}", packet.getParams());

        try {
            Object paramsObj = packet.getParams();
            if (paramsObj instanceof java.util.Map) {
                java.util.Map<String, Object> params = (java.util.Map<String, Object>) paramsObj;
                String data = (String) params.get("data");
                String result = processData(data);

                log.info("Data processing result: {}", result);
            }
        } catch (Exception e) {
            log.error("Error handling skill.submit command", e);
        }
    }

    @CommandHandler(CommandType.SKILL_STATUS)
    public void handleSkillStatus(CommandPacket packet) {
        log.info("Handling skill.status command");
    }

    private String processData(String data) {
        return "Processed data: " + data;
    }
    
    @CommandHandler(CommandType.ROUTE_FORWARD)
    public void handleRouteForward(CommandPacket packet) {
        log.info("Handling route.forward command");
        log.info("Params: {}", packet.getParams());
        
        try {
            Object paramsObj = packet.getParams();
            if (paramsObj instanceof java.util.Map) {
                java.util.Map<String, Object> params = (java.util.Map<String, Object>) paramsObj;
                String action = (String) params.get("action");
                
                if ("vfs-sync".equals(action)) {
                    handleVfsSyncCommand(packet);
                } else {
                    log.warn("Unknown action: {}", action);
                }
            }
        } catch (Exception e) {
            log.error("Error handling route.forward command", e);
        }
    }
    
    /**
     * Handle VFS sync command received from Route Agent
     */
    private void handleVfsSyncCommand(CommandPacket packet) {
        log.info("Handling VFS sync command");
        
        try {
            // Perform folder sync comparison and sync files
            storageService.syncAllToVfs();
            storageService.syncAllFromVfs();
            
            log.info("VFS sync completed successfully");
        } catch (Exception e) {
            log.error("Error during VFS sync", e);
        }
    }
}
