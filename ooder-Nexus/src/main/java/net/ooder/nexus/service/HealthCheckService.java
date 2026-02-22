package net.ooder.nexus.service;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.system.model.HealthCheckResult;
import net.ooder.nexus.domain.system.model.HealthReport;
import net.ooder.nexus.domain.system.model.HealthCheckSchedule;
import net.ooder.nexus.domain.system.model.ServiceCheckResult;

import java.util.Map;

/**
 * 健康检查服务接口
 *
 * <p>提供系统健康检查、报告导出、定时检查调度等功能。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>运行健康检查 - 检查系统各项健康指标</li>
 *   <li>导出健康报告 - 生成并导出健康检查报告</li>
 *   <li>调度健康检查 - 配置定时健康检查任务</li>
 *   <li>检查指定服务 - 检查特定服务的健康状态</li>
 * </ul>
 *
 * @author ooder Team
 * @version 2.0.0-openwrt-preview
 * @since 2.0.0
 * @see HealthCheckResult
 * @see HealthReport
 * @see HealthCheckSchedule
 * @see ServiceCheckResult
 */
public interface HealthCheckService {

    /**
     * 运行健康检查
     *
     * <p>执行全面的系统健康检查，包括：</p>
     * <ul>
     *   <li>系统资源检查（CPU、内存、磁盘）</li>
     *   <li>服务状态检查</li>
     *   <li>网络连接检查</li>
     *   <li>P2P 网络健康检查</li>
     * </ul>
     *
     * @param params 检查参数，可包含检查类型、深度等配置
     * @return 健康检查结果
     */
    Result<HealthCheckResult> runHealthCheck(Map<String, Object> params);

    /**
     * 导出健康报告
     *
     * <p>生成健康检查报告并导出，支持多种格式：</p>
     * <ul>
     *   <li>PDF 格式</li>
     *   <li>HTML 格式</li>
     *   <li>JSON 格式</li>
     * </ul>
     *
     * @return 健康报告导出结果
     */
    Result<HealthReport> exportHealthReport();

    /**
     * 调度健康检查
     *
     * <p>配置定时健康检查任务，支持：</p>
     * <ul>
     *   <li>定时执行（Cron 表达式）</li>
     *   <li>检查频率设置</li>
     *   <li>告警阈值配置</li>
     *   <li>通知方式设置</li>
     * </ul>
     *
     * @param params 调度参数，包含定时规则、通知配置等
     * @return 调度配置结果
     */
    Result<HealthCheckSchedule> scheduleHealthCheck(Map<String, Object> params);

    /**
     * 检查指定服务
     *
     * <p>对特定服务进行健康检查，检查内容包括：</p>
     * <ul>
     *   <li>服务运行状态</li>
     *   <li>服务响应时间</li>
     *   <li>服务资源占用</li>
     *   <li>服务依赖检查</li>
     * </ul>
     *
     * @param serviceName 服务名称
     * @return 服务检查结果
     */
    Result<ServiceCheckResult> checkService(String serviceName);
}
