(function(global) {
    'use strict';

    var capabilityId = null;
    var capability = null;
    var lifecycleState = null;

    var CATEGORY_CONFIG = {
        'FULL': { 
            icon: 'ri-layout-grid-line', 
            name: '完整场景技能',
            description: '具备完整自驱能力的业务场景',
            color: '#2563eb',
            installHint: '安装后将自动激活并按计划运行'
        },
        'TECHNICAL': { 
            icon: 'ri-settings-4-line', 
            name: '技术场景技能',
            description: '系统内部技术流程场景',
            color: '#d97706',
            installHint: '安装后将自动执行系统维护任务'
        },
        'SEMI_AUTO': { 
            icon: 'ri-hand-coin-line', 
            name: '半自动场景技能',
            description: '需要外部触发的业务场景',
            color: '#db2777',
            installHint: '安装后需要手动触发执行'
        },
        'default': { 
            icon: 'ri-flashlight-line', 
            name: '其他',
            description: '',
            color: '#64748b',
            installHint: ''
        }
    };

    var SceneCapabilityDetail = {
        init: function() {
            window.onPageInit = function() {
                console.log('[SceneCapabilityDetail] 页面初始化完成');
                SceneCapabilityDetail.parseUrlParams();
                SceneCapabilityDetail.loadCapability();
            };
        },

        parseUrlParams: function() {
            var urlParams = new URLSearchParams(window.location.search);
            capabilityId = urlParams.get('id');
            console.log('[SceneCapabilityDetail] capabilityId:', capabilityId);
        },

        loadCapability: function() {
            if (!capabilityId) {
                document.getElementById('capabilityName').textContent = '未找到场景能力';
                document.getElementById('capabilityDesc').textContent = '请从场景能力列表中选择一个能力查看详情';
                return;
            }

            ApiClient.post('/api/v1/discovery/gitee', { repoUrl: 'https://gitee.com/ooderCN/skills' })
                .then(function(result) {
                    var capabilities = [];
                    if (result.status === 'success' && result.data) {
                        capabilities = result.data.capabilities || [];
                    } else if (result.status === 'success' && result.data) {
                        capabilities = result.data.capabilities || [];
                    }
                    
                    capability = capabilities.find(function(c) { 
                        return c.id === capabilityId || c.skillId === capabilityId; 
                    });
                    
                    if (capability) {
                        SceneCapabilityDetail.renderCapability();
                        SceneCapabilityDetail.loadLifecycleState();
                    } else {
                        document.getElementById('capabilityName').textContent = '场景能力不存在';
                        document.getElementById('capabilityDesc').textContent = 'ID: ' + capabilityId;
                    }
                })
                .catch(function(error) {
                    console.error('[SceneCapabilityDetail] 加载失败:', error);
                    document.getElementById('capabilityName').textContent = '加载失败';
                    document.getElementById('capabilityDesc').textContent = error.message;
                });
        },

        loadLifecycleState: function() {
            if (!capability || capability.status !== 'installed') {
                return;
            }
            
            ApiClient.get('/api/v1/scene-capabilities/' + capabilityId + '/state')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        lifecycleState = result.data;
                        SceneCapabilityDetail.renderLifecycleControls();
                    }
                })
                .catch(function(error) {
                    console.log('[SceneCapabilityDetail] 生命周期状态加载失败:', error);
                });
        },

        renderCapability: function() {
            var category = capability.category || 'default';
            var categoryConfig = CATEGORY_CONFIG[category] || CATEGORY_CONFIG['default'];
            
            document.getElementById('capabilityTitle').textContent = capability.name || capability.id;
            document.getElementById('capabilityId').textContent = capability.id;
            document.getElementById('capabilityIcon').innerHTML = '<i class="' + categoryConfig.icon + '"></i>';
            document.getElementById('capabilityIcon').className = 'capability-icon-large ' + category;
            document.getElementById('capabilityName').textContent = capability.name || capability.id;
            document.getElementById('capabilityDesc').textContent = capability.description || '暂无描述';
            
            var tagsHtml = '';
            if (category !== 'default') {
                tagsHtml += '<span class="tag" style="background: ' + categoryConfig.color + '20; color: ' + categoryConfig.color + '">' + categoryConfig.name + '</span>';
            }
            if (capability.mainFirst !== undefined) {
                tagsHtml += '<span class="tag">' + (capability.mainFirst ? '自驱' : '手动触发') + '</span>';
            }
            if (capability.visibility) {
                tagsHtml += '<span class="tag">' + (capability.visibility === 'internal' ? '内部' : '公开') + '</span>';
            }
            if (capability.version) {
                tagsHtml += '<span class="tag">v' + capability.version + '</span>';
            }
            tagsHtml += '<span class="tag">' + (capability.source || 'Gitee') + '</span>';
            document.getElementById('capabilityTags').innerHTML = tagsHtml;

            var statusBadge = document.getElementById('statusBadge');
            var isInstalled = capability.status === 'installed';
            statusBadge.className = 'status-badge-large ' + (isInstalled ? 'installed' : 'available');
            statusBadge.innerHTML = '<i class="ri-' + (isInstalled ? 'checkbox-circle' : 'download') + '-line"></i> ' + 
                (isInstalled ? '已安装' : '可安装');

            document.getElementById('version').textContent = capability.version || '1.0.0';
            document.getElementById('category').textContent = categoryConfig.name;
            document.getElementById('mainFirst').textContent = capability.mainFirst !== undefined 
                ? (capability.mainFirst ? '自驱' : '手动触发') : '-';
            document.getElementById('visibility').textContent = capability.visibility 
                ? (capability.visibility === 'internal' ? '内部' : '公开') : '公开';
            document.getElementById('source').textContent = capability.source || 'Gitee';
            document.getElementById('installTime').textContent = capability.installTime ? 
                new Date(capability.installTime).toLocaleDateString() : '-';

            var categoryBadgeContainer = document.getElementById('categoryBadgeContainer');
            if (category !== 'default') {
                categoryBadgeContainer.innerHTML = '<span class="category-badge-large ' + category + '">' +
                    '<i class="' + categoryConfig.icon + '"></i> ' + categoryConfig.name + '</span>';
            } else {
                categoryBadgeContainer.innerHTML = '';
            }

            var actionButtons = document.getElementById('actionButtons');
            if (isInstalled) {
                actionButtons.innerHTML = 
                    '<button class="nx-btn nx-btn--primary" onclick="useCapability()"><i class="ri-play-line"></i> 立即使用</button>' +
                    '<button class="nx-btn nx-btn--secondary" onclick="createSceneGroup()"><i class="ri-add-line"></i> 创建场景组</button>' +
                    '<button class="nx-btn nx-btn--ghost" onclick="uninstallCapability()"><i class="ri-delete-bin-line"></i> 卸载</button>';
            } else {
                actionButtons.innerHTML = 
                    '<button class="nx-btn nx-btn--primary" onclick="installCapability()"><i class="ri-download-line"></i> 安装</button>' +
                    '<button class="nx-btn nx-btn--secondary" onclick="viewSource()"><i class="ri-github-line"></i> 查看源码</button>';
            }

            if (!isInstalled && categoryConfig.installHint) {
                var hintBox = document.getElementById('installHintBox');
                hintBox.style.display = 'flex';
                document.getElementById('installHintText').textContent = categoryConfig.installHint;
            } else {
                document.getElementById('installHintBox').style.display = 'none';
            }

            SceneCapabilityDetail.renderFeatures();
            SceneCapabilityDetail.renderDependencies();
            SceneCapabilityDetail.renderDriverConditions();
            SceneCapabilityDetail.renderParticipants();
        },

        renderDriverConditions: function() {
            var conditions = capability.driverConditions || [];
            var card = document.getElementById('driverConditionsCard');
            var list = document.getElementById('driverConditionsList');
            
            if (conditions.length === 0) {
                card.style.display = 'none';
                return;
            }
            
            card.style.display = 'block';
            var html = '';
            conditions.forEach(function(condition) {
                var type = condition.type || 'schedule';
                var icon = type === 'schedule' ? 'ri-timer-line' : 
                           type === 'event' ? 'ri-notification-3-line' : 
                           type === 'manual' ? 'ri-hand-coin-line' : 'ri-flashlight-line';
                
                html += '<div class="driver-condition-item">' +
                    '<i class="' + icon + '"></i>' +
                    '<span>' + (condition.name || condition.type || '驱动条件') + '</span>' +
                    '</div>';
            });
            list.innerHTML = html;
        },

        renderParticipants: function() {
            var participants = capability.participants || [];
            var card = document.getElementById('participantsCard');
            var list = document.getElementById('participantsList');
            
            if (participants.length === 0) {
                card.style.display = 'none';
                return;
            }
            
            card.style.display = 'block';
            var html = '';
            participants.forEach(function(participant) {
                var role = participant.role || 'PARTICIPANT';
                var icon = role === 'LEADER' ? 'ri-user-star-line' : 
                           role === 'COLLABORATOR' ? 'ri-user-line' : 
                           'ri-user-3-line';
                
                html += '<div class="participant-item">' +
                    '<i class="' + icon + '"></i>' +
                    '<span>' + (participant.name || role) + '</span>' +
                    '<span class="tag">' + role + '</span>' +
                    '</div>';
            });
            list.innerHTML = html;
        },

        renderLifecycleControls: function() {
            if (!lifecycleState) {
                document.getElementById('lifecycleSection').style.display = 'none';
                return;
            }
            
            var section = document.getElementById('lifecycleSection');
            var actions = document.getElementById('lifecycleActions');
            
            section.style.display = 'block';
            
            var html = '';
            
            if (lifecycleState.canPause) {
                html += '<button class="lifecycle-btn" onclick="pauseCapability()">' +
                    '<i class="ri-pause-line"></i> 暂停</button>';
            }
            
            if (lifecycleState.canResume) {
                html += '<button class="lifecycle-btn primary" onclick="resumeCapability()">' +
                    '<i class="ri-play-line"></i> 恢复</button>';
            }
            
            if (lifecycleState.canTrigger) {
                html += '<button class="lifecycle-btn primary" onclick="triggerCapability()">' +
                    '<i class="ri-flashlight-line"></i> 触发执行</button>';
            }
            
            if (lifecycleState.canArchive) {
                html += '<button class="lifecycle-btn danger" onclick="archiveCapability()">' +
                    '<i class="ri-archive-line"></i> 归档</button>';
            }
            
            actions.innerHTML = html || '<span style="color: var(--nx-text-secondary); font-size: 12px;">当前状态无可用操作</span>';
        },

        renderFeatures: function() {
            var features = capability.capabilities || [];
            var featureList = document.getElementById('featureList');
            
            if (features.length === 0) {
                featureList.innerHTML = '<div class="capability-desc">暂无功能列表</div>';
                return;
            }

            var html = '';
            features.forEach(function(feature) {
                html += '<div class="feature-item">' +
                    '<div class="feature-icon"><i class="ri-checkbox-circle-line"></i></div>' +
                    '<div class="feature-info">' +
                    '<div class="feature-name">' + (feature.name || feature) + '</div>' +
                    '<div class="feature-desc">' + (feature.description || '功能能力') + '</div>' +
                    '</div></div>';
            });
            featureList.innerHTML = html;
        },

        renderDependencies: function() {
            var dependencies = capability.dependencies || capability.requiredSkills || [];
            var dependencyList = document.getElementById('dependencyList');
            
            if (dependencies.length === 0) {
                dependencyList.innerHTML = '<div class="capability-desc">无依赖技能</div>';
                return;
            }

            var html = '';
            dependencies.forEach(function(dep) {
                var depId = dep.id || dep;
                var depName = dep.name || depId;
                var isInstalled = dep.required !== false;
                
                html += '<div class="dependency-item">' +
                    '<div class="dependency-info">' +
                    '<div class="dependency-status ' + (isInstalled ? 'installed' : 'missing') + '"></div>' +
                    '<span>' + depName + '</span>' +
                    '</div>' +
                    '<span class="tag">' + (dep.version || 'any') + '</span>' +
                    '</div>';
            });
            dependencyList.innerHTML = html;
        }
    };

    SceneCapabilityDetail.init();

    global.goBack = function() {
        window.history.back();
    };

    global.switchTab = function(tabId) {
        document.querySelectorAll('.tab-item').forEach(function(item) {
            item.classList.remove('active');
            if (item.dataset.tab === tabId) {
                item.classList.add('active');
            }
        });
        
        document.querySelectorAll('.tab-panel').forEach(function(panel) {
            panel.classList.remove('active');
        });
        document.getElementById(tabId + 'Panel').classList.add('active');
    };

    global.useCapability = function() {
        if (!capability) return;
        window.location.href = '/console/pages/scene-group-detail.html?templateId=' + capability.id;
    };

    global.createSceneGroup = function() {
        if (!capability) return;
        window.location.href = '/console/pages/scene-group-management.html?action=create&templateId=' + capability.id;
    };

    global.installCapability = function() {
        if (!capability) return;
        
        var btn = document.querySelector('.nx-btn--primary');
        var originalHtml = btn.innerHTML;
        btn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i> 安装中...';
        btn.disabled = true;

        ApiClient.post('/api/v1/discovery/install', { skillId: capability.id, source: capability.source || 'GITEE' })
            .then(function(result) {
                if ((result.status === 'success') && result.data) {
                    capability.status = 'installed';
                    SceneCapabilityDetail.renderCapability();
                    SceneCapabilityDetail.loadLifecycleState();
                } else {
                    btn.innerHTML = originalHtml;
                    btn.disabled = false;
                    alert('安装失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                btn.innerHTML = originalHtml;
                btn.disabled = false;
                alert('安装失败: ' + error.message);
            });
    };

    global.uninstallCapability = function() {
        if (!capability) return;
        if (!confirm('确定要卸载此场景能力吗？此操作不可恢复。')) return;
        
        var sceneGroupId = capability.sceneGroupId;
        var capabilityId = capability.id;
        
        fetch('/api/v1/scene-capabilities/' + capabilityId + '/uninstall', {
            method: 'POST'
        })
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('卸载成功');
                    window.location.href = 'scene-group-detail.html?id=' + sceneGroupId;
                } else {
                    alert('卸载失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                console.error('Failed to uninstall:', error);
                alert('卸载失败: ' + error.message);
            });
    };

    global.viewSource = function() {
        if (!capability) return;
        var url = capability.repoUrl || capability.downloadUrl || 
            'https://gitee.com/ooderCN/skills/tree/main/skills/' + capability.id;
        window.open(url, '_blank');
    };

    global.pauseCapability = function() {
        if (!capability) return;
        
        ApiClient.post('/api/v1/scene-capabilities/' + capability.id + '/pause')
            .then(function(result) {
                if (result.status === 'success') {
                    alert('场景已暂停');
                    SceneCapabilityDetail.loadLifecycleState();
                } else {
                    alert('暂停失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                alert('暂停失败: ' + error.message);
            });
    };

    global.resumeCapability = function() {
        if (!capability) return;
        
        ApiClient.post('/api/v1/scene-capabilities/' + capability.id + '/resume')
            .then(function(result) {
                if (result.status === 'success') {
                    alert('场景已恢复');
                    SceneCapabilityDetail.loadLifecycleState();
                } else {
                    alert('恢复失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                alert('恢复失败: ' + error.message);
            });
    };

    global.triggerCapability = function() {
        if (!capability) return;
        
        var action = prompt('请输入触发动作:', 'default');
        if (!action) return;
        
        ApiClient.post('/api/v1/scene-capabilities/' + capability.id + '/trigger', { action: action })
            .then(function(result) {
                if (result.status === 'success') {
                    alert('场景已触发执行');
                    SceneCapabilityDetail.loadLifecycleState();
                } else {
                    alert('触发失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                alert('触发失败: ' + error.message);
            });
    };

    global.archiveCapability = function() {
        if (!capability) return;
        if (!confirm('确定要归档此场景能力吗？归档后将无法恢复。')) return;
        
        ApiClient.post('/api/v1/scene-capabilities/' + capability.id + '/archive')
            .then(function(result) {
                if (result.status === 'success') {
                    alert('场景已归档');
                    window.history.back();
                } else {
                    alert('归档失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                alert('归档失败: ' + error.message);
            });
    };

})(typeof window !== 'undefined' ? window : this);
