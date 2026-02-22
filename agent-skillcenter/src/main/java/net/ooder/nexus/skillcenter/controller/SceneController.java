package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.scene.*;
import net.ooder.skillcenter.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenes")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SceneController extends BaseController {

    private final SceneService sceneService;

    @Autowired
    public SceneController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @PostMapping("/create")
    public ResultModel<SceneDefinitionDTO> create(@RequestBody SceneDefinitionDTO definition) {
        long startTime = System.currentTimeMillis();
        logRequestStart("create", definition);

        try {
            SceneDefinitionDTO result = sceneService.create(definition);
            logRequestEnd("create", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("create", e);
            return ResultModel.error(500, "创建场景失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResultModel<Boolean> delete(@RequestBody SceneIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("delete", request);

        try {
            boolean result = sceneService.delete(request.getSceneId());
            logRequestEnd("delete", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("delete", e);
            return ResultModel.error(500, "删除场景失败: " + e.getMessage());
        }
    }

    @PostMapping("/get")
    public ResultModel<SceneDefinitionDTO> get(@RequestBody SceneIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("get", request);

        try {
            SceneDefinitionDTO result = sceneService.get(request.getSceneId());
            if (result == null) {
                logRequestEnd("get", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("场景不存在");
            }
            logRequestEnd("get", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("get", e);
            return ResultModel.error(500, "获取场景失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    public ResultModel<PageResult<SceneDefinitionDTO>> listAll(@RequestBody PageRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listAll", request);

        try {
            PageResult<SceneDefinitionDTO> result = sceneService.listAll(
                request.getPageNum(), request.getPageSize());
            logRequestEnd("listAll", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listAll", e);
            return ResultModel.error(500, "获取场景列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/activate")
    public ResultModel<Boolean> activate(@RequestBody SceneIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("activate", request);

        try {
            boolean result = sceneService.activate(request.getSceneId());
            logRequestEnd("activate", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("activate", e);
            return ResultModel.error(500, "激活场景失败: " + e.getMessage());
        }
    }

    @PostMapping("/deactivate")
    public ResultModel<Boolean> deactivate(@RequestBody SceneIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deactivate", request);

        try {
            boolean result = sceneService.deactivate(request.getSceneId());
            logRequestEnd("deactivate", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("deactivate", e);
            return ResultModel.error(500, "停用场景失败: " + e.getMessage());
        }
    }

    @PostMapping("/state")
    public ResultModel<SceneStateDTO> getState(@RequestBody SceneIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getState", request);

        try {
            SceneStateDTO result = sceneService.getState(request.getSceneId());
            logRequestEnd("getState", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getState", e);
            return ResultModel.error(500, "获取场景状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/capabilities/add")
    public ResultModel<Boolean> addCapability(@RequestBody CapabilityRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("addCapability", request);

        try {
            boolean result = sceneService.addCapability(request.getSceneId(), request.getCapability());
            logRequestEnd("addCapability", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("addCapability", e);
            return ResultModel.error(500, "添加能力失败: " + e.getMessage());
        }
    }

    @PostMapping("/capabilities/remove")
    public ResultModel<Boolean> removeCapability(@RequestBody CapabilityIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("removeCapability", request);

        try {
            boolean result = sceneService.removeCapability(request.getSceneId(), request.getCapId());
            logRequestEnd("removeCapability", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("removeCapability", e);
            return ResultModel.error(500, "移除能力失败: " + e.getMessage());
        }
    }

    @PostMapping("/capabilities/list")
    public ResultModel<PageResult<CapabilityDTO>> listCapabilities(@RequestBody SceneIdPageRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listCapabilities", request);

        try {
            PageResult<CapabilityDTO> result = sceneService.listCapabilities(
                request.getSceneId(), request.getPageNum(), request.getPageSize());
            logRequestEnd("listCapabilities", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listCapabilities", e);
            return ResultModel.error(500, "获取能力列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/capabilities/get")
    public ResultModel<CapabilityDTO> getCapability(@RequestBody CapabilityIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getCapability", request);

        try {
            CapabilityDTO result = sceneService.getCapability(request.getSceneId(), request.getCapId());
            logRequestEnd("getCapability", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getCapability", e);
            return ResultModel.error(500, "获取能力失败: " + e.getMessage());
        }
    }

    @PostMapping("/collaborative/add")
    public ResultModel<Boolean> addCollaborativeScene(@RequestBody CollaborativeRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("addCollaborativeScene", request);

        try {
            boolean result = sceneService.addCollaborativeScene(
                request.getSceneId(), request.getCollaborativeSceneId());
            logRequestEnd("addCollaborativeScene", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("addCollaborativeScene", e);
            return ResultModel.error(500, "添加协作场景失败: " + e.getMessage());
        }
    }

    @PostMapping("/collaborative/remove")
    public ResultModel<Boolean> removeCollaborativeScene(@RequestBody CollaborativeRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("removeCollaborativeScene", request);

        try {
            boolean result = sceneService.removeCollaborativeScene(
                request.getSceneId(), request.getCollaborativeSceneId());
            logRequestEnd("removeCollaborativeScene", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("removeCollaborativeScene", e);
            return ResultModel.error(500, "移除协作场景失败: " + e.getMessage());
        }
    }

    @PostMapping("/collaborative/list")
    public ResultModel<PageResult<String>> listCollaborativeScenes(@RequestBody SceneIdPageRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listCollaborativeScenes", request);

        try {
            PageResult<String> result = sceneService.listCollaborativeScenes(
                request.getSceneId(), request.getPageNum(), request.getPageSize());
            logRequestEnd("listCollaborativeScenes", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listCollaborativeScenes", e);
            return ResultModel.error(500, "获取协作场景列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/snapshot/create")
    public ResultModel<SceneSnapshotDTO> createSnapshot(@RequestBody SceneIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createSnapshot", request);

        try {
            SceneSnapshotDTO result = sceneService.createSnapshot(request.getSceneId());
            logRequestEnd("createSnapshot", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("createSnapshot", e);
            return ResultModel.error(500, "创建快照失败: " + e.getMessage());
        }
    }

    @PostMapping("/snapshot/restore")
    public ResultModel<Boolean> restoreSnapshot(@RequestBody SnapshotRestoreRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("restoreSnapshot", request);

        try {
            boolean result = sceneService.restoreSnapshot(request.getSceneId(), request.getSnapshot());
            logRequestEnd("restoreSnapshot", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("restoreSnapshot", e);
            return ResultModel.error(500, "恢复快照失败: " + e.getMessage());
        }
    }

    public static class SceneIdRequest {
        private String sceneId;
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    }

    public static class PageRequest {
        private int pageNum = 1;
        private int pageSize = 10;
        public int getPageNum() { return pageNum; }
        public void setPageNum(int pageNum) { this.pageNum = pageNum; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    }

    public static class SceneIdPageRequest extends SceneIdRequest {
        private int pageNum = 1;
        private int pageSize = 10;
        public int getPageNum() { return pageNum; }
        public void setPageNum(int pageNum) { this.pageNum = pageNum; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    }

    public static class CapabilityRequest extends SceneIdRequest {
        private CapabilityDTO capability;
        public CapabilityDTO getCapability() { return capability; }
        public void setCapability(CapabilityDTO capability) { this.capability = capability; }
    }

    public static class CapabilityIdRequest extends SceneIdRequest {
        private String capId;
        public String getCapId() { return capId; }
        public void setCapId(String capId) { this.capId = capId; }
    }

    public static class CollaborativeRequest extends SceneIdRequest {
        private String collaborativeSceneId;
        public String getCollaborativeSceneId() { return collaborativeSceneId; }
        public void setCollaborativeSceneId(String collaborativeSceneId) { this.collaborativeSceneId = collaborativeSceneId; }
    }

    public static class SnapshotRestoreRequest extends SceneIdRequest {
        private SceneSnapshotDTO snapshot;
        public SceneSnapshotDTO getSnapshot() { return snapshot; }
        public void setSnapshot(SceneSnapshotDTO snapshot) { this.snapshot = snapshot; }
    }
}
