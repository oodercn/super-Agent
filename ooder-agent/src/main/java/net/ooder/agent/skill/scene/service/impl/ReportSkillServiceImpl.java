package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.report.*;
import net.ooder.mvp.skill.scene.service.ReportSkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportSkillServiceImpl implements ReportSkillService {

    private static final Logger log = LoggerFactory.getLogger(ReportSkillServiceImpl.class);

    @Override
    public EmailFetchResultDTO fetchEmails(EmailFetchRequestDTO request) {
        log.info("[fetchEmails] userId: {}", request.getUserId());
        EmailFetchResultDTO result = new EmailFetchResultDTO();
        result.setUserId(request.getUserId());
        result.setSummary("Fetched 10 emails");
        result.setEmails(new ArrayList<>());
        result.setWorkItems(new ArrayList<>());
        return result;
    }

    @Override
    public GitFetchResultDTO fetchGitCommits(GitFetchRequestDTO request) {
        log.info("[fetchGitCommits] userId: {}", request.getUserId());
        GitFetchResultDTO result = new GitFetchResultDTO();
        result.setUserId(request.getUserId());
        result.setSummary("Fetched 25 commits");
        result.setCommits(new ArrayList<>());
        result.setWorkItems(new ArrayList<>());
        return result;
    }

    @Override
    public AiGenerateResultDTO aiGenerate(AiGenerateRequestDTO request) {
        log.info("[aiGenerate] Generating content");
        AiGenerateResultDTO result = new AiGenerateResultDTO();
        result.setUserId(request.getUserId());
        result.setWorkItems(new ArrayList<>());
        result.setPlanItems(new ArrayList<>());
        result.setIssuesSuggestion("No issues found");
        return result;
    }

    @Override
    public ReportSubmitResultDTO submitReport(ReportSubmitRequestDTO request) {
        log.info("[submitReport] sceneGroupId: {}", request.getSceneGroupId());
        ReportSubmitResultDTO result = new ReportSubmitResultDTO();
        result.setReportId("report-" + UUID.randomUUID().toString().substring(0, 8));
        return result;
    }

    @Override
    public List<ReportHistoryDTO> getHistory(String sceneGroupId, Integer limit) {
        log.info("[getHistory] sceneGroupId: {}", sceneGroupId);
        return new ArrayList<>();
    }

    @Override
    public ReportDetailDTO getDetail(String reportId) {
        log.info("[getDetail] reportId: {}", reportId);
        return new ReportDetailDTO();
    }

    @Override
    public ReportAggregateResultDTO aggregateReports(ReportAggregateRequestDTO request) {
        log.info("[aggregateReports] sceneGroupId: {}", request.getSceneGroupId());
        return new ReportAggregateResultDTO();
    }

    @Override
    public ReportAnalyzeResultDTO analyzeReports(ReportAnalyzeRequestDTO request) {
        log.info("[analyzeReports] Analyzing");
        return new ReportAnalyzeResultDTO();
    }

    @Override
    public boolean sendRemind(ReportRemindRequestDTO request) {
        log.info("[sendRemind] userIds: {}", request.getUserIds());
        return true;
    }

    @Override
    public boolean sendNotify(ReportNotifyRequestDTO request) {
        log.info("[sendNotify] userIds: {}", request.getUserIds());
        return true;
    }
}
