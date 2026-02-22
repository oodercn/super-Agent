package net.ooder.nexus.debug.simulator;

import net.ooder.nexus.debug.model.ExecutionResult;
import net.ooder.nexus.debug.model.ExecutionLog;
import net.ooder.nexus.debug.model.StepResult;
import net.ooder.nexus.debug.model.ExecutionSummary;
import net.ooder.nexus.debug.model.Scenario;
import net.ooder.nexus.debug.model.ScenarioStep;
import net.ooder.nexus.debug.model.Simulator;
import net.ooder.nexus.debug.model.SimulatorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟器抽象基类
 */
public abstract class AbstractSimulator implements ProtocolSimulator {

    protected String simulatorId;
    protected String name;
    protected String protocolType;
    protected SimulatorType type;
    protected Simulator config;

    protected final List<ExecutionLog> logs = new ArrayList<>();
    protected final AtomicInteger commandsExecuted = new AtomicInteger(0);
    protected final AtomicInteger errors = new AtomicInteger(0);
    protected volatile long startTime;
    protected volatile boolean running = false;

    protected Random random = new Random();

    @Override
    public String getSimulatorId() {
        return simulatorId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProtocolType() {
        return protocolType;
    }

    @Override
    public SimulatorType getType() {
        return type;
    }

    @Override
    public void initialize(Simulator config) {
        this.config = config;
        this.simulatorId = config.getSimulatorId();
        this.name = config.getName();
        this.type = config.getType();
        this.protocolType = config.getProtocolType();

        log("INFO", "Simulator initialized: " + name + " (" + protocolType + ")");
    }

    @Override
    public void start() {
        if (running) {
            log("WARN", "Simulator is already running");
            return;
        }

        running = true;
        startTime = System.currentTimeMillis();

        SimulatorStatus status = getStatus();
        status.setStatus("RUNNING");
        status.setUptime(0);

        log("INFO", "Simulator started: " + name);
    }

    @Override
    public void stop() {
        if (!running) {
            log("WARN", "Simulator is not running");
            return;
        }

        running = false;
        long uptime = System.currentTimeMillis() - startTime;

        SimulatorStatus status = getStatus();
        status.setStatus("STOPPED");
        status.setUptime(uptime);

        log("INFO", "Simulator stopped. Uptime: " + uptime + "ms, Commands: " + commandsExecuted.get());
    }

    @Override
    public SimulatorStatus getStatus() {
        SimulatorStatus status = new SimulatorStatus();
        if (running) {
            status.setStatus("RUNNING");
            status.setUptime(System.currentTimeMillis() - startTime);
        } else {
            status.setStatus("STOPPED");
            status.setUptime(0);
        }
        status.setCommandsExecuted(commandsExecuted.get());
        status.setErrors(errors.get());
        return status;
    }

    @Override
    public List<ExecutionLog> getLogs() {
        return new ArrayList<>(logs);
    }

    @Override
    public void clearLogs() {
        logs.clear();
        log("INFO", "Logs cleared");
    }

    @Override
    public ExecutionResult executeScenario(Scenario scenario) {
        ExecutionResult result = new ExecutionResult(
                generateExecutionId(),
                simulatorId,
                scenario.getScenarioId()
        );

        result.setStartTime(System.currentTimeMillis());
        result.setStatus("RUNNING");

        log("INFO", "Executing scenario: " + scenario.getName());

        int stepNumber = 0;
        for (ScenarioStep step : scenario.getSteps()) {
            if (!running) {
                result.setStatus("CANCELLED");
                break;
            }

            StepResult stepResult = executeStep(step);
            result.getSteps().add(stepResult);

            stepNumber++;

            // 更新汇总
            if (result.getSummary() == null) {
                result.setSummary(new ExecutionSummary());
            }
            result.getSummary().setTotalSteps(stepNumber);
            if (stepResult.isSuccess()) {
                result.getSummary().setSuccessSteps(result.getSummary().getSuccessSteps() + 1);
            } else {
                result.getSummary().setFailedSteps(result.getSummary().getFailedSteps() + 1);
            }

            // 添加延迟
            if (step.getDelay() > 0) {
                try {
                    Thread.sleep(step.getDelay());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        result.setEndTime(System.currentTimeMillis());
        result.setDuration(result.getEndTime() - result.getStartTime());

        if (running) {
            result.setStatus("COMPLETED");
        }

        // 计算平均响应时间
        if (result.getSummary() != null && result.getSummary().getTotalSteps() > 0) {
            double totalDuration = result.getSteps().stream()
                    .mapToLong(sr -> sr.getDuration())
                    .sum();
            result.getSummary().setAvgResponseTime(totalDuration / result.getSummary().getTotalSteps());
        }

        log("INFO", "Scenario completed. Status: " + result.getStatus() +
                ", Steps: " + result.getSummary().getTotalSteps() +
                ", Success: " + result.getSummary().getSuccessSteps() +
                ", Duration: " + result.getDuration() + "ms");

        return result;
    }

    @Override
    public StepResult executeStep(ScenarioStep step) {
        StepResult result = new StepResult();
        result.setStepId(step.getStepId());
        result.setAction(step.getAction());
        result.setTimestamp(System.currentTimeMillis());

        long start = System.currentTimeMillis();

        try {
            // 模拟随机延迟
            Simulator.RandomDelayConfig delayConfig = config != null ?
                    config.getBehavior().getRandomDelay() : null;
            if (delayConfig != null && delayConfig.isEnabled()) {
                int delay = delayConfig.getMinMs() +
                        random.nextInt(delayConfig.getMaxMs() - delayConfig.getMinMs());
                Thread.sleep(delay);
            }

            // 执行命令
            Map<String, Object> response = doExecute(step.getAction(), step);

            Map<String, Object> request = new HashMap<>();
            request.put("action", step.getAction());
            result.setRequest(request);
            result.setResponse(response);
            result.setSuccess(true);

            commandsExecuted.incrementAndGet();

        } catch (Exception e) {
            result.setSuccess(false);
            result.setError(e.getMessage());
            errors.incrementAndGet();

            log("ERROR", "Step failed: " + step.getAction() + " - " + e.getMessage());
        }

        result.setDuration(System.currentTimeMillis() - start);

        return result;
    }

    /**
     * 执行具体命令 (子类实现)
     */
    protected abstract Map<String, Object> doExecute(String action, ScenarioStep step) throws Exception;

    /**
     * 记录日志
     */
    protected void log(String level, String message) {
        ExecutionLog log = new ExecutionLog(level, message);
        logs.add(log);
        System.out.println("[" + level + "] " + message);
    }

    /**
     * 记录命令日志
     */
    protected void logCommand(String commandType, long duration) {
        ExecutionLog log = new ExecutionLog("INFO", "Command executed: " + commandType, commandType, duration);
        logs.add(log);
    }

    /**
     * 生成执行ID
     */
    protected String generateExecutionId() {
        return "EXEC-" + System.currentTimeMillis() + "-" + String.format("%04d", random.nextInt(10000));
    }

    /**
     * 生成模拟节点ID
     */
    protected String generateNodeId() {
        return "SIM-" + protocolType + "-" + String.format("%06d", random.nextInt(1000000));
    }

    /**
     * 检查模拟器是否运行
     */
    protected void checkRunning() {
        if (!running) {
            throw new IllegalStateException("Simulator is not running");
        }
    }
}
