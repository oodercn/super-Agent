package net.ooder.mvp.skill.scene.dto.config;

public class TestResultDTO {
    
    private Boolean success;
    private String message;
    private Long testedAt;
    private String responseTime;
    
    public TestResultDTO() {
    }
    
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Long getTestedAt() { return testedAt; }
    public void setTestedAt(Long testedAt) { this.testedAt = testedAt; }
    
    public String getResponseTime() { return responseTime; }
    public void setResponseTime(String responseTime) { this.responseTime = responseTime; }
}
