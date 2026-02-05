package net.ooder.skillcenter.personalai;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备管理器，管理个人AI中心的设备
 */
public class DeviceManager {
    // 设备映射，key为设备ID，value为设备信息
    private Map<String, DeviceInfo> devices;
    
    /**
     * 构造方法
     */
    public DeviceManager() {
        this.devices = new ConcurrentHashMap<>();
    }
    
    /**
     * 启动设备管理器
     */
    public void start() {
        System.out.println("Device Manager started");
        // 启动设备发现和状态监控
        discoverDevices();
    }
    
    /**
     * 停止设备管理器
     */
    public void stop() {
        System.out.println("Device Manager stopped");
    }
    
    /**
     * 发现设备
     */
    private void discoverDevices() {
        // 发现局域网内的设备
        System.out.println("Discovering devices...");
        // 这里简单实现，不执行实际的设备发现
    }
    
    /**
     * 添加设备
     * @param device 设备信息
     */
    public void addDevice(DeviceInfo device) {
        devices.put(device.getDeviceId(), device);
        System.out.println("Added device: " + device.getName() + " (" + device.getDeviceId() + ")");
    }
    
    /**
     * 移除设备
     * @param deviceId 设备ID
     */
    public void removeDevice(String deviceId) {
        DeviceInfo device = devices.remove(deviceId);
        if (device != null) {
            System.out.println("Removed device: " + device.getName() + " (" + deviceId + ")");
        }
    }
    
    /**
     * 获取设备
     * @param deviceId 设备ID
     * @return 设备信息
     */
    public DeviceInfo getDevice(String deviceId) {
        return devices.get(deviceId);
    }
    
    /**
     * 获取所有设备
     * @return 设备列表
     */
    public List<DeviceInfo> getAllDevices() {
        return new ArrayList<>(devices.values());
    }
    
    /**
     * 获取在线设备
     * @return 在线设备列表
     */
    public List<DeviceInfo> getOnlineDevices() {
        List<DeviceInfo> onlineDevices = new ArrayList<>();
        for (DeviceInfo device : devices.values()) {
            if (device.isOnline()) {
                onlineDevices.add(device);
            }
        }
        return onlineDevices;
    }
    
    /**
     * 获取设备数量
     * @return 设备数量
     */
    public int getDeviceCount() {
        return devices.size();
    }
    
    /**
     * 获取在线设备数量
     * @return 在线设备数量
     */
    public int getOnlineDeviceCount() {
        int count = 0;
        for (DeviceInfo device : devices.values()) {
            if (device.isOnline()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 更新设备状态
     * @param deviceId 设备ID
     * @param online 是否在线
     */
    public void updateDeviceStatus(String deviceId, boolean online) {
        DeviceInfo device = devices.get(deviceId);
        if (device != null) {
            device.setOnline(online);
            System.out.println("Updated device status: " + device.getName() + " -> " + (online ? "online" : "offline"));
        }
    }
    
    /**
     * 设备信息类
     */
    public static class DeviceInfo {
        private String deviceId;
        private String name;
        private String type;
        private String ipAddress;
        private int port;
        private boolean online;
        private String status;
        private Map<String, Object> capabilities;
        private long lastSeen;
        private long addedAt;
        
        public DeviceInfo() {
            this.deviceId = "device-" + UUID.randomUUID().toString();
            this.addedAt = System.currentTimeMillis();
            this.lastSeen = System.currentTimeMillis();
            this.capabilities = new HashMap<>();
        }
        
        public DeviceInfo(String deviceId, String name, String type) {
            this();
            this.deviceId = deviceId;
            this.name = name;
            this.type = type;
        }
        
        // Getters and setters
        public String getDeviceId() { return deviceId; }
        public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public boolean isOnline() { return online; }
        public void setOnline(boolean online) { 
            this.online = online;
            this.status = online ? "online" : "offline";
            if (online) {
                this.lastSeen = System.currentTimeMillis();
            }
        }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Map<String, Object> getCapabilities() { return capabilities; }
        public void setCapabilities(Map<String, Object> capabilities) { this.capabilities = capabilities; }
        public void addCapability(String key, Object value) { this.capabilities.put(key, value); }
        public long getLastSeen() { return lastSeen; }
        public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
        public long getAddedAt() { return addedAt; }
        public void setAddedAt(long addedAt) { this.addedAt = addedAt; }
    }
}