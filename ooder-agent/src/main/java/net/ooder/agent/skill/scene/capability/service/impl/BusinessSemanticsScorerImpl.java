package net.ooder.mvp.skill.scene.capability.service.impl;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.service.BusinessSemanticsScorer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BusinessSemanticsScorerImpl implements BusinessSemanticsScorer {

    private static final Logger log = LoggerFactory.getLogger(BusinessSemanticsScorerImpl.class);
    
    private static final int DRIVER_CONDITIONS_MAX_SCORE = 3;
    private static final int PARTICIPANTS_MAX_SCORE = 3;
    private static final int VISIBILITY_MAX_SCORE = 2;
    private static final int COLLABORATION_MAX_SCORE = 1;
    private static final int BUSINESS_TAGS_MAX_SCORE = 1;
    private static final int MAX_TOTAL_SCORE = 10;

    @Override
    public int calculateScore(Capability capability) {
        if (capability == null) {
            return 0;
        }
        return getDetailedScore(capability).getTotalScore();
    }

    @Override
    public int calculateScore(Map<String, Object> metadata) {
        if (metadata == null) {
            return 0;
        }
        return getDetailedScore(metadata).getTotalScore();
    }

    @Override
    public BusinessSemanticsScore getDetailedScore(Capability capability) {
        BusinessSemanticsScore score = new BusinessSemanticsScore();
        
        if (capability == null) {
            score.setLevel("none");
            return score;
        }
        
        int driverConditionsScore = scoreDriverConditions(capability.getDriverConditions());
        int participantsScore = scoreParticipants(capability.getParticipants());
        int visibilityScore = scoreVisibility(capability.getVisibility());
        int collaborationScore = scoreCollaboration(capability.getCollaborativeCapabilities());
        int businessTagsScore = scoreBusinessTags(capability.getMetadata());
        
        score.setDriverConditionsScore(driverConditionsScore);
        score.setParticipantsScore(participantsScore);
        score.setVisibilityScore(visibilityScore);
        score.setCollaborationScore(collaborationScore);
        score.setBusinessTagsScore(businessTagsScore);
        
        int totalScore = driverConditionsScore + participantsScore + visibilityScore 
            + collaborationScore + businessTagsScore;
        score.setTotalScore(totalScore);
        score.setLevel(determineLevel(totalScore));
        
        log.debug("[getDetailedScore] Capability: {}, Total: {}, DriverConditions: {}, Participants: {}, Visibility: {}, Collaboration: {}, BusinessTags: {}",
            capability.getCapabilityId(), totalScore, driverConditionsScore, participantsScore, 
            visibilityScore, collaborationScore, businessTagsScore);
        
        return score;
    }

    @Override
    public BusinessSemanticsScore getDetailedScore(Map<String, Object> metadata) {
        BusinessSemanticsScore score = new BusinessSemanticsScore();
        
        if (metadata == null) {
            score.setLevel("none");
            return score;
        }
        
        int driverConditionsScore = scoreDriverConditionsFromMap(metadata);
        int participantsScore = scoreParticipantsFromMap(metadata);
        int visibilityScore = scoreVisibilityFromMap(metadata);
        int collaborationScore = scoreCollaborationFromMap(metadata);
        int businessTagsScore = scoreBusinessTagsFromMap(metadata);
        
        score.setDriverConditionsScore(driverConditionsScore);
        score.setParticipantsScore(participantsScore);
        score.setVisibilityScore(visibilityScore);
        score.setCollaborationScore(collaborationScore);
        score.setBusinessTagsScore(businessTagsScore);
        
        int totalScore = driverConditionsScore + participantsScore + visibilityScore 
            + collaborationScore + businessTagsScore;
        score.setTotalScore(totalScore);
        score.setLevel(determineLevel(totalScore));
        
        return score;
    }

    private int scoreDriverConditions(List<?> driverConditions) {
        if (driverConditions != null && !driverConditions.isEmpty()) {
            return DRIVER_CONDITIONS_MAX_SCORE;
        }
        return 0;
    }

    private int scoreDriverConditionsFromMap(Map<String, Object> metadata) {
        Object driverConditions = metadata.get("driverConditions");
        if (driverConditions instanceof List && !((List<?>) driverConditions).isEmpty()) {
            return DRIVER_CONDITIONS_MAX_SCORE;
        }
        
        Object driverCapabilities = metadata.get("driverCapabilities");
        if (driverCapabilities instanceof List && !((List<?>) driverCapabilities).isEmpty()) {
            return DRIVER_CONDITIONS_MAX_SCORE;
        }
        
        Object requiredCapabilities = metadata.get("requiredCapabilities");
        if (requiredCapabilities instanceof List && !((List<?>) requiredCapabilities).isEmpty()) {
            return DRIVER_CONDITIONS_MAX_SCORE;
        }
        
        Object spec = metadata.get("spec");
        if (spec instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> specMap = (Map<String, Object>) spec;
            Object specDriverConditions = specMap.get("driverConditions");
            if (specDriverConditions instanceof List && !((List<?>) specDriverConditions).isEmpty()) {
                return DRIVER_CONDITIONS_MAX_SCORE;
            }
        }
        
        return 0;
    }

    private int scoreParticipants(List<?> participants) {
        if (participants != null && !participants.isEmpty()) {
            return PARTICIPANTS_MAX_SCORE;
        }
        return 0;
    }

    private int scoreParticipantsFromMap(Map<String, Object> metadata) {
        Object participants = metadata.get("participants");
        if (participants instanceof List && !((List<?>) participants).isEmpty()) {
            return PARTICIPANTS_MAX_SCORE;
        }
        
        Object maxMembers = metadata.get("maxMembers");
        if (maxMembers != null) {
            return PARTICIPANTS_MAX_SCORE;
        }
        
        Object spec = metadata.get("spec");
        if (spec instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> specMap = (Map<String, Object>) spec;
            Object specParticipants = specMap.get("participants");
            if (specParticipants instanceof List && !((List<?>) specParticipants).isEmpty()) {
                return PARTICIPANTS_MAX_SCORE;
            }
        }
        
        return 0;
    }

    private int scoreVisibility(String visibility) {
        if ("public".equals(visibility)) {
            return VISIBILITY_MAX_SCORE;
        }
        return 0;
    }

    private int scoreVisibilityFromMap(Map<String, Object> metadata) {
        Object visibility = metadata.get("visibility");
        if ("public".equals(visibility)) {
            return VISIBILITY_MAX_SCORE;
        }
        return 0;
    }

    private int scoreCollaboration(List<?> collaborativeCapabilities) {
        if (collaborativeCapabilities != null && !collaborativeCapabilities.isEmpty()) {
            return COLLABORATION_MAX_SCORE;
        }
        
        return 0;
    }

    private int scoreCollaborationFromMap(Map<String, Object> metadata) {
        Object collaboration = metadata.get("collaboration");
        if (collaboration != null) {
            return COLLABORATION_MAX_SCORE;
        }
        
        Object collabCaps = metadata.get("collaborativeCapabilities");
        if (collabCaps instanceof List && !((List<?>) collabCaps).isEmpty()) {
            return COLLABORATION_MAX_SCORE;
        }
        
        return 0;
    }

    private int scoreBusinessTags(Map<String, Object> metadata) {
        if (metadata == null) {
            return 0;
        }
        
        Object businessTags = metadata.get("businessTags");
        if (businessTags instanceof List && !((List<?>) businessTags).isEmpty()) {
            return BUSINESS_TAGS_MAX_SCORE;
        }
        
        Object tags = metadata.get("tags");
        if (tags instanceof List && !((List<?>) tags).isEmpty()) {
            return BUSINESS_TAGS_MAX_SCORE;
        }
        
        return 0;
    }

    private int scoreBusinessTagsFromMap(Map<String, Object> metadata) {
        Object businessTags = metadata.get("businessTags");
        if (businessTags instanceof List && !((List<?>) businessTags).isEmpty()) {
            return BUSINESS_TAGS_MAX_SCORE;
        }
        
        Object tags = metadata.get("tags");
        if (tags instanceof List && !((List<?>) tags).isEmpty()) {
            return BUSINESS_TAGS_MAX_SCORE;
        }
        
        return 0;
    }

    private String determineLevel(int totalScore) {
        if (totalScore >= 8) {
            return "high";
        } else if (totalScore >= 3) {
            return "medium";
        } else {
            return "low";
        }
    }
}
