package net.ooder.nexus.debug.service;

import net.ooder.nexus.debug.model.*;
import net.ooder.nexus.debug.simulator.McpSimulator;
import net.ooder.nexus.debug.simulator.ProtocolSimulator;
import net.ooder.nexus.debug.simulator.RouteSimulator;
import net.ooder.nexus.debug.storage.LocalStorageEngine;
import net.ooder.nexus.debug.storage.LocalStorageEngine.StorageIndex;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 模拟器注册表 - 管理所有模拟器实例
 */
@Service
public class SimulatorRegistry {

    private final Map<String, ProtocolSimulator> simulators = new ConcurrentHashMap<>();
    private final LocalStorageEngine storage;

    public SimulatorRegistry() {
        this.storage = new LocalStorageEngine();
        loadSimulators();
    }

    /**
     * 注册模拟器
     */
    public void registerSimulator(ProtocolSimulator simulator) {
        String simulatorId = simulator.getSimulatorId();
        if (simulators.containsKey(simulatorId)) {
            throw new IllegalArgumentException("Simulator already registered: " + simulatorId);
        }

        simulators.put(simulatorId, simulator);
        saveSimulator(simulator);

        System.out.println("Simulator registered: " + simulatorId);
    }

    /**
     * 注销模拟器
     */
    public void unregisterSimulator(String simulatorId) {
        ProtocolSimulator simulator = simulators.remove(simulatorId);
        if (simulator != null) {
            if (simulator.getStatus().getStatus().equals("RUNNING")) {
                simulator.stop();
            }
            storage.delete("simulators", simulatorId);
            System.out.println("Simulator unregistered: " + simulatorId);
        }
    }

    /**
     * 获取模拟器
     */
    public ProtocolSimulator getSimulator(String simulatorId) {
        return simulators.get(simulatorId);
    }

    /**
     * 获取所有模拟器
     */
    public List<SimulatorInfo> getAllSimulators() {
        return simulators.values().stream()
                .map(this::toSimulatorInfo)
                .collect(Collectors.toList());
    }

    /**
     * 创建模拟器
     */
    public Simulator createSimulator(SimulatorConfig config) {
        String simulatorId = config.getSimulatorId();
        if (simulatorId == null || simulatorId.isEmpty()) {
            simulatorId = generateSimulatorId(config.getType());
            config.setSimulatorId(simulatorId);
        }

        Simulator simulator = new Simulator();
        simulator.setSimulatorId(simulatorId);
        simulator.setName(config.getName());
        simulator.setType(config.getType());
        simulator.setProtocolType(config.getProtocolType());
        simulator.setDescription(config.getDescription());
        simulator.setConfig(config.getConfig());
        simulator.setBehavior(config.getBehavior());
        simulator.setStatus("STOPPED");
        simulator.setCreatedAt(java.time.Instant.now().toString());

        // 保存到存储
        storage.save("simulators", simulatorId, simulator);

        // 创建模拟器实例
        ProtocolSimulator instance = createSimulatorInstance(config.getType());
        instance.initialize(simulator);

        return simulator;
    }

    /**
     * 启动模拟器
     */
    public void startSimulator(String simulatorId) {
        ProtocolSimulator simulator = simulators.get(simulatorId);
        if (simulator == null) {
            throw new IllegalArgumentException("Simulator not found: " + simulatorId);
        }

        simulator.start();
        updateSimulatorStatus(simulatorId, "RUNNING");
    }

    /**
     * 停止模拟器
     */
    public void stopSimulator(String simulatorId) {
        ProtocolSimulator simulator = simulators.get(simulatorId);
        if (simulator == null) {
            throw new IllegalArgumentException("Simulator not found: " + simulatorId);
        }

        simulator.stop();
        updateSimulatorStatus(simulatorId, "STOPPED");
    }

    /**
     * 执行场景
     */
    public ExecutionResult executeScenario(String simulatorId, Scenario scenario) {
        ProtocolSimulator simulator = simulators.get(simulatorId);
        if (simulator == null) {
            throw new IllegalArgumentException("Simulator not found: " + simulatorId);
        }

        return simulator.executeScenario(scenario);
    }

    /**
     * 获取模拟器日志
     */
    public List<ProtocolSimulator.ExecutionLog> getSimulatorLogs(String simulatorId) {
        ProtocolSimulator simulator = simulators.get(simulatorId);
        if (simulator == null) {
            return new ArrayList<>();
        }
        return simulator.getLogs();
    }

    /**
     * 加载已保存的模拟器
     */
    private void loadSimulators() {
        List<String> simulatorIds = storage.listFiles("simulators");
        for (String id : simulatorIds) {
            Simulator simulator = storage.load("simulators", id, Simulator.class);
            if (simulator != null) {
                ProtocolSimulator instance = createSimulatorInstance(simulator.getType());
                instance.initialize(simulator);
                simulators.put(simulator.getSimulatorId(), instance);
            }
        }
        System.out.println("Loaded " + simulators.size() + " simulators from storage");
    }

    /**
     * 保存模拟器
     */
    private void saveSimulator(ProtocolSimulator simulator) {
        SimulatorInfo info = toSimulatorInfo(simulator);
        storage.save("simulators", simulator.getSimulatorId(), info);
    }

    /**
     * 更新模拟器状态
     */
    private void updateSimulatorStatus(String simulatorId, String status) {
        Simulator simulator = storage.load("simulators", simulatorId, Simulator.class);
        if (simulator != null) {
            simulator.setStatus(status);
            simulator.setUpdatedAt(java.time.Instant.now().toString());
            storage.save("simulators", simulatorId, simulator);
        }
    }

    /**
     * 创建模拟器实例
     */
    private ProtocolSimulator createSimulatorInstance(SimulatorType type) {
        switch (type) {
            case MCP_NODE:
                return new McpSimulator();
            case ROUTE_NODE:
                return new RouteSimulator();
            case END_DEVICE:
                return new McpSimulator();
            default:
                throw new IllegalArgumentException("Unsupported simulator type: " + type);
        }
    }

    /**
     * 转换为模拟器信息
     */
    private SimulatorInfo toSimulatorInfo(ProtocolSimulator simulator) {
        SimulatorInfo info = new SimulatorInfo();
        info.simulatorId = simulator.getSimulatorId();
        info.name = simulator.getName();
        info.type = simulator.getType();
        info.protocolType = simulator.getProtocolType();
        info.status = simulator.getStatus().getStatus();
        info.commandsExecuted = simulator.getStatus().getCommandsExecuted();
        info.errors = simulator.getStatus().getErrors();
        info.uptime = simulator.getStatus().getUptime();
        return info;
    }

    /**
     * 生成模拟器ID
     */
    private String generateSimulatorId(SimulatorType type) {
        return "SIM-" + type.name() + "-" + System.currentTimeMillis();
    }

    /**
     * 模拟器信息
     */
    public static class SimulatorInfo {
        public String simulatorId;
        public String name;
        public SimulatorType type;
        public String protocolType;
        public String status;
        public int commandsExecuted;
        public int errors;
        public long uptime;
    }

    /**
     * 模拟器配置
     */
    public static class SimulatorConfig {
        public String simulatorId;
        public String name;
        public SimulatorType type;
        public String protocolType;
        public String description;
        public Map<String, Object> config;
        public Simulator.BehaviorConfig behavior;

        public String getSimulatorId() { return simulatorId; }
        public void setSimulatorId(String simulatorId) { this.simulatorId = simulatorId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public SimulatorType getType() { return type; }
        public void setType(SimulatorType type) { this.type = type; }
        public String getProtocolType() { return protocolType; }
        public void setProtocolType(String protocolType) { this.protocolType = protocolType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public Simulator.BehaviorConfig getBehavior() { return behavior; }
        public void setBehavior(Simulator.BehaviorConfig behavior) { this.behavior = behavior; }
    }
}
