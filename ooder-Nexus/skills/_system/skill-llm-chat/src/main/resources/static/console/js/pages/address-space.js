var AddressSpace = {
    categories: [],
    addresses: [],
    statusData: {},
    
    init: function() {
        var self = this;
        CategoryService.loadCategories().then(function(categories) {
            self.categories = categories.map(function(cat, index) {
                var base = index * 8;
                return {
                    code: cat.code,
                    name: cat.name,
                    range: '0x' + base.toString(16).toUpperCase().padStart(2, '0') + '-0x' + (base + 7).toString(16).toUpperCase().padStart(2, '0'),
                    base: base,
                    color: cat.color,
                    icon: cat.icon,
                    userFacing: cat.userFacing
                };
            });
            self.loadAddresses();
            self.loadStatus();
        });
    },
    
    getCategoryColor: function(categoryId) {
        return CategoryService.getColor(categoryId);
    },
    
    loadAddresses: function() {
        var self = this;
        fetch('/api/v1/config-meta/addresses')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    self.addresses = result.data.addresses || [];
                    self.renderMap();
                    self.renderCategoryFilter();
                    self.renderCategoryTree();
                    self.renderLegend();
                } else {
                    self.loadMockAddresses();
                }
            })
            .catch(function(error) {
                console.error('[loadAddresses] Error:', error);
                self.loadMockAddresses();
            });
    },
    
    loadMockAddresses: function() {
        this.addresses = [
            { address: 0x00, name: 'SYS_REGISTRY', category: 'sys', description: '系统注册中心' },
            { address: 0x01, name: 'SYS_CONFIG', category: 'sys', description: '系统配置中心' },
            { address: 0x02, name: 'SYS_CAPABILITY', category: 'sys', description: '能力管理服务' },
            { address: 0x03, name: 'SYS_PROTOCOL', category: 'sys', description: '协议处理服务' },
            { address: 0x08, name: 'ORG_LOCAL', category: 'org', description: '本地组织服务' },
            { address: 0x09, name: 'ORG_DINGDING', category: 'org', description: '钉钉组织服务' },
            { address: 0x0A, name: 'ORG_FEISHU', category: 'org', description: '飞书组织服务' },
            { address: 0x0B, name: 'ORG_WECOM', category: 'org', description: '企业微信组织服务' },
            { address: 0x0C, name: 'ORG_LDAP', category: 'org', description: 'LDAP组织服务' },
            { address: 0x10, name: 'AUTH_USER', category: 'auth', description: '用户认证服务' },
            { address: 0x11, name: 'AUTH_TOKEN', category: 'auth', description: '令牌管理服务' },
            { address: 0x18, name: 'NET_PROXY', category: 'net', description: '网络代理服务' },
            { address: 0x19, name: 'NET_DNS', category: 'net', description: 'DNS服务' },
            { address: 0x20, name: 'VFS_BASE', category: 'vfs', description: 'VFS基础服务' },
            { address: 0x21, name: 'VFS_LOCAL', category: 'vfs', description: '本地文件存储' },
            { address: 0x22, name: 'VFS_MINIO', category: 'vfs', description: 'MinIO对象存储' },
            { address: 0x23, name: 'VFS_OSS', category: 'vfs', description: '阿里云OSS存储' },
            { address: 0x24, name: 'VFS_S3', category: 'vfs', description: 'AWS S3存储' },
            { address: 0x28, name: 'DB_MYSQL', category: 'db', description: 'MySQL数据库' },
            { address: 0x29, name: 'DB_POSTGRES', category: 'db', description: 'PostgreSQL数据库' },
            { address: 0x2A, name: 'DB_MONGODB', category: 'db', description: 'MongoDB数据库' },
            { address: 0x2B, name: 'DB_REDIS', category: 'db', description: 'Redis缓存' },
            { address: 0x30, name: 'LLM_BASE', category: 'llm', description: 'LLM基础服务', fallback: 0x31 },
            { address: 0x31, name: 'LLM_OLLAMA', category: 'llm', description: 'Ollama本地模型', fallback: 0x32 },
            { address: 0x32, name: 'LLM_OPENAI', category: 'llm', description: 'OpenAI API' },
            { address: 0x33, name: 'LLM_QIANWEN', category: 'llm', description: '通义千问' },
            { address: 0x34, name: 'LLM_DEEPSEEK', category: 'llm', description: 'DeepSeek' },
            { address: 0x35, name: 'LLM_VOLCENGINE', category: 'llm', description: '火山引擎豆包' },
            { address: 0x38, name: 'KNOW_BASE', category: 'know', description: '知识库基础服务' },
            { address: 0x39, name: 'KNOW_VECTOR', category: 'know', description: '向量知识库' },
            { address: 0x3A, name: 'KNOW_RAG', category: 'know', description: 'RAG检索服务' },
            { address: 0x3B, name: 'KNOW_EMBEDDING', category: 'know', description: '嵌入服务' },
            { address: 0x40, name: 'PAY_BASE', category: 'payment', description: '支付基础服务' },
            { address: 0x41, name: 'PAY_ALIPAY', category: 'payment', description: '支付宝' },
            { address: 0x42, name: 'PAY_WECHAT', category: 'payment', description: '微信支付' },
            { address: 0x48, name: 'MEDIA_BASE', category: 'media', description: '媒体基础服务' },
            { address: 0x49, name: 'MEDIA_WECHAT', category: 'media', description: '微信公众号' },
            { address: 0x4A, name: 'MEDIA_WEIBO', category: 'media', description: '微博' },
            { address: 0x50, name: 'COMM_BASE', category: 'comm', description: '通讯基础服务' },
            { address: 0x51, name: 'COMM_MQTT', category: 'comm', description: 'MQTT服务' },
            { address: 0x52, name: 'COMM_EMAIL', category: 'comm', description: '邮件服务' },
            { address: 0x53, name: 'COMM_NOTIFY', category: 'comm', description: '通知服务' },
            { address: 0x58, name: 'MON_BASE', category: 'mon', description: '监控基础服务' },
            { address: 0x59, name: 'MON_HEALTH', category: 'mon', description: '健康检查' },
            { address: 0x5A, name: 'MON_AGENT', category: 'mon', description: '代理管理' },
            { address: 0x60, name: 'IOT_BASE', category: 'iot', description: '物联网基础服务' },
            { address: 0x61, name: 'IOT_K8S', category: 'iot', description: 'Kubernetes' },
            { address: 0x68, name: 'SEARCH_BASE', category: 'search', description: '搜索基础服务' },
            { address: 0x69, name: 'SEARCH_ES', category: 'search', description: 'Elasticsearch' },
            { address: 0x70, name: 'SCHED_BASE', category: 'sched', description: '调度基础服务' },
            { address: 0x71, name: 'SCHED_QUARTZ', category: 'sched', description: 'Quartz调度' },
            { address: 0x78, name: 'SEC_BASE', category: 'sec', description: '安全基础服务' },
            { address: 0x79, name: 'SEC_ACCESS', category: 'sec', description: '访问控制' },
            { address: 0xF0, name: 'UTIL_BASE', category: 'util', description: '工具基础服务' },
            { address: 0xF1, name: 'UTIL_REPORT', category: 'util', description: '报表服务' }
        ];
        this.renderMap();
        this.renderCategoryFilter();
        this.renderCategoryTree();
        this.renderLegend();
    },
    
    loadStatus: function() {
        var self = this;
        fetch('/api/v1/capabilities/status')
            .then(function(response) { return response.json(); })
            .then(function(result) {
                if (result.status === 'success' && result.data) {
                    self.statusData = result.data;
                    self.updateMapStatus();
                    self.renderStats();
                }
            })
            .catch(function(error) {
                console.error('[loadStatus] Error:', error);
                self.generateMockStatus();
            });
    },
    
    generateMockStatus: function() {
        var self = this;
        this.addresses.forEach(function(addr) {
            var cat = addr.category;
            if (cat === 'sys' || cat === 'auth' || cat === 'mon') {
                self.statusData[addr.address] = 'active';
            } else if (cat === 'llm' || cat === 'db' || cat === 'vfs') {
                self.statusData[addr.address] = Math.random() > 0.5 ? 'active' : 'inactive';
            } else {
                self.statusData[addr.address] = 'inactive';
            }
        });
        this.updateMapStatus();
        this.renderStats();
    },
    
    renderMap: function() {
        var container = document.getElementById('addressMap');
        if (!container) return;
        
        var html = '<div class="map-grid">';
        
        this.categories.forEach(function(cat) {
            html += '<div class="category-block" data-category="' + cat.code + '" style="--cat-color: ' + cat.color + '">';
            html += '<div class="category-header">';
            html += '<i class="' + cat.icon + '"></i>';
            html += '<span class="category-name">' + cat.name + '</span>';
            html += '<span class="category-range">' + cat.range + '</span>';
            html += '</div>';
            html += '<div class="address-cells">';
            
            var startAddr = cat.base;
            var endAddr = cat.code === 'util' ? 0xFF : cat.base + 7;
            
            for (var addr = startAddr; addr <= endAddr; addr++) {
                var addrHex = '0x' + addr.toString(16).toUpperCase().padStart(2, '0');
                var addrInfo = this.findAddress(addr);
                var status = this.statusData[addr] || 'inactive';
                
                html += '<div class="address-cell ' + status + '" data-address="' + addr + '" onclick="showAddressDetail(' + addr + ')">';
                html += '<div class="cell-address">' + addrHex + '</div>';
                if (addrInfo) {
                    html += '<div class="cell-name">' + addrInfo.name + '</div>';
                    html += '<div class="cell-desc">' + addrInfo.description + '</div>';
                } else {
                    html += '<div class="cell-name">空闲</div>';
                }
                html += '<div class="cell-status"><i class="ri-checkbox-circle-line"></i></div>';
                html += '</div>';
            }
            
            html += '</div></div>';
        }.bind(this));
        
        html += '</div>';
        container.innerHTML = html;
    },
    
    findAddress: function(addr) {
        return this.addresses.find(function(a) { return a.address === addr; });
    },
    
    updateMapStatus: function() {
        var self = this;
        Object.keys(this.statusData).forEach(function(addr) {
            var cell = document.querySelector('.address-cell[data-address="' + addr + '"]');
            if (cell) {
                cell.classList.remove('active', 'inactive', 'pending');
                cell.classList.add(self.statusData[addr]);
            }
        });
    },
    
    renderCategoryFilter: function() {
        var select = document.getElementById('categoryFilter');
        if (!select) return;
        
        var html = '<option value="all">全部分类</option>';
        this.categories.forEach(function(cat) {
            html += '<option value="' + cat.code + '">' + cat.name + ' (' + cat.range + ')</option>';
        });
        select.innerHTML = html;
    },
    
    renderCategoryTree: function() {
        var container = document.getElementById('categoryTree');
        if (!container) return;
        
        var html = '';
        this.categories.forEach(function(cat) {
            var count = this.addresses.filter(function(a) { return a.category === cat.code; }).length;
            var activeCount = Object.keys(this.statusData).filter(function(addr) {
                var addrInfo = this.findAddress(parseInt(addr));
                return addrInfo && addrInfo.category === cat.code && this.statusData[addr] === 'active';
            }.bind(this)).length;
            
            html += '<div class="tree-item" data-category="' + cat.code + '" onclick="goToCategory(\'' + cat.code + '\')">';
            html += '<div class="tree-icon" style="color: ' + cat.color + '"><i class="' + cat.icon + '"></i></div>';
            html += '<div class="tree-info">';
            html += '<div class="tree-name">' + cat.name + '</div>';
            html += '<div class="tree-count">' + activeCount + '/' + count + ' 已注册</div>';
            html += '</div>';
            html += '</div>';
        }.bind(this));
        
        container.innerHTML = html;
    },
    
    renderLegend: function() {
        var container = document.getElementById('legendItems');
        if (!container) return;
        
        var html = '<div class="legend-item"><span class="legend-dot active"></span><span>已注册</span></div>';
        html += '<div class="legend-item"><span class="legend-dot inactive"></span><span>空闲</span></div>';
        html += '<div class="legend-item"><span class="legend-dot pending"></span><span>待分配</span></div>';
        html += '<div class="legend-item"><span class="legend-dot error"></span><span>冲突/错误</span></div>';
        
        container.innerHTML = html;
    },
    
    renderStats: function() {
        var container = document.getElementById('statsGrid');
        if (!container) return;
        
        var total = this.addresses.length;
        var active = Object.values(this.statusData).filter(function(s) { return s === 'active'; }).length;
        var inactive = Object.values(this.statusData).filter(function(s) { return s === 'inactive'; }).length;
        
        var html = '<div class="stat-item"><div class="stat-value">' + total + '</div><div class="stat-label">总地址数</div></div>';
        html += '<div class="stat-item"><div class="stat-value active">' + active + '</div><div class="stat-label">已注册</div></div>';
        html += '<div class="stat-item"><div class="stat-value inactive">' + inactive + '</div><div class="stat-label">空闲</div></div>';
        html += '<div class="stat-item"><div class="stat-value">' + this.categories.length + '</div><div class="stat-label">分类数</div></div>';
        
        container.innerHTML = html;
    }
};

function filterByCategory() {
    var value = document.getElementById('categoryFilter').value;
    var blocks = document.querySelectorAll('.category-block');
    blocks.forEach(function(block) {
        if (value === 'all' || block.dataset.category === value) {
            block.style.display = '';
        } else {
            block.style.display = 'none';
        }
    });
}

function filterByStatus() {
    var value = document.getElementById('statusFilter').value;
    var cells = document.querySelectorAll('.address-cell');
    cells.forEach(function(cell) {
        if (value === 'all') {
            cell.style.display = '';
        } else {
            var status = cell.classList.contains(value);
            cell.style.display = status ? '' : 'none';
        }
    });
}

function goToCategory(code) {
    document.getElementById('categoryFilter').value = code;
    filterByCategory();
    
    var block = document.querySelector('.category-block[data-category="' + code + '"]');
    if (block) {
        block.scrollIntoView({ behavior: 'smooth', block: 'center' });
        block.classList.add('highlight');
        setTimeout(function() { block.classList.remove('highlight'); }, 2000);
    }
}

function showAddressDetail(addr) {
    var addrInfo = AddressSpace.findAddress(addr);
    if (!addrInfo) return;
    
    var category = AddressSpace.categories.find(function(c) { return c.code === addrInfo.category; });
    var status = AddressSpace.statusData[addr] || 'inactive';
    
    document.getElementById('modalTitle').textContent = addrInfo.name + ' (0x' + addr.toString(16).toUpperCase().padStart(2, '0') + ')';
    
    var html = '<div class="detail-section">';
    html += '<div class="detail-row"><label>分类:</label><span style="color:' + (category ? category.color : '#666') + '">' + (category ? category.name : addrInfo.category) + '</span></div>';
    html += '<div class="detail-row"><label>描述:</label><span>' + addrInfo.description + '</span></div>';
    html += '<div class="detail-row"><label>状态:</label><span class="status-badge ' + status + '">' + (status === 'active' ? '已注册' : '空闲') + '</span></div>';
    if (addrInfo.fallback) {
        var fallbackInfo = AddressSpace.findAddress(addrInfo.fallback);
        html += '<div class="detail-row"><label>Fallback:</label><span>→ 0x' + addrInfo.fallback.toString(16).toUpperCase() + ' (' + (fallbackInfo ? fallbackInfo.name : '未知') + ')</span></div>';
    }
    html += '</div>';
    
    html += '<div class="detail-actions">';
    if (status === 'inactive') {
        html += '<button class="nx-btn nx-btn--primary" onclick="configureAddress(' + addr + ')"><i class="ri-settings-3-line"></i> 配置驱动</button>';
    } else {
        html += '<button class="nx-btn nx-btn--secondary" onclick="testConnection(' + addr + ')"><i class="ri-link"></i> 测试连接</button>';
        html += '<button class="nx-btn nx-btn--ghost" onclick="viewLogs(' + addr + ')"><i class="ri-file-list-line"></i> 查看日志</button>';
    }
    html += '</div>';
    
    document.getElementById('modalBody').innerHTML = html;
    document.getElementById('addressDetailModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('addressDetailModal').style.display = 'none';
}

function refreshStatus() {
    AddressSpace.loadStatus();
}

function openConfigWizard() {
    window.location.href = 'driver-config.html';
}

function configureAddress(addr) {
    var addrInfo = AddressSpace.findAddress(addr);
    if (addrInfo) {
        window.location.href = 'driver-config.html?category=' + addrInfo.category + '&address=' + addr;
    }
}

function testConnection(addr) {
    var addrInfo = AddressSpace.findAddress(addr);
    if (!addrInfo) {
        alert('地址信息不存在');
        return;
    }
    
    var modalHtml = `
        <div class="modal-overlay" id="testConnectionModal" style="display: flex; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); z-index: 1000; align-items: center; justify-content: center;">
            <div class="modal" style="background: var(--nx-bg-elevated); border-radius: 12px; max-width: 450px; width: 90%;">
                <div class="modal-header" style="padding: 16px 20px; border-bottom: 1px solid var(--nx-border-color); display: flex; justify-content: space-between; align-items: center;">
                    <h3 style="margin: 0; font-size: 16px;"><i class="ri-link"></i> 测试连接 - 0x${addr.toString(16).toUpperCase()}</h3>
                    <button class="modal-close" onclick="closeTestConnectionModal()" style="background: none; border: none; font-size: 20px; cursor: pointer;"><i class="ri-close-line"></i></button>
                </div>
                <div class="modal-body" style="padding: 20px;" id="testConnectionContent">
                    <div style="text-align: center; padding: 20px;">
                        <i class="ri-loader-4-line ri-spin" style="font-size: 24px;"></i>
                        <p>正在测试连接...</p>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    var existing = document.getElementById('testConnectionModal');
    if (existing) existing.remove();
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    fetch('/api/v1/config/addresses/' + addr + '/test', {
        method: 'POST'
    })
        .then(function(response) { return response.json(); })
        .then(function(result) {
            var content = document.getElementById('testConnectionContent');
            var isSuccess = result.status === 'success' || result.connected === true;
            content.innerHTML = `
                <div style="text-align: center; padding: 20px;">
                    <i class="ri-${isSuccess ? 'checkbox-circle' : 'close-circle'}-line" style="font-size: 48px; color: var(--nx-${isSuccess ? 'success' : 'danger'}-color);"></i>
                    <h4 style="margin: 16px 0 8px;">${isSuccess ? '连接成功' : '连接失败'}</h4>
                    <p style="color: var(--nx-text-secondary);">${result.message || ''}</p>
                    ${result.latency ? '<p style="font-size: 12px; color: var(--nx-text-secondary);">延迟: ' + result.latency + 'ms</p>' : ''}
                </div>
            `;
        })
        .catch(function(error) {
            var content = document.getElementById('testConnectionContent');
            content.innerHTML = `
                <div style="text-align: center; padding: 20px;">
                    <i class="ri-close-circle-line" style="font-size: 48px; color: var(--nx-danger-color);"></i>
                    <h4 style="margin: 16px 0 8px;">测试失败</h4>
                    <p style="color: var(--nx-text-secondary);">${error.message}</p>
                </div>
            `;
        });
}

function closeTestConnectionModal() {
    var modal = document.getElementById('testConnectionModal');
    if (modal) modal.remove();
}

function viewLogs(addr) {
    var modalHtml = `
        <div class="modal-overlay" id="viewLogsModal" style="display: flex; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); z-index: 1000; align-items: center; justify-content: center;">
            <div class="modal" style="background: var(--nx-bg-elevated); border-radius: 12px; max-width: 700px; width: 90%; max-height: 80vh;">
                <div class="modal-header" style="padding: 16px 20px; border-bottom: 1px solid var(--nx-border-color); display: flex; justify-content: space-between; align-items: center;">
                    <h3 style="margin: 0; font-size: 16px;"><i class="ri-file-list-3-line"></i> 日志 - 0x${addr.toString(16).toUpperCase()}</h3>
                    <button class="modal-close" onclick="closeViewLogsModal()" style="background: none; border: none; font-size: 20px; cursor: pointer;"><i class="ri-close-line"></i></button>
                </div>
                <div class="modal-body" style="padding: 20px; max-height: 500px; overflow-y: auto;" id="viewLogsContent">
                    <div style="text-align: center; padding: 20px;">
                        <i class="ri-loader-4-line ri-spin" style="font-size: 24px;"></i>
                        <p>正在加载日志...</p>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 16px 20px; border-top: 1px solid var(--nx-border-color); display: flex; justify-content: space-between;">
                    <button class="nx-btn nx-btn--secondary" onclick="refreshLogs(${addr})"><i class="ri-refresh-line"></i> 刷新</button>
                    <button class="nx-btn nx-btn--secondary" onclick="closeViewLogsModal()">关闭</button>
                </div>
            </div>
        </div>
    `;
    
    var existing = document.getElementById('viewLogsModal');
    if (existing) existing.remove();
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    loadLogs(addr);
}

function loadLogs(addr) {
    fetch('/api/v1/config/addresses/' + addr + '/logs?limit=100')
        .then(function(response) { return response.json(); })
        .then(function(result) {
            var content = document.getElementById('viewLogsContent');
            var logs = result.data || result.logs || [];
            
            if (logs.length === 0) {
                content.innerHTML = '<p style="text-align: center; color: var(--nx-text-secondary); padding: 40px;">暂无日志记录</p>';
                return;
            }
            
            var html = '<div style="font-family: monospace; font-size: 12px;">';
            logs.forEach(function(log) {
                var levelClass = log.level === 'ERROR' ? 'color: var(--nx-danger-color);' : 
                                 log.level === 'WARN' ? 'color: var(--nx-warning-color);' : 
                                 log.level === 'INFO' ? 'color: var(--nx-success-color);' : '';
                html += `
                    <div style="padding: 8px; border-bottom: 1px solid var(--nx-border-color);">
                        <div style="display: flex; gap: 8px; margin-bottom: 4px;">
                            <span style="${levelClass}">[${log.level || 'INFO'}]</span>
                            <span style="color: var(--nx-text-secondary);">${log.timestamp || ''}</span>
                        </div>
                        <div style="color: var(--nx-text-primary);">${log.message || log.content || ''}</div>
                    </div>
                `;
            });
            html += '</div>';
            content.innerHTML = html;
        })
        .catch(function(error) {
            var content = document.getElementById('viewLogsContent');
            content.innerHTML = '<p style="text-align: center; color: var(--nx-danger-color); padding: 40px;">加载日志失败: ' + error.message + '</p>';
        });
}

function refreshLogs(addr) {
    loadLogs(addr);
}

function closeViewLogsModal() {
    var modal = document.getElementById('viewLogsModal');
    if (modal) modal.remove();
}

AddressSpace.init();
