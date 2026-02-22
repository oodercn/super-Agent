package net.ooder.nexus.adapter.inbound.controller.storage;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.core.storage.vfs.FileVersion;
import net.ooder.nexus.service.storage.FileVersionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/storage/version", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class FileVersionController {

    private static final Logger log = LoggerFactory.getLogger(FileVersionController.class);

    @Autowired
    private FileVersionService versionService;

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<Map<String, Object>> createVersion(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        String personId = request.get("personId");
        String changeNote = request.get("changeNote");
        
        log.info("Create version request: fileId={}, personId={}", fileId, personId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            FileVersion version = versionService.createVersion(fileId, personId, changeNote);
            result.setData(version.toMap());
            result.setRequestStatus(200);
            result.setMessage("版本创建成功");
        } catch (Exception e) {
            log.error("Error creating version", e);
            result.setRequestStatus(500);
            result.setMessage("创建版本失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getVersion(@RequestBody Map<String, String> request) {
        String versionId = request.get("versionId");
        
        log.info("Get version request: versionId={}", versionId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            FileVersion version = versionService.getVersion(versionId);
            if (version != null) {
                result.setData(version.toMap());
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("版本不存在");
            }
        } catch (Exception e) {
            log.error("Error getting version", e);
            result.setRequestStatus(500);
            result.setMessage("获取版本失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/history")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getVersionHistory(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        
        log.info("Get version history request: fileId={}", fileId);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        
        try {
            List<FileVersion> versions = versionService.getVersionHistory(fileId);
            List<Map<String, Object>> data = new ArrayList<>();
            for (FileVersion v : versions) {
                data.add(v.toMap());
            }
            result.setData(data);
            result.setSize(data.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting version history", e);
            result.setRequestStatus(500);
            result.setMessage("获取版本历史失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/current")
    @ResponseBody
    public ResultModel<Map<String, Object>> getCurrentVersion(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        
        log.info("Get current version request: fileId={}", fileId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            FileVersion version = versionService.getCurrentVersion(fileId);
            if (version != null) {
                result.setData(version.toMap());
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("当前版本不存在");
            }
        } catch (Exception e) {
            log.error("Error getting current version", e);
            result.setRequestStatus(500);
            result.setMessage("获取当前版本失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/set-current")
    @ResponseBody
    public ResultModel<Boolean> setCurrentVersion(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        String versionId = request.get("versionId");
        
        log.info("Set current version request: fileId={}, versionId={}", fileId, versionId);
        ResultModel<Boolean> result = new ResultModel<>();
        
        try {
            boolean success = versionService.setCurrentVersion(fileId, versionId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "设置成功" : "版本不存在");
        } catch (Exception e) {
            log.error("Error setting current version", e);
            result.setRequestStatus(500);
            result.setMessage("设置当前版本失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteVersion(@RequestBody Map<String, String> request) {
        String versionId = request.get("versionId");
        
        log.info("Delete version request: versionId={}", versionId);
        ResultModel<Boolean> result = new ResultModel<>();
        
        try {
            boolean success = versionService.deleteVersion(versionId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "删除成功" : "版本不存在");
        } catch (Exception e) {
            log.error("Error deleting version", e);
            result.setRequestStatus(500);
            result.setMessage("删除版本失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/archive")
    @ResponseBody
    public ResultModel<Boolean> archiveVersion(@RequestBody Map<String, String> request) {
        String versionId = request.get("versionId");
        
        log.info("Archive version request: versionId={}", versionId);
        ResultModel<Boolean> result = new ResultModel<>();
        
        try {
            boolean success = versionService.archiveVersion(versionId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "归档成功" : "版本不存在");
        } catch (Exception e) {
            log.error("Error archiving version", e);
            result.setRequestStatus(500);
            result.setMessage("归档版本失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/restore")
    @ResponseBody
    public ResultModel<Boolean> restoreVersion(@RequestBody Map<String, String> request) {
        String versionId = request.get("versionId");
        
        log.info("Restore version request: versionId={}", versionId);
        ResultModel<Boolean> result = new ResultModel<>();
        
        try {
            boolean success = versionService.restoreVersion(versionId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "恢复成功" : "版本不存在");
        } catch (Exception e) {
            log.error("Error restoring version", e);
            result.setRequestStatus(500);
            result.setMessage("恢复版本失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/compare")
    @ResponseBody
    public ResultModel<Map<String, Object>> compareVersions(@RequestBody Map<String, String> request) {
        String versionId1 = request.get("versionId1");
        String versionId2 = request.get("versionId2");
        
        log.info("Compare versions request: v1={}, v2={}", versionId1, versionId2);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            Map<String, Object> comparison = versionService.compareVersions(versionId1, versionId2);
            if (comparison != null) {
                result.setData(comparison);
                result.setRequestStatus(200);
                result.setMessage("比较成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("版本不存在");
            }
        } catch (Exception e) {
            log.error("Error comparing versions", e);
            result.setRequestStatus(500);
            result.setMessage("比较版本失败: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/stats")
    @ResponseBody
    public ResultModel<Map<String, Object>> getVersionStatistics(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        
        log.info("Get version statistics request: fileId={}", fileId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        try {
            Map<String, Object> stats = versionService.getVersionStatistics(fileId);
            result.setData(stats);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting version statistics", e);
            result.setRequestStatus(500);
            result.setMessage("获取版本统计失败: " + e.getMessage());
        }
        
        return result;
    }
}
