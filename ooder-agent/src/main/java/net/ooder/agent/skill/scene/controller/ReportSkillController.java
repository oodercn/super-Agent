package net.ooder.mvp.skill.scene.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.dto.report.*;
import net.ooder.mvp.skill.scene.service.ReportSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
@Tag(name = "日志技能API", description = "日志汇报场景的底层技能能力")
public class ReportSkillController {

    @Autowired
    private ReportSkillService reportSkillService;

    @PostMapping("/report-email-fetch")
    @Operation(summary = "获取邮件数据", description = "获取用户邮件数据并汇总")
    public ResultModel<EmailFetchResultDTO> fetchEmails(@RequestBody EmailFetchRequestDTO request) {
        EmailFetchResultDTO result = reportSkillService.fetchEmails(request);
        return ResultModel.success(result);
    }

    @PostMapping("/report-git-fetch")
    @Operation(summary = "获取Git提交记录", description = "获取用户Git提交记录并汇总")
    public ResultModel<GitFetchResultDTO> fetchGitCommits(@RequestBody GitFetchRequestDTO request) {
        GitFetchResultDTO result = reportSkillService.fetchGitCommits(request);
        return ResultModel.success(result);
    }

    @PostMapping("/report-ai-generate")
    @Operation(summary = "AI生成日志内容", description = "使用AI智能生成日志内容")
    public ResultModel<AiGenerateResultDTO> aiGenerate(@RequestBody AiGenerateRequestDTO request) {
        AiGenerateResultDTO result = reportSkillService.aiGenerate(request);
        return ResultModel.success(result);
    }

    @PostMapping("/report-submit")
    @Operation(summary = "提交日志", description = "提交日志到数据库")
    public ResultModel<ReportSubmitResultDTO> submitReport(@RequestBody ReportSubmitRequestDTO request) {
        ReportSubmitResultDTO result = reportSkillService.submitReport(request);
        return ResultModel.success(result);
    }

    @GetMapping("/report-history")
    @Operation(summary = "获取历史记录", description = "获取用户的历史日志记录")
    public ResultModel<List<ReportHistoryDTO>> getHistory(
            @RequestParam String sceneGroupId,
            @RequestParam(required = false) Integer limit) {
        List<ReportHistoryDTO> history = reportSkillService.getHistory(sceneGroupId, limit);
        return ResultModel.success(history);
    }

    @GetMapping("/report-detail/{reportId}")
    @Operation(summary = "获取日志详情", description = "获取指定日志的详细信息")
    public ResultModel<ReportDetailDTO> getDetail(@PathVariable String reportId) {
        ReportDetailDTO detail = reportSkillService.getDetail(reportId);
        return ResultModel.success(detail);
    }

    @PostMapping("/report-aggregate")
    @Operation(summary = "汇总日志", description = "汇总指定场景组的所有日志")
    public ResultModel<ReportAggregateResultDTO> aggregateReports(@RequestBody ReportAggregateRequestDTO request) {
        ReportAggregateResultDTO result = reportSkillService.aggregateReports(request);
        return ResultModel.success(result);
    }

    @PostMapping("/report-analyze")
    @Operation(summary = "分析日志", description = "使用AI分析日志内容")
    public ResultModel<ReportAnalyzeResultDTO> analyzeReports(@RequestBody ReportAnalyzeRequestDTO request) {
        ReportAnalyzeResultDTO result = reportSkillService.analyzeReports(request);
        return ResultModel.success(result);
    }

    @PostMapping("/report-remind")
    @Operation(summary = "发送提醒", description = "发送日志提交提醒")
    public ResultModel<Boolean> sendRemind(@RequestBody ReportRemindRequestDTO request) {
        boolean result = reportSkillService.sendRemind(request);
        return ResultModel.success(result);
    }

    @PostMapping("/report-notify")
    @Operation(summary = "发送通知", description = "发送日志相关通知")
    public ResultModel<Boolean> sendNotify(@RequestBody ReportNotifyRequestDTO request) {
        boolean result = reportSkillService.sendNotify(request);
        return ResultModel.success(result);
    }
}
