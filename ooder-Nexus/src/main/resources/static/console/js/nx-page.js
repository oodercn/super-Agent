class NxPage {
    constructor() {
        this.sidebarCollapsed = false;
        this.sidebarOpen = false;
        this.dropdownOpen = false;
        this.theme = localStorage.getItem('nx-theme') || 'dark';
        
        this.init();
    }
    
    init() {
        this.applyTheme();
        this.bindEvents();
        this.loadUserInfo();
    }
    
    bindEvents() {
        const sidebarToggle = document.getElementById('sidebar-toggle');
        const mobileToggle = document.getElementById('mobile-toggle');
        const overlay = document.getElementById('page-overlay');
        const userMenu = document.getElementById('user-menu');
        const themeToggle = document.getElementById('theme-toggle');
        
        if (sidebarToggle) {
            sidebarToggle.addEventListener('click', () => this.toggleSidebar());
        }
        
        if (mobileToggle) {
            mobileToggle.addEventListener('click', () => this.toggleMobileSidebar());
        }
        
        if (overlay) {
            overlay.addEventListener('click', () => this.closeMobileSidebar());
        }
        
        if (userMenu) {
            userMenu.addEventListener('click', (e) => {
                e.stopPropagation();
                this.toggleDropdown();
            });
        }
        
        if (themeToggle) {
            themeToggle.addEventListener('click', () => this.toggleTheme());
        }
        
        document.addEventListener('click', () => {
            if (this.dropdownOpen) {
                this.closeDropdown();
            }
        });
        
        window.addEventListener('resize', () => {
            if (window.innerWidth > 1024 && this.sidebarOpen) {
                this.closeMobileSidebar();
            }
        });
    }
    
    toggleSidebar() {
        this.sidebarCollapsed = !this.sidebarCollapsed;
        const sidebar = document.querySelector('.nx-page__sidebar');
        if (sidebar) {
            sidebar.classList.toggle('nx-page__sidebar--collapsed', this.sidebarCollapsed);
        }
        localStorage.setItem('nx-sidebar-collapsed', this.sidebarCollapsed);
    }
    
    toggleMobileSidebar() {
        this.sidebarOpen = !this.sidebarOpen;
        const sidebar = document.querySelector('.nx-page__sidebar');
        const overlay = document.getElementById('page-overlay');
        
        if (sidebar) {
            sidebar.classList.toggle('nx-page__sidebar--open', this.sidebarOpen);
        }
        if (overlay) {
            overlay.classList.toggle('nx-page__overlay--active', this.sidebarOpen);
        }
    }
    
    closeMobileSidebar() {
        this.sidebarOpen = false;
        const sidebar = document.querySelector('.nx-page__sidebar');
        const overlay = document.getElementById('page-overlay');
        
        if (sidebar) {
            sidebar.classList.remove('nx-page__sidebar--open');
        }
        if (overlay) {
            overlay.classList.remove('nx-page__overlay--active');
        }
    }
    
    toggleDropdown() {
        this.dropdownOpen = !this.dropdownOpen;
        const dropdown = document.getElementById('user-dropdown');
        if (dropdown) {
            dropdown.classList.toggle('nx-page__dropdown-menu--open', this.dropdownOpen);
        }
    }
    
    closeDropdown() {
        this.dropdownOpen = false;
        const dropdown = document.getElementById('user-dropdown');
        if (dropdown) {
            dropdown.classList.remove('nx-page__dropdown-menu--open');
        }
    }
    
    toggleTheme() {
        this.theme = this.theme === 'dark' ? 'light' : 'dark';
        this.applyTheme();
        localStorage.setItem('nx-theme', this.theme);
    }
    
    applyTheme() {
        if (this.theme === 'light') {
            document.documentElement.setAttribute('data-theme', 'light');
        } else {
            document.documentElement.removeAttribute('data-theme');
        }
        
        const themeIcon = document.getElementById('theme-icon');
        if (themeIcon) {
            themeIcon.className = this.theme === 'light' ? 'ri-sun-line' : 'ri-moon-line';
        }
    }
    
    async loadUserInfo() {
        try {
            const response = await fetch('/api/v1/auth/session');
            const result = await response.json();
            
            if (result.status === 'success' && result.data) {
                const user = result.data;
                const userNameEl = document.getElementById('user-name');
                const userRoleEl = document.getElementById('user-role');
                const userAvatarEl = document.getElementById('user-avatar');
                
                if (userNameEl) userNameEl.textContent = user.name || user.userId || '用户';
                if (userRoleEl) userRoleEl.textContent = user.role || '';
                if (userAvatarEl) userAvatarEl.textContent = (user.name || user.userId || 'U').charAt(0).toUpperCase();
            }
        } catch (e) {
            console.error('Failed to load user info:', e);
        }
    }
    
    logout() {
        fetch('/api/v1/auth/logout', { method: 'POST' }).catch(() => {});
        localStorage.clear();
        window.location.href = '/console/pages/login.html';
    }
}

function initNxPage() {
    window.nxPage = new NxPage();
    
    const collapsed = localStorage.getItem('nx-sidebar-collapsed') === 'true';
    if (collapsed) {
        window.nxPage.sidebarCollapsed = true;
        const sidebar = document.querySelector('.nx-page__sidebar');
        if (sidebar) {
            sidebar.classList.add('nx-page__sidebar--collapsed');
        }
    }
}

document.addEventListener('DOMContentLoaded', initNxPage);
