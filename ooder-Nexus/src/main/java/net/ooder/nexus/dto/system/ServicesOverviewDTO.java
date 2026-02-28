package net.ooder.nexus.dto.system;

import java.io.Serializable;
import java.util.List;

public class ServicesOverviewDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ServiceStatusDTO> services;
    private Integer total;
    private Integer running;
    private Integer stopped;

    public List<ServiceStatusDTO> getServices() {
        return services;
    }

    public void setServices(List<ServiceStatusDTO> services) {
        this.services = services;
    }

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

    public Integer getStopped() {
        return stopped;
    }

    public void setStopped(Integer stopped) {
        this.stopped = stopped;
    }

    public static class ServiceStatusDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id;
        private String name;
        private String status;
        private String uptime;
        private ServiceMetricsDTO metrics;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }

        public ServiceMetricsDTO getMetrics() {
            return metrics;
        }

        public void setMetrics(ServiceMetricsDTO metrics) {
            this.metrics = metrics;
        }
    }

    public static class ServiceMetricsDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Double cpu;
        private Double memory;
        private Long requests;

        public Double getCpu() {
            return cpu;
        }

        public void setCpu(Double cpu) {
            this.cpu = cpu;
        }

        public Double getMemory() {
            return memory;
        }

        public void setMemory(Double memory) {
            this.memory = memory;
        }

        public Long getRequests() {
            return requests;
        }

        public void setRequests(Long requests) {
            this.requests = requests;
        }
    }
}
