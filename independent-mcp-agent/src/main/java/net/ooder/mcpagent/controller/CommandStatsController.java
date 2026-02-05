package net.ooder.mcpagent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/command/stats")
public class CommandStatsController {

    private static final Logger log = LoggerFactory.getLogger(CommandStatsController.class);

    // 命令执行历史记录
    private final List<CommandExecution> executionHistory = new ArrayList<>();

    // 命令类型统计
    private final Map<String, Integer> commandTypeStats = new ConcurrentHashMap<>();

    // 命令状态统计
    private final Map<String, Integer> commandStatusStats = new ConcurrentHashMap<>();

    // 初始化默认数据
    public CommandStatsController() {
        initializeDefaultData();
    }

    private void initializeDefaultData() {
        // 初始化命令状态统计
        commandStatusStats.put("success", 156);
        commandStatusStats.put("failed", 23);
        commandStatusStats.put("pending", 12);
        commandStatusStats.put("cancelled", 5);

        // 初始化命令类型统计
        commandTypeStats.put("network_config", 45);
        commandTypeStats.put("device_control", 67);
        commandTypeStats.put("security_audit", 34);
        commandTypeStats.put("system_management", 48);

        // 初始化执行历史数据
        long now = System.currentTimeMillis();
        for (int i = 0; i < 24; i++) {
            long timestamp = now - (i * 60 * 60 * 1000); // 过去24小时，每小时一条数据
            executionHistory.add(new CommandExecution(
                    "cmd-" + i,
                    "network_config",
                    "success",
                    timestamp,
                    timestamp + 1000 + (int)(Math.random() * 5000), // 执行时间1-6秒
                    "192.168.1." + (100 + i % 50)
            ));
            if (i % 5 == 0) {
                executionHistory.add(new CommandExecution(
                        "cmd-fail-" + i,
                        "device_control",
                        "failed",
                        timestamp + 30000,
                        timestamp + 35000,
                        "192.168.1." + (150 + i % 50)
                ));
            }
        }
    }

    /**
     * 获取命令执行统计
     */
    @GetMapping("/overview")
    public Map<String, Object> getCommandStatsOverview() {
        log.info("Get command stats overview requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCommands", commandStatusStats.values().stream().mapToInt(Integer::intValue).sum());
            stats.put("successRate", calculateSuccessRate());
            stats.put("averageExecutionTime", calculateAverageExecutionTime());
            stats.put("statusDistribution", commandStatusStats);
            stats.put("typeDistribution", commandTypeStats);

            response.put("status", "success");
            response.put("message", "Command stats overview retrieved successfully");
            response.put("data", stats);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting command stats overview: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get command stats overview: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取命令执行趋势
     */
    @GetMapping("/trend")
    public Map<String, Object> getCommandTrend(
            @RequestParam(defaultValue = "86400000") long timeRange, // 默认24小时
            @RequestParam(defaultValue = "3600000") long interval) { // 默认1小时
        log.info("Get command trend requested: timeRange={}, interval={}", timeRange, interval);
        Map<String, Object> response = new HashMap<>();

        try {
            long now = System.currentTimeMillis();
            long startTime = now - timeRange;

            List<Map<String, Object>> trendData = new ArrayList<>();
            long currentTime = startTime;

            while (currentTime < now) {
                final long currentIntervalStart = currentTime;
                final long currentIntervalEnd = currentTime + interval;
                List<CommandExecution> intervalCommands = executionHistory.stream()
                        .filter(cmd -> cmd.getStartTime() >= currentIntervalStart && cmd.getStartTime() < currentIntervalEnd)
                        .collect(Collectors.toList());

                Map<String, Object> intervalData = new HashMap<>();
                intervalData.put("timestamp", currentTime);
                intervalData.put("count", intervalCommands.size());
                intervalData.put("successCount", intervalCommands.stream().filter(cmd -> "success".equals(cmd.getStatus())).count());
                intervalData.put("failedCount", intervalCommands.stream().filter(cmd -> "failed".equals(cmd.getStatus())).count());

                trendData.add(intervalData);
                currentTime = currentIntervalEnd;
            }

            response.put("status", "success");
            response.put("message", "Command trend data retrieved successfully");
            response.put("data", trendData);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting command trend: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get command trend: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取命令类型分析
     */
    @GetMapping("/type-analysis")
    public Map<String, Object> getCommandTypeAnalysis() {
        log.info("Get command type analysis requested");
        Map<String, Object> response = new HashMap<>();

        try {
            List<Map<String, Object>> typeAnalysis = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : commandTypeStats.entrySet()) {
                Map<String, Object> typeData = new HashMap<>();
                typeData.put("type", entry.getKey());
                typeData.put("count", entry.getValue());
                typeData.put("percentage", calculateTypePercentage(entry.getKey()));
                typeAnalysis.add(typeData);
            }

            response.put("status", "success");
            response.put("message", "Command type analysis retrieved successfully");
            response.put("data", typeAnalysis);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting command type analysis: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get command type analysis: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取命令执行效率分析
     */
    @GetMapping("/efficiency")
    public Map<String, Object> getCommandEfficiencyAnalysis() {
        log.info("Get command efficiency analysis requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> efficiencyData = new HashMap<>();
            efficiencyData.put("averageExecutionTime", calculateAverageExecutionTime());
            efficiencyData.put("medianExecutionTime", calculateMedianExecutionTime());
            efficiencyData.put("executionTimePercentiles", calculateExecutionTimePercentiles());
            efficiencyData.put("successRate", calculateSuccessRate());

            response.put("status", "success");
            response.put("message", "Command efficiency analysis retrieved successfully");
            response.put("data", efficiencyData);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting command efficiency analysis: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get command efficiency analysis: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    /**
     * 获取命令执行失败分析
     */
    @GetMapping("/failure-analysis")
    public Map<String, Object> getCommandFailureAnalysis() {
        log.info("Get command failure analysis requested");
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> failureData = new HashMap<>();
            failureData.put("totalFailures", commandStatusStats.getOrDefault("failed", 0));
            failureData.put("failureRate", calculateFailureRate());
            failureData.put("failureReasons", getFailureReasons());

            response.put("status", "success");
            response.put("message", "Command failure analysis retrieved successfully");
            response.put("data", failureData);
            response.put("code", 200);
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error getting command failure analysis: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to get command failure analysis: " + e.getMessage());
            response.put("code", 500);
            response.put("timestamp", System.currentTimeMillis());
        }

        return response;
    }

    // 辅助方法：计算成功率
    private double calculateSuccessRate() {
        int total = commandStatusStats.values().stream().mapToInt(Integer::intValue).sum();
        int success = commandStatusStats.getOrDefault("success", 0);
        return total > 0 ? (double) success / total * 100 : 0;
    }

    // 辅助方法：计算失败率
    private double calculateFailureRate() {
        int total = commandStatusStats.values().stream().mapToInt(Integer::intValue).sum();
        int failed = commandStatusStats.getOrDefault("failed", 0);
        return total > 0 ? (double) failed / total * 100 : 0;
    }

    // 辅助方法：计算平均执行时间
    private long calculateAverageExecutionTime() {
        if (executionHistory.isEmpty()) {
            return 2500; // 默认2.5秒
        }
        long totalTime = executionHistory.stream()
                .mapToLong(cmd -> cmd.getEndTime() - cmd.getStartTime())
                .sum();
        return totalTime / executionHistory.size();
    }

    // 辅助方法：计算中位数执行时间
    private long calculateMedianExecutionTime() {
        if (executionHistory.isEmpty()) {
            return 2000; // 默认2秒
        }
        List<Long> executionTimes = executionHistory.stream()
                .map(cmd -> cmd.getEndTime() - cmd.getStartTime())
                .sorted()
                .collect(Collectors.toList());
        int middle = executionTimes.size() / 2;
        return executionTimes.get(middle);
    }

    // 辅助方法：计算执行时间百分位数
    private Map<String, Long> calculateExecutionTimePercentiles() {
        Map<String, Long> percentiles = new HashMap<>();
        if (executionHistory.isEmpty()) {
            percentiles.put("p50", 2000L);
            percentiles.put("p90", 4500L);
            percentiles.put("p99", 8000L);
            return percentiles;
        }

        List<Long> executionTimes = executionHistory.stream()
                .map(cmd -> cmd.getEndTime() - cmd.getStartTime())
                .sorted()
                .collect(Collectors.toList());

        percentiles.put("p50", executionTimes.get(executionTimes.size() * 50 / 100));
        percentiles.put("p90", executionTimes.get(executionTimes.size() * 90 / 100));
        percentiles.put("p99", executionTimes.get(executionTimes.size() * 99 / 100));

        return percentiles;
    }

    // 辅助方法：计算类型百分比
    private double calculateTypePercentage(String type) {
        int total = commandTypeStats.values().stream().mapToInt(Integer::intValue).sum();
        int count = commandTypeStats.getOrDefault(type, 0);
        return total > 0 ? (double) count / total * 100 : 0;
    }

    // 辅助方法：获取失败原因
    private List<Map<String, Object>> getFailureReasons() {
        List<Map<String, Object>> reasons = new ArrayList<>();

        Map<String, Object> reason1 = new HashMap<>();
        reason1.put("reason", "Network timeout");
        reason1.put("count", 8);
        reason1.put("percentage", 34.8);
        reasons.add(reason1);

        Map<String, Object> reason2 = new HashMap<>();
        reason2.put("reason", "Device not responding");
        reason2.put("count", 6);
        reason2.put("percentage", 26.1);
        reasons.add(reason2);

        Map<String, Object> reason3 = new HashMap<>();
        reason3.put("reason", "Permission denied");
        reason3.put("count", 4);
        reason3.put("percentage", 17.4);
        reasons.add(reason3);

        Map<String, Object> reason4 = new HashMap<>();
        reason4.put("reason", "Invalid parameters");
        reason4.put("count", 3);
        reason4.put("percentage", 13.0);
        reasons.add(reason4);

        Map<String, Object> reason5 = new HashMap<>();
        reason5.put("reason", "System error");
        reason5.put("count", 2);
        reason5.put("percentage", 8.7);
        reasons.add(reason5);

        return reasons;
    }

    // 命令执行记录类
    private static class CommandExecution {
        private final String commandId;
        private final String type;
        private final String status;
        private final long startTime;
        private final long endTime;
        private final String sourceIp;

        public CommandExecution(String commandId, String type, String status, long startTime, long endTime, String sourceIp) {
            this.commandId = commandId;
            this.type = type;
            this.status = status;
            this.startTime = startTime;
            this.endTime = endTime;
            this.sourceIp = sourceIp;
        }

        public String getCommandId() {
            return commandId;
        }

        public String getType() {
            return type;
        }

        public String getStatus() {
            return status;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public String getSourceIp() {
            return sourceIp;
        }
    }
}
