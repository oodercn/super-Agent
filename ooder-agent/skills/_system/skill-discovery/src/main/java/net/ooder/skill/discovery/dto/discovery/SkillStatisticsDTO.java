package net.ooder.skill.discovery.dto.discovery;

import java.util.List;

public class SkillStatisticsDTO {
    
    private int total;
    private List<CategoryStatistics> byBusinessCategory;
    private List<CategoryStatistics> bySkillForm;
    private List<CategoryStatistics> byVisibility;
    private List<CategoryStatistics> byCategory;
    
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    
    public List<CategoryStatistics> getByBusinessCategory() { return byBusinessCategory; }
    public void setByBusinessCategory(List<CategoryStatistics> byBusinessCategory) { this.byBusinessCategory = byBusinessCategory; }
    
    public List<CategoryStatistics> getBySkillForm() { return bySkillForm; }
    public void setBySkillForm(List<CategoryStatistics> bySkillForm) { this.bySkillForm = bySkillForm; }
    
    public List<CategoryStatistics> getByVisibility() { return byVisibility; }
    public void setByVisibility(List<CategoryStatistics> byVisibility) { this.byVisibility = byVisibility; }
    
    public List<CategoryStatistics> getByCategory() { return byCategory; }
    public void setByCategory(List<CategoryStatistics> byCategory) { this.byCategory = byCategory; }
    
    public static class CategoryStatistics {
        private String name;
        private String label;
        private int count;
        private int percentage;
        
        public CategoryStatistics() {}
        
        public CategoryStatistics(String name, String label, int count, int total) {
            this.name = name;
            this.label = label;
            this.count = count;
            this.percentage = total > 0 ? Math.round((count * 100.0f) / total) : 0;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        
        public int getPercentage() { return percentage; }
        public void setPercentage(int percentage) { this.percentage = percentage; }
    }
}
