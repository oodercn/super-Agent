package net.ooder.examples.skillc.controller;

import net.ooder.examples.skillc.model.ErrorDetail;
import net.ooder.examples.skillc.model.ErrorResponse;
import net.ooder.examples.skillc.model.Group;
import net.ooder.examples.skillc.model.Scene;
import net.ooder.examples.skillc.service.CoordinationService;
import net.ooder.examples.skillc.service.SceneService;
import net.ooder.examples.skillc.model.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skill-c")
public class RpcController {
    private static final Logger logger = LoggerFactory.getLogger(RpcController.class);

    private final SceneService sceneService;
    private final CoordinationService coordinationService;

    @Value("${skill.scene.id:RBC_SCENE_001}")
    private String defaultSceneId;

    @Autowired
    public RpcController(SceneService sceneService, CoordinationService coordinationService) {
        this.sceneService = sceneService;
        this.coordinationService = coordinationService;
    }
    
    // Helper method for standardized error responses
    private ResponseEntity<?> createErrorResponse(int code, HttpStatus httpStatus, String message, String sceneId, String groupId) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus("error");
        error.setCode(code);
        error.setHttpStatus(httpStatus.value());
        error.setMessage(message);
        
        ErrorDetail detail = new ErrorDetail();
        detail.setErrorId(UUID.randomUUID().toString());
        detail.setSceneGroupInfo(sceneId, groupId);
        error.setDetail(detail);
        
        return new ResponseEntity<>(error, httpStatus);
    }
    
    // Helper method for standardized success responses
    private ResponseEntity<?> createSuccessResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/scenes/{sceneId}")
    public ResponseEntity<?> getScene(@PathVariable String sceneId) {
        logger.info("Getting scene details: {}", sceneId);
        Scene scene = sceneService.getScene(sceneId);
        if (scene == null) {
            return createErrorResponse(SceneService.SCENE_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Scene not found", sceneId, null);
        }
        return createSuccessResponse(scene);
    }

    @GetMapping("/scenes/{sceneId}/participants")
    public ResponseEntity<?> getSceneParticipants(@PathVariable String sceneId) {
        logger.info("Getting participants for scene: {}", sceneId);
        Scene scene = sceneService.getScene(sceneId);
        if (scene == null) {
            return createErrorResponse(SceneService.SCENE_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Scene not found", sceneId, null);
        }
        List<Agent> participants = sceneService.getSceneParticipants(sceneId);
        return createSuccessResponse(participants);
    }

    @PostMapping("/scenes/{sceneId}/join")
    public ResponseEntity<?> joinScene(@PathVariable String sceneId, @RequestBody Agent agent) {
        logger.info("Agent joining scene: {} - Agent: {}", sceneId, agent.getAgentId());
        
        Scene scene = sceneService.getScene(sceneId);
        if (scene == null) {
            return createErrorResponse(SceneService.SCENE_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Scene not found", sceneId, null);
        }
        
        // 检查agent是否已经在场景中
        if (scene.containsParticipant(agent.getAgentId())) {
            return createErrorResponse(SceneService.AGENT_ALREADY_IN_SCENE, HttpStatus.BAD_REQUEST, 
                "Agent already in scene", sceneId, null);
        }
        
        boolean joined = sceneService.joinScene(sceneId, agent);
        if (joined) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Agent joined scene successfully");
            return createSuccessResponse(response);
        } else {
            return createErrorResponse(5000, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to join scene", sceneId, null);
        }
    }

    @PostMapping("/scenes/{sceneId}/leave")
    public ResponseEntity<?> leaveScene(@PathVariable String sceneId, @RequestBody Map<String, String> request) {
        String agentId = request.get("agentId");
        logger.info("Agent leaving scene: {} - Agent: {}", sceneId, agentId);
        
        if (agentId == null) {
            return createErrorResponse(SceneService.AGENT_NOT_FOUND, HttpStatus.BAD_REQUEST, 
                "Agent ID is required", sceneId, null);
        }
        
        Scene scene = sceneService.getScene(sceneId);
        if (scene == null) {
            return createErrorResponse(SceneService.SCENE_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Scene not found", sceneId, null);
        }
        
        if (!scene.containsParticipant(agentId)) {
            return createErrorResponse(SceneService.AGENT_NOT_FOUND, HttpStatus.BAD_REQUEST, 
                "Agent not in scene", sceneId, null);
        }
        
        boolean left = sceneService.leaveScene(sceneId, agentId);
        if (left) {
            coordinationService.clearAgentData(agentId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Agent left scene successfully");
            return createSuccessResponse(response);
        } else {
            return createErrorResponse(5000, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to leave scene", sceneId, null);
        }
    }

    @PostMapping("/data/coordinate")
    public ResponseEntity<?> coordinateDataFlow(@RequestBody(required = false) Map<String, String> request) {
        String sceneId = request != null && request.containsKey("sceneId") ? request.get("sceneId") : defaultSceneId;
        logger.info("Coordinating data flow for scene: {}", sceneId);
        
        Scene scene = sceneService.getScene(sceneId);
        if (scene == null) {
            return createErrorResponse(SceneService.SCENE_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Scene not found", sceneId, null);
        }
        
        try {
            Map<String, Object> result = coordinationService.coordinateDataFlow(sceneId).block();
            return createSuccessResponse(result);
        } catch (Exception e) {
            logger.error("Failed to coordinate data flow: {}", e.getMessage());
            return createErrorResponse(5000, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to coordinate data flow: " + e.getMessage(), sceneId, null);
        }
    }

    @GetMapping("/data/{agentId}")
    public ResponseEntity<?> getAgentData(@PathVariable String agentId) {
        logger.info("Getting data for agent: {}", agentId);
        Map<String, Object> data = coordinationService.getAgentData(agentId);
        if (data == null || data.isEmpty()) {
            return createErrorResponse(SceneService.AGENT_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "No data found for agent", null, null);
        }
        return createSuccessResponse(data);
    }

    @PostMapping("/data/store/{agentId}")
    public ResponseEntity<?> storeAgentData(@PathVariable String agentId, @RequestBody Map<String, Object> data) {
        logger.info("Storing data for agent: {}", agentId);
        
        if (agentId == null || agentId.isEmpty()) {
            return createErrorResponse(SceneService.AGENT_NOT_FOUND, HttpStatus.BAD_REQUEST, 
                "Agent ID is required", null, null);
        }
        
        boolean stored = coordinationService.storeAgentData(agentId, data);
        if (stored) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Data stored successfully");
            return createSuccessResponse(response);
        } else {
            return createErrorResponse(5000, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to store data", null, null);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Skill C - Route Agent");
        return createSuccessResponse(response);
    }
    
    // Group management endpoints
    @PostMapping("/scenes/{sceneId}/groups")
    public ResponseEntity<?> createGroup(@PathVariable String sceneId, @RequestBody Map<String, String> request) {
        logger.info("Creating group in scene: {}", sceneId);
        
        Scene scene = sceneService.getScene(sceneId);
        if (scene == null) {
            return createErrorResponse(SceneService.SCENE_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Scene not found", sceneId, null);
        }
        
        String name = request.get("name");
        String description = request.get("description");
        String groupId = request.get("groupId");
        
        if (name == null || groupId == null) {
            return createErrorResponse(5000, HttpStatus.BAD_REQUEST, 
                "Group name and ID are required", sceneId, null);
        }
        
        Group group = sceneService.createGroup(name, description != null ? description : "", groupId, sceneId);
        if (group == null) {
            return createErrorResponse(SceneService.GROUP_ALREADY_EXISTS, HttpStatus.BAD_REQUEST, 
                "Failed to create group", sceneId, null);
        }
        
        return createSuccessResponse(group);
    }
    
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable String groupId) {
        logger.info("Getting group details: {}", groupId);
        Group group = sceneService.getGroup(groupId);
        if (group == null) {
            return createErrorResponse(SceneService.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Group not found", null, groupId);
        }
        return createSuccessResponse(group);
    }
    
    @PostMapping("/groups/{groupId}/members")
    public ResponseEntity<?> addAgentToGroup(@PathVariable String groupId, @RequestBody Agent agent) {
        logger.info("Adding agent to group: {} - Agent: {}", groupId, agent.getAgentId());
        
        Group group = sceneService.getGroup(groupId);
        if (group == null) {
            return createErrorResponse(SceneService.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Group not found", group != null ? group.getSceneId() : null, groupId);
        }
        
        if (group.containsMember(agent.getAgentId())) {
            return createErrorResponse(SceneService.AGENT_ALREADY_IN_GROUP, HttpStatus.BAD_REQUEST, 
                "Agent already in group", group.getSceneId(), groupId);
        }
        
        boolean added = sceneService.addAgentToGroup(groupId, agent);
        if (added) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Agent added to group successfully");
            return createSuccessResponse(response);
        } else {
            return createErrorResponse(5000, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to add agent to group", group.getSceneId(), groupId);
        }
    }
    
    @DeleteMapping("/groups/{groupId}/members/{agentId}")
    public ResponseEntity<?> removeAgentFromGroup(@PathVariable String groupId, @PathVariable String agentId) {
        logger.info("Removing agent from group: {} - Agent: {}", groupId, agentId);
        
        Group group = sceneService.getGroup(groupId);
        if (group == null) {
            return createErrorResponse(SceneService.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Group not found", group != null ? group.getSceneId() : null, groupId);
        }
        
        if (!group.containsMember(agentId)) {
            return createErrorResponse(SceneService.AGENT_NOT_FOUND, HttpStatus.BAD_REQUEST, 
                "Agent not in group", group.getSceneId(), groupId);
        }
        
        boolean removed = sceneService.removeAgentFromGroup(groupId, agentId);
        if (removed) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Agent removed from group successfully");
            return createSuccessResponse(response);
        } else {
            return createErrorResponse(5000, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to remove agent from group", group.getSceneId(), groupId);
        }
    }
    
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable String groupId) {
        logger.info("Deleting group: {}", groupId);
        
        Group group = sceneService.getGroup(groupId);
        if (group == null) {
            return createErrorResponse(SceneService.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Group not found", group != null ? group.getSceneId() : null, groupId);
        }
        
        boolean deleted = sceneService.deleteGroup(groupId);
        if (deleted) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Group deleted successfully");
            return createSuccessResponse(response);
        } else {
            return createErrorResponse(5000, HttpStatus.INTERNAL_SERVER_ERROR, 
                "Failed to delete group", group.getSceneId(), groupId);
        }
    }
    
    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable String groupId) {
        logger.info("Getting members for group: {}", groupId);
        
        Group group = sceneService.getGroup(groupId);
        if (group == null) {
            return createErrorResponse(SceneService.GROUP_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Group not found", group != null ? group.getSceneId() : null, groupId);
        }
        
        List<Agent> members = sceneService.getGroupMembers(groupId);
        return createSuccessResponse(members);
    }
    
    @GetMapping("/scenes/{sceneId}/groups")
    public ResponseEntity<?> getSceneGroups(@PathVariable String sceneId) {
        logger.info("Getting groups for scene: {}", sceneId);
        
        Scene scene = sceneService.getScene(sceneId);
        if (scene == null) {
            return createErrorResponse(SceneService.SCENE_NOT_FOUND, HttpStatus.NOT_FOUND, 
                "Scene not found", sceneId, null);
        }
        
        List<Group> groups = sceneService.getSceneGroups(sceneId);
        return createSuccessResponse(groups);
    }
}