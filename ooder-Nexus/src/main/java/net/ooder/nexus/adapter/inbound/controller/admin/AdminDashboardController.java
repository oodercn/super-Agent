package net.ooder.nexus.adapter.inbound.controller.admin;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.service.AdminDashboardService;
import net.ooder.nexus.service.AdminDashboardService.DashboardStatistics;
import net.ooder.nexus.service.AdminDashboardService.SystemHealth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminDashboardController {

    private static final Logger log = LoggerFactory.getLogger(AdminDashboardController.class);

    @Autowired
    private AdminDashboardService adminDashboardService;

    @PostMapping("/stats")
    @ResponseBody
    public ResultModel<DashboardStatistics> getStats() {
        log.info("Get admin dashboard stats requested");
        ResultModel<DashboardStatistics> result = new ResultModel<DashboardStatistics>();

        try {
            DashboardStatistics stats = adminDashboardService.getDashboardStats();
            result.setData(stats);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting dashboard stats", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/activities")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getRecentActivities(@RequestBody(required = false) Map<String, Object> request) {
        log.info("Get recent activities requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();

        try {
            int limit = 10;
            if (request != null && request.get("limit") != null) {
                limit = ((Number) request.get("limit")).intValue();
            }

            List<Map<String, Object>> activities = adminDashboardService.getRecentActivities(limit);
            result.setData(activities);
            result.setSize(activities.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting recent activities", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/alerts")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getSystemAlerts() {
        log.info("Get system alerts requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();

        try {
            List<Map<String, Object>> alerts = adminDashboardService.getSystemAlerts();
            result.setData(alerts);
            result.setSize(alerts.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting system alerts", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/top-skills")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getTopSkills(@RequestBody(required = false) Map<String, Object> request) {
        log.info("Get top skills requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();

        try {
            int limit = 5;
            if (request != null && request.get("limit") != null) {
                limit = ((Number) request.get("limit")).intValue();
            }

            List<Map<String, Object>> skills = adminDashboardService.getTopSkills(limit);
            result.setData(skills);
            result.setSize(skills.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting top skills", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/active-users")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getActiveUsers(@RequestBody(required = false) Map<String, Object> request) {
        log.info("Get active users requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();

        try {
            int limit = 10;
            if (request != null && request.get("limit") != null) {
                limit = ((Number) request.get("limit")).intValue();
            }

            List<Map<String, Object>> users = adminDashboardService.getActiveUsers(limit);
            result.setData(users);
            result.setSize(users.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting active users", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/health")
    @ResponseBody
    public ResultModel<SystemHealth> getSystemHealth() {
        log.info("Get system health requested");
        ResultModel<SystemHealth> result = new ResultModel<SystemHealth>();

        try {
            SystemHealth health = adminDashboardService.getSystemHealth();
            result.setData(health);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting system health", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }
}
