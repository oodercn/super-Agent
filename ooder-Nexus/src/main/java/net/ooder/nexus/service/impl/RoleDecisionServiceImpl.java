package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.RoleDecisionService;
import net.ooder.nexus.service.RadarService;
import net.ooder.nexus.service.DomainService;

import net.ooder.sdk.common.enums.AgentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RoleDecisionServiceImpl implements RoleDecisionService {

    private static final Logger log = LoggerFactory.getLogger(RoleDecisionServiceImpl.class);

    private AgentType currentRole = AgentType.END;
    private final List<RoleChangeListener> listeners = new CopyOnWriteArrayList<RoleChangeListener>();
    private final List<DecisionHistory> historyList = new ArrayList<DecisionHistory>();

    @Autowired(required = false)
    private RadarService radarService;

    @Autowired(required = false)
    private DomainService domainService;

    public RoleDecisionServiceImpl() {
        log.info("RoleDecisionServiceImpl initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<RoleDecision> decideRole() {
        log.info("Starting role decision process");
        
        return CompletableFuture.supplyAsync(() -> {
            RoleDecision decision = new RoleDecision();
            List<DecisionFactor> factors = new ArrayList<DecisionFactor>();
            
            boolean hasMcp = checkForMcp();
            DecisionFactor mcpFactor = new DecisionFactor();
            mcpFactor.setFactorName("MCP存在");
            mcpFactor.setFactorValue(hasMcp ? "是" : "否");
            mcpFactor.setWeight(0.4);
            mcpFactor.setImpact(hasMcp ? "作为END或ROUTE加入" : "可升级为MCP");
            factors.add(mcpFactor);
            
            boolean hasDomain = domainService != null && domainService.hasJoinedDomain();
            DecisionFactor domainFactor = new DecisionFactor();
            domainFactor.setFactorName("已加入域");
            domainFactor.setFactorValue(hasDomain ? "是" : "否");
            domainFactor.setWeight(0.3);
            domainFactor.setImpact(hasDomain ? "跟随域角色" : "独立决策");
            factors.add(domainFactor);
            
            boolean hasNetwork = checkNetworkConnectivity();
            DecisionFactor networkFactor = new DecisionFactor();
            networkFactor.setFactorName("网络连接");
            networkFactor.setFactorValue(hasNetwork ? "可用" : "不可用");
            networkFactor.setWeight(0.2);
            networkFactor.setImpact(hasNetwork ? "可参与网络" : "离线模式");
            factors.add(networkFactor);
            
            int nodeCount = getNearbyNodeCount();
            DecisionFactor nodeFactor = new DecisionFactor();
            nodeFactor.setFactorName("附近节点数");
            nodeFactor.setFactorValue(String.valueOf(nodeCount));
            nodeFactor.setWeight(0.1);
            nodeFactor.setImpact(nodeCount > 0 ? "有协作伙伴" : "独立运行");
            factors.add(nodeFactor);
            
            AgentType decidedRole;
            String reason;
            
            if (!hasNetwork) {
                decidedRole = AgentType.END;
                reason = "无网络连接，进入离线END模式";
            } else if (hasMcp) {
                if (nodeCount > 5) {
                    decidedRole = AgentType.ROUTE;
                    reason = "存在MCP且节点较多，作为ROUTE代理";
                } else {
                    decidedRole = AgentType.END;
                    reason = "存在MCP，作为END代理加入";
                }
            } else {
                decidedRole = AgentType.MCP;
                reason = "无MCP存在，自动升级为MCP";
            }
            
            decision.setDecidedRole(decidedRole);
            decision.setReason(reason);
            decision.setFactors(factors);
            decision.setDecisionTime(System.currentTimeMillis());
            decision.setAutoApplied(true);
            
            applyRoleDecision(decision);
            
            log.info("Role decision completed: {} - {}", decidedRole, reason);
            return decision;
        });
    }

    private boolean checkForMcp() {
        if (radarService != null) {
            try {
                List<RadarService.DiscoveredNode> nodes = radarService.discoverNearbyNodes().join();
                for (RadarService.DiscoveredNode node : nodes) {
                    if ("MCP".equals(node.getNodeType())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to check for MCP: {}", e.getMessage());
            }
        }
        return false;
    }

    private boolean checkNetworkConnectivity() {
        return true;
    }

    private int getNearbyNodeCount() {
        if (radarService != null) {
            try {
                List<RadarService.DiscoveredNode> nodes = radarService.discoverNearbyNodes().join();
                return nodes.size();
            } catch (Exception e) {
                log.warn("Failed to get nearby node count: {}", e.getMessage());
            }
        }
        return 0;
    }

    private void applyRoleDecision(RoleDecision decision) {
        AgentType oldRole = currentRole;
        currentRole = decision.getDecidedRole();
        
        DecisionHistory history = new DecisionHistory();
        history.setHistoryId("history-" + System.currentTimeMillis());
        history.setFromRole(oldRole);
        history.setToRole(decision.getDecidedRole());
        history.setReason(decision.getReason());
        history.setDecisionTime(decision.getDecisionTime());
        history.setApplied(true);
        historyList.add(0, history);
        
        if (historyList.size() > 100) {
            historyList.remove(historyList.size() - 1);
        }
        
        notifyRoleChanged(oldRole, currentRole, decision.getReason());
    }

    @Override
    public AgentType getCurrentRole() {
        return currentRole;
    }

    @Override
    public CompletableFuture<Void> setRole(AgentType role) {
        log.info("Manually setting role to: {}", role);
        
        return CompletableFuture.runAsync(() -> {
            AgentType oldRole = currentRole;
            currentRole = role;
            
            DecisionHistory history = new DecisionHistory();
            history.setHistoryId("history-" + System.currentTimeMillis());
            history.setFromRole(oldRole);
            history.setToRole(role);
            history.setReason("手动设置");
            history.setDecisionTime(System.currentTimeMillis());
            history.setApplied(true);
            historyList.add(0, history);
            
            notifyRoleChanged(oldRole, role, "手动设置");
            log.info("Role changed from {} to {}", oldRole, role);
        });
    }

    @Override
    public CompletableFuture<UpgradeCheckResult> checkMcpUpgrade() {
        log.info("Checking MCP upgrade eligibility");
        
        return CompletableFuture.supplyAsync(() -> {
            UpgradeCheckResult result = new UpgradeCheckResult();
            List<String> requirements = new ArrayList<String>();
            List<String> missing = new ArrayList<String>();
            
            requirements.add("稳定的网络连接");
            requirements.add("足够的计算资源");
            requirements.add("持久存储能力");
            requirements.add("无其他MCP在局域网内");
            
            boolean hasNetwork = checkNetworkConnectivity();
            if (!hasNetwork) {
                missing.add("稳定的网络连接");
            }
            
            boolean hasStorage = true;
            if (!hasStorage) {
                missing.add("持久存储能力");
            }
            
            boolean noOtherMcp = !checkForMcp();
            if (!noOtherMcp) {
                missing.add("无其他MCP在局域网内");
            }
            
            result.setRequirements(requirements);
            result.setMissingRequirements(missing);
            result.setCanUpgrade(missing.isEmpty());
            
            if (missing.isEmpty()) {
                result.setRecommendation("满足所有条件，可以升级为MCP");
            } else {
                result.setRecommendation("缺少条件: " + String.join(", ", missing));
            }
            
            return result;
        });
    }

    @Override
    public CompletableFuture<UpgradeResult> upgradeRole(AgentType targetRole) {
        log.info("Upgrading role to: {}", targetRole);
        
        return CompletableFuture.supplyAsync(() -> {
            UpgradeResult result = new UpgradeResult();
            result.setPreviousRole(currentRole);
            
            try {
                AgentType oldRole = currentRole;
                currentRole = targetRole;
                
                result.setSuccess(true);
                result.setNewRole(targetRole);
                result.setMessage("角色升级成功");
                result.setUpgradeTime(System.currentTimeMillis());
                
                DecisionHistory history = new DecisionHistory();
                history.setHistoryId("history-" + System.currentTimeMillis());
                history.setFromRole(oldRole);
                history.setToRole(targetRole);
                history.setReason("角色升级");
                history.setDecisionTime(System.currentTimeMillis());
                history.setApplied(true);
                historyList.add(0, history);
                
                notifyRoleChanged(oldRole, targetRole, "角色升级");
                
                log.info("Role upgraded from {} to {}", oldRole, targetRole);
            } catch (Exception e) {
                result.setSuccess(false);
                result.setNewRole(currentRole);
                result.setMessage("角色升级失败: " + e.getMessage());
                log.error("Failed to upgrade role", e);
            }
            
            return result;
        });
    }

    @Override
    public CompletableFuture<List<DecisionHistory>> getDecisionHistory(int limit) {
        log.info("Getting decision history, limit: {}", limit);
        
        return CompletableFuture.supplyAsync(() -> {
            if (limit <= 0 || limit > historyList.size()) {
                return new ArrayList<DecisionHistory>(historyList);
            }
            return new ArrayList<DecisionHistory>(historyList.subList(0, limit));
        });
    }

    @Override
    public void addRoleChangeListener(RoleChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeRoleChangeListener(RoleChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyRoleChanged(AgentType oldRole, AgentType newRole, String reason) {
        for (RoleChangeListener listener : listeners) {
            try {
                listener.onRoleChanged(oldRole, newRole, reason);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }
}
