(function(global) {
    'use strict';

    var allConfig = {};
    var categories = [];
    var currentCategory = 'server';

    var AppConfig = {
        init: function() {
            window.onPageInit = function() {
                AppConfig.loadCategories();
                AppConfig.loadAllConfig();
            };
        },

        loadCategories: async function() {
            try {
                var response = await fetch('/api/config/categories', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({})
                });
                var rs = await response.json();
                if (rs.requestStatus === 200) {
                    categories = rs.data;
                    AppConfig.renderCategories();
                }
            } catch (error) {
                console.error('Error loading categories:', error);
            }
        },

        loadAllConfig: async function() {
            try {
                var response = await fetch('/api/config/all', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({})
                });
                var rs = await response.json();
                if (rs.requestStatus === 200) {
                    allConfig = rs.data;
                    AppConfig.renderConfig(currentCategory);
                }
            } catch (error) {
                console.error('Error loading config:', error);
                AppConfig.showError('加载配置失败');
            }
        },

        renderCategories: function() {
            var nav = document.getElementById('config-nav');
            nav.innerHTML = categories.map(function(cat) {
                return '\
                    <div class="config-nav-item ' + (cat.id === currentCategory ? 'active' : '') + '" \
                         onclick="selectCategory(\'' + cat.id + '\')">\
                        <i class="' + cat.icon + '"></i>\
                        <span>' + cat.name + '</span>\
                    </div>\
                ';
            }).join('');
        },

        selectCategory: function(categoryId) {
            currentCategory = categoryId;
            AppConfig.renderCategories();
            AppConfig.renderConfig(categoryId);
        },

        renderConfig: function(categoryId) {
            var display = document.getElementById('config-display');
            var config = allConfig[categoryId];

            if (!config) {
                display.innerHTML = '\
                    <div class="empty-state">\
                        <i class="ri-file-unknow-line" style="font-size: 48px; opacity: 0.5;"></i>\
                        <p>暂无配置数据</p>\
                    </div>\
                ';
                return;
            }

            var category = categories.find(function(c) { return c.id === categoryId; });
            var title = category ? category.name : categoryId;

            display.innerHTML = '\
                <div class="config-header">\
                    <div class="config-section-title">\
                        <i class="' + (category ? category.icon : 'ri-settings-line') + '"></i>\
                        ' + title + '\
                    </div>\
                </div>\
                ' + AppConfig.renderConfigSections(config) + '\
            ';
        },

        renderConfigSections: function(config) {
            var html = '';
            var keys = Object.keys(config);

            for (var i = 0; i < keys.length; i++) {
                var key = keys[i];
                var value = config[key];
                
                if (value && typeof value === 'object' && !Array.isArray(value)) {
                    html += '\
                        <div class="config-section">\
                            <div class="config-section-title" style="font-size: 15px;">\
                                ' + AppConfig.formatKey(key) + '\
                            </div>\
                            <div class="config-grid">\
                                ' + AppConfig.renderConfigItems(value) + '\
                            </div>\
                        </div>\
                    ';
                } else {
                    html += '\
                        <div class="config-item">\
                            <div class="config-item-label">' + AppConfig.formatKey(key) + '</div>\
                            <div class="config-item-value ' + AppConfig.getValueClass(value) + '">' + AppConfig.formatValue(value) + '</div>\
                        </div>\
                    ';
                }
            }

            return html || '<div class="empty-state">暂无配置项</div>';
        },

        renderConfigItems: function(obj) {
            var html = '';
            var keys = Object.keys(obj);
            
            for (var i = 0; i < keys.length; i++) {
                var key = keys[i];
                var value = obj[key];
                html += '\
                    <div class="config-item">\
                        <div class="config-item-label">' + AppConfig.formatKey(key) + '</div>\
                        <div class="config-item-value ' + AppConfig.getValueClass(value) + '">' + AppConfig.formatValue(value) + '</div>\
                    </div>\
                ';
            }
            return html;
        },

        formatKey: function(key) {
            return key
                .replace(/([A-Z])/g, ' $1')
                .replace(/^./, function(str) { return str.toUpperCase(); })
                .trim();
        },

        formatValue: function(value) {
            if (value === null || value === undefined || value === '') {
                return '(未设置)';
            }
            if (typeof value === 'boolean') {
                return value ? '是' : '否';
            }
            if (typeof value === 'object') {
                return JSON.stringify(value);
            }
            return String(value);
        },

        getValueClass: function(value) {
            if (value === null || value === undefined || value === '') {
                return 'empty';
            }
            if (typeof value === 'boolean') {
                return value ? 'boolean-true' : 'boolean-false';
            }
            if (typeof value === 'number') {
                return 'number';
            }
            return '';
        },

        refreshConfig: function() {
            AppConfig.loadAllConfig();
        },

        showError: function(message) {
            var display = document.getElementById('config-display');
            display.innerHTML = '\
                <div class="empty-state">\
                    <i class="ri-error-warning-line" style="font-size: 48px; color: var(--ns-danger);"></i>\
                    <p>' + AppConfig.escapeHtml(message) + '</p>\
                    <button class="nx-btn nx-btn--secondary" onclick="refreshConfig()" style="margin-top: 16px;">\
                        <i class="ri-refresh-line"></i> 重试\
                    </button>\
                </div>\
            ';
        },

        escapeHtml: function(text) {
            if (!text) return '';
            var div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        }
    };

    AppConfig.init();

    global.selectCategory = AppConfig.selectCategory;
    global.refreshConfig = AppConfig.refreshConfig;
    global.AppConfig = AppConfig;

})(typeof window !== 'undefined' ? window : this);
