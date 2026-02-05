package net.ooder.nexus.service.health;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.system.HealthCheckResult;
import net.ooder.nexus.model.system.HealthReport;
import net.ooder.nexus.model.system.HealthCheckSchedule;
import net.ooder.nexus.model.system.SystemHealthData;

import java.util.Map;

/**
 * 健康检查服务接口
 * 负责系统健康检查、服务状态检查、健康报告等功能
 */
public interface IHealthService {
    
    /**
     * 获取系统健康状态
     * @return 系统健康状态
     */
    Result<SystemHealthData> getSystemHealth();

    /**
     * 运行健康检查
     * @param params 检查参数
     * @return 检查结果
     */
    Result<HealthCheckResult> runHealthCheck(Map<String, Object> params);

    /**
     * 导出健康报告
     * @return 导出结果
     */
    Result<HealthReport> exportHealthReport();

    /**
     * 设置定时健康检查
     * @param params 定时参数
     * @return 设置结果
     */
    Result<HealthCheckSchedule> scheduleHealthCheck(Map<String, Object> params);
}
