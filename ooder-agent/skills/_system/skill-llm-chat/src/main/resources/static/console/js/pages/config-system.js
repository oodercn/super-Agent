var ConfigSystem = {
    currentTab: 'overview',
    systemConfig: null,
    capabilities: [],
    profiles: ['micro', 'small', 'large', 'enterprise'],

    init: function() {
        Sidebar.init();
        this.loadSystemConfig();
        this.loadCapabilities();
    },

    loadSystemConfig: function() {
        var self = this;
        fetch('/api/v1/config/system')
            .then(function(response) { return response.json(); })
            .then(function(data) {
                self.systemConfig = data;
                self.renderOverview();
            })
            .catch(function(error) {
                console.error('Failed to load system config:', error);
            });
    },

    loadCapabilities: function() {
        var self = this;
        fetch('/api/v1/config/system/capabilities')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    self.capabilities = result.data;
                } else {
                    self.capabilities = {};
                }
                self.renderCapabilities();
            })
            .catch(function(error) {
                console.error('Failed to load capabilities:', error);
                self.capabilities = {};
            });
    },

    switchTab: function(tabName) {
        this.currentTab = tabName;
        
        document.querySelectorAll('.config-tabs .tab').forEach(function(tab) {
            tab.classList.remove('active');
            if (tab.dataset.tab === tabName) {
                tab.classList.add('active');
            }
        });
        
        document.querySelectorAll('.tab-panel').forEach(function(panel) {
            panel.classList.remove('active');
        });
        
        var panel = document.getElementById(tabName + 'Panel');
        if (panel) {
            panel.classList.add('active');
        }
    },

    renderOverview: function() {
        var panel = document.getElementById('overviewPanel');
        if (!panel) return;
        
        var config = this.systemConfig;
        if (!config) return;
        
        var metadata = config.metadata || {};
        var spec = config.spec || {};
        var capabilities = spec.capabilities || {};
        
        var enabledCount = 0;
        var totalCount = Object.keys(capabilities).length;
        
        for (var key in capabilities) {
            if (capabilities[key].enabled) {
                enabledCount++;
            }
        }
        
        var html = '<div class="overview-grid">';
        html += '<div class="overview-card">';
        html += '<div class="overview-icon"><i class="ri-server-line"></i></div>';
        html += '<div class="overview-title">系统信息</div>';
        html += '<div class="overview-value">' + (metadata.name || 'N/A') + '</div>';
        html += '<div class="overview-label">名称: ' + (metadata.version || '1.0.0') + '</div>';
        html += '<div class="overview-label">版本: ' + (metadata.profile || 'micro') + '</div>';
        html += '<div class="overview-label">Profile: ' + (metadata.updatedAt || 'N/A') + '</div>';
        html += '<div class="overview-label">更新时间: ' + new Date(metadata.updatedAt).toLocaleString() + '</div>';
        html += '</div>';
        
        html += '<div class="overview-card">';
        html += '<div class="overview-icon"><i class="ri-pie-chart-line"></i></div>';
        html += '<div class="overview-title">能力统计</div>';
        html += '<div class="overview-value">' + enabledCount + '/' + totalCount + '</div>';
        html += '<div class="overview-label">已启用能力</div>';
        html += '<div class="progress-bar"><div class="progress-fill" style="width: ' + (enabledCount / totalCount * 100) + '%"></div></div>';
        html += '</div>';
        
        html += '<div class="overview-card">';
        html += '<div class="overview-icon"><i class="ri-database-2-line"></i></div>';
        html += '<div class="overview-title">数据库</div>';
        var dbConfig = capabilities.db || {};
        html += '<div class="overview-value">' + (dbConfig.enabled ? '已配置' : '未配置') + '</div>';
        html += '<div class="overview-label">默认: ' + (dbConfig.default || 'N/A') + '</div>';
        html += '</div>';
        
        html += '<div class="overview-card">';
        html += '<div class="overview-icon"><i class="ri-robot-line"></i></div>';
        html += '<div class="overview-title">LLM</div>';
        var llmConfig = capabilities.llm || {};
        html += '<div class="overview-value">' + (llmConfig.enabled ? '已配置' : '未配置') + '</div>';
        html += '<div class="overview-label">默认: ' + (llmConfig.default || 'N/A') + '</div>';
        html += '</div>';
        
        html += '</div>';
        
        panel.innerHTML = html;
    },

    renderCapabilities: function() {
        var panel = document.getElementById('capabilitiesPanel');
        if (!panel) return;
        
        var capabilities = this.capabilities;
        if (!capabilities) return;
        
        var html = '<div class="capabilities-grid">';
        
        var icons = {
            llm: 'ri-robot-line',
            db: 'ri-database-2-line',
            vfs: 'ri-folder-line',
            org: 'ri-team-line',
            know: 'ri-book-line',
            comm: 'ri-message-line',
            auth: 'ri-shield-line',
            mon: 'ri-line-chart-line',
            payment: 'ri-bank-card-line',
            media: 'ri-image-line',
            search: 'ri-search-line',
            sched: 'ri-calendar-line',
            sec: 'ri-lock-line',
            iot: 'ri-cpu-line',
            net: 'ri-global-line',
            sys: 'ri-settings-3-line',
            util: 'ri-tools-line'
        };
        
        var names = {
            llm: 'LLM能力',
            db: '数据库',
            vfs: '虚拟文件系统',
            org: '组织管理',
            know: '知识库',
            comm: '通信',
            auth: '认证',
            mon: '监控',
            payment: '支付',
            media: '媒体',
            search: '搜索',
            sched: '调度',
            sec: '安全',
            iot: 'IoT',
            net: '网络',
            sys: '系统',
            util: '工具'
        };
        
        for (var key in capabilities) {
            var cap = capabilities[key];
            html += '<div class="capability-card ' + (cap.enabled ? 'enabled' : 'disabled') + '" onclick="ConfigSystem.editCapability(\'' + key + '\')">';
            html += '<div class="capability-icon"><i class="' + (icons[key] || 'ri-checkbox-line') + '"></i></div>';
            html += '<div class="capability-name">' + (names[key] || key) + '</div>';
            html += '<div class="capability-status">' + (cap.enabled ? '已启用' : '未启用') + '</div>';
            html += '<div class="capability-default">' + (cap.default || '无默认驱动') + '</div>';
            html += '</div>';
        }
        
        html += '</div>';
        
        panel.innerHTML = html;
    },

    editCapability: function(address) {
        var capability = this.capabilities[address];
        if (!capability) {
            capability = { enabled: false, default: null, config: {} };
        }
        
        var modal = document.getElementById('configEditorModal');
        var title = document.getElementById('editorTitle');
        var body = document.getElementById('editorBody');
        
        title.innerHTML = '<i class="ri-settings-3-line"></i> 编辑能力配置: ' + address.toUpperCase();
        
        var html = '<div class="editor-form">';
        html += '<div class="form-group">';
        html += '<label>启用状态</label>';
        html += '<select id="capEnabled" class="form-select">';
        html += '<option value="true" ' + (capability.enabled ? 'selected' : '') + '>启用</option>';
        html += '<option value="false" ' + (!capability.enabled ? 'selected' : '') + '>禁用</option>';
        html += '</select>';
        html += '</div>';
        
        html += '<div class="form-group">';
        html += '<label>默认驱动</label>';
        html += '<input type="text" id="capDefault" class="form-input" value="' + (capability.default || '') + '" placeholder="skill-xxx-xxx">';
        html += '</div>';
        
        html += '<div class="form-group">';
        html += '<label>备用驱动</label>';
        html += '<input type="text" id="capFallback" class="form-input" value="' + (capability.fallback || '') + '" placeholder="skill-xxx-xxx">';
        html += '</div>';
        
        html += '<div class="form-group">';
        html += '<label>配置参数 (JSON)</label>';
        html += '<textarea id="capConfig" class="form-textarea" rows="10">' + JSON.stringify(capability.config || {}, null, 2) + '</textarea>';
        html += '</div>';
        
        html += '</div>';
        
        body.innerHTML = html;
        
        modal.style.display = 'flex';
        
        this.currentEditAddress = address;
    },

    saveEditorConfig: function() {
        var address = this.currentEditAddress;
        if (!address) return;
        
        var enabled = document.getElementById('capEnabled').value === 'true';
        var defaultDriver = document.getElementById('capDefault').value;
        var fallback = document.getElementById('capFallback').value;
        var configText = document.getElementById('capConfig').value;
        
        var config = {};
        try {
            if (configText.trim()) {
                config = JSON.parse(configText);
            }
        } catch (e) {
            alert('配置参数JSON格式错误');
            return;
        }
        
        var capabilityConfig = {
            enabled: enabled,
            default: defaultDriver || null,
            fallback: fallback || null,
            config: config
        };
        
        fetch('/api/v1/config/system/capabilities/' + address, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(capabilityConfig)
        })
        .then(function(response) {
            if (response.ok) {
                alert('配置保存成功');
                this.closeEditorModal();
                this.loadSystemConfig();
                this.loadCapabilities();
            } else {
                alert('配置保存失败');
            }
        })
        .catch(function(error) {
            console.error('Save error:', error);
            alert('配置保存失败');
        });
    },

    closeEditorModal: function() {
        var modal = document.getElementById('configEditorModal');
        modal.style.display = 'none';
        this.currentEditAddress = null;
    },

    switchProfile: function() {
        var profile = document.getElementById('profileSelect').value;
        if (confirm('确定切换到 ' + profile + ' 配置吗？这将覆盖当前配置。)) {
            fetch('/api/v1/config/system/profile', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ profile: profile })
            })
            .then(function(response) { return response.json(); })
            .then(function(data) {
                alert('Profile切换成功');
                this.loadSystemConfig();
                this.loadCapabilities();
            })
            .catch(function(error) {
                console.error('Profile switch error:', error);
                alert('Profile切换失败');
            });
        }
    },

    resetToDefault: function() {
        if (confirm('确定重置为默认配置吗？这将清除所有自定义配置.)) {
            fetch('/api/v1/config/system/reset', {
                method: 'POST'
            })
            .then(function(response) { return response.json(); })
            .then(function(data) {
                alert('配置已重置');
                this.loadSystemConfig();
                this.loadCapabilities();
            })
            .catch(function(error) {
                console.error('Reset error:', error);
                alert('重置失败');
            });
        }
    },

    saveConfig: function() {
        fetch('/api/v1/config/system', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(this.systemConfig)
        })
        .then(function(response) {
            if (response.ok) {
                alert('配置保存成功');
            } else {
                alert('配置保存失败');
            }
        })
        .catch(function(error) {
            console.error('Save error:', error);
            alert('配置保存失败');
        });
    }
};

document.addEventListener('DOMContentLoaded', function() {
    ConfigSystem.init();
});
