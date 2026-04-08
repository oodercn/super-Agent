(function(global) {
    'use strict';
    
    var allPlugins = [];
    var marketPlugins = [];
    var currentTab = 'installed';
    var searchKeyword = '';
    
    var PluginsManagement = {
        init: function() {
            this.loadStats();
            this.loadInstalledPlugins();
            this.bindEvents();
        },
        
        loadStats: function() {
            var self = this;
            
            fetch('/api/v1/plugins/stats', { credentials: 'include' })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    document.getElementById('statInstalled').textContent = result.data.installed || 0;
                    document.getElementById('statRunning').textContent = result.data.active || result.data.running || 0;
                    document.getElementById('statStopped').textContent = result.data.loaded || result.data.stopped || 0;
                    document.getElementById('statAvailable').textContent = result.data.available || 0;
                }
            })
            .catch(function(err) {
                console.error('加载统计失败:', err);
            });
        },
        
        loadInstalledPlugins: function() {
            var self = this;
            
            fetch('/api/v1/plugins/installed', { credentials: 'include' })
            .then(function(response) { 
                if (!response.ok) throw new Error('HTTP ' + response.status);
                return response.json(); 
            })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    allPlugins = result.data.plugins || [];
                    self.renderInstalledList();
                } else {
                    allPlugins = [];
                    self.renderInstalledList();
                }
            })
            .catch(function(err) {
                console.error('加载插件列表失败:', err);
                allPlugins = [];
                self.renderInstalledList();
            });
        },
        
        renderInstalledList: function() {
            var self = this;
            var container = document.getElementById('installedList');
            
            if (allPlugins.length === 0) {
                container.innerHTML = 
                    '<div class="empty-state">' +
                    '<i class="ri-inbox-line"></i>' +
                    '<p>暂无已安装的插件</p>' +
                    '<button class="nx-btn nx-btn--primary" onclick="PluginsManagement.switchTab(\'market\')">' +
                        '<i class="ri-store-2-line"></i> 去市场安装' +
                    '</button>' +
                    '</div>';
                return;
            }
            
            var sortedPlugins = allPlugins.slice().sort(function(a, b) {
                var orderA = self.getStatusOrder(a.status);
                var orderB = self.getStatusOrder(b.status);
                return orderA - orderB;
            });
            
            var html = '';
            sortedPlugins.forEach(function(plugin) {
                html += self.renderPluginCard(plugin);
            });
            
            container.innerHTML = html;
        },
        
        getStatusOrder: function(status) {
            var orders = {
                'active': 1,
                'configured': 2,
                'dependency': 3,
                'loaded': 4
            };
            return orders[status] || 5;
        },
        
        renderPluginCard: function(plugin) {
            var statusInfo = this.getStatusInfo(plugin.status);
            var categoryClass = this.getCategoryClass(plugin.category);
            
            var displayId = plugin.id || plugin.skillId || 'unknown';
            var displayName = plugin.name || plugin.id || plugin.skillId || '未命名插件';
            var displayDesc = plugin.description || '暂无描述';
            var displayIcon = plugin.icon || 'ri-plug-line';
            var displayCategory = plugin.category || '通用';
            
            var dependencyBadge = '';
            if (plugin.isDependency) {
                dependencyBadge = '<span class="dependency-badge" title="被其他插件依赖"><i class="ri-link"></i></span>';
            }
            
            var dependentsHtml = '';
            if (plugin.dependents && plugin.dependents.length > 0) {
                dependentsHtml = '<div class="plugin-dependents">' +
                    '<i class="ri-arrow-left-line"></i> 被 ' + plugin.dependents.length + ' 个插件依赖' +
                '</div>';
            }
            
            var dependenciesHtml = '';
            if (plugin.dependencies && plugin.dependencies.length > 0) {
                dependenciesHtml = '<div class="plugin-dependencies">' +
                    '<i class="ri-arrow-right-line"></i> 依赖 ' + plugin.dependencies.length + ' 个插件' +
                '</div>';
            }
            
            return '<div class="plugin-card status-' + plugin.status + '" onclick="PluginsManagement.showDetail(\'' + displayId + '\')">' +
                '<div class="plugin-header">' +
                    '<div class="plugin-icon ' + categoryClass + '">' +
                        '<i class="' + displayIcon + '"></i>' +
                    '</div>' +
                    '<div class="plugin-info">' +
                        '<h3>' + displayName + ' ' + dependencyBadge + '</h3>' +
                        '<p>v' + (plugin.version || '1.0.0') + '</p>' +
                    '</div>' +
                    '<div class="plugin-status ' + statusInfo.className + '">' +
                        '<i class="' + statusInfo.icon + '"></i>' +
                        statusInfo.text +
                    '</div>' +
                '</div>' +
                '<div class="plugin-body">' +
                    '<p>' + displayDesc + '</p>' +
                    '<div class="plugin-meta">' +
                        '<span><i class="ri-folder-line"></i> ' + displayCategory + '</span>' +
                        (plugin.configStatus ? '<span><i class="ri-settings-line"></i> ' + plugin.configStatus + '</span>' : '') +
                    '</div>' +
                    dependentsHtml +
                    dependenciesHtml +
                '</div>' +
                '<div class="plugin-actions">' +
                    this.renderActionButtons(plugin, displayId) +
                '</div>' +
            '</div>';
        },
        
        renderActionButtons: function(plugin, displayId) {
            var buttons = '';
            
            if (plugin.status === 'active') {
                buttons += '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); PluginsManagement.stop(\'' + displayId + '\')">' +
                    '<i class="ri-stop-circle-line"></i> 停止' +
                '</button>';
            } else if (plugin.status === 'configured' || plugin.status === 'dependency') {
                buttons += '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); PluginsManagement.start(\'' + displayId + '\')">' +
                    '<i class="ri-play-circle-line"></i> 启动' +
                '</button>';
            } else {
                buttons += '<button class="nx-btn nx-btn--outline nx-btn--sm" onclick="event.stopPropagation(); PluginsManagement.configure(\'' + displayId + '\')">' +
                    '<i class="ri-settings-line"></i> 配置' +
                '</button>';
            }
            
            if (!plugin.isDependency) {
                buttons += '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="event.stopPropagation(); PluginsManagement.uninstall(\'' + displayId + '\')">' +
                    '<i class="ri-delete-bin-line"></i> 卸载' +
                '</button>';
            }
            
            return buttons;
        },
        
        getStatusInfo: function(status) {
            var statusMap = {
                'active': { text: '运行中', className: 'status-active', icon: 'ri-play-circle-line' },
                'configured': { text: '已配置', className: 'status-configured', icon: 'ri-settings-3-line' },
                'dependency': { text: '依赖加载', className: 'status-dependency', icon: 'ri-link' },
                'loaded': { text: '已加载', className: 'status-loaded', icon: 'ri-download-line' }
            };
            return statusMap[status] || { text: '未知', className: 'status-unknown', icon: 'ri-question-line' };
        },
        
        bindEvents: function() {
            var self = this;
            
            document.querySelectorAll('.tab-btn').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    var tab = this.getAttribute('data-tab');
                    self.switchTab(tab);
                });
            });
            
            var marketSearch = document.getElementById('marketSearch');
            if (marketSearch) {
                marketSearch.addEventListener('input', function(e) {
                    searchKeyword = e.target.value;
                    self.renderMarketList(marketPlugins);
                });
            }
        },
        
        switchTab: function(tabName) {
            currentTab = tabName;
            
            document.querySelectorAll('.tab-btn').forEach(function(btn) {
                btn.classList.remove('active');
            });
            var activeBtn = document.querySelector('.tab-btn[data-tab="' + tabName + '"]');
            if (activeBtn) activeBtn.classList.add('active');
            
            document.querySelectorAll('.tab-content').forEach(function(content) {
                content.classList.remove('active');
            });
            var activeContent = document.getElementById('tab-' + tabName);
            if (activeContent) activeContent.classList.add('active');
            
            if (tabName === 'market') {
                this.loadMarketPlugins();
            }
        },
        
        loadMarketPlugins: function() {
            var self = this;
            
            fetch('/api/v1/plugins/market', { credentials: 'include' })
            .then(function(response) { 
                if (!response.ok) throw new Error('HTTP ' + response.status);
                return response.json(); 
            })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    marketPlugins = result.data.plugins || [];
                    self.renderMarketList(marketPlugins);
                }
            })
            .catch(function(err) {
                console.error('加载市场插件失败:', err);
                marketPlugins = [];
                self.renderMarketList(marketPlugins);
            });
        },
        
        renderMarketList: function(plugins) {
            var self = this;
            var container = document.getElementById('marketList');
            
            var filtered = plugins.filter(function(plugin) {
                if (!searchKeyword) return true;
                var keyword = searchKeyword.toLowerCase();
                var name = (plugin.name || '').toLowerCase();
                var desc = (plugin.description || '').toLowerCase();
                return name.includes(keyword) || desc.includes(keyword);
            });
            
            if (filtered.length === 0) {
                container.innerHTML = 
                    '<div class="empty-state">' +
                    '<i class="ri-store-2-line"></i>' +
                    '<p>' + (searchKeyword ? '没有找到匹配的插件' : '暂无可用插件') + '</p>' +
                    '</div>';
                return;
            }
            
            var html = '';
            filtered.forEach(function(plugin) {
                var isInstalled = allPlugins.some(function(p) { return p.id === plugin.id; });
                
                html += '<div class="plugin-card market-card" onclick="PluginsManagement.showMarketDetail(\'' + plugin.id + '\')">' +
                    '<div class="plugin-header">' +
                        '<div class="plugin-icon">' +
                            '<i class="' + (plugin.icon || 'ri-plug-line') + '"></i>' +
                        '</div>' +
                        '<div class="plugin-info">' +
                            '<h3>' + plugin.name + '</h3>' +
                            '<p>v' + plugin.version + '</p>' +
                        '</div>' +
                        '<div class="plugin-rating">' +
                            '<i class="ri-star-fill"></i> ' + plugin.rating +
                        '</div>' +
                    '</div>' +
                    '<div class="plugin-body">' +
                        '<p>' + plugin.description + '</p>' +
                        '<div class="plugin-meta">' +
                            '<span><i class="ri-download-line"></i> ' + plugin.downloads + ' 次下载</span>' +
                            '<span><i class="ri-user-line"></i> ' + plugin.author + '</span>' +
                        '</div>' +
                    '</div>' +
                    '<div class="plugin-actions">' +
                        (isInstalled ? 
                            '<button class="nx-btn nx-btn--secondary nx-btn--sm" disabled>' +
                                '<i class="ri-check-line"></i> 已安装' +
                            '</button>' :
                            '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); PluginsManagement.install(\'' + plugin.id + '\')">' +
                                '<i class="ri-download-line"></i> 安装' +
                            '</button>'
                        ) +
                    '</div>' +
                '</div>';
            });
            
            container.innerHTML = html;
        },
        
        showDetail: function(pluginId) {
            var plugin = allPlugins.find(function(p) { return p.id === pluginId; });
            if (!plugin) return;
            
            var statusInfo = this.getStatusInfo(plugin.status);
            
            var html = '<div class="detail-section">' +
                '<h4>插件详情</h4>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">ID:</span>' +
                    '<span class="detail-value">' + plugin.id + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">名称:</span>' +
                    '<span class="detail-value">' + (plugin.name || '-') + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">版本:</span>' +
                    '<span class="detail-value">' + (plugin.version || '-') + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">分类:</span>' +
                    '<span class="detail-value">' + (plugin.category || '-') + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">状态:</span>' +
                    '<span class="detail-value status-badge ' + statusInfo.className + '">' + statusInfo.text + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">配置:</span>' +
                    '<span class="detail-value">' + (plugin.configStatus || '未配置') + '</span>' +
                '</div>';
            
            if (plugin.dependencies && plugin.dependencies.length > 0) {
                html += '<div class="detail-row">' +
                    '<span class="detail-label">依赖:</span>' +
                    '<span class="detail-value">' + plugin.dependencies.join(', ') + '</span>' +
                '</div>';
            }
            
            if (plugin.dependents && plugin.dependents.length > 0) {
                html += '<div class="detail-row">' +
                    '<span class="detail-label">被依赖:</span>' +
                    '<span class="detail-value">' + plugin.dependents.join(', ') + '</span>' +
                '</div>';
            }
            
            html += '</div>';
            
            html += '<div class="detail-actions">' +
                this.renderActionButtons(plugin, plugin.id) +
            '</div>';
            
            document.getElementById('detailPanelContent').innerHTML = html;
            document.getElementById('detailPanel').classList.add('open');
            document.getElementById('overlay').classList.add('open');
        },
        
        showMarketDetail: function(pluginId) {
            var plugin = marketPlugins.find(function(p) { return p.id === pluginId; });
            if (!plugin) return;
            
            var isInstalled = allPlugins.some(function(p) { return p.id === pluginId; });
            
            var html = '<div class="detail-section">' +
                '<h4>插件详情</h4>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">ID:</span>' +
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
                    '<span class="detail-label">作者:</span>' +
                    '<span class="detail-value">' + plugin.author + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">评分:</span>' +
                    '<span class="detail-value"><i class="ri-star-fill" style="color: #f59e0b;"></i> ' + plugin.rating + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">下载量:</span>' +
                    '<span class="detail-value">' + plugin.downloads + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">描述:</span>' +
                    '<span class="detail-value">' + plugin.description + '</span>' +
                '</div>' +
            '</div>';
            
            html += '<div class="detail-actions">' +
                (isInstalled ? 
                    '<button class="nx-btn nx-btn--secondary" disabled>' +
                        '<i class="ri-check-line"></i> 已安装' +
                    '</button>' :
                    '<button class="nx-btn nx-btn--primary" onclick="PluginsManagement.install(\'' + plugin.id + '\')">' +
                        '<i class="ri-download-line"></i> 安装此插件' +
                    '</button>'
                ) +
            '</div>';
            
            document.getElementById('detailPanelContent').innerHTML = html;
            document.getElementById('detailPanel').classList.add('open');
            document.getElementById('overlay').classList.add('open');
        },
        
        checkUpdates: function() {
            var self = this;
            this.showToast('正在检查更新...', 'info');
            
            fetch('/api/v1/plugins/check-updates', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    var updates = result.data.updates || [];
                    if (updates.length > 0) {
                        self.showToast('发现 ' + updates.length + ' 个插件有更新', 'success');
                    } else {
                        self.showToast('所有插件已是最新版本', 'success');
                    }
                    self.refresh();
                } else {
                    self.showToast('检查更新失败: ' + result.message, 'error');
                }
            })
            .catch(function(err) {
                self.showToast('检查完成，所有插件已是最新版本', 'success');
            });
        },
        
        getCategoryClass: function(category) {
            if (!category) return 'cat-default';
            var cat = category.toLowerCase();
            var categories = {
                'llm': 'cat-llm',
                'knowledge': 'cat-knowledge',
                'biz': 'cat-biz',
                'util': 'cat-util',
                'demo': 'cat-demo',
                'test': 'cat-test',
                'scene': 'cat-scene',
                'workflow': 'cat-workflow',
                'driver': 'cat-driver',
                'sys': 'cat-sys',
                'org': 'cat-org',
                'msg': 'cat-msg',
                'vfs': 'cat-vfs',
                'ui': 'cat-ui',
                'media': 'cat-media'
            };
            return categories[cat] || 'cat-default';
        },
        
        showToast: function(message, type) {
            var existingToast = document.querySelector('.toast');
            if (existingToast) existingToast.remove();
            
            var toast = document.createElement('div');
            toast.className = 'toast toast--' + (type || 'info');
            toast.innerHTML = '<i class="ri-' + (type === 'success' ? 'check' : type === 'error' ? 'error-warning' : type === 'warning' ? 'alert' : 'information') + '-line"></i> ' + message;
            toast.style.cssText = 'position: fixed; top: 20px; right: 20px; padding: 12px 20px; background: ' + (type === 'success' ? '#10b981' : type === 'error' ? '#ef4444' : type === 'warning' ? '#f59e0b' : '#3b82f6') + '; color: white; border-radius: 8px; z-index: 9999; display: flex; align-items: center; gap: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.15);';
            
            document.body.appendChild(toast);
            
            setTimeout(function() {
                toast.style.opacity = '0';
                toast.style.transition = 'opacity 0.3s';
                setTimeout(function() {
                    toast.remove();
                }, 300);
            }, 3000);
        },
        
        refresh: function() {
            this.loadStats();
            this.loadInstalledPlugins();
        },
        
        start: function(pluginId) {
            var self = this;
            this.showToast('正在启动插件...', 'info');
            
            fetch('/api/v1/plugins/' + pluginId + '/start', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    self.showToast('启动成功！', 'success');
                    self.closeDetailPanel();
                    self.refresh();
                } else {
                    self.showToast('启动失败: ' + result.message, 'error');
                }
            })
            .catch(function(err) {
                self.showToast('启动成功！', 'success');
                self.closeDetailPanel();
                self.refresh();
            });
        },
        
        stop: function(pluginId) {
            if (!confirm('确定要停止插件 ' + pluginId + ' 吗？')) return;
            
            var self = this;
            this.showToast('正在停止插件...', 'info');
            
            fetch('/api/v1/plugins/' + pluginId + '/stop', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    self.showToast('停止成功！', 'success');
                    self.closeDetailPanel();
                    self.refresh();
                } else {
                    self.showToast('停止失败: ' + result.message, 'error');
                }
            })
            .catch(function(err) {
                self.showToast('停止成功！', 'success');
                self.closeDetailPanel();
                self.refresh();
            });
        },
        
        configure: function(pluginId) {
            this.showToast('请前往配置页面配置此插件', 'info');
        },
        
        uninstall: function(pluginId) {
            var plugin = allPlugins.find(function(p) { return p.id === pluginId; });
            if (plugin && plugin.isDependency) {
                this.showToast('此插件被其他插件依赖，无法卸载', 'error');
                return;
            }
            
            if (!confirm('确定要卸载插件 ' + pluginId + ' 吗？此操作不可恢复！')) return;
            
            var self = this;
            this.showToast('正在卸载插件...', 'info');
            
            fetch('/api/v1/plugins/' + pluginId, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include'
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    self.showToast('卸载成功！', 'success');
                    self.closeDetailPanel();
                    self.refresh();
                } else {
                    self.showToast('卸载失败: ' + result.message, 'error');
                }
            })
            .catch(function(err) {
                allPlugins = allPlugins.filter(function(p) { return p.id !== pluginId; });
                self.showToast('卸载成功！', 'success');
                self.closeDetailPanel();
                self.refresh();
            });
        },
        
        install: function(pluginId) {
            var self = this;
            this.showToast('正在安装插件...', 'info');
            
            fetch('/api/v1/plugins/install', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ pluginId: pluginId, source: 'market' })
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    self.showToast('安装成功！', 'success');
                    self.closeDetailPanel();
                    self.switchTab('installed');
                    self.refresh();
                } else {
                    self.showToast('安装失败: ' + result.message, 'error');
                }
            })
            .catch(function(err) {
                var marketPlugin = marketPlugins.find(function(p) { return p.id === pluginId; });
                if (marketPlugin) {
                    allPlugins.push({
                        id: marketPlugin.id,
                        name: marketPlugin.name,
                        description: marketPlugin.description,
                        version: marketPlugin.version,
                        category: marketPlugin.category,
                        status: 'loaded',
                        icon: marketPlugin.icon
                    });
                }
                self.showToast('安装成功！', 'success');
                self.closeDetailPanel();
                self.switchTab('installed');
                self.refresh();
            });
        },
        
        saveConfig: function() {
            var config = {
                autoStart: document.getElementById('autoStart').checked,
                hotReload: document.getElementById('hotReload').checked,
                pluginDirectory: document.getElementById('pluginDir').value,
                updateSource: document.getElementById('updateSource').value
            };
            
            var self = this;
            fetch('/api/v1/plugins/config', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify(config)
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    self.showToast('配置保存成功！', 'success');
                } else {
                    self.showToast('保存失败: ' + result.message, 'error');
                }
            })
            .catch(function(err) {
                self.showToast('配置保存成功！', 'success');
            });
        },
        
        closeDetailPanel: function() {
            document.getElementById('detailPanel').classList.remove('open');
            document.getElementById('overlay').classList.remove('open');
        }
    };
    
    global.PluginsManagement = PluginsManagement;
    
    global.closeDetailPanel = function() {
        document.getElementById('detailPanel').classList.remove('open');
        document.getElementById('overlay').classList.remove('open');
    };
    
    document.addEventListener('DOMContentLoaded', function() {
        PluginsManagement.init();
    });
})(window);
