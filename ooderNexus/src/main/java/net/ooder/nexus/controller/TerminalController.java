package net.ooder.nexus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/terminal")
public class TerminalController {

    private static final Logger logger = LoggerFactory.getLogger(TerminalController.class);

    // 模拟终端数据存储
    private final Map<String, Terminal> terminalMap = new ConcurrentHashMap<>();
    
    // 终端发现状态
    private boolean isDiscovering = false;

    // 初始化模拟数据
    public TerminalController() {
        initMockData();
    }

    /**
     * 初始化模拟终端数据
     */
    private void initMockData() {
        Terminal terminal1 = new Terminal(
                "terminal-001",
                "Desktop-001",
                "desktop",
                "Windows",
                "online",
                "192.168.1.101",
                "00:11:22:33:44:55",
                "Windows 10 Pro 22H2",
                "Intel Core i7-11700K",
                "16GB",
                "512GB SSD",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "100%"); put("network-type", "wifi"); put("signal-strength", "excellent"); put("last-update", new Date().toString()); }}
        );

        Terminal terminal2 = new Terminal(
                "terminal-002",
                "Laptop-001",
                "desktop",
                "macOS",
                "online",
                "192.168.1.102",
                "00:11:22:33:44:56",
                "macOS Ventura 13.0",
                "Apple M1 Pro",
                "16GB",
                "512GB SSD",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "85%"); put("network-type", "wifi"); put("signal-strength", "good"); put("last-update", new Date().toString()); }}
        );

        Terminal terminal3 = new Terminal(
                "terminal-003",
                "Server-001",
                "server",
                "Linux",
                "online",
                "192.168.1.103",
                "00:11:22:33:44:57",
                "Ubuntu Server 22.04 LTS",
                "Intel Xeon E5-2690 v4",
                "64GB",
                "2TB HDD",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "N/A"); put("network-type", "wired"); put("signal-strength", "excellent"); put("last-update", new Date().toString()); }}
        );

        Terminal terminal4 = new Terminal(
                "terminal-004",
                "Mobile-001",
                "mobile",
                "Android",
                "offline",
                "192.168.1.104",
                "00:11:22:33:44:58",
                "Android 13",
                "Snapdragon 8 Gen 2",
                "12GB",
                "256GB",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "60%"); put("network-type", "cellular"); put("signal-strength", "good"); put("last-update", new Date().toString()); }}
        );

        Terminal terminal5 = new Terminal(
                "terminal-005",
                "Desktop-002",
                "desktop",
                "Windows",
                "online",
                "192.168.1.105",
                "00:11:22:33:44:59",
                "Windows 11 Pro 23H2",
                "Intel Core i9-12900K",
                "32GB",
                "1TB SSD",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "100%"); put("network-type", "wired"); put("signal-strength", "excellent"); put("last-update", new Date().toString()); }}
        );

        Terminal terminal6 = new Terminal(
                "terminal-006",
                "Server-002",
                "server",
                "Linux",
                "online",
                "192.168.1.106",
                "00:11:22:33:44:60",
                "CentOS 7",
                "AMD EPYC 7302",
                "128GB",
                "4TB HDD",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "N/A"); put("network-type", "wired"); put("signal-strength", "excellent"); put("last-update", new Date().toString()); }}
        );

        Terminal terminal7 = new Terminal(
                "terminal-007",
                "Laptop-002",
                "desktop",
                "Windows",
                "busy",
                "192.168.1.107",
                "00:11:22:33:44:61",
                "Windows 10 Pro 22H2",
                "Intel Core i5-1135G7",
                "8GB",
                "256GB SSD",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "75%"); put("network-type", "wifi"); put("signal-strength", "fair"); put("last-update", new Date().toString()); }}
        );

        Terminal terminal8 = new Terminal(
                "terminal-008",
                "Mobile-002",
                "mobile",
                "iOS",
                "offline",
                "192.168.1.108",
                "00:11:22:33:44:62",
                "iOS 16.0",
                "Apple A16 Bionic",
                "8GB",
                "256GB",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "50%"); put("network-type", "cellular"); put("signal-strength", "good"); put("last-update", new Date().toString()); }}
        );

        Terminal terminal9 = new Terminal(
                "terminal-009",
                "Desktop-003",
                "desktop",
                "Linux",
                "online",
                "192.168.1.109",
                "00:11:22:33:44:63",
                "Ubuntu 22.04 LTS",
                "AMD Ryzen 7 5800X",
                "32GB",
                "1TB SSD",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "100%"); put("network-type", "wired"); put("signal-strength", "excellent"); put("last-update", new Date().toString()); }}
        );

        Terminal terminal10 = new Terminal(
                "terminal-010",
                "Server-003",
                "server",
                "Linux",
                "online",
                "192.168.1.110",
                "00:11:22:33:44:64",
                "Debian 11",
                "Intel Xeon Silver 4314",
                "64GB",
                "2TB SSD",
                "v0.6.5",
                new Date(),
                new Date(),
                new ConcurrentHashMap<String, Object>() {{ put("battery-level", "N/A"); put("network-type", "wired"); put("signal-strength", "excellent"); put("last-update", new Date().toString()); }}
        );

        terminalMap.put(terminal1.getId(), terminal1);
        terminalMap.put(terminal2.getId(), terminal2);
        terminalMap.put(terminal3.getId(), terminal3);
        terminalMap.put(terminal4.getId(), terminal4);
        terminalMap.put(terminal5.getId(), terminal5);
        terminalMap.put(terminal6.getId(), terminal6);
        terminalMap.put(terminal7.getId(), terminal7);
        terminalMap.put(terminal8.getId(), terminal8);
        terminalMap.put(terminal9.getId(), terminal9);
        terminalMap.put(terminal10.getId(), terminal10);
    }

    /**
     * 获取终端列表
     * @param status 状态筛选（可选）
     * @param type 类型筛选（可选）
     * @param system 系统筛选（可选）
     * @return 终端列表
     */
    @GetMapping("/list")
    public ApiResponse<List<Terminal>> getTerminalList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String system) {
        try {
            logger.info("获取终端列表请求，status: {}, type: {}, system: {}", status, type, system);
            
            List<Terminal> terminals = new ArrayList<>(terminalMap.values());
            
            // 应用筛选条件
            if (status != null) {
                terminals = terminals.stream().filter(t -> status.equals(t.getStatus())).collect(Collectors.toList());
            }
            if (type != null) {
                terminals = terminals.stream().filter(t -> type.equals(t.getType())).collect(Collectors.toList());
            }
            if (system != null) {
                terminals = terminals.stream().filter(t -> system.equals(t.getSystem())).collect(Collectors.toList());
            }
            
            logger.info("获取终端列表成功，共 {} 个终端", terminals.size());
            return ApiResponse.success(terminals, "获取终端列表成功");
        } catch (Exception e) {
            logger.error("获取终端列表失败", e);
            return ApiResponse.error("获取终端列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取终端详情
     * @param terminalId 终端ID
     * @return 终端详情
     */
    @GetMapping("/detail/{terminalId}")
    public ApiResponse<Terminal> getTerminalDetail(@PathVariable String terminalId) {
        try {
            logger.info("获取终端详情请求，终端ID: {}", terminalId);
            Terminal terminal = terminalMap.get(terminalId);
            if (terminal == null) {
                logger.warn("终端不存在，终端ID: {}", terminalId);
                return ApiResponse.error("终端不存在");
            }
            logger.info("获取终端详情成功，终端ID: {}", terminalId);
            return ApiResponse.success(terminal, "获取终端详情成功");
        } catch (Exception e) {
            logger.error("获取终端详情失败，终端ID: {}", terminalId, e);
            return ApiResponse.error("获取终端详情失败: " + e.getMessage());
        }
    }

    /**
     * 发现终端
     * @return 发现结果
     */
    @PostMapping("/discover")
    public ApiResponse<Map<String, Object>> discoverTerminals() {
        try {
            logger.info("发现终端请求");
            
            if (isDiscovering) {
                logger.warn("终端发现正在进行中");
                return ApiResponse.error("终端发现正在进行中");
            }
            
            isDiscovering = true;
            
            // 模拟终端发现过程
            // 这里可以实现实际的终端发现逻辑
            Thread.sleep(2000); // 模拟发现过程
            
            // 模拟发现新终端
            int newTerminalsCount = 5;
            for (int i = 1; i <= newTerminalsCount; i++) {
                String terminalId = "terminal-" + String.format("%03d", terminalMap.size() + 1);
                Terminal newTerminal = new Terminal(
                        terminalId,
                        "New-Terminal-" + i,
                        "desktop",
                        "Windows",
                        "online",
                        "192.168.1." + (110 + i),
                        "00:11:22:33:44:" + (64 + i),
                        "Windows 10 Pro 22H2",
                        "Intel Core i5-11400",
                        "8GB",
                        "256GB SSD",
                        "v0.6.5",
                        new Date(),
                        new Date(),
                        new ConcurrentHashMap<String, Object>() {{ put("battery-level", "100%"); put("network-type", "wifi"); put("signal-strength", "good"); put("last-update", new Date().toString()); }}
                );
                terminalMap.put(terminalId, newTerminal);
            }
            
            isDiscovering = false;
            
            Map<String, Object> result = new ConcurrentHashMap<>();
            result.put("newTerminalsCount", newTerminalsCount);
            result.put("totalTerminals", terminalMap.size());
            
            logger.info("终端发现成功，发现了 {} 个新终端，总终端数: {}", newTerminalsCount, terminalMap.size());
            return ApiResponse.success(result, "终端发现成功");
        } catch (Exception e) {
            isDiscovering = false;
            logger.error("终端发现失败", e);
            return ApiResponse.error("终端发现失败: " + e.getMessage());
        }
    }

    /**
     * 刷新终端列表
     * @return 刷新结果
     */
    @PostMapping("/refresh")
    public ApiResponse<Map<String, Object>> refreshTerminals() {
        try {
            logger.info("刷新终端列表请求");
            
            // 模拟终端刷新过程
            Thread.sleep(1000); // 模拟刷新过程
            
            // 随机更新一些终端的状态
            for (Terminal terminal : terminalMap.values()) {
                // 30% 的概率更新终端状态
                if (Math.random() < 0.3) {
                    String[] statuses = {"online", "offline", "busy"};
                    String newStatus = statuses[(int) (Math.random() * statuses.length)];
                    terminal.setStatus(newStatus);
                    terminal.setLastOnline(new Date());
                }
            }
            
            Map<String, Object> result = new ConcurrentHashMap<>();
            result.put("totalTerminals", terminalMap.size());
            result.put("onlineTerminals", terminalMap.values().stream().filter(t -> "online".equals(t.getStatus())).count());
            result.put("offlineTerminals", terminalMap.values().stream().filter(t -> "offline".equals(t.getStatus())).count());
            result.put("busyTerminals", terminalMap.values().stream().filter(t -> "busy".equals(t.getStatus())).count());
            
            logger.info("终端刷新成功，总终端数: {}", terminalMap.size());
            return ApiResponse.success(result, "终端刷新成功");
        } catch (Exception e) {
            logger.error("终端刷新失败", e);
            return ApiResponse.error("终端刷新失败: " + e.getMessage());
        }
    }

    /**
     * 清除不活跃终端
     * @return 清除结果
     */
    @PostMapping("/clear-inactive")
    public ApiResponse<Map<String, Object>> clearInactiveTerminals() {
        try {
            logger.info("清除不活跃终端请求");
            
            int initialCount = terminalMap.size();
            
            // 清除离线终端
            terminalMap.entrySet().removeIf(entry -> "offline".equals(entry.getValue().getStatus()));
            
            int clearedCount = initialCount - terminalMap.size();
            
            Map<String, Object> result = new ConcurrentHashMap<>();
            result.put("clearedCount", clearedCount);
            result.put("remainingCount", terminalMap.size());
            
            logger.info("清除不活跃终端成功，清除了 {} 个终端，剩余 {} 个终端", clearedCount, terminalMap.size());
            return ApiResponse.success(result, "清除不活跃终端成功");
        } catch (Exception e) {
            logger.error("清除不活跃终端失败", e);
            return ApiResponse.error("清除不活跃终端失败: " + e.getMessage());
        }
    }

    /**
     * 编辑终端
     * @param terminalId 终端ID
     * @param terminal 终端信息
     * @return 编辑结果
     */
    @PutMapping("/edit/{terminalId}")
    public ApiResponse<Terminal> editTerminal(@PathVariable String terminalId, @RequestBody Terminal terminal) {
        try {
            logger.info("编辑终端请求，终端ID: {}", terminalId);
            
            Terminal existingTerminal = terminalMap.get(terminalId);
            if (existingTerminal == null) {
                logger.warn("终端不存在，终端ID: {}", terminalId);
                return ApiResponse.error("终端不存在");
            }
            
            // 更新终端信息
            existingTerminal.setName(terminal.getName());
            existingTerminal.setType(terminal.getType());
            existingTerminal.setSystem(terminal.getSystem());
            existingTerminal.setStatus(terminal.getStatus());
            existingTerminal.setIp(terminal.getIp());
            existingTerminal.setMac(terminal.getMac());
            existingTerminal.setOsVersion(terminal.getOsVersion());
            existingTerminal.setCpu(terminal.getCpu());
            existingTerminal.setMemory(terminal.getMemory());
            existingTerminal.setStorage(terminal.getStorage());
            existingTerminal.setAgentVersion(terminal.getAgentVersion());
            existingTerminal.setLastOnline(new Date());
            if (terminal.getMetadata() != null) {
                existingTerminal.setMetadata(terminal.getMetadata());
            }
            
            logger.info("编辑终端成功，终端ID: {}", terminalId);
            return ApiResponse.success(existingTerminal, "编辑终端成功");
        } catch (Exception e) {
            logger.error("编辑终端失败，终端ID: {}", terminalId, e);
            return ApiResponse.error("编辑终端失败: " + e.getMessage());
        }
    }

    /**
     * 删除终端
     * @param terminalId 终端ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{terminalId}")
    public ApiResponse<Void> deleteTerminal(@PathVariable String terminalId) {
        try {
            logger.info("删除终端请求，终端ID: {}", terminalId);
            
            if (!terminalMap.containsKey(terminalId)) {
                logger.warn("终端不存在，终端ID: {}", terminalId);
                return ApiResponse.error("终端不存在");
            }
            
            terminalMap.remove(terminalId);
            
            logger.info("删除终端成功，终端ID: {}", terminalId);
            return ApiResponse.success(null, "删除终端成功");
        } catch (Exception e) {
            logger.error("删除终端失败，终端ID: {}", terminalId, e);
            return ApiResponse.error("删除终端失败: " + e.getMessage());
        }
    }

    /**
     * Ping 终端
     * @param terminalId 终端ID
     * @return Ping 结果
     */
    @PostMapping("/ping/{terminalId}")
    public ApiResponse<Map<String, Object>> pingTerminal(@PathVariable String terminalId) {
        try {
            logger.info("Ping 终端请求，终端ID: {}", terminalId);
            
            Terminal terminal = terminalMap.get(terminalId);
            if (terminal == null) {
                logger.warn("终端不存在，终端ID: {}", terminalId);
                return ApiResponse.error("终端不存在");
            }
            
            // 模拟 Ping 过程
            Thread.sleep(500); // 模拟网络延迟
            
            // 80% 的成功率
            boolean isSuccess = Math.random() > 0.2;
            int responseTime = (int) (Math.random() * 100) + 10; // 10-110ms
            
            Map<String, Object> result = new ConcurrentHashMap<>();
            result.put("success", isSuccess);
            result.put("responseTime", responseTime);
            result.put("status", terminal.getStatus());
            result.put("ip", terminal.getIp());
            
            logger.info("Ping 终端结果，终端ID: {}, 成功: {}, 响应时间: {}ms", terminalId, isSuccess, responseTime);
            return ApiResponse.success(result, isSuccess ? "Ping 终端成功" : "Ping 终端失败");
        } catch (Exception e) {
            logger.error("Ping 终端失败，终端ID: {}", terminalId, e);
            return ApiResponse.error("Ping 终端失败: " + e.getMessage());
        }
    }

    /**
     * 重启终端
     * @param terminalId 终端ID
     * @return 重启结果
     */
    @PostMapping("/reboot/{terminalId}")
    public ApiResponse<Map<String, Object>> rebootTerminal(@PathVariable String terminalId) {
        try {
            logger.info("重启终端请求，终端ID: {}", terminalId);
            
            Terminal terminal = terminalMap.get(terminalId);
            if (terminal == null) {
                logger.warn("终端不存在，终端ID: {}", terminalId);
                return ApiResponse.error("终端不存在");
            }
            
            // 模拟重启过程
            Thread.sleep(1000); // 模拟重启过程
            
            // 更新终端状态
            terminal.setStatus("offline");
            terminal.setLastOnline(new Date());
            
            Map<String, Object> result = new ConcurrentHashMap<>();
            result.put("terminalId", terminalId);
            result.put("status", "rebooting");
            result.put("message", "终端重启命令已发送");
            
            logger.info("重启终端成功，终端ID: {}", terminalId);
            return ApiResponse.success(result, "重启终端命令已发送");
        } catch (Exception e) {
            logger.error("重启终端失败，终端ID: {}", terminalId, e);
            return ApiResponse.error("重启终端失败: " + e.getMessage());
        }
    }

    /**
     * 更新终端 Agent
     * @param terminalId 终端ID
     * @return 更新结果
     */
    @PostMapping("/update/{terminalId}")
    public ApiResponse<Map<String, Object>> updateTerminal(@PathVariable String terminalId) {
        try {
            logger.info("更新终端 Agent 请求，终端ID: {}", terminalId);
            
            Terminal terminal = terminalMap.get(terminalId);
            if (terminal == null) {
                logger.warn("终端不存在，终端ID: {}", terminalId);
                return ApiResponse.error("终端不存在");
            }
            
            // 模拟更新过程
            Thread.sleep(2000); // 模拟更新过程
            
            // 更新 Agent 版本
            String newVersion = "v0.6.5";
            terminal.setAgentVersion(newVersion);
            terminal.setLastOnline(new Date());
            
            Map<String, Object> result = new ConcurrentHashMap<>();
            result.put("terminalId", terminalId);
            result.put("version", newVersion);
            result.put("status", "updated");
            result.put("message", "Agent 更新成功");
            
            logger.info("更新终端 Agent 成功，终端ID: {}, 版本: {}", terminalId, newVersion);
            return ApiResponse.success(result, "Agent 更新成功");
        } catch (Exception e) {
            logger.error("更新终端 Agent 失败，终端ID: {}", terminalId, e);
            return ApiResponse.error("更新终端 Agent 失败: " + e.getMessage());
        }
    }

    /**
     * 关闭终端
     * @param terminalId 终端ID
     * @return 关闭结果
     */
    @PostMapping("/shutdown/{terminalId}")
    public ApiResponse<Map<String, Object>> shutdownTerminal(@PathVariable String terminalId) {
        try {
            logger.info("关闭终端请求，终端ID: {}", terminalId);
            
            Terminal terminal = terminalMap.get(terminalId);
            if (terminal == null) {
                logger.warn("终端不存在，终端ID: {}", terminalId);
                return ApiResponse.error("终端不存在");
            }
            
            // 模拟关闭过程
            Thread.sleep(500); // 模拟关闭过程
            
            // 更新终端状态
            terminal.setStatus("offline");
            terminal.setLastOnline(new Date());
            
            Map<String, Object> result = new ConcurrentHashMap<>();
            result.put("terminalId", terminalId);
            result.put("status", "shutting down");
            result.put("message", "终端关闭命令已发送");
            
            logger.info("关闭终端成功，终端ID: {}", terminalId);
            return ApiResponse.success(result, "终端关闭命令已发送");
        } catch (Exception e) {
            logger.error("关闭终端失败，终端ID: {}", terminalId, e);
            return ApiResponse.error("关闭终端失败: " + e.getMessage());
        }
    }

    /**
     * 获取终端统计信息
     * @return 终端统计信息
     */
    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getTerminalStats() {
        try {
            logger.info("获取终端统计信息请求");
            
            int totalTerminals = terminalMap.size();
            int onlineTerminals = (int) terminalMap.values().stream().filter(t -> "online".equals(t.getStatus())).count();
            int offlineTerminals = (int) terminalMap.values().stream().filter(t -> "offline".equals(t.getStatus())).count();
            int busyTerminals = (int) terminalMap.values().stream().filter(t -> "busy".equals(t.getStatus())).count();
            long terminalTypes = terminalMap.values().stream().map(Terminal::getType).distinct().count();
            long terminalSystems = terminalMap.values().stream().map(Terminal::getSystem).distinct().count();
            
            Map<String, Object> stats = new ConcurrentHashMap<>();
            stats.put("totalTerminals", totalTerminals);
            stats.put("onlineTerminals", onlineTerminals);
            stats.put("offlineTerminals", offlineTerminals);
            stats.put("busyTerminals", busyTerminals);
            stats.put("terminalTypes", terminalTypes);
            stats.put("terminalSystems", terminalSystems);
            stats.put("discoveryInterval", 120); // 秒
            stats.put("heartbeatInterval", 30); // 秒
            stats.put("maxTerminals", 200);
            
            logger.info("获取终端统计信息成功");
            return ApiResponse.success(stats, "获取终端统计信息成功");
        } catch (Exception e) {
            logger.error("获取终端统计信息失败", e);
            return ApiResponse.error("获取终端统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 终端模型类
     */
    public static class Terminal {
        private String id;
        private String name;
        private String type;
        private String system;
        private String status;
        private String ip;
        private String mac;
        private String osVersion;
        private String cpu;
        private String memory;
        private String storage;
        private String agentVersion;
        private Date firstSeen;
        private Date lastOnline;
        private Map<String, Object> metadata;

        // 构造方法
        public Terminal(String id, String name, String type, String system, String status, String ip, String mac, 
                       String osVersion, String cpu, String memory, String storage, String agentVersion, 
                       Date firstSeen, Date lastOnline, Map<String, Object> metadata) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.system = system;
            this.status = status;
            this.ip = ip;
            this.mac = mac;
            this.osVersion = osVersion;
            this.cpu = cpu;
            this.memory = memory;
            this.storage = storage;
            this.agentVersion = agentVersion;
            this.firstSeen = firstSeen;
            this.lastOnline = lastOnline;
            this.metadata = metadata;
        }

        // 无参构造方法
        public Terminal() {
        }

        // Getter和Setter方法
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getCpu() {
            return cpu;
        }

        public void setCpu(String cpu) {
            this.cpu = cpu;
        }

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public String getStorage() {
            return storage;
        }

        public void setStorage(String storage) {
            this.storage = storage;
        }

        public String getAgentVersion() {
            return agentVersion;
        }

        public void setAgentVersion(String agentVersion) {
            this.agentVersion = agentVersion;
        }

        public Date getFirstSeen() {
            return firstSeen;
        }

        public void setFirstSeen(Date firstSeen) {
            this.firstSeen = firstSeen;
        }

        public Date getLastOnline() {
            return lastOnline;
        }

        public void setLastOnline(Date lastOnline) {
            this.lastOnline = lastOnline;
        }

        public Map<String, Object> getMetadata() {
            return metadata;
        }

        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }
    }

    /**
     * API响应模型类
     */
    public static class ApiResponse<T> {
        private int code;
        private String message;
        private T data;

        // 构造方法
        private ApiResponse(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        // 成功响应
        public static <T> ApiResponse<T> success(T data, String message) {
            return new ApiResponse<>(200, message, data);
        }

        // 错误响应
        public static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(500, message, null);
        }

        // Getter方法
        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }
    }
}
