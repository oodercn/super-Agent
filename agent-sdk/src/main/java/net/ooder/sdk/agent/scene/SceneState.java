package net.ooder.sdk.agent.scene;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 * 场景状态类，用于表示场景的当前状态信息
 */
public class SceneState {
    public enum StateType {
        CREATING,    // 创建中
        READY,       // 就绪
        RUNNING,     // 运行中
        PAUSED,      // 暂停
        COMPLETED,   // 已完成
        FAILED,      // 失败
        TERMINATED   // 已终止
    }

    private String sceneId;
    private StateType currentState;
    private Date stateTimestamp;
    private Map<String, Object> stateProperties;
    private Map<String, MemberState> memberStates;
    private String lastError;

    public SceneState() {
        this.stateProperties = new HashMap<>();
        this.memberStates = new HashMap<>();
        this.stateTimestamp = new Date();
    }

    public SceneState(String sceneId, StateType initialState) {
        this();
        this.sceneId = sceneId;
        this.currentState = initialState;
    }

    // Getters and setters
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }

    public StateType getCurrentState() { return currentState; }
    public void setCurrentState(StateType currentState) {
        this.currentState = currentState;
        this.stateTimestamp = new Date();
    }

    public Date getStateTimestamp() { return stateTimestamp; }
    public void setStateTimestamp(Date stateTimestamp) { this.stateTimestamp = stateTimestamp; }

    public Map<String, Object> getStateProperties() { return stateProperties; }
    public void setStateProperties(Map<String, Object> stateProperties) {
        this.stateProperties = stateProperties != null ? stateProperties : new HashMap<>();
    }

    public Map<String, MemberState> getMemberStates() { return memberStates; }
    public void setMemberStates(Map<String, MemberState> memberStates) {
        this.memberStates = memberStates != null ? memberStates : new HashMap<>();
    }

    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }

    /**
     * 添加或更新成员状态
     */
    public void updateMemberState(String agentId, MemberState memberState) {
        if (agentId != null && memberState != null) {
            this.memberStates.put(agentId, memberState);
        }
    }

    /**
     * 移除成员状态
     */
    public void removeMemberState(String agentId) {
        if (agentId != null) {
            this.memberStates.remove(agentId);
        }
    }

    /**
     * 获取成员状态
     */
    public MemberState getMemberState(String agentId) {
        return agentId != null ? this.memberStates.get(agentId) : null;
    }

    /**
     * 设置状态属性
     */
    public void setStateProperty(String key, Object value) {
        if (key != null) {
            this.stateProperties.put(key, value);
        }
    }

    /**
     * 获取状态属性
     */
    @SuppressWarnings("unchecked")
    public <T> T getStateProperty(String key, T defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        Object value = this.stateProperties.get(key);
        return value != null ? (T) value : defaultValue;
    }

    /**
     * 检查状态是否为运行中
     */
    public boolean isRunning() {
        return currentState == StateType.RUNNING;
    }

    /**
     * 检查状态是否为就绪
     */
    public boolean isReady() {
        return currentState == StateType.READY;
    }

    /**
     * 检查状态是否为暂停
     */
    public boolean isPaused() {
        return currentState == StateType.PAUSED;
    }

    /**
     * 检查状态是否为已完成
     */
    public boolean isCompleted() {
        return currentState == StateType.COMPLETED;
    }

    /**
     * 检查状态是否为失败
     */
    public boolean isFailed() {
        return currentState == StateType.FAILED;
    }

    @Override
    public String toString() {
        return "SceneState{" +
                "sceneId='" + sceneId + '\'' +
                ", currentState=" + currentState +
                ", stateTimestamp=" + stateTimestamp +
                ", memberStatesCount=" + memberStates.size() +
                ", lastError='" + lastError + '\'' +
                '}';
    }

    /**
     * 成员状态类，用于表示场景中成员的状态信息
     */
    public static class MemberState {
        public enum MemberStateType {
            JOINING,    // 加入中
            ACTIVE,     // 活跃
            IDLE,       // 空闲
            BUSY,       // 忙碌
            DISCONNECTED, // 断开连接
            FAILED,     // 失败
            EXITED      // 已退出
        }

        private String agentId;
        private MemberStateType state;
        private Date stateTimestamp;
        private Map<String, Object> properties;
        private String lastError;

        public MemberState() {
            this.properties = new HashMap<>();
            this.stateTimestamp = new Date();
        }

        public MemberState(String agentId, MemberStateType initialState) {
            this();
            this.agentId = agentId;
            this.state = initialState;
        }

        // Getters and setters
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }

        public MemberStateType getState() { return state; }
        public void setState(MemberStateType state) {
            this.state = state;
            this.stateTimestamp = new Date();
        }

        public Date getStateTimestamp() { return stateTimestamp; }
        public void setStateTimestamp(Date stateTimestamp) { this.stateTimestamp = stateTimestamp; }

        public Map<String, Object> getProperties() { return properties; }
        public void setProperties(Map<String, Object> properties) {
            this.properties = properties != null ? properties : new HashMap<>();
        }

        public String getLastError() { return lastError; }
        public void setLastError(String lastError) { this.lastError = lastError; }

        /**
         * 设置成员属性
         */
        public void setProperty(String key, Object value) {
            if (key != null) {
                this.properties.put(key, value);
            }
        }

        /**
         * 获取成员属性
         */
        @SuppressWarnings("unchecked")
        public <T> T getProperty(String key, T defaultValue) {
            if (key == null) {
                return defaultValue;
            }
            Object value = this.properties.get(key);
            return value != null ? (T) value : defaultValue;
        }

        /**
         * 检查成员是否活跃
         */
        public boolean isActive() {
            return state == MemberStateType.ACTIVE;
        }

        /**
         * 检查成员是否断开连接
         */
        public boolean isDisconnected() {
            return state == MemberStateType.DISCONNECTED;
        }

        /**
         * 检查成员是否失败
         */
        public boolean isFailed() {
            return state == MemberStateType.FAILED;
        }

        @Override
        public String toString() {
            return "MemberState{" +
                    "agentId='" + agentId + '\'' +
                    ", state=" + state +
                    ", stateTimestamp=" + stateTimestamp +
                    ", lastError='" + lastError + '\'' +
                    '}';
        }
    }
}
