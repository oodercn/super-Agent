package net.ooder.nexus.debug.service;

import net.ooder.nexus.debug.model.*;
import net.ooder.nexus.debug.simulator.ProtocolSimulator;
import net.ooder.nexus.debug.storage.LocalStorageEngine;
import net.ooder.nexus.debug.storage.LocalStorageEngine.StorageIndex;
import net.ooder.nexus.core.protocol.ProtocolHub;
import net.ooder.nexus.core.protocol.model.ProtocolStats;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 调试控制台主服务
 */
@Service
public class DebugConsoleService {

    private final SimulatorRegistry simulatorRegistry;
    private final LocalStorageEngine storage;
    private final ProtocolHub protocolHub;

    @Autowired
    public DebugConsoleService(ProtocolHub protocolHub) {
        this.simulatorRegistry = new SimulatorRegistry();
        this.storage = new LocalStorageEngine();
        this.protocolHub = protocolHub;
    }

    /**
     * 获取所有协议
     */
    public List<ProtocolInfo> getAllProtocols() {
        List<String> protocolTypes = protocolHub.getSupportedProtocols();
        List<ProtocolInfo> protocols = new ArrayList<>();

        for (String type : protocolTypes) {
            ProtocolStats stats = protocolHub.getProtocolStats(type);
            if (stats != null) {
                ProtocolInfo info = new ProtocolInfo();
                info.protocolId = type;
                info.protocolType = type;
                info.name = getProtocolName(type);
                info.description = getProtocolDescription(type);
                info.version = "2.0";
                info.status = "ACTIVE";
                info.commandCount = stats.getTotalCommands();
                info.successRate = calculateSuccessRate(stats);
                info.avgResponseTime = stats.getAvgResponseTime();
                info.createdAt = java.time.Instant.now().toString();
                protocols.add(info);
            }
        }

        return protocols;
    }

    /**
     * 获取协议详情
     */
    public ProtocolDetail getProtocolDetail(String protocolType) {
        if (!protocolHub.isProtocolRegistered(protocolType)) {
            throw new IllegalArgumentException("Protocol not found: " + protocolType);
        }

        ProtocolStats stats = protocolHub.getProtocolStats(protocolType);
        ProtocolDetail detail = new ProtocolDetail();
        detail.protocolId = protocolType;
        detail.protocolType = protocolType;
        detail.name = getProtocolName(protocolType);
        detail.description = getProtocolDescription(protocolType);
        detail.version = "2.0";
        detail.status = "ACTIVE";
        detail.supportedCommands = getSupportedCommands(protocolType);
        detail.statistics = toStatistics(stats);
        detail.createdAt = java.time.Instant.now().toString();

        return detail;
    }

    /**
     * 获取所有模拟器
     */
    public List<SimulatorRegistry.SimulatorInfo> getAllSimulators() {
        return simulatorRegistry.getAllSimulators();
    }

    /**
     * 创建模拟器
     */
    public Simulator createSimulator(SimulatorRegistry.SimulatorConfig config) {
        return simulatorRegistry.createSimulator(config);
    }

    /**
     * 启动模拟器
     */
    public void startSimulator(String simulatorId) {
        simulatorRegistry.startSimulator(simulatorId);
    }

    /**
     * 停止模拟器
     */
    public void stopSimulator(String simulatorId) {
        simulatorRegistry.stopSimulator(simulatorId);
    }

    /**
     * 执行场景
     */
    public ExecutionResult executeScenario(String simulatorId, Scenario scenario) {
        return simulatorRegistry.executeScenario(simulatorId, scenario);
    }

    /**
     * 获取模拟器日志
     */
    public List<ProtocolSimulator.ExecutionLog> getSimulatorLogs(String simulatorId) {
        return simulatorRegistry.getSimulatorLogs(simulatorId);
    }

    /**
     * 获取所有场景
     */
    public List<ScenarioInfo> getAllScenarios() {
        List<String> scenarioIds = storage.listFiles("scenarios");
        List<ScenarioInfo> scenarios = new ArrayList<>();

        for (String id : scenarioIds) {
            Scenario scenario = storage.load("scenarios", id, Scenario.class);
            if (scenario != null) {
                ScenarioInfo info = new ScenarioInfo();
                info.scenarioId = scenario.getScenarioId();
                info.name = scenario.getName();
                info.type = scenario.getType();
                info.protocolType = scenario.getProtocolType();
                info.description = scenario.getDescription();
                info.stepCount = scenario.getSteps() != null ? scenario.getSteps().size() : 0;
                info.createdAt = scenario.getCreatedAt();
                scenarios.add(info);
            }
        }

        return scenarios;
    }

    /**
     * 保存场景
     */
    public void saveScenario(Scenario scenario) {
        storage.save("scenarios", scenario.getScenarioId(), scenario);
    }

    /**
     * 获取所有执行结果
     */
    public List<ExecutionResult> getAllExecutionResults() {
        List<String> executionIds = storage.listFiles("results");
        List<ExecutionResult> results = new ArrayList<>();

        for (String id : executionIds) {
            ExecutionResult result = storage.load("results", id, ExecutionResult.class);
            if (result != null) {
                results.add(result);
            }
        }

        return results;
    }

    /**
     * 保存执行结果
     */
    public void saveExecutionResult(ExecutionResult result) {
        storage.save("results", result.getExecutionId(), result);
    }

    /**
     * 获取协议名称
     */
    private String getProtocolName(String type) {
        switch (type) {
            case "MCP": return "Master Control Protocol";
            case "ROUTE": return "Route Protocol";
            case "END": return "End Device Protocol";
            default: return type;
        }
    }

    /**
     * 获取协议描述
     */
    private String getProtocolDescription(String type) {
        switch (type) {
            case "MCP": return "主控制协议，用于管理MCP节点";
            case "ROUTE": return "路由协议，用于网络路由转发";
            case "END": return "终端设备协议，用于终端设备通信";
            default: return "Unknown protocol";
        }
    }

    /**
     * 获取支持的命令
     */
    private List<CommandInfo> getSupportedCommands(String protocolType) {
        List<CommandInfo> commands = new ArrayList<>();

        switch (protocolType) {
            case "MCP":
                commands.add(new CommandInfo("MCP_REGISTER", "节点注册"));
                commands.add(new CommandInfo("MCP_DEREGISTER", "节点注销"));
                commands.add(new CommandInfo("MCP_HEARTBEAT", "心跳保活"));
                commands.add(new CommandInfo("MCP_STATUS", "状态查询"));
                commands.add(new CommandInfo("MCP_DISCOVER", "设备发现"));
                commands.add(new CommandInfo("MCP_CONFIG", "配置下发"));
                break;
            case "ROUTE":
                commands.add(new CommandInfo("ROUTE_REGISTER", "路由注册"));
                commands.add(new CommandInfo("ROUTE_DEREGISTER", "路由注销"));
                commands.add(new CommandInfo("ROUTE_UPDATE", "路由表更新"));
                commands.add(new CommandInfo("ROUTE_QUERY", "路由查询"));
                commands.add(new CommandInfo("ROUTE_STATUS", "状态查询"));
                commands.add(new CommandInfo("ROUTE_HEARTBEAT", "心跳保活"));
                break;
            case "END":
                commands.add(new CommandInfo("END_REGISTER", "终端注册"));
                commands.add(new CommandInfo("END_DEREGISTER", "终端注销"));
                commands.add(new CommandInfo("END_CAPABILITY", "能力上报"));
                commands.add(new CommandInfo("END_STATUS", "状态查询"));
                commands.add(new CommandInfo("END_COMMAND", "命令下发"));
                commands.add(new CommandInfo("END_RESULT", "结果上报"));
                commands.add(new CommandInfo("END_HEARTBEAT", "心跳保活"));
                break;
        }

        return commands;
    }

    /**
     * 转换为统计信息
     */
    private ProtocolStatistics toStatistics(ProtocolStats stats) {
        ProtocolStatistics statistics = new ProtocolStatistics();
        statistics.totalCommands = stats.getTotalCommands();
        statistics.successCommands = stats.getSuccessCommands();
        statistics.failedCommands = stats.getFailedCommands();
        statistics.successRate = calculateSuccessRate(stats);
        statistics.avgResponseTime = stats.getAvgResponseTime();
        return statistics;
    }

    /**
     * 计算成功率
     */
    private double calculateSuccessRate(ProtocolStats stats) {
        if (stats.getTotalCommands() == 0) {
            return 0.0;
        }
        return (double) stats.getSuccessCommands() / stats.getTotalCommands() * 100;
    }

    /**
     * 协议信息
     */
    public static class ProtocolInfo {
        public String protocolId;
        public String protocolType;
        public String name;
        public String description;
        public String version;
        public String status;
        public long commandCount;
        public double successRate;
        public double avgResponseTime;
        public String createdAt;
    }

    /**
     * 协议详情
     */
    public static class ProtocolDetail {
        public String protocolId;
        public String protocolType;
        public String name;
        public String description;
        public String version;
        public String status;
        public List<CommandInfo> supportedCommands;
        public ProtocolStatistics statistics;
        public String createdAt;
    }

    /**
     * 命令信息
     */
    public static class CommandInfo {
        public CommandInfo(String commandId, String description) {
            this.commandId = commandId;
            this.description = description;
        }
        public String commandId;
        public String description;
    }

    /**
     * 协议统计
     */
    public static class ProtocolStatistics {
        public long totalCommands;
        public long successCommands;
        public long failedCommands;
        public double successRate;
        public double avgResponseTime;
    }

    /**
     * 场景信息
     */
    public static class ScenarioInfo {
        public String scenarioId;
        public String name;
        public String type;
        public String protocolType;
        public String description;
        public int stepCount;
        public String createdAt;
    }
}
