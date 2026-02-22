(function(global) {
    'use strict';

    var networkNodes = [
        { id: 'mcp-001', name: '主Nexus', type: 'mcp', description: '主Nexus节点', status: 'active' },
        { id: 'route-001', name: '路由代理1', type: 'route', description: '网络路由代理', status: 'active' },
        { id: 'end-001', name: '终端代理1', type: 'end', description: '智能终端代理', status: 'active' },
        { id: 'gateway-001', name: '网关', type: 'gateway', description: '网络网关', status: 'active' }
    ];

    var networkConnections = [
        { id: 'conn-001', source: 'mcp-001', target: 'route-001', type: 'direct', status: 'active' },
        { id: 'conn-002', source: 'route-001', target: 'end-001', type: 'direct', status: 'active' },
        { id: 'conn-003', source: 'mcp-001', target: 'gateway-001', type: 'direct', status: 'active' }
    ];

    var NetworkNodes = {
        init: function() {
            var doInit = function() {
                console.log('网络节点管理页面初始化完成');
                NetworkNodes.renderNodes();
                NetworkNodes.renderConnections();
            };
            
            if (window.PageInit && window.PageInit.initialized) {
                doInit();
            } else {
                window.addEventListener('pageInitComplete', doInit);
            }
        },

        renderNodes: function() {
            var tbody = document.getElementById('nodeTableBody');
            tbody.innerHTML = '';
            networkNodes.forEach(function(node) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + node.id + '</td>' +
                    '<td>' + node.name + '</td>' +
                    '<td>' + NetworkNodes.getNodeTypeName(node.type) + '</td>' +
                    '<td>' + node.description + '</td>' +
                    '<td><span class="' + (node.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (node.status === 'active' ? '活跃' : '禁用') + '</span></td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editNode(\'' + node.id + '\')">编辑</button> ' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeNode(\'' + node.id + '\')">移除</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        renderConnections: function() {
            var tbody = document.getElementById('connectionTableBody');
            tbody.innerHTML = '';
            networkConnections.forEach(function(conn) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + conn.id + '</td>' +
                    '<td>' + conn.source + '</td>' +
                    '<td>' + conn.target + '</td>' +
                    '<td>' + (conn.type === 'direct' ? '直接连接' : '间接连接') + '</td>' +
                    '<td><span class="' + (conn.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (conn.status === 'active' ? '活跃' : '断开') + '</span></td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="disconnect(\'' + conn.id + '\')">断开</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        getNodeTypeName: function(type) {
            var typeMap = { mcp: 'Nexus', route: 'Route Agent', end: 'End Agent', gateway: 'Gateway' };
            return typeMap[type] || type;
        },

        showAddNodeModal: function() {
            alert('注册节点功能开发中...');
        },

        editNode: function(id) {
            alert('编辑节点: ' + id);
        },

        removeNode: function(id) {
            if (confirm('确定要移除该节点吗？')) {
                networkNodes = networkNodes.filter(function(n) { return n.id !== id; });
                networkConnections = networkConnections.filter(function(c) { return c.source !== id && c.target !== id; });
                NetworkNodes.renderNodes();
                NetworkNodes.renderConnections();
            }
        },

        disconnect: function(id) {
            if (confirm('确定要断开该连接吗？')) {
                networkConnections = networkConnections.filter(function(c) { return c.id !== id; });
                NetworkNodes.renderConnections();
            }
        },

        refreshNodes: function() {
            NetworkNodes.renderNodes();
            NetworkNodes.renderConnections();
        }
    };

    NetworkNodes.init();

    global.showAddNodeModal = NetworkNodes.showAddNodeModal;
    global.editNode = NetworkNodes.editNode;
    global.removeNode = NetworkNodes.removeNode;
    global.disconnect = NetworkNodes.disconnect;
    global.refreshNodes = NetworkNodes.refreshNodes;

})(typeof window !== 'undefined' ? window : this);
