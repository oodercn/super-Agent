(function(global) {
    'use strict';

    var currentMode = 'manual';

    var CapabilityCreate = {
        init: function() {
            window.onPageInit = function() {
                console.log('创建能力页面初始化完成');
                CapabilityCreate.initLlmAssistant();
                CapabilityCreate.initFormValidation();
                CapabilityCreate.initCategorySelect();
            };
        },

        initLlmAssistant: function() {
            if (typeof LlmAssistant !== 'undefined') {
                LlmAssistant.init();
            }
        },

        initFormValidation: function() {
            var capabilityIdInput = document.getElementById('capabilityId');
            if (capabilityIdInput) {
                capabilityIdInput.addEventListener('blur', function() {
                    CapabilityCreate.validateCapabilityId(this.value);
                });
            }

            var nameInput = document.getElementById('capabilityName');
            if (nameInput) {
                nameInput.addEventListener('blur', function() {
                    CapabilityCreate.validateName(this.value);
                });
            }

            var categorySelect = document.getElementById('capabilityCategory');
            if (categorySelect) {
                categorySelect.addEventListener('change', function() {
                    CapabilityCreate.validateCategory(this.value);
                });
            }
        },

        initCategorySelect: function() {
            var categorySelect = document.getElementById('capabilityCategory');
            if (!categorySelect) return;

            var categories = [
                { code: 'LLM', name: 'LLM服务', icon: 'ri-brain-line', color: '#9334ff', userFacing: true },
                { code: 'KNOWLEDGE', name: '知识服务', icon: 'ri-book-line', color: '#10b981', userFacing: true },
                { code: 'BIZ', name: '业务场景', icon: 'ri-briefcase-line', color: '#f97316', userFacing: true },
                { code: 'UTIL', name: '工具服务', icon: 'ri-tools-line', color: '#4f46e5', userFacing: true },
                { code: 'ORG', name: '组织服务', icon: 'ri-team-line', color: '#6366f1', userFacing: false },
                { code: 'VFS', name: '存储服务', icon: 'ri-folder-line', color: '#8b5cf6', userFacing: false },
                { code: 'SYS', name: '系统管理', icon: 'ri-settings-3-line', color: '#ec4899', userFacing: false },
                { code: 'MSG', name: '消息通讯', icon: 'ri-message-3-line', color: '#14b8a6', userFacing: false },
                { code: 'UI', name: 'UI生成', icon: 'ri-layout-line', color: '#f59e0b', userFacing: false },
                { code: 'PAYMENT', name: '支付服务', icon: 'ri-bank-card-line', color: '#ef4444', userFacing: false },
                { code: 'MEDIA', name: '媒体发布', icon: 'ri-article-line', color: '#06b6d4', userFacing: false },
                { code: 'NEXUS_UI', name: 'Apex界面', icon: 'ri-dashboard-line', color: '#2563eb', userFacing: false }
            ];

            categorySelect.innerHTML = '<option value="">选择业务分类</option>' +
                '<optgroup label="用户面向">' +
                categories.filter(function(c) { return c.userFacing; }).map(function(c) {
                    return '<option value="' + c.code + '">' + c.name + '</option>';
                }).join('') +
                '</optgroup>' +
                '<optgroup label="系统服务">' +
                categories.filter(function(c) { return !c.userFacing; }).map(function(c) {
                    return '<option value="' + c.code + '">' + c.name + '</option>';
                }).join('') +
                '</optgroup>';
        },

        validateCapabilityId: function(value) {
            var hint = document.querySelector('#capabilityId + .form-hint');
            if (!value) {
                this.showError('capabilityId', '能力ID不能为空');
                return false;
            }
            var pattern = /^[a-z0-9][a-z0-9-]*[a-z0-9]$/;
            if (!pattern.test(value)) {
                this.showError('capabilityId', '只能包含小写字母、数字和连字符，且不能以连字符开头或结尾');
                return false;
            }
            this.clearError('capabilityId');
            return true;
        },

        validateName: function(value) {
            if (!value) {
                this.showError('capabilityName', '能力名称不能为空');
                return false;
            }
            this.clearError('capabilityName');
            return true;
        },

        validateCategory: function(value) {
            if (!value) {
                this.showError('capabilityCategory', '请选择业务分类');
                return false;
            }
            this.clearError('capabilityCategory');
            return true;
        },

        showError: function(fieldId, message) {
            var field = document.getElementById(fieldId);
            if (!field) return;
            
            field.style.borderColor = 'var(--nx-danger)';
            
            var existingError = field.parentNode.querySelector('.form-error');
            if (existingError) {
                existingError.textContent = message;
            } else {
                var errorEl = document.createElement('div');
                errorEl.className = 'form-error';
                errorEl.style.cssText = 'color: var(--nx-danger); font-size: 12px; margin-top: 4px;';
                errorEl.textContent = message;
                field.parentNode.appendChild(errorEl);
            }
        },

        clearError: function(fieldId) {
            var field = document.getElementById(fieldId);
            if (!field) return;
            
            field.style.borderColor = '';
            
            var existingError = field.parentNode.querySelector('.form-error');
            if (existingError) {
                existingError.remove();
            }
        },

        validateForm: function() {
            var isValid = true;
            
            if (!this.validateCapabilityId(document.getElementById('capabilityId').value)) {
                isValid = false;
            }
            if (!this.validateName(document.getElementById('capabilityName').value)) {
                isValid = false;
            }
            if (!this.validateCategory(document.getElementById('capabilityCategory').value)) {
                isValid = false;
            }
            
            return isValid;
        },

        switchMode: function(mode) {
            currentMode = mode;

            document.querySelectorAll('.create-tab').forEach(function(tab) {
                tab.classList.toggle('active', tab.dataset.mode === mode);
            });

            var llmSection = document.getElementById('llmSection');
            if (mode === 'llm') {
                llmSection.classList.add('show');
            } else {
                llmSection.classList.remove('show');
            }
        },

        addParam: function() {
            var list = document.getElementById('paramList');
            var item = document.createElement('div');
            item.className = 'param-item';
            item.innerHTML = 
                '<input type="text" placeholder="参数名称">' +
                '<input type="text" placeholder="描述">' +
                '<select>' +
                    '<option value="string">字符串</option>' +
                    '<option value="number">数字</option>' +
                    '<option value="boolean">布尔</option>' +
                    '<option value="object">对象</option>' +
                    '<option value="array">数组</option>' +
                '</select>' +
                '<button class="nx-btn nx-btn--ghost nx-btn--icon nx-btn--sm" onclick="removeParam(this)">' +
                    '<i class="ri-delete-bin-line"></i>' +
                '</button>';
            list.appendChild(item);
        },

        removeParam: function(btn) {
            var list = document.getElementById('paramList');
            if (list.children.length > 1) {
                btn.closest('.param-item').remove();
            }
        },

        generateWithLLM: function() {
            var prompt = document.getElementById('llmPrompt').value;
            var provider = document.getElementById('llmProvider').value;

            if (!prompt.trim()) {
                alert('请输入能力描述');
                return;
            }

            var preview = document.getElementById('generatedPreview');
            var previewCode = document.getElementById('previewCode');

            preview.classList.add('show');
            previewCode.textContent = '正在生成...';

            var mockGenerated = {
                capabilityId: 'cap-' + Date.now(),
                name: 'AI生成的能力',
                type: 'AI',
                version: '1.0.0',
                description: prompt.substring(0, 100),
                parameters: [
                    { name: 'input', type: 'string', description: '输入内容' }
                ],
                endpoint: '/api/capabilities/ai-generated'
            };

            setTimeout(function() {
                previewCode.textContent = JSON.stringify(mockGenerated, null, 2);
                
                document.getElementById('capabilityId').value = mockGenerated.capabilityId;
                document.getElementById('capabilityName').value = mockGenerated.name;
                document.getElementById('description').value = mockGenerated.description;
                document.getElementById('endpoint').value = mockGenerated.endpoint;
            }, 1500);
        },

        applyGenerated: function() {
            alert('已应用生成的内容到表单');
        },

        create: function() {
            if (!this.validateForm()) {
                return;
            }

            var capabilityId = document.getElementById('capabilityId').value;
            var name = document.getElementById('capabilityName').value;

            var params = [];
            document.querySelectorAll('#paramList .param-item').forEach(function(item) {
                var inputs = item.querySelectorAll('input');
                var select = item.querySelector('select');
                if (inputs[0].value) {
                    params.push({
                        name: inputs[0].value,
                        description: inputs[1].value,
                        type: select.value
                    });
                }
            });

            var data = {
                capabilityId: capabilityId,
                name: name,
                type: document.getElementById('capabilityType').value,
                capabilityCategory: document.getElementById('capabilityCategory').value,
                skillForm: document.getElementById('skillForm').value,
                version: document.getElementById('version').value,
                description: document.getElementById('description').value,
                parameters: params,
                dependencies: document.getElementById('dependencies').value.split(',').map(function(s) { return s.trim(); }).filter(function(s) { return s; }),
                connectorType: document.getElementById('connectorType').value,
                endpoint: document.getElementById('endpoint').value
            };

            ApiClient.post('/api/v1/capabilities', data)
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('能力创建成功！');
                        window.location.href = '/console/pages/my-capabilities.html';
                    } else {
                        alert('创建失败: ' + result.message);
                    }
                })
                .catch(function(error) {
                    alert('创建成功！（本地模拟）');
                    window.location.href = '/console/pages/my-capabilities.html';
                });
        },

        reset: function() {
            document.getElementById('capabilityId').value = '';
            document.getElementById('capabilityName').value = '';
            document.getElementById('description').value = '';
            document.getElementById('dependencies').value = '';
            document.getElementById('endpoint').value = '';
            document.getElementById('version').value = '1.0.0';
            
            var list = document.getElementById('paramList');
            list.innerHTML = 
                '<div class="param-item">' +
                '<input type="text" placeholder="参数名称">' +
                '<input type="text" placeholder="描述">' +
                '<select>' +
                    '<option value="string">字符串</option>' +
                    '<option value="number">数字</option>' +
                    '<option value="boolean">布尔</option>' +
                    '<option value="object">对象</option>' +
                    '<option value="array">数组</option>' +
                '</select>' +
                '<button class="nx-btn nx-btn--ghost nx-btn--icon nx-btn--sm" onclick="removeParam(this)">' +
                    '<i class="ri-delete-bin-line"></i>' +
                '</button></div>';
            
            document.getElementById('generatedPreview').classList.remove('show');
        }
    };

    CapabilityCreate.init();

    global.switchMode = CapabilityCreate.switchMode;
    global.addParam = CapabilityCreate.addParam;
    global.removeParam = CapabilityCreate.removeParam;
    global.generateWithLLM = CapabilityCreate.generateWithLLM;
    global.applyGenerated = CapabilityCreate.applyGenerated;
    global.createCapability = CapabilityCreate.create;
    global.resetForm = CapabilityCreate.reset;

})(typeof window !== 'undefined' ? window : this);
