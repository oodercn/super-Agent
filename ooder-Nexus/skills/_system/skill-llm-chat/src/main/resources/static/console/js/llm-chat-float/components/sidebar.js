/**
 * Agent Chat - 侧边栏组件
 * 包含TAB切换、参与者列表、待办列表
 */
export class Sidebar {
    constructor(container, options) {
        this.container = container;
        this.options = options;
        this.currentTab = 'all';
        this.tabs = [];
        this.init();
    }

    init() {
        this.render();
        this.bindEvents();
    }

    render() {
        this.container.innerHTML = `
            <div class="chat-sidebar" id="chatSidebar">
                <div class="chat-tabs" id="chatTabs">
                    ${this.renderTabs()}
                </div>
                <div class="chat-sidebar-content" id="sidebarContent">
                ${this.renderContent()}
                </div>
            </        `;
    }

    renderTabs() {
        const tabConfigs = [
            { id: 'all', icon: 'ri-chat-3-line', label: '全部' },
            { id: 'agents', icon: 'ri-robot-line', label: 'Agent' },
            { id: 'users', icon: 'ri-user-line', label: '用户' },
            { id: 'todos', icon: 'ri-task-line', label: '待办' }
        ];

        this.tabs = tabConfigs;
        this.container.querySelector('#chatTabs').innerHTML = tabConfigs.map(tab => `
            <div class="chat-tab ${tab.id === this.currentTab ? 'active' : ''}" 
                data-tab="${tab.id}">
                <i class="${tab.icon}"></i>
                <span>${tab.label}</span>
            </div>
        `).join('');
    }

    renderContent() {
        switch (this.currentTab) {
            case 'all':
            case 'agents':
                this.renderParticipants();
                break;
            case 'users':
                this.renderParticipants();
                break;
            case 'todos':
                this.renderTodos();
                break;
        }
    }

    renderParticipants() {
        const participants = this.options.participants || [];
        const container = this.container.querySelector('#sidebarContent');
        
        if (participants.length === 0) {
            container.innerHTML = '<div class="sidebar-empty">暂无参与者</div>';
            return;
        }

        container.innerHTML = `
            <div class="participant-list">
                ${participants.map(p => this.renderParticipant(p)).join('')}
            </div>
        `;
    }

    renderParticipant(participant) {
        const isAgent = participant.type === 'AGENT';
        const isSelected = this.options.selectedParticipant?.id === participant.id;
        
        return `
            <div class="participant-item ${isSelected ? 'selected' : ''}" 
                 data-id="${participant.id}" data-type="${participant.type}">
                <div class="participant-avatar ${isAgent ? 'agent' : ''}">
                    <i class="${isAgent ? 'ri-robot-line' : 'ri-user-line'}"></i>
                    <span class="online-dot"></span>
                </div>
                <div class="participant-info">
                    <div class="participant-name">${participant.name || '未知'}</div>
                    <div class="participant-status">${isAgent ? 'Agent' : '在线'}</div>
                </div>
            </div>
        `;
    }

    renderTodos() {
        const todos = this.options.todos || [];
        const container = this.container.querySelector('#sidebarContent');
        
        if (todos.length === 0) {
            container.innerHTML = '<div class="sidebar-empty">暂无待办事项</div>';
            return;
        }

        container.innerHTML = `
            <div class="todo-list">
                ${todos.map(todo => this.renderTodo(todo)).join('')}
            </div>
        `;
    }

    renderTodo(todo) {
        const priority = (todo.priority || 'LOW').toLowerCase();
        return `
            <div class="todo-card" data-id="${todo.id}">
                <div class="todo-header">
                    <span class="todo-priority ${priority}">${todo.priority || 'LOW'}</span>
                    <span class="todo-status">${todo.status || 'PENDING'}</span>
                </div>
                <div class="todo-title">${todo.title || todo.content || '无标题'}</div>
                ${todo.description ? `<div class="todo-desc">${todo.description}</div>` : ''}
                <div class="todo-actions">
                    <button class="todo-btn accept" data-action="accept">
                        <i class="ri-check-line"></i> 接受
                    </button>
                    <button class="todo-btn reject" data-action="reject">
                        <i class="ri-close-line"></i> 拒绝
                    </button>
                </div>
            </div>
        `;
    }

    bindEvents() {
        const tabsContainer = this.container.querySelector('#chatTabs');
        tabsContainer.addEventListener('click', (e) => {
            const tab = e.target.closest('.chat-tab');
            if (tab) {
                this.switchTab(tab.dataset.tab);
            }
        });

        this.container.addEventListener('click', (e) => {
            const participantItem = e.target.closest('.participant-item');
            if (participantItem) {
                this.options.onParticipantSelect?.(participantItem.dataset.id);
            }
        });
    }

    switchTab(tabId) {
        this.currentTab = tabId;
        this.renderTabs();
        this.renderContent();
        this.options.onTabChange?.(tabId);
    }

    setParticipants(participants) {
        this.renderParticipants();
    }

    setTodos(todos) {
        this.renderTodos();
    }

    setSelectedParticipant(participant) {
        this.renderParticipants();
    }
}

export default Sidebar;
