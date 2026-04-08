/**
 * 表情选择器组件
 * 提供常用表情选择功能
 */
export class EmojiPicker {
    constructor(options = {}) {
        this.container = options.container;
        this.input = options.input;
        this.onSelect = options.onSelect || (() => {});
        this.isVisible = false;
        this.element = null;
        
        this.emojis = [
            { category: '常用', items: ['😀', '😊', '😂', '🤣', '😍', '🥰', '😘', '😋', '🤔', '😎', '👍', '👎', '👏', '🙏', '💪', '❤️', '🔥', '✨', '🎉', '🎊'] },
            { category: '表情', items: ['😀', '😃', '😄', '😁', '😅', '😂', '🤣', '😊', '😇', '🙂', '😉', '😌', '😍', '🥰', '😘', '😗', '😙', '😚', '😋', '😛'] },
            { category: '手势', items: ['👍', '👎', '👏', '🙌', '🤝', '🙏', '💪', '✊', '✌️', '🤞', '👌', '🤙', '👋', '🤚', '✋', '🖐️', '🖖', '👊', '🤛', '🤜'] },
            { category: '符号', items: ['❤️', '🧡', '💛', '💚', '💙', '💜', '🖤', '🤍', '💔', '❣️', '💕', '💞', '💓', '💗', '💖', '💘', '💝', '💟', '🔥', '✨'] }
        ];
        
        this.currentCategory = 0;
        this.init();
    }
    
    init() {
        this.render();
        this.bindEvents();
    }
    
    render() {
        this.element = document.createElement('div');
        this.element.className = 'emoji-picker';
        this.element.style.display = 'none';
        
        this.element.innerHTML = `
            <div class="emoji-picker-header">
                ${this.emojis.map((cat, i) => `
                    <div class="emoji-category-tab ${i === 0 ? 'active' : ''}" data-index="${i}">
                        ${cat.category}
                    </div>
                `).join('')}
            </div>
            <div class="emoji-picker-body">
                ${this.renderEmojis(this.currentCategory)}
            </div>
        `;
        
        this.container.appendChild(this.element);
        this.addStyles();
    }
    
    renderEmojis(categoryIndex) {
        const category = this.emojis[categoryIndex];
        return `
            <div class="emoji-grid">
                ${category.items.map(emoji => `
                    <div class="emoji-item" data-emoji="${emoji}">${emoji}</div>
                `).join('')}
            </div>
        `;
    }
    
    bindEvents() {
        this.element.addEventListener('click', (e) => {
            const tab = e.target.closest('.emoji-category-tab');
            if (tab) {
                this.switchCategory(parseInt(tab.dataset.index));
                return;
            }
            
            const emojiItem = e.target.closest('.emoji-item');
            if (emojiItem) {
                this.selectEmoji(emojiItem.dataset.emoji);
            }
        });
        
        document.addEventListener('click', (e) => {
            if (this.isVisible && !this.element.contains(e.target) && !e.target.closest('.emoji-trigger')) {
                this.hide();
            }
        });
    }
    
    switchCategory(index) {
        this.currentCategory = index;
        
        const tabs = this.element.querySelectorAll('.emoji-category-tab');
        tabs.forEach((tab, i) => tab.classList.toggle('active', i === index));
        
        const body = this.element.querySelector('.emoji-picker-body');
        body.innerHTML = this.renderEmojis(index);
    }
    
    selectEmoji(emoji) {
        if (this.input) {
            const start = this.input.selectionStart;
            const end = this.input.selectionEnd;
            const value = this.input.value;
            
            this.input.value = value.substring(0, start) + emoji + value.substring(end);
            this.input.selectionStart = this.input.selectionEnd = start + emoji.length;
            this.input.focus();
        }
        
        this.onSelect(emoji);
        this.hide();
    }
    
    toggle() {
        if (this.isVisible) {
            this.hide();
        } else {
            this.show();
        }
    }
    
    show() {
        this.isVisible = true;
        this.element.style.display = 'block';
    }
    
    hide() {
        this.isVisible = false;
        this.element.style.display = 'none';
    }
    
    addStyles() {
        if (document.getElementById('emoji-picker-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'emoji-picker-styles';
        style.textContent = `
            .emoji-picker {
                position: absolute;
                bottom: 60px;
                left: 16px;
                width: 280px;
                background: white;
                border-radius: 12px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
                z-index: 1000;
                overflow: hidden;
            }
            .emoji-picker-header {
                display: flex;
                border-bottom: 1px solid #e2e8f0;
                background: #f8fafc;
            }
            .emoji-category-tab {
                flex: 1;
                padding: 10px 8px;
                text-align: center;
                font-size: 12px;
                color: #64748b;
                cursor: pointer;
                transition: all 0.2s;
            }
            .emoji-category-tab:hover {
                background: #f1f5f9;
            }
            .emoji-category-tab.active {
                background: white;
                color: #6366f1;
                font-weight: 500;
            }
            .emoji-picker-body {
                padding: 8px;
                max-height: 200px;
                overflow-y: auto;
            }
            .emoji-grid {
                display: grid;
                grid-template-columns: repeat(5, 1fr);
                gap: 4px;
            }
            .emoji-item {
                width: 44px;
                height: 44px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 24px;
                cursor: pointer;
                border-radius: 8px;
                transition: background 0.2s;
            }
            .emoji-item:hover {
                background: #f1f5f9;
            }
        `;
        document.head.appendChild(style);
    }
    
    destroy() {
        if (this.element) {
            this.element.remove();
        }
    }
}

export default EmojiPicker;
