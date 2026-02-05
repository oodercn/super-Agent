package net.ooder.sdk.terminal;

import net.ooder.sdk.terminal.model.TerminalDevice;
import net.ooder.sdk.terminal.model.TerminalEvent;
import net.ooder.sdk.terminal.model.TerminalStatus;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface TerminalManager {
    // 终端发现相关方法
    void startDiscovery();
    void stopDiscovery();
    boolean isDiscoveryRunning();
    
    // 终端管理相关方法
    TerminalDevice registerDevice(TerminalDevice device);
    void deregisterDevice(String deviceId);
    TerminalDevice getDevice(String deviceId);
    List<TerminalDevice> getAllDevices();
    List<TerminalDevice> getDevicesByStatus(TerminalStatus status);
    List<TerminalDevice> getDevicesByType(String deviceType);
    
    // 终端状态管理相关方法
    void updateDeviceStatus(String deviceId, TerminalStatus status);
    void updateDeviceMetadata(String deviceId, Map<String, Object> metadata);
    void syncDeviceStatus(String deviceId);
    
    // 终端事件相关方法
    void publishEvent(TerminalEvent event);
    void subscribeToEvents(Consumer<TerminalEvent> eventHandler);
    void unsubscribeFromEvents(Consumer<TerminalEvent> eventHandler);
    List<TerminalEvent> getRecentEvents(int limit);
    
    // 终端监控相关方法
    void startMonitoring();
    void stopMonitoring();
    Map<String, Object> getDeviceStats(String deviceId);
    Map<String, Object> getOverallStats();
}
