package net.ooder.mvp.skill.scene.dto.llm;

import jakarta.validation.constraints.NotBlank;

public class SummarizeRequestDTO {
    
    @NotBlank(message = "文本不能为空")
    private String text;
    
    private Integer maxLength;
    
    private String style;
    
    private String model;

    public SummarizeRequestDTO() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
