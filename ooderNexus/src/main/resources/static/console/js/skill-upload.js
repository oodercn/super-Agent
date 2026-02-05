class SkillUpload {
    constructor() {
        this.syncAPI = new SkillCenterSyncAPI();
        this.uploadHistory = [];
    }

    async init() {
        this.bindEvents();
    }

    bindEvents() {
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => {
                document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
                e.currentTarget.classList.add('active');
                this.loadPage(e.currentTarget.dataset.page);
            });
        });

        const dropZone = document.getElementById('upload-drop-zone');
        const fileInput = document.getElementById('file-input');

        dropZone.addEventListener('dragover', (e) => {
            e.preventDefault();
            dropZone.classList.add('drag-over');
        });

        dropZone.addEventListener('dragleave', (e) => {
            e.preventDefault();
            dropZone.classList.remove('drag-over');
        });

        dropZone.addEventListener('drop', (e) => {
            e.preventDefault();
            dropZone.classList.remove('drag-over');
            const files = e.dataTransfer.files;
            this.handleFiles(files);
        });

        fileInput.addEventListener('change', (e) => {
            const files = e.target.files;
            this.handleFiles(files);
        });
    }

    async handleFiles(files) {
        for (const file of files) {
            await this.uploadFile(file);
        }
    }

    async uploadFile(file) {
        const metadata = {
            name: file.name,
            size: file.size,
            type: file.type
        };

        this.showUploadProgress(0);
        
        try {
            const response = await this.syncAPI.uploadSkill(file, metadata);
            
            if ((response.code === 200 || response.message === "success") && (response.data && response.data.success)) {
                this.showSuccess(`技能 ${file.name} 上传成功`);
                this.addUploadHistory(response.data);
            } else {
                this.showError(`技能 ${file.name} 上传失败`);
            }
        } catch (error) {
            this.showError(`技能 ${file.name} 上传异常：${error.message}`);
        }
        
        this.hideUploadProgress();
    }

    showUploadProgress(percent) {
        const progressDiv = document.getElementById('upload-progress');
        const progressFill = progressDiv.querySelector('.progress-fill');
        const progressText = progressDiv.querySelector('.progress-text');
        
        progressDiv.style.display = 'block';
        progressFill.style.width = `${percent}%`;
        progressText.textContent = `${percent}%`;
    }

    hideUploadProgress() {
        document.getElementById('upload-progress').style.display = 'none';
    }

    addUploadHistory(result) {
        this.uploadHistory.unshift(result);
        this.renderUploadHistory();
    }

    renderUploadHistory() {
        const historyList = document.getElementById('upload-history-list');
        let html = '';

        if (this.uploadHistory.length === 0) {
            historyList.innerHTML = '<div class="empty-state">暂无上传历史</div>';
            return;
        }

        this.uploadHistory.forEach(item => {
            html += `<div class="history-item">`;
            html += `<div class="history-info">`;
            html += `<i class="ri-file-line"></i>`;
            html += `<span class="history-name">${item.skillName}</span>`;
            html += `<span class="history-version">${item.version}</span>`;
            html += `</div>`;
            html += `<div class="history-status ${item.success ? 'success' : 'error'}">`;
            html += item.success ? '成功' : '失败';
            html += `</div>`;
            html += `</div>`;
        });

        historyList.innerHTML = html;
    }

    clearUploadHistory() {
        if (confirm('确定要清除上传历史吗？')) {
            this.uploadHistory = [];
            this.renderUploadHistory();
            this.showSuccess('上传历史已清除');
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

let skillUpload;
document.addEventListener('DOMContentLoaded', () => {
    skillUpload = new SkillUpload();
    skillUpload.init();
});

function clearUploadHistory() {
    skillUpload.clearUploadHistory();
}
