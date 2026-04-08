package net.ooder.skill.discovery.dto.discovery;

import java.util.List;

public class CategoryDetailDTO {
    
    private String code;
    private String name;
    private String icon;
    private String color;
    private int count;
    private int percentage;
    private List<String> skillIds;
    private List<String> absolutePaths;
    
    public CategoryDetailDTO() {}
    
    public CategoryDetailDTO(String code, String name, int count, int total) {
        this.code = code;
        this.name = name;
        this.count = count;
        this.percentage = total > 0 ? Math.round((count * 100.0f) / total) : 0;
    }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    
    public int getPercentage() { return percentage; }
    public void setPercentage(int percentage) { this.percentage = percentage; }
    
    public List<String> getSkillIds() { return skillIds; }
    public void setSkillIds(List<String> skillIds) { this.skillIds = skillIds; }
    
    public List<String> getAbsolutePaths() { return absolutePaths; }
    public void setAbsolutePaths(List<String> absolutePaths) { this.absolutePaths = absolutePaths; }
}
