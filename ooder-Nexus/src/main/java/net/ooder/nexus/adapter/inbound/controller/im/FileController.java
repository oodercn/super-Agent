package net.ooder.nexus.adapter.inbound.controller.im;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.domain.im.model.SharedFile;
import net.ooder.nexus.service.im.FileService;
import net.ooder.nexus.service.im.FileService.StorageStats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件共享控制器
 *
 * <p>提供文件共享 API，支持：</p>
 * <ul>
 *   <li>文件上传下载</li>
 *   <li>文件列表管理</li>
 *   <li>文件分享</li>
 * </ul>
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/im/file", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    /**
     * 获取文件列表
     */
    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<SharedFile>> getFiles(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String folder = request.get("folder");
        String fileType = request.get("fileType");
        
        log.info("Get files request: userId={}, folder={}, type={}", userId, folder, fileType);
        ListResultModel<List<SharedFile>> result = new ListResultModel<>();
        try {
            List<SharedFile> files = fileService.getFiles(userId, folder != null ? folder : "all", fileType);
            result.setData(files);
            result.setSize(files.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting files", e);
            result.setRequestStatus(500);
            result.setMessage("获取文件列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取文件详情
     */
    @GetMapping("/{fileId}")
    @ResponseBody
    public ResultModel<SharedFile> getFile(@PathVariable String fileId) {
        log.info("Get file request: fileId={}", fileId);
        ResultModel<SharedFile> result = new ResultModel<>();
        try {
            SharedFile file = fileService.getFile(fileId);
            if (file != null) {
                result.setData(file);
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("文件不存在");
            }
        } catch (Exception e) {
            log.error("Error getting file", e);
            result.setRequestStatus(500);
            result.setMessage("获取文件失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResultModel<SharedFile> uploadFile(@RequestBody Map<String, Object> request) {
        String fileName = (String) request.get("fileName");
        Long fileSize = request.get("fileSize") != null ? ((Number) request.get("fileSize")).longValue() : 0L;
        String fileType = (String) request.get("fileType");
        String uploaderId = (String) request.get("uploaderId");
        String uploaderName = (String) request.get("uploaderName");
        String sourceId = (String) request.get("sourceId");
        String sourceType = (String) request.get("sourceType");
        
        log.info("Upload file request: fileName={}, uploader={}", fileName, uploaderId);
        ResultModel<SharedFile> result = new ResultModel<>();
        try {
            SharedFile file = fileService.uploadFile(fileName, fileSize, fileType, 
                uploaderId, uploaderName, sourceId, sourceType);
            result.setData(file);
            result.setRequestStatus(200);
            result.setMessage("上传成功");
        } catch (Exception e) {
            log.error("Error uploading file", e);
            result.setRequestStatus(500);
            result.setMessage("上传文件失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    @ResponseBody
    public ResultModel<Boolean> deleteFile(@PathVariable String fileId, 
                                           @RequestParam String operatorId) {
        log.info("Delete file request: fileId={}, operator={}", fileId, operatorId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = fileService.deleteFile(fileId, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 403);
            result.setMessage(success ? "删除成功" : "无权删除此文件");
        } catch (Exception e) {
            log.error("Error deleting file", e);
            result.setRequestStatus(500);
            result.setMessage("删除文件失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 分享文件
     */
    @PostMapping("/share")
    @ResponseBody
    public ResultModel<Boolean> shareFile(@RequestBody Map<String, Object> request) {
        String fileId = (String) request.get("fileId");
        String operatorId = (String) request.get("operatorId");
        @SuppressWarnings("unchecked")
        List<String> targetIds = (List<String>) request.get("targetIds");
        
        log.info("Share file request: fileId={}, to={}", fileId, targetIds);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = fileService.shareFile(fileId, targetIds, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 403);
            result.setMessage(success ? "分享成功" : "无权分享此文件");
        } catch (Exception e) {
            log.error("Error sharing file", e);
            result.setRequestStatus(500);
            result.setMessage("分享文件失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取存储统计
     */
    @PostMapping("/storage/stats")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStorageStats(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        log.info("Get storage stats request: userId={}", userId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            StorageStats stats = fileService.getStorageStats(userId);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("usedSpace", stats.getUsedSpace());
            data.put("totalSpace", stats.getTotalSpace());
            data.put("fileCount", stats.getFileCount());
            data.put("usedPercent", (double) stats.getUsedSpace() / stats.getTotalSpace() * 100);
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting storage stats", e);
            result.setRequestStatus(500);
            result.setMessage("获取存储统计失败: " + e.getMessage());
        }
        return result;
    }
}
