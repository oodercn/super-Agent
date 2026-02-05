/**
 * 统一菜单加载脚本
 * 处理菜单配置加载、角色过滤、路径构建和菜单渲染
 */

class MenuLoader {
    constructor() {
        this.menuConfig = null;
        this.currentRole = this.getCurrentRole();
    }

    /**
     * 获取当前角色（从localStorage或默认值）
     */
    getCurrentRole() {
        return localStorage.getItem('currentRole') || 'home';
    }

    /**
     * 加载菜单配置
     */
    async loadMenuConfig() {
        try {
            // 从根路径加载菜单配置，确保从任何页面访问都正确
            const response = await fetch('/console/menu-config.json');
            if (!response.ok) {
                throw new Error('菜单配置加载失败');
            }
            this.menuConfig = await response.json();
            return this.menuConfig;
        } catch (error) {
            console.error('加载菜单配置错误:', error);
            throw error;
        }
    }

    /**
     * 检查菜单项是否对当前角色可见
     */
    isMenuItemVisible(menuItem) {
        return menuItem.roles && menuItem.roles.includes(this.currentRole);
    }

    /**
     * 过滤菜单项，只保留对当前角色可见的项
     */
    filterMenuItems(items) {
        return items.filter(item => {
            if (!this.isMenuItemVisible(item)) {
                return false;
            }
            
            // 过滤子菜单
            if (item.children && item.children.length > 0) {
                item.children = this.filterMenuItems(item.children);
                return item.children.length > 0;
            }
            
            return true;
        });
    }

    /**
     * 构建正确的路径
     */
    buildUrl(url) {
        if (!url) return '#';
        
        // 如果是完整URL，直接返回
        if (url.startsWith('http://') || url.startsWith('https://')) {
            return url;
        }
        
        // 如果已经是从根路径开始的相对路径，直接返回
        if (url.startsWith('/')) {
            return url;
        }
        
        // 构建从根路径开始的完整路径
        return `/console/${url}`;
    }

    /**
     * 创建菜单项
     */
    createMenuItem(menuItem) {
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
            a.addEventListener('click', (e) => {
                e.preventDefault();
                const toggleIcon = a.querySelector('.toggle-icon');
                const submenu = a.nextElementSibling;
                
                if (submenu) {
                    if (submenu.style.display === 'block') {
                        submenu.style.display = 'none';
                    } else {
                        submenu.style.display = 'block';
                    }
                    toggleIcon.classList.toggle('collapsed');
                }
            });
            
            li.appendChild(a);
            
            // 创建子菜单
            const submenu = document.createElement('ul');
            submenu.className = 'submenu';
            
            menuItem.children.forEach(childItem => {
                // 只显示已实现的子功能
                if (childItem.status === 'implemented') {
                    const childLi = this.createMenuItem(childItem);
                    submenu.appendChild(childLi);
                }
            });
            
            // 只有当子菜单不为空时才添加
            if (submenu.children.length > 0) {
                li.appendChild(submenu);
            }
        } else {
            // 无子菜单的菜单项
            const a = document.createElement('a');
            if (menuItem.url) {
                a.href = this.buildUrl(menuItem.url);
            } else {
                a.href = `#${menuItem.id}`;
                a.setAttribute('data-page', menuItem.page || menuItem.id);
            }
            a.innerHTML = `
                <i class="${menuItem.icon}"></i>
                ${menuItem.name}
            `;
            
            // 标记当前页面为活跃状态
            if (menuItem.url) {
                const currentPath = window.location.pathname;
                const menuPath = this.buildUrl(menuItem.url);
                if (currentPath.includes(menuItem.url)) {
                    a.classList.add('active');
                }
            }
            
            li.appendChild(a);
        }
        
        return li;
    }

    /**
     * 渲染菜单
     */
    renderMenu(menuContainerId = 'nav-menu') {
        const navMenu = document.getElementById(menuContainerId);
        if (!navMenu) {
            console.error('菜单容器未找到:', menuContainerId);
            return;
        }
        
        navMenu.innerHTML = '';
        
        if (!this.menuConfig || !this.menuConfig.menu) {
            this.renderDefaultMenu(navMenu);
            return;
        }
        
        // 过滤菜单，只显示当前角色可见的项
        const filteredMenu = this.filterMenuItems(this.menuConfig.menu);
        
        filteredMenu.forEach(item => {
            // 只显示已实现的功能（status为implemented）
            if (item.status === 'implemented' || item.id === 'dashboard') {
                const menuItem = this.createMenuItem(item);
                navMenu.appendChild(menuItem);
                // 添加菜单分隔线
                if (item.id !== 'dashboard') {
                    const divider = document.createElement('div');
                    divider.className = 'menu-divider';
                    navMenu.appendChild(divider);
                }
            }
        });
    }

    /**
     * 渲染默认菜单（加载失败时使用）
     */
    renderDefaultMenu(navMenu) {
        navMenu.innerHTML = `
            <li><a href="/console/index.html"><i class="ri-dashboard-line"></i> 仪表盘</a></li>
            <li><a href="/console/pages/home/home-dashboard.html"><i class="ri-home-line"></i> 家庭功能</a></li>
            <li><a href="/console/pages/lan/lan-dashboard.html"><i class="ri-wifi-line"></i> 小局域网功能</a></li>
            <li><a href="/console/pages/enterprise/enterprise-dashboard.html"><i class="ri-building-line"></i> 企业级全功能应用</a></li>
        `;
    }

    /**
     * 初始化菜单加载
     */
    async init() {
        try {
            await this.loadMenuConfig();
            this.renderMenu();
        } catch (error) {
            console.error('菜单初始化错误:', error);
            // 加载失败时渲染默认菜单
            const navMenu = document.getElementById('nav-menu');
            if (navMenu) {
                this.renderDefaultMenu(navMenu);
            }
        }
    }
}

// 导出MenuLoader类
if (typeof module !== 'undefined' && module.exports) {
    module.exports = MenuLoader;
} else {
    window.MenuLoader = MenuLoader;
}

// 全局初始化函数
window.initMenu = async function() {
    const menuLoader = new MenuLoader();
    await menuLoader.init();
};