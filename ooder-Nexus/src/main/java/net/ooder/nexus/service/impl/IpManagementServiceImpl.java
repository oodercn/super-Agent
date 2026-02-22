package net.ooder.nexus.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.ooder.nexus.domain.network.model.IpAllocation;
import net.ooder.nexus.domain.network.model.IpPool;
import net.ooder.nexus.service.IpManagementService;
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
 * IP管理服务实现类
 */
@Service
public class IpManagementServiceImpl implements IpManagementService {

    private static final Logger log = LoggerFactory.getLogger(IpManagementServiceImpl.class);
    private static final String DATA_DIR = "./storage/network";
    private static final String IP_ALLOC_FILE = "ip-allocations.json";
    private static final String IP_POOL_FILE = "ip-pool.json";

    private final Map<String, IpAllocation> allocationCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Path allocationsPath;
    private final Path poolPath;
    private IpPool ipPool;

    public IpManagementServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.allocationsPath = Paths.get(DATA_DIR, IP_ALLOC_FILE);
        this.poolPath = Paths.get(DATA_DIR, IP_POOL_FILE);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            loadData();

            if (allocationCache.isEmpty()) {
                initDefaultData();
            }

            log.info("IP管理服务初始化完成，共加载 {} 个IP分配", allocationCache.size());
        } catch (IOException e) {
            log.error("初始化IP管理服务失败", e);
        }
    }

    private void loadData() {
        if (Files.exists(allocationsPath)) {
            try {
                String json = new String(Files.readAllBytes(allocationsPath), StandardCharsets.UTF_8);
                List<IpAllocation> allocations = objectMapper.readValue(json, new TypeReference<List<IpAllocation>>() {});
                allocations.forEach(a -> allocationCache.put(a.getId(), a));
            } catch (IOException e) {
                log.error("加载IP分配数据失败", e);
            }
        }

        if (Files.exists(poolPath)) {
            try {
                String json = new String(Files.readAllBytes(poolPath), StandardCharsets.UTF_8);
                ipPool = objectMapper.readValue(json, IpPool.class);
            } catch (IOException e) {
                log.error("加载IP池配置失败", e);
            }
        }
    }

    private void saveAllocations() {
        try {
            List<IpAllocation> allocations = new ArrayList<>(allocationCache.values());
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(allocations);
            Files.write(allocationsPath, json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("保存IP分配数据失败", e);
        }
    }

    private void savePool() {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ipPool);
            Files.write(poolPath, json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("保存IP池配置失败", e);
        }
    }

    @Override
    public void initDefaultData() {
        List<IpAllocation> defaultAllocations = Arrays.asList(
            new IpAllocation("ip-001", "主路由器", "00:11:22:33:44:55", "192.168.1.1", "active", "static", "主路由器"),
            new IpAllocation("ip-002", "智能家居中心", "AA:BB:CC:DD:EE:FF", "192.168.1.10", "active", "static", "智能家居中心"),
            new IpAllocation("ip-003", "网络存储", "11:22:33:44:55:66", "192.168.1.20", "active", "static", "网络存储"),
            new IpAllocation("ip-004", "打印机", "22:33:44:55:66:77", "192.168.1.30", "inactive", "static", "打印机"),
            new IpAllocation("ip-005", "监控摄像头", "33:44:55:66:77:88", "192.168.1.40", "active", "static", "监控摄像头"),
            new IpAllocation("ip-006", "笔记本电脑", "44:55:66:77:88:99", "192.168.1.101", "active", "dhcp", "用户笔记本电脑"),
            new IpAllocation("ip-007", "智能手机", "55:66:77:88:99:AA", "192.168.1.102", "active", "dhcp", "用户手机"),
            new IpAllocation("ip-008", "平板电脑", "66:77:88:99:AA:BB", "192.168.1.103", "active", "dhcp", "用户平板"),
            new IpAllocation("ip-009", "智能电视", "77:88:99:AA:BB:CC", "192.168.1.104", "active", "dhcp", "客厅电视"),
            new IpAllocation("ip-010", "游戏主机", "88:99:AA:BB:CC:DD", "192.168.1.105", "active", "dhcp", "游戏主机"),
            new IpAllocation("ip-011", "智能音箱", "99:AA:BB:CC:DD:EE", "192.168.1.106", "inactive", "dhcp", "卧室音箱"),
            new IpAllocation("ip-012", "智能灯泡", "AA:BB:CC:DD:EE:01", "192.168.1.107", "active", "dhcp", "客厅灯泡")
        );

        defaultAllocations.forEach(a -> allocationCache.put(a.getId(), a));
        saveAllocations();

        ipPool = new IpPool("pool-001", "192.168.1.100", "192.168.1.200", "255.255.255.0");
        ipPool.setGateway("192.168.1.1");
        ipPool.setDns("192.168.1.1");
        ipPool.setAllocatedCount(defaultAllocations.size());
        savePool();

        log.info("初始化默认IP分配数据完成，共 {} 个分配", defaultAllocations.size());
    }

    @Override
    public List<IpAllocation> getAllAllocations() {
        return new ArrayList<>(allocationCache.values());
    }

    @Override
    public List<IpAllocation> getAllocationsByType(String type) {
        return allocationCache.values().stream()
                .filter(a -> type.equals(a.getAllocationType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<IpAllocation> getAllocationsByStatus(String status) {
        return allocationCache.values().stream()
                .filter(a -> status.equals(a.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public IpAllocation createAllocation(IpAllocation allocation) {
        if (allocation.getId() == null || allocation.getId().isEmpty()) {
            allocation.setId("ip-" + UUID.randomUUID().toString().substring(0, 8));
        }
        allocation.setAllocateTime(LocalDateTime.now());
        allocationCache.put(allocation.getId(), allocation);
        saveAllocations();
        updatePoolAllocation();
        log.info("创建IP分配: {} -> {}", allocation.getDeviceName(), allocation.getIpAddress());
        return allocation;
    }

    @Override
    public IpAllocation updateAllocation(String id, IpAllocation allocation) {
        if (!allocationCache.containsKey(id)) {
            return null;
        }
        allocation.setId(id);
        allocation.setAllocateTime(allocationCache.get(id).getAllocateTime());
        allocationCache.put(id, allocation);
        saveAllocations();
        log.info("更新IP分配: {}", id);
        return allocation;
    }

    @Override
    public boolean deleteAllocation(String id) {
        if (allocationCache.remove(id) != null) {
            saveAllocations();
            updatePoolAllocation();
            log.info("删除IP分配: {}", id);
            return true;
        }
        return false;
    }

    @Override
    public IpPool getIpPool() {
        if (ipPool == null) {
            ipPool = new IpPool("pool-001", "192.168.1.100", "192.168.1.200", "255.255.255.0");
            ipPool.setAllocatedCount(allocationCache.size());
        }
        return ipPool;
    }

    @Override
    public IpPool updateIpPool(IpPool pool) {
        if (pool.getId() == null) {
            pool.setId("pool-001");
        }
        ipPool = pool;
        savePool();
        log.info("更新IP池配置");
        return ipPool;
    }

    @Override
    public IpStatistics getStatistics() {
        IpStatistics stats = new IpStatistics();
        List<IpAllocation> allocations = getAllAllocations();

        stats.setTotalAllocated(allocations.size());
        stats.setStaticCount((int) allocations.stream().filter(a -> "static".equals(a.getAllocationType())).count());
        stats.setDhcpCount((int) allocations.stream().filter(a -> "dhcp".equals(a.getAllocationType())).count());
        stats.setActiveCount((int) allocations.stream().filter(a -> "active".equals(a.getStatus())).count());
        stats.setInactiveCount((int) allocations.stream().filter(a -> "inactive".equals(a.getStatus())).count());

        IpPool pool = getIpPool();
        stats.setTotalAvailable(pool.getAvailableCount());
        stats.setAllocationRate(pool.getAllocationRate());

        return stats;
    }

    private void updatePoolAllocation() {
        if (ipPool != null) {
            ipPool.setAllocatedCount(allocationCache.size());
            savePool();
        }
    }
}
