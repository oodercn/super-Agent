/**
 * 主页面脚本
 * 处理页面类型选择和导航功能
 */

// 页面加载时检查本地存储中的页面类型
window.onload = function() {
    checkSavedPageType();
    loadRecentAccess();
};

// 检查本地存储中的页面类型
function checkSavedPageType() {
    const savedPageType = localStorage.getItem('mcp_agent_page_type');
    if (savedPageType) {
        // 根据页面类型设置角色
        let role = 'home';
        switch(savedPageType) {
            case 'home':
            case 'lan':
                role = 'home';
                break;
            case 'enterprise':
                role = 'enterprise';
                break;
        }
        localStorage.setItem('currentRole', role);
        
        // 如果有保存的页面类型，提示用户是否直接进入
        if (confirm(`检测到您上次使用的是${getPageTypeName(savedPageType)}页面，是否直接进入？`)) {
            navigateToPage(savedPageType);
        }
    }
}

// 加载最近访问记录
function loadRecentAccess() {
    const recentAccess = localStorage.getItem('mcp_agent_recent_access');
    const recentAccessEl = document.getElementById('recent-access');
    
    if (recentAccess) {
        const recentPages = JSON.parse(recentAccess);
        let html = '<div style="display: flex; gap: 16px; justify-content: center;">';
        
        recentPages.forEach(page => {
            html += `
                <div style="padding: 8px 16px; background-color: var(--nexus-input-bg); border-radius: 20px; font-size: 14px; color: var(--nexus-secondary);">
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

// 选择页面类型
function selectPageType(pageType) {
    // 保存页面类型到本地存储
    localStorage.setItem('mcp_agent_page_type', pageType);
    
    // 根据页面类型设置角色
    let role = 'home';
    switch(pageType) {
        case 'home':
        case 'lan':
            role = 'home';
            break;
        case 'enterprise':
            role = 'enterprise';
            break;
    }
    localStorage.setItem('currentRole', role);
    
    // 更新最近访问记录
    updateRecentAccess(pageType);
    
    // 导航到对应页面
    navigateToPage(pageType);
}

// 更新最近访问记录
function updateRecentAccess(pageType) {
    const recentAccess = localStorage.getItem('mcp_agent_recent_access');
    let recentPages = [];
    
    if (recentAccess) {
        recentPages = JSON.parse(recentAccess);
    }
    
    // 添加新的访问记录
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
    
    // 只保留最近3条记录
    recentPages = recentPages.slice(0, 3);
    
    // 保存到本地存储
    localStorage.setItem('mcp_agent_recent_access', JSON.stringify(recentPages));
}

// 导航到对应页面
function navigateToPage(pageType) {
    switch(pageType) {
        case 'home':
            window.location.href = '/console/pages/dashboard.html';
            break;
        case 'lan':
            window.location.href = '/console/pages/lan/lan-dashboard.html';
            break;
        case 'enterprise':
            window.location.href = '/console/pages/admin/dashboard.html';
            break;
    }
}

// 获取页面类型名称
function getPageTypeName(pageType) {
    switch(pageType) {
        case 'home':
            return '家庭';
        case 'lan':
            return '局域网';
        case 'enterprise':
            return '企业网';
        default:
            return '未知';
    }
}

// 获取页面类型图标
function getPageTypeIcon(pageType) {
    switch(pageType) {
        case 'home':
            return 'ri-home-smile-line';
        case 'lan':
            return 'ri-wifi-line';
        case 'enterprise':
            return 'ri-building-line';
        default:
            return 'ri-server-line';
    }
}
