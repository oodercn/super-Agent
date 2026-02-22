package net.ooder.nexus.dto.protocol;

import java.io.Serializable;

/**
 * Protocol handler search request DTO
 */
public class ProtocolHandlerSearchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String keyword;
    private String status;
    private Integer limit;

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
}
