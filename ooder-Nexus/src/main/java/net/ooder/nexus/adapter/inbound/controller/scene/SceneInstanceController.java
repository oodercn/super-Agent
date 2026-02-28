package net.ooder.nexus.adapter.inbound.controller.scene;

import net.ooder.nexus.domain.scene.model.SceneInstance;
import net.ooder.nexus.dto.scene.*;
import net.ooder.nexus.model.ApiResponse;
import net.ooder.nexus.service.scene.SceneDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scene/instance")
public class SceneInstanceController {

    private static final Logger log = LoggerFactory.getLogger(SceneInstanceController.class);

    @Autowired
    private SceneDefinitionService sceneDefinitionService;

    @PostMapping("/create")
    public ApiResponse<SceneInstance> createInstance(@RequestBody SceneInstanceCreateDTO request) {
        try {
            String sceneId = request.getSceneId();
            String instanceName = request.getInstanceName();
            
            SceneInstance instance = sceneDefinitionService.createSceneInstance(sceneId, instanceName, request.getConfig());
            if (instance != null) {
                return ApiResponse.success("场景实例创建成功", instance);
            } else {
                return ApiResponse.notFound("场景定义不存在");
            }
        } catch (Exception e) {
            log.error("Failed to create scene instance", e);
            return ApiResponse.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/get")
    public ApiResponse<SceneInstance> getInstance(@RequestBody SceneInstanceQueryDTO request) {
        try {
            String instanceId = request.getInstanceId();
            SceneInstance instance = sceneDefinitionService.getSceneInstance(instanceId);
            if (instance != null) {
                return ApiResponse.success(instance);
            } else {
                return ApiResponse.notFound("场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to get scene instance", e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    public ApiResponse<List<SceneInstance>> listInstances(@RequestBody SceneInstanceListDTO request) {
        try {
            String ownerId = request.getOwnerId();
            int page = request.getPage() != null ? request.getPage() : 0;
            int size = request.getSize() != null ? request.getSize() : 20;

            List<SceneInstance> instances = sceneDefinitionService.listSceneInstances(ownerId, page, size);
            return ApiResponse.success(instances);
        } catch (Exception e) {
            log.error("Failed to list scene instances", e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/{instanceId}/start")
    public ApiResponse<Void> startInstance(@PathVariable String instanceId) {
        try {
            boolean started = sceneDefinitionService.startSceneInstance(instanceId);
            if (started) {
                return ApiResponse.success("场景实例已启动");
            } else {
                return ApiResponse.notFound("场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to start scene instance", e);
            return ApiResponse.error("启动失败: " + e.getMessage());
        }
    }

    @PostMapping("/{instanceId}/stop")
    public ApiResponse<Void> stopInstance(@PathVariable String instanceId) {
        try {
            boolean stopped = sceneDefinitionService.stopSceneInstance(instanceId);
            if (stopped) {
                return ApiResponse.success("场景实例已停止");
            } else {
                return ApiResponse.notFound("场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to stop scene instance", e);
            return ApiResponse.error("停止失败: " + e.getMessage());
        }
    }

    @PostMapping("/{instanceId}/pause")
    public ApiResponse<Void> pauseInstance(@PathVariable String instanceId) {
        try {
            boolean paused = sceneDefinitionService.pauseSceneInstance(instanceId);
            if (paused) {
                return ApiResponse.success("场景实例已暂停");
            } else {
                return ApiResponse.notFound("场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to pause scene instance", e);
            return ApiResponse.error("暂停失败: " + e.getMessage());
        }
    }

    @PostMapping("/{instanceId}/resume")
    public ApiResponse<Void> resumeInstance(@PathVariable String instanceId) {
        try {
            boolean resumed = sceneDefinitionService.resumeSceneInstance(instanceId);
            if (resumed) {
                return ApiResponse.success("场景实例已恢复");
            } else {
                return ApiResponse.notFound("场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to resume scene instance", e);
            return ApiResponse.error("恢复失败: " + e.getMessage());
        }
    }

    @PostMapping("/{instanceId}/archive")
    public ApiResponse<Void> archiveInstance(@PathVariable String instanceId) {
        try {
            boolean archived = sceneDefinitionService.archiveSceneInstance(instanceId);
            if (archived) {
                return ApiResponse.success("场景实例已归档");
            } else {
                return ApiResponse.notFound("场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to archive scene instance", e);
            return ApiResponse.error("归档失败: " + e.getMessage());
        }
    }

    @PostMapping("/{instanceId}/invite")
    public ApiResponse<SceneInviteCodeDTO> generateInviteCode(@PathVariable String instanceId) {
        try {
            String inviteCode = sceneDefinitionService.generateInviteCode(instanceId);
            if (inviteCode != null) {
                SceneInviteCodeDTO data = new SceneInviteCodeDTO();
                data.setInstanceId(instanceId);
                data.setInviteCode(inviteCode);
                return ApiResponse.success(data);
            } else {
                return ApiResponse.notFound("场景实例不存在");
            }
        } catch (Exception e) {
            log.error("Failed to generate invite code", e);
            return ApiResponse.error("生成邀请码失败: " + e.getMessage());
        }
    }

    @PostMapping("/{instanceId}/join")
    public ApiResponse<Void> joinInstance(@PathVariable String instanceId, @RequestBody SceneJoinDTO request) {
        try {
            String inviteCode = request.getInviteCode();
            String memberId = request.getMemberId();

            boolean joined = sceneDefinitionService.joinSceneInstance(instanceId, inviteCode, memberId);
            if (joined) {
                return ApiResponse.success("加入成功");
            } else {
                return ApiResponse.badRequest("加入失败，邀请码无效或场景不存在");
            }
        } catch (Exception e) {
            log.error("Failed to join scene instance", e);
            return ApiResponse.error("加入失败: " + e.getMessage());
        }
    }
}
