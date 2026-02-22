(function(global) {
    'use strict';

    var endAgents = [
        { id: 'end-001', name: '智能灯泡1', type: 'light', ip: '192.168.1.100', mac: 'AA:BB:CC:DD:EE:FF', status: 'active' },
        { id: 'end-002', name: '智能插座1', type: 'socket', ip: '192.168.1.101', mac: 'AA:BB:CC:DD:EE:GG', status: 'active' },
        { id: 'end-003', name: '摄像头1', type: 'camera', ip: '192.168.1.102', mac: 'AA:BB:CC:DD:EE:HH', status: 'inactive' },
        { id: 'end-004', name: '智能音箱1', type: 'speaker', ip: '192.168.1.103', mac: 'AA:BB:CC:DD:EE:II', status: 'active' },
        { id: 'end-005', name: '温控器1', type: 'thermostat', ip: '192.168.1.104', mac: 'AA:BB:CC:DD:EE:JJ', status: 'active' }
    ];

    var EndAgentManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('终端代理管理页面初始化完成');
                EndAgentManagement.renderEndAgents();
            };
        },

        renderEndAgents: function() {
            var tbody = document.getElementById('endAgentTableBody');
            tbody.innerHTML = '';
            endAgents.forEach(function(agent) {
                var row = document.createElement('tr');
                row.innerHTML = '\
                    <td>' + agent.id + '</td>\
                    <td>' + agent.name + '</td>\
                    <td>' + EndAgentManagement.getTypeName(agent.type) + '</td>\
                    <td>' + agent.ip + '</td>\
                    <td>' + agent.mac + '</td>\
                    <td><span class="' + (agent.status === 'active' ? 'nx-text-success' : 'nx-text-danger') + '">' + (agent.status === 'active' ? '活跃' : '离线') + '</span></td>\
                    <td>\
                        <button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="editAgent(\'' + agent.id + '\')">编辑</button>\
                        <button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeAgent(\'' + agent.id + '\')">移除</button>\
                    </td>\
                ';
                tbody.appendChild(row);
            });
        },

        getTypeName: function(type) {
            var types = { light: '智能灯泡', socket: '智能插座', camera: '摄像头', speaker: '智能音箱', thermostat: '温控器', doorlock: '智能门锁' };
            return types[type] || type;
        },

        addEndAgent: function() {
            alert('添加终端功能开发中...');
        },

        editAgent: function(id) {
            alert('编辑终端: ' + id);
        },

        removeAgent: function(id) {
            if (confirm('确定要移除该终端吗？')) {
                endAgents = endAgents.filter(function(a) { return a.id !== id; });
                EndAgentManagement.renderEndAgents();
            }
        },

        refreshEndAgents: function() {
            EndAgentManagement.renderEndAgents();
        }
    };

    EndAgentManagement.init();

    global.addEndAgent = EndAgentManagement.addEndAgent;
    global.editAgent = EndAgentManagement.editAgent;
    global.removeAgent = EndAgentManagement.removeAgent;
    global.refreshEndAgents = EndAgentManagement.refreshEndAgents;
    global.EndAgentManagement = EndAgentManagement;

})(typeof window !== 'undefined' ? window : this);
