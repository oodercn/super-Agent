package net.ooder.agent.metadata.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP透传模型
 * 实现SkillFlow通过MCP透传访问Skill内部数据及通讯的能力
 */
public class McpPassthrough implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 透传目标类型
     */
    @JSONField(name = "target_type")
    private String targetType;

    /**
     * 透传目标ID
     */
    @JSONField(name = "target_id")
    private String targetId;

    /**
     * 透传命令
     */
    @JSONField(name = "passthrough_command")
    private String passthroughCommand;

    /**
     * 透传参数
     */
    @JSONField(name = "passthrough_params")
    private Map<String, Object> passthroughParams;

    /**
     * 透传上下文
     */
    @JSONField(name = "context")
    private Map<String, Object> context;

    /**
     * 透传元数据
     */
    @JSONField(name = "metadata")
    private PassthroughMetadata metadata;

    public McpPassthrough() {
        this.passthroughParams = new HashMap<>();
        this.context = new HashMap<>();
        this.metadata = new PassthroughMetadata();
    }

    public McpPassthrough(String targetType, String targetId, String passthroughCommand) {
        this();
        this.targetType = targetType;
        this.targetId = targetId;
        this.passthroughCommand = passthroughCommand;
    }

    // Getters and Setters
    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getPassthroughCommand() {
        return passthroughCommand;
    }

    public void setPassthroughCommand(String passthroughCommand) {
        this.passthroughCommand = passthroughCommand;
    }

    public Map<String, Object> getPassthroughParams() {
        return passthroughParams;
    }

    public void setPassthroughParams(Map<String, Object> passthroughParams) {
        this.passthroughParams = passthroughParams;
    }

    public void addPassthroughParam(String key, Object value) {
        this.passthroughParams.put(key, value);
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public void addContext(String key, Object value) {
        this.context.put(key, value);
    }

    public PassthroughMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PassthroughMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * 透传元数据类
     */
    public static class PassthroughMetadata implements Serializable {
        private static final long serialVersionUID = 1L;

        @JSONField(name = "skillflow_id")
        private String skillflowId;

        @JSONField(name = "skillflow_node_id")
        private String skillflowNodeId;

        @JSONField(name = "timeout")
        private long timeout;

        @JSONField(name = "ttl")
        private int ttl;

        @JSONField(name = "passthrough_depth")
        private int passthroughDepth;

        @JSONField(name = "trace_id")
        private String traceId;

        @JSONField(name = "parent_trace_id")
        private String parentTraceId;

        public String getSkillflowId() {
            return skillflowId;
        }

        public void setSkillflowId(String skillflowId) {
            this.skillflowId = skillflowId;
        }

        public String getSkillflowNodeId() {
            return skillflowNodeId;
        }

        public void setSkillflowNodeId(String skillflowNodeId) {
            this.skillflowNodeId = skillflowNodeId;
        }

        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }

        public int getPassthroughDepth() {
            return passthroughDepth;
        }

        public void setPassthroughDepth(int passthroughDepth) {
            this.passthroughDepth = passthroughDepth;
        }

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getParentTraceId() {
            return parentTraceId;
        }

        public void setParentTraceId(String parentTraceId) {
            this.parentTraceId = parentTraceId;
        }
    }

    @Override
    public String toString() {
        return "McpPassthrough{" +
                "targetType='" + targetType + '\'' +
                ", targetId='" + targetId + '\'' +
                ", passthroughCommand='" + passthroughCommand + '\'' +
                ", passthroughParams=" + passthroughParams +
                ", metadata=" + metadata +
                '}';
    }
}
