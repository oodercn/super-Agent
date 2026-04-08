package net.ooder.mvp.skill.scene.dto.llm;

import java.util.Map;

public class LlmConfigAuditDTO {
    
    public static final String OP_CREATE = "CREATE";
    public static final String OP_UPDATE = "UPDATE";
    public static final String OP_DELETE = "DELETE";
    public static final String OP_ENABLE = "ENABLE";
    public static final String OP_DISABLE = "DISABLE";
    
    private Long id;
    private String configId;
    private String configName;
    private String operation;
    private String operator;
    private String operatorIp;
    private Map<String, Object> beforeValue;
    private Map<String, Object> afterValue;
    private long operateTime;
    private String detail;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getConfigId() { return configId; }
    public void setConfigId(String configId) { this.configId = configId; }
    
    public String getConfigName() { return configName; }
    public void setConfigName(String configName) { this.configName = configName; }
    
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    
    public String getOperatorIp() { return operatorIp; }
    public void setOperatorIp(String operatorIp) { this.operatorIp = operatorIp; }
    
    public Map<String, Object> getBeforeValue() { return beforeValue; }
    public void setBeforeValue(Map<String, Object> beforeValue) { this.beforeValue = beforeValue; }
    
    public Map<String, Object> getAfterValue() { return afterValue; }
    public void setAfterValue(Map<String, Object> afterValue) { this.afterValue = afterValue; }
    
    public long getOperateTime() { return operateTime; }
    public void setOperateTime(long operateTime) { this.operateTime = operateTime; }
    
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
}
