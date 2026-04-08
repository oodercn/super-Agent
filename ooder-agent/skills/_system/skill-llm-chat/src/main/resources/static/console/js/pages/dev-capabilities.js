(function(global) {
    'use strict';
    
    var allCapabilities = [];
    var currentStatusFilter = 'all';
    var searchKeyword = '';
    
    var DevCapabilities = {
        init: function() {
            this.loadCapabilities();
            this.bindEvents();
        },
        
        loadCapabilities: function() {
            var self = this;
            fetch('/api/v1/discovery/dev', {
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
            var dev = 0, testing = 0, published = 0;
            
            allCapabilities.forEach(function(cap) {
                if (cap.status === 'DEV') dev++;
                else if (cap.status === 'TESTING') testing++;
                else if (cap.status === 'PUBLISHED') published++;
            });
            
            document.getElementById('statDev').textContent = dev;
            document.getElementById('statTesting').textContent = testing;
            document.getElementById('statPublished').textContent = published;
        },
        
        renderList: function() {
            var self = this;
            var filtered = allCapabilities.filter(function(cap) {
                if (currentStatusFilter !== 'all' && cap.status !== currentStatusFilter) {
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
                    '<i class="ri-code-box-line"></i>' +
                    '<p>' + (searchKeyword || currentStatusFilter !== 'all' ? '没有找到匹配的能力' : '暂无开发中的能力') + '</p>' +
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
            var statusClass = cap.status === 'DEV' ? 'dev' : 
                             cap.status === 'TESTING' ? 'testing' : 'published';
            var statusText = cap.status === 'DEV' ? '开发中' : 
                            cap.status === 'TESTING' ? '测试中' : '已发布';
            var statusIcon = cap.status === 'DEV' ? 'ri-code-line' : 
                            cap.status === 'TESTING' ? 'ri-test-tube-line' : 'ri-upload-cloud-line';
            
            return '<div class="capability-card" onclick="DevCapabilities.showDetail(\'' + cap.skillId + '\')">' +
                '<div class="capability-header">' +
                    '<div class="capability-icon">' +
                        '<i class="' + (cap.icon || 'ri-puzzle-line') + '"></i>' +
                    '</div>' +
                    '<div class="capability-info">' +
                        '<h3>' + (cap.name || cap.skillId) + '</h3>' +
                        '<p>' + cap.skillId + '</p>' +
                    '</div>' +
                    '<div class="capability-status ' + statusClass + '">' +
                        '<i class="' + statusIcon + '"></i>' +
                        statusText +
                    '</div>' +
                '</div>' +
                '<div class="capability-body">' +
                    '<p>' + (cap.description || '暂无描述') + '</p>' +
                    '<div class="capability-meta">' +
                        '<span><i class="ri-time-line"></i> 最后修改: ' + this.formatTime(cap.updateTime) + '</span>' +
                    '</div>' +
                '</div>' +
                '<div class="capability-actions">' +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); DevCapabilities.edit(\'' + cap.skillId + '\')">' +
                        '<i class="ri-edit-line"></i> 编辑' +
                    '</button>' +
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); DevCapabilities.test(\'' + cap.skillId + '\')">' +
                        '<i class="ri-test-tube-line"></i> 测试' +
                    '</button>' +
                    '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); DevCapabilities.publish(\'' + cap.skillId + '\')">' +
                        '<i class="ri-upload-cloud-line"></i> 发布' +
                    '</button>' +
                    '<button class="nx-btn nx-btn--danger nx-btn--sm" onclick="event.stopPropagation(); DevCapabilities.delete(\'' + cap.skillId + '\')">' +
                        '<i class="ri-delete-bin-line"></i> 删除' +
                    '</button>' +
                '</div>' +
            '</div>';
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
            
            document.getElementById('statusFilter').addEventListener('change', function(e) {
                currentStatusFilter = e.target.value;
                self.renderList();
            });
        },
        
        showDetail: function(skillId) {
            var cap = allCapabilities.find(function(c) { return c.skillId === skillId; });
            if (!cap) return;
            
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
                    '<span class="detail-label">分类:</span>' +
                    '<span class="detail-value">' + (cap.category || '-') + '</span>' +
                '</div>' +
                '<div class="detail-row">' +
                    '<span class="detail-label">状态:</span>' +
                    '<span class="detail-value">' + cap.status + '</span>' +
                '</div>' +
            '</div>';
            
            document.getElementById('detailPanelContent').innerHTML = html;
            document.getElementById('detailPanel').classList.add('open');
        },
        
        createNew: function() {
            alert('创建新能力功能开发中...');
        },
        
        createFromTemplate: function() {
            alert('从模板创建功能开发中...');
        },
        
        refresh: function() {
            this.loadCapabilities();
        },
        
        edit: function(skillId) {
            alert('编辑能力: ' + skillId);
        },
        
        test: function(skillId) {
            alert('测试能力: ' + skillId);
        },
        
        publish: function(skillId) {
            if (!confirm('确定要发布能力 ' + skillId + ' 吗？')) return;
            
            var self = this;
            fetch('/api/v1/capability-publish/' + skillId + '/gitee', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('发布成功！');
                    self.refresh();
                } else {
                    alert('发布失败: ' + result.message);
                }
            })
            .catch(function(err) {
                alert('发布失败: ' + err.message);
            });
        },
        
        delete: function(skillId) {
            if (!confirm('确定要删除能力 ' + skillId + ' 吗？此操作不可恢复！')) return;
            alert('删除功能开发中...');
        },
        
        showError: function(message) {
            var container = document.getElementById('capabilityList');
            container.innerHTML = 
                '<div class="error-state">' +
                '<i class="ri-error-warning-line"></i>' +
                '<p>' + message + '</p>' +
                '<button class="nx-btn nx-btn--secondary" onclick="DevCapabilities.refresh()">' +
                    '<i class="ri-refresh-line"></i> 重试' +
                '</button>' +
                '</div>';
        }
    };
    
    global.DevCapabilities = DevCapabilities;
    
    global.closeDetailPanel = function() {
        document.getElementById('detailPanel').classList.remove('open');
    };
    
    document.addEventListener('DOMContentLoaded', function() {
        DevCapabilities.init();
    });
})(window);
