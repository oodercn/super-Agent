package net.ooder.skillcenter.lifecycle.registration;

import java.util.Map;

public interface SkillRegistrationManager {
    
    RegistrationResult registerSkill(SkillRegistrationRequest request);
    
    RegistrationResult updateSkill(String skillId, SkillUpdateRequest request);
    
    boolean unregisterSkill(String skillId);
    
    SkillRegistrationInfo getSkillRegistration(String skillId);
    
    java.util.List<SkillRegistrationInfo> getAllRegistrations();
    
    java.util.List<SkillRegistrationInfo> getRegistrationsByStatus(String status);
    
    boolean approveRegistration(String skillId, String reviewer, String comments);
    
    boolean rejectRegistration(String skillId, String reviewer, String comments);
    
    class SkillRegistrationRequest {
        private String skillId;
        private String skillName;
        private String description;
        private String category;
        private String version;
        private byte[] skillCode;
        private Map<String, String> parameters;
        private Map<String, Object> metadata;
        private String applicant;
        
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
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
        
        public byte[] getSkillCode() {
            return skillCode;
        }
        
        public void setSkillCode(byte[] skillCode) {
            this.skillCode = skillCode;
        }
        
        public Map<String, String> getParameters() {
            return parameters;
        }
        
        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }
        
        public Map<String, Object> getMetadata() {
            return metadata;
        }
        
        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }
        
        public String getApplicant() {
            return applicant;
        }
        
        public void setApplicant(String applicant) {
            this.applicant = applicant;
        }
    }
    
    class SkillUpdateRequest {
        private String skillName;
        private String description;
        private String category;
        private String version;
        private byte[] skillCode;
        private Map<String, String> parameters;
        private Map<String, Object> metadata;
        
        public String getSkillName() {
            return skillName;
        }
        
        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
        
        public byte[] getSkillCode() {
            return skillCode;
        }
        
        public void setSkillCode(byte[] skillCode) {
            this.skillCode = skillCode;
        }
        
        public Map<String, String> getParameters() {
            return parameters;
        }
        
        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }
        
        public Map<String, Object> getMetadata() {
            return metadata;
        }
        
        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }
    }
    
    class RegistrationResult {
        private boolean success;
        private String skillId;
        private String message;
        private String status;
        private long timestamp;
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getSkillId() {
            return skillId;
        }
        
        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
    
    class SkillRegistrationInfo {
        private String skillId;
        private String skillName;
        private String description;
        private String category;
        private String version;
        private String status;
        private String applicant;
        private String reviewer;
        private String comments;
        private long createdAt;
        private long updatedAt;
        
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
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public String getApplicant() {
            return applicant;
        }
        
        public void setApplicant(String applicant) {
            this.applicant = applicant;
        }
        
        public String getReviewer() {
            return reviewer;
        }
        
        public void setReviewer(String reviewer) {
            this.reviewer = reviewer;
        }
        
        public String getComments() {
            return comments;
        }
        
        public void setComments(String comments) {
            this.comments = comments;
        }
        
        public long getCreatedAt() {
            return createdAt;
        }
        
        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }
        
        public long getUpdatedAt() {
            return updatedAt;
        }
        
        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
