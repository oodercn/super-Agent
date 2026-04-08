(function() {
    'use strict';

    var AgentList = {
        allAgents: [],
        bindingCounts: {},
        searchKeyword: '',
        typeFilter: '',
        statusFilter: '',
        
        init: function() {
            this.initFilters();
            this.loadAgents();
        },
        
        loadAgents: function() {
            var self = this;
            fetch('/api/agent/list')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.allAgents = result.data;
                        self.loadBindingCounts();
                    } else {
                        self.showEmpty();
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load agents:', e);
                    self.showError();
                });
        },
        
        loadBindingCounts: function() {
            var self = this;
            fetch('/api/v1/capabilities/bindings')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.bindingCounts = {};
                        result.data.forEach(function(binding) {
                            if (binding.agentId) {
                                self.bindingCounts[binding.agentId] = (self.bindingCounts[binding.agentId] || 0) + 1;
                            }
                        });
                        self.updateStats();
                        self.renderAgents();
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load binding counts:', e);
                    self.updateStats();
                    self.renderAgents();
                });
        },
        
        updateStats: function() {
            var stats = {
                total: this.allAgents.length,
                online: 0,
                busy: 0,
                offline: 0
            };
            
            this.allAgents.forEach(function(agent) {
                var status = (agent.status || 'offline').toLowerCase();
                if (status === 'online' || status === 'running') stats.online++;
                else if (status === 'busy') stats.busy++;
                else stats.offline++;
            });
            
            document.getElementById('stat-total').textContent = stats.total;
            document.getElementById('stat-online').textContent = stats.online;
            document.getElementById('stat-busy').textContent = stats.busy;
            document.getElementById('stat-offline').textContent = stats.offline;
        },
        
        filterAgents: function() {
            var self = this;
            return this.allAgents.filter(function(agent) {
                if (self.typeFilter && agent.agentType !== self.typeFilter) return false;
                if (self.statusFilter) {
                    var status = (agent.status || 'offline').toLowerCase();
                    if (self.statusFilter === 'online' && status !== 'online' && status !== 'running') return false;
                    if (self.statusFilter === 'offline' && status !== 'offline') return false;
                    if (self.statusFilter === 'busy' && status !== 'busy') return false;
                    if (self.statusFilter === 'error' && status !== 'error') return false;
                }
                if (self.searchKeyword) {
                    var keyword = self.searchKeyword.toLowerCase();
                    var name = (agent.agentName || agent.name || '').toLowerCase();
                    var id = (agent.agentId || '').toLowerCase();
                    var ip = (agent.ipAddress || '').toLowerCase();
                    return name.indexOf(keyword) >= 0 || id.indexOf(keyword) >= 0 || ip.indexOf(keyword) >= 0;
                }
                return true;
            });
        },
        
        renderAgents: function() {
            var filtered = this.filterAgents();
            var container = document.getElementById('agent-list');
            
            if (filtered.length === 0) {
                container.innerHTML = '<tr><td colspan="7"><div class="agents-empty-state"><i class="ri-robot-line"></i><h3>暂无 Agent</h3><p>没有找到匹配的 Agent</p></div></td></tr>';
                return;
            }
            
            var self = this;
            container.innerHTML = filtered.map(function(agent) { return self.renderAgentRow(agent); }).join('');
        },
        
        renderAgentRow: function(agent) {
            var agentId = agent.agentId || agent.id;
            var name = agent.agentName || agent.name || '未命名';
            var type = agent.agentType || agent.type || 'UNKNOWN';
            var status = (agent.status || 'offline').toLowerCase();
            var ip = agent.ipAddress || '-';
            var port = agent.port || '';
            var lastHeartbeat = agent.lastHeartbeat ? this.formatTime(agent.lastHeartbeat) : '-';
            var bindingCount = this.bindingCounts[agentId] || 0;
            
            var statusClass = status === 'online' || status === 'running' ? 'online' : 
                status === 'busy' ? 'busy' : 
                status === 'error' ? 'error' : 'offline';
            var statusText = status === 'online' || status === 'running' ? '在线' :
                status === 'busy' ? '忙碌' :
                status === 'error' ? '错误' : '离线';
            
            return '<tr>' +
                '<td><div class="agent-name"><div class="agent-icon"><i class="ri-robot-line"></i></div><div class="agent-info"><h4>' + name + '</h4><span>' + agentId + '</span></div></div></td>' +
                '<td><span class="agent-type-badge">' + type + '</span></td>' +
                '<td><span class="agent-status-badge ' + statusClass + '"><i class="ri-checkbox-blank-circle-fill"></i> ' + statusText + '</span></td>' +
                '<td>' + ip + (port ? ':' + port : '') + '</td>' +
                '<td><span class="agent-binding-count ' + (bindingCount > 0 ? 'has-bindings' : '') + '" onclick="AgentList.showAgentBindings(\'' + agentId + '\')"><i class="ri-link"></i> ' + bindingCount + '</span></td>' +
                '<td>' + lastHeartbeat + '</td>' +
                '<td><div class="agent-action-btns">' +
                    '<button class="agent-action-btn" onclick="AgentList.showAgentDetail(\'' + agentId + '\')" title="查看详情"><i class="ri-eye-line"></i></button>' +
                    '<button class="agent-action-btn" onclick="AgentList.sendHeartbeat(\'' + agentId + '\')" title="发送心跳"><i class="ri-heart-pulse-line"></i></button>' +
                    '<button class="agent-action-btn danger" onclick="AgentList.deleteAgent(\'' + agentId + '\')" title="删除"><i class="ri-delete-bin-line"></i></button>' +
                '</div></td>' +
            '</tr>';
        },
        
        formatTime: function(timestamp) {
            if (!timestamp) return '-';
            var date = new Date(timestamp);
            var now = new Date();
            var diff = now - date;
            
            if (diff < 60000) return '刚刚';
            if (diff < 3600000) return Math.floor(diff / 60000) + ' 分钟前';
            if (diff < 86400000) return Math.floor(diff / 3600000) + ' 小时前';
            return date.toLocaleDateString();
        },
        
        showAgentDetail: function(agentId) {
            var self = this;
            fetch('/api/agent/' + agentId)
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        var agent = result.data;
                        var bindingCount = self.bindingCounts[agentId] || 0;
                        
                        var html = '<div class="agent-detail-row"><div class="agent-detail-label">Agent ID</div><div class="agent-detail-value">' + (agent.agentId || agentId) + '</div></div>' +
                            '<div class="agent-detail-row"><div class="agent-detail-label">名称</div><div class="agent-detail-value">' + (agent.agentName || agent.name || '-') + '</div></div>' +
                            '<div class="agent-detail-row"><div class="agent-detail-label">类型</div><div class="agent-detail-value">' + (agent.agentType || agent.type || '-') + '</div></div>' +
                            '<div class="agent-detail-row"><div class="agent-detail-label">状态</div><div class="agent-detail-value">' + (agent.status || '-') + '</div></div>' +
                            '<div class="agent-detail-row"><div class="agent-detail-label">IP 地址</div><div class="agent-detail-value">' + (agent.ipAddress || '-') + (agent.port ? ':' + agent.port : '') + '</div></div>' +
                            '<div class="agent-detail-row"><div class="agent-detail-label">版本</div><div class="agent-detail-value">' + (agent.version || '-') + '</div></div>' +
                            '<div class="agent-detail-row"><div class="agent-detail-label">注册时间</div><div class="agent-detail-value">' + (agent.registerTime ? new Date(agent.registerTime).toLocaleString() : '-') + '</div></div>' +
                            '<div class="agent-detail-row"><div class="agent-detail-label">最后心跳</div><div class="agent-detail-value">' + (agent.lastHeartbeat ? new Date(agent.lastHeartbeat).toLocaleString() : '-') + '</div></div>' +
                            '<div class="agent-detail-row"><div class="agent-detail-label">绑定数</div><div class="agent-detail-value">' + bindingCount + '</div></div>';
                        
                        document.getElementById('detail-content').innerHTML = html;
                        document.getElementById('detail-modal').classList.add('active');
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load agent detail:', e);
                });
        },
        
        closeDetailModal: function() {
            document.getElementById('detail-modal').classList.remove('active');
        },
        
        showAgentBindings: function(agentId) {
            var self = this;
            fetch('/api/v1/capabilities/bindings')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        var bindings = result.data.filter(function(b) { return b.agentId === agentId; });
                        
                        var bindingsHtml = '';
                        if (bindings.length === 0) {
                            bindingsHtml = '<p style="color: var(--nx-text-secondary); text-align: center; padding: 20px;">暂无绑定</p>';
                        } else {
                            bindingsHtml = '<div class="agent-bindings-list">' +
                                '<div class="agent-bindings-list-header">关联的能力绑定 (' + bindings.length + ')</div>' +
                                bindings.map(function(b) {
                                    return '<div class="agent-binding-item">' +
                                        '<div><strong>' + (b.capId || b.capabilityId) + '</strong><br><small style="color: var(--nx-text-secondary);">' + (b.sceneGroupId || '-') + '</small></div>' +
                                        '<span class="agent-status-badge ' + (b.status === 'ACTIVE' ? 'online' : 'offline') + '">' + (b.status || '-') + '</span>' +
                                    '</div>';
                                }).join('') +
                                '</div>';
                        }
                        
                        document.getElementById('detail-content').innerHTML = '<h4 style="margin: 0 0 16px 0;">Agent: ' + agentId + '</h4>' + bindingsHtml;
                        document.getElementById('detail-modal').classList.add('active');
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load agent bindings:', e);
                });
        },
        
        sendHeartbeat: function(agentId) {
            var self = this;
            fetch('/api/agent/' + agentId + '/heartbeat', { method: 'POST' })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('心跳发送成功');
                        self.loadAgents();
                    } else {
                        alert('心跳发送失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(e) {
                    alert('心跳发送失败: ' + e.message);
                });
        },
        
        deleteAgent: function(agentId) {
            if (!confirm('确定要删除 Agent ' + agentId + ' 吗？')) return;
            
            var self = this;
            fetch('/api/agent/' + agentId, { method: 'DELETE' })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success') {
                        self.loadAgents();
                    } else {
                        alert('删除失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(e) {
                    alert('删除失败: ' + e.message);
                });
        },
        
        initFilters: function() {
            var self = this;
            var searchInput = document.getElementById('search-input');
            var debounceTimer;
            
            searchInput.addEventListener('input', function() {
                var input = this;
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(function() {
                    self.searchKeyword = input.value.trim();
                    self.renderAgents();
                }, 300);
            });
            
            document.getElementById('type-filter').addEventListener('change', function() {
                self.typeFilter = this.value;
                self.renderAgents();
            });
            
            document.getElementById('status-filter').addEventListener('change', function() {
                self.statusFilter = this.value;
                self.renderAgents();
            });
        },
        
        showEmpty: function() {
            document.getElementById('agent-list').innerHTML = '<tr><td colspan="7"><div class="agents-empty-state"><i class="ri-robot-line"></i><h3>暂无 Agent</h3><p>请先注册 Agent</p></div></td></tr>';
        },
        
        showError: function() {
            document.getElementById('agent-list').innerHTML = '<tr><td colspan="7"><div class="agents-empty-state"><i class="ri-error-warning-line"></i><h3>加载失败</h3><p>请刷新页面重试</p></div></td></tr>';
        }
    };

    window.AgentList = AgentList;
    
    document.addEventListener('DOMContentLoaded', function() {
        AgentList.init();
    });
})();
