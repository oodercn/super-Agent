/**
 * 统一菜单加载脚本
 * 处理菜单配置加载、角色过滤、路径构建和菜单渲染
 * 支持菜单状态记忆功能（使用 localStorage）
 */

// 防止重复声明
if (window.MenuLoader) {
    console.log('MenuLoader 已存在，跳过重复加载');
} else {

    class MenuLoader {
        constructor() {
            this.menuConfig = null;
            this.currentRole = this.getCurrentRole();
            this.expandedMenus = this.loadExpandedMenus();
        }

        /**
         * 获取当前角色（从localStorage或默认值）
         */
        getCurrentRole() {
            return localStorage.getItem('currentRole') || 'home';
        }

        /**
         * 加载已展开的菜单状态
         */
        loadExpandedMenus() {
            try {
                const saved = localStorage.getItem('expandedMenus');
                return saved ? JSON.parse(saved) : [];
            } catch (e) {
                return [];
            }
        }

        /**
         * 保存展开的菜单状态
         */
        saveExpandedMenus() {
            try {
                localStorage.setItem('expandedMenus', JSON.stringify(this.expandedMenus));
            } catch (e) {
                console.warn('保存菜单状态失败:', e);
            }
        }

        /**
         * 添加展开的菜单ID
         */
        addExpandedMenu(menuId) {
            if (!this.expandedMenus.includes(menuId)) {
                this.expandedMenus.push(menuId);
                this.saveExpandedMenus();
            }
        }

        /**
         * 移除展开的菜单ID
         */
        removeExpandedMenu(menuId) {
            const index = this.expandedMenus.indexOf(menuId);
            if (index > -1) {
                this.expandedMenus.splice(index, 1);
                this.saveExpandedMenus();
            }
        }

        /**
         * 检查菜单是否已展开
         */
        isMenuExpanded(menuId) {
            return this.expandedMenus.includes(menuId);
        }

        /**
         * 加载菜单配置
         */
        async loadMenuConfig() {
            try {
                // 从根路径加载菜单配置，确保从任何页面访问都正确
                // 添加时间戳参数防止缓存
                const timestamp = new Date().getTime();
                const response = await fetch(`/console/menu-config.json?v=${timestamp}`);
                if (!response.ok) {
                    throw new Error('菜单配置加载失败');
                }
                this.menuConfig = await response.json();
                console.log('[MenuLoader] 菜单配置加载成功:', this.menuConfig);
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

            // 如果已经是 /console/ 开头的路径，直接返回
            if (url.startsWith('/console/')) {
                return url;
            }

            // 如果已经是以 / 开头的其他路径，直接返回
            if (url.startsWith('/')) {
                return url;
            }

            // 如果 URL 已经包含 console/ 前缀，避免重复添加
            if (url.startsWith('console/')) {
                return `/${url}`;
            }

            // 构建从根路径开始的完整路径
            return `/console/${url}`;
        }

        /**
         * 查找当前页面对应的菜单路径
         */
        findCurrentMenuPath(items, currentPath, path = []) {
            for (const item of items) {
                const newPath = [...path, item.id];
                
                // 检查当前项是否是当前页面
                if (item.url) {
                    const itemPath = this.buildUrl(item.url);
                    if (currentPath.includes(item.url) || itemPath === currentPath) {
                        return newPath;
                    }
                }
                
                // 递归检查子菜单
                if (item.children && item.children.length > 0) {
                    const childPath = this.findCurrentMenuPath(item.children, currentPath, newPath);
                    if (childPath) {
                        return childPath;
                    }
                }
            }
            return null;
        }

        /**
         * 创建菜单项
         */
        createMenuItem(menuItem, parentPath = []) {
            const li = document.createElement('li');
            const currentPath = window.location.pathname;
            const isCurrentPage = menuItem.url && (currentPath.includes(menuItem.url) || this.buildUrl(menuItem.url) === currentPath);
            
            if (menuItem.children && menuItem.children.length > 0) {
                // 有子菜单的菜单项
                const a = document.createElement('a');
                a.href = `#${menuItem.id}`;
                a.innerHTML = `
                    <i class="${menuItem.icon}"></i>
                    ${menuItem.name}
                    <span class="toggle-icon">›</span>
                `;
                
                // 检查是否应该展开（从localStorage或当前页面路径）
                const shouldExpand = this.isMenuExpanded(menuItem.id) || 
                                    (isCurrentPage && !this.expandedMenus.includes(menuItem.id));
                
                // 处理点击事件
                a.addEventListener('click', (e) => {
                    e.preventDefault();
                    
                    const toggleIcon = a.querySelector('.toggle-icon');
                    const submenu = a.nextElementSibling;
                    
                    if (submenu) {
                        // 切换当前子菜单
                        if (submenu.style.display === 'block') {
                            submenu.style.display = 'none';
                            toggleIcon.classList.remove('collapsed');
                            this.removeExpandedMenu(menuItem.id);
                        } else {
                            submenu.style.display = 'block';
                            toggleIcon.classList.add('collapsed');
                            this.addExpandedMenu(menuItem.id);
                        }
                    }
                });
                
                li.appendChild(a);
                
                // 创建子菜单
                const submenu = document.createElement('ul');
                submenu.className = 'submenu';
                submenu.style.display = shouldExpand ? 'block' : 'none';
                
                if (shouldExpand) {
                    const toggleIcon = a.querySelector('.toggle-icon');
                    if (toggleIcon) {
                        toggleIcon.classList.add('collapsed');
                    }
                    // 确保父菜单也在展开列表中
                    this.addExpandedMenu(menuItem.id);
                }
                
                menuItem.children.forEach(childItem => {
                    // 只显示已实现的子功能
                    if (childItem.status === 'implemented') {
                        const childLi = this.createMenuItem(childItem, [...parentPath, menuItem.id]);
                        submenu.appendChild(childLi);
                    }
                });
                
                // 只有当子菜单不为空时才添加
                if (submenu.children.length > 0) {
                    li.appendChild(submenu);
                }
            } else {
                // 无子菜单的菜单项（叶子节点）
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
                if (isCurrentPage) {
                    a.classList.add('active');
                }
                
                // 阻止点击事件冒泡，防止父菜单收回
                a.addEventListener('click', (e) => {
                    e.stopPropagation();
                });
                
                li.appendChild(a);
            }
            
            return li;
        }

        /**
         * 根据当前页面自动展开相关菜单
         */
        autoExpandCurrentMenu() {
            const currentPath = window.location.pathname;
            if (!this.menuConfig || !this.menuConfig.menu) return;
            
            // 查找当前页面对应的菜单路径
            const menuPath = this.findCurrentMenuPath(this.menuConfig.menu, currentPath);
            
            if (menuPath && menuPath.length > 0) {
                // 展开路径上的所有父菜单
                menuPath.forEach(menuId => {
                    this.addExpandedMenu(menuId);
                });
            }
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
            
            console.log('[MenuLoader] 当前角色:', this.currentRole);
            console.log('[MenuLoader] 菜单配置:', this.menuConfig);
            
            if (!this.menuConfig || !this.menuConfig.menu) {
                console.warn('[MenuLoader] 菜单配置为空，使用默认菜单');
                this.renderDefaultMenu(navMenu);
                return;
            }
            
            // 根据当前页面自动展开相关菜单
            this.autoExpandCurrentMenu();
            
            // 过滤菜单，只显示当前角色可见的项
            const filteredMenu = this.filterMenuItems(this.menuConfig.menu);
            console.log('[MenuLoader] 过滤后的菜单:', filteredMenu);
            
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

} // 结束防止重复声明的 else 块
