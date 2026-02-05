package net.ooder.sdk.network.packet;

import com.alibaba.fastjson.JSON;

public class HeartbeatPacket extends UDPPacket {
    private String agentId;
    private long sequence;
    private RetryInfo retryInfo;
    private SleepInfo sleepInfo;

    public HeartbeatPacket() {
        super();
    }
    
    @Override
    public String getType() {
        return "heartbeat";
    }

    // Getter and Setter methods
    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public RetryInfo getRetryInfo() {
        return retryInfo;
    }

    public void setRetryInfo(RetryInfo retryInfo) {
        this.retryInfo = retryInfo;
    }

    public SleepInfo getSleepInfo() {
        return sleepInfo;
    }

    public void setSleepInfo(SleepInfo sleepInfo) {
        this.sleepInfo = sleepInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        HeartbeatPacket that = (HeartbeatPacket) o;

        if (sequence != that.sequence) return false;
        if (agentId != null ? !agentId.equals(that.agentId) : that.agentId != null) return false;
        if (retryInfo != null ? !retryInfo.equals(that.retryInfo) : that.retryInfo != null) return false;
        return sleepInfo != null ? sleepInfo.equals(that.sleepInfo) : that.sleepInfo == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (agentId != null ? agentId.hashCode() : 0);
        result = 31 * result + (int) (sequence ^ (sequence >>> 32));
        result = 31 * result + (retryInfo != null ? retryInfo.hashCode() : 0);
        result = 31 * result + (sleepInfo != null ? sleepInfo.hashCode() : 0);
        return result;
    }

    public static HeartbeatPacket fromJson(String json) {
        return JSON.parseObject(json, HeartbeatPacket.class);
    }

    // Builder class
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HeartbeatPacket packet = new HeartbeatPacket();

        public Builder agentId(String agentId) {
            packet.setAgentId(agentId);
            return this;
        }

        public Builder sequence(long sequence) {
            packet.setSequence(sequence);
            return this;
        }

        public Builder retryInfo(RetryInfo retryInfo) {
            packet.setRetryInfo(retryInfo);
            return this;
        }

        public Builder sleepInfo(SleepInfo sleepInfo) {
            packet.setSleepInfo(sleepInfo);
            return this;
        }

        public HeartbeatPacket build() {
            return packet;
        }
    }
}
