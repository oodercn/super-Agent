package net.ooder.nexus.skillcenter.dto.domain;

import java.util.List;
import java.util.Map;

public class ManagerActionDTO {
    private String actionId;
    private String domainId;
    private String managerId;
    private ActionType actionType;
    private String targetId;
    private String targetType;
    private Map<String, Object> parameters;
    private ActionResultDTO result;
    private ActionStatus status;
    private long createdAt;
    private long completedAt;

    public String getActionId() { return actionId; }
    public void setActionId(String actionId) { this.actionId = actionId; }
    public String getDomainId() { return domainId; }
    public void setDomainId(String domainId) { this.domainId = domainId; }
    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }
    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public ActionResultDTO getResult() { return result; }
    public void setResult(ActionResultDTO result) { this.result = result; }
    public ActionStatus getStatus() { return status; }
    public void setStatus(ActionStatus status) { this.status = status; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getCompletedAt() { return completedAt; }
    public void setCompletedAt(long completedAt) { this.completedAt = completedAt; }

    public enum ActionType {
        CREATE_DOMAIN,
        UPDATE_DOMAIN,
        DELETE_DOMAIN,
        ADD_MEMBER,
        REMOVE_MEMBER,
        UPDATE_POLICY,
        DEPLOY_SKILL,
        UNDEPLOY_SKILL,
        START_EXECUTION,
        STOP_EXECUTION,
        CONFIGURE_TOPOLOGY,
        ENABLE_OBSERVATION,
        DISABLE_OBSERVATION
    }

    public enum ActionStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public static class ActionResultDTO {
        private boolean success;
        private String message;
        private int errorCode;
        private Map<String, Object> data;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public int getErrorCode() { return errorCode; }
        public void setErrorCode(int errorCode) { this.errorCode = errorCode; }
        public Map<String, Object> getData() { return data; }
        public void setData(Map<String, Object> data) { this.data = data; }
    }
}
