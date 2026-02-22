package net.ooder.nexus.skillcenter.dto.orchestration;

public class TemplateQueryRequestDTO {
    private String category;
    private String status;
    private int pageNum = 1;
    private int pageSize = 10;

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
