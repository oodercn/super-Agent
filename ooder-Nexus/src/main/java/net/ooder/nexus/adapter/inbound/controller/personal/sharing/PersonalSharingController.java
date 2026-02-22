package net.ooder.nexus.adapter.inbound.controller.personal.sharing;

import net.ooder.nexus.common.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 个人分享管理控制器
 *
 * <p>提供个人技能分享相关接口（分享、接收、管理）</p>
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api/personal/sharing")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class PersonalSharingController {

    /**
     * 获取我分享的技能列表
     *
     * @param request 包含 page 和 size 的请求
     * @return 分享的技能列表
     */
    @PostMapping("/shared")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSharedSkills(@RequestBody Map<String, Object> request) {
        try {
            int page = request.get("page") != null ? (Integer) request.get("page") : 0;
            int size = request.get("size") != null ? (Integer) request.get("size") : 10;

            List<Map<String, Object>> sharedList = new ArrayList<>();

            Map<String, Object> shared = new HashMap<>();
            shared.put("shareId", UUID.randomUUID().toString());
            shared.put("skillId", "skill-001");
            shared.put("skillName", "示例技能");
            shared.put("shareType", "public");
            shared.put("shareTime", System.currentTimeMillis());
            shared.put("accessCount", 5);
            sharedList.add(shared);

            Map<String, Object> result = new HashMap<>();
            result.put("content", sharedList);
            result.put("totalElements", 1);
            result.put("totalPages", 1);
            result.put("number", page);
            result.put("size", size);

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("Failed to get shared skills: " + e.getMessage(), 500);
        }
    }

    /**
     * 获取接收到的分享列表
     *
     * @param request 包含 page 和 size 的请求
     * @return 接收到的分享列表
     */
    @PostMapping("/received")
    @ResponseBody
    public ResultModel<Map<String, Object>> getReceivedShares(@RequestBody Map<String, Object> request) {
        try {
            int page = request.get("page") != null ? (Integer) request.get("page") : 0;
            int size = request.get("size") != null ? (Integer) request.get("size") : 10;

            List<Map<String, Object>> receivedList = new ArrayList<>();

            Map<String, Object> received = new HashMap<>();
            received.put("shareId", UUID.randomUUID().toString());
            received.put("skillId", "skill-002");
            received.put("skillName", "共享技能");
            received.put("fromUser", "user-001");
            received.put("fromUsername", "张三");
            received.put("receiveTime", System.currentTimeMillis());
            received.put("status", "active");
            receivedList.add(received);

            Map<String, Object> result = new HashMap<>();
            result.put("content", receivedList);
            result.put("totalElements", 1);
            result.put("totalPages", 1);
            result.put("number", page);
            result.put("size", size);

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("Failed to get received shares: " + e.getMessage(), 500);
        }
    }

    /**
     * 分享技能
     *
     * @param request 包含 skillId 和 shareType 的请求
     * @return 分享结果
     */
    @PostMapping("/share")
    @ResponseBody
    public ResultModel<Map<String, Object>> shareSkill(@RequestBody Map<String, Object> request) {
        try {
            String skillId = (String) request.get("skillId");
            String shareType = (String) request.get("shareType");
            List<String> targetUsers = (List<String>) request.get("targetUsers");

            Map<String, Object> result = new HashMap<>();
            result.put("shareId", UUID.randomUUID().toString());
            result.put("skillId", skillId);
            result.put("shareType", shareType);
            result.put("shareTime", System.currentTimeMillis());
            result.put("status", "active");

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("Failed to share skill: " + e.getMessage(), 500);
        }
    }

    /**
     * 取消分享
     *
     * @param request 包含 shareId 的请求
     * @return 取消结果
     */
    @PostMapping("/cancel")
    @ResponseBody
    public ResultModel<Boolean> cancelShare(@RequestBody Map<String, String> request) {
        try {
            String shareId = request.get("shareId");
            return ResultModel.success(true);
        } catch (Exception e) {
            return ResultModel.error("Failed to cancel share: " + e.getMessage(), 500);
        }
    }

    /**
     * 更新分享设置
     *
     * @param request 包含 shareId 和 settings 的请求
     * @return 更新结果
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Boolean> updateShareSettings(@RequestBody Map<String, Object> request) {
        try {
            String shareId = (String) request.get("shareId");
            Map<String, Object> settings = (Map<String, Object>) request.get("settings");
            return ResultModel.success(true);
        } catch (Exception e) {
            return ResultModel.error("Failed to update share settings: " + e.getMessage(), 500);
        }
    }
}
