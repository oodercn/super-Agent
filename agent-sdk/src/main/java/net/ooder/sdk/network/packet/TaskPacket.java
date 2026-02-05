package net.ooder.sdk.network.packet;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class TaskPacket extends UDPPacket {
    private String taskId;
    private String taskType;
    private Map<String, Object> params;
    private String status;
    private Map<String, Object> result;
    private String errorMessage;
    private String skillflowId;
    private String endAgentId;

    public TaskPacket() {
        super();
    }
    
    public static TaskPacket fromJson(String json) {
        return JSON.parseObject(json, TaskPacket.class);
    }
    
    @Override
    public String getType() {
        return "task";
    }
    
    // Getter and Setter methods
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSkillflowId() {
        return skillflowId;
    }

    public void setSkillflowId(String skillflowId) {
        this.skillflowId = skillflowId;
    }

    public String getEndAgentId() {
        return endAgentId;
    }

    public void setEndAgentId(String endAgentId) {
        this.endAgentId = endAgentId;
    }

    // Builder class
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TaskPacket packet = new TaskPacket();

        public Builder taskId(String taskId) {
            packet.setTaskId(taskId);
            return this;
        }

        public Builder taskType(String taskType) {
            packet.setTaskType(taskType);
            return this;
        }

        public Builder params(Map<String, Object> params) {
            packet.setParams(params);
            return this;
        }

        public Builder status(String status) {
            packet.setStatus(status);
            return this;
        }

        public Builder result(Map<String, Object> result) {
            packet.setResult(result);
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            packet.setErrorMessage(errorMessage);
            return this;
        }

        public Builder skillflowId(String skillflowId) {
            packet.setSkillflowId(skillflowId);
            return this;
        }

        public Builder endAgentId(String endAgentId) {
            packet.setEndAgentId(endAgentId);
            return this;
        }

        public Builder senderId(String senderId) {
            packet.setSenderId(senderId);
            return this;
        }

        public Builder receiverId(String receiverId) {
            packet.setReceiverId(receiverId);
            return this;
        }

        public TaskPacket build() {
            return packet;
        }
    }
}