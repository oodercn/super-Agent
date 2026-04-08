package net.ooder.spi.core;

import java.util.List;

public class PageResult<T> {

    private List<T> list;
    private int total;
    private int pageNum;
    private int pageSize;

    public PageResult() {}

    public PageResult(List<T> list, int total, int pageNum, int pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public List<T> getList() { return list; }
    public void setList(List<T> list) { this.list = list; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public static <T> PageResult<T> of(List<T> list, int total, int pageNum, int pageSize) {
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(java.util.Collections.emptyList(), 0, 1, 10);
    }
}
