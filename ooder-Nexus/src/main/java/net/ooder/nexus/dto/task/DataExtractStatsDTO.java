package net.ooder.nexus.dto.task;

import java.io.Serializable;

public class DataExtractStatsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer total;
    private Integer running;
    private Integer completed;
    private Integer pending;
    private Integer failed;
    private TodayStatsDTO today;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    public TodayStatsDTO getToday() {
        return today;
    }

    public void setToday(TodayStatsDTO today) {
        this.today = today;
    }

    public static class TodayStatsDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer extracted;
        private Integer failed;
        private Double successRate;

        public Integer getExtracted() {
            return extracted;
        }

        public void setExtracted(Integer extracted) {
            this.extracted = extracted;
        }

        public Integer getFailed() {
            return failed;
        }

        public void setFailed(Integer failed) {
            this.failed = failed;
        }

        public Double getSuccessRate() {
            return successRate;
        }

        public void setSuccessRate(Double successRate) {
            this.successRate = successRate;
        }
    }
}
