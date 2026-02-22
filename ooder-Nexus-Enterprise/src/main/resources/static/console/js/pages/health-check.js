(function(global) {
    'use strict';

    var services = [
        { name: 'Nexus Service', status: 'running', port: 9876, responseTime: 15 },
        { name: 'Web Console', status: 'running', port: 8080, responseTime: 20 },
        { name: 'API Service', status: 'running', port: 8081, responseTime: 10 },
        { name: 'Monitoring Service', status: 'running', port: 9090, responseTime: 5 }
    ];

    var HealthCheck = {
        init: function() {
            window.onPageInit = function() {
                console.log('健康检查页面初始化完成');
                HealthCheck.renderServices();
                HealthCheck.updateHealthData();
            };
        },

        renderServices: function() {
            var tbody = document.getElementById('serviceTableBody');
            tbody.innerHTML = '';
            services.forEach(function(service) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + service.name + '</td>' +
                    '<td><span class="nx-text-success">运行中</span></td>' +
                    '<td>' + service.port + '</td>' +
                    '<td>' + service.responseTime + 'ms</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="restartService(\'' + service.name + '\')">重启</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        updateHealthData: function() {
            document.getElementById('lastCheck').textContent = new Date().toLocaleString('zh-CN');
            document.getElementById('checkDuration').textContent = '1.2s';
        },

        runHealthCheck: function() {
            alert('正在运行健康检查...');
            setTimeout(function() {
                HealthCheck.updateHealthData();
                alert('健康检查完成！');
            }, 1500);
        },

        exportHealthReport: function() {
            var report = { status: 'healthy', score: 98, services: services };
            var blob = new Blob([JSON.stringify(report, null, 2)], { type: 'application/json' });
            var url = URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = url;
            a.download = 'health-report.json';
            a.click();
            URL.revokeObjectURL(url);
        },

        restartService: function(name) {
            alert('重启服务: ' + name);
        }
    };

    HealthCheck.init();

    global.runHealthCheck = HealthCheck.runHealthCheck;
    global.exportHealthReport = HealthCheck.exportHealthReport;
    global.restartService = HealthCheck.restartService;

})(typeof window !== 'undefined' ? window : this);
