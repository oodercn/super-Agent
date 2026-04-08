package net.ooder.spi.workflow;

import java.util.Map;

public interface WorkflowDriver {

    boolean isAvailable();

    <T> WorkflowResult<T> routeTo(String activityInstId, String toUserId, Map<String, Object> vars);

    <T> WorkflowResult<T> endTask(String activityInstId);

    <T> WorkflowResult<T> getActivityInfo(String activityInstId);

    <T> WorkflowResult<T> startProcess(String processDefKey, Map<String, Object> vars);

    interface WorkflowResult<T> {
        boolean isSuccess();
        T getData();
        String getErrorCode();
        String getErrorMessage();
    }
}
