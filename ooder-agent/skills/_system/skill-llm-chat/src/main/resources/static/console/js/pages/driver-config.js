var DriverConfig = {
    categories: [],
    drivers: {},
    configs: {},
    currentCategory: null,
    currentDriver: null,

    init: function() {
        var self = this;
        CategoryService.loadCategories().then(function(categories) {
            self.categories = categories.map(function(cat) {
                return {
                    code: cat.code,
                    name: cat.name,
                    icon: cat.icon,
                    color: cat.color
                };
            });
            self.loadDrivers();
            self.loadConfigs();
            self.renderTabs();
        });
    },

    loadDrivers: function() {
        var self = this;
        ApiClient.get('/api/v1/config-meta/drivers')
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    self.drivers = result.data;
                    self.renderDriverCards();
                }
            })
            .catch(function(error) {
                console.error('[loadDrivers] Error:', error);
                self.loadMockDrivers();
            });
    },

    loadMockDrivers: function() {
        this.drivers = {
            llm: {
                category: 'llm',
                displayName: '大语言模型',
                drivers: [
                    { id: 'skill-llm-ollama', name: 'Ollama', address: 0x31, type: 'local', price: '免费', features: ['本地部署', '数据安全'], icon: 'ri-computer-line' },
                    { id: 'skill-llm-openai', name: 'OpenAI', address: 0x32, type: 'cloud', price: '$0.005/1K', features: ['最强能力', '多模态'], icon: 'ri-openai-line' },
                    { id: 'skill-llm-qianwen', name: '通义千问', address: 0x33, type: 'cloud', price: '¥0.002/1K', features: ['中文优化', '企业级'], icon: 'ri-bubble-chart-line' },
                    { id: 'skill-llm-deepseek', name: 'DeepSeek', address: 0x34, type: 'cloud', price: '¥0.001/1K', features: ['高性价比', 'Function Calling'], icon: 'ri-robot-2-line' },
                    { id: 'skill-llm-volcengine', name: '火山引擎', address: 0x35, type: 'cloud', price: '¥0.001/1K', features: ['企业级', '稳定'], icon: 'ri-fire-line' }
                ]
            },
            db: {
                category: 'db',
                displayName: '数据库',
                drivers: [
                    { id: 'skill-db-mysql', name: 'MySQL', address: 0x28, type: 'relational', price: '开源', features: ['关系型', '成熟稳定'], icon: 'ri-database-2-line' },
                    { id: 'skill-db-postgres', name: 'PostgreSQL', address: 0x29, type: 'relational', price: '开源', features: ['关系型', '企业级'], icon: 'ri-database-2-line' },
                    { id: 'skill-db-mongodb', name: 'MongoDB', address: 0x2A, type: 'document', price: '开源', features: ['文档型', '灵活'], icon: 'ri-leaf-line' },
                    { id: 'skill-db-redis', name: 'Redis', address: 0x2B, type: 'cache', price: '开源', features: ['缓存', '高性能'], icon: 'ri-speed-line' }
                ]
            },
            vfs: {
                category: 'vfs',
                displayName: '文件存储',
                drivers: [
                    { id: 'skill-vfs-local', name: '本地存储', address: 0x21, type: 'local', price: '免费', features: ['简单', '无依赖'], icon: 'ri-folder-line' },
                    { id: 'skill-vfs-minio', name: 'MinIO', address: 0x22, type: 'object', price: '开源', features: ['对象存储', '私有云'], icon: 'ri-cloud-line' },
                    { id: 'skill-vfs-oss', name: '阿里云OSS', address: 0x23, type: 'object', price: '按量付费', features: ['对象存储', '公有云'], icon: 'ri-cloud-line' },
                    { id: 'skill-vfs-s3', name: 'AWS S3', address: 0x24, type: 'object', price: '按量付费', features: ['对象存储', '国际化'], icon: 'ri-cloud-line' }
                ]
            },
            org: {
                category: 'org',
                displayName: '组织服务',
                drivers: [
                    { id: 'skill-org-local', name: '本地组织', address: 0x08, type: 'local', price: '免费', features: ['简单', '小型团队'], icon: 'ri-user-line' },
                    { id: 'skill-org-dingding', name: '钉钉', address: 0x09, type: 'cloud', price: '免费', features: ['企业通讯', '钉钉用户'], icon: 'ri-message-3-line' },
                    { id: 'skill-org-feishu', name: '飞书', address: 0x0A, type: 'cloud', price: '免费', features: ['企业通讯', '飞书用户'], icon: 'ri-message-3-line' },
                    { id: 'skill-org-wecom', name: '企业微信', address: 0x0B, type: 'cloud', price: '免费', features: ['企业通讯', '企微用户'], icon: 'ri-wechat-line' },
                    { id: 'skill-org-ldap', name: 'LDAP', address: 0x0C, type: 'enterprise', price: '免费', features: ['目录服务', '企业集成'], icon: 'ri-shield-keyhole-line' }
                ]
            },
            know: {
                category: 'know',
                displayName: '知识库',
                drivers: [
                    { id: 'skill-know-base', name: '知识库基础', address: 0x38, type: 'base', price: '免费', features: ['知识管理', '文档存储'], icon: 'ri-book-2-line' },
                    { id: 'skill-know-vector', name: '向量知识库', address: 0x39, type: 'vector', price: '开源', features: ['向量检索', '语义搜索'], icon: 'ri-node-tree' },
                    { id: 'skill-know-rag', name: 'RAG服务', address: 0x3A, type: 'rag', price: '开源', features: ['检索增强', '智能问答'], icon: 'ri-magic-line' },
                    { id: 'skill-know-embedding', name: '嵌入服务', address: 0x3B, type: 'embedding', price: '按量付费', features: ['文本嵌入', '向量化'], icon: 'ri-code-box-line' }
                ]
            },
            comm: {
                category: 'comm',
                displayName: '通讯服务',
                drivers: [
                    { id: 'skill-comm-mqtt', name: 'MQTT', address: 0x51, type: 'mqtt', price: '开源', features: ['消息队列', 'IoT'], icon: 'ri-signal-tower-line' },
                    { id: 'skill-comm-email', name: '邮件服务', address: 0x52, type: 'email', price: '免费', features: ['邮件发送', '通知'], icon: 'ri-mail-line' },
                    { id: 'skill-comm-notify', name: '通知服务', address: 0x53, type: 'notify', price: '免费', features: ['系统通知', '推送'], icon: 'ri-notification-3-line' }
                ]
            },
            auth: {
                category: 'auth',
                displayName: '认证服务',
                drivers: [
                    { id: 'skill-auth-user', name: '用户认证', address: 0x10, type: 'user', price: '免费', features: ['用户登录', '身份验证'], icon: 'ri-user-line' },
                    { id: 'skill-auth-token', name: '令牌管理', address: 0x11, type: 'token', price: '免费', features: ['Token管理', '会话'], icon: 'ri-key-line' }
                ]
            },
            mon: {
                category: 'mon',
                displayName: '监控服务',
                drivers: [
                    { id: 'skill-mon-health', name: '健康检查', address: 0x59, type: 'health', price: '免费', features: ['健康状态', '心跳检测'], icon: 'ri-heart-pulse-line' },
                    { id: 'skill-mon-metrics', name: '指标采集', address: 0x5B, type: 'metrics', price: '免费', features: ['性能指标', '监控'], icon: 'ri-line-chart-line' },
                    { id: 'skill-mon-log', name: '日志服务', address: 0x5C, type: 'log', price: '免费', features: ['日志收集', '分析'], icon: 'ri-file-list-3-line' }
                ]
            },
            payment: {
                category: 'payment',
                displayName: '支付服务',
                drivers: [
                    { id: 'skill-pay-alipay', name: '支付宝', address: 0x41, type: 'alipay', price: '按量付费', features: ['在线支付', '支付宝'], icon: 'ri-bank-card-line' },
                    { id: 'skill-pay-wechat', name: '微信支付', address: 0x42, type: 'wechat', price: '按量付费', features: ['在线支付', '微信'], icon: 'ri-wechat-pay-line' }
                ]
            },
            media: {
                category: 'media',
                displayName: '媒体服务',
                drivers: [
                    { id: 'skill-media-wechat', name: '微信公众号', address: 0x49, type: 'wechat', price: '免费', features: ['内容发布', '微信'], icon: 'ri-wechat-line' },
                    { id: 'skill-media-weibo', name: '微博', address: 0x4A, type: 'weibo', price: '免费', features: ['内容发布', '微博'], icon: 'ri-weibo-line' },
                    { id: 'skill-media-zhihu', name: '知乎', address: 0x4B, type: 'zhihu', price: '免费', features: ['内容发布', '知乎'], icon: 'ri-question-line' }
                ]
            },
            search: {
                category: 'search',
                displayName: '搜索服务',
                drivers: [
                    { id: 'skill-search-base', name: '搜索基础', address: 0x68, type: 'base', price: '免费', features: ['全文搜索', '索引'], icon: 'ri-search-line' },
                    { id: 'skill-search-es', name: 'Elasticsearch', address: 0x69, type: 'es', price: '开源', features: ['分布式搜索', '高性能'], icon: 'ri-search-eye-line' }
                ]
            },
            sched: {
                category: 'sched',
                displayName: '调度服务',
                drivers: [
                    { id: 'skill-sched-base', name: '调度基础', address: 0x70, type: 'base', price: '免费', features: ['定时任务', '调度'], icon: 'ri-time-line' },
                    { id: 'skill-sched-quartz', name: 'Quartz', address: 0x71, type: 'quartz', price: '开源', features: ['企业级调度', '集群'], icon: 'ri-timer-line' }
                ]
            },
            sec: {
                category: 'sec',
                displayName: '安全服务',
                drivers: [
                    { id: 'skill-sec-access', name: '访问控制', address: 0x79, type: 'access', price: '免费', features: ['权限管理', '访问控制'], icon: 'ri-lock-line' },
                    { id: 'skill-sec-audit', name: '审计服务', address: 0x7A, type: 'audit', price: '免费', features: ['操作审计', '日志'], icon: 'ri-file-shield-line' }
                ]
            },
            iot: {
                category: 'iot',
                displayName: '物联网',
                drivers: [
                    { id: 'skill-iot-k8s', name: 'Kubernetes', address: 0x61, type: 'k8s', price: '开源', features: ['容器编排', '集群'], icon: 'ri-ship-line' },
                    { id: 'skill-iot-hosting', name: '托管服务', address: 0x62, type: 'hosting', price: '按量付费', features: ['应用托管', '自动扩缩'], icon: 'ri-cloud-line' },
                    { id: 'skill-iot-openwrt', name: 'OpenWrt', address: 0x63, type: 'openwrt', price: '免费', features: ['路由器管理', 'IoT网关'], icon: 'ri-router-line' }
                ]
            },
            net: {
                category: 'net',
                displayName: '网络服务',
                drivers: [
                    { id: 'skill-net-proxy', name: '网络代理', address: 0x18, type: 'proxy', price: '免费', features: ['代理服务', '转发'], icon: 'ri-route-line' },
                    { id: 'skill-net-dns', name: 'DNS服务', address: 0x19, type: 'dns', price: '免费', features: ['域名解析', 'DNS'], icon: 'ri-global-line' }
                ]
            },
            sys: {
                category: 'sys',
                displayName: '系统核心',
                drivers: [
                    { id: 'skill-sys-registry', name: '系统注册', address: 0x00, type: 'registry', price: '免费', features: ['服务注册', '发现'], icon: 'ri-server-line' },
                    { id: 'skill-sys-config', name: '系统配置', address: 0x01, type: 'config', price: '免费', features: ['配置管理', '热更新'], icon: 'ri-settings-3-line' }
                ]
            },
            util: {
                category: 'util',
                displayName: '工具服务',
                drivers: [
                    { id: 'skill-util-report', name: '报表服务', address: 0xF1, type: 'report', price: '免费', features: ['报表生成', '导出'], icon: 'ri-file-chart-line' },
                    { id: 'skill-util-share', name: '分享服务', address: 0xF2, type: 'share', price: '免费', features: ['内容分享', '社交'], icon: 'ri-share-line' }
                ]
            }
        };
        this.renderDriverCards();
    },

    loadConfigs: function() {
        var self = this;
        ApiClient.get('/api/v1/config/driver-configs')
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    self.configs = result.data;
                    self.updateDriverStatus();
                }
            })
            .catch(function(error) {
                console.error('[loadConfigs] Error:', error);
            });
    },

    renderTabs: function() {
        var header = document.getElementById('configTabsHeader');
        if (!header) return;

        var html = '';
        this.categories.forEach(function(cat, index) {
            var activeClass = index === 0 ? 'active' : '';
            html += '<div class="tab-item ' + activeClass + '" data-category="' + cat.code + '" onclick="switchCategory(\'' + cat.code + '\')">';
            html += '<i class="' + cat.icon + '" style="color: ' + cat.color + '"></i>';
            html += '<span>' + cat.name + '</span>';
            html += '</div>';
        });

        header.innerHTML = html;
        this.currentCategory = this.categories[0] ? this.categories[0].code : null;
    },

    renderDriverCards: function() {
        var content = document.getElementById('configTabsContent');
        if (!content) return;

        var html = '';
        var self = this;

        this.categories.forEach(function(cat, index) {
            var activeClass = index === 0 ? 'active' : '';
            var driverList = self.drivers[cat.code];

            html += '<div class="tab-pane ' + activeClass + '" id="pane-' + cat.code + '">';
            html += '<div class="driver-grid">';

            if (driverList && driverList.drivers) {
                driverList.drivers.forEach(function(driver) {
                    var config = self.configs[driver.id] || {};
                    var statusClass = config.enabled ? 'enabled' : 'disabled';
                    var selectedClass = config.selected ? 'selected' : '';

                    html += '<div class="driver-card ' + statusClass + ' ' + selectedClass + '" data-driver="' + driver.id + '">';
                    html += '<div class="card-header">';
                    html += '<div class="driver-icon"><i class="' + (driver.icon || 'ri-puzzle-line') + '"></i></div>';
                    html += '<div class="driver-status">';
                    if (config.selected) {
                        html += '<span class="status-badge selected"><i class="ri-check-line"></i> 已选择</span>';
                    }
                    html += '</div></div>';
                    html += '<div class="card-body">';
                    html += '<div class="driver-name">' + driver.name + '</div>';
                    html += '<div class="driver-address">地址: 0x' + driver.address.toString(16).toUpperCase().padStart(2, '0') + '</div>';
                    html += '<div class="driver-features">';
                    driver.features.forEach(function(f) {
                        html += '<span class="feature-tag">' + f + '</span>';
                    });
                    html += '</div></div>';
                    html += '<div class="card-footer">';
                    html += '<span class="driver-price">' + driver.price + '</span>';
                    html += '<div class="card-actions">';
                    html += '<button class="nx-btn nx-btn--sm nx-btn--ghost" onclick="testDriver(\'' + driver.id + '\')"><i class="ri-link"></i></button>';
                    html += '<button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="openConfigModal(\'' + driver.id + '\')">配置</button>';
                    html += '</div></div></div>';
                });
            }

            html += '</div></div>';
        });

        content.innerHTML = html;
    },

    updateDriverStatus: function() {
        var self = this;
        Object.keys(this.configs).forEach(function(driverId) {
            var config = self.configs[driverId];
            var card = document.querySelector('.driver-card[data-driver="' + driverId + '"]');
            if (card && config.enabled) {
                card.classList.add('enabled');
            }
            if (card && config.selected) {
                card.classList.add('selected');
            }
        });
    }
};

function switchCategory(code) {
    document.querySelectorAll('.tab-item').forEach(function(tab) {
        tab.classList.remove('active');
        if (tab.dataset.category === code) {
            tab.classList.add('active');
        }
    });

    document.querySelectorAll('.tab-pane').forEach(function(pane) {
        pane.classList.remove('active');
    });

    var pane = document.getElementById('pane-' + code);
    if (pane) {
        pane.classList.add('active');
    }

    DriverConfig.currentCategory = code;
}

function openConfigModal(driverId) {
    var driver = null;
    Object.values(DriverConfig.drivers).forEach(function(cat) {
        var found = cat.drivers.find(function(d) { return d.id === driverId; });
        if (found) driver = found;
    });

    if (!driver) return;

    DriverConfig.currentDriver = driverId;
    document.getElementById('configModalTitle').textContent = driver.name + ' 配置';

    var config = DriverConfig.configs[driverId] || {};
    var html = generateConfigForm(driver, config);

    document.getElementById('configModalBody').innerHTML = html;
    document.getElementById('configModal').style.display = 'flex';
}

function generateConfigForm(driver, config) {
    var category = driver.id.split('-')[1];
    var html = '<div class="config-form">';

    html += '<div class="form-section">';
    html += '<div class="form-row">';
    html += '<label class="form-label">启用此驱动</label>';
    html += '<label class="toggle-switch"><input type="checkbox" id="configEnabled" ' + (config.enabled ? 'checked' : '') + '><span class="toggle-slider"></span></label>';
    html += '</div>';
    html += '<div class="form-row">';
    html += '<label class="form-label">设为默认</label>';
    html += '<label class="toggle-switch"><input type="checkbox" id="configSelected" ' + (config.selected ? 'checked' : '') + '><span class="toggle-slider"></span></label>';
    html += '</div>';
    html += '</div>';

    if (category === 'llm') {
        html += '<div class="form-section">';
        html += '<h4>API配置</h4>';
        html += '<div class="form-row"><label>API Key</label><input type="password" class="form-input" id="configApiKey" value="' + (config.apiKey || '') + '" placeholder="输入API Key"></div>';
        html += '<div class="form-row"><label>API Base URL</label><input type="text" class="form-input" id="configApiBase" value="' + (config.apiBase || '') + '" placeholder="可选，自定义API地址"></div>';
        html += '<div class="form-row"><label>默认模型</label><input type="text" class="form-input" id="configModel" value="' + (config.model || '') + '" placeholder="如: deepseek-chat"></div>';
        html += '</div>';
    } else if (category === 'db') {
        html += '<div class="form-section">';
        html += '<h4>数据库配置</h4>';
        html += '<div class="form-row"><label>主机地址</label><input type="text" class="form-input" id="configHost" value="' + (config.host || 'localhost') + '"></div>';
        html += '<div class="form-row"><label>端口</label><input type="number" class="form-input" id="configPort" value="' + (config.port || 3306) + '"></div>';
        html += '<div class="form-row"><label>数据库名</label><input type="text" class="form-input" id="configDatabase" value="' + (config.database || '') + '"></div>';
        html += '<div class="form-row"><label>用户名</label><input type="text" class="form-input" id="configUsername" value="' + (config.username || '') + '"></div>';
        html += '<div class="form-row"><label>密码</label><input type="password" class="form-input" id="configPassword" value="' + (config.password || '') + '"></div>';
        html += '</div>';
    } else if (category === 'vfs') {
        html += '<div class="form-section">';
        html += '<h4>存储配置</h4>';
        html += '<div class="form-row"><label>存储路径/Endpoint</label><input type="text" class="form-input" id="configPath" value="' + (config.path || '') + '"></div>';
        html += '<div class="form-row"><label>Access Key</label><input type="text" class="form-input" id="configAccessKey" value="' + (config.accessKey || '') + '"></div>';
        html += '<div class="form-row"><label>Secret Key</label><input type="password" class="form-input" id="configSecretKey" value="' + (config.secretKey || '') + '"></div>';
        html += '<div class="form-row"><label>Bucket名称</label><input type="text" class="form-input" id="configBucket" value="' + (config.bucket || '') + '"></div>';
        html += '</div>';
    } else if (category === 'org') {
        html += '<div class="form-section">';
        html += '<h4>组织服务配置</h4>';
        html += '<div class="form-row"><label>App Key</label><input type="text" class="form-input" id="configAppKey" value="' + (config.appKey || '') + '"></div>';
        html += '<div class="form-row"><label>App Secret</label><input type="password" class="form-input" id="configAppSecret" value="' + (config.appSecret || '') + '"></div>';
        html += '<div class="form-row"><label>企业ID</label><input type="text" class="form-input" id="configCorpId" value="' + (config.corpId || '') + '"></div>';
        html += '</div>';
    } else {
        html += '<div class="form-section">';
        html += '<h4>通用配置</h4>';
        html += '<div class="form-row"><label>自定义配置 (JSON)</label><textarea class="form-textarea" id="configCustom" rows="4" placeholder=\'{"key": "value"}\'>' + (config.custom ? JSON.stringify(config.custom, null, 2) : '') + '</textarea></div>';
        html += '</div>';
    }

    html += '</div>';
    return html;
}

function closeConfigModal() {
    document.getElementById('configModal').style.display = 'none';
}

function saveDriverConfig() {
    var driverId = DriverConfig.currentDriver;
    if (!driverId) return;

    var config = {
        enabled: document.getElementById('configEnabled').checked,
        selected: document.getElementById('configSelected').checked
    };

    var apiKey = document.getElementById('configApiKey');
    if (apiKey) config.apiKey = apiKey.value;

    var apiBase = document.getElementById('configApiBase');
    if (apiBase) config.apiBase = apiBase.value;

    var model = document.getElementById('configModel');
    if (model) config.model = model.value;

    var host = document.getElementById('configHost');
    if (host) config.host = host.value;

    var port = document.getElementById('configPort');
    if (port) config.port = parseInt(port.value);

    var database = document.getElementById('configDatabase');
    if (database) config.database = database.value;

    var username = document.getElementById('configUsername');
    if (username) config.username = username.value;

    var password = document.getElementById('configPassword');
    if (password) config.password = password.value;

    DriverConfig.configs[driverId] = config;

    ApiClient.post('/api/v1/config/driver-configs/' + driverId, config)
        .then(function(result) {
            if (result.status === 'success') {
                closeConfigModal();
                DriverConfig.renderDriverCards();
                alert('配置保存成功');
            } else {
                alert('保存失败: ' + result.message);
            }
        })
        .catch(function(error) {
            console.error('[saveDriverConfig] Error:', error);
            closeConfigModal();
            DriverConfig.renderDriverCards();
        });
}

function testDriver(driverId) {
    var driver = DriverConfig.configs.find(function(d) { return d.driverId === driverId || d.id === driverId; });
    if (!driver) {
        alert('驱动配置不存在');
        return;
    }
    
    var resultContainer = document.getElementById('testResultContent');
    if (resultContainer) {
        resultContainer.innerHTML = '<div style="text-align: center; padding: 20px;"><i class="ri-loader-4-line ri-spin" style="font-size: 24px;"></i><p>正在测试连接...</p></div>';
        document.getElementById('testResultModal').style.display = 'flex';
    }
    
    fetch('/api/v1/drivers/' + driverId + '/test', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(driver.config || {})
    })
        .then(function(response) { return response.json(); })
        .then(function(result) {
            if (resultContainer) {
                var isSuccess = result.status === 'success' || result.connected === true;
                resultContainer.innerHTML = `
                    <div style="text-align: center; padding: 20px;">
                        <i class="ri-${isSuccess ? 'checkbox-circle' : 'close-circle'}-line" style="font-size: 48px; color: var(--nx-${isSuccess ? 'success' : 'danger'}-color);"></i>
                        <h4 style="margin: 16px 0 8px;">${isSuccess ? '连接成功' : '连接失败'}</h4>
                        <p style="color: var(--nx-text-secondary);">${result.message || (isSuccess ? '驱动连接测试通过' : '无法连接到驱动')}</p>
                        ${result.latency ? '<p style="font-size: 12px; color: var(--nx-text-secondary);">延迟: ' + result.latency + 'ms</p>' : ''}
                    </div>
                `;
            }
        })
        .catch(function(error) {
            if (resultContainer) {
                resultContainer.innerHTML = `
                    <div style="text-align: center; padding: 20px;">
                        <i class="ri-close-circle-line" style="font-size: 48px; color: var(--nx-danger-color);"></i>
                        <h4 style="margin: 16px 0 8px;">测试失败</h4>
                        <p style="color: var(--nx-text-secondary);">${error.message}</p>
                    </div>
                `;
            }
        });
}

function testAllConnections() {
    var resultContainer = document.getElementById('testResultContent');
    if (resultContainer) {
        resultContainer.innerHTML = '<div style="text-align: center; padding: 20px;"><i class="ri-loader-4-line ri-spin" style="font-size: 24px;"></i><p>正在测试所有连接...</p></div>';
        document.getElementById('testResultModal').style.display = 'flex';
    }
    
    fetch('/api/v1/drivers/test-all', {
        method: 'POST'
    })
        .then(function(response) { return response.json(); })
        .then(function(result) {
            if (resultContainer) {
                var results = result.results || [];
                var successCount = results.filter(function(r) { return r.success; }).length;
                var html = '<div style="padding: 16px;"><h4 style="margin-bottom: 12px;">测试结果</h4>';
                html += '<p style="margin-bottom: 16px;">成功: ' + successCount + ' / ' + results.length + '</p>';
                html += '<div style="max-height: 300px; overflow-y: auto;">';
                results.forEach(function(r) {
                    html += `
                        <div style="display: flex; align-items: center; gap: 8px; padding: 8px; border-bottom: 1px solid var(--nx-border-color);">
                            <i class="ri-${r.success ? 'checkbox-circle' : 'close-circle'}-line" style="color: var(--nx-${r.success ? 'success' : 'danger'}-color);"></i>
                            <span>${r.driverId || r.name}</span>
                            <span style="color: var(--nx-text-secondary); font-size: 12px;">${r.message || ''}</span>
                        </div>
                    `;
                });
                html += '</div></div>';
                resultContainer.innerHTML = html;
            }
        })
        .catch(function(error) {
            if (resultContainer) {
                resultContainer.innerHTML = `
                    <div style="text-align: center; padding: 20px;">
                        <i class="ri-close-circle-line" style="font-size: 48px; color: var(--nx-danger-color);"></i>
                        <h4 style="margin: 16px 0 8px;">测试失败</h4>
                        <p style="color: var(--nx-text-secondary);">${error.message}</p>
                    </div>
                `;
            }
        });
}

function saveAllConfigs() {
    ApiClient.post('/api/v1/config/driver-configs', DriverConfig.configs)
        .then(function(result) {
            if (result.status === 'success') {
                alert('全部配置保存成功');
            } else {
                alert('保存失败: ' + result.message);
            }
        })
        .catch(function(error) {
            console.error('[saveAllConfigs] Error:', error);
            alert('保存失败');
        });
}

function closeTestResultModal() {
    document.getElementById('testResultModal').style.display = 'none';
}

DriverConfig.init();
