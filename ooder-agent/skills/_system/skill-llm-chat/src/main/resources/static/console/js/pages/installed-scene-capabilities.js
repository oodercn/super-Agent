(function(global) {
    'use strict';

    var allCapabilities = [];
    var filteredCapabilities = [];
    var searchKeyword = '';
    var CATEGORY_CONFIG = null;

    var SKILL_FORM_CONFIG = {
        'SCENE': { icon: 'ri-layout-grid-line', color: 'warning', label: '场景应用' },
        'PROVIDER': { icon: 'ri-service-line', color: 'primary', label: '能力服务' },
        'DRIVER': { icon: 'ri-steering-line', color: 'success', label: '驱动适配' },
        'default': { icon: 'ri-puzzle-line', color: 'secondary', label: '其他' }
    };

    var InstalledSceneCapabilities = {
        init: function() {
            var self = this;
            window.onPageInit = function() {
                console.log('[InstalledSceneCapabilities] 页面初始化完成');
                self.loadCategoryConfig().then(function() {
                    InstalledSceneCapabilities.loadCapabilities();
                });
            };
        },

        loadCategoryConfig: function() {
            if (typeof CategoryService === 'undefined') {
                console.warn('[InstalledSceneCapabilities] CategoryService 未加载，使用默认配置');
                CATEGORY_CONFIG = {
                    'default': { icon: 'ri-folder-line', color: '#8c8c8c', label: '其他' }
                };
                return Promise.resolve();
            }
            
            return CategoryService.loadCategories().then(function(categories) {
                CATEGORY_CONFIG = {};
                categories.forEach(function(cat) {
                    CATEGORY_CONFIG[cat.code] = {
                        icon: cat.icon,
                        color: cat.color,
                        label: cat.name
                    };
                });
                CATEGORY_CONFIG['default'] = { icon: 'ri-folder-line', color: '#8c8c8c', label: '其他' };
            });
        },

        loadCapabilities: function() {
            ApiClient.post('/api/v1/discovery/gitee', { repoUrl: 'https://gitee.com/ooderCN/skills' })
                .then(function(result) {
                    console.log('[InstalledSceneCapabilities] API response:', result);
                    if (result.status === 'success' && result.data) {
                        allCapabilities = result.data.capabilities || [];
                        console.log('[InstalledSceneCapabilities] Total capabilities:', allCapabilities.length);
                        var installed = allCapabilities.filter(function(c) { return c.status === 'installed'; });
                        console.log('[InstalledSceneCapabilities] Installed count:', installed.length);
                        installed.forEach(function(c) { console.log('  -', c.id, c.status); });
                        InstalledSceneCapabilities.updateStats();
                        InstalledSceneCapabilities.renderCapabilities();
                    }
                })
                .catch(function(error) {
                    console.error('[InstalledSceneCapabilities] 加载失败:', error);
                    allCapabilities = [];
                    InstalledSceneCapabilities.updateStats();
                    InstalledSceneCapabilities.renderCapabilities();
                });
        },

        updateStats: function() {
            var installed = allCapabilities.filter(function(c) { return c.status === 'installed'; });
            var installedScenes = installed.filter(function(c) { return c.sceneCapability === true; });
            var installedSkills = installed.filter(function(c) { return c.sceneCapability !== true; });
            var available = allCapabilities.filter(function(c) { return c.status !== 'installed'; });

            document.getElementById('totalCount').textContent = installed.length;
            document.getElementById('sceneCount').textContent = installedScenes.length;
            document.getElementById('skillCount').textContent = installedSkills.length;
            document.getElementById('availableCount').textContent = available.length;
        },

        filterCapabilities: function() {
            filteredCapabilities = allCapabilities.filter(function(cap) {
                var isInstalled = cap.status === 'installed';
                var matchSearch = !searchKeyword || 
                    (cap.name && cap.name.toLowerCase().includes(searchKeyword.toLowerCase())) ||
                    (cap.id && cap.id.toLowerCase().includes(searchKeyword.toLowerCase())) ||
                    (cap.description && cap.description.toLowerCase().includes(searchKeyword.toLowerCase()));
                return isInstalled && matchSearch;
            });
        },

        renderCapabilities: function() {
            InstalledSceneCapabilities.filterCapabilities();
            var container = document.getElementById('capabilitiesList');

            if (filteredCapabilities.length === 0) {
                container.innerHTML = '<div class="empty-state">' +
                    '<i class="ri-checkbox-circle-line"></i>' +
                    '<div class="empty-state-title">暂无已安装的场景能力</div>' +
                    '<div class="empty-state-desc">请前往场景能力列表安装需要的场景能力</div>' +
                    '<button class="nx-btn nx-btn--primary" onclick="goToMarket()"><i class="ri-store-2-line"></i> 去安装</button>' +
                    '</div>';
                return;
            }

            var html = '';
            filteredCapabilities.forEach(function(cap) {
                var skillForm = cap.skillForm || 'PROVIDER';
                var formConfig = SKILL_FORM_CONFIG[skillForm] || SKILL_FORM_CONFIG['default'];
                
                var category = cap.category || cap.capabilityCategory || 'default';
                var catConfig = CATEGORY_CONFIG[category] || CATEGORY_CONFIG['default'];

                html += '<div class="capability-item" data-id="' + cap.id + '">' +
                    '<div class="capability-icon" style="color: ' + catConfig.color + '"><i class="' + catConfig.icon + '"></i></div>' +
                    '<div class="capability-info">' +
                    '<div class="capability-name">' + (cap.name || cap.id) + '</div>' +
                    '<div class="capability-desc">' + (cap.description || '暂无描述') + '</div>' +
                    '<div class="capability-meta">' +
                    '<span><i class="ri-information-line"></i> v' + (cap.version || '1.0.0') + '</span>' +
                    '<span><i class="ri-folder-line"></i> ' + catConfig.label + '</span>' +
                    '<span><i class="' + formConfig.icon + '"></i> ' + formConfig.label + '</span>' +
                    '</div>' +
                    '</div>' +
                    '<div class="capability-actions">' +
                    '<button class="nx-btn nx-btn--primary" onclick="useCapability(\'' + cap.id + '\')"><i class="ri-play-line"></i> 使用</button>' +
                    '<button class="nx-btn nx-btn--secondary" onclick="viewDetail(\'' + cap.id + '\')"><i class="ri-eye-line"></i> 详情</button>' +
                    '</div>' +
                    '</div>';
            });
            container.innerHTML = html;
        }
    };

    InstalledSceneCapabilities.init();

    global.searchCapabilities = function() {
        searchKeyword = document.getElementById('searchInput').value;
        InstalledSceneCapabilities.renderCapabilities();
    };

    global.goToMarket = function() {
        window.location.href = '/console/pages/scene-capabilities.html';
    };

    global.useCapability = function(capabilityId) {
        window.location.href = '/console/pages/scene-capability-detail.html?id=' + capabilityId + '&action=use';
    };

    global.viewDetail = function(capabilityId) {
        window.location.href = '/console/pages/scene-capability-detail.html?id=' + capabilityId;
    };

})(typeof window !== 'undefined' ? window : this);
