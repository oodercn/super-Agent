package net.ooder.nexus.controller;

import net.ooder.nexus.service.P2PService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// @RestController
// @RequestMapping("/api/p2p/network")
public class P2PNetworkController {
    
    // @Autowired
    private P2PService p2pService;
    
    @GetMapping("/discovery")
    public ResponseEntity<Map<String, Object>> discoverNodes() {
        try {
            List<Map<String, Object>> nodes = p2pService.discoverNodes();
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", nodes);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to discover nodes: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinNetwork(@RequestBody Map<String, Object> nodeInfo) {
        try {
            boolean result = p2pService.joinNetwork(nodeInfo);
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", result);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to join network: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/nodes")
    public ResponseEntity<Map<String, Object>> getNodes() {
        try {
            List<Map<String, Object>> nodes = p2pService.getNodes();
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", nodes);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to get nodes: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/nodes/{nodeId}")
    public ResponseEntity<Map<String, Object>> getNodeDetails(@PathVariable String nodeId) {
        try {
            Map<String, Object> nodeDetails = p2pService.getNodeDetails(nodeId);
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", nodeDetails);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to get node details: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @DeleteMapping("/nodes/{nodeId}")
    public ResponseEntity<Map<String, Object>> removeNode(@PathVariable String nodeId) {
        try {
            boolean result = p2pService.removeNode(nodeId);
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", result);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to remove node: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/skill/publish")
    public ResponseEntity<Map<String, Object>> publishSkill(@RequestBody Map<String, Object> skillInfo) {
        try {
            boolean result = p2pService.publishSkill(skillInfo);
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", result);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to publish skill: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/skill/subscribe")
    public ResponseEntity<Map<String, Object>> subscribeSkill(@RequestBody Map<String, Object> request) {
        try {
            String skillId = (String) request.get("skillId");
            boolean result = p2pService.subscribeSkill(skillId);
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", result);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to subscribe skill: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/skill/market")
    public ResponseEntity<Map<String, Object>> getSkillMarket() {
        try {
            List<Map<String, Object>> skills = p2pService.getSkillMarket();
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", skills);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to get skill market: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getNetworkStatus() {
        try {
            Map<String, Object> status = p2pService.getNetworkStatus();
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", status);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to get network status: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/message")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, Object> request) {
        try {
            String targetNodeId = (String) request.get("targetNodeId");
            Map<String, Object> message = (Map<String, Object>) request.get("message");
            boolean result = p2pService.sendMessage(targetNodeId, message);
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", result);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "Failed to send message: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
