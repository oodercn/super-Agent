/**
 * ListManager - 通用列表管理器
 * 用于管理分页、排序和加载列表数据
 */
class ListManager {
    constructor(options) {
        this.resource = options.resource || '';
        this.page = options.page || 1;
        this.pageSize = options.pageSize || 10;
        this.sortBy = options.sortBy || 'id';
        this.sortOrder = options.sortOrder || 'asc';
        this.onLoad = options.onLoad || function() {};
        this.onError = options.onError || function() {};
        this.data = [];
        this.total = 0;
    }

    async load() {
        try {
            const response = await fetch(`/api/${this.resource}?page=${this.page}&pageSize=${this.pageSize}&sortBy=${this.sortBy}&sortOrder=${this.sortOrder}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const result = await response.json();
            if (result.success) {
                this.data = result.data || [];
                this.total = result.total || 0;
                this.onLoad(result);
            } else {
                throw new Error(result.message || '加载数据失败');
            }
        } catch (error) {
            console.error('ListManager load error:', error);
            this.onError(error);
        }
    }

    setPage(page) {
        this.page = page;
        return this.load();
    }

    setPageSize(pageSize) {
        this.pageSize = pageSize;
        this.page = 1;
        return this.load();
    }

    setSort(sortBy, sortOrder) {
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
        return this.load();
    }

    getTotalPages() {
        return Math.ceil(this.total / this.pageSize);
    }

    hasNextPage() {
        return this.page < this.getTotalPages();
    }

    hasPrevPage() {
        return this.page > 1;
    }

    nextPage() {
        if (this.hasNextPage()) {
            return this.setPage(this.page + 1);
        }
        return Promise.resolve();
    }

    prevPage() {
        if (this.hasPrevPage()) {
            return this.setPage(this.page - 1);
        }
        return Promise.resolve();
    }
}

// 导出到全局作用域
if (typeof window !== 'undefined') {
    window.ListManager = ListManager;
}
