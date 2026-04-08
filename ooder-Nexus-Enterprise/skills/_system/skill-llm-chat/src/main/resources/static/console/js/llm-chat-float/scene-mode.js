/**
 * LlmChatFloat 场景模式模块
 * 包含场景模式下的消息发送、参与者选择等功能
 */
export const SceneModeMixin = {
    switchTab(tab) {
        this.currentTab = tab;
        this.renderSceneMessages();
        
        document.querySelectorAll('.agent-chat-sidebar-tab').forEach(t => {
            t.classList.toggle('active', t.dataset.tab === tab);
        });
    },

    selectParticipant(participant) {
        this.selectedParticipant = participant;
        this.updateInputTarget();
        this.renderParticipants();
    },

    updateInputTarget() {
        const target = document.getElementById('chatTarget');
        if (target) {
            if (this.selectedParticipant) {
                target.textContent = `@${this.selectedParticipant.name}`;
                target.style.display = 'inline';
            } else {
                target.style.display = 'none';
            }
        }
    },

    async sendSceneMessage(content) {
        if (!content || !this.sceneGroupId) return;
        
        const message = {
            messageId: 'msg-' + Date.now(),
            sceneGroupId: this.sceneGroupId,
            content: content,
            messageType: 'USER_MESSAGE',
            conversationType: 'CHAT',
            createTime: Date.now(),
            status: 'SENDING'
        };

        message.fromParticipant = {
            id: 'current-user',
            type: 'USER',
            name: '我'
        };

        if (this.selectedParticipant) {
            message.toParticipant = {
                id: this.selectedParticipant.id,
                type: this.selectedParticipant.type,
                name: this.selectedParticipant.name
            };
        }

        this.sceneMessages.push(message);
        this.renderSceneMessages();

        try {
            const response = await fetch(`/api/v1/scene-groups/${this.sceneGroupId}/chat/messages`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(message)
            });

            const result = await response.json();
            console.log('[LlmChatFloat] Send message result:', result);
            
            if (result.status === 'success' || result.code === 200) {
                message.status = 'DELIVERED';
                if (result.data) {
                    this.sceneMessages.push(result.data);
                }
            } else {
                message.status = 'FAILED';
                console.error('[LlmChatFloat] Failed to send message:', result.message || 'Unknown error');
            }
        } catch (error) {
            message.status = 'FAILED';
            console.error('[LlmChatFloat] Error sending message:', error);
        }

        this.renderSceneMessages();
        this.updateBadge();
    },

    async acceptTodo(todoId) {
        try {
            const response = await fetch(`/api/v1/scene-groups/${this.sceneGroupId}/chat/todos/${todoId}/accept`, {
                method: 'POST'
            });
            const result = await response.json();
            
            if (result.code === 200) {
                const todo = this.sceneTodos.find(t => t.id === todoId);
                if (todo) {
                    todo.status = 'ACCEPTED';
                    this.renderTodos();
                }
            }
        } catch (error) {
            console.error('[LlmChatFloat] Failed to accept todo:', error);
        }
    },

    async rejectTodo(todoId, reason) {
        try {
            const response = await fetch(`/api/v1/scene-groups/${this.sceneGroupId}/chat/todos/${todoId}/reject`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ reason })
            });
            const result = await response.json();
            
            if (result.code === 200) {
                const todo = this.sceneTodos.find(t => t.id === todoId);
                if (todo) {
                    todo.status = 'REJECTED';
                    this.renderTodos();
                }
            }
        } catch (error) {
            console.error('[LlmChatFloat] Failed to reject todo:', error);
        }
    },

    async delegateTodo(todoId, toUserId) {
        try {
            const response = await fetch(`/api/v1/scene-groups/${this.sceneGroupId}/chat/todos/${todoId}/delegate`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ toUserId })
            });
            const result = await response.json();
            
            if (result.code === 200) {
                this.loadSceneTodos();
            }
        } catch (error) {
            console.error('[LlmChatFloat] Failed to delegate todo:', error);
        }
    },

    updateForSceneMode() {
        const header = document.querySelector('.llm-chat-float-header');
        const input = document.getElementById('llmChatInput');
        
        if (this.isSceneMode()) {
            if (header) {
                header.classList.add('scene-mode');
            }
            if (input) {
                input.placeholder = '输入消息... (@提及参与者)';
            }
            this.loadSceneMessages();
        } else {
            if (header) {
                header.classList.remove('scene-mode');
            }
            if (input) {
                input.placeholder = '输入消息...';
            }
        }
    },

    filterMessagesByTab() {
        if (this.currentTab === 'all') {
            return this.sceneMessages;
        }
        
        return this.sceneMessages.filter(msg => {
            if (this.currentTab === 'agents') {
                return msg.fromParticipant && 
                    (msg.fromParticipant.type === 'AGENT' || msg.fromParticipant.type === 'SUPER_AGENT');
            }
            if (this.currentTab === 'users') {
                return msg.fromParticipant && msg.fromParticipant.type === 'USER';
            }
            if (this.currentTab === 'todos') {
                return msg.messageType === 'TODO';
            }
            return true;
        });
    }
};

export default SceneModeMixin;
