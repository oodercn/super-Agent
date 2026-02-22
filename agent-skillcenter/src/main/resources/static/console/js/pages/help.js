/**
 * 帮助页面脚本
 */

// 当前页面
let currentPage = 'help';

// 菜单配置
let menuConfig = null;

// 初始化页面
function init() {
    updateTimestamp();
    loadMenuConfig();
    setInterval(updateTimestamp, 60000); // 每分钟更新时间戳
}

// 更新时间戳
function updateTimestamp() {
    const now = new Date();
    const timestamp = document.getElementById('timestamp');
    if (timestamp) {
        timestamp.textContent = now.toLocaleString('zh-CN');
    }
}

// 加载菜单配置
async function loadMenuConfig() {
    try {
        const response = await fetch('/skillcenter/console/menu-config.json');
        if (!response.ok) {
            throw new Error('菜单配置加载失败');
        }
        menuConfig = await response.json();
        renderMenu();
    } catch (error) {
        console.error('加载菜单配置错误:', error);
        renderDefaultMenu();
    }
}

// 渲染菜单
function renderMenu() {
    const navMenu = document.getElementById('nav-menu');
    if (!navMenu) {
        return;
    }

    navMenu.innerHTML = '';

    menuConfig.menu.forEach(item => {
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

// 创建菜单项
function createMenuItem(menuItem) {
    const li = document.createElement('li');

    if (menuItem.children && menuItem.children.length > 0) {
        // 有子菜单的菜单项
        const a = document.createElement('a');
        a.href = '#';
        a.innerHTML = `
            <i class="${menuItem.icon}"></i>
            ${menuItem.name}
            <span class="toggle-icon">›</span>
        `;
        
        // 处理点击事件
        try {
            if (a && typeof a.addEventListener === 'function') {
                a.addEventListener('click', function(e) {
                    e.preventDefault();
                    toggleSubmenu(this);
                });
            }
        } catch (e) {
            console.warn('添加点击事件监听器失败:', e);
        }

        li.appendChild(a);

        // 创建子菜单
        const submenu = document.createElement('ul');
        submenu.className = 'submenu';

        menuItem.children.forEach(child => {
            const childItem = createMenuItem(child);
            submenu.appendChild(childItem);
        });

        // 只有当子菜单不为空时才添加
        if (submenu.children.length > 0) {
            li.appendChild(submenu);
        }
    } else {
        // 无子菜单的菜单项
        const a = document.createElement('a');
        if (menuItem.url) {
            a.href = '#';
            a.setAttribute('data-url', menuItem.url);
        } else {
            a.href = `#${menuItem.id}`;
        }
        a.setAttribute('data-page', menuItem.page || menuItem.id);
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

// 切换子菜单
function toggleSubmenu(element) {
    const submenu = element.nextElementSibling;
    const toggleIcon = element.querySelector('.toggle-icon');
    
    if (submenu && submenu.classList.contains('submenu')) {
        submenu.classList.toggle('hidden');
        if (toggleIcon) {
            toggleIcon.classList.toggle('collapsed');
        }
    }
}

// 渲染默认菜单（加载失败时使用）
function renderDefaultMenu() {
    const navMenu = document.getElementById('nav-menu');
    if (!navMenu) {
        return;
    }

    navMenu.innerHTML = `
        <li><a href="#dashboard" class="active" data-page="dashboard"><i class="ri-dashboard-line"></i> 仪表盘</a></li>
        <li><a href="#help" class="active" data-page="help"><i class="ri-question-line"></i> 帮助中心</a></li>
    `;

    setupNavigation();
}

// 设置导航
function setupNavigation() {
    try {
        console.log('开始设置导航...');
        const navMenu = document.getElementById('nav-menu');
        if (!navMenu) {
            console.warn('未找到 nav-menu 元素');
            return;
        }
        
        const navLinks = navMenu.querySelectorAll('a');
        console.log('找到导航链接数量:', navLinks.length);
        
        if (!navLinks || navLinks.length === 0) {
            console.warn('未找到导航链接');
            return;
        }
        
        navLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                const page = this.getAttribute('data-page');
                if (page) {
                    loadPageContentByUrl(page);
                }
            });
        });
    } catch (error) {
        console.error('设置导航时出错:', error);
    }
}

// 标签页切换函数
function switchTab(tabId) {
    // 隐藏所有标签页内容
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.add('hidden');
    });
    
    // 显示选中的标签页内容
    document.getElementById(`${tabId}-tab`).classList.remove('hidden');
    
    // 更新标签页按钮状态
    document.querySelectorAll('.btn-secondary').forEach(btn => {
        btn.style.backgroundColor = '#1a1a1a';
        btn.style.color = 'var(--ooder-secondary)';
    });
    
    // 高亮当前标签页按钮
    event.target.style.backgroundColor = 'var(--ooder-primary)';
    event.target.style.color = 'white';
}

// 提交支持请求
function submitSupportRequest() {
    // 获取表单数据
    const form = document.getElementById('support-form');
    const formData = new FormData(form);
    
    // 模拟提交
    alert('支持请求已提交！我们会尽快与您联系。');
    
    // 重置表单
    form.reset();
}

// 页面加载完成后初始化
window.onload = function() {
    init();
};
