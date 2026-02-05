package net.ooder.nexus.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/lan")
public class LanController {
    
    // 模拟网络设备数据
    private final List<Map<String, Object>> networkDevices = new ArrayList<>();
    
    // 模拟带宽数据
    private final List<Map<String, Object>> bandwidthData = new ArrayList<>();
    
    // 网络状态数据
    private final Map<String, Object> networkStatus = new HashMap<>();
    
    // 初始化数据
    public LanController() {
        // 初始化网络设备数据
        networkDevices.add(createNetworkDevice(1, "主路由器", "router", "192.168.1.1", "online", "AA:BB:CC:DD:EE:FF"));
        networkDevices.add(createNetworkDevice(2, "交换机", "switch", "192.168.1.2", "online", "AA:BB:CC:DD:EE:GG"));
        networkDevices.add(createNetworkDevice(3, "PC 1", "pc", "192.168.1.100", "online", "AA:BB:CC:DD:EE:HH"));
        networkDevices.add(createNetworkDevice(4, "打印机", "printer", "192.168.1.101", "warning", "AA:BB:CC:DD:EE:II"));
        networkDevices.add(createNetworkDevice(5, "NAS", "nas", "192.168.1.102", "offline", "AA:BB:CC:DD:EE:JJ"));
        networkDevices.add(createNetworkDevice(6, "PC 2", "pc", "192.168.1.103", "online", "AA:BB:CC:DD:EE:KK"));
        
        // 初始化带宽数据
        bandwidthData.add(createBandwidthData("00:00", 1.2, 3.5));
        bandwidthData.add(createBandwidthData("03:00", 0.8, 2.1));
        bandwidthData.add(createBandwidthData("06:00", 1.5, 4.2));
        bandwidthData.add(createBandwidthData("09:00", 2.8, 8.5));
        bandwidthData.add(createBandwidthData("12:00", 2.2, 6.8));
        bandwidthData.add(createBandwidthData("15:00", 3.1, 9.2));
        bandwidthData.add(createBandwidthData("18:00", 2.5, 7.5));
        bandwidthData.add(createBandwidthData("21:00", 1.8, 5.2));
        
        // 初始化网络状态数据
        networkStatus.put("status", "normal");
        networkStatus.put("bandwidthUsage", "45%");
        networkStatus.put("ipUsage", "60%");
        networkStatus.put("ping", "12ms");
        networkStatus.put("packetLoss", "0%");
    }
    
    // 创建网络设备地图
    private Map<String, Object> createNetworkDevice(int id, String name, String type, String ip, String status, String mac) {
        Map<String, Object> device = new HashMap<>();
        device.put("id", id);
        device.put("name", name);
        device.put("type", type);
        device.put("ip", ip);
        device.put("status", status);
        device.put("mac", mac);
        return device;
    }
    
    // 创建带宽数据地图
    private Map<String, Object> createBandwidthData(String time, double upload, double download) {
        Map<String, Object> data = new HashMap<>();
        data.put("time", time);
        data.put("upload", upload);
        data.put("download", download);
        return data;
    }
    
    @GetMapping("/devices")
    public Map<String, Object> getNetworkDevices(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type
    ) {
        List<Map<String, Object>> filteredDevices = new ArrayList<>(networkDevices);
        
        // 按状态过滤
        if (status != null && !status.equals("all")) {
            filteredDevices.removeIf(device -> !device.get("status").equals(status));
        }
        
        // 按类型过滤
        if (type != null && !type.equals("all")) {
            filteredDevices.removeIf(device -> !device.get("type").equals(type));
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("devices", filteredDevices);
        result.put("total", filteredDevices.size());
        return result;
    }
    
    @GetMapping("/devices/{id}")
    public Map<String, Object> getNetworkDevice(@PathVariable int id) {
        Optional<Map<String, Object>> device = networkDevices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();
        
        Map<String, Object> result = new HashMap<>();
        if (device.isPresent()) {
            result.put("status", "success");
            result.put("device", device.get());
        } else {
            result.put("status", "error");
            result.put("message", "设备不存在");
        }
        return result;
    }
    
    @GetMapping("/bandwidth")
    public Map<String, Object> getBandwidthData(
            @RequestParam(required = false) String timeRange
    ) {
        // 模拟不同时间范围的带宽数据
        List<Map<String, Object>> data = new ArrayList<>(bandwidthData);
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("bandwidth", data);
        result.put("unit", "Mbps");
        return result;
    }
    
    @GetMapping("/network/status")
    public Map<String, Object> getNetworkStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("network", networkStatus);
        return result;
    }
    
    @GetMapping("/ip/usage")
    public Map<String, Object> getIpUsage() {
        // 模拟IP使用情况
        List<Map<String, Object>> ipUsage = new ArrayList<>();
        ipUsage.add(createIpUsageData("192.168.1.1", "主路由器", "used"));
        ipUsage.add(createIpUsageData("192.168.1.2", "交换机", "used"));
        ipUsage.add(createIpUsageData("192.168.1.100", "PC 1", "used"));
        ipUsage.add(createIpUsageData("192.168.1.101", "打印机", "used"));
        ipUsage.add(createIpUsageData("192.168.1.102", "NAS", "used"));
        ipUsage.add(createIpUsageData("192.168.1.103", "PC 2", "used"));
        ipUsage.add(createIpUsageData("192.168.1.104", "", "available"));
        ipUsage.add(createIpUsageData("192.168.1.105", "", "available"));
        
        int usedIp = (int) ipUsage.stream()
                .filter(ip -> ip.get("status").equals("used"))
                .count();
        int availableIp = (int) ipUsage.stream()
                .filter(ip -> ip.get("status").equals("available"))
                .count();
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("used", usedIp);
        statistics.put("available", availableIp);
        statistics.put("total", usedIp + availableIp);
        statistics.put("usagePercentage", ((double) usedIp / (usedIp + availableIp)) * 100);
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("ipUsage", ipUsage);
        result.put("statistics", statistics);
        return result;
    }
    
    // 创建IP使用数据地图
    private Map<String, Object> createIpUsageData(String ip, String device, String status) {
        Map<String, Object> data = new HashMap<>();
        data.put("ip", ip);
        data.put("device", device);
        data.put("status", status);
        return data;
    }
    
    @GetMapping("/dashboard")
    public Map<String, Object> getLanDashboard() {
        int totalDevices = networkDevices.size();
        int onlineDevices = (int) networkDevices.stream()
                .filter(d -> d.get("status").equals("online"))
                .count();
        int warningDevices = (int) networkDevices.stream()
                .filter(d -> d.get("status").equals("warning"))
                .count();
        int offlineDevices = (int) networkDevices.stream()
                .filter(d -> d.get("status").equals("offline"))
                .count();
        
        // 计算平均带宽使用
        double avgUpload = bandwidthData.stream()
                .mapToDouble(d -> (double) d.get("upload"))
                .average()
                .orElse(0);
        double avgDownload = bandwidthData.stream()
                .mapToDouble(d -> (double) d.get("download"))
                .average()
                .orElse(0);
        
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalDevices", totalDevices);
        dashboard.put("onlineDevices", onlineDevices);
        dashboard.put("warningDevices", warningDevices);
        dashboard.put("offlineDevices", offlineDevices);
        dashboard.put("networkStatus", networkStatus.get("status"));
        dashboard.put("bandwidthUsage", networkStatus.get("bandwidthUsage"));
        dashboard.put("ipUsage", networkStatus.get("ipUsage"));
        dashboard.put("avgUpload", String.format("%.1f Mbps", avgUpload));
        dashboard.put("avgDownload", String.format("%.1f Mbps", avgDownload));
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("dashboard", dashboard);
        return result;
    }
    
    @PostMapping("/devices/{id}/status")
    public Map<String, Object> updateDeviceStatus(@PathVariable int id, @RequestBody Map<String, Object> statusData) {
        Optional<Map<String, Object>> deviceOptional = networkDevices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst();
        
        Map<String, Object> result = new HashMap<>();
        if (deviceOptional.isPresent()) {
            Map<String, Object> device = deviceOptional.get();
            String newStatus = (String) statusData.get("status");
            
            device.put("status", newStatus);
            
            result.put("status", "success");
            result.put("message", "设备状态更新成功");
            result.put("device", device);
        } else {
            result.put("status", "error");
            result.put("message", "设备不存在");
        }
        return result;
    }
    
    @GetMapping("/topology")
    public Map<String, Object> getNetworkTopology() {
        // 模拟网络拓扑数据
        List<Map<String, Object>> nodes = new ArrayList<>();
        nodes.add(createTopologyNode("router", "主路由器", "router", 400, 50));
        nodes.add(createTopologyNode("switch", "交换机", "switch", 400, 120));
        nodes.add(createTopologyNode("pc1", "PC 1", "pc", 200, 200));
        nodes.add(createTopologyNode("pc2", "PC 2", "pc", 350, 200));
        nodes.add(createTopologyNode("printer", "打印机", "printer", 500, 200));
        nodes.add(createTopologyNode("nas", "NAS", "nas", 650, 200));
        
        List<Map<String, Object>> links = new ArrayList<>();
        links.add(createTopologyLink("router", "switch"));
        links.add(createTopologyLink("switch", "pc1"));
        links.add(createTopologyLink("switch", "pc2"));
        links.add(createTopologyLink("switch", "printer"));
        links.add(createTopologyLink("switch", "nas"));
        
        Map<String, Object> topology = new HashMap<>();
        topology.put("nodes", nodes);
        topology.put("links", links);
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("topology", topology);
        return result;
    }
    
    // 创建拓扑节点地图
    private Map<String, Object> createTopologyNode(String id, String name, String type, int x, int y) {
        Map<String, Object> node = new HashMap<>();
        node.put("id", id);
        node.put("name", name);
        node.put("type", type);
        Map<String, Integer> position = new HashMap<>();
        position.put("x", x);
        position.put("y", y);
        node.put("position", position);
        return node;
    }
    
    // 创建拓扑链接地图
    private Map<String, Object> createTopologyLink(String source, String target) {
        Map<String, Object> link = new HashMap<>();
        link.put("source", source);
        link.put("target", target);
        return link;
    }
}
