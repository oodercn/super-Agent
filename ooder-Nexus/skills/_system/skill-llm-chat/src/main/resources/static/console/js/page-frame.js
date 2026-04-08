/**
 * 页面框架组件
 * 提供统一的页面布局，包含菜单折叠、主题切换、退出按钮
 */

(function() {
    'use strict';

    const PageFrame = {
        initialized: false,
        sidebarCollapsed: false,
        currentUser: null,

        init(options = {}) {
            if (this.initialized) return;
            
            this.requiredRole = options.requiredRole || null;
            this.onLoginSuccess = options.onLoginSuccess || null;
            this.onLoginFailure = options.onLoginFailure || null;
            this.pageTitle = options.pageTitle || 'Ooder';
            this.pageSubtitle = options.pageSubtitle || '';
            
            this.setupEventListeners();
            this.checkLogin();
            this.initialized = true;
        },

        setupEventListeners() {
            document.addEventListener('DOMContentLoaded', () => {
                this.bindSidebarToggle();
                this.bindThemeToggle();
                this.bindLogout();
            });
        },

        bindSidebarToggle() {
            const toggleBtn = document.getElementById('sidebar-toggle');
            if (toggleBtn) {
                toggleBtn.addEventListener('click', () => this.toggleSidebar());
            }
        },

        bindThemeToggle() {
            const themeToggle = document.getElementById('theme-toggle');
            if (themeToggle) {
                themeToggle.addEventListener('click', () => this.toggleTheme());
            }
        },

        bindLogout() {
            const logoutBtn = document.getElementById('logout-btn');
            if (logoutBtn) {
                logoutBtn.addEventListener('click', () => this.handleLogout());
            }
        },

        async checkLogin() {
            try {
                const response = await fetch('/api/v1/auth/session');
                const result = await response.json();
                
                if (result.status === 'success' && result.data) {
                    this.currentUser = result.data;
                    
                    if (this.requiredRole && this.currentUser.roleType !== this.requiredRole) {
                        this.redirectToLogin();
                        return;
                    }
                    
                    this.updateUserInfo();
                    await this.loadMenu();
                    
                    if (this.onLoginSuccess) {
                        this.onLoginSuccess(this.currentUser);
                    }
                } else {
                    this.redirectToLogin();
                }
            } catch (e) {
                console.error('Session check failed:', e);
                if (this.onLoginFailure) {
                    this.onLoginFailure(e);
                } else {
                    this.redirectToLogin();
                }
            }
        },

        updateUserInfo() {
            const userNameEl = document.getElementById('user-name');
            if (userNameEl && this.currentUser) {
                userNameEl.textContent = this.currentUser.name || this.currentUser.username;
            }
            
            const userBadgeEl = document.getElementById('user-badge');
            if (userBadgeEl && this.currentUser) {
                userBadgeEl.title = `${this.currentUser.name} (${this.currentUser.roleType})`;
            }
        },

        async loadMenu() {
            try {
                const roleType = this.currentUser?.roleType || 'collaborator';
                const userId = this.currentUser?.userId || 'default-user';
                
                const response = await fetch(`/api/v1/auth/menu-config?role=${encodeURIComponent(roleType)}&userId=${encodeURIComponent(userId)}`);
                const result = await response.json();
                
                if (result.status === 'success' && result.data) {
                    this.renderMenu(result.data);
                }
            } catch (e) {
                console.error('Failed to load menu:', e);
            }
        },

        renderMenu(menuItems) {
            const menuEl = document.getElementById('nav-menu');
            if (!menuEl) return;
            
            menuEl.innerHTML = menuItems.map(item => `
                <li class="nav-menu__item ${item.active ? 'nav-menu__item--active' : ''}">
                    <a href="${item.url}"><i class="${item.icon}"></i> <span class="nav-menu__text">${item.name}</span></a>
                </li>
            `).join('');
        },

        toggleSidebar() {
            this.sidebarCollapsed = !this.sidebarCollapsed;
            const page = document.querySelector('.nx-page');
            const toggleIcon = document.querySelector('#sidebar-toggle i');
            
            if (this.sidebarCollapsed) {
                page.classList.add('nx-page--sidebar-collapsed');
                if (toggleIcon) toggleIcon.className = 'ri-menu-unfold-line';
            } else {
                page.classList.remove('nx-page--sidebar-collapsed');
                if (toggleIcon) toggleIcon.className = 'ri-menu-fold-line';
            }
        },

        toggleTheme() {
            const html = document.documentElement;
            const themeToggle = document.getElementById('theme-toggle');
            const icon = themeToggle ? themeToggle.querySelector('i') : null;
            const text = themeToggle ? themeToggle.querySelector('span') : null;
            
            if (html.getAttribute('data-theme') === 'light') {
                html.removeAttribute('data-theme');
                localStorage.setItem('nx-theme', 'dark');
                if (icon) icon.className = 'ri-moon-line';
                if (text) text.textContent = '浅色模式';
            } else {
                html.setAttribute('data-theme', 'light');
                localStorage.setItem('nx-theme', 'light');
                if (icon) icon.className = 'ri-sun-line';
                if (text) text.textContent = '深色模式';
            }
        },

        async handleLogout() {
            try {
                await fetch('/api/v1/auth/logout', { method: 'POST' });
            } catch (e) {
                console.error('Logout error:', e);
            }
            localStorage.clear();
            this.redirectToLogin();
        },

        redirectToLogin() {
            window.location.href = '/console/pages/login.html';
        },

        async fetchApi(url, options = {}) {
            const defaultOptions = {
                headers: {
                    'Content-Type': 'application/json'
                }
            };
            
            const mergedOptions = { ...defaultOptions, ...options };
            
            try {
                const response = await fetch(url, mergedOptions);
                const result = await response.json();
                
                if (result.status === 'success') {
                    return result.data;
                } else {
                    throw new Error(result.message || 'API请求失败');
                }
            } catch (e) {
                console.error('API Error:', e);
                throw e;
            }
        },

        showToast(message, type = 'info') {
            const colors = {
                success: 'var(--ns-success)',
                error: 'var(--ns-danger)',
                warning: 'var(--ns-warning)',
                info: 'var(--ns-info)'
            };
            
            const toast = document.createElement('div');
            toast.className = 'nx-toast';
            toast.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                padding: 12px 24px;
                border-radius: 8px;
                background: ${colors[type]};
                color: white;
                z-index: 9999;
                animation: slideIn 0.3s ease-out;
            `;
            toast.textContent = message;
            
            document.body.appendChild(toast);
            
            setTimeout(() => {
                toast.style.animation = 'slideOut 0.3s ease-in';
                setTimeout(() => toast.remove(), 300);
            }, 3000);
        },

        initTheme() {
            const savedTheme = localStorage.getItem('nx-theme') || 'dark';
            const html = document.documentElement;
            const themeToggle = document.getElementById('theme-toggle');
            const icon = themeToggle ? themeToggle.querySelector('i') : null;
            const text = themeToggle ? themeToggle.querySelector('span') : null;
            
            if (savedTheme === 'light') {
                html.setAttribute('data-theme', 'light');
                if (icon) icon.className = 'ri-sun-line';
                if (text) text.textContent = '深色模式';
            } else {
                html.removeAttribute('data-theme');
                if (icon) icon.className = 'ri-moon-line';
                if (text) text.textContent = '浅色模式';
            }
        }
    };

    window.PageFrame = PageFrame;

})();
