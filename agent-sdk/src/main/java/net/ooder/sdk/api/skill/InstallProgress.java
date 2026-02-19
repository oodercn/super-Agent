package net.ooder.sdk.api.skill;

import java.util.ArrayList;
import java.util.List;

public class InstallProgress {
    
    private String installId;
    private String skillId;
    private InstallState state;
    private int progress;
    private String currentStep;
    private long startTime;
    private long estimatedEndTime;
    private List<String> completedSteps = new ArrayList<String>();
    private List<String> pendingSteps = new ArrayList<String>();
    private String errorMessage;
    private Throwable error;
    
    public enum InstallState {
        PENDING,
        DOWNLOADING,
        EXTRACTING,
        INSTALLING,
        CONFIGURING,
        COMPLETED,
        FAILED,
        CANCELLED
    }
    
    public InstallProgress() {
        this.state = InstallState.PENDING;
        this.progress = 0;
        this.startTime = System.currentTimeMillis();
    }
    
    public InstallProgress(String installId, String skillId) {
        this();
        this.installId = installId;
        this.skillId = skillId;
    }
    
    public String getInstallId() { return installId; }
    public void setInstallId(String installId) { this.installId = installId; }
    
    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }
    
    public InstallState getState() { return state; }
    public void setState(InstallState state) { this.state = state; }
    
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
    
    public String getCurrentStep() { return currentStep; }
    public void setCurrentStep(String currentStep) { this.currentStep = currentStep; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEstimatedEndTime() { return estimatedEndTime; }
    public void setEstimatedEndTime(long estimatedEndTime) { this.estimatedEndTime = estimatedEndTime; }
    
    public List<String> getCompletedSteps() { return completedSteps; }
    public void setCompletedSteps(List<String> completedSteps) { this.completedSteps = completedSteps; }
    
    public List<String> getPendingSteps() { return pendingSteps; }
    public void setPendingSteps(List<String> pendingSteps) { this.pendingSteps = pendingSteps; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public Throwable getError() { return error; }
    public void setError(Throwable error) { this.error = error; }
    
    public boolean isCompleted() { return state == InstallState.COMPLETED; }
    
    public boolean isFailed() { return state == InstallState.FAILED; }
    
    public boolean isCancelled() { return state == InstallState.CANCELLED; }
    
    public boolean isInProgress() {
        return state != InstallState.COMPLETED && 
               state != InstallState.FAILED && 
               state != InstallState.CANCELLED;
    }
    
    public void addCompletedStep(String step) {
        completedSteps.add(step);
        if (pendingSteps.contains(step)) {
            pendingSteps.remove(step);
        }
    }
    
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    public double getProgressRate() {
        long elapsed = getElapsedTime();
        if (elapsed > 0 && progress > 0) {
            return (double) progress / elapsed * 1000;
        }
        return 0.0;
    }
}
