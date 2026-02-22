package net.ooder.nexus.skillcenter.dto.orchestration;

public class ExecutionQueryRequestDTO {
    private String templateId;
    private String status;
    private int pageNum = 1;
    private int pageSize = 10;

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
