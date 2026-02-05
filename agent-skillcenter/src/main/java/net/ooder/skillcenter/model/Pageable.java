package net.ooder.skillcenter.model;

/**
 * 分页请求参数
 */
public class Pageable {
    
    private int page = 1; // 默认页码为1
    private int size = 10; // 默认每页大小为10
    private String sortBy; // 排序字段
    private String sortDirection = "asc"; // 排序方向，默认升序

    // Getters and Setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page > 0 ? page : 1;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size > 0 ? size : 10;
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
        this.sortDirection = "desc".equalsIgnoreCase(sortDirection) ? "desc" : "asc";
    }

    /**
     * 获取偏移量
     * @return 偏移量
     */
    public int getOffset() {
        return (page - 1) * size;
    }

    /**
     * 获取限制数量
     * @return 限制数量
     */
    public int getLimit() {
        return size;
    }
}
