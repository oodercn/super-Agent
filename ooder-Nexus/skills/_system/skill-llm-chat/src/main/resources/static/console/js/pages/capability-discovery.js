(function(global) {'use strict';
var currentMethod = null;
var isScanning = false;
var discoveredCapabilities = [];
var logs = [];
var scanStats = { scanned: 0, found: 0, new: 0, installed: 0 };
var selectedBusinessCategory = null;
var currentInstallCap = null;
var currentInstallStep = 1;
var installSteps = [1, 5, 7, 8];
var selectedCapabilities = [];
var batchMode = false;

var DISCOVERY_METHODS = [];

var BUSINESS_CATEGORIES = [];

var CapabilityDiscovery = {
    init: function() {
        var self = this;
        window.onPageInit = function() {
            console.log('能力发现页面初始化完成');
            self.loadDiscoveryMethods().then(function() {
                self.loadCategories().then(function() {
                    CapabilityDiscovery.renderMethods();
                    CapabilityDiscovery.renderBusinessCategoryFilter();
                    CapabilityDiscovery.initFilters();
                });
            });
        };
    },

    loadDiscoveryMethods: function() {
        return fetch('/api/v1/discovery/methods')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    DISCOVERY_METHODS = result.data;
                }
            })
            .catch(function(error) {
                console.error('加载发现方法失败:', error);
                DISCOVERY_METHODS = [];
            });
    },

    loadCategories: function() {
        return CategoryService.loadCategories().then(function(categories) {
            BUSINESS_CATEGORIES = categories;
        });
    },

    renderMethods: function() {
        var container = document.getElementById('methodList');
        if (!container) return;
        var html = '';
        DISCOVERY_METHODS.forEach(function(method) {
            html += '<div class="method-item" data-method="' + method.id + '" onclick="selectMethod(\'' + method.id + '\')">' +
                '<div class="method-icon" style="background: ' + method.color + '20; color: ' + method.color + ';">' +
                '<i class="' + method.icon + '"></i></div>' +
                '<div class="method-info">' +
                '<div class="method-name">' + method.name + '</div>' +
                '<div class="method-desc">' + method.desc + '</div></div>' +
                '<span class="method-badge ready" id="badge-' + method.id + '">就绪</span></div>';
        });
        container.innerHTML = html;
        
        var giteeMethod = DISCOVERY_METHODS.find(function(m) { return m.id === 'GITEE'; });
        if (giteeMethod) {
            setTimeout(function() {
                CapabilityDiscovery.selectMethod('GITEE');
            }, 100);
        }
    },

    renderBusinessCategoryFilter: function() {
        var container = document.getElementById('businessCategoryFilter');
        if (!container) return;
        
        fetch('/api/v1/discovery/categories/user-facing')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result && result.status === 'success' && result.data) {
                    CapabilityDiscovery.renderUserFacingCategories(result.data);
                } else {
                    CapabilityDiscovery.renderDefaultCategories();
                }
            })
            .catch(function(error) {
                console.error('加载用户可见分类失败:', error);
                CapabilityDiscovery.renderDefaultCategories();
            });
    },

    renderUserFacingCategories: function(categories) {
        var container = document.getElementById('businessCategoryFilter');
        if (!container) return;
        var html = '<span class="filter-group-label">业务领域:</span>';
        html += '<span class="filter-chip" data-bc="" onclick="selectBusinessCategory(\'\')" title="全部业务领域"><i class="ri-apps-line"></i> 全部</span>';
        categories.forEach(function(cat) {
            html += '<span class="filter-chip" data-bc="' + cat.id + '" onclick="selectBusinessCategory(\'' + cat.id + '\')" title="' + (cat.desc || cat.name) + '">' +
                '<i class="' + cat.icon + '"></i> ' + cat.name + '</span>';
        });
        container.innerHTML = html;
    },

    renderDefaultCategories: function() {
        var container = document.getElementById('businessCategoryFilter');
        if (!container) return;
        var userFacingCategories = CategoryService.getUserFacing();
        var html = '<span class="filter-group-label">业务领域:</span>';
        html += '<span class="filter-chip" data-bc="" onclick="selectBusinessCategory(\'\')" title="全部业务领域"><i class="ri-apps-line"></i> 全部</span>';
        userFacingCategories.forEach(function(cat) {
            html += '<span class="filter-chip" data-bc="' + cat.code + '" onclick="selectBusinessCategory(\'' + cat.code + '\')" title="' + (cat.desc || cat.name) + '">' +
                '<i class="' + cat.icon + '"></i> ' + cat.name + '</span>';
        });
        container.innerHTML = html;
    },

    selectMethod: function(methodId) {
        currentMethod = methodId;
        document.querySelectorAll('.method-item').forEach(function(item) {
            item.classList.remove('active');
            if (item.dataset.method === methodId) {
                item.classList.add('active');
            }
        });
        var method = DISCOVERY_METHODS.find(function(m) { return m.id === methodId; });
        if (method) {
            document.getElementById('radarTitle').textContent = method.name + ' 扫描';
            CapabilityDiscovery.hideConfig();
        }
    },

    showConfig: function(method) {
        var panel = document.getElementById('configPanel');
        var form = document.getElementById('configForm');
        var title = document.getElementById('configTitle');
        title.textContent = method.name + ' 配置';
        var html = '';
        method.configFields.forEach(function(field) {
            html += '<div class="form-group">' +
                '<label class="form-label">' + field.label + '</label>';
            if (field.type === 'password') {
                html += '<input type="password" class="form-input" id="config_' + field.name + '" placeholder="' + (field.placeholder || '') + '">';
            } else {
                html += '<input type="text" class="form-input" id="config_' + field.name + '" value="' + (field.default || '') + '" placeholder="' + (field.placeholder || '') + '">';
            }
            html += '</div>';
        });
        form.innerHTML = html;
        panel.classList.add('show');
    },

    hideConfig: function() {
        document.getElementById('configPanel').classList.remove('show');
    },

    startScan: function() {
        console.log('[startScan] Starting scan, currentMethod:', currentMethod);
        if (!currentMethod) {
            alert('请先选择发现途径');
            return;
        }
        if (isScanning) return;
        isScanning = true;
        discoveredCapabilities = [];
        scanStats = { scanned: 0, found: 0, new: 0, installed: 0 };
        var sweep = document.getElementById('radarSweep');
        if (sweep) sweep.classList.remove('idle');
        var startBtn = document.getElementById('startBtn');
        if (startBtn) startBtn.disabled = true;
        var statusText = document.getElementById('radarStatusText');
        if (statusText) statusText.textContent = '扫描中...';
        var badge = document.getElementById('badge-' + currentMethod);
        if (badge) {
            badge.textContent = '扫描中';
            badge.className = 'method-badge running';
        }
        CapabilityDiscovery.updateStats();
        CapabilityDiscovery.addLog('info', '开始扫描: ' + currentMethod);
        var method = DISCOVERY_METHODS.find(function(m) { return m.id === currentMethod; });
        console.log('[startScan] Found method:', method);
        if (currentMethod === 'GITHUB') {
            CapabilityDiscovery.discoverFromGitService('/api/v1/discovery/github', method);
        } else if (currentMethod === 'GITEE') {
            CapabilityDiscovery.discoverFromGitService('/api/v1/discovery/gitee', method);
        } else {
            CapabilityDiscovery.discoverFromLocal();
        }
    },

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
                console.log('[discoverFromGitService] result.status:', result.status);
                console.log('[discoverFromGitService] result.data:', result.data);
                console.log('[discoverFromGitService] result.data type:', typeof result.data);
                if (result.status === 'success' && result.data) {
                    console.log('[discoverFromGitService] Processing result.data, capabilities count:', result.data.capabilities ? result.data.capabilities.length : 0);
                    console.log('[discoverFromGitService] Calling processDiscoveryResult...');
                    try {
                        CapabilityDiscovery.processDiscoveryResult(result.data);
                        console.log('[discoverFromGitService] processDiscoveryResult completed');
                    } catch (e) {
                        console.error('[discoverFromGitService] processDiscoveryResult error:', e);
                    }
                } else {
                    console.error('[discoverFromGitService] Scan failed:', result.message);
                    CapabilityDiscovery.addLog('error', '扫描失败: ' + (result.message || '未知错误'));
                    CapabilityDiscovery.showEmptyResult();
                }
            })
            .catch(function(error) {
                console.error('[discoverFromGitService] Error:', error);
                CapabilityDiscovery.addLog('error', '扫描失败: ' + error.message);
                CapabilityDiscovery.showEmptyResult();
            })
            .finally(function() {
                CapabilityDiscovery.finishScan();
            });
    },

    discoverFromLocal: function() {
        ApiClient.post('/api/v1/discovery/local', {}, { timeout: 60000 })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    CapabilityDiscovery.processDiscoveryResult(result.data);
                } else {
                    CapabilityDiscovery.addLog('error', '扫描失败: ' + (result.message || '未知错误'));
                    CapabilityDiscovery.showEmptyResult();
                }
            })
            .catch(function(error) {
                CapabilityDiscovery.addLog('error', '扫描失败: ' + error.message);
                CapabilityDiscovery.showEmptyResult();
            })
            .finally(function() {
                CapabilityDiscovery.finishScan();
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
            discoveredCapabilities = [];
            caps.forEach(function(cap) {
                console.log('[processDiscoveryResult] Processing cap:', cap.id, 'skillForm:', cap.skillForm);
                if (cap.skillForm === 'INTERNAL') {
                    console.log('[processDiscoveryResult] Filtering out INTERNAL skill:', cap.id);
                    return;
                }
                var isInstalled = cap.installed === true;
                discoveredCapabilities.push({
                    capabilityId: cap.id,
                    id: cap.id,
                    name: cap.name,
                    type: cap.type,
                    description: cap.description,
                    version: cap.version,
                    source: currentMethod,
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
                    skillId: cap.skillId || null,
                    visibility: cap.visibility || 'public',
                    capabilityCategory: cap.capabilityCategory || null,
                    tags: cap.tags || [],
                    roles: cap.roles || [],
                    participants: cap.participants || []
                });
                if (isInstalled) { installedCount++; } else { newCount++; }
            });
            console.log('[processDiscoveryResult] Final discoveredCapabilities length:', discoveredCapabilities.length);
            scanStats.found = discoveredCapabilities.length;
            scanStats.new = newCount;
            scanStats.installed = installedCount;
            scanStats.scanned = data.total || caps.length;
            CapabilityDiscovery.addLog('success', '发现 ' + discoveredCapabilities.length + ' 个能力（已过滤内部服务）');
            console.log('[processDiscoveryResult] Calling renderResults...');
            CapabilityDiscovery.renderResults();
            console.log('[processDiscoveryResult] Calling addRadarDots...');
            CapabilityDiscovery.addRadarDots();
            console.log('[processDiscoveryResult] Completed successfully');
        } catch (e) {
            console.error('[processDiscoveryResult] Error:', e);
            CapabilityDiscovery.addLog('error', '处理结果失败: ' + e.message);
        }
    },

    finishScan: function() {
        console.log('[finishScan] Starting finish scan');
        isScanning = false;
        var sweep = document.getElementById('radarSweep');
        if (sweep) sweep.classList.add('idle');
        var startBtn = document.getElementById('startBtn');
        if (startBtn) startBtn.disabled = false;
        var statusText = document.getElementById('radarStatusText');
        if (statusText) statusText.textContent = '扫描完成';
        if (currentMethod) {
            var badge = document.getElementById('badge-' + currentMethod);
            if (badge) {
                badge.textContent = '完成';
                badge.className = 'method-badge success';
            }
        }
        CapabilityDiscovery.updateStats();
        console.log('[finishScan] Finish scan completed');
    },

    showEmptyResult: function() {
        discoveredCapabilities = [];
        scanStats.found = 0;
        scanStats.scanned = 0;
        CapabilityDiscovery.renderResults();
    },

    addRadarDots: function() {
        var container = document.getElementById('radarDots');
        if (!container) return;
        container.innerHTML = '';
        var count = Math.min(discoveredCapabilities.length, 20);
        for (var i = 0; i < count; i++) {
            var dot = document.createElement('div');
            dot.className = 'radar-dot';
            dot.style.left = (20 + Math.random() * 60) + '%';
            dot.style.top = (20 + Math.random() * 60) + '%';
            dot.style.animationDelay = (Math.random() * 2) + 's';
            container.appendChild(dot);
        }
    },

    updateStats: function() {
        document.getElementById('statScanned').textContent = scanStats.scanned;
        document.getElementById('statFound').textContent = scanStats.found;
        document.getElementById('statNew').textContent = scanStats.new;
        document.getElementById('statInstalled').textContent = scanStats.installed;
    },

    initFilters: function() {
        var primaryFilterContainer = document.querySelector('.results-filter--primary');
        if (primaryFilterContainer) {
            primaryFilterContainer.addEventListener('click', function(e) {
                var chip = e.target.closest('.filter-chip');
                if (!chip || chip.dataset.bc !== undefined) return;
                primaryFilterContainer.querySelectorAll('.filter-chip').forEach(function(c) { c.classList.remove('active'); });
                chip.classList.add('active');
                var filter = chip.dataset.filter;
                CapabilityDiscovery.applyFilter(filter);
            });
        }
        
        var businessFilterContainer = document.getElementById('businessCategoryFilter');
        if (businessFilterContainer) {
            businessFilterContainer.addEventListener('click', function(e) {
                var chip = e.target.closest('.filter-chip');
                if (!chip || chip.dataset.bc === undefined) return;
                businessFilterContainer.querySelectorAll('.filter-chip').forEach(function(c) { c.classList.remove('active'); });
                chip.classList.add('active');
                var bc = chip.dataset.bc;
                selectedBusinessCategory = bc;
                CapabilityDiscovery.applyBusinessCategoryFilter(bc);
            });
        }
        
        var scoreFilterContainer = document.getElementById('scoreFilter');
        if (scoreFilterContainer) {
            scoreFilterContainer.addEventListener('click', function(e) {
                var chip = e.target.closest('.filter-chip');
                if (!chip || chip.dataset.score === undefined) return;
                scoreFilterContainer.querySelectorAll('.filter-chip').forEach(function(c) { c.classList.remove('active'); });
                chip.classList.add('active');
                CapabilityDiscovery.applyScoreFilter(chip.dataset.score);
            });
        }
    },

    applyScoreFilter: function(scoreRange) {
        var container = document.getElementById('resultsBody');
        if (!container) return;
        var items = container.querySelectorAll('.result-item');
        var visibleCount = 0;
        
        items.forEach(function(item) {
            var score = parseInt(item.dataset.score) || 0;
            var show = false;
            
            if (scoreRange === 'all') {
                show = true;
            } else if (scoreRange === 'high' && score >= 8) {
                show = true;
            } else if (scoreRange === 'medium' && score >= 3 && score < 8) {
                show = true;
            } else if (scoreRange === 'low' && score < 3) {
                show = true;
            }
            
            if (show && item.style.display !== 'none') {
                visibleCount++;
            }
            
            if (show) {
                item.style.display = '';
            } else {
                item.style.display = 'none';
            }
        });
        
        document.getElementById('resultsCount').textContent = visibleCount;
    },

    applyBusinessCategoryFilter: function(bc) {
        var container = document.getElementById('resultsBody');
        if (!container) return;
        var items = container.querySelectorAll('.result-item');
        var visibleCount = 0;
        
        items.forEach(function(item) {
            var itemBc = item.dataset.businessCategory;
            var show = !bc || itemBc === bc;
            
            if (show) {
                item.style.display = '';
                visibleCount++;
            } else {
                item.style.display = 'none';
            }
        });
        
        document.getElementById('resultsCount').textContent = visibleCount;
    },

    selectBusinessCategory: function(bc) {
        selectedBusinessCategory = bc;
        var container = document.getElementById('businessCategoryFilter');
        if (!container) return;
        container.querySelectorAll('.filter-chip').forEach(function(chip) {
            chip.classList.remove('active');
            if (chip.dataset.bc === bc) {
                chip.classList.add('active');
            }
        });
        CapabilityDiscovery.applyBusinessCategoryFilter(bc);
    },

    applyFilter: function(filter) {
        var visibleCount = 0;
        var container = document.getElementById('resultsBody');
        if (!container) return;
        var items = container.querySelectorAll('.result-item');
        items.forEach(function(item) {
            var skillForm = item.dataset.skillForm;
            var sceneType = item.dataset.sceneType;
            var businessCategory = item.dataset.businessCategory;
            var installed = item.dataset.installed === 'true';
            var show = false;
            if (filter === 'all') { show = true; }
            else if (filter === 'scene' && skillForm === 'SCENE') { show = true; }
            else if (filter === 'provider' && skillForm === 'PROVIDER') { show = true; }
            else if (filter === 'driver' && skillForm === 'DRIVER') { show = true; }
            else if (filter === 'new' && !installed) { show = true; }
            else if (filter === 'installed' && installed) { show = true; }
            else if (filter && filter.startsWith('bc_')) {
                var bc = filter.replace('bc_', '');
                if (businessCategory === bc) { show = true; }
            }
            item.style.display = show ? '' : 'none';
            if (show) { visibleCount++; }
        });
        document.getElementById('resultsCount').textContent = visibleCount;
    },

    renderResults: function() {
        console.log('[renderResults] Starting render, discoveredCapabilities length:', discoveredCapabilities.length);
        var container = document.getElementById('resultsBody');
        if (!container) {
            console.error('[renderResults] Container resultsBody not found');
            return;
        }
        var html = '';
        var counts = { scene: 0, provider: 0, driver: 0, new: 0, installed: 0 };
        var businessCounts = {};
        var scoreCounts = { high: 0, medium: 0, low: 0 };
        discoveredCapabilities.forEach(function(cap) {
            console.log('[renderResults] Processing cap:', cap.id, 'skillForm:', cap.skillForm);
            if (cap.skillForm === 'SCENE') { counts.scene++; }
            else if (cap.skillForm === 'PROVIDER') { counts.provider++; }
            else if (cap.skillForm === 'DRIVER') { counts.driver++; }
            if (cap.installed) { counts.installed++; } else { counts.new++; }
            if (cap.businessCategory) {
                businessCounts[cap.businessCategory] = (businessCounts[cap.businessCategory] || 0) + 1;
            }
            var score = cap.businessSemanticsScore || 5;
            if (score >= 8) { scoreCounts.high++; }
            else if (score >= 3) { scoreCounts.medium++; }
            else { scoreCounts.low++; }
            var categoryInfo = CapabilityDiscovery.getCategoryInfo(cap.skillForm, cap.sceneType);
            var bcInfo = CapabilityDiscovery.getBusinessCategoryInfo(cap.businessCategory);
            var scoreLevel = score >= 8 ? 'high' : (score >= 3 ? 'medium' : 'low');
            var scoreColor = score >= 8 ? '#10b981' : (score >= 3 ? '#f59e0b' : '#6b7280');
            html += '<div class="result-item" data-skill-form="' + cap.skillForm + '" data-scene-type="' + (cap.sceneType || '') + '" data-business-category="' + (cap.businessCategory || '') + '" data-installed="' + cap.installed + '" data-skill-id="' + cap.id + '" data-score="' + score + '" onclick="toggleCapabilitySelection(\'' + cap.id + '\', event)">' +
                '<div class="result-header">' +
                '<div class="result-icon"><i class="' + categoryInfo.icon + '"></i></div>' +
                '<div class="result-info">' +
                '<div class="result-name">' + cap.name + '</div>' +
                '<div class="result-desc">' + (cap.description || '') + '</div></div>' +
                '<div class="result-badges">' +
                '<span class="badge badge-' + categoryInfo.code.toLowerCase() + '">' + categoryInfo.name + '</span>' +
                (cap.businessCategory ? '<span class="badge badge-bc" style="background: ' + bcInfo.color + '20; color: ' + bcInfo.color + ';">' + bcInfo.name + '</span>' : '') +
                '<span class="badge badge-score" style="background: ' + scoreColor + '20; color: ' + scoreColor + ';" title="业务语义评分: ' + score + '/10"><i class="ri-star-line"></i> ' + score + '</span>' +
                (cap.installed ? '<span class="badge badge-installed">已安装</span>' : '<span class="badge badge-new">新能力</span>') +
                '</div></div>' +
                '<div class="result-actions">' +
                '<button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="event.stopPropagation(); viewCapability(\'' + cap.id + '\')" title="查看详情"><i class="ri-eye-line"></i></button>' +
                (cap.installed ? 
                    '<button class="nx-btn nx-btn--secondary nx-btn--sm" onclick="event.stopPropagation(); openCapability(\'' + cap.id + '\')" title="打开"><i class="ri-external-link-line"></i> 打开</button>' :
                    '<button class="nx-btn nx-btn--primary nx-btn--sm" onclick="event.stopPropagation(); installCapability(\'' + cap.id + '\')" title="安装"><i class="ri-download-line"></i> 安装</button>') +
                '</div></div>';
        });
        console.log('[renderResults] Generated HTML length:', html.length);
        container.innerHTML = html;
        document.getElementById('resultsCount').textContent = discoveredCapabilities.length;
        CapabilityDiscovery.updateFilterCounts(counts, businessCounts);
        CapabilityDiscovery.updateScoreFilterCounts(scoreCounts);
        CapabilityDiscovery.renderCharts(businessCounts, counts, discoveredCapabilities.length);
        console.log('[renderResults] Render completed');
    },

    updateScoreFilterCounts: function(scoreCounts) {
        var el;
        el = document.getElementById('scoreCountAll'); if (el) el.textContent = discoveredCapabilities.length;
        el = document.getElementById('scoreCountHigh'); if (el) el.textContent = scoreCounts.high;
        el = document.getElementById('scoreCountMedium'); if (el) el.textContent = scoreCounts.medium;
        el = document.getElementById('scoreCountLow'); if (el) el.textContent = scoreCounts.low;
    },

    updateFilterCounts: function(counts, businessCounts) {
        var el;
        el = document.getElementById('filterCountAll'); if (el) el.textContent = discoveredCapabilities.length;
        el = document.getElementById('filterCountScene'); if (el) el.textContent = counts.scene;
        el = document.getElementById('filterCountProvider'); if (el) el.textContent = counts.provider;
        el = document.getElementById('filterCountDriver'); if (el) el.textContent = counts.driver;
        el = document.getElementById('filterCountNew'); if (el) el.textContent = counts.new;
        el = document.getElementById('filterCountInstalled'); if (el) el.textContent = counts.installed;
    },

    renderCharts: function(businessCounts, skillFormCounts, total) {
        var chartSection = document.getElementById('chartSection');
        var businessChart = document.getElementById('businessChart');
        var skillFormChart = document.getElementById('skillFormChart');
        if (!chartSection || !businessChart || !skillFormChart) return;
        if (total === 0) { chartSection.style.display = 'none'; return; }
        chartSection.style.display = 'block';
        var bcHtml = '';
        for (var bc in businessCounts) {
            var count = businessCounts[bc];
            var percent = Math.round((count * 100.0) / total);
            var bcInfo = CategoryService.getInfo(bc);
            var color = bcInfo.color || '#8c8c8c';
            var name = bcInfo.name || bc;
            bcHtml += '<div class="chart-pie-item" onclick="selectBusinessCategory(\'' + bc + '\')" style="cursor: pointer;">' +
                '<div class="chart-pie-dot" style="background: ' + color + '"></div>' +
                '<span class="chart-pie-label">' + name + '</span>' +
                '<span class="chart-pie-value">' + count + '</span>' +
                '<span class="chart-pie-percent">(' + percent + '%)</span></div>';
        }
        businessChart.innerHTML = bcHtml;
        var sfColors = { 'SCENE': '#722ed1', 'PROVIDER': '#1890ff', 'DRIVER': '#52c41a' };
        var sfNames = { 'SCENE': '场景应用', 'PROVIDER': '能力服务', 'DRIVER': '驱动适配' };
        var sfHtml = '';
        for (var sf in sfNames) {
            var count = sf === 'SCENE' ? skillFormCounts.scene : (sf === 'PROVIDER' ? skillFormCounts.provider : skillFormCounts.driver);
            if (count > 0) {
                var percent = Math.round((count * 100.0) / total);
                var color = sfColors[sf] || '#8c8c8c';
                var barWidth = Math.max(10, percent);
                sfHtml += '<div class="chart-bar">' +
                    '<span class="chart-bar-label">' + sfNames[sf] + '</span>' +
                    '<div class="chart-bar-bar" style="width: ' + barWidth + 'px; background: ' + color + '"></div>' +
                    '<span class="chart-bar-value">' + count + '</span></div>';
            }
        }
        skillFormChart.innerHTML = sfHtml;
    },

    getCategoryInfo: function(skillForm, sceneType) {
        if (skillForm === 'SCENE') {
            return { code: 'SCENE', name: '场景应用', icon: 'ri-layout-grid-line' };
        } else if (skillForm === 'PROVIDER') { return { code: 'PROVIDER', name: '能力服务', icon: 'ri-cpu-line' }; }
        else if (skillForm === 'DRIVER') { return { code: 'DRIVER', name: '驱动适配', icon: 'ri-steering-line' }; }
        return { code: 'PROVIDER', name: '能力服务', icon: 'ri-cpu-line' };
    },

    getBusinessCategoryInfo: function(bc) {
        return CategoryService.getInfo(bc);
    },
    
    showUserSelector: function(type, title, callback) {
        var modal = document.getElementById('userSelectorModal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'userSelectorModal';
            modal.className = 'modal-overlay';
            modal.innerHTML = 
                '<div class="modal-content user-selector-modal">' +
                '<div class="modal-header">' +
                '<h3>' + title + '</h3>' +
                '<button class="modal-close" onclick="closeUserSelector()"><i class="ri-close-line"></i></button>' +
                '</div>' +
                '<div class="modal-body">' +
                '<div class="user-search">' +
                '<input type="text" id="userSearchInput" class="form-input" placeholder="搜索用户..." oninput="searchUsers(this.value)">' +
                '</div>' +
                '<div class="user-list" id="userSelectorList">' +
                '<div class="user-loading">加载中...</div>' +
                '</div>' +
                '</div>' +
                '</div>';
            document.body.appendChild(modal);
        }
        
        modal.style.display = 'flex';
        window._userSelectorCallback = callback;
        
        CapabilityDiscovery.loadUsers();
    },
    
    loadUsers: function(keyword) {
        var list = document.getElementById('userSelectorList');
        if (!list) return;
        
        list.innerHTML = '<div class="user-loading">加载中...</div>';
        
        ApiClient.get('/api/v1/org/users', { keyword: keyword || '' })
            .then(function(result) {
                var users = result.data || result || [];
                if (users.length === 0) {
                    list.innerHTML = '<div class="user-empty">暂无用户</div>';
                    return;
                }
                
                var html = '';
                users.forEach(function(user) {
                    var name = user.name || user.username || user;
                    html += '<div class="user-item" onclick="selectUser(this)" data-user=\'' + JSON.stringify(user) + '\'>' +
                        '<div class="user-avatar"><i class="ri-user-line"></i></div>' +
                        '<div class="user-info">' +
                        '<div class="user-name">' + name + '</div>' +
                        (user.email ? '<div class="user-email">' + user.email + '</div>' : '') +
                        '</div>' +
                        '</div>';
                });
                list.innerHTML = html;
            })
            .catch(function(error) {
                console.error('[loadUsers] Error:', error);
                var html = '<div class="user-empty">加载失败，请重试</div>';
                list.innerHTML = html;
            });
    },

    showDetailModal: function(cap) {
        var modal = document.getElementById('detailModal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'detailModal';
            modal.className = 'modal-overlay';
            modal.innerHTML = '<div class="modal-content" style="max-width: 600px;">' +
                '<div class="modal-header">' +
                '<h3 id="detailModalTitle"></h3>' +
                '<button class="modal-close" onclick="closeDetailModal()"><i class="ri-close-line"></i></button>' +
                '</div>' +
                '<div class="modal-body" id="detailModalBody"></div>' +
                '<div class="modal-footer">' +
                '<button class="nx-btn nx-btn--secondary" onclick="closeDetailModal()">关闭</button>' +
                '<button class="nx-btn nx-btn--primary" id="detailModalAction"></button>' +
                '</div></div>';
            document.body.appendChild(modal);
        }
        var categoryInfo = CapabilityDiscovery.getCategoryInfo(cap.skillForm, cap.sceneType);
        var bcInfo = CapabilityDiscovery.getBusinessCategoryInfo(cap.businessCategory);
        document.getElementById('detailModalTitle').innerHTML = '<i class="' + categoryInfo.icon + '"></i> ' + cap.name;
        document.getElementById('detailModalBody').innerHTML = 
            '<div class="detail-section">' +
            '<div class="detail-row"><span class="detail-label">类型:</span><span class="detail-value">' + categoryInfo.name + '</span></div>' +
            '<div class="detail-row"><span class="detail-label">业务领域:</span><span class="detail-value">' + (bcInfo.name || '-') + '</span></div>' +
            '<div class="detail-row"><span class="detail-label">版本:</span><span class="detail-value">' + (cap.version || '1.0.0') + '</span></div>' +
            '<div class="detail-row"><span class="detail-label">状态:</span><span class="detail-value">' + (cap.installed ? '已安装' : '未安装') + '</span></div>' +
            '</div>' +
            '<div class="detail-section">' +
            '<div class="detail-label">描述:</div>' +
            '<div class="detail-value">' + (cap.description || '暂无描述') + '</div>' +
            '</div>' +
            (cap.tags && cap.tags.length > 0 ? 
                '<div class="detail-section">' +
                '<div class="detail-label">标签:</div>' +
                '<div class="detail-value">' + cap.tags.map(function(t) { return '<span class="badge">' + t + '</span>'; }).join(' ') + '</div>' +
                '</div>' : '');
        var actionBtn = document.getElementById('detailModalAction');
        if (cap.installed) {
            actionBtn.innerHTML = '<i class="ri-external-link-line"></i> 打开';
            actionBtn.onclick = function() { window.location.href = 'capability-detail.html?id=' + cap.id; };
        } else {
            actionBtn.innerHTML = '<i class="ri-download-line"></i> 安装';
            actionBtn.onclick = function() { installCapability(cap.id); closeDetailModal(); };
        }
        modal.classList.add('show');
    },

    openInstallWizard: function(cap) {
        var modal = document.getElementById('installModal');
        if (!modal) return;
        currentInstallCap = cap;
        currentInstallStep = 1;
        installSteps = CapabilityDiscovery.getInstallSteps(cap);
        CapabilityDiscovery.updateInstallWizard();
        modal.classList.add('show');
    },

    getInstallSteps: function(cap) {
        var steps = [1];
        if (cap.skillForm === 'SCENE') {
            steps.push(2, 3);
        }
        if (cap.skillForm === 'SCENE' || cap.skillForm === 'DRIVER') {
            steps.push(4);
        }
        steps.push(5);
        if (cap.skillForm === 'SCENE' && CapabilityDiscovery.needsLLMConfig(cap)) {
            steps.push(6);
        }
        steps.push(7, 8);
        return steps;
    },

    needsLLMConfig: function(cap) {
        if (cap.capabilityCategory === 'llm' || cap.capabilityCategory === 'know') {
            return true;
        }
        if (cap.businessCategory === 'AI_ASSISTANT') {
            return true;
        }
        if (cap.dependencies && cap.dependencies.length > 0) {
            var llmDeps = cap.dependencies.filter(function(d) {
                return d.indexOf('llm') >= 0 || d.indexOf('knowledge') >= 0 || d.indexOf('rag') >= 0;
            });
            return llmDeps.length > 0;
        }
        return false;
    },

    updateInstallWizard: function() {
        var cap = currentInstallCap;
        var step = currentInstallStep;
        var steps = installSteps;
        var stepIndex = steps.indexOf(step);
        
        document.getElementById('detailName').textContent = cap.name;
        document.getElementById('detailId').textContent = cap.id;
        document.getElementById('detailDescription').textContent = cap.description || '暂无描述';
        document.getElementById('detailVersion').textContent = cap.version || '1.0.0';
        var categoryInfo = CapabilityDiscovery.getCategoryInfo(cap.skillForm, cap.sceneType);
        document.getElementById('detailTypeBadge').textContent = categoryInfo.name;
        
        var allSteppers = document.querySelectorAll('.stepper-item');
        allSteppers.forEach(function(s) { s.style.display = 'none'; });
        steps.forEach(function(stepNum, i) {
            var stepper = document.querySelector('.stepper-item[data-step="' + stepNum + '"]');
            if (stepper) {
                stepper.style.display = '';
                stepper.classList.toggle('active', i <= stepIndex);
                stepper.classList.toggle('completed', i < stepIndex);
            }
        });
        
        var allSteps = document.querySelectorAll('.wizard-step');
        allSteps.forEach(function(s) { s.classList.remove('active'); });
        var currentStepEl = document.getElementById('wizardStep' + step);
        if (currentStepEl) { currentStepEl.classList.add('active'); }
        
        var prevIndex = stepIndex - 1;
        var nextIndex = stepIndex + 1;
        document.getElementById('installPrev').style.display = prevIndex >= 0 ? '' : 'none';
        document.getElementById('installNext').style.display = nextIndex < steps.length ? '' : 'none';
        document.getElementById('installDone').style.display = step === 8 ? '' : 'none';
        
        CapabilityDiscovery.renderStepContent(step);
    },

    renderStepContent: function(step) {
        var cap = currentInstallCap;
        if (!cap) return;
        
        switch (step) {
            case 1:
                CapabilityDiscovery.renderPreviewStep(cap);
                break;
            case 2:
                CapabilityDiscovery.renderRolesStep(cap);
                break;
            case 3:
                CapabilityDiscovery.renderParticipantsStep(cap);
                break;
            case 4:
                CapabilityDiscovery.renderDriverConditionsStep(cap);
                break;
            case 5:
                CapabilityDiscovery.renderDependenciesStep(cap);
                break;
            case 6:
                CapabilityDiscovery.renderLLMConfigStep(cap);
                break;
            case 7:
                CapabilityDiscovery.renderInstallProgressStep(cap);
                break;
            case 8:
                CapabilityDiscovery.renderCompleteStep(cap);
                break;
        }
    },

    renderPreviewStep: function(cap) {
        var featureList = document.getElementById('featureList');
        if (featureList) {
            var features = cap.tags || [];
            if (features.length === 0) {
                features = ['核心功能', '场景支持', '数据管理'];
            }
            var html = '';
            features.forEach(function(f) {
                html += '<div class="feature-item"><i class="ri-check-line"></i> ' + f + '</div>';
            });
            featureList.innerHTML = html;
        }
        
        var rolePreviewList = document.getElementById('rolePreviewList');
        if (rolePreviewList) {
            var roles = cap.roles || [];
            if (roles.length === 0 && cap.skillForm === 'SCENE') {
                roles = [{ name: '主导者', desc: '场景启动者' }, { name: '参与者', desc: '场景参与者' }];
            }
            var html = '';
            roles.forEach(function(r) {
                var name = typeof r === 'string' ? r : (r.name || r.roleName || '角色');
                html += '<div class="role-preview-item"><i class="ri-user-line"></i><span>' + name + '</span></div>';
            });
            if (html === '') {
                html = '<div class="role-preview-item"><i class="ri-user-line"></i><span>默认角色</span></div>';
            }
            rolePreviewList.innerHTML = html;
        }
        
        var dependencyPreviewList = document.getElementById('dependencyPreviewList');
        if (dependencyPreviewList) {
            var deps = cap.dependencies || [];
            var html = '';
            deps.forEach(function(d) {
                html += '<div class="dependency-preview-item installed"><i class="ri-checkbox-circle-line"></i><span>' + d + '</span></div>';
            });
            if (html === '') {
                html = '<div class="dependency-preview-item"><i class="ri-checkbox-blank-circle-line"></i><span>无依赖项</span></div>';
            }
            dependencyPreviewList.innerHTML = html;
        }
    },

    renderRolesStep: function(cap) {
        var roleSelectionList = document.getElementById('roleSelectionList');
        if (!roleSelectionList) return;
        
        var roles = cap.roles || [];
        if (roles.length === 0 && cap.skillForm === 'SCENE') {
            roles = [
                { id: 'leader', name: '主导者', desc: '拥有场景启动权限，可配置参与者' },
                { id: 'participant', name: '参与者', desc: '参与场景协作，可执行场景操作' }
            ];
        }
        
        var html = '';
        roles.forEach(function(r, i) {
            var id = r.id || ('role_' + i);
            var name = typeof r === 'string' ? r : (r.name || r.roleName || '角色');
            var desc = r.desc || r.description || '';
            var checked = i === 0 ? 'checked' : '';
            html += '<div class="role-selection-item">' +
                '<label class="role-radio">' +
                '<input type="radio" name="selectedRole" value="' + id + '" ' + checked + '>' +
                '<span class="radio-mark"></span>' +
                '</label>' +
                '<div class="role-info">' +
                '<div class="role-name">' + name + '</div>' +
                (desc ? '<div class="role-desc">' + desc + '</div>' : '') +
                '</div></div>';
        });
        if (html === '') {
            html = '<div class="role-selection-item"><div class="role-info"><div class="role-name">默认角色</div></div></div>';
        }
        roleSelectionList.innerHTML = html;
    },

    renderParticipantsStep: function(cap) {
        var leaderInput = document.getElementById('leaderInput');
        if (leaderInput && !leaderInput.value) {
            leaderInput.value = '当前用户';
        }
        var collaboratorList = document.getElementById('collaboratorList');
        if (collaboratorList && collaboratorList.children.length === 0) {
            collaboratorList.innerHTML = '<div class="participant-empty">暂无协作者，点击下方按钮添加</div>';
        }
        var pushType = document.getElementById('pushType');
        if (pushType && !pushType.value) {
            pushType.value = 'SHARE';
        }
        if (pushType) {
            pushType.onchange = function() {
                var hint = document.getElementById('pushHint');
                if (!hint) return;
                switch (pushType.value) {
                    case 'SHARE':
                        hint.textContent = '分享给参与者，对方可选择是否接受';
                        break;
                    case 'INVITE':
                        hint.textContent = '邀请参与者，对方需确认接受';
                        break;
                    case 'DELEGATE':
                        hint.textContent = '委派给参与者，对方将被强制使用';
                        break;
                }
            };
        }
    },

    renderDriverConditionsStep: function(cap) {
        var driverConditionList = document.getElementById('driverConditionList');
        if (!driverConditionList) return;
        
        var conditions = [
            { id: 'manual', name: '手动触发', desc: '用户手动启动场景', icon: 'ri-hand-coin-line' },
            { id: 'schedule', name: '定时触发', desc: '按计划时间自动执行', icon: 'ri-timer-line' },
            { id: 'event', name: '事件触发', desc: '当特定事件发生时执行', icon: 'ri-flashlight-line' }
        ];
        
        var html = '';
        conditions.forEach(function(c, i) {
            var checked = i === 0 ? 'checked' : '';
            html += '<div class="driver-condition-item">' +
                '<label class="condition-checkbox">' +
                '<input type="checkbox" name="driverCondition" value="' + c.id + '" ' + checked + '>' +
                '<span class="checkbox-mark"></span>' +
                '</label>' +
                '<div class="condition-icon"><i class="' + c.icon + '"></i></div>' +
                '<div class="condition-info">' +
                '<div class="condition-name">' + c.name + '</div>' +
                '<div class="condition-desc">' + c.desc + '</div>' +
                '</div></div>';
        });
        driverConditionList.innerHTML = html;
    },

    renderDependenciesStep: function(cap) {
        var dependencyList = document.getElementById('dependencyList');
        if (!dependencyList) return;
        
        var deps = cap.dependencies || [];
        var html = '';
        if (deps.length === 0) {
            html = '<div class="dependency-empty">' +
                '<i class="ri-checkbox-circle-line"></i>' +
                '<span>无依赖项，可直接安装</span></div>';
        } else {
            deps.forEach(function(d) {
                html += '<div class="dependency-item installed">' +
                    '<i class="ri-checkbox-circle-line"></i>' +
                    '<span class="dependency-name">' + d + '</span>' +
                    '<span class="dependency-status">已满足</span></div>';
            });
        }
        dependencyList.innerHTML = html;
    },

    renderLLMConfigStep: function(cap) {
        var llmProviderList = document.getElementById('llmProviderList');
        if (llmProviderList) {
            var items = llmProviderList.querySelectorAll('.llm-provider-item');
            items.forEach(function(item) {
                item.classList.remove('selected');
            });
            var firstItem = llmProviderList.querySelector('.llm-provider-item');
            if (firstItem) {
                firstItem.classList.add('selected');
            }
        }
        var systemPrompt = document.getElementById('systemPrompt');
        if (systemPrompt && cap.description) {
            systemPrompt.value = '你是' + cap.name + '场景的AI助手。' + cap.description;
        }
    },

    renderInstallProgressStep: function(cap) {
        var installProgress = document.getElementById('installProgress');
        var installStepsEl = document.getElementById('installSteps');
        if (installProgress) {
            installProgress.style.width = '30%';
        }
        if (installStepsEl) {
            var steps = [
                { name: '检查依赖', status: 'done' },
                { name: '下载资源', status: 'running' },
                { name: '注册能力', status: 'pending' },
                { name: '配置权限', status: 'pending' },
                { name: '完成安装', status: 'pending' }
            ];
            var html = '';
            steps.forEach(function(s) {
                var iconClass = s.status === 'done' ? 'ri-checkbox-circle-line' : 
                               (s.status === 'running' ? 'ri-loader-4-line' : 'ri-checkbox-blank-circle-line');
                html += '<div class="install-step-item ' + s.status + '">' +
                    '<i class="' + iconClass + '"></i>' +
                    '<span>' + s.name + '</span></div>';
            });
            installStepsEl.innerHTML = html;
        }
    },

    renderCompleteStep: function(cap) {
        var completeName = document.getElementById('completeName');
        var completeRole = document.getElementById('completeRole');
        var completeParticipants = document.getElementById('completeParticipants');
        
        if (completeName) completeName.textContent = cap.name;
        if (completeRole) {
            var selectedRole = document.querySelector('input[name="selectedRole"]:checked');
            completeRole.textContent = selectedRole ? selectedRole.value : '主导者';
        }
        if (completeParticipants) completeParticipants.textContent = '当前用户';
        
        var completeMenuPreview = document.getElementById('completeMenuPreview');
        if (completeMenuPreview) {
            completeMenuPreview.innerHTML = '<div class="menu-preview-item">' +
                '<i class="ri-puzzle-line"></i>' +
                '<span>' + cap.name + '</span></div>';
        }
        
        var completeNotifyStatus = document.getElementById('completeNotifyStatus');
        if (completeNotifyStatus) {
            completeNotifyStatus.innerHTML = '<div class="notify-item success">' +
                '<i class="ri-checkbox-circle-line"></i>' +
                '<span>场景已成功安装并激活</span></div>';
        }
        
        CapabilityDiscovery.saveInstallConfig(cap);
        CapabilityDiscovery.addMenuForInstalledCapability(cap);
    },

    saveInstallConfig: function(cap) {
        var sceneGroupId = cap.id || ('sg-' + Date.now());
        var installConfig = CapabilityDiscovery.collectInstallConfig(cap);
        
        if (installConfig.llmConfig) {
            ApiClient.put('/api/v1/scene-groups/' + sceneGroupId + '/llm/config', installConfig.llmConfig)
                .then(function(result) {
                    if (result.status === 'success') {
                        console.log('[saveInstallConfig] LLM config saved successfully');
                        CapabilityDiscovery.addLog('success', 'LLM配置保存成功');
                    }
                })
                .catch(function(error) {
                    console.error('[saveInstallConfig] Failed to save LLM config:', error);
                });
        }
        
        if (installConfig.participants && installConfig.participants.length > 0) {
            installConfig.participants.forEach(function(participant) {
                ApiClient.post('/api/v1/scene-groups/' + sceneGroupId + '/participants', participant)
                    .then(function(result) {
                        if (result.status === 'success') {
                            console.log('[saveInstallConfig] Participant added:', participant.userId);
                        }
                    })
                    .catch(function(error) {
                        console.error('[saveInstallConfig] Failed to add participant:', error);
                    });
            });
        }
        
        if (installConfig.llmConfig && installConfig.llmConfig.knowledge && installConfig.llmConfig.knowledge.bases) {
            installConfig.llmConfig.knowledge.bases.forEach(function(kb) {
                ApiClient.post('/api/v1/scene-groups/' + sceneGroupId + '/knowledge', {
                    kbId: kb.id || kb,
                    layer: kb.layer || 'GENERAL'
                })
                    .then(function(result) {
                        if (result.status === 'success') {
                            console.log('[saveInstallConfig] Knowledge base bound:', kb);
                        }
                    })
                    .catch(function(error) {
                        console.error('[saveInstallConfig] Failed to bind knowledge:', error);
                    });
            });
        }
        
        console.log('[saveInstallConfig] All configurations saved for scene group:', sceneGroupId);
    },

    addMenuForInstalledCapability: function(cap) {
        var currentRole = localStorage.getItem('currentRole') || 'personal';
        var categoryInfo = CapabilityDiscovery.getCategoryInfo(cap.skillForm, cap.sceneType);
        var menuIcon = categoryInfo.icon;
        
        var menuItem = {
            id: 'menu-' + cap.id,
            name: cap.name,
            url: '/console/pages/capability-detail.html?id=' + cap.id,
            icon: menuIcon,
            sort: 100
        };
        
        ApiClient.post('/api/v1/role-management/roles/' + currentRole + '/menus', menuItem)
            .then(function(result) {
                if (result.status === 'success') {
                    console.log('[addMenuForInstalledCapability] Menu added successfully:', cap.name);
                    CapabilityDiscovery.addLog('success', '菜单添加成功: ' + cap.name);
                } else {
                    console.error('[addMenuForInstalledCapability] Failed to add menu:', result.message);
                    CapabilityDiscovery.addLog('error', '菜单添加失败: ' + (result.message || '未知错误'));
                }
            })
            .catch(function(error) {
                console.error('[addMenuForInstalledCapability] Error:', error);
                CapabilityDiscovery.addLog('error', '菜单添加失败: ' + error.message);
            });
    },

    nextInstallStep: function() {
        var stepIndex = installSteps.indexOf(currentInstallStep);
        if (stepIndex < installSteps.length - 1) {
            currentInstallStep = installSteps[stepIndex + 1];
            if (currentInstallStep === 7) {
                CapabilityDiscovery.executeInstall();
            }
            CapabilityDiscovery.updateInstallWizard();
        }
    },

    prevInstallStep: function() {
        var stepIndex = installSteps.indexOf(currentInstallStep);
        if (stepIndex > 0) {
            currentInstallStep = installSteps[stepIndex - 1];
            CapabilityDiscovery.updateInstallWizard();
        }
    },

    executeInstall: function() {
        var cap = currentInstallCap;
        if (!cap) return;
        CapabilityDiscovery.addLog('info', '开始安装: ' + cap.name);
        
        var installProgress = document.getElementById('installProgress');
        var installStepsEl = document.getElementById('installSteps');
        
        var progressSteps = [
            { name: '检查依赖', progress: 20 },
            { name: '下载资源', progress: 40 },
            { name: '注册能力', progress: 60 },
            { name: '配置权限', progress: 80 },
            { name: '完成安装', progress: 100 }
        ];
        
        var stepIndex = 0;
        function updateProgressUI(progress, stepName) {
            if (installProgress) {
                installProgress.style.width = progress + '%';
                installProgress.style.background = '';
            }
            if (installStepsEl) {
                var items = installStepsEl.querySelectorAll('.install-step-item');
                items.forEach(function(item, i) {
                    item.classList.remove('done', 'running', 'pending');
                    if (i < stepIndex) {
                        item.classList.add('done');
                    } else if (i === stepIndex) {
                        item.classList.add('running');
                    } else {
                        item.classList.add('pending');
                    }
                    var icon = item.querySelector('i');
                    if (icon) {
                        if (i < stepIndex) {
                            icon.className = 'ri-checkbox-circle-line';
                        } else if (i === stepIndex) {
                            icon.className = 'ri-loader-4-line';
                        } else {
                            icon.className = 'ri-checkbox-blank-circle-line';
                        }
                    }
                });
            }
        }
        
        var installRequest = CapabilityDiscovery.collectInstallConfig(cap);
        
        updateProgressUI(10, '初始化');
        
        ApiClient.post('/api/v1/discovery/install', installRequest)
            .then(function(result) {
                if (result.status === 'success') {
                    var data = result.data || result;
                    
                    var progress = data.progress || 100;
                    var currentStep = data.currentStep || '完成安装';
                    
                    stepIndex = progressSteps.findIndex(function(s) { return s.name === currentStep; });
                    if (stepIndex < 0) stepIndex = progressSteps.length - 1;
                    
                    updateProgressUI(progress, currentStep);
                    
                    setTimeout(function() {
                        CapabilityDiscovery.addLog('success', '安装成功: ' + cap.name);
                        cap.installed = true;
                        CapabilityDiscovery.renderResults();
                        CapabilityDiscovery.nextInstallStep();
                    }, 500);
                } else {
                    CapabilityDiscovery.addLog('error', '安装失败: ' + (result.message || '未知错误'));
                    if (installProgress) {
                        installProgress.style.width = '0%';
                        installProgress.style.background = '#f5222d';
                    }
                }
            })
            .catch(function(error) {
                CapabilityDiscovery.addLog('error', '安装失败: ' + error.message);
                if (installProgress) {
                    installProgress.style.width = '0%';
                    installProgress.style.background = '#f5222d';
                }
            });
    },

    collectInstallConfig: function(cap) {
        var request = {
            skillId: cap.id,
            name: cap.name,
            type: cap.skillForm,
            description: cap.description,
            source: cap.source || 'LOCAL'
        };
        
        var selectedRoleEl = document.querySelector('input[name="selectedRole"]:checked');
        if (selectedRoleEl) {
            request.selectedRole = selectedRoleEl.value;
        }
        
        var leaderInput = document.getElementById('leaderInput');
        var pushTypeEl = document.getElementById('pushType');
        var collaboratorList = document.getElementById('collaboratorList');
        
        if (leaderInput || pushTypeEl || collaboratorList) {
            request.participants = {
                leader: leaderInput ? leaderInput.value : 'current_user',
                pushType: pushTypeEl ? pushTypeEl.value : 'SHARE',
                collaborators: []
            };
            
            if (collaboratorList) {
                var tags = collaboratorList.querySelectorAll('.participant-tag span');
                tags.forEach(function(tag) {
                    request.participants.collaborators.push(tag.textContent);
                });
            }
        }
        
        var driverConditionEls = document.querySelectorAll('input[name="driverCondition"]:checked');
        if (driverConditionEls.length > 0) {
            request.driverConditions = [];
            driverConditionEls.forEach(function(el) {
                request.driverConditions.push(el.value);
            });
        }
        
        var llmProvider = document.querySelector('.provider-card.selected');
        var modelSelect = document.getElementById('llmModelSelect');
        var systemPrompt = document.getElementById('systemPrompt');
        var enableFunctionCall = document.getElementById('enableFunctionCall');
        var enableKnowledge = document.getElementById('enableKnowledge');
        
        if (llmProvider || modelSelect) {
            request.llmConfig = {
                provider: llmProvider ? llmProvider.dataset.provider : 'deepseek',
                model: modelSelect ? modelSelect.value : 'deepseek-chat',
                systemPrompt: systemPrompt ? systemPrompt.value : '',
                enableFunctionCall: enableFunctionCall ? enableFunctionCall.checked : true,
                functionTools: [],
                knowledge: null,
                parameters: {}
            };
            
            var temperatureEl = document.getElementById('temperature');
            var maxTokensEl = document.getElementById('maxTokens');
            if (temperatureEl) {
                request.llmConfig.parameters.temperature = parseInt(temperatureEl.value) / 100;
            }
            if (maxTokensEl) {
                request.llmConfig.parameters.maxTokens = parseInt(maxTokensEl.value);
            }
            
            var fcToolEls = document.querySelectorAll('input[name="fcTool"]:checked');
            fcToolEls.forEach(function(el) {
                request.llmConfig.functionTools.push(el.value);
            });
            
            if (enableKnowledge && enableKnowledge.checked) {
                var ragTopK = document.getElementById('ragTopK');
                var ragThreshold = document.getElementById('ragThreshold');
                var kbSelects = document.querySelectorAll('select[name="kbSelect"]');
                
                request.llmConfig.knowledge = {
                    enabled: true,
                    topK: ragTopK ? parseInt(ragTopK.value) : 5,
                    scoreThreshold: ragThreshold ? parseFloat(ragThreshold.value) : 0.7,
                    bases: []
                };
                
                kbSelects.forEach(function(select) {
                    if (select.value) {
                        request.llmConfig.knowledge.bases.push(select.value);
                    }
                });
            }
        }
        
        console.log('[collectInstallConfig] Install request:', request);
        return request;
    },

    addLog: function(level, message) {
        var log = { level: level, message: message, time: new Date() };
        logs.unshift(log);
        if (logs.length > 100) { logs.pop(); }
        var container = document.getElementById('logsBody');
        if (!container) {
            container = document.getElementById('logsBody');
            if (!container) return;
        }
        var item = document.createElement('div');
        item.className = 'log-item log-' + level;
        item.innerHTML = '<span class="log-time">' + log.time.toLocaleTimeString() + '</span><span class="log-message">' + message + '</span>';
        container.insertBefore(item, container.firstChild);
        if (container.children.length > 50) {
            container.removeChild(container.lastChild);
        }
        
        if (level === 'error') {
            CapabilityDiscovery.showToast(message, 'error');
        } else if (level === 'success') {
            CapabilityDiscovery.showToast(message, 'success');
        }
    },
    
    showToast: function(message, type) {
        type = type || 'info';
        var existing = document.querySelector('.nx-notification');
        if (existing) existing.remove();
        
        var notification = document.createElement('div');
        notification.className = 'nx-notification nx-notification--' + type;
        
        var icon = '';
        if (type === 'error') {
            icon = '<i class="ri-error-warning-line" style="margin-right: 8px;"></i>';
        } else if (type === 'success') {
            icon = '<i class="ri-checkbox-circle-line" style="margin-right: 8px;"></i>';
        } else if (type === 'warning') {
            icon = '<i class="ri-alert-line" style="margin-right: 8px;"></i>';
        } else {
            icon = '<i class="ri-information-line" style="margin-right: 8px;"></i>';
        }
        
        notification.innerHTML = icon + message;
        document.body.appendChild(notification);
        
        setTimeout(function() {
            notification.style.opacity = '0';
            notification.style.transform = 'translateX(100%)';
            setTimeout(function() { notification.remove(); }, 300);
        }, 5000);
    }
};

global.CapabilityDiscovery = CapabilityDiscovery;
global.selectMethod = function(methodId) { CapabilityDiscovery.selectMethod(methodId); };
global.startDiscovery = function() { CapabilityDiscovery.startScan(); };
global.forceRefresh = function() { CapabilityDiscovery.startScan(); };
global.hideConfig = function() { CapabilityDiscovery.hideConfig(); };
global.applyConfig = function() { CapabilityDiscovery.startScan(); };
global.toggleLogs = function() { 
    var panel = document.getElementById('logsPanel');
    if (panel) { panel.classList.toggle('collapsed'); }
};
global.selectBusinessCategory = function(bc) { CapabilityDiscovery.selectBusinessCategory(bc); };
global.viewCapability = function(skillId) {
    var cap = discoveredCapabilities.find(function(c) { return c.id === skillId; });
    if (cap) { CapabilityDiscovery.showDetailModal(cap); }
};
global.closeDetailModal = function() {
    var modal = document.getElementById('detailModal');
    if (modal) { modal.classList.remove('show'); }
};
global.openCapability = function(skillId) {
    window.location.href = 'capability-detail.html?id=' + skillId;
};
global.installCapability = function(skillId) {
    var cap = discoveredCapabilities.find(function(c) { return c.id === skillId; });
    if (cap) { CapabilityDiscovery.openInstallWizard(cap); }
};
global.closeInstall = function() {
    var modal = document.getElementById('installModal');
    if (modal) { modal.classList.remove('show'); }
};
global.cancelInstall = function() { global.closeInstall(); };
global.nextStep = function() { CapabilityDiscovery.nextInstallStep(); };
global.prevStep = function() { CapabilityDiscovery.prevInstallStep(); };
global.goToCapability = function() { window.location.href = 'my-capabilities.html'; };
global.switchLLMTab = function(tabName) {
    var tabs = document.querySelectorAll('.llm-tab');
    var panes = document.querySelectorAll('.llm-tab-pane');
    
    tabs.forEach(function(tab) {
        tab.classList.remove('active');
        if (tab.dataset.tab === tabName) {
            tab.classList.add('active');
        }
    });
    
    panes.forEach(function(pane) {
        pane.classList.remove('active');
        if (pane.id === 'llmTab' + tabName.charAt(0).toUpperCase() + tabName.slice(1)) {
            pane.classList.add('active');
        }
    });
};

global.selectLLMProvider = function(provider) {
    var cards = document.querySelectorAll('.provider-card');
    cards.forEach(function(card) {
        card.classList.remove('selected');
        if (card.dataset.provider === provider) {
            card.classList.add('selected');
        }
    });
    
    var modelSelect = document.getElementById('llmModelSelect');
    if (modelSelect) {
        var models = {
            'deepseek': [
                { value: 'deepseek-chat', text: 'DeepSeek Chat (推荐)', context: '64K', features: '支持Function Calling' },
                { value: 'deepseek-coder', text: 'DeepSeek Coder', context: '16K', features: '代码优化' }
            ],
            'qianwen': [
                { value: 'qwen-max', text: '通义千问 Max', context: '32K', features: '支持Function Calling' },
                { value: 'qwen-plus', text: '通义千问 Plus', context: '8K', features: '性价比高' }
            ],
            'openai': [
                { value: 'gpt-4o', text: 'GPT-4o (推荐)', context: '128K', features: '最强能力' },
                { value: 'gpt-4-turbo', text: 'GPT-4 Turbo', context: '128K', features: '快速响应' },
                { value: 'gpt-3.5-turbo', text: 'GPT-3.5 Turbo', context: '16K', features: '经济实惠' }
            ],
            'ollama': [
                { value: 'llama3', text: 'Llama 3', context: '8K', features: '本地运行' },
                { value: 'qwen2', text: 'Qwen 2', context: '32K', features: '中文优化' }
            ]
        };
        
        var providerModels = models[provider] || models['deepseek'];
        modelSelect.innerHTML = '';
        providerModels.forEach(function(m) {
            var opt = document.createElement('option');
            opt.value = m.value;
            opt.textContent = m.text;
            modelSelect.appendChild(opt);
        });
        
        var modelMeta = document.getElementById('modelMeta');
        if (modelMeta && providerModels[0]) {
            modelMeta.innerHTML = '<span class="meta-item"><i class="ri-text"></i> 上下文: ' + providerModels[0].context + '</span>' +
                '<span class="meta-item"><i class="ri-flashlight-line"></i> ' + providerModels[0].features + '</span>';
        }
    }
    
    updateLLMConfigSummary();
};

global.onModelChange = function() {
    var modelSelect = document.getElementById('llmModelSelect');
    var selectedOption = modelSelect.options[modelSelect.selectedIndex];
    console.log('[onModelChange] Selected model:', selectedOption.value);
    updateLLMConfigSummary();
};

global.updateParamDisplay = function(param) {
    if (param === 'temperature') {
        var slider = document.getElementById('temperature');
        var display = document.getElementById('temperatureValue');
        if (slider && display) {
            display.textContent = (slider.value / 100).toFixed(1);
        }
    } else if (param === 'topP') {
        var slider = document.getElementById('topP');
        var display = document.getElementById('topPValue');
        if (slider && display) {
            display.textContent = (slider.value / 100).toFixed(1);
        }
    } else if (param === 'freqPenalty') {
        var slider = document.getElementById('freqPenalty');
        var display = document.getElementById('freqPenaltyValue');
        if (slider && display) {
            display.textContent = (slider.value / 10).toFixed(1);
        }
    } else if (param === 'maxTokens') {
        var input = document.getElementById('maxTokens');
        var display = document.getElementById('maxTokensValue');
        if (input && display) {
            display.textContent = input.value;
        }
    }
};

global.resetModelParams = function() {
    document.getElementById('temperature').value = 70;
    document.getElementById('temperatureValue').textContent = '0.7';
    document.getElementById('maxTokens').value = 4096;
    document.getElementById('maxTokensValue').textContent = '4096';
    document.getElementById('topP').value = 90;
    document.getElementById('topPValue').textContent = '0.9';
    document.getElementById('freqPenalty').value = 0;
    document.getElementById('freqPenaltyValue').textContent = '0';
};

global.toggleFunctionCallTools = function() {
    var enabled = document.getElementById('enableFunctionCall').checked;
    var toolsPanel = document.getElementById('functionCallTools');
    if (toolsPanel) {
        toolsPanel.style.opacity = enabled ? '1' : '0.5';
        toolsPanel.style.pointerEvents = enabled ? 'auto' : 'none';
    }
    updateLLMConfigSummary();
};

global.toggleKnowledgeConfig = function() {
    var enabled = document.getElementById('enableKnowledge').checked;
    var panel = document.getElementById('knowledgeConfigPanel');
    if (panel) {
        panel.style.display = enabled ? 'block' : 'none';
    }
};

global.addKnowledgeBase = function() {
    var container = document.getElementById('knowledgeBases');
    if (!container) return;
    
    var empty = container.querySelector('.knowledge-base-empty');
    if (empty) {
        container.innerHTML = '';
    }
    
    var item = document.createElement('div');
    item.className = 'knowledge-base-item';
    item.innerHTML = 
        '<div class="kb-select">' +
        '<select class="form-select" name="kbSelect">' +
        '<option value="">选择知识库...</option>' +
        '<option value="kb-product">产品文档库</option>' +
        '<option value="kb-faq">常见问题库</option>' +
        '<option value="kb-manual">操作手册库</option>' +
        '</select>' +
        '</div>' +
        '<button type="button" class="nx-btn nx-btn--ghost nx-btn--sm" onclick="this.parentElement.remove()">' +
        '<i class="ri-close-line"></i>' +
        '</button>';
    container.appendChild(item);
};

function updateLLMConfigSummary() {
    var summary = document.getElementById('llmConfigSummary');
    if (!summary) return;
    
    var providerCard = document.querySelector('.provider-card.selected');
    var providerName = providerCard ? providerCard.querySelector('.provider-name').textContent : 'DeepSeek';
    
    var modelSelect = document.getElementById('llmModelSelect');
    var modelName = modelSelect ? modelSelect.value : 'deepseek-chat';
    
    var tools = document.querySelectorAll('input[name="fcTool"]:checked');
    var toolCount = tools.length;
    
    summary.innerHTML = 
        '<span class="summary-item"><i class="ri-cloud-line"></i> ' + providerName + '</span>' +
        '<span class="summary-item"><i class="ri-cpu-line"></i> ' + modelName + '</span>' +
        '<span class="summary-item"><i class="ri-tools-line"></i> ' + toolCount + ' 工具</span>';
}
global.generatePrompt = function() {
    var cap = currentInstallCap;
    if (!cap) return;
    var prompt = '你是' + cap.name + '场景的AI助手。\n\n';
    prompt += '场景描述：' + (cap.description || '暂无描述') + '\n\n';
    prompt += '你的职责：\n';
    prompt += '1. 协助用户理解和使用场景功能\n';
    prompt += '2. 提供场景相关的建议和指导\n';
    prompt += '3. 帮助用户完成场景内的任务\n';
    document.getElementById('systemPrompt').value = prompt;
};
global.resetPrompt = function() {
    var cap = currentInstallCap;
    if (cap) {
        document.getElementById('systemPrompt').value = '你是' + cap.name + '场景的AI助手。' + (cap.description || '');
    }
};
global.selectLeader = function() {
    CapabilityDiscovery.showUserSelector('leader', '选择主导者', function(user) {
        var leaderInput = document.getElementById('leaderInput');
        if (leaderInput) {
            leaderInput.value = user.name || user.username || user;
        }
    });
};

global.addCollaborator = function() {
    CapabilityDiscovery.showUserSelector('collaborator', '选择协作者', function(user) {
        var list = document.getElementById('collaboratorList');
        if (!list) return;
        
        var empty = list.querySelector('.participant-empty');
        if (empty) {
            list.innerHTML = '';
        }
        
        var userName = user.name || user.username || user;
        var existingTags = list.querySelectorAll('.participant-tag span');
        for (var i = 0; i < existingTags.length; i++) {
            if (existingTags[i].textContent === userName) {
                return;
            }
        }
        
        var item = document.createElement('div');
        item.className = 'participant-tag';
        item.innerHTML = '<span>' + userName + '</span>' +
            '<button class="participant-remove" onclick="this.parentElement.remove()">' +
            '<i class="ri-close-line"></i></button>';
        list.appendChild(item);
    });
};

global.closeUserSelector = function() {
    var modal = document.getElementById('userSelectorModal');
    if (modal) {
        modal.style.display = 'none';
    }
};

global.selectUser = function(el) {
    var userJson = el.dataset.user;
    if (userJson) {
        var user = JSON.parse(userJson);
        if (window._userSelectorCallback) {
            window._userSelectorCallback(user);
        }
    }
    global.closeUserSelector();
};

global.searchUsers = function(keyword) {
    CapabilityDiscovery.loadUsers(keyword);
};

global.toggleBatchMode = function() {
    batchMode = !batchMode;
    selectedCapabilities = [];
    
    var batchBar = document.getElementById('batchBar');
    var resultsBody = document.getElementById('resultsBody');
    
    if (batchMode) {
        if (batchBar) batchBar.style.display = 'flex';
        if (resultsBody) resultsBody.classList.add('batch-mode');
    } else {
        if (batchBar) batchBar.style.display = 'none';
        if (resultsBody) resultsBody.classList.remove('batch-mode');
        resultsBody.querySelectorAll('.result-item').forEach(function(item) {
            item.classList.remove('selected');
        });
    }
    
    CapabilityDiscovery.updateBatchCount();
};

global.toggleCapabilitySelection = function(skillId, event) {
    if (!batchMode) return;
    
    event.stopPropagation();
    
    var item = event.target.closest('.result-item');
    var index = selectedCapabilities.indexOf(skillId);
    
    if (index > -1) {
        selectedCapabilities.splice(index, 1);
        if (item) item.classList.remove('selected');
    } else {
        selectedCapabilities.push(skillId);
        if (item) item.classList.add('selected');
    }
    
    CapabilityDiscovery.updateBatchCount();
};

global.selectAllCapabilities = function() {
    var items = document.querySelectorAll('.result-item');
    items.forEach(function(item) {
        var skillId = item.dataset.skillId;
        if (skillId && selectedCapabilities.indexOf(skillId) === -1) {
            selectedCapabilities.push(skillId);
            item.classList.add('selected');
        }
    });
    CapabilityDiscovery.updateBatchCount();
};

global.deselectAllCapabilities = function() {
    selectedCapabilities = [];
    document.querySelectorAll('.result-item.selected').forEach(function(item) {
        item.classList.remove('selected');
    });
    CapabilityDiscovery.updateBatchCount();
};

global.batchInstall = function() {
    if (selectedCapabilities.length === 0) {
        alert('请先选择要安装的能力');
        return;
    }
    
    var caps = selectedCapabilities.map(function(id) {
        return discoveredCapabilities.find(function(c) { return c.id === id; });
    }).filter(function(c) { return c && !c.installed; });
    
    if (caps.length === 0) {
        alert('所选能力已全部安装');
        return;
    }
    
    if (!confirm('确定要批量安装 ' + caps.length + ' 个能力吗？')) {
        return;
    }
    
    CapabilityDiscovery.executeBatchInstall(caps);
};

CapabilityDiscovery.updateBatchCount = function() {
    var countEl = document.getElementById('selectedCount');
    if (countEl) {
        countEl.textContent = selectedCapabilities.length;
    }
    
    var installBtn = document.getElementById('batchInstallBtn');
    if (installBtn) {
        installBtn.disabled = selectedCapabilities.length === 0;
    }
};

CapabilityDiscovery.executeBatchInstall = function(caps) {
    var modal = document.getElementById('batchInstallModal');
    if (!modal) {
        modal = document.createElement('div');
        modal.id = 'batchInstallModal';
        modal.className = 'modal-overlay';
        modal.innerHTML = 
            '<div class="modal-content" style="max-width: 600px;">' +
            '<div class="modal-header">' +
            '<h3><i class="ri-download-cloud-line"></i> 批量安装</h3>' +
            '</div>' +
            '<div class="modal-body">' +
            '<div class="batch-progress">' +
            '<div class="batch-progress-bar" id="batchProgressBar"></div>' +
            '</div>' +
            '<div class="batch-status" id="batchStatus">准备安装...</div>' +
            '<div class="batch-list" id="batchList"></div>' +
            '</div>' +
            '<div class="modal-footer" style="display: none;" id="batchFooter">' +
            '<button class="nx-btn nx-btn--primary" onclick="closeBatchInstall()">完成</button>' +
            '</div>' +
            '</div>';
        document.body.appendChild(modal);
    }
    
    modal.classList.add('show');
    
    var progressBar = document.getElementById('batchProgressBar');
    var statusEl = document.getElementById('batchStatus');
    var listEl = document.getElementById('batchList');
    var footerEl = document.getElementById('batchFooter');
    
    listEl.innerHTML = caps.map(function(cap) {
        return '<div class="batch-item" data-id="' + cap.id + '">' +
            '<i class="ri-time-line batch-item-icon pending"></i>' +
            '<span class="batch-item-name">' + cap.name + '</span>' +
            '<span class="batch-item-status">等待中</span></div>';
    }).join('');
    
    var completed = 0;
    var total = caps.length;
    
    function installNext() {
        if (completed >= total) {
            progressBar.style.width = '100%';
            progressBar.style.background = 'var(--nx-success)';
            statusEl.textContent = '全部安装完成！成功: ' + total;
            footerEl.style.display = 'flex';
            return;
        }
        
        var cap = caps[completed];
        var progress = Math.round((completed / total) * 100);
        progressBar.style.width = progress + '%';
        statusEl.textContent = '正在安装: ' + cap.name + ' (' + (completed + 1) + '/' + total + ')';
        
        var itemEl = listEl.querySelector('[data-id="' + cap.id + '"]');
        if (itemEl) {
            itemEl.querySelector('.batch-item-icon').className = 'batch-item-icon ri-loader-4-line ri-spin';
            itemEl.querySelector('.batch-item-status').textContent = '安装中...';
        }
        
        ApiClient.post('/api/v1/discovery/install', {
            capabilityId: cap.id,
            name: cap.name,
            skillForm: cap.skillForm
        })
        .then(function(result) {
            if (itemEl) {
                itemEl.querySelector('.batch-item-icon').className = 'batch-item-icon ri-checkbox-circle-line';
                itemEl.querySelector('.batch-item-icon').style.color = 'var(--nx-success)';
                itemEl.querySelector('.batch-item-status').textContent = '成功';
            }
        })
        .catch(function(error) {
            if (itemEl) {
                itemEl.querySelector('.batch-item-icon').className = 'batch-item-icon ri-close-circle-line';
                itemEl.querySelector('.batch-item-icon').style.color = 'var(--nx-danger)';
                itemEl.querySelector('.batch-item-status').textContent = '失败';
            }
        })
        .finally(function() {
            completed++;
            setTimeout(installNext, 500);
        });
    }
    
    installNext();
};

global.closeBatchInstall = function() {
    var modal = document.getElementById('batchInstallModal');
    if (modal) {
        modal.classList.remove('show');
    }
    global.toggleBatchMode();
    CapabilityDiscovery.startScan();
};

global.openDiscoveryConfig = function() {
    var modal = document.getElementById('discoveryConfigModal');
    if (!modal) {
        modal = document.createElement('div');
        modal.id = 'discoveryConfigModal';
        modal.className = 'modal-overlay';
        modal.innerHTML = 
            '<div class="modal-content" style="max-width: 600px;">' +
            '<div class="modal-header">' +
            '<h3><i class="ri-settings-3-line"></i> 发现源配置</h3>' +
            '<button class="modal-close" onclick="closeDiscoveryConfig()"><i class="ri-close-line"></i></button>' +
            '</div>' +
            '<div class="modal-body">' +
            '<div class="config-tabs">' +
            '<div class="config-tab active" data-tab="gitee" onclick="switchConfigTab(\'gitee\')">Gitee</div>' +
            '<div class="config-tab" data-tab="github" onclick="switchConfigTab(\'github\')">GitHub</div>' +
            '</div>' +
            '<div class="config-tab-content active" id="configTabGitee">' +
            '<div class="config-form-vertical">' +
            '<div class="form-group">' +
            '<label class="form-label">仓库地址</label>' +
            '<input type="text" class="form-input" id="configGiteeRepo" placeholder="例如: owner/repo">' +
            '<div class="form-hint">Gitee 仓库地址，格式: owner/repo</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<label class="form-label">访问令牌 (可选)</label>' +
            '<input type="password" class="form-input" id="configGiteeToken" placeholder="用于访问私有仓库">' +
            '<div class="form-hint">私有仓库需要提供访问令牌</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<label class="form-label">分支</label>' +
            '<input type="text" class="form-input" id="configGiteeBranch" value="main" placeholder="默认: main">' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="config-tab-content" id="configTabGithub">' +
            '<div class="config-form-vertical">' +
            '<div class="form-group">' +
            '<label class="form-label">仓库地址</label>' +
            '<input type="text" class="form-input" id="configGithubRepo" placeholder="例如: owner/repo">' +
            '<div class="form-hint">GitHub 仓库地址，格式: owner/repo</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<label class="form-label">访问令牌 (可选)</label>' +
            '<input type="password" class="form-input" id="configGithubToken" placeholder="用于访问私有仓库">' +
            '<div class="form-hint">私有仓库需要提供访问令牌</div>' +
            '</div>' +
            '<div class="form-group">' +
            '<label class="form-label">分支</label>' +
            '<input type="text" class="form-input" id="configGithubBranch" value="main" placeholder="默认: main">' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="modal-footer">' +
            '<button class="nx-btn nx-btn--secondary" onclick="closeDiscoveryConfig()">取消</button>' +
            '<button class="nx-btn nx-btn--primary" onclick="saveDiscoveryConfig()">保存配置</button>' +
            '</div>' +
            '</div>';
        document.body.appendChild(modal);
        
        var style = document.createElement('style');
        style.textContent = 
            '.config-tabs { display: flex; gap: 4px; margin-bottom: 20px; border-bottom: 1px solid var(--nx-border); padding-bottom: 12px; }' +
            '.config-tab { padding: 8px 16px; border-radius: 6px; cursor: pointer; font-size: 14px; color: var(--nx-text-secondary); transition: all 0.2s; }' +
            '.config-tab:hover { background: var(--nx-bg-hover); color: var(--nx-text-primary); }' +
            '.config-tab.active { background: var(--nx-primary); color: white; }' +
            '.config-tab-content { display: none; }' +
            '.config-tab-content.active { display: block; }' +
            '.config-form-vertical { display: flex; flex-direction: column; gap: 16px; }';
        document.head.appendChild(style);
        
        CapabilityDiscovery.loadDiscoveryConfig();
    }
    
    modal.classList.add('show');
};

global.closeDiscoveryConfig = function() {
    var modal = document.getElementById('discoveryConfigModal');
    if (modal) {
        modal.classList.remove('show');
    }
};

global.switchConfigTab = function(tab) {
    var tabs = document.querySelectorAll('.config-tab');
    var contents = document.querySelectorAll('.config-tab-content');
    
    tabs.forEach(function(t) {
        t.classList.remove('active');
        if (t.dataset.tab === tab) {
            t.classList.add('active');
        }
    });
    
    contents.forEach(function(c) {
        c.classList.remove('active');
        if (c.id === 'configTab' + tab.charAt(0).toUpperCase() + tab.slice(1)) {
            c.classList.add('active');
        }
    });
};

CapabilityDiscovery.loadDiscoveryConfig = function() {
    ApiClient.get('/api/v1/discovery/config')
        .then(function(result) {
            if (result.status === 'success' && result.data) {
                if (result.data.gitee) {
                    var gitee = result.data.gitee;
                    var repoEl = document.getElementById('configGiteeRepo');
                    var tokenEl = document.getElementById('configGiteeToken');
                    var branchEl = document.getElementById('configGiteeBranch');
                    if (repoEl) repoEl.value = gitee.repo || '';
                    if (tokenEl) tokenEl.value = gitee.token || '';
                    if (branchEl) branchEl.value = gitee.branch || 'main';
                }
                if (result.data.github) {
                    var github = result.data.github;
                    var repoEl = document.getElementById('configGithubRepo');
                    var tokenEl = document.getElementById('configGithubToken');
                    var branchEl = document.getElementById('configGithubBranch');
                    if (repoEl) repoEl.value = github.repo || '';
                    if (tokenEl) tokenEl.value = github.token || '';
                    if (branchEl) branchEl.value = github.branch || 'main';
                }
            }
        })
        .catch(function(error) {
            console.error('[loadDiscoveryConfig] Error:', error);
        });
};

global.saveDiscoveryConfig = function() {
    var config = {
        gitee: {
            repo: document.getElementById('configGiteeRepo')?.value || '',
            token: document.getElementById('configGiteeToken')?.value || '',
            branch: document.getElementById('configGiteeBranch')?.value || 'main'
        },
        github: {
            repo: document.getElementById('configGithubRepo')?.value || '',
            token: document.getElementById('configGithubToken')?.value || '',
            branch: document.getElementById('configGithubBranch')?.value || 'main'
        }
    };
    
    ApiClient.put('/api/v1/discovery/config', config)
        .then(function(result) {
            if (result.status === 'success') {
                CapabilityDiscovery.addLog('success', '配置保存成功');
                global.closeDiscoveryConfig();
            } else {
                CapabilityDiscovery.addLog('error', '配置保存失败: ' + (result.message || '未知错误'));
            }
        })
        .catch(function(error) {
            CapabilityDiscovery.addLog('error', '配置保存失败: ' + error.message);
        });
};

CapabilityDiscovery.init();

})(typeof window !== 'undefined' ? window : this);
