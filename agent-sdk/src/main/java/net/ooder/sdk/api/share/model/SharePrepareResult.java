package net.ooder.sdk.api.share.model;

public class SharePrepareResult {
    
    private String shareId;
    private String skillId;
    private String skillName;
    private String version;
    private long packageSize;
    private String packageHash;
    private long preparedAt;
    private long expiresAt;
    private boolean success;
    private String errorCode;
    private String errorMessage;
    
    public String getShareId() { return shareId; }
    public void setShareId(String shareId) { this.shareId = shareId; }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public long getPackageSize() { return packageSize; }
    public void setPackageSize(long packageSize) { this.packageSize = packageSize; }
    
    public String getPackageHash() { return packageHash; }
    public void setPackageHash(String packageHash) { this.packageHash = packageHash; }
    
    public long getPreparedAt() { return preparedAt; }
    public void setPreparedAt(long preparedAt) { this.preparedAt = preparedAt; }
    
    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
