/**
 * LlmChatFloat 事件处理模块
 * 包含事件绑定和处理
 */
export const EventHandlerMixin = {
    bindEvents() {
        const floatBtn = document.getElementById('llmChatFloatBtn');
        if (floatBtn) {
            floatBtn.addEventListener('click', () => this.toggle());
        }
        
        this.bindNormalModeEvents();
        if (this.isSceneMode()) {
            this.bindSceneModeEvents();
        }
    },

    bindNormalModeEvents() {
        const input = document.getElementById('llmChatInput');
        const sendBtn = document.getElementById('llmChatSendBtn');
        const clearBtn = document.getElementById('clearBtn');
        const closeBtn = document.getElementById('closeBtn');
        const contextBtn = document.getElementById('contextBtn');
        const modelBtn = document.getElementById('modelBtn');
        const templateBtn = document.getElementById('templateBtn');

        if (input) {
            input.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    this.sendMessage();
                }
            });
        }

        if (sendBtn) {
            sendBtn.addEventListener('click', () => this.sendMessage());
        }

        if (clearBtn) {
            clearBtn.addEventListener('click', () => this.clearChat());
        }

        if (closeBtn) {
            closeBtn.addEventListener('click', () => this.close());
        }

        if (contextBtn) {
            contextBtn.addEventListener('click', () => this.showContextModal());
        }

        if (modelBtn) {
            modelBtn.addEventListener('click', () => this.showModelModal());
        }

        if (templateBtn) {
            templateBtn.addEventListener('click', () => this.showTemplateModal());
        }
    },

    bindSceneModeEvents() {
        const sidebar = document.querySelector('.chat-sidebar');
        const input = document.getElementById('llmChatInput');
        const sendBtn = document.getElementById('llmChatSendBtn');
        const closeBtn = document.getElementById('closeBtn');
        const historyBtn = document.getElementById('historyBtn');
        const clearTargetBtn = document.getElementById('clearTargetBtn');

        if (sidebar) {
            sidebar.addEventListener('click', (e) => {
                const tab = e.target.closest('.chat-tab');
                if (tab) {
                    this.switchTab(tab.dataset.tab);
                }
                
                const participantItem = e.target.closest('.participant-item');
                if (participantItem) {
                    this.selectParticipant(participantItem.dataset.id);
                }
            });
        }

        if (input) {
            input.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    this.sendMessage();
                }
            });
        }

        if (sendBtn) {
            sendBtn.addEventListener('click', () => this.sendMessage());
        }

        if (closeBtn) {
            closeBtn.addEventListener('click', () => this.close());
        }

        if (clearTargetBtn) {
            clearTargetBtn.addEventListener('click', () => this.clearTarget());
        }
    },

    switchTab(tab) {
        this.currentTab = tab;
        const tabs = document.querySelectorAll('.chat-tab');
        tabs.forEach(t => t.classList.toggle('active', t.dataset.tab === tab));
        
        this.renderSceneMessages();
        this.renderParticipants();
        this.renderTodos();
    },

    toggleParticipants() {
        const sidebar = document.getElementById('agentChatSidebar');
        if (sidebar) {
            sidebar.classList.toggle('show');
        }
    },

    clearTarget() {
        this.selectedParticipant = null;
        const target = document.getElementById('chatTarget');
        if (target) {
            target.style.display = 'none';
        }
    },

    selectParticipant(participantId) {
        const participant = this.sceneParticipants?.find(p => p.id === participantId);
        if (participant) {
            this.selectedParticipant = participant;
            const target = document.getElementById('chatTarget');
            if (target) {
                target.textContent = `@${participant.name}`;
                target.style.display = 'inline';
            }
            this.renderParticipants();
        }
    },

    async sendSceneMessageToAgent(content) {
        if (!content || !this.sceneGroupId) return;
        
        const response = await fetch(`/api/v1/scene-groups/${this.sceneGroupId}/chat/messages`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                message: content,
                toParticipantId: this.selectedParticipant?.id,
                conversationType: this.selectedParticipant?.type || 'P2A'
            })
        });
        
        const result = await response.json();
        if (result.code === 200) {
            this.renderSceneMessages();
        } else {
            console.error('[LlmChatFloat] Failed to send message to agent:', result.message);
            this.addSceneMessage('assistant', content);
        }
    },

    toggle() {
        this.isOpen = !this.isOpen;
        const windowEl = document.getElementById('llmChatFloatWindow');
        if (windowEl) {
            windowEl.classList.toggle('open', this.isOpen);
        }
    },

    close() {
        this.isOpen = false;
        const windowEl = document.getElementById('llmChatFloatWindow');
        if (windowEl) {
            windowEl.classList.remove('open');
        }
    },

    closeModal() {
        document.querySelectorAll('.llm-chat-float-modal').forEach(modal => {
            modal.remove();
        });
    }
};

export default EventHandlerMixin;
