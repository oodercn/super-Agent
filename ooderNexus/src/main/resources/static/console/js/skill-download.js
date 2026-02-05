class SkillDownload {
    constructor() {
        this.syncAPI = new SkillCenterSyncAPI();
        this.skillList = [];
        this.filter = {
            category: '',
            keyword: ''
        };
    }

    async init() {
        await this.loadSkillList();
        this.bindEvents();
    }

    async loadSkillList() {
        const response = await this.syncAPI.getSkillList(this.filter.category, 0, 50);
        if (response.code === 200 || response.message === "success") {
            this.skillList = response.data;
            this.renderSkillList();
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
            html += `<div class="skill-item">`;
            html += `<div class="skill-icon">`;
            html += `<i class="${skill.icon || 'ri-file-line'}"></i>`;
            html += `</div>`;
            html += `<div class="skill-info">`;
            html += `<h3 class="skill-name">${skill.skillName}</h3>`;
            html += `<p class="skill-description">${skill.description}</p>`;
            html += `<div class="skill-meta">`;
            html += `<span class="skill-version">版本：${skill.version}</span>`;
            html += `<span class="skill-author">作者：${skill.author || '未知'}</span>`;
            html += `<span class="skill-rating">评分：${skill.rating}</span>`;
            html += `</div>`;
            html += `</div>`;
            html += `<div class="skill-actions">`;
            html += `<button class="btn btn-primary" onclick="skillDownload.downloadSkill('${skill.skillId}')">`;
            html += `<i class="ri-download-line"></i>`;
            html += `下载</button>`;
            html += `</div>`;
            html += `</div>`;
        });

        skillList.innerHTML = html;
    }

    bindEvents() {
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => {
                document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
                e.currentTarget.classList.add('active');
                this.loadPage(e.currentTarget.dataset.page);
            });
        });

        document.getElementById('search-input').addEventListener('input', (e) => {
            this.filter.keyword = e.target.value;
            this.loadSkillList();
        });

        document.getElementById('category-filter').addEventListener('change', (e) => {
            this.filter.category = e.target.value;
            this.loadSkillList();
        });
    }

    async downloadSkill(skillId) {
        try {
            const blob = await this.syncAPI.downloadSkill(skillId);
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `skill-${skillId}.zip`;
            a.click();
            URL.revokeObjectURL(url);
            
            this.showSuccess('技能下载成功');
        } catch (error) {
            this.showError('技能下载失败');
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
        window.location.href = `/console/pages/skillcenter-sync/${page}.html`;
    }
}

let skillDownload;
document.addEventListener('DOMContentLoaded', () => {
    skillDownload = new SkillDownload();
    skillDownload.init();
});

async function refreshSkillList() {
    await skillDownload.loadSkillList();
}
