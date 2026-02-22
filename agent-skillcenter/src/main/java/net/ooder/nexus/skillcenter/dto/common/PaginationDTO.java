package net.ooder.nexus.skillcenter.dto.common;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class PaginationDTO extends BaseDTO {

    private int pageNum = 1;
    private int pageSize = 10;
    private String sortBy;
    private String sortDirection = "desc";

    public PaginationDTO() {}

    public PaginationDTO(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
