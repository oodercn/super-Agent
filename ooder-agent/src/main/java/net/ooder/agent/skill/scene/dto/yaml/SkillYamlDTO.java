package net.ooder.mvp.skill.scene.dto.yaml;

import java.util.List;
import java.util.Map;

public class SkillYamlDTO {
    
    private String apiVersion;
    private String kind;
    private SkillMetadataDTO metadata;
    private SkillSpecDTO spec;
    
    public String getApiVersion() { return apiVersion; }
    public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }
    
    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }
    
    public SkillMetadataDTO getMetadata() { return metadata; }
    public void setMetadata(SkillMetadataDTO metadata) { this.metadata = metadata; }
    
    public SkillSpecDTO getSpec() { return spec; }
    public void setSpec(SkillSpecDTO spec) { this.spec = spec; }
}
