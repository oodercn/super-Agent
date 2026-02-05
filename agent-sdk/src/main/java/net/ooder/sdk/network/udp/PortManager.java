package net.ooder.sdk.network.udp;

import net.ooder.sdk.config.PortProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.DatagramSocket;
import java.net.BindException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;

public class PortManager {
    private static final Logger log = LoggerFactory.getLogger(PortManager.class);
    
    private final NetworkEnvironmentDetector envDetector;
    private final Map<ServiceType, Integer> allocatedPorts;
    private final Map<Integer, ServiceType> portToServiceMap;
    private final Map<NetworkEnvironment, PortRangeStrategy> environmentStrategies;
    private final AtomicInteger allocationAttempts = new AtomicInteger(0);
    private final AtomicInteger portConflicts = new AtomicInteger(0);
    
    private final PortProperties portProperties;
    private final List<PortUsageRecord> usageHistory;
    
    @Autowired
    public PortManager(PortProperties portProperties) {
        this.envDetector = new NetworkEnvironmentDetector();
        this.portProperties = portProperties;
        this.usageHistory = new ArrayList<>();
        this.allocatedPorts = new ConcurrentHashMap<>();
        this.portToServiceMap = new ConcurrentHashMap<>();
        this.environmentStrategies = new ConcurrentHashMap<>();
        this.initEnvironmentStrategies();
    }
    
    private void initEnvironmentStrategies() {
        environmentStrategies.put(NetworkEnvironment.LOCAL, 
            new PortRangeStrategy(portProperties.getLocalStart(), portProperties.getLocalEnd()));
        environmentStrategies.put(NetworkEnvironment.LAN, 
            new PortRangeStrategy(portProperties.getLanStart(), portProperties.getLanEnd()));
        environmentStrategies.put(NetworkEnvironment.INTRANET, 
            new PortRangeStrategy(portProperties.getIntranetStart(), portProperties.getIntranetEnd()));
    }
    
    public boolean isPortAvailable(int port) {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            socket.setReuseAddress(true);
            socket.close();
            return true;
        } catch (BindException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int allocatePort(ServiceType type) {
        allocationAttempts.incrementAndGet();
        NetworkEnvironment env = envDetector.getEnvironment();
        PortRangeStrategy strategy = environmentStrategies.get(env);
        
        // 1. 尝试默认端口
        int defaultPort = getEnvironmentSpecificPort(type, env);
        if (isPortAvailable(defaultPort)) {
            recordPortUsage(defaultPort, type, env, true);
            allocatePortInternal(type, defaultPort);
            log.info("分配默认端口{}给{}在{}环境", defaultPort, type, env);
            return defaultPort;
        }
        
        portConflicts.incrementAndGet();
        log.warn("默认端口{}给{}在{}环境被占用", defaultPort, type, env);
        
        // 2. 按照网络环境的端口范围顺序扫描
        int startPort = strategy.getStartPort();
        int endPort = strategy.getEndPort();
        for (int port = startPort; port <= endPort; port++) {
            if (isPortAvailable(port) && !portToServiceMap.containsKey(port)) {
                recordPortUsage(port, type, env, true);
                allocatePortInternal(type, port);
                log.info("顺序分配端口{}给{}在{}环境", port, type, env);
                return port;
            }
        }
        
        // 3. 智能分配：基于历史数据选择最佳端口
        if (portProperties.isSmartAllocationEnabled()) {
            int smartPort = findBestAvailablePort(type, env);
            if (smartPort != -1) {
                recordPortUsage(smartPort, type, env, true);
                allocatePortInternal(type, smartPort);
                log.info("智能分配端口{}给{}在{}环境", smartPort, type, env);
                return smartPort;
            }
        }
        
        // 4. 全局随机分配（最后手段）
        for (int i = 0; i < 50; i++) {
            int port = findRandomAvailablePort();
            if (port != -1) {
                recordPortUsage(port, type, env, true);
                allocatePortInternal(type, port);
                log.info("随机分配端口{}给{}在{}环境", port, type, env);
                return port;
            }
        }
        
        throw new PortAllocationException("无法为" + type + "在" + env + "环境分配端口");
    }
    
    private int findBestAvailablePort(ServiceType type, NetworkEnvironment env) {
        List<Integer> candidates = findCandidatePorts(type, env);
        
        for (int port : candidates) {
            if (isPortAvailable(port) && !portToServiceMap.containsKey(port)) {
                double score = calculatePortScore(port);
                if (score > 0.7) {
                    return port;
                }
            }
        }
        
        return -1;
    }
    
    private List<Integer> findCandidatePorts(ServiceType type, NetworkEnvironment env) {
        List<Integer> candidates = new ArrayList<>();
        PortRangeStrategy strategy = environmentStrategies.get(env);
        
        // 添加历史使用良好的端口
        for (PortUsageRecord record : usageHistory) {
            if (record.getServiceType() == type && 
                record.getEnvironment() == env &&
                calculatePortScore(record.getPort()) > 0.6) {
                candidates.add(record.getPort());
            }
        }
        
        // 添加策略范围内的端口
        for (int port = strategy.getStartPort(); port <= strategy.getEndPort(); port++) {
            if (!candidates.contains(port)) {
                candidates.add(port);
            }
        }
        
        return candidates;
    }
    
    private double calculatePortScore(int port) {
        int successCount = 0;
        int conflictCount = 0;
        int recentUsage = 0;
        
        for (PortUsageRecord record : usageHistory) {
            if (record.getPort() == port) {
                if (record.isSuccess()) {
                    successCount++;
                } else {
                    conflictCount++;
                }
                
                if (record.getTimestamp() > System.currentTimeMillis() - 3600000) {
                    recentUsage++;
                }
            }
        }
        
        double totalUsage = successCount + conflictCount + recentUsage;
        if (totalUsage > 0) {
            double successRate = successCount / totalUsage;
            double conflictRate = conflictCount / totalUsage;
            double recentUsageRate = recentUsage / totalUsage;
            return (successRate * 0.5) - (conflictRate * 0.3) + (recentUsageRate * 0.2);
        }
        
        return 0.0;
    }
    
    private void recordPortUsage(int port, ServiceType type, NetworkEnvironment env, boolean success) {
        PortUsageRecord record = new PortUsageRecord(port, type, env, System.currentTimeMillis(), success);
        usageHistory.add(record);
        
        // 保持历史记录不超过配置的大小
        while (usageHistory.size() > portProperties.getHistorySize()) {
            usageHistory.remove(0);
        }
    }
    
    private int findRandomAvailablePort() {
        Random random = new Random();
        int startPort = portProperties.getGlobalStart();
        int endPort = portProperties.getGlobalEnd();
        
        for (int i = 0; i < 100; i++) {
            int port = startPort + random.nextInt(endPort - startPort + 1);
            if (isPortAvailable(port) && !portToServiceMap.containsKey(port)) {
                return port;
            }
        }
        
        return -1;
    }
    
    private void allocatePortInternal(ServiceType type, int port) {
        allocatedPorts.put(type, port);
        portToServiceMap.put(port, type);
    }
    
    public void releasePort(ServiceType type) {
        Integer port = allocatedPorts.remove(type);
        if (port != null) {
            portToServiceMap.remove(port);
            log.info("释放端口{}给{}", port, type);
        }
    }
    
    public void releasePort(int port) {
        ServiceType type = portToServiceMap.remove(port);
        if (type != null) {
            allocatedPorts.remove(type);
            log.info("释放端口{}给{}", port, type);
        }
    }
    
    public Integer getAllocatedPort(ServiceType type) {
        return allocatedPorts.get(type);
    }
    
    public ServiceType getServiceTypeForPort(int port) {
        return portToServiceMap.get(port);
    }
    
    public NetworkEnvironment getNetworkEnvironment() {
        return envDetector.getEnvironment();
    }
    
    private int getEnvironmentSpecificPort(ServiceType type, NetworkEnvironment env) {
        switch (env) {
            case LOCAL:
                return type.getDefaultPort();
            case LAN:
                return type.getDefaultPort() + 1000;
            case INTRANET:
                return type.getDefaultPort() + 2000;
            default:
                return type.getDefaultPort();
        }
    }
    
    public Map<String, Object> getPortStatistics() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("allocationAttempts", allocationAttempts.get());
        stats.put("portConflicts", portConflicts.get());
        stats.put("allocatedPorts", allocatedPorts.size());
        stats.put("networkEnvironment", getNetworkEnvironment());
        stats.put("portMap", portToServiceMap);
        stats.put("usageHistorySize", usageHistory.size());
        return stats;
    }
    
    public void printPortStatistics() {
        Map<String, Object> stats = getPortStatistics();
        log.info("端口管理器统计信息: {}", stats);
    }
    
    public static class PortRangeStrategy {
        private final int startPort;
        private final int endPort;
        
        public PortRangeStrategy(int startPort, int endPort) {
            this.startPort = startPort;
            this.endPort = endPort;
        }
        
        public int getStartPort() {
            return startPort;
        }
        
        public int getEndPort() {
            return endPort;
        }
    }
    
    public enum ServiceType {
        UDPSDK(8080, 8080, 8100),
        DISCOVERY(5000, 5000, 5020),
        SKILL(9000, 9000, 9020),
        TEMPORARY(10000, 10000, 11000);
        
        private final int defaultPort;
        private final int startPort;
        private final int endPort;
        
        ServiceType(int defaultPort, int startPort, int endPort) {
            this.defaultPort = defaultPort;
            this.startPort = startPort;
            this.endPort = endPort;
        }
        
        public int getDefaultPort() {
            return defaultPort;
        }
        
        public int getStartPort() {
            return startPort;
        }
        
        public int getEndPort() {
            return endPort;
        }
    }
    
    public enum NetworkEnvironment {
        LOCAL,
        LAN,
        INTRANET
    }
    
    public static class PortAllocationException extends RuntimeException {
        public PortAllocationException(String message) {
            super(message);
        }
    }
    
    private class PortUsageRecord {
        private final int port;
        private final ServiceType serviceType;
        private final NetworkEnvironment environment;
        private final long timestamp;
        private final boolean success;
        
        public PortUsageRecord(int port, ServiceType serviceType, NetworkEnvironment environment,
                            long timestamp, boolean success) {
            this.port = port;
            this.serviceType = serviceType;
            this.environment = environment;
            this.timestamp = timestamp;
            this.success = success;
        }
        
        public int getPort() {
            return port;
        }
        
        public ServiceType getServiceType() {
            return serviceType;
        }
        
        public NetworkEnvironment getEnvironment() {
            return environment;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public boolean isSuccess() {
            return success;
        }
    }
}
