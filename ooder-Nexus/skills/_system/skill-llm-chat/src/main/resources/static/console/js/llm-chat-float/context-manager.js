/**
 * LlmChatFloat 上下文管理模块
 * 包含上下文管理、Token计算
 */
export const ContextManagerMixin = {
    getSkillContext() {
        const context = [];
        
        if (this.config.context) {
            context.push({
                role: 'system',
                content: this.config.context
            });
        }

        const skillContext = this.buildContextString();
        if (skillContext) {
            context.push({
                role: 'system',
                content: skillContext
            });
        }

        return context;
    },

    buildContextString() {
        const parts = [];

        if (this.config.context) {
            parts.push(this.config.context);
        }

        if (this.isSceneMode() && this.sceneContext) {
            if (this.sceneContext.description) {
                parts.push(`场景: ${this.sceneContext.description}`);
            }
            if (this.sceneParticipants && this.sceneParticipants.length > 0) {
                const participantNames = this.sceneParticipants
                    .map(p => p.name || p.id)
                    .join(', ');
                parts.push(`参与者: ${participantNames}`);
            }
        }

        return parts.length > 0 ? parts.join('\n\n') : '';
    },

    getHistoryMessages() {
        return this.messages.slice(-20).map(msg => ({
            role: msg.role,
            content: msg.content
        }));
    },

    getContextTokens() {
        const context = this.buildContextString();
        const history = this.getHistoryMessages();
        
        let totalTokens = 0;
        
        if (context) {
            totalTokens += Math.ceil(context.length / 4);
        }
        
        history.forEach(msg => {
            if (msg.content) {
                totalTokens += Math.ceil(msg.content.length / 4);
            }
        });

        return totalTokens;
    },

    updateContextTokens() {
        const tokens = this.getContextTokens();
        const tokenDisplay = document.getElementById('contextTokens');
        if (tokenDisplay) {
            tokenDisplay.textContent = `~${tokens} tokens`;
        }
    },

    showContextModal() {
        const modal = document.createElement('div');
        modal.className = 'llm-chat-float-modal';
        modal.innerHTML = `
            <div class="llm-chat-float-modal-content">
                <div class="llm-chat-float-modal-header">
                    <h3>上下文管理</h3>
                    <button class="llm-chat-float-modal-close">&times;</button>
                </div>
                <div class="llm-chat-float-modal-body">
                    <div class="context-section">
                        <label>系统上下文</label>
                        <textarea id="contextInput" rows="5">${this.config.context || ''}</textarea>
                    </div>
                    <div class="context-section">
                        <label>Token 估算: <span id="modalTokenCount">${this.getContextTokens()}</span></label>
                    </div>
                </div>
                <div class="llm-chat-float-modal-footer">
                    <button class="llm-chat-float-btn" id="exportContextBtn">导出</button>
                    <button class="llm-chat-float-btn" id="trimContextBtn">裁剪</button>
                    <button class="llm-chat-float-btn" id="clearContextBtn">清空</button>
                    <button class="llm-chat-float-btn primary" id="saveContextBtn">保存</button>
                </div>
            </div>
        `;

        document.body.appendChild(modal);

        modal.querySelector('.llm-chat-float-modal-close').addEventListener('click', () => modal.remove());
        modal.querySelector('#saveContextBtn').addEventListener('click', () => {
            this.config.context = document.getElementById('contextInput').value;
            this.saveConfig();
            modal.remove();
        });
        modal.querySelector('#exportContextBtn').addEventListener('click', () => this.exportContext());
        modal.querySelector('#trimContextBtn').addEventListener('click', () => this.trimContext());
        modal.querySelector('#clearContextBtn').addEventListener('click', () => this.clearContext());
    },

    exportContext() {
        const context = {
            config: this.config,
            messages: this.messages
        };
        const blob = new Blob([JSON.stringify(context, null, 2)], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `llm-context-${Date.now()}.json`;
        a.click();
        URL.revokeObjectURL(url);
    },

    trimContext() {
        if (this.messages.length > 10) {
            this.messages = this.messages.slice(-10);
            this.reRenderMessages();
            this.updateContextTokens();
        }
    },

    clearContext() {
        this.config.context = '';
        this.messages = [];
        this.reRenderMessages();
        this.updateContextTokens();
    }
};

export default ContextManagerMixin;
