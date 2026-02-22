package net.ooder.nexus.service.audit;

import net.ooder.nexus.domain.skill.model.SkillResourceLog;

import java.util.List;
import java.util.Map;

/**
 * 审计日志服务接口
 * 提供技能资源访问日志记录、查询、统计和告警功能
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface AuditLogService {
    
    /**
     * 记录资源访问日志
     * @param entry 日志条目
     */
    void logAccess(SkillResourceLog entry);
    
    /**
     * 查询审计日志
     * @param query 查询条件
     * @return 日志列表
     */
    List<SkillResourceLog> queryLogs(AuditLogQuery query);
    
    /**
     * 获取统计数据
     * @param userId 用户ID
     * @param skillId 技能ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    Map<String, Object> getStatistics(String userId, String skillId, long startTime, long endTime);
    
    /**
     * 获取告警列表
     * @param userId 用户ID
     * @return 告警列表
     */
    List<Map<String, Object>> getAlerts(String userId);
    
    /**
     * 标记告警为已读
     * @param logId 日志ID
     * @param userId 用户ID
     */
    void markAsRead(String logId, String userId);
    
    /**
     * 统计未读告警数量
     * @param userId 用户ID
     * @return 未读数量
     */
    long countUnread(String userId);
    
    /**
     * 导出日志
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param format 导出格式 (json/csv)
     */
    void exportLogs(String userId, long startTime, long endTime, String format);
    
    /**
     * 审计日志查询条件
     * 支持多条件组合查询和分页
     */
    public static class AuditLogQuery {
        private String userId;
        private String skillId;
        private String action;
        private String resourceType;
        private Integer status;
        private Long startTime;
        private Long endTime;
        private int page = 1;
        private int pageSize = 20;
        
        /**
         * 获取用户ID
         * @return 用户ID
         */
        public String getUserId() {
            return userId;
        }
        
        /**
         * 设置用户ID
         * @param userId 用户ID
         */
        public void setUserId(String userId) {
            this.userId = userId;
        }
        
        /**
         * 获取技能ID
         * @return 技能ID
         */
        public String getSkillId() {
            return skillId;
        }
        
        /**
         * 设置技能ID
         * @param skillId 技能ID
         */
        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }
        
        /**
         * 获取操作类型
         * @return 操作类型
         */
        public String getAction() {
            return action;
        }
        
        /**
         * 设置操作类型
         * @param action 操作类型
         */
        public void setAction(String action) {
            this.action = action;
        }
        
        /**
         * 获取资源类型
         * @return 资源类型
         */
        public String getResourceType() {
            return resourceType;
        }
        
        /**
         * 设置资源类型
         * @param resourceType 资源类型
         */
        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }
        
        /**
         * 获取状态
         * @return 状态 (1=成功, 0=失败, 2=拒绝)
         */
        public Integer getStatus() {
            return status;
        }
        
        /**
         * 设置状态
         * @param status 状态
         */
        public void setStatus(Integer status) {
            this.status = status;
        }
        
        /**
         * 获取开始时间
         * @return 开始时间戳
         */
        public Long getStartTime() {
            return startTime;
        }
        
        /**
         * 设置开始时间
         * @param startTime 开始时间戳
         */
        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }
        
        /**
         * 获取结束时间
         * @return 结束时间戳
         */
        public Long getEndTime() {
            return endTime;
        }
        
        /**
         * 设置结束时间
         * @param endTime 结束时间戳
         */
        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }
        
        /**
         * 获取页码
         * @return 页码 (从1开始)
         */
        public int getPage() {
            return page;
        }
        
        /**
         * 设置页码
         * @param page 页码
         */
        public void setPage(int page) {
            this.page = page;
        }
        
        /**
         * 获取每页大小
         * @return 每页大小
         */
        public int getPageSize() {
            return pageSize;
        }
        
        /**
         * 设置每页大小
         * @param pageSize 每页大小
         */
        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }
}
