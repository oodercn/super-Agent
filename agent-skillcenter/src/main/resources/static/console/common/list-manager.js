/**
 * 列表管理模块
 */

import { showNotification } from './utils.js';
import { ModalManager } from './modal-manager.js';

/**
 * 列表管理类
 */
export class ListManager {
    /**
     * 初始化列表
     * @param {string} listSelector - 列表选择器
     * @param {Object} options - 配置选项
     */
    static init(listSelector, options = {}) {
        const list = document.querySelector(listSelector);
        if (!list) return;

        const { 
            data = [], 
            columns = [], 
            onItemClick, 
            onItemEdit, 
            onItemDelete, 
            onItemView, 
            showActions = true,
            pagination = false,
            pageSize = 10
        } = options;

        // 渲染列表
        ListManager.renderList(list, data, {
            columns,
            onItemClick,
            onItemEdit,
            onItemDelete,
            onItemView,
            showActions,
            pagination,
            pageSize
        });
    }

    /**
     * 渲染列表
     * @param {HTMLElement} list - 列表元素
     * @param {Array} data - 列表数据
     * @param {Object} options - 配置选项
     */
    static renderList(list, data, options = {}) {
        const {
            columns = [],
            onItemClick,
            onItemEdit,
            onItemDelete,
            onItemView,
            showActions = true,
            pagination = false,
            pageSize = 10
        } = options;

        // 清空列表
        list.innerHTML = '';

        // 如果没有数据
        if (data.length === 0) {
            list.innerHTML = `
                <div class="list-empty">
                    <p>暂无数据</p>
                </div>
            `;
            return;
        }

        // 渲染表头
        if (columns.length > 0) {
            const thead = document.createElement('thead');
            const headerRow = document.createElement('tr');
            
            columns.forEach(column => {
                const th = document.createElement('th');
                th.textContent = column.title;
                if (column.width) {
                    th.style.width = column.width;
                }
                headerRow.appendChild(th);
            });
            
            if (showActions) {
                const th = document.createElement('th');
                th.textContent = '操作';
                th.style.width = '150px';
                th.style.textAlign = 'center';
                headerRow.appendChild(th);
            }
            
            thead.appendChild(headerRow);
            list.appendChild(thead);
        }

        // 渲染表体
        const tbody = document.createElement('tbody');
        
        data.forEach((item, index) => {
            const row = document.createElement('tr');
            row.className = index % 2 === 0 ? 'even' : 'odd';
            
            // 渲染列
            columns.forEach(column => {
                const td = document.createElement('td');
                
                if (column.render) {
                    // 自定义渲染
                    td.innerHTML = column.render(item, index);
                } else if (column.field) {
                    // 字段渲染
                    td.textContent = item[column.field] || '';
                }
                
                if (column.align) {
                    td.style.textAlign = column.align;
                }
                
                row.appendChild(td);
            });
            
            // 渲染操作按钮
            if (showActions) {
                const td = document.createElement('td');
                td.style.textAlign = 'center';
                
                const actionsHTML = `
                    <div class="list-actions">
                        ${onItemView ? `<button class="action-view" data-index="${index}">查看</button>` : ''}
                        ${onItemEdit ? `<button class="action-edit" data-index="${index}">编辑</button>` : ''}
                        ${onItemDelete ? `<button class="action-delete" data-index="${index}">删除</button>` : ''}
                    </div>
                `;
                
                td.innerHTML = actionsHTML;
                row.appendChild(td);
            }
            
            // 添加点击事件
            if (onItemClick) {
                row.style.cursor = 'pointer';
                row.addEventListener('click', (e) => {
                    // 避免触发按钮事件时也触发行点击
                    if (!e.target.closest('.list-actions')) {
                        onItemClick(item, index);
                    }
                });
            }
            
            tbody.appendChild(row);
        });
        
        list.appendChild(tbody);

        // 添加事件监听器
        if (showActions) {
            // 查看按钮事件
            if (onItemView) {
                const viewButtons = list.querySelectorAll('.action-view');
                viewButtons.forEach(button => {
                    button.addEventListener('click', (e) => {
                        e.stopPropagation();
                        const index = parseInt(button.getAttribute('data-index'));
                        onItemView(data[index], index);
                    });
                });
            }
            
            // 编辑按钮事件
            if (onItemEdit) {
                const editButtons = list.querySelectorAll('.action-edit');
                editButtons.forEach(button => {
                    button.addEventListener('click', (e) => {
                        e.stopPropagation();
                        const index = parseInt(button.getAttribute('data-index'));
                        onItemEdit(data[index], index);
                    });
                });
            }
            
            // 删除按钮事件
            if (onItemDelete) {
                const deleteButtons = list.querySelectorAll('.action-delete');
                deleteButtons.forEach(button => {
                    button.addEventListener('click', (e) => {
                        e.stopPropagation();
                        const index = parseInt(button.getAttribute('data-index'));
                        const item = data[index];
                        
                        // 显示确认对话框
                        ModalManager.confirm({
                            title: '确认删除',
                            content: `确定要删除 "${item.name || item.title || '该项'}" 吗？`,
                            onConfirm: () => {
                                onItemDelete(item, index);
                            }
                        });
                    });
                });
            }
        }

        // 渲染分页
        if (pagination && data.length > pageSize) {
            ListManager.renderPagination(list, data.length, pageSize, options.onPageChange);
        }
    }

    /**
     * 渲染分页
     * @param {HTMLElement} list - 列表元素
     * @param {number} total - 总数据量
     * @param {number} pageSize - 每页大小
     * @param {Function} onPageChange - 页码变化回调
     */
    static renderPagination(list, total, pageSize, onPageChange) {
        const totalPages = Math.ceil(total / pageSize);
        if (totalPages <= 1) return;

        const pagination = document.createElement('div');
        pagination.className = 'list-pagination';

        let paginationHTML = `
            <div class="pagination-info">
                共 ${total} 条记录，每页 ${pageSize} 条，共 ${totalPages} 页
            </div>
            <div class="pagination-buttons">
                <button class="page-btn page-prev" disabled>&laquo;</button>
        `;

        // 渲染页码按钮
        for (let i = 1; i <= totalPages; i++) {
            paginationHTML += `
                <button class="page-btn ${i === 1 ? 'active' : ''}" data-page="${i}">${i}</button>
            `;
        }

        paginationHTML += `
                <button class="page-btn page-next">»</button>
            </div>
        `;

        pagination.innerHTML = paginationHTML;
        list.parentNode.appendChild(pagination);

        // 添加分页事件
        const pageButtons = pagination.querySelectorAll('.page-btn');
        let currentPage = 1;

        pageButtons.forEach(button => {
            button.addEventListener('click', () => {
                if (button.classList.contains('page-prev')) {
                    // 上一页
                    if (currentPage > 1) {
                        currentPage--;
                        ListManager.updatePagination(pagination, currentPage, totalPages);
                        if (onPageChange) onPageChange(currentPage);
                    }
                } else if (button.classList.contains('page-next')) {
                    // 下一页
                    if (currentPage < totalPages) {
                        currentPage++;
                        ListManager.updatePagination(pagination, currentPage, totalPages);
                        if (onPageChange) onPageChange(currentPage);
                    }
                } else {
                    // 页码按钮
                    const page = parseInt(button.getAttribute('data-page'));
                    currentPage = page;
                    ListManager.updatePagination(pagination, currentPage, totalPages);
                    if (onPageChange) onPageChange(currentPage);
                }
            });
        });
    }

    /**
     * 更新分页状态
     * @param {HTMLElement} pagination - 分页元素
     * @param {number} currentPage - 当前页码
     * @param {number} totalPages - 总页数
     */
    static updatePagination(pagination, currentPage, totalPages) {
        const pageButtons = pagination.querySelectorAll('.page-btn');
        const prevBtn = pagination.querySelector('.page-prev');
        const nextBtn = pagination.querySelector('.page-next');

        // 更新上一页/下一页按钮状态
        prevBtn.disabled = currentPage === 1;
        nextBtn.disabled = currentPage === totalPages;

        // 更新页码按钮状态
        pageButtons.forEach(button => {
            if (!button.classList.contains('page-prev') && !button.classList.contains('page-next')) {
                const page = parseInt(button.getAttribute('data-page'));
                if (page === currentPage) {
                    button.classList.add('active');
                } else {
                    button.classList.remove('active');
                }
            }
        });
    }

    /**
     * 刷新列表
     * @param {string} listSelector - 列表选择器
     * @param {Array} data - 新数据
     * @param {Object} options - 配置选项
     */
    static refreshList(listSelector, data, options = {}) {
        const list = document.querySelector(listSelector);
        if (!list) return;

        ListManager.renderList(list, data, options);
    }

    /**
     * 添加列表项
     * @param {string} listSelector - 列表选择器
     * @param {Object} item - 新项
     * @param {Object} options - 配置选项
     */
    static addItem(listSelector, item, options = {}) {
        const list = document.querySelector(listSelector);
        if (!list) return;

        // 获取当前数据
        const currentData = options.data || [];
        const newData = [...currentData, item];

        // 刷新列表
        ListManager.refreshList(listSelector, newData, { ...options, data: newData });

        showNotification('添加成功', 'success');
    }

    /**
     * 删除列表项
     * @param {string} listSelector - 列表选择器
     * @param {number} index - 索引
     * @param {Object} options - 配置选项
     */
    static removeItem(listSelector, index, options = {}) {
        const list = document.querySelector(listSelector);
        if (!list) return;

        // 获取当前数据
        const currentData = options.data || [];
        const newData = currentData.filter((_, i) => i !== index);

        // 刷新列表
        ListManager.refreshList(listSelector, newData, { ...options, data: newData });

        showNotification('删除成功', 'success');
    }

    /**
     * 更新列表项
     * @param {string} listSelector - 列表选择器
     * @param {number} index - 索引
     * @param {Object} item - 更新项
     * @param {Object} options - 配置选项
     */
    static updateItem(listSelector, index, item, options = {}) {
        const list = document.querySelector(listSelector);
        if (!list) return;

        // 获取当前数据
        const currentData = options.data || [];
        const newData = [...currentData];
        newData[index] = { ...newData[index], ...item };

        // 刷新列表
        ListManager.refreshList(listSelector, newData, { ...options, data: newData });

        showNotification('更新成功', 'success');
    }
}