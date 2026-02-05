/**
 * 通用JavaScript功能模块
 * 包含所有页面共用的功能函数
 */

// 全局变量
const COMMON = {
    // API基础URL
    API_BASE_URL: '/api/mcp',
    // 当前页面
    currentPage: 'dashboard',
    // 菜单配置
    menuConfig: null,
    // 当前用户场景
    currentScenario: null
};

// 页面加载完成后初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initCommon);
} else {
    initCommon();
}

/**
 * 初始化通用功能
 */
function initCommon() {
    updateTimestamp();
    loadMenuConfig();
    setupEventListeners();
    // 每分钟更新时间戳
    setInterval(updateTimestamp, 60000);
}

/**
 * 设置全局事件监听器
 */
function setupEventListeners() {
    // 禁止鼠标右键菜单（可选）
    document.addEventListener('contextmenu', function(e) {
        e.preventDefault();
    });
    
    // 禁止文本选择（可选）
    document.addEventListener('selectstart', function(e) {
        e.preventDefault();
    });
}

/**
 * 更新时间戳
 */
function updateTimestamp() {
    const timestampElement = document.getElementById('timestamp');
    if (timestampElement) {
        const now = new Date();
        timestampElement.textContent = now.toLocaleString('zh-CN');
    }
}

/**
 * 加载菜单配置
 */
async function loadMenuConfig() {
    try {
        const response = await fetch('menu-config.json');
        if (!response.ok) {
            throw new Error('菜单配置加载失败');
        }
        COMMON.menuConfig = await response.json();
        renderMenu();
    } catch (error) {
        console.error('加载菜单配置错误:', error);
        // 加载失败时显示默认菜单
        renderDefaultMenu();
    }
}

/**
 * 渲染菜单
 */
function renderMenu() {
    const navMenu = document.getElementById('nav-menu');
    if (!navMenu) return;
    
    navMenu.innerHTML = '';
    
    COMMON.menuConfig.menu.forEach(item => {
        const menuItem = createMenuItem(item);
        navMenu.appendChild(menuItem);
        // 添加菜单分隔线
        if (item.id !== 'dashboard') {
            const divider = document.createElement('div');
            divider.className = 'menu-divider';
            navMenu.appendChild(divider);
        }
    });
    
    setupNavigation();
}

/**
 * 创建菜单项
 * @param {Object} menuItem 菜单项配置
 * @returns {HTMLElement} 菜单项元素
 */
function createMenuItem(menuItem) {
    const li = document.createElement('li');
    
    if (menuItem.children && menuItem.children.length > 0) {
        // 有子菜单的菜单项
        const a = document.createElement('a');
        a.href = `#${menuItem.id}`;
        a.innerHTML = `
            <i class="${menuItem.icon}"></i>
            ${menuItem.name}
            <span class="toggle-icon">›</span>
        `;
        
        // 处理点击事件
        a.addEventListener('click', function(e) {
            e.preventDefault();
            const toggleIcon = this.querySelector('.toggle-icon');
            const submenu = this.nextElementSibling;
            
            if (submenu) {
                submenu.classList.toggle('hidden');
                toggleIcon.classList.toggle('collapsed');
            }
        });
        
        li.appendChild(a);
        
        // 创建子菜单
        const submenu = document.createElement('ul');
        submenu.className = 'submenu';
        
        menuItem.children.forEach(childItem => {
            const childLi = createMenuItem(childItem);
            submenu.appendChild(childLi);
        });
        
        // 只有当子菜单不为空时才添加
        if (submenu.children.length > 0) {
            li.appendChild(submenu);
        }
    } else {
        // 无子菜单的菜单项
        const a = document.createElement('a');
        if (menuItem.url) {
            a.href = menuItem.url;
        } else {
            a.href = `#${menuItem.id}`;
            a.setAttribute('data-page', menuItem.page || menuItem.id);
        }
        a.innerHTML = `
            <i class="${menuItem.icon}"></i>
            ${menuItem.name}
        `;
        
        // 检查是否已实现
        if (menuItem.status !== 'implemented' && menuItem.id !== 'dashboard') {
            a.classList.add('disabled');
            a.title = '功能开发中，敬请期待';
        }
        
        li.appendChild(a);
    }
    
    return li;
}

/**
 * 渲染默认菜单（加载失败时使用）
 */
function renderDefaultMenu() {
    const navMenu = document.getElementById('nav-menu');
    if (!navMenu) return;
    
    navMenu.innerHTML = `
        <li><a href="#dashboard" class="active" data-page="dashboard"><i class="ri-dashboard-line"></i> 仪表盘</a></li>
        <li><a href="#system" data-page="system"><i class="ri-computer-line"></i> 系统信息</a></li>
        <li><a href="#network" data-page="network"><i class="ri-network-line"></i> 网络状态</a></li>
        <li><a href="#logs" data-page="logs"><i class="ri-file-text-line"></i> 日志查看</a></li>
        <li><a href="#tests" data-page="tests"><i class="ri-test-tube-line"></i> 测试用例</a></li>
        <li><a href="#config" data-page="config"><i class="ri-settings-3-line"></i> 配置管理</a></li>
    `;
    setupNavigation();
}

/**
 * 设置导航
 */
function setupNavigation() {
    const navLinks = document.querySelectorAll('.nav-menu a');
    navLinks.forEach(link => {
        // 只处理带有data-page属性的链接（即叶子节点且没有url属性）
        if (link.hasAttribute('data-page') && !link.hasAttribute('href') && link.getAttribute('href') !== '#') {
            link.addEventListener('click', function(e) {
                e.preventDefault();
                
                // 更新活跃状态
                navLinks.forEach(l => l.classList.remove('active'));
                this.classList.add('active');
            });
        }
    });
}

/**
 * 显示加载状态
 * @param {string} message 加载消息
 */
function showLoading(message = '加载中...') {
    const loadingOverlay = document.getElementById('loading-overlay');
    const loadingText = document.getElementById('loading-text');
    
    if (loadingText) {
        loadingText.textContent = message;
    }
    
    if (loadingOverlay) {
        loadingOverlay.classList.add('active');
    }
}

/**
 * 隐藏加载状态
 */
function hideLoading() {
    const loadingOverlay = document.getElementById('loading-overlay');
    if (loadingOverlay) {
        loadingOverlay.classList.remove('active');
    }
}

/**
 * 显示错误提示
 * @param {string} message 错误消息
 */
function showErrorToast(message) {
    const errorToast = document.getElementById('error-toast');
    const errorMessage = document.getElementById('error-message');
    
    if (errorMessage) {
        errorMessage.textContent = message;
    }
    
    if (errorToast) {
        errorToast.classList.add('active');
        setTimeout(hideErrorToast, 3000);
    }
}

/**
 * 隐藏错误提示
 */
function hideErrorToast() {
    const errorToast = document.getElementById('error-toast');
    if (errorToast) {
        errorToast.classList.remove('active');
    }
}

/**
 * 显示成功提示
 * @param {string} message 成功消息
 */
function showSuccessToast(message) {
    const successToast = document.getElementById('success-toast');
    const successMessage = document.getElementById('success-message');
    
    if (successMessage) {
        successMessage.textContent = message;
    }
    
    if (successToast) {
        successToast.classList.add('active');
        setTimeout(hideSuccessToast, 3000);
    }
}

/**
 * 隐藏成功提示
 */
function hideSuccessToast() {
    const successToast = document.getElementById('success-toast');
    if (successToast) {
        successToast.classList.remove('active');
    }
}

/**
 * 格式化字节数
 * @param {number} bytes 字节数
 * @returns {string} 格式化后的字符串
 */
function formatBytes(bytes) {
    if (bytes === 0) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

/**
 * 格式化时间
 * @param {number} timestamp 时间戳
 * @returns {string} 格式化后的时间字符串
 */
function formatTime(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleString('zh-CN');
}

/**
 * 计算相对时间
 * @param {number} timestamp 时间戳
 * @returns {string} 相对时间字符串
 */
function getRelativeTime(timestamp) {
    const now = Date.now();
    const diff = Math.floor((now - timestamp) / 1000);
    
    if (diff < 60) {
        return `${diff}s ago`;
    } else if (diff < 3600) {
        return `${Math.floor(diff / 60)}m ago`;
    } else if (diff < 86400) {
        return `${Math.floor(diff / 3600)}h ago`;
    } else {
        return `${Math.floor(diff / 86400)}d ago`;
    }
}

/**
 * 异步获取数据
 * @param {string} url 请求URL
 * @param {Object} options 请求选项
 * @returns {Promise<any>} 响应数据
 */
async function fetchData(url, options = {}) {
    try {
        showLoading('加载数据中...');
        
        const defaultOptions = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        };
        
        const response = await fetch(url, {
            ...defaultOptions,
            ...options
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        hideLoading();
        return data;
    } catch (error) {
        hideLoading();
        showErrorToast(`数据加载失败: ${error.message}`);
        console.error('Fetch error:', error);
        throw error;
    }
}

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

/**
 * 导出通用模块
 */
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        COMMON,
        initCommon,
        updateTimestamp,
        loadMenuConfig,
        renderMenu,
        createMenuItem,
        renderDefaultMenu,
        setupNavigation,
        showLoading,
        hideLoading,
        showErrorToast,
        hideErrorToast,
        showSuccessToast,
        hideSuccessToast,
        formatBytes,
        formatTime,
        getRelativeTime,
        fetchData,
        enterScenario,
        loadScenarioFeatures
    };
}
