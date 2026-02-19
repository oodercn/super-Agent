package net.ooder.sdk.core.skill.impl;

import net.ooder.sdk.api.skill.*;
import net.ooder.sdk.api.scene.store.SkillStore;
import net.ooder.sdk.api.scene.store.SkillRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class SkillRegistryImpl implements SkillRegistry {
    
    private static final Logger log = LoggerFactory.getLogger(SkillRegistryImpl.class);
    
    private final Map<String, SkillService> services = new ConcurrentHashMap<>();
    private final Map<String, SkillDefinition> definitions = new ConcurrentHashMap<>();
    private final Map<String, Long> heartbeats = new ConcurrentHashMap<>();
    private final Map<String, Long> registerTimes = new ConcurrentHashMap<>();
    
    private final SkillStore skillStore;
    private final boolean persistenceEnabled;
    
    private volatile boolean running = false;
    private ScheduledExecutorService heartbeatChecker;
    private long heartbeatTimeout = 60000;
    
    public SkillRegistryImpl() {
        this(null, false);
    }
    
    public SkillRegistryImpl(SkillStore skillStore, boolean persistenceEnabled) {
        this.skillStore = skillStore;
        this.persistenceEnabled = persistenceEnabled;
    }
    
    public void setHeartbeatTimeout(long timeoutMs) {
        this.heartbeatTimeout = timeoutMs;
    }
    
    @Override
    public void start() {
        log.info("Starting SkillRegistry");
        running = true;
        
        heartbeatChecker = Executors.newSingleThreadScheduledExecutor();
        heartbeatChecker.scheduleAtFixedRate(
            this::checkHeartbeats,
            heartbeatTimeout,
            heartbeatTimeout / 2,
            TimeUnit.MILLISECONDS
        );
        
        if (persistenceEnabled && skillStore != null) {
            loadFromPersistence();
        }
        
        log.info("SkillRegistry started");
    }
    
    @Override
    public void stop() {
        log.info("Stopping SkillRegistry");
        running = false;
        
        if (heartbeatChecker != null) {
            heartbeatChecker.shutdown();
        }
        
        for (SkillService service : services.values()) {
            try {
                service.stop();
            } catch (Exception e) {
                log.error("Error stopping skill: {}", service.getSkillId(), e);
            }
        }
        
        services.clear();
        definitions.clear();
        heartbeats.clear();
        registerTimes.clear();
        
        log.info("SkillRegistry stopped");
    }
    
    @Override
    public CompletableFuture<String> register(SkillDefinition definition, SkillService service) {
        return CompletableFuture.supplyAsync(() -> {
            if (definition == null || service == null) {
                throw new IllegalArgumentException("Definition and service cannot be null");
            }
            
            String skillId = definition.getSkillId();
            log.info("Registering skill: {} type: {}", skillId, definition.getSkillType());
            
            definitions.put(skillId, definition);
            services.put(skillId, service);
            heartbeats.put(skillId, System.currentTimeMillis());
            registerTimes.put(skillId, System.currentTimeMillis());
            
            SkillContext context = SkillContext.create(
                definition.getSceneId(),
                definition.getGroupId(),
                skillId
            );
            context.setConfig(definition.getConfig());
            context.setAgentId(definition.getMetadata().get("agentId") != null 
                ? definition.getMetadata().get("agentId").toString() : null);
            
            try {
                service.initialize(context);
                service.start();
            } catch (Exception e) {
                log.error("Failed to initialize skill: {}", skillId, e);
                services.remove(skillId);
                definitions.remove(skillId);
                heartbeats.remove(skillId);
                registerTimes.remove(skillId);
                throw new RuntimeException("Failed to initialize skill: " + skillId, e);
            }
            
            if (persistenceEnabled && skillStore != null) {
                try {
                    SkillRegistration registration = createRegistration(definition, service);
                    skillStore.saveSkill(registration);
                } catch (Exception e) {
                    log.error("Failed to persist skill registration: {}", skillId, e);
                }
            }
            
            log.info("Skill registered: {}", skillId);
            return skillId;
        });
    }
    
    @Override
    public CompletableFuture<Void> unregister(String skillId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Unregistering skill: {}", skillId);
            
            SkillService service = services.remove(skillId);
            definitions.remove(skillId);
            heartbeats.remove(skillId);
            registerTimes.remove(skillId);
            
            if (service != null) {
                try {
                    service.stop();
                } catch (Exception e) {
                    log.error("Error stopping skill: {}", skillId, e);
                }
            }
            
            if (persistenceEnabled && skillStore != null) {
                try {
                    skillStore.deleteSkill(skillId);
                } catch (Exception e) {
                    log.error("Failed to delete skill persistence: {}", skillId, e);
                }
            }
            
            log.info("Skill unregistered: {}", skillId);
        });
    }
    
    @Override
    public SkillService getService(String skillId) {
        return services.get(skillId);
    }
    
    @Override
    public SkillDefinition getDefinition(String skillId) {
        return definitions.get(skillId);
    }
    
    @Override
    public SkillRegistration getRegistration(String skillId) {
        SkillDefinition definition = definitions.get(skillId);
        SkillService service = services.get(skillId);
        
        if (definition == null) {
            return null;
        }
        
        return createRegistration(definition, service);
    }
    
    @Override
    public List<SkillService> getServices(String sceneId, String groupId) {
        List<SkillService> result = new ArrayList<>();
        for (Map.Entry<String, SkillDefinition> entry : definitions.entrySet()) {
            SkillDefinition def = entry.getValue();
            if ((sceneId == null || sceneId.equals(def.getSceneId())) &&
                (groupId == null || groupId.equals(def.getGroupId()))) {
                SkillService service = services.get(entry.getKey());
                if (service != null) {
                    result.add(service);
                }
            }
        }
        return result;
    }
    
    @Override
    public List<SkillService> getServicesByType(String sceneId, String skillType) {
        List<SkillService> result = new ArrayList<>();
        for (Map.Entry<String, SkillDefinition> entry : definitions.entrySet()) {
            SkillDefinition def = entry.getValue();
            if ((sceneId == null || sceneId.equals(def.getSceneId())) &&
                (skillType == null || skillType.equals(def.getSkillType()))) {
                SkillService service = services.get(entry.getKey());
                if (service != null) {
                    result.add(service);
                }
            }
        }
        return result;
    }
    
    @Override
    public SkillService getServiceByType(String sceneId, String skillType) {
        for (Map.Entry<String, SkillDefinition> entry : definitions.entrySet()) {
            SkillDefinition def = entry.getValue();
            if ((sceneId == null || sceneId.equals(def.getSceneId())) &&
                skillType != null && skillType.equals(def.getSkillType())) {
                return services.get(entry.getKey());
            }
        }
        return null;
    }
    
    @Override
    public List<SkillDefinition> listDefinitions(String sceneId) {
        List<SkillDefinition> result = new ArrayList<>();
        for (SkillDefinition def : definitions.values()) {
            if (sceneId == null || sceneId.equals(def.getSceneId())) {
                result.add(def);
            }
        }
        return result;
    }
    
    @Override
    public List<SkillRegistration> listRegistrations(String sceneId, String groupId) {
        List<SkillRegistration> result = new ArrayList<>();
        for (Map.Entry<String, SkillDefinition> entry : definitions.entrySet()) {
            SkillDefinition def = entry.getValue();
            if ((sceneId == null || sceneId.equals(def.getSceneId())) &&
                (groupId == null || groupId.equals(def.getGroupId()))) {
                SkillService service = services.get(entry.getKey());
                result.add(createRegistration(def, service));
            }
        }
        return result;
    }
    
    @Override
    public Map<String, Object> getEndpoints(String skillId) {
        SkillService service = services.get(skillId);
        if (service != null) {
            Map<String, Object> info = service.getSkillInfo();
            @SuppressWarnings("unchecked")
            Map<String, Object> endpoints = (Map<String, Object>) info.get("endpoints");
            return endpoints != null ? endpoints : new HashMap<>();
        }
        return new HashMap<>();
    }
    
    @Override
    public void sendHeartbeat(String skillId) {
        heartbeats.put(skillId, System.currentTimeMillis());
        
        if (persistenceEnabled && skillStore != null) {
            try {
                skillStore.updateSkillHeartbeat(skillId, System.currentTimeMillis());
            } catch (Exception e) {
                log.debug("Failed to update skill heartbeat: {}", skillId);
            }
        }
    }
    
    @Override
    public boolean isAlive(String skillId) {
        return isAlive(skillId, heartbeatTimeout);
    }
    
    @Override
    public boolean isAlive(String skillId, long timeoutMs) {
        Long lastHeartbeat = heartbeats.get(skillId);
        if (lastHeartbeat == null) {
            return false;
        }
        return (System.currentTimeMillis() - lastHeartbeat) < timeoutMs;
    }
    
    private void checkHeartbeats() {
        long now = System.currentTimeMillis();
        List<String> deadSkills = new ArrayList<>();
        
        for (Map.Entry<String, Long> entry : heartbeats.entrySet()) {
            if ((now - entry.getValue()) > heartbeatTimeout) {
                deadSkills.add(entry.getKey());
            }
        }
        
        for (String skillId : deadSkills) {
            log.warn("Skill heartbeat timeout, marking as dead: {}", skillId);
            unregister(skillId);
        }
    }
    
    private void loadFromPersistence() {
        if (skillStore == null) {
            return;
        }
        
        log.info("Loading skills from persistence");
    }
    
    private SkillRegistration createRegistration(SkillDefinition definition, SkillService service) {
        SkillRegistration registration = new SkillRegistration();
        registration.setSkillId(definition.getSkillId());
        registration.setSceneId(definition.getSceneId());
        registration.setGroupId(definition.getGroupId());
        registration.setSkillType(definition.getSkillType());
        registration.setStatus(service != null && service.isRunning() ? "running" : "stopped");
        
        Long registerTime = registerTimes.get(definition.getSkillId());
        if (registerTime != null) {
            registration.setRegisterTime(registerTime);
        }
        
        Long lastHeartbeat = heartbeats.get(definition.getSkillId());
        if (lastHeartbeat != null) {
            registration.setLastHeartbeat(lastHeartbeat);
        }
        
        if (service != null) {
            Map<String, Object> info = service.getSkillInfo();
            @SuppressWarnings("unchecked")
            Map<String, Object> endpoints = (Map<String, Object>) info.get("endpoints");
            if (endpoints != null) {
                registration.setEndpoints(endpoints);
            }
        }
        
        return registration;
    }
    
    public int getSkillCount() {
        return services.size();
    }
    
    public boolean isRunning() {
        return running;
    }
}
