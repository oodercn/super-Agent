package net.ooder.sdk.southbound.protocol.impl;

import net.ooder.sdk.southbound.protocol.*;
import net.ooder.sdk.southbound.protocol.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class RoleProtocolImpl implements RoleProtocol {
    
    private static final Logger log = LoggerFactory.getLogger(RoleProtocolImpl.class);
    
    private final Map<String, RoleInfo> registeredRoles;
    private final List<RoleListener> listeners;
    private final ExecutorService executor;
    private final AtomicReference<RoleDecision> currentDecision;
    
    public RoleProtocolImpl() {
        this.registeredRoles = new ConcurrentHashMap<String, RoleInfo>();
        this.listeners = new CopyOnWriteArrayList<RoleListener>();
        this.executor = Executors.newCachedThreadPool();
        this.currentDecision = new AtomicReference<RoleDecision>();
        log.info("RoleProtocolImpl initialized");
    }
    
    @Override
    public CompletableFuture<RoleDecision> decideRole(RoleContext context) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Deciding role for agent: {}", context.getAgentId());
            
            RoleDecision decision = new RoleDecision();
            decision.setAgentId(context.getAgentId());
            decision.setContext(context);
            decision.setDecidedTime(System.currentTimeMillis());
            
            if (context.isHasMcp()) {
                decision.setDecidedRole(RoleType.ROUTE_AGENT);
                decision.setReason("MCP detected, registering as RouteAgent");
            } else if (context.isHasNetwork()) {
                decision.setDecidedRole(RoleType.MCP_AGENT);
                decision.setReason("No MCP detected, upgrading to McpAgent");
            } else {
                decision.setDecidedRole(RoleType.END_AGENT);
                decision.setReason("No network, running as EndAgent");
            }
            
            currentDecision.set(decision);
            log.info("Role decided: {} for agent {}", decision.getDecidedRole(), context.getAgentId());
            return decision;
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> registerRole(RoleRegistration registration) {
        return CompletableFuture.runAsync(() -> {
            log.info("Registering role: {} for agent {}", registration.getRoleType(), registration.getAgentId());
            
            RoleInfo roleInfo = new RoleInfo();
            roleInfo.setAgentId(registration.getAgentId());
            roleInfo.setRoleType(registration.getRoleType());
            roleInfo.setMcpId(registration.getMcpId());
            roleInfo.setDomainId(registration.getDomainId());
            roleInfo.setStatus(RoleStatus.REGISTERED);
            roleInfo.setRegisteredTime(System.currentTimeMillis());
            roleInfo.setLastHeartbeat(System.currentTimeMillis());
            
            registeredRoles.put(registration.getAgentId(), roleInfo);
            notifyRoleRegistered(roleInfo);
            
            log.info("Role registered: {} for agent {}", registration.getRoleType(), registration.getAgentId());
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> unregisterRole(String agentId) {
        return CompletableFuture.runAsync(() -> {
            log.info("Unregistering role for agent: {}", agentId);
            
            RoleInfo removed = registeredRoles.remove(agentId);
            if (removed != null) {
                notifyRoleUnregistered(agentId);
                log.info("Role unregistered for agent: {}", agentId);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<RoleInfo> getRoleInfo(String agentId) {
        return CompletableFuture.supplyAsync(() -> registeredRoles.get(agentId), executor);
    }
    
    @Override
    public CompletableFuture<Void> upgradeRole(String agentId, RoleType newRole) {
        return CompletableFuture.runAsync(() -> {
            log.info("Upgrading role for agent {} to {}", agentId, newRole);
            
            RoleInfo roleInfo = registeredRoles.get(agentId);
            if (roleInfo != null) {
                RoleType oldRole = roleInfo.getRoleType();
                roleInfo.setRoleType(newRole);
                notifyRoleChanged(agentId, oldRole, newRole);
                log.info("Role upgraded for agent {} to {}", agentId, newRole);
            }
        }, executor);
    }
    
    @Override
    public CompletableFuture<Void> downgradeRole(String agentId, RoleType newRole) {
        return CompletableFuture.runAsync(() -> {
            log.info("Downgrading role for agent {} to {}", agentId, newRole);
            
            RoleInfo roleInfo = registeredRoles.get(agentId);
            if (roleInfo != null) {
                RoleType oldRole = roleInfo.getRoleType();
                roleInfo.setRoleType(newRole);
                notifyRoleChanged(agentId, oldRole, newRole);
                log.info("Role downgraded for agent {} to {}", agentId, newRole);
            }
        }, executor);
    }
    
    @Override
    public void addRoleListener(RoleListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeRoleListener(RoleListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyRoleChanged(String agentId, RoleType oldRole, RoleType newRole) {
        for (RoleListener listener : listeners) {
            try {
                listener.onRoleChanged(agentId, oldRole, newRole);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyRoleRegistered(RoleInfo roleInfo) {
        for (RoleListener listener : listeners) {
            try {
                listener.onRoleRegistered(roleInfo);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    private void notifyRoleUnregistered(String agentId) {
        for (RoleListener listener : listeners) {
            try {
                listener.onRoleUnregistered(agentId);
            } catch (Exception e) {
                log.warn("Listener notification failed", e);
            }
        }
    }
    
    public void shutdown() {
        log.info("Shutting down RoleProtocol");
        executor.shutdown();
        registeredRoles.clear();
        currentDecision.set(null);
        log.info("RoleProtocol shutdown complete");
    }
    
    public RoleDecision getCurrentDecision() {
        return currentDecision.get();
    }
}
