package net.ooder.nexus.adapter.inbound.controller.admin;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.service.AdminRemoteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/remote")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminRemoteController {

    private static final Logger log = LoggerFactory.getLogger(AdminRemoteController.class);

    @Autowired
    private AdminRemoteService adminRemoteService;

    @PostMapping("/agents/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getAgentList() {
        log.info("Get remote agents requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> agents = adminRemoteService.getAllRemoteAgents();
            result.setData(agents);
            result.setSize(agents.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting remote agents", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/agents/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getAgent(@RequestBody Map<String, String> request) {
        log.info("Get remote agent requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> agent = adminRemoteService.getRemoteAgentById(request.get("id"));
            if (agent == null) {
                result.setRequestStatus(404);
                result.setMessage("Agent not found");
            } else {
                result.setData(agent);
                result.setRequestStatus(200);
                result.setMessage("Success");
            }
        } catch (Exception e) {
            log.error("Error getting remote agent", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/agents/register")
    @ResponseBody
    public ResultModel<Map<String, Object>> registerAgent(@RequestBody Map<String, Object> request) {
        log.info("Register remote agent requested: {}", request.get("name"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> agent = adminRemoteService.registerRemoteAgent(request);
            result.setData(agent);
            result.setRequestStatus(200);
            result.setMessage("Agent registered successfully");
        } catch (Exception e) {
            log.error("Error registering remote agent", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/agents/update")
    @ResponseBody
    public ResultModel<Map<String, Object>> updateAgent(@RequestBody Map<String, Object> request) {
        log.info("Update remote agent requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            String id = (String) request.get("id");
            Map<String, Object> agent = adminRemoteService.updateRemoteAgent(id, request);
            if (agent == null) {
                result.setRequestStatus(404);
                result.setMessage("Agent not found");
            } else {
                result.setData(agent);
                result.setRequestStatus(200);
                result.setMessage("Updated successfully");
            }
        } catch (Exception e) {
            log.error("Error updating remote agent", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/agents/unregister")
    @ResponseBody
    public ResultModel<Boolean> unregisterAgent(@RequestBody Map<String, String> request) {
        log.info("Unregister remote agent requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminRemoteService.unregisterRemoteAgent(request.get("id"));
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Agent unregistered successfully" : "Agent not found");
        } catch (Exception e) {
            log.error("Error unregistering remote agent", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/agents/test")
    @ResponseBody
    public ResultModel<Boolean> testConnection(@RequestBody Map<String, String> request) {
        log.info("Test connection requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminRemoteService.testConnection(request.get("id"));
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage(success ? "Connection successful" : "Connection failed");
        } catch (Exception e) {
            log.error("Error testing connection", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/agents/logs")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getConnectionLogs(@RequestBody Map<String, Object> request) {
        log.info("Get connection logs requested: {}", request.get("agentId"));
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            String agentId = (String) request.get("agentId");
            int limit = request.get("limit") != null ? ((Number) request.get("limit")).intValue() : 100;
            List<Map<String, Object>> logs = adminRemoteService.getConnectionLogs(agentId, limit);
            result.setData(logs);
            result.setSize(logs.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting connection logs", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/statistics")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatistics() {
        log.info("Get remote statistics requested");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> stats = adminRemoteService.getRemoteStatistics();
            result.setData(stats);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting statistics", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }
}
