package net.ooder.nexus.adapter.inbound.controller.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/network/monitor")
public class NetworkMonitorController {

    private static final Logger log = LoggerFactory.getLogger(NetworkMonitorController.class);
    
    // 网络状态存储
    private final Map<String, NetworkStatus> networkStatus = new ConcurrentHashMap<>();
    
    // 带宽监控数据
    private final List<BandwidthData> bandwidthHistory = new ArrayList<>();
    
    // 网络接口状态
    private final List<NetworkInterface> networkInterfaces = new ArrayList<>();
    
    // 初始化默认网络监控数据
    public NetworkMonitorController() {
        initializeDefaultNetworkStatus();
        initializeDefaultBandwidthData();
        initializeDefaultNetworkInterfaces();
    }
    
    private void initializeDefaultNetworkStatus() {
        // 添加默认网络状态
        networkStatus.put("overall", new NetworkStatus(
            "overall", "整体网络状态", "healthy", "网络连接正常", 
            95, 10, 5, 15, 1000, System.currentTimeMillis()
        ));
        
        networkStatus.put("internet", new NetworkStatus(
            "internet", "互联网连接", "healthy", "互联网连接正常", 
            98, 15, 8, 20, 1000, System.currentTimeMillis()
        ));
        
        networkStatus.put("lan", new NetworkStatus(
            "lan", "局域网状态", "healthy", "局域网连接正常", 
            100, 5, 2, 10, 1000, System.currentTimeMillis()
        ));
        
        networkStatus.put("wifi", new NetworkStatus(
            "wifi", "WiFi状态", "warning", "WiFi信号强度一般", 
            75, 25, 15, 30, 867, System.currentTimeMillis()
        ));
    }
    
    private void initializeDefaultBandwidthData() {
        // 生成过去10分钟的带宽数据
        long now = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            long timestamp = now - (9 - i) * 60000; // 从9分钟前开始，每分钟一条数据
            bandwidthHistory.add(new BandwidthData(
                timestamp,
                5000000 + (long)(Math.random() * 10000000), // 5-15 Mbps
                3000000 + (long)(Math.random() * 8000000),  // 3-11 Mbps
                1000 + (int)(Math.random() * 2000),         // 1-3 ms
                5 + (int)(Math.random() * 15)             // 5-20%
            ));
        }
    }
    
    private void initializeDefaultNetworkInterfaces() {
        // 添加默认网络接口
        networkInterfaces.add(new NetworkInterface(
            "eth0", "以太网接口", "up", "192.168.1.100", "255.255.255.0", 
            "AA:BB:CC:DD:EE:01", "1000Mbps", 80, 30, System.currentTimeMillis()
        ));
        
        networkInterfaces.add(new NetworkInterface(
            "wlan0", "无线接口", "up", "192.168.1.101", "255.255.255.0", 
            "AA:BB:CC:DD:EE:02", "867Mbps", 60, 20, System.currentTimeMillis()
        ));
        
        networkInterfaces.add(new NetworkInterface(
            "lo", "本地回环", "up", "127.0.0.1", "255.0.0.0", 
            "00:00:00:00:00:00", "10000Mbps", 0, 0, System.currentTimeMillis()
        ));
    }
    
    /**
     * 获取网络状态
     */
    @GetMapping("/status")
    public Map<String, Object> getNetworkStatus(@RequestParam(required = false) String type) {
        log.info("Get network status requested: type={}", type);
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> statusData = new HashMap<>();
            
            if (type != null) {
                // 获取特定类型的网络状态
                NetworkStatus status = networkStatus.get(type);
                if (status == null) {
                    response.put("status", "error");
                    response.put("message", "Network status type not found");
                    response.put("code", "NETWORK_STATUS_NOT_FOUND");
                    response.put("timestamp", System.currentTimeMillis());
                    return response;
                }
                statusData.put("status", status.toMap());
            } else {
                // 获取所有网络状态
                Map<String, Object> allStatus = new HashMap<>();
                for (Map.Entry<String, NetworkStatus> entry : networkStatus.entrySet()) {
                    allStatus.put(entry.getKey(), entry.getValue().toMap());
                }
                statusData.put("statuses", allStatus);
                
                // 计算整体网络评分
                double averageScore = networkStatus.values().stream()
                        .mapToInt(NetworkStatus::getScore)
                        .average()
                        .orElse(0);
                statusData.put("averageScore", Math.round(averageScore));
                
                // 网络状态统计
                long healthyCount = networkStatus.values().stream()
                        .filter(s -> "healthy".equals(s.getStatus())).count();
                long warningCount = networkStatus.values().stream()
                        .filter(s -> "warning".equals(s.getStatus())).count();
                long criticalCount = networkStatus.values().stream()
                        .filter(s -> "critical".equals(s.getStatus())).count();
                
                Map<String, Object> stats = new ConcurrentHashMap<>();
                stats.put("total", networkStatus.size());
                stats.put("healthy", healthyCount);
                stats.put("warning", warningCount);
                stats.put("critical", criticalCount);
                statusData.put("stats", stats);
            }
            
            response.put("status", "success");
            response.put("message", "Network status retrieved successfully");
            response.put("data", statusData);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting network status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_STATUS_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取带宽监控数据
     */
    @GetMapping("/bandwidth")
    public Map<String, Object> getBandwidthData(
            @RequestParam(defaultValue = "600000") long timeRange, // 默认10分钟
            @RequestParam(defaultValue = "60000") long interval) { // 默认1分钟间隔
        log.info("Get bandwidth data requested: timeRange={}, interval={}", timeRange, interval);
        Map<String, Object> response = new HashMap<>();
        
        try {
            long now = System.currentTimeMillis();
            long startTime = now - timeRange;
            
            // 过滤时间范围内的数据
            List<BandwidthData> filteredData = bandwidthHistory.stream()
                    .filter(data -> data.getTimestamp() >= startTime)
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> bandwidthList = new ArrayList<>();
            for (BandwidthData data : filteredData) {
                bandwidthList.add(data.toMap());
            }
            
            // 计算带宽统计
            Map<String, Object> stats = new HashMap<>();
            if (!filteredData.isEmpty()) {
                long maxUpload = filteredData.stream()
                        .mapToLong(BandwidthData::getUploadBytes)
                        .max()
                        .orElse(0);
                long minUpload = filteredData.stream()
                        .mapToLong(BandwidthData::getUploadBytes)
                        .min()
                        .orElse(0);
                long avgUpload = (long) filteredData.stream()
                        .mapToLong(BandwidthData::getUploadBytes)
                        .average()
                        .orElse(0);
                
                long maxDownload = filteredData.stream()
                        .mapToLong(BandwidthData::getDownloadBytes)
                        .max()
                        .orElse(0);
                long minDownload = filteredData.stream()
                        .mapToLong(BandwidthData::getDownloadBytes)
                        .min()
                        .orElse(0);
                long avgDownload = (long) filteredData.stream()
                        .mapToLong(BandwidthData::getDownloadBytes)
                        .average()
                        .orElse(0);
                
                double maxLatency = filteredData.stream()
                        .mapToInt(BandwidthData::getLatency)
                        .max()
                        .orElse(0);
                double minLatency = filteredData.stream()
                        .mapToInt(BandwidthData::getLatency)
                        .min()
                        .orElse(0);
                double avgLatency = filteredData.stream()
                        .mapToInt(BandwidthData::getLatency)
                        .average()
                        .orElse(0);
                
                Map<String, Object> uploadStats = new ConcurrentHashMap<>();
                uploadStats.put("max", maxUpload);
                uploadStats.put("min", minUpload);
                uploadStats.put("avg", avgUpload);
                uploadStats.put("maxMbps", (double) maxUpload * 8 / 1000000);
                uploadStats.put("minMbps", (double) minUpload * 8 / 1000000);
                uploadStats.put("avgMbps", (double) avgUpload * 8 / 1000000);
                stats.put("upload", uploadStats);
                
                Map<String, Object> downloadStats = new ConcurrentHashMap<>();
                downloadStats.put("max", maxDownload);
                downloadStats.put("min", minDownload);
                downloadStats.put("avg", avgDownload);
                downloadStats.put("maxMbps", (double) maxDownload * 8 / 1000000);
                downloadStats.put("minMbps", (double) minDownload * 8 / 1000000);
                downloadStats.put("avgMbps", (double) avgDownload * 8 / 1000000);
                stats.put("download", downloadStats);
                
                Map<String, Object> latencyStats = new ConcurrentHashMap<>();
                latencyStats.put("max", maxLatency);
                latencyStats.put("min", minLatency);
                latencyStats.put("avg", avgLatency);
                stats.put("latency", latencyStats);
            }
            
            response.put("status", "success");
            response.put("message", "Bandwidth data retrieved successfully");
            response.put("data", bandwidthList);
            response.put("stats", stats);
            response.put("timeRange", timeRange);
            response.put("interval", interval);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting bandwidth data: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "BANDWIDTH_DATA_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取网络接口状态
     */
    @GetMapping("/interfaces")
    public Map<String, Object> getNetworkInterfaces(
            @RequestParam(required = false) String status) {
        log.info("Get network interfaces requested: status={}", status);
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<NetworkInterface> filteredInterfaces = networkInterfaces.stream()
                    .filter(intf -> (status == null || intf.getStatus().equals(status)))
                    .collect(Collectors.toList());
            
            // 转换为响应格式
            List<Map<String, Object>> interfaceList = new ArrayList<>();
            for (NetworkInterface intf : filteredInterfaces) {
                interfaceList.add(intf.toMap());
            }
            
            // 网络接口统计
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", networkInterfaces.size());
            stats.put("up", networkInterfaces.stream().filter(i -> "up".equals(i.getStatus())).count());
            stats.put("down", networkInterfaces.stream().filter(i -> "down".equals(i.getStatus())).count());
            
            // 计算总带宽
            int totalBandwidth = networkInterfaces.stream()
                    .mapToInt(intf -> {
                        try {
                            return Integer.parseInt(intf.getSpeed().replace("Mbps", ""));
                        } catch (Exception e) {
                            return 0;
                        }
                    })
                    .sum();
            stats.put("totalBandwidth", totalBandwidth + "Mbps");
            
            response.put("status", "success");
            response.put("message", "Network interfaces retrieved successfully");
            response.put("data", interfaceList);
            response.put("stats", stats);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting network interfaces: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_INTERFACES_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 刷新网络状态
     */
    @PostMapping("/refresh")
    public Map<String, Object> refreshNetworkStatus() {
        log.info("Refresh network status requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 刷新网络状态
            for (NetworkStatus status : networkStatus.values()) {
                status.refresh();
            }
            
            // 添加最新的带宽数据
            addLatestBandwidthData();
            
            // 刷新网络接口状态
            for (NetworkInterface intf : networkInterfaces) {
                intf.refresh();
            }
            
            response.put("status", "success");
            response.put("message", "Network status refreshed successfully");
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error refreshing network status: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_STATUS_REFRESH_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 获取网络监控摘要
     */
    @GetMapping("/summary")
    public Map<String, Object> getNetworkMonitorSummary() {
        log.info("Get network monitor summary requested");
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> summary = new HashMap<>();
            
            // 网络状态摘要
            Map<String, Object> statusSummary = new HashMap<>();
            double averageScore = networkStatus.values().stream()
                    .mapToInt(NetworkStatus::getScore)
                    .average()
                    .orElse(0);
            statusSummary.put("averageScore", Math.round(averageScore));
            Map<String, Object> statusCounts = new ConcurrentHashMap<>();
            statusCounts.put("healthy", networkStatus.values().stream().filter(s -> "healthy".equals(s.getStatus())).count());
            statusCounts.put("warning", networkStatus.values().stream().filter(s -> "warning".equals(s.getStatus())).count());
            statusCounts.put("critical", networkStatus.values().stream().filter(s -> "critical".equals(s.getStatus())).count());
            statusSummary.put("statusCounts", statusCounts);
            summary.put("status", statusSummary);
            
            // 带宽摘要
            if (!bandwidthHistory.isEmpty()) {
                BandwidthData latestBandwidth = bandwidthHistory.get(bandwidthHistory.size() - 1);
                Map<String, Object> bandwidthSummary = new ConcurrentHashMap<>();
                bandwidthSummary.put("uploadMbps", (double) latestBandwidth.getUploadBytes() * 8 / 1000000);
                bandwidthSummary.put("downloadMbps", (double) latestBandwidth.getDownloadBytes() * 8 / 1000000);
                bandwidthSummary.put("latency", latestBandwidth.getLatency());
                bandwidthSummary.put("packetLoss", latestBandwidth.getPacketLoss());
                summary.put("bandwidth", bandwidthSummary);
            }
            
            // 网络接口摘要
            Map<String, Object> interfacesSummary = new ConcurrentHashMap<>();
            interfacesSummary.put("total", networkInterfaces.size());
            interfacesSummary.put("up", networkInterfaces.stream().filter(i -> "up".equals(i.getStatus())).count());
            interfacesSummary.put("down", networkInterfaces.stream().filter(i -> "down".equals(i.getStatus())).count());
            summary.put("interfaces", interfacesSummary);
            
            response.put("status", "success");
            response.put("message", "Network monitor summary retrieved successfully");
            response.put("data", summary);
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            log.error("Error getting network monitor summary: {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("code", "NETWORK_SUMMARY_RETRIEVAL_ERROR");
            response.put("timestamp", System.currentTimeMillis());
        }
        
        return response;
    }
    
    /**
     * 添加最新的带宽数据
     */
    private void addLatestBandwidthData() {
        long now = System.currentTimeMillis();
        bandwidthHistory.add(new BandwidthData(
            now,
            5000000 + (long)(Math.random() * 10000000), // 5-15 Mbps
            3000000 + (long)(Math.random() * 8000000),  // 3-11 Mbps
            1000 + (int)(Math.random() * 2000),         // 1-3 ms
            5 + (int)(Math.random() * 15)             // 5-20%
        ));
        
        // 限制带宽数据数量，只保留最近1小时的数据
        if (bandwidthHistory.size() > 60) { // 1小时，每分钟一条
            bandwidthHistory.remove(0);
        }
    }
    
    // 网络状态类
    private static class NetworkStatus {
        private final String id;
        private final String name;
        private String status;
        private String message;
        private int score;
        private int latency;
        private int jitter;
        private int packetLoss;
        private int bandwidth;
        private long lastUpdated;
        
        public NetworkStatus(String id, String name, String status, String message, int score,
                           int latency, int jitter, int packetLoss, int bandwidth, long lastUpdated) {
            this.id = id;
            this.name = name;
            this.status = status;
            this.message = message;
            this.score = score;
            this.latency = latency;
            this.jitter = jitter;
            this.packetLoss = packetLoss;
            this.bandwidth = bandwidth;
            this.lastUpdated = lastUpdated;
        }
        
        public void refresh() {
            // 模拟刷新网络状态
            this.latency = 5 + (int)(Math.random() * 30); // 5-35 ms
            this.jitter = 2 + (int)(Math.random() * 15); // 2-17 ms
            this.packetLoss = (int)(Math.random() * 10); // 0-10%
            
            // 根据指标更新状态
            if (latency < 50 && jitter < 20 && packetLoss < 5) {
                this.status = "healthy";
                this.message = "网络连接正常";
                this.score = 90 + (int)(Math.random() * 10); // 90-100
            } else if (latency < 100 && jitter < 50 && packetLoss < 15) {
                this.status = "warning";
                this.message = "网络连接一般";
                this.score = 60 + (int)(Math.random() * 30); // 60-90
            } else {
                this.status = "critical";
                this.message = "网络连接异常";
                this.score = 0 + (int)(Math.random() * 60); // 0-60
            }
            
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("status", status);
            map.put("message", message);
            map.put("score", score);
            map.put("latency", latency);
            map.put("jitter", jitter);
            map.put("packetLoss", packetLoss);
            map.put("bandwidth", bandwidth);
            map.put("lastUpdated", lastUpdated);
            return map;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getStatus() { return status; }
        public int getScore() { return score; }
    }
    
    // 带宽数据类
    private static class BandwidthData {
        private final long timestamp;
        private final long uploadBytes;
        private final long downloadBytes;
        private final int latency;
        private final int packetLoss;
        
        public BandwidthData(long timestamp, long uploadBytes, long downloadBytes, int latency, int packetLoss) {
            this.timestamp = timestamp;
            this.uploadBytes = uploadBytes;
            this.downloadBytes = downloadBytes;
            this.latency = latency;
            this.packetLoss = packetLoss;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("timestamp", timestamp);
            map.put("uploadBytes", uploadBytes);
            map.put("downloadBytes", downloadBytes);
            map.put("uploadMbps", (double) uploadBytes * 8 / 1000000);
            map.put("downloadMbps", (double) downloadBytes * 8 / 1000000);
            map.put("latency", latency);
            map.put("packetLoss", packetLoss);
            return map;
        }
        
        public long getTimestamp() { return timestamp; }
        public long getUploadBytes() { return uploadBytes; }
        public long getDownloadBytes() { return downloadBytes; }
        public int getLatency() { return latency; }
        public int getPacketLoss() { return packetLoss; }
    }
    
    // 网络接口类
    private static class NetworkInterface {
        private final String name;
        private final String description;
        private String status;
        private final String ipAddress;
        private final String subnetMask;
        private final String macAddress;
        private final String speed;
        private int usage;
        private int errorRate;
        private long lastUpdated;
        
        public NetworkInterface(String name, String description, String status, String ipAddress,
                              String subnetMask, String macAddress, String speed, int usage, int errorRate, long lastUpdated) {
            this.name = name;
            this.description = description;
            this.status = status;
            this.ipAddress = ipAddress;
            this.subnetMask = subnetMask;
            this.macAddress = macAddress;
            this.speed = speed;
            this.usage = usage;
            this.errorRate = errorRate;
            this.lastUpdated = lastUpdated;
        }
        
        public void refresh() {
            // 模拟刷新网络接口状态
            this.usage = 20 + (int)(Math.random() * 60); // 20-80%
            this.errorRate = (int)(Math.random() * 5); // 0-5%
            this.lastUpdated = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("description", description);
            map.put("status", status);
            map.put("ipAddress", ipAddress);
            map.put("subnetMask", subnetMask);
            map.put("macAddress", macAddress);
            map.put("speed", speed);
            map.put("usage", usage);
            map.put("errorRate", errorRate);
            map.put("lastUpdated", lastUpdated);
            return map;
        }
        
        public String getName() { return name; }
        public String getStatus() { return status; }
        public String getSpeed() { return speed; }
    }
}
