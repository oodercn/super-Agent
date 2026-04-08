(function(global) {
    'use strict';
    
    var allCapabilities = [];
    var currentSourceFilter = 'all';
    var currentTypeFilter = 'all';
    var searchKeyword = '';
    
    var LocalCapabilities = {
        init: function() {
            this.loadCapabilities();
            this.bindEvents();
        },
        
        loadCapabilities: function() {
            var self = this;
            fetch('/api/v1/discovery/local', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    allCapabilities = result.data.capabilities || [];
                    self.updateStats();
                    self.renderList();
                } else {
                    console.error('加载失败:', result.message);
                    self.showError(result.message);
                }
            })
            .catch(function(err) {
                console.error('加载失败:', err);
                self.showError('加载失败: ' + err.message);
            });
        },
        
        updateStats: function() {
            var installed = 0, downloaded = 0, activated = 0, jar = 0;
            
            allCapabilities.forEach(function(cap) {
                if (cap.source === 'installed') installed++;
                else if (cap.source === 'downloaded') downloaded++;
                else if (cap.source === 'activated') activated++;
                
                if (cap.type === 'jar') jar++;
            });
            
            document.getElementById('statInstalled').textContent = installed;
            document.getElementById('statDownloaded').textContent = downloaded;
            document.getElementById('statActivated').textContent = activated;
            document.getElementById('statJar').textContent = jar;
        },
        
        renderList: function() {
            var self = this;
            var filtered = allCapabilities.filter(function(cap) {
                if (currentSourceFilter !== 'all' && cap.source !== currentSourceFilter) {
                    return false;
                }
                if (currentTypeFilter !== 'all' && cap.type !== currentTypeFilter) {
                    return false;
                }
                if (searchKeyword) {
                    var keyword = searchKeyword.toLowerCase();
                    var name = (cap.name || '').toLowerCase();
                    var skillId = (cap.skillId || '').toLowerCase();
                    return name.includes(keyword) || skillId.includes(keyword);
                }
                return true;
            });
            
            var container = document.getElementById('capabilityList');
            
            if (filtered.length === 0) {
                container.innerHTML = 
                    '<div class="empty-state">' +
                    '<i class="ri-folder-open-line"></i>' +
                    '<p>' + (searchKeyword || currentSourceFilter !== 'all' || currentTypeFilter !== 'all' ? '没有找到匹配的能力' : '暂无本地能力') + '</p>' +
                    '</div>';
                return;
            }
            
            var html = '';
            filtered.forEach(function(cap) {
                html += self.renderCapabilityCard(cap);
            });
            
            container.innerHTML = html;
        },
        
        renderCapabilityCard: function(cap) {
            var sourceInfo = this.getSourceInfo(cap.source);
            var typeInfo = cap.type === 'jar' ? 
                { icon: 'ri-file-zip-line', text: 'JAR包', class: 'jar' } : 
                { icon: 'ri-folder-3-line', text: '文件夹', class: 'folder' };
            var activatedClass = cap.activated ? 'activated' : '';
            
            return '<div class="capability-card ' + activatedClass + '" onclick="LocalCapabilities.showDetail(\'' + cap.skillId + '\')">' +
                '<div class="capability-header">' +
                    '<div class="capability-icon">' +
                        '<i class="' + (cap.icon || 'ri-puzzle-line') + '"></i>' +
                    '</div>' +
                    '<div class="capability-info">' +
                        '<h3>' + (cap.name || cap.skillId) + '</h3>' +
                        '<p>' + cap.skillId + '</p>' +
                    '</div>' +
                    '<div class="capability-badges">' +
                        '<span class="source-badge ' + sourceInfo.class + '">' +
                            '<i class="' + sourceInfo.icon + '"></i>' +
                            sourceInfo.text +
                        '</span>' +
                        '<span class="type-badge ' + typeInfo.class + '">' +
                            '<i class="' + typeInfo.icon + '"></i>' +
                            typeInfo.text +
                        '</span>' +
                        (cap.activated ? '<span class="activated-badge"><i class="ri-flashlight-line"></i> 已激活</span>' : '') +
                    '</div>' +
                '</div>' +
                '<div class="capability-body">' +
                    '<p>' + (cap.description || '暂无描述') + '</p>' +
                    '<div class="capability-meta">' +
                        '<span><i class="ri-time-line"></i> 安装时间: ' + this.formatTime(cap.installTime) + '</span>' +
                        '<span><i class="ri-folder-line"></i> ' + (cap.location || '未知位置') + '</span>' +
                    '</div>' +
                '</div>' +
                '<div class="capability-actions">' +
                    (cap.activated ? 
                        '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); LocalCapabilities.deactivate(\'' + cap.skillId + '\')">' +
                            '<i class="ri-flashlight-line"></i> 停用' +
                        '</button>' :
                        '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); LocalCapabilities.activate(\'' + cap.skillId + '\')">' +
                            '<i class="ri-flashlight-line"></i> 激活' +
                        '</button>'
                    ) +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); LocalCapabilities.share(\'' + cap.skillId + '\')">' +
                        '<i class="ri-share-line"></i> 分享' +
                    '</button>' +
                    '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="event.stopPropagation(); LocalCapabilities.uninstall(\'' + cap.skillId + '\')">' +
                        '<i class="ri-delete-bin-line"></i> 卸载' +
                    '</button>' +
                '</div>' +
            '</div>';
        },
        
        getSourceInfo: function(source) {
            var sources = {
                'installed': { icon: 'ri-install-line', text: '已安装', class: 'installed' },
                'downloaded': { icon: 'ri-download-cloud-2-line', text: '已下载', class: 'downloaded' },
                'activated': { icon: 'ri-flashlight-line', text: '已激活', class: 'activated' }
            };
            return sources[source] || { icon: 'ri-folder-line', text: '未知', class: 'unknown' };
        },
        
        formatTime: function(timestamp) {
            if (!timestamp) return '未知';
            var date = new Date(timestamp);
            var now = new Date();
            var diff = now - date;
            
            if (diff < 60000) return '刚刚';
            if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前';
            if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前';
            if (diff < 604800000) return Math.floor(diff / 86400000) + '天前';
            
            return date.toLocaleDateString('zh-CN');
        },
        
        bindEvents: function() {
            var self = this;
            
            document.getElementById('searchInput').addEventListener('input', function(e) {
                searchKeyword = e.target.value;
                self.renderList();
            });
            
            document.getElementById('sourceFilter').addEventListener('change', function(e) {
                currentSourceFilter = e.target.value;
                self.renderList();
            });
            
            document.getElementById('typeFilter').addEventListener('change', function(e) {
                currentTypeFilter = e.target.value;
                self.renderList();
            });
        },
        
        showDetail: function(skillId) {
            var cap = allCapabilities.find(function(c) { return c.skillId === skillId; });
            if (!cap) return;
            
            var sourceInfo = this.getSourceInfo(cap.source);
            
            var html = '<div class="detail-section">' +
                '<h4>基本信息</h4>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">能力ID:</span>' +
                    '<span class="detail-value">' + cap.skillId + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">名称:</span>' +
                    '<span class="detail-value">' + (cap.name || '-') + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">描述:</span>' +
                    '<span class="detail-value">' + (cap.description || '-') + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">版本:</span>' +
                    '<span class="detail-value">' + (cap.version || '-') + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">来源:</span>' +
                    '<span class="detail-value">' + sourceInfo.text + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">类型:</span>' +
                    '<span class="detail-value">' + (cap.type === 'jar' ? 'JAR包' : '文件夹') + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">位置:</span>' +
                    '<span class="detail-value code">' + (cap.location || '-') + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">状态:</span>' +
                    '<span class="detail-value">' + (cap.activated ? '已激活' : '未激活') + '</span>' +
                '</div>' +
            '</div>';
            
            if (cap.downloadSource) {
                html += '<div class="detail-section">' +
                    '<h4>下载来源</h4>' +
                    '<div class="detail-row">' +
                        '<span class="detail-label">来源平台:</span>' +
                        '<span class="detail-value">' + (cap.downloadSource.platform || '-') + '</span>' +
                    '</div>' +
                    '<div class="detail-row">' +
                        '<span class="detail-label">仓库地址:</span>' +
                        '<span class="detail-value code">' + (cap.downloadSource.repository || '-') + '</span>' +
                    '</div>' +
                    '<div class="detail-row">' +
                        '<span class="detail-label">下载时间:</span>' +
                        '<span class="detail-value">' + this.formatTime(cap.downloadSource.downloadTime) + '</span>' +
                    '</div>' +
                '</div>';
            }
            
            html += '<div class="detail-actions">' +
                (cap.activated ? 
                    '<button class="nx-btn nx-btn--secondary" onclick="LocalCapabilities.deactivate(\'' + cap.skillId + '\')">' +
                        '<i class="ri-flashlight-line"></i> 停用能力' +
                    '</button>' :
                    '<button class="nx-btn nx-btn--primary" onclick="LocalCapabilities.activate(\'' + cap.skillId + '\')">' +
                        '<i class="ri-flashlight-line"></i> 激活能力' +
                    '</button>'
                ) +
                '<button class="nx-btn nx-btn--secondary" onclick="LocalCapabilities.share(\'' + cap.skillId + '\')">' +
                    '<i class="ri-share-line"></i> 分享' +
                '</button>' +
                '<button class="nx-btn nx-btn--danger" onclick="LocalCapabilities.uninstall(\'' + cap.skillId + '\')">' +
                    '<i class="ri-delete-bin-line"></i> 卸载' +
                '</button>' +
            '</div>';
            
            document.getElementById('detailPanelContent').innerHTML = html;
            document.getElementById('detailPanel').classList.add('open');
        },
        
        scanLocal: function() {
            var self = this;
            fetch('/api/v1/discovery/scan', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('扫描完成！发现 ' + (result.data.count || 0) + ' 个能力');
                    self.loadCapabilities();
                } else {
                    alert('扫描失败: ' + result.message);
                }
            })
            .catch(function(err) {
                alert('扫描失败: ' + err.message);
            });
        },
        
        importCapability: function() {
            alert('导入能力功能开发中...');
        },
        
        refresh: function() {
            this.loadCapabilities();
        },
        
        activate: function(skillId) {
            var self = this;
            fetch('/api/v1/discovery/capability/' + skillId + '/activate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('激活成功！');
                    self.loadCapabilities();
                } else {
                    alert('激活失败: ' + result.message);
                }
            })
            .catch(function(err) {
                alert('激活失败: ' + err.message);
            });
        },
        
        deactivate: function(skillId) {
            var self = this;
            fetch('/api/v1/discovery/capability/' + skillId + '/deactivate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('停用成功！');
                    self.loadCapabilities();
                } else {
                    alert('停用失败: ' + result.message);
                }
            })
            .catch(function(err) {
                alert('停用失败: ' + err.message);
            });
        },
        
        share: function(skillId) {
            alert('分享能力: ' + skillId + ' 功能开发中...');
        },
        
        uninstall: function(skillId) {
            if (!confirm('确定要卸载能力 ' + skillId + ' 吗？此操作不可恢复！')) return;
            
            var self = this;
            fetch('/api/v1/discovery/capability/' + skillId + '/uninstall', {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('卸载成功！');
                    self.loadCapabilities();
                } else {
                    alert('卸载失败: ' + result.message);
                }
            })
            .catch(function(err) {
                alert('卸载失败: ' + err.message);
            });
        },
        
        showError: function(message) {
            var container = document.getElementById('capabilityList');
            container.innerHTML = 
                '<div class="error-state">' +
                '<i class="ri-error-warning-line"></i>' +
                '<p>' + message + '</p>' +
                '<button class="nx-btn nx-btn--secondary" onclick="LocalCapabilities.refresh()">' +
                    '<i class="ri-refresh-line"></i> 重试' +
                '</button>' +
                '</div>';
        }
    };
    
    global.LocalCapabilities = LocalCapabilities;
    
    global.closeDetailPanel = function() {
        document.getElementById('detailPanel').classList.remove('open');
    };
    
    document.addEventListener('DOMContentLoaded', function() {
        LocalCapabilities.init();
    });
})(window);
