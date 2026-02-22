class SkillLifecycleControl {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.skillId = null;
        this.status = 'STOPPED';
        this.onStatusChange = null;
    }

    load(skillId) {
        this.skillId = skillId;
        this.fetchStatus();
    }

    fetchStatus() {
        if (!this.skillId) return;

        fetch('/api/skillcenter/installed/get', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: this.skillId })
        })
        .then(response => response.json())
        .then(data => {
            if (data.requestStatus === 200 && data.data) {
                this.status = data.data.status || 'STOPPED';
                this.render();
            }
        })
        .catch(error => {
            console.error('Failed to fetch skill status:', error);
        });
    }

    start() {
        this.performAction('start');
    }

    stop() {
        this.performAction('stop');
    }

    pause() {
        this.performAction('pause');
    }

    resume() {
        this.performAction('resume');
    }

    restart() {
        this.performAction('restart');
    }

    performAction(action) {
        if (!this.skillId) return;

        const endpoint = action === 'start' ? '/api/skillcenter/installed/run' :
                        action === 'stop' ? '/api/skillcenter/installed/stop' :
                        `/api/skillcenter/installed/${action}`;

        this.status = 'TRANSITIONING';
        this.render();

        fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: this.skillId })
        })
        .then(response => response.json())
        .then(data => {
            if (data.requestStatus === 200) {
                this.status = data.data?.status || this.getStatusAfterAction(action);
                this.render();
                if (this.onStatusChange) {
                    this.onStatusChange(this.status);
                }
            } else {
                this.status = 'ERROR';
                this.render();
            }
        })
        .catch(error => {
            console.error(`Failed to ${action} skill:`, error);
            this.status = 'ERROR';
            this.render();
        });
    }

    getStatusAfterAction(action) {
        switch (action) {
            case 'start':
            case 'resume':
                return 'RUNNING';
            case 'stop':
                return 'STOPPED';
            case 'pause':
                return 'PAUSED';
            case 'restart':
                return 'RUNNING';
            default:
                return this.status;
        }
    }

    render() {
        if (!this.container) return;

        const buttons = this.getAvailableActions();
        const statusClass = this.getStatusClass();
        const statusIcon = this.getStatusIcon();

        this.container.innerHTML = `
            <div class="lifecycle-control-container">
                <div class="lifecycle-status-display ${statusClass}">
                    <span class="lifecycle-status-icon">
                        <i class="${statusIcon}"></i>
                    </span>
                    <span class="lifecycle-status-text">${this.status}</span>
                </div>
                <div class="lifecycle-control-buttons">
                    ${buttons.map(btn => `
                        <button class="lifecycle-btn ${btn.class}" onclick="skillLifecycleControl.${btn.action}()" ${btn.disabled ? 'disabled' : ''}>
                            <i class="${btn.icon}"></i>
                            <span>${btn.label}</span>
                        </button>
                    `).join('')}
                </div>
            </div>
        `;
    }

    getAvailableActions() {
        const actions = [];

        if (this.status === 'STOPPED' || this.status === 'ERROR') {
            actions.push({
                action: 'start',
                label: 'Start',
                icon: 'ri-play-fill',
                class: 'btn-start',
                disabled: false
            });
        }

        if (this.status === 'RUNNING') {
            actions.push({
                action: 'pause',
                label: 'Pause',
                icon: 'ri-pause-fill',
                class: 'btn-pause',
                disabled: false
            });
            actions.push({
                action: 'stop',
                label: 'Stop',
                icon: 'ri-stop-fill',
                class: 'btn-stop',
                disabled: false
            });
        }

        if (this.status === 'PAUSED') {
            actions.push({
                action: 'resume',
                label: 'Resume',
                icon: 'ri-play-fill',
                class: 'btn-start',
                disabled: false
            });
            actions.push({
                action: 'stop',
                label: 'Stop',
                icon: 'ri-stop-fill',
                class: 'btn-stop',
                disabled: false
            });
        }

        if (this.status === 'TRANSITIONING') {
            actions.push({
                action: 'none',
                label: 'Processing...',
                icon: 'ri-loader-4-line ri-spin',
                class: 'btn-disabled',
                disabled: true
            });
        }

        return actions;
    }

    getStatusClass() {
        switch (this.status) {
            case 'RUNNING':
                return 'status-running';
            case 'PAUSED':
                return 'status-paused';
            case 'STOPPED':
                return 'status-stopped';
            case 'ERROR':
                return 'status-error';
            case 'TRANSITIONING':
                return 'status-transitioning';
            default:
                return 'status-unknown';
        }
    }

    getStatusIcon() {
        switch (this.status) {
            case 'RUNNING':
                return 'ri-play-circle-fill';
            case 'PAUSED':
                return 'ri-pause-circle-fill';
            case 'STOPPED':
                return 'ri-stop-circle-fill';
            case 'ERROR':
                return 'ri-error-warning-fill';
            case 'TRANSITIONING':
                return 'ri-loader-4-line ri-spin';
            default:
                return 'ri-question-line';
        }
    }
}

window.SkillLifecycleControl = SkillLifecycleControl;
