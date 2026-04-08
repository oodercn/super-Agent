(function(global) {
    'use strict';

    var sceneGroups = [];
    var capabilities = [];
    var bindings = [];
    var currentSceneGroup = null;
    var selectedBinding = null;

    var TYPE_ICONS = {
        'DRIVER': 'ri-hard-drive-2-line',
        'SERVICE': 'ri-server-line',
        'MANAGEMENT': 'ri-settings-3-line',
        'AI': 'ri-brain-line',
        'STORAGE': 'ri-database-2-line',
        'COMMUNICATION': 'ri-message-3-line',
        'SECURITY': 'ri-shield-check-line',
        'MONITORING': 'ri-pulse-line',
        'SKILL': 'ri-flashlight-line',
        'SCENE': 'ri-layout-grid-line',
        'CUSTOM': 'ri-tools-line'
    };

    var TYPE_COLORS = {
        'DRIVER': 'type-SERVICE',
        'SERVICE': 'type-SERVICE',
        'MANAGEMENT': 'type-CUSTOM',
        'AI': 'type-AI',
        'STORAGE': 'type-SERVICE',
        'COMMUNICATION': 'type-COMMUNICATION',
        'SECURITY': 'type-CUSTOM',
        'MONITORING': 'type-CUSTOM',
        'SKILL': 'type-SKILL',
        'SCENE': 'type-SCENE',
        'CUSTOM': 'type-CUSTOM'
    };

    var CapabilityBinding = {
        init: function() {
            window.onPageInit = function() {
                console.log('能力绑定页面初始化完成');
                CapabilityBinding.loadSceneGroups();
                CapabilityBinding.loadCapabilities();
                CapabilityBinding.initLlmAssistant();
                CapabilityBinding.handleUrlParams();
            };
        },

        handleUrlParams: function() {
            var urlParams = new URLSearchParams(window.location.search);
            var capabilityId = urlParams.get('capabilityId');
            if (capabilityId) {
                console.log('[handleUrlParams] capabilityId from URL:', capabilityId);
                CapabilityBinding.highlightCapability = capabilityId;
            }
        },

        initLlmAssistant: function() {
            if (typeof LlmAssistant !== 'undefined') {
                LlmAssistant.init();
            }
        },

        loadSceneGroups: function() {
            ApiClient.get('/api/selector/scene-groups')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        sceneGroups = result.data.map(function(item) {
                            return {
                                sceneGroupId: item.id,
                                name: item.name,
                                description: item.description,
                                status: item.status || 'ACTIVE'
                            };
                        });
                        CapabilityBinding.renderSceneGroups();
                    }
                })
                .catch(function(error) {
                    console.error('加载场景组失败:', error);
                    sceneGroups = [];
                    CapabilityBinding.renderSceneGroups();
                });
        },

        loadMockSceneGroups: function() {
            sceneGroups = [];
            CapabilityBinding.renderSceneGroups();
        },

        loadCapabilities: function() {
            ApiClient.get('/api/v1/capabilities')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        capabilities = result.data.map(function(item) {
                            return {
                                capabilityId: item.capabilityId || item.id,
                                name: item.name,
                                type: item.type,
                                description: item.description
                            };
                        });
                        CapabilityBinding.renderCapabilitySelect();
                    }
                })
                .catch(function(error) {
                    console.error('加载能力失败:', error);
                    capabilities = [];
                    CapabilityBinding.renderCapabilitySelect();
                });
        },

        renderSceneGroups: function() {
            var container = document.getElementById('sceneGroupList');
            if (!container) return;

            if (sceneGroups.length === 0) {
                container.innerHTML = '<div class="empty-state" style="padding: 40px 20px;">' +
                    '<i class="ri-folder-line"></i>' +
                    '<div class="empty-state-title">暂无场景组</div></div>';
                return;
            }

            var html = '';
            sceneGroups.forEach(function(group) {
                var bindingCount = bindings.filter(function(b) { 
                    return b.sceneGroupId === group.sceneGroupId; 
                }).length;

                html += '<div class="scene-group-item" data-id="' + group.sceneGroupId + '" onclick="selectSceneGroup(\'' + group.sceneGroupId + '\')">' +
                    '<div class="group-icon"><i class="ri-folder-line"></i></div>' +
                    '<div class="group-info">' +
                    '<div class="group-name">' + (group.name || group.sceneGroupId) + '</div>' +
                    '<div class="group-count">' + bindingCount + ' 个绑定</div>' +
                    '</div></div>';
            });
            container.innerHTML = html;
        },

        selectSceneGroup: function(sceneGroupId) {
            currentSceneGroup = sceneGroupId;
            selectedBinding = null;
            CapabilityBinding.hideDetailPanel();

            document.querySelectorAll('.scene-group-item').forEach(function(item) {
                item.classList.remove('active');
                if (item.dataset.id === sceneGroupId) {
                    item.classList.add('active');
                }
            });

            var group = sceneGroups.find(function(g) { return g.sceneGroupId === sceneGroupId; });
            document.getElementById('currentGroupName').textContent = group ? (group.name || sceneGroupId) : sceneGroupId;
            document.getElementById('addBindingBtn').disabled = false;

            CapabilityBinding.loadBindings(sceneGroupId);
        },

        loadBindings: function(sceneGroupId) {
            ApiClient.get('/api/v1/capabilities/bindings?sceneGroupId=' + sceneGroupId)
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        bindings = result.data.map(function(item) {
                            return {
                                bindingId: item.bindingId || item.id,
                                sceneGroupId: item.sceneGroupId,
                                capabilityId: item.capabilityId,
                                capabilityName: item.capabilityName || item.name,
                                capabilityType: item.capabilityType || item.type || 'CUSTOM',
                                providerType: item.providerType || 'SKILL',
                                priority: item.priority || 1,
                                status: item.status || 'ACTIVE',
                                lastInvokeTime: item.lastInvokeTime,
                                description: item.description,
                                connectorType: item.connectorType || 'INTERNAL',
                                createTime: item.createTime,
                                updateTime: item.updateTime
                            };
                        });
                    } else {
                        bindings = [];
                    }
                    CapabilityBinding.renderBindings();
                    CapabilityBinding.renderSceneGroups();
                })
                .catch(function(error) {
                    console.error('加载绑定失败:', error);
                    bindings = [];
                    CapabilityBinding.renderBindings();
                    CapabilityBinding.renderSceneGroups();
                });
        },

        renderBindings: function() {
            var tbody = document.getElementById('bindingTableBody');
            var filteredBindings = bindings.filter(function(b) { 
                return b.sceneGroupId === currentSceneGroup; 
            });

            if (filteredBindings.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 60px 20px; color: var(--nx-text-secondary);">' +
                    '<i class="ri-link" style="font-size: 32px; display: block; margin-bottom: 8px;"></i>' +
                    '暂无能力绑定，点击"添加绑定"开始配置</td></tr>';
                return;
            }

            var html = '';
            filteredBindings.forEach(function(binding) {
                var typeIcon = TYPE_ICONS[binding.capabilityType] || TYPE_ICONS['CUSTOM'];
                var typeClass = TYPE_COLORS[binding.capabilityType] || 'type-CUSTOM';
                var statusClass = binding.status === 'ACTIVE' ? 'active' : 'inactive';
                var statusText = binding.status === 'ACTIVE' ? '已激活' : '未激活';
                var isSelected = selectedBinding && selectedBinding.bindingId === binding.bindingId;

                html += '<tr class="' + (isSelected ? 'selected' : '') + '" onclick="selectBinding(\'' + binding.bindingId + '\')">' +
                    '<td><div class="cap-info">' +
                    '<div class="cap-icon ' + typeClass + '"><i class="' + typeIcon + '"></i></div>' +
                    '<div><div class="cap-name">' + (binding.capabilityName || binding.capabilityId) + '</div>' +
                    '<div class="cap-id">' + binding.capabilityId + '</div></div></div></td>' +
                    '<td>' + (binding.providerType || 'SKILL') + '</td>' +
                    '<td><span class="priority-badge">P' + (binding.priority || 1) + '</span></td>' +
                    '<td><span class="status-badge ' + statusClass + '">' + statusText + '</span></td>' +
                    '<td>' +
                    '<button class="action-btn" onclick="event.stopPropagation(); testBinding(\'' + binding.bindingId + '\')" title="测试">' +
                    '<i class="ri-play-line"></i></button>' +
                    '<button class="action-btn danger" onclick="event.stopPropagation(); deleteBinding(\'' + binding.bindingId + '\')" title="解绑">' +
                    '<i class="ri-link-unlink"></i></button>' +
                    '</td></tr>';
            });
            tbody.innerHTML = html;
        },

        selectBinding: function(bindingId) {
            var binding = bindings.find(function(b) { return b.bindingId === bindingId; });
            if (!binding) return;

            selectedBinding = binding;
            CapabilityBinding.renderBindings();
            CapabilityBinding.showDetailPanel(binding);
        },

        showDetailPanel: function(binding) {
            document.getElementById('emptyDetail').style.display = 'none';
            document.getElementById('detailContent').style.display = 'flex';

            var typeIcon = TYPE_ICONS[binding.capabilityType] || TYPE_ICONS['CUSTOM'];
            var typeClass = TYPE_COLORS[binding.capabilityType] || 'type-CUSTOM';
            var statusClass = binding.status === 'ACTIVE' ? 'active' : 'inactive';
            var statusText = binding.status === 'ACTIVE' ? '已激活' : '未激活';

            document.getElementById('detailIcon').className = 'detail-icon ' + typeClass;
            document.getElementById('detailIcon').innerHTML = '<i class="' + typeIcon + '"></i>';
            document.getElementById('detailTitle').textContent = binding.capabilityName || binding.capabilityId;
            document.getElementById('detailId').textContent = binding.capabilityId;
            document.getElementById('detailStatus').className = 'status-badge ' + statusClass;
            document.getElementById('detailStatus').innerHTML = '<i class="ri-checkbox-circle-line"></i> ' + statusText;
            document.getElementById('detailProviderType').textContent = binding.providerType || 'SKILL';
            document.getElementById('detailPriority').textContent = 'P' + (binding.priority || 1);
            document.getElementById('detailConnector').textContent = binding.connectorType || 'INTERNAL';
            document.getElementById('detailLastInvoke').textContent = binding.lastInvokeTime ? CapabilityBinding.formatTime(binding.lastInvokeTime) : '-';
            document.getElementById('detailDescription').textContent = binding.description || '暂无描述信息';
            document.getElementById('detailCreateTime').textContent = binding.createTime ? CapabilityBinding.formatDateTime(binding.createTime) : '-';
            document.getElementById('detailUpdateTime').textContent = binding.updateTime ? CapabilityBinding.formatDateTime(binding.updateTime) : '-';
        },

        hideDetailPanel: function() {
            document.getElementById('emptyDetail').style.display = 'flex';
            document.getElementById('detailContent').style.display = 'none';
        },

        renderCapabilitySelect: function() {
            var select = document.getElementById('capabilitySelect');
            if (!select) return;

            var html = '<option value="">请选择能力</option>';
            capabilities.forEach(function(cap) {
                html += '<option value="' + cap.capabilityId + '">' + (cap.name || cap.capabilityId) + ' (' + cap.capabilityId + ')</option>';
            });
            select.innerHTML = html;
        },

        showAddModal: function() {
            if (!currentSceneGroup) {
                alert('请先选择场景组');
                return;
            }
            document.getElementById('addBindingModal').classList.add('show');
        },

        hideAddModal: function() {
            document.getElementById('addBindingModal').classList.remove('show');
        },

        createBindingRequest: function() {
            var capabilityId = document.getElementById('capabilitySelect').value;
            var providerType = document.getElementById('providerTypeSelect').value;
            var priority = document.getElementById('priorityInput').value;
            var connectorType = document.getElementById('connectorTypeSelect').value;

            if (!capabilityId) {
                alert('请选择能力');
                return;
            }

            var cap = capabilities.find(function(c) { return c.capabilityId === capabilityId; });

            var request = {
                sceneGroupId: currentSceneGroup,
                capabilityId: capabilityId,
                providerType: providerType,
                priority: parseInt(priority) || 1,
                connectorType: connectorType
            };

            ApiClient.post('/api/v1/capabilities/bindings', request)
                .then(function(result) {
                    if (result.status === 'success') {
                        bindings.push({
                            bindingId: result.data ? result.data.bindingId : 'bind-' + Date.now(),
                            sceneGroupId: currentSceneGroup,
                            capabilityId: capabilityId,
                            capabilityName: cap ? cap.name : capabilityId,
                            capabilityType: cap ? cap.type : 'CUSTOM',
                            providerType: providerType,
                            priority: parseInt(priority) || 1,
                            status: 'ACTIVE',
                            lastInvokeTime: null
                        });
                        CapabilityBinding.renderBindings();
                        CapabilityBinding.hideAddModal();
                        CapabilityBinding.renderSceneGroups();
                    } else {
                        alert('创建绑定失败: ' + result.message);
                    }
                })
                .catch(function(error) {
                    bindings.push({
                        bindingBindingId: 'bind-' + Date.now(),
                        sceneGroupId: currentSceneGroup,
                        capabilityId: capabilityId,
                        capabilityName: cap ? cap.name : capabilityId,
                        capabilityType: cap ? cap.type : 'CUSTOM',
                        providerType: providerType,
                        priority: parseInt(priority) || 1,
                        status: 'ACTIVE',
                        lastInvokeTime: null
                    });
                    CapabilityBinding.renderBindings();
                    CapabilityBinding.hideAddModal();
                    CapabilityBinding.renderSceneGroups();
                });
        },

        deleteBindingRequest: function(bindingId) {
            if (!confirm('确定要解绑此能力吗？')) return;

            ApiClient.delete('/api/v1/capabilities/bindings/' + bindingId)
                .then(function(result) {
                    if (result.status === 'success') {
                        bindings = bindings.filter(function(b) { return b.bindingId !== bindingId; });
                        if (selectedBinding && selectedBinding.bindingId === bindingId) {
                            selectedBinding = null;
                            CapabilityBinding.hideDetailPanel();
                        }
                        CapabilityBinding.renderBindings();
                        CapabilityBinding.renderSceneGroups();
                    }
                })
                .catch(function(error) {
                    bindings = bindings.filter(function(b) { return b.bindingId !== bindingId; });
                    if (selectedBinding && selectedBinding.bindingId === bindingId) {
                        selectedBinding = null;
                        CapabilityBinding.hideDetailPanel();
                    }
                    CapabilityBinding.renderBindings();
                    CapabilityBinding.renderSceneGroups();
                });
        },

        editBindingRequest: function(bindingId) {
            var binding = bindings.find(function(b) { return b.bindingId === bindingId; });
            if (!binding) {
                alert('绑定不存在');
                return;
            }
            
            var newPriority = prompt('请输入新的优先级 (1-100):', binding.priority || 1);
            if (newPriority === null) return;
            
            var priority = parseInt(newPriority);
            if (isNaN(priority) || priority < 1 || priority > 100) {
                alert('优先级必须是 1-100 之间的数字');
                return;
            }
            
            ApiClient.put('/api/v1/capabilities/bindings/' + bindingId + '/priority', { priority: priority })
                .then(function(result) {
                    if (result.status === 'success') {
                        binding.priority = priority;
                        CapabilityBinding.renderBindings();
                        if (selectedBinding && selectedBinding.bindingId === bindingId) {
                            CapabilityBinding.showDetailPanel(binding);
                        }
                    } else {
                        alert('更新失败: ' + result.message);
                    }
                })
                .catch(function(error) {
                    binding.priority = priority;
                    CapabilityBinding.renderBindings();
                    if (selectedBinding && selectedBinding.bindingId === bindingId) {
                        CapabilityBinding.showDetailPanel(binding);
                    }
                });
        },

        testBindingRequest: function(bindingId) {
            var binding = bindings.find(function(b) { return b.bindingId === bindingId; });
            if (!binding) {
                alert('绑定不存在');
                return;
            }
            
            if (binding.status !== 'ACTIVE') {
                alert('此绑定未激活，无法测试');
                return;
            }
            
            var paramsStr = prompt('请输入测试参数 (JSON格式，留空则使用空参数):', '{}');
            if (paramsStr === null) return;
            
            var params = {};
            if (paramsStr.trim()) {
                try {
                    params = JSON.parse(paramsStr);
                } catch (e) {
                    alert('JSON格式错误: ' + e.message);
                    return;
                }
            }
            
            var btn = event.target.closest('button') || event.target;
            var originalHtml = btn.innerHTML;
            btn.innerHTML = '<i class="ri-loader-4-line ri-spin"></i>';
            btn.disabled = true;
            
            ApiClient.post('/api/v1/capabilities/bindings/' + bindingId + '/test', params)
                .then(function(result) {
                    btn.innerHTML = originalHtml;
                    btn.disabled = false;
                    
                    if (result.status === 'success' && result.data) {
                        var data = result.data;
                        var message = '测试调用结果\n\n';
                        message += '能力ID: ' + data.capabilityId + '\n';
                        message += '成功: ' + (data.success ? '是' : '否') + '\n';
                        message += '消息: ' + (data.message || '-') + '\n';
                        if (data.result) {
                            message += '\n返回数据:\n' + JSON.stringify(data.result, null, 2);
                        }
                        if (data.error) {
                            message += '\n错误: ' + data.error;
                        }
                        alert(message);
                    } else {
                        alert('测试失败: ' + (result.message || '未知错误'));
                    }
                })
                .catch(function(error) {
                    btn.innerHTML = originalHtml;
                    btn.disabled = false;
                    alert('测试失败: ' + error.message);
                });
        },

        formatTime: function(timestamp) {
            var now = Date.now();
            var diff = now - timestamp;
            
            if (diff < 60000) return '刚刚';
            if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前';
            if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前';
            return Math.floor(diff / 86400000) + '天前';
        },

        formatDateTime: function(timestamp) {
            var date = new Date(timestamp);
            return date.getFullYear() + '-' + 
                   String(date.getMonth() + 1).padStart(2, '0') + '-' + 
                   String(date.getDate()).padStart(2, '0') + ' ' +
                   String(date.getHours()).padStart(2, '0') + ':' +
                   String(date.getMinutes()).padStart(2, '0');
        },

        testSelectedBinding: function() {
            if (selectedBinding) {
                CapabilityBinding.testBindingRequest(selectedBinding.bindingId);
            }
        },

        editSelectedBinding: function() {
            if (selectedBinding) {
                CapabilityBinding.editBindingRequest(selectedBinding.bindingId);
            }
        },

        deleteSelectedBinding: function() {
            if (selectedBinding) {
                CapabilityBinding.deleteBindingRequest(selectedBinding.bindingId);
            }
        }
    };

    CapabilityBinding.init();

    global.selectSceneGroup = CapabilityBinding.selectSceneGroup;
    global.selectBinding = CapabilityBinding.selectBinding;
    global.showAddBindingModal = CapabilityBinding.showAddModal;
    global.hideAddBindingModal = CapabilityBinding.hideAddModal;
    global.createBinding = CapabilityBinding.createBindingRequest;
    global.deleteBinding = CapabilityBinding.deleteBindingRequest;
    global.editBinding = CapabilityBinding.editBindingRequest;
    global.testBinding = CapabilityBinding.testBindingRequest;
    global.testSelectedBinding = CapabilityBinding.testSelectedBinding;
    global.editSelectedBinding = CapabilityBinding.editSelectedBinding;
    global.deleteSelectedBinding = CapabilityBinding.deleteSelectedBinding;

})(typeof window !== 'undefined' ? window : this);
