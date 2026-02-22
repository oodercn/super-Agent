// 链路管理模块
(function() {
    'use strict';

    // 链路数据缓存
    let networkLinks = new Map();

    // DOM元素
    let elements = {
        linkCount: document.getElementById('linkCount'),
        activeLinkCount: document.getElementById('activeLinkCount'),
        errorLinkCount: document.getElementById('errorLinkCount'),
        avgResponseTime: document.getElementById('avgResponseTime'),
        packetSuccessRate: document.getElementById('packetSuccessRate'),
        bandwidthUsage: document.getElementById('bandwidthUsage'),
        linksTableBody: document.getElementById('linksTableBody'),
        addLinkBtn: document.getElementById('addLinkBtn'),
        refreshLinksBtn: document.getElementById('refreshLinksBtn'),
        addLinkModal: document.getElementById('addLinkModal'),
        closeAddLinkModal: document.getElementById('closeAddLinkModal'),
        cancelAddLinkBtn: document.getElementById('cancelAddLinkBtn'),
        submitAddLinkBtn: document.getElementById('submitAddLinkBtn'),
        addLinkForm: document.getElementById('addLinkForm')
    };

    // 初始化
    function init() {
        console.log('Initializing Link Management Module');
        bindEvents();
        loadInitialData();
        setInterval(updateStatus, 5000); // 每5秒更新一次状态
    }

    // 绑定事件
    function bindEvents() {
        if (elements.addLinkBtn) {
            elements.addLinkBtn.addEventListener('click', showAddLinkModal);
        }
        if (elements.refreshLinksBtn) {
            elements.refreshLinksBtn.addEventListener('click', refreshLinks);
        }
        if (elements.closeAddLinkModal) {
            elements.closeAddLinkModal.addEventListener('click', hideAddLinkModal);
        }
        if (elements.cancelAddLinkBtn) {
            elements.cancelAddLinkBtn.addEventListener('click', hideAddLinkModal);
        }
        if (elements.submitAddLinkBtn) {
            elements.submitAddLinkBtn.addEventListener('click', addLink);
        }
    }

    // 加载初始数据
    function loadInitialData() {
        console.log('Loading initial link data...');
        // 模拟加载数据
        setTimeout(() => {
            generateMockData();
            updateLinksTable();
            updateStatus();
        }, 500);
    }

    // 生成模拟数据
    function generateMockData() {
        // 生成链路数据
        const linkTypes = ['direct', 'relay', 'p2p'];
        const statuses = ['active', 'inactive', 'error'];

        for (let i = 1; i <= 12; i++) {
            const linkId = `link_${i}`;
            const linkType = linkTypes[Math.floor(Math.random() * linkTypes.length)];
            const status = i % 4 === 0 ? 'error' : i % 3 === 0 ? 'inactive' : 'active';

            networkLinks.set(linkId, {
                linkId: linkId,
                sourceAgentId: `agent_${Math.floor(Math.random() * 5) + 1}`,
                targetAgentId: `agent_${Math.floor(Math.random() * 5) + 6}`,
                linkType: linkType,
                status: status,
                createTime: new Date(Date.now() - i * 3600000).toISOString(),
                lastHeartbeatTime: new Date(Date.now() - i * 600000).toISOString(),
                healthStats: {
                    responseTime: Math.floor(Math.random() * 100) + 10,
                    packetSuccessRate: Math.floor(Math.random() * 30) + 70,
                    bandwidthUsage: Math.floor(Math.random() * 50) + 10
                }
            });
        }
    }

    // 更新状态
    function updateStatus() {
        if (elements.linkCount) {
            elements.linkCount.textContent = networkLinks.size;
        }
        if (elements.activeLinkCount) {
            const activeCount = Array.from(networkLinks.values()).filter(link => link.status === 'active').length;
            elements.activeLinkCount.textContent = activeCount;
        }
        if (elements.errorLinkCount) {
            const errorCount = Array.from(networkLinks.values()).filter(link => link.status === 'error').length;
            elements.errorLinkCount.textContent = errorCount;
        }
        if (elements.avgResponseTime) {
            const avgTime = calculateAverageResponseTime();
            elements.avgResponseTime.textContent = `${avgTime}ms`;
        }
        if (elements.packetSuccessRate) {
            const successRate = calculateAveragePacketSuccessRate();
            elements.packetSuccessRate.textContent = `${successRate}%`;
        }
        if (elements.bandwidthUsage) {
            const usage = calculateAverageBandwidthUsage();
            elements.bandwidthUsage.textContent = `${usage}%`;
        }
    }

    // 计算平均响应时间
    function calculateAverageResponseTime() {
        if (networkLinks.size === 0) return 0;
        const totalTime = Array.from(networkLinks.values()).reduce((sum, link) => {
            return sum + (link.healthStats?.responseTime || 0);
        }, 0);
        return Math.round(totalTime / networkLinks.size);
    }

    // 计算平均数据包成功率
    function calculateAveragePacketSuccessRate() {
        if (networkLinks.size === 0) return 0;
        const totalRate = Array.from(networkLinks.values()).reduce((sum, link) => {
            return sum + (link.healthStats?.packetSuccessRate || 0);
        }, 0);
        return Math.round(totalRate / networkLinks.size);
    }

    // 计算平均带宽使用率
    function calculateAverageBandwidthUsage() {
        if (networkLinks.size === 0) return 0;
        const totalUsage = Array.from(networkLinks.values()).reduce((sum, link) => {
            return sum + (link.healthStats?.bandwidthUsage || 0);
        }, 0);
        return Math.round(totalUsage / networkLinks.size);
    }

    // 更新链路表格
    function updateLinksTable() {
        if (!elements.linksTableBody) return;

        elements.linksTableBody.innerHTML = '';

        networkLinks.forEach((link) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${link.linkId}</td>
                <td>${link.sourceAgentId}</td>
                <td>${link.targetAgentId}</td>
                <td>
                    <span class="type-badge type-${link.linkType}">
                        ${getLinkTypeText(link.linkType)}
                    </span>
                </td>
                <td>
                    <span class="status-badge ${getStatusClass(link.status)}">
                        ${getStatusText(link.status)}
                    </span>
                </td>
                <td>${formatDate(link.createTime)}</td>
                <td>${formatDate(link.lastHeartbeatTime)}</td>
                <td>
                    <button class="btn-action btn-edit" data-id="${link.linkId}">
                        <i class="ri-edit-line"></i>
                    </button>
                    <button class="btn-action btn-delete" data-id="${link.linkId}">
                        <i class="ri-delete-line"></i>
                    </button>
                    <button class="btn-action btn-status" data-id="${link.linkId}" data-status="${link.status}">
                        <i class="ri-refresh-line"></i>
                    </button>
                </td>
            `;
            elements.linksTableBody.appendChild(row);
        });

        // 绑定操作按钮事件
        bindLinkActionEvents();
    }

    // 绑定链路操作事件
    function bindLinkActionEvents() {
        document.querySelectorAll('.btn-action.btn-edit[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const linkId = this.getAttribute('data-id');
                editLink(linkId);
            });
        });

        document.querySelectorAll('.btn-action.btn-delete[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const linkId = this.getAttribute('data-id');
                deleteLink(linkId);
            });
        });

        document.querySelectorAll('.btn-action.btn-status[data-id]').forEach(btn => {
            btn.addEventListener('click', function() {
                const linkId = this.getAttribute('data-id');
                const currentStatus = this.getAttribute('data-status');
                updateLinkStatus(linkId, currentStatus);
            });
        });
    }

    // 显示添加链路模态框
    function showAddLinkModal() {
        if (elements.addLinkModal) {
            elements.addLinkModal.classList.add('show');
        }
    }

    // 隐藏添加链路模态框
    function hideAddLinkModal() {
        if (elements.addLinkModal) {
            elements.addLinkModal.classList.remove('show');
        }
        if (elements.addLinkForm) {
            elements.addLinkForm.reset();
        }
    }

    // 添加链路
    function addLink() {
        if (!elements.addLinkForm) return;

        const formData = new FormData(elements.addLinkForm);
        const linkId = formData.get('linkId');
        const sourceAgentId = formData.get('sourceAgentId');
        const targetAgentId = formData.get('targetAgentId');
        const linkType = formData.get('linkType');

        if (!linkId || !sourceAgentId || !targetAgentId) {
            alert('请填写完整的链路信息');
            return;
        }

        const newLink = {
            linkId: linkId,
            sourceAgentId: sourceAgentId,
            targetAgentId: targetAgentId,
            linkType: linkType,
            status: 'active',
            createTime: new Date().toISOString(),
            lastHeartbeatTime: new Date().toISOString(),
            healthStats: {
                responseTime: Math.floor(Math.random() * 100) + 10,
                packetSuccessRate: Math.floor(Math.random() * 30) + 70,
                bandwidthUsage: Math.floor(Math.random() * 50) + 10
            }
        };

        networkLinks.set(linkId, newLink);
        updateLinksTable();
        updateStatus();
        hideAddLinkModal();

        console.log('Added new link:', newLink);
    }

    // 编辑链路
    function editLink(linkId) {
        console.log('Editing link:', linkId);
        // 这里可以实现编辑逻辑
        alert(`编辑链路: ${linkId}`);
    }

    // 删除链路
    function deleteLink(linkId) {
        if (confirm(`确定要删除链路 ${linkId} 吗？`)) {
            networkLinks.delete(linkId);
            updateLinksTable();
            updateStatus();
            console.log('Deleted link:', linkId);
        }
    }

    // 更新链路状态
    function updateLinkStatus(linkId, currentStatus) {
        const link = networkLinks.get(linkId);
        if (!link) return;

        // 切换状态
        let newStatus;
        switch (currentStatus) {
            case 'active':
                newStatus = 'inactive';
                break;
            case 'inactive':
                newStatus = 'active';
                break;
            case 'error':
                newStatus = 'active';
                break;
            default:
                newStatus = 'active';
        }

        link.status = newStatus;
        link.lastHeartbeatTime = new Date().toISOString();
        link.healthStats.lastStatusChangeTime = new Date().toISOString();
        link.healthStats.currentStatus = newStatus;

        networkLinks.set(linkId, link);
        updateLinksTable();
        updateStatus();

        console.log(`Updated link status: ${linkId} -> ${newStatus}`);
    }

    // 刷新链路数据
    function refreshLinks() {
        console.log('Refreshing links data');
        // 这里可以实现从服务器加载数据的逻辑
        updateLinksTable();
        updateStatus();
        alert('链路数据已刷新');
    }

    // 获取链路类型文本
    function getLinkTypeText(linkType) {
        const typeMap = {
            'direct': '直连',
            'relay': '中继',
            'p2p': 'P2P'
        };
        return typeMap[linkType] || linkType;
    }

    // 获取状态文本
    function getStatusText(status) {
        const statusMap = {
            'active': '活跃',
            'inactive': '非活跃',
            'error': '异常'
        };
        return statusMap[status] || status;
    }

    // 获取状态类名
    function getStatusClass(status) {
        const classMap = {
            'active': 'status-active',
            'inactive': 'status-inactive',
            'error': 'status-error'
        };
        return classMap[status] || '';
    }

    // 格式化日期
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
    }

    // 页面加载完成后初始化
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    // 暴露全局方法
    window.LinkManager = {
        init: init,
        addLink: addLink,
        refreshLinks: refreshLinks
    };
})();
