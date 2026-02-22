// 初始化菜单
initMenu('dashboard');

// 模拟数据
document.addEventListener('DOMContentLoaded', function() {
    // 更新统计数据
    document.getElementById('skill-count').textContent = '5';
    document.getElementById('execution-count').textContent = '12';
    document.getElementById('shared-count').textContent = '2';
    document.getElementById('group-count').textContent = '3';
    
    // 更新最近活动
    const activityList = document.getElementById('activity-list');
    activityList.innerHTML = `
        <div class="activity-item">
            <div class="activity-icon ri-play-line"></div>
            <div class="activity-content">
                <p>执行了技能 <span class="activity-skill">文本转大写</span></p>
                <span class="activity-time">10分钟前</span>
            </div>
        </div>
        <div class="activity-item">
            <div class="activity-icon ri-share-line"></div>
            <div class="activity-content">
                <p>分享了技能 <span class="activity-skill">代码生成</span> 到群组 <span class="activity-group">开发团队</span></p>
                <span class="activity-time">2小时前</span>
            </div>
        </div>
        <div class="activity-item">
            <div class="activity-icon ri-upload-line"></div>
            <div class="activity-content">
                <p>发布了新技能 <span class="activity-skill">JSON格式化</span></p>
                <span class="activity-time">1天前</span>
            </div>
        </div>
    `;
    
    // 简单的执行统计图表
    const canvas = document.getElementById('execution-chart');
    const ctx = canvas.getContext('2d');
    
    // 绘制柱状图
    const data = [5, 8, 3, 7, 4, 9];
    const labels = ['周一', '周二', '周三', '周四', '周五', '周六'];
    const barWidth = 30;
    const barSpacing = 10;
    
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // 绘制坐标轴
    ctx.beginPath();
    ctx.moveTo(50, 10);
    ctx.lineTo(50, 190);
    ctx.lineTo(390, 190);
    ctx.strokeStyle = '#333';
    ctx.stroke();
    
    // 绘制柱状图
    data.forEach((value, index) => {
        const x = 50 + (barWidth + barSpacing) * index + barSpacing;
        const height = value * 15;
        const y = 190 - height;
        
        ctx.fillStyle = '#4CAF50';
        ctx.fillRect(x, y, barWidth, height);
        
        // 绘制标签
        ctx.fillStyle = '#e0e0e0';
        ctx.font = '12px Arial';
        ctx.textAlign = 'center';
        ctx.fillText(labels[index], x + barWidth / 2, 205);
    });
});
