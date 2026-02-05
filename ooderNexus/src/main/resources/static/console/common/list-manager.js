/**
 * 列表管理器
 * 抽象列表管理功能，包括分页、排序、过滤等
 */
function ListManager(options = {}) {
    this.resource = options.resource || 'agents';
    this.page = options.page || 1;
    this.pageSize = options.pageSize || 10;
    this.sortBy = options.sortBy || 'id';
    this.sortOrder = options.sortOrder || 'asc';
    this.filters = options.filters || {};
    this.data = [];
    this.pagination = {
        page: this.page,
        pageSize: this.pageSize,
        total: 0,
        totalPages: 0
    };
    this.loading = false;
    this.error = null;
    this.callbacks = {
        onLoad: options.onLoad || null,
        onError: options.onError || null,
        onUpdate: options.onUpdate || null
    };
}

/**
 * 加载数据
 * @param {Object} options - 加载选项
 * @returns {Promise<any>} - 响应数据
 */
ListManager.prototype.load = async function(options = {}) {
    this.loading = true;
    this.error = null;

    try {
        const params = {
            resource: options.resource || this.resource,
            page: options.page || this.page,
            pageSize: options.pageSize || this.pageSize,
            sortBy: options.sortBy || this.sortBy,
            sortOrder: options.sortOrder || this.sortOrder,
            filters: options.filters || this.filters
        };

        const response = await apiClient.batchFetch(params);

        if (response.status === 'success') {
            this.data = response.data || [];
            this.pagination = response.pagination || this.pagination;
            this.page = this.pagination.page;
            this.pageSize = this.pagination.pageSize;

            if (this.callbacks.onLoad) {
                this.callbacks.onLoad(response);
            }
            if (this.callbacks.onUpdate) {
                this.callbacks.onUpdate(this);
            }

            return response;
        } else {
            throw new Error(response.message || '加载失败');
        }
    } catch (error) {
        this.error = error;
        if (this.callbacks.onError) {
            this.callbacks.onError(error);
        }
        throw error;
    } finally {
        this.loading = false;
    }
};

/**
 * 分页
 * @param {number} page - 页码
 * @returns {Promise<any>} - 响应数据
 */
ListManager.prototype.paginate = async function(page) {
    return this.load({ page });
};

/**
 * 更改每页大小
 * @param {number} pageSize - 每页大小
 * @returns {Promise<any>} - 响应数据
 */
ListManager.prototype.setPageSize = async function(pageSize) {
    return this.load({ pageSize, page: 1 }); // 重置到第一页
};

/**
 * 排序
 * @param {string} sortBy - 排序字段
 * @param {string} sortOrder - 排序顺序
 * @returns {Promise<any>} - 响应数据
 */
ListManager.prototype.sort = async function(sortBy, sortOrder = 'asc') {
    return this.load({ sortBy, sortOrder });
};

/**
 * 过滤
 * @param {Object} filters - 过滤条件
 * @returns {Promise<any>} - 响应数据
 */
ListManager.prototype.filter = async function(filters) {
    return this.load({ filters, page: 1 }); // 重置到第一页
};

/**
 * 重置
 * @returns {Promise<any>} - 响应数据
 */
ListManager.prototype.reset = async function() {
    this.page = 1;
    this.pageSize = 10;
    this.sortBy = 'id';
    this.sortOrder = 'asc';
    this.filters = {};
    return this.load();
};

/**
 * 获取当前页面数据
 * @returns {Array} - 当前页面数据
 */
ListManager.prototype.getCurrentPageData = function() {
    return this.data;
};

/**
 * 获取分页信息
 * @returns {Object} - 分页信息
 */
ListManager.prototype.getPagination = function() {
    return this.pagination;
};

/**
 * 是否正在加载
 * @returns {boolean} - 加载状态
 */
ListManager.prototype.isLoading = function() {
    return this.loading;
};

/**
 * 是否有错误
 * @returns {Error|null} - 错误信息
 */
ListManager.prototype.getError = function() {
    return this.error;
};

/**
 * 设置回调函数
 * @param {Object} callbacks - 回调函数
 */
ListManager.prototype.setCallbacks = function(callbacks) {
    this.callbacks = {
        ...this.callbacks,
        ...callbacks
    };
};

/**
 * 更新资源类型
 * @param {string} resource - 资源类型
 * @returns {Promise<any>} - 响应数据
 */
ListManager.prototype.setResource = async function(resource) {
    this.resource = resource;
    return this.load({ resource, page: 1 }); // 重置到第一页
};

// 全局导出
if (typeof window !== 'undefined') {
    window.ListManager = ListManager;
}
