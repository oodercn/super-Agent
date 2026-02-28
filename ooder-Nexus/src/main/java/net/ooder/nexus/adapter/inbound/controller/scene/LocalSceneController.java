package net.ooder.nexus.adapter.inbound.controller.scene;

import net.ooder.nexus.dto.scene.LocalSceneOverviewDTO;
import net.ooder.nexus.model.ApiResponse;
import net.ooder.nexus.service.scene.SceneDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scene/local")
public class LocalSceneController {

    private static final Logger log = LoggerFactory.getLogger(LocalSceneController.class);

    @Autowired
    private SceneDefinitionService sceneDefinitionService;

    @GetMapping("/overview")
    public ApiResponse<LocalSceneOverviewDTO> getOverview() {
        try {
            Map<String, Object> overview = sceneDefinitionService.getLocalSceneOverview();
            LocalSceneOverviewDTO dto = convertToDTO(overview);
            return ApiResponse.success(dto);
        } catch (Exception e) {
            log.error("Failed to get local scene overview", e);
            return ApiResponse.error("获取概览失败: " + e.getMessage());
        }
    }

    private LocalSceneOverviewDTO convertToDTO(Map<String, Object> map) {
        LocalSceneOverviewDTO dto = new LocalSceneOverviewDTO();
        
        Object totalScenes = map.get("totalScenes");
        if (totalScenes instanceof Number) {
            dto.setTotalScenes(((Number) totalScenes).intValue());
        }
        
        Object activeScenes = map.get("activeScenes");
        if (activeScenes instanceof Number) {
            dto.setActiveScenes(((Number) activeScenes).intValue());
        }
        
        Object archivedScenes = map.get("archivedScenes");
        if (archivedScenes instanceof Number) {
            dto.setArchivedScenes(((Number) archivedScenes).intValue());
        }
        
        return dto;
    }
}
