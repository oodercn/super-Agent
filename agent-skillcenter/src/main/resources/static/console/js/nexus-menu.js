/**
 * Nexus Menu System v3.0
 * 统一的菜单系统，所有页面必须使用此模块
 * 
 * 特性：
 * - 从后端API动态获取菜单
 * - 后端完成角色权限控制
 * - 自动高亮当前页面
 * - 菜单状态持久化
 * - 统一的交互行为
 */

(function() {
  'use strict';

  const NexusMenu = {
    version: '3.0.0',
    
    config: {
      menuApiUrl: '/skillcenter/api/menu',
      containerId: 'nav-menu',
      storageKey: 'skillcenter_menu_state'
    },
    
    state: {
      menuData: null,
      expandedMenus: [],
      initialized: false
    },
    
    async init(options = {}) {
      Object.assign(this.config, options);
      
      if (this.state.initialized) {
        return this;
      }
      
      const container = document.getElementById(this.config.containerId);
      if (!container) {
        this.state.initialized = true;
        return this;
      }
      
      try {
        this._restoreState();
        await this._loadMenuData();
        this._renderMenu();
        this._bindEvents();
        this._highlightCurrentPage();
        
        this.state.initialized = true;
        window.dispatchEvent(new CustomEvent('nexusMenuReady'));
        
      } catch (error) {
        console.error('[NexusMenu] 初始化失败:', error);
        this._renderError('菜单加载失败: ' + error.message);
      }
      
      return this;
    },
    
    async _loadMenuData() {
      try {
        const response = await fetch(this.config.menuApiUrl + '?_t=' + Date.now(), {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          },
          body: JSON.stringify({})
        });

        if (!response.ok) {
          throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }

        const result = await response.json();
        console.log('[NexusMenu] API响应:', result);
        if (result.code !== 200) {
          throw new Error(result.message || '菜单加载失败');
        }
        this.state.menuData = result.data || [];
        console.log('[NexusMenu] 菜单数据:', this.state.menuData);
      } catch (error) {
        console.warn('[NexusMenu] API加载失败，尝试加载本地配置:', error);
        const configResponse = await fetch('/skillcenter/console/menu-config.json');
        if (configResponse.ok) {
          const config = await configResponse.json();
          this.state.menuData = config.navigation || config.menu || [];
          console.log('[NexusMenu] 本地菜单数据:', this.state.menuData);
        } else {
          throw new Error('无法加载菜单配置');
        }
      }
    },
    
    _renderMenu() {
      const container = document.getElementById(this.config.containerId);
      if (!container) {
        throw new Error(`未找到菜单容器: #${this.config.containerId}`);
      }
      
      if (!this.state.menuData || this.state.menuData.length === 0) {
        container.innerHTML = '<li class="nav-item"><span class="nav-text">暂无菜单</span></li>';
        return;
      }
      
      container.innerHTML = this._renderMenuItems(this.state.menuData);
    },
    
    _renderMenuItems(items, level = 0) {
      if (!items || items.length === 0) return '';
      
      return items.map(item => {
        const hasChildren = item.children && item.children.length > 0;
        const itemUrl = item.url || item.path;
        const isActive = this._isCurrentPage(itemUrl);
        const url = this._buildUrl(itemUrl);
        
        let html = `<li class="nav-item ${isActive ? 'active' : ''}" 
                        data-level="${level}" 
                        data-id="${item.id}">`;
        
        if (hasChildren) {
          const isExpanded = isActive || this.state.expandedMenus.includes(item.id);
          html += `
            <a href="javascript:void(0)" 
               class="nav-link has-submenu ${isExpanded ? 'expanded' : ''}" 
               data-toggle="submenu">
              ${item.icon ? `<i class="${item.icon}"></i>` : ''}
              <span class="nav-text">${item.name}</span>
              <i class="ri-arrow-down-s-line submenu-icon"></i>
            </a>
            <ul class="submenu ${isExpanded ? 'show' : ''}">
              ${this._renderMenuItems(item.children, level + 1)}
            </ul>
          `;
        } else {
          html += `
            <a href="${url}" class="nav-link">
              ${item.icon ? `<i class="${item.icon}"></i>` : ''}
              <span class="nav-text">${item.name}</span>
            </a>
          `;
        }
        
        html += '</li>';
        return html;
      }).join('');
    },
    
    _buildUrl(url) {
      if (!url) return 'javascript:void(0)';
      if (url.startsWith('http://') || url.startsWith('https://')) return url;
      if (url.startsWith('/skillcenter/')) return url;
      if (url.startsWith('/')) return url;
      return `/skillcenter/console/${url}`;
    },
    
    _isCurrentPage(url) {
      if (!url) return false;
      const currentPath = window.location.pathname;
      return currentPath.includes(url) || url.includes(currentPath);
    },
    
    _bindEvents() {
      const container = document.getElementById(this.config.containerId);
      if (!container) return;
      
      container.addEventListener('click', (e) => {
        const link = e.target.closest('.nav-link');
        if (!link) return;
        
        const hasSubmenu = link.hasAttribute('data-toggle') && link.getAttribute('data-toggle') === 'submenu';
        console.log('[NexusMenu] 点击事件, hasSubmenu:', hasSubmenu, 'link:', link);
        
        if (hasSubmenu) {
          e.preventDefault();
          e.stopPropagation();
          
          const navItem = link.closest('.nav-item');
          const submenu = navItem.querySelector('.submenu');
          const menuId = navItem.getAttribute('data-id');
          
          console.log('[NexusMenu] 展开/收起菜单, menuId:', menuId, 'submenu:', submenu);
          
          if (submenu) {
            const isExpanded = submenu.classList.contains('show');
            
            if (isExpanded) {
              submenu.classList.remove('show');
              link.classList.remove('expanded');
              this._removeExpandedMenu(menuId);
            } else {
              submenu.classList.add('show');
              link.classList.add('expanded');
              this._addExpandedMenu(menuId);
            }
          }
        }
      });
    },
    
    _addExpandedMenu(menuId) {
      if (!this.state.expandedMenus.includes(menuId)) {
        this.state.expandedMenus.push(menuId);
        this._saveState();
      }
    },
    
    _removeExpandedMenu(menuId) {
      this.state.expandedMenus = this.state.expandedMenus.filter(id => id !== menuId);
      this._saveState();
    },
    
    _saveState() {
      try {
        localStorage.setItem(this.config.storageKey, JSON.stringify({
          expandedMenus: this.state.expandedMenus,
          timestamp: Date.now()
        }));
      } catch (e) {
      }
    },
    
    _restoreState() {
      try {
        const saved = localStorage.getItem(this.config.storageKey);
        if (saved) {
          const data = JSON.parse(saved);
          this.state.expandedMenus = data.expandedMenus || [];
        }
      } catch (e) {
      }
    },
    
    _highlightCurrentPage() {
      const currentPath = window.location.pathname;
      const container = document.getElementById(this.config.containerId);
      if (!container) return;
      
      const links = container.querySelectorAll('.nav-link');
      
      links.forEach((link) => {
        const href = link.getAttribute('href');
        if (href && !href.startsWith('javascript:')) {
          if (currentPath.includes(href) || href.includes(currentPath)) {
            link.classList.add('active');
            
            let parent = link.closest('.submenu');
            let depth = 0;
            while (parent && depth < 10) {
              parent.classList.add('show');
              const parentLink = parent.previousElementSibling;
              if (parentLink) {
                parentLink.classList.add('expanded');
              }
              parent = parent.closest('.submenu');
              depth++;
            }
          }
        }
      });
    },
    
    _renderError(message) {
      const container = document.getElementById(this.config.containerId);
      if (container) {
        container.innerHTML = `
          <li class="nav-item nav-item--error">
            <span class="nav-text"><i class="ri-error-warning-line"></i> ${message}</span>
          </li>
        `;
      }
    },
    
    refresh() {
      this.state.initialized = false;
      return this.init();
    },
    
    expandMenu(menuId) {
      const navItem = document.querySelector(`#${this.config.containerId} .nav-item[data-id="${menuId}"]`);
      if (navItem) {
        const submenu = navItem.querySelector('.submenu');
        const link = navItem.querySelector('.nav-link');
        
        if (submenu && !submenu.classList.contains('show')) {
          submenu.classList.add('show');
          link.classList.add('expanded');
          this._addExpandedMenu(menuId);
        }
      }
    },
    
    collapseMenu(menuId) {
      const navItem = document.querySelector(`#${this.config.containerId} .nav-item[data-id="${menuId}"]`);
      if (navItem) {
        const submenu = navItem.querySelector('.submenu');
        const link = navItem.querySelector('.nav-link');
        
        if (submenu && submenu.classList.contains('show')) {
          submenu.classList.remove('show');
          link.classList.remove('expanded');
          this._removeExpandedMenu(menuId);
        }
      }
    }
  };

  window.NexusMenu = NexusMenu;

  window.initMenu = function() {
    return NexusMenu.init();
  };

  if (document.currentScript && document.currentScript.hasAttribute('data-auto-init')) {
    NexusMenu.init();
  }

})();
