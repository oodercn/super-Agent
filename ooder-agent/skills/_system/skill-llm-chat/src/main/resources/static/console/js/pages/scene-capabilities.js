let allCapabilities = [];
let filteredCapabilities = [];
let capabilityTypes = [];
let currentPage = 1;
let pageSize = 20;
let totalPages = 1;
let totalCount = 0;

function getUrlParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

function getSkillForm(cap) {
    return cap.skillForm || cap.skillFormCode || 'STANDALONE';
}

function getSceneType(cap) {
    return cap.sceneType || cap.sceneTypeCode || null;
}

function isInternalCapability(cap) {
    if (cap.visibility === 'internal') {
        return true;
    }
    
    var category = (cap.category || '').toUpperCase();
    if (category === 'ASS') {
        return true;
    }
    
    var skillForm = getSkillForm(cap);
    var sceneType = getSceneType(cap);
    var businessScore = cap.businessSemanticsScore || cap.score || 5;
    
    if (skillForm === 'SCENE' && sceneType === 'AUTO' && businessScore < 6) {
        return true;
    }
    
    return false;
}

async function initPage() {
    const redirectType = getUrlParam('type');
    if (redirectType) {
        window.location.href = '/console/pages/my-capabilities.html?type=' + redirectType;
        return;
    }
    
    await loadCapabilityTypes();
    await loadCapabilities();
}

async function loadCapabilityTypes() {
    try {
        const response = await fetch('/api/v1/discovery/capabilities/types');
        const result = await response.json();
        
        if (result.status === 'success' && result.data) {
            capabilityTypes = result.data;
        } else if (Array.isArray(result)) {
            capabilityTypes = result;
        } else {
            capabilityTypes = [
                { id: 'SCENE', code: 'SCENE', name: '场景能力' },
                { id: 'SKILL', code: 'SKILL', name: '技能能力' },
                { id: 'CUSTOM', code: 'CUSTOM', name: '自定义能力' }
            ];
        }
        renderTypeOptions();
    } catch (e) {
        console.error('Failed to load capability types:', e);
        capabilityTypes = [
            { id: 'SCENE', code: 'SCENE', name: '场景能力' },
            { id: 'SKILL', code: 'SKILL', name: '技能能力' },
            { id: 'CUSTOM', code: 'CUSTOM', name: '自定义能力' }
        ];
        renderTypeOptions();
    }
}

function renderTypeOptions() {
    const select = document.getElementById('type-filter');
    select.innerHTML = '<option value="">全部类型</option>';
    
    capabilityTypes.forEach(type => {
        const option = document.createElement('option');
        option.value = type.id || type.code;
        option.textContent = type.name;
        select.appendChild(option);
    });
}

async function loadCapabilities() {
    const container = document.getElementById('capability-list');
    container.innerHTML = '<div class="nx-flex nx-items-center nx-justify-center nx-p-8"><i class="ri-loader-4-line ri-spin" style="font-size: 24px;"></i><span class="nx-ml-2">加载中...</span></div>';
    
    try {
        const response = await fetch('/api/v1/discovery/capabilities?pageNum=' + currentPage + '&pageSize=' + pageSize);
        const result = await response.json();
        
        var rawList = [];
        if (result.status === 'success' && result.data) {
            rawList = result.data.list || result.data || [];
        } else if (Array.isArray(result)) {
            rawList = result;
        }
        
        console.log('[scene-capabilities] 原始数据:', rawList.length, '条');
        
        if (rawList.length > 0) {
            console.log('[scene-capabilities] 第一条数据结构:', JSON.stringify(rawList[0], null, 2));
            
            var allFields = {};
            rawList.forEach(function(cap) {
                Object.keys(cap).forEach(function(key) {
                    if (!allFields[key]) {
                        allFields[key] = cap[key];
                    }
                });
            });
            console.log('[scene-capabilities] 所有字段:', Object.keys(allFields));
            console.log('[scene-capabilities] 字段值示例:', allFields);
        }
        
        var internalCount = 0;
        allCapabilities = rawList.filter(function(cap) {
            var isInternal = isInternalCapability(cap);
            if (isInternal) {
                internalCount++;
                console.log('[scene-capabilities] 过滤内部能力:', cap.name || cap.id, {
                    category: cap.category,
                    skillForm: getSkillForm(cap),
                    sceneType: getSceneType(cap),
                    visibility: cap.visibility,
                    businessScore: cap.businessSemanticsScore || cap.score
                });
            }
            return !isInternal;
        });
        
        console.log('[scene-capabilities] 过滤后数据:', allCapabilities.length, '条，剔除内部能力:', internalCount, '条');
        
        filteredCapabilities = allCapabilities;
        updateStats();
        renderCapabilities();
    } catch (e) {
        console.error('Failed to load capabilities:', e);
        container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">加载失败: ' + e.message + '</p>';
    }
}

function refreshCapabilities() {
    loadCapabilities();
}

function updateStats() {
    var total = filteredCapabilities.length;
    var active = filteredCapabilities.filter(function(c) {
        return c.status === 'ACTIVE' || c.status === 'REGISTERED' || c.status === 'ENABLED' || c.active === true;
    }).length;
    var scene = filteredCapabilities.filter(function(c) {
        return c.type === 'SCENE' || c.type === 'SCENE_GROUP' || c.sceneCapability === true || getSkillForm(c) === 'SCENE' || c.skillForm === 'SCENE';
    }).length;
    var custom = filteredCapabilities.filter(function(c) {
        return c.type === 'CUSTOM';
    }).length;
    
    document.getElementById('total-count').textContent = total;
    document.getElementById('active-count').textContent = active;
    document.getElementById('scene-count').textContent = scene;
    document.getElementById('custom-count').textContent = custom;
}

function renderCapabilities() {
    const container = document.getElementById('capability-list');
    
    totalCount = filteredCapabilities.length;
    totalPages = Math.ceil(totalCount / pageSize) || 1;
    
    if (currentPage > totalPages) {
        currentPage = totalPages;
    }
    
    const startIdx = (currentPage - 1) * pageSize;
    const endIdx = Math.min(startIdx + pageSize, totalCount);
    const pagedData = filteredCapabilities.slice(startIdx, endIdx);
    
    if (pagedData.length === 0) {
        container.innerHTML = '<p class="nx-text-secondary nx-text-center nx-p-4">暂无能力数据</p>';
        renderPagination();
        return;
    }
    
    let html = '<div class="nx-table-container"><table class="nx-table">';
    html += '<thead><tr><th>能力名称</th><th>类型</th><th>提供者</th><th>状态</th><th>描述</th><th>操作</th></tr></thead><tbody>';
    
    pagedData.forEach(cap => {
        const capId = cap.id || cap.capabilityId;
        const isActive = cap.status === 'ACTIVE' || cap.status === 'REGISTERED' || cap.status === 'ENABLED' || cap.active === true || cap.installed === true;
        const statusClass = isActive ? 'nx-badge--success' : 'nx-badge--secondary';
        const statusText = isActive ? '活跃' : '停用';
        const typeName = getTypeName(cap.type);
        
        html += '<tr>';
        html += '<td><div class="nx-flex nx-items-center nx-gap-2"><i class="ri-puzzle-line"></i><span class="nx-font-medium">' + (cap.name || capId) + '</span></div></td>';
        html += '<td><span class="nx-badge nx-badge--primary">' + typeName + '</span></td>';
        html += '<td>' + (cap.skillId || cap.provider || '-') + '</td>';
        html += '<td><span class="nx-badge ' + statusClass + '">' + statusText + '</span></td>';
        html += '<td>' + (cap.description || '-') + '</td>';
        html += '<td>';
        html += '<button class="nx-btn nx-btn--primary nx-btn--sm" data-id="' + encodeURIComponent(capId) + '" onclick="useCapability(decodeURIComponent(this.dataset.id))">使用</button> ';
        html += '<button class="nx-btn nx-btn--secondary nx-btn--sm" data-id="' + encodeURIComponent(capId) + '" onclick="showDetail(decodeURIComponent(this.dataset.id))">详情</button> ';
        html += '</td>';
        html += '</tr>';
    });
    
    html += '</tbody></table></div>';
    container.innerHTML = html;
    renderPagination();
}

function getTypeName(type) {
    const typeObj = capabilityTypes.find(t => t.id === type || t.code === type);
    return typeObj ? typeObj.name : (type || '未知');
}

function handleFilter() {
    const keyword = document.getElementById('search-input').value.toLowerCase();
    const typeFilter = document.getElementById('type-filter').value;
    const statusFilter = document.getElementById('status-filter').value;
    
    filteredCapabilities = allCapabilities.filter(function(cap) {
        const capId = cap.id || cap.capabilityId;
        const matchKeyword = !keyword || (cap.name || '').toLowerCase().includes(keyword) || (capId || '').toLowerCase().includes(keyword);
        const matchType = !typeFilter || cap.type === typeFilter;
        const isActive = cap.status === 'ACTIVE' || cap.status === 'REGISTERED' || cap.status === 'ENABLED' || cap.active === true || cap.installed === true;
        const matchStatus = !statusFilter || 
            (statusFilter === 'ACTIVE' && isActive) || 
            (statusFilter === 'INACTIVE' && !isActive);
        return matchKeyword && matchType && matchStatus;
    });
    
    updateStats();
    renderCapabilities();
}

function showDetail(capabilityId) {
    window.location.href = '/console/pages/capability-detail.html?id=' + encodeURIComponent(capabilityId);
}

function useCapability(capabilityId) {
    if (capabilityId === 'daily-report' || capabilityId === 'report-submit' || capabilityId.includes('report')) {
        window.location.href = '/console/pages/daily-report-form.html?capabilityId=' + encodeURIComponent(capabilityId);
    } else {
        alert('该能力暂不支持直接使用');
    }
}

function closeDetailModal() {
    document.getElementById('detail-modal').classList.remove('nx-modal--open');
}

function renderPagination() {
    const container = document.getElementById('pagination-container');
    if (!container) {
        console.log('[renderPagination] Container not found');
        return;
    }
    
    totalCount = filteredCapabilities.length;
    totalPages = Math.ceil(totalCount / pageSize) || 1;
    
    console.log('[renderPagination] total:', totalCount, 'totalPages:', totalPages, 'currentPage:', currentPage);
    
    if (totalPages <= 1) {
        container.innerHTML = '<div class="nx-flex nx-items-center nx-gap-4"><span class="nx-text-sm nx-text-secondary">共 ' + totalCount + ' 条记录</span></div>';
        return;
    }
    
    let html = '<div class="nx-pagination">';
    
    html += '<button class="nx-pagination__btn" onclick="goToPage(1)" ' + (currentPage === 1 ? 'disabled' : '') + ' title="首页"><i class="ri-skip-back-line"></i></button>';
    html += '<button class="nx-pagination__btn" onclick="goToPage(' + (currentPage - 1) + ')" ' + (currentPage === 1 ? 'disabled' : '') + ' title="上一页"><i class="ri-arrow-left-s-line"></i></button>';
    
    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, startPage + 4);
    
    for (let i = startPage; i <= endPage; i++) {
        html += '<button class="nx-pagination__btn ' + (i === currentPage ? 'nx-pagination__btn--active' : '') + '" onclick="goToPage(' + i + ')">' + i + '</button>';
    }
    
    html += '<button class="nx-pagination__btn" onclick="goToPage(' + (currentPage + 1) + ')" ' + (currentPage === totalPages ? 'disabled' : '') + ' title="下一页"><i class="ri-arrow-right-s-line"></i></button>';
    html += '<button class="nx-pagination__btn" onclick="goToPage(' + totalPages + ')" ' + (currentPage === totalPages ? 'disabled' : '') + ' title="末页"><i class="ri-skip-forward-line"></i></button>';
    
    html += '</div>';
    html += '<div class="nx-flex nx-items-center nx-gap-2 nx-ml-4">';
    html += '<span class="nx-text-sm nx-text-secondary">第 ' + currentPage + ' / ' + totalPages + ' 页</span>';
    html += '<span class="nx-text-sm nx-text-secondary">共 ' + totalCount + ' 条</span>';
    html += '</div>';
    
    container.innerHTML = html;
}

function goToPage(page) {
    if (page < 1 || page > totalPages || page === currentPage) return;
    currentPage = page;
    renderCapabilities();
    renderPagination();
}

function changePageSize(newSize) {
    pageSize = newSize;
    currentPage = 1;
    renderCapabilities();
    renderPagination();
}

document.addEventListener('DOMContentLoaded', initPage);
