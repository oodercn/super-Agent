package net.ooder.nexus.adapter.inbound.controller.scene;

import net.ooder.nexus.domain.scene.model.SceneDefinition;
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
@RequestMapping("/api/scene/definition")
public class SceneDefinitionController {

    private static final Logger log = LoggerFactory.getLogger(SceneDefinitionController.class);

    @Autowired
    private SceneDefinitionService sceneDefinitionService;

    @PostMapping("/create")
    public Map<String, Object> createDefinition(@RequestBody SceneDefinition definition) {
        Map<String, Object> result = new HashMap<>();
        try {
            SceneDefinition created = sceneDefinitionService.createSceneDefinition(definition);
            result.put("requestStatus", 200);
            result.put("message", "场景创建成功");
            result.put("data", created);
        } catch (Exception e) {
            log.error("Failed to create scene definition", e);
            result.put("requestStatus", 500);
            result.put("message", "创建失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/get")
    public Map<String, Object> getDefinition(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String sceneId = request.get("sceneId");
            SceneDefinition definition = sceneDefinitionService.getSceneDefinition(sceneId);
            if (definition != null) {
                result.put("requestStatus", 200);
                result.put("data", definition);
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景不存在");
            }
        } catch (Exception e) {
            log.error("Failed to get scene definition", e);
            result.put("requestStatus", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/list")
    public Map<String, Object> listDefinitions(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String type = (String) request.get("type");
            String status = (String) request.get("status");
            int page = request.containsKey("page") ? ((Number) request.get("page")).intValue() : 0;
            int size = request.containsKey("size") ? ((Number) request.get("size")).intValue() : 20;

            List<SceneDefinition> definitions = sceneDefinitionService.listSceneDefinitions(type, status, page, size);
            result.put("requestStatus", 200);
            result.put("data", definitions);
        } catch (Exception e) {
            log.error("Failed to list scene definitions", e);
            result.put("requestStatus", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> updateDefinition(@RequestBody SceneDefinition definition) {
        Map<String, Object> result = new HashMap<>();
        try {
            SceneDefinition updated = sceneDefinitionService.updateSceneDefinition(definition);
            if (updated != null) {
                result.put("requestStatus", 200);
                result.put("message", "更新成功");
                result.put("data", updated);
            } else {
                result.put("requestStatus", 404);
                result.put("message", "场景不存在");
            }
        } catch (Exception e) {
            log.error("Failed to update scene definition", e);
            result.put("requestStatus", 500);
            result.put("message", "更新失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> deleteDefinition(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String sceneId = (String) request.get("sceneId");
            boolean confirm = Boolean.TRUE.equals(request.get("confirm"));

            boolean deleted = sceneDefinitionService.deleteSceneDefinition(sceneId, confirm);
            if (deleted) {
                result.put("requestStatus", 200);
                result.put("message", "删除成功");
            } else {
                result.put("requestStatus", 400);
                result.put("message", "删除失败，请确认删除操作");
            }
        } catch (Exception e) {
            log.error("Failed to delete scene definition", e);
            result.put("requestStatus", 500);
            result.put("message", "删除失败: " + e.getMessage());
        }
        return result;
    }
}
