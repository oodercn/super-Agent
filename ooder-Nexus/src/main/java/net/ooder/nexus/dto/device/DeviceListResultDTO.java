package net.ooder.nexus.dto.device;

import net.ooder.nexus.domain.end.model.Device;

import java.io.Serializable;
import java.util.List;

public class DeviceListResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Device> devices;
    private DeviceStatsDTO stats;

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public DeviceStatsDTO getStats() {
        return stats;
    }

    public void setStats(DeviceStatsDTO stats) {
        this.stats = stats;
    }

    public static class DeviceStatsDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer total;
        private Long online;
        private Long offline;
        private Long types;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Long getOnline() {
            return online;
        }

        public void setOnline(Long online) {
            this.online = online;
        }

        public Long getOffline() {
            return offline;
        }

        public void setOffline(Long offline) {
            this.offline = offline;
        }

        public Long getTypes() {
            return types;
        }

        public void setTypes(Long types) {
            this.types = types;
        }
    }
}
