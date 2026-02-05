/**
 * 菜单模块
 */

import { fetchJson, getCurrentPage, showNotification } from '../common/utils.js';

/**
 * 菜单初始化函数
 * @param {string} activeSection - 当前活动的菜单 section
 */
export async function initMenu(activeSection) {
    try {
        const data = await fetchJson('/skillcenter/console/menu-config.json');
        renderMenu(data.menu, activeSection);
    } catch (error) {
        console.error('Failed to load menu:', error);
        renderDefaultMenu(activeSection);
        showNotification('菜单配置加载失败，使用默认菜单', 'warning');
    }
}

/**
 * 渲染菜单
 * @param {Array} menuItems - 菜单项数组
 * @param {string} activeSection - 当前活动的菜单 section
 */
export function renderMenu(menuItems, activeSection) {
    const sidebar = document.getElementById('sidebar');
    if (!sidebar) return;

    let menuHTML = `
        <div class="sidebar-header">
            <h1><i class="ri-bolt-line"></i> SkillCenter</h1>
        </div>
        <ul class="menu">
    `;

    menuItems.forEach(item => {
        menuHTML += renderMenuItem(item, activeSection);
    });

    menuHTML += `
        </ul>
    `;

    sidebar.innerHTML = menuHTML;

    // 添加菜单点击事件
    addMenuEvents();
}

/**
 * 渲染菜单项
 * @param {Object} item - 菜单项
 * @param {string} activeSection - 当前活动的菜单 section
 * @returns {string} 菜单项 HTML
 */
export function renderMenuItem(item, activeSection) {
    // 检查是否有子菜单项是活动的
    const hasActiveChild = item.children && item.children.some(child => 
        child.id === activeSection || 
        (child.children && child.children.some(grandchild => grandchild.id === activeSection))
    );
    const isActive = item.id === activeSection || hasActiveChild;
    let menuItemHTML = `
        <li class="menu-item">
            <a href="${item.src || '#'}" class="menu-link ${isActive ? 'active' : ''}">
                <i class="menu-icon ri ${item.icon}"></i>
                <span class="menu-text">${item.name}</span>
                ${item.children && item.children.length > 0 ? '<i class="menu-arrow ri-arrow-down-s-line"></i>' : ''}
            </a>
    `;

    if (item.children && item.children.length > 0) {
        menuItemHTML += `
            <ul class="submenu ${isActive ? 'show' : ''}">
        `;

        item.children.forEach(child => {
            // 只显示已实现的子菜单
            if (!child.implemented || child.implemented === true) {
                menuItemHTML += renderSubMenuItem(child, activeSection);
            }
        });

        menuItemHTML += `
            </ul>
        `;
    }

    menuItemHTML += `
        </li>
    `;

    return menuItemHTML;
}

/**
 * 渲染子菜单项
 * @param {Object} item - 子菜单项
 * @param {string} activeSection - 当前活动的菜单 section
 * @returns {string} 子菜单项 HTML
 */
export function renderSubMenuItem(item, activeSection) {
    const isActive = item.id === activeSection;
    
    let submenuItemHTML = `
        <li class="submenu-item">
            <a href="${item.src || '#'}" class="submenu-link ${isActive ? 'active' : ''}">
                <i class="menu-icon ri ${item.icon}"></i>
                <span class="menu-text">${item.name}</span>
    `;

    if (item.children && item.children.length > 0) {
        submenuItemHTML += `
                <i class="menu-arrow ri-arrow-down-s-line"></i>
            </a>
            <ul class="submenu ${isActive ? 'show' : ''}">
        `;

        item.children.forEach(child => {
            // 只显示已实现的子菜单
            if (!child.implemented || child.implemented === true) {
                submenuItemHTML += renderSubSubMenuItem(child, activeSection);
            }
        });

        submenuItemHTML += `
            </ul>
        `;
    } else {
        submenuItemHTML += `
            </a>
        `;
    }

    submenuItemHTML += `
        </li>
    `;

    return submenuItemHTML;
}

/**
 * 渲染三级子菜单项
 * @param {Object} item - 三级子菜单项
 * @param {string} activeSection - 当前活动的菜单 section
 * @returns {string} 三级子菜单项 HTML
 */
export function renderSubSubMenuItem(item, activeSection) {
    const isActive = item.id === activeSection;
    
    // 图标映射表（当RemixIcon不可用时使用备选图标）
    const iconMap = {
        'ri-dashboard-line': '📊',
        'ri-lightbulb-line': '💡',
        'ri-play-circle-line': '▶️',
        'ri-share-line': '📤',
        'ri-group-line': '👥',
        'ri-id-card-line': '🆔',
        'ri-question-line': '❓',
        'ri-admin-line': '⚙️',
        'ri-shopping-cart-line': '🛒',
        'ri-shield-check-line': '🛡️',
        'ri-cloud-line': '☁️',
        'ri-database-line': '💾',
        'ri-server-line': '🖥️',
        'ri-list-check': '✅',
        'ri-upload-line': '📤',
        'ri-edit-line': '✏️',
        'ri-delete-line': '🗑️',
        'ri-play-line': '▶️',
        'ri-history-line': '⏰',
        'ri-file-check-line': '📋',
        'ri-share-box-line': '📦',
        'ri-share-forward-line': '🔄',
        'ri-download-line': '📥',
        'ri-team-line': '👨‍👩‍👧‍👦',
        'ri-book-line': '📚',
        'ri-information-line': 'ℹ️',
        'ri-folder-line': '📁',
        'ri-check-circle-line': '✓',
        'ri-add-circle-line': '➕',
        'ri-user-add-line': '👤',
        'ri-lock-line': '🔒',
        'ri-cloud-upload-line': '☁️',
        'ri-server-fill': '🖥️',
        'ri-eye-line': '👁️',
        'ri-disk-line': '💾',
        'ri-save-line': '💾',
        'ri-refresh-line': '🔄',
        'ri-delete-bin-line': '🗑️',
        'ri-settings-line': '⚙️',
        'ri-heartbeat-line': '❤️',
        'ri-file-text-line': '📄',
        'ri-bar-chart-2-line': '📈',
        'ri-power-line': '⚡'
    };
    
    const menuIcon = iconMap[item.icon] || '📄';
    
    return `
        <li class="subsubmenu-item">
            <a href="${item.src || '#'}" class="subsubmenu-link ${isActive ? 'active' : ''}">
                <span class="menu-icon">${menuIcon}</span>
                <span class="menu-text">${item.name}</span>
            </a>
        </li>
    `;
}

/**
 * 添加菜单事件
 */
export function addMenuEvents() {
    const menuLinks = document.querySelectorAll('.menu-link, .submenu-link');
    
    menuLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            // 检查是否有子菜单
            const nextSibling = this.nextElementSibling;
            const hasSubmenu = nextSibling && nextSibling.classList.contains('submenu');
            
            if (hasSubmenu) {
                // 阻止默认跳转
                e.preventDefault();
                
                // 切换子菜单
                nextSibling.classList.toggle('show');
                
                // 切换箭头方向
                const arrow = this.querySelector('.menu-arrow');
                if (arrow) {
                    arrow.classList.toggle('ri-arrow-down-s-line');
                    arrow.classList.toggle('ri-arrow-right-s-line');
                }
            } else {
                // 没有子菜单，正常跳转
                const href = this.getAttribute('href');
                if (href && href !== '#') {
                    window.location.href = href;
                }
            }
        });
    });
}

/**
 * 渲染默认菜单（当菜单配置加载失败时）
 * @param {string} activeSection - 当前活动的菜单 section
 */
export function renderDefaultMenu(activeSection) {
    const sidebar = document.getElementById('sidebar');
    if (!sidebar) return;

    const defaultMenu = `
        <div class="sidebar-header">
            <h1>SkillCenter</h1>
        </div>
        <ul class="menu">
            <li class="menu-item">
                <a href="/skillcenter/console/pages/personal/dashboard.html" class="menu-link ${activeSection === 'dashboard' ? 'active' : ''}">
                    <i class="menu-icon ri ri-dashboard-line"></i>
                    <span class="menu-text">仪表盘</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/personal/my-skill.html" class="menu-link ${activeSection === 'my-skills' ? 'active' : ''}">
                    <i class="menu-icon ri ri-lightbulb-line"></i>
                    <span class="menu-text">我的技能</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/personal/my-execution.html" class="menu-link ${activeSection === 'my-execution' ? 'active' : ''}">
                    <i class="menu-icon ri ri-play-circle-line"></i>
                    <span class="menu-text">执行管理</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/personal/my-sharing.html" class="menu-link ${activeSection === 'my-sharing' ? 'active' : ''}">
                    <i class="menu-icon ri ri-share-line"></i>
                    <span class="menu-text">技能分享</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/personal/my-group.html" class="menu-link ${activeSection === 'my-groups' ? 'active' : ''}">
                    <i class="menu-icon ri ri-group-line"></i>
                    <span class="menu-text">我的群组</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/personal/my-identity.html" class="menu-link ${activeSection === 'my-identity' ? 'active' : ''}">
                    <i class="menu-icon ri ri-id-card-line"></i>
                    <span class="menu-text">个人身份</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/personal/my-help.html" class="menu-link ${activeSection === 'my-help' ? 'active' : ''}">
                    <i class="menu-icon ri ri-question-line"></i>
                    <span class="menu-text">帮助与支持</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/admin/dashboard.html" class="menu-link ${activeSection === 'admin-dashboard' ? 'active' : ''}">
                    <i class="menu-icon ri ri-dashboard-line"></i>
                    <span class="menu-text">管理仪表盘</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/admin/skill-management.html" class="menu-link ${activeSection === 'skill-management' ? 'active' : ''}">
                    <i class="menu-icon ri ri-lightbulb-line"></i>
                    <span class="menu-text">技能管理</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/admin/market-management.html" class="menu-link ${activeSection === 'market-management' ? 'active' : ''}">
                    <i class="menu-icon ri ri-shopping-cart-line"></i>
                    <span class="menu-text">市场管理</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/admin/skill-authentication.html" class="menu-link ${activeSection === 'skill-authentication' ? 'active' : ''}">
                    <i class="menu-icon ri ri-shield-check-line"></i>
                    <span class="menu-text">技能认证</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/admin/group-management.html" class="menu-link ${activeSection === 'group-management' ? 'active' : ''}">
                    <i class="menu-icon ri ri-group-line"></i>
                    <span class="menu-text">群组管理</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/admin/remote-hosting.html" class="menu-link ${activeSection === 'remote-hosting' ? 'active' : ''}">
                    <i class="menu-icon ri ri-cloud-line"></i>
                    <span class="menu-text">远程托管</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/admin/storage-management.html" class="menu-link ${activeSection === 'storage-management' ? 'active' : ''}">
                    <i class="menu-icon ri ri-database-line"></i>
                    <span class="menu-text">存储管理</span>
                </a>
            </li>
            <li class="menu-item">
                <a href="/skillcenter/console/pages/admin/system-management.html" class="menu-link ${activeSection === 'system-management' ? 'active' : ''}">
                    <i class="menu-icon ri ri-server-line"></i>
                    <span class="menu-text">系统管理</span>
                </a>
            </li>
        </ul>
    `;

    sidebar.innerHTML = defaultMenu;
}

/**
 * 切换菜单折叠状态
 */
export function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.querySelector('.main-content');
    if (sidebar && mainContent) {
        sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('expanded');
    }
}

/**
 * 初始化菜单模块
 */
export function initMenuModule() {
    document.addEventListener('DOMContentLoaded', function() {
        // 从URL中获取当前页面
        const activeSection = getCurrentPage();

        // 初始化菜单
        initMenu(activeSection);

        // 绑定侧边栏切换按钮
        const toggleBtn = document.getElementById('sidebar-toggle');
        if (toggleBtn) {
            toggleBtn.addEventListener('click', toggleSidebar);
        }
    });
}

// 自动初始化菜单模块
initMenuModule();
