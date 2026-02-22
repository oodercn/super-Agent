package net.ooder.nexus.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.ooder.nexus.domain.end.model.EndAgent;
import net.ooder.nexus.service.EndAgentService;
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
 * 终端代理服务实现类
 */
@Service
public class EndAgentServiceImpl implements EndAgentService {

    private static final Logger log = LoggerFactory.getLogger(EndAgentServiceImpl.class);
    private static final String DATA_DIR = "./storage/agents";
    private static final String AGENTS_FILE = "end-agents.json";

    private final Map<String, EndAgent> agentCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Path storagePath;

    public EndAgentServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.storagePath = Paths.get(DATA_DIR, AGENTS_FILE);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            loadAgents();
            
            // 如果没有数据，初始化默认数据
            if (agentCache.isEmpty()) {
                initDefaultData();
            }
            
            log.info("终端代理服务初始化完成，共加载 {} 个终端", agentCache.size());
        } catch (IOException e) {
            log.error("初始化终端代理服务失败", e);
        }
    }

    @Override
    public void initDefaultData() {
        // 智能灯泡
        EndAgent agent1 = new EndAgent();
        agent1.setId("endagent-001");
        agent1.setName("智能灯泡1");
        agent1.setType("light");
        agent1.setIp("192.168.1.100");
        agent1.setMac("AA:BB:CC:DD:EE:FF");
        agent1.setStatus("active");
        agent1.setProperties(new HashMap<String, Object>() {{
            put("brightness", 80);
            put("color", "#FFFFFF");
        }});

        // 智能插座
        EndAgent agent2 = new EndAgent();
        agent2.setId("endagent-002");
        agent2.setName("智能插座1");
        agent2.setType("socket");
        agent2.setIp("192.168.1.101");
        agent2.setMac("AA:BB:CC:DD:EE:GG");
        agent2.setStatus("active");
        agent2.setProperties(new HashMap<String, Object>() {{
            put("power", "on");
            put("voltage", 220);
        }});

        // 摄像头
        EndAgent agent3 = new EndAgent();
        agent3.setId("endagent-003");
        agent3.setName("摄像头1");
        agent3.setType("camera");
        agent3.setIp("192.168.1.102");
        agent3.setMac("AA:BB:CC:DD:EE:HH");
        agent3.setStatus("inactive");
        agent3.setProperties(new HashMap<String, Object>() {{
            put("resolution", "1080p");
            put("storage", "128GB");
        }});

        // 智能音箱
        EndAgent agent4 = new EndAgent();
        agent4.setId("endagent-004");
        agent4.setName("智能音箱1");
        agent4.setType("speaker");
        agent4.setIp("192.168.1.103");
        agent4.setMac("AA:BB:CC:DD:EE:II");
        agent4.setStatus("active");
        agent4.setProperties(new HashMap<String, Object>() {{
            put("volume", 50);
        }});

        // 温控器
        EndAgent agent5 = new EndAgent();
        agent5.setId("endagent-005");
        agent5.setName("温控器1");
        agent5.setType("thermostat");
        agent5.setIp("192.168.1.104");
        agent5.setMac("AA:BB:CC:DD:EE:JJ");
        agent5.setStatus("active");
        agent5.setProperties(new HashMap<String, Object>() {{
            put("temperature", 25);
            put("humidity", 45);
        }});

        agentCache.put(agent1.getId(), agent1);
        agentCache.put(agent2.getId(), agent2);
        agentCache.put(agent3.getId(), agent3);
        agentCache.put(agent4.getId(), agent4);
        agentCache.put(agent5.getId(), agent5);
        
        saveAgents();
        log.info("初始化默认终端代理数据完成，共 {} 个终端", agentCache.size());
    }

    private void loadAgents() {
        if (!Files.exists(storagePath)) {
            return;
        }

        try {
            String json = new String(Files.readAllBytes(storagePath), StandardCharsets.UTF_8);
            List<EndAgent> agents = objectMapper.readValue(json, new TypeReference<List<EndAgent>>() {});
            agents.forEach(agent -> agentCache.put(agent.getId(), agent));
        } catch (IOException e) {
            log.error("加载终端代理数据失败", e);
        }
    }

    private void saveAgents() {
        try {
            List<EndAgent> agents = new ArrayList<>(agentCache.values());
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(agents);
            Files.write(storagePath, json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.error("保存终端代理数据失败", e);
        }
    }

    @Override
    public EndAgent createAgent(EndAgent agent) {
        if (agent.getId() == null || agent.getId().isEmpty()) {
            agent.setId(UUID.randomUUID().toString());
        }

        agent.setCreateTime(LocalDateTime.now());
        agent.setLastUpdate(LocalDateTime.now());

        agentCache.put(agent.getId(), agent);
        saveAgents();

        log.info("创建终端代理: {}", agent.getName());
        return agent;
    }

    @Override
    public EndAgent updateAgent(String id, EndAgent agent) {
        EndAgent existingAgent = agentCache.get(id);
        if (existingAgent == null) {
            log.warn("终端代理不存在: {}", id);
            return null;
        }

        agent.setId(id);
        agent.setCreateTime(existingAgent.getCreateTime());
        agent.setLastUpdate(LocalDateTime.now());

        agentCache.put(id, agent);
        saveAgents();

        log.info("更新终端代理: {}", agent.getName());
        return agent;
    }

    @Override
    public boolean deleteAgent(String id) {
        EndAgent removed = agentCache.remove(id);
        if (removed != null) {
            saveAgents();
            log.info("删除终端代理: {}", removed.getName());
            return true;
        }
        return false;
    }

    @Override
    public EndAgent getAgentById(String id) {
        return agentCache.get(id);
    }

    @Override
    public List<EndAgent> getAllAgents() {
        return new ArrayList<>(agentCache.values());
    }

    @Override
    public List<EndAgent> getAgentsByType(String type) {
        return agentCache.values().stream()
                .filter(agent -> type.equals(agent.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EndAgent> getAgentsByStatus(String status) {
        return agentCache.values().stream()
                .filter(agent -> status.equals(agent.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public AgentStats getAgentStats() {
        AgentStats stats = new AgentStats();
        stats.setTotalCount(agentCache.size());
        stats.setActiveCount((int) agentCache.values().stream()
                .filter(a -> "active".equals(a.getStatus())).count());
        stats.setInactiveCount((int) agentCache.values().stream()
                .filter(a -> "inactive".equals(a.getStatus())).count());
        return stats;
    }

    @Override
    public EndAgent updateAgentProperties(String id, Map<String, Object> properties) {
        EndAgent agent = agentCache.get(id);
        if (agent == null) {
            log.warn("终端代理不存在: {}", id);
            return null;
        }

        agent.setProperties(properties);
        agent.setLastUpdate(LocalDateTime.now());
        saveAgents();

        log.info("更新终端代理属性: {}", agent.getName());
        return agent;
    }
}
