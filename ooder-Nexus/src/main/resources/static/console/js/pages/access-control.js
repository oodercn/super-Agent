(function(global) {
    'use strict';

    var AccessControl = {
        rules: [],
        editingId: null,

        init: function() {
            window.onPageInit = function() {
                AccessControl.loadRules();
            };
        },

        loadRules: function() {
            fetch('/api/firewall/rules', { method: 'POST' })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200 && rs.data) {
                        AccessControl.rules = rs.data;
                        AccessControl.renderRules();
                        AccessControl.updateStats();
                    }
                })
                .catch(function(error) {
                    console.error('加载规则列表失败:', error);
                });
        },

        updateStats: function() {
            var total = AccessControl.rules.length;
            var active = AccessControl.rules.filter(function(r) {
                return r.status === 'active' || r.enabled;
            }).length;
            var inactive = total - active;
            document.querySelector('.rule-stats').innerHTML = 
                '<div class="rule-stat-card">' +
                    '<div class="rule-stat-value">' + total + '</div>' +
                    '<div class="rule-stat-label">总规则数</div>' +
                '</div>' +
                '<div class="rule-stat-card">' +
                    '<div class="rule-stat-value" style="color: var(--ns-success);">' + active + '</div>' +
                    '<div class="rule-stat-label">已启用</div>' +
                '</div>' +
                '<div class="rule-stat-card">' +
                    '<div class="rule-stat-value" style="color: var(--ns-warning);">' + inactive + '</div>' +
                    '<div class="rule-stat-label">已禁用</div>' +
                '</div>' +
                '<div class="rule-stat-card">' +
                    '<div class="rule-stat-value" style="color: var(--ns-danger);">0</div>' +
                    '<div class="rule-stat-label">拦截次数</div>' +
                '</div>';
        },

        renderRules: function() {
            var tbody = document.querySelector('table tbody');
            var html = '';
            AccessControl.rules.forEach(function(r) {
                var actionClass = r.action === 'allow' ? 'badge-success' : 'badge-danger';
                var actionText = r.action === 'allow' ? '允许' : '阻止';
                var statusClass = (r.status === 'active' || r.enabled) ? 'badge-success' : 'badge-secondary';
                var statusText = (r.status === 'active' || r.enabled) ? '已启用' : '已禁用';
                var toggleText = (r.status === 'active' || r.enabled) ? '禁用' : '启用';
                
                html += '<tr>' +
                    '<td>' + (r.name || r.ruleName || '-') + '</td>' +
                    '<td><span class="badge ' + actionClass + '">' + actionText + '</span></td>' +
                    '<td>' + (r.protocol || '全部') + '</td>' +
                    '<td>' + (r.sourceIp || r.source || '任何') + '</td>' +
                    '<td>' + (r.destPort || r.port || '任何') + '</td>' +
                    '<td><span class="badge ' + statusClass + '">' + statusText + '</span></td>' +
                    '<td>' + (r.description || '-') + '</td>' +
                    '<td>' +
                        '<button class="btn btn-sm btn-secondary" onclick="toggleRule(\'' + r.id + '\')">' + toggleText + '</button> ' +
                        '<button class="btn btn-sm btn-secondary" onclick="editRule(\'' + r.id + '\')">编辑</button> ' +
                        '<button class="btn btn-sm btn-danger" onclick="deleteRule(\'' + r.id + '\')">删除</button>' +
                    '</td>' +
                '</tr>';
            });
            tbody.innerHTML = html;
        },

        showAddRule: function() {
            AccessControl.editingId = null;
            document.getElementById('ruleModalTitle').textContent = '添加规则';
            document.getElementById('ruleForm').reset();
            document.getElementById('ruleModal').style.display = 'flex';
        },

        editRule: function(id) {
            AccessControl.editingId = id;
            var rule = AccessControl.rules.find(function(r) {
                return r.id === id;
            });
            if (rule) {
                document.getElementById('ruleModalTitle').textContent = '编辑规则';
                document.getElementById('ruleName').value = rule.name || rule.ruleName || '';
                document.getElementById('ruleAction').value = rule.action || 'allow';
                document.getElementById('ruleProtocol').value = rule.protocol || 'all';
                document.getElementById('ruleSourceIp').value = rule.sourceIp || rule.source || '';
                document.getElementById('ruleDestPort').value = rule.destPort || rule.port || '';
                document.getElementById('ruleDescription').value = rule.description || '';
                document.getElementById('ruleModal').style.display = 'flex';
            }
        },

        closeRuleModal: function() {
            document.getElementById('ruleModal').style.display = 'none';
        },

        saveRule: function() {
            var data = {
                name: document.getElementById('ruleName').value,
                action: document.getElementById('ruleAction').value,
                protocol: document.getElementById('ruleProtocol').value,
                sourceIp: document.getElementById('ruleSourceIp').value,
                destPort: document.getElementById('ruleDestPort').value,
                description: document.getElementById('ruleDescription').value
            };

            if (!data.name) {
                alert('请填写规则名称');
                return;
            }

            var url = AccessControl.editingId ? '/api/firewall/rules/update' : '/api/firewall/rules/add';
            if (AccessControl.editingId) {
                data.id = AccessControl.editingId;
            }

            fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        AccessControl.closeRuleModal();
                        AccessControl.loadRules();
                        alert(AccessControl.editingId ? '规则更新成功' : '规则添加成功');
                    } else {
                        alert(rs.message || '操作失败');
                    }
                })
                .catch(function(error) {
                    console.error('保存规则失败:', error);
                    alert('保存规则失败');
                });
        },

        toggleRule: function(id) {
            var rule = AccessControl.rules.find(function(r) {
                return r.id === id;
            });
            if (!rule) return;

            fetch('/api/firewall/rules/toggle', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: id })
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        AccessControl.loadRules();
                    } else {
                        alert(rs.message || '操作失败');
                    }
                })
                .catch(function(error) {
                    console.error('切换规则状态失败:', error);
                    alert('切换规则状态失败');
                });
        },

        deleteRule: function(id) {
            if (!confirm('确定要删除此规则吗？')) return;

            fetch('/api/firewall/rules/delete', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: id })
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(rs) {
                    if (rs.requestStatus === 200) {
                        AccessControl.loadRules();
                        alert('规则删除成功');
                    } else {
                        alert(rs.message || '删除失败');
                    }
                })
                .catch(function(error) {
                    console.error('删除规则失败:', error);
                    alert('删除规则失败');
                });
        }
    };

    AccessControl.init();

    global.showAddRule = AccessControl.showAddRule;
    global.editRule = AccessControl.editRule;
    global.closeRuleModal = AccessControl.closeRuleModal;
    global.saveRule = AccessControl.saveRule;
    global.toggleRule = AccessControl.toggleRule;
    global.deleteRule = AccessControl.deleteRule;

})(typeof window !== 'undefined' ? window : this);
