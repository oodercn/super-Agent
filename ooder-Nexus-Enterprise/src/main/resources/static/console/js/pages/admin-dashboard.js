(function(global) {
    'use strict';

    var AdminDashboard = {
        init: function() {
            window.onPageInit = function() {
                AdminDashboard.loadDashboardData();
            };
        },

        loadDashboardData: function() {
            fetch('/api/admin/dashboard/stats')
                .then(function(response) {
                    var contentType = response.headers.get('content-type');
                    if (contentType && contentType.includes('application/json')) {
                        return response.json();
                    } else {
                        return response.text().then(function(text) {
                            throw new Error('非JSON响应: ' + text.substring(0, 100) + '...');
                        });
                    }
                })
                .then(function(result) {
                    if (result && result.status === 'success') {
                        var data = result.data;
                        document.getElementById('total-skills').textContent = data.totalSkills || 0;
                        document.getElementById('total-executions').textContent = data.totalExecutions || 0;
                        document.getElementById('total-users').textContent = data.totalUsers || 0;
                        document.getElementById('total-groups').textContent = data.totalGroups || 0;

                        if (data.recentActivities) {
                            AdminDashboard.updateActivityList(data.recentActivities);
                        }

                        if (data.systemStatus) {
                            AdminDashboard.updateSystemStatus(data.systemStatus);
                        }
                    }
                })
                .catch(function(error) {
                    console.error('加载仪表盘数据失败:', error);
                });
        },

        updateActivityList: function(activities) {
            var activityList = document.getElementById('activity-list');
            activityList.innerHTML = '';

            activities.forEach(function(activity) {
                var item = document.createElement('div');
                item.className = 'activity-item';
                item.innerHTML = '<div class="activity-icon ' + AdminDashboard.getActivityIcon(activity.type) + '"></div>' +
                    '<div class="activity-content">' +
                    '<p>' + activity.description + '</p>' +
                    '<span class="activity-time">' + AdminDashboard.formatTime(activity.timestamp) + '</span>' +
                    '</div>';
                activityList.appendChild(item);
            });
        },

        updateSystemStatus: function(status) {
            document.getElementById('cpu-usage').textContent = status.cpuUsage + '%';
            document.getElementById('memory-usage').textContent = status.memoryUsage + '%';
            document.getElementById('disk-usage').textContent = status.diskUsage + '%';
            document.getElementById('network-status').textContent = status.networkStatus;
        },

        getActivityIcon: function(type) {
            var iconMap = {
                'execution': 'ri-play-line',
                'publish': 'ri-upload-line',
                'share': 'ri-share-line',
                'user': 'ri-user-line'
            };
            return iconMap[type] || 'ri-information-line';
        },

        formatTime: function(timestamp) {
            var date = new Date(timestamp);
            var now = new Date();
            var diff = now - date;

            if (diff < 60000) {
                return Math.floor(diff / 1000) + '分钟前';
            } else if (diff < 3600000) {
                return Math.floor(diff / 3600000) + '小时前';
            } else {
                return Math.floor(diff / 86400000) + '天前';
            }
        }
    };

    AdminDashboard.init();

})(typeof window !== 'undefined' ? window : this);
