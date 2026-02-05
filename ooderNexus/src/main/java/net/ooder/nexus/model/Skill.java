package net.ooder.nexus.model;

import java.util.Map;

public interface Skill {
    String getId();
    
    String getName();
    
    String getDescription();
    
    SkillResult execute(SkillContext context) throws SkillException;
    
    boolean isAvailable();
    
    Map<String, SkillParam> getParams();
}
