package net.ooder.nexus.model.llm;

import java.util.List;

public class ContextAnalysis {
    private String requestId;
    private String intent;
    private List<String> entities;
    private String context;
    private List<SkillRecommendation> recommendedSkills;
    private double confidence;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<String> getEntities() {
        return entities;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<SkillRecommendation> getRecommendedSkills() {
        return recommendedSkills;
    }

    public void setRecommendedSkills(List<SkillRecommendation> recommendedSkills) {
        this.recommendedSkills = recommendedSkills;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public static class SkillRecommendation {
        private String skillId;
        private String skillName;
        private double confidence;
        private String reason;

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
