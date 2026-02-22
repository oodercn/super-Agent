package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.AdminRemoteService;
import net.ooder.sdk.api.storage.StorageService;
import net.ooder.sdk.api.storage.TypeReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminRemoteServiceImpl implements AdminRemoteService {

    private static final Logger log = LoggerFactory.getLogger(AdminRemoteServiceImpl.class);
    private static final String AGENT_KEY = "admin/remote-agents";
    private static final String LOG_KEY = "admin/remote-logs";
    private static final String SKILL_KEY = "admin/remote-skills";
    private static final String HOSTING_KEY = "admin/remote-hostings";
    private static final String MONITORING_KEY = "admin/remote-monitorings";

    private final StorageService storageService;

    @Autowired
    public AdminRemoteServiceImpl(StorageService storageService) {
        this.storageService = storageService;
        log.info("AdminRemoteServiceImpl initialized with StorageService (SDK 0.7.1)");
    }

    @Override
    public List<Map<String, Object>> getAllRemoteAgents() {
        log.info("Getting all remote agents");
        Optional<List<Map<String, Object>>> agentsOpt = storageService.load(AGENT_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        return agentsOpt.orElse(new ArrayList<Map<String, Object>>());
    }

    @Override
    public Map<String, Object> getRemoteAgentById(String agentId) {
        log.info("Getting remote agent by id: {}", agentId);
        List<Map<String, Object>> agents = getAllRemoteAgents();
        for (Map<String, Object> agent : agents) {
            if (agentId.equals(agent.get("id"))) {
                return agent;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> registerRemoteAgent(Map<String, Object> agentData) {
        log.info("Registering remote agent: {}", agentData.get("name"));
        List<Map<String, Object>> agents = getAllRemoteAgents();
        String id = "agent-" + System.currentTimeMillis();
        Map<String, Object> agent = new HashMap<String, Object>(agentData);
        agent.put("id", id);
        agent.put("status", "active");
        agent.put("registerTime", System.currentTimeMillis());
        agent.put("lastHeartbeat", System.currentTimeMillis());
        agents.add(agent);
        storageService.save(AGENT_KEY, agents);
        return agent;
    }

    @Override
    public Map<String, Object> updateRemoteAgent(String agentId, Map<String, Object> agentData) {
        log.info("Updating remote agent: {}", agentId);
        List<Map<String, Object>> agents = getAllRemoteAgents();
        for (int i = 0; i < agents.size(); i++) {
            if (agentId.equals(agents.get(i).get("id"))) {
                Map<String, Object> existing = agents.get(i);
                existing.putAll(agentData);
                existing.put("id", agentId);
                existing.put("updateTime", System.currentTimeMillis());
                agents.set(i, existing);
                storageService.save(AGENT_KEY, agents);
                return existing;
            }
        }
        return null;
    }

    @Override
    public boolean unregisterRemoteAgent(String agentId) {
        log.info("Unregistering remote agent: {}", agentId);
        List<Map<String, Object>> agents = getAllRemoteAgents();
        boolean removed = agents.removeIf(agent -> agentId.equals(agent.get("id")));
        if (removed) {
            storageService.save(AGENT_KEY, agents);
        }
        return removed;
    }

    @Override
    public boolean testConnection(String agentId) {
        log.info("Testing connection for agent: {}", agentId);
        Map<String, Object> agent = getRemoteAgentById(agentId);
        if (agent != null) {
            agent.put("lastTestTime", System.currentTimeMillis());
            agent.put("connectionStatus", "connected");
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getConnectionLogs(String agentId, int limit) {
        log.info("Getting connection logs for agent: {}", agentId);
        String key = LOG_KEY + "/" + agentId;
        Optional<List<Map<String, Object>>> logsOpt = storageService.load(key, 
            new TypeReference<List<Map<String, Object>>>() {});
        List<Map<String, Object>> logs = logsOpt.orElse(new ArrayList<Map<String, Object>>());
        if (logs.size() > limit) {
            return logs.subList(0, limit);
        }
        return logs;
    }

    @Override
    public Map<String, Object> getRemoteStatistics() {
        log.info("Getting remote statistics");
        Map<String, Object> stats = new HashMap<String, Object>();
        List<Map<String, Object>> agents = getAllRemoteAgents();
        stats.put("totalAgents", agents.size());
        
        int active = 0;
        int inactive = 0;
        for (Map<String, Object> agent : agents) {
            String status = (String) agent.get("status");
            if ("active".equals(status)) {
                active++;
            } else {
                inactive++;
            }
        }
        stats.put("activeAgents", active);
        stats.put("inactiveAgents", inactive);
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getAllRemoteSkills() {
        log.info("Getting all remote skills");
        Optional<List<Map<String, Object>>> skillsOpt = storageService.load(SKILL_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        List<Map<String, Object>> skills = skillsOpt.orElse(new ArrayList<Map<String, Object>>());
        if (skills.isEmpty()) {
            skills = initDefaultRemoteSkills();
            storageService.save(SKILL_KEY, skills);
        }
        return skills;
    }

    @Override
    public Map<String, Object> getRemoteSkillById(String skillId) {
        log.info("Getting remote skill by id: {}", skillId);
        List<Map<String, Object>> skills = getAllRemoteSkills();
        for (Map<String, Object> skill : skills) {
            if (skillId.equals(skill.get("id"))) {
                return skill;
            }
        }
        return null;
    }

    @Override
    public boolean syncRemoteSkill(String skillId) {
        log.info("Syncing remote skill: {}", skillId);
        List<Map<String, Object>> skills = getAllRemoteSkills();
        for (Map<String, Object> skill : skills) {
            if (skillId.equals(skill.get("id"))) {
                skill.put("lastSync", System.currentTimeMillis());
                skill.put("status", "synced");
                storageService.save(SKILL_KEY, skills);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeRemoteSkill(String skillId) {
        log.info("Removing remote skill: {}", skillId);
        List<Map<String, Object>> skills = getAllRemoteSkills();
        boolean removed = skills.removeIf(skill -> skillId.equals(skill.get("id")));
        if (removed) {
            storageService.save(SKILL_KEY, skills);
        }
        return removed;
    }

    @Override
    public List<Map<String, Object>> getAllHostings() {
        log.info("Getting all hostings");
        Optional<List<Map<String, Object>>> hostingsOpt = storageService.load(HOSTING_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        List<Map<String, Object>> hostings = hostingsOpt.orElse(new ArrayList<Map<String, Object>>());
        if (hostings.isEmpty()) {
            hostings = initDefaultHostings();
            storageService.save(HOSTING_KEY, hostings);
        }
        return hostings;
    }

    @Override
    public Map<String, Object> getHostingById(String hostingId) {
        log.info("Getting hosting by id: {}", hostingId);
        List<Map<String, Object>> hostings = getAllHostings();
        for (Map<String, Object> hosting : hostings) {
            if (hostingId.equals(hosting.get("id"))) {
                return hosting;
            }
        }
        return null;
    }

    @Override
    public boolean toggleHosting(String hostingId) {
        log.info("Toggling hosting: {}", hostingId);
        List<Map<String, Object>> hostings = getAllHostings();
        for (Map<String, Object> hosting : hostings) {
            if (hostingId.equals(hosting.get("id"))) {
                String status = (String) hosting.get("status");
                hosting.put("status", "active".equals(status) ? "inactive" : "active");
                hosting.put("updateTime", System.currentTimeMillis());
                storageService.save(HOSTING_KEY, hostings);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeHosting(String hostingId) {
        log.info("Removing hosting: {}", hostingId);
        List<Map<String, Object>> hostings = getAllHostings();
        boolean removed = hostings.removeIf(hosting -> hostingId.equals(hosting.get("id")));
        if (removed) {
            storageService.save(HOSTING_KEY, hostings);
        }
        return removed;
    }

    @Override
    public List<Map<String, Object>> getAllMonitorings() {
        log.info("Getting all monitorings");
        Optional<List<Map<String, Object>>> monitoringsOpt = storageService.load(MONITORING_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        List<Map<String, Object>> monitorings = monitoringsOpt.orElse(new ArrayList<Map<String, Object>>());
        if (monitorings.isEmpty()) {
            monitorings = initDefaultMonitorings();
            storageService.save(MONITORING_KEY, monitorings);
        }
        return monitorings;
    }

    @Override
    public Map<String, Object> getMonitoringById(String monitoringId) {
        log.info("Getting monitoring by id: {}", monitoringId);
        List<Map<String, Object>> monitorings = getAllMonitorings();
        for (Map<String, Object> monitoring : monitorings) {
            if (monitoringId.equals(monitoring.get("id"))) {
                return monitoring;
            }
        }
        return null;
    }

    @Override
    public boolean checkMonitoring(String monitoringId) {
        log.info("Checking monitoring: {}", monitoringId);
        List<Map<String, Object>> monitorings = getAllMonitorings();
        for (Map<String, Object> monitoring : monitorings) {
            if (monitoringId.equals(monitoring.get("id"))) {
                monitoring.put("lastChecked", System.currentTimeMillis());
                monitoring.put("status", "online");
                monitoring.put("responseTime", new Random().nextInt(100) + 50);
                storageService.save(MONITORING_KEY, monitorings);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeMonitoring(String monitoringId) {
        log.info("Removing monitoring: {}", monitoringId);
        List<Map<String, Object>> monitorings = getAllMonitorings();
        boolean removed = monitorings.removeIf(monitoring -> monitoringId.equals(monitoring.get("id")));
        if (removed) {
            storageService.save(MONITORING_KEY, monitorings);
        }
        return removed;
    }

    private List<Map<String, Object>> initDefaultRemoteSkills() {
        List<Map<String, Object>> skills = new ArrayList<Map<String, Object>>();
        
        Map<String, Object> skill1 = new HashMap<String, Object>();
        skill1.put("id", "skill-001");
        skill1.put("name", "文本翻译助手");
        skill1.put("description", "支持多语言翻译的AI技能");
        skill1.put("category", "text-processing");
        skill1.put("author", "ooder Team");
        skill1.put("remoteUrl", "https://remote.ooder.cn/skills/translate");
        skill1.put("status", "active");
        skill1.put("lastSync", System.currentTimeMillis() - 3600000);
        skills.add(skill1);
        
        Map<String, Object> skill2 = new HashMap<String, Object>();
        skill2.put("id", "skill-002");
        skill2.put("name", "代码生成器");
        skill2.put("description", "根据需求自动生成代码");
        skill2.put("category", "development");
        skill2.put("author", "ooder Team");
        skill2.put("remoteUrl", "https://remote.ooder.cn/skills/codegen");
        skill2.put("status", "active");
        skill2.put("lastSync", System.currentTimeMillis() - 7200000);
        skills.add(skill2);
        
        return skills;
    }

    private List<Map<String, Object>> initDefaultHostings() {
        List<Map<String, Object>> hostings = new ArrayList<Map<String, Object>>();
        
        Map<String, Object> hosting1 = new HashMap<String, Object>();
        hosting1.put("id", "hosting-001");
        hosting1.put("skillName", "文本翻译助手");
        hosting1.put("hostingUrl", "https://host.ooder.cn/skills/translate");
        hosting1.put("status", "active");
        hosting1.put("provider", "ooder Cloud");
        hosting1.put("region", "cn-east");
        hosting1.put("createdAt", System.currentTimeMillis() - 86400000);
        hostings.add(hosting1);
        
        return hostings;
    }

    private List<Map<String, Object>> initDefaultMonitorings() {
        List<Map<String, Object>> monitorings = new ArrayList<Map<String, Object>>();
        
        Map<String, Object> monitoring1 = new HashMap<String, Object>();
        monitoring1.put("id", "monitoring-001");
        monitoring1.put("skillName", "文本翻译助手");
        monitoring1.put("monitoringUrl", "https://host.ooder.cn/skills/translate/health");
        monitoring1.put("status", "online");
        monitoring1.put("type", "health-check");
        monitoring1.put("lastChecked", System.currentTimeMillis() - 300000);
        monitoring1.put("responseTime", 85);
        monitorings.add(monitoring1);
        
        return monitorings;
    }
}
