package net.ooder.examples.skilla.controller;

import net.ooder.examples.skilla.service.InformationRetrievalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/skill-a")
public class RpcController {
    private static final Logger logger = LoggerFactory.getLogger(RpcController.class);
    private final InformationRetrievalService informationRetrievalService;

    @Autowired
    public RpcController(InformationRetrievalService informationRetrievalService) {
        this.informationRetrievalService = informationRetrievalService;
    }

    @GetMapping("/retrieve")
    public ResponseEntity<?> retrieveInformation() {
        logger.info("Received information retrieval request");
        try {
            Map<String, Object> data = informationRetrievalService.retrieveData();
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            logger.error("Failed to retrieve information: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "FAILED");
            errorResponse.put("message", "Failed to retrieve information");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/retrieve/validated")
    public ResponseEntity<?> retrieveValidatedInformation() {
        logger.info("Received validated information retrieval request");
        try {
            Map<String, Object> data = informationRetrievalService.retrieveDataWithValidation();
            if ("SUCCESS".equals(data.get("status"))) {
                return ResponseEntity.ok(data);
            } else {
                return ResponseEntity.badRequest().body(data);
            }
        } catch (Exception e) {
            logger.error("Failed to retrieve validated information: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "FAILED");
            errorResponse.put("message", "Failed to retrieve validated information");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        logger.info("Received status request");
        try {
            Map<String, Object> status = informationRetrievalService.getStatus();
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

    @PostMapping("/retrieve")
    public ResponseEntity<?> retrieveInformationPost(@RequestBody(required = false) Map<String, Object> request) {
        logger.info("Received POST information retrieval request: {}", request);
        // For compatibility with POST requests
        return retrieveInformation();
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, String> healthResponse = new HashMap<>();
        healthResponse.put("status", "UP");
        healthResponse.put("service", "Skill A - Information Retrieval");
        healthResponse.put("version", "1.0.0");
        return ResponseEntity.ok(healthResponse);
    }
}