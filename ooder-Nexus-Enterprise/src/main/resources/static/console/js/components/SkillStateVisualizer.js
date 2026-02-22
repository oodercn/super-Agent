class SkillStateVisualizer {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.currentState = 'STOPPED';
        this.previousState = null;
        this.transitions = [];
        this.stateHistory = [];
    }

    setState(state) {
        this.previousState = this.currentState;
        this.currentState = state;
        this.stateHistory.push({
            from: this.previousState,
            to: state,
            timestamp: Date.now()
        });
        this.render();
    }

    getStates() {
        return [
            { id: 'CREATED', label: 'Created', description: 'Skill has been created' },
            { id: 'INITIALIZING', label: 'Initializing', description: 'Skill is initializing' },
            { id: 'INITIALIZED', label: 'Initialized', description: 'Skill is ready to start' },
            { id: 'STARTING', label: 'Starting', description: 'Skill is starting' },
            { id: 'RUNNING', label: 'Running', description: 'Skill is running' },
            { id: 'PAUSED', label: 'Paused', description: 'Skill is paused' },
            { id: 'STOPPING', label: 'Stopping', description: 'Skill is stopping' },
            { id: 'STOPPED', label: 'Stopped', description: 'Skill has stopped' },
            { id: 'ERROR', label: 'Error', description: 'Skill encountered an error' }
        ];
    }

    getStatePosition(stateId) {
        const positions = {
            'CREATED': { x: 50, y: 10 },
            'INITIALIZING': { x: 20, y: 30 },
            'INITIALIZED': { x: 80, y: 30 },
            'STARTING': { x: 50, y: 50 },
            'RUNNING': { x: 50, y: 70 },
            'PAUSED': { x: 20, y: 70 },
            'STOPPING': { x: 80, y: 50 },
            'STOPPED': { x: 50, y: 90 },
            'ERROR': { x: 80, y: 90 }
        };
        return positions[stateId] || { x: 50, y: 50 };
    }

    render() {
        if (!this.container) return;

        const states = this.getStates();

        this.container.innerHTML = `
            <div class="state-visualizer-container">
                <div class="state-visualizer-header">
                    <span class="state-visualizer-title">
                        <i class="ri-flow-chart"></i>
                        Skill State Machine
                    </span>
                    <span class="state-visualizer-current">
                        Current: <strong>${this.currentState}</strong>
                    </span>
                </div>
                <div class="state-visualizer-diagram">
                    <svg viewBox="0 0 100 100" class="state-diagram-svg">
                        ${this.renderTransitions()}
                        ${states.map(state => this.renderState(state)).join('')}
                    </svg>
                </div>
                <div class="state-visualizer-legend">
                    ${states.map(state => `
                        <div class="state-legend-item ${state.id === this.currentState ? 'active' : ''}">
                            <span class="state-legend-dot ${this.getStateClass(state.id)}"></span>
                            <span class="state-legend-label">${state.label}</span>
                        </div>
                    `).join('')}
                </div>
                ${this.stateHistory.length > 0 ? `
                    <div class="state-visualizer-history">
                        <div class="state-history-title">Recent Transitions</div>
                        <div class="state-history-items">
                            ${this.stateHistory.slice(-5).reverse().map(h => `
                                <div class="state-history-item">
                                    <span class="history-from">${h.from || 'N/A'}</span>
                                    <i class="ri-arrow-right-line"></i>
                                    <span class="history-to">${h.to}</span>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                ` : ''}
            </div>
        `;
    }

    renderState(state) {
        const pos = this.getStatePosition(state.id);
        const isActive = state.id === this.currentState;
        const stateClass = this.getStateClass(state.id);

        return `
            <g class="state-node ${isActive ? 'active' : ''}" transform="translate(${pos.x}, ${pos.y})">
                <circle r="6" class="state-circle ${stateClass}" />
                ${isActive ? '<circle r="8" class="state-pulse" />' : ''}
                <text y="12" class="state-label" text-anchor="middle">${state.label}</text>
            </g>
        `;
    }

    renderTransitions() {
        const transitions = [
            ['CREATED', 'INITIALIZING'],
            ['INITIALIZING', 'INITIALIZED'],
            ['INITIALIZED', 'STARTING'],
            ['STARTING', 'RUNNING'],
            ['RUNNING', 'PAUSED'],
            ['PAUSED', 'RUNNING'],
            ['RUNNING', 'STOPPING'],
            ['STOPPING', 'STOPPED'],
            ['STOPPED', 'STARTING'],
            ['INITIALIZING', 'ERROR'],
            ['STARTING', 'ERROR'],
            ['RUNNING', 'ERROR']
        ];

        return transitions.map(([from, to]) => {
            const fromPos = this.getStatePosition(from);
            const toPos = this.getStatePosition(to);
            const isActive = (from === this.previousState && to === this.currentState);

            return `
                <line 
                    x1="${fromPos.x}" y1="${fromPos.y}" 
                    x2="${toPos.x}" y2="${toPos.y}" 
                    class="state-transition ${isActive ? 'active' : ''}"
                />
            `;
        }).join('');
    }

    getStateClass(stateId) {
        switch (stateId) {
            case 'RUNNING':
                return 'state-running';
            case 'PAUSED':
                return 'state-paused';
            case 'STOPPED':
                return 'state-stopped';
            case 'ERROR':
                return 'state-error';
            case 'INITIALIZING':
            case 'STARTING':
            case 'STOPPING':
                return 'state-transitioning';
            default:
                return 'state-idle';
        }
    }
}

window.SkillStateVisualizer = SkillStateVisualizer;
