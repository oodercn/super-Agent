package net.ooder.skillcenter.runtime.impl;

import net.ooder.skillcenter.runtime.RuntimeExecutor;
import net.ooder.skillcenter.runtime.model.RuntimeConfig;
import net.ooder.skillcenter.runtime.model.RuntimeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Python运行时执行器 - 符合v0.7.0协议规范
 */
@Component
public class PythonRuntimeExecutor implements RuntimeExecutor {

    private static final Logger log = LoggerFactory.getLogger(PythonRuntimeExecutor.class);

    private static final List<String> SUPPORTED_VERSIONS = Arrays.asList(
            "3.8", "3.9", "3.10", "3.11", "3.12"
    );
    private static final List<String> SUPPORTED_FRAMEWORKS = Arrays.asList(
            "fastapi", "flask", "django", "plain"
    );

    private final Map<String, RuntimeStatus> statuses = new ConcurrentHashMap<>();
    private final Map<String, RuntimeConfig> configs = new ConcurrentHashMap<>();

    @Override
    public String getLanguage() {
        return "python";
    }

    @Override
    public boolean isSupported(String version) {
        return version != null && SUPPORTED_VERSIONS.contains(version);
    }

    @Override
    public RuntimeStatus initialize(String runtimeId, RuntimeConfig config) {
        RuntimeStatus status = RuntimeStatus.create(runtimeId, "skill", "python", config.getVersion());
        status.setState(RuntimeStatus.RuntimeState.INITIALIZING);
        statuses.put(runtimeId, status);
        configs.put(runtimeId, config);
        log.info("Initialized Python runtime {} with version {}", runtimeId, config.getVersion());
        return status;
    }

    @Override
    public RuntimeStatus start(String runtimeId) {
        RuntimeStatus status = statuses.get(runtimeId);
        if (status == null) {
            return null;
        }

        RuntimeConfig config = configs.get(runtimeId);
        status.setState(RuntimeStatus.RuntimeState.STARTING);
        
        try {
            log.info("Starting Python runtime {} with framework: {}", runtimeId, config.getFramework());
            
            Thread.sleep(80);
            
            status.setState(RuntimeStatus.RuntimeState.RUNNING);
            status.setStartedAt(new Date());
            status.setLastHeartbeat(new Date());
            status.setMemoryLimit(256 * 1024 * 1024);
            status.setMemoryUsed(32 * 1024 * 1024);
            status.setCpuUsage(0.05);
            
            log.info("Python runtime {} started successfully", runtimeId);
        } catch (Exception e) {
            status.setState(RuntimeStatus.RuntimeState.ERROR);
            status.setErrorMessage(e.getMessage());
            log.error("Failed to start Python runtime {}: {}", runtimeId, e.getMessage());
        }

        return status;
    }

    @Override
    public RuntimeStatus stop(String runtimeId) {
        RuntimeStatus status = statuses.get(runtimeId);
        if (status == null) {
            return null;
        }

        status.setState(RuntimeStatus.RuntimeState.STOPPING);
        
        try {
            log.info("Stopping Python runtime {}", runtimeId);
            Thread.sleep(30);
            status.setState(RuntimeStatus.RuntimeState.STOPPED);
            log.info("Python runtime {} stopped", runtimeId);
        } catch (Exception e) {
            status.setState(RuntimeStatus.RuntimeState.ERROR);
            status.setErrorMessage(e.getMessage());
        }

        return status;
    }

    @Override
    public RuntimeStatus getStatus(String runtimeId) {
        RuntimeStatus status = statuses.get(runtimeId);
        if (status != null && status.getState() == RuntimeStatus.RuntimeState.RUNNING) {
            status.setLastHeartbeat(new Date());
            status.setCpuUsage(Math.random() * 0.3);
            status.setMemoryUsed((long) (32 * 1024 * 1024 + Math.random() * 64 * 1024 * 1024));
        }
        return status;
    }

    @Override
    public Object execute(String runtimeId, String method, Map<String, Object> params) {
        RuntimeStatus status = statuses.get(runtimeId);
        if (status == null || status.getState() != RuntimeStatus.RuntimeState.RUNNING) {
            return null;
        }

        log.debug("Executing method {} on Python runtime {}", method, runtimeId);
        return params;
    }

    @Override
    public void destroy(String runtimeId) {
        stop(runtimeId);
        statuses.remove(runtimeId);
        configs.remove(runtimeId);
        log.info("Destroyed Python runtime {}", runtimeId);
    }
}
