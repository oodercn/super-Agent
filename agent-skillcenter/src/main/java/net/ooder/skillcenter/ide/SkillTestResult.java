package net.ooder.skillcenter.ide;

import java.util.List;

/**
 * 技能测试结果
 */
public class SkillTestResult {
    private String skillId;
    private boolean success;
    private String message;
    private int testCount;
    private int passCount;
    private int failCount;
    private List<String> testDetails;
    private long executionTime;
    
    /**
     * 获取技能ID
     * @return 技能ID
     */
    public String getSkillId() {
        return skillId;
    }
    
    /**
     * 设置技能ID
     * @param skillId 技能ID
     */
    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }
    
    /**
     * 是否测试成功
     * @return 是否测试成功
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * 设置测试是否成功
     * @param success 是否测试成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    /**
     * 获取测试消息
     * @return 测试消息
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 设置测试消息
     * @param message 测试消息
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 获取测试总数
     * @return 测试总数
     */
    public int getTestCount() {
        return testCount;
    }
    
    /**
     * 设置测试总数
     * @param testCount 测试总数
     */
    public void setTestCount(int testCount) {
        this.testCount = testCount;
    }
    
    /**
     * 获取通过的测试数
     * @return 通过的测试数
     */
    public int getPassCount() {
        return passCount;
    }
    
    /**
     * 设置通过的测试数
     * @param passCount 通过的测试数
     */
    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }
    
    /**
     * 获取失败的测试数
     * @return 失败的测试数
     */
    public int getFailCount() {
        return failCount;
    }
    
    /**
     * 设置失败的测试数
     * @param failCount 失败的测试数
     */
    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
    
    /**
     * 获取测试详情
     * @return 测试详情
     */
    public List<String> getTestDetails() {
        return testDetails;
    }
    
    /**
     * 设置测试详情
     * @param testDetails 测试详情
     */
    public void setTestDetails(List<String> testDetails) {
        this.testDetails = testDetails;
    }
    
    /**
     * 获取执行时间
     * @return 执行时间
     */
    public long getExecutionTime() {
        return executionTime;
    }
    
    /**
     * 设置执行时间
     * @param executionTime 执行时间
     */
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}