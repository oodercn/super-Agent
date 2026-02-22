(function(global) {
    'use strict';

    var scenarios = [];

    var ScenarioManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('场景管理页面初始化完成');
                ScenarioManagement.loadScenarios();
            };
        },

        loadScenarios: async function() {
            try {
                var response = await fetch('/api/scene/groups/list', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({})
                });
                var result = await response.json();
                
                if (result.requestStatus === 200 && result.data) {
                    scenarios = result.data;
                    ScenarioManagement.renderScenarios(scenarios);
                    ScenarioManagement.updateStatusOverview(scenarios);
                } else {
                    console.warn('使用模拟数据');
                    ScenarioManagement.loadMockData();
                }
            } catch (error) {
                console.error('加载场景数据失败:', error);
                ScenarioManagement.loadMockData();
            }
        },

        loadMockData: function() {
            scenarios = [
                { id: 'scenario-001', name: '企业网络场景', type: 'enterprise', status: 'active', heartbeatStarted: true, createdAt: '2026-02-01', memberCount: 5 },
                { id: 'scenario-002', name: '智能家居场景', type: 'personal', status: 'active', heartbeatStarted: false, createdAt: '2026-02-05', memberCount: 3 },
                { id: 'scenario-003', name: '测试网络场景', type: 'test', status: 'inactive', heartbeatStarted: false, createdAt: '2026-02-10', memberCount: 2 }
            ];
            ScenarioManagement.renderScenarios(scenarios);
            ScenarioManagement.updateStatusOverview(scenarios);
        },

        renderScenarios: function(scenarios) {
            var tbody = document.getElementById('scenarioTableBody');
            tbody.innerHTML = '';
            
            scenarios.forEach(function(scenario) {
                var statusClass = scenario.status === 'active' ? 'nx-text-success' : 'nx-text-danger';
                var statusText = scenario.status === 'active' ? '活跃' : '非活跃';
                var heartbeatClass = scenario.heartbeatStarted ? 'nx-badge--success' : 'nx-badge--secondary';
                var heartbeatText = scenario.heartbeatStarted ? '运行中' : '已停止';
                
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + scenario.name + '</td>' +
                    '<td>' + ScenarioManagement.getScenarioTypeName(scenario.type) + '</td>' +
                    '<td><span class="' + statusClass + '">' + statusText + '</span></td>' +
                    '<td><span class="nx-badge ' + heartbeatClass + '">' + heartbeatText + '</span></td>' +
                    '<td>' + (scenario.memberCount || 0) + '</td>' +
                    '<td>' + scenario.createdAt + '</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="viewScenarioDetail(\'' + scenario.id + '\')">详情</button> ' +
                    '<button class="nx-btn nx-btn--sm ' + (scenario.heartbeatStarted ? 'nx-btn--danger' : 'nx-btn--success') + '" onclick="toggleHeartbeat(\'' + scenario.id + '\')">' +
                    (scenario.heartbeatStarted ? '停止心跳' : '启动心跳') + '</button> ' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="deleteScenario(\'' + scenario.id + '\')">删除</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        getScenarioTypeName: function(type) {
            var typeMap = {
                'enterprise': '企业网络',
                'personal': '个人网络',
                'test': '测试网络',
                'development': '开发环境'
            };
            return typeMap[type] || type;
        },

        updateStatusOverview: function(scenarios) {
            document.getElementById('totalScenarios').textContent = scenarios.length;
            var activeCount = 0;
            var types = {};
            scenarios.forEach(function(s) {
                if (s.status === 'active') activeCount++;
                types[s.type] = true;
            });
            document.getElementById('activeScenarios').textContent = activeCount;
            document.getElementById('scenarioTypes').textContent = Object.keys(types).length;
        },

        viewScenarioDetail: async function(scenarioId) {
            try {
                var response = await fetch('/api/scene/heartbeat/status/' + scenarioId);
                var result = await response.json();
                
                if (result.requestStatus === 200 && result.data) {
                    var data = result.data;
                    alert('场景详情:\n' +
                        'ID: ' + scenarioId + '\n' +
                        '心跳状态: ' + (data.heartbeatStarted ? '运行中' : '已停止') + '\n' +
                        '时间戳: ' + new Date(data.timestamp).toLocaleString());
                } else {
                    var scenario = scenarios.find(function(s) { return s.id === scenarioId; });
                    if (scenario) {
                        alert('场景详情:\n' +
                            'ID: ' + scenario.id + '\n' +
                            '名称: ' + scenario.name + '\n' +
                            '类型: ' + ScenarioManagement.getScenarioTypeName(scenario.type) + '\n' +
                            '状态: ' + (scenario.status === 'active' ? '活跃' : '非活跃') + '\n' +
                            '心跳: ' + (scenario.heartbeatStarted ? '运行中' : '已停止') + '\n' +
                            '成员数: ' + (scenario.memberCount || 0));
                    }
                }
            } catch (error) {
                console.error('获取场景详情失败:', error);
            }
        },

        toggleHeartbeat: async function(scenarioId) {
            var scenario = scenarios.find(function(s) { return s.id === scenarioId; });
            if (!scenario) return;
            
            var action = scenario.heartbeatStarted ? 'stop' : 'start';
            var url = '/api/scene/heartbeat/' + action + '/' + scenarioId;
            
            try {
                var response = await fetch(url, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                });
                
                var result = await response.json();
                if (result.requestStatus === 200) {
                    scenario.heartbeatStarted = !scenario.heartbeatStarted;
                    ScenarioManagement.renderScenarios(scenarios);
                    ScenarioManagement.showSuccess(action === 'start' ? '心跳已启动' : '心跳已停止');
                } else {
                    ScenarioManagement.showError(result.message || '操作失败');
                }
            } catch (error) {
                console.error('切换心跳状态失败:', error);
                scenario.heartbeatStarted = !scenario.heartbeatStarted;
                ScenarioManagement.renderScenarios(scenarios);
                ScenarioManagement.showSuccess(action === 'start' ? '心跳已启动(模拟)' : '心跳已停止(模拟)');
            }
        },

        sendHeartbeat: async function(scenarioId) {
            try {
                var response = await fetch('/api/scene/heartbeat/send/' + scenarioId, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                });
                
                var result = await response.json();
                if (result.requestStatus === 200) {
                    ScenarioManagement.showSuccess('心跳已发送');
                } else {
                    ScenarioManagement.showError(result.message || '发送失败');
                }
            } catch (error) {
                console.error('发送心跳失败:', error);
                ScenarioManagement.showSuccess('心跳已发送(模拟)');
            }
        },

        viewMembers: async function(scenarioId) {
            try {
                var response = await fetch('/api/scene/heartbeat/members/' + scenarioId);
                var result = await response.json();
                
                if (result.requestStatus === 200 && result.data) {
                    var members = result.data.members || [];
                    var message = '成员列表:\n';
                    members.forEach(function(m) {
                        message += '- ' + m.memberId + ' (' + m.status + ')\n';
                    });
                    alert(message || '暂无成员');
                }
            } catch (error) {
                console.error('获取成员列表失败:', error);
                alert('获取成员列表失败');
            }
        },

        createScenario: function() {
            var name = prompt('请输入场景名称:');
            var type = prompt('请输入场景类型 (enterprise/personal/test/development):');
            
            if (name && type) {
                var newScenario = {
                    id: 'scenario-' + Date.now(),
                    name: name,
                    type: type,
                    status: 'active',
                    heartbeatStarted: false,
                    createdAt: new Date().toISOString().split('T')[0],
                    memberCount: 0
                };
                scenarios.push(newScenario);
                ScenarioManagement.renderScenarios(scenarios);
                ScenarioManagement.updateStatusOverview(scenarios);
                ScenarioManagement.showSuccess('场景创建成功');
            }
        },

        editScenario: function(id) {
            var scenario = scenarios.find(function(s) { return s.id === id; });
            if (!scenario) return;
            
            var newName = prompt('请输入新名称:', scenario.name);
            if (newName) {
                scenario.name = newName;
                ScenarioManagement.renderScenarios(scenarios);
                ScenarioManagement.showSuccess('场景已更新');
            }
        },

        deleteScenario: function(id) {
            if (!confirm('确定要删除该场景吗？')) return;
            
            scenarios = scenarios.filter(function(s) { return s.id !== id; });
            ScenarioManagement.renderScenarios(scenarios);
            ScenarioManagement.updateStatusOverview(scenarios);
            ScenarioManagement.showSuccess('场景已删除');
        },

        refreshScenarios: function() {
            ScenarioManagement.loadScenarios();
        },

        showSuccess: function(message) {
            alert(message);
        },

        showError: function(message) {
            alert('错误: ' + message);
        }
    };

    ScenarioManagement.init();

    global.createScenario = ScenarioManagement.createScenario;
    global.editScenario = ScenarioManagement.editScenario;
    global.deleteScenario = ScenarioManagement.deleteScenario;
    global.refreshScenarios = ScenarioManagement.refreshScenarios;
    global.viewScenarioDetail = ScenarioManagement.viewScenarioDetail;
    global.toggleHeartbeat = ScenarioManagement.toggleHeartbeat;
    global.sendHeartbeat = ScenarioManagement.sendHeartbeat;
    global.viewMembers = ScenarioManagement.viewMembers;

})(typeof window !== 'undefined' ? window : this);
