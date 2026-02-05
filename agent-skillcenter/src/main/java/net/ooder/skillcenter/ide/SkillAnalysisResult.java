package net.ooder.skillcenter.ide;

/**
 * 技能分析结果
 */
public class SkillAnalysisResult {
    private String skillId;
    private boolean analysisAvailable;
    private String message;
    private double performanceScore;
    private double codeQualityScore;
    private double securityScore;
    private String optimizationSuggestions;
    private String potentialIssues;
    
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
     * 分析是否可用
     * @return 分析是否可用
     */
    public boolean isAnalysisAvailable() {
        return analysisAvailable;
    }
    
    /**
     * 设置分析是否可用
     * @param analysisAvailable 分析是否可用
     */
    public void setAnalysisAvailable(boolean analysisAvailable) {
        this.analysisAvailable = analysisAvailable;
    }
    
    /**
     * 获取分析消息
     * @return 分析消息
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 设置分析消息
     * @param message 分析消息
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 获取性能评分
     * @return 性能评分
     */
    public double getPerformanceScore() {
        return performanceScore;
    }
    
    /**
     * 设置性能评分
     * @param performanceScore 性能评分
     */
    public void setPerformanceScore(double performanceScore) {
        this.performanceScore = performanceScore;
    }
    
    /**
     * 获取代码质量评分
     * @return 代码质量评分
     */
    public double getCodeQualityScore() {
        return codeQualityScore;
    }
    
    /**
     * 设置代码质量评分
     * @param codeQualityScore 代码质量评分
     */
    public void setCodeQualityScore(double codeQualityScore) {
        this.codeQualityScore = codeQualityScore;
    }
    
    /**
     * 获取安全评分
     * @return 安全评分
     */
    public double getSecurityScore() {
        return securityScore;
    }
    
    /**
     * 设置安全评分
     * @param securityScore 安全评分
     */
    public void setSecurityScore(double securityScore) {
        this.securityScore = securityScore;
    }
    
    /**
     * 获取优化建议
     * @return 优化建议
     */
    public String getOptimizationSuggestions() {
        return optimizationSuggestions;
    }
    
    /**
     * 设置优化建议
     * @param optimizationSuggestions 优化建议
     */
    public void setOptimizationSuggestions(String optimizationSuggestions) {
        this.optimizationSuggestions = optimizationSuggestions;
    }
    
    /**
     * 获取潜在问题
     * @return 潜在问题
     */
    public String getPotentialIssues() {
        return potentialIssues;
    }
    
    /**
     * 设置潜在问题
     * @param potentialIssues 潜在问题
     */
    public void setPotentialIssues(String potentialIssues) {
        this.potentialIssues = potentialIssues;
    }
}