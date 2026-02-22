class DependencyListComponent {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.dependencies = [];
        this.skillId = null;
        this.onInstallDependency = null;
    }

    load(skillId) {
        this.skillId = skillId;
        this.fetchDependencies();
    }

    fetchDependencies() {
        if (!this.skillId) return;

        fetch('/api/skillcenter/installed/dependencies', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ skillId: this.skillId })
        })
        .then(response => response.json())
        .then(data => {
            if (data.requestStatus === 200 && data.data) {
                this.dependencies = data.data.dependencies || [];
                this.render();
            } else {
                this.dependencies = [];
                this.render();
            }
        })
        .catch(error => {
            console.error('Failed to fetch dependencies:', error);
            this.dependencies = [];
            this.render();
        });
    }

    installDependency(depId) {
        if (this.onInstallDependency) {
            this.onInstallDependency(depId);
        }
    }

    render() {
        if (!this.container) return;

        if (this.dependencies.length === 0) {
            this.container.innerHTML = `
                <div class="dependency-list-empty">
                    <i class="ri-checkbox-circle-line"></i>
                    <span>No dependencies required</span>
                </div>
            `;
            return;
        }

        const satisfiedCount = this.dependencies.filter(d => d.installed).length;
        const totalCount = this.dependencies.length;

        this.container.innerHTML = `
            <div class="dependency-list-container">
                <div class="dependency-list-header">
                    <span class="dependency-list-title">
                        <i class="ri-git-branch-line"></i>
                        Dependencies
                    </span>
                    <span class="dependency-list-count">
                        ${satisfiedCount}/${totalCount} satisfied
                    </span>
                </div>
                <div class="dependency-list-items">
                    ${this.dependencies.map(dep => this.renderDependencyItem(dep)).join('')}
                </div>
            </div>
        `;
    }

    renderDependencyItem(dep) {
        const statusClass = dep.installed ? 'status-installed' : 'status-missing';
        const statusIcon = dep.installed ? 'ri-checkbox-circle-fill' : 'ri-error-warning-fill';
        const statusText = dep.installed ? 'Installed' : 'Missing';

        return `
            <div class="dependency-item ${statusClass}">
                <div class="dependency-item-info">
                    <span class="dependency-item-icon">
                        <i class="${statusIcon}"></i>
                    </span>
                    <div class="dependency-item-details">
                        <span class="dependency-item-name">${dep.name || dep.skillId}</span>
                        <span class="dependency-item-version">v${dep.version || 'latest'}</span>
                    </div>
                </div>
                <div class="dependency-item-actions">
                    <span class="dependency-item-status ${statusClass}">${statusText}</span>
                    ${!dep.installed ? `
                        <button class="dependency-install-btn" onclick="dependencyList.installDependency('${dep.skillId}')">
                            <i class="ri-download-line"></i>
                            Install
                        </button>
                    ` : ''}
                </div>
            </div>
        `;
    }

    checkDependencies(skillId, callback) {
        fetch('/api/skillcenter/installed/dependencies/check', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ skillId: skillId })
        })
        .then(response => response.json())
        .then(data => {
            if (callback) {
                callback(data.requestStatus === 200 && data.data && data.data.allSatisfied);
            }
        })
        .catch(error => {
            console.error('Dependency check failed:', error);
            if (callback) callback(false);
        });
    }
}

window.DependencyListComponent = DependencyListComponent;
