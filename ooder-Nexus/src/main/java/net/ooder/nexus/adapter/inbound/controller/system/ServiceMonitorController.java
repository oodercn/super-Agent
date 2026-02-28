package net.ooder.nexus.adapter.inbound.controller.system;

import net.ooder.nexus.dto.system.*;
import net.ooder.nexus.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/system/services")
public class ServiceMonitorController {

    private static final Logger log = LoggerFactory.getLogger(ServiceMonitorController.class);

    private final long startTime = System.currentTimeMillis();

    @GetMapping("/overview")
    public ApiResponse<ServicesOverviewDTO> getServicesOverview() {
        try {
            List<ServicesOverviewDTO.ServiceStatusDTO> services = getCoreServices();
            
            int total = services.size();
            int running = 0;
            int stopped = 0;
            int warning = 0;
            
            for (ServicesOverviewDTO.ServiceStatusDTO s : services) {
                String status = s.getStatus();
                if ("RUNNING".equals(status)) {
                    running++;
                } else if ("STOPPED".equals(status)) {
                    stopped++;
                } else if ("WARNING".equals(status)) {
                    warning++;
                }
            }
            
            ServicesOverviewDTO data = new ServicesOverviewDTO();
            data.setTotal(total);
            data.setRunning(running);
            data.setStopped(stopped);
            data.setServices(services);
            
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("Failed to get services overview", e);
            return ApiResponse.error("获取服务概览失败: " + e.getMessage());
        }
    }

    @GetMapping("/skills")
    public ApiResponse<SkillServicesDTO> getSkillServices() {
        try {
            List<SkillServicesDTO.SkillServiceStatusDTO> skills = getSkillServiceStatuses();
            
            int total = skills.size();
            int connected = 0;
            int disconnected = 0;
            int inactive = 0;
            
            for (SkillServicesDTO.SkillServiceStatusDTO s : skills) {
                String status = s.getStatus();
                if ("CONNECTED".equals(status)) {
                    connected++;
                } else if ("DISCONNECTED".equals(status) || "CONNECTION_FAILED".equals(status)) {
                    disconnected++;
                } else if ("NOT_CONFIGURED".equals(status) || "PENDING_CONFIG".equals(status)) {
                    inactive++;
                }
            }
            
            SkillServicesDTO data = new SkillServicesDTO();
            data.setTotal(total);
            data.setActive(connected);
            data.setInactive(inactive + disconnected);
            data.setSkills(skills);
            
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("Failed to get skill services", e);
            return ApiResponse.error("获取Skill服务状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/{serviceId}/check")
    public ApiResponse<ServicesOverviewDTO.ServiceStatusDTO> checkService(@PathVariable String serviceId) {
        try {
            ServicesOverviewDTO.ServiceStatusDTO status = checkServiceStatus(serviceId);
            if (status != null) {
                return ApiResponse.success(status);
            } else {
                return ApiResponse.notFound("服务不存在");
            }
        } catch (Exception e) {
            log.error("Failed to check service", e);
            return ApiResponse.error("检查服务失败: " + e.getMessage());
        }
    }

    @GetMapping("/{serviceId}/history")
    public ApiResponse<ServiceHistoryDTO> getServiceHistory(
            @PathVariable String serviceId,
            @RequestParam(defaultValue = "24h") String period) {
        try {
            ServiceHistoryDTO data = new ServiceHistoryDTO();
            data.setPeriod(period);
            data.setHistory(generateMockHistory(period));
            
            ServiceHistoryDTO.ServiceStatisticsDTO statistics = new ServiceHistoryDTO.ServiceStatisticsDTO();
            statistics.setAvailability(99.9);
            statistics.setIncidents(0);
            statistics.setTotalDowntime(0L);
            data.setStatistics(statistics);
            
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("Failed to get service history", e);
            return ApiResponse.error("获取历史数据失败: " + e.getMessage());
        }
    }

    private List<ServicesOverviewDTO.ServiceStatusDTO> getCoreServices() {
        List<ServicesOverviewDTO.ServiceStatusDTO> services = new ArrayList<>();
        
        ServicesOverviewDTO.ServiceStatusDTO nexusCore = new ServicesOverviewDTO.ServiceStatusDTO();
        nexusCore.setId("nexus-core");
        nexusCore.setName("Nexus Core Service");
        nexusCore.setStatus("RUNNING");
        nexusCore.setUptime(formatUptime(System.currentTimeMillis() - startTime));
        
        ServicesOverviewDTO.ServiceMetricsDTO nexusMetrics = new ServicesOverviewDTO.ServiceMetricsDTO();
        nexusMetrics.setCpu(15.5);
        nexusMetrics.setMemory(256.0);
        nexusMetrics.setRequests(1000L);
        nexusCore.setMetrics(nexusMetrics);
        services.add(nexusCore);
        
        ServicesOverviewDTO.ServiceStatusDTO sceneEngine = new ServicesOverviewDTO.ServiceStatusDTO();
        sceneEngine.setId("scene-engine");
        sceneEngine.setName("Scene Engine");
        sceneEngine.setStatus("RUNNING");
        sceneEngine.setUptime(formatUptime(System.currentTimeMillis() - startTime - 10000));
        services.add(sceneEngine);
        
        ServicesOverviewDTO.ServiceStatusDTO msgQueue = new ServicesOverviewDTO.ServiceStatusDTO();
        msgQueue.setId("message-queue");
        msgQueue.setName("Message Queue");
        msgQueue.setStatus("STOPPED");
        msgQueue.setUptime("0");
        services.add(msgQueue);
        
        return services;
    }

    private List<SkillServicesDTO.SkillServiceStatusDTO> getSkillServiceStatuses() {
        List<SkillServicesDTO.SkillServiceStatusDTO> skills = new ArrayList<>();
        
        SkillServicesDTO.SkillServiceStatusDTO dingding = new SkillServicesDTO.SkillServiceStatusDTO();
        dingding.setId("skill-org-dingding");
        dingding.setName("钉钉组织服务");
        dingding.setStatus("CONNECTED");
        dingding.setLastActive("刚刚");
        dingding.setExecutions(150);
        skills.add(dingding);
        
        SkillServicesDTO.SkillServiceStatusDTO feishu = new SkillServicesDTO.SkillServiceStatusDTO();
        feishu.setId("skill-org-feishu");
        feishu.setName("飞书组织服务");
        feishu.setStatus("DISCONNECTED");
        feishu.setLastActive("1小时前");
        feishu.setExecutions(0);
        skills.add(feishu);
        
        SkillServicesDTO.SkillServiceStatusDTO qiwei = new SkillServicesDTO.SkillServiceStatusDTO();
        qiwei.setId("skill-org-qiwei");
        qiwei.setName("企业微信");
        qiwei.setStatus("PENDING_CONFIG");
        qiwei.setLastActive("-");
        qiwei.setExecutions(0);
        skills.add(qiwei);
        
        SkillServicesDTO.SkillServiceStatusDTO dbMysql = new SkillServicesDTO.SkillServiceStatusDTO();
        dbMysql.setId("skill-db-mysql");
        dbMysql.setName("MySQL数据库");
        dbMysql.setStatus("CONNECTED");
        dbMysql.setLastActive("刚刚");
        dbMysql.setExecutions(500);
        skills.add(dbMysql);
        
        return skills;
    }

    private ServicesOverviewDTO.ServiceStatusDTO checkServiceStatus(String serviceId) {
        for (ServicesOverviewDTO.ServiceStatusDTO s : getCoreServices()) {
            if (serviceId.equals(s.getId())) {
                return s;
            }
        }
        return null;
    }

    private List<ServiceHistoryDTO.ServiceHistoryPointDTO> generateMockHistory(String period) {
        List<ServiceHistoryDTO.ServiceHistoryPointDTO> history = new ArrayList<>();
        Random random = new Random();
        
        int points = "24h".equals(period) ? 24 : "7d".equals(period) ? 168 : 6;
        
        for (int i = 0; i < points; i++) {
            ServiceHistoryDTO.ServiceHistoryPointDTO point = new ServiceHistoryDTO.ServiceHistoryPointDTO();
            point.setTime(String.valueOf(System.currentTimeMillis() - (points - i) * 3600000));
            point.setRunning(1);
            point.setStopped(0);
            history.add(point);
        }
        
        return history;
    }

    private String formatUptime(long uptimeMs) {
        long seconds = uptimeMs / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + "天 " + (hours % 24) + "小时";
        } else if (hours > 0) {
            return hours + "小时 " + (minutes % 60) + "分钟";
        } else {
            return minutes + "分钟";
        }
    }
}
