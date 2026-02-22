(function(global) {
    'use strict';

    var syncAPI = null;
    var syncStatus = null;

    var SyncStatus = {
        init: function() {
            window.onPageInit = function() {
                console.log('同步状态页面初始化完成');
                syncAPI = new SkillCenterSyncAPI();
                SyncStatus.loadSyncStatus();
            };
        },

        loadSyncStatus: function() {
            syncAPI.getSyncStatus()
                .then(function(response) {
                    if (response.success) {
                        syncStatus = response.data;
                        SyncStatus.renderSyncStatus();
                    } else {
                        SyncStatus.showError(response.message || '加载同步状态失败');
                        document.getElementById('sync-status-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    }
                })
                .catch(function(error) {
                    console.error('加载同步状态错误:', error);
                    SyncStatus.showError('加载同步状态失败');
                    document.getElementById('sync-status-list').innerHTML = '<div class="empty-state">加载失败</div>';
                });
        },

        renderSyncStatus: function() {
            var syncStatusList = document.getElementById('sync-status-list');
            var html = '';

            if (!syncStatus || !syncStatus.tasks || syncStatus.tasks.length === 0) {
                syncStatusList.innerHTML = '<div class="empty-state">暂无同步任务</div>';
                return;
            }

            html += '<div class="sync-summary">';
            html += '<div class="summary-item"><span class="summary-label">总任务数：</span><span class="summary-value">' + syncStatus.totalSkills + '</span></div>';
            html += '<div class="summary-item"><span class="summary-label">已完成：</span><span class="summary-value success">' + syncStatus.completedSkills + '</span></div>';
            html += '<div class="summary-item"><span class="summary-label">失败：</span><span class="summary-value error">' + syncStatus.failedSkills + '</span></div>';
            html += '<div class="summary-item"><span class="summary-label">状态：</span><span class="summary-value ' + SyncStatus.getStateClass(syncStatus.state) + '">' + SyncStatus.getStateText(syncStatus.state) + '</span></div>';
            html += '</div>';

            html += '<h3 style="margin-bottom: 16px; color: var(--ns-dark);">同步任务详情</h3>';
            
            syncStatus.tasks.forEach(function(task) {
                html += '<div class="sync-task-item">';
                html += '<div class="task-header">';
                html += '<span class="task-name">' + (task.skillName || task.skillId) + '</span>';
                html += '<span class="task-status ' + SyncStatus.getTaskStateClass(task.state) + '">' + SyncStatus.getTaskStateText(task.state) + '</span>';
                html += '</div>';
                if (task.errorMessage) {
                    html += '<div class="task-error"><strong>错误信息：</strong>' + task.errorMessage + '</div>';
                }
                html += '<div class="task-time">';
                html += '<span>开始时间：' + new Date(task.startTime).toLocaleString('zh-CN') + '</span>';
                if (task.endTime) {
                    html += '<span>结束时间：' + new Date(task.endTime).toLocaleString('zh-CN') + '</span>';
                }
                html += '</div>';
                html += '</div>';
            });

            syncStatusList.innerHTML = html;
        },

        getStateText: function(state) {
            var stateMap = {
                'PENDING': '等待中',
                'IN_PROGRESS': '进行中',
                'COMPLETED': '已完成',
                'FAILED': '失败',
                'CANCELLED': '已取消'
            };
            return stateMap[state] || state;
        },

        getStateClass: function(state) {
            var classMap = {
                'PENDING': 'pending',
                'IN_PROGRESS': 'in-progress',
                'COMPLETED': 'completed',
                'FAILED': 'failed',
                'CANCELLED': 'cancelled'
            };
            return classMap[state] || '';
        },

        getTaskStateText: function(state) {
            return SyncStatus.getStateText(state);
        },

        getTaskStateClass: function(state) {
            return SyncStatus.getStateClass(state);
        },

        refreshSyncStatus: function() {
            document.getElementById('sync-status-list').innerHTML = '<div class="loading">加载中...</div>';
            SyncStatus.loadSyncStatus();
        },

        cancelAllSync: function() {
            alert('取消全部同步功能开发中...');
        },

        showSuccess: function(message) {
            SyncStatus.showNotification(message, 'success');
        },

        showError: function(message) {
            SyncStatus.showNotification(message, 'error');
        },

        showNotification: function(message, type) {
            var notification = document.createElement('div');
            notification.className = 'notification ' + type;
            notification.textContent = message;
            document.body.appendChild(notification);
            setTimeout(function() { notification.classList.add('show'); }, 10);
            setTimeout(function() { notification.remove(); }, 3000);
        }
    };

    SyncStatus.init();

    global.refreshSyncStatus = SyncStatus.refreshSyncStatus;
    global.cancelAllSync = SyncStatus.cancelAllSync;

})(typeof window !== 'undefined' ? window : this);
