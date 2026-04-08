/**
 * NexusMenu - 统一菜单系统
 * 所有页面必须引用此文件
 * 
 * 使用方法:
 * <script src="/console/js/nexus.js"></script>
 * <script src="/console/js/menu.js"></script>
 * <script src="/console/js/page-init.js" data-auto-init></script>
 * <script src="/console/js/api.js"></script>
 */

(function() {
    'use strict';

    const NexusMenu = {
        initialized: false,
        menuConfig: null,
        currentUser: null,
        expandedMenus: [],

        async init() {
            if (this.initialized) {
                console.log('[NexusMenu] 已经初始化，跳过');
                return;
            }

            console.log('[NexusMenu] 开始初始化...');

            this.expandedMenus = this.loadExpandedMenus();

            try {
                await this.loadSession();
                await this.loadMenuConfig();
                this.renderMenu();
                this.initialized = true;
                console.log('[NexusMenu] 初始化完成');
            } catch (error) {
                console.error('[NexusMenu] 初始化失败:', error);
                this.renderDefaultMenu();
            }
        },

        loadExpandedMenus() {
            try {
                const saved = localStorage.getItem('expandedMenus');
                return saved ? JSON.parse(saved) : [];
            } catch (e) {
                return [];
            }
        },

        saveExpandedMenus() {
            try {
                localStorage.setItem('expandedMenus', JSON.stringify(this.expandedMenus));
            } catch (e) {
                console.warn('[NexusMenu] 保存菜单状态失败:', e);
            }
        },

        async loadSession() {
            try {
                const response = await fetch('/api/v1/scene-auth/session');
                
                // 检查HTTP状态码
                if (!response.ok) {
                    // 只有401/403才认为是会话失效，需要跳转登录页
                    if (response.status === 401 || response.status === 403) {
                        console.warn('[NexusMenu] 会话已过期或无效，跳转到登录页');
                        if (!window.location.pathname.includes('login.html')) {
                            window.location.href = '/console/pages/login.html';
                        }
                        throw new Error('会话已过期');
                    } else {
                        // 其他HTTP错误（404、500等）直接报错，不跳转登录页
                        throw new Error('HTTP ' + response.status + ': ' + response.statusText);
                    }
                }
                
                const result = await response.json();
                if (result.status === 'success' && result.data) {
                    this.currentUser = result.data;
                    console.log('[NexusMenu] 用户会话加载成功:', this.currentUser.name);
                } else {
                    console.warn('[NexusMenu] 会话数据无效');
                    throw new Error('会话数据无效');
                }
            } catch (e) {
                console.error('[NexusMenu] 加载会话失败:', e);
                // 只有会话过期才跳转登录页，其他错误直接抛出
                if (e.message === '会话已过期') {
                    throw e;
                }
                // 其他错误（网络错误、404、500等）使用默认菜单，不强制跳转
                console.warn('[NexusMenu] 使用默认菜单');
                this.renderDefaultMenu();
            }
        },

        async loadMenuConfig() {
            const roleType = this.currentUser?.roleType || 'collaborator';
            const userId = this.currentUser?.userId || 'default-user';
            
            const response = await fetch(`/api/v1/scene-auth/menu-config?role=${encodeURIComponent(roleType)}&userId=${encodeURIComponent(userId)}`);
            const result = await response.json();
            
            if (result.status === 'success' && result.data) {
                this.menuConfig = result.data;
                console.log('[NexusMenu] 菜单配置加载成功:', this.menuConfig.length, '项, 角色:', roleType);
            } else {
                throw new Error('菜单配置加载失败');
            }
        },

        renderMenu() {
            const navMenu = document.getElementById('nav-menu');
            if (!navMenu) {
                console.warn('[NexusMenu] 菜单容器未找到');
                return;
            }

            if (!this.menuConfig || this.menuConfig.length === 0) {
                this.renderDefaultMenu();
                return;
            }

            const currentPath = window.location.pathname;
            
            navMenu.innerHTML = '';
            
            this.menuConfig.forEach(item => {
                const li = this.createMenuItem(item, currentPath, 0);
                navMenu.appendChild(li);
            });
        },

        createMenuItem(item, currentPath, level) {
            const li = document.createElement('li');
            li.className = 'nav-menu__item';
            li.setAttribute('data-menu-id', item.id);
            
            const isActive = this.isMenuItemActive(item, currentPath);
            const hasActiveChild = this.hasActiveChild(item, currentPath);
            
            if (isActive) {
                li.classList.add('nav-menu__item--active');
            }
            
            if (hasActiveChild) {
                li.classList.add('nav-menu__item--has-active-child');
            }

            if (item.children && item.children.length > 0) {
                const isExpanded = this.expandedMenus.includes(item.id) || isActive || hasActiveChild;
                if (isExpanded) {
                    li.classList.add('nav-menu__item--expanded');
                }

                const a = document.createElement('a');
                a.href = 'javascript:void(0)';
                a.innerHTML = `<i class="${item.icon}"></i><span>${item.name}</span><i class="ri-arrow-right-s-line nav-menu__arrow"></i>`;
                a.addEventListener('click', (e) => {
                    e.preventDefault();
                    this.toggleSubmenu(item.id, li);
                });
                li.appendChild(a);

                const submenu = document.createElement('ul');
                submenu.className = 'nav-menu__submenu';
                if (isExpanded) {
                    submenu.classList.add('nav-menu__submenu--open');
                }
                
                item.children.forEach(child => {
                    const childLi = this.createMenuItem(child, currentPath, level + 1);
                    submenu.appendChild(childLi);
                });
                
                li.appendChild(submenu);
            } else {
                const a = document.createElement('a');
                a.href = item.url || '#';
                a.innerHTML = `<i class="${item.icon}"></i><span>${item.name}</span>`;
                li.appendChild(a);
            }

            return li;
        },

        isMenuItemActive(item, currentPath) {
            if (item.active) return true;
            if (item.url && currentPath.includes(item.url)) return true;
            return false;
        },

        hasActiveChild(item, currentPath) {
            if (!item.children) return false;
            return item.children.some(child => 
                this.isMenuItemActive(child, currentPath) || this.hasActiveChild(child, currentPath)
            );
        },

        toggleSubmenu(itemId, li) {
            const submenu = li.querySelector('.nav-menu__submenu');
            if (!submenu) return;

            if (submenu.classList.contains('nav-menu__submenu--open')) {
                submenu.classList.remove('nav-menu__submenu--open');
                li.classList.remove('nav-menu__item--expanded');
                this.expandedMenus = this.expandedMenus.filter(id => id !== itemId);
            } else {
                submenu.classList.add('nav-menu__submenu--open');
                li.classList.add('nav-menu__item--expanded');
                if (!this.expandedMenus.includes(itemId)) {
                    this.expandedMenus.push(itemId);
                }
            }
            this.saveExpandedMenus();
        },

        renderDefaultMenu() {
            const navMenu = document.getElementById('nav-menu');
            if (!navMenu) return;

            const defaultMenus = [
                { name: '工作台', url: '/console/pages/role-installer.html', icon: 'ri-home-line' },
                { name: '技能市场', url: '/console/pages/capability-discovery.html', icon: 'ri-store-2-line' },
                { name: '已安装技能', url: '/console/pages/my-capabilities.html', icon: 'ri-download-cloud-line' }
            ];

            navMenu.innerHTML = defaultMenus.map(item => {
                const isActive = window.location.pathname.includes(item.url);
                return `
                    <li class="nav-menu__item ${isActive ? 'nav-menu__item--active' : ''}">
                        <a href="${item.url}"><i class="${item.icon}"></i><span>${item.name}</span></a>
                    </li>
                `;
            }).join('');
        },

        async refresh() {
            this.initialized = false;
            this.menuConfig = null;
            await this.init();
        },

        getCurrentUser() {
            return this.currentUser;
        },

        getCurrentRole() {
            return this.currentUser?.roleType || null;
        }
    };

    window.NexusMenu = NexusMenu;

})();
