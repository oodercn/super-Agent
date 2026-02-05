package net.ooder.nexus.model;

public class TestCommandResult {
    private String commandId;
    private String status;
    private String output;
    private long executionTime;
    private String error;

    public TestCommandResult() {
    }

    public TestCommandResult(String commandId, String status, String output, long executionTime, String error) {
        this.commandId = commandId;
        this.status = status;
        this.output = output;
        this.executionTime = executionTime;
        this.error = error;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}