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
      },
      
      apply(theme) {
        this.current = theme;
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('nx-theme', theme);
        
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
          btn.addEventListener('click', () => this.toggle());
        });
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
    
    // 初始化
    init() {
      this.theme.init();
      this.sidebar.init();
      this.nav.init();
      this.modal.init();
      
      console.log(`ooderNexus UI v${this.version} initialized`);
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
