(function(global) {
    'use strict';

    var cachedCategories = null;
    var cachedUserFacingCategories = null;
    var cacheTimestamp = 0;
    var CACHE_TTL = 5 * 60 * 1000;

    var DEFAULT_CATEGORIES = [
        { code: 'org', name: '组织服务', icon: 'ri-team-line', color: '#8b5cf6', userFacing: false },
        { code: 'vfs', name: '存储服务', icon: 'ri-database-2-line', color: '#f5970b', userFacing: false },
        { code: 'llm', name: 'LLM服务', icon: 'ri-brain-line', color: '#9334ff', userFacing: true },
        { code: 'knowledge', name: '知识服务', icon: 'ri-book-line', color: '#10b981', userFacing: true },
        { code: 'biz', name: '业务应用', icon: 'ri-briefcase-line', color: '#f97316', userFacing: true },
        { code: 'sys', name: '系统管理', icon: 'ri-settings-3-line', color: '#6366f1', userFacing: false },
        { code: 'msg', name: '消息通讯', icon: 'ri-message-3-line', color: '#f97b72', userFacing: false },
        { code: 'ui', name: 'UI生成', icon: 'ri-palette-line', color: '#ec4899', userFacing: false },
        { code: 'payment', name: '支付服务', icon: 'ri-bank-card-line', color: '#8b5cf6', userFacing: false },
        { code: 'media', name: '媒体发布', icon: 'ri-edit-line', color: '#f5970b', userFacing: false },
        { code: 'util', name: '工具服务', icon: 'ri-tools-line', color: '#4f46e5', userFacing: true },
        { code: 'nexus-ui', name: 'Nexus界面', icon: 'ri-layout-line', color: '#6366f1', userFacing: false },
        { code: 'form', name: '表单服务', icon: 'ri-file-list-3-line', color: '#10b981', userFacing: true },
        { code: 'template', name: '模板服务', icon: 'ri-file-copy-line', color: '#8b5cf6', userFacing: true },
        { code: 'workflow', name: '工作流', icon: 'ri-flow-chart', color: '#f97316', userFacing: true },
        { code: 'record', name: '记录服务', icon: 'ri-history-line', color: '#6366f1', userFacing: true },
        { code: 'dashboard', name: '仪表盘', icon: 'ri-dashboard-line', color: '#ec4899', userFacing: true },
        { code: 'notification', name: '通知服务', icon: 'ri-notification-line', color: '#f97b72', userFacing: true },
        { code: 'recruitment', name: '招聘服务', icon: 'ri-user-add-line', color: '#10b981', userFacing: true },
        { code: 'approval', name: '审批服务', icon: 'ri-checkbox-circle-line', color: '#8b5cf6', userFacing: true },
        { code: 'hr', name: '人力资源', icon: 'ri-team-line', color: '#f97316', userFacing: true },
        { code: 'management', name: '管理服务', icon: 'ri-settings-4-line', color: '#6366f1', userFacing: true }
    ];

    var CategoryService = {
        loadCategories: function(forceRefresh) {
            var self = this;
            
            if (!forceRefresh && cachedCategories && (Date.now() - cacheTimestamp) < CACHE_TTL) {
                return Promise.resolve(cachedCategories);
            }

            return fetch('/api/v1/config-meta/categories')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data && result.data.length > 0) {
                        cachedCategories = result.data;
                        cacheTimestamp = Date.now();
                        cachedUserFacingCategories = null;
                        return cachedCategories;
                    }
                    return self.getDefaultCategories();
                })
                .catch(function(error) {
                    console.error('[CategoryService] Failed to load categories:', error);
                    return self.getDefaultCategories();
                });
        },

        loadUserFacingCategories: function(forceRefresh) {
            var self = this;
            
            if (!forceRefresh && cachedUserFacingCategories) {
                return Promise.resolve(cachedUserFacingCategories);
            }

            return fetch('/api/v1/discovery/categories/user-facing')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data && result.data.length > 0) {
                        cachedUserFacingCategories = result.data;
                        return cachedUserFacingCategories;
                    }
                    return self.getDefaultUserFacingCategories();
                })
                .catch(function(error) {
                    console.error('[CategoryService] Failed to load user-facing categories:', error);
                    return self.getDefaultUserFacingCategories();
                });
        },

        getDefaultCategories: function() {
            return JSON.parse(JSON.stringify(DEFAULT_CATEGORIES));
        },

        getDefaultUserFacingCategories: function() {
            return DEFAULT_CATEGORIES.filter(function(cat) {
                return cat.userFacing === true;
            }).map(function(cat) {
                return {
                    id: cat.code,
                    code: cat.code,
                    name: cat.name,
                    icon: cat.icon,
                    color: cat.color
                };
            });
        },

        getAll: function() {
            var self = this;
            if (cachedCategories) {
                return Promise.resolve(cachedCategories);
            }
            return this.loadCategories();
        },

        getUserFacing: function() {
            var self = this;
            if (cachedUserFacingCategories) {
                return Promise.resolve(cachedUserFacingCategories);
            }
            return this.loadUserFacingCategories();
        },

        getByCode: function(code) {
            var found = DEFAULT_CATEGORIES.find(function(cat) {
                return cat.code === code;
            });
            if (found) {
                return found;
            }
            if (cachedCategories) {
                return cachedCategories.find(function(cat) {
                    return cat.code === code;
                }) || null;
            }
            return null;
        },

        getInfo: function(code) {
            var cat = this.getByCode(code);
            if (cat) {
                return {
                    name: cat.name,
                    icon: cat.icon,
                    color: cat.color,
                    code: cat.code
                };
            }
            return {
                name: code || '未分类',
                icon: 'ri-folder-line',
                color: '#8c8c8c',
                code: code
            };
        },

        getColor: function(code) {
            var cat = this.getByCode(code);
            return cat ? cat.color : '#8c8c8c';
        },

        getName: function(code) {
            var cat = this.getByCode(code);
            return cat ? cat.name : (code || '未分类');
        },

        getIcon: function(code) {
            var cat = this.getByCode(code);
            return cat ? cat.icon : 'ri-folder-line';
        },

        isUserFacing: function(code) {
            var cat = this.getByCode(code);
            return cat ? (cat.userFacing === true) : false;
        },

        clearCache: function() {
            cachedCategories = null;
            cachedUserFacingCategories = null;
            cacheTimestamp = 0;
        },

        refresh: function() {
            this.clearCache();
            return this.loadCategories(true);
        }
    };

    global.CategoryService = CategoryService;

})(typeof window !== 'undefined' ? window : this);
