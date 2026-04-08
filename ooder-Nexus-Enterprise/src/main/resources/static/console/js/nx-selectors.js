(function(global) {
    'use strict';

    var NxSelectors = {
        treeSelector: null,
        categorySelector: null,
        listSelector: null,

        init: function() {
            this.addStyles();
        },

        addStyles: function() {
            if (document.getElementById('nx-selectors-styles')) return;
            
            var style = document.createElement('style');
            style.id = 'nx-selectors-styles';
            style.textContent = this.getStyles();
            document.head.appendChild(style);
        },

        getStyles: function() {
            return '\
.nx-selector-overlay {\
    position: fixed;\
    top: 0;\
    left: 0;\
    right: 0;\
    bottom: 0;\
    background: rgba(0, 0, 0, 0.5);\
    z-index: 10000;\
    display: none;\
    align-items: center;\
    justify-content: center;\
}\
.nx-selector-overlay.open {\
    display: flex;\
}\
.nx-selector-modal {\
    background: var(--nx-bg-card, #1a1a2e);\
    border-radius: 12px;\
    width: 90%;\
    max-width: 500px;\
    max-height: 80vh;\
    display: flex;\
    flex-direction: column;\
    border: 1px solid var(--nx-border, #2a2a4a);\
    box-shadow: 0 20px 50px rgba(0, 0, 0, 0.3);\
}\
.nx-selector-header {\
    display: flex;\
    justify-content: space-between;\
    align-items: center;\
    padding: 16px 20px;\
    border-bottom: 1px solid var(--nx-border, #2a2a4a);\
}\
.nx-selector-header h3 {\
    margin: 0;\
    font-size: 16px;\
    color: var(--nx-text-primary, #fff);\
}\
.nx-selector-close {\
    background: none;\
    border: none;\
    font-size: 24px;\
    cursor: pointer;\
    color: var(--nx-text-secondary, #888);\
    padding: 0;\
    line-height: 1;\
}\
.nx-selector-close:hover {\
    color: var(--nx-text-primary, #fff);\
}\
.nx-selector-search {\
    padding: 12px 16px;\
    border-bottom: 1px solid var(--nx-border, #2a2a4a);\
}\
.nx-selector-search input {\
    width: 100%;\
    padding: 8px 12px;\
    border-radius: 6px;\
    border: 1px solid var(--nx-border, #2a2a4a);\
    background: var(--nx-bg-primary, #1a1a2e);\
    color: var(--nx-text-primary, #fff);\
    font-size: 14px;\
}\
.nx-selector-search input:focus {\
    outline: none;\
    border-color: var(--nx-primary, #3b82f6);\
}\
.nx-selector-body {\
    flex: 1;\
    overflow-y: auto;\
    padding: 8px 0;\
}\
.nx-selector-footer {\
    display: flex;\
    justify-content: flex-end;\
    gap: 12px;\
    padding: 16px 20px;\
    border-top: 1px solid var(--nx-border, #2a2a4a);\
}\
.nx-tree-node {\
    padding: 8px 16px;\
    cursor: pointer;\
    display: flex;\
    align-items: center;\
    gap: 8px;\
    transition: background 0.15s;\
}\
.nx-tree-node:hover {\
    background: var(--nx-bg-hover, rgba(255,255,255,0.05));\
}\
.nx-tree-node.selected {\
    background: var(--nx-primary-light, rgba(59, 130, 246, 0.1));\
    color: var(--nx-primary, #3b82f6);\
}\
.nx-tree-node.folder {\
    font-weight: 600;\
}\
.nx-tree-node.folder > i {\
    color: var(--nx-warning, #f59e0b);\
}\
.nx-tree-node.item > i {\
    color: var(--nx-primary, #3b82f6);\
}\
.nx-tree-children {\
    display: none;\
    padding-left: 20px;\
}\
.nx-tree-children.expanded {\
    display: block;\
}\
.nx-category-list {\
    display: flex;\
    flex-direction: column;\
}\
.nx-category-item {\
    padding: 12px 16px;\
    cursor: pointer;\
    display: flex;\
    align-items: center;\
    gap: 10px;\
    border-bottom: 1px solid var(--nx-border, #2a2a4a);\
    transition: background 0.15s;\
}\
.nx-category-item:last-child {\
    border-bottom: none;\
}\
.nx-category-item:hover {\
    background: var(--nx-bg-hover, rgba(255,255,255,0.05));\
}\
.nx-category-item.selected {\
    background: var(--nx-primary-light, rgba(59, 130, 246, 0.1));\
    color: var(--nx-primary, #3b82f6);\
}\
.nx-category-item i {\
    font-size: 20px;\
    color: var(--nx-primary, #3b82f6);\
}\
.nx-category-item .nx-category-count {\
    margin-left: auto;\
    font-size: 12px;\
    color: var(--nx-text-secondary, #888);\
    background: var(--nx-bg-elevated, #16213e);\
    padding: 2px 8px;\
    border-radius: 10px;\
}\
.nx-list-item {\
    padding: 12px 16px;\
    cursor: pointer;\
    display: flex;\
    align-items: center;\
    gap: 10px;\
    border-bottom: 1px solid var(--nx-border, #2a2a4a);\
    transition: background 0.15s;\
}\
.nx-list-item:last-child {\
    border-bottom: none;\
}\
.nx-list-item:hover {\
    background: var(--nx-bg-hover, rgba(255,255,255,0.05));\
}\
.nx-list-item.selected {\
    background: var(--nx-primary-light, rgba(59, 130, 246, 0.1));\
}\
.nx-list-item .nx-list-item-main {\
    flex: 1;\
}\
.nx-list-item .nx-list-item-name {\
    font-weight: 500;\
    color: var(--nx-text-primary, #fff);\
}\
.nx-list-item .nx-list-item-desc {\
    font-size: 12px;\
    color: var(--nx-text-secondary, #888);\
    margin-top: 2px;\
}\
.nx-list-item .nx-list-item-badge {\
    font-size: 11px;\
    color: var(--nx-text-secondary, #888);\
    background: var(--nx-bg-elevated, #16213e);\
    padding: 2px 8px;\
    border-radius: 4px;\
}\
.nx-selector-empty {\
    padding: 40px 20px;\
    text-align: center;\
    color: var(--nx-text-secondary, #888);\
}\
.nx-selector-empty i {\
    font-size: 48px;\
    margin-bottom: 12px;\
    opacity: 0.5;\
}\
';
        },

        showTreeSelector: function(options) {
            var self = this;
            options = options || {};
            
            var title = options.title || '选择项目';
            var dataUrl = options.dataUrl || '/api/v1/org/tree';
            var data = options.data || null;
            var onSelect = options.onSelect || function() {};
            var multiple = options.multiple || false;
            var selectedIds = options.selectedIds || [];

            if (this.treeSelector) {
                document.body.removeChild(this.treeSelector);
            }

            var overlay = document.createElement('div');
            overlay.className = 'nx-selector-overlay';
            overlay.innerHTML = '\
                <div class="nx-selector-modal">\
                    <div class="nx-selector-header">\
                        <h3>' + title + '</h3>\
                        <button class="nx-selector-close">&times;</button>\
                    </div>\
                    <div class="nx-selector-search">\
                        <input type="text" placeholder="搜索..." id="nx-tree-search">\
                    </div>\
                    <div class="nx-selector-body" id="nx-tree-body">\
                        <div class="nx-selector-empty">\
                            <i class="ri-loader-line ri-spin"></i>\
                            <p>加载中...</p>\
                        </div>\
                    </div>\
                    <div class="nx-selector-footer">\
                        <button class="nx-btn nx-btn--secondary" id="nx-tree-cancel">取消</button>\
                        <button class="nx-btn nx-btn--primary" id="nx-tree-confirm">确定</button>\
                    </div>\
                </div>';

            document.body.appendChild(overlay);
            this.treeSelector = overlay;

            var selectedNodes = [];

            overlay.querySelector('.nx-selector-close').addEventListener('click', function() {
                overlay.classList.remove('open');
            });

            overlay.querySelector('#nx-tree-cancel').addEventListener('click', function() {
                overlay.classList.remove('open');
            });

            overlay.querySelector('#nx-tree-confirm').addEventListener('click', function() {
                overlay.classList.remove('open');
                onSelect(multiple ? selectedNodes : selectedNodes[0]);
            });

            overlay.addEventListener('click', function(e) {
                if (e.target === overlay) {
                    overlay.classList.remove('open');
                }
            });

            var searchInput = overlay.querySelector('#nx-tree-search');
            searchInput.addEventListener('input', function() {
                self.filterTree(overlay, this.value.toLowerCase());
            });

            var loadData = function() {
                if (data) {
                    self.renderTree(overlay, data, selectedIds, selectedNodes);
                    return;
                }

                fetch(dataUrl)
                    .then(function(res) { return res.json(); })
                    .then(function(result) {
                        if (result.status === 'success' && result.data) {
                            self.renderTree(overlay, result.data, selectedIds, selectedNodes);
                        } else {
                            self.renderTreeError(overlay, '加载数据失败');
                        }
                    })
                    .catch(function(err) {
                        console.error('Load tree data error:', err);
                        self.renderTreeError(overlay, '网络错误');
                    });
            };

            setTimeout(function() {
                overlay.classList.add('open');
                loadData();
            }, 10);
        },

        renderTree: function(overlay, data, selectedIds, selectedNodes) {
            var body = overlay.querySelector('#nx-tree-body');
            var self = this;

            if (!data || data.length === 0) {
                body.innerHTML = '<div class="nx-selector-empty"><i class="ri-folder-open-line"></i><p>暂无数据</p></div>';
                return;
            }

            var html = '';
            data.forEach(function(node) {
                html += self.renderTreeNode(node, selectedIds, selectedNodes, 0);
            });
            body.innerHTML = html;

            body.querySelectorAll('.nx-tree-node').forEach(function(nodeEl) {
                nodeEl.addEventListener('click', function(e) {
                    e.stopPropagation();
                    var id = this.getAttribute('data-id');
                    var name = this.getAttribute('data-name');
                    var isFolder = this.classList.contains('folder');

                    if (isFolder) {
                        var children = this.nextElementSibling;
                        if (children && children.classList.contains('nx-tree-children')) {
                            children.classList.toggle('expanded');
                            var icon = this.querySelector('i');
                            if (icon) {
                                icon.className = children.classList.contains('expanded') ? 'ri-folder-open-line' : 'ri-folder-line';
                            }
                        }
                    } else {
                        body.querySelectorAll('.nx-tree-node.item').forEach(function(n) { n.classList.remove('selected'); });
                        this.classList.add('selected');
                        selectedNodes.length = 0;
                        selectedNodes.push({ id: id, name: name });
                    }
                });
            });
        },

        renderTreeNode: function(node, selectedIds, selectedNodes, level) {
            var self = this;
            var isFolder = node.children && node.children.length > 0;
            var isSelected = selectedIds.indexOf(node.id) >= 0;
            var iconClass = isFolder ? 'ri-folder-line' : (node.icon || 'ri-user-line');
            var nodeClass = isFolder ? 'folder' : 'item';

            if (isSelected && !isFolder) {
                selectedNodes.push({ id: node.id, name: node.name });
            }

            var html = '<div class="nx-tree-node ' + nodeClass + (isSelected ? ' selected' : '') + '" data-id="' + node.id + '" data-name="' + node.name + '" style="padding-left: ' + (16 + level * 20) + 'px;">';
            html += '<i class="' + iconClass + '"></i>';
            html += '<span>' + node.name + '</span>';
            html += '</div>';

            if (isFolder) {
                html += '<div class="nx-tree-children">';
                node.children.forEach(function(child) {
                    html += self.renderTreeNode(child, selectedIds, selectedNodes, level + 1);
                });
                html += '</div>';
            }

            return html;
        },

        renderTreeError: function(overlay, message) {
            var body = overlay.querySelector('#nx-tree-body');
            body.innerHTML = '<div class="nx-selector-empty"><i class="ri-error-warning-line"></i><p>' + message + '</p></div>';
        },

        filterTree: function(overlay, keyword) {
            var body = overlay.querySelector('#nx-tree-body');
            var nodes = body.querySelectorAll('.nx-tree-node');

            if (!keyword) {
                nodes.forEach(function(n) { n.style.display = ''; });
                return;
            }

            nodes.forEach(function(node) {
                var name = node.getAttribute('data-name') || '';
                var text = name.toLowerCase();
                if (text.indexOf(keyword) >= 0) {
                    node.style.display = '';
                    var parent = node.parentElement;
                    while (parent && parent.classList.contains('nx-tree-children')) {
                        parent.classList.add('expanded');
                        parent = parent.parentElement;
                    }
                } else {
                    node.style.display = 'none';
                }
            });
        },

        showCategorySelector: function(options) {
            var self = this;
            options = options || {};

            var title = options.title || '选择分类';
            var dataUrl = options.dataUrl || '/api/v1/capabilities/types';
            var data = options.data || null;
            var onSelect = options.onSelect || function() {};
            var selectedId = options.selectedId || null;

            if (this.categorySelector) {
                document.body.removeChild(this.categorySelector);
            }

            var overlay = document.createElement('div');
            overlay.className = 'nx-selector-overlay';
            overlay.innerHTML = '\
                <div class="nx-selector-modal">\
                    <div class="nx-selector-header">\
                        <h3>' + title + '</h3>\
                        <button class="nx-selector-close">&times;</button>\
                    </div>\
                    <div class="nx-selector-search">\
                        <input type="text" placeholder="搜索分类..." id="nx-category-search">\
                    </div>\
                    <div class="nx-selector-body" id="nx-category-body">\
                        <div class="nx-selector-empty">\
                            <i class="ri-loader-line ri-spin"></i>\
                            <p>加载中...</p>\
                        </div>\
                    </div>\
                    <div class="nx-selector-footer">\
                        <button class="nx-btn nx-btn--secondary" id="nx-category-cancel">取消</button>\
                        <button class="nx-btn nx-btn--primary" id="nx-category-confirm">确定</button>\
                    </div>\
                </div>';

            document.body.appendChild(overlay);
            this.categorySelector = overlay;

            var selectedCategory = null;

            overlay.querySelector('.nx-selector-close').addEventListener('click', function() {
                overlay.classList.remove('open');
            });

            overlay.querySelector('#nx-category-cancel').addEventListener('click', function() {
                overlay.classList.remove('open');
            });

            overlay.querySelector('#nx-category-confirm').addEventListener('click', function() {
                overlay.classList.remove('open');
                if (selectedCategory) {
                    onSelect(selectedCategory);
                }
            });

            overlay.addEventListener('click', function(e) {
                if (e.target === overlay) {
                    overlay.classList.remove('open');
                }
            });

            var searchInput = overlay.querySelector('#nx-category-search');
            searchInput.addEventListener('input', function() {
                self.filterCategory(overlay, this.value.toLowerCase());
            });

            var loadData = function() {
                if (data) {
                    self.renderCategory(overlay, data, selectedId, selectedCategory);
                    return;
                }

                fetch(dataUrl)
                    .then(function(res) { return res.json(); })
                    .then(function(result) {
                        if (result.status === 'success' && result.data) {
                            self.renderCategory(overlay, result.data, selectedId, selectedCategory);
                        } else {
                            self.renderCategoryError(overlay, '加载数据失败');
                        }
                    })
                    .catch(function(err) {
                        console.error('Load category data error:', err);
                        self.renderCategoryError(overlay, '网络错误');
                    });
            };

            setTimeout(function() {
                overlay.classList.add('open');
                loadData();
            }, 10);
        },

        renderCategory: function(overlay, data, selectedId, selectedCategory) {
            var body = overlay.querySelector('#nx-category-body');

            if (!data || data.length === 0) {
                body.innerHTML = '<div class="nx-selector-empty"><i class="ri-folder-open-line"></i><p>暂无分类</p></div>';
                return;
            }

            var icons = {
                'DRIVER': 'ri-hard-drive-2-line',
                'SERVICE': 'ri-server-line',
                'MANAGEMENT': 'ri-settings-3-line',
                'AI': 'ri-brain-line',
                'STORAGE': 'ri-database-2-line',
                'COMMUNICATION': 'ri-message-3-line',
                'SECURITY': 'ri-shield-check-line',
                'MONITORING': 'ri-pulse-line',
                'SKILL': 'ri-flashlight-line',
                'SCENE': 'ri-layout-grid-line',
                'CUSTOM': 'ri-tools-line'
            };

            var html = '<div class="nx-category-list">';
            data.forEach(function(cat) {
                var id = cat.id || cat.name;
                var name = cat.name || cat.id;
                var desc = cat.description || '';
                var count = cat.count || 0;
                var icon = cat.icon || icons[id] || 'ri-folder-line';
                var isSelected = id === selectedId;

                if (isSelected) {
                    selectedCategory = { id: id, name: name, description: desc };
                }

                html += '<div class="nx-category-item' + (isSelected ? ' selected' : '') + '" data-id="' + id + '" data-name="' + name + '" data-desc="' + desc + '">';
                html += '<i class="' + icon + '"></i>';
                html += '<div class="nx-list-item-main">';
                html += '<div class="nx-list-item-name">' + name + '</div>';
                if (desc) {
                    html += '<div class="nx-list-item-desc">' + desc + '</div>';
                }
                html += '</div>';
                if (count > 0) {
                    html += '<span class="nx-category-count">' + count + '</span>';
                }
                html += '</div>';
            });
            html += '</div>';
            body.innerHTML = html;

            body.querySelectorAll('.nx-category-item').forEach(function(item) {
                item.addEventListener('click', function() {
                    body.querySelectorAll('.nx-category-item').forEach(function(n) { n.classList.remove('selected'); });
                    this.classList.add('selected');
                    selectedCategory = {
                        id: this.getAttribute('data-id'),
                        name: this.getAttribute('data-name'),
                        description: this.getAttribute('data-desc')
                    };
                });
            });
        },

        renderCategoryError: function(overlay, message) {
            var body = overlay.querySelector('#nx-category-body');
            body.innerHTML = '<div class="nx-selector-empty"><i class="ri-error-warning-line"></i><p>' + message + '</p></div>';
        },

        filterCategory: function(overlay, keyword) {
            var body = overlay.querySelector('#nx-category-body');
            var items = body.querySelectorAll('.nx-category-item');

            items.forEach(function(item) {
                var name = (item.getAttribute('data-name') || '').toLowerCase();
                var desc = (item.getAttribute('data-desc') || '').toLowerCase();
                if (!keyword || name.indexOf(keyword) >= 0 || desc.indexOf(keyword) >= 0) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        },

        showListSelector: function(options) {
            var self = this;
            options = options || {};

            var title = options.title || '选择项目';
            var dataUrl = options.dataUrl || '';
            var data = options.data || null;
            var onSelect = options.onSelect || function() {};
            var selectedId = options.selectedId || null;
            var valueField = options.valueField || 'id';
            var displayField = options.displayField || 'name';
            var descField = options.descField || 'description';

            if (this.listSelector) {
                document.body.removeChild(this.listSelector);
            }

            var overlay = document.createElement('div');
            overlay.className = 'nx-selector-overlay';
            overlay.innerHTML = '\
                <div class="nx-selector-modal">\
                    <div class="nx-selector-header">\
                        <h3>' + title + '</h3>\
                        <button class="nx-selector-close">&times;</button>\
                    </div>\
                    <div class="nx-selector-search">\
                        <input type="text" placeholder="搜索..." id="nx-list-search">\
                    </div>\
                    <div class="nx-selector-body" id="nx-list-body">\
                        <div class="nx-selector-empty">\
                            <i class="ri-loader-line ri-spin"></i>\
                            <p>加载中...</p>\
                        </div>\
                    </div>\
                    <div class="nx-selector-footer">\
                        <button class="nx-btn nx-btn--secondary" id="nx-list-cancel">取消</button>\
                        <button class="nx-btn nx-btn--primary" id="nx-list-confirm">确定</button>\
                    </div>\
                </div>';

            document.body.appendChild(overlay);
            this.listSelector = overlay;

            var selectedItem = null;

            overlay.querySelector('.nx-selector-close').addEventListener('click', function() {
                overlay.classList.remove('open');
            });

            overlay.querySelector('#nx-list-cancel').addEventListener('click', function() {
                overlay.classList.remove('open');
            });

            overlay.querySelector('#nx-list-confirm').addEventListener('click', function() {
                overlay.classList.remove('open');
                if (selectedItem) {
                    onSelect(selectedItem);
                }
            });

            overlay.addEventListener('click', function(e) {
                if (e.target === overlay) {
                    overlay.classList.remove('open');
                }
            });

            var searchInput = overlay.querySelector('#nx-list-search');
            searchInput.addEventListener('input', function() {
                self.filterList(overlay, this.value.toLowerCase());
            });

            var loadData = function() {
                if (data) {
                    self.renderList(overlay, data, selectedId, selectedItem, valueField, displayField, descField);
                    return;
                }

                if (!dataUrl) {
                    self.renderListError(overlay, '未配置数据源');
                    return;
                }

                fetch(dataUrl)
                    .then(function(res) { return res.json(); })
                    .then(function(result) {
                        if (result.status === 'success' && result.data) {
                            self.renderList(overlay, result.data, selectedId, selectedItem, valueField, displayField, descField);
                        } else {
                            self.renderListError(overlay, '加载数据失败');
                        }
                    })
                    .catch(function(err) {
                        console.error('Load list data error:', err);
                        self.renderListError(overlay, '网络错误');
                    });
            };

            setTimeout(function() {
                overlay.classList.add('open');
                loadData();
            }, 10);
        },

        renderList: function(overlay, data, selectedId, selectedItem, valueField, displayField, descField) {
            var body = overlay.querySelector('#nx-list-body');

            if (!data || data.length === 0) {
                body.innerHTML = '<div class="nx-selector-empty"><i class="ri-inbox-line"></i><p>暂无数据</p></div>';
                return;
            }

            var html = '';
            data.forEach(function(item) {
                var id = item[valueField] || item.id;
                var name = item[displayField] || item.name || id;
                var desc = item[descField] || item.description || '';
                var badge = item.badge || item.type || '';
                var isSelected = id === selectedId;

                if (isSelected) {
                    selectedItem = item;
                }

                html += '<div class="nx-list-item' + (isSelected ? ' selected' : '') + '" data-id="' + id + '">';
                html += '<div class="nx-list-item-main">';
                html += '<div class="nx-list-item-name">' + name + '</div>';
                if (desc) {
                    html += '<div class="nx-list-item-desc">' + desc + '</div>';
                }
                html += '</div>';
                if (badge) {
                    html += '<span class="nx-list-item-badge">' + badge + '</span>';
                }
                html += '</div>';
            });
            body.innerHTML = html;

            body.querySelectorAll('.nx-list-item').forEach(function(item) {
                item.addEventListener('click', function() {
                    body.querySelectorAll('.nx-list-item').forEach(function(n) { n.classList.remove('selected'); });
                    this.classList.add('selected');
                    var id = this.getAttribute('data-id');
                    selectedItem = data.find(function(d) { return (d[valueField] || d.id) === id; });
                });
            });
        },

        renderListError: function(overlay, message) {
            var body = overlay.querySelector('#nx-list-body');
            body.innerHTML = '<div class="nx-selector-empty"><i class="ri-error-warning-line"></i><p>' + message + '</p></div>';
        },

        filterList: function(overlay, keyword) {
            var body = overlay.querySelector('#nx-list-body');
            var items = body.querySelectorAll('.nx-list-item');

            items.forEach(function(item) {
                var name = item.querySelector('.nx-list-item-name');
                var desc = item.querySelector('.nx-list-item-desc');
                var nameText = name ? name.textContent.toLowerCase() : '';
                var descText = desc ? desc.textContent.toLowerCase() : '';

                if (!keyword || nameText.indexOf(keyword) >= 0 || descText.indexOf(keyword) >= 0) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        },

        closeAll: function() {
            if (this.treeSelector) this.treeSelector.classList.remove('open');
            if (this.categorySelector) this.categorySelector.classList.remove('open');
            if (this.listSelector) this.listSelector.classList.remove('open');
        }
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            NxSelectors.init();
        });
    } else {
        NxSelectors.init();
    }

    global.NxSelectors = NxSelectors;
})(window);
