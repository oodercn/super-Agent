/**
 * 统一API服务层
 * 封装所有后端API调用，提供统一的错误处理和认证管理
 */

class ApiService {
    constructor() {
        this.baseUrl = '/api/v1';
        this.defaultHeaders = {
            'Content-Type': 'application/json'
        };
    }

    // ==================== 通用请求方法 ====================

    async request(url, options = {}) {
        const fullUrl = url.startsWith('http') ? url : `${this.baseUrl}${url}`;
        
        const config = {
            ...options,
            headers: {
                ...this.defaultHeaders,
                ...options.headers
            }
        };

        try {
            const response = await fetch(fullUrl, config);
            
            // 处理HTTP错误
            if (!response.ok) {
                if (response.status === 401 || response.status === 403) {
                    // 认证失败，跳转登录页
                    if (!window.location.pathname.includes('login.html')) {
                        window.location.href = '/console/pages/login.html';
                    }
                    throw new Error('会话已过期，请重新登录');
                }
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }

            const result = await response.json();
            
            // 处理业务错误
            if (result.status !== 'success') {
                throw new Error(result.message || '请求失败');
            }

            return result.data;
        } catch (error) {
            console.error('[ApiService] 请求失败:', error);
            throw error;
        }
    }

    async get(url, params = {}) {
        const queryString = Object.keys(params)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
        const fullUrl = queryString ? `${url}?${queryString}` : url;
        
        return this.request(fullUrl, { method: 'GET' });
    }

    async post(url, data = {}) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    async postForm(url, params = {}) {
        const formData = new URLSearchParams();
        Object.keys(params).forEach(key => {
            formData.append(key, params[key]);
        });

        return this.request(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData.toString()
        });
    }

    // ==================== 工作台API ====================

    /**
     * 获取工作台数据
     */
    async getWorkbenchData(userId) {
        return this.get('/workbench/data', { userId });
    }

    /**
     * 获取场景-待办分组
     */
    async getSceneTodoGroups(userId, status = '') {
        const params = { userId };
        if (status) params.status = status;
        return this.get('/workbench/scene-todos', params);
    }

    /**
     * 获取用户待办统计
     */
    async getTodoStatistics(userId) {
        return this.get('/workbench/statistics', { userId });
    }

    /**
     * 处理待办
     */
    async processTodo(userId, todoId, action) {
        return this.postForm('/workbench/process-todo', { userId, todoId, action });
    }

    /**
     * 完成待办并触发回调
     */
    async completeTodoWithCallback(userId, todoId) {
        return this.postForm('/workbench/complete-todo', { userId, todoId });
    }

    /**
     * 批量处理场景待办
     */
    async batchProcessSceneTodos(userId, sceneGroupId, action) {
        return this.postForm('/workbench/batch-process', { userId, sceneGroupId, action });
    }

    // ==================== 场景API ====================

    /**
     * 获取场景列表
     */
    async getSceneList(userId, page = 1, size = 20) {
        return this.get('/scene-groups', { userId, page, size });
    }

    /**
     * 获取场景详情
     */
    async getSceneDetail(sceneGroupId) {
        return this.get(`/scene-groups/${sceneGroupId}`);
    }

    /**
     * 获取场景待办
     */
    async getSceneTodos(sceneGroupId) {
        return this.get(`/workbench/scene/${sceneGroupId}/todos`);
    }

    /**
     * 获取用户在某场景的待办
     */
    async getMyTodosInScene(userId, sceneGroupId) {
        return this.get(`/workbench/user/${userId}/scene/${sceneGroupId}/todos`);
    }

    /**
     * 获取场景待办统计
     */
    async getSceneTodoStatistics(sceneGroupId) {
        return this.get(`/workbench/scene/${sceneGroupId}/statistics`);
    }

    /**
     * 检查场景是否有待处理待办
     */
    async hasPendingTodos(sceneGroupId, userId = '') {
        const params = {};
        if (userId) params.userId = userId;
        return this.get(`/workbench/scene/${sceneGroupId}/has-pending`, params);
    }

    /**
     * 获取场景下一步操作提示
     */
    async getNextActionHint(sceneGroupId, userId) {
        return this.get(`/workbench/scene/${sceneGroupId}/next-action`, { userId });
    }

    // ==================== 待办API ====================

    /**
     * 获取用户待办列表
     */
    async getTodoList(userId, status = '', page = 1, size = 20) {
        const params = { userId, page, size };
        if (status) params.status = status;
        return this.get('/todos', params);
    }

    /**
     * 获取待办详情
     */
    async getTodoDetail(todoId) {
        return this.get(`/todos/${todoId}`);
    }

    /**
     * 创建待办
     */
    async createTodo(todoData) {
        return this.post('/todos', todoData);
    }

    /**
     * 更新待办
     */
    async updateTodo(todoId, todoData) {
        return this.request(`/todos/${todoId}`, {
            method: 'PUT',
            body: JSON.stringify(todoData)
        });
    }

    /**
     * 删除待办
     */
    async deleteTodo(todoId) {
        return this.request(`/todos/${todoId}`, { method: 'DELETE' });
    }

    /**
     * 接受待办
     */
    async acceptTodo(userId, todoId) {
        return this.postForm('/todos/accept', { userId, todoId });
    }

    /**
     * 拒绝待办
     */
    async rejectTodo(userId, todoId) {
        return this.postForm('/todos/reject', { userId, todoId });
    }

    /**
     * 审批待办
     */
    async approveTodo(userId, todoId) {
        return this.postForm('/todos/approve', { userId, todoId });
    }

    // ==================== 用户认证API ====================

    /**
     * 获取当前会话
     */
    async getSession() {
        return this.get('/scene-auth/session');
    }

    /**
     * 获取菜单配置
     */
    async getMenuConfig(role, userId) {
        return this.get('/scene-auth/menu-config', { role, userId });
    }

    // ==================== 消息通知API ====================

    /**
     * 获取消息列表
     */
    async getMessages(userId, type = '', page = 1, size = 20) {
        const params = { userId, page, size };
        if (type) params.type = type;
        return this.get('/messages', params);
    }

    /**
     * 获取未读消息数
     */
    async getUnreadCount(userId) {
        return this.get('/messages/unread-count', { userId });
    }

    /**
     * 标记消息已读
     */
    async markMessageRead(messageId) {
        return this.post(`/messages/${messageId}/read`);
    }

    /**
     * 标记所有消息已读
     */
    async markAllMessagesRead(userId) {
        return this.post('/messages/read-all', { userId });
    }
}

// 创建全局实例
const apiService = new ApiService();

// 导出
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { ApiService, apiService };
} else {
    window.ApiService = ApiService;
    window.apiService = apiService;
}
