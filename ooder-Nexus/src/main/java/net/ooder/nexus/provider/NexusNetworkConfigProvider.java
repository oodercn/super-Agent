package net.ooder.nexus.provider;

import net.ooder.scene.core.Result;
import net.ooder.scene.core.PageResult;
import net.ooder.scene.core.SceneEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * NetworkConfigProvider 实现
 *
 * <p>基于 NetworkConfigService 实现 NetworkConfigProvider 接口</p>
 */
@Component
public class NexusNetworkConfigProvider implements NetworkConfigProvider {

    private static final Logger log = LoggerFactory.getLogger(NexusNetworkConfigProvider.class);
    private static final String PROVIDER_NAME = "NexusNetworkConfigProvider";
    private static final String VERSION = "1.0.0";

    private SceneEngine sceneEngine;
    private boolean initialized = false;
    private boolean running = false;

    private final Map<String, NetworkSetting> settings = new ConcurrentHashMap<String, NetworkSetting>();
    private final Map<String, IPAddress> ipAddresses = new ConcurrentHashMap<String, IPAddress>();
    private final Map<String, IPBlacklist> ipBlacklist = new ConcurrentHashMap<String, IPBlacklist>();
    private final Map<String, NetworkDevice> networkDevices = new ConcurrentHashMap<String, NetworkDevice>();
    
    private final AtomicLong ipIdCounter = new AtomicLong(0);
    private final AtomicLong blacklistIdCounter = new AtomicLong(0);

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public void initialize(SceneEngine engine) {
        this.sceneEngine = engine;
        this.initialized = true;
        
        initDefaultData();
        
        log.info("NexusNetworkConfigProvider initialized");
    }

    @Override
    public void start() {
        this.running = true;
        log.info("NexusNetworkConfigProvider started");
    }

    @Override
    public void stop() {
        this.running = false;
        log.info("NexusNetworkConfigProvider stopped");
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void initDefaultData() {
        NetworkSetting basic = new NetworkSetting("basic", "Basic Settings", "default", "enabled", "192.168.1.1");
        basic.setConfig(new HashMap<String, Object>());
        basic.getConfig().put("dns1", "8.8.8.8");
        basic.getConfig().put("dns2", "8.8.4.4");
        settings.put("basic", basic);
        
        NetworkSetting dhcp = new NetworkSetting("dhcp", "DHCP Settings", "default", "enabled", "192.168.1.1");
        dhcp.setConfig(new HashMap<String, Object>());
        dhcp.getConfig().put("startIp", "192.168.1.100");
        dhcp.getConfig().put("endIp", "192.168.1.200");
        dhcp.getConfig().put("leaseTime", "24h");
        settings.put("dhcp", dhcp);
        
        IPAddress ip1 = new IPAddress("ip-1", "192.168.1.100", "static", "online", 
            "Real Device", "AA:BB:CC:DD:EE:FF", "client", "永久");
        ipAddresses.put(ip1.getIpId(), ip1);
        
        IPAddress ip2 = new IPAddress("ip-2", "192.168.1.101", "dynamic", "online", 
            "Mobile Device", "AA:BB:CC:DD:EE:GG", "client", "24小时");
        ipAddresses.put(ip2.getIpId(), ip2);
        
        NetworkDevice device1 = new NetworkDevice();
        device1.setDeviceId("device-1");
        device1.setName("Router");
        device1.setType("router");
        device1.setStatus("online");
        device1.setIp("192.168.1.1");
        device1.setMac("00:11:22:33:44:55");
        device1.setLastSeen(System.currentTimeMillis());
        networkDevices.put(device1.getDeviceId(), device1);
    }

    @Override
    public Result<NetworkSetting> getNetworkSetting(String settingType) {
        log.debug("Getting network setting: {}", settingType);
        
        NetworkSetting setting = settings.get(settingType);
        if (setting == null) {
            setting = new NetworkSetting(settingType, settingType + " Settings", "default", "enabled", "192.168.1.1");
            settings.put(settingType, setting);
        }
        
        return Result.success(setting);
    }

    @Override
    public Result<List<NetworkSetting>> getAllNetworkSettings() {
        log.debug("Getting all network settings");
        
        return Result.success(new ArrayList<NetworkSetting>(settings.values()));
    }

    @Override
    public Result<NetworkSetting> updateNetworkSetting(String settingType, Map<String, Object> settingData) {
        log.info("Updating network setting: {}, data: {}", settingType, settingData);
        
        NetworkSetting setting = settings.get(settingType);
        if (setting == null) {
            setting = new NetworkSetting();
            setting.setSettingType(settingType);
        }
        
        if (settingData.containsKey("name")) {
            setting.setName((String) settingData.get("name"));
        }
        if (settingData.containsKey("profile")) {
            setting.setProfile((String) settingData.get("profile"));
        }
        if (settingData.containsKey("status")) {
            setting.setStatus((String) settingData.get("status"));
        }
        if (settingData.containsKey("gateway")) {
            setting.setGateway((String) settingData.get("gateway"));
        }
        if (settingData.containsKey("config")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> config = (Map<String, Object>) settingData.get("config");
            setting.setConfig(config);
        }
        
        setting.setUpdatedAt(System.currentTimeMillis());
        settings.put(settingType, setting);
        
        return Result.success(setting);
    }

    @Override
    public Result<PageResult<IPAddress>> listIPAddresses(String type, String status) {
        log.debug("Listing IP addresses: type={}, status={}", type, status);
        
        List<IPAddress> filtered = new ArrayList<IPAddress>();
        
        for (IPAddress ip : ipAddresses.values()) {
            boolean match = true;
            if (type != null && !type.isEmpty() && !type.equals(ip.getType())) {
                match = false;
            }
            if (status != null && !status.isEmpty() && !status.equals(ip.getStatus())) {
                match = false;
            }
            if (match) {
                filtered.add(ip);
            }
        }
        
        PageResult<IPAddress> pageResult = new PageResult<IPAddress>();
        pageResult.setItems(filtered);
        pageResult.setTotal(filtered.size());
        pageResult.setPageNum(1);
        pageResult.setPageSize(filtered.size());
        
        return Result.success(pageResult);
    }

    @Override
    public Result<IPAddress> addStaticIPAddress(Map<String, Object> ipData) {
        log.info("Adding static IP address: {}", ipData);
        
        try {
            String ipId = "ip-" + ipIdCounter.incrementAndGet();
            
            IPAddress ip = new IPAddress();
            ip.setIpId(ipId);
            ip.setIpAddress((String) ipData.get("ipAddress"));
            ip.setType("static");
            ip.setStatus("online");
            ip.setDeviceName((String) ipData.getOrDefault("deviceName", "Unknown Device"));
            ip.setMacAddress((String) ipData.getOrDefault("macAddress", ""));
            ip.setDeviceType((String) ipData.getOrDefault("deviceType", "client"));
            ip.setLeaseTime("永久");
            ip.setCreatedAt(System.currentTimeMillis());
            
            ipAddresses.put(ipId, ip);
            
            return Result.success(ip);
        } catch (Exception e) {
            log.error("Failed to add static IP address", e);
            return Result.error("添加静态IP地址失败: " + e.getMessage());
        }
    }

    @Override
    public Result<IPAddress> deleteIPAddress(String ipId) {
        log.info("Deleting IP address: {}", ipId);
        
        IPAddress removed = ipAddresses.remove(ipId);
        if (removed == null) {
            return Result.error("IP address not found: " + ipId);
        }
        
        return Result.success(removed);
    }

    @Override
    public Result<List<IPBlacklist>> getIPBlacklist() {
        log.debug("Getting IP blacklist");
        
        return Result.success(new ArrayList<IPBlacklist>(ipBlacklist.values()));
    }

    @Override
    public Result<IPBlacklist> addIPToBlacklist(Map<String, Object> blacklistData) {
        log.info("Adding IP to blacklist: {}", blacklistData);
        
        try {
            String blacklistId = "blacklist-" + blacklistIdCounter.incrementAndGet();
            
            IPBlacklist item = new IPBlacklist();
            item.setBlacklistId(blacklistId);
            item.setIpAddress((String) blacklistData.get("ipAddress"));
            item.setReason((String) blacklistData.getOrDefault("reason", "未指定"));
            item.setSource((String) blacklistData.getOrDefault("source", "手动添加"));
            item.setCreatedAt(System.currentTimeMillis());
            
            ipBlacklist.put(blacklistId, item);
            
            return Result.success(item);
        } catch (Exception e) {
            log.error("Failed to add IP to blacklist", e);
            return Result.error("添加IP到黑名单失败: " + e.getMessage());
        }
    }

    @Override
    public Result<IPBlacklist> removeIPFromBlacklist(String blacklistId) {
        log.info("Removing IP from blacklist: {}", blacklistId);
        
        IPBlacklist removed = ipBlacklist.remove(blacklistId);
        if (removed == null) {
            return Result.error("Blacklist entry not found: " + blacklistId);
        }
        
        return Result.success(removed);
    }

    @Override
    public Result<PageResult<NetworkDevice>> listNetworkDevices(String type, String status) {
        log.debug("Listing network devices: type={}, status={}", type, status);
        
        List<NetworkDevice> filtered = new ArrayList<NetworkDevice>();
        
        for (NetworkDevice device : networkDevices.values()) {
            boolean match = true;
            if (type != null && !type.isEmpty() && !type.equals(device.getType())) {
                match = false;
            }
            if (status != null && !status.isEmpty() && !status.equals(device.getStatus())) {
                match = false;
            }
            if (match) {
                filtered.add(device);
            }
        }
        
        PageResult<NetworkDevice> pageResult = new PageResult<NetworkDevice>();
        pageResult.setItems(filtered);
        pageResult.setTotal(filtered.size());
        pageResult.setPageNum(1);
        pageResult.setPageSize(filtered.size());
        
        return Result.success(pageResult);
    }
}
