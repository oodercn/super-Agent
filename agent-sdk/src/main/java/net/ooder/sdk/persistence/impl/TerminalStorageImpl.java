package net.ooder.sdk.persistence.impl;

import net.ooder.sdk.persistence.StorageManager;
import net.ooder.sdk.persistence.TerminalStorage;
import net.ooder.sdk.terminal.model.TerminalDevice;
import net.ooder.sdk.terminal.model.TerminalStatus;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TerminalStorageImpl implements TerminalStorage {
    private static final String TERMINAL_PREFIX = "terminal_";
    private final StorageManager storageManager;
    
    public TerminalStorageImpl(StorageManager storageManager) {
        this.storageManager = storageManager;
    }
    
    @Override
    public CompletableFuture<Boolean> saveTerminal(TerminalDevice device) {
        String key = TERMINAL_PREFIX + device.getDeviceId();
        return storageManager.save(key, device);
    }
    
    @Override
    public CompletableFuture<Boolean> saveTerminals(List<TerminalDevice> devices) {
        Map<String, TerminalDevice> entries = new HashMap<>();
        for (TerminalDevice device : devices) {
            String key = TERMINAL_PREFIX + device.getDeviceId();
            entries.put(key, device);
        }
        return storageManager.saveAll(entries);
    }
    
    @Override
    public CompletableFuture<TerminalDevice> loadTerminal(String terminalId) {
        String key = TERMINAL_PREFIX + terminalId;
        return storageManager.load(key, TerminalDevice.class);
    }
    
    @Override
    public CompletableFuture<List<TerminalDevice>> loadAllTerminals() {
        return storageManager.loadAll(TerminalDevice.class)
            .thenApply(map -> new ArrayList<>(map.values()));
    }
    
    @Override
    public CompletableFuture<List<TerminalDevice>> loadTerminalsByType(String deviceType) {
        return loadAllTerminals()
            .thenApply(devices -> devices.stream()
                .filter(device -> deviceType.equals(device.getDeviceType()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<List<TerminalDevice>> loadTerminalsByStatus(String status) {
        return loadAllTerminals()
            .thenApply(devices -> devices.stream()
                .filter(device -> status.equals(device.getStatus().toString()))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<Boolean> deleteTerminal(String terminalId) {
        String key = TERMINAL_PREFIX + terminalId;
        return storageManager.delete(key);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteTerminals(List<String> terminalIds) {
        List<String> keys = terminalIds.stream()
            .map(id -> TERMINAL_PREFIX + id)
            .collect(Collectors.toList());
        return storageManager.deleteAll(keys);
    }
    
    @Override
    public CompletableFuture<Boolean> deleteAllTerminals() {
        return loadAllTerminals()
            .thenCompose(devices -> {
                List<String> keys = devices.stream()
                    .map(device -> TERMINAL_PREFIX + device.getDeviceId())
                    .collect(Collectors.toList());
                return storageManager.deleteAll(keys);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> existsTerminal(String terminalId) {
        String key = TERMINAL_PREFIX + terminalId;
        return storageManager.exists(key);
    }
    
    @Override
    public CompletableFuture<Long> countTerminals() {
        return loadAllTerminals()
            .thenApply(devices -> (long) devices.size());
    }
    
    @Override
    public CompletableFuture<Long> countTerminalsByType(String deviceType) {
        return loadTerminalsByType(deviceType)
            .thenApply(devices -> (long) devices.size());
    }
    
    @Override
    public CompletableFuture<Long> countTerminalsByStatus(String status) {
        return loadTerminalsByStatus(status)
            .thenApply(devices -> (long) devices.size());
    }
    
    @Override
    public CompletableFuture<List<TerminalDevice>> searchTerminals(Map<String, Object> criteria) {
        return loadAllTerminals()
            .thenApply(devices -> devices.stream()
                .filter(device -> matchesCriteria(device, criteria))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<List<TerminalDevice>> findTerminalsByAttribute(String attributeName, Object attributeValue) {
        return loadAllTerminals()
            .thenApply(devices -> devices.stream()
                .filter(device -> hasAttribute(device, attributeName, attributeValue))
                .collect(Collectors.toList()));
    }
    
    @Override
    public CompletableFuture<Map<String, Long>> getTerminalStatusCounts() {
        return loadAllTerminals()
            .thenApply(devices -> {
                Map<String, Long> counts = new HashMap<>();
                for (TerminalDevice device : devices) {
                    String status = device.getStatus().toString();
                    counts.put(status, counts.getOrDefault(status, 0L) + 1);
                }
                return counts;
            });
    }
    
    @Override
    public CompletableFuture<Map<String, Long>> getTerminalTypeCounts() {
        return loadAllTerminals()
            .thenApply(devices -> {
                Map<String, Long> counts = new HashMap<>();
                for (TerminalDevice device : devices) {
                    String type = device.getDeviceType();
                    counts.put(type, counts.getOrDefault(type, 0L) + 1);
                }
                return counts;
            });
    }
    
    @Override
    public CompletableFuture<Map<String, Object>> getTerminalStatistics() {
        return loadAllTerminals()
            .thenApply(devices -> {
                Map<String, Object> stats = new HashMap<>();
                stats.put("totalTerminals", devices.size());
                
                Map<String, Long> statusCounts = new HashMap<>();
                Map<String, Long> typeCounts = new HashMap<>();
                
                for (TerminalDevice device : devices) {
                    String status = device.getStatus().toString();
                    statusCounts.put(status, statusCounts.getOrDefault(status, 0L) + 1);
                    
                    String type = device.getDeviceType();
                    typeCounts.put(type, typeCounts.getOrDefault(type, 0L) + 1);
                }
                
                stats.put("statusCounts", statusCounts);
                stats.put("typeCounts", typeCounts);
                
                return stats;
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateTerminalStatus(String terminalId, String status) {
        return loadTerminal(terminalId)
            .thenCompose(device -> {
                if (device == null) {
                    return CompletableFuture.completedFuture(false);
                }
                try {
                    device.setStatus(TerminalStatus.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return CompletableFuture.completedFuture(false);
                }
                return saveTerminal(device);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateTerminalMetadata(String terminalId, Map<String, Object> metadata) {
        return loadTerminal(terminalId)
            .thenCompose(device -> {
                if (device == null) {
                    return CompletableFuture.completedFuture(false);
                }
                device.updateMetadata(metadata);
                return saveTerminal(device);
            });
    }
    
    @Override
    public CompletableFuture<Boolean> updateTerminalAttributes(String terminalId, Map<String, Object> attributes) {
        return loadTerminal(terminalId)
            .thenCompose(device -> {
                if (device == null) {
                    return CompletableFuture.completedFuture(false);
                }
                
                // 更新属性
                if (attributes.containsKey("deviceName")) {
                    device.setDeviceName((String) attributes.get("deviceName"));
                }
                if (attributes.containsKey("deviceType")) {
                    device.setDeviceType((String) attributes.get("deviceType"));
                }
                if (attributes.containsKey("ipAddress")) {
                    device.setIpAddress((String) attributes.get("ipAddress"));
                }
                if (attributes.containsKey("status")) {
                    try {
                        device.setStatus(TerminalStatus.valueOf(((String) attributes.get("status")).toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        // 忽略无效的状态值
                    }
                }
                if (attributes.containsKey("metadata")) {
                    device.updateMetadata((Map<String, Object>) attributes.get("metadata"));
                }
                
                return saveTerminal(device);
            });
    }
    
    // 辅助方法：检查设备是否匹配查询条件
    private boolean matchesCriteria(TerminalDevice device, Map<String, Object> criteria) {
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            switch (key) {
                case "deviceId":
                    if (!device.getDeviceId().equals(value)) {
                        return false;
                    }
                    break;
                case "deviceName":
                    if (!device.getDeviceName().equals(value)) {
                        return false;
                    }
                    break;
                case "deviceType":
                    if (!device.getDeviceType().equals(value)) {
                        return false;
                    }
                    break;
                case "ipAddress":
                    if (!device.getIpAddress().equals(value)) {
                        return false;
                    }
                    break;
                case "status":
                    if (!device.getStatus().toString().equals(value)) {
                        return false;
                    }
                    break;
                // 可以添加更多属性的匹配逻辑
            }
        }
        return true;
    }
    
    // 辅助方法：检查设备是否有指定的属性值
    private boolean hasAttribute(TerminalDevice device, String attributeName, Object attributeValue) {
        switch (attributeName) {
            case "deviceId":
                return device.getDeviceId().equals(attributeValue);
            case "deviceName":
                return device.getDeviceName().equals(attributeValue);
            case "deviceType":
                return device.getDeviceType().equals(attributeValue);
            case "ipAddress":
                return device.getIpAddress().equals(attributeValue);
            case "status":
                return device.getStatus().toString().equals(attributeValue);
            case "metadata":
                return device.getMetadata().equals(attributeValue);
            default:
                return false;
        }
    }
}
