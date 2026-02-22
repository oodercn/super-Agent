/**
 * 场景管理模块
 */

/**
 * 进入用户场景
 * @param {string} scenario 场景名称
 */
function enterScenario(scenario) {
    COMMON.currentScenario = scenario;
    
    // 隐藏场景选择，显示系统概览
    const section = document.querySelector('.section');
    const systemOverview = document.getElementById('system-overview-section');
    
    if (section) {
        section.classList.add('hidden');
    }
    
    if (systemOverview) {
        systemOverview.classList.remove('hidden');
    }
    
    // 根据场景加载不同的功能
    loadScenarioFeatures(scenario);
}

/**
 * 加载场景特定功能
 * @param {string} scenario 场景名称
 */
function loadScenarioFeatures(scenario) {
    console.log('进入场景:', scenario);
    
    // 根据场景显示不同的导航菜单
    const navMenu = document.querySelector('.nav-menu');
    const navItems = navMenu ? navMenu.querySelectorAll('li') : [];
    
    // 重置所有导航项
    navItems.forEach(item => item.style.display = 'none');
    
    // 根据场景显示不同的导航项
    switch(scenario) {
        case 'enterprise':
            // 小微企业场景：显示所有功能
            navItems.forEach(item => item.style.display = 'block');
            break;
        case 'personal':
            // 个人用户场景：只显示基本功能
            if (navItems.length > 0) navItems[0].style.display = 'block'; // 仪表盘
            if (navItems.length > 1) navItems[1].style.display = 'block'; // 系统信息
            if (navItems.length > 2) navItems[2].style.display = 'block'; // 网络状态
            break;
        case 'multi-network':
            // 集中多网端场景：显示高级功能
            navItems.forEach(item => item.style.display = 'block');
            break;
    }
    
    // 加载系统状态
    if (typeof loadDashboard === 'function') {
        loadDashboard();
    }
}

// 导出模块
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        enterScenario,
        loadScenarioFeatures
    };
}
