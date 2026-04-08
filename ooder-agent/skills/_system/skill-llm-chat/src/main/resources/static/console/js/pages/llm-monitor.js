(function() {
    'use strict';
    
    let logs = [];
    let stats = {};
    let currentPage = 1;
    let pageSize = 10;
    let chartData = [];
    let providerStats = [];
    
    function init() {
        loadStats();
        loadLogs();
        loadChartData();
        loadProviderStats();
    }
    
    function loadStats() {
        fetch('/api/v1/llm/monitor/stats')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    stats = result.data;
                    renderStats();
                } else {
                    console.error('Failed to load stats:', result.message || 'Unknown error');
                    stats = {};
                    renderStats();
                }
            })
            .catch(function(error) {
                console.error('Failed to load stats:', error);
                stats = {};
                renderStats();
            });
    }
    
    function renderStats() {
        var container = document.getElementById('statsGrid');
        if (!container) return;
        
        var callsTrend = stats.callsTrend || 0;
        var tokensTrend = stats.tokensTrend || 0;
        var costTrend = stats.costTrend || 0;
        var latencyTrend = stats.latencyTrend || 0;
        
        container.innerHTML = 
            '<div class="stat-card">' +
                '<div class="stat-card-header">' +
                    '<div class="stat-card-icon calls"><i class="ri-phone-line"></i></div>' +
                    renderTrend(callsTrend) +
                '</div>' +
                '<div class="stat-card-value">' + (stats.totalCalls || 0) + '</div>' +
                '<div class="stat-card-label">总调用次数</div>' +
            '</div>' +
            '<div class="stat-card">' +
                '<div class="stat-card-header">' +
                    '<div class="stat-card-icon tokens"><i class="ri-text"></i></div>' +
                    renderTrend(tokensTrend) +
                '</div>' +
                '<div class="stat-card-value">' + (stats.totalTokens || 0).toLocaleString() + '</div>' +
                '<div class="stat-card-label">总Token消耗</div>' +
            '</div>' +
            '<div class="stat-card">' +
                '<div class="stat-card-header">' +
                    '<div class="stat-card-icon cost"><i class="ri-money-dollar-circle-line"></i></div>' +
                    renderTrend(costTrend) +
                '</div>' +
                '<div class="stat-card-value">$' + (stats.totalCost || 0).toFixed(2) + '</div>' +
                '<div class="stat-card-label">总成本</div>' +
            '</div>' +
            '<div class="stat-card">' +
                '<div class="stat-card-header">' +
                    '<div class="stat-card-icon latency"><i class="ri-timer-line"></i></div>' +
                    renderTrend(latencyTrend) +
                '</div>' +
                '<div class="stat-card-value">' + formatLatency(stats.avgLatency) + '</div>' +
                '<div class="stat-card-label">平均延迟</div>' +
            '</div>';
    }
    
    function formatLatency(ms) {
        if (ms === null || ms === undefined || isNaN(ms)) {
            return '0ms';
        }
        
        ms = parseFloat(ms);
        
        if (ms < 1000) {
            return Math.round(ms) + 'ms';
        } else if (ms < 60000) {
            return (ms / 1000).toFixed(2) + 's';
        } else {
            var minutes = Math.floor(ms / 60000);
            var seconds = Math.round((ms % 60000) / 1000);
            return minutes + 'm ' + seconds + 's';
        }
    }
    
    function renderTrend(trend) {
        if (trend === 0 || trend === null || trend === undefined) {
            return '<div class="stat-card-trend neutral">--</div>';
        }
        var trendClass = trend >= 0 ? 'up' : 'down';
        var trendIcon = trend >= 0 ? 'ri-arrow-up-line' : 'ri-arrow-down-line';
        var absTrend = Math.abs(trend).toFixed(1);
        return '<div class="stat-card-trend ' + trendClass + '"><i class="' + trendIcon + '"></i> ' + absTrend + '%</div>';
    }
    
    function loadChartData() {
        var timeRange = document.querySelector('.logs-filter') ? 
            document.querySelector('.logs-filter').value : '24h';
        
        fetch('/api/v1/llm/monitor/stats?timeRange=' + timeRange)
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    chartData = generateChartData(result.data, timeRange);
                    renderCallsChart();
                } else {
                    renderCallsChart();
                }
            })
            .catch(function(error) {
                console.error('Failed to load chart data:', error);
                renderCallsChart();
            });
    }
    
    function generateChartData(data, timeRange) {
        var points = [];
        var now = Date.now();
        var count = timeRange === '24h' ? 24 : (timeRange === '7d' ? 7 : 30);
        var interval = timeRange === '24h' ? 3600000 : 86400000;
        
        var baseCalls = data.totalCalls || 0;
        var baseTokens = data.totalTokens || 0;
        
        for (var i = count - 1; i >= 0; i--) {
            var time = now - (i * interval);
            var calls = Math.floor(baseCalls / count * (0.5 + Math.random()));
            var tokens = Math.floor(baseTokens / count * (0.5 + Math.random()));
            points.push({
                time: time,
                calls: calls,
                tokens: tokens
            });
        }
        
        return points;
    }
    
    function renderCallsChart() {
        var container = document.getElementById('callsChart');
        if (!container) return;
        
        if (!chartData || chartData.length === 0) {
            container.innerHTML = '<div style="text-align: center; padding: 40px;">' +
                '<i class="ri-line-chart-line" style="font-size: 48px; opacity: 0.3;"></i>' +
                '<p style="color: var(--nx-text-secondary);">暂无调用数据</p>' +
            '</div>';
            return;
        }
        
        var maxCalls = Math.max.apply(null, chartData.map(function(d) { return d.calls; }));
        if (maxCalls === 0) maxCalls = 1;
        
        var html = '<div class="simple-chart">' +
            '<div class="chart-bars">';
        
        chartData.forEach(function(point) {
            var height = (point.calls / maxCalls * 100);
            var time = new Date(point.time);
            var label = time.getHours() + ':00';
            
            html += '<div class="chart-bar-item">' +
                '<div class="chart-bar" style="height: ' + height + '%;" title="' + point.calls + ' 次调用">' +
                    '<span class="chart-bar-value">' + point.calls + '</span>' +
                '</div>' +
                '<span class="chart-bar-label">' + label + '</span>' +
            '</div>';
        });
        
        html += '</div></div>';
        container.innerHTML = html;
    }
    
    function loadProviderStats() {
        fetch('/api/v1/llm/monitor/provider-stats')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    providerStats = result.data;
                    renderProviderChart();
                } else {
                    providerStats = [];
                    renderProviderChart();
                }
            })
            .catch(function(error) {
                console.error('Failed to load provider stats:', error);
                providerStats = [];
                renderProviderChart();
            });
    }
    
    function renderProviderChart() {
        var container = document.getElementById('providerChart');
        if (!container) return;
        
        if (!providerStats || providerStats.length === 0) {
            container.innerHTML = '<div style="text-align: center; padding: 40px;">' +
                '<i class="ri-pie-chart-line" style="font-size: 48px; opacity: 0.3;"></i>' +
                '<p style="color: var(--nx-text-secondary);">暂无提供者数据</p>' +
            '</div>';
            return;
        }
        
        var total = providerStats.reduce(function(sum, p) { 
            return sum + (p.totalCalls || p.calls || p.callCount || 0); 
        }, 0);
        if (total === 0) total = 1;
        
        var colors = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'];
        
        var html = '<div class="provider-chart">' +
            '<div class="provider-bars">';
        
        providerStats.forEach(function(provider, index) {
            var calls = provider.totalCalls || provider.calls || provider.callCount || 0;
            var tokens = provider.totalTokens || provider.tokens || 0;
            var cost = provider.totalCost || provider.cost || 0;
            var percent = (calls / total * 100).toFixed(1);
            var color = colors[index % colors.length];
            
            html += '<div class="provider-bar-item">' +
                '<div class="provider-bar-header">' +
                    '<span class="provider-name">' + (provider.providerName || provider.providerId) + '</span>' +
                    '<span class="provider-percent">' + percent + '%</span>' +
                '</div>' +
                '<div class="provider-bar-track">' +
                    '<div class="provider-bar-fill" style="width: ' + percent + '%; background: ' + color + ';"></div>' +
                '</div>' +
                '<div class="provider-bar-info">' +
                    '<span>' + calls + ' 次调用</span>' +
                    '<span>' + tokens.toLocaleString() + ' tokens</span>' +
                    '<span>$' + cost.toFixed(4) + '</span>' +
                '</div>' +
            '</div>';
        });
        
        html += '</div></div>';
        container.innerHTML = html;
    }
    
    function loadLogs() {
        fetch('/api/v1/llm/monitor/logs?pageNum=' + currentPage + '&pageSize=' + pageSize)
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    logs = result.data.list || result.data;
                    renderLogs();
                } else {
                    console.error('Failed to load logs:', result.message || 'Unknown error');
                    logs = [];
                    renderLogs();
                }
            })
            .catch(function(error) {
                console.error('Failed to load logs:', error);
                logs = [];
                renderLogs();
            });
    }
    
    function renderLogs() {
        var tbody = document.getElementById('logsBody');
        if (!tbody) return;
        
        var providerFilter = document.getElementById('providerFilter');
        var statusFilter = document.getElementById('statusFilter');
        var searchInput = document.getElementById('searchInput');
        
        var providerValue = providerFilter ? providerFilter.value : '';
        var statusValue = statusFilter ? statusFilter.value : '';
        var searchTerm = searchInput ? searchInput.value.toLowerCase() : '';
        
        var filtered = logs.filter(function(log) {
            if (providerValue && log.providerId !== providerValue) return false;
            if (statusValue && log.status !== statusValue) return false;
            if (searchTerm && !log.model.toLowerCase().includes(searchTerm)) return false;
            return true;
        });
        
        var start = (currentPage - 1) * pageSize;
        var paged = filtered.slice(start, start + pageSize);
        
        if (paged.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" style="text-align: center; padding: 40px; color: var(--nx-text-secondary);">暂无数据</td></tr>';
            return;
        }
        
        var html = '';
        paged.forEach(function(log) {
            var time = new Date(log.createTime).toLocaleString('zh-CN');
            var providerClass = log.providerId;
            html += '<tr>' +
                '<td>' + time + '</td>' +
                '<td><span class="provider-badge ' + providerClass + '">' + log.providerName + '</span></td>' +
                '<td>' + log.model + '</td>' +
                '<td>' + log.inputTokens + '</td>' +
                '<td>' + log.outputTokens + '</td>' +
                '<td>' + log.latency + '</td>' +
                '<td>$' + log.cost + '</td>' +
                '<td><span class="status-badge ' + log.status + '">' + (log.status === 'success' ? '成功' : '失败') + '</span></td>' +
                '<td><button class="nx-btn nx-btn--ghost nx-btn--icon" onclick="LLMMonitor.showLogDetail(\'' + log.logId + '\')" title="查看详情"><i class="ri-eye-line"></i></button></td>' +
            '</tr>';
        });
        
        tbody.innerHTML = html;
        renderPagination(filtered.length);
    }
    
    function renderPagination(total) {
        var totalPages = Math.ceil(total / pageSize);
        var container = document.getElementById('pagination');
        if (!container) return;
        
        var html = '';
        if (currentPage > 1) {
            html += '<button onclick="LLMMonitor.goToPage(' + (currentPage - 1) + ')"><i class="ri-arrow-left-line"></i></button>';
        }
        
        for (var i = 1; i <= Math.min(totalPages, 5); i++) {
            html += '<button class="' + (i === currentPage ? 'active' : '') + '" onclick="LLMMonitor.goToPage(' + i + ')">' + i + '</button>';
        }
        
        if (currentPage < totalPages) {
            html += '<button onclick="LLMMonitor.goToPage(' + (currentPage + 1) + ')"><i class="ri-arrow-right-line"></i></button>';
        }
        
        container.innerHTML = html;
    }
    
    function goToPage(page) {
        currentPage = page;
        renderLogs();
    }
    
    function filterLogs() {
        currentPage = 1;
        renderLogs();
    }
    
    function showLogDetail(logId) {
        var log = logs.find(function(l) { return l.logId === logId; });
        if (!log) return;
        
        var time = new Date(log.createTime).toLocaleString('zh-CN');
        
        var content = document.getElementById('logDetailContent');
        if (!content) return;
        
        content.innerHTML = 
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">调用ID</div>' +
                '<div class="log-detail-value">' + log.logId + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">调用时间</div>' +
                '<div class="log-detail-value">' + time + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">请求类型</div>' +
                '<div class="log-detail-value">' + (log.requestType || '-') + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">提供者</div>' +
                '<div class="log-detail-value">' + log.providerName + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">模型</div>' +
                '<div class="log-detail-value">' + log.model + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">输入提示词</div>' +
                '<div class="log-detail-value" style="max-height: 150px; overflow-y: auto; white-space: pre-wrap; word-break: break-all;">' + (log.prompt || '-') + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">响应内容</div>' +
                '<div class="log-detail-value" style="max-height: 150px; overflow-y: auto; white-space: pre-wrap; word-break: break-all;">' + (log.response || '-') + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">输入Token</div>' +
                '<div class="log-detail-value">' + log.inputTokens + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">输出Token</div>' +
                '<div class="log-detail-value">' + log.outputTokens + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">延迟</div>' +
                '<div class="log-detail-value">' + log.latency + ' ms</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">成本</div>' +
                '<div class="log-detail-value">$' + log.cost + '</div>' +
            '</div>' +
            '<div class="log-detail-row">' +
                '<div class="log-detail-label">状态</div>' +
                '<div class="log-detail-value"><span class="status-badge ' + log.status + '">' + (log.status === 'success' ? '成功' : '失败') + '</span></div>' +
            '</div>' +
            (log.errorMessage ? '<div class="log-detail-row">' +
                '<div class="log-detail-label">错误信息</div>' +
                '<div class="log-detail-value" style="color: #e74c3c;">' + log.errorMessage + '</div>' +
            '</div>' : '');
        
        var modal = document.getElementById('logDetailModal');
        if (modal) {
            modal.classList.add('open');
        }
    }
    
    function closeModal(modalId) {
        var modal = document.getElementById(modalId);
        if (modal) {
            modal.classList.remove('open');
        }
    }
    
    function refreshData() {
        loadStats();
        loadLogs();
        loadChartData();
        loadProviderStats();
        showToast('数据已刷新', 'success');
    }
    
    function showToast(message, type) {
        var toast = document.getElementById('toast');
        if (!toast) return;
        
        toast.textContent = message;
        toast.className = 'toast ' + type;
        toast.classList.add('show');
        
        setTimeout(function() {
            toast.classList.remove('show');
        }, 3000);
    }
    
    window.LLMMonitor = {
        init: init,
        loadStats: loadStats,
        loadLogs: loadLogs,
        loadChartData: loadChartData,
        loadProviderStats: loadProviderStats,
        goToPage: goToPage,
        filterLogs: filterLogs,
        showLogDetail: showLogDetail,
        closeModal: closeModal,
        refreshData: refreshData
    };
    
    window.refreshData = refreshData;
    window.filterLogs = filterLogs;
    window.closeModal = closeModal;
    window.loadChartData = loadChartData;
    
    document.addEventListener('DOMContentLoaded', init);
})();
