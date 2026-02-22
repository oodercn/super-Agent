package net.ooder.nexus.skillcenter.dto.market;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class MarketQueryDTO extends BaseDTO {

    private String category;
    private String keyword;
    private int pageNum = 1;
    private int pageSize = 10;

    public MarketQueryDTO() {}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
