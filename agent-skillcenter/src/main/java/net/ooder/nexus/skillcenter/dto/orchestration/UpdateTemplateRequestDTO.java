package net.ooder.nexus.skillcenter.dto.orchestration;

import net.ooder.nexus.skillcenter.dto.orchestration.OrchestrationTemplateDTO.SkillNode;
import net.ooder.nexus.skillcenter.dto.orchestration.OrchestrationTemplateDTO.DataFlow;
import java.util.List;

public class UpdateTemplateRequestDTO {
    private String templateId;
    private String name;
    private String description;
    private List<SkillNode> skills;
    private List<DataFlow> dataFlows;

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<SkillNode> getSkills() { return skills; }
    public void setSkills(List<SkillNode> skills) { this.skills = skills; }
    public List<DataFlow> getDataFlows() { return dataFlows; }
    public void setDataFlows(List<DataFlow> dataFlows) { this.dataFlows = dataFlows; }
}
