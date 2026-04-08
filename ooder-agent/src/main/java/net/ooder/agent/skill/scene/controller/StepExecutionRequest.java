package net.ooder.mvp.skill.scene.controller;

import java.util.Map;

public class StepExecutionRequest {
    private Map<String, Object> data;

    public StepExecutionRequest() {}

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
