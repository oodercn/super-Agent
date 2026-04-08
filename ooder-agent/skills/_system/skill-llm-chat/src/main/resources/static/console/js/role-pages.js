/**
 * 角色页面公共JavaScript
 * 所有角色页面（installer, admin, leader, collaborator）共用
 */

(function() {
    'use strict';

    const RolePage = {
        currentUser: null,
        requiredRole: null,
        
        init(options = {}) {
            this.requiredRole = options.requiredRole || null;
            this.onLoginSuccess = options.onLoginSuccess || null;
            this.onLoginFailure = options.onLoginFailure || null;
            
            this.checkLogin();
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
                    <a href="${item.url}"><i class="${item.icon}"></i> ${item.name}</a>
                </li>
            `).join('');
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
        
        showLoading(containerId, text = '加载中...') {
            const container = document.getElementById(containerId);
            if (!container) return;
            
            container.innerHTML = `
                <div class="nx-loading">
                    <div class="nx-loading__spinner"></div>
                    <div class="nx-loading__text">${text}</div>
                </div>
            `;
        },
        
        hideLoading(containerId) {
            const container = document.getElementById(containerId);
            if (!container) return;
            
            const loading = container.querySelector('.nx-loading');
            if (loading) {
                loading.remove();
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
        
        formatTime(timestamp) {
            if (!timestamp) return '-';
            const date = new Date(timestamp);
            return date.toLocaleString('zh-CN');
        },
        
        formatRelativeTime(timestamp) {
            if (!timestamp) return '-';
            
            const now = Date.now();
            const diff = now - timestamp;
            
            if (diff < 60000) return '刚刚';
            if (diff < 3600000) return `${Math.floor(diff / 60000)} 分钟前`;
            if (diff < 86400000) return `${Math.floor(diff / 3600000)} 小时前`;
            if (diff < 604800000) return `${Math.floor(diff / 86400000)} 天前`;
            
            return this.formatTime(timestamp);
        }
    };

    window.RolePage = RolePage;

})();
