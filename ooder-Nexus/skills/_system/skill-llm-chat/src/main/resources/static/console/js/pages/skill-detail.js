(function(global) {
    'use strict';

    var skillId = null;
    var skillData = null;
    var activationProgress = null;

    var OWNERSHIP_CONFIG = {
        'PLATFORM': { name: '平台能力', icon: 'ri-cloud-line' },
        'INDEPENDENT': { name: '独立能力', icon: 'ri-box-3-line' },
        'SCENE_INTERNAL': { name: '场景内置', icon: 'ri-layout-grid-line' }
    };

    var STATUS_CONFIG = {
        'REGISTERED': { name: '已注册', class: 'status-badge--registered' },
        'ENABLED': { name: '已启用', class: 'status-badge--registered' },
        'ACTIVE': { name: '活跃', class: 'status-badge--registered' },
        'INSTALLED': { name: '已安装', class: 'status-badge--installed' },
        'DISABLED': { name: '已禁用', class: 'status-badge--disabled' }
    };

    var SkillDetailPage = {
        init: function() {
            this.parseUrlParams();
            if (skillId) {
                this.loadSkillDetail();
            } else {
                this.showError('缺少 Skill ID 参数');
            }
            this.bindEvents();
        },

        parseUrlParams: function() {
            var params = new URLSearchParams(window.location.search);
            skillId = params.get('id');
        },

        bindEvents: function() {
            var self = this;
            document.querySelectorAll('.tab-btn').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    self.switchTab(this.getAttribute('data-tab'));
                });
            });
            
            var activateBtn = document.getElementById('btn-activate');
            if (activateBtn) {
                activateBtn.addEventListener('click', function() {
                    self.showActivationDialog();
                });
            }
            
            var deactivateBtn = document.getElementById('btn-deactivate');
            if (deactivateBtn) {
                deactivateBtn.addEventListener('click', function() {
                    self.deactivateSkill();
                });
            }
        },

        switchTab: function(tabId) {
            document.querySelectorAll('.tab-btn').forEach(function(btn) {
                btn.classList.remove('active');
                if (btn.getAttribute('data-tab') === tabId) {
                    btn.classList.add('active');
                }
            });

            document.querySelectorAll('.tab-content').forEach(function(content) {
                content.classList.remove('active');
            });

            var panel = document.getElementById('tab-' + tabId);
            if (panel) {
                panel.classList.add('active');
            }
        },

        loadSkillDetail: function() {
            var self = this;
            console.log('[loadSkillDetail] Loading skill detail for:', skillId);
            
            ApiClient.get('/api/v1/capabilities/' + skillId)
                .then(function(result) {
                    console.log('[loadSkillDetail] API result:', result);
                    if (result.status === 'success' && result.data) {
                        skillData = result.data;
                        self.render();
                        self.loadBindings();
                    } else {
                        self.showError(result.message || 'Skill 不存在');
                    }
                })
                .catch(function(error) {
                    console.error('[loadSkillDetail] Error:', error);
                    self.showError('加载失败: ' + (error.message || '网络请求失败'));
                });
        },

        loadBindings: function() {
            var self = this;
            
            ApiClient.get('/api/v1/capabilities/bindings')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        var bindings = result.data.filter(function(b) {
                            return b.capabilityId === skillId || b.capId === skillId;
                        });
                        self.renderBindings(bindings);

                        var sceneGroups = [];
                        bindings.forEach(function(b) {
                            if (b.sceneGroupId && sceneGroups.indexOf(b.sceneGroupId) === -1) {
                                sceneGroups.push(b.sceneGroupId);
                            }
                        });
                        self.renderSceneGroups(sceneGroups);
                    }
                })
                .catch(function(e) {
                    console.error('Failed to load bindings:', e);
                });
        },

        render: function() {
            document.getElementById('loading-state').style.display = 'none';
            document.getElementById('content-area').style.display = 'block';

            var name = skillData.name || skillId;
            var version = skillData.version || '-';
            var status = skillData.status || 'UNKNOWN';
            var icon = skillData.icon || 'ri-puzzle-line';

            document.getElementById('pageTitle').textContent = name + ' - Skill 详情';
            document.getElementById('skill-name').textContent = name;
            document.getElementById('skill-id').textContent = skillId;
            document.getElementById('skill-version').textContent = 'v' + version;
            document.getElementById('skill-icon').innerHTML = '<i class="' + icon + '"></i>';

            var statusConfig = STATUS_CONFIG[status] || STATUS_CONFIG['DISABLED'];
            document.getElementById('skill-status').textContent = status;
            document.getElementById('skill-status').className = 'status-badge ' + statusConfig.class;

            var skillForm = skillData.skillForm || skillData.capabilityType || '-';
            document.getElementById('skill-form').textContent = skillForm;

            document.getElementById('skill-description').textContent = skillData.description || '暂无描述';

            document.getElementById('info-id').textContent = skillId;
            document.getElementById('info-name').textContent = name;
            document.getElementById('info-version').textContent = version;
            document.getElementById('info-skill-form').textContent = skillForm;
            document.getElementById('info-scene-type').textContent = skillData.sceneType || '-';
            document.getElementById('info-status').textContent = status;
            document.getElementById('info-ownership').textContent = this.getOwnershipText(skillData.ownership);
            document.getElementById('info-visibility').textContent = skillData.visibility || 'PUBLIC';
            document.getElementById('info-category').textContent = skillData.category || skillData.capabilityCategory || '-';

            this.updateActionButtons(status);

            if (skillData.createdAt) {
                document.getElementById('info-create-time').textContent = this.formatTime(skillData.createdAt);
            }
            if (skillData.updatedAt) {
                document.getElementById('info-update-time').textContent = this.formatTime(skillData.updatedAt);
            }

            this.renderDependencies();
            this.renderTags();
            this.renderSceneTypes();
            this.renderParameters();
            this.renderConfig();
            this.renderMetadata();
        },

        getOwnershipText: function(ownership) {
            if (!ownership) return '-';
            var config = OWNERSHIP_CONFIG[ownership];
            return config ? config.name : ownership;
        },

        updateActionButtons: function(status) {
            var activateBtn = document.getElementById('btn-activate');
            var deactivateBtn = document.getElementById('btn-deactivate');
            
            if (!activateBtn || !deactivateBtn) return;
            
            var canActivate = ['REGISTERED', 'INSTALLED', 'DISABLED'].indexOf(status) !== -1;
            var canDeactivate = ['ACTIVE', 'ENABLED'].indexOf(status) !== -1;
            
            activateBtn.style.display = canActivate ? 'inline-flex' : 'none';
            deactivateBtn.style.display = canDeactivate ? 'inline-flex' : 'none';
        },

        renderDependencies: function() {
            var deps = skillData.dependencies || skillData.capabilities || [];
            var container = document.getElementById('dependencies-list');

            if (deps.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-link"></i><p>无依赖</p></div>';
                return;
            }

            var html = '<div class="tag-list">';
            deps.forEach(function(dep) {
                var depName = typeof dep === 'string' ? dep : (dep.name || dep.capabilityId || dep);
                html += '<span class="tag">' + depName + '</span>';
            });
            html += '</div>';
            container.innerHTML = html;
        },

        renderTags: function() {
            var tags = skillData.tags || [];
            var container = document.getElementById('tags-list');

            if (tags.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-price-tag-3-line"></i><p>无标签</p></div>';
                return;
            }

            var html = '<div class="tag-list">';
            tags.forEach(function(tag) {
                html += '<span class="tag tag--primary">' + tag + '</span>';
            });
            html += '</div>';
            container.innerHTML = html;
        },

        renderSceneTypes: function() {
            var types = skillData.supportedSceneTypes || [];
            var container = document.getElementById('scene-types-list');

            if (types.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-building-line"></i><p>无场景类型</p></div>';
                return;
            }

            var html = '<div class="tag-list">';
            types.forEach(function(type) {
                html += '<span class="tag">' + type + '</span>';
            });
            html += '</div>';
            container.innerHTML = html;
        },

        renderParameters: function() {
            var params = skillData.parameters || [];
            var inputContainer = document.getElementById('params-input');

            if (params.length === 0) {
                inputContainer.innerHTML = '<div class="empty-state"><i class="ri-input-method-line"></i><p>无输入参数定义</p></div>';
            } else {
                var html = '<table class="param-table"><thead><tr>';
                html += '<th>参数名</th><th>类型</th><th>必填</th><th>默认值</th><th>描述</th>';
                html += '</tr></thead><tbody>';

                params.forEach(function(p) {
                    html += '<tr>';
                    html += '<td><span class="param-name">' + (p.name || '-') + '</span></td>';
                    html += '<td><span class="param-type">' + (p.type || 'any') + '</span></td>';
                    html += '<td>' + (p.required ? '<span class="param-required">必填</span>' : '可选') + '</td>';
                    html += '<td>' + (p.defaultValue !== null && p.defaultValue !== undefined ? String(p.defaultValue) : '-') + '</td>';
                    html += '<td>' + (p.description || '-') + '</td>';
                    html += '</tr>';
                });

                html += '</tbody></table>';
                inputContainer.innerHTML = html;
            }

            var returns = skillData.returns;
            var outputContainer = document.getElementById('params-output');

            if (!returns) {
                outputContainer.innerHTML = '<div class="empty-state"><i class="ri-output-method-line"></i><p>无返回值定义</p></div>';
            } else {
                var html = '<div class="info-grid-2col">';
                html += '<div class="info-item"><span class="info-item-label">返回类型</span><span class="info-item-value info-item-value--mono">' + (returns.type || 'any') + '</span></div>';
                html += '</div>';

                if (returns.properties && Object.keys(returns.properties).length > 0) {
                    html += '<table class="param-table" style="margin-top: 12px;"><thead><tr><th>属性名</th><th>类型</th></tr></thead><tbody>';
                    for (var key in returns.properties) {
                        if (returns.properties.hasOwnProperty(key)) {
                            html += '<tr><td><span class="param-name">' + key + '</span></td><td>' + returns.properties[key] + '</td></tr>';
                        }
                    }
                    html += '</tbody></table>';
                }

                outputContainer.innerHTML = html;
            }
        },

        renderConfig: function() {
            var config = skillData.config || skillData.metadata;
            var configArea = document.getElementById('config-area');

            if (config && Object.keys(config).length > 0) {
                configArea.textContent = JSON.stringify(config, null, 2);
            } else {
                configArea.textContent = '暂无配置';
            }

            var llmConfig = document.getElementById('llm-config-area');
            if (skillData.metadata && (skillData.metadata.llmConfig || skillData.metadata.llm)) {
                var llm = skillData.metadata.llmConfig || skillData.metadata.llm;
                llmConfig.innerHTML = '<div class="json-block">' + JSON.stringify(llm, null, 2) + '</div>';
            } else {
                llmConfig.innerHTML = '<div class="empty-state"><i class="ri-robot-line"></i><p>无 LLM 配置</p></div>';
            }

            var driverConfig = document.getElementById('driver-config-area');
            if (skillData.driverType || skillData.driverConditions || skillData.mainFirstConfig) {
                var driver = {
                    driverType: skillData.driverType,
                    driverConditions: skillData.driverConditions,
                    mainFirst: skillData.mainFirst,
                    mainFirstConfig: skillData.mainFirstConfig
                };
                driverConfig.innerHTML = '<div class="json-block">' + JSON.stringify(driver, null, 2) + '</div>';
            } else {
                driverConfig.innerHTML = '<div class="empty-state"><i class="ri-flashlight-line"></i><p>无驱动配置</p></div>';
            }
        },

        renderMetadata: function() {
            var metadata = skillData.metadata || skillData;
            var metadataArea = document.getElementById('metadata-area');
            metadataArea.textContent = JSON.stringify(metadata, null, 2);
        },

        renderBindings: function(bindings) {
            var container = document.getElementById('bindings-list');

            if (!bindings || bindings.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-link"></i><p>暂无能力绑定</p></div>';
                return;
            }

            var html = '';
            bindings.forEach(function(b) {
                var providerType = b.providerType || '-';
                var providerId = b.providerId || b.agentId || '-';
                var sceneGroupId = b.sceneGroupId || '-';
                var status = b.status || 'UNKNOWN';

                html += '<div class="binding-item">' +
                    '<div>' +
                        '<div class="nx-font-medium">Provider: ' + providerType + '</div>' +
                        '<div class="nx-text-sm nx-text-secondary">Agent: ' + providerId + '</div>' +
                        '<div class="nx-text-sm nx-text-secondary">场景组: <a href="scene-group-detail.html?id=' + sceneGroupId + '" class="binding-link">' + sceneGroupId + '</a></div>' +
                    '</div>' +
                    '<span class="nx-badge ' + (status === 'ACTIVE' ? 'nx-badge--success' : 'nx-badge--secondary') + '">' + status + '</span>' +
                '</div>';
            });
            container.innerHTML = html;
        },

        renderSceneGroups: function(sceneGroups) {
            var container = document.getElementById('scene-groups-list');

            if (!sceneGroups || sceneGroups.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-group-line"></i><p>暂无关联的场景组</p></div>';
                return;
            }

            var html = '';
            sceneGroups.forEach(function(sgId) {
                html += '<div class="binding-item">' +
                    '<a href="scene-group-detail.html?id=' + sgId + '" class="binding-link">' +
                        '<i class="ri-group-line"></i> ' + sgId +
                    '</a>' +
                '</div>';
            });
            container.innerHTML = html;
        },

        formatTime: function(timestamp) {
            if (!timestamp) return '-';
            return new Date(timestamp).toLocaleString('zh-CN');
        },

        showActivationDialog: function() {
            var self = this;
            
            var modalHtml = '<div class="modal-overlay" id="activation-modal">' +
                '<div class="modal-content">' +
                    '<div class="modal-header">' +
                        '<h3><i class="ri-play-circle-line"></i> 激活技能</h3>' +
                        '<button class="modal-close" onclick="SkillDetailPage.closeModal()">&times;</button>' +
                    '</div>' +
                    '<div class="modal-body">' +
                        '<div class="form-group">' +
                            '<label>选择模板</label>' +
                            '<select id="activation-template" class="form-control">' +
                                '<option value="recruitment-scene">招聘管理场景</option>' +
                            '</select>' +
                        '</div>' +
                        '<div class="form-group">' +
                            '<label>选择角色</label>' +
                            '<select id="activation-role" class="form-control">' +
                                '<option value="MANAGER">管理者</option>' +
                                '<option value="HR">HR</option>' +
                                '<option value="INTERVIEWER">面试官</option>' +
                            '</select>' +
                        '</div>' +
                        '<div id="activation-progress-container"></div>' +
                    '</div>' +
                    '<div class="modal-footer">' +
                        '<button class="nx-btn nx-btn--ghost" onclick="SkillDetailPage.closeModal()">取消</button>' +
                        '<button class="nx-btn nx-btn--primary" id="btn-start-activation" onclick="SkillDetailPage.startActivation()">' +
                            '<i class="ri-play-line"></i> 开始激活' +
                        '</button>' +
                    '</div>' +
                '</div>' +
            '</div>';
            
            var modalContainer = document.createElement('div');
            modalContainer.innerHTML = modalHtml;
            document.body.appendChild(modalContainer);
        },

        closeModal: function() {
            var modal = document.getElementById('activation-modal');
            if (modal) {
                modal.parentElement.remove();
            }
            if (activationProgress) {
                activationProgress.destroy();
                activationProgress = null;
            }
        },

        startActivation: function() {
            var self = this;
            var templateId = document.getElementById('activation-template').value;
            var roleName = document.getElementById('activation-role').value;
            
            var startBtn = document.getElementById('btn-start-activation');
            if (startBtn) {
                startBtn.disabled = true;
                startBtn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 启动中...';
            }
            
            var progressContainer = document.getElementById('activation-progress-container');
            if (progressContainer) {
                progressContainer.innerHTML = '<div class="activation-progress-loading">' +
                    '<i class="ri-loader-4-line ri-spin"></i> 正在启动激活流程...' +
                '</div>';
            }
            
            var installId = 'install-' + skillId + '-' + Date.now();
            
            ApiClient.post('/api/v1/activations/' + installId + '/start-with-template', {
                templateId: templateId,
                sceneGroupId: null,
                activator: 'default-user',
                roleName: roleName
            }).then(function(result) {
                if (result.status === 'success' && result.data) {
                    return ApiClient.post('/api/v1/activations/' + installId + '/auto-activate');
                } else {
                    throw new Error(result.message || '启动激活失败');
                }
            }).then(function(result) {
                if (result.status === 'success' && result.data) {
                    self.showActivationProgress(installId);
                } else {
                    self.showActivationError(result.message || '激活失败');
                }
            }).catch(function(error) {
                self.showActivationError(error.message || '网络请求失败');
            });
        },

        showActivationProgress: function(installId) {
            var progressContainer = document.getElementById('activation-progress-container');
            if (!progressContainer) return;
            
            progressContainer.innerHTML = '<div id="activation-progress-' + installId + '"></div>';
            
            if (typeof ActivationProgress !== 'undefined') {
                activationProgress = Object.assign({}, ActivationProgress);
                activationProgress.init('activation-progress-' + installId, installId, skillId);
                activationProgress.onCompleteCallback = function(process) {
                    if (process.status === 'COMPLETED') {
                        setTimeout(function() {
                            SkillDetailPage.closeModal();
                            SkillDetailPage.loadSkillDetail();
                        }, 2000);
                    }
                };
            } else {
                this.pollActivationProgress(installId);
            }
        },

        pollActivationProgress: function(installId) {
            var self = this;
            var progressContainer = document.getElementById('activation-progress-container');
            
            var poll = function() {
                ApiClient.get('/api/v1/activations/' + installId + '/process')
                    .then(function(result) {
                        if (result.status === 'success' && result.data) {
                            var process = result.data;
                            self.updateProgressUI(process);
                            
                            if (process.status === 'IN_PROGRESS') {
                                setTimeout(poll, 1000);
                            } else if (process.status === 'COMPLETED') {
                                setTimeout(function() {
                                    self.closeModal();
                                    self.loadSkillDetail();
                                }, 2000);
                            }
                        }
                    });
            };
            
            poll();
        },

        updateProgressUI: function(process) {
            var progressContainer = document.getElementById('activation-progress-container');
            if (!progressContainer) return;
            
            var percentage = process.totalSteps > 0 ? 
                Math.round((process.currentStep / process.totalSteps) * 100) : 0;
            
            var html = '<div class="activation-progress status-' + process.status.toLowerCase() + '">' +
                '<div class="progress-header">' +
                    '<h4>激活进度</h4>' +
                    '<span class="progress-status">' + this.getStatusText(process.status) + '</span>' +
                '</div>' +
                '<div class="progress-bar-container">' +
                    '<div class="progress-bar" style="width: ' + percentage + '%"></div>' +
                '</div>' +
                '<div class="progress-percentage">' + percentage + '%</div>' +
                '<div class="progress-steps">';
            
            if (process.steps) {
                process.steps.forEach(function(step) {
                    html += '<div class="step-item step-' + step.status.toLowerCase() + '">' +
                        '<span class="step-name">' + (step.name || step.stepId) + '</span>' +
                        '<span class="step-status">' + step.status + '</span>' +
                    '</div>';
                });
            }
            
            html += '</div></div>';
            progressContainer.innerHTML = html;
        },

        getStatusText: function(status) {
            var map = {
                'PENDING': '等待中',
                'IN_PROGRESS': '进行中',
                'COMPLETED': '已完成',
                'FAILED': '失败',
                'CANCELLED': '已取消'
            };
            return map[status] || status;
        },

        showActivationError: function(message) {
            var progressContainer = document.getElementById('activation-progress-container');
            if (progressContainer) {
                progressContainer.innerHTML = '<div class="activation-error">' +
                    '<i class="ri-error-warning-line"></i>' +
                    '<p>' + message + '</p>' +
                '</div>';
            }
            
            var startBtn = document.getElementById('btn-start-activation');
            if (startBtn) {
                startBtn.disabled = false;
                startBtn.innerHTML = '<i class="ri-play-line"></i> 重试';
            }
        },

        deactivateSkill: function() {
            if (!confirm('确定要停用此技能吗？')) {
                return;
            }
            
            var self = this;
            
            ApiClient.post('/api/v1/capabilities/' + skillId + '/deactivate')
                .then(function(result) {
                    if (result.status === 'success') {
                        alert('技能已停用');
                        self.loadSkillDetail();
                    } else {
                        alert('停用失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    alert('停用失败: ' + error.message);
                });
        },

        showError: function(message) {
            document.getElementById('loading-state').innerHTML = 
                '<div class="empty-state">' +
                    '<i class="ri-error-warning-line" style="font-size: 48px; color: var(--nx-danger);"></i>' +
                    '<h3>' + message + '</h3>' +
                    '<p><a href="capability-management.html">返回能力管理</a></p>' +
                '</div>';
        }
    };

    global.SkillDetailPage = SkillDetailPage;

    function initWhenReady() {
        console.log('[initWhenReady] Checking ApiClient...', typeof ApiClient);
        if (typeof ApiClient !== 'undefined') {
            console.log('[initWhenReady] ApiClient ready, initializing...');
            SkillDetailPage.init();
        } else {
            console.log('[initWhenReady] ApiClient not ready, waiting...');
            setTimeout(initWhenReady, 100);
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initWhenReady);
    } else {
        initWhenReady();
    }

})(typeof window !== 'undefined' ? window : this);
