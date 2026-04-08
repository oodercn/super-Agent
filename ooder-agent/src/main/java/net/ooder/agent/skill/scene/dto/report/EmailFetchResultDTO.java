package net.ooder.mvp.skill.scene.dto.report;

import java.util.List;

public class EmailFetchResultDTO {
    private String userId;
    private List<EmailItem> emails;
    private String summary;
    private List<String> workItems;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<EmailItem> getEmails() { return emails; }
    public void setEmails(List<EmailItem> emails) { this.emails = emails; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public List<String> getWorkItems() { return workItems; }
    public void setWorkItems(List<String> workItems) { this.workItems = workItems; }

    public static class EmailItem {
        private String subject;
        private String from;
        private Long time;
        private String summary;

        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        public Long getTime() { return time; }
        public void setTime(Long time) { this.time = time; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
    }
}
