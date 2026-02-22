package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.config.ResultModel;
import net.ooder.nexus.domain.skill.model.InstallPreview;
import net.ooder.nexus.domain.skill.model.SkillDependency;
import net.ooder.nexus.service.skill.SkillDependencyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/skill/install", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SkillInstallController {

    private static final Logger log = LoggerFactory.getLogger(SkillInstallController.class);

    @Autowired
    private SkillDependencyService dependencyService;

    @PostMapping("/preview")
    @ResponseBody
    public ResultModel<Map<String, Object>> previewInstall(@RequestBody Map<String, String> request) {
        String skillId = request.get("skillId");
        String userId = request.get("userId");
        String downloadUrl = request.get("downloadUrl");
        
        log.info("Preview install request: skillId={}, userId={}", skillId, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            InstallPreview preview = dependencyService.previewInstall(skillId, userId, downloadUrl);
            result.setData(preview.toMap());
            result.setRequestStatus(200);
            result.setMessage("获取安装预览成功");
        } catch (Exception e) {
            log.error("Error previewing install", e);
            result.setRequestStatus(500);
            result.setMessage("获取安装预览失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/dependencies")
    @ResponseBody
    public ResultModel<Map<String, Object>> analyzeDependencies(@RequestBody Map<String, String> request) {
        String skillId = request.get("skillId");
        String userId = request.get("userId");
        
        log.info("Analyze dependencies request: skillId={}, userId={}", skillId, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            SkillDependency dep = dependencyService.analyzeDependencies(skillId, userId);
            result.setData(dep.toMap());
            result.setRequestStatus(200);
            result.setMessage("分析依赖成功");
        } catch (Exception e) {
            log.error("Error analyzing dependencies", e);
            result.setRequestStatus(500);
            result.setMessage("分析依赖失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/permissions")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> analyzePermissions(@RequestBody Map<String, String> request) {
        String skillId = request.get("skillId");
        
        log.info("Analyze permissions request: skillId={}", skillId);
        ResultModel<List<Map<String, Object>>> result = new ResultModel<>();
        
        try {
            List<Map<String, Object>> permissions = dependencyService.analyzeRequiredPermissions(skillId);
            result.setData(permissions);
            result.setRequestStatus(200);
            result.setMessage("分析权限成功");
        } catch (Exception e) {
            log.error("Error analyzing permissions", e);
            result.setRequestStatus(500);
            result.setMessage("分析权限失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/scenes")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> analyzeScenes(@RequestBody Map<String, String> request) {
        String skillId = request.get("skillId");
        
        log.info("Analyze scenes request: skillId={}", skillId);
        ResultModel<List<Map<String, Object>>> result = new ResultModel<>();
        
        try {
            List<Map<String, Object>> scenes = dependencyService.analyzeSceneDependencies(skillId);
            result.setData(scenes);
            result.setRequestStatus(200);
            result.setMessage("分析场景成功");
        } catch (Exception e) {
            log.error("Error analyzing scenes", e);
            result.setRequestStatus(500);
            result.setMessage("分析场景失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/group-address")
    @ResponseBody
    public ResultModel<Map<String, Object>> getGroupAddress(@RequestBody Map<String, String> request) {
        String sceneId = request.get("sceneId");
        String userId = request.get("userId");
        
        log.info("Get group address request: sceneId={}, userId={}", sceneId, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            Map<String, Object> address = dependencyService.getGroupAddress(sceneId, userId);
            result.setData(address);
            result.setRequestStatus(200);
            result.setMessage("获取组地址成功");
        } catch (Exception e) {
            log.error("Error getting group address", e);
            result.setRequestStatus(500);
            result.setMessage("获取组地址失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/cap/write")
    @ResponseBody
    public ResultModel<Map<String, Object>> writeCapInfo(@RequestBody Map<String, Object> request) {
        String skillId = (String) request.get("skillId");
        String userId = (String) request.get("userId");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> capConfig = (Map<String, Object>) request.get("capConfig");
        
        log.info("Write Cap info request: skillId={}, userId={}", skillId, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            Map<String, Object> capInfo = dependencyService.writeCapInfo(skillId, userId, capConfig);
            result.setData(capInfo);
            result.setRequestStatus(200);
            result.setMessage("写入Cap信息成功");
        } catch (Exception e) {
            log.error("Error writing Cap info", e);
            result.setRequestStatus(500);
            result.setMessage("写入Cap信息失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/confirm")
    @ResponseBody
    public ResultModel<Map<String, Object>> confirmInstall(@RequestBody Map<String, Object> request) {
        String previewId = (String) request.get("previewId");
        String userId = (String) request.get("userId");
        
        @SuppressWarnings("unchecked")
        List<String> grantedPermissions = (List<String>) request.get("grantedPermissions");
        @SuppressWarnings("unchecked")
        List<String> joinScenes = (List<String>) request.get("joinScenes");
        
        log.info("Confirm install request: previewId={}, userId={}", previewId, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            Map<String, Object> capConfig = new HashMap<>();
            capConfig.put("grantedPermissions", grantedPermissions);
            capConfig.put("joinScenes", joinScenes);
            capConfig.put("confirmedAt", System.currentTimeMillis());
            
            Map<String, Object> confirmResult = new HashMap<>();
            confirmResult.put("previewId", previewId);
            confirmResult.put("userId", userId);
            confirmResult.put("status", "confirmed");
            confirmResult.put("capConfig", capConfig);
            confirmResult.put("confirmedAt", System.currentTimeMillis());
            
            result.setData(confirmResult);
            result.setRequestStatus(200);
            result.setMessage("安装确认成功");
        } catch (Exception e) {
            log.error("Error confirming install", e);
            result.setRequestStatus(500);
            result.setMessage("安装确认失败: " + e.getMessage());
        }
        
        return result;
    }
}
