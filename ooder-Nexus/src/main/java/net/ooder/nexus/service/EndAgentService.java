package net.ooder.nexus.service;

import net.ooder.nexus.domain.end.model.EndAgent;

import java.util.List;
import java.util.Map;

/**
 * 终端代理服务接口
 */
public interface EndAgentService {
    
    /**
     * 创建终端代理
     */
    EndAgent createAgent(EndAgent agent);
    
    /**
     * 更新终端代理
     */
    EndAgent updateAgent(String id, EndAgent agent);
    
    /**
     * 删除终端代理
     */
    boolean deleteAgent(String id);
    
    /**
     * 根据ID获取终端代理
     */
    EndAgent getAgentById(String id);
    
    /**
     * 获取所有终端代理
     */
    List<EndAgent> getAllAgents();
    
    /**
     * 根据类型获取终端代理
     */
    List<EndAgent> getAgentsByType(String type);
    
    /**
     * 根据状态获取终端代理
     */
    List<EndAgent> getAgentsByStatus(String status);
    
    /**
     * 初始化默认数据
     */
    void initDefaultData();
    
    /**
     * 获取终端代理统计信息
     */
    AgentStats getAgentStats();
    
    /**
     * 更新终端代理属性
     */
    EndAgent updateAgentProperties(String id, Map<String, Object> properties);
    
    /**
     * 终端代理统计信息
     */
    class AgentStats {
        private int totalCount;
        private int activeCount;
        private int inactiveCount;
        
        public int getTotalCount() {
            return totalCount;
        }
        
        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
        
        public int getActiveCount() {
            return activeCount;
        }
        
        public void setActiveCount(int activeCount) {
            this.activeCount = activeCount;
        }
        
        public int getInactiveCount() {
            return inactiveCount;
        }
        
        public void setInactiveCount(int inactiveCount) {
            this.inactiveCount = inactiveCount;
        }
    }
}
