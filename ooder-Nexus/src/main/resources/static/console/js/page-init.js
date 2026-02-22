/**
 * ooderNexus 统一页面初始化模块
 * 所有页面都应该使用此模块进行初始化
 * 
 * 使用方法:
 * 1. 在页面中引入: <script src="../../js/page-init.js"></script>
 * 2. 调用: PageInit.init();
 * 
 * 或者使用自动初始化（推荐）:
 * <script src="../../js/page-init.js" data-auto-init></script>
 */

(function() {
  'use strict';

  /**
   * 页面初始化配置
   */
  const PageInit = {
    // 版本号
    version: '1.0.0',
    
    // 初始化状态
    initialized: false,
    
    // 页面配置
    config: {
      // 是否自动初始化菜单
      autoInitMenu: true,
      // 是否自动绑定主题切换
      autoBindThemeToggle: true,
      // 是否自动绑定侧边栏切换
      autoBindSidebarToggle: true,
      // 默认页面标题
      defaultTitle: 'Nexus Console'
    },
    
    /**
     * 初始化页面
     * @param {Object} options - 初始化选项
     */
    init(options = {}) {
      // 合并配置
      this.config = { ...this.config, ...options };
      
      // 防止重复初始化
      if (this.initialized) {
        console.warn('[PageInit] 页面已经初始化，跳过');
        return this;
      }
      
      console.log('[PageInit] 开始初始化页面...');
      console.log('[PageInit] document.readyState:', document.readyState);
      
      // 等待DOM加载完成
      if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', async () => await this._doInit());
      } else {
        this._doInit();
      }
      
      return this;
    },
    
    /**
     * 执行初始化
     * @private
     */
    async _doInit() {
      try {
        console.log('[PageInit] _doInit 开始执行...');
        
        // 1. 初始化 Nexus UI 框架
        console.log('[PageInit] 步骤1: 初始化 Nexus UI...');
        this._initNexusUI();
        
        // 2. 初始化菜单（菜单加载完成后会绑定事件）
        if (this.config.autoInitMenu) {
          console.log('[PageInit] 步骤2: 初始化菜单...');
          await this._initMenu();
        }
        
        // 3. 绑定主题切换
        if (this.config.autoBindThemeToggle) {
          console.log('[PageInit] 步骤3: 绑定主题切换...');
          this._bindThemeToggle();
        }
        
        // 4. 绑定侧边栏切换
        if (this.config.autoBindSidebarToggle) {
          console.log('[PageInit] 步骤4: 绑定侧边栏切换...');
          this._bindSidebarToggle();
        }
        
        // 5. 执行页面特定的初始化
        console.log('[PageInit] 步骤5: 页面特定初始化...');
        this._initPageSpecific();
        
        this.initialized = true;
        console.log('[PageInit] 页面初始化完成');
        
        // 触发自定义事件
        window.dispatchEvent(new CustomEvent('pageInitComplete'));
        
      } catch (error) {
        console.error('[PageInit] 初始化失败:', error);
      }
    },
    
    /**
     * 初始化 Nexus UI 框架
     * @private
     */
    _initNexusUI() {
      // 确保 NX 对象存在
      if (typeof NX === 'undefined') {
        console.warn('[PageInit] NX 对象未找到，部分功能可能不可用');
        return;
      }
      
      // 初始化主题
      if (NX.theme && typeof NX.theme.init === 'function') {
        NX.theme.init();
      }
      
      // 初始化侧边栏
      if (NX.sidebar && typeof NX.sidebar.init === 'function') {
        NX.sidebar.init();
      }
      
      // 初始化导航
      if (NX.nav && typeof NX.nav.init === 'function') {
        NX.nav.init();
      }
      
      // 初始化模态框
      if (NX.modal && typeof NX.modal.init === 'function') {
        NX.modal.init();
      }
    },
    
    /**
     * 初始化菜单
     * @private
     */
    async _initMenu() {
      console.log('[PageInit] _initMenu 开始...');
      console.log('[PageInit] NexusMenu 是否存在:', typeof NexusMenu !== 'undefined');
      console.log('[PageInit] ApiClient 是否存在:', typeof ApiClient !== 'undefined');
      console.log('[PageInit] ApiClient.getMenu 是否存在:', typeof ApiClient !== 'undefined' && typeof ApiClient.getMenu === 'function');
      
      // 使用 NexusMenu 系统
      if (typeof NexusMenu !== 'undefined') {
        console.log('[PageInit] 调用 NexusMenu.init()...');
        await NexusMenu.init();
        console.log('[PageInit] NexusMenu.init() 完成');
      } else {
        console.warn('[PageInit] NexusMenu 未加载');
      }
    },
    
    /**
     * 渲染菜单项
     * @private
     */
    _renderMenuItems(items, level = 0) {
      return items.map(item => {
        const hasChildren = item.children && item.children.length > 0;
        const isActive = this._isCurrentPage(item.url);
        const url = this._buildMenuUrl(item.url);
        
        let html = `<li class="nav-item ${isActive ? 'active' : ''}" data-level="${level}" data-id="${item.id}">`;
        
        if (hasChildren) {
          html += `
            <a href="javascript:void(0)" class="nav-link has-submenu" data-toggle="submenu">
              ${item.icon ? `<i class="${item.icon}"></i>` : ''}
              <span>${item.name}</span>
              <i class="ri-arrow-down-s-line submenu-icon"></i>
            </a>
            <ul class="submenu ${isActive ? 'show' : ''}">
              ${this._renderMenuItems(item.children, level + 1)}
            </ul>
          `;
        } else {
          html += `
            <a href="${url}" class="nav-link">
              ${item.icon ? `<i class="${item.icon}"></i>` : ''}
              <span>${item.name}</span>
            </a>
          `;
        }
        
        html += '</li>';
        return html;
      }).join('');
    },
    
    /**
     * 构建菜单URL
     * @private
     */
    _buildMenuUrl(url) {
      if (!url) return 'javascript:void(0)';
      
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
    },
    
    /**
     * 判断是否为当前页面
     * @private
     */
    _isCurrentPage(url) {
      if (!url) return false;
      const currentPath = window.location.pathname;
      return currentPath.includes(url) || url.includes(currentPath);
    },
    
    /**
     * 绑定菜单点击事件
     * @private
     */
    _bindMenuEvents() {
      const navMenu = document.getElementById('nav-menu');
      if (!navMenu) return;
      
      // 使用事件委托处理菜单点击
      navMenu.addEventListener('click', (e) => {
        const link = e.target.closest('.nav-link');
        if (!link) return;
        
        // 检查是否有子菜单
        const hasSubmenu = link.hasAttribute('data-toggle') && link.getAttribute('data-toggle') === 'submenu';
        
        if (hasSubmenu) {
          e.preventDefault();
          e.stopPropagation();
          
          // 切换子菜单显示状态
          const submenu = link.nextElementSibling;
          if (submenu && submenu.classList.contains('submenu')) {
            const isExpanded = submenu.classList.contains('show');
            
            // 切换当前子菜单
            if (isExpanded) {
              submenu.classList.remove('show');
              link.classList.remove('expanded');
            } else {
              submenu.classList.add('show');
              link.classList.add('expanded');
            }
            
            // 保存展开状态到 localStorage
            const menuId = link.closest('.nav-item')?.getAttribute('data-id');
            if (menuId) {
              this._saveMenuState(menuId, !isExpanded);
            }
          }
        }
      });
    },
    
    /**
     * 保存菜单展开状态
     * @private
     */
    _saveMenuState(menuId, isExpanded) {
      try {
        let expandedMenus = JSON.parse(localStorage.getItem('expandedMenus') || '[]');
        
        if (isExpanded) {
          if (!expandedMenus.includes(menuId)) {
            expandedMenus.push(menuId);
          }
        } else {
          expandedMenus = expandedMenus.filter(id => id !== menuId);
        }
        
        localStorage.setItem('expandedMenus', JSON.stringify(expandedMenus));
      } catch (e) {
        console.warn('[PageInit] 保存菜单状态失败:', e);
      }
    },
    
    /**
     * 恢复菜单展开状态
     * @private
     */
    _restoreMenuState() {
      try {
        const expandedMenus = JSON.parse(localStorage.getItem('expandedMenus') || '[]');
        
        expandedMenus.forEach(menuId => {
          const menuItem = document.querySelector(`.nav-item[data-id="${menuId}"]`);
          if (menuItem) {
            const submenu = menuItem.querySelector('.submenu');
            const link = menuItem.querySelector('.nav-link');
            
            if (submenu) {
              submenu.classList.add('show');
            }
            if (link) {
              link.classList.add('expanded');
            }
          }
        });
      } catch (e) {
        console.warn('[PageInit] 恢复菜单状态失败:', e);
      }
    },
    
    /**
     * 高亮当前菜单项
     * @private
     */
    _highlightCurrentMenu() {
      const currentPath = window.location.pathname;
      const navLinks = document.querySelectorAll('.nav-menu a');
      
      navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href && !href.startsWith('javascript:')) {
          // 检查是否匹配当前页面
          if (currentPath.includes(href) || href.includes(currentPath)) {
            link.classList.add('active');
            // 展开父级菜单
            const parentSubmenu = link.closest('.submenu');
            if (parentSubmenu) {
              parentSubmenu.classList.add('show');
              const parentLink = parentSubmenu.previousElementSibling;
              if (parentLink) {
                parentLink.classList.add('active');
              }
            }
          }
        }
      });
      
      // 恢复菜单展开状态
      this._restoreMenuState();
    },
    
    /**
     * 绑定主题切换
     * @private
     */
    _bindThemeToggle() {
      const themeToggle = document.querySelector('[data-nx-theme-toggle]');
      if (themeToggle && typeof NX !== 'undefined' && NX.theme) {
        themeToggle.addEventListener('click', () => {
          NX.theme.toggle();
        });
      }
    },
    
    /**
     * 绑定侧边栏切换
     * @private
     */
    _bindSidebarToggle() {
      const sidebarToggle = document.querySelector('[data-nx-sidebar-toggle]');
      if (sidebarToggle && typeof NX !== 'undefined' && NX.sidebar) {
        sidebarToggle.addEventListener('click', () => {
          NX.sidebar.toggle();
        });
      }
    },
    
    /**
     * 执行页面特定的初始化
     * @private
     */
    _initPageSpecific() {
      // 调用 globals.js 的初始化
      if (typeof window._globalsInit === 'function') {
        try {
          window._globalsInit();
        } catch (error) {
          console.error('[PageInit] globals.js 初始化失败:', error);
        }
      }
      
      // 调用 common.js 的初始化
      if (typeof window._commonInit === 'function') {
        try {
          window._commonInit();
        } catch (error) {
          console.error('[PageInit] common.js 初始化失败:', error);
        }
      }
      
      // 调用全局的 onPageInit 函数（如果存在）
      if (typeof window.onPageInit === 'function') {
        try {
          window.onPageInit();
        } catch (error) {
          console.error('[PageInit] 页面特定初始化失败:', error);
        }
      }
    },
    
    /**
     * 设置页面标题
     * @param {string} title - 页面标题
     */
    setTitle(title) {
      document.title = `${title} - ${this.config.defaultTitle}`;
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
     */
    hideLoading(containerId) {
      const container = document.getElementById(containerId);
      if (!container) return;
      
      const loading = container.querySelector('.nx-loading');
      if (loading) {
        loading.remove();
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
     * @param {string} containerId - 容器ID
     * @param {string} message - 错误消息
     */
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

  // 暴露到全局
  window.PageInit = PageInit;

  // 自动初始化（如果脚本有 data-auto-init 属性）
  const currentScript = document.currentScript;
  if (currentScript && currentScript.hasAttribute('data-auto-init')) {
    const doInit = () => {
      if (typeof NexusMenu !== 'undefined') {
        PageInit.init();
      } else {
        console.warn('[PageInit] NexusMenu 未加载，菜单可能无法正常显示');
        PageInit.init();
      }
    };
    
    // 检查是否所有依赖都已加载
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', doInit);
    } else {
      doInit();
    }
  }

})();
