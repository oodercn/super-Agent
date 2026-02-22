(function(global) {
    'use strict';

    var capabilities = [
        { id: 'network-topology', name: '网络拓扑分析', description: '分析网络拓扑结构，生成网络地图', registeredTime: '2024-01-01', status: 'active' },
        { id: 'link-management', name: '链路管理', description: '管理网络链路的创建和断开', registeredTime: '2024-01-02', status: 'active' },
        { id: 'endagent-discovery', name: '终端发现', description: '发现网络中的终端设备', registeredTime: '2024-01-03', status: 'active' },
        { id: 'route-discovery', name: '路由发现', description: '发现网络中的路由路径', registeredTime: '2024-01-04', status: 'active' }
    ];

    var CapabilityManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('能力管理页面初始化完成');
                CapabilityManagement.renderCapabilities();
            };
        },

        renderCapabilities: function() {
            var tbody = document.getElementById('capabilityTableBody');
            tbody.innerHTML = '';
            capabilities.forEach(function(capability) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + capability.id + '</td>' +
                    '<td>' + capability.name + '</td>' +
                    '<td>' + capability.description + '</td>' +
                    '<td>' + capability.registeredTime + '</td>' +
                    '<td><span class="' + (capability.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (capability.status === 'active' ? '活跃' : '禁用') + '</span></td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="invokeCapability(\'' + capability.id + '\')">调用</button> ' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeCapability(\'' + capability.id + '\')">移除</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        registerCapability: function() {
            alert('注册能力功能开发中...');
        },

        invokeCapability: function(id) {
            var capability = capabilities.find(function(c) { return c.id === id; });
            if (capability) {
                var result = {
                    status: 'success',
                    capabilityId: id,
                    result: '能力 ' + capability.name + ' 调用成功',
                    timestamp: new Date().toISOString()
                };
                document.getElementById('capabilityResult').textContent = JSON.stringify(result, null, 2);
            }
        },

        removeCapability: function(id) {
            if (confirm('确定要移除该能力吗？')) {
                capabilities = capabilities.filter(function(c) { return c.id !== id; });
                CapabilityManagement.renderCapabilities();
            }
        },

        refreshCapabilities: function() {
            CapabilityManagement.renderCapabilities();
        }
    };

    CapabilityManagement.init();

    global.registerCapability = CapabilityManagement.registerCapability;
    global.invokeCapability = CapabilityManagement.invokeCapability;
    global.removeCapability = CapabilityManagement.removeCapability;
    global.refreshCapabilities = CapabilityManagement.refreshCapabilities;

})(typeof window !== 'undefined' ? window : this);
