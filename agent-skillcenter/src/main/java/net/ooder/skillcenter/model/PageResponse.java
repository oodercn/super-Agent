package net.ooder.skillcenter.model;

import java.util.List;

/**
 * 分页响应模型 - 符合 ooderNexus 规范
 * @param <T> 数据类型
 */
public class PageResponse<T> {
    
    private List<T> content; // 数据列表
    private int page; // 当前页码
    private int size; // 每页大小
    private long totalElements; // 总元素数
    private int totalPages; // 总页数
    private boolean hasNext; // 是否有下一页
    private boolean hasPrevious; // 是否有上一页
    private int firstPage; // 第一页页码
    private int lastPage; // 最后页页码
    private int nextPage; // 下一页页码
    private int previousPage; // 上一页页码

    // 构造方法
    public PageResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.hasNext = page < totalPages;
        this.hasPrevious = page > 1;
        this.firstPage = 1;
        this.lastPage = totalPages > 0 ? totalPages : 1;
        this.nextPage = hasNext ? page + 1 : page;
        this.previousPage = hasPrevious ? page - 1 : page;
    }

    // Getters and Setters
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        this.hasNext = page < totalPages;
        this.hasPrevious = page > 1;
        this.nextPage = hasNext ? page + 1 : page;
        this.previousPage = hasPrevious ? page - 1 : page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.hasNext = page < totalPages;
        this.lastPage = totalPages > 0 ? totalPages : 1;
        this.nextPage = hasNext ? page + 1 : page;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.hasNext = page < totalPages;
        this.lastPage = totalPages > 0 ? totalPages : 1;
        this.nextPage = hasNext ? page + 1 : page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        this.hasNext = page < totalPages;
        this.lastPage = totalPages > 0 ? totalPages : 1;
        this.nextPage = hasNext ? page + 1 : page;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
        this.nextPage = hasNext ? page + 1 : page;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
        this.previousPage = hasPrevious ? page - 1 : page;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    /**
     * 静态构建方法
     * @param content 数据列表
     * @param page 当前页码
     * @param size 每页大小
     * @param totalElements 总元素数
     * @param <T> 数据类型
     * @return 分页响应对象
     */
    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
        return new PageResponse<>(content, page, size, totalElements);
    }

    /**
     * 空分页响应
     * @param <T> 数据类型
     * @return 空分页响应对象
     */
    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(new java.util.ArrayList<>(), 1, 10, 0);
    }

    /**
     * 空分页响应（指定每页大小）
     * @param size 每页大小
     * @param <T> 数据类型
     * @return 空分页响应对象
     */
    public static <T> PageResponse<T> empty(int size) {
        return new PageResponse<>(new java.util.ArrayList<>(), 1, size, 0);
    }
}
