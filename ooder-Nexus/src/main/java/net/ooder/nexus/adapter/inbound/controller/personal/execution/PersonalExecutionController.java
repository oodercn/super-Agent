package net.ooder.nexus.adapter.inbound.controller.personal.execution;

import net.ooder.nexus.common.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 个人任务执行控制器
 *
 * <p>提供个人技能执行相关接口（执行、历史、结果）</p>
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api/personal/execution")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class PersonalExecutionController {

    /**
     * 执行技能
     *
     * @param request 包含 skillId 和 parameters 的请求
     * @return 执行结果
     */
    @PostMapping("/execute")
    @ResponseBody
    public ResultModel<Map<String, Object>> execute(@RequestBody Map<String, Object> request) {
        try {
            String skillId = (String) request.get("skillId");
            Map<String, Object> parameters = (Map<String, Object>) request.get("parameters");

            Map<String, Object> result = new HashMap<>();
            result.put("executionId", UUID.randomUUID().toString());
            result.put("skillId", skillId);
            result.put("status", "running");
            result.put("startTime", System.currentTimeMillis());

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("Failed to execute skill: " + e.getMessage(), 500);
        }
    }

    /**
     * 获取执行历史
     *
     * @param request 包含 page 和 size 的请求
     * @return 执行历史列表
     */
    @PostMapping("/history")
    @ResponseBody
    public ResultModel<Map<String, Object>> getHistory(@RequestBody Map<String, Object> request) {
        try {
            int page = request.get("page") != null ? (Integer) request.get("page") : 0;
            int size = request.get("size") != null ? (Integer) request.get("size") : 10;

            List<Map<String, Object>> historyList = new ArrayList<>();

            Map<String, Object> history = new HashMap<>();
            history.put("executionId", UUID.randomUUID().toString());
            history.put("skillId", "skill-001");
            history.put("skillName", "示例技能");
            history.put("status", "completed");
            history.put("startTime", System.currentTimeMillis() - 3600000);
            history.put("endTime", System.currentTimeMillis());
            historyList.add(history);

            Map<String, Object> result = new HashMap<>();
            result.put("content", historyList);
            result.put("totalElements", 1);
            result.put("totalPages", 1);
            result.put("number", page);
            result.put("size", size);

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("Failed to get execution history: " + e.getMessage(), 500);
        }
    }

    /**
     * 获取执行结果
     *
     * @param request 包含 executionId 的请求
     * @return 执行结果详情
     */
    @PostMapping("/result")
    @ResponseBody
    public ResultModel<Map<String, Object>> getResult(@RequestBody Map<String, String> request) {
        try {
            String executionId = request.get("executionId");

            Map<String, Object> result = new HashMap<>();
            result.put("executionId", executionId);
            result.put("status", "completed");
            result.put("output", "执行成功");
            result.put("exitCode", 0);
            result.put("startTime", System.currentTimeMillis() - 60000);
            result.put("endTime", System.currentTimeMillis());

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("Failed to get execution result: " + e.getMessage(), 500);
        }
    }
}
