package net.ooder.nexus.skillcenter.dto.common;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class SearchDTO extends BaseDTO {

    private String keyword;
    private int pageNum = 1;
    private int pageSize = 10;

    public SearchDTO() {}

    public SearchDTO(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
}
