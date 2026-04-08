/**
 * 待办中心窗口
 * 管理待办事项
 * 集成后端数据同步
 */
import { BaseWindow } from './base-window.js';
import { eventBus } from '../core/event-bus.js';

export class TodoWindow extends BaseWindow {
    constructor(container, options = {}) {
        super(container, {
            id: 'todo-window',
            title: '待办中心',
            icon: 'ri-task-line',
            width: 380,
            height: 500,
            ...options
        });
        
        this.todos = [];
        this.currentFilter = 'all';
        this.isLoading = false;
    }

    renderContent() {
        return `
            <div class="todo-container">
                <div class="todo-header">
                    <div class="todo-tabs">
                        <div class="todo-tab active" data-status="all">全部</div>
                        <div class="todo-tab" data-status="pending">待处理</div>
                        <div class="todo-tab" data-status="done">已完成</div>
                    </div>
                    <button class="todo-add-btn" id="todoAddBtn" title="新增待办">
                        <i class="ri-add-line"></i>
                    </button>
                </div>
                <div class="todo-list" id="todoList">
                    ${this.renderTodos()}
                </div>
                <div class="todo-add-form" id="todoAddForm" style="display:none;">
                    <input type="text" id="todoNewInput" placeholder="输入待办内容..." />
                    <div class="todo-add-actions">
                        <select id="todoNewPriority">
                            <option value="LOW">低优先级</option>
                            <option value="MEDIUM">中优先级</option>
                            <option value="HIGH">高优先级</option>
                        </select>
                        <button class="todo-add-confirm" id="todoAddConfirm">添加</button>
                        <button class="todo-add-cancel" id="todoAddCancel">取消</button>
                    </div>
                </div>
                <div class="todo-help">
                    <p><i class="ri-information-line"></i> 待办数据来源于场景组任务，点击复选框标记完成</p>
                </div>
            </div>
        `;
    }

    renderTodos() {
        if (this.todos.length === 0) {
            return `
                <div class="todo-empty">
                    <i class="ri-task-line"></i>
                    <p>暂无待办事项</p>
                    <span>点击右上角 + 添加新待办</span>
                </div>
            `;
        }
        
        let filtered = this.todos;
        if (this.currentFilter === 'pending') {
            filtered = this.todos.filter(t => t.status !== 'DONE');
        } else if (this.currentFilter === 'done') {
            filtered = this.todos.filter(t => t.status === 'DONE');
        }
        
        if (filtered.length === 0) {
            return `
                <div class="todo-empty">
                    <i class="ri-filter-line"></i>
                    <p>无匹配的待办</p>
                </div>
            `;
        }
        
        return filtered.map(todo => this.renderTodoItem(todo)).join('');
    }

    renderTodoItem(todo) {
        const priority = (todo.priority || 'LOW').toLowerCase();
        const status = todo.status || 'PENDING';
        const isDone = status === 'DONE';
        
        return `
            <div class="todo-item ${isDone ? 'done' : ''}" data-id="${todo.id}">
                <div class="todo-checkbox ${isDone ? 'checked' : ''}">
                    <i class="ri-check-line"></i>
                </div>
                <div class="todo-content">
                    <div class="todo-title">${this.escapeHtml(todo.title || todo.content)}</div>
                    ${todo.description ? `<div class="todo-desc">${this.escapeHtml(todo.description)}</div>` : ''}
                    <div class="todo-meta">
                        <span class="todo-priority ${priority}">${this.getPriorityLabel(todo.priority)}</span>
                        ${todo.dueTime ? `<span class="todo-time"><i class="ri-time-line"></i> ${this.formatDueTime(todo.dueTime)}</span>` : ''}
                    </div>
                </div>
                <div class="todo-actions">
                    <button class="todo-action-btn edit" title="编辑">
                        <i class="ri-edit-line"></i>
                    </button>
                    <button class="todo-action-btn delete" title="删除">
                        <i class="ri-delete-bin-line"></i>
                    </button>
                </div>
            </div>
        `;
    }

    getPriorityLabel(priority) {
        const labels = {
            'HIGH': '高',
            'MEDIUM': '中',
            'LOW': '低'
        };
        return labels[priority] || '低';
    }

    formatDueTime(dueTime) {
        if (!dueTime) return '';
        const date = new Date(dueTime);
        const now = new Date();
        const diff = date - now;
        
        if (diff < 0) {
            return '已过期';
        } else if (diff < 3600000) {
            return Math.floor(diff / 60000) + '分钟后';
        } else if (diff < 86400000) {
            return Math.floor(diff / 3600000) + '小时后';
        } else {
            return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' });
        }
    }

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    bindContentEvents() {
        this.addContentStyles();
        
        const tabs = this.element.querySelectorAll('.todo-tab');
        tabs.forEach(tab => {
            tab.addEventListener('click', () => {
                tabs.forEach(t => t.classList.remove('active'));
                tab.classList.add('active');
                this.currentFilter = tab.dataset.status;
                this.renderList();
            });
        });
        
        const addBtn = this.element.querySelector('#todoAddBtn');
        const addForm = this.element.querySelector('#todoAddForm');
        const addConfirm = this.element.querySelector('#todoAddConfirm');
        const addCancel = this.element.querySelector('#todoAddCancel');
        const newInput = this.element.querySelector('#todoNewInput');
        
        addBtn.addEventListener('click', () => {
            addForm.style.display = 'block';
            newInput.focus();
        });
        
        addCancel.addEventListener('click', () => {
            addForm.style.display = 'none';
            newInput.value = '';
        });
        
        newInput.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                this.addNewTodo();
            } else if (e.key === 'Escape') {
                addForm.style.display = 'none';
                newInput.value = '';
            }
        });
        
        addConfirm.addEventListener('click', () => this.addNewTodo());
        
        this.element.addEventListener('click', (e) => {
            const checkbox = e.target.closest('.todo-checkbox');
            if (checkbox) {
                const item = checkbox.closest('.todo-item');
                this.toggleTodo(item.dataset.id);
            }
            
            const editBtn = e.target.closest('.todo-action-btn.edit');
            if (editBtn) {
                const item = editBtn.closest('.todo-item');
                this.editTodo(item.dataset.id);
            }
            
            const deleteBtn = e.target.closest('.todo-action-btn.delete');
            if (deleteBtn) {
                const item = deleteBtn.closest('.todo-item');
                this.deleteTodo(item.dataset.id);
            }
        });
        
        eventBus.on('todo:update', (data) => {
            this.handleTodoUpdate(data);
        });
        
        this.loadTodos();
    }

    async loadTodos() {
        if (this.isLoading) return;
        this.isLoading = true;
        
        try {
            const sceneGroupId = this.options.sceneGroupId;
            const url = sceneGroupId
                ? `/api/v1/scene-groups/${sceneGroupId}/chat/todos`
                : '/api/v1/my/todos';
            
            const response = await fetch(url);
            const result = await response.json();
            
            if (result.status === 'success' || result.code === 200) {
                this.todos = result.data?.list || result.data || [];
                this.renderList();
            }
        } catch (e) {
            console.error('[TodoWindow] Load error:', e);
            this.loadMockTodos();
        } finally {
            this.isLoading = false;
        }
    }

    loadMockTodos() {
        this.todos = [
            { id: 'todo-1', title: '完成项目文档编写', priority: 'HIGH', status: 'PENDING', dueTime: Date.now() + 86400000 },
            { id: 'todo-2', title: '代码审查', priority: 'MEDIUM', status: 'PENDING', dueTime: Date.now() + 172800000 },
            { id: 'todo-3', title: '更新依赖版本', priority: 'LOW', status: 'DONE' }
        ];
        this.renderList();
    }

    async addNewTodo() {
        const input = this.element.querySelector('#todoNewInput');
        const prioritySelect = this.element.querySelector('#todoNewPriority');
        const content = input.value.trim();
        
        if (!content) return;
        
        const todo = {
            id: 'todo-' + Date.now(),
            title: content,
            content: content,
            priority: prioritySelect.value,
            status: 'PENDING',
            createTime: Date.now()
        };
        
        try {
            const sceneGroupId = this.options.sceneGroupId;
            const url = sceneGroupId
                ? `/api/v1/scene-groups/${sceneGroupId}/chat/todos`
                : '/api/v1/my/todos';

            const response = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(todo)
            });
            
            const result = await response.json();
            
            if (result.status === 'success' || result.code === 200) {
                const newTodo = result.data || todo;
                this.todos.unshift(newTodo);
                this.renderList();
                
                input.value = '';
                this.element.querySelector('#todoAddForm').style.display = 'none';
                
                eventBus.emit('todo:added', { todo: newTodo });
            }
        } catch (e) {
            console.error('[TodoWindow] Add error:', e);
            this.todos.unshift(todo);
            this.renderList();
            input.value = '';
            this.element.querySelector('#todoAddForm').style.display = 'none';
        }
    }

    async toggleTodo(id) {
        const todo = this.todos.find(t => t.id === id);
        if (!todo) return;
        
        const newStatus = todo.status === 'DONE' ? 'PENDING' : 'DONE';
        const oldStatus = todo.status;
        
        todo.status = newStatus;
        this.renderList();
        
        try {
            await fetch(`/api/v1/my/todos/${id}/complete`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });
            
            eventBus.emit('todo:toggle', { id, status: newStatus });
        } catch (e) {
            console.error('[TodoWindow] Toggle error:', e);
            todo.status = oldStatus;
            this.renderList();
        }
    }

    async deleteTodo(id) {
        const index = this.todos.findIndex(t => t.id === id);
        if (index === -1) return;
        
        const todo = this.todos[index];
        
        this.todos.splice(index, 1);
        this.renderList();
        
        try {
            await fetch(`/api/v1/my/todos/${id}`, {
                method: 'DELETE'
            });
            
            eventBus.emit('todo:delete', { id });
        } catch (e) {
            console.error('[TodoWindow] Delete error:', e);
            this.todos.splice(index, 0, todo);
            this.renderList();
        }
    }

    editTodo(id) {
        const todo = this.todos.find(t => t.id === id);
        if (!todo) return;
        
        const newTitle = prompt('编辑待办内容:', todo.title || todo.content);
        if (newTitle === null || newTitle.trim() === '') return;
        
        const oldTitle = todo.title || todo.content;
        todo.title = newTitle.trim();
        todo.content = newTitle.trim();
        this.renderList();
        
        fetch(`/api/v1/my/todos/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title: newTitle.trim() })
        }).catch(e => {
            console.error('[TodoWindow] Edit error:', e);
            todo.title = oldTitle;
            todo.content = oldTitle;
            this.renderList();
        });
    }

    handleTodoUpdate(data) {
        const todo = this.todos.find(t => t.id === data.id);
        if (todo) {
            Object.assign(todo, data);
            this.renderList();
        }
    }

    setTodos(todos) {
        this.todos = todos || [];
        this.renderList();
    }

    addTodo(todo) {
        this.todos.unshift(todo);
        this.renderList();
    }

    renderList() {
        const list = this.element.querySelector('#todoList');
        list.innerHTML = this.renderTodos();
    }

    open() {
        super.open();
        this.loadTodos();
    }

    addContentStyles() {
        if (document.getElementById('todo-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'todo-styles';
        style.textContent = `
            .todo-container {
                display: flex;
                flex-direction: column;
                height: 100%;
            }
            .todo-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 12px 16px;
                border-bottom: 1px solid #e2e8f0;
                background: #f8fafc;
            }
            .todo-tabs {
                display: flex;
                gap: 8px;
            }
            .todo-tab {
                padding: 6px 12px;
                border-radius: 16px;
                font-size: 13px;
                cursor: pointer;
                color: #64748b;
                transition: all 0.2s;
            }
            .todo-tab:hover { background: #f1f5f9; }
            .todo-tab.active {
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
            }
            .todo-add-btn {
                background: none;
                border: none;
                color: #6366f1;
                cursor: pointer;
                padding: 6px;
                border-radius: 6px;
                font-size: 18px;
                transition: all 0.2s;
            }
            .todo-add-btn:hover {
                background: rgba(99, 102, 241, 0.1);
            }
            .todo-list {
                flex: 1;
                overflow-y: auto;
                padding: 12px;
            }
            .todo-empty {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                height: 100%;
                color: #94a3b8;
            }
            .todo-empty i { font-size: 48px; margin-bottom: 16px; opacity: 0.5; }
            .todo-empty p { font-size: 14px; margin: 0 0 4px; }
            .todo-empty span { font-size: 12px; }
            .todo-item {
                display: flex;
                align-items: flex-start;
                gap: 12px;
                padding: 12px;
                background: white;
                border-radius: 12px;
                border: 1px solid #e2e8f0;
                margin-bottom: 8px;
                transition: all 0.2s;
            }
            .todo-item:hover { 
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06); 
            }
            .todo-item.done {
                opacity: 0.6;
            }
            .todo-item.done .todo-title {
                text-decoration: line-through;
                color: #94a3b8;
            }
            .todo-checkbox {
                width: 24px;
                height: 24px;
                min-width: 24px;
                border-radius: 50%;
                border: 2px solid #e2e8f0;
                display: flex;
                align-items: center;
                justify-content: center;
                cursor: pointer;
                transition: all 0.2s;
                color: transparent;
                margin-top: 2px;
            }
            .todo-checkbox:hover { border-color: #6366f1; }
            .todo-checkbox.checked {
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                border-color: transparent;
                color: white;
            }
            .todo-content { flex: 1; min-width: 0; }
            .todo-title {
                font-size: 14px;
                font-weight: 500;
                color: #334155;
                margin-bottom: 4px;
                word-break: break-word;
            }
            .todo-desc {
                font-size: 13px;
                color: #64748b;
                margin-bottom: 8px;
                line-height: 1.4;
            }
            .todo-meta {
                display: flex;
                gap: 8px;
                align-items: center;
                flex-wrap: wrap;
            }
            .todo-priority {
                font-size: 11px;
                padding: 2px 8px;
                border-radius: 10px;
                font-weight: 500;
            }
            .todo-priority.high { background: #fef2f2; color: #dc2626; }
            .todo-priority.medium { background: #fffbeb; color: #d97706; }
            .todo-priority.low { background: #f0fdf4; color: #16a34a; }
            .todo-time { 
                font-size: 11px; 
                color: #94a3b8; 
                display: flex;
                align-items: center;
                gap: 4px;
            }
            .todo-actions {
                display: flex;
                gap: 4px;
                opacity: 0;
                transition: opacity 0.2s;
            }
            .todo-item:hover .todo-actions { opacity: 1; }
            .todo-action-btn {
                width: 28px;
                height: 28px;
                border-radius: 6px;
                border: none;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 14px;
                transition: all 0.2s;
            }
            .todo-action-btn.edit {
                background: #f1f5f9;
                color: #64748b;
            }
            .todo-action-btn.edit:hover {
                background: #6366f1;
                color: white;
            }
            .todo-action-btn.delete {
                background: #fef2f2;
                color: #dc2626;
            }
            .todo-action-btn.delete:hover {
                background: #dc2626;
                color: white;
            }
            .todo-add-form {
                padding: 12px 16px;
                border-top: 1px solid #e2e8f0;
                background: #f8fafc;
            }
            .todo-add-form input {
                width: 100%;
                padding: 10px 14px;
                border: 1px solid #e2e8f0;
                border-radius: 8px;
                font-size: 14px;
                margin-bottom: 8px;
            }
            .todo-add-form input:focus {
                outline: none;
                border-color: #6366f1;
            }
            .todo-add-actions {
                display: flex;
                gap: 8px;
                align-items: center;
            }
            .todo-add-actions select {
                padding: 6px 10px;
                border: 1px solid #e2e8f0;
                border-radius: 6px;
                font-size: 13px;
            }
            .todo-add-confirm {
                padding: 6px 16px;
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 13px;
            }
            .todo-add-cancel {
                padding: 6px 16px;
                background: #f1f5f9;
                color: #64748b;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 13px;
            }
            .todo-help {
                padding: 8px 16px;
                background: #f8fafc;
                border-top: 1px solid #e2e8f0;
                font-size: 12px;
                color: #94a3b8;
            }
            .todo-help i {
                margin-right: 4px;
            }
        `;
        document.head.appendChild(style);
    }
}

export default TodoWindow;
