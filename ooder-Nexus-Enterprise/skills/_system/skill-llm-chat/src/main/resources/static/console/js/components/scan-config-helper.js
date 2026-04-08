(function(global) {
'use strict';

var ScanConfigHelper = {
    providers: {
        dingding: {
            name: '钉钉',
            icon: 'ri-message-2-line',
            color: '#0089FF',
            fields: [
                { name: 'appKey', label: 'AppKey', type: 'text', required: true },
                { name: 'appSecret', label: 'AppSecret', type: 'password', required: true },
                { name: 'corpId', label: 'CorpId', type: 'text', required: true }
            ],
            scanUrl: '/api/v1/scan/dingding',
            helpUrl: 'https://open.dingtalk.com/document/orgapp/obtain-the-appkey-and-appsecret'
        },
        feishu: {
            name: '飞书',
            icon: 'ri-send-plane-line',
            color: '#3370FF',
            fields: [
                { name: 'appId', label: 'App ID', type: 'text', required: true },
                { name: 'appSecret', label: 'App Secret', type: 'password', required: true }
            ],
            scanUrl: '/api/v1/scan/feishu',
            helpUrl: 'https://open.feishu.cn/document/home/introduction-to-feishu-open-platform/'
        },
        wecom: {
            name: '企业微信',
            icon: 'ri-wechat-line',
            color: '#07C160',
            fields: [
                { name: 'corpId', label: 'CorpId', type: 'text', required: true },
                { name: 'agentId', label: 'AgentId', type: 'text', required: true },
                { name: 'secret', label: 'Secret', type: 'password', required: true }
            ],
            scanUrl: '/api/v1/scan/wecom',
            helpUrl: 'https://developer.work.weixin.qq.com/document/'
        }
    },

    init: function(containerId, provider, onConfigLoaded) {
        var container = document.getElementById(containerId);
        if (!container) return;

        var providerConfig = this.providers[provider];
        if (!providerConfig) return;

        this.renderScanSection(container, providerConfig, onConfigLoaded);
    },

    renderScanSection: function(container, provider, onConfigLoaded) {
        var self = this;
        var html = 
            '<div class="scan-config-section">' +
            '<div class="scan-config-header">' +
            '<div class="scan-config-title">' +
            '<i class="' + provider.icon + '" style="color: ' + provider.color + ';"></i>' +
            '<span>' + provider.name + ' 配置</span>' +
            '</div>' +
            '<div class="scan-config-actions">' +
            '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="ScanConfigHelper.showScanModal(\'' + provider.name + '\')">' +
            '<i class="ri-qr-code-line"></i> 扫码配置' +
            '</button>' +
            '<a href="' + provider.helpUrl + '" target="_blank" class="nx-btn nx-btn--ghost nx-btn--sm">' +
            '<i class="ri-question-line"></i> 帮助' +
            '</a>' +
            '</div>' +
            '</div>' +
            '<div class="scan-config-body">' +
            '<div class="scan-config-form">';

        provider.fields.forEach(function(field) {
            html += '<div class="form-group">' +
                '<label>' + field.label;
            if (field.required) {
                html += ' <span class="required">*</span>';
            }
            html += '</label>' +
                '<input type="' + field.type + '" class="form-input" id="scan_' + field.name + '" ' +
                'data-field="' + field.name + '" placeholder="请输入' + field.label + '">' +
                '</div>';
        });

        html += '</div>' +
            '<div class="scan-config-status" id="scanStatus_' + provider.name + '">' +
            '<div class="scan-status-item">' +
            '<i class="ri-checkbox-blank-circle-line"></i>' +
            '<span>未配置</span>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';

        container.innerHTML = html;

        this.loadSavedConfig(provider, onConfigLoaded);
    },

    showScanModal: function(providerName) {
        var self = this;
        var modal = document.getElementById('scanConfigModal');
        
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'scanConfigModal';
            modal.className = 'modal-overlay';
            document.body.appendChild(modal);
        }

        var provider = Object.values(this.providers).find(function(p) { return p.name === providerName; });
        if (!provider) return;

        modal.innerHTML = 
            '<div class="modal-content scan-modal">' +
            '<div class="modal-header">' +
            '<h3><i class="' + provider.icon + '" style="color: ' + provider.color + ';"></i> ' + provider.name + ' 扫码配置</h3>' +
            '<button class="modal-close" onclick="ScanConfigHelper.closeScanModal()"><i class="ri-close-line"></i></button>' +
            '</div>' +
            '<div class="modal-body">' +
            '<div class="scan-instructions">' +
            '<div class="scan-step">' +
            '<div class="scan-step-num">1</div>' +
            '<div class="scan-step-content">' +
            '<h4>打开' + provider.name + '移动端</h4>' +
            '<p>在手机上打开' + provider.name + '应用，进入扫一扫功能</p>' +
            '</div>' +
            '</div>' +
            '<div class="scan-step">' +
            '<div class="scan-step-num">2</div>' +
            '<div class="scan-step-content">' +
            '<h4>扫描下方二维码</h4>' +
            '<p>使用' + provider.name + '扫描下方二维码进行授权配置</p>' +
            '</div>' +
            '</div>' +
            '<div class="scan-step">' +
            '<div class="scan-step-num">3</div>' +
            '<div class="scan-step-content">' +
            '<h4>确认授权</h4>' +
            '<p>在手机上确认授权后，配置信息将自动填充</p>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="scan-qrcode-container">' +
            '<div class="scan-qrcode" id="scanQrCode">' +
            '<div class="qrcode-placeholder">' +
            '<i class="ri-qr-code-line"></i>' +
            '<p>正在生成二维码...</p>' +
            '</div>' +
            '</div>' +
            '<div class="scan-status" id="scanStatus">' +
            '<span class="scan-status-text">等待扫码...</span>' +
            '<span class="scan-status-time" id="scanCountdown">120s</span>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button class="nx-btn nx-btn--secondary" onclick="ScanConfigHelper.closeScanModal()">取消</button>' +
            '<button class="nx-btn nx-btn--primary" onclick="ScanConfigHelper.refreshQrCode(\'' + provider.name + '\')">' +
            '<i class="ri-refresh-line"></i> 刷新二维码' +
            '</button>' +
            '</div>' +
            '</div>';

        modal.classList.add('show');
        this.generateQrCode(provider);
        this.startPolling(provider);
    },

    closeScanModal: function() {
        var modal = document.getElementById('scanConfigModal');
        if (modal) {
            modal.classList.remove('show');
        }
        this.stopPolling();
    },

    generateQrCode: function(provider) {
        var qrContainer = document.getElementById('scanQrCode');
        if (!qrContainer) return;

        var qrData = 'apexos://scan/config?provider=' + provider.name.toLowerCase() + '&token=' + this.generateToken();
        
        qrContainer.innerHTML = '<div class="qrcode-image" style="width: 200px; height: 200px; background: #f5f5f5; display: flex; align-items: center; justify-content: center; border-radius: 8px;">' +
            '<div style="text-align: center;">' +
            '<i class="ri-qr-code-line" style="font-size: 48px; color: #999;"></i>' +
            '<p style="font-size: 12px; color: #666; margin-top: 8px;">扫码配置</p>' +
            '</div>' +
            '</div>';

        this.currentScanToken = qrData.split('token=')[1];
        this.scanCountdown = 120;
        this.updateCountdown();
    },

    refreshQrCode: function(providerName) {
        var provider = Object.values(this.providers).find(function(p) { return p.name === providerName; });
        if (provider) {
            this.generateQrCode(provider);
        }
    },

    generateToken: function() {
        return 'scan_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    },

    startPolling: function(provider) {
        var self = this;
        this.pollingInterval = setInterval(function() {
            self.checkScanStatus(provider);
        }, 2000);
    },

    stopPolling: function() {
        if (this.pollingInterval) {
            clearInterval(this.pollingInterval);
            this.pollingInterval = null;
        }
        if (this.countdownInterval) {
            clearInterval(this.countdownInterval);
            this.countdownInterval = null;
        }
    },

    checkScanStatus: function(provider) {
        var self = this;
        
        ApiClient.get(provider.scanUrl + '/status?token=' + this.currentScanToken)
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    if (result.data.scanned) {
                        self.updateScanStatus('scanned');
                    }
                    if (result.data.confirmed && result.data.config) {
                        self.applyScanConfig(provider, result.data.config);
                        self.closeScanModal();
                    }
                }
            })
            .catch(function(error) {
                console.log('[ScanConfigHelper] Polling error:', error);
            });
    },

    updateScanStatus: function(status) {
        var statusEl = document.getElementById('scanStatus');
        if (!statusEl) return;

        var textEl = statusEl.querySelector('.scan-status-text');
        if (status === 'scanned' && textEl) {
            textEl.textContent = '已扫码，等待确认...';
            textEl.style.color = 'var(--nx-warning)';
        }
    },

    updateCountdown: function() {
        var self = this;
        this.countdownInterval = setInterval(function() {
            self.scanCountdown--;
            var countdownEl = document.getElementById('scanCountdown');
            if (countdownEl) {
                countdownEl.textContent = self.scanCountdown + 's';
            }
            if (self.scanCountdown <= 0) {
                self.stopPolling();
                self.closeScanModal();
            }
        }, 1000);
    },

    applyScanConfig: function(provider, config) {
        var self = this;
        provider.fields.forEach(function(field) {
            var input = document.getElementById('scan_' + field.name);
            if (input && config[field.name]) {
                input.value = config[field.name];
            }
        });

        this.updateConfigStatus(provider.name, 'configured');

        if (this.onConfigLoaded) {
            this.onConfigLoaded(config);
        }

        if (typeof NX !== 'undefined' && NX.notify) {
            NX.notify.success(provider.name + ' 配置已自动填充');
        }
    },

    updateConfigStatus: function(providerName, status) {
        var statusEl = document.getElementById('scanStatus_' + providerName);
        if (!statusEl) return;

        var icon, text, color;
        if (status === 'configured') {
            icon = 'ri-checkbox-circle-fill';
            text = '已配置';
            color = 'var(--nx-success)';
        } else if (status === 'error') {
            icon = 'ri-error-warning-fill';
            text = '配置错误';
            color = 'var(--nx-danger)';
        } else {
            icon = 'ri-checkbox-blank-circle-line';
            text = '未配置';
            color = 'var(--nx-muted)';
        }

        statusEl.innerHTML = 
            '<div class="scan-status-item" style="color: ' + color + ';">' +
            '<i class="' + icon + '"></i>' +
            '<span>' + text + '</span>' +
            '</div>';
    },

    loadSavedConfig: function(provider, callback) {
        var self = this;
        ApiClient.get('/api/v1/config/' + provider.name.toLowerCase())
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    provider.fields.forEach(function(field) {
                        var input = document.getElementById('scan_' + field.name);
                        if (input && result.data[field.name]) {
                            input.value = result.data[field.name];
                        }
                    });
                    self.updateConfigStatus(provider.name, 'configured');
                    if (callback) callback(result.data);
                }
            })
            .catch(function(error) {
                console.log('[ScanConfigHelper] Load config error:', error);
            });
    },

    getConfig: function(providerName) {
        var provider = this.providers[providerName.toLowerCase()];
        if (!provider) return null;

        var config = {};
        provider.fields.forEach(function(field) {
            var input = document.getElementById('scan_' + field.name);
            if (input) {
                config[field.name] = input.value;
            }
        });
        return config;
    },

    saveConfig: function(providerName) {
        var self = this;
        var config = this.getConfig(providerName);
        
        return ApiClient.put('/api/v1/config/' + providerName.toLowerCase(), config)
            .then(function(result) {
                if (result.status === 'success') {
                    self.updateConfigStatus(providerName, 'configured');
                    return { success: true };
                }
                return { success: false, message: result.message };
            })
            .catch(function(error) {
                return { success: false, message: error.message };
            });
    },

    testConnection: function(providerName) {
        var config = this.getConfig(providerName);
        
        return ApiClient.post('/api/v1/config/' + providerName.toLowerCase() + '/test', config)
            .then(function(result) {
                if (result.status === 'success') {
                    return { success: true, data: result.data };
                }
                return { success: false, message: result.message };
            })
            .catch(function(error) {
                return { success: false, message: error.message };
            });
    }
};

var style = document.createElement('style');
style.textContent = 
    '.scan-config-section { background: var(--nx-card-bg); border: 1px solid var(--nx-border); border-radius: 8px; padding: 16px; margin-bottom: 16px; }' +
    '.scan-config-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; padding-bottom: 12px; border-bottom: 1px solid var(--nx-border); }' +
    '.scan-config-title { display: flex; align-items: center; gap: 8px; font-weight: 500; }' +
    '.scan-config-title i { font-size: 20px; }' +
    '.scan-config-actions { display: flex; gap: 8px; }' +
    '.scan-config-body { display: flex; gap: 24px; }' +
    '.scan-config-form { flex: 1; display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 12px; }' +
    '.scan-config-status { min-width: 120px; display: flex; align-items: center; justify-content: center; }' +
    '.scan-status-item { display: flex; align-items: center; gap: 6px; font-size: 14px; }' +
    '.scan-modal { max-width: 600px; }' +
    '.scan-instructions { margin-bottom: 24px; }' +
    '.scan-step { display: flex; gap: 16px; margin-bottom: 16px; }' +
    '.scan-step-num { width: 32px; height: 32px; border-radius: 50%; background: var(--nx-primary); color: white; display: flex; align-items: center; justify-content: center; font-weight: bold; flex-shrink: 0; }' +
    '.scan-step-content h4 { margin: 0 0 4px; font-size: 14px; }' +
    '.scan-step-content p { margin: 0; font-size: 12px; color: var(--nx-text-secondary); }' +
    '.scan-qrcode-container { display: flex; flex-direction: column; align-items: center; padding: 24px; background: var(--nx-bg-secondary); border-radius: 8px; }' +
    '.scan-qrcode { margin-bottom: 16px; }' +
    '.qrcode-placeholder { width: 200px; height: 200px; display: flex; flex-direction: column; align-items: center; justify-content: center; background: white; border-radius: 8px; }' +
    '.qrcode-placeholder i { font-size: 48px; color: #999; }' +
    '.scan-status { display: flex; align-items: center; gap: 12px; font-size: 14px; }' +
    '.scan-status-time { color: var(--nx-text-secondary); }' +
    '.required { color: var(--nx-danger); }';
document.head.appendChild(style);

global.ScanConfigHelper = ScanConfigHelper;

})(typeof window !== 'undefined' ? window : this);
