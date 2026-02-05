package net.ooder.skillcenter.shell.lifecycle;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.lifecycle.traffic.TrafficControlManager;
import net.ooder.skillcenter.lifecycle.traffic.TrafficControlManager.RateLimitConfig;
import net.ooder.skillcenter.lifecycle.traffic.TrafficControlManager.CircuitBreakerConfig;
import net.ooder.skillcenter.lifecycle.traffic.TrafficControlManager.TrafficStats;
import net.ooder.skillcenter.lifecycle.traffic.impl.TrafficControlManagerImpl;

public class SkillTrafficCommand extends AbstractCommand {
    
    private TrafficControlManager trafficManager;
    
    public SkillTrafficCommand() {
        super();
        this.trafficManager = new TrafficControlManagerImpl();
    }
    
    @Override
    public String getName() {
        return "skill traffic";
    }
    
    @Override
    public String getDescription() {
        return "管理技能流量控制";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            error("用法: skill traffic <skillId> <action> [options]");
            println("操作类型:");
            println("  rate-limit               - 设置限流配置");
            println("  circuit-breaker          - 设置熔断配置");
            println("  stats                    - 查看流量统计");
            println("");
            println("限流选项:");
            println("  --maxRequestsPerSecond <num>    - 每秒最大请求数");
            println("  --maxRequestsPerMinute <num>    - 每分钟最大请求数");
            println("  --maxConcurrentRequests <num>    - 最大并发请求数");
            println("  --timeWindowMs <ms>                - 时间窗口 (毫秒)");
            println("");
            println("熔断选项:");
            println("  --failureThreshold <num>         - 失败阈值");
            println("  --successThreshold <num>         - 成功阈值");
            println("  --timeoutMs <ms>                - 超时时间 (毫秒)");
            println("  --halfOpenTimeoutMs <ms>         - 半开超时 (毫秒)");
            println("  --enabled <true|false>          - 是否启用");
            return;
        }
        
        String skillId = args[0];
        String action = args[1];
        
        switch (action) {
            case "rate-limit":
                handleRateLimit(skillId, args);
                break;
            case "circuit-breaker":
                handleCircuitBreaker(skillId, args);
                break;
            case "stats":
                handleStats(skillId);
                break;
            default:
                error("无效的操作类型: " + action);
                println("有效的操作类型: rate-limit, circuit-breaker, stats");
        }
    }
    
    private void handleRateLimit(String skillId, String[] args) {
        RateLimitConfig config = new RateLimitConfig();
        
        for (int i = 2; i < args.length; i++) {
            if (args[i].startsWith("--maxRequestsPerSecond=")) {
                config.setMaxRequestsPerSecond(Integer.parseInt(args[i].substring(22)));
            } else if (args[i].startsWith("--maxRequestsPerMinute=")) {
                config.setMaxRequestsPerMinute(Integer.parseInt(args[i].substring(22)));
            } else if (args[i].startsWith("--maxConcurrentRequests=")) {
                config.setMaxConcurrentRequests(Integer.parseInt(args[i].substring(24)));
            } else if (args[i].startsWith("--timeWindowMs=")) {
                config.setTimeWindowMs(Long.parseLong(args[i].substring(14)));
            }
        }
        
        println("正在设置限流配置: " + skillId);
        trafficManager.setRateLimit(skillId, config);
        
        success("限流配置设置成功");
        println("每秒最大请求数: " + config.getMaxRequestsPerSecond());
        println("每分钟最大请求数: " + config.getMaxRequestsPerMinute());
        println("最大并发请求数: " + config.getMaxConcurrentRequests());
        println("时间窗口: " + config.getTimeWindowMs() + " ms");
    }
    
    private void handleCircuitBreaker(String skillId, String[] args) {
        CircuitBreakerConfig config = new CircuitBreakerConfig();
        
        for (int i = 2; i < args.length; i++) {
            if (args[i].startsWith("--failureThreshold=")) {
                config.setFailureThreshold(Integer.parseInt(args[i].substring(19)));
            } else if (args[i].startsWith("--successThreshold=")) {
                config.setSuccessThreshold(Integer.parseInt(args[i].substring(19)));
            } else if (args[i].startsWith("--timeoutMs=")) {
                config.setTimeoutMs(Long.parseLong(args[i].substring(12)));
            } else if (args[i].startsWith("--halfOpenTimeoutMs=")) {
                config.setHalfOpenTimeoutMs(Long.parseLong(args[i].substring(19)));
            } else if (args[i].startsWith("--enabled=")) {
                config.setEnabled(Boolean.parseBoolean(args[i].substring(10)));
            }
        }
        
        println("正在设置熔断配置: " + skillId);
        trafficManager.setCircuitBreaker(skillId, config);
        
        success("熔断配置设置成功");
        println("失败阈值: " + config.getFailureThreshold());
        println("成功阈值: " + config.getSuccessThreshold());
        println("超时时间: " + config.getTimeoutMs() + " ms");
        println("半开超时: " + config.getHalfOpenTimeoutMs() + " ms");
        println("是否启用: " + config.isEnabled());
    }
    
    private void handleStats(String skillId) {
        TrafficStats stats = trafficManager.getTrafficStats(skillId);
        
        if (stats == null) {
            println("技能 " + skillId + " 没有流量统计");
            return;
        }
        
        println("==============================================");
        println("流量统计: " + skillId);
        println("==============================================");
        println("总请求数:       " + stats.getTotalRequests());
        println("成功请求数:     " + stats.getSuccessfulRequests());
        println("失败请求数:     " + stats.getFailedRequests());
        println("当前请求数:     " + stats.getCurrentRequests());
        println("平均响应时间:   " + String.format("%.2f ms", stats.getAverageResponseTime()));
        println("最后更新时间:   " + formatTimestamp(stats.getLastUpdated()));
        println("==============================================");
    }
    
    private String formatTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "N/A";
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
