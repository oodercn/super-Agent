class SkillMarket {
    constructor() {
        this.skillList = [];
        this.categories = [];
        this.filter = {
            category: '',
            keyword: ''
        };
        this.currentPreviewSkill = null;
        
        // 分类色系配置
        this.categoryColors = {
            '网络管理': { color: '#3b82f6', light: '#60a5fa', bg: 'rgba(59, 130, 246, 0.1)' },
            '家庭安全': { color: '#10b981', light: '#34d399', bg: 'rgba(16, 185, 129, 0.1)' },
            '网络优化': { color: '#f59e0b', light: '#fbbf24', bg: 'rgba(245, 158, 11, 0.1)' },
            '网络安全': { color: '#ef4444', light: '#f87171', bg: 'rgba(239, 68, 68, 0.1)' },
            '设备管理': { color: '#8b5cf6', light: '#a78bfa', bg: 'rgba(139, 92, 246, 0.1)' },
            '网络监控': { color: '#06b6d4', light: '#22d3ee', bg: 'rgba(6, 182, 212, 0.1)' },
            '网络工具': { color: '#64748b', light: '#94a3b8', bg: 'rgba(100, 116, 139, 0.1)' },
            '系统核心': { color: '#ec4899', light: '#f472b6', bg: 'rgba(236, 72, 153, 0.1)' },
            '智能家居': { color: '#14b8a6', light: '#2dd4bf', bg: 'rgba(20, 184, 166, 0.1)' }
        };
    }

    async init() {
        try {
            await this.loadSkillMarket();
        } catch (error) {
            console.error('初始化技能市场失败:', error);
            this.skillList = [];
            this.renderSkillMarket();
        }
        this.bindEvents();
    }

    async loadSkillMarket() {
        try {
            const skillMarketGrid = document.getElementById('skill-market-grid');
            skillMarketGrid.innerHTML = `
                <div class="loading">
                    <i class="ri-loader-4-line ri-spin" style="font-size: 32px;"></i>
                    <p>加载中...</p>
                </div>
            `;
            
            // 从本地JSON文件加载技能数据
            const response = await fetch('/console/data/skill-market-data.json');
            const data = await response.json();
            
            if (data && data.code === 200 && data.data) {
                this.skillList = data.data;
                // 更新统计信息
                this.updateStats();
                // 应用筛选
                this.applyFilter();
            } else {
                this.skillList = [];
                this.renderSkillMarket();
            }
        } catch (error) {
            console.error('加载技能市场失败:', error);
            this.skillList = [];
            this.renderSkillMarket();
            this.showError('加载技能市场失败，请检查网络连接');
        }
    }

    updateStats() {
        const totalSkills = this.skillList.length;
        const totalDownloads = this.skillList.reduce((sum, skill) => {
            return sum + (skill.metadata?.downloads || 0);
        }, 0);
        const avgRating = totalSkills > 0 ? 
            (this.skillList.reduce((sum, skill) => sum + (skill.metadata?.rating || 0), 0) / totalSkills).toFixed(1) : 
            '0.0';
        
        document.getElementById('total-skills').textContent = totalSkills;
        document.getElementById('total-downloads').textContent = this.formatNumber(totalDownloads);
        document.getElementById('avg-rating').textContent = avgRating;
    }

    applyFilter() {
        let filteredSkills = this.skillList;
        
        // 分类筛选
        if (this.filter.category) {
            filteredSkills = filteredSkills.filter(skill => skill.category === this.filter.category);
        }
        
        // 关键词搜索
        if (this.filter.keyword) {
            const keyword = this.filter.keyword.toLowerCase();
            filteredSkills = filteredSkills.filter(skill => 
                skill.skillName.toLowerCase().includes(keyword) ||
                skill.description.toLowerCase().includes(keyword) ||
                skill.tags?.some(tag => tag.toLowerCase().includes(keyword))
            );
        }
        
        this.renderSkillMarket(filteredSkills);
    }

    renderSkillMarket(skills = this.skillList) {
        const skillMarketGrid = document.getElementById('skill-market-grid');
        
        if (skills.length === 0) {
            skillMarketGrid.innerHTML = `
                <div class="empty-state" style="grid-column: 1 / -1;">
                    <i class="ri-store-3-line"></i>
                    <h3>暂无技能</h3>
                    <p>没有找到匹配的技能，请尝试其他搜索条件</p>
                </div>
            `;
            return;
        }

        let html = '';
        skills.forEach(skill => {
            const meta = skill.metadata || {};
            const tags = meta.tags || [];
            const categoryColor = this.categoryColors[skill.category] || this.categoryColors['网络管理'];
            
            html += `
                <div class="skill-card" data-skill-id="${skill.skillId}" data-category="${skill.category}">
                    <div class="skill-card-header">
                        <div class="skill-icon">
                            <i class="${meta.icon || 'ri-code-line'}"></i>
                        </div>
                        <div class="skill-info">
                            <div class="skill-name">${skill.skillName}</div>
                            <span class="skill-category">${skill.category}</span>
                        </div>
                    </div>
                    <div class="skill-description">${skill.description}</div>
                    <div class="skill-meta">
                        <span><i class="ri-user-line"></i> ${meta.author || '未知'}</span>
                        <span><i class="ri-star-fill"></i> ${meta.rating || 0}</span>
                        <span><i class="ri-download-line"></i> ${this.formatNumber(meta.downloads || 0)}</span>
                        <span><i class="ri-code-box-line"></i> ${meta.version || '1.0.0'}</span>
                    </div>
                    <div class="skill-tags">
                        ${tags.slice(0, 3).map(tag => `<span class="skill-tag">${tag}</span>`).join('')}
                    </div>
                    <div class="skill-actions">
                        <button class="btn btn-secondary" onclick="skillMarket.previewSkill('${skill.skillId}')">
                            <i class="ri-eye-line"></i> 预览
                        </button>
                        <button class="btn btn-primary" onclick="skillMarket.installSkill('${skill.skillId}')">
                            <i class="ri-download-line"></i> 安装
                        </button>
                    </div>
                </div>
            `;
        });

        skillMarketGrid.innerHTML = html;
    }

    formatNumber(num) {
        if (num >= 10000) {
            return (num / 10000).toFixed(1) + 'w';
        } else if (num >= 1000) {
            return (num / 1000).toFixed(1) + 'k';
        }
        return num.toString();
    }

    bindEvents() {
        // 绑定搜索事件
        const searchInput = document.getElementById('search-input');
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                this.filter.keyword = e.target.value;
                this.applyFilter();
            });
        }

        // 绑定分类筛选事件
        const categoryFilter = document.getElementById('category-filter');
        if (categoryFilter) {
            categoryFilter.addEventListener('change', (e) => {
                this.filter.category = e.target.value;
                this.applyFilter();
            });
        }
    }

    previewSkill(skillId) {
        const skill = this.skillList.find(s => s.skillId === skillId);
        if (!skill) return;

        this.currentPreviewSkill = skill;
        const meta = skill.metadata || {};
        const categoryColor = this.categoryColors[skill.category] || this.categoryColors['网络管理'];

        // 更新弹窗内容
        document.getElementById('preview-skill-name').textContent = skill.skillName;
        document.getElementById('preview-skill-author').textContent = meta.author || '未知';
        document.getElementById('preview-skill-category').textContent = skill.category;
        document.getElementById('preview-skill-rating').textContent = (meta.rating || 0) + ' 分';
        document.getElementById('preview-skill-downloads').textContent = this.formatNumber(meta.downloads || 0) + ' 次';
        document.getElementById('preview-skill-description').textContent = skill.description;
        
        const iconEl = document.getElementById('preview-skill-icon');
        iconEl.className = meta.icon || 'ri-code-line';

        // 更新能力列表
        const capabilitiesList = document.getElementById('preview-skill-capabilities');
        const capabilities = meta.capabilities || [];
        capabilitiesList.innerHTML = capabilities.map(cap => `<li>${cap}</li>`).join('');

        // 设置弹窗颜色主题 - 通过设置CSS变量
        const modalContent = document.querySelector('#skill-preview-modal .modal-content');
        if (modalContent) {
            modalContent.style.setProperty('--skill-color', categoryColor.color);
            modalContent.style.setProperty('--skill-color-light', categoryColor.light);
            modalContent.style.setProperty('--skill-bg', categoryColor.bg);
        }

        // 显示弹窗
        openModal('skill-preview-modal');
    }

    installSkill(skillId) {
        const skill = this.skillList.find(s => s.skillId === skillId);
        if (!skill) return;

        // 模拟安装过程
        this.showNotification(`正在安装 ${skill.skillName}...`, 'info');
        
        setTimeout(() => {
            this.showNotification(`${skill.skillName} 安装成功！`, 'success');
        }, 1500);
    }

    showNotification(message, type = 'success') {
        // 移除现有通知
        const existingNotification = document.querySelector('.notification');
        if (existingNotification) {
            existingNotification.remove();
        }

        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        document.body.appendChild(notification);

        setTimeout(() => {
            notification.remove();
        }, 3000);
    }

    showError(message) {
        this.showNotification(message, 'error');
    }
}

// 全局函数，供HTML调用
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('active');
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('active');
    }
}

function installSkillFromPreview() {
    if (skillMarket.currentPreviewSkill) {
        skillMarket.installSkill(skillMarket.currentPreviewSkill.skillId);
        closeModal('skill-preview-modal');
    }
}

function refreshSkillMarket() {
    skillMarket.loadSkillMarket();
}

// 初始化
const skillMarket = new SkillMarket();

// 等待DOM加载完成后初始化
document.addEventListener('DOMContentLoaded', () => {
    skillMarket.init();
});