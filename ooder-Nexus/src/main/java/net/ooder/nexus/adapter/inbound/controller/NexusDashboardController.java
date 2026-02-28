package net.ooder.nexus.adapter.inbound.controller;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.dashboard.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/nexus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NexusDashboardController {

    @GetMapping("/command/stats")
    public ResultModel<CommandStatsDTO> getCommandStats() {
        CommandStatsDTO stats = new CommandStatsDTO();
        stats.setTotal(12580L);
        stats.setSuccess(12350L);
        stats.setFailed(230L);
        stats.setPending(15L);
        stats.setSuccessRate(98.2);
        
        Map<String, Long> byType = new HashMap<>();
        byType.put("MCP", 5230L);
        byType.put("ROUTE", 4120L);
        byType.put("END", 3230L);
        stats.setByType(byType);
        
        return ResultModel.success("获取成功", stats);
    }

    @GetMapping("/health/overview")
    public ResultModel<HealthOverviewDTO> getHealthOverview() {
        HealthOverviewDTO overview = new HealthOverviewDTO();
        overview.setStatus("healthy");
        overview.setScore(95);
        
        List<HealthOverviewDTO.ComponentHealthDTO> components = new ArrayList<>();
        components.add(createComponentHealth("network", "healthy", 98));
        components.add(createComponentHealth("storage", "healthy", 95));
        components.add(createComponentHealth("skills", "healthy", 92));
        components.add(createComponentHealth("security", "warning", 85));
        overview.setComponents(components);
        
        List<HealthOverviewDTO.AlertDTO> alerts = new ArrayList<>();
        alerts.add(createAlert("warning", "安全证书即将过期", "2026-03-15"));
        overview.setAlerts(alerts);
        
        return ResultModel.success("获取成功", overview);
    }

    private HealthOverviewDTO.ComponentHealthDTO createComponentHealth(String name, String status, int score) {
        HealthOverviewDTO.ComponentHealthDTO health = new HealthOverviewDTO.ComponentHealthDTO();
        health.setName(name);
        health.setStatus(status);
        health.setScore(score);
        return health;
    }

    private HealthOverviewDTO.AlertDTO createAlert(String level, String message, String date) {
        HealthOverviewDTO.AlertDTO alert = new HealthOverviewDTO.AlertDTO();
        alert.setLevel(level);
        alert.setMessage(message);
        alert.setDate(date);
        return alert;
    }
}
