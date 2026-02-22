/**
 * SkillCenter 统一页面初始化模块
 * 所有页面都应该使用此模块进行初始化
 */

(function() {
  'use strict';

  const PageInit = {
    version: '1.0.0',
    
    initialized: false,
    
    config: {
      autoInitMenu: true,
      autoBindThemeToggle: true,
      autoBindSidebarToggle: true,
      defaultTitle: 'SkillCenter Console'
    },
    
    init(options = {}) {
      this.config = { ...this.config, ...options };
      
      if (this.initialized) {
        console.warn('[PageInit] 页面已经初始化，跳过');
        return this;
      }
      
      console.log('[PageInit] 开始初始化页面...');
      
      if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', async () => await this._doInit());
      } else {
        this._doInit();
      }
      
      return this;
    },
    
    async _doInit() {
      try {
        console.log('[PageInit] _doInit 开始执行...');
        
        this._initTheme();
        
        if (this.config.autoInitMenu) {
          console.log('[PageInit] 初始化菜单...');
          await this._initMenu();
        }
        
        if (this.config.autoBindThemeToggle) {
          this._bindThemeToggle();
        }
        
        if (this.config.autoBindSidebarToggle) {
          this._bindSidebarToggle();
        }
        
        this._initPageSpecific();
        
        this.initialized = true;
        console.log('[PageInit] 页面初始化完成');
        
        window.dispatchEvent(new CustomEvent('pageInitComplete'));
        
      } catch (error) {
        console.error('[PageInit] 初始化失败:', error);
      }
    },
    
    _initTheme() {
      if (typeof window.themeManager !== 'undefined') {
        window.themeManager.applyTheme();
      }
    },
    
    async _initMenu() {
      if (typeof NexusMenu !== 'undefined') {
        await NexusMenu.init();
      } else {
        console.warn('[PageInit] NexusMenu 未加载');
      }
    },
    
    _bindThemeToggle() {
      const themeToggle = document.querySelector('[data-nx-theme-toggle]');
      if (themeToggle && typeof window.toggleTheme === 'function') {
        themeToggle.addEventListener('click', () => {
          window.toggleTheme();
        });
      }
    },
    
    _bindSidebarToggle() {
      const sidebarToggle = document.querySelector('[data-nx-sidebar-toggle]');
      if (sidebarToggle) {
        sidebarToggle.addEventListener('click', () => {
          const sidebar = document.querySelector('.nx-page__sidebar');
          if (sidebar) {
            sidebar.classList.toggle('collapsed');
          }
        });
      }
    },
    
    _initPageSpecific() {
      if (typeof window.onPageInit === 'function') {
        try {
          window.onPageInit();
        } catch (error) {
          console.error('[PageInit] 页面特定初始化失败:', error);
        }
      }
    },
    
    setTitle(title) {
      document.title = `${title} - ${this.config.defaultTitle}`;
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
    
    showError(containerId, message = '加载失败') {
      this.showEmpty(containerId, message, '请检查网络连接后重试', 'ri-error-warning-line');
      const container = document.getElementById(containerId);
      if (container) {
        const icon = container.querySelector('.nx-empty__icon');
        if (icon) {
          icon.style.color = 'var(--nx-danger)';
        }
      }
    }
  };

  window.PageInit = PageInit;

  const currentScript = document.currentScript;
  if (currentScript && currentScript.hasAttribute('data-auto-init')) {
    const doInit = () => {
      PageInit.init();
    };
    
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', doInit);
    } else {
      doInit();
    }
  }

})();
