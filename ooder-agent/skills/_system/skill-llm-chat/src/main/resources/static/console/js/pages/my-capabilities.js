(function(global) {
    'use strict';

    var CATEGORY_CONFIG = null;
    var getAddressHex = null;

    var allSkills = [];
    var allCapabilities = [];
    var bindings = [];
    var currentFilter = 'all';
    var currentCategoryFilter = 'all';
    var currentTypeFilter = 'all';
    var searchKeyword = '';
    var currentDrillDown = null;
    var currentPage = 1;
    var pageSize = 20;
    var totalPages = 1;
    var totalCount = 0;

    var SKILL_STATUS = {
        DOWNLOADED: { code: 'DOWNLOADED', name: '已下载', icon: 'ri-download-line', color: '#6366f1' },
        INSTALLED: { code: 'INSTALLED', name: '已安装', icon: 'ri-install-line', color: '#10b981' },
        ACTIVATED: { code: 'ACTIVATED', name: '已激活', icon: 'ri-play-circle-line', color: '#22c55e' },
        DEV: { code: 'DEV', name: '开发中', icon: 'ri-code-line', color: '#f59e0b' },
        REGISTERED: { code: 'REGISTERED', name: '已注册', icon: 'ri-checkbox-circle-line', color: '#3b82f6' },
        ENABLED: { code: 'ENABLED', name: '已启用', icon: 'ri-toggle-line', color: '#22c55e' },
        DISABLED: { code: 'DISABLED', name: '已禁用', icon: 'ri-toggle-line', color: '#ef4444' }
    };

    var CATEGORY_MAPPING = {
        'ORG': 'org',
        'VFS': 'vfs',
        'LLM': 'llm',
        'KNOWLEDGE': 'knowledge',
        'BIZ': 'biz',
        'SYS': 'sys',
        'MSG': 'msg',
        'UI': 'ui',
        'PAYMENT': 'payment',
        'MEDIA': 'media',
        'UTIL': 'util',
        'NEXUS_UI': 'nexus-ui',
        'NEXUS-UI': 'nexus-ui',
        'abs': 'knowledge',
        'tbs': 'knowledge',
        'ass': 'knowledge',
        'business': 'biz',
        'infrastructure': 'sys',
        'scheduler': 'sys',
        'auth': 'org',
        'db': 'vfs',
        'know': 'knowledge',
        'comm': 'msg',
        'mon': 'sys',
        'search': 'sys',
        'sched': 'sys',
        'sec': 'sys',
        'iot': 'sys',
        'net': 'sys',
        'service': 'util',
        'scene': 'biz',
        'tool': 'util',
        'workflow': 'biz',
        'data': 'vfs',
        'other': 'util',
        'form': 'biz',
        'template': 'biz',
        'record': 'biz',
        'dashboard': 'biz',
        'notification': 'msg',
        'recruitment': 'biz',
        'approval': 'biz',
        'hr': 'biz',
        'TOOL': 'util',
        'WORKFLOW': 'biz',
        'DATA': 'vfs',
        'SERVICE': 'util',
        'OTHER': 'util',
        'FORM': 'biz',
        'TEMPLATE': 'biz',
        'RECORD': 'biz',
        'DASHBOARD': 'biz',
        'NOTIFICATION': 'msg',
        'RECRUITMENT': 'biz',
        'APPROVAL': 'biz',
        'HR': 'biz',
        'SCENE': 'biz'
    };

    function normalizeCategory(category) {
        if (!category) return 'util';
        
        var trimmed = category.trim();
        
        if (CATEGORY_MAPPING[trimmed]) {
            return CATEGORY_MAPPING[trimmed];
        }
        
        var lower = trimmed.toLowerCase();
        if (CATEGORY_MAPPING[lower]) {
            return CATEGORY_MAPPING[lower];
        }
        
        var upper = trimmed.toUpperCase();
        if (CATEGORY_MAPPING[upper]) {
            return CATEGORY_MAPPING[upper];
        }
        
        if (CATEGORY_CONFIG && CATEGORY_CONFIG[lower]) {
            return lower;
        }
        
        return 'util';
    }

    var MyCapabilities = {
        init: function() {
            var self = this;
            
            if (typeof CategoryService === 'undefined') {
                console.error('CategoryService 未加载，请确保 category-service.js 已加载');
                return;
            }
            
            console.log('我的能力页面初始化');
            
            CategoryService.loadCategories().then(function(categories) {
                CATEGORY_CONFIG = {};
                categories.forEach(function(cat) {
                    CATEGORY_CONFIG[cat.code] = {
                        name: cat.name,
                        icon: cat.icon,
                        color: cat.color,
                        desc: cat.desc || '',
                        userFacing: cat.userFacing
                    };
                });
                
                if (typeof OoderCapability !== 'undefined') {
                    getAddressHex = OoderCapability.getAddressHex;
                }
                
                self.renderCategoryFilters(categories);
                self.parseUrlParams();
                self.loadCapabilities();
            });
        },

        renderCategoryFilters: function(categories) {
            var container = document.getElementById('categoryFilterChips');
            if (!container) return;
            
            var html = '<span class="filter-chip active" data-category="all" onclick="filterByCategory(\'all\')">全部</span>';
            
            categories.forEach(function(cat) {
                html += '<span class="filter-chip" data-category="' + cat.code + '" onclick="filterByCategory(\'' + cat.code + '\')">' +
                    '<i class="' + cat.icon + '"></i> ' + cat.name + '</span>';
            });
            
            container.innerHTML = html;
        },

        parseUrlParams: function() {
            var urlParams = new URLSearchParams(window.location.search);
            var categoryParam = urlParams.get('category');
            if (categoryParam && CATEGORY_CONFIG && CATEGORY_CONFIG[categoryParam]) {
                currentCategoryFilter = categoryParam;
            }
        },

        loadCapabilities: function() {
            fetch('/api/v1/discovery/local', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({})
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result && result.status === 'success' && result.data && result.data.capabilities) {
                    allCapabilities = result.data.capabilities;
                    allCapabilities = MyCapabilities.filterInternalSkills(allCapabilities);
                    allSkills = MyCapabilities.groupBySkill(allCapabilities);
                    totalCount = allSkills.length;
                    totalPages = Math.ceil(totalCount / pageSize);
                    MyCapabilities.updateStats();
                    MyCapabilities.renderTable();
                } else {
                    console.error('API返回错误:', result);
                    MyCapabilities.showError(result.message || '加载能力列表失败');
                }
            })
            .catch(function(error) {
                console.error('加载能力失败:', error);
                MyCapabilities.showError('加载能力列表失败');
            });
        },

        filterInternalSkills: function(capabilities) {
            return capabilities.filter(function(cap) {
                if (cap.visibility === 'internal') return false;
                if (cap.sceneInternal === true) return false;
                if (cap.ownership === 'SCENE_INTERNAL') return false;
                if (cap.internalVisible === true && cap.publicVisible === false) return false;
                if (cap.developerVisible === true && cap.publicVisible === false) return false;
                if (cap.skillForm === 'INTERNAL') return false;
                return true;
            });
        },

        groupBySkill: function(capabilities) {
            var skillMap = {};
            
            capabilities.forEach(function(cap) {
                var skillId = cap.skillId || cap.id || 'unknown-' + (cap.capabilityId || cap.id);
                
                var skillForm = cap.skillForm || 'STANDALONE';
                
                if (skillForm === 'INTERNAL') {
                    return;
                }
                
                if (!skillMap[skillId]) {
                    var rawCategory = cap.category || cap.capabilityCategory || cap.businessCategory || 'util';
                    var normalizedCategory = normalizeCategory(rawCategory);
                    var capabilityId = cap.capabilityId || cap.id || skillId;
                    
                    skillMap[skillId] = {
                        skillId: skillId,
                        capabilityId: capabilityId,
                        name: cap.name || skillId,
                        description: cap.description || '',
                        category: normalizedCategory,
                        rawCategory: rawCategory,
                        version: cap.version || '-',
                        skillForm: skillForm,
                        status: cap.status || 'REGISTERED',
                        enabled: cap.enabled !== false,
                        installed: cap.installed === true,
                        platform: cap.platform === true,
                        ownership: cap.ownership || 'PLATFORM',
                        createTime: cap.createTime,
                        updateTime: cap.updateTime,
                        capabilities: [],
                        supportedSceneTypes: cap.supportedSceneTypes || [],
                        icon: cap.icon || 'ri-puzzle-line',
                        capabilitiesList: cap.capabilities || []
                    };
                }
                
                skillMap[skillId].capabilities.push({
                    id: cap.id || cap.capabilityId,
                    name: cap.name,
                    description: cap.description
                });
            });
            
            return Object.values(skillMap).sort(function(a, b) {
                if (a.platform !== b.platform) return b.platform ? 1 : -1;
                return (a.name || '').localeCompare(b.name || '');
            });
        },

        updateStats: function() {
            document.getElementById('statTotal').textContent = totalCount;
            
            var activatedCount = 0;
            var installedCount = 0;
            var downloadedCount = 0;
            var devCount = 0;
            var categoryCounts = {};
            
            if (CATEGORY_CONFIG) {
                Object.keys(CATEGORY_CONFIG).forEach(function(cat) {
                    categoryCounts[cat] = 0;
                });
            }
            
            allSkills.forEach(function(skill) {
                var category = skill.category || 'util';
                if (categoryCounts[category] !== undefined) {
                    categoryCounts[category]++;
                } else {
                    categoryCounts['util'] = (categoryCounts['util'] || 0) + 1;
                }
                
                var skillStatus = skill.installStatus || skill.status;
                
                if (skillStatus === 'ACTIVATED' || (skill.installed && skill.enabled)) {
                    activatedCount++;
                } else if (skillStatus === 'INSTALLED' || skill.installed) {
                    installedCount++;
                } else if (skillStatus === 'DOWNLOADED') {
                    downloadedCount++;
                } else if (skillStatus === 'DEV') {
                    devCount++;
                }
            });
            
            var activatedEl = document.getElementById('statActivated');
            if (activatedEl) activatedEl.textContent = activatedCount;
            
            var installedEl = document.getElementById('statInstalled');
            if (installedEl) installedEl.textContent = installedCount;
            
            var downloadedEl = document.getElementById('statDownloaded');
            if (downloadedEl) downloadedEl.textContent = downloadedCount;
            
            document.querySelectorAll('#categoryFilterChips .filter-chip').forEach(function(chip) {
                var cat = chip.getAttribute('data-category');
                if (cat === 'all') {
                    chip.innerHTML = '全部 (' + totalCount + ')';
                } else if (categoryCounts[cat] !== undefined && CATEGORY_CONFIG) {
                    var config = CATEGORY_CONFIG[cat];
                    if (config) {
                        chip.innerHTML = '<i class="' + config.icon + '"></i> ' + config.name + ' (' + categoryCounts[cat] + ')';
                    }
                }
            });
        },

        renderTable: function() {
            var tbody = document.getElementById('capabilityTableBody');
            if (!tbody) {
                console.error('capabilityTableBody 元素未找到');
                return;
            }
            tbody.innerHTML = '';

            var filtered = allSkills;

            if (currentCategoryFilter !== 'all') {
                filtered = filtered.filter(function(skill) {
                    return skill.category === currentCategoryFilter;
                });
            }

            if (currentFilter !== 'all') {
                filtered = filtered.filter(function(skill) {
                    var skillStatus = skill.installStatus || skill.status;
                    if (currentFilter === 'installed') {
                        return skill.installed === true;
                    } else if (currentFilter === 'active') {
                        return skill.installed === true && skill.enabled === true;
                    } else if (currentFilter === 'ACTIVATED') {
                        return skillStatus === 'ACTIVATED' || (skill.installed && skill.enabled);
                    } else if (currentFilter === 'INSTALLED') {
                        return skillStatus === 'INSTALLED' || (skill.installed && !skill.enabled);
                    } else if (currentFilter === 'DOWNLOADED') {
                        return skillStatus === 'DOWNLOADED';
                    } else if (currentFilter === 'DEV') {
                        return skillStatus === 'DEV';
                    } else if (currentFilter === 'ENABLED') {
                        return skill.enabled === true;
                    } else if (currentFilter === 'DISABLED') {
                        return skill.enabled === false || skillStatus === 'DISABLED';
                    }
                    return true;
                });
            }

            if (currentTypeFilter !== 'all') {
                filtered = filtered.filter(function(skill) {
                    if (currentTypeFilter === 'SERVICE') {
                        return skill.skillForm === 'PROVIDER' || skill.skillForm === 'SERVICE';
                    } else if (currentTypeFilter === 'AI') {
                        return skill.skillForm === 'AI' || skill.category === 'llm';
                    } else if (currentTypeFilter === 'DATA') {
                        return skill.skillForm === 'DATA' || skill.category === 'knowledge';
                    } else if (currentTypeFilter === 'CONNECTOR') {
                        return skill.skillForm === 'CONNECTOR';
                    }
                    return true;
                });
            }

            if (searchKeyword) {
                var keyword = searchKeyword.toLowerCase();
                filtered = filtered.filter(function(skill) {
                    var name = (skill.name || '').toLowerCase();
                    var desc = (skill.description || '').toLowerCase();
                    var skillId = (skill.skillId || '').toLowerCase();
                    return name.includes(keyword) || desc.includes(keyword) || skillId.includes(keyword);
                });
            }

            var start = (currentPage - 1) * pageSize;
            var end = Math.min(start + pageSize, filtered.length);

            if (filtered.length === 0) {
                tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 60px; color: #6b7280;">' +
                    '<i class="ri-inbox-line" style="font-size: 48px; display: block; margin-bottom: 16px;"></i>' +
                    '<div style="font-size: 16px;">暂无匹配的技能</div></td></tr>';
                return;
            }

            filtered.slice(start, end).forEach(function(skill) {
                var tr = MyCapabilities.renderSkillRow(skill);
                tbody.appendChild(tr);
            });
        },

        renderSkillRow: function(skill) {
            var tr = document.createElement('tr');
            tr.setAttribute('data-skill-id', skill.skillId);
            tr.style.cursor = 'pointer';
            tr.onclick = function() { 
                drillDownSkill(skill.skillId); 
            };

            var categoryConfig = (CATEGORY_CONFIG && CATEGORY_CONFIG[skill.category]) || 
                { name: skill.category || '工具', color: '#6b7280' };
            
            var nameCell = document.createElement('td');
            var iconClass = skill.icon || 'ri-puzzle-line';
            nameCell.innerHTML = '<div style="display: flex; align-items: center; gap: 10px;">' +
                '<div style="width: 36px; height: 36px; border-radius: 8px; background: linear-gradient(135deg, ' + categoryConfig.color + '20, ' + categoryConfig.color + '40); display: flex; align-items: center; justify-content: center;">' +
                '<i class="' + iconClass + '" style="font-size: 18px; color: ' + categoryConfig.color + ';"></i></div>' +
                '<div><strong style="font-size: 14px;">' + skill.name + '</strong>' +
                '<div style="font-size: 12px; color: #6b7280;">' + (skill.skillId || '-') + '</div></div></div>';
            tr.appendChild(nameCell);

            var ownershipCell = document.createElement('td');
            var ownershipNames = {
                'PLATFORM': '平台技能',
                'INDEPENDENT': '独立技能',
                'SCENE_INTERNAL': '场景内部'
            };
            var ownershipColors = {
                'PLATFORM': '#3b82f6',
                'INDEPENDENT': '#10b981',
                'SCENE_INTERNAL': '#6b7280'
            };
            var ownership = skill.ownership || 'PLATFORM';
            ownershipCell.innerHTML = '<span style="color: ' + ownershipColors[ownership] + '; font-size: 13px;">' + 
                (ownershipNames[ownership] || ownership) + '</span>';
            tr.appendChild(ownershipCell);

            var typeCell = document.createElement('td');
            typeCell.innerHTML = '<span class="category-badge" style="background-color: ' + categoryConfig.color + '; color: white;">' + 
                categoryConfig.name + '</span>';
            tr.appendChild(typeCell);

            var statusCell = document.createElement('td');
            var statusText = '';
            var statusClass = '';
            var statusIcon = '';
            
            var skillStatus = skill.installStatus || skill.status;
            
            if (skillStatus === 'ACTIVATED' || (skill.installed && skill.enabled)) {
                statusText = '已激活';
                statusClass = 'status-active';
                statusIcon = 'ri-play-circle-line';
            } else if (skillStatus === 'INSTALLED' || skill.installed) {
                statusText = '已安装';
                statusClass = 'status-installed';
                statusIcon = 'ri-install-line';
            } else if (skillStatus === 'DOWNLOADED') {
                statusText = '已下载';
                statusClass = 'status-downloaded';
                statusIcon = 'ri-download-line';
            } else if (skillStatus === 'DEV') {
                statusText = '开发中';
                statusClass = 'status-dev';
                statusIcon = 'ri-code-line';
            } else if (skillStatus === 'DISABLED' || !skill.enabled) {
                statusText = '已禁用';
                statusClass = 'status-disabled';
                statusIcon = 'ri-toggle-line';
            } else {
                statusText = '已注册';
                statusClass = 'status-other';
                statusIcon = 'ri-checkbox-circle-line';
            }
            
            statusCell.className = 'status-badge ' + statusClass;
            statusCell.innerHTML = '<i class="' + statusIcon + '"></i> ' + statusText;
            tr.appendChild(statusCell);

            var versionCell = document.createElement('td');
            versionCell.innerHTML = skill.version || '-';
            versionCell.style.fontSize = '13px';
            tr.appendChild(versionCell);

            var capsCell = document.createElement('td');
            var capCount = skill.capabilitiesList ? skill.capabilitiesList.length : (skill.capabilities ? skill.capabilities.length : 0);
            capsCell.innerHTML = capCount > 0 ? capCount + ' 个能力' : '-';
            capsCell.style.fontSize = '13px';
            tr.appendChild(capsCell);

            var updateTimeCell = document.createElement('td');
            updateTimeCell.innerHTML = skill.updateTime ? MyCapabilities.formatTime(skill.updateTime) : '-';
            updateTimeCell.style.fontSize = '13px';
            tr.appendChild(updateTimeCell);

            var actionsCell = document.createElement('td');
            actionsCell.style.textAlign = 'right';
            actionsCell.innerHTML = '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="event.stopPropagation(); drillDownSkill(\'' + skill.skillId + '\')">' +
                '<i class="ri-eye-line"></i> 详情</button>';
            tr.appendChild(actionsCell);

            return tr;
        },

        showError: function(message) {
            var tbody = document.getElementById('capabilityTableBody');
            if (tbody) {
                tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 60px; color: #ef4444;">' +
                    '<i class="ri-error-warning-line" style="font-size: 48px; display: block; margin-bottom: 16px;"></i>' +
                    '<div style="font-size: 16px; font-weight: 500;">' + message + '</div></td></tr>';
            }
        },

        showDetailPanel: function(skill) {
            var detailPanel = document.getElementById('detailPanel');
            var detailTitle = document.getElementById('detailTitle');
            var detailBody = document.getElementById('detailBody');
            var overlay = document.getElementById('overlay');

            if (!detailPanel || !detailBody) return;

            detailTitle.textContent = skill.name || '技能详情';

            var categoryConfig = (CATEGORY_CONFIG && CATEGORY_CONFIG[skill.category]) || 
                { name: skill.category || '工具', color: '#6b7280' };
            
            var statusText = '';
            var statusClass = '';
            if (skill.installed && skill.enabled) {
                statusText = '运行中';
                statusClass = 'status-active';
            } else if (skill.installed) {
                statusText = '已安装';
                statusClass = 'status-installed';
            } else if (!skill.enabled) {
                statusText = '已禁用';
                statusClass = 'status-disabled';
            } else {
                statusText = skill.status === 'REGISTERED' ? '已注册' : (skill.status || '可用');
                statusClass = 'status-other';
            }

            var html = '';
            
            html += '<div class="detail-section">';
            html += '<div class="detail-section-title"><i class="ri-information-line"></i> 基本信息</div>';
            html += '<div class="detail-row"><span class="detail-row-label">技能ID</span><span class="detail-row-value">' + (skill.skillId || '-') + '</span></div>';
            html += '<div class="detail-row"><span class="detail-row-label">描述</span><span class="detail-row-value">' + (skill.description || '-') + '</span></div>';
            html += '<div class="detail-row"><span class="detail-row-label">分类</span><span class="detail-row-value"><span style="background:' + categoryConfig.color + ';color:white;padding:2px 8px;border-radius:4px;font-size:12px;">' + categoryConfig.name + '</span></span></div>';
            html += '<div class="detail-row"><span class="detail-row-label">状态</span><span class="detail-row-value status-badge ' + statusClass + '">' + statusText + '</span></div>';
            html += '<div class="detail-row"><span class="detail-row-label">版本</span><span class="detail-row-value">' + (skill.version || '-') + '</span></div>';
            html += '<div class="detail-row"><span class="detail-row-label">形态</span><span class="detail-row-value">' + (skill.skillForm || '-') + '</span></div>';
            html += '</div>';

            if (skill.capabilitiesList && skill.capabilitiesList.length > 0) {
                html += '<div class="detail-section">';
                html += '<div class="detail-section-title"><i class="ri-puzzle-line"></i> 包含能力 (' + skill.capabilitiesList.length + ')</div>';
                html += '<div style="display: flex; flex-wrap: wrap; gap: 8px;">';
                skill.capabilitiesList.forEach(function(cap) {
                    html += '<span class="feature-tag" style="background: var(--nx-bg-elevated); padding: 4px 12px; border-radius: 6px; font-size: 13px;">' +
                        '<i class="ri-checkbox-circle-line" style="color: var(--nx-success);"></i> ' + 
                        (cap.name || cap.id || cap) + '</span>';
                });
                html += '</div></div>';
            } else if (skill.capabilities && skill.capabilities.length > 0) {
                html += '<div class="detail-section">';
                html += '<div class="detail-section-title"><i class="ri-puzzle-line"></i> 包含能力 (' + skill.capabilities.length + ')</div>';
                html += '<div style="display: flex; flex-wrap: wrap; gap: 8px;">';
                skill.capabilities.forEach(function(cap) {
                    html += '<span class="feature-tag" style="background: var(--nx-bg-elevated); padding: 4px 12px; border-radius: 6px; font-size: 13px;">' +
                        '<i class="ri-checkbox-circle-line" style="color: var(--nx-success);"></i> ' + 
                        (cap.name || cap.id || cap.capabilityId || cap) + '</span>';
                });
                html += '</div></div>';
            }

            if (skill.supportedSceneTypes && skill.supportedSceneTypes.length > 0) {
                html += '<div class="detail-section">';
                html += '<div class="detail-section-title"><i class="ri-artboard-line"></i> 支持场景类型</div>';
                html += '<div class="scene-types-list">';
                skill.supportedSceneTypes.forEach(function(st) {
                    html += '<span class="scene-type-tag">' + st + '</span>';
                });
                html += '</div></div>';
            }

            html += '<div class="detail-section">';
            html += '<div class="detail-section-title"><i class="ri-time-line"></i> 时间信息</div>';
            html += '<div class="detail-row"><span class="detail-row-label">创建时间</span><span class="detail-row-value">' + (skill.createTime ? MyCapabilities.formatTime(skill.createTime) : '-') + '</span></div>';
            html += '<div class="detail-row"><span class="detail-row-label">更新时间</span><span class="detail-row-value">' + (skill.updateTime ? MyCapabilities.formatTime(skill.updateTime) : '-') + '</span></div>';
            html += '</div>';

            html += '<div class="detail-section" style="margin-top: 20px;">';
            html += '<div class="detail-section-title"><i class="ri-settings-3-line"></i> 操作</div>';
            html += '<div style="display: flex; gap: 12px; flex-wrap: wrap;">';
            var isSceneSkill = skill.skillForm === 'SCENE' || skill.skillForm === 'scene-skill' || 
                               skill.capabilityType === 'SCENE' || skill.sceneType != null;
            if (skill.installed && !skill.enabled && isSceneSkill) {
                html += '<button class="nx-btn nx-btn--primary" onclick="MyCapabilities.activateSkill(\'' + skill.capabilityId + '\')">' +
                    '<i class="ri-play-circle-line"></i> 激活</button>';
            } else if (skill.installed && skill.enabled && isSceneSkill) {
                html += '<button class="nx-btn nx-btn--secondary" onclick="MyCapabilities.deactivateSkill(\'' + skill.capabilityId + '\')">' +
                    '<i class="ri-stop-circle-line"></i> 停用</button>';
            }
            html += '<button class="nx-btn nx-btn--secondary" onclick="MyCapabilities.viewDetails(\'' + skill.capabilityId + '\')">' +
                '<i class="ri-external-link-line"></i> 查看详情</button>';
            html += '</div></div>';

            detailBody.innerHTML = html;
            detailPanel.classList.add('open');
            overlay.classList.add('open');
        },

        formatTime: function(timestamp) {
            if (!timestamp) return '-';
            var date = new Date(timestamp);
            return date.toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
            });
        },
        
        activateSkill: function(capabilityId) {
            if (!confirm('确定要激活能力 "' + capabilityId + '" 吗？')) return;
            
            fetch('/api/v1/scene-capabilities/' + capabilityId + '/activate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(data) {
                if (data.status === 'success' || (data.data && data.data.success)) {
                    alert('激活成功');
                    closeDetailPanel();
                    MyCapabilities.loadCapabilities();
                } else {
                    alert('激活失败: ' + (data.message || '未知错误'));
                }
            })
            .catch(function(err) {
                alert('激活失败: ' + err.message);
            });
        },
        
        deactivateSkill: function(capabilityId) {
            if (!confirm('确定要停用能力 "' + capabilityId + '" 吗？')) return;
            
            fetch('/api/v1/scene-capabilities/' + capabilityId + '/deactivate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(data) {
                if (data.status === 'success' || (data.data && data.data.success)) {
                    alert('停用成功');
                    closeDetailPanel();
                    MyCapabilities.loadCapabilities();
                } else {
                    alert('停用失败: ' + (data.message || '未知错误'));
                }
            })
            .catch(function(err) {
                alert('停用失败: ' + err.message);
            });
        },
        
        viewDetails: function(capabilityId) {
            window.open('/console/pages/skill-detail.html?id=' + capabilityId, '_blank');
        }
    };

    global.filterByCategory = function(category) {
        currentCategoryFilter = category;
        currentFilter = 'all';
        currentPage = 1;
        
        document.querySelectorAll('#categoryFilterChipsRow1 .filter-chip, #categoryFilterChipsRow2 .filter-chip').forEach(function(chip) {
            chip.classList.remove('active');
        });
        var activeChip = document.querySelector('#categoryFilterChipsRow1 .filter-chip[data-category="' + category + '"], #categoryFilterChipsRow2 .filter-chip[data-category="' + category + '"]');
        if (activeChip) activeChip.classList.add('active');
        
        MyCapabilities.renderTable();
    };

    global.filterByStatus = function(status) {
        currentFilter = status;
        currentPage = 1;
        document.querySelectorAll('#statusFilterChips .filter-chip').forEach(function(chip) {
            chip.classList.remove('active');
        });
        var activeChip = document.querySelector('#statusFilterChips .filter-chip[data-status="' + status + '"]');
        if (activeChip) activeChip.classList.add('active');
        MyCapabilities.renderTable();
    };

    global.filterByType = function(type) {
        currentTypeFilter = type;
        currentPage = 1;
        document.querySelectorAll('#typeFilterChips .filter-chip').forEach(function(chip) {
            chip.classList.remove('active');
        });
        var activeChip = document.querySelector('#typeFilterChips .filter-chip[data-type="' + type + '"]');
        if (activeChip) activeChip.classList.add('active');
        MyCapabilities.renderTable();
    };

    global.filterCapabilities = function() {
        searchKeyword = document.getElementById('searchInput')?.value || '';
        currentPage = 1;
        MyCapabilities.renderTable();
    };

    global.searchCapabilities = function(keyword) {
        searchKeyword = keyword;
        currentPage = 1;
        MyCapabilities.renderTable();
    };

    global.drillDownSkill = function(skillId) {
        currentDrillDown = skillId;
        var skill = allSkills.find(function(s) { 
            return s.skillId === skillId;
        });
        
        if (skill) {
            MyCapabilities.showDetailPanel(skill);
        }
    };

    global.refreshCapabilities = function() {
        MyCapabilities.loadCapabilities();
    };

    global.goToPage = function(page) {
        if (page < 1 || page > totalPages || page === currentPage) return;
        currentPage = page;
        MyCapabilities.renderTable();
    };

    global.changePageSize = function(newSize) {
        pageSize = newSize;
        currentPage = 1;
        MyCapabilities.renderTable();
    };

    global.closeDetailPanel = function() {
        document.getElementById('detailPanel').classList.remove('open');
        document.getElementById('overlay').classList.remove('open');
    };
    
    global.MyCapabilities = MyCapabilities;

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            MyCapabilities.init();
        });
    } else {
        MyCapabilities.init();
    }

})(typeof window !== 'undefined' ? window : this);
