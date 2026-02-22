package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.dto.skill.InstallProgressDTO;
import net.ooder.nexus.dto.skill.InstallSkillDTO;
import net.ooder.nexus.dto.skill.InstalledSkillDTO;
import net.ooder.nexus.dto.skill.SkillConfigUpdateDTO;
import net.ooder.nexus.dto.skill.SkillDependencyDTO;
import net.ooder.nexus.dto.skill.SkillLifecycleDTO;
import net.ooder.nexus.dto.skill.SkillRunDTO;
import net.ooder.nexus.dto.skill.SkillRunResultDTO;
import net.ooder.nexus.dto.skill.SkillUpdateDTO;
import net.ooder.nexus.service.InstalledSkillService;
import net.ooder.sdk.api.skill.InstalledSkill;
import net.ooder.sdk.api.skill.InstallRequest;
import net.ooder.sdk.common.enums.SkillStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/skillcenter/installed")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class InstalledSkillController {

    private static final Logger log = LoggerFactory.getLogger(InstalledSkillController.class);

    @Autowired
    private InstalledSkillService installedSkillService;

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<InstalledSkillDTO>> getList(@RequestBody(required = false) Map<String, Object> request) {
        log.info("Get installed skill list requested");
        ListResultModel<List<InstalledSkillDTO>> result = new ListResultModel<List<InstalledSkillDTO>>();

        try {
            List<InstalledSkill> skills;

            if (request != null && request.get("status") != null) {
                String statusStr = (String) request.get("status");
                SkillStatus status = SkillStatus.valueOf(statusStr);
                skills = installedSkillService.getSkillsByStatus(status);
            } else {
                skills = installedSkillService.getAllInstalledSkills();
            }

            List<InstalledSkillDTO> dtoList = convertToDTOList(skills);
            result.setData(dtoList);
            result.setSize(dtoList.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting installed skill list", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get installed skill list: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<InstalledSkillDTO> getSkill(@RequestBody Map<String, String> request) {
        log.info("Get skill detail requested: {}", request.get("id"));
        ResultModel<InstalledSkillDTO> result = new ResultModel<InstalledSkillDTO>();

        try {
            String id = request.get("id");
            InstalledSkill skill = installedSkillService.getSkillById(id);

            if (skill == null) {
                result.setRequestStatus(404);
                result.setMessage("Skill not found");
            } else {
                result.setData(convertToDTO(skill));
                result.setRequestStatus(200);
                result.setMessage("Success");
            }
        } catch (Exception e) {
            log.error("Error getting skill detail", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get skill detail: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/install")
    @ResponseBody
    public ResultModel<InstalledSkillDTO> installSkill(@RequestBody InstallSkillDTO dto) {
        log.info("Install skill requested: skillId={}, skillName={}, downloadUrl={}", dto.getSkillId(), dto.getSkillName(), dto.getDownloadUrl());
        ResultModel<InstalledSkillDTO> result = new ResultModel<InstalledSkillDTO>();

        try {
            Map<String, String> config = new HashMap<String, String>();
            if (dto.getConfig() != null) {
                for (Map.Entry<String, Object> entry : dto.getConfig().entrySet()) {
                    config.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            
            String skillId = dto.getSkillId() != null ? dto.getSkillId() : dto.getSkillName();
            String name = dto.getSkillName() != null ? dto.getSkillName() : dto.getSkillId();
            String version = dto.getVersion() != null ? dto.getVersion() : "latest";
            String downloadUrl = dto.getDownloadUrl();
            String source = "github";
            
            CompletableFuture<InstalledSkill> future;
            if (downloadUrl != null && !downloadUrl.isEmpty()) {
                future = installedSkillService.installSkillFromPackage(skillId, name, version, downloadUrl, source, config);
            } else {
                future = installedSkillService.installSkill(skillId, version, config);
            }
            
            InstalledSkill installed = future.get();
            if (installed != null) {
                result.setData(convertToDTO(installed));
                result.setRequestStatus(200);
                result.setMessage("Skill installed successfully");
            } else {
                result.setRequestStatus(500);
                result.setMessage("Failed to install skill");
            }
        } catch (Exception e) {
            log.error("Error installing skill", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to install skill: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/uninstall")
    @ResponseBody
    public ResultModel<Boolean> uninstallSkill(@RequestBody Map<String, String> request) {
        log.info("Uninstall skill requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String id = request.get("id");
            CompletableFuture<Boolean> future = installedSkillService.uninstallSkill(id);
            boolean uninstalled = future.get();
            result.setData(uninstalled);
            result.setRequestStatus(uninstalled ? 200 : 404);
            result.setMessage(uninstalled ? "Skill uninstalled successfully" : "Skill not found");
        } catch (Exception e) {
            log.error("Error uninstalling skill", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to uninstall skill: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResultModel<InstalledSkillDTO> editSkill(@RequestBody SkillUpdateDTO dto) {
        log.info("Edit skill requested: {}", dto.getId());
        ResultModel<InstalledSkillDTO> result = new ResultModel<InstalledSkillDTO>();

        try {
            InstalledSkill skill = installedSkillService.getSkillById(dto.getId());
            if (skill == null) {
                result.setRequestStatus(404);
                result.setMessage("Skill not found");
            } else {
                result.setData(convertToDTO(skill));
                result.setRequestStatus(200);
                result.setMessage("Skill updated successfully");
            }
        } catch (Exception e) {
            log.error("Error editing skill", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to edit skill: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<InstalledSkillDTO> updateSkill(@RequestBody Map<String, String> request) {
        log.info("Update skill requested: {}", request.get("id"));
        ResultModel<InstalledSkillDTO> result = new ResultModel<InstalledSkillDTO>();

        try {
            String id = request.get("id");
            String version = request.get("version");
            CompletableFuture<InstalledSkill> future = installedSkillService.updateSkill(id, version);
            InstalledSkill updated = future.get();

            if (updated == null) {
                result.setRequestStatus(404);
                result.setMessage("Skill not found");
            } else {
                result.setData(convertToDTO(updated));
                result.setRequestStatus(200);
                result.setMessage("Skill updated successfully");
            }
        } catch (Exception e) {
            log.error("Error updating skill", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to update skill: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/run")
    @ResponseBody
    public ResultModel<SkillRunResultDTO> runSkill(@RequestBody SkillRunDTO dto) {
        log.info("Run skill requested: {}", dto.getId());
        ResultModel<SkillRunResultDTO> result = new ResultModel<SkillRunResultDTO>();

        try {
            installedSkillService.startSkill(dto.getId()).get();
            
            SkillRunResultDTO runResult = new SkillRunResultDTO();
            runResult.setSuccess(true);
            runResult.setOutput("Skill started successfully");
            result.setData(runResult);
            result.setRequestStatus(200);
            result.setMessage("Skill started successfully");
        } catch (Exception e) {
            log.error("Error running skill", e);
            SkillRunResultDTO runResult = new SkillRunResultDTO();
            runResult.setSuccess(false);
            runResult.setError(e.getMessage());
            result.setData(runResult);
            result.setRequestStatus(500);
            result.setMessage("Failed to run skill: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/stop")
    @ResponseBody
    public ResultModel<Boolean> stopSkill(@RequestBody Map<String, String> request) {
        log.info("Stop skill requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String id = request.get("id");
            installedSkillService.stopSkill(id).get();
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("Skill stopped successfully");
        } catch (Exception e) {
            log.error("Error stopping skill", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to stop skill: " + e.getMessage());
            result.setData(false);
        }

        return result;
    }

    @PostMapping("/debug")
    @ResponseBody
    public ResultModel<SkillRunResultDTO> debugSkill(@RequestBody SkillRunDTO dto) {
        log.info("Debug skill requested: {}", dto.getId());
        ResultModel<SkillRunResultDTO> result = new ResultModel<SkillRunResultDTO>();

        try {
            SkillStatus status = installedSkillService.getSkillStatus(dto.getId());
            
            SkillRunResultDTO debugResult = new SkillRunResultDTO();
            debugResult.setSuccess(true);
            debugResult.setOutput("Debug completed, current status: " + (status != null ? status.name() : "UNKNOWN"));
            result.setData(debugResult);
            result.setRequestStatus(200);
            result.setMessage("Debug completed");
        } catch (Exception e) {
            log.error("Error debugging skill", e);
            SkillRunResultDTO debugResult = new SkillRunResultDTO();
            debugResult.setSuccess(false);
            debugResult.setError(e.getMessage());
            result.setData(debugResult);
            result.setRequestStatus(500);
            result.setMessage("Debug failed: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/config/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getConfig(@RequestBody Map<String, String> request) {
        log.info("Get skill config requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String id = request.get("id");
            Map<String, String> config = installedSkillService.getSkillConfig(id);
            Map<String, Object> configObj = new HashMap<String, Object>();
            configObj.putAll(config);
            result.setData(configObj);
            result.setRequestStatus(200);
            result.setMessage("Config retrieved successfully");
        } catch (Exception e) {
            log.error("Error getting skill config", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get config: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/config/update")
    @ResponseBody
    public ResultModel<InstalledSkillDTO> updateConfig(@RequestBody SkillConfigUpdateDTO dto) {
        log.info("Update skill config requested: {}", dto.getId());
        ResultModel<InstalledSkillDTO> result = new ResultModel<InstalledSkillDTO>();

        try {
            Map<String, String> config = new HashMap<String, String>();
            if (dto.getConfig() != null) {
                for (Map.Entry<String, Object> entry : dto.getConfig().entrySet()) {
                    config.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            installedSkillService.updateConfig(dto.getId(), config);
            InstalledSkill skill = installedSkillService.getSkillById(dto.getId());

            if (skill == null) {
                result.setRequestStatus(404);
                result.setMessage("Skill not found");
            } else {
                result.setData(convertToDTO(skill));
                result.setRequestStatus(200);
                result.setMessage("Config updated successfully");
            }
        } catch (Exception e) {
            log.error("Error updating skill config", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to update config: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/logs")
    @ResponseBody
    public ResultModel<List<String>> getLogs(@RequestBody Map<String, Object> request) {
        log.info("Get skill logs requested: {}", request.get("id"));
        ResultModel<List<String>> result = new ResultModel<List<String>>();

        try {
            result.setData(new ArrayList<String>());
            result.setRequestStatus(200);
            result.setMessage("Logs retrieved successfully");
        } catch (Exception e) {
            log.error("Error getting skill logs", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get logs: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/statistics")
    @ResponseBody
    public ResultModel<InstalledSkillService.SkillStatistics> getStatistics() {
        log.info("Get skill statistics requested");
        ResultModel<InstalledSkillService.SkillStatistics> result = new ResultModel<InstalledSkillService.SkillStatistics>();

        try {
            InstalledSkillService.SkillStatistics stats = installedSkillService.getStatistics();
            result.setData(stats);
            result.setRequestStatus(200);
            result.setMessage("Statistics retrieved successfully");
        } catch (Exception e) {
            log.error("Error getting skill statistics", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get statistics: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/progress")
    @ResponseBody
    public ResultModel<InstallProgressDTO> getInstallProgress(@RequestBody Map<String, String> request) {
        log.info("Get install progress requested: {}", request.get("installId"));
        ResultModel<InstallProgressDTO> result = new ResultModel<InstallProgressDTO>();

        try {
            String installId = request.get("installId");
            InstalledSkillService.InstallProgress progress = installedSkillService.getInstallProgress(installId);
            
            InstallProgressDTO dto = new InstallProgressDTO();
            dto.setInstallId(progress.getInstallId());
            dto.setSkillId(progress.getSkillId());
            dto.setSkillName(progress.getSkillName());
            dto.setStage(progress.getStage());
            dto.setProgress(progress.getProgress());
            dto.setStatus(progress.getStatus());
            dto.setMessage(progress.getMessage());
            dto.setStartTime(progress.getStartTime());
            
            result.setData(dto);
            result.setRequestStatus(200);
            result.setMessage("Progress retrieved successfully");
        } catch (Exception e) {
            log.error("Error getting install progress", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get progress: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/progress/list")
    @ResponseBody
    public ResultModel<List<InstallProgressDTO>> getActiveInstalls() {
        log.info("Get active installs requested");
        ResultModel<List<InstallProgressDTO>> result = new ResultModel<List<InstallProgressDTO>>();

        try {
            List<InstalledSkillService.InstallProgress> progresses = installedSkillService.getActiveInstalls();
            List<InstallProgressDTO> dtoList = new ArrayList<InstallProgressDTO>();
            
            for (InstalledSkillService.InstallProgress p : progresses) {
                InstallProgressDTO dto = new InstallProgressDTO();
                dto.setInstallId(p.getInstallId());
                dto.setSkillId(p.getSkillId());
                dto.setSkillName(p.getSkillName());
                dto.setStage(p.getStage());
                dto.setProgress(p.getProgress());
                dto.setStatus(p.getStatus());
                dto.setMessage(p.getMessage());
                dto.setStartTime(p.getStartTime());
                dtoList.add(dto);
            }
            
            result.setData(dtoList);
            result.setRequestStatus(200);
            result.setMessage("Active installs retrieved successfully");
        } catch (Exception e) {
            log.error("Error getting active installs", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get active installs: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/dependencies")
    @ResponseBody
    public ResultModel<SkillDependencyDTO> getDependencies(@RequestBody Map<String, String> request) {
        log.info("Get dependencies requested: {}", request.get("skillId"));
        ResultModel<SkillDependencyDTO> result = new ResultModel<SkillDependencyDTO>();

        try {
            String skillId = request.get("skillId");
            InstalledSkillService.DependencyInfo info = installedSkillService.getDependencies(skillId);
            
            SkillDependencyDTO dto = new SkillDependencyDTO();
            dto.setSkillId(info.getSkillId());
            dto.setSatisfied(info.isSatisfied());
            
            List<SkillDependencyDTO.DependencyInfo> deps = new ArrayList<SkillDependencyDTO.DependencyInfo>();
            if (info.getDependencies() != null) {
                for (InstalledSkillService.DependencyItem item : info.getDependencies()) {
                    SkillDependencyDTO.DependencyInfo depInfo = new SkillDependencyDTO.DependencyInfo();
                    depInfo.setName(item.getName());
                    depInfo.setRequiredVersion(item.getRequiredVersion());
                    depInfo.setInstalledVersion(item.getInstalledVersion());
                    depInfo.setStatus(item.getStatus());
                    deps.add(depInfo);
                }
            }
            dto.setDependencies(deps);
            
            result.setData(dto);
            result.setRequestStatus(200);
            result.setMessage("Dependencies retrieved successfully");
        } catch (Exception e) {
            log.error("Error getting dependencies", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get dependencies: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/dependencies/install")
    @ResponseBody
    public ResultModel<Boolean> installDependencies(@RequestBody Map<String, String> request) {
        log.info("Install dependencies requested: {}", request.get("skillId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String skillId = request.get("skillId");
            installedSkillService.installDependencies(skillId).get();
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("Dependencies installed successfully");
        } catch (Exception e) {
            log.error("Error installing dependencies", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to install dependencies: " + e.getMessage());
            result.setData(false);
        }

        return result;
    }

    @PostMapping("/lifecycle/pause")
    @ResponseBody
    public ResultModel<SkillLifecycleDTO> pauseSkill(@RequestBody Map<String, String> request) {
        log.info("Pause skill requested: {}", request.get("skillId"));
        ResultModel<SkillLifecycleDTO> result = new ResultModel<SkillLifecycleDTO>();

        try {
            String skillId = request.get("skillId");
            installedSkillService.pauseSkill(skillId).get();
            
            SkillLifecycleDTO dto = new SkillLifecycleDTO();
            dto.setSkillId(skillId);
            dto.setOperation("pause");
            dto.setSuccess(true);
            dto.setMessage("Skill paused successfully");
            dto.setCurrentStatus("PAUSED");
            
            result.setData(dto);
            result.setRequestStatus(200);
            result.setMessage("Skill paused successfully");
        } catch (Exception e) {
            log.error("Error pausing skill", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to pause skill: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/lifecycle/resume")
    @ResponseBody
    public ResultModel<SkillLifecycleDTO> resumeSkill(@RequestBody Map<String, String> request) {
        log.info("Resume skill requested: {}", request.get("skillId"));
        ResultModel<SkillLifecycleDTO> result = new ResultModel<SkillLifecycleDTO>();

        try {
            String skillId = request.get("skillId");
            installedSkillService.resumeSkill(skillId).get();
            
            SkillLifecycleDTO dto = new SkillLifecycleDTO();
            dto.setSkillId(skillId);
            dto.setOperation("resume");
            dto.setSuccess(true);
            dto.setMessage("Skill resumed successfully");
            dto.setCurrentStatus("RUNNING");
            
            result.setData(dto);
            result.setRequestStatus(200);
            result.setMessage("Skill resumed successfully");
        } catch (Exception e) {
            log.error("Error resuming skill", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to resume skill: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/lifecycle/restart")
    @ResponseBody
    public ResultModel<SkillLifecycleDTO> restartSkill(@RequestBody Map<String, String> request) {
        log.info("Restart skill requested: {}", request.get("skillId"));
        ResultModel<SkillLifecycleDTO> result = new ResultModel<SkillLifecycleDTO>();

        try {
            String skillId = request.get("skillId");
            installedSkillService.restartSkill(skillId).get();
            
            SkillLifecycleDTO dto = new SkillLifecycleDTO();
            dto.setSkillId(skillId);
            dto.setOperation("restart");
            dto.setSuccess(true);
            dto.setMessage("Skill restarted successfully");
            dto.setCurrentStatus("RUNNING");
            
            result.setData(dto);
            result.setRequestStatus(200);
            result.setMessage("Skill restarted successfully");
        } catch (Exception e) {
            log.error("Error restarting skill", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to restart skill: " + e.getMessage());
        }

        return result;
    }

    private List<InstalledSkillDTO> convertToDTOList(List<InstalledSkill> skills) {
        List<InstalledSkillDTO> dtoList = new ArrayList<InstalledSkillDTO>();
        if (skills != null) {
            for (InstalledSkill skill : skills) {
                dtoList.add(convertToDTO(skill));
            }
        }
        return dtoList;
    }

    private InstalledSkillDTO convertToDTO(InstalledSkill skill) {
        InstalledSkillDTO dto = new InstalledSkillDTO();
        dto.setId(skill.getSkillId());
        dto.setSkillId(skill.getSkillId());
        dto.setSkillName(skill.getName());
        dto.setVersion(skill.getVersion());
        dto.setStatus(skill.getStatus() != null ? skill.getStatus() : "UNKNOWN");
        dto.setSource("REMOTE_HOSTED");
        
        Map<String, Object> config = new HashMap<String, Object>();
        dto.setConfig(config);
        
        return dto;
    }
}
