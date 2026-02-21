package net.ooder.skillcenter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/actuator")
public class HealthController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("application", applicationName);
        healthStatus.put("port", serverPort);
        healthStatus.put("service", "SkillCenter");
        healthStatus.put("version", "0.7.0");
        return new ResponseEntity<>(healthStatus, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", applicationName);
        info.put("version", "0.7.0");
        info.put("description", "SkillCenter for Ooder Agent SDK");
        
        Map<String, Boolean> features = new HashMap<>();
        features.put("skill-registration", true);
        features.put("skill-management", true);
        features.put("skill-distribution", true);
        features.put("health-check", true);
        info.put("features", features);
        
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

}
