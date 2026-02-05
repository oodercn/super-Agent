package net.ooder.sdk.system.compile;

public class ExecutionResult {
    private boolean success;
    private String language;
    private String output;
    private String errorOutput;
    private int exitCode;
    private long executionTimeMs;

    public ExecutionResult() {
    }

    public ExecutionResult(boolean success, String language, String output, 
                         String errorOutput, int exitCode, long executionTimeMs) {
        this.success = success;
        this.language = language;
        this.output = output;
        this.errorOutput = errorOutput;
        this.exitCode = exitCode;
        this.executionTimeMs = executionTimeMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public void setErrorOutput(String errorOutput) {
        this.errorOutput = errorOutput;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
}