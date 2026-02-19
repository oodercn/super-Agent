package net.ooder.sdk.capability.model;

public class StepCondition {
    
    private String expression;
    private String variable;
    private String operator;
    private Object value;
    
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
    
    public String getVariable() { return variable; }
    public void setVariable(String variable) { this.variable = variable; }
    
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
}
