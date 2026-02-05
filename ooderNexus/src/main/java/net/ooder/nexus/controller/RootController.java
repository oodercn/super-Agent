package net.ooder.nexus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class RootController {
    
    @GetMapping
    public Map<String, Object> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Ooder Nexus API");
        response.put("status", "running");
        response.put("version", "1.0.0");
        response.put("endpoints", new String[] {
            "/api/personal",
            "/api/system/monitor",
            "/api/storage",
            "/api/skillcenter",
            "/api/p2p/network"
        });
        return response;
    }
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Ooder Nexus API");
        return response;
    }
}