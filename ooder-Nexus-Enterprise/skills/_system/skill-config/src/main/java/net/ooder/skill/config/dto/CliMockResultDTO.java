package net.ooder.skill.config.dto;

import java.util.Map;

public class CliMockResultDTO {
    
    private boolean success;
    private String cliId;
    private String action;
    private Map<String, Object> params;
    private String result;
    private long timestamp;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getCliId() { return cliId; }
    public void setCliId(String cliId) { this.cliId = cliId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Map<String, Object> getParams() { return params; }
    public void setParams(Map<String, Object> params) { this.params = params; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
