package net.ooder.mvp.skill.scene.dto.scene;

import net.ooder.mvp.skill.scene.dto.base.FullDTO;
import java.util.List;

public class SceneDTO extends FullDTO {
    
    private String sceneId;
    private String skillId;
    private String sceneType;
    private String visibility;
    private String skillForm;
    private List<String> capabilities;
    private List<String> dependencies;
    
    public SceneDTO() {
        super();
    }
    
    public String getSceneId() { 
        return sceneId; 
    }
    
    public void setSceneId(String sceneId) { 
        this.sceneId = sceneId; 
        this.id = sceneId;
    }
    
    public String getSkillId() { 
        return skillId; 
    }
    
    public void setSkillId(String skillId) { 
        this.skillId = skillId; 
    }
    
    public String getSceneType() { 
        return sceneType; 
    }
    
    public void setSceneType(String sceneType) { 
        this.sceneType = sceneType; 
    }
    
    public String getVisibility() { 
        return visibility; 
    }
    
    public void setVisibility(String visibility) { 
        this.visibility = visibility; 
    }
    
    public String getSkillForm() { 
        return skillForm; 
    }
    
    public void setSkillForm(String skillForm) { 
        this.skillForm = skillForm; 
    }
    
    public List<String> getCapabilities() { 
        return capabilities; 
    }
    
    public void setCapabilities(List<String> capabilities) { 
        this.capabilities = capabilities; 
    }
    
    public List<String> getDependencies() { 
        return dependencies; 
    }
    
    public void setDependencies(List<String> dependencies) { 
        this.dependencies = dependencies; 
    }
}
