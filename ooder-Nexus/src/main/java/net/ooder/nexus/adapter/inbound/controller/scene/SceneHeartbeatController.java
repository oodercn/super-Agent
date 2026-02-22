package net.ooder.nexus.adapter.inbound.controller.scene;

import net.ooder.config.ResultModel;
import net.ooder.nexus.service.scene.SceneGroupService;
import net.ooder.nexus.service.scene.SceneEngineService;
import net.ooder.scene.core.HeartbeatStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/scene/heartbeat")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class SceneHeartbeatController {

    private static final Logger log = LoggerFactory.getLogger(SceneHeartbeatController.class);

    @Autowired
    private SceneGroupService sceneGroupService;

    @Autowired(required = false)
    private SceneEngineService sceneEngineService;

    @PostMapping("/start/{groupId}")
    public ResultModel<Map<String, Object>> startHeartbeat(@PathVariable String groupId) {
        log.info("Starting heartbeat for group: {}", groupId);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            if (sceneEngineService != null && sceneEngineService.isAvailable()) {
                sceneEngineService.startHeartbeat(groupId);
                
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("groupId", groupId);
                data.put("heartbeatStarted", true);
                data.put("timestamp", System.currentTimeMillis());
                
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("Heartbeat started for group: " + groupId);
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("groupId", groupId);
                data.put("heartbeatStarted", false);
                data.put("timestamp", System.currentTimeMillis());
                
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("SceneEngineService not available, heartbeat simulated");
            }
        } catch (Exception e) {
            log.error("Failed to start heartbeat", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/stop/{groupId}")
    public ResultModel<Map<String, Object>> stopHeartbeat(@PathVariable String groupId) {
        log.info("Stopping heartbeat for group: {}", groupId);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            if (sceneEngineService != null && sceneEngineService.isAvailable()) {
                sceneEngineService.stopHeartbeat(groupId);
                
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("groupId", groupId);
                data.put("heartbeatStarted", false);
                data.put("timestamp", System.currentTimeMillis());
                
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("Heartbeat stopped for group: " + groupId);
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("groupId", groupId);
                data.put("heartbeatStarted", false);
                data.put("timestamp", System.currentTimeMillis());
                
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("SceneEngineService not available, stop simulated");
            }
        } catch (Exception e) {
            log.error("Failed to stop heartbeat", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/status/{groupId}")
    public ResultModel<Map<String, Object>> getHeartbeatStatus(@PathVariable String groupId) {
        log.info("Getting heartbeat status for group: {}", groupId);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("groupId", groupId);
            data.put("timestamp", System.currentTimeMillis());
            
            if (sceneEngineService != null && sceneEngineService.isAvailable()) {
                HeartbeatStatus status = sceneEngineService.getHeartbeatStatus(groupId);
                if (status != null) {
                    data.put("running", status.isRunning());
                    data.put("lastHeartbeatTime", status.getLastHeartbeatTime());
                    data.put("missedCount", status.getMissedCount());
                }
            }
            
            result.setData(data);
            result.setRequestStatus(200);
        } catch (Exception e) {
            log.error("Failed to get heartbeat status", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/send/{groupId}")
    public ResultModel<Map<String, Object>> sendHeartbeat(@PathVariable String groupId) {
        log.info("Sending heartbeat for group: {}", groupId);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            sceneGroupService.sendHeartbeat(groupId);
            
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("groupId", groupId);
            data.put("sent", true);
            data.put("timestamp", System.currentTimeMillis());
            
            result.setData(data);
            result.setRequestStatus(200);
        } catch (Exception e) {
            log.error("Failed to send heartbeat", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/members/{groupId}")
    public ResultModel<Map<String, Object>> getMemberHeartbeats(@PathVariable String groupId) {
        log.info("Getting member heartbeats for group: {}", groupId);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("groupId", groupId);
            data.put("timestamp", System.currentTimeMillis());
            
            result.setData(data);
            result.setRequestStatus(200);
        } catch (Exception e) {
            log.error("Failed to get member heartbeats", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }
}
