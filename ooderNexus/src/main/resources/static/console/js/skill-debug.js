class SkillDebug {
    constructor() {
        this.llmAPI = new LLMIntegrationAPI();
        this.skillList = [];
        this.currentSkill = null;
        this.logs = [];
    }

    async init() {
        await this.loadSkillList();
        this.bindEvents();
    }

    async loadSkillList() {
        const response = await this.llmAPI.getRegisteredSkills({});
        if (response && (response.code === 200 || response.message === "success")) {
            this.skillList = response.data || [];
            this.renderSkillList();
            this.renderSkillSelect();
        } else {
            this.addLog('error', '加载技能列表失败');
            const debugSkillList = document.getElementById('debug-skill-list');
            debugSkillList.innerHTML = '<div class="log-empty">加载失败</div>';
        }
    }

    renderSkillList() {
        const debugSkillList = document.getElementById('debug-skill-list');
        let html = '';

        if (this.skillList.length === 0) {
            debugSkillList.innerHTML = '<div class="log-empty">暂无技能</div>';
            return;
        }

        this.skillList.forEach(skill => {
            html += `<div class="debug-skill-item" data-skill-id="${skill.skillId}">`;
            html += `<i class="${skill.icon || 'ri-code-line'}"></i>`;
            html += `<span class="debug-skill-name">${skill.skillName}</span>`;
            html += `</div>`;
        });

        debugSkillList.innerHTML = html;
    }

    renderSkillSelect() {
        const debugSkillSelect = document.getElementById('debug-skill-select');
        let html = '<option value="">选择技能</option>';

        this.skillList.forEach(skill => {
            html += `<option value="${skill.skillId}">${skill.skillName}</option>`;
        });

        debugSkillSelect.innerHTML = html;
    }

    bindEvents() {
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => {
                document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
                e.currentTarget.classList.add('active');
                this.loadPage(e.currentTarget.dataset.page);
            });
        });

        document.querySelectorAll('.debug-skill-item').forEach(item => {
            item.addEventListener('click', (e) => {
                const skillId = e.currentTarget.dataset.skillId;
                document.getElementById('debug-skill-select').value = skillId;
            });
        });
    }

    async testSkill() {
        const skillId = document.getElementById('debug-skill-select').value;
        const params = document.getElementById('debug-params').value;

        if (!skillId) {
            this.showError('请选择技能');
            return;
        }

        this.addLog('info', `开始测试技能：${skillId}`);

        try {
            const response = await this.llmAPI.testSkill(skillId, { params });
            if (response.code === 200 || response.message === "success") {
                this.addLog('info', `技能测试成功：${response.data.result}`);
                this.showSuccess('技能测试成功');
            } else {
                this.addLog('error', `技能测试失败：${response.message}`);
                this.showError('技能测试失败');
            }
        } catch (error) {
            this.addLog('error', `技能测试异常：${error.message}`);
            this.showError('技能测试异常');
        }
    }

    addLog(level, message) {
        const logEntry = {
            timestamp: Date.now(),
            level: level,
            message: message
        };
        this.logs.unshift(logEntry);
        this.renderLogs();
    }

    renderLogs() {
        const debugLog = document.getElementById('debug-log');
        let html = '';

        if (this.logs.length === 0) {
            debugLog.innerHTML = '<div class="log-empty">暂无日志</div>';
            return;
        }

        this.logs.forEach(log => {
            const levelClass = `log-${log.level}`;
            const time = new Date(log.timestamp).toLocaleTimeString();
            html += `<div class="log-entry ${levelClass}">`;
            html += `<span class="log-time">[${time}]</span>`;
            html += `<span class="log-message">${log.message}</span>`;
            html += `</div>`;
        });

        debugLog.innerHTML = html;
    }

    async clearLogs() {
        if (confirm('确定要清除所有日志吗？')) {
            this.logs = [];
            this.renderLogs();
            this.showSuccess('日志已清除');
        }
    }

    async exportLogs() {
        const logText = this.logs.map(log => 
            `[${new Date(log.timestamp).toLocaleString()}] [${log.level.toUpperCase()}] ${log.message}`
        ).join('\n');

        const blob = new Blob([logText], { type: 'text/plain' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `debug-logs-${Date.now()}.txt`;
        a.click();
        URL.revokeObjectURL(url);

        this.showSuccess('日志导出成功');
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

let skillDebug;
document.addEventListener('DOMContentLoaded', () => {
    skillDebug = new SkillDebug();
    skillDebug.init();
});

async function testSkill() {
    await skillDebug.testSkill();
}

async function clearLogs() {
    await skillDebug.clearLogs();
}

async function exportLogs() {
    await skillDebug.exportLogs();
}
