// EndRoute管理模块
(function() {
    'use strict';

    // 路由数据缓存
    let routeAgents = new Map();
    let endAgents = new Map();

    // DOM元素
    let elements = {
        routeCount: document.getElementById('routeCount'),
        endAgentCount: document.getElementById('endAgentCount'),
        networkStatus: document.getElementById('networkStatus'),
        routesTableBody: document.getElementById('routesTableBody'),
        endAgentsTableBody: document.getElementById('endAgentsTableBody'),
        addRouteBtn: document.getElementById('addRouteBtn'),
        addEndAgentBtn: document.getElementById('addEndAgentBtn'),
        refreshRoutesBtn: document.getElementById('refreshRoutesBtn'),
        refreshEndAgentsBtn: document.getElementById('refreshEndAgentsBtn'),
        addRouteModal: document.getElementById('addRouteModal'),
        addEndAgentModal: document.getElementById('addEndAgentModal'),
        closeAddRouteModal: document.getElementById('closeAddRouteModal'),
        closeAddEndAgentModal: document.getElementById('closeAddEndAgentModal'),
        cancelAddRouteBtn: document.getElementById('cancelAddRouteBtn'),
        cancelAddEndAgentBtn: document.getElementById('cancelAddEndAgentBtn'),
        submitAddRouteBtn: document.getElementById('submitAddRouteBtn'),
        submitAddEndAgentBtn: document.getElementById('submitAddEndAgentBtn'),
        addRouteForm: document.getElementById('addRouteForm'),
        addEndAgentForm: document.getElementById('addEndAgentForm')
    };

    // 初始化
    function init() {
        console.log('Initializing EndRoute Management Module');
        bindEvents();
        loadInitialData();
        setInterval(updateStatus, 5000); // 每5秒更新一次状态
    }

    // 绑定事件
    function bindEvents() {
        if (elements.addRouteBtn) {
            elements.addRouteBtn.addEventListener('click', showAddRouteModal);
        }
        if (elements.addEndAgentBtn) {
            elements.addEndAgentBtn.addEventListener('click', showAddEndAgentModal);
        }
        if (elements.refreshRoutesBtn) {
            elements.refreshRoutesBtn.addEventListener('click', refreshRoutes);
        }
        if (elements.refreshEndAgentsBtn) {
            elements.refreshEndAgentsBtn.addEventListener('click', refreshEndAgents);
        }
        if (elements.closeAddRouteModal) {
            elements.closeAddRouteModal.addEventListener('click', hideAddRouteModal);
        }
        if (elements.closeAddEndAgentModal) {
            elements.closeAddEndAgentModal.addEventListener('click', hideAddEndAgentModal);
        }
        if (elements.cancelAddRouteBtn) {
            elements.cancelAddRouteBtn.addEventListener('click', hideAddRouteModal);
        }
        if (elements.cancelAddEndAgentBtn) {
            elements.cancelAddEndAgentBtn.addEventListener('click', hideAddEndAgentModal);
        }
        if (elements.submitAddRouteBtn) {
            elements.submitAddRouteBtn.addEventListener('click', addRoute);
        }
        if (elements.submitAddEndAgentBtn) {
            elements.submitAddEndAgentBtn.addEventListener('click', addEndAgent);
        }
    }

    // 加载初始数据
    function loadInitialData() {
        console.log('Loading initial data...');
        // 模拟加载数据
        setTimeout(() => {
            generateMockData();
            updateRouteTable();
            updateEndAgentTable();
            updateStatus();
        }, 500);
    }

    // 生成模拟数据
    function generateMockData() {
        // 生成路由数据
        for (let i = 1; i <= 5; i++) {
            const routeId = `route_${i}`;
            routeAgents.set(routeId, {
                routeId: routeId,
                status: i % 2 === 0 ? 'active' : 'inactive',
                registerTime: new Date(Date.now() - i * 3600000).toISOString(),
                lastHeartbeatTime: new Date(Date.now() - i * 600000).toISOString()
            });
        }

        // 生成终端代理数据
        for (let i = 1; i <= 8; i++) {
            const endAgentId = `endagent_${i}`;
            endAgents.set(endAgentId, {
                endAgentId: endAgentId,
                status: i % 3 !== 0 ? 'active' : 'inactive',
                createTime: new Date(Date.now() - i * 7200000).toISOString(),
                lastHeartbeatTime: new Date(Date.now() - i * 300000).toISOString()
            });
        }
    }

    // 更新状态
    function updateStatus() {
        if (elements.routeCount) {
            elements.routeCount.textContent = routeAgents.size;
        }
        if (elements.endAgentCount) {
            elements.endAgentCount.textContent = endAgents.size;
        }
        if (elements.networkStatus) {
            elements.networkStatus.textContent = '正常';
        }
    }

    // 更新路由表格
    function updateRouteTable() {
        if (!elements.routesTableBody) return;

        elements.routesTableBody.innerHTML = '';

        routeAgents.forEach((route) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${route.routeId}</td>
                <td>
                    <span class="status-badge ${route.status === 'active' ? 'status-active' : 'status-inactive'}">
                        ${route.status === 'active' ? '活跃' : '非活跃'}
                    </span>
                </td>
                <td>${formatDate(route.registerTime)}</td>
                <td>${formatDate(route.lastHeartbeatTime)}</td>
                <td>
                    <button class="btn-action btn-edit" data-id="${route.routeId}">
                        <i class="ri-edit-line"></i>
                    </button>
                    <button class="btn-action btn-delete" data-id="${route.routeId}">
                        <i class="ri-delete-line"></i>
                    </button>
                </td>
            `;
            elements.routesTableBody.appendChild(row);
        });

        // 绑定操作按钮事件
        bindRouteActionEvents();
    }

    // 更新终端代理表格
    function updateEndAgentTable() {
        if (!elements.endAgentsTableBody) return;

        elements.endAgentsTableBody.innerHTML = '';

        endAgents.forEach((agent) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${agent.endAgentId}</td>
                <td>
                    <span class="status-badge ${agent.status === 'active' ? 'status-active' : 'status-inactive'}">
                        ${agent.status === 'active' ? '活跃' : '非活跃'}
                    </span>
                </td>
                <td>${formatDate(agent.createTime)}</td>
                <td>${formatDate(agent.lastHeartbeatTime)}</td>
                <td>
                    <button class="btn-action btn-edit" data-id="${agent.endAgentId}">
                        <i class="ri-edit-line"></i>
                    </button>
                    <button class="btn-action btn-delete" data-id="${agent.endAgentId}">
                        <i class="ri-delete-line"></i>
                    </button>
                </td>
            `;
            elements.endAgentsTableBody.appendChild(row);
        });

        // 绑定操作按钮事件
        bindEndAgentActionEvents();
    }

    // 绑定路由操作事件
    function bindRouteActionEvents() {
        document.querySelectorAll('.btn-action.btn-edit[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const routeId = this.getAttribute('data-id');
                editRoute(routeId);
            });
        });

        document.querySelectorAll('.btn-action.btn-delete[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const routeId = this.getAttribute('data-id');
                deleteRoute(routeId);
            });
        });
    }

    // 绑定终端代理操作事件
    function bindEndAgentActionEvents() {
        document.querySelectorAll('.btn-action.btn-edit[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const endAgentId = this.getAttribute('data-id');
                editEndAgent(endAgentId);
            });
        });

        document.querySelectorAll('.btn-action.btn-delete[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const endAgentId = this.getAttribute('data-id');
                deleteEndAgent(endAgentId);
            });
        });
    }

    // 显示添加路由模态框
    function showAddRouteModal() {
        if (elements.addRouteModal) {
            elements.addRouteModal.classList.add('show');
        }
    }

    // 隐藏添加路由模态框
    function hideAddRouteModal() {
        if (elements.addRouteModal) {
            elements.addRouteModal.classList.remove('show');
        }
        if (elements.addRouteForm) {
            elements.addRouteForm.reset();
        }
    }

    // 显示添加终端代理模态框
    function showAddEndAgentModal() {
        if (elements.addEndAgentModal) {
            elements.addEndAgentModal.classList.add('show');
        }
    }

    // 隐藏添加终端代理模态框
    function hideAddEndAgentModal() {
        if (elements.addEndAgentModal) {
            elements.addEndAgentModal.classList.remove('show');
        }
        if (elements.addEndAgentForm) {
            elements.addEndAgentForm.reset();
        }
    }

    // 添加路由
    function addRoute() {
        if (!elements.addRouteForm) return;

        const formData = new FormData(elements.addRouteForm);
        const routeId = formData.get('routeId');
        const status = formData.get('routeStatus');

        if (!routeId) {
            alert('请输入路由ID');
            return;
        }

        const newRoute = {
            routeId: routeId,
            status: status,
            registerTime: new Date().toISOString(),
            lastHeartbeatTime: new Date().toISOString()
        };

        routeAgents.set(routeId, newRoute);
        updateRouteTable();
        updateStatus();
        hideAddRouteModal();

        console.log('Added new route:', newRoute);
    }

    // 添加终端代理
    function addEndAgent() {
        if (!elements.addEndAgentForm) return;

        const formData = new FormData(elements.addEndAgentForm);
        const endAgentId = formData.get('endAgentId');
        const status = formData.get('endAgentStatus');

        if (!endAgentId) {
            alert('请输入终端ID');
            return;
        }

        const newEndAgent = {
            endAgentId: endAgentId,
            status: status,
            createTime: new Date().toISOString(),
            lastHeartbeatTime: new Date().toISOString()
        };

        endAgents.set(endAgentId, newEndAgent);
        updateEndAgentTable();
        updateStatus();
        hideAddEndAgentModal();

        console.log('Added new end agent:', newEndAgent);
    }

    // 编辑路由
    function editRoute(routeId) {
        console.log('Editing route:', routeId);
        // 这里可以实现编辑逻辑
        alert(`编辑路由: ${routeId}`);
    }

    // 删除路由
    function deleteRoute(routeId) {
        if (confirm(`确定要删除路由 ${routeId} 吗？`)) {
            routeAgents.delete(routeId);
            updateRouteTable();
            updateStatus();
            console.log('Deleted route:', routeId);
        }
    }

    // 编辑终端代理
    function editEndAgent(endAgentId) {
        console.log('Editing end agent:', endAgentId);
        // 这里可以实现编辑逻辑
        alert(`编辑终端代理: ${endAgentId}`);
    }

    // 删除终端代理
    function deleteEndAgent(endAgentId) {
        if (confirm(`确定要删除终端代理 ${endAgentId} 吗？`)) {
            endAgents.delete(endAgentId);
            updateEndAgentTable();
            updateStatus();
            console.log('Deleted end agent:', endAgentId);
        }
    }

    // 刷新路由数据
    function refreshRoutes() {
        console.log('Refreshing routes data');
        // 这里可以实现从服务器加载数据的逻辑
        updateRouteTable();
        alert('路由数据已刷新');
    }

    // 刷新终端代理数据
    function refreshEndAgents() {
        console.log('Refreshing end agents data');
        // 这里可以实现从服务器加载数据的逻辑
        updateEndAgentTable();
        alert('终端代理数据已刷新');
    }

    // 格式化日期
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
    }

    // 页面加载完成后初始化
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    // 暴露全局方法
    window.EndRouteManager = {
        init: init,
        addRoute: addRoute,
        addEndAgent: addEndAgent,
        refreshRoutes: refreshRoutes,
        refreshEndAgents: refreshEndAgents
    };
})();
