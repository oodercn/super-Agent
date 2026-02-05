package net.ooder.examples.skilla.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;

@Service
public class InformationRetrievalService {
    private static final Logger logger = LoggerFactory.getLogger(InformationRetrievalService.class);
    private final NetworkService networkService;
    private final DiscoveryService discoveryService;

    @Autowired
    public InformationRetrievalService(NetworkService networkService, DiscoveryService discoveryService) {
        this.networkService = networkService;
        this.discoveryService = discoveryService;
    }

    public Map<String, Object> retrieveData() {
        logger.info("Starting information retrieval process");
        
        // Check if we're joined to a scene
        if (!discoveryService.isJoined()) {
            logger.warn("Not joined to any scene. Proceeding with data retrieval anyway.");
        } else {
            logger.info("Retrieving data while joined to scene: {}", discoveryService.getSceneId());
        }

        // Retrieve information from network
        Map<String, Object> retrievedData = networkService.retrieveInformation();
        
        // Add metadata
        Map<String, Object> result = new HashMap<>(retrievedData);
        result.put("agentId", discoveryService.getAgentId());
        result.put("skillType", "skill-a");
        result.put("sceneId", discoveryService.getSceneId());
        result.put("timestamp", System.currentTimeMillis());
        result.put("status", "SUCCESS");
        
        logger.info("Information retrieval completed: {}", JSON.toJSONString(result));
        return result;
    }

    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("agentId", discoveryService.getAgentId());
        status.put("skillType", "skill-a");
        status.put("isJoined", discoveryService.isJoined());
        status.put("sceneId", discoveryService.getSceneId());
        status.put("cachedData", networkService.getCachedData());
        return status;
    }

    public Map<String, Object> retrieveDataWithValidation() {
        try {
            Map<String, Object> data = retrieveData();
            
            // Validate retrieved data
            if (validateData(data)) {
                return data;
            } else {
                logger.error("Retrieved data failed validation");
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("status", "FAILED");
                errorResult.put("message", "Data validation failed");
                errorResult.put("agentId", discoveryService.getAgentId());
                return errorResult;
            }
        } catch (Exception e) {
            logger.error("Error during information retrieval: {}", e.getMessage());
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "FAILED");
            errorResult.put("message", e.getMessage());
            errorResult.put("agentId", discoveryService.getAgentId());
            return errorResult;
        }
    }

    private boolean validateData(Map<String, Object> data) {
        // Basic validation example
        return data != null && 
               data.containsKey("id") && 
               data.containsKey("name") && 
               data.containsKey("status") && 
               "SUCCESS".equals(data.get("status"));
    }
}