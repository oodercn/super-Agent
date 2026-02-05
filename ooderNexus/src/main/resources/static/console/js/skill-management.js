class SkillManagement {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.skillList = [];
        this.filter = {
            category: '',
            keyword: '',
            status: 'all'
        };
    }

    async init() {
        try {
            await this.loadSkillList();
        } catch (error) {
            console.error('初始化技能管理失败:', error);
            // 使用默认值
            this.skillList = [];
            this.renderSkillList();
        }
        this.bindEvents();
    }

    async loadSkillList() {
        try {
            const skillList = document.getElementById('skill-list');
            skillList.innerHTML = '<div class="loading">加载中...</div>';
            
            try {
                // 尝试从API获取数据
                const response = await this.llmAPI.getRegisteredSkills(this.filter);
                if (response && (response.code === 200 || response.message === "success")) {
                    this.skillList = response.data || [];
                    this.renderSkillList();
                    return;
                }
            } catch (apiError) {
                console.warn('API调用失败，使用本地模拟数据:', apiError);
            }
            
            // 使用本地模拟数据
            const mockResponse = await fetch('/console/data/skills-data.json');
            if (mockResponse.ok) {
                const data = await mockResponse.json();
                this.skillList = data.skills || [];
                this.renderSkillList();
            } else {
                this.skillList = [];
                this.renderSkillList();
                this.showError('加载技能列表失败');
            }
        } catch (error) {
            console.error('加载技能列表失败:', error);
            this.skillList = [];
            this.renderSkillList();
            this.showError('加载技能列表失败');
        }
    }

    renderSkillList() {
        const skillList = document.getElementById('skill-list');
        let html = '';

        if (this.skillList.length === 0) {
            skillList.innerHTML = '<div class="empty-state">暂无技能</div>';
            return;
        }

        this.skillList.forEach(skill => {
            html += `<div class="skill-item" data-skill-id="${skill.skillId}">`;
            html += `<div class="skill-icon">`;
            html += `<i class="${skill.icon || 'ri-code-line'}"></i>`;
            html += `</div>`;
            html += `<div class="skill-info">`;
            html += `<h3 class="skill-name">${skill.skillName}</h3>`;
            html += `<p class="skill-description">${skill.description}</p>`;
            html += `<div class="skill-meta">`;
            html += `<span class="skill-version">版本：${skill.skillVersion}</span>`;
            html += `<span class="skill-status">状态：${this.getStatusText(skill.status)}</span>`;
            html += `<span class="skill-author">作者：${skill.author}</span>`;
            html += `</div>`;
            html += `</div>`;
            html += `<div class="skill-actions">`;
            html += `<button class="btn btn-sm btn-primary" onclick="skillManagement.executeSkill('${skill.skillId}')">`;
            html += `<i class="ri-play-line"></i>`;
            html += `执行</button>`;
            html += `<button class="btn btn-sm btn-secondary" onclick="skillManagement.editSkill('${skill.skillId}')">`;
            html += `<i class="ri-edit-line"></i>`;
            html += `编辑</button>`;
            html += `<button class="btn btn-sm btn-danger" onclick="skillManagement.deleteSkill('${skill.skillId}')">`;
            html += `<i class="ri-delete-bin-line"></i>`;
            html += `删除</button>`;
            html += `</div>`;
            html += `</div>`;
        });

        skillList.innerHTML = html;
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
    }

    async executeSkill(skillId) {
        try {
            const response = await this.llmAPI.executeSkill(skillId, {});
            if (response && (response.code === 200 || response.message === "success")) {
                this.showSuccess('技能执行成功');
            } else {
                this.showError('技能执行失败');
            }
        } catch (error) {
            console.error('执行技能失败:', error);
            this.showError('执行技能失败，请检查网络连接');
        }
    }

    async editSkill(skillId) {
        const skill = this.skillList.find(s => s.skillId === skillId);
        if (skill) {
            this.showEditSkillModal(skill);
        }
    }

    async deleteSkill(skillId) {
        if (confirm('确定要删除该技能吗？')) {
            try {
                const response = await this.llmAPI.deleteSkill(skillId);
                if (response && (response.code === 200 || response.message === "success")) {
                    this.showSuccess('技能删除成功');
                    await this.loadSkillList();
                } else {
                    this.showError('技能删除失败');
                }
            } catch (error) {
                console.error('删除技能失败:', error);
                this.showError('删除技能失败，请检查网络连接');
            }
        }
    }

    getStatusText(status) {
        const statusMap = {
            'ACTIVE': '启用',
            'DISABLED': '禁用',
            'ERROR': '错误'
        };
        return statusMap[status] || status;
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

    showEditSkillModal(skill) {
        alert('编辑功能开发中...');
    }

    loadPage(page) {
        window.location.href = `/console/pages/llm-integration/${page}.html`;
    }
}

let skillManagement;
document.addEventListener('DOMContentLoaded', () => {
    skillManagement = new SkillManagement();
    skillManagement.init();
});

function showRegisterSkillModal() {
    document.getElementById('register-skill-modal').style.display = 'flex';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

async function registerSkill() {
    try {
        const skillData = {
            skillName: document.getElementById('skill-name').value,
            skillVersion: document.getElementById('skill-version').value,
            description: document.getElementById('skill-description').value,
            category: document.getElementById('skill-category').value,
            author: document.getElementById('skill-author').value,
            icon: document.getElementById('skill-icon').value,
            tags: []
        };

        // 表单验证
        if (!skillData.skillName || !skillData.skillVersion || !skillData.description) {
            skillManagement.showError('请填写必填字段');
            return;
        }

        const response = await skillManagement.llmAPI.registerSkill(skillData);
        if (response && (response.code === 200 || response.message === "success")) {
            skillManagement.showSuccess('技能注册成功');
            closeModal('register-skill-modal');
            await skillManagement.loadSkillList();
        } else {
            skillManagement.showError('技能注册失败');
        }
    } catch (error) {
        console.error('注册技能失败:', error);
        skillManagement.showError('注册技能失败，请检查网络连接');
    }
}

async function refreshSkillList() {
    if (skillManagement) {
        await skillManagement.loadSkillList();
    } else {
        alert('页面未完全加载，请刷新后重试');
    }
}
