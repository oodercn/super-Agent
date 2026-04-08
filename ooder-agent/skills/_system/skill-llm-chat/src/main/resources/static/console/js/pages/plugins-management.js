(function(global) {
    'use strict';
    
    var allPlugins = [];
    var currentTab = 'installed';
    var searchKeyword = '';
    
    var PluginManagement = {
        init: function() {
            this.loadPlugins();
            this.bindEvents();
        },
        
        loadPlugins: function() {
            var self = this;
            fetch('/api/v1/plugins')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    allPlugins = result.data || [];
                    self.updateStats();
                    self.renderPluginList();
                } else {
                    console.error('加载失败:', result.message);
                    self.showError(result.message);
                }
            })
            .catch(function(err) {
                console.error('加载失败:', err);
                self.loadMockPlugins();
            });
        },
        
        loadMockPlugins: function() {
            allPlugins = [
                {
                    id: 'skill-scenes-3.0.1',
                    name: '场景管理插件',
                    version: '3.0.1',
                    size: '2.5MB',
                    status: 'RUNNING',
                    loadTime: '2026-04-06 10:30:00',
                    description: '提供场景的创建、编辑和管理功能'
                },
                {
                    id: 'skill-workflow-2.1.0',
                    name: '工作流插件',
                    version: '2.1.0',
                    size: '1.8MB',
                    status: 'STOPPED',
                    loadTime: '2026-04-05 15:20:00',
                    description: '提供工作流的设计和执行功能'
                }
            ];
            this.updateStats();
            this.renderPluginList();
        },
        
        updateStats: function() {
            var running = 0, stopped = 0;
            
            allPlugins.forEach(function(plugin) {
                if (plugin.status === 'RUNNING') running++;
                else stopped++;
            });
            
            document.getElementById('statRunning').textContent = running;
            document.getElementById('statStopped').textContent = stopped;
            document.getElementById('statTotal').textContent = allPlugins.length;
        },
        
        renderPluginList: function() {
            var self = this;
            var filtered = allPlugins.filter(function(plugin) {
                if (searchKeyword) {
                    var keyword = searchKeyword.toLowerCase();
                    var name = (plugin.name || '').toLowerCase();
                    var id = (plugin.id || '').toLowerCase();
                    return name.includes(keyword) || id.includes(keyword);
                }
                return true;
            });
            
            var container = document.getElementById('pluginList');
            
            if (filtered.length === 0) {
                container.innerHTML = 
                    '<div class="empty-state">' +
                    '<i class="ri-plug-line"></i>' +
                    '<p>' + (searchKeyword ? '没有找到匹配的插件' : '暂无已安装的插件') + '</p>' +
                    '</div>';
                return;
            }
            
            var html = '';
            filtered.forEach(function(plugin) {
                html += self.renderPluginCard(plugin);
            });
            
            container.innerHTML = html;
        },
        
        renderPluginCard: function(plugin) {
            var statusClass = plugin.status === 'RUNNING' ? 'running' : 'stopped';
            var statusText = plugin.status === 'RUNNING' ? '运行中' : '已停止';
            var statusIcon = plugin.status === 'RUNNING' ? 'ri-play-circle-line' : 'ri-stop-circle-line';
            
            return '<div class="plugin-card" onclick="PluginManagement.showDetail(\'' + plugin.id + '\')">' +
                '<div class="plugin-header">' +
                    '<div class="plugin-icon">' +
                        '<i class="ri-plug-line"></i>' +
                    '</div>' +
                    '<div class="plugin-info">' +
                        '<h3>' + plugin.name + '</h3>' +
                        '<p>' + plugin.id + '</p>' +
                    '</div>' +
                    '<div class="plugin-status ' + statusClass + '">' +
                        '<i class="' + statusIcon + '"></i>' +
                        statusText +
                    '</div>' +
                '</div>' +
                '<div class="plugin-body">' +
                    '<p>' + (plugin.description || '暂无描述') + '</p>' +
                    '<div class="plugin-meta">' +
                        '<span><i class="ri-file-zip-line"></i> ' + plugin.size + '</span>' +
                        '<span><i class="ri-time-line"></i> ' + plugin.loadTime + '</span>' +
                    '</div>' +
                '</div>' +
                '<div class="plugin-actions">' +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); PluginManagement.config(\'' + plugin.id + '\')">' +
                        '<i class="ri-settings-3-line"></i> 配置' +
                    '</button>' +
                    (plugin.status === 'RUNNING' ?
                        '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); PluginManagement.stop(\'' + plugin.id + '\')">' +
                            '<i class="ri-stop-circle-line"></i> 停止' +
                        '</button>' :
                        '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); PluginManagement.start(\'' + plugin.id + '\')">' +
                            '<i class="ri-play-circle-line"></i> 启动' +
                        '</button>'
                    ) +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); PluginManagement.viewLogs(\'' + plugin.id + '\')">' +
                        '<i class="ri-file-list-line"></i> 日志' +
                    '</button>' +
                    '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="event.stopPropagation(); PluginManagement.uninstall(\'' + plugin.id + '\')">' +
                        '<i class="ri-delete-bin-line"></i> 卸载' +
                    '</button>' +
                '</div>' +
            '</div>';
        },
        
        bindEvents: function() {
            var self = this;
            
            document.getElementById('searchInput').addEventListener('input', function(e) {
                searchKeyword = e.target.value;
                self.renderPluginList();
            });
        },
        
        switchTab: function(tabName) {
            currentTab = tabName;
            
            document.querySelectorAll('.tab').forEach(function(tab) {
                tab.classList.remove('active');
            });
            document.querySelector('.tab[data-tab="' + tabName + '"]').classList.add('active');
            
            document.querySelectorAll('.tab-content').forEach(function(content) {
                content.classList.remove('active');
            });
            document.getElementById(tabName + '-tab').classList.add('active');
        },
        
        showDetail: function(pluginId) {
            var plugin = allPlugins.find(function(p) { return p.id === pluginId; });
            if (!plugin) return;
            
            var html = '<div class="detail-section">' +
                '<h4>基本信息</h4>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">插件ID:</span>' +
                    '<span class="detail-value">' + plugin.id + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">名称:</span>' +
                    '<span class="detail-value">' + plugin.name + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">版本:</span>' +
                    '<span class="detail-value">' + plugin.version + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">大小:</span>' +
                    '<span class="detail-value">' + plugin.size + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">状态:</span>' +
                    '<span class="detail-value">' + plugin.status + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">加载时间:</span>' +
                    '<span class="detail-value">' + plugin.loadTime + '</span>' +
                '</div>' +
            '</div>';
            
            document.getElementById('detailPanelContent').innerHTML = html;
            document.getElementById('detailPanel').classList.add('open');
        },
        
        installFromFile: function() {
            var input = document.createElement('input');
            input.type = 'file';
            input.accept = '.jar';
            input.onchange = function(e) {
                var file = e.target.files[0];
                if (file) {
                    alert('安装插件: ' + file.name + '\n\n功能开发中...');
                }
            };
            input.click();
        },
        
        refresh: function() {
            this.loadPlugins();
        },
        
        config: function(pluginId) {
            this.switchTab('config');
            alert('配置插件: ' + pluginId);
        },
        
        start: function(pluginId) {
            var self = this;
            fetch('/api/v1/plugins/' + pluginId + '/start', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('启动成功！');
                    self.refresh();
                } else {
                    alert('启动失败: ' + result.message);
                }
            })
            .catch(function(err) {
                alert('启动失败: ' + err.message);
            });
        },
        
        stop: function(pluginId) {
            if (!confirm('确定要停止插件 ' + pluginId + ' 吗？')) return;
            
            var self = this;
            fetch('/api/v1/plugins/' + pluginId + '/stop', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('停止成功！');
                    self.refresh();
                } else {
                    alert('停止失败: ' + result.message);
                }
            })
            .catch(function(err) {
                alert('停止失败: ' + err.message);
            });
        },
        
        viewLogs: function(pluginId) {
            alert('查看日志: ' + pluginId + '\n\n功能开发中...');
        },
        
        uninstall: function(pluginId) {
            if (!confirm('确定要卸载插件 ' + pluginId + ' 吗？此操作不可恢复！')) return;
            
            var self = this;
            fetch('/api/v1/plugins/' + pluginId + '/uninstall', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('卸载成功！');
                    self.refresh();
                } else {
                    alert('卸载失败: ' + result.message);
                }
            })
            .catch(function(err) {
                alert('卸载失败: ' + err.message);
            });
        },
        
        showError: function(message) {
            var container = document.getElementById('pluginList');
            container.innerHTML = 
                '<div class="error-state">' +
                '<i class="ri-error-warning-line"></i>' +
                '<p>' + message + '</p>' +
                '<button class="nx-btn nx-btn--secondary" onclick="PluginManagement.refresh()">' +
                    '<i class="ri-refresh-line"></i> 重试' +
                '</button>' +
                '</div>';
        }
    };
    
    global.PluginManagement = PluginManagement;
    
    global.closeDetailPanel = function() {
        document.getElementById('detailPanel').classList.remove('open');
    };
    
    document.addEventListener('DOMContentLoaded', function() {
        PluginManagement.init();
    });
})(window);
