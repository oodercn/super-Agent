/**
 * LlmChatFloat @提及模块
 * 包含@提及功能的下拉菜单
 */
export const MentionMixin = {
    handleMentionInput(e) {
        const input = e.target;
        const value = input.value;
        const cursorPos = input.selectionStart;

        const textBeforeCursor = value.substring(0, cursorPos);
        const mentionMatch = textBeforeCursor.match(/@([^\s@]*)$/);

        if (mentionMatch) {
            const query = mentionMatch[1].toLowerCase();
            this.showMentionDropdown(query, cursorPos);
        } else {
            this.hideMentionDropdown();
        }
    },

    handleMentionKeydown(e) {
        const dropdown = document.getElementById('mentionDropdown');
        if (!dropdown || dropdown.style.display === 'none') return;

        const items = dropdown.querySelectorAll('.mention-dropdown-item');
        let activeItem = dropdown.querySelector('.mention-dropdown-item.active');

        switch (e.key) {
            case 'ArrowDown':
                e.preventDefault();
                if (!activeItem) {
                    items[0]?.classList.add('active');
                } else {
                    activeItem.classList.remove('active');
                    const next = activeItem.nextElementSibling;
                    if (next) next.classList.add('active');
                    else items[0]?.classList.add('active');
                }
                break;
            case 'ArrowUp':
                e.preventDefault();
                if (!activeItem) {
                    items[items.length - 1]?.classList.add('active');
                } else {
                    activeItem.classList.remove('active');
                    const prev = activeItem.previousElementSibling;
                    if (prev) prev.classList.add('active');
                    else items[items.length - 1]?.classList.add('active');
                }
                break;
            case 'Enter':
            case 'Tab':
                if (activeItem) {
                    e.preventDefault();
                    this.selectMention(activeItem.dataset.id);
                }
                break;
            case 'Escape':
                this.hideMentionDropdown();
                break;
        }
    },

    showMentionDropdown(query, cursorPos) {
        let dropdown = document.getElementById('mentionDropdown');
        if (!dropdown) {
            dropdown = document.createElement('div');
            dropdown.id = 'mentionDropdown';
            dropdown.className = 'mention-dropdown';
            document.body.appendChild(dropdown);
        }

        if (!this.sceneParticipants || this.sceneParticipants.length === 0) {
            this.hideMentionDropdown();
            return;
        }

        const filtered = this.sceneParticipants.filter(p =>
            p && p.name && p.id && (
                p.name.toLowerCase().includes(query) ||
                p.id.toLowerCase().includes(query)
            )
        );

        if (filtered.length === 0) {
            this.hideMentionDropdown();
            return;
        }

        dropdown.innerHTML = filtered.map(p => `
            <div class="mention-dropdown-item" data-id="${p.id}" data-name="${p.name}" data-type="${p.type}">
                <div class="mention-avatar ${p.type === 'AGENT' ? 'is-agent' : ''}">
                    <i class="${p.type === 'AGENT' ? 'ri-robot-line' : 'ri-user-line'}"></i>
                </div>
                <div class="mention-info">
                    <div class="mention-name">${p.name}</div>
                    <div class="mention-type">${p.type === 'AGENT' ? 'Agent' : '用户'}</div>
                </div>
            </div>
        `).join('');

        dropdown.querySelectorAll('.mention-dropdown-item').forEach(item => {
            item.addEventListener('click', () => this.selectMention(item.dataset.id));
        });

        const input = document.getElementById('llmChatInput');
        const rect = input.getBoundingClientRect();
        dropdown.style.left = rect.left + 'px';
        dropdown.style.top = (rect.top - dropdown.offsetHeight - 8) + 'px';
        dropdown.style.width = rect.width + 'px';
        dropdown.style.display = 'block';
    },

    hideMentionDropdown() {
        const dropdown = document.getElementById('mentionDropdown');
        if (dropdown) {
            dropdown.style.display = 'none';
        }
    },

    selectMention(participantId) {
        const input = document.getElementById('llmChatInput');
        const value = input.value;
        const cursorPos = input.selectionStart;

        const textBeforeCursor = value.substring(0, cursorPos);
        const mentionMatch = textBeforeCursor.match(/@([^\s@]*)$/);

        if (mentionMatch) {
            const participant = this.sceneParticipants.find(p => p.id === participantId);
            if (participant) {
                const startPos = cursorPos - mentionMatch[0].length;
                const newValue = value.substring(0, startPos) + '@' + participant.name + ' ' + value.substring(cursorPos);
                input.value = newValue;
                input.focus();
                input.setSelectionRange(startPos + participant.name.length + 2, startPos + participant.name.length + 2);

                this.selectedParticipant = participant;
            }
        }

        this.hideMentionDropdown();
    }
};

export default MentionMixin;
