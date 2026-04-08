(function(global) {
'use strict';

var state = DiscoveryState;

var llmProvidersCache = null;
var llmModelsCache = {};

var DiscoveryInstallWizard = {
    openInstallWizard: function(cap) {
        console.log('[openInstallWizard] cap:', cap);
        console.log('[openInstallWizard] cap.skillForm:', cap.skillForm);
        console.log('[openInstallWizard] cap.sceneType:', cap.sceneType);
        var modal = document.getElementById('installModal');
        if (!modal) return;
        modal.classList.add('show');
        state.currentInstallCap = cap;
        state.currentInstallStep = 1;
        state.installSteps = DiscoveryInstallWizard.getInstallSteps(cap);
        console.log('[openInstallWizard] installSteps:', state.installSteps);
        DiscoveryInstallWizard.updateInstallWizard();
        modal.classList.add('show');
        DiscoveryInstallWizard.loadLLMProviders();
    },

    loadLLMProviders: function() {
        if (llmProvidersCache) {
            DiscoveryInstallWizard.renderLLMProviders(llmProvidersCache);
            return;
        }
        
        fetch('/api/v1/llm-providers/providers')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    llmProvidersCache = result.data;
                    DiscoveryInstallWizard.renderLLMProviders(result.data);
                }
            })
            .catch(function(error) {
                console.error('[loadLLMProviders] Failed to load providers:', error);
            });
    },

    renderLLMProviders: function(providers) {
        var container = document.getElementById('llmProviderList');
        if (!container) return;
        
        var html = '';
        providers.forEach(function(provider) {
            var selectedClass = provider.enabled ? 'selected' : '';
            var configuredIcon = provider.configured ? 'ri-check-line' : 'ri-alert-line';
            html += '<div class="provider-card ' + selectedClass + '" data-provider="' + provider.providerId + '" onclick="selectLLMProvider(\'' + provider.providerId + '\')">' +
                '<div class="provider-icon"><i class="ri-robot-line"></i></div>' +
                '<div class="provider-info">' +
                '<div class="provider-name">' + provider.name + '</div>' +
                '<div class="provider-status"><i class="' + configuredIcon + '"></i> ' + (provider.configured ? '已配置' : '未配置') + '</div>' +
                '</div></div>';
        });
        container.innerHTML = html;
        
        var enabledProvider = providers.find(function(p) { return p.enabled; });
        if (enabledProvider) {
            global.selectLLMProvider(enabledProvider.providerId);
        }
    },

    loadLLMModels: function(providerId) {
        if (llmModelsCache[providerId]) {
            DiscoveryInstallWizard.renderLLMModels(llmModelsCache[providerId]);
            return;
        }
        
        fetch('/api/v1/llm-providers/providers/' + providerId + '/models')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    llmModelsCache[providerId] = result.data;
                    DiscoveryInstallWizard.renderLLMModels(result.data);
                }
            })
            .catch(function(error) {
                console.error('[loadLLMModels] Failed to load models:', error);
            });
    },

    renderLLMModels: function(models) {
        var modelSelect = document.getElementById('llmModelSelect');
        if (!modelSelect) return;
        
        modelSelect.innerHTML = '';
        models.forEach(function(m) {
            var opt = document.createElement('option');
            opt.value = m.modelId;
            opt.textContent = m.displayName;
            opt.dataset.supportsFunctionCalling = m.supportsFunctionCalling;
            opt.dataset.supportsMultimodal = m.supportsMultimodal;
            opt.dataset.maxTokens = m.maxTokens;
            modelSelect.appendChild(opt);
        });
        
        var modelMeta = document.getElementById('modelMeta');
        if (modelMeta && models[0]) {
            var features = [];
            if (models[0].supportsFunctionCalling) features.push('支持Function Calling');
            if (models[0].supportsMultimodal) features.push('多模态');
            modelMeta.innerHTML = '<span class="meta-item"><i class="ri-text"></i> 最大Token: ' + (models[0].maxTokens || '-') + '</span>' +
                '<span class="meta-item"><i class="ri-flashlight-line"></i> ' + (features.join(', ') || '标准模型') + '</span>';
        }
        
        updateLLMConfigSummary();
    },

    getInstallSteps: function(cap) {
        var steps = [1];
        if (cap.skillForm === 'SCENE') {
            steps.push(2, 3);
        }
        if (cap.skillForm === 'SCENE' || cap.skillForm === 'DRIVER') {
            steps.push(4);
        }
        var hasDeps = cap.dependencies && cap.dependencies.length > 0;
        if (hasDeps) {
            steps.push(5);
        }
        if (DiscoveryInstallWizard.needsLLMConfig(cap)) {
            steps.push(6);
        }
        steps.push(7, 8);
        return steps;
    },

    needsLLMConfig: function(cap) {
        if (cap.capabilityCategory === 'llm' || cap.businessCategory === 'AI_ASSISTANT') {
            return true;
        }
        if (cap.skillForm === 'SCENE') {
            return true;
        }
        if (cap.dependencies && cap.dependencies.length > 0) {
            var llmDeps = cap.dependencies.filter(function(d) {
                var depStr = typeof d === 'string' ? d : (d.name || d.skillId || '');
                return depStr.indexOf('llm') >= 0 || depStr.indexOf('knowledge') >= 0 || depStr.indexOf('rag') >= 0;
            });
            if (llmDeps.length > 0) {
                return true;
            }
        }
        if (cap.metadata && cap.metadata.llmConfig) {
            return true;
        }
        return false;
    },

    updateInstallWizard: function() {
        var cap = state.currentInstallCap;
        var step = state.currentInstallStep;
        var steps = state.installSteps;
        var stepIndex = steps.indexOf(step);
        
        document.querySelectorAll('.stepper-item').forEach(function(s) { s.style.display = 'none'; });
        steps.forEach(function(stepNum, i) {
            var stepper = document.querySelector('.stepper-item[data-step="' + stepNum + '"]');
            if (stepper) {
                stepper.style.display = '';
                stepper.classList.toggle('active', i <= stepIndex);
                stepper.classList.toggle('completed', i < stepIndex);
            }
        });
        
        var allSteps = document.querySelectorAll('.wizard-step');
        allSteps.forEach(function(s) { s.classList.remove('active'); });
        var currentStepEl = document.getElementById('wizardStep' + step);
        if (currentStepEl) { currentStepEl.classList.add('active'); }
        
        var prevBtn = document.getElementById('installPrev');
        var nextBtn = document.getElementById('installNext');
        var doneBtn = document.getElementById('installDone');
        
        if (prevBtn) {
            prevBtn.style.display = stepIndex > 0 ? '' : 'none';
        }
        if (nextBtn) {
            nextBtn.style.display = stepIndex < steps.length - 1 ? '' : 'none';
        }
        if (doneBtn) {
            doneBtn.style.display = step === 8 ? '' : 'none';
        }
        
        DiscoveryInstallWizard.renderStepContent(step);
    },

    renderStepContent: function(step) {
        var cap = state.currentInstallCap;
        if (!cap) return;
        
        switch (step) {
            case 1:
                DiscoveryInstallWizard.renderPreviewStep(cap);
                break;
            case 2:
                DiscoveryInstallWizard.renderRolesStep(cap);
                break;
            case 3:
                DiscoveryInstallWizard.renderParticipantsStep(cap);
                break;
            case 4:
                DiscoveryInstallWizard.renderDriverConditionsStep(cap);
                break;
            case 5:
                DiscoveryInstallWizard.renderDependenciesStep(cap);
                break;
            case 6:
                DiscoveryInstallWizard.renderLLMConfigStep(cap);
                break;
            case 7:
                DiscoveryInstallWizard.renderInstallProgressStep(cap);
                break;
            case 8:
                DiscoveryInstallWizard.renderCompleteStep(cap);
                break;
        }
    },

    renderPreviewStep: function(cap) {
        var featureList = document.getElementById('featureList');
        if (featureList) {
            var features = cap.tags || cap.capabilities || [];
            if (features.length === 0 && cap.metadata && cap.metadata.tags) {
                features = cap.metadata.tags;
            }
            if (features.length === 0) {
                features = ['核心功能', '场景支持', '数据管理'];
            }
            var html = '';
            features.forEach(function(f) {
                var featureName = typeof f === 'string' ? f : (f.name || f.id || '功能');
                html += '<div class="feature-item"><i class="ri-check-line"></i> ' + featureName + '</div>';
            });
            featureList.innerHTML = html;
        }
        
        var rolePreviewList = document.getElementById('rolePreviewList');
        if (rolePreviewList) {
            var roles = cap.roles || cap.participants || [];
            if (roles.length === 0 && cap.metadata && cap.metadata.roles) {
                roles = cap.metadata.roles;
            }
            if (roles.length === 0 && cap.skillForm === 'SCENE') {
                roles = [{ name: '主导者', desc: '场景启动者' }, { name: '参与者', desc: '场景参与者' }];
            }
            var html = '';
            roles.forEach(function(r) {
                var name = typeof r === 'string' ? r : (r.name || r.roleName || '角色');
                html += '<div class="role-preview-item"><i class="ri-user-line"></i><span>' + name + '</span></div>';
            });
            if (html === '') {
                html = '<div class="role-preview-item"><i class="ri-user-line"></i><span>默认角色</span></div>';
            }
            rolePreviewList.innerHTML = html;
        }
        
        var dependencyPreviewList = document.getElementById('dependencyPreviewList');
        if (dependencyPreviewList) {
            var deps = cap.dependencies || [];
            var html = '';
            deps.forEach(function(d) {
                var depName = typeof d === 'string' ? d : (d.name || d.skillId || d);
                var depInstalled = typeof d === 'object' ? (d.installed === true || d.installed === 'true') : false;
                var iconClass = depInstalled ? 'ri-checkbox-circle-line' : 'ri-time-line';
                var itemClass = depInstalled ? 'installed' : 'pending';
                html += '<div class="dependency-preview-item ' + itemClass + '"><i class="' + iconClass + '"></i><span>' + depName + '</span></div>';
            });
            if (html === '') {
                html = '<div class="dependency-preview-item"><i class="ri-checkbox-blank-circle-line"></i><span>无依赖项</span></div>';
            }
            dependencyPreviewList.innerHTML = html;
        }
    },

    renderRolesStep: function(cap) {
        var roleSelectionList = document.getElementById('roleSelectionList');
        if (!roleSelectionList) return;
        
        var roles = cap.roles || cap.participants || [];
        if (roles.length === 0 && cap.metadata && cap.metadata.roles) {
            roles = cap.metadata.roles;
        }
        if (roles.length === 0 && cap.skillForm === 'SCENE') {
            roles = [
                { id: 'leader', name: '主导者', desc: '拥有场景启动权限，可配置参与者' },
                { id: 'participant', name: '参与者', desc: '参与场景协作，可执行场景操作' }
            ];
        }
        
        var html = '';
        roles.forEach(function(r, i) {
            var id = r.id || r.roleId || ('role_' + i);
            var name = typeof r === 'string' ? r : (r.name || r.roleName || '角色');
            var desc = r.desc || r.description || '';
            var checked = i === 0 ? 'checked' : '';
            html += '<div class="role-selection-item">' +
                '<label class="role-radio">' +
                '<input type="radio" name="selectedRole" value="' + id + '" ' + checked + '>' +
                '<span class="radio-mark"></span>' +
                '</label>' +
                '<div class="role-info">' +
                '<div class="role-name">' + name + '</div>' +
                (desc ? '<div class="role-desc">' + desc + '</div>' : '') +
                '</div></div>';
        });
        if (html === '') {
            html = '<div class="role-selection-item"><div class="role-info"><div class="role-name">默认角色</div></div>';
        }
        roleSelectionList.innerHTML = html;
    },

    renderParticipantsStep: function(cap) {
        var leaderInput = document.getElementById('leaderInput');
        if (leaderInput && !leaderInput.value) {
            ApiClient.get('/api/v1/user/current')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        var user = result.data;
                        leaderInput.value = user.name || user.username || user.id || '当前用户';
                    } else {
                        leaderInput.value = '当前用户';
                    }
                })
                .catch(function(error) {
                    console.error('[renderParticipantsStep] Failed to get current user:', error);
                    leaderInput.value = '当前用户';
                });
        }
        var collaboratorList = document.getElementById('collaboratorList');
        if (collaboratorList && collaboratorList.children.length === 0) {
            collaboratorList.innerHTML = '<div class="participant-empty">暂无协作者，点击下方按钮添加</div>';
        }
        var pushType = document.getElementById('pushType');
        if (pushType && !pushType.value) {
            pushType.value = 'SHARE';
        }
        if (pushType) {
            pushType.onchange = function() {
                var hint = document.getElementById('pushHint');
                if (!hint) return;
                switch (pushType.value) {
                    case 'SHARE':
                        hint.textContent = '分享给参与者，对方可选择是否接受';
                        break;
                    case 'INVITE':
                        hint.textContent = '邀请参与者，对方需确认接受';
                        break;
                    case 'DELEGATE':
                        hint.textContent = '委派给参与者，对方将被强制使用';
                        break;
                }
            };
        }
    },

    renderDriverConditionsStep: function(cap) {
        var driverConditionList = document.getElementById('driverConditionList');
        if (!driverConditionList) return;
        
        var conditions = cap.driverConditions || [];
        
        if (conditions.length === 0) {
            conditions = [
                { id: 'manual', name: '手动触发', desc: '用户手动启动场景', icon: 'ri-hand-coin-line', type: 'MANUAL' },
                { id: 'schedule', name: '定时触发', desc: '按计划时间自动执行', icon: 'ri-timer-line', type: 'SCHEDULE' },
                { id: 'event', name: '事件触发', desc: '当特定事件发生时执行', icon: 'ri-flashlight-line', type: 'EVENT' }
            ];
            
            if (cap.metadata && cap.metadata.driverConditions) {
                conditions = cap.metadata.driverConditions;
            }
        }
        
        var html = '';
        conditions.forEach(function(c, i) {
            var id = c.id || c.conditionId || ('condition_' + i);
            var name = c.name || c.conditionName || '触发条件';
            var desc = c.desc || c.description || '';
            var icon = c.icon || 'ri-flashlight-line';
            var type = c.type || c.triggerType || 'MANUAL';
            var checked = i === 0 ? 'checked' : '';
            html += '<div class="driver-condition-item">' +
                '<label class="condition-checkbox">' +
                '<input type="checkbox" name="driverCondition" value="' + type + '" ' + checked + '>' +
                '<span class="checkbox-mark"></span>' +
                '</label>' +
                '<div class="condition-icon"><i class="' + icon + '"></i></div>' +
                '<div class="condition-info">' +
                '<div class="condition-name">' + name + '</div>' +
                (desc ? '<div class="condition-desc">' + desc + '</div>' : '') +
                '</div></div>';
        });
        driverConditionList.innerHTML = html;
    },

    renderDependenciesStep: function(cap) {
        var dependencyList = document.getElementById('dependencyList');
        if (!dependencyList) return;
        
        var deps = cap.dependencies || [];
        var html = '';
        if (deps.length === 0) {
            html = '<div class="dependency-empty">' +
                '<i class="ri-checkbox-circle-line"></i>' +
                '<span>无依赖项，可直接安装</span></div>';
        } else {
            deps.forEach(function(d) {
                var depName = typeof d === 'string' ? d : (d.name || d.skillId || d);
                var depInstalled = typeof d === 'object' ? (d.installed === true || d.installed === 'true') : false;
                var statusClass = depInstalled ? 'installed' : 'pending';
                var statusText = depInstalled ? '已安装' : '待安装';
                var iconClass = depInstalled ? 'ri-checkbox-circle-line' : 'ri-time-line';
                html += '<div class="dependency-item ' + statusClass + '">' +
                    '<i class="' + iconClass + '"></i>' +
                    '<span class="dependency-name">' + depName + '</span>' +
                    '<span class="dependency-status">' + statusText + '</span></div>';
            });
        }
        dependencyList.innerHTML = html;
    },

    renderLLMConfigStep: function(cap) {
        var llmProviderList = document.getElementById('llmProviderList');
        if (llmProviderList) {
            var items = llmProviderList.querySelectorAll('.llm-provider-item');
            items.forEach(function(item) {
                item.classList.remove('selected');
            });
            var firstItem = llmProviderList.querySelector('.llm-provider-item');
            if (firstItem) {
                firstItem.classList.add('selected');
            }
        }
        
        var providerCard = document.querySelector('.provider-card.selected');
        if (!providerCard) {
            var firstCard = document.querySelector('.provider-card');
            if (firstCard) {
                firstCard.classList.add('selected');
            }
        }
        
        var systemPrompt = document.getElementById('systemPrompt');
        if (systemPrompt) {
            var promptText = '你是' + cap.name + '场景的AI助手。';
            if (cap.description) {
                promptText += '\n\n场景描述：' + cap.description;
            }
            if (cap.metadata && cap.metadata.systemPrompt) {
                promptText = cap.metadata.systemPrompt;
            }
            systemPrompt.value = promptText;
        }
        
        if (cap.metadata && cap.metadata.llmConfig) {
            var llmConfig = cap.metadata.llmConfig;
            if (llmConfig.provider) {
                selectLLMProvider(llmConfig.provider);
            }
            if (llmConfig.model) {
                var modelSelect = document.getElementById('llmModelSelect');
                if (modelSelect) {
                    modelSelect.value = llmConfig.model;
                }
            }
            if (llmConfig.parameters) {
                if (llmConfig.parameters.temperature !== undefined) {
                    var tempSlider = document.getElementById('temperature');
                    var tempDisplay = document.getElementById('temperatureValue');
                    if (tempSlider && tempDisplay) {
                        tempSlider.value = llmConfig.parameters.temperature * 100;
                        tempDisplay.textContent = llmConfig.parameters.temperature.toFixed(1);
                    }
                }
                if (llmConfig.parameters.maxTokens !== undefined) {
                    var maxTokensInput = document.getElementById('maxTokens');
                    var maxTokensDisplay = document.getElementById('maxTokensValue');
                    if (maxTokensInput && maxTokensDisplay) {
                        maxTokensInput.value = llmConfig.parameters.maxTokens;
                        maxTokensDisplay.textContent = llmConfig.parameters.maxTokens;
                    }
                }
            }
        }
        
        updateLLMConfigSummary();
    },

    renderInstallProgressStep: function(cap) {
        var installProgress = document.getElementById('installProgress');
        var installStepsEl = document.getElementById('installSteps');
        if (installProgress) {
            installProgress.style.width = '30%';
        }
        if (installStepsEl) {
            var steps = [
                { name: '检查依赖', status: 'done' },
                { name: '下载资源', status: 'running' },
                { name: '注册能力', status: 'pending' },
                { name: '配置权限', status: 'pending' },
                { name: '完成安装', status: 'pending' }
            ];
            var html = '';
            steps.forEach(function(s) {
                var iconClass = s.status === 'done' ? 'ri-checkbox-circle-line' : 
                               (s.status === 'running' ? 'ri-loader-4-line' : 'ri-checkbox-blank-circle-line');
                html += '<div class="install-step-item ' + s.status + '">' +
                    '<i class="' + iconClass + '"></i>' +
                    '<span>' + s.name + '</span></div>';
            });
            installStepsEl.innerHTML = html;
        }
    },

    renderCompleteStep: function(cap) {
        var completeName = document.getElementById('completeName');
        var completeRole = document.getElementById('completeRole');
        var completeParticipants = document.getElementById('completeParticipants');
        
        if (completeName) completeName.textContent = cap.name;
        if (completeRole) {
            var selectedRole = document.querySelector('input[name="selectedRole"]:checked');
            var roleText = '主导者';
            if (selectedRole) {
                var parentEl = selectedRole.parentElement;
                if (parentEl && parentEl.nextElementSibling) {
                    var roleNameEl = parentEl.nextElementSibling.querySelector('.role-name');
                    roleText = roleNameEl ? roleNameEl.textContent : selectedRole.value;
                } else {
                    roleText = selectedRole.value;
                }
            }
            completeRole.textContent = roleText;
        }
        if (completeParticipants) {
            var leaderInput = document.getElementById('leaderInput');
            var leader = leaderInput ? leaderInput.value : '当前用户';
            var collaboratorList = document.getElementById('collaboratorList');
            var collaborators = [];
            if (collaboratorList) {
                var tags = collaboratorList.querySelectorAll('.participant-tag span');
                tags.forEach(function(tag) {
                    collaborators.push(tag.textContent);
                });
            }
            var participantsText = leader;
            if (collaborators.length > 0) {
                participantsText += ', ' + collaborators.join(', ');
            }
            completeParticipants.textContent = participantsText;
        }
        
        var completeMenuPreview = document.getElementById('completeMenuPreview');
        if (completeMenuPreview) {
            var categoryInfo = DiscoveryUtils.getCategoryInfo(cap.skillForm, cap.sceneType);
            completeMenuPreview.innerHTML = '<div class="menu-preview-item">' +
                '<i class="' + categoryInfo.icon + '"></i>' +
                '<span>' + cap.name + '</span></div>';
        }
        
        var completeNotifyStatus = document.getElementById('completeNotifyStatus');
        if (completeNotifyStatus) {
            var pushType = document.getElementById('pushType');
            var pushTypeValue = pushType ? pushType.value : 'SHARE';
            var notifyText = '场景已成功安装并激活';
            if (pushTypeValue === 'SHARE') {
                notifyText = '已分享给参与者，对方可选择是否接受';
            } else if (pushTypeValue === 'INVITE') {
                notifyText = '已发送邀请通知，等待参与者确认';
            } else if (pushTypeValue === 'DELEGATE') {
                notifyText = '已委派给参与者，对方将被强制使用';
            }
            completeNotifyStatus.innerHTML = '<div class="notify-item success">' +
                '<i class="ri-checkbox-circle-line"></i>' +
                '<span>' + notifyText + '</span></div>';
        }
        
        DiscoveryInstallExec.saveInstallConfig(cap);
        DiscoveryInstallExec.addMenuForInstalledCapability(cap);
    },

    nextInstallStep: function() {
        var stepIndex = state.installSteps.indexOf(state.currentInstallStep);
        if (stepIndex < state.installSteps.length - 1) {
            state.currentInstallStep = state.installSteps[stepIndex + 1];
            if (state.currentInstallStep === 7) {
                DiscoveryInstallExec.executeInstall();
            }
            DiscoveryInstallWizard.updateInstallWizard();
        }
    },

    prevInstallStep: function() {
        var stepIndex = state.installSteps.indexOf(state.currentInstallStep);
        if (stepIndex > 0) {
            state.currentInstallStep = state.installSteps[stepIndex - 1];
            DiscoveryInstallWizard.updateInstallWizard();
        }
    }
};

global.DiscoveryInstallWizard = DiscoveryInstallWizard;

global.nextStep = function() { DiscoveryInstallWizard.nextInstallStep(); };
global.prevStep = function() { DiscoveryInstallWizard.prevInstallStep(); };
global.closeInstall = function() {
    var modal = document.getElementById('installModal');
    if (modal) { modal.classList.remove('show'); }
};
global.cancelInstall = function() { global.closeInstall(); };
global.goToCapability = function() { window.location.href = 'my-capabilities.html'; };

global.selectLLMProvider = function(provider) {
    var cards = document.querySelectorAll('.provider-card');
    cards.forEach(function(card) {
        card.classList.remove('selected');
        if (card.dataset.provider === provider) {
            card.classList.add('selected');
        }
    });
    
    DiscoveryInstallWizard.loadLLMModels(provider);
}

global.onModelChange = function() {
    var modelSelect = document.getElementById('llmModelSelect');
    var selectedOption = modelSelect.options[modelSelect.selectedIndex];
    console.log('[onModelChange] Selected model:', selectedOption.value);
    updateLLMConfigSummary();
};

global.updateParamDisplay = function(param) {
    if (param === 'temperature') {
        var slider = document.getElementById('temperature');
        var display = document.getElementById('temperatureValue');
        if (slider && display) {
            display.textContent = (slider.value / 100).toFixed(1);
        }
    } else if (param === 'topP') {
        var slider = document.getElementById('topP');
        var display = document.getElementById('topPValue');
        if (slider && display) {
            display.textContent = (slider.value / 100).toFixed(1);
        }
    } else if (param === 'freqPenalty') {
        var slider = document.getElementById('freqPenalty');
        var display = document.getElementById('freqPenaltyValue');
        if (slider && display) {
            display.textContent = (slider.value / 10).toFixed(1);
        }
    } else if (param === 'maxTokens') {
        var input = document.getElementById('maxTokens');
        var display = document.getElementById('maxTokensValue');
        if (input && display) {
            display.textContent = input.value;
        }
    }
}

global.resetModelParams = function() {
    document.getElementById('temperature').value = 70;
    document.getElementById('temperatureValue').textContent = '0.7';
    document.getElementById('maxTokens').value = 4096;
    document.getElementById('maxTokensValue').textContent = '4096';
    document.getElementById('topP').value = 90;
    document.getElementById('topPValue').textContent = '0.9';
    document.getElementById('freqPenalty').value = 0;
    document.getElementById('freqPenaltyValue').textContent = '0';
}

global.toggleFunctionCallTools = function() {
    var enabled = document.getElementById('enableFunctionCall').checked;
    var toolsPanel = document.getElementById('functionCallTools');
    if (toolsPanel) {
        toolsPanel.style.opacity = enabled ? '1' : '0.5';
        toolsPanel.style.pointerEvents = enabled ? 'auto' : 'none';
    }
    updateLLMConfigSummary();
}

global.toggleKnowledgeConfig = function() {
    var enabled = document.getElementById('enableKnowledge').checked;
    var panel = document.getElementById('knowledgeConfigPanel');
    if (panel) {
        panel.style.display = enabled ? 'block' : 'none';
    }
}

global.addKnowledgeBase = function() {
    var container = document.getElementById('knowledgeBases');
    if (!container) return;
    
    var empty = container.querySelector('.knowledge-base-empty');
    if (empty) {
        container.innerHTML = '';
    }
    
    var item = document.createElement('div');
    item.className = 'knowledge-base-item';
    item.innerHTML = 
        '<div class="kb-select">' +
        '<select class="form-select" name="kbSelect">' +
        '<option value="">选择知识库...</option>' +
        '<option value="kb-product">产品文档库</option>' +
        '<option value="kb-faq">常见问题库</option>' +
        '<option value="kb-manual">操作手册库</option>' +
        '</select>' +
        '</div>' +
        '<button type="button" class="nx-btn nx-btn--ghost nx-btn--sm" onclick="this.parentElement.remove()">' +
        '<i class="ri-close-line"></i>' +
        '</button>';
    container.appendChild(item);
}

global.generatePrompt = function() {
    var cap = state.currentInstallCap;
    if (!cap) return;
    var prompt = '你是' + cap.name + '场景的AI助手。\\n\n';
    prompt += '场景描述:' + (cap.description || '暂无描述') + '\n\n';
    prompt += '你的职责:\n';
    prompt += '1. 协助用户理解和使用场景功能\n';
    prompt += '2. 提供场景相关的建议和指导\n';
    prompt += '3. 弽助用户完成场景内的任务\n';
    document.getElementById('systemPrompt').value = prompt;
}

global.resetPrompt = function() {
    var cap = state.currentInstallCap;
    if (cap) {
        document.getElementById('systemPrompt').value = '你是' + cap.name + '场景的AI助手。' + (cap.description || '');
    }
}

global.selectLeader = function() {
    DiscoveryUserSelector.show('leader', '选择主导者', function(user) {
        var leaderInput = document.getElementById('leaderInput');
        if (leaderInput) {
            leaderInput.value = user.name || user.username || user;
        }
    });
};

global.addCollaborator = function() {
    DiscoveryUserSelector.show('collaborator', '选择协作者', function(user) {
        var list = document.getElementById('collaboratorList');
        if (!list) return;
        
        var empty = list.querySelector('.participant-empty');
        if (empty) {
            list.innerHTML = '';
        }
        
        var userName = user.name || user.username || user;
        var existingTags = list.querySelectorAll('.participant-tag span');
        for (var i = 0; i < existingTags.length; i++) {
            if (existingTags[i].textContent === userName) {
                return;
            }
        }
        
        var item = document.createElement('div');
        item.className = 'participant-tag';
        item.innerHTML = '<span>' + userName + '</span>' +
            '<button class="participant-remove" onclick="this.parentElement.remove()">' +
            '<i class="ri-close-line"></i></button>';
        list.appendChild(item);
    });
}

global.closeUserSelector = function() {
    var modal = document.getElementById('userSelectorModal');
    if (modal) {
        modal.style.display = 'none';
    }
}
global.installCapability = function(skillId) {
    var cap = state.discoveredCapabilities.find(function(c) { return c.id === skillId || c.skillId === skillId; });
    if (cap) {
        DiscoveryInstallWizard.openInstallWizard(cap);
    } else {
        console.error('[installCapability] Capability not found:', skillId);
    }
};
global.selectUser = function(el) {
    var userJson = el.dataset.user;
    if (userJson) {
        var user = JSON.parse(userJson);
        if (window._userSelectorCallback) {
            window._userSelectorCallback(user);
        }
    }
    global.closeUserSelector();
}
global.searchUsers = function(keyword) {
    DiscoveryUserSelector.loadUsers(keyword);
}
function updateLLMConfigSummary() {
    var summary = document.getElementById('llmConfigSummary');
    if (!summary) return;
    
    var providerCard = document.querySelector('.provider-card.selected');
    var providerName = providerCard ? providerCard.querySelector('.provider-name').textContent : 'DeepSeek';
    
    var modelSelect = document.getElementById('llmModelSelect');
    var modelName = modelSelect ? modelSelect.value : 'deepseek-chat';
    
    var tools = document.querySelectorAll('input[name="fcTool"]:checked');
    var toolCount = tools.length;
    
    summary.innerHTML = 
        '<span class="summary-item"><i class="ri-cloud-line"></i> ' + providerName + '</span>' +
        '<span class="summary-item"><i class="ri-cpu-line"></i> ' + modelName + '</span>' +
        '<span class="summary-item"><i class="ri-tools-line"></i> ' + toolCount + ' 工具</span>';
}

})(typeof window !== 'undefined' ? window : this);
