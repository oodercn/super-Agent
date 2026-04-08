const NotificationCenter = {
    notifications: [],
    unreadCounts: {
        all: 0,
        todo: 0,
        a2a: 0,
        system: 0
    },
    currentTab: 'all',
    isOpen: false,
    pollInterval: null,
    websocket: null,
    reconnectAttempts: 0,
    maxReconnectAttempts: 5,

    async init() {
        this.render();
        this.bindEvents();
        this.initBrowserNotification();
        await this.loadNotifications();
        this.connectWebSocket();
        this.startPolling();
    },

    render() {
        const container = document.getElementById('notificationCenter');
        if (!container) return;

        container.innerHTML = `
            <button class="nx-notification-btn" id="notificationBtn" title="消息中心">
                <i class="ri-notification-3-line"></i>
                <span class="nx-notification-badge" id="notificationBadge"></span>
            </button>
            <div class="nx-notification-panel" id="notificationPanel">
                <div class="nx-notification-header">
                    <h4>消息中心</h4>
                    <button onclick="NotificationCenter.markAllAsRead()">全部已读</button>
                </div>
                <div class="nx-notification-tabs">
                    <button class="active" data-tab="all" onclick="NotificationCenter.switchTab('all')">
                        全部
                    </button>
                    <button data-tab="todo" onclick="NotificationCenter.switchTab('todo')">
                        待办 <span class="nx-notification-tab-badge" id="todoBadge"></span>
                    </button>
                    <button data-tab="a2a" onclick="NotificationCenter.switchTab('a2a')">
                        A2A <span class="nx-notification-tab-badge" id="a2aBadge"></span>
                    </button>
                    <button data-tab="system" onclick="NotificationCenter.switchTab('system')">
                        系统
                    </button>
                </div>
                <div class="nx-notification-list" id="notificationList">
                    <div class="nx-notification-loading">
                        <i class="ri-loader-4-line"></i>
                        <span>加载中...</span>
                    </div>
                </div>
                <div class="nx-notification-footer">
                    <a href="/console/pages/my-todos.html">
                        <i class="ri-checkbox-circle-line"></i> 查看全部待办
                    </a>
                    <a href="javascript:void(0)" onclick="NotificationCenter.openDetailPage()">
                        <i class="ri-notification-3-line"></i> 消息中心
                    </a>
                </div>
            </div>
        `;
    },

    bindEvents() {
        document.addEventListener('click', (e) => {
            const container = document.getElementById('notificationCenter');
            const panel = document.getElementById('notificationPanel');
            const btn = document.getElementById('notificationBtn');

            if (this.isOpen && container && !container.contains(e.target)) {
                this.closePanel();
            }
        });

        const btn = document.getElementById('notificationBtn');
        if (btn) {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                this.togglePanel();
            });
        }
    },

    togglePanel() {
        this.isOpen = !this.isOpen;
        const panel = document.getElementById('notificationPanel');
        if (panel) {
            panel.classList.toggle('open', this.isOpen);
        }
    },

    closePanel() {
        this.isOpen = false;
        const panel = document.getElementById('notificationPanel');
        if (panel) {
            panel.classList.remove('open');
        }
    },

    async loadNotifications() {
        try {
            const response = await ApiClient.get('/api/v1/notifications');
            if (response.status === 'success') {
                this.notifications = response.data.notifications || [];
                this.unreadCounts = response.data.unreadCounts || { all: 0, todo: 0, a2a: 0, system: 0 };
                this.renderList();
                this.updateBadges();
            }
        } catch (error) {
            console.error('Failed to load notifications:', error);
            this.renderEmpty('加载失败，请稍后重试');
        }
    },

    renderList() {
        const container = document.getElementById('notificationList');
        if (!container) return;

        const filtered = this.currentTab === 'all' 
            ? this.notifications 
            : this.notifications.filter(n => this.getNotificationCategory(n.type) === this.currentTab);

        if (filtered.length === 0) {
            this.renderEmpty();
            return;
        }

        container.innerHTML = filtered.map(n => this.renderItem(n)).join('');
    },

    renderItem(notification) {
        const iconClass = this.getIconClass(notification.type);
        const icon = this.getIcon(notification.type);
        const category = this.getNotificationCategory(notification.type);

        return `
            <div class="nx-notification-item ${notification.unread ? 'unread' : ''}" 
                 data-type="${notification.type}" 
                 data-id="${notification.id}"
                 onclick="NotificationCenter.handleItemClick('${notification.id}')">
                <div class="nx-notification-icon ${iconClass}">
                    <i class="${icon}"></i>
                </div>
                <div class="nx-notification-content">
                    <div class="nx-notification-title">
                        ${category === 'a2a' ? `<span class="nx-notification-type-tag">A2A</span>` : ''}
                        ${this.escapeHtml(notification.title)}
                    </div>
                    <div class="nx-notification-desc">${this.escapeHtml(notification.description)}</div>
                    <div class="nx-notification-meta">
                        <span class="nx-notification-time">${this.formatTime(notification.time)}</span>
                        ${notification.sceneGroupName ? `<span><i class="ri-folder-line"></i> ${this.escapeHtml(notification.sceneGroupName)}</span>` : ''}
                    </div>
                    ${notification.todoCard ? this.renderTodoCard(notification.todoCard) : ''}
                </div>
                ${this.renderActions(notification)}
            </div>
        `;
    },

    renderTodoCard(todoCard) {
        const deadlineClass = todoCard.isUrgent ? 'urgent' : '';
        return `
            <div class="nx-notification-todo-card">
                <div class="nx-notification-todo-header">
                    <span class="nx-notification-todo-type">${this.escapeHtml(todoCard.typeLabel)}</span>
                    ${todoCard.deadline ? `<span class="nx-notification-todo-deadline ${deadlineClass}"><i class="ri-time-line"></i> ${this.escapeHtml(todoCard.deadline)}</span>` : ''}
                </div>
                <div class="nx-notification-todo-title">${this.escapeHtml(todoCard.title)}</div>
                ${todoCard.fromUserName ? `<div class="nx-notification-todo-meta"><i class="ri-user-line"></i> ${this.escapeHtml(todoCard.fromUserName)}</div>` : ''}
            </div>
        `;
    },

    renderActions(notification) {
        if (!notification.actions || notification.actions.length === 0) {
            return '';
        }

        return `
            <div class="nx-notification-actions">
                ${notification.actions.map(a => `
                    <button class="nx-btn nx-btn--${a.primary ? 'primary' : 'ghost'} nx-btn--sm"
                            onclick="event.stopPropagation(); NotificationCenter.handleAction('${notification.id}', '${a.action}')">
                        ${this.escapeHtml(a.label)}
                    </button>
                `).join('')}
            </div>
        `;
    },

    renderEmpty(message = '暂无消息') {
        const container = document.getElementById('notificationList');
        if (!container) return;

        container.innerHTML = `
            <div class="nx-notification-empty">
                <i class="ri-notification-off-line"></i>
                <div class="nx-notification-empty-title">${message}</div>
                <div class="nx-notification-empty-desc">新消息会在这里显示</div>
            </div>
        `;
    },

    updateBadges() {
        const mainBadge = document.getElementById('notificationBadge');
        const todoBadge = document.getElementById('todoBadge');
        const a2aBadge = document.getElementById('a2aBadge');

        if (mainBadge) {
            mainBadge.textContent = this.unreadCounts.all > 99 ? '99+' : this.unreadCounts.all;
            mainBadge.setAttribute('data-count', this.unreadCounts.all);
        }

        if (todoBadge) {
            todoBadge.textContent = this.unreadCounts.todo > 99 ? '99+' : this.unreadCounts.todo;
            todoBadge.setAttribute('data-count', this.unreadCounts.todo);
        }

        if (a2aBadge) {
            a2aBadge.textContent = this.unreadCounts.a2a > 99 ? '99+' : this.unreadCounts.a2a;
            a2aBadge.setAttribute('data-count', this.unreadCounts.a2a);
        }
    },

    switchTab(tab) {
        this.currentTab = tab;

        const tabs = document.querySelectorAll('.nx-notification-tabs button');
        tabs.forEach(btn => {
            btn.classList.toggle('active', btn.dataset.tab === tab);
        });

        this.renderList();
    },

    async handleItemClick(notificationId) {
        const notification = this.notifications.find(n => n.id === notificationId);
        if (!notification) return;

        if (notification.unread) {
            await this.markAsRead(notificationId);
        }

        if (notification.link) {
            window.location.href = notification.link;
            return;
        }

        if (notification.type.startsWith('A2A') || notification.type.startsWith('A2P')) {
            this.openAgentChat(notification);
        }
    },

    async handleAction(notificationId, action) {
        try {
            const response = await ApiClient.post(`/api/v1/notifications/${notificationId}/action`, { action });
            if (response.status === 'success') {
                await this.loadNotifications();
                if (response.data.message) {
                    Toast.show(response.data.message, 'success');
                }
            }
        } catch (error) {
            console.error('Failed to handle action:', error);
            Toast.show('操作失败', 'error');
        }
    },

    async markAsRead(notificationId) {
        try {
            await ApiClient.post(`/api/v1/notifications/${notificationId}/read`);
            const notification = this.notifications.find(n => n.id === notificationId);
            if (notification) {
                notification.unread = false;
                const category = this.getNotificationCategory(notification.type);
                this.unreadCounts.all = Math.max(0, this.unreadCounts.all - 1);
                this.unreadCounts[category] = Math.max(0, this.unreadCounts[category] - 1);
                this.updateBadges();
                this.renderList();
            }
        } catch (error) {
            console.error('Failed to mark as read:', error);
        }
    },

    async markAllAsRead() {
        try {
            await ApiClient.post('/api/v1/notifications/read-all');
            this.notifications.forEach(n => n.unread = false);
            this.unreadCounts = { all: 0, todo: 0, a2a: 0, system: 0 };
            this.updateBadges();
            this.renderList();
            Toast.show('已全部标记为已读', 'success');
        } catch (error) {
            console.error('Failed to mark all as read:', error);
            Toast.show('操作失败', 'error');
        }
    },

    openAgentChat(notification) {
        if (typeof openAgentChat === 'function') {
            openAgentChat(notification.conversationId);
        } else {
            const chatBtn = document.querySelector('.agent-chat-btn');
            if (chatBtn) {
                chatBtn.click();
            }
        }
        this.closePanel();
    },

    openDetailPage() {
        window.location.href = '/console/pages/notifications.html';
    },

    startPolling() {
        this.pollInterval = setInterval(() => {
            this.loadNotifications();
        }, 30000);
    },

    stopPolling() {
        if (this.pollInterval) {
            clearInterval(this.pollInterval);
            this.pollInterval = null;
        }
    },

    getNotificationCategory(type) {
        if (type.startsWith('TODO_')) return 'todo';
        if (type.startsWith('A2A') || type.startsWith('A2P')) return 'a2a';
        return 'system';
    },

    getIcon(type) {
        const icons = {
            'TODO_INVITATION': 'ri-user-add-line',
            'TODO_DELEGATION': 'ri-user-star-line',
            'TODO_REMINDER': 'ri-alarm-line',
            'TODO_APPROVAL': 'ri-checkbox-circle-line',
            'TODO_ACTIVATION': 'ri-key-2-line',
            'TODO_SCENE_NOTIFICATION': 'ri-notification-3-line',
            'A2A_MESSAGE': 'ri-robot-line',
            'A2P_MESSAGE': 'ri-user-line',
            'SYSTEM_NOTICE': 'ri-information-line',
            'SCENE_EVENT': 'ri-flashlight-line'
        };
        return icons[type] || 'ri-notification-line';
    },

    getIconClass(type) {
        return type.toLowerCase().replace('_', '-');
    },

    formatTime(timestamp) {
        if (!timestamp) return '';

        const date = new Date(timestamp);
        const now = new Date();
        const diff = now - date;

        const minutes = Math.floor(diff / 60000);
        const hours = Math.floor(diff / 3600000);
        const days = Math.floor(diff / 86400000);

        if (minutes < 1) return '刚刚';
        if (minutes < 60) return `${minutes}分钟前`;
        if (hours < 24) return `${hours}小时前`;
        if (days < 7) return `${days}天前`;

        return `${date.getMonth() + 1}月${date.getDate()}日`;
    },

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    },

    connectWebSocket() {
        if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
            return;
        }

        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const wsUrl = `${protocol}//${window.location.host}/ws/notifications?userId=${this.getCurrentUserId()}`;

        try {
            this.websocket = new WebSocket(wsUrl);

            this.websocket.onopen = () => {
                console.log('[NotificationCenter] WebSocket connected');
                this.reconnectAttempts = 0;
            };

            this.websocket.onmessage = (event) => {
                try {
                    const data = JSON.parse(event.data);
                    this.handleWebSocketMessage(data);
                } catch (e) {
                    console.error('[NotificationCenter] Failed to parse WebSocket message:', e);
                }
            };

            this.websocket.onclose = (event) => {
                console.log('[NotificationCenter] WebSocket closed:', event.code);
                this.websocket = null;
                
                if (this.reconnectAttempts < this.maxReconnectAttempts) {
                    this.reconnectAttempts++;
                    const delay = Math.min(1000 * Math.pow(2, this.reconnectAttempts), 30000);
                    console.log(`[NotificationCenter] Reconnecting in ${delay}ms (attempt ${this.reconnectAttempts})`);
                    setTimeout(() => this.connectWebSocket(), delay);
                }
            };

            this.websocket.onerror = (error) => {
                console.error('[NotificationCenter] WebSocket error:', error);
            };
        } catch (error) {
            console.error('[NotificationCenter] Failed to connect WebSocket:', error);
        }
    },

    handleWebSocketMessage(data) {
        switch (data.type) {
            case 'connected':
                console.log('[NotificationCenter] WebSocket session established');
                break;
            case 'notification':
                this.handleNewNotification(data.data);
                break;
            case 'unread_count_update':
                this.unreadCounts = data.data;
                this.updateBadges();
                break;
            case 'pong':
                break;
            default:
                console.log('[NotificationCenter] Unknown message type:', data.type);
        }
    },

    handleNewNotification(notification) {
        this.notifications.unshift(notification);
        
        const category = this.getNotificationCategory(notification.type);
        this.unreadCounts.all++;
        this.unreadCounts[category]++;

        this.updateBadges();
        this.renderList();

        this.showBrowserNotification(notification);

        this.playNotificationSound();
    },

    initBrowserNotification() {
        if (!('Notification' in window)) {
            console.log('[NotificationCenter] Browser notifications not supported');
            return;
        }

        if (Notification.permission === 'default') {
            Notification.requestPermission().then(permission => {
                console.log('[NotificationCenter] Notification permission:', permission);
            });
        }
    },

    showBrowserNotification(notification) {
        if (!('Notification' in window) || Notification.permission !== 'granted') {
            return;
        }

        try {
            const browserNotif = new Notification(notification.title, {
                body: notification.description,
                icon: '/favicon.svg',
                tag: notification.id,
                requireInteraction: false
            });

            browserNotif.onclick = () => {
                window.focus();
                this.handleItemClick(notification.id);
                browserNotif.close();
            };

            setTimeout(() => browserNotif.close(), 5000);
        } catch (e) {
            console.error('[NotificationCenter] Failed to show browser notification:', e);
        }
    },

    playNotificationSound() {
        try {
            const audio = new Audio('/console/sounds/notification.mp3');
            audio.volume = 0.5;
            audio.play().catch(e => {
                console.log('[NotificationCenter] Could not play notification sound:', e);
            });
        } catch (e) {
            console.log('[NotificationCenter] Notification sound not available');
        }
    },

    getCurrentUserId() {
        if (typeof NexusMenu !== 'undefined' && NexusMenu.currentUser && NexusMenu.currentUser.userId) {
            return NexusMenu.currentUser.userId;
        }
        
        const userIdMeta = document.querySelector('meta[name="user-id"]');
        if (userIdMeta) {
            return userIdMeta.getAttribute('content');
        }
        
        const userMenu = document.querySelector('.user-menu');
        if (userMenu && userMenu.dataset.userId) {
            return userMenu.dataset.userId;
        }
        
        if (typeof localStorage !== 'undefined') {
            const storedUserId = localStorage.getItem('userId');
            if (storedUserId) {
                return storedUserId;
            }
        }
        
        return 'default-user';
    },

    disconnectWebSocket() {
        if (this.websocket) {
            this.websocket.close();
            this.websocket = null;
        }
    }
};

window.NotificationCenter = NotificationCenter;
