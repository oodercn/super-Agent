(function(global) {
    'use strict';

    var LinkManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('链路管理页面初始化完成');
                LinkManagement.loadLinks();
                LinkManagement.loadNetworkStats();
                LinkManagement.loadMonitorStatus();
            };
        },

        loadLinks: async function() {
            try {
                var response = await fetch('/api/network/quality/all');
                var result = await response.json();
                
                if (result.requestStatus === 200 && result.data) {
                    LinkManagement.renderLinks(result.data);
                    LinkManagement.updateLinkStats(result.data);
                } else {
                    console.warn('使用模拟数据');
                    LinkManagement.loadMockData();
                }
            } catch (error) {
                console.error('加载链路数据失败:', error);
                LinkManagement.loadMockData();
            }
        },

        loadMockData: function() {
            var mockLinks = [
                { linkId: 'link-001', sourceId: 'mcp-001', targetId: 'route-001', latency: 15, packetLoss: 0.1, bandwidth: 1000, qualityLevel: 'EXCELLENT', score: 95 },
                { linkId: 'link-002', sourceId: 'mcp-001', targetId: 'route-002', latency: 20, packetLoss: 0.2, bandwidth: 800, qualityLevel: 'GOOD', score: 85 },
                { linkId: 'link-003', sourceId: 'route-001', targetId: 'end-001', latency: 35, packetLoss: 0.5, bandwidth: 500, qualityLevel: 'FAIR', score: 70 },
                { linkId: 'link-004', sourceId: 'route-002', targetId: 'end-002', latency: 100, packetLoss: 2.0, bandwidth: 200, qualityLevel: 'POOR', score: 40 }
            ];
            LinkManagement.renderLinks(mockLinks);
            LinkManagement.updateLinkStats(mockLinks);
        },

        renderLinks: function(links) {
            var tbody = document.getElementById('linksTableBody');
            tbody.innerHTML = '';
            
            links.forEach(function(link) {
                var qualityClass = LinkManagement.getQualityClass(link.qualityLevel);
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + link.linkId + '</td>' +
                    '<td>' + link.sourceId + '</td>' +
                    '<td>' + link.targetId + '</td>' +
                    '<td>' + (link.linkType || 'DIRECT') + '</td>' +
                    '<td><span class="nx-badge nx-badge--' + qualityClass + '">' + (link.qualityLevel || 'UNKNOWN') + '</span></td>' +
                    '<td>' + (link.latency || 0) + 'ms</td>' +
                    '<td>' + (link.packetLoss || 0).toFixed(2) + '%</td>' +
                    '<td>' + (link.bandwidth || 0) + 'Mbps</td>' +
                    '<td>' +
                    '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="viewLinkDetail(\'' + link.linkId + '\')">详情</button> ' +
                    '<button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeLink(\'' + link.linkId + '\')">删除</button>' +
                    '</td>';
                tbody.appendChild(row);
            });
        },

        getQualityClass: function(level) {
            var classes = {
                'EXCELLENT': 'success',
                'GOOD': 'info',
                'FAIR': 'warning',
                'POOR': 'danger'
            };
            return classes[level] || 'secondary';
        },

        updateLinkStats: function(links) {
            var total = links.length;
            var excellent = 0, good = 0, fair = 0, poor = 0;
            var totalLatency = 0;
            
            links.forEach(function(link) {
                totalLatency += link.latency || 0;
                switch(link.qualityLevel) {
                    case 'EXCELLENT': excellent++; break;
                    case 'GOOD': good++; break;
                    case 'FAIR': fair++; break;
                    case 'POOR': poor++; break;
                }
            });
            
            document.getElementById('linkCount').textContent = total;
            document.getElementById('activeLinkCount').textContent = excellent + good;
            document.getElementById('errorLinkCount').textContent = poor;
            document.getElementById('avgResponseTime').textContent = total > 0 ? Math.round(totalLatency / total) + 'ms' : '0ms';
            document.getElementById('packetSuccessRate').textContent = total > 0 ? Math.round((1 - poor / total) * 100) + '%' : '100%';
        },

        loadNetworkStats: async function() {
            try {
                var response = await fetch('/api/network/quality/stats');
                var result = await response.json();
                
                if (result.requestStatus === 200 && result.data) {
                    document.getElementById('bandwidthUsage').textContent = (result.data.bandwidthUsage || 45) + '%';
                }
            } catch (error) {
                console.error('加载网络统计失败:', error);
            }
        },

        loadMonitorStatus: async function() {
            try {
                var response = await fetch('/api/network/quality/monitor/status');
                var result = await response.json();
                
                if (result.requestStatus === 200 && result.data) {
                    LinkManagement.updateMonitorUI(result.data.enabled);
                }
            } catch (error) {
                console.error('加载监控状态失败:', error);
            }
        },

        updateMonitorUI: function(enabled) {
            var btn = document.getElementById('monitorToggleBtn');
            if (btn) {
                btn.textContent = enabled ? '停止监控' : '启动监控';
                btn.className = enabled ? 'nx-btn nx-btn--danger' : 'nx-btn nx-btn--success';
            }
        },

        toggleMonitor: async function() {
            try {
                var statusRes = await fetch('/api/network/quality/monitor/status');
                var statusResult = await statusRes.json();
                var isEnabled = statusResult.data && statusResult.data.enabled;
                
                var url = isEnabled ? '/api/network/quality/monitor/disable' : '/api/network/quality/monitor/enable';
                var body = isEnabled ? {} : { intervalMs: 5000 };
                
                var response = await fetch(url, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(body)
                });
                
                var result = await response.json();
                if (result.requestStatus === 200) {
                    LinkManagement.updateMonitorUI(!isEnabled);
                    LinkManagement.showSuccess(isEnabled ? '监控已停止' : '监控已启动');
                }
            } catch (error) {
                console.error('切换监控状态失败:', error);
                LinkManagement.showError('操作失败');
            }
        },

        viewLinkDetail: async function(linkId) {
            try {
                var response = await fetch('/api/network/quality/link/' + linkId);
                var result = await response.json();
                
                if (result.requestStatus === 200 && result.data) {
                    var data = result.data;
                    alert('链路详情:\n' +
                        'ID: ' + data.linkId + '\n' +
                        '延迟: ' + data.latency + 'ms\n' +
                        '丢包率: ' + data.packetLoss + '%\n' +
                        '带宽: ' + data.bandwidth + 'Mbps\n' +
                        '质量等级: ' + data.qualityLevel + '\n' +
                        '评分: ' + data.score);
                }
            } catch (error) {
                console.error('获取链路详情失败:', error);
            }
        },

        addLink: function() {
            var sourceId = prompt('请输入源节点ID:');
            var targetId = prompt('请输入目标节点ID:');
            var linkType = prompt('请输入链路类型 (DIRECT/ROUTED/TUNNEL/P2P/RELAY):');
            
            if (sourceId && targetId && linkType) {
                LinkManagement.createLink(sourceId, targetId, linkType);
            }
        },

        createLink: async function(sourceId, targetId, linkType) {
            try {
                var response = await fetch('/api/network/links', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        sourceId: sourceId,
                        targetId: targetId,
                        linkType: linkType
                    })
                });
                
                var result = await response.json();
                if (result.requestStatus === 200) {
                    LinkManagement.showSuccess('链路创建成功');
                    LinkManagement.loadLinks();
                } else {
                    LinkManagement.showError(result.message || '创建失败');
                }
            } catch (error) {
                console.error('创建链路失败:', error);
                LinkManagement.showError('创建失败');
            }
        },

        removeLink: async function(linkId) {
            if (!confirm('确定要删除该链路吗？')) return;
            
            try {
                var response = await fetch('/api/network/links/' + linkId, {
                    method: 'DELETE'
                });
                
                var result = await response.json();
                if (result.requestStatus === 200) {
                    LinkManagement.showSuccess('链路已删除');
                    LinkManagement.loadLinks();
                } else {
                    LinkManagement.showError(result.message || '删除失败');
                }
            } catch (error) {
                console.error('删除链路失败:', error);
                LinkManagement.showError('删除失败');
            }
        },

        refreshLinks: function() {
            LinkManagement.loadLinks();
            LinkManagement.loadNetworkStats();
            LinkManagement.loadMonitorStatus();
        },

        showSuccess: function(message) {
            alert(message);
        },

        showError: function(message) {
            alert('错误: ' + message);
        }
    };

    LinkManagement.init();

    global.addLink = LinkManagement.addLink;
    global.editLink = LinkManagement.viewLinkDetail;
    global.removeLink = LinkManagement.removeLink;
    global.refreshLinks = LinkManagement.refreshLinks;
    global.viewLinkDetail = LinkManagement.viewLinkDetail;
    global.toggleMonitor = LinkManagement.toggleMonitor;
    global.LinkManagement = LinkManagement;

})(typeof window !== 'undefined' ? window : this);
