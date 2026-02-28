package net.ooder.nexus.dto.dashboard;

import java.io.Serializable;
import java.util.List;

public class HealthOverviewDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private Integer score;
    private List<ComponentHealthDTO> components;
    private List<AlertDTO> alerts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<ComponentHealthDTO> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentHealthDTO> components) {
        this.components = components;
    }

    public List<AlertDTO> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<AlertDTO> alerts) {
        this.alerts = alerts;
    }

    public static class ComponentHealthDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private String status;
        private Integer score;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }
    }

    public static class AlertDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String level;
        private String message;
        private String date;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
