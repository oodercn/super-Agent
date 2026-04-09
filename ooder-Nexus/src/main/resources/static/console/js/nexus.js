/**
 * ooderNexus UI Framework v2.0 - JavaScript Core
 * 全新重构版本
 */

(function() {
  'use strict';

  // Nexus 全局命名空间
  window.NX = {
    version: '2.0.0',
    
    // 主题管理
    theme: {
      current: localStorage.getItem('nx-theme') || 'dark',
      
      init() {
        this.apply(this.current);
        this.setupToggle();
        // 确保初始化后更新按钮图标
        this.updateButtons();
      },
      
      apply(theme) {
        this.current = theme;
        
        if (theme === 'light') {
          document.documentElement.setAttribute('data-theme', 'light');
        } else {
          document.documentElement.removeAttribute('data-theme');
        }
        
        localStorage.setItem('nx-theme', theme);
        
        // 更新所有主题切换按钮的图标
        this.updateButtons();
        
        // 触发主题变更事件
        window.dispatchEvent(new CustomEvent('nx:themechange', { 
          detail: { theme } 
        }));
      },
      
      toggle() {
        const newTheme = this.current === 'dark' ? 'light' : 'dark';
        this.apply(newTheme);
      },
      
      setupToggle() {
        document.querySelectorAll('[data-nx-theme-toggle]').forEach(btn => {
          // 移除旧的事件监听器，避免重复绑定
          btn.removeEventListener('click', this._toggleHandler);
          btn.addEventListener('click', () => this.toggle());
        });
      },
      
      updateButtons() {
        const buttons = document.querySelectorAll('[data-nx-theme-toggle]');
        if (buttons.length === 0) {
          console.warn('[NX.theme] 未找到主题切换按钮');
          return;
        }
        
        buttons.forEach(btn => {
          const icon = btn.querySelector('i');
          if (icon) {
            // 深色主题显示太阳图标（暗示可切换到浅色），浅色主题显示月亮图标（暗示可切换到深色）
            icon.className = this.current === 'dark' ? 'ri-sun-line' : 'ri-moon-line';
          }
        });
        
        console.log('[NX.theme] 按钮图标已更新，当前主题:', this.current);
      }
    },
    
    // 侧边栏管理
    sidebar: {
      init() {
        this.setupToggle();
        this.setupMobile();
      },
      
      toggle() {
        const sidebar = document.querySelector('.nx-page__sidebar');
        if (!sidebar) return;
        
        if (window.innerWidth <= 768) {
          sidebar.classList.toggle('nx-page__sidebar--open');
        } else {
          sidebar.classList.toggle('nx-page__sidebar--collapsed');
        }
      },
      
      setupToggle() {
        document.querySelectorAll('[data-nx-sidebar-toggle]').forEach(btn => {
          btn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.toggle();
          });
        });
      },
      
      setupMobile() {
        document.addEventListener('click', (e) => {
          const sidebar = document.querySelector('.nx-page__sidebar');
          if (sidebar?.classList.contains('nx-page__sidebar--open')) {
            if (!sidebar.contains(e.target) && !e.target.closest('[data-nx-sidebar-toggle]')) {
              sidebar.classList.remove('nx-page__sidebar--open');
            }
          }
        });
        
        window.addEventListener('resize', () => {
          const sidebar = document.querySelector('.nx-page__sidebar');
          if (sidebar && window.innerWidth > 768) {
            sidebar.classList.remove('nx-page__sidebar--open');
          }
        });
      }
    },
    
    // 导航管理
    nav: {
      init() {
        this.setActive();
      },
      
      setActive() {
        const currentPath = window.location.pathname;
        document.querySelectorAll('.nx-nav__item').forEach(item => {
          const href = item.getAttribute('href');
          if (href && currentPath.includes(href)) {
            item.classList.add('nx-nav__item--active');
          }
        });
      }
    },
    
    // 模态框管理
    modal: {
      open(id) {
        const modal = document.getElementById(id);
        if (modal) {
          modal.classList.add('nx-modal--open');
          document.body.style.overflow = 'hidden';
        }
      },
      
      close(id) {
        const modal = id ? document.getElementById(id) : document.querySelector('.nx-modal--open');
        if (modal) {
          modal.classList.remove('nx-modal--open');
          document.body.style.overflow = '';
        }
      },
      
      init() {
        // 点击遮罩关闭
        document.addEventListener('click', (e) => {
          if (e.target.classList.contains('nx-modal')) {
            this.close(e.target.id);
          }
        });
        
        // ESC键关闭
        document.addEventListener('keydown', (e) => {
          if (e.key === 'Escape') {
            this.close();
          }
        });
      }
    },
    
    // 用户菜单管理
    userMenu: {
      init() {
        this.bindEvents();
        this.loadUserInfo();
      },
      
      bindEvents() {
        const userMenu = document.getElementById('user-menu');
        const dropdown = document.getElementById('user-dropdown');
        
        if (userMenu && dropdown) {
          userMenu.addEventListener('click', (e) => {
            e.stopPropagation();
            dropdown.classList.toggle('nx-page__dropdown-menu--open');
          });
          
          document.addEventListener('click', () => {
            dropdown.classList.remove('nx-page__dropdown-menu--open');
          });
          
          dropdown.querySelectorAll('.nx-page__dropdown-item').forEach(item => {
            item.addEventListener('click', () => {
              dropdown.classList.remove('nx-page__dropdown-menu--open');
            });
          });
        }
      },
      
      loadUserInfo() {
        const avatarEl = document.getElementById('user-avatar');
        const nameEl = document.getElementById('user-name');
        
        if (!avatarEl || !nameEl) return;
        
        const session = NX.storage.get('nx-session');
        if (session && session.user) {
          const user = session.user;
          avatarEl.textContent = (user.name || user.username || 'U').charAt(0).toUpperCase();
          nameEl.textContent = user.name || user.username || '用户';
        } else {
          fetch('/api/v1/org/users/current')
            .then(res => res.json())
            .then(result => {
              if (result.status === 'success' && result.data) {
                const user = result.data;
                avatarEl.textContent = (user.name || user.userId || 'U').charAt(0).toUpperCase();
                nameEl.textContent = user.name || user.userId || '用户';
                NX.storage.set('nx-session', { user: user });
              } else {
                avatarEl.textContent = 'U';
                nameEl.textContent = '用户';
              }
            })
            .catch(() => {
              avatarEl.textContent = 'U';
              nameEl.textContent = '用户';
            });
        }
      },
      
      logout() {
        fetch('/api/v1/auth/logout', { method: 'POST' }).catch(() => {});
        NX.storage.clear();
        window.location.href = '/console/pages/login.html';
      }
    },
    
    // 初始化
    init() {
      this.theme.init();
      this.sidebar.init();
      this.nav.init();
      this.modal.init();
      this.userMenu.init();
      
      console.log(`ooderNexus UI v${this.version} initialized`);
    },
    
    // ========== 公共组件 ==========
    
    /**
     * 公共组件渲染器
     */
    components: {
      /**
       * 渲染用户菜单组件
       * @returns {string} HTML字符串
       */
      renderUserMenu() {
        return `
          <div class="nx-relative">
            <div class="nx-page__user-menu" id="user-menu">
              <div class="nx-page__user-avatar" id="user-avatar">U</div>
              <span class="nx-page__user-name" id="user-name">加载中...</span>
              <i class="ri-arrow-down-s-line"></i>
            </div>
            <div class="nx-page__dropdown-menu" id="user-dropdown">
              <div class="nx-page__dropdown-item" onclick="window.location.href='my-profile.html'">
                <i class="ri-user-line"></i> 个人中心
              </div>
              <div class="nx-page__dropdown-item" onclick="window.location.href='my-capabilities.html'">
                <i class="ri-puzzle-line"></i> 我的能力
              </div>
              <div class="nx-page__dropdown-item nx-page__dropdown-item--danger" onclick="NX.userMenu.logout()">
                <i class="ri-logout-box-line"></i> 退出登录
              </div>
            </div>
          </div>
        `;
      },
      
      /**
       * 渲染主题切换按钮
       * @returns {string} HTML字符串
       */
      renderThemeToggle() {
        return `
          <button class="nx-btn nx-btn--ghost nx-btn--icon" data-nx-theme-toggle aria-label="切换主题">
            <i class="ri-sun-line"></i>
          </button>
        `;
      },
      
      /**
       * 渲染侧边栏折叠按钮
       * @returns {string} HTML字符串
       */
      renderSidebarToggle() {
        return `
          <button class="nx-btn nx-btn--ghost nx-btn--icon" data-nx-sidebar-toggle aria-label="切换侧边栏">
            <i class="ri-menu-line"></i>
          </button>
        `;
      },
      
      /**
       * 渲染完整的页面头部工具栏
       * @param {Object} options - 配置选项
       * @param {string} options.title - 页面标题
       * @param {string} options.subtitle - 页面副标题
       * @param {string} options.icon - 标题图标
       * @param {string} options.actions - 额外的操作按钮HTML
       * @returns {string} HTML字符串
       */
      renderPageHeader(options = {}) {
        const { title = '页面标题', subtitle = '', icon = 'ri-file-line', actions = '' } = options;
        
        return `
          <header class="nx-page__header">
            <div class="nx-flex nx-items-center nx-gap-3">
              ${this.renderSidebarToggle()}
              <div class="nx-page__title">
                <h1><i class="${icon}" aria-hidden="true"></i> ${title}</h1>
                ${subtitle ? `<p>${subtitle}</p>` : ''}
              </div>
            </div>
            <div class="nx-page__actions">
              ${this.renderThemeToggle()}
              ${this.renderUserMenu()}
              ${actions}
            </div>
          </header>
        `;
      },
      
      /**
       * 初始化页面头部组件
       * 自动查找并初始化页面中的用户菜单、主题切换等组件
       */
      initPageHeader() {
        // 初始化用户菜单
        NX.userMenu.init();
        // 初始化主题切换
        NX.theme.setupToggle();
        NX.theme.updateButtons();
        // 初始化侧边栏切换
        NX.sidebar.setupToggle();
      }
    },
    
    // ========== 工具函数 ==========
    
    /**
     * 显示通知
     * @param {string} message - 通知消息
     * @param {string} type - 通知类型: success, error, warning, info
     * @param {number} duration - 显示时长(毫秒)
     */
    notify(message, type = 'info', duration = 3000) {
      // 移除已存在的通知
      const existing = document.querySelector('.nx-notification');
      if (existing) existing.remove();
      
      const notification = document.createElement('div');
      notification.className = `nx-notification nx-notification--${type}`;
      notification.textContent = message;
      document.body.appendChild(notification);
      
      setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => notification.remove(), 300);
      }, duration);
    },
    
    /**
     * 显示成功通知
     */
    success(message, duration) {
      this.notify(message, 'success', duration);
    },
    
    /**
     * 显示错误通知
     */
    error(message, duration) {
      this.notify(message, 'error', duration);
    },
    
    /**
     * 显示警告通知
     */
    warning(message, duration) {
      this.notify(message, 'warning', duration);
    },
    
    /**
     * 显示加载状态
     * @param {string} containerId - 容器ID
     * @param {string} text - 加载文本
     */
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
    
    /**
     * 隐藏加载状态
     * @param {string} containerId - 容器ID
     * @param {string} html - 替换的HTML内容
     */
    hideLoading(containerId, html = '') {
      const container = document.getElementById(containerId);
      if (!container) return;
      
      const loading = container.querySelector('.nx-loading');
      if (loading) {
        loading.remove();
      }
      if (html) {
        container.innerHTML = html;
      }
    },
    
    /**
     * 显示空状态
     * @param {string} containerId - 容器ID
     * @param {string} title - 标题
     * @param {string} description - 描述
     * @param {string} icon - 图标类名
     */
    showEmpty(containerId, title = '暂无数据', description = '', icon = 'ri-inbox-line') {
      const container = document.getElementById(containerId);
      if (!container) return;
      
      container.innerHTML = `
        <div class="nx-empty">
          <i class="${icon} nx-empty__icon"></i>
          <div class="nx-empty__title">${title}</div>
          ${description ? `<div class="nx-empty__description">${description}</div>` : ''}
        </div>
      `;
    },
    
    /**
     * 显示错误状态
     */
    showError(containerId, message = '加载失败') {
      this.showEmpty(containerId, message, '请检查网络连接后重试', 'ri-error-warning-line');
      const icon = document.querySelector(`#${containerId} .nx-empty__icon`);
      if (icon) {
        icon.style.color = 'var(--nx-danger)';
      }
    },
    
    /**
     * 格式化日期
     * @param {Date|string|number} date - 日期
     * @param {string} format - 格式: YYYY-MM-DD, YYYY-MM-DD HH:mm:ss, etc.
     */
    formatDate(date, format = 'YYYY-MM-DD') {
      const d = new Date(date);
      if (isNaN(d.getTime())) return '-';
      
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hours = String(d.getHours()).padStart(2, '0');
      const minutes = String(d.getMinutes()).padStart(2, '0');
      const seconds = String(d.getSeconds()).padStart(2, '0');
      
      return format
        .replace('YYYY', year)
        .replace('MM', month)
        .replace('DD', day)
        .replace('HH', hours)
        .replace('mm', minutes)
        .replace('ss', seconds);
    },
    
    /**
     * 格式化数字
     * @param {number} num - 数字
     * @param {number} decimals - 小数位数
     * @param {string} unit - 单位
     */
    formatNumber(num, decimals = 0, unit = '') {
      if (num === null || num === undefined || isNaN(num)) return '-';
      
      let formatted = Number(num).toFixed(decimals);
      
      // 添加千分位
      if (decimals === 0) {
        formatted = Number(formatted).toLocaleString('zh-CN');
      }
      
      // 大数字简化
      if (num >= 1000000) {
        return (num / 1000000).toFixed(1) + 'M' + unit;
      } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'K' + unit;
      }
      
      return formatted + unit;
    },
    
    /**
     * 格式化文件大小
     */
    formatFileSize(bytes) {
      if (bytes === 0) return '0 B';
      const k = 1024;
      const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },
    
    /**
     * 防抖函数
     * @param {Function} func - 要执行的函数
     * @param {number} wait - 等待时间(毫秒)
     */
    debounce(func, wait = 300) {
      let timeout;
      return function executedFunction(...args) {
        const later = () => {
          clearTimeout(timeout);
          func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
      };
    },
    
    /**
     * 节流函数
     * @param {Function} func - 要执行的函数
     * @param {number} limit - 限制时间(毫秒)
     */
    throttle(func, limit = 300) {
      let inThrottle;
      return function executedFunction(...args) {
        if (!inThrottle) {
          func(...args);
          inThrottle = true;
          setTimeout(() => inThrottle = false, limit);
        }
      };
    },
    
    /**
     * 深度克隆对象
     */
    clone(obj) {
      return JSON.parse(JSON.stringify(obj));
    },
    
    /**
     * 合并对象
     */
    merge(target, ...sources) {
      return Object.assign({}, target, ...sources);
    },
    
    /**
     * 获取URL参数
     */
    getQueryParam(name) {
      const urlParams = new URLSearchParams(window.location.search);
      return urlParams.get(name);
    },
    
    /**
     * 设置URL参数
     */
    setQueryParam(name, value) {
      const url = new URL(window.location);
      if (value === null || value === undefined) {
        url.searchParams.delete(name);
      } else {
        url.searchParams.set(name, value);
      }
      window.history.pushState({}, '', url);
    },
    
    /**
     * 复制到剪贴板
     */
    async copyToClipboard(text) {
      try {
        await navigator.clipboard.writeText(text);
        this.success('已复制到剪贴板');
        return true;
      } catch (err) {
        this.error('复制失败');
        return false;
      }
    },
    
    /**
     * 下载文件
     */
    downloadFile(url, filename) {
      const a = document.createElement('a');
      a.href = url;
      a.download = filename || '';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    },
    
    /**
     * 下载Blob文件
     */
    downloadBlob(blob, filename) {
      const url = URL.createObjectURL(blob);
      this.downloadFile(url, filename);
      URL.revokeObjectURL(url);
    },
    
    /**
     * 确认对话框
     */
    confirm(message, onConfirm, onCancel) {
      if (window.confirm(message)) {
        onConfirm?.();
      } else {
        onCancel?.();
      }
    },
    
    /**
     * 安全的JSON解析
     */
    safeJSONParse(str, defaultValue = null) {
      try {
        return JSON.parse(str);
      } catch (e) {
        return defaultValue;
      }
    },
    
    /**
     * 生成唯一ID
     */
    generateId(prefix = 'nx') {
      return `${prefix}-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
    },
    
    /**
     * 检查元素是否在视口内
     */
    isInViewport(element) {
      const rect = element.getBoundingClientRect();
      return (
        rect.top >= 0 &&
        rect.left >= 0 &&
        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
        rect.right <= (window.innerWidth || document.documentElement.clientWidth)
      );
    },
    
    /**
     * 滚动到元素
     */
    scrollTo(element, behavior = 'smooth') {
      if (typeof element === 'string') {
        element = document.getElementById(element);
      }
      element?.scrollIntoView({ behavior });
    },
    
    /**
     * 本地存储封装
     */
    api: {
      async get(url) {
        const response = await fetch(url, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        });
        return response.json();
      },
      
      async post(url, data) {
        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        });
        return response.json();
      }
    },
    
    storage: {
      get(key, defaultValue = null) {
        try {
          const item = localStorage.getItem(key);
          return item ? JSON.parse(item) : defaultValue;
        } catch (e) {
          return defaultValue;
        }
      },
      
      set(key, value) {
        try {
          localStorage.setItem(key, JSON.stringify(value));
          return true;
        } catch (e) {
          return false;
        }
      },
      
      remove(key) {
        localStorage.removeItem(key);
      },
      
      clear() {
        localStorage.clear();
      }
    },
    
    /**
     * Session存储封装
     */
    session: {
      get(key, defaultValue = null) {
        try {
          const item = sessionStorage.getItem(key);
          return item ? JSON.parse(item) : defaultValue;
        } catch (e) {
          return defaultValue;
        }
      },
      
      set(key, value) {
        try {
          sessionStorage.setItem(key, JSON.stringify(value));
          return true;
        } catch (e) {
          return false;
        }
      },
      
      remove(key) {
        sessionStorage.removeItem(key);
      },
      
      clear() {
        sessionStorage.clear();
      }
    }
  };

  // DOM加载完成后初始化
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => NX.init());
  } else {
    NX.init();
  }
})();
