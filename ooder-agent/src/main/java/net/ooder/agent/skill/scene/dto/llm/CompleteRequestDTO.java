package net.ooder.mvp.skill.scene.dto.llm;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class CompleteRequestDTO {
    
    @NotBlank(message = "提示词不能为空")
    private String prompt;
    
    private String model;
    
    private Integer maxTokens;
    
    private Double temperature;
    
    private Double topP;
    
    private List<String> stop;

    public CompleteRequestDTO() {}

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public List<String> getStop() {
        return stop;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }
}
