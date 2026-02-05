class SkillMarket {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.skillList = [];
        this.categories = [];
        this.filter = {
            category: '',
            keyword: ''
        };
        this.currentPreviewSkill = null;
    }

    async init() {
        try {
            await this.loadSkillMarket();
        } catch (error) {
            console.error('初始化技能市场失败:', error);
            // 使用默认值
            this.skillList = [];
            this.renderSkillMarket();
        }
        this.bindEvents();
    }

    async loadSkillMarket() {
        try {
            const skillMarketGrid = document.getElementById('skill-market-grid');
            skillMarketGrid.innerHTML = '<div class="loading">加载中...</div>';
            
            const response = await this.llmAPI.discoverSkills(this.filter.category, this.filter.keyword);
            if (response && (response.code === 200 || response.message === "success")) {
                this.skillList = response.data || [];
                this.renderSkillMarket();
            } else {
                this.skillList = [];
                this.renderSkillMarket();
                this.showError('加载技能市场失败');
            }
        } catch (error) {
            console.error('加载技能市场失败:', error);
            this.skillList = [];
            this.renderSkillMarket();
            this.showError('加载技能市场失败');
        }
    }

    renderSkillMarket() {
        const skillMarketGrid = document.getElementById('skill-market-grid');
        let html = '';

        if (this.skillList.length === 0) {
            skillMarketGrid.innerHTML = '<div class="empty-state">暂无技能</div>';
            return;
        }

        this.skillList.forEach(skill => {
            html += `<div class="skill-card" data-skill-id="${skill.skillId}">`;
            html += `<div class="skill-card-icon">`;
            html += `<i class="${skill.metadata?.icon || 'ri-code-line'}"></i>`;
            html += `</div>`;
            html += `<div class="skill-card-info">`;
            html += `<h3 class="skill-name">${skill.skillName}</h3>`;
            html += `<p class="skill-description">${skill.description}</p>`;
            html += `<div class="skill-card-meta">`;
            html += `<span class="skill-category">分类：${skill.category}</span>`;
            html += `<span class="skill-author">作者：${skill.metadata?.author || '未知'}</span>`;
            html += `<span class="skill-rating">评分：${skill.metadata?.rating || 0}</span>`;
            html += `</div>`;
            html += `</div>`;
            html += `<div class="skill-card-actions">`;
            html += `<button class="btn btn-primary" onclick="skillMarket.previewSkill('${skill.skillId}')">`;
            html += `<i class="ri-eye-line"></i>`;
            html += `预览</button>`;
            html += `<button class="btn btn-secondary" onclick="skillMarket.installSkill('${skill.skillId}')">`;
            html += `<i class="ri-download-line"></i>`;
            html += `安装</button>`;
            html += `</div>`;
            html += `</div>`;
        });

        skillMarketGrid.innerHTML = html;
    }

    bindEvents() {
        // 绑定菜单事件
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => {
                document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
                e.currentTarget.classList.add('active');
                this.loadPage(e.currentTarget.dataset.page);
            });
        });

        // 绑定搜索事件
        const searchInput = document.getElementById('search-input');
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                this.filter.keyword = e.target.value;
                this.loadSkillMarket();
            });
        }

        // 绑定分类筛选事件
        const categoryFilter = document.getElementById('category-filter');
        if (categoryFilter) {
            categoryFilter.addEventListener('change', (e) => {
                this.filter.category = e.target.value;
                this.loadSkillMarket();
            });
        }
    }

    async installSkill(skillId) {
        try {
            const skill = this.skillList.find(s => s.skillId === skillId);
            if (!skill) {
                this.showError('技能不存在');
                return;
            }

            const response = await this.llmAPI.registerSkill({
                skillName: skill.skillName,
                skillVersion: skill.metadata?.version || '1.0.0',
                description: skill.description,
                category: skill.category,
                author: skill.metadata?.author || 'Market',
                icon: skill.metadata?.icon || 'ri-code-line',
                tags: []
            });
            if (response && (response.code === 200 || response.message === "success")) {
                this.showSuccess('技能安装成功');
            } else {
                this.showError('技能安装失败');
            }
        } catch (error) {
            console.error('安装技能失败:', error);
            this.showError('安装技能失败，请检查网络连接');
        }
    }

    async previewSkill(skillId) {
        try {
            const skill = this.skillList.find(s => s.skillId === skillId);
            if (skill) {
                this.currentPreviewSkill = skill;
                document.getElementById('preview-skill-name').textContent = skill.skillName;
                document.getElementById('preview-skill-description').textContent = skill.description;
                document.getElementById('preview-skill-version').textContent = skill.metadata?.version || '1.0.0';
                document.getElementById('preview-skill-author').textContent = skill.metadata?.author || '未知';
                document.getElementById('preview-skill-category').textContent = skill.category;
                document.getElementById('preview-skill-icon').className = skill.metadata?.icon || 'ri-code-line';
                document.getElementById('skill-preview-modal').style.display = 'flex';
            } else {
                this.showError('技能不存在');
            }
        } catch (error) {
            console.error('预览技能失败:', error);
            this.showError('预览技能失败');
        }
    }

    showSuccess(message) {
        this.showNotification(message, 'success');
    }

    showError(message) {
        this.showNotification(message, 'error');
    }

    showNotification(message, type) {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        document.body.appendChild(notification);
        setTimeout(() => notification.remove(), 3000);
    }

    loadPage(page) {
        window.location.href = `/console/pages/llm-integration/${page}.html`;
    }
}

let skillMarket;
document.addEventListener('DOMContentLoaded', () => {
    skillMarket = new SkillMarket();
    skillMarket.init();
});

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'none';
    }
}

async function refreshSkillMarket() {
    if (skillMarket) {
        await skillMarket.loadSkillMarket();
    } else {
        alert('页面未完全加载，请刷新后重试');
    }
}

async function installSkillFromPreview() {
    if (skillMarket && skillMarket.currentPreviewSkill) {
        await skillMarket.installSkill(skillMarket.currentPreviewSkill.skillId);
        closeModal('skill-preview-modal');
    }
}
