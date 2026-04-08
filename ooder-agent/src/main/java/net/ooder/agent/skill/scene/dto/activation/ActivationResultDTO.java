package net.ooder.mvp.skill.scene.dto.activation;

import java.util.List;
import java.util.Map;

public class ActivationResultDTO {
    
    private String processId;
    private String capabilityId;
    private String status;
    private String currentStep;
    private List<StepResultDTO> stepResults;
    private Map<String, Object> metadata;
    
    public ActivationResultDTO() {
    }
    
    public String getProcessId() { return processId; }
    public void setProcessId(String processId) { this.processId = processId; }
    
    public String getCapabilityId() { return capabilityId; }
    public void setCapabilityId(String capabilityId) { this.capabilityId = capabilityId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCurrentStep() { return currentStep; }
    public void setCurrentStep(String currentStep) { this.currentStep = currentStep; }
    
    public List<StepResultDTO> getStepResults() { return stepResults; }
    public void setStepResults(List<StepResultDTO> stepResults) { this.stepResults = stepResults; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public static class StepResultDTO {
        private String stepId;
        private String status;
        private String message;
        private Map<String, Object> result;
        
        public String getStepId() { return stepId; }
        public void setStepId(String stepId) { this.stepId = stepId; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Map<String, Object> getResult() { return result; }
        public void setResult(Map<String, Object> result) { this.result = result; }
    }
}
