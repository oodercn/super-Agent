package net.ooder.mvp.skill.scene.capability.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/network-join")
@CrossOrigin(origins = "*")
public class NetworkJoinController {

    private static final Logger log = LoggerFactory.getLogger(NetworkJoinController.class);

    @GetMapping("/requests")
    public ResultModel<List<Map<String, Object>>> getRequests(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        
        log.info("[getRequests] status={}, type={}", status, type);
        
        List<Map<String, Object>> requests = new ArrayList<>();
        
        requests.add(createMockRequest("req-001", "USER_JOIN", "PENDING", "张三", "zhangsan@company.com", "研发部", "参与日志汇报场景"));
        requests.add(createMockRequest("req-002", "DEVICE_JOIN", "PENDING", "Agent-Node-003", "192.168.1.103", "WORKER", "作为日志汇报场景的执行节点"));
        requests.add(createMockRequest("req-003", "USER_JOIN", "APPROVED", "李四", "lisi@company.com", "产品部", "参与需求管理场景"));
        
        return ResultModel.success(requests);
    }

    @GetMapping("/requests/{requestId}")
    public ResultModel<Map<String, Object>> getRequest(@PathVariable String requestId) {
        log.info("[getRequest] requestId={}", requestId);
        
        Map<String, Object> request = createMockRequest(requestId, "USER_JOIN", "PENDING", "张三", "zhangsan@company.com", "研发部", "参与日志汇报场景");
        return ResultModel.success(request);
    }

    @PostMapping("/requests")
    public ResultModel<Map<String, Object>> createRequest(@RequestBody Map<String, Object> request) {
        log.info("[createRequest] request={}", request);
        
        Map<String, Object> result = new HashMap<>();
        result.put("requestId", "req-" + System.currentTimeMillis());
        result.put("status", "PENDING");
        result.put("createdAt", System.currentTimeMillis());
        result.putAll(request);
        
        return ResultModel.success(result);
    }

    @PostMapping("/requests/{requestId}/approve")
    public ResultModel<Map<String, Object>> approveRequest(
            @PathVariable String requestId,
            @RequestBody(required = false) Map<String, Object> approval) {
        
        log.info("[approveRequest] requestId={}, approval={}", requestId, approval);
        
        Map<String, Object> result = new HashMap<>();
        result.put("requestId", requestId);
        result.put("status", "APPROVED");
        result.put("reviewedAt", System.currentTimeMillis());
        result.put("reviewerId", "admin");
        
        if (approval != null) {
            result.put("keyRule", approval.get("keyRule"));
            result.put("validityDays", approval.getOrDefault("validityDays", 30));
            result.put("allowedScenes", approval.get("allowedScenes"));
            result.put("reviewComment", approval.get("reviewComment"));
        }
        
        result.put("generatedKeyId", "key-" + System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @PostMapping("/requests/{requestId}/reject")
    public ResultModel<Map<String, Object>> rejectRequest(
            @PathVariable String requestId,
            @RequestBody(required = false) Map<String, Object> rejection) {
        
        log.info("[rejectRequest] requestId={}, rejection={}", requestId, rejection);
        
        Map<String, Object> result = new HashMap<>();
        result.put("requestId", requestId);
        result.put("status", "REJECTED");
        result.put("reviewedAt", System.currentTimeMillis());
        result.put("reviewerId", "admin");
        
        if (rejection != null) {
            result.put("reviewComment", rejection.get("reviewComment"));
        }
        
        return ResultModel.success(result);
    }

    @DeleteMapping("/requests/{requestId}")
    public ResultModel<Boolean> cancelRequest(@PathVariable String requestId) {
        log.info("[cancelRequest] requestId={}", requestId);
        return ResultModel.success(true);
    }

    @GetMapping("/pending-count")
    public ResultModel<Map<String, Object>> getPendingCount() {
        log.info("[getPendingCount]");
        
        Map<String, Object> result = new HashMap<>();
        result.put("pending", 2);
        result.put("approved", 1);
        result.put("rejected", 0);
        result.put("total", 3);
        
        return ResultModel.success(result);
    }

    @GetMapping("/stats")
    public ResultModel<Map<String, Object>> getStats() {
        log.info("[getStats]");
        
        Map<String, Object> result = new HashMap<>();
        result.put("pending", 2);
        result.put("todayApproved", 1);
        result.put("todayRejected", 0);
        result.put("totalUsers", 15);
        result.put("totalDevices", 5);
        result.put("totalAgents", 8);
        
        return ResultModel.success(result);
    }

    private Map<String, Object> createMockRequest(String requestId, String type, String status,
            String applicantName, String applicantEmail, String department, String reason) {
        
        Map<String, Object> request = new HashMap<>();
        request.put("requestId", requestId);
        request.put("type", type);
        request.put("status", status);
        request.put("applicantId", "user-" + requestId);
        request.put("applicantName", applicantName);
        request.put("applicantEmail", applicantEmail);
        request.put("department", department);
        request.put("reason", reason);
        request.put("createdAt", System.currentTimeMillis() - 3600000);
        
        List<String> requestedScenes = new ArrayList<>();
        requestedScenes.add("daily-log-scene");
        request.put("requestedScenes", requestedScenes);
        
        Map<String, Object> recommendedRule = new HashMap<>();
        recommendedRule.put("ruleId", "rule-default");
        recommendedRule.put("ruleName", "默认规则");
        recommendedRule.put("validityDays", 30);
        recommendedRule.put("maxUseCount", 1000);
        request.put("recommendedRule", recommendedRule);
        
        if ("APPROVED".equals(status)) {
            request.put("reviewerId", "admin");
            request.put("reviewerName", "管理员");
            request.put("reviewedAt", System.currentTimeMillis() - 1800000);
            request.put("generatedKeyId", "key-" + requestId);
        }
        
        return request;
    }
}
