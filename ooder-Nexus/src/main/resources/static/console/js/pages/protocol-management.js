(function(global) {
    'use strict';

    var protocolHandlers = [
        { commandType: 'MCP_REGISTER', name: '注册命令处理器', description: '处理Nexus注册命令', status: 'active' },
        { commandType: 'MCP_DEREGISTER', name: '注销命令处理器', description: '处理Nexus注销命令', status: 'active' },
        { commandType: 'MCP_HEARTBEAT', name: '心跳命令处理器', description: '处理Nexus心跳命令', status: 'active' },
        { commandType: 'MCP_STATUS', name: '状态命令处理器', description: '处理Nexus状态命令', status: 'active' }
    ];

    var ProtocolManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('协议管理页面初始化完成');
                ProtocolManagement.renderProtocolHandlers();
            };
        },

        renderProtocolHandlers: function() {
            var tbody = document.getElementById('protocolTableBody');
            tbody.innerHTML = '';
            protocolHandlers.forEach(function(handler) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + handler.commandType + '</td>' +
                    '<td>' + handler.name + '</td>' +
                    '<td>' + handler.description + '</td>' +
                    '<td><span class="' + (handler.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (handler.status === 'active' ? '活跃' : '禁用') + '</span></td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editHandler(\'' + handler.commandType + '\')">编辑</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        editHandler: function(commandType) {
            alert('编辑处理器: ' + commandType);
        },

        refreshProtocolHandlers: function() {
            ProtocolManagement.renderProtocolHandlers();
        }
    };

    ProtocolManagement.init();

    global.editHandler = ProtocolManagement.editHandler;
    global.refreshProtocolHandlers = ProtocolManagement.refreshProtocolHandlers;

})(typeof window !== 'undefined' ? window : this);
