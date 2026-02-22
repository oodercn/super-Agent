package net.ooder.nexus.skillcenter.dto.orchestration;

public class ScheduleQueryRequestDTO {
    private int pageNum = 1;
    private int pageSize = 10;

    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}
