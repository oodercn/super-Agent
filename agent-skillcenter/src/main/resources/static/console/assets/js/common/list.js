/**
 * 列表组件 - 封装表格列表功能
 */

class ListComponent {
    /**
     * 构造函数
     * @param {string} tableId - 表格ID
     * @param {Object} options - 配置选项
     */
    constructor(tableId, options = {}) {
        this.tableId = tableId;
        this.options = {
            columns: [],
            data: [],
            pagination: {
                enabled: true,
                page: 1,
                size: 10,
                totalElements: 0,
                totalPages: 0
            },
            sorting: {
                enabled: true,
                sortBy: null,
                sortDirection: 'asc'
            },
            onRowClick: null,
            onAction: null,
            ...options
        };
        
        this.table = document.getElementById(tableId);
        this.tbody = this.table ? this.table.querySelector('tbody') : null;
        this.paginationContainer = options.paginationContainer;
    }

    /**
     * 渲染列表
     * @param {Array} data - 数据数组
     * @param {Object} pagination - 分页信息
     */
    render(data = this.options.data, pagination = this.options.pagination) {
        if (!this.tbody) {
            console.error('Table body not found');
            return;
        }
        
        this.options.data = data;
        this.options.pagination = pagination;
        
        // 清空表格
        this.tbody.innerHTML = '';
        
        // 渲染数据
        if (data.length === 0) {
            const emptyRow = document.createElement('tr');
            emptyRow.innerHTML = `<td colspan="${this.options.columns.length}" style="text-align: center; padding: 24px; color: var(--ooder-secondary);">暂无数据</td>`;
            this.tbody.appendChild(emptyRow);
        } else {
            data.forEach((item, index) => {
                const row = this.createRow(item, index);
                this.tbody.appendChild(row);
            });
        }
        
        // 渲染分页
        if (this.options.pagination.enabled && this.paginationContainer) {
            this.renderPagination();
        }
    }

    /**
     * 创建表格行
     * @param {Object} item - 数据项
     * @param {number} index - 索引
     * @returns {HTMLTableRowElement} - 表格行元素
     */
    createRow(item, index) {
        const row = document.createElement('tr');
        
        // 添加点击事件
        if (this.options.onRowClick) {
            row.style.cursor = 'pointer';
            row.addEventListener('click', () => {
                this.options.onRowClick(item, index);
            });
        }
        
        // 渲染列
        this.options.columns.forEach(column => {
            const cell = document.createElement('td');
            
            if (column.render) {
                // 使用自定义渲染函数
                cell.innerHTML = column.render(item, index);
            } else if (column.field) {
                // 使用字段值
                const value = this.getValueFromPath(item, column.field);
                cell.textContent = value !== undefined && value !== null ? value : '';
            }
            
            // 添加列样式
            if (column.style) {
                Object.assign(cell.style, column.style);
            }
            
            row.appendChild(cell);
        });
        
        return row;
    }

    /**
     * 从对象路径获取值
     * @param {Object} obj - 对象
     * @param {string} path - 路径
     * @returns {any} - 值
     */
    getValueFromPath(obj, path) {
        return path.split('.').reduce((acc, key) => {
            return acc && acc[key] !== undefined ? acc[key] : undefined;
        }, obj);
    }

    /**
     * 渲染分页
     */
    renderPagination() {
        if (!this.paginationContainer) {
            return;
        }
        
        const { page, size, totalElements, totalPages } = this.options.pagination;
        const container = document.getElementById(this.paginationContainer);
        
        if (!container) {
            return;
        }
        
        // 清空容器
        container.innerHTML = '';
        
        // 创建分页控件
        const pagination = document.createElement('div');
        pagination.className = 'pagination';
        pagination.style.display = 'flex';
        pagination.style.justifyContent = 'space-between';
        pagination.style.alignItems = 'center';
        pagination.style.padding = '16px';
        pagination.style.backgroundColor = '#121212';
        pagination.style.borderTop = '1px solid var(--ooder-border)';
        pagination.style.borderRadius = '0 0 var(--ooder-radius) var(--ooder-radius)';
        
        // 左侧信息
        const info = document.createElement('div');
        info.style.color = 'var(--ooder-secondary)';
        info.style.fontSize = '14px';
        info.textContent = `显示 ${(page - 1) * size + 1} 到 ${Math.min(page * size, totalElements)} 共 ${totalElements} 条记录`;
        pagination.appendChild(info);
        
        // 右侧分页按钮
        const buttons = document.createElement('div');
        buttons.style.display = 'flex';
        buttons.style.gap = '8px';
        
        // 上一页按钮
        const prevButton = document.createElement('button');
        prevButton.className = 'btn btn-secondary';
        prevButton.style.padding = '6px 12px';
        prevButton.style.fontSize = '14px';
        prevButton.textContent = '上一页';
        prevButton.disabled = page === 1;
        prevButton.addEventListener('click', () => {
            if (page > 1) {
                this.options.onPageChange && this.options.onPageChange(page - 1, size);
            }
        });
        buttons.appendChild(prevButton);
        
        // 页码按钮
        const startPage = Math.max(1, page - 2);
        const endPage = Math.min(totalPages, startPage + 4);
        
        for (let i = startPage; i <= endPage; i++) {
            const pageButton = document.createElement('button');
            pageButton.className = 'btn btn-secondary';
            pageButton.style.padding = '6px 12px';
            pageButton.style.fontSize = '14px';
            pageButton.textContent = i;
            
            if (i === page) {
                pageButton.style.backgroundColor = 'var(--ooder-primary)';
                pageButton.style.color = 'white';
            }
            
            pageButton.addEventListener('click', () => {
                this.options.onPageChange && this.options.onPageChange(i, size);
            });
            
            buttons.appendChild(pageButton);
        }
        
        // 下一页按钮
        const nextButton = document.createElement('button');
        nextButton.className = 'btn btn-secondary';
        nextButton.style.padding = '6px 12px';
        nextButton.style.fontSize = '14px';
        nextButton.textContent = '下一页';
        nextButton.disabled = page === totalPages;
        nextButton.addEventListener('click', () => {
            if (page < totalPages) {
                this.options.onPageChange && this.options.onPageChange(page + 1, size);
            }
        });
        buttons.appendChild(nextButton);
        
        // 每页大小选择
        const sizeSelector = document.createElement('div');
        sizeSelector.style.display = 'flex';
        sizeSelector.style.alignItems = 'center';
        sizeSelector.style.marginLeft = '16px';
        sizeSelector.style.gap = '8px';
        
        const sizeLabel = document.createElement('span');
        sizeLabel.style.color = 'var(--ooder-secondary)';
        sizeLabel.style.fontSize = '14px';
        sizeLabel.textContent = '每页显示:';
        sizeSelector.appendChild(sizeLabel);
        
        const sizeSelect = document.createElement('select');
        sizeSelect.style.padding = '6px 8px';
        sizeSelect.style.border = '1px solid var(--ooder-border)';
        sizeSelect.style.borderRadius = 'var(--ooder-radius)';
        sizeSelect.style.backgroundColor = '#1a1a1a';
        sizeSelect.style.color = 'var(--ooder-dark)';
        sizeSelect.style.fontSize = '14px';
        
        const sizes = [10, 20, 50, 100];
        sizes.forEach(s => {
            const option = document.createElement('option');
            option.value = s;
            option.textContent = s;
            if (s === size) {
                option.selected = true;
            }
            sizeSelect.appendChild(option);
        });
        
        sizeSelect.addEventListener('change', () => {
            const newSize = parseInt(sizeSelect.value);
            this.options.onPageChange && this.options.onPageChange(1, newSize);
        });
        
        sizeSelector.appendChild(sizeSelect);
        buttons.appendChild(sizeSelector);
        pagination.appendChild(buttons);
        container.appendChild(pagination);
    }

    /**
     * 排序
     * @param {string} sortBy - 排序字段
     * @param {string} sortDirection - 排序方向
     */
    sort(sortBy, sortDirection = 'asc') {
        this.options.sorting.sortBy = sortBy;
        this.options.sorting.sortDirection = sortDirection;
        this.options.onSort && this.options.onSort(sortBy, sortDirection);
    }

    /**
     * 获取当前分页信息
     * @returns {Object} - 分页信息
     */
    getPagination() {
        return this.options.pagination;
    }

    /**
     * 获取当前排序信息
     * @returns {Object} - 排序信息
     */
    getSorting() {
        return this.options.sorting;
    }

    /**
     * 更新数据
     * @param {Array} data - 新数据
     */
    updateData(data) {
        this.options.data = data;
        this.render();
    }

    /**
     * 更新分页信息
     * @param {Object} pagination - 新分页信息
     */
    updatePagination(pagination) {
        this.options.pagination = { ...this.options.pagination, ...pagination };
        this.render();
    }
}

// 导出列表组件
export { ListComponent };
