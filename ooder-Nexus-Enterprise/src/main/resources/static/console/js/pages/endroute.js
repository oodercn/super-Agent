(function(global) {
    'use strict';

    var routes = [
        { id: 'route-001', status: 'active', registeredTime: '2024-01-01', lastHeartbeat: '2024-01-10 10:00' },
        { id: 'route-002', status: 'active', registeredTime: '2024-01-02', lastHeartbeat: '2024-01-10 10:00' }
    ];

    var endAgents = [
        { id: 'end-001', status: 'active', createdTime: '2024-01-01', lastHeartbeat: '2024-01-10 10:00' },
        { id: 'end-002', status: 'active', createdTime: '2024-01-02', lastHeartbeat: '2024-01-10 10:00' },
        { id: 'end-003', status: 'inactive', createdTime: '2024-01-03', lastHeartbeat: '2024-01-09 08:00' }
    ];

    var EndRoute = {
        init: function() {
            window.onPageInit = function() {
                console.log('EndRoute管理页面初始化完成');
                EndRoute.renderRoutes();
                EndRoute.renderEndAgents();
                EndRoute.updateStats();
            };
        },

        renderRoutes: function() {
            var tbody = document.getElementById('routesTableBody');
            tbody.innerHTML = '';
            routes.forEach(function(route) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + route.id + '</td>' +
                    '<td><span class="' + (route.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (route.status === 'active' ? '活跃' : '非活跃') + '</span></td>' +
                    '<td>' + route.registeredTime + '</td>' +
                    '<td>' + route.lastHeartbeat + '</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editRoute(\'' + route.id + '\')">编辑</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        renderEndAgents: function() {
            var tbody = document.getElementById('endAgentsTableBody');
            tbody.innerHTML = '';
            endAgents.forEach(function(agent) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + agent.id + '</td>' +
                    '<td><span class="' + (agent.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (agent.status === 'active' ? '活跃' : '离线') + '</span></td>' +
                    '<td>' + agent.createdTime + '</td>' +
                    '<td>' + agent.lastHeartbeat + '</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editEndAgent(\'' + agent.id + '\')">编辑</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        updateStats: function() {
            document.getElementById('routeCount').textContent = routes.length;
            document.getElementById('endAgentCount').textContent = endAgents.length;
        },

        refreshRoutes: function() {
            EndRoute.renderRoutes();
        },

        refreshEndAgents: function() {
            EndRoute.renderEndAgents();
        },

        editRoute: function(id) {
            var route = routes.find(function(r) { return r.id === id; });
            if (route) {
                document.getElementById('edit-route-id').value = route.id;
                document.getElementById('edit-route-id-display').value = route.id;
                document.getElementById('edit-route-status').value = route.status;
                document.getElementById('edit-route-modal').style.display = 'flex';
            }
        },

        closeEditRouteModal: function() {
            document.getElementById('edit-route-modal').style.display = 'none';
        },

        submitEditRoute: function() {
            var id = document.getElementById('edit-route-id').value;
            var status = document.getElementById('edit-route-status').value;

            var route = routes.find(function(r) { return r.id === id; });
            if (route) {
                route.status = status;
                EndRoute.closeEditRouteModal();
                EndRoute.renderRoutes();
            }
        },

        editEndAgent: function(id) {
            var agent = endAgents.find(function(a) { return a.id === id; });
            if (agent) {
                document.getElementById('edit-agent-id').value = agent.id;
                document.getElementById('edit-agent-id-display').value = agent.id;
                document.getElementById('edit-agent-status').value = agent.status;
                document.getElementById('edit-agent-modal').style.display = 'flex';
            }
        },

        closeEditAgentModal: function() {
            document.getElementById('edit-agent-modal').style.display = 'none';
        },

        submitEditAgent: function() {
            var id = document.getElementById('edit-agent-id').value;
            var status = document.getElementById('edit-agent-status').value;

            var agent = endAgents.find(function(a) { return a.id === id; });
            if (agent) {
                agent.status = status;
                EndRoute.closeEditAgentModal();
                EndRoute.renderEndAgents();
            }
        }
    };

    EndRoute.init();

    global.refreshRoutes = EndRoute.refreshRoutes;
    global.refreshEndAgents = EndRoute.refreshEndAgents;
    global.editRoute = EndRoute.editRoute;
    global.closeEditRouteModal = EndRoute.closeEditRouteModal;
    global.submitEditRoute = EndRoute.submitEditRoute;
    global.editEndAgent = EndRoute.editEndAgent;
    global.closeEditAgentModal = EndRoute.closeEditAgentModal;
    global.submitEditAgent = EndRoute.submitEditAgent;

})(typeof window !== 'undefined' ? window : this);
