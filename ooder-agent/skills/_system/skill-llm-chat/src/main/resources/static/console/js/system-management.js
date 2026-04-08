// 使用 IIFE 封装，避免全局变量污染
(function() {
    // 模拟日志数据
    const systemLogData = [
        { level: "info", timestamp: "2026-01-31 10:30:00", message: "系统启动成功" },
        { level: "info", timestamp: "2026-01-31 10:30:05", message: "加载技能管理模块" },
        { level: "info", timestamp: "2026-01-31 10:30:10", message: "加载市场管理模块" },
        { level: "warn", timestamp: "2026-01-31 10:30:15", message: "存储路径权限警告" },
        { level: "info", timestamp: "2026-01-31 10:30:20", message: "系统初始化完成" },
        { level: "info", timestamp: "2026-01-31 10:35:00", message: "用户登录: admin" },
        { level: "error", timestamp: "2026-01-31 10:40:00", message: "技能执行失败: text-to-uppercase-skill" },
        { level: "info", timestamp: "2026-01-31 10:45:00", message: "技能发布成功: code-generation-skill" },
        { level: "info", timestamp: "2026-01-31 10:50:00", message: "用户登出: admin" }
    ];

    // 页面加载时渲染日志
    document.addEventListener('DOMContentLoaded', function() {
        renderLogs();
    });

    // 渲染日志
    function renderLogs() {
        const logList = document.getElementById('logList');
        if (!logList) return;
        
        logList.innerHTML = '';
        
        systemLogData.forEach(log => {
            const logItem = document.createElement('div');
            logItem.className = `log-item ${log.level}`;
            logItem.innerHTML = `
                <div><strong>[${log.timestamp}]</strong> [${log.level.toUpperCase()}]</div>
                <div>${log.message}</div>
            `;
            logList.appendChild(logItem);
        });
    }

    // 重启系统
    window.restartSystem = function() {
        if (confirm('确定要重启系统吗？')) {
            alert('系统重启中...');
        }
    };

    // 关闭系统
    window.shutdownSystem = function() {
        if (confirm('确定要关闭系统吗？')) {
            alert('系统关闭中...');
        }
    };
})();
