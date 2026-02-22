package net.ooder.skillcenter.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页结果DTO
 * @param <T> 数据类型
 */
public class PageResult<T> {
    
    private List<T> list;
    private long total;
    private int pageNum;
    private int pageSize;
    private int totalPages;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    
    public PageResult() {}
    
    public PageResult(List<T> list, long total, int pageNum, int pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
        this.hasNextPage = pageNum < totalPages;
        this.hasPreviousPage = pageNum > 1;
    }
    
    // Getters and Setters
    public List<T> getList() { return list; }
    public void setList(List<T> list) { this.list = list; }
    
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    
    public int getPageNum() { return pageNum; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public boolean isHasNextPage() { return hasNextPage; }
    public void setHasNextPage(boolean hasNextPage) { this.hasNextPage = hasNextPage; }
    
    public boolean isHasPreviousPage() { return hasPreviousPage; }
    public void setHasPreviousPage(boolean hasPreviousPage) { this.hasPreviousPage = hasPreviousPage; }
    
    /**
     * 创建空分页结果
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(new ArrayList<>(), 0, 1, 10);
    }
    
    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(List<T> list, long total, int pageNum, int pageSize) {
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}
