package net.ooder.examples.skillb.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;

@Service
public class DataSubmissionService {
    private static final Logger logger = LoggerFactory.getLogger(DataSubmissionService.class);
    private final NetworkService networkService;
    private final DiscoveryService discoveryService;

    @Autowired
    public DataSubmissionService(NetworkService networkService, DiscoveryService discoveryService) {
        this.networkService = networkService;
        this.discoveryService = discoveryService;
    }

    public Map<String, Object> submitData(Map<String, Object> data) {
        logger.info("Starting data submission process");
        
        // Check if we're joined to a scene
        if (!discoveryService.isJoined()) {
            logger.warn("Not joined to any scene. Proceeding with data submission anyway.");
        } else {
            logger.info("Submitting data while joined to scene: {}", discoveryService.getSceneId());
        }

        // Validate input data
        if (!validateData(data)) {
            logger.error("Input data validation failed");
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "FAILED");
            errorResult.put("message", "Data validation failed");
            errorResult.put("agentId", discoveryService.getAgentId());
            return errorResult;
        }

        // Submit data to network
        Map<String, Object> submissionResult = networkService.submitData(data);
        
        // Add metadata
        Map<String, Object> result = new HashMap<>(submissionResult);
        result.put("agentId", discoveryService.getAgentId());
        result.put("skillType", "skill-b");
        result.put("sceneId", discoveryService.getSceneId());
        result.put("timestamp", System.currentTimeMillis());
        
        logger.info("Data submission completed: {}", JSON.toJSONString(result));
        return result;
    }

    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("agentId", discoveryService.getAgentId());
        status.put("skillType", "skill-b");
        status.put("isJoined", discoveryService.isJoined());
        status.put("sceneId", discoveryService.getSceneId());
        status.put("submissionHistory", networkService.getSubmissionHistory());
        return status;
    }

    private boolean validateData(Map<String, Object> data) {
        // Basic validation example
        return data != null && 
               data.containsKey("id") && 
               data.containsKey("name") && 
               data.containsKey("status");
    }
}