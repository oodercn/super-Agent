/**
 * 菜单管理模块
 */

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
        <li><a href="#network" data-page="network"><i class="ri-wifi-line"></i> 网络状态</a></li>
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

// 导出模块
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        loadMenuConfig,
        renderMenu,
        createMenuItem,
        renderDefaultMenu,
        setupNavigation
    };
}
