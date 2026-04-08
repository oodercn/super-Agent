(function(global) {
'use strict';

var state = DiscoveryState;

var DiscoveryResult = {
    renderResults: function() {
        console.log('[renderResults] Starting render, discoveredCapabilities length:', state.discoveredCapabilities.length);
        var container = document.getElementById('resultsBody');
        if (!container) {
            console.error('[renderResults] Container resultsBody not found');
            return;
        }
        var html = '';
        var counts = { scene: 0, provider: 0, driver: 0, new: 0, installed: 0 };
        var businessCounts = {};
        var scoreCounts = { high: 0, medium: 0, low: 0 };
        
        state.discoveredCapabilities.forEach(function(cap) {
            console.log('[renderResults] Processing cap:', cap.id, 'skillForm:', cap.skillForm);
            if (cap.skillForm === 'SCENE') { counts.scene++; }
            else if (cap.skillForm === 'PROVIDER') { counts.provider++; }
            else if (cap.skillForm === 'DRIVER') { counts.driver++; }
            if (cap.installed) { counts.installed++; } else { counts.new++; }
            if (cap.businessCategory) {
                businessCounts[cap.businessCategory] = (businessCounts[cap.businessCategory] || 0) + 1;
            }
            var score = cap.businessSemanticsScore || 5;
            if (score >= 8) { scoreCounts.high++; }
            else if (score >= 3) { scoreCounts.medium++; }
            else { scoreCounts.low++; }
            
            var categoryInfo = DiscoveryUtils.getCategoryInfo(cap.skillForm, cap.sceneType);
            var bcInfo = DiscoveryUtils.getBusinessCategoryInfo(cap.businessCategory);
            var scoreLevel = score >= 8 ? 'high' : (score >= 3 ? 'medium' : 'low');
            var scoreColor = score >= 8 ? '#10b981' : (score >= 3 ? '#f59e0b' : '#6b7280');
            
            html += '<div class="result-item" data-skill-form="' + cap.skillForm + '" data-scene-type="' + (cap.sceneType || '') + '" data-business-category="' + (cap.businessCategory || '') + '" data-installed="' + cap.installed + '" data-skill-id="' + cap.id + '" data-score="' + score + '" onclick="toggleCapabilitySelection(\'' + cap.id + '\', event)">' +
                '<div class="result-header">' +
                '<div class="result-icon"><i class="' + categoryInfo.icon + '"></i></div>' +
                '<div class="result-info">' +
                '<div class="result-name">' + cap.name + '</div>' +
                '<div class="result-desc">' + (cap.description || '') + '</div></div>' +
                '<div class="result-badges">' +
                '<span class="badge badge-' + categoryInfo.code.toLowerCase() + '">' + categoryInfo.name + '</span>' +
                (cap.businessCategory ? '<span class="badge badge-bc" style="background: ' + bcInfo.color + '20; color: ' + bcInfo.color + ';">' + bcInfo.name + '</span>' : '') +
                '<span class="badge badge-score" style="background: ' + scoreColor + '20; color: ' + scoreColor + ';" title="业务语义评分: ' + score + '/10"><i class="ri-star-line"></i> ' + score + '</span>' +
                (cap.installed ? '<span class="badge badge-installed">已安装</span>' : '<span class="badge badge-new">新能力</span>') +
                '</div></div>' +
                '<div class="result-actions">' +
                '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="event.stopPropagation(); viewCapability(\'' + cap.id + '\')" title="查看详情"><i class="ri-eye-line"></i></button>' +
                (cap.installed ? 
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); openCapability(\'' + cap.id + '\')" title="打开"><i class="ri-external-link-line"></i> 打开</button>' :
                    '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); installCapability(\'' + cap.id + '\')" title="安装"><i class="ri-download-line"></i> 安装</button>') +
                '</div></div>';
        });
        
        console.log('[renderResults] Generated HTML length:', html.length);
        container.innerHTML = html;
        document.getElementById('resultsCount').textContent = state.discoveredCapabilities.length;
        DiscoveryFilter.updateFilterCounts(counts, businessCounts);
        DiscoveryFilter.updateScoreFilterCounts(scoreCounts);
        DiscoveryResult.renderCharts(businessCounts, counts, state.discoveredCapabilities.length);
        console.log('[renderResults] Render completed');
    },

    renderCharts: function(businessCounts, skillFormCounts, total) {
        var chartSection = document.getElementById('chartSection');
        var businessChart = document.getElementById('businessChart');
        var skillFormChart = document.getElementById('skillFormChart');
        if (!chartSection || !businessChart || !skillFormChart) return;
        if (total === 0) { chartSection.style.display = 'none'; return; }
        chartSection.style.display = 'block';
        
        var bcHtml = '';
        for (var bc in businessCounts) {
            var count = businessCounts[bc];
            var percent = Math.round((count * 100.0) / total);
            var bcInfo = CategoryService.getInfo(bc);
            var color = bcInfo.color || '#8c8c8c';
            var name = bcInfo.name || bc;
            bcHtml += '<div class="chart-pie-item" onclick="selectBusinessCategory(\'' + bc + '\')" style="cursor: pointer;">' +
                '<div class="chart-pie-dot" style="background: ' + color + '"></div>' +
                '<span class="chart-pie-label">' + name + '</span>' +
                '<span class="chart-pie-value">' + count + '</span>' +
                '<span class="chart-pie-percent">(' + percent + '%)</span></div>';
        }
        businessChart.innerHTML = bcHtml;
        
        var sfColors = { 'SCENE': '#722ed1', 'PROVIDER': '#1890ff', 'DRIVER': '#52c41a' };
        var sfNames = { 'SCENE': '场景应用', 'PROVIDER': '能力服务', 'DRIVER': '驱动适配' };
        var sfHtml = '';
        for (var sf in sfNames) {
            var count = sf === 'SCENE' ? skillFormCounts.scene : (sf === 'PROVIDER' ? skillFormCounts.provider : skillFormCounts.driver);
            if (count > 0) {
                var percent = Math.round((count * 100.0) / total);
                var color = sfColors[sf] || '#8c8c8c';
                var barWidth = Math.max(10, percent);
                sfHtml += '<div class="chart-bar">' +
                    '<span class="chart-bar-label">' + sfNames[sf] + '</span>' +
                    '<div class="chart-bar-bar" style="width: ' + barWidth + 'px; background: ' + color + '"></div>' +
                    '<span class="chart-bar-value">' + count + '</span></div>';
            }
        }
        skillFormChart.innerHTML = sfHtml;
    },

    showDetailModal: function(cap) {
        var modal = document.getElementById('detailModal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'detailModal';
            modal.className = 'modal-overlay';
            modal.innerHTML = '<div class="modal-content" style="max-width: 600px;">' +
                '<div class="modal-header">' +
                '<h3 id="detailModalTitle"></h3>' +
                '<button class="modal-close" onclick="closeDetailModal()"><i class="ri-close-line"></i></button>' +
                '</div>' +
                '<div class="modal-body" id="detailModalBody"></div>' +
                '<div class="modal-footer">' +
                '<button class="nx-btn nx-btn--secondary" onclick="closeDetailModal()">关闭</button>' +
                '<button class="nx-btn nx-btn--primary" id="detailModalAction"></button>' +
                '</div></div>';
            document.body.appendChild(modal);
        }
        
        var categoryInfo = DiscoveryUtils.getCategoryInfo(cap.skillForm, cap.sceneType);
        var bcInfo = DiscoveryUtils.getBusinessCategoryInfo(cap.businessCategory);
        
        document.getElementById('detailModalTitle').innerHTML = '<i class="' + categoryInfo.icon + '"></i> ' + cap.name;
        document.getElementById('detailModalBody').innerHTML = 
            '<div class="detail-section">' +
            '<div class="detail-row"><span class="detail-label">类型:</span><span class="detail-value">' + categoryInfo.name + '</span></div>' +
            '<div class="detail-row"><span class="detail-label">业务领域:</span><span class="detail-value">' + (bcInfo.name || '-') + '</span></div>' +
            '<div class="detail-row"><span class="detail-label">版本:</span><span class="detail-value">' + (cap.version || '1.0.0') + '</span></div>' +
            '<div class="detail-row"><span class="detail-label">状态:</span><span class="detail-value">' + (cap.installed ? '已安装' : '未安装') + '</span></div>' +
            '</div>' +
            '<div class="detail-section">' +
            '<div class="detail-label">描述:</div>' +
            '<div class="detail-value">' + (cap.description || '暂无描述') + '</div>' +
            '</div>' +
            (cap.tags && cap.tags.length > 0 ? 
                '<div class="detail-section">' +
                '<div class="detail-label">标签:</div>' +
                '<div class="detail-value">' + cap.tags.map(function(t) { return '<span class="badge">' + t + '</span>'; }).join(' ') + '</div>' +
                '</div>' : '');
        
        var actionBtn = document.getElementById('detailModalAction');
        if (cap.installed) {
            actionBtn.innerHTML = '<i class="ri-external-link-line"></i> 打开';
            actionBtn.onclick = function() { window.location.href = 'capability-detail.html?id=' + cap.id; };
        } else {
            actionBtn.innerHTML = '<i class="ri-download-line"></i> 安装';
            actionBtn.onclick = function() { installCapability(cap.id); closeDetailModal(); };
        }
        modal.classList.add('show');
    }
};

global.DiscoveryResult = DiscoveryResult;
global.viewCapability = function(skillId) {
    var cap = state.discoveredCapabilities.find(function(c) { return c.id === skillId; });
    if (cap) { DiscoveryResult.showDetailModal(cap); }
};
global.closeDetailModal = function() {
    var modal = document.getElementById('detailModal');
    if (modal) { modal.classList.remove('show'); }
};
global.openCapability = function(skillId) {
    window.location.href = 'capability-detail.html?id=' + skillId;
};

})(typeof window !== 'undefined' ? window : this);
