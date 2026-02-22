(function(global) {
    'use strict';

    var routes = [
        { id: 'route-001', name: '主路由', source: 'mcp-agent-001', target: 'route-agent-001', type: 'direct', status: 'active', latency: 10 },
        { id: 'route-002', name: '备用路由', source: 'mcp-agent-001', target: 'route-agent-002', type: 'indirect', status: 'inactive', latency: 15 },
        { id: 'route-003', name: '终端路由', source: 'route-agent-001', target: 'end-agent-001', type: 'direct', status: 'active', latency: 8 }
    ];

    var RouteManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('路由管理页面初始化完成');
                RouteManagement.renderRoutes();
            };
        },

        renderRoutes: function() {
            var tbody = document.getElementById('routeTableBody');
            tbody.innerHTML = '';
            routes.forEach(function(route) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + route.id + '</td>' +
                    '<td>' + route.name + '</td>' +
                    '<td>' + route.source + '</td>' +
                    '<td>' + route.target + '</td>' +
                    '<td>' + RouteManagement.getRouteTypeName(route.type) + '</td>' +
                    '<td><span class="' + (route.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (route.status === 'active' ? '活跃' : '非活跃') + '</span></td>' +
                    '<td>' + route.latency + '</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editRoute(\'' + route.id + '\')">编辑</button> ' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeRoute(\'' + route.id + '\')">删除</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        getRouteTypeName: function(type) {
            var typeMap = {
                'direct': '直接路由',
                'indirect': '间接路由',
                'static': '静态路由',
                'dynamic': '动态路由'
            };
            return typeMap[type] || type;
        },

        showAddRouteModal: function() {
            alert('添加路由功能开发中...');
        },

        editRoute: function(id) {
            alert('编辑路由: ' + id);
        },

        removeRoute: function(id) {
            if (confirm('确定要删除该路由吗？')) {
                routes = routes.filter(function(r) { return r.id !== id; });
                RouteManagement.renderRoutes();
            }
        },

        refreshRoutes: function() {
            RouteManagement.renderRoutes();
        }
    };

    RouteManagement.init();

    global.showAddRouteModal = RouteManagement.showAddRouteModal;
    global.editRoute = RouteManagement.editRoute;
    global.removeRoute = RouteManagement.removeRoute;
    global.refreshRoutes = RouteManagement.refreshRoutes;

})(typeof window !== 'undefined' ? window : this);
