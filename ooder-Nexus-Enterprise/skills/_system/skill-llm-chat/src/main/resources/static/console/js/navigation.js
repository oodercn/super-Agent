(function(global) {
    'use strict';
    
    var Navigation = {
        defaultMenuItems: [
            {
                id: 'workbench',
                name: '工作台',
                icon: 'ri-dashboard-line',
                url: '/console/pages/workbench.html'
            },
            {
                id: 'capability-center',
                name: '能力中心',
                icon: 'ri-puzzle-line',
                children: [
                    { id: 'capability-discovery', name: '能力发现', icon: 'ri-search-line', url: '/console/pages/capability-discovery.html' },
                    { id: 'my-capabilities', name: '我的能力', icon: 'ri-user-star-line', url: '/console/pages/my-capabilities.html' },
                    { id: 'dev-capabilities', name: '开发中的能力', icon: 'ri-code-line', url: '/console/pages/dev-capabilities.html' },
                    { id: 'local-capabilities', name: '本地能力', icon: 'ri-folder-line', url: '/console/pages/local-capabilities.html' },
                    { id: 'plugins-management', name: '插件管理', icon: 'ri-plug-line', url: '/console/pages/plugins-management.html' }
                ]
            },
            {
                id: 'knowledge-center',
                name: '知识中心',
                icon: 'ri-book-3-line',
                children: [
                    { id: 'knowledge-overview', name: '知识概览', icon: 'ri-dashboard-line', url: '/console/pages/knowledge-center.html' },
                    { id: 'knowledge-bases', name: '知识库管理', icon: 'ri-database-2-line', url: '/console/pages/knowledge-base.html' }
                ]
            },
            {
                id: 'scene-center',
                name: '场景中心',
                icon: 'ri-folder-line',
                children: [
                    { id: 'scene-list', name: '我的场景', icon: 'ri-artboard-line', url: '/console/pages/scene-list.html' },
                    { id: 'scene-todos', name: '场景待办', icon: 'ri-task-line', url: '/console/pages/todo-center.html?type=scene' }
                ]
            },
            {
                id: 'system-management',
                name: '系统管理',
                icon: 'ri-settings-4-line',
                children: [
                    { id: 'org-management', name: '组织管理', icon: 'ri-team-line', url: '/console/pages/org-management.html' },
                    { id: 'role-management', name: '角色权限', icon: 'ri-shield-user-line', url: '/console/pages/role-admin.html' }
                ]
            }
        ],
        
        init: function() {
            this.renderSidebar();
            this.bindSidebarToggle();
            this.highlightCurrentPage();
        },
        
        renderSidebar: function() {
            var sidebar = document.getElementById('sidebar');
            if (!sidebar) return;
            
            var self = this;
            var html = '<nav class="nx-sidebar__nav"><ul class="nx-sidebar__menu">';
            
            this.defaultMenuItems.forEach(function(item) {
                html += self.renderMenuItem(item);
            });
            
            html += '</ul></nav>';
            sidebar.innerHTML = html;
        },
        
        renderMenuItem: function(item) {
            var currentPath = window.location.pathname;
            var isActive = item.url && currentPath.includes(item.url);
            
            if (item.children && item.children.length > 0) {
                var hasActiveChild = item.children.some(function(child) {
                    return child.url && currentPath.includes(child.url);
                });
                
                var html = '<li class="nx-sidebar__item' + (hasActiveChild ? ' nx-sidebar__item--expanded' : '') + '">';
                html += '<a href="#" class="nx-sidebar__link' + (hasActiveChild ? ' nx-sidebar__link--active' : '') + '" data-menu-id="' + item.id + '">';
                html += '<i class="' + item.icon + '"></i>';
                html += '<span>' + item.name + '</span>';
                html += '<i class="ri-arrow-down-s-line nx-sidebar__arrow"></i>';
                html += '</a>';
                html += '<ul class="nx-sidebar__submenu' + (hasActiveChild ? ' nx-sidebar__submenu--open' : '') + '">';
                
                var self = this;
                item.children.forEach(function(child) {
                    html += self.renderSubMenuItem(child);
                });
                
                html += '</ul></li>';
                return html;
            } else {
                return '<li class="nx-sidebar__item">' +
                    '<a href="' + (item.url || '#') + '" class="nx-sidebar__link' + (isActive ? ' nx-sidebar__link--active' : '') + '">' +
                    '<i class="' + item.icon + '"></i>' +
                    '<span>' + item.name + '</span>' +
                    '</a></li>';
            }
        },
        
        renderSubMenuItem: function(item) {
            var currentPath = window.location.pathname;
            var isActive = item.url && currentPath.includes(item.url);
            
            return '<li class="nx-sidebar__subitem">' +
                '<a href="' + (item.url || '#') + '" class="nx-sidebar__sublink' + (isActive ? ' nx-sidebar__sublink--active' : '') + '">' +
                '<i class="' + item.icon + '"></i>' +
                '<span>' + item.name + '</span>' +
                '</a></li>';
        },
        
        bindSidebarToggle: function() {
            var toggleBtn = document.querySelector('[data-nx-sidebar-toggle]');
            var sidebar = document.getElementById('sidebar');
            var page = document.querySelector('.nx-page');
            
            if (toggleBtn && sidebar) {
                toggleBtn.addEventListener('click', function() {
                    sidebar.classList.toggle('nx-sidebar--collapsed');
                    if (page) {
                        page.classList.toggle('nx-page--sidebar-collapsed');
                    }
                });
            }
            
            var menuLinks = document.querySelectorAll('.nx-sidebar__link[data-menu-id]');
            menuLinks.forEach(function(link) {
                link.addEventListener('click', function(e) {
                    e.preventDefault();
                    var parent = this.parentElement;
                    var submenu = parent.querySelector('.nx-sidebar__submenu');
                    
                    if (submenu) {
                        parent.classList.toggle('nx-sidebar__item--expanded');
                        submenu.classList.toggle('nx-sidebar__submenu--open');
                    }
                });
            });
        },
        
        highlightCurrentPage: function() {
            var currentPath = window.location.pathname;
            var links = document.querySelectorAll('.nx-sidebar__sublink');
            
            links.forEach(function(link) {
                var href = link.getAttribute('href');
                if (href && currentPath.includes(href)) {
                    link.classList.add('nx-sidebar__sublink--active');
                    var parentItem = link.closest('.nx-sidebar__item');
                    if (parentItem) {
                        parentItem.classList.add('nx-sidebar__item--expanded');
                        var submenu = parentItem.querySelector('.nx-sidebar__submenu');
                        if (submenu) {
                            submenu.classList.add('nx-sidebar__submenu--open');
                        }
                    }
                }
            });
        }
    };
    
    global.Navigation = Navigation;
    
    document.addEventListener('DOMContentLoaded', function() {
        Navigation.init();
    });
})(window);
