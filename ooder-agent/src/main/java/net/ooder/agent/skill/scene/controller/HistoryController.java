package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.history.HistoryDTO;
import net.ooder.mvp.skill.scene.dto.history.HistoryStatisticsDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.HistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/my/history")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/scenes")
    public ResultModel<PageResult<HistoryDTO>> listMyHistory(
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        String currentUserId = "current-user";
        PageResult<HistoryDTO> result = historyService.listMyHistory(
            currentUserId, days, category, status, keyword, pageNum, pageSize);
        return ResultModel.success(result);
    }

    @GetMapping("/{executionId}")
    public ResultModel<HistoryDTO> getExecutionDetail(@PathVariable String executionId) {
        String currentUserId = "current-user";
        HistoryDTO result = historyService.getExecutionDetail(executionId, currentUserId);
        if (result == null) {
            return ResultModel.error(404, "执行记录不存在");
        }
        return ResultModel.success(result);
    }

    @GetMapping("/statistics")
    public ResultModel<HistoryStatisticsDTO> getStatistics(
            @RequestParam(required = false) Integer days) {
        String currentUserId = "current-user";
        HistoryStatisticsDTO result = historyService.getStatistics(currentUserId, days);
        return ResultModel.success(result);
    }

    @PostMapping("/{sceneGroupId}/rerun")
    public ResultModel<Boolean> rerunScene(@PathVariable String sceneGroupId) {
        String currentUserId = "current-user";
        boolean result = historyService.rerunScene(currentUserId, sceneGroupId);
        if (result) {
            return ResultModel.success(true);
        }
        return ResultModel.error(400, "重新执行失败");
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportHistory(
            @RequestParam(required = false) Integer days,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        String currentUserId = "current-user";
        byte[] data = historyService.exportHistory(currentUserId, days, category, status);
        
        String filename = "场景历史记录_" + new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + ".csv";
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
            .body(data);
    }
}
