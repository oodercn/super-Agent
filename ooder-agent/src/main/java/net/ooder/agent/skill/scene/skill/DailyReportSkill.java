package net.ooder.mvp.skill.scene.skill;

import net.ooder.mvp.skill.scene.dto.discovery.CapabilityDTO;
import net.ooder.mvp.skill.scene.dto.dailyreport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/skills/daily-report")
public class DailyReportSkill {

    private static final Logger log = LoggerFactory.getLogger(DailyReportSkill.class);

    private final Map<String, List<Report>> reports = new ConcurrentHashMap<>();
    private final Map<String, List<Reminder>> reminders = new ConcurrentHashMap<>();

    @GetMapping("/capabilities")
    public List<CapabilityDTO> getCapabilities() {
        List<CapabilityDTO> capabilities = new ArrayList<>();
        
        CapabilityDTO remindCap = new CapabilityDTO();
        remindCap.setCapId("report-remind");
        remindCap.setName("日志提醒");
        remindCap.setDescription("定时提醒员工提交日志");
        remindCap.setCategory("notification");
        capabilities.add(remindCap);
        
        CapabilityDTO submitCap = new CapabilityDTO();
        submitCap.setCapId("report-submit");
        submitCap.setName("日志提交");
        submitCap.setDescription("员工提交工作日志");
        submitCap.setCategory("data-input");
        capabilities.add(submitCap);
        
        CapabilityDTO aggregateCap = new CapabilityDTO();
        aggregateCap.setCapId("report-aggregate");
        aggregateCap.setName("日志汇总");
        aggregateCap.setDescription("汇总所有员工日志");
        aggregateCap.setCategory("data-processing");
        capabilities.add(aggregateCap);
        
        CapabilityDTO analyzeCap = new CapabilityDTO();
        analyzeCap.setCapId("report-analyze");
        analyzeCap.setName("日志分析");
        analyzeCap.setDescription("AI分析日志内容");
        analyzeCap.setCategory("intelligence");
        capabilities.add(analyzeCap);
        
        return capabilities;
    }

    @PostMapping("/remind")
    public DailyReportResponseDTO sendReminder(@RequestBody @Valid ReminderRequestDTO request) {
        log.info("Sending reminder with params: sceneGroupId={}, targetUsers={}", 
                request.getSceneGroupId(), request.getTargetUsers());
        
        String sceneGroupId = request.getSceneGroupId();
        List<String> targetUsers = request.getTargetUsers();
        String message = request.getMessage() != null ? request.getMessage() : "请及时提交今日工作日志";
        
        Reminder reminder = new Reminder();
        reminder.setReminderId("remind-" + System.currentTimeMillis());
        reminder.setSceneGroupId(sceneGroupId);
        reminder.setTargetUsers(targetUsers);
        reminder.setMessage(message);
        reminder.setSendTime(System.currentTimeMillis());
        reminder.setStatus("sent");
        
        reminders.computeIfAbsent(sceneGroupId, k -> new ArrayList<>()).add(reminder);
        
        DailyReportResponseDTO response = DailyReportResponseDTO.success("提醒已发送");
        response.setReminderId(reminder.getReminderId());
        response.setSentCount(targetUsers != null ? targetUsers.size() : 0);
        
        return response;
    }

    @PostMapping("/submit")
    public DailyReportResponseDTO submitReport(@RequestBody @Valid ReportSubmitRequestDTO request) {
        log.info("Submitting report with params: sceneGroupId={}, userId={}", 
                request.getSceneGroupId(), request.getUserId());
        
        Report report = new Report();
        report.setReportId("report-" + System.currentTimeMillis());
        report.setSceneGroupId(request.getSceneGroupId());
        report.setUserId(request.getUserId());
        report.setContent(request.getContent());
        report.setAttachments(request.getAttachments());
        report.setSubmitTime(System.currentTimeMillis());
        report.setDate(new Date());
        
        reports.computeIfAbsent(request.getSceneGroupId(), k -> new ArrayList<>()).add(report);
        
        DailyReportResponseDTO response = DailyReportResponseDTO.success("日志提交成功");
        response.setReportId(report.getReportId());
        
        return response;
    }

    @PostMapping("/aggregate")
    public DailyReportResponseDTO aggregateReports(@RequestBody @Valid AggregateRequestDTO request) {
        log.info("Aggregating reports with params: sceneGroupId={}", request.getSceneGroupId());
        
        String sceneGroupId = request.getSceneGroupId();
        List<String> userIds = request.getUserIds();
        
        List<Report> sceneReports = reports.getOrDefault(sceneGroupId, new ArrayList<>());
        
        List<Report> filteredReports = new ArrayList<>();
        for (Report report : sceneReports) {
            if (userIds == null || userIds.isEmpty() || userIds.contains(report.getUserId())) {
                filteredReports.add(report);
            }
        }
        
        Map<String, Integer> userReportCount = new HashMap<>();
        
        for (Report report : filteredReports) {
            String userId = report.getUserId();
            userReportCount.merge(userId, 1, Integer::sum);
        }
        
        DailyReportResponseDTO response = DailyReportResponseDTO.success("汇总完成");
        response.setTotalReports(filteredReports.size());
        response.setUserReportCount(userReportCount);
        response.setReports(filteredReports);
        response.setAggregateTime(System.currentTimeMillis());
        
        return response;
    }

    @PostMapping("/analyze")
    public DailyReportResponseDTO analyzeReports(@RequestBody @Valid AnalyzeRequestDTO request) {
        log.info("Analyzing reports with params: sceneGroupId={}", request.getSceneGroupId());
        
        String analyzeType = request.getAnalyzeType() != null ? request.getAnalyzeType() : "summary";
        
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("analyzeType", analyzeType);
        analysis.put("analyzeTime", System.currentTimeMillis());
        
        if ("summary".equals(analyzeType)) {
            analysis.put("summary", "今日团队整体工作进展顺利，共完成" + 
                    (request.getReports() != null ? request.getReports().size() : 0) + "项任务。");
            analysis.put("highlights", Arrays.asList("项目进度正常", "团队协作良好", "无重大问题"));
            analysis.put("suggestions", Arrays.asList("继续保持良好的工作节奏", "关注项目关键节点"));
        } else if ("sentiment".equals(analyzeType)) {
            analysis.put("sentiment", "positive");
            analysis.put("confidence", 0.85);
            analysis.put("keywords", Arrays.asList("完成", "进展", "顺利", "协作"));
        }
        
        DailyReportResponseDTO response = DailyReportResponseDTO.success("分析完成");
        response.setAnalysis(analysis);
        
        return response;
    }

    @GetMapping("/reports/{sceneGroupId}")
    public DailyReportResponseDTO getReports(@PathVariable String sceneGroupId) {
        List<Report> sceneReports = reports.getOrDefault(sceneGroupId, new ArrayList<>());
        
        DailyReportResponseDTO response = DailyReportResponseDTO.success("查询成功");
        response.setReports(sceneReports);
        response.setTotal(sceneReports.size());
        
        return response;
    }

    public static class Report {
        private String reportId;
        private String sceneGroupId;
        private String userId;
        private String content;
        private List<String> attachments;
        private long submitTime;
        private Date date;

        public String getReportId() { return reportId; }
        public void setReportId(String reportId) { this.reportId = reportId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public List<String> getAttachments() { return attachments; }
        public void setAttachments(List<String> attachments) { this.attachments = attachments; }
        public long getSubmitTime() { return submitTime; }
        public void setSubmitTime(long submitTime) { this.submitTime = submitTime; }
        public Date getDate() { return date; }
        public void setDate(Date date) { this.date = date; }
    }

    public static class Reminder {
        private String reminderId;
        private String sceneGroupId;
        private List<String> targetUsers;
        private String message;
        private long sendTime;
        private String status;

        public String getReminderId() { return reminderId; }
        public void setReminderId(String reminderId) { this.reminderId = reminderId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public List<String> getTargetUsers() { return targetUsers; }
        public void setTargetUsers(List<String> targetUsers) { this.targetUsers = targetUsers; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getSendTime() { return sendTime; }
        public void setSendTime(long sendTime) { this.sendTime = sendTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
