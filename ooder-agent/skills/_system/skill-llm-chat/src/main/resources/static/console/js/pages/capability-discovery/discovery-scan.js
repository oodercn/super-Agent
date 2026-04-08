(function(global) {
'use strict';

var state = DiscoveryState;

var DiscoveryScan = {
    discoverFromGitService: function(apiUrl, method) {
        var config = {};
        if (method && method.configFields) {
            method.configFields.forEach(function(field) {
                var el = document.getElementById('config_' + field.name);
                if (el) config[field.name] = el.value;
            });
        }
        
        console.log('[discoverFromGitService] Starting discovery with config:', config);
        console.log('[discoverFromGitService] API URL:', apiUrl);
        
        ApiClient.post(apiUrl, config, { timeout: 120000 })
            .then(function(result) {
                console.log('[discoverFromGitService] API response:', result);
                if (result.status === 'success' && result.data) {
                    console.log('[discoverFromGitService] Processing result.data, capabilities count:', result.data.capabilities ? result.data.capabilities.length : 0);
                    try {
                        DiscoveryScan.processDiscoveryResult(result.data);
                        console.log('[discoverFromGitService] processDiscoveryResult completed');
                    } catch (e) {
                        console.error('[discoverFromGitService] processDiscoveryResult error:', e);
                    }
                } else {
                    console.error('[discoverFromGitService] Scan failed:', result.message);
                    DiscoveryUtils.addLog('error', '扫描失败: ' + (result.message || '未知错误'));
                    DiscoveryScan.showEmptyResult();
                }
            })
            .catch(function(error) {
                console.error('[discoverFromGitService] Error:', error);
                DiscoveryUtils.addLog('error', '扫描失败: ' + error.message);
                DiscoveryScan.showEmptyResult();
            })
            .finally(function() {
                DiscoveryScan.finishScan();
            });
    },

    discoverFromLocal: function() {
        ApiClient.post('/api/v1/discovery/local', {}, { timeout: 60000 })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    DiscoveryScan.processDiscoveryResult(result.data);
                } else {
                    DiscoveryUtils.addLog('error', '扫描失败: ' + (result.message || '未知错误'));
                    DiscoveryScan.showEmptyResult();
                }
            })
            .catch(function(error) {
                DiscoveryUtils.addLog('error', '扫描失败: ' + error.message);
                DiscoveryScan.showEmptyResult();
            })
            .finally(function() {
                DiscoveryScan.finishScan();
            });
    },

    discoverFromUdpBroadcast: function(method) {
        var config = {};
        if (method && method.configFields) {
            method.configFields.forEach(function(field) {
                var el = document.getElementById('config_' + field.name);
                if (el) config[field.name] = el.value;
            });
        }
        
        DiscoveryUtils.addLog('info', '开始UDP广播发现...');
        ApiClient.post('/api/v1/discovery/udp', config, { timeout: 30000 })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    DiscoveryScan.processDiscoveryResult(result.data);
                } else {
                    DiscoveryUtils.addLog('error', 'UDP广播发现失败: ' + (result.message || '未知错误'));
                    DiscoveryScan.showEmptyResult();
                }
            })
            .catch(function(error) {
                DiscoveryUtils.addLog('error', 'UDP广播发现失败: ' + error.message);
                DiscoveryScan.showEmptyResult();
            })
            .finally(function() {
                DiscoveryScan.finishScan();
            });
    },

    discoverFromMdns: function(method) {
        var config = {};
        if (method && method.configFields) {
            method.configFields.forEach(function(field) {
                var el = document.getElementById('config_' + field.name);
                if (el) config[field.name] = el.value;
            });
        }
        
        DiscoveryUtils.addLog('info', '开始mDNS服务发现...');
        ApiClient.post('/api/v1/discovery/mdns', config, { timeout: 30000 })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    DiscoveryScan.processDiscoveryResult(result.data);
                } else {
                    DiscoveryUtils.addLog('error', 'mDNS发现失败: ' + (result.message || '未知错误'));
                    DiscoveryScan.showEmptyResult();
                }
            })
            .catch(function(error) {
                DiscoveryUtils.addLog('error', 'mDNS发现失败: ' + error.message);
                DiscoveryScan.showEmptyResult();
            })
            .finally(function() {
                DiscoveryScan.finishScan();
            });
    },

    discoverFromRestApi: function(method) {
        var config = {};
        if (method && method.configFields) {
            method.configFields.forEach(function(field) {
                var el = document.getElementById('config_' + field.name);
                if (el) config[field.name] = el.value;
            });
        }
        
        DiscoveryUtils.addLog('info', '开始REST API发现...');
        ApiClient.post('/api/v1/discovery/rest-api', config, { timeout: 60000 })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    DiscoveryScan.processDiscoveryResult(result.data);
                } else {
                    DiscoveryUtils.addLog('error', 'REST API发现失败: ' + (result.message || '未知错误'));
                    DiscoveryScan.showEmptyResult();
                }
            })
            .catch(function(error) {
                DiscoveryUtils.addLog('error', 'REST API发现失败: ' + error.message);
                DiscoveryScan.showEmptyResult();
            })
            .finally(function() {
                DiscoveryScan.finishScan();
            });
    },

    discoverFromSkillCenter: function(method) {
        var config = {};
        if (method && method.configFields) {
            method.configFields.forEach(function(field) {
                var el = document.getElementById('config_' + field.name);
                if (el) config[field.name] = el.value;
            });
        }
        
        DiscoveryUtils.addLog('info', '开始从能力中心发现...');
        ApiClient.post('/api/v1/discovery/skill-center', config, { timeout: 60000 })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    DiscoveryScan.processDiscoveryResult(result.data);
                } else {
                    DiscoveryUtils.addLog('error', '能力中心发现失败: ' + (result.message || '未知错误'));
                    DiscoveryScan.showEmptyResult();
                }
            })
            .catch(function(error) {
                DiscoveryUtils.addLog('error', '能力中心发现失败: ' + error.message);
                DiscoveryScan.showEmptyResult();
            })
            .finally(function() {
                DiscoveryScan.finishScan();
            });
    },

    discoverFromGitRepository: function(method) {
        var config = {};
        if (method && method.configFields) {
            method.configFields.forEach(function(field) {
                var el = document.getElementById('config_' + field.name);
                if (el) config[field.name] = el.value;
            });
        }
        
        DiscoveryUtils.addLog('info', '开始从Git仓库发现...');
        ApiClient.post('/api/v1/discovery/git-repository', config, { timeout: 120000 })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    DiscoveryScan.processDiscoveryResult(result.data);
                } else {
                    DiscoveryUtils.addLog('error', 'Git仓库发现失败: ' + (result.message || '未知错误'));
                    DiscoveryScan.showEmptyResult();
                }
            })
            .catch(function(error) {
                DiscoveryUtils.addLog('error', 'Git仓库发现失败: ' + error.message);
                DiscoveryScan.showEmptyResult();
            })
            .finally(function() {
                DiscoveryScan.finishScan();
            });
    },

    processDiscoveryResult: function(data) {
        console.log('[processDiscoveryResult] Processing data:', data);
        try {
            var caps = data.capabilities || [];
            console.log('[processDiscoveryResult] capabilities array length:', caps.length);
            console.log('[processDiscoveryResult] first 3 capabilities:', caps.slice(0, 3));
            var newCount = 0;
            var installedCount = 0;
            state.discoveredCapabilities = [];
            caps.forEach(function(cap) {
                console.log('[processDiscoveryResult] Processing cap:', cap.id || cap.skillId, 'skillForm:', cap.skillForm);
                if (cap.skillForm === 'INTERNAL') {
                    console.log('[processDiscoveryResult] Filtering out INTERNAL skill:', cap.id || cap.skillId);
                    return;
                }
                var isInstalled = cap.installed === true || cap.installed === 'true';
                var skillId = cap.skillId || cap.id || cap.capabilityId;
                var capability = {
                    capabilityId: skillId,
                    id: skillId,
                    skillId: skillId,
                    name: cap.name,
                    type: cap.type || cap.capabilityType,
                    capabilityType: cap.capabilityType || cap.type,
                    description: cap.description,
                    version: cap.version || '1.0.0',
                    source: cap.source || state.currentMethod,
                    isSceneCapability: cap.isSceneCapability || cap.sceneCapability || false,
                    sceneCapability: cap.sceneCapability || cap.isSceneCapability || false,
                    dependencies: cap.dependencies || [],
                    provider: cap.provider || null,
                    installed: isInstalled,
                    category: cap.category || 'NOT_SCENE_SKILL',
                    skillForm: cap.skillForm || 'PROVIDER',
                    sceneType: cap.sceneType || null,
                    skillCategory: cap.skillCategory || null,
                    businessCategory: cap.businessCategory || null,
                    hasSelfDrive: cap.hasSelfDrive || false,
                    businessSemanticsScore: cap.businessSemanticsScore || 5,
                    visibility: cap.visibility || 'public',
                    capabilityCategory: cap.capabilityCategory || null,
                    tags: cap.tags || [],
                    roles: cap.roles || cap.participants || [],
                    participants: cap.participants || [],
                    driverConditions: cap.driverConditions || [],
                    capabilities: cap.capabilities || [],
                    metadata: cap.metadata || {},
                    icon: cap.icon || null,
                    ownership: cap.ownership || null,
                    mainFirst: cap.mainFirst || false,
                    mainFirstConfig: cap.mainFirstConfig || null,
                    driverType: cap.driverType || null,
                    enabled: cap.enabled || false
                };
                state.discoveredCapabilities.push(capability);
                if (isInstalled) { installedCount++; } else { newCount++; }
            });
            console.log('[processDiscoveryResult] Final discoveredCapabilities length:', state.discoveredCapabilities.length);
            state.scanStats.found = state.discoveredCapabilities.length;
            state.scanStats.new = newCount;
            state.scanStats.installed = installedCount;
            state.scanStats.scanned = data.total || caps.length;
            DiscoveryUtils.addLog('success', '发现 ' + state.discoveredCapabilities.length + ' 个能力（已过滤内部服务）');
            console.log('[processDiscoveryResult] Calling renderResults...');
            DiscoveryResult.renderResults();
            console.log('[processDiscoveryResult] Calling addRadarDots...');
            DiscoveryCore.addRadarDots();
            console.log('[processDiscoveryResult] Completed successfully');
        } catch (e) {
            console.error('[processDiscoveryResult] Error:', e);
            DiscoveryUtils.addLog('error', '处理结果失败: ' + e.message);
        }
    },

    finishScan: function() {
        console.log('[finishScan] Starting finish scan');
        state.isScanning = false;
        var sweep = document.getElementById('radarSweep');
        if (sweep) sweep.classList.add('idle');
        var startBtn = document.getElementById('startBtn');
        if (startBtn) startBtn.disabled = false;
        var statusText = document.getElementById('radarStatusText');
        if (statusText) statusText.textContent = '扫描完成';
        if (state.currentMethod) {
            var badge = document.getElementById('badge-' + state.currentMethod);
            if (badge) {
                badge.textContent = '完成';
                badge.className = 'method-badge success';
            }
        }
        DiscoveryCore.updateStats();
        console.log('[finishScan] Finish scan completed');
    },

    showEmptyResult: function() {
        state.discoveredCapabilities = [];
        state.scanStats.found = 0;
        state.scanStats.scanned = 0;
        DiscoveryResult.renderResults();
    }
};

global.DiscoveryScan = DiscoveryScan;

})(typeof window !== 'undefined' ? window : this);
