(function(global) {
    'use strict';

    var createdSceneGroups = [];
    var participatedSceneGroups = [];
    var activeSceneGroups = [];
    var currentSceneGroup = null;
    var currentTab = 'overview';
    var currentFilter = 'all';
    var currentStatusFilter = 'all';
    var searchKeyword = '';
    var auditLogs = [];
    var knowledgeBases = [];
    var llmProviders = [];

    var SCENE_GROUP_STATUS_CONFIG = {
        'DRAFT': { name: '草稿', shortName: 'DRAFT', icon: 'ri-file-line', color: '#64748b', bgColor: '#f1f5f9' },
        'CREATING': { name: '创建中', shortName: 'CREATING', icon: 'ri-loader-line', color: '#3b82f6', bgColor: '#dbeafe' },
        'CONFIGURING': { name: '配置中', shortName: 'CONFIG', icon: 'ri-settings-3-line', color: '#3b82f6', bgColor: '#dbeafe' },
        'PENDING': { name: '待激活', shortName: 'PEND', icon: 'ri-time-line', color: '#f59e0b', bgColor: '#fef3c7' },
        'ACTIVE': { name: '运行中', shortName: 'ACTIVE', icon: 'ri-play-circle-line', color: '#10b981', bgColor: '#d1fae5' },
        'SUSPENDED': { name: '已暂停', shortName: 'SUSP', icon: 'ri-pause-circle-line', color: '#f59e0b', bgColor: '#fef3c7' },
        'SCALING': { name: '扩缩容中', shortName: 'SCALE', icon: 'ri-expand-diagonal-line', color: '#8b5cf6', bgColor: '#ede9fe' },
        'MIGRATING': { name: '迁移中', shortName: 'MIGR', icon: 'ri-arrow-left-right-line', color: '#8b5cf6', bgColor: '#ede9fe' },
        'DESTROYING': { name: '销毁中', shortName: 'DEST', icon: 'ri-delete-bin-line', color: '#ef4444', bgColor: '#fee2e2' },
        'DESTROYED': { name: '已销毁', shortName: 'DSTROY', icon: 'ri-delete-bin-2-line', color: '#64748b', bgColor: '#f1f5f9' },
        'ERROR': { name: '错误', shortName: 'ERR', icon: 'ri-error-warning-line', color: '#ef4444', bgColor: '#fee2e2' }
    };

    var PARTICIPANT_TYPE_CONFIG = {
        'USER': { name: '用户', shortName: 'USER', icon: 'ri-user-line', desc: '人类用户参与者', bgColor: '#dbeafe', color: '#2563eb' },
        'AGENT': { name: 'Agent', shortName: 'AGT', icon: 'ri-robot-line', desc: '智能代理参与者', bgColor: '#fef3c7', color: '#d97706' },
        'SUPER_AGENT': { name: '超级Agent', shortName: 'SAGT', icon: 'ri-robot-2-line', desc: '超级智能代理', bgColor: '#ede9fe', color: '#7c3aed' }
    };

    var PARTICIPANT_ROLE_CONFIG = {
        'MANAGER': { name: '管理者', shortName: 'MGR', icon: 'ri-user-star-line', desc: '完整管理权限', bgColor: '#fef3c7', color: '#d97706' },
        'EMPLOYEE': { name: '员工', shortName: 'EMP', icon: 'ri-user-line', desc: '参与场景执行', bgColor: '#dbeafe', color: '#2563eb' },
        'HR': { name: 'HR', shortName: 'HR', icon: 'ri-team-line', desc: '管理人事相关', bgColor: '#d1fae5', color: '#059669' },
        'LLM_ASSISTANT': { name: 'LLM助手', shortName: 'LLM', icon: 'ri-brain-line', desc: 'AI分析助手', bgColor: '#ede9fe', color: '#7c3aed' },
        'COORDINATOR': { name: '协调Agent', shortName: 'CRD', icon: 'ri-git-branch-line', desc: '任务协调Agent', bgColor: '#fce7f3', color: '#db2777' },
        'SUPER_AGENT': { name: '超级Agent', shortName: 'SAGT', icon: 'ri-robot-2-line', desc: '超级智能代理', bgColor: '#fee2e2', color: '#dc2626' }
    };

    var PARTICIPANT_STATUS_CONFIG = {
        'PENDING': { name: '待确认', icon: 'ri-time-line', color: '#f59e0b' },
        'JOINED': { name: '已加入', icon: 'ri-check-line', color: '#10b981' },
        'ACTIVE': { name: '活跃', icon: 'ri-heart-pulse-line', color: '#10b981' },
        'INACTIVE': { name: '非活跃', icon: 'ri-zzz-line', color: '#64748b' },
        'SUSPENDED': { name: '已暂停', icon: 'ri-pause-line', color: '#f59e0b' },
        'REMOVED': { name: '已移除', icon: 'ri-user-unfollow-line', color: '#ef4444' }
    };

    var BINDING_STATUS_CONFIG = {
        'PENDING': { name: '待激活', icon: 'ri-time-line', color: '#f59e0b' },
        'ACTIVE': { name: '已激活', icon: 'ri-check-line', color: '#10b981' },
        'INACTIVE': { name: '未激活', icon: 'ri-pause-line', color: '#64748b' },
        'ERROR': { name: '错误', icon: 'ri-error-warning-line', color: '#ef4444' },
        'DISABLED': { name: '已禁用', icon: 'ri-forbid-line', color: '#64748b' }
    };

    var PROVIDER_TYPE_CONFIG = {
        'INTERNAL': { name: '内部能力', icon: 'ri-cpu-line', desc: '系统内部能力', bgColor: '#f1f5f9', color: '#64748b' },
        'HTTP': { name: 'HTTP服务', icon: 'ri-global-line', desc: '外部HTTP服务', bgColor: '#dbeafe', color: '#2563eb' },
        'LLM': { name: 'LLM服务', icon: 'ri-brain-line', desc: '大语言模型服务', bgColor: '#fef3c7', color: '#d97706' },
        'KNOWLEDGE': { name: '知识库', icon: 'ri-book-2-line', desc: '知识库服务', bgColor: '#d1fae5', color: '#059669' },
        'AGENT': { name: 'Agent服务', icon: 'ri-robot-line', desc: '智能代理服务', bgColor: '#ede9fe', color: '#7c3aed' }
    };

    var CONNECTOR_TYPE_CONFIG = {
        'HTTP': { name: 'HTTP', icon: 'ri-global-line', desc: 'HTTP/HTTPS协议', bgColor: '#dbeafe', color: '#2563eb' },
        'INTERNAL': { name: '内部调用', icon: 'ri-cpu-line', desc: '进程内调用', bgColor: '#f1f5f9', color: '#64748b' },
        'WEBSOCKET': { name: 'WebSocket', icon: 'ri-plug-line', desc: 'WebSocket协议', bgColor: '#fef3c7', color: '#d97706' }
    };

    var FALLBACK_STRATEGY_CONFIG = {
        'RETRY': { name: '重试', icon: 'ri-refresh-line', desc: '自动重试' },
        'FALLBACK_PROVIDER': { name: '备用Provider', icon: 'ri-git-branch-line', desc: '切换到备用Provider' },
        'CACHE': { name: '缓存结果', icon: 'ri-database-2-line', desc: '使用缓存结果' },
        'DEFAULT_VALUE': { name: '默认值', icon: 'ri-checkbox-circle-line', desc: '返回默认值' },
        'SKIP': { name: '跳过', icon: 'ri-skip-forward-line', desc: '跳过此能力' },
        'FAIL_FAST': { name: '快速失败', icon: 'ri-close-circle-line', desc: '立即返回错误' }
    };

    var VIEW_TYPE_CONFIG = {
        'created': { name: '我创建的', icon: 'ri-add-circle-line', color: 'var(--nx-primary)' },
        'participated': { name: '我参与的', icon: 'ri-user-star-line', color: 'var(--nx-info)' },
        'active': { name: '运行中的', icon: 'ri-play-circle-line', color: 'var(--nx-success)' }
    };

    var MyScenes = {
        init: function() {
            window.onPageInit = function() {
                console.log('我的场景页面初始化');
                MyScenes.loadAllSceneGroups();
            };
        },

        loadAllSceneGroups: function() {
            Promise.all([
                MyScenes.loadCreatedSceneGroups(),
                MyScenes.loadParticipatedSceneGroups(),
                MyScenes.loadActiveSceneGroups()
            ]).then(function() {
                MyScenes.updateStats();
                MyScenes.updateFilterChips();
                MyScenes.renderTable();
            });
        },

        loadCreatedSceneGroups: function() {
            return ApiClient.get('/api/v1/scene-groups/my/created?pageNum=1&pageSize=50')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        createdSceneGroups = result.data.list || result.data || [];
                    }
                })
                .catch(function(error) {
                    console.error('加载创建的场景失败:', error);
                    createdSceneGroups = [];
                });
        },

        loadParticipatedSceneGroups: function() {
            return ApiClient.get('/api/v1/scene-groups/my/participated?pageNum=1&pageSize=50')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        participatedSceneGroups = result.data.list || result.data || [];
                    }
                })
                .catch(function(error) {
                    console.error('加载参与的场景失败:', error);
                    participatedSceneGroups = [];
                });
        },

        loadActiveSceneGroups: function() {
            return ApiClient.get('/api/v1/scene-groups?pageNum=1&pageSize=50&status=ACTIVE')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        activeSceneGroups = result.data.list || result.data || [];
                    }
                })
                .catch(function(error) {
                    console.error('加载运行中的场景失败:', error);
                    activeSceneGroups = [];
                });
        },

        updateStats: function() {
            document.getElementById('createdCount').textContent = createdSceneGroups.length;
            document.getElementById('runningCount').textContent = activeSceneGroups.length;
            document.getElementById('participatedCount').textContent = participatedSceneGroups.length;
            
            MyScenes.loadPendingTodoCount();
        },

        loadPendingTodoCount: function() {
            ApiClient.get('/api/v1/my/todos/count')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        var pendingCount = result.data.pending || 0;
                        var total = Object.values(result.data).reduce(function(a, b) { return a + b; }, 0);
                        document.getElementById('pendingCount').textContent = pendingCount;
                        
                        var pendingCard = document.querySelector('.stat-card[data-view="pending"]');
                        if (pendingCard) {
                            if (pendingCount > 0) {
                                pendingCard.classList.add('has-todos');
                                pendingCard.setAttribute('title', '点击查看待办事项');
                            } else {
                                pendingCard.classList.remove('has-todos');
                            }
                        }
                    }
                })
                .catch(function(error) {
                    console.warn('加载待办数量失败:', error);
                    document.getElementById('pendingCount').textContent = '0';
                });
        },

        updateFilterChips: function() {
            var statusChips = document.getElementById('statusFilterChips');
            if (!statusChips) return;

            var html = '<span class="filter-chip' + (currentStatusFilter === 'all' ? ' active' : '') + '" data-status="all" onclick="filterByStatus(\'all\')">全部</span>';
            
            var statuses = ['ACTIVE', 'SUSPENDED', 'PENDING', 'ERROR'];
            statuses.forEach(function(status) {
                var config = SCENE_GROUP_STATUS_CONFIG[status];
                if (config) {
                    html += '<span class="filter-chip' + (currentStatusFilter === status ? ' active' : '') + '" data-status="' + status + '" onclick="filterByStatus(\'' + status + '\')">' + config.name + '</span>';
                }
            });

            statusChips.innerHTML = html;
        },

        renderTable: function() {
            var tbody = document.getElementById('sceneTableBody');
            var allGroups = [];

            if (currentFilter === 'all') {
                allGroups = createdSceneGroups.concat(participatedSceneGroups).concat(activeSceneGroups);
                var seen = {};
                allGroups = allGroups.filter(function(g) {
                    if (seen[g.sceneGroupId]) return false;
                    seen[g.sceneGroupId] = true;
                    return true;
                });
            } else if (currentFilter === 'created') {
                allGroups = createdSceneGroups;
            } else if (currentFilter === 'participated') {
                allGroups = participatedSceneGroups;
            } else if (currentFilter === 'active') {
                allGroups = activeSceneGroups;
            }

            var filtered = allGroups.filter(function(group) {
                var searchMatch = !searchKeyword || 
                    (group.name && group.name.toLowerCase().indexOf(searchKeyword.toLowerCase()) >= 0) ||
                    (group.sceneGroupId && group.sceneGroupId.toLowerCase().indexOf(searchKeyword.toLowerCase()) >= 0);
                var statusMatch = currentStatusFilter === 'all' || group.status === currentStatusFilter;
                return searchMatch && statusMatch;
            });

            if (filtered.length === 0) {
                tbody.innerHTML = '<tr><td colspan="8" style="text-align: center; padding: 60px; color: var(--nx-text-secondary);">' +
                    '<i class="ri-folder-open-line" style="font-size: 48px; display: block; margin-bottom: 16px; opacity: 0.5;"></i>' +
                    '<div style="font-size: 16px; font-weight: 500;">暂无场景组数据</div></td></tr>';
                return;
            }

            var html = '';
            filtered.forEach(function(group) {
                var statusConfig = SCENE_GROUP_STATUS_CONFIG[group.status] || SCENE_GROUP_STATUS_CONFIG['DRAFT'];
                var viewType = MyScenes.getViewType(group);
                var viewConfig = VIEW_TYPE_CONFIG[viewType];
                var bindingCount = group.capabilityBindings ? group.capabilityBindings.length : 0;
                var participantCount = group.participants ? group.participants.length : 0;

                html += '<tr>' +
                    '<td><div class="cap-info">' +
                    '<div class="cap-icon" style="background: ' + statusConfig.bgColor + '; color: ' + statusConfig.color + ';"><i class="' + statusConfig.icon + '"></i></div>' +
                    '<div><div class="cap-name">' + group.name + '</div>' +
                    '<div class="cap-id">' + group.sceneGroupId + '</div></div></div></td>' +
                    '<td><span class="ownership-badge" style="background: ' + viewConfig.color + '20; color: ' + viewConfig.color + ';">' + viewConfig.name + '</span></td>' +
                    '<td>' + MyScenes.renderStatusBadge(group.status) + '</td>' +
                    '<td><span class="type-badge">' + (group.templateName || group.templateId || '-') + '</span></td>' +
                    '<td>' + participantCount + ' 人 / ' + bindingCount + ' 个能力</td>' +
                    '<td>' + MyScenes.formatTime(group.createTime) + '</td>' +
                    '<td>' + MyScenes.formatTime(group.lastUpdateTime) + '</td>' +
                    '<td><div class="action-btns">' +
                    '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="openSceneGroupDetail(\'' + group.sceneGroupId + '\')" title="查看详情">' +
                    '<i class="ri-eye-line"></i></button>' +
                    '</div></td></tr>';
            });
            tbody.innerHTML = html;
        },

        getViewType: function(group) {
            var userId = MyScenes.getCurrentUserId();
            if (group.creatorId === userId) return 'created';
            var isParticipant = group.participants && group.participants.some(function(p) {
                return p.participantId === userId;
            });
            if (isParticipant) return 'participated';
            if (group.status === 'ACTIVE') return 'active';
            return 'created';
        },

        getCurrentUserId: function() {
            var userAvatar = document.getElementById('user-avatar');
            return userAvatar ? userAvatar.textContent : 'U';
        },

        renderStatusBadge: function(status) {
            var config = SCENE_GROUP_STATUS_CONFIG[status] || SCENE_GROUP_STATUS_CONFIG['DRAFT'];
            return '<span class="status-badge" style="background: ' + config.bgColor + '; color: ' + config.color + ';">' +
                '<span class="status-dot" style="background: ' + config.color + ';"></span>' + config.name + '</span>';
        },

        openSceneGroupDetail: function(sceneGroupId) {
            ApiClient.get('/api/v1/scene-groups/' + sceneGroupId)
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        currentSceneGroup = result.data;
                        currentTab = 'overview';
                        MyScenes.renderDetailPanel();
                        MyScenes.loadRelatedData(sceneGroupId);
                    }
                });
        },

        loadRelatedData: function(sceneGroupId) {
            ApiClient.get('/api/v1/scene-groups/' + sceneGroupId + '/event-log?limit=20')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        auditLogs = result.data || [];
                        if (currentTab === 'audit') MyScenes.renderAuditTab();
                    }
                })
                .catch(function(error) {
                    console.error('Failed to load audit logs:', error);
                    auditLogs = [];
                });

            ApiClient.get('/api/v1/knowledge-bases')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        knowledgeBases = result.data.list || result.data || [];
                        if (currentTab === 'knowledge') MyScenes.renderKnowledgeTab();
                    }
                });

            fetch('/api/v1/llm-providers/providers?configuredOnly=true', { method: 'GET', headers: { 'Content-Type': 'application/json' } })
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    var data = result.data || result.Data || [];
                    llmProviders = Array.isArray(data) ? data : [];
                    if (currentTab === 'llm') MyScenes.renderLLMTab();
                })
                .catch(function(error) {
                    console.error('Failed to load LLM providers:', error);
                    llmProviders = [];
                });
        },

        renderDetailPanel: function() {
            var group = currentSceneGroup;
            if (!group) return;

            var statusConfig = SCENE_GROUP_STATUS_CONFIG[group.status] || SCENE_GROUP_STATUS_CONFIG['DRAFT'];
            document.getElementById('detailTitle').innerHTML = 
                '<span style="display: flex; align-items: center; gap: 8px;">' +
                '<i class="' + statusConfig.icon + '" style="color: ' + statusConfig.color + ';"></i>' +
                group.name + '</span>';

            var tabHtml = '<div class="tab-nav">' +
                '<button class="tab-btn' + (currentTab === 'overview' ? ' active' : '') + '" onclick="switchTab(\'overview\')">' +
                '<i class="ri-dashboard-line"></i> 概览</button>' +
                '<button class="tab-btn' + (currentTab === 'participants' ? ' active' : '') + '" onclick="switchTab(\'participants\')">' +
                '<i class="ri-users-line"></i> 参与者</button>' +
                '<button class="tab-btn' + (currentTab === 'bindings' ? ' active' : '') + '" onclick="switchTab(\'bindings\')">' +
                '<i class="ri-puzzle-line"></i> 能力绑定</button>' +
                '<button class="tab-btn' + (currentTab === 'knowledge' ? ' active' : '') + '" onclick="switchTab(\'knowledge\')">' +
                '<i class="ri-book-2-line"></i> 知识库</button>' +
                '<button class="tab-btn' + (currentTab === 'llm' ? ' active' : '') + '" onclick="switchTab(\'llm\')">' +
                '<i class="ri-brain-line"></i> LLM配置</button>' +
                '<button class="tab-btn' + (currentTab === 'audit' ? ' active' : '') + '" onclick="switchTab(\'audit\')">' +
                '<i class="ri-file-list-3-line"></i> 审计日志</button>' +
                '</div><div class="tab-content" id="tabContent"></div>';

            document.getElementById('detailBody').innerHTML = tabHtml;
            document.getElementById('detailPanel').classList.add('open');
            document.getElementById('overlay').classList.add('open');

            setTimeout(function() {
                MyScenes.renderTabContent();
            }, 0);
        },

        switchTab: function(tab) {
            currentTab = tab;
            MyScenes.renderDetailPanel();
        },

        renderTabContent: function() {
            var tabContentEl = document.getElementById('tabContent');
            
            try {
                switch(currentTab) {
                    case 'overview': MyScenes.renderOverviewTab(); break;
                    case 'participants': MyScenes.renderParticipantsTab(); break;
                    case 'bindings': MyScenes.renderBindingsTab(); break;
                    case 'knowledge': MyScenes.renderKnowledgeTab(); break;
                    case 'llm': MyScenes.renderLLMTab(); break;
                    case 'audit': MyScenes.renderAuditTab(); break;
                    default: 
                        MyScenes.renderOverviewTab(); 
                        break;
                }
            } catch (e) {
                console.error('[renderTabContent] Error:', e);
            }
        },

        renderOverviewTab: function() {
            var group = currentSceneGroup;
            if (!group) return;
            
            var statusConfig = SCENE_GROUP_STATUS_CONFIG[group.status] || SCENE_GROUP_STATUS_CONFIG['DRAFT'];
            var participantCount = group.participants ? group.participants.length : 0;
            var bindingCount = group.capabilityBindings ? group.capabilityBindings.length : 0;

            var html = '<div class="overview-stats">' +
                '<div class="stat-item">' +
                '<div class="stat-item-icon" style="background: #dbeafe; color: #2563eb;"><i class="ri-users-line"></i></div>' +
                '<div class="stat-item-content">' +
                '<div class="stat-item-value">' + participantCount + '</div>' +
                '<div class="stat-item-label">参与者</div>' +
                '</div></div>' +
                '<div class="stat-item">' +
                '<div class="stat-item-icon" style="background: #d1fae5; color: #059669;"><i class="ri-puzzle-line"></i></div>' +
                '<div class="stat-item-content">' +
                '<div class="stat-item-value">' + bindingCount + '</div>' +
                '<div class="stat-item-label">能力绑定</div>' +
                '</div></div>' +
                '<div class="stat-item">' +
                '<div class="stat-item-icon" style="background: #fef3c7; color: #d97706;"><i class="ri-book-2-line"></i></div>' +
                '<div class="stat-item-content">' +
                '<div class="stat-item-value">' + (group.knowledgeBases ? group.knowledgeBases.length : 0) + '</div>' +
                '<div class="stat-item-label">知识库</div>' +
                '</div></div>' +
                '</div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-information-line"></i> 基本信息</div>' +
                '<div class="info-grid">' +
                '<div class="info-item"><span class="info-label">场景组ID</span><span class="info-value" style="font-family: monospace;">' + group.sceneGroupId + '</span></div>' +
                '<div class="info-item"><span class="info-label">状态</span>' + MyScenes.renderStatusBadge(group.status) + '</div>' +
                '<div class="info-item"><span class="info-label">模板</span><span class="info-value">' + (group.templateName || group.templateId || '-') + '</span></div>' +
                '<div class="info-item"><span class="info-label">创建者</span><span class="info-value">' + (group.creatorId || '-') + '</span></div>' +
                '<div class="info-item"><span class="info-label">创建时间</span><span class="info-value">' + MyScenes.formatTime(group.createTime) + '</span></div>' +
                '<div class="info-item"><span class="info-label">更新时间</span><span class="info-value">' + MyScenes.formatTime(group.lastUpdateTime) + '</span></div>' +
                '</div></div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-tools-line"></i> 快速操作</div>' +
                '<div class="action-grid">';

            if (group.status === 'ACTIVE') {
                html += '<button class="nx-btn nx-btn--warning" onclick="toggleSceneStatus(\'' + group.sceneGroupId + '\', \'ACTIVE\')">' +
                    '<i class="ri-pause-line"></i> 暂停场景</button>';
            } else if (group.status === 'SUSPENDED') {
                html += '<button class="nx-btn nx-btn--success" onclick="toggleSceneStatus(\'' + group.sceneGroupId + '\', \'SUSPENDED\')">' +
                    '<i class="ri-play-line"></i> 激活场景</button>';
            }

            html += '<button class="nx-btn nx-btn--secondary" onclick="switchTab(\'participants\')">' +
                '<i class="ri-user-add-line"></i> 邀请参与者</button>' +
                '<button class="nx-btn nx-btn--secondary" onclick="switchTab(\'bindings\')">' +
                '<i class="ri-puzzle-line"></i> 绑定能力</button>' +
                '<button class="nx-btn nx-btn--danger" onclick="destroySceneGroup(\'' + group.sceneGroupId + '\')">' +
                '<i class="ri-delete-bin-line"></i> 销毁场景</button>' +
                '</div></div>';

            var tabContentEl = document.getElementById('tabContent');
            if (tabContentEl) {
                tabContentEl.innerHTML = html;
            }
        },

        renderParticipantsTab: function() {
            var group = currentSceneGroup;
            if (!group) return;
            
            var participants = group.participants || [];

            var html = '<div class="tab-header">' +
                '<span>参与者列表 (' + participants.length + ' 人)</span>' +
                '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="inviteParticipant()">' +
                '<i class="ri-user-add-line"></i> 邀请参与者</button>' +
                '</div>';

            if (participants.length === 0) {
                html += '<div class="empty-state"><i class="ri-user-line"></i><div class="empty-state-title">暂无参与者</div></div>';
            } else {
                html += '<div class="list-container">';
                participants.forEach(function(p) {
                    var typeConfig = PARTICIPANT_TYPE_CONFIG[p.participantType] || PARTICIPANT_TYPE_CONFIG['USER'];
                    var roleConfig = PARTICIPANT_ROLE_CONFIG[p.role] || PARTICIPANT_ROLE_CONFIG['EMPLOYEE'];
                    var pStatusConfig = PARTICIPANT_STATUS_CONFIG[p.status] || PARTICIPANT_STATUS_CONFIG['JOINED'];

                    html += '<div class="list-item" onclick="showParticipantDetail(\'' + p.participantId + '\')">' +
                        '<div class="list-item-icon" style="background: ' + typeConfig.bgColor + '; color: ' + typeConfig.color + ';"><i class="' + typeConfig.icon + '"></i></div>' +
                        '<div class="list-item-content">' +
                        '<div class="list-item-title">' + (p.name || p.participantId) + '</div>' +
                        '<div class="list-item-subtitle">' +
                        '<span class="type-badge"><i class="' + typeConfig.icon + '"></i> ' + typeConfig.name + '</span>' +
                        '<span class="type-badge"><i class="' + roleConfig.icon + '"></i> ' + roleConfig.name + '</span>' +
                        '</div></div>' +
                        '<div class="list-item-status">' + MyScenes.renderParticipantStatus(p.status) + '</div>' +
                        '<div class="list-item-action"><i class="ri-arrow-right-s-line"></i></div>' +
                        '</div>';
                });
                html += '</div>';
            }

            var tabContentEl = document.getElementById('tabContent');
            if (tabContentEl) {
                tabContentEl.innerHTML = html;
            }
        },

        renderBindingsTab: function() {
            var group = currentSceneGroup;
            if (!group) return;
            
            var bindings = group.capabilityBindings || [];

            var html = '<div class="tab-header">' +
                '<span>能力绑定列表 (' + bindings.length + ' 个)</span>' +
                '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="bindCapability()">' +
                '<i class="ri-puzzle-line"></i> 绑定能力</button>' +
                '</div>';

            html += '<div class="info-tip" style="margin-bottom: 16px;">' +
                '<i class="ri-information-line"></i>' +
                '<div class="info-tip-content">' +
                '<div class="info-tip-title">能力绑定说明</div>' +
                '<div class="info-tip-desc">能力绑定是场景组与能力之间的连接，包含调用统计、链路信息、LLM优选逻辑等。点击查看详情。</div>' +
                '</div></div>';

            if (bindings.length === 0) {
                html += '<div class="empty-state"><i class="ri-puzzle-line"></i><div class="empty-state-title">暂无能力绑定</div></div>';
            } else {
                html += '<div class="list-container">';
                bindings.forEach(function(b) {
                    var providerConfig = PROVIDER_TYPE_CONFIG[b.providerType] || PROVIDER_TYPE_CONFIG['INTERNAL'];
                    var connectorConfig = CONNECTOR_TYPE_CONFIG[b.connectorType] || CONNECTOR_TYPE_CONFIG['INTERNAL'];
                    var bStatusConfig = BINDING_STATUS_CONFIG[b.status] || BINDING_STATUS_CONFIG['PENDING'];

                    var successRate = b.successCount + b.failureCount > 0 
                        ? Math.round(b.successCount / (b.successCount + b.failureCount) * 100) 
                        : 0;

                    html += '<div class="list-item">' +
                        '<div class="list-item-icon" style="background: ' + providerConfig.bgColor + '; color: ' + providerConfig.color + ';"><i class="' + providerConfig.icon + '"></i></div>' +
                        '<div class="list-item-content" onclick="showBindingDetail(\'' + b.bindingId + '\')">' +
                        '<div class="list-item-title">' + (b.capName || b.capId || b.capabilityId) + '</div>' +
                        '<div class="list-item-subtitle">' +
                        '<span class="type-badge"><i class="' + providerConfig.icon + '"></i> ' + providerConfig.name + '</span>' +
                        '<span class="type-badge"><i class="' + connectorConfig.icon + '"></i> ' + connectorConfig.name + '</span>' +
                        '<span class="type-badge">优先级: ' + (b.priority || 1) + '</span>' +
                        '</div></div>' +
                        '<div class="list-item-stats">' +
                        '<div class="stat-mini"><span class="stat-mini-value">' + (b.successCount || 0) + '</span><span class="stat-mini-label">成功</span></div>' +
                        '<div class="stat-mini"><span class="stat-mini-value" style="color: #ef4444;">' + (b.failureCount || 0) + '</span><span class="stat-mini-label">失败</span></div>' +
                        '<div class="stat-mini"><span class="stat-mini-value">' + successRate + '%</span><span class="stat-mini-label">成功率</span></div>' +
                        '</div>' +
                        '<div class="list-item-status">' + MyScenes.renderBindingStatus(b.status) + '</div>' +
                        '<div class="list-item-actions">' +
                        (b.capId && (b.capId.includes('report') || b.capId.includes('submit')) ? '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); useCapability(\'' + b.capId + '\')">使用</button>' : '') +
                        '<i class="ri-arrow-right-s-line" onclick="showBindingDetail(\'' + b.bindingId + '\')"></i>' +
                        '</div>' +
                        '</div>';
                });
                html += '</div>';
            }

            var tabContentEl = document.getElementById('tabContent');
            if (tabContentEl) {
                tabContentEl.innerHTML = html;
            }
        },

        renderKnowledgeTab: function() {
            var group = currentSceneGroup;
            if (!group) return;
            
            var sceneKnowledgeBases = group.knowledgeBases || [];

            var html = '<div class="tab-header">' +
                '<span>知识库配置</span>' +
                '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="bindKnowledgeBase()">' +
                '<i class="ri-book-2-line"></i> 绑定知识库</button>' +
                '</div>';

            html += '<div class="info-tip" style="margin-bottom: 16px;">' +
                '<i class="ri-book-2-line"></i>' +
                '<div class="info-tip-content">' +
                '<div class="info-tip-title">知识库能力</div>' +
                '<div class="info-tip-desc">知识库是场景组的核心能力之一，支持RAG检索增强生成。可配置分层知识（通用/专业/场景）。</div>' +
                '</div></div>';

            if (sceneKnowledgeBases.length === 0) {
                html += '<div class="empty-state"><i class="ri-book-2-line"></i><div class="empty-state-title">暂未绑定知识库</div>' +
                    '<button class="nx-btn nx-btn--primary" onclick="bindKnowledgeBase()" style="margin-top: 16px;">绑定知识库</button></div>';
            } else {
                html += '<div class="list-container">';
                sceneKnowledgeBases.forEach(function(kb) {
                    html += '<div class="list-item" onclick="showKnowledgeDetail(\'' + kb.kbId + '\')">' +
                        '<div class="list-item-icon" style="background: #fef3c7; color: #d97706;"><i class="ri-book-2-line"></i></div>' +
                        '<div class="list-item-content">' +
                        '<div class="list-item-title">' + kb.name + '</div>' +
                        '<div class="list-item-subtitle">' +
                        '<span class="type-badge">层级: ' + (kb.layer || 'SCENE') + '</span>' +
                        '<span class="type-badge">TopK: ' + (kb.topK || 5) + '</span>' +
                        '</div></div>' +
                        '<div class="list-item-status"><span class="status-badge" style="background: #d1fae5; color: #059669;">已绑定</span></div>' +
                        '<div class="list-item-action"><i class="ri-arrow-right-s-line"></i></div>' +
                        '</div>';
                });
                html += '</div>';
            }

            html += '<div class="detail-section" style="margin-top: 24px;">' +
                '<div class="detail-section-title"><i class="ri-database-2-line"></i> 可用知识库</div>';

            if (knowledgeBases.length === 0) {
                html += '<p style="color: var(--nx-text-secondary);">暂无可用知识库，请先创建知识库</p>';
            } else {
                html += '<div class="list-container">';
                knowledgeBases.forEach(function(kb) {
                    var isBound = sceneKnowledgeBases.some(function(skb) { return skb.kbId === kb.kbId; });
                    html += '<div class="list-item">' +
                        '<div class="list-item-icon" style="background: #dbeafe; color: #2563eb;"><i class="ri-database-2-line"></i></div>' +
                        '<div class="list-item-content">' +
                        '<div class="list-item-title">' + kb.name + '</div>' +
                        '<div class="list-item-subtitle">' + (kb.description || '-') + '</div>' +
                        '</div>' +
                        (isBound ? '<span class="type-badge" style="background: #d1fae5; color: #059669;">已绑定</span>' :
                        '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="bindKnowledgeBaseToScene(\'' + kb.kbId + '\')">绑定</button>') +
                        '</div>';
                });
                html += '</div>';
            }
            html += '</div>';

            document.getElementById('tabContent').innerHTML = html;
        },

        renderLLMTab: function() {
            var group = currentSceneGroup;

            var html = '<div class="tab-header">' +
                '<span>LLM Provider配置</span>' +
                '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="configureLLM()">' +
                '<i class="ri-brain-line"></i> 配置LLM</button>' +
                '</div>';

            html += '<div class="info-tip" style="margin-bottom: 16px;">' +
                '<i class="ri-brain-line"></i>' +
                '<div class="info-tip-content">' +
                '<div class="info-tip-title">LLM能力</div>' +
                '<div class="info-tip-desc">LLM是场景组的核心能力之一，支持多Provider（Baidu、DeepSeek、Ollama等）、模型动态切换、流式输出。</div>' +
                '</div></div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-cpu-line"></i> 当前配置</div>' +
                '<div class="info-grid">' +
                '<div class="info-item"><span class="info-label">当前Provider</span><span class="info-value">' + (group.llmProvider || 'DeepSeek') + '</span></div>' +
                '<div class="info-item"><span class="info-label">当前模型</span><span class="info-value">' + (group.llmModel || 'deepseek-chat') + '</span></div>' +
                '<div class="info-item"><span class="info-label">温度</span><span class="info-value">' + (group.llmTemperature || 0.7) + '</span></div>' +
                '<div class="info-item"><span class="info-label">最大Token</span><span class="info-value">' + (group.llmMaxTokens || 4096) + '</span></div>' +
                '</div></div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-git-branch-line"></i> LLM优选逻辑</div>' +
                '<div class="llm-strategy-info">' +
                '<p style="font-size: 13px; color: var(--nx-text-secondary); margin-bottom: 12px;">当调用LLM能力时，系统会按以下优先级选择Provider：</p>' +
                '<ol style="font-size: 13px; color: var(--nx-text-secondary); padding-left: 20px;">' +
                '<li>场景组级别配置的Provider</li>' +
                '<li>能力绑定指定的Provider</li>' +
                '<li>系统默认Provider（DeepSeek）</li>' +
                '</ol></div></div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-server-line"></i> 可用Provider</div>';

            if (llmProviders.length === 0) {
                html += '<p style="color: var(--nx-text-secondary);">暂无可用Provider，请先在系统配置中配置API Key</p>';
            } else {
                html += '<div class="list-container">';
                llmProviders.forEach(function(provider) {
                    var providerName = provider.name || 'Unknown';
                    var providerId = provider.providerId || providerName;
                    var providerModels = provider.models || [];
                    var modelNames = providerModels.map(function(m) { return m.displayName || m.modelId; });
                    var isActive = providerId === group.llmProvider || providerName === group.llmProvider;
                    html += '<div class="list-item">' +
                        '<div class="list-item-icon" style="background: ' + (isActive ? '#d1fae5' : '#f1f5f9') + '; color: ' + (isActive ? '#059669' : '#64748b') + ';"><i class="ri-brain-line"></i></div>' +
                        '<div class="list-item-content">' +
                        '<div class="list-item-title">' + providerName + '</div>' +
                        '<div class="list-item-subtitle">模型: ' + (modelNames.length > 0 ? modelNames.slice(0, 3).join(', ') + (modelNames.length > 3 ? '...' : '') : '-') + '</div>' +
                        '</div>' +
                        (isActive ? '<span class="status-badge" style="background: #d1fae5; color: #059669;"><span class="status-dot"></span>当前使用</span>' :
                        '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="switchLLMProvider(\'' + providerId + '\')">切换</button>') +
                        '</div>';
                });
                html += '</div>';
            }
            html += '</div>';

            document.getElementById('tabContent').innerHTML = html;
        },

        renderAuditTab: function() {
            var group = currentSceneGroup;

            var html = '<div class="tab-header">' +
                '<span>审计日志</span>' +
                '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="exportAuditLogs()">' +
                '<i class="ri-download-line"></i> 导出</button>' +
                '</div>';

            if (auditLogs.length === 0) {
                html += '<div class="empty-state"><i class="ri-file-list-3-line"></i><div class="empty-state-title">暂无审计日志</div></div>';
            } else {
                html += '<div class="audit-log-list">';
                auditLogs.forEach(function(log) {
                    var resultColor = log.result === 'SUCCESS' ? '#059669' : '#ef4444';
                    var resultBg = log.result === 'SUCCESS' ? '#d1fae5' : '#fee2e2';

                    html += '<div class="audit-log-item">' +
                        '<div class="audit-log-icon" style="background: ' + resultBg + '; color: ' + resultColor + ';"><i class="ri-checkbox-circle-line"></i></div>' +
                        '<div class="audit-log-content">' +
                        '<div class="audit-log-title">' + (log.action || log.eventType) + '</div>' +
                        '<div class="audit-log-detail">' +
                        '<span>操作者: ' + (log.userId || log.agentId || '-') + '</span>' +
                        '<span>资源: ' + (log.resourceType || '-') + '</span>' +
                        '</div></div>' +
                        '<div class="audit-log-time">' + MyScenes.formatTime(log.timestamp) + '</div>' +
                        '</div>';
                });
                html += '</div>';
            }

            document.getElementById('tabContent').innerHTML = html;
        },

        showBindingDetail: function(bindingId) {
            if (!currentSceneGroup || !currentSceneGroup.capabilityBindings) return;
            var b = currentSceneGroup.capabilityBindings.find(function(x) { return x.bindingId === bindingId; });
            if (!b) return;

            var providerConfig = PROVIDER_TYPE_CONFIG[b.providerType] || PROVIDER_TYPE_CONFIG['INTERNAL'];
            var connectorConfig = CONNECTOR_TYPE_CONFIG[b.connectorType] || CONNECTOR_TYPE_CONFIG['INTERNAL'];
            var successRate = b.successCount + b.failureCount > 0 
                ? Math.round(b.successCount / (b.successCount + b.failureCount) * 100) 
                : 0;

            document.getElementById('detailTitle').innerHTML = 
                '<span style="display: flex; align-items: center; gap: 8px;">' +
                '<i class="ri-puzzle-line" style="color: var(--nx-primary);"></i>' +
                (b.capName || b.capId) + ' - 能力绑定详情</span>';

            var html = '<div class="info-tip">' +
                '<i class="ri-puzzle-line"></i>' +
                '<div class="info-tip-content">' +
                '<div class="info-tip-title">能力绑定层级</div>' +
                '<div class="info-tip-desc">能力绑定是场景组与能力之间的连接实例，包含调用统计、链路信息、LLM优选逻辑等。</div>' +
                '</div></div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-information-line"></i> 绑定信息</div>' +
                '<div class="info-grid">' +
                '<div class="info-item"><span class="info-label">绑定ID</span><span class="info-value" style="font-family: monospace;">' + b.bindingId + '</span></div>' +
                '<div class="info-item"><span class="info-label">能力ID</span><span class="info-value" style="font-family: monospace;">' + (b.capabilityId || b.capId) + '</span></div>' +
                '<div class="info-item"><span class="info-label">能力名称</span><span class="info-value">' + (b.capName || '-') + '</span></div>' +
                '<div class="info-item"><span class="info-label">提供者类型</span><span class="type-badge"><i class="' + providerConfig.icon + '"></i> ' + providerConfig.name + '</span></div>' +
                '<div class="info-item"><span class="info-label">连接器类型</span><span class="type-badge"><i class="' + connectorConfig.icon + '"></i> ' + connectorConfig.name + '</span></div>' +
                '<div class="info-item"><span class="info-label">优先级</span><span class="info-value">' + (b.priority || 1) + '</span></div>' +
                '<div class="info-item"><span class="info-label">状态</span>' + MyScenes.renderBindingStatus(b.status) + '</div>' +
                '<div class="info-item"><span class="info-label">Endpoint</span><span class="info-value" style="font-family: monospace; font-size: 11px;">' + (b.endpoint || '-') + '</span></div>' +
                '</div></div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-bar-chart-line"></i> 调用统计</div>' +
                '<div class="stats-row" style="grid-template-columns: repeat(4, 1fr);">' +
                '<div class="stat-mini-card"><div class="stat-mini-value">' + (b.successCount || 0) + '</div><div class="stat-mini-label">成功次数</div></div>' +
                '<div class="stat-mini-card"><div class="stat-mini-value" style="color: #ef4444;">' + (b.failureCount || 0) + '</div><div class="stat-mini-label">失败次数</div></div>' +
                '<div class="stat-mini-card"><div class="stat-mini-value">' + successRate + '%</div><div class="stat-mini-label">成功率</div></div>' +
                '<div class="stat-mini-card"><div class="stat-mini-value">' + MyScenes.formatTime(b.lastInvokeTime) + '</div><div class="stat-mini-label">最后调用</div></div>' +
                '</div></div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-git-branch-line"></i> 调用链路图</div>' +
                '<div class="call-chain-diagram">' +
                '<div class="chain-node start"><i class="ri-play-circle-line"></i><span>场景组</span></div>' +
                '<div class="chain-arrow"><i class="ri-arrow-right-line"></i></div>' +
                '<div class="chain-node"><i class="ri-puzzle-line"></i><span>能力绑定</span></div>' +
                '<div class="chain-arrow"><i class="ri-arrow-right-line"></i></div>' +
                '<div class="chain-node"><i class="' + connectorConfig.icon + '"></i><span>' + connectorConfig.name + '</span></div>' +
                '<div class="chain-arrow"><i class="ri-arrow-right-line"></i></div>' +
                '<div class="chain-node end"><i class="' + providerConfig.icon + '"></i><span>' + providerConfig.name + '</span></div>' +
                '</div>';

            if (b.fallback && b.fallbackBindingId) {
                html += '<div class="fallback-chain" style="margin-top: 12px;">' +
                    '<span style="font-size: 12px; color: var(--nx-text-secondary);">Fallback链路: </span>' +
                    '<span class="type-badge" style="background: #fef3c7; color: #d97706;"><i class="ri-git-branch-line"></i> ' + b.fallbackBindingId + '</span>' +
                    '</div>';
            }
            html += '</div>';

            if (b.providerType === 'LLM') {
                html += '<div class="detail-section">' +
                    '<div class="detail-section-title"><i class="ri-brain-line"></i> LLM优选逻辑</div>' +
                    '<div class="llm-strategy-info">' +
                    '<p style="font-size: 13px; color: var(--nx-text-secondary); margin-bottom: 12px;">此能力为LLM能力，系统会按以下逻辑选择Provider：</p>' +
                    '<ol style="font-size: 13px; color: var(--nx-text-secondary); padding-left: 20px;">' +
                    '<li>能力绑定指定的Provider: <strong>' + (b.providerId || '默认') + '</strong></li>' +
                    '<li>场景组级别配置的Provider</li>' +
                    '<li>系统默认Provider（DeepSeek）</li>' +
                    '</ol>' +
                    '<div style="margin-top: 12px; padding: 12px; background: var(--nx-bg); border-radius: 8px;">' +
                    '<div style="font-size: 12px; color: var(--nx-text-secondary);">当前Provider</div>' +
                    '<div style="font-size: 16px; font-weight: 500;"><i class="ri-brain-line"></i> ' + (b.providerId || 'DeepSeek') + '</div>' +
                    '</div></div></div>';
            }

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-shield-check-line"></i> Fallback配置</div>' +
                '<div class="info-grid">' +
                '<div class="info-item"><span class="info-label">启用Fallback</span><span class="info-value">' + (b.fallback ? '是' : '否') + '</span></div>' +
                '<div class="info-item"><span class="info-label">备用绑定</span><span class="info-value">' + (b.fallbackBindingId || '-') + '</span></div>' +
                '</div></div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-tools-line"></i> 操作</div>' +
                '<div style="display: flex; gap: 8px; flex-wrap: wrap;">' +
                '<button class="nx-btn nx-btn--primary" onclick="invokeCapability(\'' + b.capabilityId + '\')">' +
                '<i class="ri-play-line"></i> 调用能力</button>' +
                '<button class="nx-btn nx-btn--secondary" onclick="viewBindingAuditLogs(\'' + b.bindingId + '\')">' +
                '<i class="ri-file-list-3-line"></i> 查看审计日志</button>' +
                '<button class="nx-btn nx-btn--danger" onclick="unbindCapability(\'' + b.bindingId + '\')">' +
                '<i class="ri-link-unlink"></i> 解绑能力</button>' +
                '</div></div>';

            document.getElementById('detailBody').innerHTML = html;
        },

        showParticipantDetail: function(participantId) {
            if (!currentSceneGroup || !currentSceneGroup.participants) return;
            var p = currentSceneGroup.participants.find(function(x) { return x.participantId === participantId; });
            if (!p) return;

            var typeConfig = PARTICIPANT_TYPE_CONFIG[p.participantType] || PARTICIPANT_TYPE_CONFIG['USER'];
            var roleConfig = PARTICIPANT_ROLE_CONFIG[p.role] || PARTICIPANT_ROLE_CONFIG['EMPLOYEE'];

            document.getElementById('detailTitle').innerHTML = 
                '<span style="display: flex; align-items: center; gap: 8px;">' +
                '<i class="' + typeConfig.icon + '" style="color: var(--nx-primary);"></i>' +
                (p.name || participantId) + ' - 参与者详情</span>';

            var html = '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-user-line"></i> 参与者信息</div>' +
                '<div class="info-grid">' +
                '<div class="info-item"><span class="info-label">参与者ID</span><span class="info-value" style="font-family: monospace;">' + participantId + '</span></div>' +
                '<div class="info-item"><span class="info-label">参与者名称</span><span class="info-value">' + (p.name || '-') + '</span></div>' +
                '<div class="info-item"><span class="info-label">参与者类型</span><span class="type-badge"><i class="' + typeConfig.icon + '"></i> ' + typeConfig.name + '</span></div>' +
                '<div class="info-item"><span class="info-label">角色</span><span class="type-badge" style="background: var(--nx-primary-light); color: var(--nx-primary);"><i class="' + roleConfig.icon + '"></i> ' + roleConfig.name + '</span></div>' +
                '<div class="info-item"><span class="info-label">状态</span>' + MyScenes.renderParticipantStatus(p.status) + '</div>' +
                '<div class="info-item"><span class="info-label">加入时间</span><span class="info-value">' + MyScenes.formatTime(p.joinTime) + '</span></div>' +
                '</div></div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-file-text-line"></i> 类型说明</div>' +
                '<p style="font-size: 13px; color: var(--nx-text-secondary);">' + typeConfig.desc + '</p>' +
                '<p style="font-size: 13px; color: var(--nx-text-secondary);">角色权限: ' + roleConfig.desc + '</p>' +
                '</div>';

            html += '<div class="detail-section">' +
                '<div class="detail-section-title"><i class="ri-tools-line"></i> 操作</div>' +
                '<div style="display: flex; gap: 8px; flex-wrap: wrap;">' +
                '<button class="nx-btn nx-btn--primary" onclick="changeParticipantRole(\'' + participantId + '\')">' +
                '<i class="ri-user-settings-line"></i> 变更角色</button>' +
                '<button class="nx-btn nx-btn--danger" onclick="removeParticipant(\'' + participantId + '\')">' +
                '<i class="ri-user-unfollow-line"></i> 移除参与者</button>' +
                '</div></div>';

            document.getElementById('detailBody').innerHTML = html;
        },

        renderParticipantStatus: function(status) {
            var config = PARTICIPANT_STATUS_CONFIG[status] || PARTICIPANT_STATUS_CONFIG['JOINED'];
            return '<span class="status-badge" style="background: ' + config.color + '20; color: ' + config.color + ';">' +
                '<span class="status-dot" style="background: ' + config.color + ';"></span>' + config.name + '</span>';
        },

        renderBindingStatus: function(status) {
            var config = BINDING_STATUS_CONFIG[status] || BINDING_STATUS_CONFIG['PENDING'];
            return '<span class="status-badge" style="background: ' + config.color + '20; color: ' + config.color + ';">' +
                '<span class="status-dot" style="background: ' + config.color + ';"></span>' + config.name + '</span>';
        },

        formatTime: function(timestamp) {
            if (!timestamp) return '-';
            var date = new Date(timestamp);
            return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });
        },

        closeDetail: function() {
            document.getElementById('detailPanel').classList.remove('open');
            document.getElementById('overlay').classList.remove('open');
            currentSceneGroup = null;
        },

        filterByView: function(view) {
            if (view === 'pending') {
                window.location.href = '/console/pages/my-todos.html';
                return;
            }
            
            currentFilter = view;
            currentStatusFilter = 'all';
            document.querySelectorAll('.stat-card').forEach(function(card) {
                card.classList.toggle('active', card.dataset.view === view);
            });
            MyScenes.updateFilterChips();
            MyScenes.renderTable();
        },

        filterByStatus: function(status) {
            currentStatusFilter = status;
            MyScenes.updateFilterChips();
            MyScenes.renderTable();
        },

        toggleSceneStatus: function(sceneGroupId, currentStatus) {
            var action = currentStatus === 'ACTIVE' ? 'deactivate' : 'activate';
            var confirmMsg = currentStatus === 'ACTIVE' ? '确定要暂停此场景吗？' : '确定要激活此场景吗？';
            
            if (!confirm(confirmMsg)) return;
            
            ApiClient.post('/api/v1/scene-groups/' + sceneGroupId + '/' + action)
                .then(function(result) {
                    if (result.status === 'success') {
                        MyScenes.loadAllSceneGroups();
                        MyScenes.closeDetail();
                    } else {
                        alert('操作失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('操作失败: ' + error.message);
                });
        },

        destroySceneGroup: function(sceneGroupId) {
            if (!confirm('确定要销毁此场景组吗？此操作不可恢复！')) return;
            
            ApiClient.delete('/api/v1/scene-groups/' + sceneGroupId)
                .then(function(result) {
                    if (result.status === 'success') {
                        MyScenes.loadAllSceneGroups();
                        MyScenes.closeDetail();
                    } else {
                        alert('销毁失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('销毁失败: ' + error.message);
                });
        },

        createScene: function() {
            window.location.href = '/console/pages/scene-group-management.html?action=create';
        },

        exportAuditLogs: function() {
            if (!currentSceneGroup) return;
            var url = '/api/v1/audit/export?resourceId=' + currentSceneGroup.sceneGroupId;
            window.open(url, '_blank');
        },

        bindCapability: function() {
            if (!currentSceneGroup) return;
            window.location.href = '/console/pages/capability-binding.html?sceneGroupId=' + currentSceneGroup.sceneGroupId;
        },

        unbindCapability: function(bindingId) {
            if (!currentSceneGroup) return;
            if (!confirm('确定要解绑此能力吗？')) return;
            
            ApiClient.delete('/api/v1/scene-groups/' + currentSceneGroup.sceneGroupId + '/capabilities/' + bindingId)
                .then(function(result) {
                    if (result.status === 'success') {
                        MyScenes.openSceneGroupDetail(currentSceneGroup.sceneGroupId);
                    } else {
                        alert('解绑失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('解绑失败: ' + error.message);
                });
        },

        invokeCapability: function(capId) {
            if (!currentSceneGroup) return;
            window.location.href = '/console/pages/capability-discovery.html?capId=' + capId + '&sceneGroupId=' + currentSceneGroup.sceneGroupId;
        },

        viewBindingAuditLogs: function(bindingId) {
            if (!currentSceneGroup) return;
            window.open('/console/pages/audit-logs.html?resourceId=' + bindingId, '_blank');
        },

        inviteParticipant: function() {
            if (!currentSceneGroup) return;
            window.location.href = '/console/pages/scene-group-detail.html?id=' + currentSceneGroup.sceneGroupId;
        },

        changeParticipantRole: function(participantId) {
            if (!currentSceneGroup) return;
            var roles = ['MANAGER', 'EMPLOYEE', 'HR', 'LLM_ASSISTANT', 'COORDINATOR'];
            var roleNames = ['管理者', '员工', 'HR', 'LLM助手', '协调Agent'];
            var selected = prompt('请输入新角色:\n' + roleNames.map(function(r, i) { return (i+1) + '. ' + r; }).join('\n'));
            
            if (!selected) return;
            var roleIndex = parseInt(selected) - 1;
            if (roleIndex < 0 || roleIndex >= roles.length) {
                alert('无效的角色选择');
                return;
            }
            
            ApiClient.put('/api/v1/scene-groups/' + currentSceneGroup.sceneGroupId + '/participants/' + participantId + '/role', {
                role: roles[roleIndex]
            })
                .then(function(result) {
                    if (result.status === 'success') {
                        MyScenes.openSceneGroupDetail(currentSceneGroup.sceneGroupId);
                    } else {
                        alert('变更角色失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('变更角色失败: ' + error.message);
                });
        },

        removeParticipant: function(participantId) {
            if (!currentSceneGroup) return;
            if (!confirm('确定要移除此参与者吗？')) return;
            
            ApiClient.delete('/api/v1/scene-groups/' + currentSceneGroup.sceneGroupId + '/participants/' + participantId)
                .then(function(result) {
                    if (result.status === 'success') {
                        MyScenes.openSceneGroupDetail(currentSceneGroup.sceneGroupId);
                    } else {
                        alert('移除失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('移除失败: ' + error.message);
                });
        },

        bindKnowledgeBase: function() {
            if (!currentSceneGroup) return;
            window.location.href = '/console/pages/scene-knowledge.html?sceneGroupId=' + currentSceneGroup.sceneGroupId;
        },

        bindKnowledgeBaseToScene: function(kbId) {
            if (!currentSceneGroup) return;
            
            ApiClient.post('/api/v1/scene-groups/' + currentSceneGroup.sceneGroupId + '/knowledge-bases', {
                kbId: kbId,
                bindingType: 'SCENE_GROUP'
            })
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('知识库绑定成功');
                        MyScenes.openSceneGroupDetail(currentSceneGroup.sceneGroupId);
                    } else {
                        alert('绑定失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('绑定失败: ' + error.message);
                });
        },

        showKnowledgeDetail: function(kbId) {
            window.open('/console/pages/knowledge-base-detail.html?kbId=' + kbId, '_blank');
        },

        configureLLM: function() {
            if (!currentSceneGroup) return;
            window.location.href = '/console/pages/llm-config.html?sceneGroupId=' + currentSceneGroup.sceneGroupId;
        },

        switchLLMProvider: function(providerName) {
            if (!currentSceneGroup) return;
            if (!confirm('确定要切换到 ' + providerName + ' 吗？')) return;
            
            ApiClient.post('/api/llm/models/set', {
                provider: providerName,
                sceneGroupId: currentSceneGroup.sceneGroupId
            })
                .then(function(result) {
                    if (result.status === 'success' || result.requestStatus === 200 || result.code === 200) {
                        alert('模型切换成功');
                        MyScenes.openSceneGroupDetail(currentSceneGroup.sceneGroupId);
                    } else {
                        alert('切换失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('切换失败: ' + error.message);
                });
        }
    };

    MyScenes.init();

    global.openSceneGroupDetail = MyScenes.openSceneGroupDetail;
    global.showParticipantDetail = MyScenes.showParticipantDetail;
    global.showBindingDetail = MyScenes.showBindingDetail;
    global.switchTab = MyScenes.switchTab;
    global.closeDetailPanel = MyScenes.closeDetail;
    global.filterByView = MyScenes.filterByView;
    global.filterByStatus = MyScenes.filterByStatus;
    global.toggleSceneStatus = MyScenes.toggleSceneStatus;
    global.destroySceneGroup = MyScenes.destroySceneGroup;
    global.createScene = MyScenes.createScene;
    global.refreshScenes = MyScenes.loadAllSceneGroups;
    global.bindCapability = MyScenes.bindCapability;
    global.unbindCapability = MyScenes.unbindCapability;
    global.invokeCapability = MyScenes.invokeCapability;
    global.viewBindingAuditLogs = MyScenes.viewBindingAuditLogs;
    global.inviteParticipant = MyScenes.inviteParticipant;
    global.changeParticipantRole = MyScenes.changeParticipantRole;
    global.removeParticipant = MyScenes.removeParticipant;
    global.bindKnowledgeBase = MyScenes.bindKnowledgeBase;
    global.bindKnowledgeBaseToScene = MyScenes.bindKnowledgeBaseToScene;
    global.showKnowledgeDetail = MyScenes.showKnowledgeDetail;
    global.configureLLM = MyScenes.configureLLM;
    global.switchLLMProvider = MyScenes.switchLLMProvider;
    global.exportAuditLogs = MyScenes.exportAuditLogs;
    global.useCapability = function(capId) {
        if (!currentSceneGroup) {
            alert('请先选择场景组');
            return;
        }
        if (capId && (capId.includes('report') || capId.includes('submit'))) {
            window.location.href = '/console/pages/daily-report-form.html?sceneGroupId=' + currentSceneGroup.sceneGroupId + '&capId=' + encodeURIComponent(capId);
        } else {
            alert('该能力暂不支持直接使用');
        }
    };

})(typeof window !== 'undefined' ? window : this);
