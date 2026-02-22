package net.ooder.nexus.debug.controller;

import net.ooder.nexus.common.model.ApiResponse;
import net.ooder.nexus.common.model.PageResult;
import net.ooder.nexus.debug.simulator.ProtocolSimulator;
import net.ooder.nexus.debug.model.Simulator;
import net.ooder.nexus.debug.model.ExecutionResult;
import net.ooder.nexus.debug.model.ExecutionLog;
import net.ooder.nexus.debug.model.Scenario;
import net.ooder.nexus.debug.service.DebugConsoleService;
import net.ooder.nexus.debug.service.SimulatorRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调试控制台控制器
 */
@RestController
@RequestMapping("/api/v1/debug")
@CrossOrigin(origins = "*")
public class DebugConsoleController {

    @Autowired
    private DebugConsoleService debugConsoleService;

    @Autowired
    private SimulatorRegistry simulatorRegistry;

    /**
     * 获取所有协议
     */
    @GetMapping("/protocols")
    public ApiResponse<List<DebugConsoleService.ProtocolInfo>> getProtocols() {
        try {
            List<DebugConsoleService.ProtocolInfo> protocols = debugConsoleService.getAllProtocols();
            return ApiResponse.success(protocols);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get protocols: " + e.getMessage());
        }
    }

    /**
     * 获取协议详情
     */
    @GetMapping("/protocols/{protocolType}")
    public ApiResponse<DebugConsoleService.ProtocolDetail> getProtocolDetail(
            @PathVariable String protocolType) {
        try {
            DebugConsoleService.ProtocolDetail detail = debugConsoleService.getProtocolDetail(protocolType);
            return ApiResponse.success(detail);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error("404", e.getMessage());
        } catch (Exception e) {
            return ApiResponse.error("Failed to get protocol detail: " + e.getMessage());
        }
    }

    /**
     * 获取所有模拟器
     */
    @GetMapping("/simulators")
    public ApiResponse<List<SimulatorRegistry.SimulatorInfo>> getSimulators() {
        try {
            List<SimulatorRegistry.SimulatorInfo> simulators = debugConsoleService.getAllSimulators();
            return ApiResponse.success(simulators);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get simulators: " + e.getMessage());
        }
    }

    /**
     * 创建模拟器
     */
    @PostMapping("/simulators")
    public ApiResponse<Simulator> createSimulator(@RequestBody SimulatorRegistry.SimulatorConfig config) {
        try {
            Simulator simulator = debugConsoleService.createSimulator(config);
            return ApiResponse.successWithData(simulator);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create simulator: " + e.getMessage());
        }
    }

    /**
     * 获取模拟器详情
     */
    @GetMapping("/simulators/{simulatorId}")
    public ApiResponse<Simulator> getSimulator(@PathVariable String simulatorId) {
        try {
            ProtocolSimulator simulator = simulatorRegistry.getSimulator(simulatorId);
            if (simulator == null) {
                return ApiResponse.error("404", "Simulator not found: " + simulatorId);
            }

            Simulator simInfo = new Simulator();
            simInfo.setSimulatorId(simulator.getSimulatorId());
            simInfo.setName(simulator.getName());
            simInfo.setType(simulator.getType());
            simInfo.setProtocolType(simulator.getProtocolType());
            simInfo.setStatus(simulator.getStatus().getStatus());

            return ApiResponse.success(simInfo);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get simulator: " + e.getMessage());
        }
    }

    /**
     * 启动模拟器
     */
    @PostMapping("/simulators/{simulatorId}/start")
    public ApiResponse<String> startSimulator(@PathVariable String simulatorId) {
        try {
            debugConsoleService.startSimulator(simulatorId);
            return ApiResponse.success("200", "Simulator started successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to start simulator: " + e.getMessage());
        }
    }

    /**
     * 停止模拟器
     */
    @PostMapping("/simulators/{simulatorId}/stop")
    public ApiResponse<String> stopSimulator(@PathVariable String simulatorId) {
        try {
            debugConsoleService.stopSimulator(simulatorId);
            return ApiResponse.success("200", "Simulator stopped successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to stop simulator: " + e.getMessage());
        }
    }

    /**
     * 删除模拟器
     */
    @DeleteMapping("/simulators/{simulatorId}")
    public ApiResponse<String> deleteSimulator(@PathVariable String simulatorId) {
        try {
            simulatorRegistry.unregisterSimulator(simulatorId);
            return ApiResponse.success("200", "Simulator deleted successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete simulator: " + e.getMessage());
        }
    }

    /**
     * 获取所有场景
     */
    @GetMapping("/scenarios")
    public ApiResponse<List<DebugConsoleService.ScenarioInfo>> getScenarios() {
        try {
            List<DebugConsoleService.ScenarioInfo> scenarios = debugConsoleService.getAllScenarios();
            return ApiResponse.success(scenarios);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get scenarios: " + e.getMessage());
        }
    }

    /**
     * 保存场景
     */
    @PostMapping("/scenarios")
    public ApiResponse<String> saveScenario(@RequestBody Scenario scenario) {
        try {
            debugConsoleService.saveScenario(scenario);
            return ApiResponse.success("200", "Scenario saved successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to save scenario: " + e.getMessage());
        }
    }

    /**
     * 执行场景
     */
    @PostMapping("/simulators/{simulatorId}/execute")
    public ApiResponse<ExecutionResult> executeScenario(
            @PathVariable String simulatorId,
            @RequestBody Scenario scenario) {
        try {
            ExecutionResult result = debugConsoleService.executeScenario(simulatorId, scenario);
            return ApiResponse.successWithData(result);
        } catch (Exception e) {
            return ApiResponse.error("Failed to execute scenario: " + e.getMessage());
        }
    }

    /**
     * 获取执行结果
     */
    @GetMapping("/executions/{executionId}")
    public ApiResponse<ExecutionResult> getExecutionResult(@PathVariable String executionId) {
        try {
            List<ExecutionResult> results = debugConsoleService.getAllExecutionResults();
            for (ExecutionResult result : results) {
                if (result.getExecutionId().equals(executionId)) {
                    return ApiResponse.success(result);
                }
            }
            return ApiResponse.error("404", "Execution not found: " + executionId);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get execution result: " + e.getMessage());
        }
    }

    /**
     * 获取模拟器日志
     */
    @GetMapping("/simulators/{simulatorId}/logs")
    public ApiResponse<List<ProtocolSimulator.ExecutionLog>> getSimulatorLogs(@PathVariable String simulatorId) {
        try {
            List<ProtocolSimulator.ExecutionLog> logs = debugConsoleService.getSimulatorLogs(simulatorId);
            return ApiResponse.success(logs);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get simulator logs: " + e.getMessage());
        }
    }

    /**
     * 获取所有执行结果
     */
    @GetMapping("/executions")
    public ApiResponse<List<ExecutionResult>> getAllExecutions() {
        try {
            List<ExecutionResult> results = debugConsoleService.getAllExecutionResults();
            return ApiResponse.success(results);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get executions: " + e.getMessage());
        }
    }
}
