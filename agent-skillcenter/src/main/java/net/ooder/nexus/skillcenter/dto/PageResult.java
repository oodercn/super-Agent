package net.ooder.nexus.skillcenter.dto;

import java.util.List;

/**
 * 分页结果封装类 - 符合 ooderNexus 规范
 * @param <T> 数据类型
 */
public class PageResult<T> {

    private List<T> list; // 数据列表
    private int total; // 总记录数
    private int pageNum; // 当前页码
    private int pageSize; // 每页大小
    private int pages; // 总页数

    // 构造方法
    public PageResult() {}

    public PageResult(List<T> list, int total, int pageNum, int pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pages = calculatePages(total, pageSize);
    }

    // 计算总页数
    private int calculatePages(int total, int pageSize) {
        if (pageSize <= 0) {
            return 0;
        }
        return (total + pageSize - 1) / pageSize;
    }

    // Getters and Setters
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        this.pages = calculatePages(total, pageSize);
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
        this.pages = calculatePages(total, pageSize);
    }

    public int getPages() {
        return pages;
    }
}