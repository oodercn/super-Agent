package net.ooder.skillcenter.model;

import java.util.List;
import java.util.Map;

/**
 * Spec Validation Models
 *
 * @author ooder Team
 * @since 2.0
 */
public class SpecValidationModels {

    public static class SpecValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        private Map<String, Object> details;

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }

    public static class VersionHistory {
        private String versionId;
        private String skillId;
        private String version;
        private String changeDescription;
        private String author;
        private long createTime;
        private Map<String, Object> changes;

        public String getVersionId() { return versionId; }
        public void setVersionId(String versionId) { this.versionId = versionId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getChangeDescription() { return changeDescription; }
        public void setChangeDescription(String changeDescription) { this.changeDescription = changeDescription; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        public Map<String, Object> getChanges() { return changes; }
        public void setChanges(Map<String, Object> changes) { this.changes = changes; }
    }

    public static class SpecValidationReport {
        private String skillId;
        private long validationTime;
        private boolean passed;
        private int totalChecks;
        private int passedChecks;
        private int failedChecks;
        private int warningCount;
        private List<ValidationCheck> checks;
        private Map<String, Object> summary;

        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public long getValidationTime() { return validationTime; }
        public void setValidationTime(long validationTime) { this.validationTime = validationTime; }
        public boolean isPassed() { return passed; }
        public void setPassed(boolean passed) { this.passed = passed; }
        public int getTotalChecks() { return totalChecks; }
        public void setTotalChecks(int totalChecks) { this.totalChecks = totalChecks; }
        public int getPassedChecks() { return passedChecks; }
        public void setPassedChecks(int passedChecks) { this.passedChecks = passedChecks; }
        public int getFailedChecks() { return failedChecks; }
        public void setFailedChecks(int failedChecks) { this.failedChecks = failedChecks; }
        public int getWarningCount() { return warningCount; }
        public void setWarningCount(int warningCount) { this.warningCount = warningCount; }
        public List<ValidationCheck> getChecks() { return checks; }
        public void setChecks(List<ValidationCheck> checks) { this.checks = checks; }
        public Map<String, Object> getSummary() { return summary; }
        public void setSummary(Map<String, Object> summary) { this.summary = summary; }
    }

    public static class ValidationCheck {
        private String checkId;
        private String name;
        private String category;
        private String severity;
        private boolean passed;
        private String message;
        private Map<String, Object> details;

        public String getCheckId() { return checkId; }
        public void setCheckId(String checkId) { this.checkId = checkId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public boolean isPassed() { return passed; }
        public void setPassed(boolean passed) { this.passed = passed; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Map<String, Object> getDetails() { return details; }
        public void setDetails(Map<String, Object> details) { this.details = details; }
    }
}
