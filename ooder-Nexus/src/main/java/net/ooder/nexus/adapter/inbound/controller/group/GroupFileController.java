package net.ooder.nexus.adapter.inbound.controller.group;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.domain.group.model.GroupFile;
import net.ooder.nexus.dto.group.FileListDTO;
import net.ooder.nexus.dto.group.FileOperationDTO;
import net.ooder.nexus.dto.group.FileShareDTO;
import net.ooder.nexus.dto.group.FileUploadDTO;
import net.ooder.nexus.service.group.GroupFileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群组文件控制器
 * 
 * <p>提供群组文件的上传、下载、共享等 API。</p>
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/group/file", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class GroupFileController {

    private static final Logger log = LoggerFactory.getLogger(GroupFileController.class);

    @Autowired
    private GroupFileService groupFileService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResultModel<GroupFile> uploadFile(@RequestBody FileUploadDTO dto) {
        log.info("Upload file request: groupId={}, fileName={}", dto.getGroupId(), dto.getFileName());
        ResultModel<GroupFile> result = new ResultModel<>();
        try {
            GroupFile file = groupFileService.uploadFile(dto);
            result.setData(file);
            result.setRequestStatus(200);
            result.setMessage("文件上传成功");
        } catch (Exception e) {
            log.error("Error uploading file", e);
            result.setRequestStatus(500);
            result.setMessage("文件上传失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取文件列表
     */
    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<GroupFile>> getFileList(@RequestBody FileListDTO dto) {
        log.info("Get file list request: groupId={}", dto.getGroupId());
        ListResultModel<List<GroupFile>> result = new ListResultModel<>();
        try {
            List<GroupFile> files = groupFileService.getFileList(dto);
            result.setData(files);
            result.setSize(files.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting file list", e);
            result.setRequestStatus(500);
            result.setMessage("获取文件列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取文件详情
     */
    @PostMapping("/get")
    @ResponseBody
    public ResultModel<GroupFile> getFile(@RequestBody Map<String, String> request) {
        String fileId = request.get("fileId");
        log.info("Get file request: fileId={}", fileId);
        ResultModel<GroupFile> result = new ResultModel<>();
        try {
            GroupFile file = groupFileService.getFile(fileId);
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
            result.setMessage("获取文件详情失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 下载文件
     */
    @PostMapping("/download")
    @ResponseBody
    public ResultModel<Map<String, Object>> downloadFile(@RequestBody FileOperationDTO dto) {
        log.info("Download file request: fileId={}, groupId={}", dto.getFileId(), dto.getGroupId());
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String filePath = groupFileService.downloadFile(dto);
            if (filePath != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("filePath", filePath);
                data.put("fileId", dto.getFileId());
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("获取下载地址成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("文件不存在或无权访问");
            }
        } catch (Exception e) {
            log.error("Error downloading file", e);
            result.setRequestStatus(500);
            result.setMessage("下载文件失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 删除文件
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteFile(@RequestBody FileOperationDTO dto) {
        log.info("Delete file request: fileId={}, operatorId={}", dto.getFileId(), dto.getOperatorId());
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = groupFileService.deleteFile(dto);
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
     * 共享文件到其他群组
     */
    @PostMapping("/share")
    @ResponseBody
    public ResultModel<GroupFile> shareFile(@RequestBody FileShareDTO dto) {
        log.info("Share file request: fileId={}, from={}, to={}", 
                dto.getFileId(), dto.getSourceGroupId(), dto.getTargetGroupId());
        ResultModel<GroupFile> result = new ResultModel<>();
        try {
            GroupFile file = groupFileService.shareFile(dto);
            if (file != null) {
                result.setData(file);
                result.setRequestStatus(200);
                result.setMessage("文件共享成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("文件不存在或无权共享");
            }
        } catch (Exception e) {
            log.error("Error sharing file", e);
            result.setRequestStatus(500);
            result.setMessage("共享文件失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取群组存储统计
     */
    @PostMapping("/stats")
    @ResponseBody
    public ResultModel<GroupFileService.StorageStats> getStorageStats(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        log.info("Get storage stats request: groupId={}", groupId);
        ResultModel<GroupFileService.StorageStats> result = new ResultModel<>();
        try {
            GroupFileService.StorageStats stats = groupFileService.getStorageStats(groupId);
            result.setData(stats);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting storage stats", e);
            result.setRequestStatus(500);
            result.setMessage("获取存储统计失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 搜索文件
     */
    @PostMapping("/search")
    @ResponseBody
    public ListResultModel<List<GroupFile>> searchFiles(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        String keyword = request.get("keyword");
        log.info("Search files request: groupId={}, keyword={}", groupId, keyword);
        ListResultModel<List<GroupFile>> result = new ListResultModel<>();
        try {
            List<GroupFile> files = groupFileService.searchFiles(groupId, keyword);
            result.setData(files);
            result.setSize(files.size());
            result.setRequestStatus(200);
            result.setMessage("搜索成功");
        } catch (Exception e) {
            log.error("Error searching files", e);
            result.setRequestStatus(500);
            result.setMessage("搜索文件失败: " + e.getMessage());
        }
        return result;
    }
}
