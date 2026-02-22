package net.ooder.nexus.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.ooder.nexus.domain.device.model.DeviceAsset;
import net.ooder.nexus.service.DeviceAssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 设备资产服务实现类
 */
@Service
public class DeviceAssetServiceImpl implements DeviceAssetService {

    private static final Logger log = LoggerFactory.getLogger(DeviceAssetServiceImpl.class);
    private static final String DATA_DIR = "./storage/devices";
    private static final String DEVICES_FILE = "device-assets.json";

    private final Map<String, DeviceAsset> deviceCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Path storagePath;

    public DeviceAssetServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.storagePath = Paths.get(DATA_DIR, DEVICES_FILE);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            loadDevices();
            
            // 如果没有数据，初始化默认数据
            if (deviceCache.isEmpty()) {
                initDefaultData();
            }
            
            log.info("设备资产服务初始化完成，共加载 {} 个设备", deviceCache.size());
        } catch (IOException e) {
            log.error("初始化设备资产服务失败", e);
        }
    }

    @Override
    public void initDefaultData() {
        List<DeviceAsset> defaultDevices = Arrays.asList(
            new DeviceAsset("device-001", "智能灯泡", "light", "192.168.1.101", "AA:BB:CC:DD:EE:01", "online", "客厅智能灯泡"),
            new DeviceAsset("device-002", "智能插座", "socket", "192.168.1.102", "AA:BB:CC:DD:EE:02", "online", "卧室智能插座"),
            new DeviceAsset("device-003", "智能门锁", "lock", "192.168.1.103", "AA:BB:CC:DD:EE:03", "offline", "前门智能门锁"),
            new DeviceAsset("device-004", "温湿度传感器", "sensor", "192.168.1.104", "AA:BB:CC:DD:EE:04", "online", "室内温湿度传感器")
        );
        
        defaultDevices.forEach(device -> deviceCache.put(device.getId(), device));
        saveDevices();
        log.info("初始化默认设备数据完成，共 {} 个设备", defaultDevices.size());
    }

    private void loadDevices() {
        if (!Files.exists(storagePath)) {
            return;
        }

        try {
            String json = new String(Files.readAllBytes(storagePath), StandardCharsets.UTF_8);
            List<DeviceAsset> devices = objectMapper.readValue(json, new TypeReference<List<DeviceAsset>>() {});
            devices.forEach(device -> deviceCache.put(device.getId(), device));
        } catch (IOException e) {
            log.error("加载设备数据失败", e);
        }
    }

    private void saveDevices() {
        try {
            List<DeviceAsset> devices = new ArrayList<>(deviceCache.values());
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(devices);
            Files.write(storagePath, json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("保存设备数据失败", e);
        }
    }

    @Override
    public DeviceAsset createDevice(DeviceAsset device) {
        if (device.getId() == null || device.getId().isEmpty()) {
            device.setId(UUID.randomUUID().toString());
        }

        device.setCreateTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());

        deviceCache.put(device.getId(), device);
        saveDevices();

        log.info("创建设备资产: {}", device.getName());
        return device;
    }

    @Override
    public DeviceAsset updateDevice(String id, DeviceAsset device) {
        DeviceAsset existingDevice = deviceCache.get(id);
        if (existingDevice == null) {
            log.warn("设备不存在: {}", id);
            return null;
        }

        device.setId(id);
        device.setCreateTime(existingDevice.getCreateTime());
        device.setUpdateTime(LocalDateTime.now());

        deviceCache.put(id, device);
        saveDevices();

        log.info("更新设备资产: {}", device.getName());
        return device;
    }

    @Override
    public boolean deleteDevice(String id) {
        DeviceAsset removed = deviceCache.remove(id);
        if (removed != null) {
            saveDevices();
            log.info("删除设备资产: {}", removed.getName());
            return true;
        }
        return false;
    }

    @Override
    public DeviceAsset getDeviceById(String id) {
        return deviceCache.get(id);
    }

    @Override
    public List<DeviceAsset> getAllDevices() {
        return new ArrayList<>(deviceCache.values());
    }

    @Override
    public List<DeviceAsset> getDevicesByType(String type) {
        return deviceCache.values().stream()
                .filter(device -> type.equals(device.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceAsset> getDevicesByStatus(String status) {
        return deviceCache.values().stream()
                .filter(device -> status.equals(device.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public DeviceStats getDeviceStats() {
        DeviceStats stats = new DeviceStats();
        stats.setTotalCount(deviceCache.size());
        stats.setOnlineCount((int) deviceCache.values().stream()
                .filter(d -> "online".equals(d.getStatus())).count());
        stats.setOfflineCount((int) deviceCache.values().stream()
                .filter(d -> "offline".equals(d.getStatus())).count());
        return stats;
    }
}
