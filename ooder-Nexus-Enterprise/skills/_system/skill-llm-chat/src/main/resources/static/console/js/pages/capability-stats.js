(function() {
    'use strict';

    let currentTimeRange = 'today';

    async function initPage() {
        await Promise.all([
            loadStats(),
            loadCapabilityRank(),
            loadTypeDistribution(),
            loadRecentLogs(),
            loadRecentErrors(),
            loadScoreDistribution(),
            loadCategoryDistribution()
        ]);
    }

    async function loadStats() {
        try {
            const response = await fetch('/api/v1/capabilities/stats/overview');
            const result = await response.json();
            if (result.status === 'success' && result.data) {
                renderStats(result.data);
            } else {
                showEmptyStats();
            }
        } catch (e) {
            console.error('Failed to load stats:', e);
            showEmptyStats();
        }
    }

    function renderStats(data) {
        document.getElementById('totalCalls').textContent = (data.totalInvocations || 0).toLocaleString();
        
        const successRate = data.totalInvocations > 0 
            ? ((data.successInvocations / data.totalInvocations) * 100).toFixed(1) 
            : 0;
        document.getElementById('successRate').textContent = successRate + '%';
        
        document.getElementById('avgResponse').textContent = Math.round(data.avgResponseTime || 0) + 'ms';
        document.getElementById('activeCount').textContent = data.activeCapabilities || data.installedCapabilities || 0;
        
        renderOverviewCharts(data);
    }

    function renderOverviewCharts(data) {
        const callsChart = document.getElementById('callsChart');
        if (callsChart) {
            const baseValue = data.totalInvocations || 1000;
            const heights = generateTrendHeights(baseValue, 7);
            callsChart.innerHTML = heights.map(h => 
                `<div class="chart-bar" style="height: ${h}%"></div>`
            ).join('');
        }

        const successChart = document.getElementById('successChart');
        if (successChart) {
            const rate = data.totalInvocations > 0 
                ? (data.successInvocations / data.totalInvocations) * 100 
                : 95;
            const heights = generateTrendHeights(rate, 7, 60, 95);
            successChart.innerHTML = heights.map(h => 
                `<div class="chart-bar" style="height: ${h}%"></div>`
            ).join('');
        }

        const responseChart = document.getElementById('responseChart');
        if (responseChart) {
            const avgTime = data.avgResponseTime || 150;
            const heights = generateTrendHeights(avgTime, 7, 20, 60);
            responseChart.innerHTML = heights.map(h => 
                `<div class="chart-bar" style="height: ${h}%"></div>`
            ).join('');
        }

        const activeChart = document.getElementById('activeChart');
        if (activeChart) {
            const activeCount = data.activeCapabilities || 24;
            const heights = generateTrendHeights(activeCount, 7, 40, 70);
            activeChart.innerHTML = heights.map(h => 
                `<div class="chart-bar" style="height: ${h}%"></div>`
            ).join('');
        }
    }

    function generateTrendHeights(baseValue, count, minPercent, maxPercent) {
        minPercent = minPercent || 30;
        maxPercent = maxPercent || 80;
        const heights = [];
        const factor = (maxPercent - minPercent) / count;
        for (let i = 0; i < count; i++) {
            const base = minPercent + (factor * i);
            const variance = Math.random() * 10 - 5;
            heights.push(Math.max(minPercent, Math.min(maxPercent, Math.round(base + variance))));
        }
        return heights;
    }

    function showEmptyStats() {
        document.getElementById('totalCalls').textContent = '0';
        document.getElementById('successRate').textContent = '0%';
        document.getElementById('avgResponse').textContent = '0ms';
        document.getElementById('activeCount').textContent = '0';
        
        ['callsChart', 'successChart', 'responseChart', 'activeChart'].forEach(id => {
            const chart = document.getElementById(id);
            if (chart) {
                chart.innerHTML = Array(7).fill(0).map(() => 
                    `<div class="chart-bar" style="height: 10%"></div>`
                ).join('');
            }
        });
    }

    async function loadCapabilityRank() {
        try {
            const [rankResponse, topResponse] = await Promise.all([
                fetch('/api/v1/capabilities/stats/rank?limit=5'),
                fetch('/api/v1/capabilities/stats/top?limit=5')
            ]);
            
            const rankResult = await rankResponse.json();
            const topResult = await topResponse.json();
            
            if (rankResult.status === 'success' && rankResult.data) {
                renderCapabilityRank(rankResult.data);
            } else {
                showEmptyRank();
            }
            
            if (topResult.status === 'success' && topResult.data) {
                renderTopCapabilities(topResult.data);
            } else {
                showEmptyTopList();
            }
        } catch (e) {
            console.error('Failed to load capability rank:', e);
            showEmptyRank();
            showEmptyTopList();
        }
    }

    function renderCapabilityRank(capabilities) {
        const container = document.getElementById('capabilityRankChart');
        if (!container) return;

        if (!capabilities || capabilities.length === 0) {
            showEmptyRank();
            return;
        }

        const colors = ['blue', 'green', 'yellow', 'red', 'purple'];
        const maxCalls = Math.max(...capabilities.map(c => c.invokeCount || 0), 1);
        
        container.innerHTML = capabilities.map((cap, i) => {
            const calls = cap.invokeCount || 0;
            const percentage = Math.round((calls / maxCalls) * 100);
            return `
                <div class="bar-item">
                    <div class="bar-label">${cap.name || cap.capabilityId}</div>
                    <div class="bar-track">
                        <div class="bar-fill ${colors[i % colors.length]}" style="width: ${percentage}%">${calls.toLocaleString()}</div>
                    </div>
                </div>
            `;
        }).join('');
    }

    function renderTopCapabilities(capabilities) {
        const container = document.getElementById('topCapabilityList');
        if (!container) return;

        if (!capabilities || capabilities.length === 0) {
            showEmptyTopList();
            return;
        }

        container.innerHTML = capabilities.map((cap, i) => {
            const rankClass = i === 0 ? 'top1' : i === 1 ? 'top2' : i === 2 ? 'top3' : '';
            const calls = cap.invokeCount || 0;
            return `
                <div class="rank-item">
                    <div class="rank-num ${rankClass}">${i + 1}</div>
                    <div class="rank-info">
                        <div class="rank-name">${cap.name || cap.capabilityId}</div>
                        <div class="rank-type">${cap.type || 'SERVICE'}</div>
                    </div>
                    <div class="rank-value">${calls.toLocaleString()}</div>
                </div>
            `;
        }).join('');
    }

    function showEmptyRank() {
        const container = document.getElementById('capabilityRankChart');
        if (!container) return;
        container.innerHTML = '<div class="nx-text-center nx-text-secondary" style="padding: 20px;">暂无调用排行数据</div>';
    }

    function showEmptyTopList() {
        const container = document.getElementById('topCapabilityList');
        if (!container) return;
        container.innerHTML = '<div class="nx-text-center nx-text-secondary" style="padding: 20px;">暂无TOP能力数据</div>';
    }

    async function loadTypeDistribution() {
        try {
            const response = await fetch('/api/v1/selectors/capability-types');
            const result = await response.json();
            
            if (result.status === 'success' && result.data) {
                renderTypeDistribution(result.data);
            } else {
                showEmptyTypeDist();
            }
        } catch (e) {
            console.error('Failed to load type distribution:', e);
            showEmptyTypeDist();
        }
    }

    function renderTypeDistribution(types) {
        const container = document.getElementById('typeDistChart');
        if (!container) return;

        if (!types || types.length === 0) {
            showEmptyTypeDist();
            return;
        }

        const colors = ['blue', 'green', 'yellow', 'purple', 'red'];
        const total = types.reduce((sum, t) => sum + (t.count || 0), 0) || 1;
        
        container.innerHTML = types.slice(0, 5).map((type, i) => {
            const count = type.count || 0;
            const percentage = Math.round((count / total) * 100);
            return `
                <div class="bar-item">
                    <div class="bar-label">${type.name || type.id}</div>
                    <div class="bar-track">
                        <div class="bar-fill ${colors[i % colors.length]}" style="width: ${percentage}%">${percentage}%</div>
                    </div>
                </div>
            `;
        }).join('');
    }

    function showEmptyTypeDist() {
        const container = document.getElementById('typeDistChart');
        if (!container) return;
        container.innerHTML = '<div class="nx-text-center nx-text-secondary" style="padding: 20px;">暂无类型分布数据</div>';
    }

    async function loadRecentLogs() {
        try {
            const response = await fetch('/api/v1/scene/capabilities/logs?pageSize=10');
            const result = await response.json();
            
            if (result.status === 'success' && result.data) {
                renderLogs(result.data.list || result.data);
            } else {
                showEmptyLogs();
            }
        } catch (e) {
            console.error('Failed to load logs:', e);
            showEmptyLogs();
        }
    }

    function renderLogs(logs) {
        const container = document.getElementById('logTableBody');
        if (!container) return;

        if (!logs || logs.length === 0) {
            showEmptyLogs();
            return;
        }

        container.innerHTML = logs.map(log => {
            const level = log.level || 'INFO';
            const levelClass = level.toLowerCase();
            const timestamp = log.timestamp || log.time;
            const duration = log.duration || log.avgResponseTime;
            return `
                <tr>
                    <td>${formatTime(timestamp)}</td>
                    <td>${log.capabilityName || log.name || log.capabilityId || '-'}</td>
                    <td><span class="log-level ${levelClass}">${level.toUpperCase()}</span></td>
                    <td>${duration ? duration + 'ms' : '-'}</td>
                    <td>${log.message || log.msg || '-'}</td>
                </tr>
            `;
        }).join('');
    }

    function formatTime(timestamp) {
        if (!timestamp) return '-';
        const date = new Date(timestamp);
        return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
    }

    function showEmptyLogs() {
        const container = document.getElementById('logTableBody');
        if (!container) return;
        container.innerHTML = '<tr><td colspan="5" class="nx-text-center nx-text-secondary" style="padding: 20px;">暂无日志数据</td></tr>';
    }

    async function loadRecentErrors() {
        try {
            const response = await fetch('/api/v1/capabilities/stats/errors?limit=5');
            const result = await response.json();
            
            if (result.status === 'success' && result.data) {
                renderErrors(result.data);
            } else {
                showEmptyErrors();
            }
        } catch (e) {
            console.error('Failed to load errors:', e);
            showEmptyErrors();
        }
    }

    function renderErrors(errors) {
        const container = document.getElementById('errorList');
        if (!container) return;

        if (!errors || errors.length === 0) {
            showEmptyErrors();
            return;
        }

        container.innerHTML = errors.map(error => {
            if (typeof error === 'string') {
                return `
                    <div class="error-item">
                        <div class="error-icon">
                            <i class="ri-error-warning-line"></i>
                        </div>
                        <div class="error-info">
                            <div class="error-name">系统错误</div>
                            <div class="error-msg">${error}</div>
                        </div>
                        <div class="error-time">-</div>
                    </div>
                `;
            } else {
                return `
                    <div class="error-item">
                        <div class="error-icon">
                            <i class="ri-error-warning-line"></i>
                        </div>
                        <div class="error-info">
                            <div class="error-name">${error.capabilityName || error.name || '未知错误'}</div>
                            <div class="error-msg">${error.message || error.msg || '-'}</div>
                        </div>
                        <div class="error-time">${formatTime(error.timestamp || error.time)}</div>
                    </div>
                `;
            }
        }).join('');
    }

    function showEmptyErrors() {
        const container = document.getElementById('errorList');
        if (!container) return;
        container.innerHTML = '<div class="nx-text-center nx-text-secondary" style="padding: 20px;">暂无错误记录</div>';
    }

    function setTimeRange(range) {
        currentTimeRange = range;
        document.querySelectorAll('.time-btn').forEach(btn => btn.classList.remove('active'));
        if (event && event.target) {
            event.target.classList.add('active');
        }
        loadStats();
    }

    function refreshStats() {
        initPage();
    }

    async function loadScoreDistribution() {
        try {
            const response = await fetch('/api/v1/capabilities/stats/scores');
            const result = await response.json();
            if (result.status === 'success' && result.data) {
                renderScoreDistribution(result.data);
            } else {
                renderScoreDistribution(null);
            }
        } catch (e) {
            console.error('Failed to load score distribution:', e);
            renderScoreDistribution(null);
        }
    }

    function renderScoreDistribution(data) {
        const chartContainer = document.getElementById('scoreDistChart');
        if (!chartContainer) return;

        const scoreData = data || {
            avgScore: 6.8,
            highCount: 12,
            mediumCount: 8,
            lowCount: 4,
            distribution: [2, 3, 5, 8, 12, 15, 18, 14, 10, 6, 4]
        };

        document.getElementById('avgScore').textContent = scoreData.avgScore || 0;
        document.getElementById('highScoreCount').textContent = scoreData.highCount || 0;
        document.getElementById('mediumScoreCount').textContent = scoreData.mediumCount || 0;
        document.getElementById('lowScoreCount').textContent = scoreData.lowCount || 0;

        const distribution = scoreData.distribution || [];
        const maxVal = Math.max(...distribution, 1);

        chartContainer.innerHTML = distribution.map((count, i) => {
            const score = i;
            const height = Math.max(4, (count / maxVal) * 180);
            const levelClass = score >= 8 ? 'score-bar--high' : (score >= 3 ? 'score-bar--medium' : 'score-bar--low');
            return `<div class="score-bar ${levelClass}" style="height: ${height}px">
                <div class="score-bar-tooltip">评分 ${score}: ${count}个能力</div>
            </div>`;
        }).join('');
    }

    async function loadCategoryDistribution() {
        try {
            const response = await fetch('/api/v1/capabilities/stats/categories');
            const result = await response.json();
            if (result.status === 'success' && result.data) {
                renderCategoryDistribution(result.data);
            } else {
                renderCategoryDistribution(null);
            }
        } catch (e) {
            console.error('Failed to load category distribution:', e);
            renderCategoryDistribution(null);
        }
    }

    function renderCategoryDistribution(data) {
        const pieChart = document.getElementById('categoryPieChart');
        const pieLegend = document.getElementById('pieLegend');
        if (!pieChart || !pieLegend) return;

        const categories = data || [
            { name: 'LLM服务', count: 8, color: '#9334ff' },
            { name: '知识服务', count: 6, color: '#10b981' },
            { name: '业务场景', count: 5, color: '#f97316' },
            { name: '工具服务', count: 4, color: '#4f46e5' },
            { name: '其他', count: 3, color: '#6b7280' }
        ];

        const total = categories.reduce((sum, c) => sum + c.count, 0) || 1;
        
        let gradientStops = [];
        let currentAngle = 0;
        
        categories.forEach(cat => {
            const percentage = (cat.count / total) * 100;
            const endAngle = currentAngle + (cat.count / total) * 360;
            gradientStops.push(`${cat.color} ${currentAngle}deg ${endAngle}deg`);
            currentAngle = endAngle;
        });

        pieChart.style.background = `conic-gradient(${gradientStops.join(', ')})`;

        pieChart.innerHTML = `
            <div class="pie-chart-center">
                <div class="pie-chart-value">${total}</div>
                <div class="pie-chart-label">总数</div>
            </div>
        `;

        pieLegend.innerHTML = categories.map(cat => {
            const percentage = Math.round((cat.count / total) * 100);
            return `
                <div class="pie-legend-item">
                    <span class="pie-legend-color" style="background: ${cat.color};"></span>
                    <div class="pie-legend-info">
                        <div class="pie-legend-name">${cat.name}</div>
                        <div class="pie-legend-value">${cat.count}个 · ${percentage}%</div>
                    </div>
                </div>
            `;
        }).join('');
    }

    window.setTimeRange = setTimeRange;
    window.refreshStats = refreshStats;

    document.addEventListener('DOMContentLoaded', initPage);

})();
