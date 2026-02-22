class CollaborativeSceneComponent {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.skillId = null;
        this.scenes = [];
        this.activeScene = null;
        this.onSceneJoin = null;
        this.onSceneLeave = null;
    }

    load(skillId) {
        this.skillId = skillId;
        this.fetchScenes();
    }

    fetchScenes() {
        if (!this.skillId) return;

        fetch('/api/skillcenter/installed/scenes', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ skillId: this.skillId })
        })
        .then(response => response.json())
        .then(data => {
            if (data.requestStatus === 200 && data.data) {
                this.scenes = data.data.scenes || [];
                this.activeScene = data.data.activeScene || null;
                this.render();
            } else {
                this.scenes = [];
                this.render();
            }
        })
        .catch(error => {
            console.error('Failed to fetch scenes:', error);
            this.scenes = [];
            this.render();
        });
    }

    joinScene(sceneId) {
        if (!this.skillId || !sceneId) return;

        fetch('/api/skillcenter/installed/scenes/join', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ skillId: this.skillId, sceneId: sceneId })
        })
        .then(response => response.json())
        .then(data => {
            if (data.requestStatus === 200) {
                this.activeScene = sceneId;
                this.render();
                if (this.onSceneJoin) {
                    this.onSceneJoin(sceneId);
                }
            }
        })
        .catch(error => {
            console.error('Failed to join scene:', error);
        });
    }

    leaveScene(sceneId) {
        if (!this.skillId || !sceneId) return;

        fetch('/api/skillcenter/installed/scenes/leave', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ skillId: this.skillId, sceneId: sceneId })
        })
        .then(response => response.json())
        .then(data => {
            if (data.requestStatus === 200) {
                if (this.activeScene === sceneId) {
                    this.activeScene = null;
                }
                this.render();
                if (this.onSceneLeave) {
                    this.onSceneLeave(sceneId);
                }
            }
        })
        .catch(error => {
            console.error('Failed to leave scene:', error);
        });
    }

    createScene() {
        const sceneName = prompt('Enter scene name:');
        if (!sceneName || !this.skillId) return;

        fetch('/api/skillcenter/installed/scenes/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ skillId: this.skillId, name: sceneName })
        })
        .then(response => response.json())
        .then(data => {
            if (data.requestStatus === 200 && data.data) {
                this.scenes.push(data.data);
                this.render();
            }
        })
        .catch(error => {
            console.error('Failed to create scene:', error);
        });
    }

    render() {
        if (!this.container) return;

        this.container.innerHTML = `
            <div class="collaborative-scene-container">
                <div class="collaborative-scene-header">
                    <span class="scene-title">
                        <i class="ri-team-line"></i>
                        Collaborative Scenes
                    </span>
                    <button class="scene-create-btn" onclick="collaborativeScene.createScene()">
                        <i class="ri-add-line"></i>
                        New Scene
                    </button>
                </div>
                
                ${this.scenes.length === 0 ? `
                    <div class="scene-empty">
                        <i class="ri-team-line"></i>
                        <span>No collaborative scenes available</span>
                        <p>Create a new scene to enable multi-agent collaboration</p>
                    </div>
                ` : `
                    <div class="scene-list">
                        ${this.scenes.map(scene => this.renderSceneItem(scene)).join('')}
                    </div>
                `}
                
                ${this.activeScene ? `
                    <div class="active-scene-indicator">
                        <i class="ri-checkbox-circle-fill"></i>
                        <span>Active: ${this.getSceneName(this.activeScene)}</span>
                    </div>
                ` : ''}
            </div>
        `;
    }

    renderSceneItem(scene) {
        const isActive = scene.sceneId === this.activeScene;
        const memberCount = scene.members ? scene.members.length : 0;

        return `
            <div class="scene-item ${isActive ? 'active' : ''}">
                <div class="scene-item-info">
                    <span class="scene-item-icon">
                        <i class="${isActive ? 'ri-checkbox-circle-fill' : 'ri-circle-line'}"></i>
                    </span>
                    <div class="scene-item-details">
                        <span class="scene-item-name">${scene.name || scene.sceneId}</span>
                        <span class="scene-item-meta">
                            <i class="ri-user-line"></i> ${memberCount} members
                            ${scene.description ? `<span class="scene-description">${scene.description}</span>` : ''}
                        </span>
                    </div>
                </div>
                <div class="scene-item-actions">
                    ${isActive ? `
                        <button class="scene-btn btn-leave" onclick="collaborativeScene.leaveScene('${scene.sceneId}')">
                            <i class="ri-logout-box-line"></i>
                            Leave
                        </button>
                    ` : `
                        <button class="scene-btn btn-join" onclick="collaborativeScene.joinScene('${scene.sceneId}')">
                            <i class="ri-login-box-line"></i>
                            Join
                        </button>
                    `}
                </div>
            </div>
        `;
    }

    getSceneName(sceneId) {
        const scene = this.scenes.find(s => s.sceneId === sceneId);
        return scene ? (scene.name || sceneId) : sceneId;
    }
}

window.CollaborativeSceneComponent = CollaborativeSceneComponent;
