package net.ooder.nexus.adapter.inbound.controller.scene;

import net.ooder.nexus.domain.scene.model.SceneDefinition;
import net.ooder.nexus.dto.scene.*;
import net.ooder.nexus.model.ApiResponse;
import net.ooder.nexus.service.scene.SceneDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scene/definition")
public class SceneDefinitionController {

    private static final Logger log = LoggerFactory.getLogger(SceneDefinitionController.class);

    @Autowired
    private SceneDefinitionService sceneDefinitionService;

    @PostMapping("/create")
    public ApiResponse<SceneDefinition> createDefinition(@RequestBody SceneDefinition definition) {
        try {
            SceneDefinition created = sceneDefinitionService.createSceneDefinition(definition);
            return ApiResponse.success("场景创建成功", created);
        } catch (Exception e) {
            log.error("Failed to create scene definition", e);
            return ApiResponse.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/get")
    public ApiResponse<SceneDefinition> getDefinition(@RequestBody SceneIdDTO request) {
        try {
            String sceneId = request.getSceneId();
            SceneDefinition definition = sceneDefinitionService.getSceneDefinition(sceneId);
            if (definition != null) {
                return ApiResponse.success(definition);
            } else {
                return ApiResponse.notFound("场景不存在");
            }
        } catch (Exception e) {
            log.error("Failed to get scene definition", e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    public ApiResponse<List<SceneDefinition>> listDefinitions(@RequestBody SceneListQueryDTO request) {
        try {
            String type = request.getType();
            String status = request.getStatus();
            int page = request.getPage() != null ? request.getPage() : 0;
            int size = request.getSize() != null ? request.getSize() : 20;

            List<SceneDefinition> definitions = sceneDefinitionService.listSceneDefinitions(type, status, page, size);
            return ApiResponse.success(definitions);
        } catch (Exception e) {
            log.error("Failed to list scene definitions", e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ApiResponse<SceneDefinition> updateDefinition(@RequestBody SceneDefinition definition) {
        try {
            SceneDefinition updated = sceneDefinitionService.updateSceneDefinition(definition);
            if (updated != null) {
                return ApiResponse.success("更新成功", updated);
            } else {
                return ApiResponse.notFound("场景不存在");
            }
        } catch (Exception e) {
            log.error("Failed to update scene definition", e);
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ApiResponse<Void> deleteDefinition(@RequestBody SceneDeleteDTO request) {
        try {
            String sceneId = request.getSceneId();
            boolean confirm = Boolean.TRUE.equals(request.getConfirm());

            boolean deleted = sceneDefinitionService.deleteSceneDefinition(sceneId, confirm);
            if (deleted) {
                return ApiResponse.success("删除成功");
            } else {
                return ApiResponse.badRequest("删除失败，请确认删除操作");
            }
        } catch (Exception e) {
            log.error("Failed to delete scene definition", e);
            return ApiResponse.error("删除失败: " + e.getMessage());
        }
    }
}
