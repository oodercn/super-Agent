(function(global) {
    'use strict';

    var capabilityId = null;
    var capability = null;
    var currentTab = 'definition';
    var bindings = [];
    var executionHistory = [];
    var callLogs = [];
    var dependencies = [];
    var references = [];

    var TYPE_CONFIG = {
        'SCENE': { icon: 'ri-layout-grid-line', name: '场景能力', color: '#2563eb' },
        'ATOMIC': { icon: 'ri-flashlight-line', name: '原子能力', color: '#10b981' },
        'COMPOSITE': { icon: 'ri-stack-line', name: '组合能力', color: '#8b5cf6' },
        'DRIVER': { icon: 'ri-timer-line', name: '驱动能力', color: '#f59e0b' },
        'SERVICE': { icon: 'ri-service-line', name: '服务能力', color: '#06b6d4' },
        'AI': { icon: 'ri-robot-line', name: 'AI能力', color: '#ec4899' },
        'DATA': { icon: 'ri-database-2-line', name: '数据能力', color: '#14b8a6' },
        'COMMUNICATION': { icon: 'ri-message-3-line', name: '通讯能力', color: '#6366f1' },
        'SECURITY': { icon: 'ri-shield-line', name: '安全能力', color: '#ef4444' },
        'TOOL': { icon: 'ri-tools-line', name: '工具能力', color: '#84cc16' }
    };

    var STATUS_CONFIG = {
        'REGISTERED': { name: '已注册', icon: 'ri-record-circle-line', class: 'status-indicator--registered' },
        'ENABLED': { name: '已启用', icon: 'ri-checkbox-circle-line', class: 'status-indicator--enabled' },
        'DISABLED': { name: '已禁用', icon: 'ri-pause-circle-line', class: 'status-indicator--disabled' },
        'ERROR': { name: '错误', icon: 'ri-error-warning-line', class: 'status-indicator--error' }
    };

    var CapabilityDetailPage = {
        init: function() {
            this.parseUrlParams();
            if (capabilityId) {
                this.loadCapability();
                this.loadBindings();
                this.loadHistory();
                this.loadDependencies();
                this.loadCallLogs();
            } else {
                this.showError('缺少能力ID参数');
            }
            this.bindEvents();
        },

        parseUrlParams: function() {
            var params = new URLSearchParams(window.location.search);
            capabilityId = params.get('id');
            var tab = params.get('tab');
            if (tab) {
                currentTab = tab;
            }
        },

        bindEvents: function() {
            var self = this;
            
            document.querySelectorAll('.tab-btn').forEach(function(btn) {
                btn.addEventListener('click', function(e) {
                    var tabId = this.getAttribute('data-tab');
                    if (tabId) {
                        self.switchTab(tabId);
                    }
                });
            });

            if (currentTab !== 'definition') {
                this.switchTab(currentTab);
            }
        },

        loadCapability: function() {
            var self = this;
            
            ApiClient.get('/api/v1/capabilities/' + capabilityId)
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        capability = result.data;
                        self.render();
                    } else {
                        self.showError(result.message || '加载失败');
                    }
                })
                .catch(function(error) {
                    self.showError(error.message || '网络请求失败');
                });
        },

        loadBindings: function() {
            var self = this;
            ApiClient.get('/api/v1/capabilities/' + capabilityId + '/bindings')
                .then(function(result) {
                    if (result.status === 'success' && result.data) {
                        bindings = result.data;
                        self.renderBindings();
                    }
                })
                .catch(function() {
                    bindings = [];
                    self.renderBindings();
                });
        },

        loadHistory: function() {
            var mockHistory = [];
            this.renderHistory(mockHistory);
        },

        loadDependencies: function() {
            var self = this;
            dependencies = [];
            references = [];
            
            ApiClient.get('/api/v1/capabilities/' + capabilityId + '/dependencies')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                        if (result.data.dependencies) dependencies = result.data.dependencies;
                        if (result.data.references) references = result.data.references;
                    }
                })
                .catch(function(e) {
                    console.log('Dependencies API not available, using capability data');
                })
                .finally(function() {
                    if (!dependencies || dependencies.length === 0) {
                        dependencies = (capability && capability.dependencies) || [];
                    }
                    self.renderDependencies();
                });
        },

        loadCallLogs: function() {
            var self = this;
            callLogs = [];
            
            ApiClient.get('/api/v1/capabilities/' + capabilityId + '/logs')
                .then(function(result) {
                    if (result && result.status === 'success' && result.data) {
                    if (Array.isArray(result.data)) {
                        callLogs = result.data;
                    } else if (result.data.list) {
                        callLogs = result.data.list;
                    }
                    }
                })
                .catch(function(e) {
                    console.log('Logs API not available');
                })
                .finally(function() {
                    self.renderCallLogs();
                });
        },

        showError: function(message) {
            var header = document.getElementById('capability-header');
            if (header) {
                header.innerHTML = 
                    '<div class="empty-state">' +
                        '<i class="ri-error-warning-line" style="color: var(--nx-danger);"></i>' +
                        '<p>' + message + '</p>' +
                        '<button class="nx-btn nx-btn--secondary nx-mt-4" onclick="window.history.back()">' +
                            '<i class="ri-arrow-left-line"></i> 返回' +
                        '</button>' +
                    '</div>';
            }
        },

        render: function() {
            this.renderHeader();
            this.renderInfoBar();
            this.renderDefinition();
            this.renderBindings();
            this.renderStatistics();
        },

        renderHeader: function() {
            var type = capability.type || 'SERVICE';
            var typeConfig = TYPE_CONFIG[type] || TYPE_CONFIG['SERVICE'];
            var status = capability.status || 'REGISTERED';
            var statusConfig = STATUS_CONFIG[status] || STATUS_CONFIG['REGISTERED'];

            var tagsHtml = '<span class="capability-tag"><i class="' + typeConfig.icon + '"></i> ' + type + '</span>';

            var header = document.getElementById('capability-header');
            header.innerHTML = 
                '<div class="capability-icon-box"><i class="' + typeConfig.icon + '"></i></div>' +
                '<div class="capability-info">' +
                    '<h1 class="capability-title">' + (capability.name || '未命名能力') + '</h1>' +
                    '<div class="capability-id">' + (capability.capabilityId || capability.id) + '</div>' +
                    '<div class="capability-tags">' + tagsHtml + '</div>' +
                '</div>' +
                '<div class="capability-actions">' +
                    '<button class="cap-action-btn" onclick="copyCapabilityId()" title="复制ID">' +
                        '<i class="ri-file-copy-line"></i>' +
                    '</button>' +
                    '<button class="cap-action-btn" onclick="shareCapability()" title="分享">' +
                        '<i class="ri-share-line"></i>' +
                    '</button>' +
                    '<div class="status-indicator ' + statusConfig.class + '">' +
                        '<i class="' + statusConfig.icon + '"></i>' +
                        '<span>' + statusConfig.name + '</span>' +
                    '</div>' +
                '</div>';
        },

        renderInfoBar: function() {
            var skillEl = document.getElementById('info-skill');
            var versionEl = document.getElementById('info-version');
            
            if (skillEl) skillEl.textContent = capability.skillId || '-';
            if (versionEl) versionEl.textContent = capability.version || '1.0.0';
            
            this.renderBusinessScore();
        },

        renderBusinessScore: function() {
            var score = capability.businessSemanticsScore;
            var container = document.getElementById('score-container');
            var scoreEl = document.getElementById('info-score');
            
            if (score !== undefined && score !== null && container && scoreEl) {
                container.style.display = 'inline-flex';
                
                var level = score >= 8 ? 'high' : (score >= 3 ? 'medium' : 'low');
                var levelConfig = {
                    high: { color: '#10b981', label: '高业务价值', icon: 'ri-star-fill' },
                    medium: { color: '#f59e0b', label: '中等价值', icon: 'ri-star-half-line' },
                    low: { color: '#6b7280', label: '基础能力', icon: 'ri-star-line' }
                };
                var config = levelConfig[level];
                
                container.title = config.label + ' (评分: ' + score + '/10)';
                scoreEl.textContent = score;
                scoreEl.style.color = config.color;
                scoreEl.style.fontWeight = '600';
                
                var icon = container.querySelector('i');
                if (icon) icon.className = config.icon;
            }
        },

        renderDefinition: function() {
            this.renderDescription();
            this.renderParameters();
            this.renderReturns();
            this.renderExamples();
            this.renderRelations();
        },

        renderDescription: function() {
            var desc = document.getElementById('description');
            if (desc) {
                desc.textContent = capability.description || '暂无描述';
            }
        },

        renderParameters: function() {
            var params = capability.parameters || [];
            var countEl = document.getElementById('param-count');
            if (countEl) countEl.textContent = params.length;
            
            var tbody = document.getElementById('params-body');
            if (!tbody) return;
            
            if (params.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" class="nx-text-center nx-text-secondary">暂无参数定义</td></tr>';
                return;
            }
            
            var html = params.map(function(p) {
                return '<tr>' +
                    '<td><span class="param-name">' + (p.name || '-') + '</span></td>' +
                    '<td><span class="param-type">' + (p.type || 'string') + '</span></td>' +
                    '<td>' + (p.required ? '<span class="param-required">必填</span>' : '<span class="nx-text-secondary">可选</span>') + '</td>' +
                    '<td>' + (p.defaultValue !== undefined && p.defaultValue !== null ? String(p.defaultValue) : '-') + '</td>' +
                    '<td class="nx-text-secondary">' + (p.description || '-') + '</td>' +
                '</tr>';
            }).join('');
            
            tbody.innerHTML = html;
        },

        renderReturns: function() {
            var returns = capability.returns;
            var container = document.getElementById('returns-def');
            if (!container) return;
            
            if (!returns) {
                container.innerHTML = '<div class="empty-state"><i class="ri-output-method-line"></i><p>暂无返回定义</p></div>';
                return;
            }
            
            var html = '<div class="returns-info">' +
                '<div class="returns-type"><span class="info-label">返回类型：</span><span class="param-type">' + (returns.type || 'object') + '</span></div>';
            
            var props = returns.properties || {};
            if (Object.keys(props).length > 0) {
                html += '<div class="returns-props"><span class="info-label">返回字段：</span>' +
                    '<div class="table-responsive"><table class="param-table"><thead><tr><th>字段名</th><th>类型</th></tr></thead><tbody>';
                Object.keys(props).forEach(function(key) {
                    html += '<tr><td><span class="param-name">' + key + '</span></td><td>' + props[key] + '</td></tr>';
                });
                html += '</tbody></table></div></div>';
            }
            html += '</div>';
            container.innerHTML = html;
        },

        renderExamples: function() {
            var examples = [];
            if (capability.metadata && capability.metadata.examples) {
                examples = capability.metadata.examples;
            }
            
            var container = document.getElementById('examples-container');
            if (!container) return;
            
            if (examples.length === 0) {
                container.innerHTML = '<div class="empty-state"><i class="ri-code-box-line"></i><p>暂无调用示例</p></div>';
                return;
            }
            
            var html = examples.map(function(ex, idx) {
                return '<div class="example-card">' +
                    '<div class="example-header">示例 ' + (idx + 1) + '</div>' +
                    '<div class="example-content">' +
                        '<div class="example-section">' +
                            '<span class="example-label">输入：</span>' +
                            '<pre class="example-code">' + JSON.stringify(ex.input, null, 2) + '</pre>' +
                        '</div>' +
                        '<div class="example-section">' +
                            '<span class="example-label">输出：</span>' +
                            '<pre class="example-code">' + JSON.stringify(ex.output, null, 2) + '</pre>' +
                        '</div>' +
                    '</div>' +
                '</div>';
            }).join('');
            
            container.innerHTML = html;
        },

        renderRelations: function() {
            var deps = capability.dependencies || [];
            var optionalCaps = capability.optionalCapabilities || [];
            var caps = capability.capabilities || [];
            
            this.renderRelationDiagram(deps, optionalCaps, caps);
        },

        renderRelationDiagram: function(deps, optionalCaps, caps) {
            var diagram = document.getElementById('relation-diagram');
            if (!diagram) return;
            
            var type = capability.type || 'SERVICE';
            var typeConfig = TYPE_CONFIG[type] || TYPE_CONFIG['SERVICE'];
            var capName = capability.name || capability.capabilityId || '当前能力';
            
            var html = '';
            
            if (deps.length > 0) {
                html += '<div class="relation-group relation-group--deps">';
                deps.slice(0, 3).forEach(function(dep) {
                    html += '<div class="relation-node relation-node--dep" title="' + dep + '" onclick="navigateToCapability(\'' + dep + '\')">' +
                        '<i class="ri-link relation-node__icon"></i>' +
                        '<span class="relation-node__name">' + this.truncateText(dep, 15) + '</span>' +
                    '</div>';
                }, this);
                if (deps.length > 3) {
                    html += '<div class="relation-group__more">+' + (deps.length - 3) + '</div>';
                }
                html += '</div>';
                html += '<div class="relation-arrow"><i class="ri-arrow-right-line"></i></div>';
            }
            
            html += '<div class="relation-node relation-node--current">' +
                '<i class="' + typeConfig.icon + ' relation-node__icon"></i>' +
                '<span class="relation-node__name">' + this.truncateText(capName, 15) + '</span>' +
            '</div>';
            
            if (optionalCaps.length > 0 || caps.length > 0) {
                html += '<div class="relation-arrow"><i class="ri-arrow-right-line"></i></div>';
                html += '<div class="relation-group relation-group--out">';
                
                optionalCaps.slice(0, 2).forEach(function(cap) {
                    html += '<div class="relation-node relation-node--optional" title="' + cap + '" onclick="navigateToCapability(\'' + cap + '\')">' +
                        '<i class="ri-team-line relation-node__icon"></i>' +
                        '<span class="relation-node__name">' + this.truncateText(cap, 12) + '</span>' +
                    '</div>';
                }, this);
                
                caps.slice(0, 2).forEach(function(cap) {
                    html += '<div class="relation-node relation-node--contained" title="' + cap + '" onclick="navigateToCapability(\'' + cap + '\')">' +
                        '<i class="ri-apps-line relation-node__icon"></i>' +
                        '<span class="relation-node__name">' + this.truncateText(cap, 12) + '</span>' +
                    '</div>';
                }, this);
                
                html += '</div>';
            }
            
            diagram.innerHTML = html;
        },

        renderBindings: function() {
            var countEl = document.getElementById('binding-count');
            if (countEl) countEl.textContent = bindings.length;
            
            var list = document.getElementById('bindings-list');
            if (!list) return;
            
            if (bindings.length === 0) {
                list.innerHTML = '<div class="empty-state"><i class="ri-link"></i><p>无绑定信息</p></div>';
                return;
            }
            
            var html = bindings.map(function(b) {
                var statusClass = b.status === 'ACTIVE' ? 'binding-item--active' : 'binding-item--inactive';
                var statusText = b.status === 'ACTIVE' ? '活跃' : '未激活';
                
                return '<div class="binding-item ' + statusClass + '">' +
                    '<div class="binding-info">' +
                        '<div class="binding-name">' + (b.name || b.bindingId) + '</div>' +
                        '<div class="binding-meta">场景组: ' + (b.sceneGroupId || '-') + ' | 状态: ' + statusText + '</div>' +
                    '</div>' +
                    '<div class="binding-actions">' +
                        '<button class="nx-btn nx-btn--sm nx-btn--ghost nx-text-danger" onclick="deleteBinding(\'' + b.bindingId + '\')">' +
                            '<i class="ri-delete-bin-line"></i>' +
                        '</button>' +
                    '</div>' +
                '</div>';
            }).join('');
            
            list.innerHTML = html;
            
            this.renderParticipants();
            this.renderDriverConditions();
        },

        renderParticipants: function() {
            var participants = capability.participants || [];
            var list = document.getElementById('participants-list');
            if (!list) return;
            
            if (participants.length === 0) {
                list.innerHTML = '<div class="empty-state"><i class="ri-user-line"></i><p>无参与者定义</p></div>';
                return;
            }
            
            var html = participants.map(function(p) {
                var role = p.role || 'PARTICIPANT';
                var icon = role === 'LEADER' ? 'ri-user-star-line' : 'ri-user-line';
                var roleClass = role === 'LEADER' ? 'participant-role--leader' : '';
                
                return '<div class="participant-card">' +
                    '<div class="participant-avatar"><i class="' + icon + '"></i></div>' +
                    '<div class="participant-info">' +
                        '<div class="participant-name">' + (p.name || p.userId || '-') + '</div>' +
                        '<div class="participant-id">' + (p.userId || '') + '</div>' +
                    '</div>' +
                    '<span class="participant-role ' + roleClass + '">' + role + '</span>' +
                '</div>';
            }).join('');
            
            list.innerHTML = html;
        },

        renderDriverConditions: function() {
            var conditions = capability.driverConditions || [];
            var list = document.getElementById('driver-conditions');
            if (!list) return;
            
            if (conditions.length === 0) {
                list.innerHTML = '<div class="empty-state"><i class="ri-timer-line"></i><p>无驱动条件</p></div>';
                return;
            }
            
            var html = conditions.map(function(c) {
                var type = c.type || 'schedule';
                var typeConfig = {
                    'SCHEDULE': { name: '定时驱动', icon: 'ri-timer-line', class: 'condition-type--schedule' },
                    'EVENT': { name: '事件驱动', icon: 'ri-notification-3-line', class: 'condition-type--event' },
                    'MANUAL': { name: '手动驱动', icon: 'ri-hand-coin-line', class: 'condition-type--manual' }
                };
                var config = typeConfig[type] || typeConfig['MANUAL'];
                
                return '<div class="driver-condition-item">' +
                    '<span class="condition-type ' + config.class + '">' +
                        '<i class="' + config.icon + '"></i> ' + config.name +
                    '</span>' +
                    '<span>' + (c.name || c.expression || '-') + '</span>' +
                '</div>';
            }).join('');
            
            list.innerHTML = html;
        },

        renderStatistics: function() {
            var successEl = document.getElementById('stat-success');
            var failedEl = document.getElementById('stat-failed');
            var avgTimeEl = document.getElementById('stat-avg-time');
            var rateEl = document.getElementById('stat-rate');
            
            var successCount = 0;
            var failedCount = 0;
            var totalTime = 0;
            var count = 0;
            
            executionHistory.forEach(function(h) {
                if (h.status === 'success') {
                    successCount++;
                } else {
                    failedCount++;
                }
                if (h.duration) {
                    totalTime += h.duration;
                    count++;
                }
            });
            
            var total = successCount + failedCount;
            var rate = total > 0 ? Math.round(successCount / total * 100) : 0;
            var avgTime = count > 0 ? Math.round(totalTime / count) : 0;
            
            if (successEl) successEl.textContent = successCount;
            if (failedEl) failedEl.textContent = failedCount;
            if (avgTimeEl) avgTimeEl.textContent = avgTime + 'ms';
            if (rateEl) rateEl.textContent = total > 0 ? rate + '%' : '-';
            
            this.renderHistory(executionHistory);
        },

        renderHistory: function(history) {
            var list = document.getElementById('history-list');
            if (!list) return;
            
            if (history.length === 0) {
                list.innerHTML = '<div class="empty-state"><i class="ri-history-line"></i><p>暂无执行记录</p></div>';
                return;
            }
            
            var html = history.map(function(h) {
                var statusIcon = h.status === 'success' ? 'ri-check-line' : 'ri-close-line';
                
                return '<div class="history-item history-item--' + h.status + '">' +
                    '<div class="history-icon"><i class="' + statusIcon + '"></i></div>' +
                    '<div class="history-content">' +
                        '<div class="history-title">执行 #' + (h.id || Date.now()).toString().slice(-4) + '</div>' +
                        '<div class="history-meta">' +
                            '<span>耗时: ' + (h.duration || 0) + 'ms</span>' +
                            '<span>状态: ' + (h.status === 'success' ? '成功' : '失败') + '</span>' +
                        '</div>' +
                    '</div>' +
                    '<div class="history-time">' + this.formatDate(h.time || h.startTime) + '</div>' +
                '</div>';
            }, this).join('');
            
            list.innerHTML = html;
        },

        renderDependencies: function() {
            this.renderDependencyGraph();
            this.renderDependencyList();
            this.renderReferenceList();
        },

        renderDependencyGraph: function() {
            var graph = document.getElementById('dependency-graph');
            if (!graph) return;
            
            var capName = capability.name || capability.capabilityId || '当前能力';
            var type = capability.type || 'SERVICE';
            var typeConfig = TYPE_CONFIG[type] || TYPE_CONFIG['SERVICE'];
            
            if (dependencies.length === 0 && references.length === 0) {
                graph.innerHTML = '<div class="empty-state"><i class="ri-mind-map"></i><p>暂无依赖关系</p></div>';
                return;
            }
            
            var html = '<div class="dep-graph">';
            
            if (references.length > 0) {
                html += '<div class="dep-group dep-group--refs">';
                references.slice(0, 3).forEach(function(ref) {
                    html += '<div class="dep-node dep-node--ref" onclick="navigateToCapability(\'' + ref.capabilityId + '\')">' +
                        '<i class="ri-link"></i>' +
                        '<span>' + this.truncateText(ref.name || ref.capabilityId, 12) + '</span>' +
                    '</div>';
                }, this);
                if (references.length > 3) {
                    html += '<div class="dep-more">+' + (references.length - 3) + '</div>';
                }
                html += '</div>';
                html += '<div class="dep-arrow"><i class="ri-arrow-right-line"></i></div>';
            }
            
            html += '<div class="dep-node dep-node--current">' +
                '<i class="' + typeConfig.icon + '"></i>' +
                '<span>' + this.truncateText(capName, 15) + '</span>' +
            '</div>';
            
            if (dependencies.length > 0) {
                html += '<div class="dep-arrow"><i class="ri-arrow-right-line"></i></div>';
                html += '<div class="dep-group dep-group--deps">';
                dependencies.slice(0, 3).forEach(function(dep) {
                    html += '<div class="dep-node dep-node--dep" onclick="navigateToCapability(\'' + dep + '\')">' +
                        '<i class="ri-flashlight-line"></i>' +
                        '<span>' + this.truncateText(dep, 12) + '</span>' +
                    '</div>';
                }, this);
                if (dependencies.length > 3) {
                    html += '<div class="dep-more">+' + (dependencies.length - 3) + '</div>';
                }
                html += '</div>';
            }
            
            html += '</div>';
            html += '<div class="dep-legend">' +
                '<span class="dep-legend-item"><span class="dep-legend-color" style="background:#8b5cf6"></span>被引用</span>' +
                '<span class="dep-legend-item"><span class="dep-legend-color" style="background:#2563eb"></span>当前能力</span>' +
                '<span class="dep-legend-item"><span class="dep-legend-color" style="background:#10b981"></span>依赖</span>' +
            '</div>';
            
            graph.innerHTML = html;
        },

        renderDependencyList: function() {
            var list = document.getElementById('dependency-list');
            if (!list) return;
            
            if (dependencies.length === 0) {
                list.innerHTML = '<div class="empty-state"><i class="ri-link"></i><p>无依赖能力</p></div>';
                return;
            }
            
            var html = dependencies.map(function(dep) {
                return '<div class="dep-item" onclick="navigateToCapability(\'' + dep + '\')">' +
                    '<i class="ri-flashlight-line"></i>' +
                    '<span class="dep-name">' + dep + '</span>' +
                    '<i class="ri-arrow-right-s-line"></i>' +
                '</div>';
            }).join('');
            
            list.innerHTML = html;
        },

        renderReferenceList: function() {
            var list = document.getElementById('reference-list');
            if (!list) return;
            
            if (references.length === 0) {
                list.innerHTML = '<div class="empty-state"><i class="ri-team-line"></i><p>无被引用关系</p></div>';
                return;
            }
            
            var html = references.map(function(ref) {
                return '<div class="ref-item" onclick="navigateToCapability(\'' + ref.capabilityId + '\')">' +
                    '<i class="ri-link"></i>' +
                    '<div class="ref-info">' +
                        '<span class="ref-name">' + (ref.name || ref.capabilityId) + '</span>' +
                        '<span class="ref-id">' + ref.capabilityId + '</span>' +
                    '</div>' +
                    '<i class="ri-arrow-right-s-line"></i>' +
                '</div>';
            }).join('');
            
            list.innerHTML = html;
        },

        renderCallLogs: function() {
            var list = document.getElementById('log-list');
            if (!list) return;
            
            var totalCalls = callLogs.length;
            var successCalls = 0;
            var errorCalls = 0;
            var totalDuration = 0;
            
            callLogs.forEach(function(log) {
                if (log.status === 'success' || log.level === 'INFO') {
                    successCalls++;
                } else {
                    errorCalls++;
                }
                totalDuration += log.duration || 0;
            });
            
            document.getElementById('log-total-calls').textContent = totalCalls;
            document.getElementById('log-success-calls').textContent = successCalls;
            document.getElementById('log-error-calls').textContent = errorCalls;
            document.getElementById('log-avg-duration').textContent = 
                totalCalls > 0 ? Math.round(totalDuration / totalCalls) + 'ms' : '0ms';
            
            if (callLogs.length === 0) {
                list.innerHTML = '<div class="empty-state"><i class="ri-file-list-line"></i><p>暂无调用日志</p></div>';
                return;
            }
            
            var html = callLogs.slice(0, 50).map(function(log) {
                var levelClass = log.level === 'ERROR' ? 'log-item--error' : 
                                 (log.level === 'WARN' ? 'log-item--warn' : 'log-item--info');
                var levelIcon = log.level === 'ERROR' ? 'ri-close-circle-line' : 
                               (log.level === 'WARN' ? 'ri-alert-line' : 'ri-checkbox-circle-line');
                
                return '<div class="log-item ' + levelClass + '">' +
                    '<div class="log-level"><i class="' + levelIcon + '"></i></div>' +
                    '<div class="log-content">' +
                        '<div class="log-message">' + (log.message || log.action || '-') + '</div>' +
                        '<div class="log-meta">' +
                            '<span>耗时: ' + (log.duration || 0) + 'ms</span>' +
                            '<span>来源: ' + (log.source || '-') + '</span>' +
                        '</div>' +
                    '</div>' +
                    '<div class="log-time">' + this.formatDate(log.timestamp) + '</div>' +
                '</div>';
            }, this).join('');
            
            list.innerHTML = html;
        },

        switchTab: function(tabId) {
            currentTab = tabId;
            
            document.querySelectorAll('.tab-btn').forEach(function(btn) {
                btn.classList.remove('tab-btn--active');
                if (btn.getAttribute('data-tab') === tabId) {
                    btn.classList.add('tab-btn--active');
                }
            });
            
            document.querySelectorAll('.tab-panel').forEach(function(panel) {
                panel.classList.remove('tab-panel--active');
            });
            
            var panel = document.getElementById(tabId + '-panel');
            if (panel) {
                panel.classList.add('tab-panel--active');
            }
            
            history.replaceState(null, '', '?id=' + capabilityId + '&tab=' + tabId);
        },

        truncateText: function(text, maxLength) {
            if (!text) return '';
            return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
        },

        formatDate: function(timestamp) {
            if (!timestamp) return '-';
            return NX.formatDate(timestamp, 'YYYY-MM-DD HH:mm');
        }
    };

    global.goBack = function() {
        window.history.back();
    };

    global.showBindModal = function() {
        var modal = document.getElementById('bind-modal');
        if (modal) {
            modal.classList.add('nx-modal--open');
        }
    };

    global.hideBindModal = function() {
        var modal = document.getElementById('bind-modal');
        if (modal) {
            modal.classList.remove('nx-modal--open');
        }
    };

    global.createBinding = function() {
        var sceneGroup = document.getElementById('bind-scene-group').value;
        var name = document.getElementById('bind-name').value;
        var configText = document.getElementById('bind-config').value || '{}';
        
        if (!sceneGroup) {
            showToast('warning', '参数错误', '请输入场景组ID');
            return;
        }
        
        try {
            var config = JSON.parse(configText);
        } catch (e) {
            showToast('warning', '参数错误', '配置参数格式错误');
            return;
        }
        
        var data = {
            capabilityId: capabilityId,
            sceneGroupId: sceneGroup,
            name: name,
            config: config
        };
        
        ApiClient.post('/api/v1/capabilities/bindings', data)
            .then(function(result) {
                if (result.status === 'success') {
                    hideBindModal();
                    CapabilityDetailPage.loadBindings();
                    showToast('success', '创建成功', '绑定已创建');
                } else {
                    showToast('error', '创建失败', result.message || '未知错误');
                }
            })
            .catch(function(error) {
                showToast('error', '创建失败', error.message);
            });
    };

    global.deleteBinding = function(bindingId) {
        if (!confirm('确定要删除此绑定吗？')) return;
        
        ApiClient.delete('/api/v1/capabilities/bindings/' + bindingId)
            .then(function(result) {
                if (result.status === 'success') {
                    CapabilityDetailPage.loadBindings();
                    showToast('success', '删除成功', '绑定已删除');
                }
            })
            .catch(function(error) {
                showToast('error', '删除失败', error.message);
            });
    };

    global.refreshHistory = function() {
        CapabilityDetailPage.loadHistory();
        showToast('info', '刷新完成', '执行历史已更新');
    };

    global.copyCapabilityId = function() {
        var id = capability ? (capability.capabilityId || capability.id) : capabilityId;
        if (navigator.clipboard) {
            navigator.clipboard.writeText(id).then(function() {
                showToast('success', '复制成功', '能力ID已复制到剪贴板');
            });
        } else {
            showToast('info', '能力ID', id);
        }
    };

    global.shareCapability = function() {
        var url = window.location.href;
        if (navigator.clipboard) {
            navigator.clipboard.writeText(url).then(function() {
                showToast('success', '分享链接', '链接已复制到剪贴板');
            });
        }
    };

    global.navigateToCapability = function(id) {
        if (id) {
            window.location.href = 'capability-detail.html?id=' + id;
        }
    };

    global.toggleSection = function(sectionId) {
        var section = document.getElementById(sectionId);
        var iconId = sectionId.replace('-section', '-toggle-icon');
        var icon = document.getElementById(iconId);
        
        if (section) {
            if (section.classList.contains('section-body--collapsed')) {
                section.classList.remove('section-body--collapsed');
                if (icon) icon.classList.add('section-toggle-icon--expanded');
            } else {
                section.classList.add('section-body--collapsed');
                if (icon) icon.classList.remove('section-toggle-icon--expanded');
            }
        }
    };

    global.refreshDependencies = function() {
        CapabilityDetailPage.loadDependencies();
        showToast('info', '刷新完成', '依赖关系已更新');
    };

    global.expandDependencyGraph = function() {
        var container = document.getElementById('dependency-graph-container');
        if (container) {
            if (container.classList.contains('fullscreen')) {
                container.classList.remove('fullscreen');
            } else {
                container.classList.add('fullscreen');
            }
        }
    };

    global.refreshLogs = function() {
        CapabilityDetailPage.loadCallLogs();
        showToast('info', '刷新完成', '调用日志已更新');
    };

    global.filterLogs = function() {
        var level = document.getElementById('log-level-filter').value;
        var items = document.querySelectorAll('.log-item');
        items.forEach(function(item) {
            if (!level || item.classList.contains('log-item--' + level.toLowerCase())) {
                item.style.display = 'flex';
            } else {
                item.style.display = 'none';
            }
        });
    };

    global.exportLogs = function() {
        if (callLogs.length === 0) {
            showToast('warning', '导出失败', '暂无日志数据');
            return;
        }
        var data = JSON.stringify(callLogs, null, 2);
        var blob = new Blob([data], { type: 'application/json' });
        var url = URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = 'capability-' + capabilityId + '-logs.json';
        a.click();
        URL.revokeObjectURL(url);
        showToast('success', '导出成功', '日志文件已下载');
    };

    function showToast(type, title, message) {
        var container = document.getElementById('toast-container');
        if (!container) return;
        
        var iconMap = {
            success: 'ri-check-line',
            error: 'ri-close-line',
            warning: 'ri-alert-line',
            info: 'ri-information-line'
        };
        
        var toast = document.createElement('div');
        toast.className = 'toast toast--' + type;
        toast.innerHTML = 
            '<i class="toast-icon ' + iconMap[type] + '"></i>' +
            '<div class="toast-content">' +
                '<div class="toast-title">' + title + '</div>' +
                '<div class="toast-message">' + message + '</div>' +
            '</div>';
        
        container.appendChild(toast);
        
        setTimeout(function() {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(100%)';
            setTimeout(function() {
                container.removeChild(toast);
            }, 300);
        }, 3000);
    }

    global.CapabilityDetailPage = CapabilityDetailPage;

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            CapabilityDetailPage.init();
        });
    } else {
        CapabilityDetailPage.init();
    }

})(typeof window !== 'undefined' ? window : this);
