package net.ooder.nexus.dto.network;

import java.io.Serializable;
import java.util.List;

public class NetworkLinkDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private NetworkLinkDTO link;
    private List<NetworkLinkHistoryDTO> history;
    private Double healthScore;

    public NetworkLinkDTO getLink() {
        return link;
    }

    public void setLink(NetworkLinkDTO link) {
        this.link = link;
    }

    public List<NetworkLinkHistoryDTO> getHistory() {
        return history;
    }

    public void setHistory(List<NetworkLinkHistoryDTO> history) {
        this.history = history;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Double healthScore) {
        this.healthScore = healthScore;
    }

    public static class NetworkLinkHistoryDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long timestamp;
        private Double reliability;
        private Double latency;
        private Double bandwidth;

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Double getReliability() {
            return reliability;
        }

        public void setReliability(Double reliability) {
            this.reliability = reliability;
        }

        public Double getLatency() {
            return latency;
        }

        public void setLatency(Double latency) {
            this.latency = latency;
        }

        public Double getBandwidth() {
            return bandwidth;
        }

        public void setBandwidth(Double bandwidth) {
            this.bandwidth = bandwidth;
        }
    }
}
