(function() {
    'use strict';

    var LlmConfig = {
        configs: [],
        providers: [],
        currentConfig: null,
        deleteConfigId: null,
        currentProvider: null,
        currentModel: null,
        modelsByProvider: {},
        selectedProvider: null,
        selectedModel: null,
        
        init: function() {
            console.log('[LlmConfig] 初始化LLM配置页面');
            LlmConfig.loadProviders();
            LlmConfig.loadConfigs();
        },
        
        loadProviders: function() {
            fetch('/api/v1/llm/providers/info', {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                console.log('[LlmConfig] Provider API response:', result);
                if (result && (result.code === 200 || result.requestStatus === 200) && result.data) {
                    var providerList = result.data;
                    LlmConfig.providers = providerList.map(function(p) {
                        var models = (p.models || []).map(function(m) {
                            return m.modelId;
                        });
                        return {
                            type: p.code,
                            name: p.displayName,
                            description: p.description,
                            defaultBaseUrl: p.defaultBaseUrl,
                            models: models,
                            modelDetails: p.models || [],
                            isCurrent: p.isCurrent,
                            isConfigured: p.isConfigured
                        };
                    });
                    
                    var currentProviderInfo = providerList.find(function(p) { return p.isCurrent; });
                    if (currentProviderInfo) {
                        LlmConfig.currentProvider = currentProviderInfo.code;
                    }
                    
                    LlmConfig.modelsByProvider = {};
                    providerList.forEach(function(p) {
                        LlmConfig.modelsByProvider[p.code] = (p.models || []).map(function(m) {
                            return m.modelId;
                        });
                    });
                    
                    LlmConfig.renderProviders();
                    LlmConfig.populateProviderSelects();
                    LlmConfig.updateCurrentStatus();
                } else {
                    console.error('[LlmConfig] Provider API返回异常:', result);
                }
            })
            .catch(function(error) {
                console.error('[LlmConfig] 加载Provider失败:', error);
                LlmConfig.providers = [];
                LlmConfig.renderProviders();
                LlmConfig.populateProviderSelects();
            });
        },
        
        loadConfigs: function() {
            fetch('/api/v1/llm-config', {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                var configsData = [];
                if (result && result.status === 'success' && result.data) {
                    if (Array.isArray(result.data.configs)) {
                        configsData = result.data.configs;
                    } else if (Array.isArray(result.data)) {
                        configsData = result.data;
                    } else {
                        console.warn('[LlmConfig] API返回数据格式异常:', result.data);
                    }
                }
                LlmConfig.configs = configsData;
                LlmConfig.renderConfigs();
                LlmConfig.updateStats();
            })
            .catch(function(error) {
                console.error('[LlmConfig] 加载配置失败:', error);
                LlmConfig.configs = [];
                LlmConfig.renderConfigs();
            });
        },
        
        renderProviders: function() {
            var grid = document.getElementById('provider-grid');
            if (!grid) return;
            
            grid.innerHTML = '';
            
            LlmConfig.providers.forEach(function(provider) {
                var card = document.createElement('div');
                card.className = 'provider-card';
                if (provider.type === LlmConfig.currentProvider) {
                    card.className += ' active';
                }
                card.onclick = function() { LlmConfig.showProviderDetail(provider.type); };
                
                var iconClass = 'provider-card__icon provider-card__icon--' + provider.type;
                var iconMap = {
                    'aliyun-bailian': 'ri-cloud-line',
                    'qianwen': 'ri-robot-line',
                    'deepseek': 'ri-brain-line',
                    'baidu': 'ri-baidu-fill',
                    'openai': 'ri-openai-fill',
                    'ollama': 'ri-robot-2-line',
                    'anthropic': 'ri-robot-line',
                    'google': 'ri-google-fill',
                    'zhipu': 'ri-brain-line',
                    'minimax': 'ri-sparkling-line',
                    'moonshot': 'ri-moon-line',
                    'siliconflow': 'ri-cpu-line',
                    'azure': 'ri-microsoft-line',
                    'mock': 'ri-test-tube-line'
                };
                
                var isActive = provider.type === LlmConfig.currentProvider;
                
                card.innerHTML = '<div class="' + iconClass + '">' +
                    '<i class="' + (iconMap[provider.type] || 'ri-robot-line') + '"></i>' +
                '</div>' +
                '<div class="provider-card__name">' + provider.name + '</div>' +
                '<div class="provider-card__models">' + (provider.models ? provider.models.length : 0) + ' 个模型</div>' +
                (isActive ? '<span class="provider-badge active">当前</span>' : '');
                
                grid.appendChild(card);
            });
        },
        
        showProviderDetail: function(providerType) {
            var provider = LlmConfig.providers.find(function(p) { return p.type === providerType; });
            if (!provider) return;
            
            LlmConfig.selectedProvider = providerType;
            LlmConfig.selectedModel = null;
            
            var modalTitle = document.getElementById('provider-modal-title');
            var modalBody = document.getElementById('provider-modal-body');
            
            if (modalTitle) modalTitle.textContent = provider.name + ' 详情';
            
            var iconMap = {
                'aliyun-bailian': 'ri-cloud-line',
                'qianwen': 'ri-robot-line',
                'deepseek': 'ri-brain-line',
                'baidu': 'ri-baidu-fill',
                'openai': 'ri-openai-fill',
                'ollama': 'ri-robot-2-line',
                'anthropic': 'ri-robot-line',
                'google': 'ri-google-fill',
                'zhipu': 'ri-brain-line',
                'minimax': 'ri-sparkling-line',
                'moonshot': 'ri-moon-line',
                'siliconflow': 'ri-cpu-line',
                'azure': 'ri-microsoft-line',
                'mock': 'ri-test-tube-line'
            };
            
            var relatedConfigs = LlmConfig.configs.filter(function(c) { 
                return c.providerType === providerType || c.provider === providerType; 
            });
            
            if (modalBody) {
                var html = '<div style="display: flex; align-items: center; gap: 16px; margin-bottom: 24px;">' +
                    '<div class="provider-card__icon provider-card__icon--' + providerType + '" style="width: 64px; height: 64px; font-size: 32px;">' +
                        '<i class="' + (iconMap[providerType] || 'ri-robot-line') + '"></i>' +
                    '</div>' +
                    '<div>' +
                        '<h4 style="margin: 0 0 4px 0; font-size: 20px;">' + provider.name + '</h4>' +
                        '<p style="margin: 0; color: var(--nx-text-secondary);">Provider ID: ' + providerType + '</p>' +
                    '</div>' +
                '</div>' +
                '<div class="nx-form-divider"><span>基本信息</span></div>' +
                '<div style="background: var(--nx-bg-elevated); padding: var(--nx-space-4); border-radius: var(--nx-radius); margin-bottom: var(--nx-space-4);">' +
                    '<div style="display: grid; grid-template-columns: 1fr 1fr; gap: var(--nx-space-4);">' +
                        '<div>' +
                            '<label style="font-size: var(--nx-text-xs); color: var(--nx-text-tertiary); display: block; margin-bottom: var(--nx-space-1);">描述</label>' +
                            '<p style="margin: 0; font-size: var(--nx-text-sm);">' + (provider.description || '暂无描述') + '</p>' +
                        '</div>' +
                        '<div>' +
                            '<label style="font-size: var(--nx-text-xs); color: var(--nx-text-tertiary); display: block; margin-bottom: var(--nx-space-1);">默认 API 地址</label>' +
                            '<p style="margin: 0; font-size: var(--nx-text-sm); font-family: monospace; word-break: break-all;">' + (provider.defaultBaseUrl || '-') + '</p>' +
                        '</div>' +
                        '<div>' +
                            '<label style="font-size: var(--nx-text-xs); color: var(--nx-text-tertiary); display: block; margin-bottom: var(--nx-space-1);">支持模型数</label>' +
                            '<p style="margin: 0; font-size: var(--nx-text-sm);">' + (provider.models ? provider.models.length : 0) + ' 个</p>' +
                        '</div>' +
                        '<div>' +
                            '<label style="font-size: var(--nx-text-xs); color: var(--nx-text-tertiary); display: block; margin-bottom: var(--nx-space-1);">状态</label>' +
                            '<p style="margin: 0; font-size: var(--nx-text-sm);">' + 
                                (provider.isCurrent ? 
                                    '<span style="color: var(--nx-success);"><i class="ri-checkbox-circle-fill"></i> 当前使用</span>' : 
                                    (provider.isConfigured ? 
                                        '<span style="color: var(--nx-success);"><i class="ri-checkbox-blank-circle-fill"></i> 已配置</span>' :
                                        '<span style="color: var(--nx-text-secondary);"><i class="ri-checkbox-blank-circle-line"></i> 未配置</span>')) +
                            '</p>' +
                        '</div>' +
                    '</div>' +
                '</div>' +
                '<div class="nx-form-divider"><span>可用模型 (' + (provider.models ? provider.models.length : 0) + ')</span></div>' +
                '<p style="font-size: var(--nx-text-xs); color: var(--nx-text-tertiary); margin-bottom: var(--nx-space-2);">点击选择模型，然后点击确定按钮应用</p>' +
                '<div id="model-list" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(180px, 1fr)); gap: 8px;">';
                
                var modelDetails = provider.modelDetails || [];
                modelDetails.forEach(function(modelInfo) {
                    var modelId = modelInfo.modelId;
                    var isCurrentModel = providerType === LlmConfig.currentProvider && modelId === LlmConfig.currentModel;
                    var badges = [];
                    if (modelInfo.supportsFunctionCalling) badges.push('<i class="ri-code-line" title="支持函数调用"></i>');
                    if (modelInfo.supportsMultimodal) badges.push('<i class="ri-image-line" title="支持多模态"></i>');
                    if (modelInfo.supportsEmbedding) badges.push('<i class="ri-file-text-line" title="支持嵌入"></i>');
                    
                    html += '<div class="model-tag' + (isCurrentModel ? ' current' : '') + '" ' +
                        'id="model-item-' + modelId.replace(/[^a-zA-Z0-9]/g, '_') + '" ' +
                        'style="padding: var(--nx-space-2) var(--nx-space-3); background: var(--nx-bg-elevated); border-radius: var(--nx-radius); font-size: var(--nx-text-sm); border: 2px solid ' + (isCurrentModel ? 'var(--nx-primary)' : 'var(--nx-border)') + '; cursor: pointer;" ' +
                        'onclick="LlmConfig.selectModel(\'' + providerType + '\', \'' + modelId + '\')">' +
                        '<div style="display: flex; align-items: center; justify-content: space-between;">' +
                            '<div>' +
                                '<i class="ri-cpu-line" style="color: var(--nx-primary); margin-right: 4px;"></i>' +
                                '<span title="' + (modelInfo.displayName || modelId) + '">' + (modelInfo.displayName || modelId) + '</span>' +
                            '</div>' +
                            '<div style="font-size: 10px; color: var(--nx-text-tertiary);">' + badges.join('') + '</div>' +
                        '</div>' +
                        '<div style="font-size: 10px; color: var(--nx-text-tertiary); margin-top: 4px;">' +
                            'Max: ' + (modelInfo.maxTokens || 4096) + ' tokens' +
                            (modelInfo.costPer1kTokens ? ' | $' + modelInfo.costPer1kTokens.toFixed(4) + '/1K' : '') +
                        '</div>' +
                        (isCurrentModel ? ' <span style="color: var(--nx-primary); font-size: 10px;">(当前)</span>' : '') +
                    '</div>';
                });
                
                html += '</div>' +
                '<div class="nx-form-divider"><span>相关配置 (' + relatedConfigs.length + ')</span></div>';
                
                if (relatedConfigs.length > 0) {
                    html += '<table class="nx-table" style="font-size: var(--nx-text-sm);">' +
                        '<thead><tr><th>配置名称</th><th>级别</th><th>模型</th><th>状态</th></tr></thead>' +
                        '<tbody>';
                    
                    relatedConfigs.forEach(function(config) {
                        html += '<tr>' +
                            '<td>' + (config.name || '未命名') + '</td>' +
                            '<td>' + LlmConfig.getLevelDisplayName(config.level) + '</td>' +
                            '<td>' + (config.model || '-') + '</td>' +
                            '<td><span class="config-status config-status--' + (config.enabled ? 'active' : 'inactive') + '">' +
                                (config.enabled ? '启用' : '禁用') + '</span></td>' +
                        '</tr>';
                    });
                    
                    html += '</tbody></table>';
                } else {
                    html += '<p style="color: var(--nx-text-tertiary); text-align: center; padding: var(--nx-space-4);">暂无相关配置</p>';
                }
                
                modalBody.innerHTML = html;
            }
            
            document.getElementById('provider-modal').classList.add('nx-modal--open');
        },
        
        selectModel: function(providerType, modelId) {
            LlmConfig.selectedProvider = providerType;
            LlmConfig.selectedModel = modelId;
            
            var modelItems = document.querySelectorAll('#model-list .model-tag');
            modelItems.forEach(function(item) {
                item.style.borderColor = 'var(--nx-border)';
                item.style.background = 'var(--nx-bg-elevated)';
            });
            
            var selectedItem = document.getElementById('model-item-' + modelId.replace(/[^a-zA-Z0-9]/g, '_'));
            if (selectedItem) {
                selectedItem.style.borderColor = 'var(--nx-primary)';
                selectedItem.style.background = 'var(--nx-primary-light)';
            }
        },
        
        confirmProviderSelection: function() {
            if (!LlmConfig.selectedProvider || !LlmConfig.selectedModel) {
                LlmConfig.showError('请先选择一个模型');
                return;
            }
            LlmConfig.setConfig(LlmConfig.selectedProvider, LlmConfig.selectedModel);
        },
        
        closeProviderModal: function() {
            document.getElementById('provider-modal').classList.remove('nx-modal--open');
        },
        
        populateProviderSelects: function() {
            var filterSelect = document.getElementById('filter-provider');
            var formSelect = document.getElementById('config-provider');
            
            if (filterSelect) {
                filterSelect.innerHTML = '<option value="">全部Provider</option>';
                LlmConfig.providers.forEach(function(p) {
                    filterSelect.innerHTML += '<option value="' + p.type + '">' + p.name + '</option>';
                });
            }
            
            if (formSelect) {
                formSelect.innerHTML = '<option value="">请选择Provider</option>';
                LlmConfig.providers.forEach(function(p) {
                    formSelect.innerHTML += '<option value="' + p.type + '">' + p.name + '</option>';
                });
            }
        },
        
        getProviderDisplayName: function(type) {
            var provider = LlmConfig.providers.find(function(p) { return p.type === type; });
            return provider ? provider.name : type;
        },
        
        renderConfigs: function() {
            var tbody = document.getElementById('config-table-body');
            if (!tbody) return;
            
            if (!LlmConfig.configs.length) {
                tbody.innerHTML = '<tr>' +
                    '<td colspan="7" class="nx-text-center nx-text-secondary nx-py-8">' +
                        '<i class="ri-inbox-line" style="font-size: 24px;"></i>' +
                        '<p>暂无配置数据</p>' +
                    '</td>' +
                '</tr>';
                return;
            }
            
            tbody.innerHTML = '';
            
            LlmConfig.configs.forEach(function(config) {
                var tr = document.createElement('tr');
                var isEnabled = config.enabled === true;
                tr.innerHTML = '<td>' + (config.name || '未命名配置') + '</td>' +
                '<td><span class="config-level config-level--' + (config.level ? config.level.toLowerCase() : 'enterprise') + '">' +
                    LlmConfig.getLevelDisplayName(config.level) +
                '</span></td>' +
                '<td>' + LlmConfig.getProviderDisplayName(config.providerType || config.provider) + '</td>' +
                '<td>' + (config.model || '-') + '</td>' +
                '<td><span class="config-status config-status--' + (isEnabled ? 'active' : 'inactive') + '">' +
                    '<i class="ri-checkbox-blank-circle-fill" style="font-size: 8px;"></i> ' +
                    (isEnabled ? '启用' : '禁用') +
                '</span></td>' +
                '<td>' + LlmConfig.formatTime(config.updatedAt || config.updateTime) + '</td>' +
                '<td>' +
                    '<div class="action-btns">' +
                        '<button class="nx-btn nx-btn--ghost nx-btn--icon nx-btn--sm" onclick="LlmConfig.editConfig(\'' + config.id + '\')" title="编辑">' +
                            '<i class="ri-edit-line"></i>' +
                        '</button>' +
                        '<button class="nx-btn nx-btn--ghost nx-btn--icon nx-btn--sm" onclick="LlmConfig.openDeleteModal(\'' + config.id + '\', \'' + (config.name || '未命名配置') + '\')" title="删除">' +
                            '<i class="ri-delete-bin-line"></i>' +
                        '</button>' +
                    '</div>' +
                '</td>';
                tbody.appendChild(tr);
            });
        },
        
        getLevelDisplayName: function(level) {
            var names = {
                'ENTERPRISE': '企业级',
                'DEPARTMENT': '部门级',
                'PERSONAL': '个人级',
                'SCENE': '场景级',
                'SCENE_GROUP': '场景组级'
            };
            return names[level] || level;
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
        
        updateStats: function() {
            var total = LlmConfig.configs.length;
            var enterprise = LlmConfig.configs.filter(function(c) { return c.level === 'ENTERPRISE'; }).length;
            var personal = LlmConfig.configs.filter(function(c) { return c.level === 'PERSONAL'; }).length;
            var providers = LlmConfig.providers.length;
            
            var totalEl = document.getElementById('stat-total');
            var enterpriseEl = document.getElementById('stat-enterprise');
            var personalEl = document.getElementById('stat-personal');
            var providersEl = document.getElementById('stat-providers');
            
            if (totalEl) totalEl.textContent = total;
            if (enterpriseEl) enterpriseEl.textContent = enterprise;
            if (personalEl) personalEl.textContent = personal;
            if (providersEl) providersEl.textContent = providers;
        },
        
        updateCurrentStatus: function() {
            var providerNameEl = document.getElementById('currentProviderName');
            var modelNameEl = document.getElementById('currentModelName');
            
            if (providerNameEl) {
                providerNameEl.textContent = LlmConfig.getProviderDisplayName(LlmConfig.currentProvider);
            }
            if (modelNameEl) {
                modelNameEl.textContent = LlmConfig.currentModel;
            }
        },
        
        filterConfigs: function() {
            var levelFilter = document.getElementById('filter-level').value;
            var providerFilter = document.getElementById('filter-provider').value;
            
            var filtered = LlmConfig.configs;
            
            if (levelFilter) {
                filtered = filtered.filter(function(c) { return c.level === levelFilter; });
            }
            
            if (providerFilter) {
                filtered = filtered.filter(function(c) { 
                    return (c.providerType || c.provider) === providerFilter; 
                });
            }
            
            var tbody = document.getElementById('config-table-body');
            if (!tbody) return;
            
            if (!filtered.length) {
                tbody.innerHTML = '<tr><td colspan="7" class="nx-text-center nx-text-secondary nx-py-8">没有匹配的配置</td></tr>';
                return;
            }
            
            var originalConfigs = LlmConfig.configs;
            LlmConfig.configs = filtered;
            LlmConfig.renderConfigs();
            LlmConfig.configs = originalConfigs;
        },
        
        openCreateModal: function() {
            LlmConfig.currentConfig = null;
            
            var modalTitle = document.querySelector('#modal-title span');
            if (modalTitle) modalTitle.textContent = '新建配置';
            
            document.getElementById('config-form').reset();
            document.getElementById('config-id').value = '';
            document.getElementById('owner-id-group').style.display = 'none';
            document.getElementById('config-temperature').value = '0.7';
            document.getElementById('config-max-tokens').value = '4096';
            
            document.getElementById('config-modal').classList.add('nx-modal--open');
        },
        
        editConfig: function(id) {
            var config = LlmConfig.configs.find(function(c) { return String(c.id) === String(id); });
            if (!config) {
                console.error('Config not found:', id);
                return;
            }
            
            LlmConfig.currentConfig = config;
            
            var modalTitle = document.querySelector('#modal-title span');
            if (modalTitle) modalTitle.textContent = '编辑配置';
            
            document.getElementById('config-id').value = config.id || '';
            document.getElementById('config-name').value = config.name || '';
            document.getElementById('config-level').value = config.level || '';
            document.getElementById('config-owner-id').value = config.scopeId || '';
            
            var providerSelect = document.getElementById('config-provider');
            providerSelect.value = config.providerType || config.provider || '';
            
            LlmConfig.onProviderChange();
            
            setTimeout(function() {
                var modelSelect = document.getElementById('config-model');
                modelSelect.value = config.model || '';
            }, 100);
            
            var apiKey = config.providerConfig && config.providerConfig.apiKey ? config.providerConfig.apiKey : '';
            var apiKeyInput = document.getElementById('config-api-key');
            if (apiKey) {
                apiKeyInput.value = apiKey;
            } else {
                apiKeyInput.value = '';
            }
            
            document.getElementById('config-base-url').value = config.providerConfig && config.providerConfig.baseUrl ? config.providerConfig.baseUrl : '';
            document.getElementById('config-temperature').value = config.options && config.options.temperature ? config.options.temperature : 0.7;
            document.getElementById('config-max-tokens').value = config.options && config.options.max_tokens ? config.options.max_tokens : 4096;
            
            LlmConfig.onLevelChange();
            
            document.getElementById('config-modal').classList.add('nx-modal--open');
        },
        
        closeModal: function() {
            document.getElementById('config-modal').classList.remove('nx-modal--open');
        },
        
        toggleApiKeyVisibility: function() {
            var input = document.getElementById('config-api-key');
            var icon = document.getElementById('api-key-toggle-icon');
            
            if (input.type === 'password') {
                input.type = 'text';
                icon.className = 'ri-eye-off-line';
            } else {
                input.type = 'password';
                icon.className = 'ri-eye-line';
            }
        },
        
        onLevelChange: function() {
            var level = document.getElementById('config-level').value;
            var ownerGroup = document.getElementById('owner-id-group');
            
            if (!ownerGroup) return;
            
            if (level === 'PERSONAL' || level === 'SCENE' || level === 'DEPARTMENT') {
                ownerGroup.style.display = 'block';
            } else {
                ownerGroup.style.display = 'none';
            }
        },
        
        onProviderChange: function() {
            var providerType = document.getElementById('config-provider').value;
            var modelSelect = document.getElementById('config-model');
            
            modelSelect.innerHTML = '<option value="">请选择模型</option>';
            
            if (!providerType) return;
            
            var provider = LlmConfig.providers.find(function(p) { return p.type === providerType; });
            if (provider && provider.models) {
                provider.models.forEach(function(model) {
                    modelSelect.innerHTML += '<option value="' + model + '">' + model + '</option>';
                });
            }
        },
        
        saveConfig: function() {
            var id = document.getElementById('config-id').value;
            
            var config = {
                name: document.getElementById('config-name').value,
                level: document.getElementById('config-level').value,
                scopeId: document.getElementById('config-owner-id').value || 'default',
                providerType: document.getElementById('config-provider').value,
                model: document.getElementById('config-model').value,
                providerConfig: {
                    apiKey: document.getElementById('config-api-key').value,
                    baseUrl: document.getElementById('config-base-url').value
                },
                options: {
                    temperature: parseFloat(document.getElementById('config-temperature').value) || 0.7,
                    max_tokens: parseInt(document.getElementById('config-max-tokens').value) || 4096
                },
                enabled: true
            };
            
            if (!config.name || !config.level || !config.providerType || !config.model) {
                alert('请填写必填字段');
                return;
            }
            
            var url = id ? '/api/v1/llm-config/' + id : '/api/v1/llm-config';
            var method = id ? 'PUT' : 'POST';
            
            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(config)
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    LlmConfig.closeModal();
                    LlmConfig.loadConfigs();
                    LlmConfig.showSuccess('保存成功');
                } else {
                    alert('保存失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                console.error('Failed to save config:', error);
                alert('保存失败: ' + error.message);
            });
        },
        
        setConfig: function(provider, model) {
            fetch('/api/v1/llm/models/set', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    provider: provider,
                    modelId: model
                })
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result && (result.code === 200 || result.requestStatus === 200)) {
                    LlmConfig.currentProvider = provider;
                    LlmConfig.currentModel = model;
                    LlmConfig.renderProviders();
                    LlmConfig.updateCurrentStatus();
                    LlmConfig.closeProviderModal();
                    LlmConfig.showSuccess('配置已更新');
                } else {
                    LlmConfig.showError(result.message || '设置失败');
                }
            })
            .catch(function(error) {
                console.error('[LlmConfig] 设置异常:', error);
                LlmConfig.showError('设置配置异常');
            });
        },
        
        openDeleteModal: function(id, name) {
            LlmConfig.deleteConfigId = id;
            document.getElementById('delete-config-name').textContent = name;
            document.getElementById('delete-modal').classList.add('nx-modal--open');
        },
        
        closeDeleteModal: function() {
            LlmConfig.deleteConfigId = null;
            document.getElementById('delete-modal').classList.remove('nx-modal--open');
        },
        
        confirmDelete: function() {
            if (!LlmConfig.deleteConfigId) return;
            
            fetch('/api/v1/llm-config/' + LlmConfig.deleteConfigId, {
                method: 'DELETE'
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' || result.code === 200 || result.requestStatus === 200) {
                    LlmConfig.closeDeleteModal();
                    LlmConfig.loadConfigs();
                    LlmConfig.showSuccess('删除成功');
                } else {
                    alert('删除失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                console.error('Failed to delete config:', error);
                alert('删除失败: ' + error.message);
            });
        },
        
        showSuccess: function(message) {
            var toast = document.createElement('div');
            toast.className = 'toast toast-success';
            toast.innerHTML = '<i class="ri-check-line"></i> ' + message;
            document.body.appendChild(toast);
            setTimeout(function() {
                toast.remove();
            }, 3000);
        },
        
        showError: function(message) {
            var toast = document.createElement('div');
            toast.className = 'toast toast-error';
            toast.innerHTML = '<i class="ri-error-warning-line"></i> ' + message;
            document.body.appendChild(toast);
            setTimeout(function() {
                toast.remove();
            }, 3000);
        },
        
        commLogs: [],
        showJsonView: false,
        
        runTest: function() {
            var provider = document.getElementById('test-provider').value;
            var model = document.getElementById('test-model').value;
            var message = document.getElementById('test-message').value;
            var enableFunctionCalling = document.getElementById('test-function-calling').checked;
            var stream = document.getElementById('test-stream').checked;
            
            if (!provider || !model) {
                LlmConfig.showError('请选择 Provider 和 Model');
                return;
            }
            
            if (!message.trim()) {
                LlmConfig.showError('请输入测试消息');
                return;
            }
            
            LlmConfig.addLog('request', '发送请求', {
                provider: provider,
                model: model,
                message: message,
                functionCalling: enableFunctionCalling,
                stream: stream
            });
            
            var requestBody = {
                message: message,
                provider: provider,
                model: model,
                useFunctionCalling: enableFunctionCalling
            };
            
            var startTime = Date.now();
            
            fetch('/api/v1/llm/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestBody)
            })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                var duration = Date.now() - startTime;
                
                if (result && result.status === 'success' && result.data) {
                    LlmConfig.addLog('response', '响应成功 (' + duration + 'ms)', result.data);
                    
                    if (result.data.toolCalls && result.data.toolCalls.length > 0) {
                        result.data.toolCalls.forEach(function(toolCall) {
                            LlmConfig.addLog('tool-call', 'Function Call: ' + toolCall.name, toolCall);
                        });
                    }
                    
                    LlmConfig.showSuccess('测试成功');
                } else {
                    LlmConfig.addLog('error', '响应失败', result);
                    LlmConfig.showError('测试失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                var duration = Date.now() - startTime;
                LlmConfig.addLog('error', '请求异常 (' + duration + 'ms)', { error: error.message });
                LlmConfig.showError('请求异常: ' + error.message);
            });
        },
        
        addLog: function(type, title, data) {
            var container = document.getElementById('comm-logs');
            if (!container) return;
            
            var emptyMsg = container.querySelector('.log-empty');
            if (emptyMsg) emptyMsg.remove();
            
            var logEntry = document.createElement('div');
            logEntry.className = 'log-entry log-' + type;
            
            var time = new Date().toLocaleTimeString();
            var contentStr = typeof data === 'string' ? data : JSON.stringify(data, null, 2);
            
            logEntry.innerHTML = 
                '<div class="log-header">' +
                    '<span class="log-type">' + title + '</span>' +
                    '<span class="log-time">' + time + '</span>' +
                '</div>' +
                '<div class="log-content collapsed">' + LlmConfig.escapeHtml(contentStr) + '</div>' +
                '<div class="log-toggle" onclick="LlmConfig.toggleLogContent(this)">展开</div>';
            
            container.appendChild(logEntry);
            container.scrollTop = container.scrollHeight;
        },
        
        escapeHtml: function(text) {
            var div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        },
        
        toggleLogContent: function(toggleBtn) {
            var content = toggleBtn.previousElementSibling;
            if (content.classList.contains('collapsed')) {
                content.classList.remove('collapsed');
                toggleBtn.textContent = '收起';
            } else {
                content.classList.add('collapsed');
                toggleBtn.textContent = '展开';
            }
        },
        
        clearLogs: function() {
            var container = document.getElementById('comm-logs');
            if (!container) return;
            
            container.innerHTML = '<div class="log-empty">' +
                '<i class="ri-file-list-3-line"></i>' +
                '<p>暂无通讯日志，请先进行 LLM 测试</p>' +
            '</div>';
            
            LlmConfig.commLogs = [];
        },
        
        toggleLogView: function() {
            LlmConfig.showJsonView = !LlmConfig.showJsonView;
            var logs = document.querySelectorAll('.log-content');
            logs.forEach(function(log) {
                if (LlmConfig.showJsonView) {
                    log.style.whiteSpace = 'pre';
                } else {
                    log.style.whiteSpace = 'pre-wrap';
                }
            });
        },
        
        loadFunctionDefinitions: function() {
            fetch('/api/v1/llm/tools')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        LlmConfig.renderFunctionList(result.data);
                    } else {
                        LlmConfig.renderFunctionList([]);
                    }
                })
                .catch(function(error) {
                    console.error('[LlmConfig] 加载 Function 定义失败:', error);
                    LlmConfig.renderFunctionList([]);
                });
        },
        
        renderFunctionList: function(functions) {
            var container = document.getElementById('function-list');
            if (!container) return;
            
            if (!functions || functions.length === 0) {
                container.innerHTML = '<div class="log-empty">' +
                    '<i class="ri-function-line"></i>' +
                    '<p>暂无 Function 定义</p>' +
                '</div>';
                return;
            }
            
            var html = '';
            functions.forEach(function(fn) {
                var params = fn.parameters ? Object.keys(fn.parameters).join(', ') : '无参数';
                html += '<div class="function-item" onclick="LlmConfig.showFunctionDetail(\'' + fn.name + '\')">' +
                    '<div class="function-header">' +
                        '<span class="function-name">' + fn.name + '</span>' +
                        '<span class="function-type">' + (fn.type || 'function') + '</span>' +
                    '</div>' +
                    '<div class="function-desc">' + (fn.description || '暂无描述') + '</div>' +
                    '<div class="function-params">参数: <code>' + params + '</code></div>' +
                '</div>';
            });
            
            container.innerHTML = html;
        },
        
        showFunctionDetail: function(fnName) {
            fetch('/api/v1/llm/tools/' + fnName)
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                    var modal = document.getElementById('function-detail-modal');
                    if (!modal) {
                        modal = document.createElement('div');
                        modal.id = 'function-detail-modal';
                        modal.className = 'nx-modal';
                        modal.innerHTML = '<div class="nx-modal__content" style="max-width: 700px;">' +
                            '<div class="nx-modal__header">' +
                                '<h3 class="nx-modal__title"><i class="ri-code-line"></i> Function 详情</h3>' +
                                '<button class="nx-modal__close" onclick="LlmConfig.closeFunctionDetail()"><i class="ri-close-line"></i></button>' +
                            '</div>' +
                            '<div class="nx-modal__body">' +
                                '<div class="json-view" id="function-detail-content"></div>' +
                            '</div>' +
                        '</div>';
                        document.body.appendChild(modal);
                    }
                    
                    var content = document.getElementById('function-detail-content');
                    content.textContent = JSON.stringify(result.data, null, 2);
                    modal.classList.add('nx-modal--open');
                }
            })
            .catch(function(error) {
                console.error('[LlmConfig] 加载 Function 详情失败:', error);
            });
        },
        
        closeFunctionDetail: function() {
            var modal = document.getElementById('function-detail-modal');
            if (modal) modal.classList.remove('nx-modal--open');
        },
        
        populateTestSelects: function() {
            var providerSelect = document.getElementById('test-provider');
            var modelSelect = document.getElementById('test-model');
            
            if (!providerSelect || !modelSelect) return;
            
            providerSelect.innerHTML = '<option value="">选择Provider</option>';
            LlmConfig.providers.forEach(function(p) {
                var option = document.createElement('option');
                option.value = p.type;
                option.textContent = p.name;
                if (p.isCurrent) option.selected = true;
                providerSelect.appendChild(option);
            });
            
            providerSelect.onchange = function() {
                var providerType = this.value;
                modelSelect.innerHTML = '<option value="">选择模型</option>';
                
                var provider = LlmConfig.providers.find(function(p) { return p.type === providerType; });
                if (provider && provider.models) {
                    provider.models.forEach(function(m) {
                        var option = document.createElement('option');
                        option.value = m;
                        option.textContent = m;
                        modelSelect.appendChild(option);
                    });
                    
                    if (provider.modelDetails) {
                        var currentModel = provider.modelDetails.find(function(m) { return m.isCurrent; });
                        if (currentModel) {
                            modelSelect.value = currentModel.modelId;
                        }
                    }
                }
            };
            
            if (providerSelect.value) {
                providerSelect.onchange();
            }
        },
        
        loadDocs: function() {
            fetch('/api/v1/llm/docs')
                .then(function(response) { return response.json(); })
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        LlmConfig.renderDocTabs(result.data);
                    }
                })
                .catch(function(error) {
                    console.error('[LlmConfig] 加载文档失败:', error);
                });
        },
        
        renderDocTabs: function(docs) {
            var tabsContainer = document.getElementById('doc-tabs');
            var contentContainer = document.getElementById('doc-content');
            
            if (!tabsContainer || !contentContainer) return;
            
            tabsContainer.innerHTML = '';
            
            docs.forEach(function(doc, index) {
                var tab = document.createElement('div');
                tab.className = 'doc-tab' + (index === 0 ? ' active' : '');
                tab.textContent = doc.title;
                tab.onclick = function() {
                    document.querySelectorAll('.doc-tab').forEach(function(t) { t.classList.remove('active'); });
                    tab.classList.add('active');
                    LlmConfig.renderDocContent(doc);
                };
                tabsContainer.appendChild(tab);
                
                if (index === 0) {
                    LlmConfig.renderDocContent(doc);
                }
            });
        },
        
        renderDocContent: function(doc) {
            var container = document.getElementById('doc-content');
            if (!container) return;
            
            var html = '<div class="doc-markdown">' + LlmConfig.renderMarkdown(doc.content) + '</div>';
            container.innerHTML = html;
        },
        
        renderMarkdown: function(text) {
            if (!text) return '';
            
            var html = text;
            
            html = html.replace(/^### (.*$)/gim, '<h3>$1</h3>');
            html = html.replace(/^## (.*$)/gim, '<h2>$1</h2>');
            html = html.replace(/^# (.*$)/gim, '<h1>$1</h1>');
            
            html = html.replace(/\*\*(.*?)\*\*/gim, '<strong>$1</strong>');
            html = html.replace(/\*(.*?)\*/gim, '<em>$1</em>');
            
            html = html.replace(/`([^`]+)`/gim, '<code>$1</code>');
            html = html.replace(/```([\s\S]*?)```/gim, '<pre><code>$1</code></pre>');
            
            html = html.replace(/^\- (.*$)/gim, '<li>$1</li>');
            html = html.replace(/^\d+\. (.*$)/gim, '<li>$1</li>');
            
            html = html.replace(/\n\n/g, '</p><p>');
            html = '<p>' + html + '</p>';
            
            html = html.replace(/<li>/g, '<ul><li>');
            html = html.replace(/<\/li>/g, '</li></ul>');
            html = html.replace(/<\/ul><ul>/g, '');
            
            return html;
        }
    };

    window.LlmConfig = LlmConfig;
    
    document.addEventListener('DOMContentLoaded', function() {
        LlmConfig.init();
        LlmConfig.loadFunctionDefinitions();
        LlmConfig.loadDocs();
        setTimeout(function() {
            LlmConfig.populateTestSelects();
        }, 500);
    });

})();
