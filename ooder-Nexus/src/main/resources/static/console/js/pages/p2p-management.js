(function(global) {
    'use strict';

    var p2pConnections = [
        { id: 'conn-001', localNode: 'mcp-agent-001', remoteNode: 'route-agent-001', type: 'direct', status: 'active' },
        { id: 'conn-002', localNode: 'mcp-agent-001', remoteNode: 'route-agent-002', type: 'direct', status: 'active' },
        { id: 'conn-003', localNode: 'route-agent-001', remoteNode: 'end-agent-001', type: 'direct', status: 'active' }
    ];

    var P2PManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('P2P管理页面初始化完成');
                P2PManagement.renderP2PConnections();
                P2PManagement.updateStatusOverview();
            };
        },

        renderP2PConnections: function() {
            var tbody = document.getElementById('p2pConnectionsTableBody');
            tbody.innerHTML = '';
            p2pConnections.forEach(function(conn) {
                var row = document.createElement('tr');
                row.innerHTML = '\
                    <td>' + conn.id + '</td>\
                    <td>' + conn.localNode + '</td>\
                    <td>' + conn.remoteNode + '</td>\
                    <td>' + (conn.type === 'direct' ? '直连' : '中继') + '</td>\
                    <td><span class="' + (conn.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (conn.status === 'active' ? '活跃' : '非活跃') + '</span></td>\
                    <td>\
                        <button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editConnection(\'' + conn.id + '\')">编辑</button>\
                        <button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeConnection(\'' + conn.id + '\')">删除</button>\
                    </td>\
                ';
                tbody.appendChild(row);
            });
        },

        updateStatusOverview: function() {
            document.getElementById('p2pConnectionCount').textContent = p2pConnections.length;
            var nodes = new Set();
            p2pConnections.forEach(function(conn) {
                nodes.add(conn.localNode);
                nodes.add(conn.remoteNode);
            });
            document.getElementById('nodeCount').textContent = nodes.size;
        },

        addP2PConnection: function() {
            alert('添加P2P连接功能开发中...');
        },

        editConnection: function(id) {
            alert('编辑连接: ' + id);
        },

        removeConnection: function(id) {
            if (confirm('确定要删除该连接吗？')) {
                p2pConnections = p2pConnections.filter(function(c) { return c.id !== id; });
                P2PManagement.renderP2PConnections();
                P2PManagement.updateStatusOverview();
            }
        },

        refreshP2PConnections: function() {
            P2PManagement.renderP2PConnections();
        }
    };

    P2PManagement.init();

    global.addP2PConnection = P2PManagement.addP2PConnection;
    global.editConnection = P2PManagement.editConnection;
    global.removeConnection = P2PManagement.removeConnection;
    global.refreshP2PConnections = P2PManagement.refreshP2PConnections;
    global.P2PManagement = P2PManagement;

})(typeof window !== 'undefined' ? window : this);
