package net.ooder.examples.skillb.controller;

import net.ooder.examples.skillb.service.DataSubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/skill-b")
public class RpcController {
    private static final Logger logger = LoggerFactory.getLogger(RpcController.class);
    private final DataSubmissionService dataSubmissionService;

    @Autowired
    public RpcController(DataSubmissionService dataSubmissionService) {
        this.dataSubmissionService = dataSubmissionService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitData(@RequestBody Map<String, Object> data) {
        logger.info("Received data submission request: {}", data);
        try {
            Map<String, Object> result = dataSubmissionService.submitData(data);
            if ("SUCCESS".equals(result.get("status"))) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.error("Failed to submit data: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "FAILED");
            errorResponse.put("message", "Failed to submit data");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        logger.info("Received status request");
        try {
            Map<String, Object> status = dataSubmissionService.getStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("Failed to get status: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "FAILED");
            errorResponse.put("message", "Failed to get status");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, String> healthResponse = new HashMap<>();
        healthResponse.put("status", "UP");
        healthResponse.put("service", "Skill B - Data Submission");
        healthResponse.put("version", "1.0.0");
        return ResponseEntity.ok(healthResponse);
    }
}