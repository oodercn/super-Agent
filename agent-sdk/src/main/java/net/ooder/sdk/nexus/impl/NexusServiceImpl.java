package net.ooder.sdk.nexus.impl;

import net.ooder.sdk.nexus.*;
import net.ooder.sdk.nexus.model.*;
import net.ooder.sdk.southbound.protocol.*;
import net.ooder.sdk.southbound.protocol.model.*;
import net.ooder.sdk.southbound.protocol.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class NexusServiceImpl implements NexusService {
    
    private static final Logger log = LoggerFactory.getLogger(NexusServiceImpl.class);
    
    private final DiscoveryProtocol discoveryProtocol;
    private final RoleProtocol roleProtocol;
    private final LoginProtocol loginProtocol;
    private final CollaborationProtocol collaborationProtocol;
    
    private NexusConfig config;
    private NexusStatus status;
    private UserSession currentSession;
    private NexusState state;
    private final List<NexusListener> listeners;
    private final ExecutorService executor;
    
    public NexusServiceImpl() {
        this.discoveryProtocol = new DiscoveryProtocolImpl();
        this.roleProtocol = new RoleProtocolImpl();
        this.loginProtocol = new LoginProtocolImpl();
        this.collaborationProtocol = new CollaborationProtocolImpl();
        this.listeners = new CopyOnWriteArrayList<NexusListener>();
        this.executor = Executors.newCachedThreadPool();
        this.state = NexusState.STOPPED;
        log.info("NexusServiceImpl initialized");
    }
    
    @Override
    public CompletableFuture<NexusStatus> start(NexusConfig config) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Starting Nexus: nodeId={}", config.getNodeId());
            
            this.config = config;
            this.state = NexusState.STARTING;
            
            this.status = new NexusStatus();
            this.status.setNodeId(config.getNodeId());
            this.status.setNodeName(config.getNodeName());
            this.status.setState(NexusState.RUNNING);
            this.status.setOnline(true);
            this.status.setStartTime(System.currentTimeMillis());
            
            this.state = NexusState.RUNNING;
            notifyStateChanged(NexusState.STOPPED, NexusState.RUNNING);
            
            log.info("Nexus started: nodeId={}", config.getNodeId());
            return status;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> stop() {
        return CompletableFuture.runAsync(() -> {
            log.info("Stopping Nexus");
            
            NexusState oldState = this.state;
            this.state = NexusState.STOPPING;
            
            if (status != null) {
                status.setState(NexusState.STOPPED);
                status.setOnline(false);
            }
            
            this.state = NexusState.STOPPED;
            notifyStateChanged(oldState, NexusState.STOPPED);
            
            log.info("Nexus stopped");
        }, executor);
    }
    
    @Override
    public CompletableFuture<NexusStatus> getStatus() {
        return CompletableFuture.supplyAsync(() -> status, executor);
    }
    
    @Override
    public CompletableFuture<Void> login(LoginRequest request) {
        return CompletableFuture.runAsync(() -> {
            log.info("Logging in: username={}", request.getUsername());
            
            LoginResult result = loginProtocol.login(request).join();
            if (result.isSuccess()) {
                currentSession = new UserSession();
                currentSession.setSessionId(result.getSessionId());
                currentSession.setUserName(request.getUsername());
                currentSession.setCreatedAt(System.currentTimeMillis());
                
                notifyLoginSuccess(currentSession);
                log.info("Login successful: username={}", request.getUsername());
            } else {
                log.warn("Login failed: username={}", request.getUsername());
                throw new RuntimeException("Login failed: " + result.getErrorMessage());
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> logout() {
        return CompletableFuture.runAsync(() -> {
            log.info("Logging out");
            currentSession = null;
            notifyLogout();
            log.info("Logout successful");
        }, executor);
    }
    
    @Override
    public CompletableFuture<UserSession> getCurrentSession() {
        return CompletableFuture.supplyAsync(() -> currentSession, executor);
    }
    
    @Override
    public CompletableFuture<List<PeerInfo>> discoverPeers() {
        return CompletableFuture.supplyAsync(() -> {
            DiscoveryResult result = discoveryProtocol.discover(new DiscoveryRequest()).join();
            return result.getPeers();
        }, executor);
    }
    
    @Override
    public CompletableFuture<RoleDecision> getCurrentRole() {
        return CompletableFuture.supplyAsync(() -> {
            RoleContext context = new RoleContext();
            context.setAgentId(config != null ? config.getNodeId() : "unknown");
            return roleProtocol.decideRole(context).join();
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> joinSceneGroup(String groupId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Joining scene group: {}", groupId);
            JoinRequest request = new JoinRequest();
            request.setAgentId(config != null ? config.getNodeId() : "unknown");
            collaborationProtocol.joinSceneGroup(groupId, request).join();
            log.info("Joined scene group: {}", groupId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> leaveSceneGroup(String groupId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Leaving scene group: {}", groupId);
            collaborationProtocol.leaveSceneGroup(groupId).join();
            notifySceneGroupLeft(groupId);
            log.info("Left scene group: {}", groupId);
        }, executor);
    }
    
    @Override
    public CompletableFuture<List<SceneGroupInfo>> listSceneGroups() {
        return CompletableFuture.supplyAsync(() -> {
            List<SceneGroupInfo> groups = new ArrayList<SceneGroupInfo>();
            
            try {
                if (collaborationProtocol != null) {
                    List<SceneGroupInfo> protocolGroups = collaborationProtocol.listJoinedGroups().get(5, TimeUnit.SECONDS);
                    if (protocolGroups != null) {
                        groups.addAll(protocolGroups);
                    }
                }
            } catch (Exception e) {
                log.debug("Failed to get scene groups from collaboration protocol: {}", e.getMessage());
            }
            
            log.debug("Listed {} scene groups", groups.size());
            return groups;
        }, executor);
    }
    
    @Override
    public void addNexusListener(NexusListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeNexusListener(NexusListener listener) {
        listeners.remove(listener);
    }
    
    @Override
    public void shutdown() {
        log.info("Shutting down NexusService");
        stop().join();
        executor.shutdown();
        listeners.clear();
        log.info("NexusService shutdown complete");
    }
    
    private void notifyStateChanged(NexusState oldState, NexusState newState) {
        for (NexusListener listener : listeners) {
            try {
                listener.onStateChanged(oldState, newState);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyLoginSuccess(UserSession session) {
        for (NexusListener listener : listeners) {
            try {
                listener.onLoginSuccess(session);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyLogout() {
        for (NexusListener listener : listeners) {
            try {
                listener.onLogout();
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifySceneGroupLeft(String groupId) {
        for (NexusListener listener : listeners) {
            try {
                listener.onSceneGroupLeft(groupId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
}
