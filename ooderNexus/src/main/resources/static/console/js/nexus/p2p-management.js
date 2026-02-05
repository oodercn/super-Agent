// P2P管理模块
(function() {
    'use strict';

    // P2P连接数据缓存
    let p2pConnections = new Map();
    let nodes = new Map();

    // DOM元素
    let elements = {
        p2pConnectionCount: document.getElementById('p2pConnectionCount'),
        nodeCount: document.getElementById('nodeCount'),
        networkStatus: document.getElementById('networkStatus'),
        p2pConnectionsTableBody: document.getElementById('p2pConnectionsTableBody'),
        topologyGraph: document.getElementById('topologyGraph'),
        addP2PBtn: document.getElementById('addP2PBtn'),
        refreshP2PBtns: document.getElementById('refreshP2PBtns'),
        addP2PModal: document.getElementById('addP2PModal'),
        closeAddP2PModal: document.getElementById('closeAddP2PModal'),
        cancelAddP2PBtn: document.getElementById('cancelAddP2PBtn'),
        submitAddP2PBtn: document.getElementById('submitAddP2PBtn'),
        addP2PForm: document.getElementById('addP2PForm')
    };

    // 初始化
    function init() {
        console.log('Initializing P2P Management Module');
        bindEvents();
        loadInitialData();
        setInterval(updateStatus, 5000); // 每5秒更新一次状态
    }

    // 绑定事件
    function bindEvents() {
        if (elements.addP2PBtn) {
            elements.addP2PBtn.addEventListener('click', showAddP2PModal);
        }
        if (elements.refreshP2PBtns) {
            elements.refreshP2PBtns.addEventListener('click', refreshP2PConnections);
        }
        if (elements.closeAddP2PModal) {
            elements.closeAddP2PModal.addEventListener('click', hideAddP2PModal);
        }
        if (elements.cancelAddP2PBtn) {
            elements.cancelAddP2PBtn.addEventListener('click', hideAddP2PModal);
        }
        if (elements.submitAddP2PBtn) {
            elements.submitAddP2PBtn.addEventListener('click', addP2PConnection);
        }
    }

    // 加载初始数据
    function loadInitialData() {
        console.log('Loading initial P2P data...');
        // 模拟加载数据
        setTimeout(() => {
            generateMockData();
            updateP2PConnectionsTable();
            updateNetworkTopology();
            updateStatus();
        }, 500);
    }

    // 生成模拟数据
    function generateMockData() {
        // 生成节点数据
        for (let i = 1; i <= 10; i++) {
            const nodeId = `node_${i}`;
            nodes.set(nodeId, {
                nodeId: nodeId,
                name: `节点 ${i}`,
                type: i % 3 === 0 ? 'relay' : i % 2 === 0 ? 'end' : 'router',
                status: i % 4 === 0 ? 'inactive' : 'active',
                ipAddress: `192.168.1.${i + 10}`,
                lastSeen: new Date(Date.now() - i * 600000).toISOString(),
                connections: []
            });
        }

        // 生成P2P连接数据
        const connectionTypes = ['direct', 'relay', 'holepunch'];
        const statuses = ['active', 'connecting', 'disconnected'];

        for (let i = 1; i <= 15; i++) {
            const connectionId = `p2p_${i}`;
            const localNodeId = `node_${Math.floor(Math.random() * 5) + 1}`;
            const remoteNodeId = `node_${Math.floor(Math.random() * 5) + 6}`;
            const connectionType = connectionTypes[Math.floor(Math.random() * connectionTypes.length)];
            const status = i % 5 === 0 ? 'disconnected' : i % 4 === 0 ? 'connecting' : 'active';

            p2pConnections.set(connectionId, {
                connectionId: connectionId,
                localNodeId: localNodeId,
                remoteNodeId: remoteNodeId,
                connectionType: connectionType,
                status: status,
                createTime: new Date(Date.now() - i * 3600000).toISOString(),
                lastActivityTime: new Date(Date.now() - i * 600000).toISOString(),
                stats: {
                    latency: Math.floor(Math.random() * 100) + 10,
                    bandwidth: Math.floor(Math.random() * 1000) + 100,
                    packetLoss: Math.floor(Math.random() * 10) / 10
                }
            });

            // 更新节点的连接列表
            if (nodes.has(localNodeId)) {
                const localNode = nodes.get(localNodeId);
                localNode.connections.push(connectionId);
                nodes.set(localNodeId, localNode);
            }
            if (nodes.has(remoteNodeId)) {
                const remoteNode = nodes.get(remoteNodeId);
                remoteNode.connections.push(connectionId);
                nodes.set(remoteNodeId, remoteNode);
            }
        }
    }

    // 更新状态
    function updateStatus() {
        if (elements.p2pConnectionCount) {
            elements.p2pConnectionCount.textContent = p2pConnections.size;
        }
        if (elements.nodeCount) {
            elements.nodeCount.textContent = nodes.size;
        }
        if (elements.networkStatus) {
            const activeConnections = Array.from(p2pConnections.values()).filter(conn => conn.status === 'active').length;
            if (activeConnections > 0) {
                elements.networkStatus.textContent = '正常';
            } else {
                elements.networkStatus.textContent = '异常';
            }
        }
    }

    // 更新P2P连接表格
    function updateP2PConnectionsTable() {
        if (!elements.p2pConnectionsTableBody) return;

        elements.p2pConnectionsTableBody.innerHTML = '';

        p2pConnections.forEach((connection) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${connection.connectionId}</td>
                <td>${connection.localNodeId}</td>
                <td>${connection.remoteNodeId}</td>
                <td>
                    <span class="type-badge type-${connection.connectionType}">
                        ${getConnectionTypeText(connection.connectionType)}
                    </span>
                </td>
                <td>
                    <span class="status-badge ${getStatusClass(connection.status)}">
                        ${getStatusText(connection.status)}
                    </span>
                </td>
                <td>${formatDate(connection.createTime)}</td>
                <td>${formatDate(connection.lastActivityTime)}</td>
                <td>
                    <button class="btn-action btn-edit" data-id="${connection.connectionId}">
                        <i class="ri-edit-line"></i>
                    </button>
                    <button class="btn-action btn-delete" data-id="${connection.connectionId}">
                        <i class="ri-delete-line"></i>
                    </button>
                    <button class="btn-action btn-status" data-id="${connection.connectionId}" data-status="${connection.status}">
                        <i class="ri-refresh-line"></i>
                    </button>
                </td>
            `;
            elements.p2pConnectionsTableBody.appendChild(row);
        });

        // 绑定操作按钮事件
        bindP2PActionEvents();
    }

    // 绑定P2P连接操作事件
    function bindP2PActionEvents() {
        document.querySelectorAll('.btn-action.btn-edit[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const connectionId = this.getAttribute('data-id');
                editP2PConnection(connectionId);
            });
        });

        document.querySelectorAll('.btn-action.btn-delete[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const connectionId = this.getAttribute('data-id');
                deleteP2PConnection(connectionId);
            });
        });

        document.querySelectorAll('.btn-action.btn-status[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const connectionId = this.getAttribute('data-id');
                const currentStatus = this.getAttribute('data-status');
                updateP2PStatus(connectionId, currentStatus);
            });
        });
    }

    // 更新网络拓扑
    function updateNetworkTopology() {
        if (!elements.topologyGraph) return;

        // 简单的网络拓扑可视化
        elements.topologyGraph.innerHTML = `
            <div class="topology-container">
                <h4>网络拓扑图</h4>
                <div class="nodes-container">
                    ${renderNodes()}
                </div>
                <div class="connections-container">
                    ${renderConnections()}
                </div>
            </div>
        `;
    }

    // 渲染节点
    function renderNodes() {
        let nodesHtml = '';
        let x = 50;
        let y = 50;

        nodes.forEach((node, index) => {
            nodesHtml += `
                <div class="node ${node.status}" style="left: ${x}px; top: ${y}px;">
                    <div class="node-id">${node.nodeId}</div>
                    <div class="node-type">${node.type}</div>
                </div>
            `;

            // 简单的布局
            x += 120;
            if (x > 800) {
                x = 50;
                y += 100;
            }
        });

        return nodesHtml;
    }

    // 渲染连接
    function renderConnections() {
        let connectionsHtml = '';

        p2pConnections.forEach((connection) => {
            if (connection.status === 'active') {
                connectionsHtml += `
                    <div class="connection active">
                        <span>${connection.connectionId}</span>
                    </div>
                `;
            }
        });

        return connectionsHtml;
    }

    // 显示添加P2P连接模态框
    function showAddP2PModal() {
        if (elements.addP2PModal) {
            elements.addP2PModal.classList.add('show');
        }
    }

    // 隐藏添加P2P连接模态框
    function hideAddP2PModal() {
        if (elements.addP2PModal) {
            elements.addP2PModal.classList.remove('show');
        }
        if (elements.addP2PForm) {
            elements.addP2PForm.reset();
        }
    }

    // 添加P2P连接
    function addP2PConnection() {
        if (!elements.addP2PForm) return;

        const formData = new FormData(elements.addP2PForm);
        const connectionId = formData.get('connectionId');
        const localNodeId = formData.get('localNodeId');
        const remoteNodeId = formData.get('remoteNodeId');
        const connectionType = formData.get('connectionType');

        if (!connectionId || !localNodeId || !remoteNodeId) {
            alert('请填写完整的连接信息');
            return;
        }

        const newConnection = {
            connectionId: connectionId,
            localNodeId: localNodeId,
            remoteNodeId: remoteNodeId,
            connectionType: connectionType,
            status: 'active',
            createTime: new Date().toISOString(),
            lastActivityTime: new Date().toISOString(),
            stats: {
                latency: Math.floor(Math.random() * 100) + 10,
                bandwidth: Math.floor(Math.random() * 1000) + 100,
                packetLoss: Math.floor(Math.random() * 10) / 10
            }
        };

        p2pConnections.set(connectionId, newConnection);
        updateP2PConnectionsTable();
        updateNetworkTopology();
        updateStatus();
        hideAddP2PModal();

        console.log('Added new P2P connection:', newConnection);
    }

    // 编辑P2P连接
    function editP2PConnection(connectionId) {
        console.log('Editing P2P connection:', connectionId);
        // 这里可以实现编辑逻辑
        alert(`编辑P2P连接: ${connectionId}`);
    }

    // 删除P2P连接
    function deleteP2PConnection(connectionId) {
        if (confirm(`确定要删除P2P连接 ${connectionId} 吗？`)) {
            p2pConnections.delete(connectionId);
            updateP2PConnectionsTable();
            updateNetworkTopology();
            updateStatus();
            console.log('Deleted P2P connection:', connectionId);
        }
    }

    // 更新P2P连接状态
    function updateP2PStatus(connectionId, currentStatus) {
        const connection = p2pConnections.get(connectionId);
        if (!connection) return;

        // 切换状态
        let newStatus;
        switch (currentStatus) {
            case 'active':
                newStatus = 'disconnected';
                break;
            case 'connecting':
                newStatus = 'active';
                break;
            case 'disconnected':
                newStatus = 'connecting';
                break;
            default:
                newStatus = 'active';
        }

        connection.status = newStatus;
        connection.lastActivityTime = new Date().toISOString();

        p2pConnections.set(connectionId, connection);
        updateP2PConnectionsTable();
        updateNetworkTopology();
        updateStatus();

        console.log(`Updated P2P connection status: ${connectionId} -> ${newStatus}`);
    }

    // 刷新P2P连接数据
    function refreshP2PConnections() {
        console.log('Refreshing P2P connections data');
        // 这里可以实现从服务器加载数据的逻辑
        updateP2PConnectionsTable();
        updateNetworkTopology();
        updateStatus();
        alert('P2P连接数据已刷新');
    }

    // 获取连接类型文本
    function getConnectionTypeText(connectionType) {
        const typeMap = {
            'direct': '直连',
            'relay': '中继',
            'holepunch': '打洞'
        };
        return typeMap[connectionType] || connectionType;
    }

    // 获取状态文本
    function getStatusText(status) {
        const statusMap = {
            'active': '活跃',
            'connecting': '连接中',
            'disconnected': '断开'
        };
        return statusMap[status] || status;
    }

    // 获取状态类名
    function getStatusClass(status) {
        const classMap = {
            'active': 'status-active',
            'connecting': 'status-warning',
            'disconnected': 'status-error'
        };
        return classMap[status] || '';
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
    window.P2PManager = {
        init: init,
        addP2PConnection: addP2PConnection,
        refreshP2PConnections: refreshP2PConnections
    };
})();
