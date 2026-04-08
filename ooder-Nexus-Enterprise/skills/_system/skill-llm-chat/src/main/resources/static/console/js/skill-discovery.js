// 技能雷达发现 JavaScript

var discoveryResults = [];
var activeSources = new Set(['skill_center', 'local_fs']);
var isScanning = false;

function initInstallPage() {
    console.log('[SkillDiscovery] 初始化技能雷达发现页面');

    if (typeof initMenu === 'function') {
        initMenu('skill-discovery');
    }

    initSourceStates();
    loadInitialData();
}

function initSourceStates() {
    activeSources.forEach(source => {
        const card = document.getElementById('source-' + source.replace('_', '-'));
        if (card) {
            card.classList.add('active');
            updateSourceBadge(source, 'active');
        }
    });
}

function loadInitialData() {
    updateStats();
    loadDiscoveryResults();
}

window.toggleSource = function(source) {
    const card = document.getElementById('source-' + source.replace('_', '-'));
    
    if (activeSources.has(source)) {
        activeSources.delete(source);
        if (card) {
            card.classList.remove('active');
            updateSourceBadge(source, 'ready');
        }
    } else {
        activeSources.add(source);
        if (card) {
            card.classList.add('active');
            updateSourceBadge(source, 'active');
        }
    }
    
    updateStats();
};

function updateSourceBadge(source, status) {
    const badge = document.getElementById('badge-' + source.replace('_', '-'));
    if (!badge) return;
    
    switch(status) {
        case 'active':
            badge.className = 'nx-badge nx-badge--success';
            badge.textContent = '已启用';
            break;
        case 'scanning':
            badge.className = 'nx-badge nx-badge--warning';
            badge.textContent = '扫描中';
            break;
        case 'ready':
        default:
            badge.className = 'nx-badge';
            badge.textContent = '就绪';
            break;
    }
}

window.startRadarScan = async function() {
    if (isScanning) {
        utils.msg.warning('扫描正在进行中...');
        return;
    }
    
    isScanning = true;
    console.log('[SkillDiscovery] 开始雷达扫描，启用源:', Array.from(activeSources));
    
    const scanStatus = document.getElementById('scanStatus');
    const radarSweep = document.getElementById('radarSweep');
    const radarContainer = document.getElementById('radarContainer');
    
    if (scanStatus) scanStatus.textContent = '正在扫描...';
    if (radarSweep) radarSweep.style.display = 'block';
    
    let totalFound = 0;
    let activeCount = 0;
    
    for (const source of activeSources) {
        const card = document.getElementById('source-' + source.replace('_', '-'));
        if (card) {
            card.classList.add('scanning');
            updateSourceBadge(source, 'scanning');
        }
        
        try {
            const result = await scanSource(source);
            totalFound += result.length;
            activeCount++;
            
            if (card) {
                card.classList.remove('scanning');
                updateSourceBadge(source, 'active');
            }
            
            addRadarDot();
            
        } catch (error) {
            console.error('[SkillDiscovery] 扫描源失败:', source, error);
            if (card) {
                card.classList.remove('scanning');
                updateSourceBadge(source, 'active');
            }
        }
    }
    
    isScanning = false;
    
    if (scanStatus) scanStatus.textContent = `扫描完成，发现 ${totalFound} 个技能`;
    document.getElementById('statLastScan').textContent = new Date().toLocaleTimeString();
    document.getElementById('statSources').textContent = `${activeCount}/${activeSources.size}`;
    
    utils.msg.success(`雷达扫描完成！发现 ${totalFound} 个技能`);
    
    loadDiscoveryResults();
};

async function scanSource(source) {
    console.log('[SkillDiscovery] 扫描源:', source);
    
    const endpoint = '/skills/discovery/list';
    const params = {
        pageNum: 1,
        pageSize: 100,
        sources: [source]
    };
    
    try {
        const result = await utils.api.post(endpoint, params);
        if (result.code === 200 && result.data) {
            const skills = result.data.skills || result.data.items || [];
            skills.forEach(skill => {
                skill.discoverySource = source;
                skill.discoveryTime = Date.now();
            });
            return skills;
        }
    } catch (error) {
        console.error('[SkillDiscovery] 扫描错误:', error);
    }
    
    return [];
}

function addRadarDot() {
    const container = document.getElementById('radarContainer');
    if (!container) return;
    
    const dot = document.createElement('div');
    dot.className = 'radar-dot';
    
    const angle = Math.random() * 360;
    const distance = 20 + Math.random() * 70;
    const x = 100 + distance * Math.cos(angle * Math.PI / 180) - 4;
    const y = 100 + distance * Math.sin(angle * Math.PI / 180) - 4;
    
    dot.style.left = x + 'px';
    dot.style.top = y + 'px';
    
    container.appendChild(dot);
    
    setTimeout(() => {
        dot.remove();
    }, 5000);
}

async function loadDiscoveryResults() {
    console.log('[SkillDiscovery] 加载发现结果');
    
    const tbody = document.getElementById('discoveryResultsBody');
    if (!tbody) return;
    
    tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">加载中...</td></tr>';
    
    try {
        const result = await utils.api.post('/skills/discovery/list', {
            pageNum: 1,
            pageSize: 50
        });
        
        if (result.code === 200 && result.data) {
            discoveryResults = result.data.skills || result.data.items || [];
            renderResults(discoveryResults);
            document.getElementById('statTotal').textContent = discoveryResults.length;
        } else {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">暂无数据</td></tr>';
        }
    } catch (error) {
        console.error('[SkillDiscovery] 加载结果错误:', error);
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">加载失败</td></tr>';
    }
}

function renderResults(results) {
    const tbody = document.getElementById('discoveryResultsBody');
    if (!tbody) return;
    
    if (!results || results.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">暂无发现结果</td></tr>';
        return;
    }
    
    tbody.innerHTML = results.map(skill => `
        <tr>
            <td>${skill.id || skill.skillId || '-'}</td>
            <td>${skill.name || '-'}</td>
            <td>
                <span class="nx-badge nx-badge--sm ${getSourceBadgeClass(skill.discoverySource)}">
                    ${getSourceName(skill.discoverySource)}
                </span>
            </td>
            <td>${skill.type || '-'}</td>
            <td>
                ${(skill.capabilities || []).slice(0, 3).map(cap => 
                    `<span class="nx-tag nx-tag--sm nx-tag--primary">${cap}</span>`
                ).join(' ')}
            </td>
            <td>${skill.discoveryTime ? utils.date.format(skill.discoveryTime) : '-'}</td>
            <td>
                <div class="action-buttons">
                    <button class="nx-btn nx-btn--primary nx-btn--sm" onclick="installSkill('${skill.id || skill.skillId}')">
                        <i class="ri-download-line"></i> 安装
                    </button>
                    <button class="nx-btn nx-btn--ghost nx-btn--sm" onclick="viewSkillDetail('${skill.id || skill.skillId}')">
                        <i class="ri-eye-line"></i> 详情
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function getSourceName(source) {
    const names = {
        'github': 'GitHub',
        'gitee': 'Gitee',
        'skill_center': '技能中心',
        'udp_broadcast': 'UDP广播',
        'local_fs': '本地文件',
        'mdns_dns_sd': 'mDNS',
        'dht_kademlia': 'DHT'
    };
    return names[source] || source || '技能中心';
}

function getSourceBadgeClass(source) {
    const classes = {
        'github': 'nx-badge--dark',
        'gitee': 'nx-badge--danger',
        'skill_center': 'nx-badge--primary',
        'udp_broadcast': 'nx-badge--warning',
        'local_fs': 'nx-badge--success',
        'mdns_dns_sd': 'nx-badge--info',
        'dht_kademlia': 'nx-badge--danger'
    };
    return classes[source] || '';
}

window.filterResults = async function() {
    const source = document.getElementById('filterSource')?.value || '';
    const type = document.getElementById('filterType')?.value || '';
    const keyword = document.getElementById('searchKeyword')?.value?.trim() || '';
    
    console.log('[SkillDiscovery] 筛选结果:', { source, type, keyword });
    
    try {
        const result = await utils.api.post('/skills/discovery/search', {
            pageNum: 1,
            pageSize: 50,
            sources: source ? [source] : null,
            types: type ? [type] : null,
            keywords: keyword ? [keyword] : null
        });
        
        if (result.code === 200 && result.data) {
            const filtered = result.data.skills || result.data.items || [];
            renderResults(filtered);
        }
    } catch (error) {
        console.error('[SkillDiscovery] 筛选错误:', error);
        utils.msg.error('筛选失败');
    }
};

window.installSkill = async function(skillId) {
    console.log('[SkillDiscovery] 安装技能:', skillId);
    
    try {
        const result = await utils.api.post('/skills/discovery/download', { skillId: skillId });
        
        if (result.code === 200 && result.data) {
            utils.msg.success('技能安装成功！');
            document.getElementById('statInstalled').textContent = 
                parseInt(document.getElementById('statInstalled').textContent) + 1;
        } else {
            utils.msg.error('安装失败: ' + (result.message || '未知错误'));
        }
    } catch (error) {
        console.error('[SkillDiscovery] 安装技能错误:', error);
        utils.msg.error('安装失败');
    }
};

window.viewSkillDetail = async function(skillId) {
    console.log('[SkillDiscovery] 查看技能详情:', skillId);
    
    try {
        const result = await utils.api.post('/skills/discovery/get', { skillId: skillId });
        
        if (result.code === 200 && result.data) {
            showSkillDetailModal(result.data);
        } else {
            utils.msg.error('获取技能详情失败');
        }
    } catch (error) {
        console.error('[SkillDiscovery] 获取技能详情错误:', error);
        utils.msg.error('获取技能详情失败');
    }
};

function showSkillDetailModal(skill) {
    const modal = document.getElementById('skillDetailModal');
    const content = document.getElementById('skillDetailContent');
    const installBtn = document.getElementById('installFromDetailBtn');
    
    if (!modal || !content) return;
    
    const metadata = skill.metadata || skill;
    
    content.innerHTML = `
        <div class="skill-detail">
            <h4 style="margin-bottom: 16px;">${metadata.name || skill.name || '未知技能'}</h4>
            <div class="nx-grid nx-grid--cols-2 nx-gap-4">
                <div><strong>技能ID:</strong> ${metadata.id || skill.id || '-'}</div>
                <div><strong>版本:</strong> ${metadata.version || skill.version || '-'}</div>
                <div><strong>类型:</strong> ${metadata.type || skill.type || '-'}</div>
                <div><strong>作者:</strong> ${metadata.author || skill.author || '-'}</div>
            </div>
            <div style="margin-top: 16px;">
                <strong>描述:</strong>
                <p style="margin-top: 8px; color: var(--nx-text-secondary);">
                    ${metadata.description || skill.description || '-'}
                </p>
            </div>
            <div style="margin-top: 16px;">
                <strong>能力标签:</strong>
                <div style="margin-top: 8px;">
                    ${(skill.capabilities || metadata.capabilities || []).map(cap => 
                        `<span class="nx-tag nx-tag--primary">${cap}</span>`
                    ).join(' ')}
                </div>
            </div>
            <div style="margin-top: 16px;">
                <strong>适用场景:</strong>
                <div style="margin-top: 8px;">
                    ${(skill.scenes || metadata.scenes || []).map(scene => 
                        `<span class="nx-tag nx-tag--info">${scene}</span>`
                    ).join(' ')}
                </div>
            </div>
        </div>
    `;
    
    if (installBtn) {
        installBtn.onclick = function() {
            installSkill(metadata.id || skill.id);
            closeSkillDetailModal();
        };
    }
    
    modal.style.display = 'flex';
}

window.closeSkillDetailModal = function() {
    const modal = document.getElementById('skillDetailModal');
    if (modal) modal.style.display = 'none';
};

function updateStats() {
    document.getElementById('statSources').textContent = `${activeSources.size}/7`;
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
        initInstallPage();
    });
} else {
    initInstallPage();
}
