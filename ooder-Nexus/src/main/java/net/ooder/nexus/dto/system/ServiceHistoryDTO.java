package net.ooder.nexus.dto.system;

import java.io.Serializable;
import java.util.List;

public class ServiceHistoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String period;
    private List<ServiceHistoryPointDTO> history;
    private ServiceStatisticsDTO statistics;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<ServiceHistoryPointDTO> getHistory() {
        return history;
    }

    public void setHistory(List<ServiceHistoryPointDTO> history) {
        this.history = history;
    }

    public ServiceStatisticsDTO getStatistics() {
        return statistics;
    }

    public void setStatistics(ServiceStatisticsDTO statistics) {
        this.statistics = statistics;
    }

    public static class ServiceHistoryPointDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String time;
        private Integer running;
        private Integer stopped;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Integer getRunning() {
            return running;
        }

        public void setRunning(Integer running) {
            this.running = running;
        }

        public Integer getStopped() {
            return stopped;
        }

        public void setStopped(Integer stopped) {
            this.stopped = stopped;
        }
    }

    public static class ServiceStatisticsDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Double availability;
        private Integer incidents;
        private Long totalDowntime;

        public Double getAvailability() {
            return availability;
        }

        public void setAvailability(Double availability) {
            this.availability = availability;
        }

        public Integer getIncidents() {
            return incidents;
        }

        public void setIncidents(Integer incidents) {
            this.incidents = incidents;
        }

        public Long getTotalDowntime() {
            return totalDowntime;
        }

        public void setTotalDowntime(Long totalDowntime) {
            this.totalDowntime = totalDowntime;
        }
    }
}
