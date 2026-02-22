package net.ooder.nexus.adapter.inbound.controller.scene;

import net.ooder.nexus.service.scene.SceneDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scene/local")
public class LocalSceneController {

    private static final Logger log = LoggerFactory.getLogger(LocalSceneController.class);

    @Autowired
    private SceneDefinitionService sceneDefinitionService;

    @GetMapping("/overview")
    public Map<String, Object> getOverview() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> overview = sceneDefinitionService.getLocalSceneOverview();
            result.put("requestStatus", 200);
            result.put("data", overview);
        } catch (Exception e) {
            log.error("Failed to get local scene overview", e);
            result.put("requestStatus", 500);
            result.put("message", "获取概览失败: " + e.getMessage());
        }
        return result;
    }
}
