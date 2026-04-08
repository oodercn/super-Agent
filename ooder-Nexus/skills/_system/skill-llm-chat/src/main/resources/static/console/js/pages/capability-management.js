(function(global) {
    'use strict';

    var capabilities = [];
    var installedCapabilities = [];
    var currentMethod = null;
    var discoveryStatus = {};
    var logs = [];
    var typeFilter = 'all';
    var statusFilter = 'all';
    var DISCOVERY_METHODS = [];

    var CAPABILITY_TYPES = {
        'ATOMIC': { name: '原子能力', icon: 'ri-flashlight-line', desc: '单一功能，不可分解' },
        'COMPOSITE': { name: '组合能力', icon: 'ri-links-line', desc: '组合多个原子能力' },
        'SCENE': { name: '场景能力', icon: 'ri-layout-grid-line', desc: '自驱型SuperAgent能力' },
        'DRIVER': { name: '驱动能力', icon: 'ri-timer-line', desc: '意图/时间/事件驱动' },
        'COLLABORATIVE': { name: '协作能力', icon: 'ri-team-line', desc: '跨场景协作能力' },
        'SERVICE': { name: '服务能力', icon: 'ri-server-line', desc: '业务服务、API服务' },
        'AI': { name: 'AI能力', icon: 'ri-brain-line', desc: 'LLM、机器学习' },
        'TOOL': { name: '工具能力', icon: 'ri-tools-line', desc: '工具类功能' },
        'CONNECTOR': { name: '连接器能力', icon: 'ri-plug-line', desc: '连接协议类' },
        'DATA': { name: '数据能力', icon: 'ri-database-2-line', desc: '数据存储、处理' },
        'MANAGEMENT': { name: '管理能力', icon: 'ri-settings-3-line', desc: '配置管理、监控管理' },
        'COMMUNICATION': { name: '通信能力', icon: 'ri-message-3-line', desc: '消息、通知' },
        'SECURITY': { name: '安全能力', icon: 'ri-shield-check-line', desc: '认证、加密' },
        'MONITORING': { name: '监控能力', icon: 'ri-pulse-line', desc: '日志、指标' },
        'SKILL': { name: '技能能力', icon: 'ri-flashlight-line', desc: '可安装的技能包' },
        'CUSTOM': { name: '自定义能力', icon: 'ri-tools-line', desc: '用户自定义' }
    };

    var CapabilityManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('能力中心页面初始化完成');
                CapabilityManagement.loadDiscoveryMethods();
                CapabilityManagement.loadInstalledCapabilities();
                CapabilityManagement.initFilters();
                CapabilityManagement.initLlmAssistant();
            };
        },

        initLlmAssistant: function() {
            if (typeof LlmAssistant !== 'undefined') {
                LlmAssistant.init();
            }
        },
        
        loadDiscoveryMethods: function() {
            fetch('/api/v1/discovery/methods')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        DISCOVERY_METHODS = result.data;
                    }
                    CapabilityManagement.renderDiscoveryMethods();
                })
                .catch(function(error) {
                    console.error('加载发现方法失败:', error);
                    DISCOVERY_METHODS = [];
                    CapabilityManagement.renderDiscoveryMethods();
                });
        },

        renderDiscoveryMethods: function() {
            var container = document.getElementById('discoveryMethods');
            if (!container) return;

            var html = '';
            DISCOVERY_METHODS.forEach(function(method) {
                var status = discoveryStatus[method.id] || 'idle';
                var statusText = status === 'idle' ? '就绪' : status === 'running' ? '发现中' : status === 'success' ? '完成' : '错误';
                
                html += '<div class="discovery-method-item ' + status + '" data-method="' + method.id + '" onclick="selectMethod(\'' + method.id + '\')">' +
                    '<div class="discovery-method-icon" style="color: ' + method.color + ';">' +
                    '<i class="' + method.icon + '"></i></div>' +
                    '<div class="discovery-method-info">' +
                    '<div class="discovery-method-name">' + method.name + '</div>' +
                    '<div class="discovery-method-desc">' + method.desc + '</div></div>' +
                    '<span class="discovery-method-status ' + status + '">' + statusText + '</span></div>';
            });
            container.innerHTML = html;
        },

        loadInstalledCapabilities: function() {
            ApiClient.get('/api/v1/capabilities')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        installedCapabilities = result.data;
                        capabilities = result.data;
                        CapabilityManagement.updateStats();
                        CapabilityManagement.renderCapabilities();
                        CapabilityManagement.renderTypeFilters();
                    }
                })
                .catch(function(error) {
                    console.error('加载能力失败:', error);
                    capabilities = [];
                    CapabilityManagement.updateStats();
                    CapabilityManagement.renderCapabilities();
                    CapabilityManagement.renderTypeFilters();
                });
        },

        loadMockCapabilities: function() {
            capabilities = [];
            CapabilityManagement.updateStats();
            CapabilityManagement.renderCapabilities();
            CapabilityManagement.renderTypeFilters();
        },

        renderTypeFilters: function() {
            var container = document.getElementById('typeFilters');
            if (!container) return;

            var types = {};
            capabilities.forEach(function(cap) {
                var type = cap.type || 'CUSTOM';
                types[type] = (types[type] || 0) + 1;
            });

            var html = '<span class="cap-filter-chip active" data-type="all" onclick="filterByType(\'all\')">全部</span>';
            Object.keys(types).forEach(function(type) {
                var typeInfo = CAPABILITY_TYPES[type] || CAPABILITY_TYPES['CUSTOM'];
                html += '<span class="cap-filter-chip" data-type="' + type + '" onclick="filterByType(\'' + type + '\')">' + typeInfo.name + ' (' + types[type] + ')</span>';
            });
            container.innerHTML = html;
        },

        initFilters: function() {
            var statusContainer = document.getElementById('statusFilters');
            if (statusContainer) {
                statusContainer.innerHTML = 
                    '<span class="cap-filter-chip active" data-status="all" onclick="filterByStatus(\'all\')">全部</span>' +
                    '<span class="cap-filter-chip" data-status="installed" onclick="filterByStatus(\'installed\')">已安装</span>' +
                    '<span class="cap-filter-chip" data-status="available" onclick="filterByStatus(\'available\')">可安装</span>';
            }
        },

        updateStats: function() {
            document.getElementById('installedCount').textContent = installedCapabilities.length;
            document.getElementById('discoverableCount').textContent = capabilities.length;
        },

        renderCapabilities: function() {
            var container = document.getElementById('capabilityGrid');
            var emptyState = document.getElementById('emptyState');
            if (!container) return;

            var filtered = capabilities.filter(function(cap) {
                var typeMatch = typeFilter === 'all' || (cap.type || 'CUSTOM') === typeFilter;
                var statusMatch = statusFilter === 'all' || cap.status === statusFilter;
                return typeMatch && statusMatch;
            });

            if (filtered.length === 0) {
                container.innerHTML = '';
                emptyState.style.display = 'block';
                return;
            }

            emptyState.style.display = 'none';
            var html = '';
            filtered.forEach(function(cap) {
                var typeInfo = CAPABILITY_TYPES[cap.type] || CAPABILITY_TYPES['CUSTOM'];
                var statusClass = cap.status === 'installed' ? 'installed' : 'available';
                var statusText = cap.status === 'installed' ? '已安装' : '可安装';

                html += '<div class="cap-card" onclick="showCapabilityDetail(\'' + cap.capabilityId + '\')">' +
                    '<div class="cap-card-header">' +
                    '<div class="cap-card-icon type-' + (cap.type || 'CUSTOM') + '">' +
                    '<i class="' + typeInfo.icon + '"></i></div>' +
                    '<div class="cap-card-title-area">' +
                    '<div class="cap-card-name">' + cap.name + '</div>' +
                    '<div class="cap-card-id">' + cap.capabilityId + '</div></div>' +
                    '<span class="cap-card-status ' + statusClass + '">' + statusText + '</span></div>' +
                    '<div class="cap-card-body">' +
                    '<div class="cap-card-desc">' + (cap.description || '暂无描述') + '</div>' +
                    '<div class="cap-card-meta">' +
                    '<span class="cap-card-meta-item"><i class="ri-git-branch-line"></i> v' + (cap.version || '1.0.0') + '</span>' +
                    '<span class="cap-card-meta-item"><i class="ri-cloud-line"></i> ' + (cap.source || 'local') + '</span>' +
                    '</div>';

                if (cap.dependencies && cap.dependencies.length > 0) {
                    html += '<div class="cap-card-deps">' +
                        '<div class="cap-card-deps-title">依赖 (' + cap.dependencies.length + ')</div>' +
                        '<div class="cap-card-deps-list">';
                    cap.dependencies.forEach(function(dep) {
                        html += '<span class="cap-card-dep">' + dep + '</span>';
                    });
                    html += '</div></div>';
                }

                html += '<div class="cap-card-actions">';
                if (cap.status === 'installed') {
                    html += '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); uninstallCapability(\'' + cap.capabilityId + '\')">' +
                        '<i class="ri-uninstall-line"></i> 卸载</button>' +
                        '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); invokeCapability(\'' + cap.capabilityId + '\')">' +
                        '<i class="ri-play-line"></i> 调用</button>';
                } else {
                    html += '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); installCapability(\'' + cap.capabilityId + '\')">' +
                        '<i class="ri-download-line"></i> 安装</button>';
                }
                html += '</div></div></div>';
            });
            container.innerHTML = html;
        },

        selectMethod: function(methodId) {
            currentMethod = methodId;
            
            document.querySelectorAll('.discovery-method-item').forEach(function(item) {
                item.classList.remove('active');
                if (item.dataset.method === methodId) {
                    item.classList.add('active');
                }
            });

            var method = DISCOVERY_METHODS.find(function(m) { return m.id === methodId; });
            if (method) {
                document.getElementById('currentMethodIcon').className = method.icon;
                document.getElementById('currentMethodName').textContent = method.name;
            }

            if (method && method.requiresConfig) {
                showConfigModal(method);
            }
        },

        startDiscovery: function() {
            if (!currentMethod) {
                alert('请先选择发现途径');
                return;
            }

            var method = DISCOVERY_METHODS.find(function(m) { return m.id === currentMethod; });
            if (method && method.requiresConfig) {
                showConfigModal(method);
                return;
            }

            executeDiscovery();
        },

        executeDiscoveryWithConfig: function(methodId, config) {
            discoveryStatus[methodId] = 'running';
            CapabilityManagement.renderDiscoveryMethods();
            CapabilityManagement.addLog('info', '开始发现: ' + methodId);

            document.getElementById('discoveryStatus').textContent = '发现中...';
            document.getElementById('startDiscoveryBtn').disabled = true;

            var apiUrl = '/api/v1/discovery/local';
            var requestBody = config || {};
            
            if (methodId === 'GITHUB' || methodId === 'GIT_REPOSITORY') {
                apiUrl = '/api/v1/discovery/github';
            } else if (methodId === 'GITEE') {
                apiUrl = '/api/v1/discovery/gitee';
            } else if (methodId === 'SKILL_CENTER') {
                apiUrl = '/api/v1/discovery/local';
            } else if (methodId === 'LOCAL_FS') {
                apiUrl = '/api/v1/discovery/local';
            }
            
            ApiClient.post(apiUrl, requestBody)
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        var discovered = result.data.capabilities || result.data || [];
                        discovered.forEach(function(cap) {
                            var capId = cap.id || cap.capabilityId;
                            var exists = capabilities.find(function(c) { return c.capabilityId === capId; });
                            if (!exists) {
                                capabilities.push({
                                    capabilityId: capId,
                                    name: cap.name || capId,
                                    type: cap.type || cap.capabilityCategory || 'CUSTOM',
                                    description: cap.description || '',
                                    status: cap.status || 'available',
                                    version: cap.version || '1.0.0',
                                    source: methodId,
                                    dependencies: cap.dependencies || [],
                                    provider: null,
                                    skillForm: cap.skillForm,
                                    category: cap.category || cap.capabilityCategory
                                });
                            }
                        });

                        discoveryStatus[methodId] = 'success';
                        CapabilityManagement.addLog('success', '发现完成: ' + methodId + ', 共 ' + discovered.length + ' 个能力');
                        CapabilityManagement.updateStats();
                        CapabilityManagement.renderCapabilities();
                    } else {
                        discoveryStatus[methodId] = 'error';
                        CapabilityManagement.addLog('error', '发现失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    console.error('Discovery error:', error);
                    discoveryStatus[methodId] = 'error';
                    CapabilityManagement.addLog('error', '发现失败: ' + error.message);
                    CapabilityManagement.addLog('warn', '未发现新能力');
                    CapabilityManagement.updateStats();
                    CapabilityManagement.renderCapabilities();
                })
                .finally(function() {
                    document.getElementById('discoveryStatus').textContent = '就绪';
                    document.getElementById('lastDiscovery').textContent = new Date().toLocaleTimeString();
                    document.getElementById('startDiscoveryBtn').disabled = false;
                    CapabilityManagement.renderDiscoveryMethods();
                });
        },

        addLog: function(level, message) {
            logs.unshift({
                time: new Date().toLocaleTimeString(),
                level: level,
                message: message
            });
            if (logs.length > 50) logs.pop();
            CapabilityManagement.renderLogs();
        },

        renderLogs: function() {
            var container = document.getElementById('logsPanel');
            if (!container) return;

            var html = '';
            logs.slice(0, 10).forEach(function(log) {
                html += '<div class="cap-log-entry">' +
                    '<span class="cap-log-time">' + log.time + '</span>' +
                    '<span class="cap-log-level ' + log.level + '">' + log.level.toUpperCase() + '</span>' +
                    '<span class="cap-log-msg">' + log.message + '</span></div>';
            });
            container.innerHTML = html;
        },

        showDetail: function(capabilityId) {
            var cap = capabilities.find(function(c) { return c.capabilityId === capabilityId; });
            if (!cap) return;

            var typeInfo = CAPABILITY_TYPES[cap.type] || CAPABILITY_TYPES['CUSTOM'];
            
            document.getElementById('detailTitle').textContent = cap.name;
            
            var html = '<div class="cap-detail-section">' +
                '<div class="cap-detail-section-title">基本信息</div>' +
                '<div class="cap-detail-row"><span class="cap-detail-row-label">能力ID</span><span class="cap-detail-row-value">' + cap.capabilityId + '</span></div>' +
                '<div class="cap-detail-row"><span class="cap-detail-row-label">类型</span><span class="cap-detail-row-value">' + typeInfo.name + '</span></div>' +
                '<div class="cap-detail-row"><span class="cap-detail-row-label">版本</span><span class="cap-detail-row-value">' + (cap.version || '1.0.0') + '</span></div>' +
                '<div class="cap-detail-row"><span class="cap-detail-row-label">状态</span><span class="cap-detail-row-value">' + (cap.status === 'installed' ? '已安装' : '可安装') + '</span></div>' +
                '<div class="cap-detail-row"><span class="cap-detail-row-label">来源</span><span class="cap-detail-row-value">' + (cap.source || 'local') + '</span></div>' +
                '</div>';

            html += '<div class="cap-detail-section">' +
                '<div class="cap-detail-section-title">描述</div>' +
                '<p style="font-size: 14px; line-height: 1.6;">' + (cap.description || '暂无描述') + '</p>' +
                '</div>';

            if (cap.dependencies && cap.dependencies.length > 0) {
                html += '<div class="cap-detail-section">' +
                    '<div class="cap-detail-section-title">依赖关系</div>' +
                    '<div class="cap-rel-graph">' +
                    '<div class="cap-rel-node primary"><i class="' + typeInfo.icon + '"></i> ' + cap.name + '</div>';
                
                cap.dependencies.forEach(function(dep) {
                    var depCap = capabilities.find(function(c) { return c.capabilityId === dep; });
                    var depTypeInfo = depCap ? (CAPABILITY_TYPES[depCap.type] || CAPABILITY_TYPES['CUSTOM']) : CAPABILITY_TYPES['CUSTOM'];
                    var isInstalled = depCap && depCap.status === 'installed';
                    html += '<div class="cap-rel-node" style="' + (isInstalled ? '' : 'opacity: 0.6;') + '">' +
                        '<i class="' + depTypeInfo.icon + '"></i> ' + dep + '</div>';
                });
                
                html += '</div></div>';
            }

            if (cap.status === 'installed') {
                html += '<div class="cap-detail-section">' +
                    '<div class="cap-detail-section-title">操作</div>' +
                    '<div style="display: flex; gap: 8px;">' +
                    '<button class="nx-btn nx-btn--primary" onclick="invokeCapability(\'' + cap.capabilityId + '\')"><i class="ri-play-line"></i> 调用能力</button>' +
                    '<button class="nx-btn nx-btn--secondary" onclick="uninstallCapability(\'' + cap.capabilityId + '\')"><i class="ri-uninstall-line"></i> 卸载</button>' +
                    '</div></div>';
            } else {
                html += '<div class="cap-detail-section">' +
                    '<div class="cap-detail-section-title">操作</div>' +
                    '<button class="nx-btn nx-btn--primary" onclick="installCapability(\'' + cap.capabilityId + '\')"><i class="ri-download-line"></i> 安装能力</button>' +
                    '</div>';
            }

            document.getElementById('detailBody').innerHTML = html;
            document.getElementById('detailPanel').classList.add('open');
            document.getElementById('overlay').classList.add('open');
        },

        closeDetail: function() {
            document.getElementById('detailPanel').classList.remove('open');
            document.getElementById('overlay').classList.remove('open');
        },

        installCap: function(capabilityId) {
            var cap = capabilities.find(function(c) { return c.capabilityId === capabilityId; });
            if (!cap) return;

            CapabilityManagement.addLog('info', '正在安装: ' + capabilityId);

            ApiClient.post('/api/v1/capabilities', {
                capabilityId: cap.capabilityId,
                name: cap.name,
                type: cap.type,
                description: cap.description
            })
                .then(function(result) {
                    if (result.status === 'success') {
                        cap.status = 'installed';
                        installedCapabilities.push(cap);
                        CapabilityManagement.addLog('success', '安装成功: ' + capabilityId);
                        CapabilityManagement.updateStats();
                        CapabilityManagement.renderCapabilities();
                        CapabilityManagement.showDetail(capabilityId);
                    } else {
                        CapabilityManagement.addLog('error', '安装失败: ' + result.message);
                    }
                })
                .catch(function(error) {
                    cap.status = 'installed';
                    installedCapabilities.push(cap);
                    CapabilityManagement.addLog('success', '安装成功(本地): ' + capabilityId);
                    CapabilityManagement.updateStats();
                    CapabilityManagement.renderCapabilities();
                });
        },

        uninstallCap: function(capabilityId) {
            if (!confirm('确定要卸载此能力吗？')) return;

            var cap = capabilities.find(function(c) { return c.capabilityId === capabilityId; });
            if (!cap) return;

            CapabilityManagement.addLog('info', '正在卸载: ' + capabilityId);

            ApiClient.delete('/api/v1/capabilities/' + capabilityId)
                .then(function(result) {
                    if (result.status === 'success') {
                        cap.status = 'available';
                        installedCapabilities = installedCapabilities.filter(function(c) { return c.capabilityId !== capabilityId; });
                        CapabilityManagement.addLog('success', '卸载成功: ' + capabilityId);
                        CapabilityManagement.updateStats();
                        CapabilityManagement.renderCapabilities();
                        CapabilityManagement.closeDetail();
                    } else {
                        CapabilityManagement.addLog('error', '卸载失败: ' + result.message);
                    }
                })
                .catch(function(error) {
                    cap.status = 'available';
                    installedCapabilities = installedCapabilities.filter(function(c) { return c.capabilityId !== capabilityId; });
                    CapabilityManagement.addLog('success', '卸载成功(本地): ' + capabilityId);
                    CapabilityManagement.updateStats();
                    CapabilityManagement.renderCapabilities();
                    CapabilityManagement.closeDetail();
                });
        },

        invokeCap: function(capabilityId) {
            var cap = capabilities.find(function(c) { return c.capabilityId === capabilityId; });
            if (!cap) return;

            CapabilityManagement.addLog('info', '正在调用: ' + capabilityId);

            ApiClient.post('/api/v1/discovery/capabilities/invoke', {
                capabilityId: capabilityId,
                params: {}
            })
                .then(function(result) {
                    if (result.status === 'success') {
                        CapabilityManagement.addLog('success', '调用成功: ' + JSON.stringify(result.data));
                        alert('调用成功: ' + JSON.stringify(result.data, null, 2));
                    } else {
                        CapabilityManagement.addLog('error', '调用失败: ' + result.message);
                        alert('调用失败: ' + result.message);
                    }
                })
                .catch(function(error) {
                    CapabilityManagement.addLog('error', '调用失败: ' + error.message);
                    alert('调用失败: ' + error.message);
                });
        },

        refresh: function() {
            if (currentMethod) {
                executeDiscovery();
            } else {
                CapabilityManagement.loadInstalledCapabilities();
            }
        },

        filterByType: function(type) {
            typeFilter = type;
            document.querySelectorAll('#typeFilters .cap-filter-chip').forEach(function(chip) {
                chip.classList.toggle('active', chip.dataset.type === type);
            });
            CapabilityManagement.renderCapabilities();
        },

        filterByStatus: function(status) {
            statusFilter = status;
            document.querySelectorAll('#statusFilters .cap-filter-chip').forEach(function(chip) {
                chip.classList.toggle('active', chip.dataset.status === status);
            });
            CapabilityManagement.renderCapabilities();
        }
    };

    CapabilityManagement.init();

    global.selectMethod = CapabilityManagement.selectMethod;
    global.startDiscovery = CapabilityManagement.startDiscovery;
    global.executeDiscovery = function() {
        CapabilityManagement.executeDiscoveryWithConfig(currentMethod, {});
    };
    global.showCapabilityDetail = CapabilityManagement.showDetail;
    global.closeDetailPanel = CapabilityManagement.closeDetail;
    global.installCapability = CapabilityManagement.installCap;
    global.uninstallCapability = CapabilityManagement.uninstallCap;
    global.invokeCapability = CapabilityManagement.invokeCap;
    global.refreshCurrentView = CapabilityManagement.refresh;
    global.filterByType = CapabilityManagement.filterByType;
    global.filterByStatus = CapabilityManagement.filterByStatus;

    global.showConfigModal = function(method) {
        var modal = document.getElementById('configModal');
        var title = document.getElementById('configModalTitle');
        var body = document.getElementById('configModalBody');

        title.textContent = method.name + ' 配置';

        var html = '';
        if (method.configFields) {
            method.configFields.forEach(function(field) {
                html += '<div class="form-group">' +
                    '<label class="form-label">' + field.label + '</label>';
                if (field.type === 'password') {
                    html += '<input type="password" class="form-input" id="config_' + field.name + '" placeholder="' + (field.placeholder || '') + '">';
                } else if (field.type === 'number') {
                    html += '<input type="number" class="form-input" id="config_' + field.name + '" value="' + (field.default || '') + '">';
                } else {
                    html += '<input type="text" class="form-input" id="config_' + field.name + '" value="' + (field.default || '') + '" placeholder="' + (field.placeholder || '') + '">';
                }
                html += '</div>';
            });
        }
        body.innerHTML = html;
        modal.classList.add('open');
    };

    global.closeConfigModal = function() {
        document.getElementById('configModal').classList.remove('open');
    };

    global.showRegisterModal = function() {
        document.getElementById('registerModal').classList.add('open');
    };

    global.closeRegisterModal = function() {
        document.getElementById('registerModal').classList.remove('open');
    };

    global.registerCapability = function() {
        var capabilityId = document.getElementById('regCapabilityId').value;
        var name = document.getElementById('regCapabilityName').value;
        var type = document.getElementById('regType').value;
        var desc = document.getElementById('regCapabilityDesc').value;

        if (!capabilityId || !name) {
            alert('请填写必填字段');
            return;
        }

            ApiClient.post('/api/v1/capabilities', {
                capabilityId: capabilityId,
                name: name,
                type: type,
                description: desc
            })
                .then(function(result) {
                    if (result.status === 'success') {
                        capabilities.push({
                            capabilityId: capabilityId,
                            name: name,
                            type: type,
                            description: desc,
                            status: 'installed',
                            version: '1.0.0',
                            source: 'local',
                            dependencies: [],
                            provider: null
                        });
                        installedCapabilities.push(capabilities[capabilities.length - 1]);
                        CapabilityManagement.updateStats();
                        CapabilityManagement.renderCapabilities();
                        closeRegisterModal();
                        alert('注册成功');
                    } else {
                        alert('注册失败: ' + result.message);
                    }
                })
                .catch(function(error) {
                    alert('注册失败: ' + error.message);
                });
    };

})(typeof window !== 'undefined' ? window : this);
