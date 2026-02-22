package net.ooder.skillcenter.southbound;

public class ExecutionResult {
    private boolean success;
    private Object output;
    private String errorMessage;
    private long duration;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Object getOutput() { return output; }
    public void setOutput(Object output) { this.output = output; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
}
