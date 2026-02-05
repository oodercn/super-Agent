package net.ooder.sdk.terminal.impl;

import net.ooder.sdk.terminal.TerminalManager;
import net.ooder.sdk.terminal.model.TerminalDevice;
import net.ooder.sdk.terminal.model.TerminalEvent;
import net.ooder.sdk.terminal.model.TerminalStatus;
import net.ooder.sdk.terminal.discovery.TerminalDiscoverer;
import net.ooder.sdk.terminal.discovery.impl.LocalNetworkDiscoverer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TerminalManagerImpl implements TerminalManager {
    private final Map<String, TerminalDevice> devices = new ConcurrentHashMap<>();
    private final List<Consumer<TerminalEvent>> eventHandlers = new CopyOnWriteArrayList<>();
    private final List<TerminalEvent> recentEvents = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    private final TerminalDiscoverer discoverer;
    private boolean discoveryRunning = false;
    private boolean monitoringRunning = false;
    
    public TerminalManagerImpl() {
        this.discoverer = new LocalNetworkDiscoverer(this);
        startMonitoring();
    }
    
    @Override
    public void startDiscovery() {
        if (!discoveryRunning) {
            discoverer.startDiscovery();
            discoveryRunning = true;
        }
    }
    
    @Override
    public void stopDiscovery() {
        if (discoveryRunning) {
            discoverer.stopDiscovery();
            discoveryRunning = false;
        }
    }
    
    @Override
    public boolean isDiscoveryRunning() {
        return discoveryRunning;
    }
    
    @Override
    public TerminalDevice registerDevice(TerminalDevice device) {
        device.setRegistered(true);
        device.setStatus(TerminalStatus.ONLINE);
        devices.put(device.getDeviceId(), device);
        
        // 发布设备注册事件
        TerminalEvent event = new TerminalEvent(
            net.ooder.sdk.terminal.model.TerminalEventType.DEVICE_REGISTERED,
            device.getDeviceId(),
            Collections.singletonMap("device", device)
        );
        publishEvent(event);
        
        return device;
    }
    
    @Override
    public void deregisterDevice(String deviceId) {
        TerminalDevice device = devices.remove(deviceId);
        if (device != null) {
            // 发布设备注销事件
            TerminalEvent event = new TerminalEvent(
                net.ooder.sdk.terminal.model.TerminalEventType.DEVICE_DEREGISTERED,
                deviceId,
                Collections.singletonMap("device", device)
            );
            publishEvent(event);
        }
    }
    
    @Override
    public TerminalDevice getDevice(String deviceId) {
        return devices.get(deviceId);
    }
    
    @Override
    public List<TerminalDevice> getAllDevices() {
        return new ArrayList<>(devices.values());
    }
    
    @Override
    public List<TerminalDevice> getDevicesByStatus(TerminalStatus status) {
        return devices.values().stream()
            .filter(device -> device.getStatus() == status)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<TerminalDevice> getDevicesByType(String deviceType) {
        return devices.values().stream()
            .filter(device -> device.getDeviceType().equals(deviceType))
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public void updateDeviceStatus(String deviceId, TerminalStatus status) {
        TerminalDevice device = devices.get(deviceId);
        if (device != null) {
            TerminalStatus oldStatus = device.getStatus();
            device.setStatus(status);
            device.setLastSeen(System.currentTimeMillis());
            
            // 发布状态变更事件
            if (oldStatus != status) {
                TerminalEvent event = new TerminalEvent(
                    net.ooder.sdk.terminal.model.TerminalEventType.DEVICE_STATUS_CHANGED,
                    deviceId,
                    new HashMap<String, Object>() {{
                        put("oldStatus", oldStatus);
                        put("newStatus", status);
                    }}
                );
                publishEvent(event);
            }
        }
    }
    
    @Override
    public void updateDeviceMetadata(String deviceId, Map<String, Object> metadata) {
        TerminalDevice device = devices.get(deviceId);
        if (device != null) {
            device.updateMetadata(metadata);
            
            // 发布元数据更新事件
            TerminalEvent event = new TerminalEvent(
                net.ooder.sdk.terminal.model.TerminalEventType.DEVICE_METADATA_UPDATED,
                deviceId,
                Collections.singletonMap("metadata", metadata)
            );
            publishEvent(event);
        }
    }
    
    @Override
    public void syncDeviceStatus(String deviceId) {
        // 实现设备状态同步逻辑
        TerminalDevice device = devices.get(deviceId);
        if (device != null) {
            // 这里可以添加与设备通信获取最新状态的逻辑
            device.setLastSeen(System.currentTimeMillis());
        }
    }
    
    @Override
    public void publishEvent(TerminalEvent event) {
        recentEvents.add(event);
        // 只保留最近100个事件
        if (recentEvents.size() > 100) {
            recentEvents.remove(0);
        }
        
        // 通知所有事件处理器
        for (Consumer<TerminalEvent> handler : eventHandlers) {
            try {
                handler.accept(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void subscribeToEvents(Consumer<TerminalEvent> eventHandler) {
        eventHandlers.add(eventHandler);
    }
    
    @Override
    public void unsubscribeFromEvents(Consumer<TerminalEvent> eventHandler) {
        eventHandlers.remove(eventHandler);
    }
    
    @Override
    public List<TerminalEvent> getRecentEvents(int limit) {
        int size = Math.min(limit, recentEvents.size());
        return recentEvents.subList(recentEvents.size() - size, recentEvents.size());
    }
    
    @Override
    public void startMonitoring() {
        if (!monitoringRunning) {
            // 定期检查设备状态
            executorService.scheduleAtFixedRate(this::checkDeviceStatus, 0, 30, TimeUnit.SECONDS);
            monitoringRunning = true;
        }
    }
    
    @Override
    public void stopMonitoring() {
        if (monitoringRunning) {
            executorService.shutdown();
            monitoringRunning = false;
        }
    }
    
    @Override
    public Map<String, Object> getDeviceStats(String deviceId) {
        TerminalDevice device = devices.get(deviceId);
        if (device == null) {
            return Collections.emptyMap();
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("deviceId", device.getDeviceId());
        stats.put("deviceName", device.getDeviceName());
        stats.put("status", device.getStatus());
        stats.put("lastSeen", device.getLastSeen());
        stats.put("uptime", System.currentTimeMillis() - device.getLastSeen());
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getOverallStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDevices", devices.size());
        stats.put("onlineDevices", getDevicesByStatus(TerminalStatus.ONLINE).size());
        stats.put("offlineDevices", getDevicesByStatus(TerminalStatus.OFFLINE).size());
        stats.put("discoveryRunning", discoveryRunning);
        stats.put("monitoringRunning", monitoringRunning);
        
        return stats;
    }
    
    // 内部方法：检查设备状态
    private void checkDeviceStatus() {
        long currentTime = System.currentTimeMillis();
        long timeoutThreshold = 2 * 60 * 1000; // 2分钟超时
        
        for (Map.Entry<String, TerminalDevice> entry : devices.entrySet()) {
            TerminalDevice device = entry.getValue();
            if (device.getStatus() == TerminalStatus.ONLINE && 
                currentTime - device.getLastSeen() > timeoutThreshold) {
                updateDeviceStatus(device.getDeviceId(), TerminalStatus.OFFLINE);
            }
        }
    }
    
    // 内部方法：添加发现的设备
    public void addDiscoveredDevice(TerminalDevice device) {
        if (!devices.containsKey(device.getDeviceId())) {
            devices.put(device.getDeviceId(), device);
            
            // 发布设备发现事件
            TerminalEvent event = new TerminalEvent(
                net.ooder.sdk.terminal.model.TerminalEventType.DEVICE_DISCOVERED,
                device.getDeviceId(),
                Collections.singletonMap("device", device)
            );
            publishEvent(event);
        }
    }
}
