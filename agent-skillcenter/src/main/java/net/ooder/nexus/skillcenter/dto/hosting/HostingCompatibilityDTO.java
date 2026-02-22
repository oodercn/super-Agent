package net.ooder.nexus.skillcenter.dto.hosting;

import java.util.List;

public class HostingCompatibilityDTO {
    private String skillId;
    private String skillName;
    private String skillType;
    private double compatibilityScore;
    private String recommendation;
    private List<CompatibilityCheck> checks;
    private List<String> issues;
    private List<String> suggestions;

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public String getSkillType() { return skillType; }
    public void setSkillType(String skillType) { this.skillType = skillType; }
    public double getCompatibilityScore() { return compatibilityScore; }
    public void setCompatibilityScore(double compatibilityScore) { this.compatibilityScore = compatibilityScore; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public List<CompatibilityCheck> getChecks() { return checks; }
    public void setChecks(List<CompatibilityCheck> checks) { this.checks = checks; }
    public List<String> getIssues() { return issues; }
    public void setIssues(List<String> issues) { this.issues = issues; }
    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

    public static class CompatibilityCheck {
        private String name;
        private String status;
        private String message;
        private boolean required;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
    }
}
