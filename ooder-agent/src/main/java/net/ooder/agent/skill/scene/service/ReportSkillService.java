package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.report.*;
import java.util.List;

public interface ReportSkillService {
    
    EmailFetchResultDTO fetchEmails(EmailFetchRequestDTO request);
    
    GitFetchResultDTO fetchGitCommits(GitFetchRequestDTO request);
    
    AiGenerateResultDTO aiGenerate(AiGenerateRequestDTO request);
    
    ReportSubmitResultDTO submitReport(ReportSubmitRequestDTO request);
    
    List<ReportHistoryDTO> getHistory(String sceneGroupId, Integer limit);
    
    ReportDetailDTO getDetail(String reportId);
    
    ReportAggregateResultDTO aggregateReports(ReportAggregateRequestDTO request);
    
    ReportAnalyzeResultDTO analyzeReports(ReportAnalyzeRequestDTO request);
    
    boolean sendRemind(ReportRemindRequestDTO request);
    
    boolean sendNotify(ReportNotifyRequestDTO request);
}
