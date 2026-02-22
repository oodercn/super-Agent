(function(global) {
    'use strict';

    var PersonalDashboard = {
        init: function() {
            window.onPageInit = function() {
                PersonalDashboard.initDashboard();
            };
        },

        initDashboard: function() {
            var api = window.personalAPI;

            api.getDashboardStats()
                .then(function(stats) {
                    if (stats.success && stats.data) {
                        var data = stats.data;

                        document.getElementById('skill-count').textContent = data.totalSkills || 0;
                        document.getElementById('execution-count').textContent = data.totalExecutions || 0;
                        document.getElementById('shared-count').textContent = data.sharedSkills || 0;
                        document.getElementById('group-count').textContent = data.myGroups || 0;

                        if (data.recentActivities && data.recentActivities.length > 0) {
                            var activityList = document.getElementById('activity-list');
                            var html = '';
                            data.recentActivities.forEach(function(activity) {
                                html += '<div class="activity-item">' +
                                    '<div class="activity-icon ri-' + PersonalDashboard.getActivityIcon(activity.type) + '"></div>' +
                                    '<div class="activity-content">' +
                                    '<p>' + PersonalDashboard.getActivityText(activity) + '</p>' +
                                    '<span class="activity-time">' + PersonalDashboard.formatTime(activity.timestamp) + '</span>' +
                                    '</div>' +
                                    '</div>';
                            });
                            activityList.innerHTML = html;
                        } else {
                            document.getElementById('activity-list').innerHTML = '<div class="empty-state">暂无活动记录</div>';
                        }

                        PersonalDashboard.drawExecutionChart(data);
                    } else {
                        document.getElementById('activity-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    }

                    return api.getPersonalIdentity();
                })
                .then(function(identity) {
                    if (identity.success && identity.data) {
                        document.getElementById('username').textContent = identity.data.username || '用户';
                    }
                })
                .catch(function(error) {
                    console.error('初始化仪表盘失败:', error);
                    document.getElementById('activity-list').innerHTML = '<div class="empty-state">加载失败</div>';
                    PersonalDashboard.showError('加载仪表盘数据失败');
                });
        },

        getActivityIcon: function(type) {
            var icons = {
                'execution': 'play-circle-line',
                'publish': 'upload-line',
                'share': 'share-line',
                'delete': 'delete-bin-line'
            };
            return icons[type] || 'clock-line';
        },

        getActivityText: function(activity) {
            if (activity.type === 'execution') {
                return '执行了技能 <span class="activity-skill">' + activity.skillName + '</span>';
            } else if (activity.type === 'publish') {
                return '发布了新技能 <span class="activity-skill">' + activity.skillName + '</span>';
            } else if (activity.type === 'share') {
                return '分享了技能 <span class="activity-skill">' + activity.skillName + '</span>';
            }
            return '未知活动';
        },

        formatTime: function(timestamp) {
            var now = new Date();
            var time = new Date(timestamp);
            var diff = Math.floor((now - time) / 1000);

            if (diff < 60) {
                return diff + '秒前';
            } else if (diff < 3600) {
                return Math.floor(diff / 60) + '分钟前';
            } else if (diff < 86400) {
                return Math.floor(diff / 3600) + '小时前';
            } else {
                return Math.floor(diff / 86400) + '天前';
            }
        },

        drawExecutionChart: function(data) {
            var canvas = document.getElementById('execution-chart');
            var ctx = canvas.getContext('2d');

            var chartData = data.executionStats || [5, 8, 3, 7, 4, 9];
            var labels = ['周一', '周二', '周三', '周四', '周五', '周六'];
            var barWidth = 30;
            var barSpacing = 10;

            ctx.clearRect(0, 0, canvas.width, canvas.height);

            ctx.beginPath();
            ctx.moveTo(50, 10);
            ctx.lineTo(50, 190);
            ctx.lineTo(390, 190);
            ctx.strokeStyle = '#333';
            ctx.stroke();

            chartData.forEach(function(value, index) {
                var x = 50 + (barWidth + barSpacing) * index + barSpacing;
                var height = value * 15;
                var y = 190 - height;

                ctx.fillStyle = '#4CAF50';
                ctx.fillRect(x, y, barWidth, height);

                ctx.fillStyle = '#e0e0e0';
                ctx.font = '12px Arial';
                ctx.textAlign = 'center';
                ctx.fillText(labels[index], x + barWidth / 2, 205);
            });
        },

        showError: function(message) {
            var notification = document.createElement('div');
            notification.className = 'notification notification-error';
            notification.textContent = message;
            document.body.appendChild(notification);

            setTimeout(function() {
                notification.remove();
            }, 3000);
        }
    };

    PersonalDashboard.init();

})(typeof window !== 'undefined' ? window : this);
