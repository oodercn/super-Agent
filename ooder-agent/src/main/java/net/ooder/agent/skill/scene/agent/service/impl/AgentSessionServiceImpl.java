package net.ooder.mvp.skill.scene.agent.service.impl;

import net.ooder.mvp.skill.scene.agent.dto.AgentSessionDTO;
import net.ooder.mvp.skill.scene.agent.dto.AgentRegistrationDTO;
import net.ooder.mvp.skill.scene.agent.dto.AgentStatus;
import net.ooder.mvp.skill.scene.agent.service.AgentSessionService;
import net.ooder.mvp.skill.scene.dto.scene.SceneParticipantDTO;
import net.ooder.mvp.skill.scene.dto.scene.ParticipantType;
import net.ooder.mvp.skill.scene.service.SceneGroupService;
import net.ooder.skill.common.storage.JsonStorageService;
import net.ooder.mvp.skill.scene.agent.config.AgentHeartbeatConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AgentSessionServiceImpl implements AgentSessionService {

    private static final Logger log = LoggerFactory.getLogger(AgentSessionServiceImpl.class);
    
    private static final String STORAGE_KEY_SESSIONS = "agent-sessions";
    private static final String STORAGE_KEY_SECRETS = "agent-secrets";
    private static final int DEFAULT_SESSION_TIMEOUT = 86400;

    @Value("${agent.session.timeout:86400}")
    private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;

    @Autowired
    private JsonStorageService storage;

    @Autowired(required = false)
    private SceneGroupService sceneGroupService;

    private final Map<String, AgentSessionDTO> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> sessionTokens = new ConcurrentHashMap<>();
    private final Map<String, String> secrets = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        loadFromStorage();
        log.info("[AgentSession] Initialized with {} sessions, timeout: {}s", 
            sessions.size(), sessionTimeout);
    }

    private void loadFromStorage() {
        try {
            Map<String, AgentSessionDTO> storedSessions = storage.getAll(STORAGE_KEY_SESSIONS);
            if (storedSessions != null) {
                for (Map.Entry<String, AgentSessionDTO> entry : storedSessions.entrySet()) {
                    AgentSessionDTO session = entry.getValue();
                    if (!session.isExpired()) {
                        sessions.put(entry.getKey(), session);
                        if (session.getSessionToken() != null) {
                            sessionTokens.put(session.getSessionToken(), session.getAgentId());
                        }
                    }
                }
                log.info("[AgentSession] Loaded {} valid sessions from storage", sessions.size());
            }

            Map<String, String> storedSecrets = storage.getAll(STORAGE_KEY_SECRETS);
            if (storedSecrets != null) {
                secrets.putAll(storedSecrets);
                log.info("[AgentSession] Loaded {} agent secrets", secrets.size());
            }
        } catch (Exception e) {
            log.warn("[AgentSession] Failed to load from storage: {}", e.getMessage());
        }
    }

    @Override
    public AgentSessionDTO register(AgentRegistrationDTO registration) {
        String agentId = registration.getAgentId();
        if (agentId == null || agentId.isEmpty()) {
            agentId = "agent-" + UUID.randomUUID().toString().substring(0, 8);
        }

        if (secrets.containsKey(agentId) && registration.getSecretKey() != null) {
            log.warn("[AgentSession] Agent {} already registered, updating secret", agentId);
        }

        if (registration.getSecretKey() != null && !registration.getSecretKey().isEmpty()) {
            secrets.put(agentId, registration.getSecretKey());
            storage.put(STORAGE_KEY_SECRETS, agentId, registration.getSecretKey());
        }

        AgentSessionDTO session = new AgentSessionDTO();
        session.setAgentId(agentId);
        session.setAgentName(registration.getAgentName() != null ? registration.getAgentName() : agentId);
        session.setAgentType(registration.getAgentType() != null ? registration.getAgentType() : "AGENT");
        session.setIpAddress(registration.getIpAddress());
        session.setPort(registration.getPort());
        session.setSceneGroupId(registration.getSceneGroupId());
        session.setRole(registration.getRole());
        session.setStatus(AgentStatus.IDLE.name());
        session.setLoginTime(System.currentTimeMillis());
        session.setLastHeartbeat(System.currentTimeMillis());
        session.setExpireTime(System.currentTimeMillis() + sessionTimeout * 1000L);

        String token = generateToken(agentId);
        session.setSessionToken(token);

        sessions.put(agentId, session);
        sessionTokens.put(token, agentId);
        persistSession(session);

        log.info("[AgentSession] Agent registered: {} ({})", agentId, session.getAgentName());
        return session;
    }

    @Override
    public AgentSessionDTO login(String agentId, String secretKey) {
        String storedSecret = secrets.get(agentId);
        if (storedSecret == null) {
            log.warn("[AgentSession] Agent {} not registered", agentId);
            return null;
        }

        if (!storedSecret.equals(secretKey)) {
            log.warn("[AgentSession] Invalid secret for agent {}", agentId);
            return null;
        }

        AgentSessionDTO session = sessions.get(agentId);
        if (session == null) {
            session = new AgentSessionDTO();
            session.setAgentId(agentId);
            session.setAgentName(agentId);
            session.setAgentType("AGENT");
        }

        String token = generateToken(agentId);
        session.setSessionToken(token);
        session.setStatus(AgentStatus.ONLINE.name());
        session.setLoginTime(System.currentTimeMillis());
        session.setLastHeartbeat(System.currentTimeMillis());
        session.setExpireTime(System.currentTimeMillis() + sessionTimeout * 1000L);

        sessions.put(agentId, session);
        sessionTokens.put(token, agentId);
        persistSession(session);

        log.info("[AgentSession] Agent logged in: {}", agentId);
        return session;
    }

    @Override
    public void logout(String agentId) {
        AgentSessionDTO session = sessions.remove(agentId);
        if (session != null && session.getSessionToken() != null) {
            sessionTokens.remove(session.getSessionToken());
        }
        storage.remove(STORAGE_KEY_SESSIONS, agentId);
        log.info("[AgentSession] Agent logged out: {}", agentId);
    }

    @Override
    public AgentSessionDTO getSession(String agentId) {
        AgentSessionDTO session = sessions.get(agentId);
        if (session != null && session.isExpired()) {
            sessions.remove(agentId);
            sessionTokens.remove(session.getSessionToken());
            return null;
        }
        return session;
    }

    @Override
    public AgentSessionDTO getSessionByToken(String sessionToken) {
        if (sessionToken == null) {
            return null;
        }
        String agentId = sessionTokens.get(sessionToken);
        if (agentId == null) {
            return null;
        }
        return getSession(agentId);
    }

    @Override
    public boolean isValid(String agentId) {
        AgentSessionDTO session = getSession(agentId);
        return session != null && !session.isExpired();
    }

    @Override
    public boolean isValidToken(String sessionToken) {
        AgentSessionDTO session = getSessionByToken(sessionToken);
        return session != null && !session.isExpired();
    }

    @Override
    public void heartbeat(String agentId) {
        AgentSessionDTO session = sessions.get(agentId);
        if (session != null) {
            session.setLastHeartbeat(System.currentTimeMillis());
            session.setExpireTime(System.currentTimeMillis() + sessionTimeout * 1000L);
            if (AgentStatus.OFFLINE.name().equals(session.getStatus())) {
                session.setStatus(AgentStatus.ONLINE.name());
            }
            persistSession(session);
            log.debug("[AgentSession] Heartbeat received from {}", agentId);
        }
    }

    @Override
    public void updateStatus(String agentId, String status) {
        AgentSessionDTO session = sessions.get(agentId);
        if (session != null) {
            session.setStatus(status);
            persistSession(session);
            log.info("[AgentSession] Agent {} status updated to {}", agentId, status);
        }
    }

    @Override
    public List<AgentSessionDTO> getActiveSessions() {
        List<AgentSessionDTO> activeList = new ArrayList<>();
        Iterator<Map.Entry<String, AgentSessionDTO>> it = sessions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, AgentSessionDTO> entry = it.next();
            AgentSessionDTO session = entry.getValue();
            if (session.isExpired()) {
                it.remove();
                sessionTokens.remove(session.getSessionToken());
            } else {
                activeList.add(session);
            }
        }
        return activeList;
    }

    @Override
    public List<AgentSessionDTO> getSessionsByScene(String sceneGroupId) {
        List<AgentSessionDTO> result = new ArrayList<>();
        for (AgentSessionDTO session : getActiveSessions()) {
            if (sceneGroupId.equals(session.getSceneGroupId())) {
                result.add(session);
            }
        }
        return result;
    }

    @Override
    public int cleanupExpiredSessions() {
        int cleaned = 0;
        Iterator<Map.Entry<String, AgentSessionDTO>> it = sessions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, AgentSessionDTO> entry = it.next();
            if (entry.getValue().isExpired()) {
                sessionTokens.remove(entry.getValue().getSessionToken());
                storage.remove(STORAGE_KEY_SESSIONS, entry.getKey());
                it.remove();
                cleaned++;
            }
        }
        if (cleaned > 0) {
            log.info("[AgentSession] Cleaned up {} expired sessions", cleaned);
        }
        return cleaned;
    }

    @Override
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    @Override
    public void setSessionTimeout(int timeoutSeconds) {
        this.sessionTimeout = timeoutSeconds;
    }
    
    @Override
    public void updateSession(AgentSessionDTO session) {
        if (session != null && session.getAgentId() != null) {
            sessions.put(session.getAgentId(), session);
            persistSession(session);
        }
    }

    private String generateToken(String agentId) {
        return "tok-" + agentId + "-" + UUID.randomUUID().toString().substring(0, 16);
    }

    private void persistSession(AgentSessionDTO session) {
        try {
            storage.put(STORAGE_KEY_SESSIONS, session.getAgentId(), session);
        } catch (Exception e) {
            log.error("[AgentSession] Failed to persist session: {}", e.getMessage());
        }
    }
}
