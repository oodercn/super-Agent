package net.ooder.mvp.skill.scene.dto.skill;

import net.ooder.mvp.skill.scene.dto.base.FullDTO;
import java.util.List;

public class SkillDTO extends FullDTO {
    
    private String skillId;
    private String version;
    private String author;
    private String icon;
    private String category;
    private String subCategory;
    private List<String> tags;
    private Long installedAt;
    
    public SkillDTO() {
        super();
    }
    
    public String getSkillId() { 
        return skillId; 
    }
    
    public void setSkillId(String skillId) { 
        this.skillId = skillId; 
        this.id = skillId;
    }
    
    public String getVersion() { 
        return version; 
    }
    
    public void setVersion(String version) { 
        this.version = version; 
    }
    
    public String getAuthor() { 
        return author; 
    }
    
    public void setAuthor(String author) { 
        this.author = author; 
    }
    
    public String getIcon() { 
        return icon; 
    }
    
    public void setIcon(String icon) { 
        this.icon = icon; 
    }
    
    public String getCategory() { 
        return category; 
    }
    
    public void setCategory(String category) { 
        this.category = category; 
    }
    
    public String getSubCategory() { 
        return subCategory; 
    }
    
    public void setSubCategory(String subCategory) { 
        this.subCategory = subCategory; 
    }
    
    public List<String> getTags() { 
        return tags; 
    }
    
    public void setTags(List<String> tags) { 
        this.tags = tags; 
    }
    
    public Long getInstalledAt() { 
        return installedAt; 
    }
    
    public void setInstalledAt(Long installedAt) { 
        this.installedAt = installedAt; 
    }
}
