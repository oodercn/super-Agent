package net.ooder.nexus.adapter.inbound.controller.collaboration;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.domain.collaboration.model.CollaborationScene;
import net.ooder.nexus.domain.collaboration.model.SceneMember;
import net.ooder.nexus.service.collaboration.CollaborationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 协作场景控制器
 *
 * <p>提供协作场景 API：</p>
 * <ul>
 *   <li>场景创建/管理</li>
 *   <li>成员管理</li>
 *   <li>场景密钥</li>
 *   <li>场景状态</li>
 * </ul>
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/collaboration/scene", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class CollaborationController {

    private static final Logger log = LoggerFactory.getLogger(CollaborationController.class);

    @Autowired
    private CollaborationService collaborationService;

    /**
     * 创建场景
     */
    @PostMapping("/create")
    @ResponseBody
    public ResultModel<Map<String, Object>> createScene(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        String ownerId = (String) request.get("ownerId");
        String ownerName = (String) request.get("ownerName");
        @SuppressWarnings("unchecked")
        List<String> skillIds = (List<String>) request.get("skillIds");

        log.info("Create scene: name={}, owner={}", name, ownerId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            CollaborationScene scene = collaborationService.createScene(name, description, ownerId, ownerName, skillIds);
            result.setData(convertSceneToMap(scene));
            result.setRequestStatus(200);
            result.setMessage("创建成功");
        } catch (Exception e) {
            log.error("Error creating scene", e);
            result.setRequestStatus(500);
            result.setMessage("创建场景失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取场景列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getSceneList(
            @RequestParam String userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("Get scene list: userId={}, status={}", userId, status);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<CollaborationScene> scenes = collaborationService.getSceneList(userId, status, page, pageSize);
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            for (CollaborationScene scene : scenes) {
                data.add(convertSceneToMap(scene));
            }
            result.setData(data);
            result.setSize(collaborationService.getSceneCount(userId));
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting scene list", e);
            result.setRequestStatus(500);
            result.setMessage("获取场景列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取场景详情
     */
    @GetMapping("/{sceneId}")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSceneDetail(@PathVariable String sceneId) {
        log.info("Get scene detail: {}", sceneId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            CollaborationScene scene = collaborationService.getSceneDetail(sceneId);
            if (scene != null) {
                result.setData(convertSceneToMap(scene));
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("场景不存在");
            }
        } catch (Exception e) {
            log.error("Error getting scene detail", e);
            result.setRequestStatus(500);
            result.setMessage("获取场景详情失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 更新场景
     */
    @PutMapping("/{sceneId}")
    @ResponseBody
    public ResultModel<Map<String, Object>> updateScene(
            @PathVariable String sceneId,
            @RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        @SuppressWarnings("unchecked")
        List<String> skillIds = (List<String>) request.get("skillIds");

        log.info("Update scene: {}", sceneId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            CollaborationScene scene = collaborationService.updateScene(sceneId, name, description, skillIds);
            if (scene != null) {
                result.setData(convertSceneToMap(scene));
                result.setRequestStatus(200);
                result.setMessage("更新成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("场景不存在");
            }
        } catch (Exception e) {
            log.error("Error updating scene", e);
            result.setRequestStatus(500);
            result.setMessage("更新场景失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 删除场景
     */
    @DeleteMapping("/{sceneId}")
    @ResponseBody
    public ResultModel<Boolean> deleteScene(
            @PathVariable String sceneId,
            @RequestParam String operatorId) {
        log.info("Delete scene: {}, operator: {}", sceneId, operatorId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = collaborationService.deleteScene(sceneId, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 400);
            result.setMessage(success ? "删除成功" : "删除失败，无权限或场景不存在");
        } catch (Exception e) {
            log.error("Error deleting scene", e);
            result.setRequestStatus(500);
            result.setMessage("删除场景失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 添加成员
     */
    @PostMapping("/{sceneId}/member")
    @ResponseBody
    public ResultModel<Boolean> addMember(
            @PathVariable String sceneId,
            @RequestBody Map<String, Object> request) {
        String memberId = (String) request.get("memberId");
        String memberName = (String) request.get("memberName");
        String role = (String) request.get("role");
        String operatorId = (String) request.get("operatorId");

        log.info("Add member to scene: scene={}, member={}", sceneId, memberId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = collaborationService.addMember(sceneId, memberId, memberName, role, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 400);
            result.setMessage(success ? "添加成功" : "添加失败，无权限或成员已存在");
        } catch (Exception e) {
            log.error("Error adding member", e);
            result.setRequestStatus(500);
            result.setMessage("添加成员失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 移除成员
     */
    @DeleteMapping("/{sceneId}/member/{memberId}")
    @ResponseBody
    public ResultModel<Boolean> removeMember(
            @PathVariable String sceneId,
            @PathVariable String memberId,
            @RequestParam String operatorId) {
        log.info("Remove member from scene: scene={}, member={}", sceneId, memberId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = collaborationService.removeMember(sceneId, memberId, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 400);
            result.setMessage(success ? "移除成功" : "移除失败，无权限或成员不存在");
        } catch (Exception e) {
            log.error("Error removing member", e);
            result.setRequestStatus(500);
            result.setMessage("移除成员失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取成员列表
     */
    @GetMapping("/{sceneId}/members")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getMembers(@PathVariable String sceneId) {
        log.info("Get members for scene: {}", sceneId);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<SceneMember> members = collaborationService.getMembers(sceneId);
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            for (SceneMember member : members) {
                data.add(convertMemberToMap(member));
            }
            result.setData(data);
            result.setSize(data.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting members", e);
            result.setRequestStatus(500);
            result.setMessage("获取成员列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 生成场景密钥
     */
    @PostMapping("/{sceneId}/key")
    @ResponseBody
    public ResultModel<Map<String, Object>> generateSceneKey(
            @PathVariable String sceneId,
            @RequestParam String operatorId) {
        log.info("Generate scene key: scene={}, operator={}", sceneId, operatorId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String newKey = collaborationService.generateSceneKey(sceneId, operatorId);
            if (newKey != null) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("sceneId", sceneId);
                data.put("sceneKey", newKey);
                data.put("generatedAt", System.currentTimeMillis());
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("生成成功");
            } else {
                result.setRequestStatus(400);
                result.setMessage("生成失败，无权限或场景不存在");
            }
        } catch (Exception e) {
            log.error("Error generating scene key", e);
            result.setRequestStatus(500);
            result.setMessage("生成场景密钥失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 更新场景状态
     */
    @PostMapping("/{sceneId}/status")
    @ResponseBody
    public ResultModel<Boolean> updateStatus(
            @PathVariable String sceneId,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        String operatorId = request.get("operatorId");

        log.info("Update scene status: scene={}, status={}", sceneId, status);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = collaborationService.updateStatus(sceneId, status, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 400);
            result.setMessage(success ? "状态更新成功" : "状态更新失败，无权限或场景不存在");
        } catch (Exception e) {
            log.error("Error updating scene status", e);
            result.setRequestStatus(500);
            result.setMessage("更新场景状态失败: " + e.getMessage());
        }
        return result;
    }

    private Map<String, Object> convertSceneToMap(CollaborationScene scene) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sceneId", scene.getSceneId());
        map.put("name", scene.getName());
        map.put("description", scene.getDescription());
        map.put("ownerId", scene.getOwnerId());
        map.put("ownerName", scene.getOwnerName());
        map.put("skillIds", scene.getSkillIds());
        map.put("status", scene.getStatus() != null ? scene.getStatus().name() : "CREATED");
        map.put("sceneKey", scene.getSceneKey());
        map.put("createTime", scene.getCreateTime());
        map.put("updateTime", scene.getUpdateTime());
        return map;
    }

    private Map<String, Object> convertMemberToMap(SceneMember member) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("memberId", member.getMemberId());
        map.put("memberName", member.getMemberName());
        map.put("role", member.getRole());
        map.put("joinedAt", member.getJoinedAt());
        map.put("online", member.isOnline());
        return map;
    }
}
