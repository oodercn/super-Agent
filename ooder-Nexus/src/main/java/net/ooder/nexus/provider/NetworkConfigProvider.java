package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;
import net.ooder.scene.provider.BaseProvider;

import java.util.List;
import java.util.Map;

/**
 * 网络配置Provider接口
 *
 * <p>定义网络配置管理相关的操作接口</p>
 * <p>注：此接口在 scene-engine 0.7.3 中不存在，由 ooderNexus 自行定义</p>
 * <p>与 NetworkProvider 不同，此接口专注于网络配置管理（DNS/DHCP/WiFi等）</p>
 */
public interface NetworkConfigProvider extends BaseProvider {

    /**
     * 获取网络设置
     */
    Result<NetworkSetting> getNetworkSetting(String settingType);

    /**
     * 获取所有网络设置
     */
    Result<List<NetworkSetting>> getAllNetworkSettings();

    /**
     * 更新网络设置
     */
    Result<NetworkSetting> updateNetworkSetting(String settingType, Map<String, Object> settingData);

    /**
     * 获取IP地址列表
     */
    Result<PageResult<IPAddress>> listIPAddresses(String type, String status);

    /**
     * 添加静态IP地址
     */
    Result<IPAddress> addStaticIPAddress(Map<String, Object> ipData);

    /**
     * 删除IP地址
     */
    Result<IPAddress> deleteIPAddress(String ipId);

    /**
     * 获取IP黑名单
     */
    Result<List<IPBlacklist>> getIPBlacklist();

    /**
     * 添加IP到黑名单
     */
    Result<IPBlacklist> addIPToBlacklist(Map<String, Object> blacklistData);

    /**
     * 从黑名单移除IP
     */
    Result<IPBlacklist> removeIPFromBlacklist(String blacklistId);

    /**
     * 获取网络设备列表
     */
    Result<PageResult<NetworkDevice>> listNetworkDevices(String type, String status);

    /**
     * 网络设置
     */
    class NetworkSetting {
        private String settingType;
        private String name;
        private String profile;
        private String status;
        private String gateway;
        private Map<String, Object> config;
        private long updatedAt;

        public NetworkSetting() {}

        public NetworkSetting(String settingType, String name, String profile, 
                              String status, String gateway) {
            this.settingType = settingType;
            this.name = name;
            this.profile = profile;
            this.status = status;
            this.gateway = gateway;
        }

        public String getSettingType() { return settingType; }
        public void setSettingType(String settingType) { this.settingType = settingType; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getProfile() { return profile; }
        public void setProfile(String profile) { this.profile = profile; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getGateway() { return gateway; }
        public void setGateway(String gateway) { this.gateway = gateway; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public long getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
    }

    /**
     * IP地址
     */
    class IPAddress {
        private String ipId;
        private String ipAddress;
        private String type;
        private String status;
        private String deviceName;
        private String macAddress;
        private String deviceType;
        private String leaseTime;
        private long createdAt;

        public IPAddress() {}

        public IPAddress(String ipId, String ipAddress, String type, String status,
                         String deviceName, String macAddress, String deviceType, String leaseTime) {
            this.ipId = ipId;
            this.ipAddress = ipAddress;
            this.type = type;
            this.status = status;
            this.deviceName = deviceName;
            this.macAddress = macAddress;
            this.deviceType = deviceType;
            this.leaseTime = leaseTime;
        }

        public String getIpId() { return ipId; }
        public void setIpId(String ipId) { this.ipId = ipId; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getDeviceName() { return deviceName; }
        public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
        public String getMacAddress() { return macAddress; }
        public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
        public String getDeviceType() { return deviceType; }
        public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
        public String getLeaseTime() { return leaseTime; }
        public void setLeaseTime(String leaseTime) { this.leaseTime = leaseTime; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    }

    /**
     * IP黑名单
     */
    class IPBlacklist {
        private String blacklistId;
        private String ipAddress;
        private String reason;
        private String source;
        private long createdAt;

        public IPBlacklist() {}

        public IPBlacklist(String blacklistId, String ipAddress, String reason, String source) {
            this.blacklistId = blacklistId;
            this.ipAddress = ipAddress;
            this.reason = reason;
            this.source = source;
        }

        public String getBlacklistId() { return blacklistId; }
        public void setBlacklistId(String blacklistId) { this.blacklistId = blacklistId; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    }

    /**
     * 网络设备
     */
    class NetworkDevice {
        private String deviceId;
        private String name;
        private String type;
        private String status;
        private String ip;
        private String mac;
        private long lastSeen;

        public String getDeviceId() { return deviceId; }
        public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getIp() { return ip; }
        public void setIp(String ip) { this.ip = ip; }
        public String getMac() { return mac; }
        public void setMac(String mac) { this.mac = mac; }
        public long getLastSeen() { return lastSeen; }
        public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    }
}
