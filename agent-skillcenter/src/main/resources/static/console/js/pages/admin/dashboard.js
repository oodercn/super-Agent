/**
 * 管理员仪表盘页面脚本
 */

// 初始化菜单
initMenu('admin-dashboard');

// 页面加载时获取数据
document.addEventListener('DOMContentLoaded', function() {
    fetchDashboardStats();
    fetchExecutionStats();
});

// 从API获取仪表盘统计数据
async function fetchDashboardStats() {
    try {
        const data = await utils.api.get('/dashboard/stats');
        if (data.success && data.data) {
            // 更新统计数据
            document.querySelector('.stats-grid .stat-card:nth-child(1) .stat-value').textContent = data.data.totalSkills || 0;
            document.querySelector('.stats-grid .stat-card:nth-child(2) .stat-value').textContent = data.data.marketSkills || 0;
            document.querySelector('.stats-grid .stat-card:nth-child(3) .stat-value').textContent = data.data.registeredUsers || 0;
            document.querySelector('.stats-grid .stat-card:nth-child(4) .stat-value').textContent = data.data.totalExecutions || 0;
            document.querySelector('.stats-grid .stat-card:nth-child(5) .stat-value').textContent = data.data.sharedSkills || 0;
            document.querySelector('.stats-grid .stat-card:nth-child(6) .stat-value').textContent = data.data.systemAvailability || '98%';
            
            // 更新最近活动
            updateRecentActivities(data.data.recentActivities || []);
        }
    } catch (error) {
        console.error('Error fetching dashboard stats:', error);
    }
}

// 更新最近活动
function updateRecentActivities(activities) {
    const activityList = document.querySelector('.activity-list');
    if (activityList && activities.length > 0) {
        activityList.innerHTML = '';
        activities.forEach(activity => {
            const li = document.createElement('li');
            li.className = 'activity-item';
            li.innerHTML = `
                <div class="activity-icon" style="color: ${getActivityColor(activity.type)}">
                    <i class="${getActivityIcon(activity.type)}"></i>
                </div>
                <div class="activity-content">
                    <p>${activity.description}</p>
                    <div class="activity-time">${utils.date.format(activity.timestamp)}</div>
                </div>
            `;
            activityList.appendChild(li);
        });
    }
}

// 获取活动图标
function getActivityIcon(type) {
    switch (type) {
        case 'skill_published': return 'ri-skill-line';
        case 'skill_approved': return 'ri-verified-badge-line';
        case 'user_registered': return 'ri-user-add-line';
        case 'group_created': return 'ri-group-add-line';
        default: return 'ri-notification-line';
    }
}

// 获取活动颜色
function getActivityColor(type) {
    switch (type) {
        case 'skill_published': return '#4CAF50';
        case 'skill_approved': return '#2196F3';
        case 'user_registered': return '#FF9800';
        case 'group_created': return '#9C27B0';
        default: return '#607D8B';
    }
}

// 从API获取执行统计数据并绘制图表
async function fetchExecutionStats() {
    try {
        const data = await utils.api.get('/dashboard/execution-stats');
        if (data.success && data.data) {
            utils.chart.drawBarChart('chartCanvas', data.data);
        } else {
            // 使用默认数据绘制图表
            utils.chart.drawBarChart('chartCanvas', {
                labels: ['1月', '2月', '3月', '4月', '5月', '6月'],
                datasets: [
                    {
                        label: '成功执行',
                        data: [120, 190, 300, 500, 400, 550],
                        backgroundColor: 'rgba(76, 175, 80, 0.2)',
                        borderColor: 'rgba(76, 175, 80, 1)',
                        borderWidth: 2
                    },
                    {
                        label: '失败执行',
                        data: [20, 30, 40, 50, 40, 50],
                        backgroundColor: 'rgba(244, 67, 54, 0.2)',
                        borderColor: 'rgba(244, 67, 54, 1)',
                        borderWidth: 2
                    }
                ]
            });
        }
    } catch (error) {
        console.error('Error fetching execution stats:', error);
        // 使用默认数据绘制图表
        utils.chart.drawBarChart('chartCanvas', {
            labels: ['1月', '2月', '3月', '4月', '5月', '6月'],
            datasets: [
                {
                    label: '成功执行',
                    data: [120, 190, 300, 500, 400, 550],
                    backgroundColor: 'rgba(76, 175, 80, 0.2)',
                    borderColor: 'rgba(76, 175, 80, 1)',
                    borderWidth: 2
                },
                {
                    label: '失败执行',
                    data: [20, 30, 40, 50, 40, 50],
                    backgroundColor: 'rgba(244, 67, 54, 0.2)',
                    borderColor: 'rgba(244, 67, 54, 1)',
                    borderWidth: 2
                }
            ]
        });
    }
}
