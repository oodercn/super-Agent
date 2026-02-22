package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.dto.install.*;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.skillcenter.manager.SkillManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/install")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class InstallController extends BaseController {

    private final SkillMarketManager marketManager;
    private final SkillManager skillManager;
    private final Map<String, InstallProgressDTO> installProgressMap;
    private final ExecutorService executorService;

    public InstallController() {
        this.marketManager = SkillMarketManager.getInstance();
        this.skillManager = SkillManager.getInstance();
        this.installProgressMap = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(5);
    }

    @PostMapping("/check-dependencies")
    public ResultModel<DependencyCheckResultDTO> checkDependencies(@RequestBody InstallRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("checkDependencies", request);

        try {
            String skillId = request.getSkillId();
            if (skillId == null || skillId.isEmpty()) {
                return ResultModel.badRequest("技能ID不能为空");
            }

            SkillListing listing = marketManager.getSkillListing(skillId);
            if (listing == null) {
                return ResultModel.notFound("技能不存在");
            }

            DependencyCheckResultDTO result = new DependencyCheckResultDTO();
            result.setSkillId(skillId);

            List<DependencyDTO> dependencies = new ArrayList<>();
            List<String> missingDependencies = new ArrayList<>();
            List<String> conflictDependencies = new ArrayList<>();

            List<String> requiredDeps = new ArrayList<>();
            if (listing.getCapabilities() != null) {
                requiredDeps.addAll(listing.getCapabilities().subList(0, Math.min(2, listing.getCapabilities().size())));
            }
            if (requiredDeps != null && !requiredDeps.isEmpty()) {
                for (String dep : requiredDeps) {
                    DependencyDTO depDto = new DependencyDTO();
                    depDto.setDependencyId(dep);
                    depDto.setName(dep);
                    depDto.setRequiredVersion("1.0.0");
                    
                    boolean installed = skillManager.getSkill(dep) != null;
                    depDto.setInstalled(installed);
                    depDto.setCompatible(installed);
                    
                    dependencies.add(depDto);
                    
                    if (!installed) {
                        missingDependencies.add(dep);
                    }
                }
            }

            result.setDependencies(dependencies);
            result.setMissingDependencies(missingDependencies);
            result.setConflictDependencies(conflictDependencies);
            result.setCanInstall(conflictDependencies.isEmpty());

            if (!missingDependencies.isEmpty()) {
                result.setMessage("缺少 " + missingDependencies.size() + " 个依赖技能");
            } else if (!conflictDependencies.isEmpty()) {
                result.setMessage("存在 " + conflictDependencies.size() + " 个版本冲突");
            } else {
                result.setMessage("依赖检查通过，可以安装");
            }

            logRequestEnd("checkDependencies", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("checkDependencies", e);
            return ResultModel.error(500, "依赖检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/start")
    public ResultModel<String> startInstall(@RequestBody InstallRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("startInstall", request);

        try {
            String skillId = request.getSkillId();
            if (skillId == null || skillId.isEmpty()) {
                return ResultModel.badRequest("技能ID不能为空");
            }

            SkillListing listing = marketManager.getSkillListing(skillId);
            if (listing == null) {
                return ResultModel.notFound("技能不存在");
            }

            String installId = "install-" + UUID.randomUUID().toString().substring(0, 8);
            
            InstallProgressDTO progress = createInitialProgress(installId, skillId, listing.getName());
            installProgressMap.put(installId, progress);

            final boolean autoInstallDeps = request.isAutoInstallDependencies();
            
            executorService.submit(() -> {
                try {
                    executeInstallPipeline(installId, skillId, listing, autoInstallDeps);
                } catch (Exception e) {
                    updateProgressOnError(installId, e.getMessage());
                }
            });

            logRequestEnd("startInstall", installId, System.currentTimeMillis() - startTime);
            return ResultModel.success(installId);
        } catch (Exception e) {
            logRequestError("startInstall", e);
            return ResultModel.error(500, "启动安装失败: " + e.getMessage());
        }
    }

    @PostMapping("/progress/{installId}")
    public ResultModel<InstallProgressDTO> getInstallProgress(@PathVariable String installId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getInstallProgress", installId);

        try {
            InstallProgressDTO progress = installProgressMap.get(installId);
            if (progress == null) {
                return ResultModel.notFound("安装任务不存在");
            }

            logRequestEnd("getInstallProgress", progress.getStatus(), System.currentTimeMillis() - startTime);
            return ResultModel.success(progress);
        } catch (Exception e) {
            logRequestError("getInstallProgress", e);
            return ResultModel.error(500, "获取安装进度失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{installId}")
    public ResultModel<Boolean> cancelInstall(@PathVariable String installId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("cancelInstall", installId);

        try {
            InstallProgressDTO progress = installProgressMap.get(installId);
            if (progress == null) {
                return ResultModel.notFound("安装任务不存在");
            }

            if ("completed".equals(progress.getStatus()) || "failed".equals(progress.getStatus())) {
                return ResultModel.error(400, "安装已完成，无法取消");
            }

            progress.setStatus("cancelled");
            progress.setEndTime(System.currentTimeMillis());
            progress.setDuration(progress.getEndTime() - progress.getStartTime());

            logRequestEnd("cancelInstall", true, System.currentTimeMillis() - startTime);
            return ResultModel.success(true);
        } catch (Exception e) {
            logRequestError("cancelInstall", e);
            return ResultModel.error(500, "取消安装失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    public ResultModel<List<InstallProgressDTO>> listInstalls() {
        long startTime = System.currentTimeMillis();
        logRequestStart("listInstalls", null);

        try {
            List<InstallProgressDTO> installs = new ArrayList<>(installProgressMap.values());
            logRequestEnd("listInstalls", installs.size() + " installs", System.currentTimeMillis() - startTime);
            return ResultModel.success(installs);
        } catch (Exception e) {
            logRequestError("listInstalls", e);
            return ResultModel.error(500, "获取安装列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/clear/{installId}")
    public ResultModel<Boolean> clearInstallRecord(@PathVariable String installId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("clearInstallRecord", installId);

        try {
            boolean result = installProgressMap.remove(installId) != null;
            logRequestEnd("clearInstallRecord", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("clearInstallRecord", e);
            return ResultModel.error(500, "清除安装记录失败: " + e.getMessage());
        }
    }

    private InstallProgressDTO createInitialProgress(String installId, String skillId, String skillName) {
        InstallProgressDTO progress = new InstallProgressDTO();
        progress.setInstallId(installId);
        progress.setSkillId(skillId);
        progress.setSkillName(skillName);
        progress.setStatus("pending");
        progress.setProgress(0);
        progress.setCurrentStage("准备安装");
        progress.setStartTime(System.currentTimeMillis());

        List<InstallStageDTO> stages = Arrays.asList(
            createStage("download", "下载技能包", "从源下载技能包文件"),
            createStage("validate", "验证完整性", "验证签名和文件完整性"),
            createStage("extract", "解压文件", "解压技能包到目标目录"),
            createStage("dependencies", "处理依赖", "检查并安装依赖技能"),
            createStage("configure", "配置技能", "应用配置和权限设置"),
            createStage("register", "注册技能", "注册技能到系统"),
            createStage("verify", "验证安装", "验证技能可正常使用")
        );
        progress.setStages(stages);

        return progress;
    }

    private InstallStageDTO createStage(String stageId, String name, String description) {
        InstallStageDTO stage = new InstallStageDTO();
        stage.setStageId(stageId);
        stage.setName(name);
        stage.setDescription(description);
        stage.setStatus("pending");
        stage.setProgress(0);
        return stage;
    }

    private void executeInstallPipeline(String installId, String skillId, SkillListing listing, boolean autoInstallDeps) {
        InstallProgressDTO progress = installProgressMap.get(installId);
        if (progress == null || "cancelled".equals(progress.getStatus())) {
            return;
        }

        List<InstallStageDTO> stages = progress.getStages();
        int totalStages = stages.size();
        int currentStageIndex = 0;

        try {
            for (InstallStageDTO stage : stages) {
                if ("cancelled".equals(progress.getStatus())) {
                    return;
                }

                stage.setStatus("running");
                stage.setStartTime(System.currentTimeMillis());
                progress.setCurrentStage(stage.getName());
                progress.setStatus("installing");

                executeStage(stage, skillId, listing, autoInstallDeps);

                stage.setStatus("completed");
                stage.setProgress(100);
                stage.setEndTime(System.currentTimeMillis());

                currentStageIndex++;
                progress.setProgress((currentStageIndex * 100) / totalStages);
            }

            progress.setStatus("completed");
            progress.setProgress(100);
            progress.setEndTime(System.currentTimeMillis());
            progress.setDuration(progress.getEndTime() - progress.getStartTime());
            progress.setCurrentStage("安装完成");

        } catch (Exception e) {
            updateProgressOnError(installId, e.getMessage());
        }
    }

    private void executeStage(InstallStageDTO stage, String skillId, SkillListing listing, boolean autoInstallDeps) throws Exception {
        String stageId = stage.getStageId();
        
        switch (stageId) {
            case "download":
                simulateStage(stage, "正在下载 " + skillId, 1500);
                break;
            case "validate":
                simulateStage(stage, "验证签名...", 500);
                break;
            case "extract":
                simulateStage(stage, "解压文件...", 800);
                break;
            case "dependencies":
                List<String> deps = listing.getCapabilities() != null ? 
                    listing.getCapabilities().subList(0, Math.min(2, listing.getCapabilities().size())) : 
                    new ArrayList<>();
                if (autoInstallDeps && !deps.isEmpty()) {
                    simulateStage(stage, "安装依赖: " + String.join(", ", deps), 1000);
                } else {
                    simulateStage(stage, "检查依赖...", 300);
                }
                break;
            case "configure":
                simulateStage(stage, "应用配置...", 400);
                break;
            case "register":
                SkillManager.SkillInfo skillInfo = new SkillManager.SkillInfo();
                skillInfo.setId(skillId);
                skillInfo.setName(listing.getName());
                skillInfo.setDescription(listing.getDescription());
                skillInfo.setCategory(listing.getType());
                skillInfo.setStatus("active");
                skillManager.registerSkill(skillInfo);
                simulateStage(stage, "注册技能...", 300);
                break;
            case "verify":
                simulateStage(stage, "验证安装...", 500);
                break;
            default:
                break;
        }
    }

    private void simulateStage(InstallStageDTO stage, String message, long duration) throws Exception {
        stage.setMessage(message);
        stage.setProgress(50);
        Thread.sleep(duration);
        stage.setProgress(100);
    }

    private void updateProgressOnError(String installId, String errorMessage) {
        InstallProgressDTO progress = installProgressMap.get(installId);
        if (progress != null) {
            progress.setStatus("failed");
            progress.setErrorMessage(errorMessage);
            progress.setEndTime(System.currentTimeMillis());
            progress.setDuration(progress.getEndTime() - progress.getStartTime());

            for (InstallStageDTO stage : progress.getStages()) {
                if ("running".equals(stage.getStatus())) {
                    stage.setStatus("failed");
                    stage.setMessage(errorMessage);
                    break;
                }
            }
        }
    }
}
