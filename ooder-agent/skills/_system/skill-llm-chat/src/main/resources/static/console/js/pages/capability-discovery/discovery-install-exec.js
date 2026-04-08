(function(global) {
'use strict';

var state = DiscoveryState;

var DiscoveryInstallExec = {
    executeInstall: function() {
        var cap = state.currentInstallCap;
        if (!cap) return;
        DiscoveryUtils.addLog('info', '开始安装: ' + cap.name);
        
        var installProgress = document.getElementById('installProgress');
        var installStepsEl = document.getElementById('installSteps');
        
        var progressSteps = [
            { name: '检查依赖', progress: 20 },
            { name: '下载资源', progress: 40 },
            { name: '注册能力', progress: 60 },
            { name: '配置权限', progress: 80 },
            { name: '完成安装', progress: 100 }
        ];
        
        var stepIndex = 0;
        function updateProgressUI(progress, stepName) {
            if (installProgress) {
                installProgress.style.width = progress + '%';
                installProgress.style.background = '';
            }
            if (installStepsEl) {
                var items = installStepsEl.querySelectorAll('.install-step-item');
                items.forEach(function(item, i) {
                    item.classList.remove('done', 'running', 'pending');
                    if (i < stepIndex) {
                        item.classList.add('done');
                    } else if (i === stepIndex) {
                        item.classList.add('running');
                    } else {
                        item.classList.add('pending');
                    }
                    var icon = item.querySelector('i');
                    if (icon) {
                        if (i < stepIndex) {
                            icon.className = 'ri-checkbox-circle-line';
                        } else if (i === stepIndex) {
                            icon.className = 'ri-loader-4-line';
                        } else {
                            icon.className = 'ri-checkbox-blank-circle-line';
                        }
                    }
                });
            }
        }
        
        var installRequest = DiscoveryInstallExec.collectInstallConfig(cap);
        
        updateProgressUI(10, '初始化');
        
        ApiClient.post('/api/v1/discovery/install', installRequest)
            .then(function(result) {
                console.log('[executeInstall] Install result:', result);
                if (result.status === 'success') {
                    var data = result.data || {};
                    
                    var progress = data.progress || 100;
                    var currentStep = data.currentStep || '完成安装';
                    var status = data.status || 'installed';
                    
                    if (status === 'installed' || status === 'success') {
                        stepIndex = progressSteps.length - 1;
                        updateProgressUI(100, '完成安装');
                        
                        setTimeout(function() {
                            DiscoveryUtils.addLog('success', '安装成功: ' + cap.name);
                            cap.installed = true;
                            DiscoveryInstallExec.markAsInstalled(cap.id);
                            DiscoveryResult.renderResults();
                            DiscoveryInstallExec.startActivation(cap, installRequest);
                        }, 500);
                    } else if (status === 'failed') {
                        DiscoveryUtils.addLog('error', '安装失败: ' + (data.message || '未知错误'));
                        if (installProgress) {
                            installProgress.style.width = '0%';
                            installProgress.style.background = '#f5222d';
                        }
                    } else {
                        stepIndex = progressSteps.findIndex(function(s) { return s.name === currentStep; });
                        if (stepIndex < 0) stepIndex = progressSteps.length - 1;
                        
                        updateProgressUI(progress, currentStep);
                        
                        setTimeout(function() {
                            DiscoveryUtils.addLog('success', '安装成功: ' + cap.name);
                            cap.installed = true;
                            DiscoveryResult.renderResults();
                            DiscoveryInstallWizard.nextInstallStep();
                        }, 500);
                    }
                } else {
                    DiscoveryUtils.addLog('error', '安装失败: ' + (result.message || '未知错误'));
                    if (installProgress) {
                        installProgress.style.width = '0%';
                        installProgress.style.background = '#f5222d';
                    }
                }
            })
            .catch(function(error) {
                console.error('[executeInstall] Install error:', error);
                DiscoveryUtils.addLog('error', '安装失败: ' + error.message);
                if (installProgress) {
                    installProgress.style.width = '0%';
                    installProgress.style.background = '#f5222d';
                }
            });
    },

    collectInstallConfig: function(cap) {
        var request = {
            skillId: cap.skillId || cap.id,
            name: cap.name,
            type: cap.skillForm || cap.type,
            description: cap.description,
            source: cap.source || 'LOCAL'
        };
        
        if (cap.repoUrl) {
            request.repoUrl = cap.repoUrl;
        }
        
        var selectedRoleEl = document.querySelector('input[name="selectedRole"]:checked');
        if (selectedRoleEl) {
            request.selectedRole = selectedRoleEl.value;
        }
        
        var leaderInput = document.getElementById('leaderInput');
        var pushTypeEl = document.getElementById('pushType');
        var collaboratorList = document.getElementById('collaboratorList');
        
        if (leaderInput || pushTypeEl || collaboratorList) {
            request.participants = {
                leader: leaderInput ? leaderInput.value : 'current_user',
                pushType: pushTypeEl ? pushTypeEl.value : 'SHARE',
                collaborators: []
            };
            
            if (collaboratorList) {
                var tags = collaboratorList.querySelectorAll('.participant-tag span');
                tags.forEach(function(tag) {
                    request.participants.collaborators.push(tag.textContent);
                });
            }
        }
        
        var driverConditionEls = document.querySelectorAll('input[name="driverCondition"]:checked');
        if (driverConditionEls.length > 0) {
            request.driverConditions = [];
            driverConditionEls.forEach(function(el) {
                request.driverConditions.push(el.value);
            });
        }
        
        var llmProvider = document.querySelector('.provider-card.selected');
        var modelSelect = document.getElementById('llmModelSelect');
        var systemPrompt = document.getElementById('systemPrompt');
        var enableFunctionCall = document.getElementById('enableFunctionCall');
        var enableKnowledge = document.getElementById('enableKnowledge');
        
        if (llmProvider || modelSelect) {
            request.llmConfig = {
                provider: llmProvider ? llmProvider.dataset.provider : 'deepseek',
                model: modelSelect ? modelSelect.value : 'deepseek-chat',
                systemPrompt: systemPrompt ? systemPrompt.value : '',
                enableFunctionCall: enableFunctionCall ? enableFunctionCall.checked : true,
                functionTools: [],
                knowledge: null,
                parameters: {}
            };
            
            var temperatureEl = document.getElementById('temperature');
            var maxTokensEl = document.getElementById('maxTokens');
            var topPEl = document.getElementById('topP');
            var freqPenaltyEl = document.getElementById('freqPenalty');
            
            if (temperatureEl) {
                request.llmConfig.parameters.temperature = parseInt(temperatureEl.value) / 100;
            }
            if (maxTokensEl) {
                request.llmConfig.parameters.maxTokens = parseInt(maxTokensEl.value);
            }
            if (topPEl) {
                request.llmConfig.parameters.topP = parseInt(topPEl.value) / 100;
            }
            if (freqPenaltyEl) {
                request.llmConfig.parameters.frequencyPenalty = parseInt(freqPenaltyEl.value) / 10;
            }
            
            var fcToolEls = document.querySelectorAll('input[name="fcTool"]:checked');
            fcToolEls.forEach(function(el) {
                request.llmConfig.functionTools.push(el.value);
            });
            
            if (enableKnowledge && enableKnowledge.checked) {
                var ragTopK = document.getElementById('ragTopK');
                var ragThreshold = document.getElementById('ragThreshold');
                var ragRerank = document.getElementById('ragRerank');
                var kbSelects = document.querySelectorAll('select[name="kbSelect"]');
                
                request.llmConfig.knowledge = {
                    enabled: true,
                    topK: ragTopK ? parseInt(ragTopK.value) : 5,
                    scoreThreshold: ragThreshold ? parseFloat(ragThreshold.value) : 0.7,
                    bases: []
                };
                
                kbSelects.forEach(function(select) {
                    if (select.value) {
                        request.llmConfig.knowledge.bases.push(select.value);
                    }
                });
            }
        }
        
        console.log('[collectInstallConfig] Install request:', request);
        return request;
    },

    saveInstallConfig: function(cap) {
        var sceneGroupId = cap.skillId || cap.id || ('sg-' + Date.now());
        var installConfig = DiscoveryInstallExec.collectInstallConfig(cap);
        
        if (installConfig.llmConfig) {
            ApiClient.put('/api/v1/scene-groups/' + sceneGroupId + '/llm/config', installConfig.llmConfig)
                .then(function(result) {
                    if (result.status === 'success') {
                        console.log('[saveInstallConfig] LLM config saved successfully');
                        DiscoveryUtils.addLog('success', 'LLM配置保存成功');
                    } else {
                        console.error('[saveInstallConfig] Failed to save LLM config:', result.message);
                    }
                })
                .catch(function(error) {
                    console.error('[saveInstallConfig] Failed to save LLM config:', error);
                });
        }
        
        if (installConfig.participants && installConfig.participants.collaborators && installConfig.participants.collaborators.length > 0) {
            installConfig.participants.collaborators.forEach(function(collaborator) {
                ApiClient.post('/api/v1/scene-groups/' + sceneGroupId + '/participants', {
                    userId: collaborator,
                    role: 'collaborator',
                    pushType: installConfig.participants.pushType || 'SHARE'
                })
                    .then(function(result) {
                        if (result.status === 'success') {
                            console.log('[saveInstallConfig] Participant added:', collaborator);
                        }
                    })
                    .catch(function(error) {
                        console.error('[saveInstallConfig] Failed to add participant:', error);
                    });
            });
        }
        
        if (installConfig.driverConditions && installConfig.driverConditions.length > 0) {
            ApiClient.post('/api/v1/scene-groups/' + sceneGroupId + '/driver-conditions', {
                conditions: installConfig.driverConditions
            })
                .then(function(result) {
                    if (result.status === 'success') {
                        console.log('[saveInstallConfig] Driver conditions saved');
                    }
                })
                .catch(function(error) {
                    console.error('[saveInstallConfig] Failed to save driver conditions:', error);
                });
        }
        
        if (installConfig.llmConfig && installConfig.llmConfig.knowledge && installConfig.llmConfig.knowledge.bases) {
            installConfig.llmConfig.knowledge.bases.forEach(function(kb) {
                ApiClient.post('/api/v1/scene-groups/' + sceneGroupId + '/knowledge', {
                    kbId: kb.id || kb,
                    layer: kb.layer || 'GENERAL'
                })
                    .then(function(result) {
                        if (result.status === 'success') {
                            console.log('[saveInstallConfig] Knowledge base bound:', kb);
                        }
                    })
                    .catch(function(error) {
                        console.error('[saveInstallConfig] Failed to bind knowledge:', error);
                    });
            });
        }
        
        console.log('[saveInstallConfig] All configurations saved for scene group:', sceneGroupId);
    },

    addMenuForInstalledCapability: function(cap) {
        var currentRole = localStorage.getItem('currentRole') || 'personal';
        var categoryInfo = DiscoveryUtils.getCategoryInfo(cap.skillForm, cap.sceneType);
        var menuIcon = cap.icon || categoryInfo.icon;
        
        var menuItem = {
            id: 'menu-' + (cap.skillId || cap.id),
            name: cap.name,
            url: '/console/pages/capability-detail.html?id=' + (cap.skillId || cap.id),
            icon: menuIcon,
            sort: 100,
            category: cap.businessCategory || cap.category,
            skillForm: cap.skillForm,
            sceneType: cap.sceneType
        };
        
        ApiClient.post('/api/v1/role-management/roles/' + currentRole + '/menus', menuItem)
            .then(function(result) {
                if (result.status === 'success') {
                    console.log('[addMenuForInstalledCapability] Menu added successfully:', cap.name);
                    DiscoveryUtils.addLog('success', '菜单添加成功: ' + cap.name);
                    
                    if (window.Menu && window.Menu.refresh) {
                        window.Menu.refresh();
                    }
                } else {
                    console.error('[addMenuForInstalledCapability] Failed to add menu:', result.message);
                    DiscoveryUtils.addLog('error', '菜单添加失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                console.error('[addMenuForInstalledCapability] Error:', error);
                DiscoveryUtils.addLog('error', '菜单添加失败: ' + error.message);
            });
    },

    markAsInstalled: function(skillId) {
        var cap = state.discoveredCapabilities.find(function(c) { return c.id === skillId || c.skillId === skillId; });
        if (cap) {
            cap.installed = true;
        }
    },

    startActivation: function(cap, installRequest) {
        console.log('[startActivation] Starting activation for:', cap.name);
        DiscoveryUtils.addLog('info', '开始激活场景...');
        
        var installId = 'install-' + (cap.skillId || cap.id) + '-' + Date.now();
        var activationRequest = {
            templateId: cap.skillId || cap.id,
            activator: installRequest.participants && installRequest.participants.leader ? installRequest.participants.leader : 'current_user',
            roleName: installRequest.selectedRole || 'MANAGER',
            leaderId: installRequest.participants && installRequest.participants.leader ? installRequest.participants.leader : null,
            collaboratorIds: installRequest.participants && installRequest.participants.collaborators ? installRequest.participants.collaborators : []
        };
        
        ApiClient.post('/api/v1/activations/' + installId + '/start-with-template', activationRequest)
            .then(function(result) {
                console.log('[startActivation] Activation started:', result);
                if (result.status === 'success') {
                    DiscoveryUtils.addLog('success', '激活流程已启动');
                    
                    return ApiClient.post('/api/v1/activations/' + installId + '/activate');
                } else {
                    throw new Error(result.message || '启动激活流程失败');
                }
            })
            .then(function(result) {
                console.log('[startActivation] Activation confirmed:', result);
                if (result.status === 'success') {
                    DiscoveryUtils.addLog('success', '场景激活成功');
                    DiscoveryInstallWizard.nextInstallStep();
                } else {
                    DiscoveryUtils.addLog('error', '激活失败: ' + (result.message || '未知错误'));
                    DiscoveryInstallWizard.nextInstallStep();
                }
            })
            .catch(function(error) {
                console.error('[startActivation] Activation error:', error);
                DiscoveryUtils.addLog('error', '激活失败: ' + error.message);
                DiscoveryInstallWizard.nextInstallStep();
            });
    }
};

global.DiscoveryInstallExec = DiscoveryInstallExec;

})(typeof window !== 'undefined' ? window : this);
