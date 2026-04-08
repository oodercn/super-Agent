/**
 * 系统安装者页面脚本
 */
(function() {
    'use strict';

    var CAPABILITIES = OoderCapability.CAPABILITIES;
    var PROFILE_DEFAULTS = OoderCapability.PROFILE_DEFAULTS;
    var PROFILE_CONFIG_STEPS = OoderCapability.PROFILE_CONFIG_STEPS;
    var REQUIRED_FIELDS_BY_PROFILE = OoderCapability.REQUIRED_FIELDS_BY_PROFILE;
    var RECOMMENDED_FIELDS_BY_PROFILE = OoderCapability.RECOMMENDED_FIELDS_BY_PROFILE;
    var getAddressHex = OoderCapability.getAddressHex;
    var getCapabilityByAddress = OoderCapability.getCapabilityByAddress;

    var selectedProfile = 'micro';
    var currentStep = 1;
    var capabilityConfig = {};
    var extendedConfig = {};
    var expandedCapabilities = new Set();
    var autoActivate = false;

    var requiredSkills = [
        { id: 'skill-scene', name: 'skill-scene 基础场景技能包', desc: '核心场景管理功能', isCurrentSystem: true },
        { id: 'skill-capability', name: 'skill-capability 能力管理包', desc: '能力发现与管理功能' },
        { id: 'skill-health', name: 'skill-health 健康检查包', desc: '系统健康监控功能' },
        { id: 'skill-llm-chat', name: 'skill-llm-chat LLM对话包', desc: '大语言模型对话功能' }
    ];
    
    var installedSkillIds = new Set();
    var currentSyscode = null;
    var completionShown = false;

    async function initPage() {
        initCapabilities();
        initStepWizard();
        await loadSystemConfig();
        await loadInstalledSkills();
        await checkLoopStatus();
    }

    function initCapabilities() {
        var list = document.getElementById('capabilitiesList');
        if (!list) return;
        
        list.innerHTML = '';
        
        CAPABILITIES.forEach(function(cap) {
            capabilityConfig[cap.address] = {
                enabled: cap.enabled,
                driver: cap.defaultDriver,
                config: {}
            };
            
            cap.configFields.forEach(function(field) {
                capabilityConfig[cap.address].config[field.name] = field.default;
            });
            
            var card = createCapabilityCard(cap);
            list.appendChild(card);
        });
        
        applyProfileDefaults(selectedProfile);
    }

    function initStepWizard() {
        renderStepWizard();
        updateStepUI();
    }

    function renderStepWizard() {
        var steps = PROFILE_CONFIG_STEPS[selectedProfile] || PROFILE_CONFIG_STEPS.micro;
        var container = document.getElementById('stepWizard');
        if (!container) return;

        var html = '<div class="step-wizard">';
        
        steps.forEach(function(step, index) {
            var stepNum = index + 1;
            var isLast = stepNum === steps.length;
            
            html += '<div class="step-item' + (stepNum === currentStep ? ' active' : '') + '" data-step="' + stepNum + '">';
            html += '<div class="step-number">' + stepNum + '</div>';
            html += '<div class="step-content">';
            html += '<div class="step-title">' + step.title + '</div>';
            html += '<div class="step-desc">' + step.description + '</div>';
            if (step.required) {
                html += '<span class="step-badge required">必需</span>';
            } else if (step.recommended) {
                html += '<span class="step-badge recommended">推荐</span>';
            }
            html += '</div>';
            if (!isLast) {
                html += '<div class="step-connector"></div>';
            }
            html += '</div>';
        });
        
        html += '</div>';
        container.innerHTML = html;
    }

    function updateStepUI() {
        var steps = PROFILE_CONFIG_STEPS[selectedProfile] || PROFILE_CONFIG_STEPS.micro;
        
        document.querySelectorAll('.step-item').forEach(function(item) {
            var stepNum = parseInt(item.getAttribute('data-step'));
            item.classList.remove('active', 'completed');
            if (stepNum < currentStep) {
                item.classList.add('completed');
            } else if (stepNum === currentStep) {
                item.classList.add('active');
            }
        });

        var prevBtn = document.getElementById('prevStepBtn');
        var nextBtn = document.getElementById('nextStepBtn');
        var saveBtn = document.getElementById('saveConfigBtn');

        if (prevBtn) {
            prevBtn.style.display = currentStep > 1 ? 'inline-flex' : 'none';
        }
        
        if (nextBtn) {
            nextBtn.style.display = currentStep < steps.length ? 'inline-flex' : 'none';
        }
        
        if (saveBtn) {
            saveBtn.style.display = currentStep === steps.length ? 'inline-flex' : 'none';
        }

        highlightCurrentStepFields();
    }

    function highlightCurrentStepFields() {
        var steps = PROFILE_CONFIG_STEPS[selectedProfile] || PROFILE_CONFIG_STEPS.micro;
        var currentStepData = steps[currentStep - 1];
        if (!currentStepData) return;

        document.querySelectorAll('.capability-config-card').forEach(function(card) {
            card.classList.remove('step-highlight', 'step-dim');
        });

        var fields = currentStepData.fields || [];
        var highlightedAddresses = new Set();

        fields.forEach(function(field) {
            if (field.endsWith('.*')) {
                var address = field.replace('.*', '');
                highlightedAddresses.add(address);
            } else {
                var parts = field.split('.');
                if (parts.length >= 1) {
                    highlightedAddresses.add(parts[0]);
                }
            }
        });

        CAPABILITIES.forEach(function(cap) {
            var card = document.getElementById('cap-card-' + cap.address);
            if (card) {
                if (highlightedAddresses.has(cap.address)) {
                    card.classList.add('step-highlight');
                } else if (currentStepData.required) {
                    card.classList.add('step-dim');
                }
            }
        });
    }

    function createCapabilityCard(cap) {
        var card = document.createElement('div');
        card.className = 'capability-config-card' + (cap.enabled ? '' : ' disabled');
        card.setAttribute('data-address', cap.address);
        card.id = 'cap-card-' + cap.address;
        
        var headerHtml = 
            '<div class="cap-card-header" onclick="toggleCapabilityCard(\'' + cap.address + '\')">' +
                '<div class="cap-icon" style="background-color: ' + cap.color + '; color: white;">' +
                    '<i class="' + cap.icon + '"></i>' +
                '</div>' +
                '<div class="cap-info">' +
                    '<div class="cap-name">' + cap.name + 
                        (cap.dbRequired ? '<span class="db-required-badge" title="需要数据库支持"><i class="ri-database-2-line"></i></span>' : '') +
                    '</div>' +
                    '<div class="cap-address">0x' + getAddressHex(cap.address) + ' | ' + cap.address + '</div>' +
                '</div>' +
                '<div class="cap-status">' +
                    '<span class="cap-driver-badge">' + (cap.defaultDriver || '无驱动') + '</span>' +
                '</div>' +
                '<div class="expand-icon"><i class="ri-arrow-down-s-line"></i></div>' +
            '</div>';
        
        var bodyHtml = '<div class="cap-card-body">';
        
        bodyHtml += 
            '<div class="enable-toggle">' +
                '<div class="toggle-switch' + (cap.enabled ? ' active' : '') + '" id="toggle-' + cap.address + '" onclick="event.stopPropagation(); toggleCapabilityEnabled(\'' + cap.address + '\')"></div>' +
                '<label>启用此能力</label>' +
            '</div>';
        
        bodyHtml += '<div class="config-form">';

        cap.configFields.forEach(function(field) {
            bodyHtml += createConfigField(cap.address, field, cap);
        });

        bodyHtml += '<div class="extended-config" id="extended-' + cap.address + '"></div>';
        
        bodyHtml += '</div></div>';
        
        card.innerHTML = headerHtml + bodyHtml;
        
        return card;
    }

    function createConfigField(address, field, cap) {
        var fieldId = 'config-' + address + '-' + field.name;
        var requiredMark = field.required ? ' <span class="required-mark">*</span>' : '';
        var recommendedMark = field.recommended ? ' <span class="recommended-mark">⚡</span>' : '';
        var descHtml = field.desc ? '<div class="field-desc">' + field.desc + '</div>' : '';
        
        var html = '<div class="config-field' + (field.required ? ' required-field' : '') + (field.recommended ? ' recommended-field' : '') + '">';
        html += '<label for="' + fieldId + '">' + field.label + requiredMark + recommendedMark + '</label>';
        
        if (field.type === 'select') {
            html += '<select id="' + fieldId + '" onchange="updateConfig(\'' + address + '\', \'' + field.name + '\', this.value); handleExtendedConfig(\'' + address + '\', \'' + field.name + '\', this.value)">';
            field.options.forEach(function(opt) {
                html += '<option value="' + opt + '"' + (opt === field.default ? ' selected' : '') + '>' + opt + '</option>';
            });
            html += '</select>';
        } else if (field.type === 'password') {
            html += '<input type="password" id="' + fieldId + '" value="' + (field.default || '') + '" placeholder="请输入' + field.label + '" onchange="updateConfig(\'' + address + '\', \'' + field.name + '\', this.value)"' + (field.required ? ' required' : '') + '>';
        } else if (field.type === 'number') {
            html += '<input type="number" id="' + fieldId + '" value="' + (field.default || 0) + '" onchange="updateConfig(\'' + address + '\', \'' + field.name + '\', parseFloat(this.value))">';
        } else if (field.type === 'textarea') {
            html += '<textarea id="' + fieldId + '" onchange="updateConfig(\'' + address + '\', \'' + field.name + '\', this.value)">' + (field.default || '') + '</textarea>';
        } else {
            html += '<input type="text" id="' + fieldId + '" value="' + (field.default || '') + '" placeholder="请输入' + field.label + '" onchange="updateConfig(\'' + address + '\', \'' + field.name + '\', this.value)"' + (field.required ? ' required' : '') + '>';
        }
        
        html += descHtml;
        html += '</div>';
        return html;
    }

    function handleExtendedConfig(address, fieldName, value) {
        var cap = getCapabilityByAddress(address);
        if (!cap || !cap.extendedConfig) return;

        var container = document.getElementById('extended-' + address);
        if (!container) return;

        var extendedFields = cap.extendedConfig[value];
        if (!extendedFields || extendedFields.length === 0) {
            container.innerHTML = '';
            return;
        }

        var html = '<div class="extended-fields-title"><i class="ri-settings-4-line"></i> ' + value.toUpperCase() + ' 配置</div>';
        
        extendedFields.forEach(function(field) {
            html += createConfigField(address, field, cap);
            if (!extendedConfig[address]) {
                extendedConfig[address] = {};
            }
            extendedConfig[address][field.name] = field.default;
        });

        container.innerHTML = html;
    }

    function applyProfileDefaults(profile) {
        var defaults = PROFILE_DEFAULTS[profile] || PROFILE_DEFAULTS.micro;
        
        Object.keys(defaults).forEach(function(address) {
            var config = defaults[address];
            if (capabilityConfig[address]) {
                if (config.enabled !== undefined) {
                    capabilityConfig[address].enabled = config.enabled;
                }
                Object.keys(config).forEach(function(key) {
                    if (key !== 'enabled') {
                        capabilityConfig[address].config[key] = config[key];
                        
                        var field = document.getElementById('config-' + address + '-' + key);
                        if (field) {
                            field.value = config[key];
                        }
                    }
                });
            }
        });
        
        updateCapabilitiesUI();
    }

    function updateCapabilitiesUI() {
        CAPABILITIES.forEach(function(cap) {
            var card = document.getElementById('cap-card-' + cap.address);
            var toggle = document.getElementById('toggle-' + cap.address);
            var config = capabilityConfig[cap.address];
            
            if (card && config) {
                if (config.enabled) {
                    card.classList.remove('disabled');
                } else {
                    card.classList.add('disabled');
                }
            }
            
            if (toggle && config) {
                if (config.enabled) {
                    toggle.classList.add('active');
                } else {
                    toggle.classList.remove('active');
                }
            }
        });
    }

    function validateRequiredFields() {
        var requiredFields = REQUIRED_FIELDS_BY_PROFILE[selectedProfile] || [];
        var missing = [];

        requiredFields.forEach(function(fieldPath) {
            var parts = fieldPath.split('.');
            if (parts.length === 2) {
                var address = parts[0];
                var fieldName = parts[1];
                var value = capabilityConfig[address] && capabilityConfig[address].config[fieldName];
                
                if (value === undefined || value === null || value === '') {
                    var cap = getCapabilityByAddress(address);
                    var field = cap && cap.configFields.find(function(f) { return f.name === fieldName; });
                    missing.push({
                        path: fieldPath,
                        address: address,
                        fieldName: fieldName,
                        label: (field && field.label) || fieldName,
                        capName: cap ? cap.name : address
                    });
                }
            }
        });

        return {
            valid: missing.length === 0,
            missing: missing
        };
    }

    function showValidationErrors(missing) {
        missing.forEach(function(item) {
            var field = document.getElementById('config-' + item.address + '-' + item.fieldName);
            if (field) {
                field.classList.add('validation-error');
                var parent = field.closest('.config-field');
                if (parent && !parent.querySelector('.error-message')) {
                    var errorMsg = document.createElement('div');
                    errorMsg.className = 'error-message';
                    errorMsg.textContent = '此字段为必填项';
                    parent.appendChild(errorMsg);
                }
            }
            
            var card = document.getElementById('cap-card-' + item.address);
            if (card) {
                card.classList.add('has-error');
            }
        });

        if (missing.length > 0) {
            var firstMissing = missing[0];
            var card = document.getElementById('cap-card-' + firstMissing.address);
            if (card) {
                card.classList.add('expanded');
                card.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }
    }

    function clearValidationErrors() {
        document.querySelectorAll('.validation-error').forEach(function(el) {
            el.classList.remove('validation-error');
        });
        document.querySelectorAll('.error-message').forEach(function(el) {
            el.remove();
        });
        document.querySelectorAll('.has-error').forEach(function(el) {
            el.classList.remove('has-error');
        });
    }
    
    async function checkLoopStatus() {
        try {
            var response = await fetch('/api/v1/installer/status/loop1');
            var result = await response.json();
            
            if (result.status === 'success' && result.data) {
                var status = result.data.status;
                
                if (status === 'completed') {
                    addLog('闭环一已完成，跳过安装', 'info');
                    requiredSkills.forEach(function(skill) {
                        installedSkillIds.add(skill.id);
                    });
                    updateProgress();
                    showCompletionMessage();
                }
            }
        } catch (e) {
            console.error('Failed to check loop status:', e);
        }
    }

    async function loadSystemConfig() {
        try {
            var response = await fetch('/api/v1/system/config');
            var result = await response.json();
            if (result.status === 'success' && result.data) {
                currentSyscode = result.data.syscode;
                addLog('当前系统: ' + currentSyscode, 'info');
                
                requiredSkills.forEach(function(skill) {
                    if (skill.id === currentSyscode || currentSyscode.includes(skill.id.replace('skill-', ''))) {
                        skill.isCurrentSystem = true;
                    }
                });
            }
        } catch (e) {
            console.error('Failed to load system config:', e);
            addLog('无法加载系统配置', 'warning');
        }
    }

    async function loadInstalledSkills() {
        addLog('检查已安装技能...', 'info');
        
        try {
            var response = await fetch('/api/v1/discovery/local', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({})
            });
            var result = await response.json();
            
            if (result.status === 'success' && result.data && result.data.capabilities) {
                var capabilities = result.data.capabilities;
                
                capabilities.forEach(function(cap) {
                    var skillId = cap.skillId || cap.id;
                    if (skillId && requiredSkills.some(function(s) { return s.id === skillId; })) {
                        installedSkillIds.add(skillId);
                    }
                });
                
                addLog('发现 ' + installedSkillIds.size + ' 个已安装技能', 'success');
            }
        } catch (e) {
            console.error('Failed to load installed skills:', e);
            addLog('无法加载已安装技能列表', 'error');
        }
        
        updateProgress();
    }

    function addLog(message, type) {
        type = type || 'info';
        var logContainer = document.getElementById('install-log');
        var icons = {
            'info': 'ri-information-line',
            'success': 'ri-checkbox-circle-line',
            'error': 'ri-error-warning-line',
            'warning': 'ri-alert-line'
        };
        var time = new Date().toLocaleTimeString();
        var div = document.createElement('div');
        div.className = 'log-item ' + type;
        div.innerHTML = '<i class="' + icons[type] + '"></i><span class="log-time">' + time + '</span>' + message;
        logContainer.appendChild(div);
        logContainer.scrollTop = logContainer.scrollHeight;
    }

    function updateProgress() {
        var installedCount = 0;
        
        requiredSkills.forEach(function(skill, index) {
            var item = document.getElementById('check-' + (index + 1));
            if (item) {
                var isCurrentSystem = skill.isCurrentSystem;
                var isInstalled = installedSkillIds.has(skill.id);
                
                if (isInstalled || isCurrentSystem) {
                    installedCount++;
                    item.classList.add('completed');
                    var icon = item.querySelector('.checklist-icon i');
                    if (icon) icon.className = 'ri-checkbox-circle-line';
                    var btn = item.querySelector('button');
                    if (btn) {
                        btn.disabled = true;
                        if (isCurrentSystem) {
                            btn.textContent = '当前系统';
                            btn.classList.remove('nx-btn--primary');
                            btn.classList.add('nx-btn--success');
                        } else {
                            btn.textContent = '已安装';
                            btn.classList.remove('nx-btn--primary');
                            btn.classList.add('nx-btn--secondary');
                        }
                    }
                } else {
                    item.classList.remove('completed');
                    var icon = item.querySelector('.checklist-icon i');
                    if (icon) icon.className = 'ri-checkbox-blank-line';
                    var btn = item.querySelector('button');
                    if (btn) {
                        btn.disabled = false;
                        btn.textContent = '安装';
                        btn.classList.remove('nx-btn--success', 'nx-btn--secondary');
                        btn.classList.add('nx-btn--primary');
                    }
                }
            }
        });
        
        var total = requiredSkills.length;
        var percent = Math.min(100, Math.round((installedCount / total) * 100));
        var pending = Math.max(0, total - installedCount);

        document.getElementById('installed-count').textContent = installedCount;
        document.getElementById('progress-percent').textContent = percent + '%';
        document.getElementById('pending-count').textContent = pending;
        
        checkAllCompleted();
    }
    
    function checkAllCompleted() {
        var total = requiredSkills.length;
        var installedCount = 0;
        
        requiredSkills.forEach(function(skill) {
            if (installedSkillIds.has(skill.id) || skill.isCurrentSystem) {
                installedCount++;
            }
        });
        
        if (installedCount === total) {
            showCompletionMessage();
        }
    }
    
    function showCompletionMessage() {
        if (completionShown) return;
        completionShown = true;
        
        var logContainer = document.getElementById('install-log');
        var completionDiv = document.createElement('div');
        completionDiv.className = 'log-item success';
        completionDiv.innerHTML = 
            '<i class="ri-checkbox-circle-line"></i>' +
            '<span class="log-time">' + new Date().toLocaleTimeString() + '</span>' +
            '<strong>闭环一完成！所有基础技能包已安装</strong>';
        logContainer.appendChild(completionDiv);
        logContainer.scrollTop = logContainer.scrollHeight;
        
        var mainContainer = document.querySelector('.installer-main');
        var actionDiv = document.createElement('div');
        actionDiv.className = 'completion-banner';
        actionDiv.innerHTML = 
            '<h3><i class="ri-checkbox-circle-fill"></i> 闭环一已完成</h3>' +
            '<p>所有基础技能包已成功安装，系统环境初始化完成</p>' +
            '<button class="nx-btn nx-btn--primary" onclick="goToNextStep()">' +
                '<i class="ri-arrow-right-line"></i> 进入闭环二：场景配置' +
            '</button>';
        mainContainer.appendChild(actionDiv);
        
        saveCompletionStatus();
    }
    
    function goToNextStep() {
        window.location.href = '/console/pages/scene-group-management.html';
    }

    async function saveConfigToSdk() {
        addLog('正在保存配置到系统...', 'info');
        
        try {
            var configData = {
                apiVersion: 'skills.ooder.io/v1',
                kind: 'SystemConfig',
                metadata: {
                    name: 'ooder-skills-system',
                    profile: selectedProfile,
                    updatedAt: new Date().toISOString()
                },
                spec: {
                    capabilities: {}
                }
            };

            Object.keys(capabilityConfig).forEach(function(address) {
                var capConfig = capabilityConfig[address];
                configData.spec.capabilities[address] = {
                    enabled: capConfig.enabled,
                    config: capConfig.config
                };
                
                if (extendedConfig[address]) {
                    Object.assign(configData.spec.capabilities[address].config, extendedConfig[address]);
                }
            });

            var response = await fetch('/api/v1/config/system', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(configData)
            });

            if (response.ok) {
                addLog('配置已保存到系统', 'success');
                return true;
            } else {
                throw new Error('保存失败: ' + response.status);
            }
        } catch (e) {
            console.error('Failed to save config:', e);
            addLog('保存配置失败: ' + e.message, 'error');
            return false;
        }
    }
    
    async function saveCompletionStatus() {
        try {
            await fetch('/api/v1/installer/status', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    loop: 'loop1',
                    status: 'completed',
                    completedAt: new Date().toISOString(),
                    profile: selectedProfile,
                    capabilities: capabilityConfig
                })
            });
        } catch (e) {
            console.error('Failed to save completion status:', e);
        }
    }

    async function installSkill(skillId) {
        var skill = requiredSkills.find(function(s) { return s.id === skillId; });
        if (!skill) return;
        
        if (skill.isCurrentSystem) {
            addLog(skill.name + ' 是当前系统，无需安装', 'warning');
            return;
        }
        
        addLog('开始安装 ' + skill.name + '...', 'info');

        try {
            var response = await fetch('/api/v1/discovery/install', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ 
                    skillId: skillId, 
                    source: 'GITEE',
                    profile: selectedProfile,
                    capabilities: capabilityConfig
                })
            });

            var result = await response.json();

            if (result.status === 'success' && result.data) {
                var installResult = result.data;
                var capabilityId = installResult.capabilityId || skillId;
                
                if (installResult.status === 'installed' || installResult.status === 'success') {
                    installedSkillIds.add(skillId);
                    addLog(skill.name + ' 安装成功', 'success');
                    
                    if (installResult.installedDependencies && installResult.installedDependencies.length > 0) {
                        addLog('安装了 ' + installResult.installedDependencies.length + ' 个依赖', 'info');
                    }
                    
                    if (typeof autoActivate !== 'undefined' && autoActivate) {
                        await activateSkillAfterInstall(capabilityId, skill.name);
                    }
                    
                    updateProgress();
                } else if (installResult.status === 'failed' || installResult.status === 'error') {
                    addLog(skill.name + ' 安装失败: ' + (installResult.message || '未知错误'), 'error');
                } else {
                    installedSkillIds.add(skillId);
                    addLog(skill.name + ' 安装完成 (' + installResult.status + ')', 'success');
                    updateProgress();
                }
            } else {
                var errorMsg = result.message || '安装请求失败';
                addLog(skill.name + ' 安装失败: ' + errorMsg, 'error');
            }
        } catch (e) {
            console.error('Install error:', e);
            addLog(skill.name + ' 安装异常: ' + e.message, 'error');
        }
    }
    
    async function activateSkillAfterInstall(capabilityId, skillName) {
        addLog('正在激活 ' + skillName + '...', 'info');
        try {
            var response = await fetch('/api/v1/scene-capabilities/' + capabilityId + '/activate', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });
            var result = await response.json();
            if (result.success) {
                addLog(skillName + ' 激活成功', 'success');
            } else {
                addLog(skillName + ' 激活失败: ' + (result.message || '未知错误'), 'warning');
            }
        } catch (e) {
            addLog(skillName + ' 激活异常: ' + e.message, 'warning');
        }
    }

    window.installSkill = installSkill;
    window.goToNextStep = goToNextStep;
    
    window.toggleAutoActivate = function(checked) {
        autoActivate = checked;
        addLog(checked ? '已启用安装后自动激活' : '已禁用安装后自动激活', 'info');
    };
    
    window.selectProfile = function(profile) {
        selectedProfile = profile;
        currentStep = 1;
        
        document.querySelectorAll('.profile-card').forEach(function(card) {
            card.classList.remove('selected');
        });
        document.querySelector('.profile-card[data-profile="' + profile + '"]').classList.add('selected');
        
        applyProfileDefaults(profile);
        renderStepWizard();
        updateStepUI();
        addLog('已选择部署规模: ' + profile, 'info');
    };

    window.prevStep = function() {
        if (currentStep > 1) {
            currentStep--;
            updateStepUI();
        }
    };

    window.nextStep = function() {
        clearValidationErrors();
        
        var validation = validateRequiredFields();
        if (!validation.valid) {
            showValidationErrors(validation.missing);
            addLog('请填写所有必填项', 'error');
            return;
        }

        var steps = PROFILE_CONFIG_STEPS[selectedProfile] || PROFILE_CONFIG_STEPS.micro;
        if (currentStep < steps.length) {
            currentStep++;
            updateStepUI();
        }
    };

    window.saveAllConfig = async function() {
        clearValidationErrors();
        
        var validation = validateRequiredFields();
        if (!validation.valid) {
            showValidationErrors(validation.missing);
            addLog('请填写所有必填项后再保存', 'error');
            return;
        }

        var saved = await saveConfigToSdk();
        if (saved) {
            await saveCompletionStatus();
            showCompletionMessage();
        }
    };
    
    window.toggleCapabilityCard = function(address) {
        var card = document.getElementById('cap-card-' + address);
        if (card) {
            if (expandedCapabilities.has(address)) {
                expandedCapabilities.delete(address);
                card.classList.remove('expanded');
            } else {
                expandedCapabilities.add(address);
                card.classList.add('expanded');
            }
        }
    };
    
    window.toggleCapabilityEnabled = function(address) {
        if (capabilityConfig[address]) {
            capabilityConfig[address].enabled = !capabilityConfig[address].enabled;
            updateCapabilitiesUI();
        }
    };
    
    window.updateConfig = function(address, fieldName, value) {
        if (capabilityConfig[address]) {
            capabilityConfig[address].config[fieldName] = value;
        }
        clearValidationErrors();
    };
    
    window.handleExtendedConfig = handleExtendedConfig;
    
    window.expandAllCapabilities = function() {
        CAPABILITIES.forEach(function(cap) {
            expandedCapabilities.add(cap.address);
            var card = document.getElementById('cap-card-' + cap.address);
            if (card) {
                card.classList.add('expanded');
            }
        });
    };
    
    window.collapseAllCapabilities = function() {
        CAPABILITIES.forEach(function(cap) {
            expandedCapabilities.delete(cap.address);
            var card = document.getElementById('cap-card-' + cap.address);
            if (card) {
                card.classList.remove('expanded');
            }
        });
    };

    document.addEventListener('DOMContentLoaded', initPage);

})();
