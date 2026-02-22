package net.ooder.nexus.dto.protocol;

import java.io.Serializable;

/**
 * Protocol handler query request DTO
 */
public class ProtocolHandlerQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String commandType;
    private String status;
    private Integer limit;

    public String getCommandType() { return commandType; }
    public void setCommandType(String commandType) { this.commandType = commandType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
}
