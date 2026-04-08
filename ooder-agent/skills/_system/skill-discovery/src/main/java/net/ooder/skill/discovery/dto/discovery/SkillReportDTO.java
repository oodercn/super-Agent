package net.ooder.skill.discovery.dto.discovery;

import java.util.List;

public class SkillReportDTO {
    
    private int total;
    private List<DirectoryStatsDTO> directoryStats;
    private List<CategoryDetailDTO> byBusinessCategory;
    private List<CategoryDetailDTO> bySkillForm;
    private List<CategoryDetailDTO> bySkillCategory;
    private List<CategoryDetailDTO> byCapabilityCategory;
    private List<CategoryDetailDTO> byVisibility;
    private List<SkillPathInfoDTO> skillPaths;
    private List<DimensionComparisonDTO> dimensionComparison;
    private TestSuggestionDTO testSuggestion;
    private long timestamp;
    
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    
    public List<DirectoryStatsDTO> getDirectoryStats() { return directoryStats; }
    public void setDirectoryStats(List<DirectoryStatsDTO> directoryStats) { this.directoryStats = directoryStats; }
    
    public List<CategoryDetailDTO> getByBusinessCategory() { return byBusinessCategory; }
    public void setByBusinessCategory(List<CategoryDetailDTO> byBusinessCategory) { this.byBusinessCategory = byBusinessCategory; }
    
    public List<CategoryDetailDTO> getBySkillForm() { return bySkillForm; }
    public void setBySkillForm(List<CategoryDetailDTO> bySkillForm) { this.bySkillForm = bySkillForm; }
    
    public List<CategoryDetailDTO> getBySkillCategory() { return bySkillCategory; }
    public void setBySkillCategory(List<CategoryDetailDTO> bySkillCategory) { this.bySkillCategory = bySkillCategory; }
    
    public List<CategoryDetailDTO> getByCapabilityCategory() { return byCapabilityCategory; }
    public void setByCapabilityCategory(List<CategoryDetailDTO> byCapabilityCategory) { this.byCapabilityCategory = byCapabilityCategory; }
    
    public List<CategoryDetailDTO> getByVisibility() { return byVisibility; }
    public void setByVisibility(List<CategoryDetailDTO> byVisibility) { this.byVisibility = byVisibility; }
    
    public List<SkillPathInfoDTO> getSkillPaths() { return skillPaths; }
    public void setSkillPaths(List<SkillPathInfoDTO> skillPaths) { this.skillPaths = skillPaths; }
    
    public List<DimensionComparisonDTO> getDimensionComparison() { return dimensionComparison; }
    public void setDimensionComparison(List<DimensionComparisonDTO> dimensionComparison) { this.dimensionComparison = dimensionComparison; }
    
    public TestSuggestionDTO getTestSuggestion() { return testSuggestion; }
    public void setTestSuggestion(TestSuggestionDTO testSuggestion) { this.testSuggestion = testSuggestion; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
