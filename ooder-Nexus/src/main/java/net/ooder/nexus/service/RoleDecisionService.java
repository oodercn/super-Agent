package net.ooder.nexus.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.ooder.sdk.common.enums.AgentType;

/**
 * 角色决策服务接口
 *
 * <p>SDK 0.7.2 新增接口，提供智能角色决策能力。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>网络环境检测</li>
 *   <li>角色自动决策</li>
 *   <li>角色切换管理</li>
 *   <li>决策历史记录</li>
 * </ul>
 *
 * @author ooder Team
 * @version 1.0
 * @since SDK 0.7.2
 */
public interface RoleDecisionService {

    /**
     * 执行角色决策
     *
     * @return 决策结果
     */
    CompletableFuture<RoleDecision> decideRole();

    /**
     * 获取当前角色
     *
     * @return 当前角色
     */
    AgentType getCurrentRole();

    /**
     * 设置当前角色
     *
     * @param role 角色类型
     * @return 设置结果
     */
    CompletableFuture<Void> setRole(AgentType role);

    /**
     * 检查是否可以升级为MCP
     *
     * @return 是否可升级
     */
    CompletableFuture<UpgradeCheckResult> checkMcpUpgrade();

    /**
     * 执行角色升级
     *
     * @param targetRole 目标角色
     * @return 升级结果
     */
    CompletableFuture<UpgradeResult> upgradeRole(AgentType targetRole);

    /**
     * 获取决策历史
     *
     * @param limit 数量限制
     * @return 决策历史列表
     */
    CompletableFuture<List<DecisionHistory>> getDecisionHistory(int limit);

    /**
     * 添加角色变更监听器
     *
     * @param listener 监听器
     */
    void addRoleChangeListener(RoleChangeListener listener);

    /**
     * 移除角色变更监听器
     *
     * @param listener 监听器
     */
    void removeRoleChangeListener(RoleChangeListener listener);

    /**
     * 角色变更监听器
     */
    interface RoleChangeListener {
        void onRoleChanged(AgentType oldRole, AgentType newRole, String reason);
    }

    /**
     * 角色决策结果
     */
    class RoleDecision {
        private AgentType decidedRole;
        private String reason;
        private List<DecisionFactor> factors;
        private long decisionTime;
        private boolean autoApplied;

        public AgentType getDecidedRole() { return decidedRole; }
        public void setDecidedRole(AgentType decidedRole) { this.decidedRole = decidedRole; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public List<DecisionFactor> getFactors() { return factors; }
        public void setFactors(List<DecisionFactor> factors) { this.factors = factors; }
        public long getDecisionTime() { return decisionTime; }
        public void setDecisionTime(long decisionTime) { this.decisionTime = decisionTime; }
        public boolean isAutoApplied() { return autoApplied; }
        public void setAutoApplied(boolean autoApplied) { this.autoApplied = autoApplied; }
    }

    /**
     * 决策因素
     */
    class DecisionFactor {
        private String factorName;
        private String factorValue;
        private double weight;
        private String impact;

        public String getFactorName() { return factorName; }
        public void setFactorName(String factorName) { this.factorName = factorName; }
        public String getFactorValue() { return factorValue; }
        public void setFactorValue(String factorValue) { this.factorValue = factorValue; }
        public double getWeight() { return weight; }
        public void setWeight(double weight) { this.weight = weight; }
        public String getImpact() { return impact; }
        public void setImpact(String impact) { this.impact = impact; }
    }

    /**
     * 升级检查结果
     */
    class UpgradeCheckResult {
        private boolean canUpgrade;
        private List<String> requirements;
        private List<String> missingRequirements;
        private String recommendation;

        public boolean isCanUpgrade() { return canUpgrade; }
        public void setCanUpgrade(boolean canUpgrade) { this.canUpgrade = canUpgrade; }
        public List<String> getRequirements() { return requirements; }
        public void setRequirements(List<String> requirements) { this.requirements = requirements; }
        public List<String> getMissingRequirements() { return missingRequirements; }
        public void setMissingRequirements(List<String> missingRequirements) { this.missingRequirements = missingRequirements; }
        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    }

    /**
     * 升级结果
     */
    class UpgradeResult {
        private boolean success;
        private AgentType previousRole;
        private AgentType newRole;
        private String message;
        private long upgradeTime;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public AgentType getPreviousRole() { return previousRole; }
        public void setPreviousRole(AgentType previousRole) { this.previousRole = previousRole; }
        public AgentType getNewRole() { return newRole; }
        public void setNewRole(AgentType newRole) { this.newRole = newRole; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getUpgradeTime() { return upgradeTime; }
        public void setUpgradeTime(long upgradeTime) { this.upgradeTime = upgradeTime; }
    }

    /**
     * 决策历史
     */
    class DecisionHistory {
        private String historyId;
        private AgentType fromRole;
        private AgentType toRole;
        private String reason;
        private long decisionTime;
        private boolean applied;

        public String getHistoryId() { return historyId; }
        public void setHistoryId(String historyId) { this.historyId = historyId; }
        public AgentType getFromRole() { return fromRole; }
        public void setFromRole(AgentType fromRole) { this.fromRole = fromRole; }
        public AgentType getToRole() { return toRole; }
        public void setToRole(AgentType toRole) { this.toRole = toRole; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public long getDecisionTime() { return decisionTime; }
        public void setDecisionTime(long decisionTime) { this.decisionTime = decisionTime; }
        public boolean isApplied() { return applied; }
        public void setApplied(boolean applied) { this.applied = applied; }
    }
}
