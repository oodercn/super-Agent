class InstallProgressComponent {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.progress = 0;
        this.status = 'idle';
        this.message = '';
        this.skillId = null;
        this.onComplete = null;
        this.onError = null;
    }

    start(skillId, skillName) {
        this.skillId = skillId;
        this.progress = 0;
        this.status = 'installing';
        this.message = `Installing ${skillName}...`;
        this.render();
        this.pollProgress();
    }

    pollProgress() {
        if (this.status !== 'installing') return;

        fetch('/api/skillcenter/installed/progress', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ skillId: this.skillId })
        })
        .then(response => response.json())
        .then(data => {
            if (data.requestStatus === 200 && data.data) {
                this.progress = data.data.progress || this.progress + 10;
                this.message = data.data.message || this.message;
                this.status = data.data.status || this.status;
                this.render();

                if (this.status === 'completed') {
                    this.handleComplete();
                } else if (this.status === 'failed') {
                    this.handleError(data.data.error || 'Installation failed');
                } else {
                    setTimeout(() => this.pollProgress(), 1000);
                }
            } else {
                this.progress = Math.min(this.progress + 10, 90);
                this.render();
                setTimeout(() => this.pollProgress(), 1000);
            }
        })
        .catch(error => {
            console.error('Progress poll error:', error);
            this.progress = Math.min(this.progress + 10, 90);
            this.render();
            setTimeout(() => this.pollProgress(), 1000);
        });
    }

    handleComplete() {
        this.progress = 100;
        this.status = 'completed';
        this.message = 'Installation completed successfully!';
        this.render();
        if (this.onComplete) {
            setTimeout(() => this.onComplete(this.skillId), 1500);
        }
    }

    handleError(error) {
        this.status = 'failed';
        this.message = error;
        this.render();
        if (this.onError) {
            setTimeout(() => this.onError(error), 1500);
        }
    }

    render() {
        if (!this.container) return;

        const statusClass = this.getStatusClass();
        const statusIcon = this.getStatusIcon();

        this.container.innerHTML = `
            <div class="install-progress-container">
                <div class="install-progress-header">
                    <span class="install-progress-icon ${statusClass}">
                        <i class="${statusIcon}"></i>
                    </span>
                    <span class="install-progress-message">${this.message}</span>
                </div>
                <div class="install-progress-bar-wrapper">
                    <div class="install-progress-bar ${statusClass}" style="width: ${this.progress}%"></div>
                </div>
                <div class="install-progress-footer">
                    <span class="install-progress-percent">${this.progress}%</span>
                    <span class="install-progress-status ${statusClass}">${this.status.toUpperCase()}</span>
                </div>
            </div>
        `;
    }

    getStatusClass() {
        switch (this.status) {
            case 'completed': return 'status-success';
            case 'failed': return 'status-error';
            case 'installing': return 'status-active';
            default: return 'status-idle';
        }
    }

    getStatusIcon() {
        switch (this.status) {
            case 'completed': return 'ri-check-line';
            case 'failed': return 'ri-error-warning-line';
            case 'installing': return 'ri-loader-4-line ri-spin';
            default: return 'ri-time-line';
        }
    }

    reset() {
        this.progress = 0;
        this.status = 'idle';
        this.message = '';
        this.skillId = null;
        this.render();
    }
}

window.InstallProgressComponent = InstallProgressComponent;
