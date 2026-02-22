package net.ooder.nexus.adapter.inbound.controller.scene;

import net.ooder.nexus.domain.scene.model.SceneInstance;
import net.ooder.nexus.service.scene.SceneDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scene/instance")
public class SceneInstanceController {

    private static final Logger log = LoggerFactory.getLogger(SceneInstanceController.class);

    @Autowired
    private SceneDefinitionService sceneDefinitionService;

    @PostMapping("/create")
    public Map<String, Object> createInstance(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String sceneId = (String) request.get("sceneId");
            String instanceName = (String) request.get("instanceName");
            
            @SuppressWarnings("unchecked")
            Map<String, Object> config = (Map<String, Object>) request.get("config");

            SceneInstance instance = sceneDefinitionService.createSceneInstance(sceneId, instanceName, config);
            if (instance != null) {
                result.put("requestStatus", 200);
                result.put("message", "场景实例创建成功");
                result.put("data", instance);
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景定义不存在");
            }
        } catch (Exception e) {
            log.error("Failed to create scene instance", e);
            result.put("requestStatus", 500);
            result.put("message", "创建失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/get")
    public Map<String, Object> getInstance(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String instanceId = request.get("instanceId");
            SceneInstance instance = sceneDefinitionService.getSceneInstance(instanceId);
            if (instance != null) {
                result.put("requestStatus", 200);
                result.put("data", instance);
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to get scene instance", e);
            result.put("requestStatus", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/list")
    public Map<String, Object> listInstances(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String ownerId = (String) request.get("ownerId");
            int page = request.containsKey("page") ? ((Number) request.get("page")).intValue() : 0;
            int size = request.containsKey("size") ? ((Number) request.get("size")).intValue() : 20;

            List<SceneInstance> instances = sceneDefinitionService.listSceneInstances(ownerId, page, size);
            result.put("requestStatus", 200);
            result.put("data", instances);
        } catch (Exception e) {
            log.error("Failed to list scene instances", e);
            result.put("requestStatus", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{instanceId}/start")
    public Map<String, Object> startInstance(@PathVariable String instanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean started = sceneDefinitionService.startSceneInstance(instanceId);
            if (started) {
                result.put("requestStatus", 200);
                result.put("message", "场景实例已启动");
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to start scene instance", e);
            result.put("requestStatus", 500);
            result.put("message", "启动失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{instanceId}/stop")
    public Map<String, Object> stopInstance(@PathVariable String instanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean stopped = sceneDefinitionService.stopSceneInstance(instanceId);
            if (stopped) {
                result.put("requestStatus", 200);
                result.put("message", "场景实例已停止");
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to stop scene instance", e);
            result.put("requestStatus", 500);
            result.put("message", "停止失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{instanceId}/pause")
    public Map<String, Object> pauseInstance(@PathVariable String instanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean paused = sceneDefinitionService.pauseSceneInstance(instanceId);
            if (paused) {
                result.put("requestStatus", 200);
                result.put("message", "场景实例已暂停");
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to pause scene instance", e);
            result.put("requestStatus", 500);
            result.put("message", "暂停失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{instanceId}/resume")
    public Map<String, Object> resumeInstance(@PathVariable String instanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean resumed = sceneDefinitionService.resumeSceneInstance(instanceId);
            if (resumed) {
                result.put("requestStatus", 200);
                result.put("message", "场景实例已恢复");
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to resume scene instance", e);
            result.put("requestStatus", 500);
            result.put("message", "恢复失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{instanceId}/archive")
    public Map<String, Object> archiveInstance(@PathVariable String instanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean archived = sceneDefinitionService.archiveSceneInstance(instanceId);
            if (archived) {
                result.put("requestStatus", 200);
                result.put("message", "场景实例已归档");
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to archive scene instance", e);
            result.put("requestStatus", 500);
            result.put("message", "归档失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{instanceId}/invite")
    public Map<String, Object> generateInviteCode(@PathVariable String instanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            String inviteCode = sceneDefinitionService.generateInviteCode(instanceId);
            if (inviteCode != null) {
                result.put("requestStatus", 200);
                result.put("data", new HashMap<String, Object>() {{
                    put("instanceId", instanceId);
                    put("inviteCode", inviteCode);
                }});
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to generate invite code", e);
            result.put("requestStatus", 500);
            result.put("message", "生成邀请码失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/{instanceId}/join")
    public Map<String, Object> joinInstance(@PathVariable String instanceId, @RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String inviteCode = request.get("inviteCode");
            String memberId = request.get("memberId");

            boolean joined = sceneDefinitionService.joinSceneInstance(instanceId, inviteCode, memberId);
            if (joined) {
                result.put("requestStatus", 200);
                result.put("message", "加入成功");
            } else {
                result.put("requestStatus", 400);
                result.put("message", "加入失败，邀请码无效或场景不存在");
            }
        } catch (Exception e) {
            log.error("Failed to join scene instance", e);
            result.put("requestStatus", 500);
            result.put("message", "加入失败: " + e.getMessage());
        }
        return result;
    }
}
