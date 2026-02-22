/**
 * 主页面脚本
 * 处理页面类型选择和导航功能
 */

window.onload = function() {
    checkSavedPageType();
    loadRecentAccess();
};

function checkSavedPageType() {
    const savedPageType = localStorage.getItem('skillcenter_page_type');
    if (savedPageType) {
        let role = 'personal';
        switch(savedPageType) {
            case 'dashboard':
            case 'personal':
            case 'market':
                role = 'personal';
                break;
            case 'admin':
                role = 'admin';
                break;
        }
        localStorage.setItem('currentRole', role);
        
        if (confirm(`检测到您上次使用的是${getPageTypeName(savedPageType)}页面，是否直接进入？`)) {
            navigateToPage(savedPageType);
        }
    }
}

function loadRecentAccess() {
    const recentAccess = localStorage.getItem('skillcenter_recent_access');
    const recentAccessEl = document.getElementById('recent-access');
    
    if (recentAccess) {
        const recentPages = JSON.parse(recentAccess);
        let html = '<div style="display: flex; gap: 16px; justify-content: center; flex-wrap: wrap;">';
        
        recentPages.forEach(page => {
            html += `
                <div style="padding: 8px 16px; background-color: var(--nexus-input-bg); border-radius: 20px; font-size: 14px; color: var(--nexus-secondary); border: 1px solid var(--nexus-border);">
                    <i class="${getPageTypeIcon(page.type)}"></i> ${getPageTypeName(page.type)} (${page.time})
                </div>
            `;
        });
        
        html += '</div>';
        recentAccessEl.innerHTML = html;
    } else {
        recentAccessEl.innerHTML = '<p style="color: var(--nexus-secondary);">暂无最近访问记录</p>';
    }
}

function selectPageType(pageType) {
    localStorage.setItem('skillcenter_page_type', pageType);
    
    let role = 'personal';
    switch(pageType) {
        case 'dashboard':
        case 'personal':
        case 'market':
            role = 'personal';
            break;
        case 'admin':
            role = 'admin';
            break;
    }
    localStorage.setItem('currentRole', role);
    
    updateRecentAccess(pageType);
    navigateToPage(pageType);
}

function updateRecentAccess(pageType) {
    const recentAccess = localStorage.getItem('skillcenter_recent_access');
    let recentPages = [];
    
    if (recentAccess) {
        recentPages = JSON.parse(recentAccess);
    }
    
    const now = new Date();
    const timeStr = now.toLocaleString('zh-CN', {
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
    
    recentPages.unshift({
        type: pageType,
        time: timeStr
    });
    
    recentPages = recentPages.slice(0, 3);
    
    localStorage.setItem('skillcenter_recent_access', JSON.stringify(recentPages));
}

function navigateToPage(pageType) {
    switch(pageType) {
        case 'dashboard':
            window.location.href = 'pages/dashboard.html';
            break;
        case 'personal':
            window.location.href = 'pages/personal/dashboard.html';
            break;
        case 'market':
            window.location.href = 'pages/market.html';
            break;
        case 'admin':
            window.location.href = 'pages/admin/dashboard.html';
            break;
    }
}

function getPageTypeName(pageType) {
    switch(pageType) {
        case 'dashboard':
            return '仪表盘';
        case 'personal':
            return '个人中心';
        case 'market':
            return '技能市场';
        case 'admin':
            return '管理中心';
        default:
            return '未知';
    }
}

function getPageTypeIcon(pageType) {
    switch(pageType) {
        case 'dashboard':
            return 'ri-dashboard-line';
        case 'personal':
            return 'ri-user-line';
        case 'market':
            return 'ri-store-2-line';
        case 'admin':
            return 'ri-admin-line';
        default:
            return 'ri-bolt-line';
    }
}
