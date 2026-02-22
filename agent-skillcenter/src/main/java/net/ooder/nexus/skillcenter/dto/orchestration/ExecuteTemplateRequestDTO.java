package net.ooder.nexus.skillcenter.dto.orchestration;

import java.util.Map;

public class ExecuteTemplateRequestDTO {
    private String templateId;
    private Map<String, Object> parameters;

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
}
