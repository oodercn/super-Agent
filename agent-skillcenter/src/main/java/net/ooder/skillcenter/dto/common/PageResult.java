package net.ooder.skillcenter.dto.common;

import java.util.Collections;
import java.util.List;

/**
 * 统一分页结果 - 符合v0.7.0协议规范
 */
public class PageResult<T> {
    
    private List<T> items;
    private long total;
    private int page;
    private int size;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public PageResult() {
        this.items = Collections.emptyList();
        this.total = 0;
        this.page = 1;
        this.size = 10;
        this.totalPages = 0;
        this.hasNext = false;
        this.hasPrevious = false;
    }

    public PageResult(List<T> items, long total, int page, int size) {
        this.items = items != null ? items : Collections.emptyList();
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPages = size > 0 ? (int) Math.ceil((double) total / size) : 0;
        this.hasNext = page < totalPages;
        this.hasPrevious = page > 1;
    }

    public static <T> PageResult<T> of(List<T> items, long total, int page, int size) {
        return new PageResult<>(items, total, page, size);
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>();
    }

    public static <T> PageResult<T> of(List<T> items) {
        return new PageResult<>(items, items != null ? items.size() : 0, 1, items != null ? items.size() : 10);
    }

    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public boolean isHasNext() { return hasNext; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }

    public boolean isHasPrevious() { return hasPrevious; }
    public void setHasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
}
