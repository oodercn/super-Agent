package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.install.InstallSceneService;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/install-scene")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InstallSceneController {

    @Autowired
    private InstallSceneService installSceneService;

    @GetMapping("/steps")
    public ResultModel<List<InstallSceneService.InstallStep>> getInstallSteps(
            @RequestParam(defaultValue = "micro") String profile) {
        List<InstallSceneService.InstallStep> steps = installSceneService.getInstallSteps(profile);
        return ResultModel.success(steps);
    }

    @GetMapping("/skills")
    public ResultModel<List<InstallSceneService.SkillPackage>> getRequiredSkills(
            @RequestParam(defaultValue = "micro") String profile) {
        List<InstallSceneService.SkillPackage> skills = installSceneService.getRequiredSkills(profile);
        return ResultModel.success(skills);
    }

    @GetMapping(value = "/start", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter startInstall(
            @RequestParam(defaultValue = "micro") String profile,
            @RequestParam(required = false) Map<String, Object> config) {
        return installSceneService.startInstallWithProgress(profile, config);
    }

    @GetMapping("/progress/{installId}")
    public ResultModel<InstallSceneService.InstallProgress> getProgress(@PathVariable String installId) {
        InstallSceneService.InstallProgress progress = installSceneService.getProgress(installId);
        if (progress == null) {
            return ResultModel.notFound("Install not found: " + installId);
        }
        return ResultModel.success(progress);
    }
}
