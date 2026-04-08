(function() {
    'use strict';
    
    var MenuAuth = {
        allRoles: {},
        selectedRoleId: null,
        menuTree: [],
        menuLibrary: [],
        expandedNodes: new Set(),
        editingMenuId: null,
        draggedNodeId: null,
        
        init: function() {
            var self = this;
            Promise.all([
                self.loadRoles(),
                self.loadLibrary()
            ]);
        },
        
        loadRoles: function() {
            var self = this;
            
            return fetch('/api/v1/scene-menu/config')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.allRoles = result.data.roles || {};
                        self.renderRoleList();
                        self.updateStats();
                        
                        if (result.data.updatedAt) {
                            document.getElementById('stat-update').textContent = self.formatTime(result.data.updatedAt);
                        }
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load roles:', e);
                });
        },
        
        loadLibrary: function() {
            var self = this;
            
            return fetch('/api/v1/scene-menu/library')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        self.menuLibrary = result.data;
                        self.renderLibrary();
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load library:', e);
                });
        },
        
        loadMenuTree: function(roleId) {
            var self = this;
            
            return fetch('/api/v1/scene-menu/roles/' + roleId + '/menu-tree')
                .then(function(response) {
                    return response.json();
                })
                .then(function(result) {
                    if (result.status === 'success') {
                        self.menuTree = result.data || [];
                        self.renderMenuTree();
                        self.updateStats();
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load menu tree:', e);
                    self.menuTree = [];
                    self.renderMenuTree();
                });
        },
        
        renderRoleList: function() {
            var container = document.getElementById('role-list');
            var roleIds = Object.keys(this.allRoles);
            var self = this;
            
            if (roleIds.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-shield-user-line"></i><p>暂无角色</p></div>';
                return;
            }
            
            container.innerHTML = roleIds.map(function(id) {
                var role = self.allRoles[id];
                var menuCount = self.countMenus(role.menus || []);
                var isActive = self.selectedRoleId === id;
                
                return '<div class="role-item ' + (isActive ? 'active' : '') + '" onclick="selectRole(\'' + id + '\')">' +
                    '<div class="role-icon">' +
                        '<i class="' + (role.icon || 'ri-user-line') + '"></i>' +
                    '</div>' +
                    '<div class="role-info">' +
                        '<h4>' + (role.name || id) + '</h4>' +
                        '<span>' + menuCount + ' 个菜单</span>' +
                    '</div>' +
                '</div>';
            }).join('');
        },
        
        countMenus: function(menus) {
            var count = menus.length;
            var self = this;
            menus.forEach(function(m) {
                if (m.children && m.children.length > 0) {
                    count += self.countMenus(m.children);
                }
            });
            return count;
        },
        
        selectRole: function(roleId) {
            this.selectedRoleId = roleId;
            this.renderRoleList();
            this.loadMenuTree(roleId);
        },
        
        renderMenuTree: function() {
            var container = document.getElementById('menu-tree-container');
            
            if (!this.selectedRoleId) {
                container.innerHTML = '<div class="empty-state">' +
                    '<i class="ri-menu-line"></i>' +
                    '<h3>请选择角色</h3>' +
                    '<p>从左侧选择角色查看菜单配置</p>' +
                '</div>';
                return;
            }
            
            if (this.menuTree.length === 0) {
                container.innerHTML = '<div class="empty-state">' +
                    '<i class="ri-menu-line"></i>' +
                    '<h3>暂无菜单</h3>' +
                    '<p>点击"添加顶级菜单"开始配置</p>' +
                '</div>';
                return;
            }
            
            container.innerHTML = '<div class="menu-tree">' + this.renderTreeNodes(this.menuTree, 0) + '</div>';
        },
        
        renderTreeNodes: function(nodes, level) {
            var self = this;
            return nodes.map(function(node) {
                var hasChildren = node.children && node.children.length > 0;
                var isExpanded = self.expandedNodes.has(node.id);
                var nodeId = node.id || 'node-' + Date.now() + '-' + Math.random();
                
                return '<div class="tree-node" data-id="' + nodeId + '" data-level="' + level + '" draggable="true">' +
                    '<div class="tree-node-content" ' +
                         'ondragstart="handleDragStart(event, \'' + nodeId + '\')" ' +
                         'ondragend="handleDragEnd(event)" ' +
                         'ondragover="handleDragOver(event)" ' +
                         'ondragleave="handleDragLeave(event)" ' +
                         'ondrop="handleDrop(event, \'' + nodeId + '\')">' +
                        '<span class="drag-handle" title="拖拽排序">' +
                            '<i class="ri-drag-move-line"></i>' +
                        '</span>' +
                        '<span class="tree-toggle ' + (hasChildren ? (isExpanded ? 'expanded' : '') : 'hidden') + '" ' +
                              'onclick="toggleNode(\'' + nodeId + '\', event)">' +
                            '<i class="ri-arrow-right-s-line"></i>' +
                        '</span>' +
                        '<span class="tree-icon">' +
                            '<i class="' + (node.icon || 'ri-menu-line') + '"></i>' +
                        '</span>' +
                        '<div class="tree-label">' +
                            '<h5>' + node.name + '</h5>' +
                            '<span>' + (node.url || '-') + '</span>' +
                        '</div>' +
                        '<span class="status-badge ' + (node.active ? 'active' : 'inactive') + '">' +
                            (node.active ? '激活' : '未激活') +
                        '</span>' +
                        '<div class="tree-actions">' +
                            '<button class="tree-action-btn" onclick="addChildMenu(\'' + nodeId + '\', \'' + node.name + '\')" title="添加子菜单">' +
                                '<i class="ri-add-line"></i>' +
                            '</button>' +
                            '<button class="tree-action-btn" onclick="editMenu(\'' + nodeId + '\')" title="编辑">' +
                                '<i class="ri-edit-line"></i>' +
                            '</button>' +
                            '<button class="tree-action-btn danger" onclick="deleteMenu(\'' + nodeId + '\')" title="删除">' +
                                '<i class="ri-delete-bin-line"></i>' +
                            '</button>' +
                        '</div>' +
                    '</div>' +
                    (hasChildren ? '<div class="tree-children" style="display: ' + (isExpanded ? 'block' : 'none') + '" id="children-' + nodeId + '">' +
                        self.renderTreeNodes(node.children, level + 1) +
                    '</div>' : '') +
                '</div>';
            }).join('');
        },
        
        toggleNode: function(nodeId, event) {
            event.stopPropagation();
            
            if (this.expandedNodes.has(nodeId)) {
                this.expandedNodes.delete(nodeId);
            } else {
                this.expandedNodes.add(nodeId);
            }
            
            var toggle = event.target.closest('.tree-toggle');
            var children = document.getElementById('children-' + nodeId);
            
            if (toggle) {
                toggle.classList.toggle('expanded');
            }
            if (children) {
                children.style.display = this.expandedNodes.has(nodeId) ? 'block' : 'none';
            }
        },
        
        expandAll: function() {
            var self = this;
            function collectIds(nodes) {
                nodes.forEach(function(node) {
                    if (node.children && node.children.length > 0) {
                        self.expandedNodes.add(node.id);
                        collectIds(node.children);
                    }
                });
            }
            
            collectIds(this.menuTree);
            this.renderMenuTree();
        },
        
        collapseAll: function() {
            this.expandedNodes.clear();
            this.renderMenuTree();
        },
        
        addRootMenu: function() {
            if (!this.selectedRoleId) {
                alert('请先选择角色');
                return;
            }
            
            this.editingMenuId = null;
            document.getElementById('modal-title').textContent = '添加顶级菜单';
            document.getElementById('menu-form').reset();
            document.getElementById('menu-id').value = '';
            document.getElementById('menu-parent-id').value = '';
            document.getElementById('menu-level').value = '0';
            document.getElementById('menu-sort').value = (this.menuTree.length + 1);
            document.getElementById('menu-modal').classList.add('active');
        },
        
        addChildMenu: function(parentId, parentName) {
            this.editingMenuId = null;
            document.getElementById('modal-title').textContent = '添加子菜单 - ' + parentName;
            document.getElementById('menu-form').reset();
            document.getElementById('menu-id').value = '';
            document.getElementById('menu-parent-id').value = parentId;
            document.getElementById('menu-level').value = '1';
            document.getElementById('menu-sort').value = '1';
            document.getElementById('menu-modal').classList.add('active');
        },
        
        editMenu: function(menuId) {
            var menu = this.findMenuById(this.menuTree, menuId);
            if (!menu) return;
            
            this.editingMenuId = menuId;
            document.getElementById('modal-title').textContent = '编辑菜单';
            document.getElementById('menu-id').value = menu.id;
            document.getElementById('menu-parent-id').value = menu.parentId || '';
            document.getElementById('menu-level').value = menu.level || 0;
            document.getElementById('menu-name').value = menu.name || '';
            document.getElementById('menu-url').value = menu.url || '';
            document.getElementById('menu-icon').value = menu.icon || '';
            document.getElementById('menu-sort').value = menu.sort || 1;
            document.getElementById('menu-active').checked = menu.active || false;
            document.getElementById('menu-modal').classList.add('active');
        },
        
        findMenuById: function(nodes, id) {
            for (var i = 0; i < nodes.length; i++) {
                var node = nodes[i];
                if (node.id === id) return node;
                if (node.children) {
                    var found = this.findMenuById(node.children, id);
                    if (found) return found;
                }
            }
            return null;
        },
        
        deleteMenu: function(menuId) {
            var self = this;
            if (!confirm('确定要删除此菜单吗？子菜单也会被一起删除。')) return;
            
            fetch('/api/v1/scene-menu/roles/' + this.selectedRoleId + '/menus/' + menuId + '/tree', {
                method: 'DELETE'
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    self.loadMenuTree(self.selectedRoleId);
                } else {
                    alert('删除失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('删除失败: ' + e.message);
            });
        },
        
        saveMenu: function() {
            var self = this;
            var id = document.getElementById('menu-id').value;
            var parentId = document.getElementById('menu-parent-id').value;
            var name = document.getElementById('menu-name').value;
            var url = document.getElementById('menu-url').value;
            var icon = document.getElementById('menu-icon').value;
            var sort = parseInt(document.getElementById('menu-sort').value) || 1;
            var active = document.getElementById('menu-active').checked;
            
            if (!name || !url) {
                alert('请填写菜单名称和URL');
                return;
            }
            
            var menu = {
                id: id || 'menu-' + Date.now(),
                name: name,
                url: url,
                icon: icon || 'ri-menu-line',
                sort: sort,
                active: active
            };
            
            var fetchUrl, method;
            if (self.editingMenuId) {
                fetchUrl = '/api/v1/scene-menu/roles/' + self.selectedRoleId + '/menus/' + self.editingMenuId;
                method = 'PUT';
            } else {
                fetchUrl = '/api/v1/scene-menu/roles/' + self.selectedRoleId + '/menus/' + (parentId || 'null') + '/children';
                method = 'POST';
            }
            
            fetch(fetchUrl, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(menu)
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    self.closeModal();
                    self.loadMenuTree(self.selectedRoleId);
                } else {
                    alert('保存失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('保存失败: ' + e.message);
            });
        },
        
        saveMenus: function() {
            if (!this.selectedRoleId) {
                alert('请先选择角色');
                return;
            }
            alert('菜单已自动保存');
        },
        
        closeModal: function() {
            document.getElementById('menu-modal').classList.remove('active');
            this.editingMenuId = null;
        },
        
        renderLibrary: function() {
            var container = document.getElementById('library-grid');
            var self = this;
            
            if (this.menuLibrary.length === 0) {
                container.innerHTML = '<div class="empty-state"><p>菜单库为空</p></div>';
                return;
            }
            
            container.innerHTML = this.menuLibrary.map(function(item, index) {
                var hasChildren = item.children && item.children.length > 0;
                return '<div class="library-item" onclick="addFromLibrary(' + index + ')" title="点击添加到当前角色">' +
                    '<i class="' + (item.icon || 'ri-menu-line') + '"></i>' +
                    '<div style="flex: 1;">' +
                        '<h5>' + item.name + '</h5>' +
                        '<span>' + item.url + '</span>' +
                        (hasChildren ? '<span style="color: var(--nx-primary); font-size: 10px;">包含 ' + item.children.length + ' 个子菜单</span>' : '') +
                    '</div>' +
                    '<i class="ri-add-circle-line" style="color: var(--nx-primary);"></i>' +
                '</div>';
            }).join('');
        },
        
        addFromLibrary: function(index) {
            if (!this.selectedRoleId) {
                alert('请先选择角色');
                return;
            }
            
            var item = this.menuLibrary[index];
            this.addMenuWithChildren(item, null);
        },
        
        addMenuWithChildren: function(item, parentId) {
            var self = this;
            var menu = {
                id: 'menu-' + Date.now(),
                name: item.name,
                url: item.url,
                icon: item.icon || 'ri-menu-line',
                sort: 1,
                active: false
            };
            
            fetch('/api/v1/scene-menu/roles/' + self.selectedRoleId + '/menus/' + (parentId || 'null') + '/children', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(menu)
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    if (item.children && item.children.length > 0) {
                        var newParentId = menu.id;
                        item.children.forEach(function(child) {
                            self.addMenuWithChildren(child, newParentId);
                        });
                    }
                    self.loadMenuTree(self.selectedRoleId);
                } else {
                    alert('添加失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('添加失败: ' + e.message);
            });
        },
        
        showAddLibraryModal: function() {
            this.editingMenuId = null;
            document.getElementById('modal-title').textContent = '添加到菜单库';
            document.getElementById('menu-form').reset();
            document.getElementById('menu-id').value = '';
            document.getElementById('menu-parent-id').value = '';
            document.getElementById('menu-modal').classList.add('active');
        },
        
        updateStats: function() {
            document.getElementById('stat-roles').textContent = Object.keys(this.allRoles).length;
            
            var totalMenus = 0;
            var maxLevel = 0;
            var self = this;
            
            function countAll(nodes, level) {
                nodes.forEach(function(node) {
                    totalMenus++;
                    maxLevel = Math.max(maxLevel, level);
                    if (node.children) {
                        countAll(node.children, level + 1);
                    }
                });
            }
            
            countAll(this.menuTree, 1);
            
            document.getElementById('stat-menus').textContent = totalMenus;
            document.getElementById('stat-level').textContent = maxLevel;
        },
        
        formatTime: function(timestamp) {
            if (!timestamp) return '-';
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN');
        },
        
        handleDragStart: function(event, nodeId) {
            this.draggedNodeId = nodeId;
            event.dataTransfer.effectAllowed = 'move';
            event.dataTransfer.setData('text/plain', nodeId);
            event.target.closest('.tree-node-content').classList.add('dragging');
        },
        
        handleDragEnd: function(event) {
            document.querySelectorAll('.tree-node-content').forEach(function(el) {
                el.classList.remove('dragging', 'drag-over', 'drag-over-top', 'drag-over-bottom');
            });
            this.draggedNodeId = null;
        },
        
        handleDragOver: function(event) {
            event.preventDefault();
            event.dataTransfer.dropEffect = 'move';
            
            var target = event.target.closest('.tree-node-content');
            if (!target) return;
            
            var rect = target.getBoundingClientRect();
            var y = event.clientY - rect.top;
            var height = rect.height;
            
            target.classList.remove('drag-over', 'drag-over-top', 'drag-over-bottom');
            
            if (y < height * 0.25) {
                target.classList.add('drag-over-top');
            } else if (y > height * 0.75) {
                target.classList.add('drag-over-bottom');
            } else {
                target.classList.add('drag-over');
            }
        },
        
        handleDragLeave: function(event) {
            var target = event.target.closest('.tree-node-content');
            if (target) {
                target.classList.remove('drag-over', 'drag-over-top', 'drag-over-bottom');
            }
        },
        
        handleDrop: function(event, targetNodeId) {
            event.preventDefault();
            
            if (!this.draggedNodeId || this.draggedNodeId === targetNodeId) return;
            
            var target = event.target.closest('.tree-node-content');
            if (!target) return;
            
            var rect = target.getBoundingClientRect();
            var y = event.clientY - rect.top;
            var height = rect.height;
            
            var dropPosition = 'inside';
            if (y < height * 0.25) {
                dropPosition = 'before';
            } else if (y > height * 0.75) {
                dropPosition = 'after';
            }
            
            this.moveMenuNode(this.draggedNodeId, targetNodeId, dropPosition);
        },
        
        moveMenuNode: function(sourceId, targetId, position) {
            var self = this;
            var newParentId = null;
            var newSort = 1;
            
            if (position === 'inside') {
                newParentId = targetId;
                newSort = 1;
            } else {
                var targetMenu = this.findMenuById(this.menuTree, targetId);
                if (targetMenu) {
                    newParentId = targetMenu.parentId;
                    newSort = position === 'before' ? targetMenu.sort : targetMenu.sort + 1;
                }
            }
            
            fetch('/api/v1/scene-menu/roles/' + this.selectedRoleId + '/menus/' + sourceId + '/move?newParentId=' + (newParentId || '') + '&newSort=' + newSort, {
                method: 'PUT'
            })
            .then(function(response) {
                return response.json();
            })
            .then(function(result) {
                if (result.status === 'success') {
                    self.loadMenuTree(self.selectedRoleId);
                } else {
                    alert('移动失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(e) {
                alert('移动失败: ' + e.message);
            });
        }
    };
    
    window.selectRole = function(roleId) { MenuAuth.selectRole(roleId); };
    window.toggleNode = function(nodeId, event) { MenuAuth.toggleNode(nodeId, event); };
    window.expandAll = function() { MenuAuth.expandAll(); };
    window.collapseAll = function() { MenuAuth.collapseAll(); };
    window.addRootMenu = function() { MenuAuth.addRootMenu(); };
    window.addChildMenu = function(parentId, parentName) { MenuAuth.addChildMenu(parentId, parentName); };
    window.editMenu = function(menuId) { MenuAuth.editMenu(menuId); };
    window.deleteMenu = function(menuId) { MenuAuth.deleteMenu(menuId); };
    window.saveMenu = function() { MenuAuth.saveMenu(); };
    window.saveMenus = function() { MenuAuth.saveMenus(); };
    window.closeModal = function() { MenuAuth.closeModal(); };
    window.addFromLibrary = function(index) { MenuAuth.addFromLibrary(index); };
    window.showAddLibraryModal = function() { MenuAuth.showAddLibraryModal(); };
    window.handleDragStart = function(event, nodeId) { MenuAuth.handleDragStart(event, nodeId); };
    window.handleDragEnd = function(event) { MenuAuth.handleDragEnd(event); };
    window.handleDragOver = function(event) { MenuAuth.handleDragOver(event); };
    window.handleDragLeave = function(event) { MenuAuth.handleDragLeave(event); };
    window.handleDrop = function(event, targetNodeId) { MenuAuth.handleDrop(event, targetNodeId); };
    
    document.addEventListener('DOMContentLoaded', MenuAuth.init.bind(MenuAuth));
})();
