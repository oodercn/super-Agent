package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import java.util.List;
import java.util.Map;

public interface BusinessSemanticsScorer {
    
    int calculateScore(Capability capability);
    
    int calculateScore(Map<String, Object> metadata);
    
    BusinessSemanticsScore getDetailedScore(Capability capability);
    
    BusinessSemanticsScore getDetailedScore(Map<String, Object> metadata);
    
    public static class BusinessSemanticsScore {
        private int totalScore;
        private int driverConditionsScore;
        private int participantsScore;
        private int visibilityScore;
        private int collaborationScore;
        private int businessTagsScore;
        private String level;
        
        public int getTotalScore() { return totalScore; }
        public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
        public int getDriverConditionsScore() { return driverConditionsScore; }
        public void setDriverConditionsScore(int driverConditionsScore) { this.driverConditionsScore = driverConditionsScore; }
        public int getParticipantsScore() { return participantsScore; }
        public void setParticipantsScore(int participantsScore) { this.participantsScore = participantsScore; }
        public int getVisibilityScore() { return visibilityScore; }
        public void setVisibilityScore(int visibilityScore) { this.visibilityScore = visibilityScore; }
        public int getCollaborationScore() { return collaborationScore; }
        public void setCollaborationScore(int collaborationScore) { this.collaborationScore = collaborationScore; }
        public int getBusinessTagsScore() { return businessTagsScore; }
        public void setBusinessTagsScore(int businessTagsScore) { this.businessTagsScore = businessTagsScore; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        
        public boolean hasHighBusinessSemantics() {
            return totalScore >= 8;
        }
        
        public boolean hasMediumBusinessSemantics() {
            return totalScore >= 3 && totalScore < 8;
        }
        
        public boolean hasLowBusinessSemantics() {
            return totalScore < 3;
        }
    }
}
