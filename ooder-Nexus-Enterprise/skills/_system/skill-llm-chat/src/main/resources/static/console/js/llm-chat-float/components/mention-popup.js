/**
 * @提及弹窗组件
 * 输入@时弹出候选列表
 */
export class MentionPopup {
    constructor(options = {}) {
        this.options = {
            container: null,
            input: null,
            participants: [],
            onSelect: null,
            ...options
        };
        
        this.isVisible = false;
        this.selectedIndex = 0;
        this.filterText = '';
        this.element = null;
        
        this.init();
    }

    init() {
        this.createElement();
        this.bindEvents();
    }

    createElement() {
        this.element = document.createElement('div');
        this.element.className = 'mention-popup';
        this.element.style.display = 'none';
        this.addStyles();
        
        if (this.options.container) {
            this.options.container.appendChild(this.element);
        } else {
            document.body.appendChild(this.element);
        }
    }

    bindEvents() {
        if (this.options.input) {
            this.options.input.addEventListener('input', (e) => this.handleInput(e));
            this.options.input.addEventListener('keydown', (e) => this.handleKeydown(e));
            this.options.input.addEventListener('blur', () => {
                setTimeout(() => this.hide(), 150);
            });
        }
        
        this.element.addEventListener('click', (e) => {
            const item = e.target.closest('.mention-item');
            if (item) {
                this.selectItem(item.dataset.id);
            }
        });
    }

    handleInput(e) {
        const input = e.target;
        const value = input.value;
        const cursorPos = input.selectionStart;
        
        const atIndex = value.lastIndexOf('@', cursorPos - 1);
        
        if (atIndex !== -1) {
            const textAfterAt = value.substring(atIndex + 1, cursorPos);
            
            if (!textAfterAt.includes(' ') && !textAfterAt.includes('\n')) {
                this.filterText = textAfterAt.toLowerCase();
                this.show(input, atIndex);
                return;
            }
        }
        
        this.hide();
    }

    handleKeydown(e) {
        if (!this.isVisible) return;
        
        const items = this.getFilteredItems();
        
        switch (e.key) {
            case 'ArrowDown':
                e.preventDefault();
                this.selectedIndex = Math.min(this.selectedIndex + 1, items.length - 1);
                this.updateSelection();
                break;
                
            case 'ArrowUp':
                e.preventDefault();
                this.selectedIndex = Math.max(this.selectedIndex - 1, 0);
                this.updateSelection();
                break;
                
            case 'Enter':
            case 'Tab':
                e.preventDefault();
                if (items[this.selectedIndex]) {
                    this.selectItem(items[this.selectedIndex].id);
                }
                break;
                
            case 'Escape':
                e.preventDefault();
                this.hide();
                break;
        }
    }

    show(input, atIndex) {
        const items = this.getFilteredItems();
        
        if (items.length === 0) {
            this.hide();
            return;
        }
        
        this.selectedIndex = 0;
        this.render(items);
        
        const rect = this.getCaretCoordinates(input, atIndex);
        const inputRect = input.getBoundingClientRect();
        
        this.element.style.left = `${inputRect.left + rect.left}px`;
        this.element.style.top = `${inputRect.top + rect.top - this.element.offsetHeight - 5}px`;
        this.element.style.display = 'block';
        this.isVisible = true;
    }

    hide() {
        this.element.style.display = 'none';
        this.isVisible = false;
    }

    getFilteredItems() {
        if (!this.filterText) {
            return this.options.participants;
        }
        
        return this.options.participants.filter(p => 
            (p.name || '').toLowerCase().includes(this.filterText)
        );
    }

    render(items) {
        this.element.innerHTML = `
            <div class="mention-list">
                ${items.map((item, index) => `
                    <div class="mention-item ${index === this.selectedIndex ? 'selected' : ''}" 
                         data-id="${item.id}" data-type="${item.type || 'USER'}">
                        <div class="mention-item-avatar ${item.type === 'AGENT' ? 'agent' : ''}">
                            <i class="${item.type === 'AGENT' ? 'ri-robot-line' : 'ri-user-line'}"></i>
                        </div>
                        <div class="mention-item-info">
                            <div class="mention-item-name">${item.name || '未知'}</div>
                            <div class="mention-item-type">${item.type === 'AGENT' ? 'Agent' : '用户'}</div>
                        </div>
                    </div>
                `).join('')}
            </div>
        `;
    }

    updateSelection() {
        const items = this.element.querySelectorAll('.mention-item');
        items.forEach((item, index) => {
            item.classList.toggle('selected', index === this.selectedIndex);
        });
    }

    selectItem(id) {
        const item = this.options.participants.find(p => p.id === id);
        if (!item) return;
        
        if (this.options.onSelect) {
            this.options.onSelect(item);
        }
        
        this.insertMention(item);
        this.hide();
    }

    insertMention(item) {
        const input = this.options.input;
        if (!input) return;
        
        const value = input.value;
        const cursorPos = input.selectionStart;
        const atIndex = value.lastIndexOf('@', cursorPos - 1);
        
        if (atIndex !== -1) {
            const before = value.substring(0, atIndex);
            const after = value.substring(cursorPos);
            const mention = `@${item.name} `;
            
            input.value = before + mention + after;
            input.focus();
            
            const newPos = before.length + mention.length;
            input.setSelectionRange(newPos, newPos);
        }
    }

    getCaretCoordinates(input, position) {
        const div = document.createElement('div');
        const style = window.getComputedStyle(input);
        
        div.style.position = 'absolute';
        div.style.visibility = 'hidden';
        div.style.whiteSpace = 'pre-wrap';
        div.style.wordWrap = 'break-word';
        div.style.width = style.width;
        div.style.font = style.font;
        div.style.padding = style.padding;
        div.style.border = style.border;
        div.style.boxSizing = style.boxSizing;
        
        const text = input.value.substring(0, position);
        div.textContent = text;
        
        const span = document.createElement('span');
        span.textContent = '|';
        div.appendChild(span);
        
        document.body.appendChild(div);
        
        const coordinates = {
            left: span.offsetLeft,
            top: span.offsetTop + parseInt(style.lineHeight)
        };
        
        document.body.removeChild(div);
        
        return coordinates;
    }

    setParticipants(participants) {
        this.options.participants = participants || [];
    }

    addStyles() {
        if (document.getElementById('mention-popup-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'mention-popup-styles';
        style.textContent = `
            .mention-popup {
                position: fixed;
                z-index: 10000;
                background: white;
                border-radius: 12px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
                min-width: 200px;
                max-height: 280px;
                overflow-y: auto;
                animation: mentionPopupIn 0.15s ease;
            }
            @keyframes mentionPopupIn {
                from { opacity: 0; transform: translateY(5px); }
                to { opacity: 1; transform: translateY(0); }
            }
            .mention-list {
                padding: 8px;
            }
            .mention-item {
                display: flex;
                align-items: center;
                gap: 10px;
                padding: 10px 12px;
                border-radius: 8px;
                cursor: pointer;
                transition: background 0.15s;
            }
            .mention-item:hover,
            .mention-item.selected {
                background: #f1f5f9;
            }
            .mention-item.selected {
                background: rgba(99, 102, 241, 0.1);
            }
            .mention-item-avatar {
                width: 32px;
                height: 32px;
                min-width: 32px;
                border-radius: 50%;
                background: #e2e8f0;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #64748b;
                font-size: 16px;
            }
            .mention-item-avatar.agent {
                background: linear-gradient(135deg, rgba(99, 102, 241, 0.2) 0%, rgba(139, 92, 246, 0.2) 100%);
                color: #6366f1;
            }
            .mention-item-info {
                flex: 1;
                min-width: 0;
            }
            .mention-item-name {
                font-size: 14px;
                font-weight: 500;
                color: #334155;
            }
            .mention-item-type {
                font-size: 12px;
                color: #94a3b8;
            }
        `;
        document.head.appendChild(style);
    }

    destroy() {
        if (this.element) {
            this.element.remove();
            this.element = null;
        }
    }
}

export default MentionPopup;
