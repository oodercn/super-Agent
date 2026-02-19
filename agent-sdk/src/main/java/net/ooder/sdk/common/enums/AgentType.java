
package net.ooder.sdk.common.enums;

public enum AgentType {
    MCP("mcp", "MCP Agent"),
    ROUTE("route", "Route Agent"),
    END("end", "End Agent");
    
    private final String code;
    private final String description;
    
    AgentType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static AgentType fromCode(String code) {
        for (AgentType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown agent type: " + code);
    }
}
