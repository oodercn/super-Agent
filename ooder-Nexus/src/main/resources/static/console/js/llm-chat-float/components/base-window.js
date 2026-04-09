/**
 * 基础窗口组件 - 所有窗口的基类
 * 支持拖拽、缩放、最小化
 */
import { eventBus } from '../core/event-bus.js';

export class BaseWindow {
    constructor(containerOrOptions, options = {}) {
        if (typeof containerOrOptions === 'string' || containerOrOptions instanceof HTMLElement) {
            this.container = typeof containerOrOptions === 'string' 
                ? document.getElementById(containerOrOptions) 
                : containerOrOptions;
            this.options = {
                id: 'window-' + Date.now(),
                title: '窗口',
                icon: 'ri-apps-line',
                width: 420,
                height: 560,
                minWidth: 320,
                minHeight: 400,
                resizable: true,
                draggable: true,
                ...options
            };
        } else {
            this.container = null;
            this.options = {
                id: 'window-' + Date.now(),
                title: '窗口',
                icon: 'ri-apps-line',
                width: 420,
                height: 560,
                minWidth: 320,
                minHeight: 400,
                resizable: true,
                draggable: true,
                ...containerOrOptions
            };
        }
        
        this.isOpen = false;
        this.isMinimized = false;
        this.position = { x: null, y: null };
        this.element = null;
        this._initialized = false;
        
        if (this.container) {
            Promise.resolve().then(() => this._init());
        }
    }

    _init() {
        if (this._initialized) return;
        this._initialized = true;
        
        this.render();
        this.bindEvents();
        this.initDraggable();
        if (this.options.resizable) {
            this.initResizable();
        }
        this.addStyles();
    }

    mount(container) {
        this.container = typeof container === 'string' 
            ? document.getElementById(container) 
            : container;
        
        if (!this.container) {
            this.container = document.createElement('div');
            this.container.id = 'agent-chat-windows';
            document.body.appendChild(this.container);
        }
        
        this._init();
    }

    render() {
        this.element = document.createElement('div');
        this.element.id = this.options.id;
        this.element.className = 'chat-window';
        this.element.style.cssText = `
            width: ${this.options.width}px;
            height: ${this.options.height}px;
            display: none;
        `;
        
        this.element.innerHTML = `
            <div class="window-header" data-draggable="${this.options.draggable}">
                <div class="window-title">
                    <i class="${this.options.icon}"></i>
                    <span>${this.options.title}</span>
                </div>
                <div class="window-actions">
                    <button class="window-btn minimize" title="最小化">
                        <i class="ri-subtract-line"></i>
                    </button>
                    <button class="window-btn close" title="关闭">
                        <i class="ri-close-line"></i>
                    </button>
                </div>
            </div>
            <div class="window-body">
                ${this.renderContent ? this.renderContent() : '<div class="window-empty">内容区域</div>'}
            </div>
        `;
        
        this.container.appendChild(this.element);
        
        if (this.bindContentEvents) {
            this.bindContentEvents();
        }
    }

    addStyles() {
        if (document.getElementById('base-window-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'base-window-styles';
        style.textContent = `
            .chat-window {
                position: fixed;
                background: #ffffff;
                border-radius: 12px;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.12);
                display: flex;
                flex-direction: column;
                z-index: 9998;
                overflow: hidden;
                border: 1px solid rgba(0, 0, 0, 0.06);
            }
            .chat-window.open {
                display: flex;
                animation: windowOpen 0.25s ease;
            }
            @keyframes windowOpen {
                from { opacity: 0; transform: scale(0.95) translateY(10px); }
                to { opacity: 1; transform: scale(1) translateY(0); }
            }
            .window-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 12px 16px;
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
                user-select: none;
            }
            .window-header[data-draggable="true"] {
                cursor: move;
            }
            .window-title {
                display: flex;
                align-items: center;
                gap: 10px;
                font-weight: 600;
                font-size: 15px;
            }
            .window-title i { font-size: 18px; }
            .window-actions { display: flex; gap: 8px; }
            .window-btn {
                background: rgba(255, 255, 255, 0.15);
                border: none;
                color: white;
                width: 28px;
                height: 28px;
                border-radius: 6px;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 16px;
                transition: background 0.2s;
            }
            .window-btn:hover { background: rgba(255, 255, 255, 0.25); }
            .window-body {
                flex: 1;
                display: flex;
                flex-direction: column;
                min-height: 0;
                overflow: hidden;
            }
            .window-empty {
                display: flex;
                align-items: center;
                justify-content: center;
                height: 100%;
                color: #94a3b8;
                font-size: 14px;
            }
        `;
        document.head.appendChild(style);
    }

    bindEvents() {
        const closeBtn = this.element.querySelector('.window-btn.close');
        const minimizeBtn = this.element.querySelector('.window-btn.minimize');
        
        closeBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.close();
        });
        
        minimizeBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.minimize();
        });
    }

    initDraggable() {
        if (!this.options.draggable) return;
        
        const header = this.element.querySelector('.window-header');
        let isDragging = false;
        let offsetX = 0;
        let offsetY = 0;
        
        header.addEventListener('mousedown', (e) => {
            if (e.target.closest('.window-btn')) return;
            isDragging = true;
            const rect = this.element.getBoundingClientRect();
            offsetX = e.clientX - rect.left;
            offsetY = e.clientY - rect.top;
            this.element.style.transition = 'none';
        });
        
        document.addEventListener('mousemove', (e) => {
            if (!isDragging) return;
            
            const x = e.clientX - offsetX;
            const y = e.clientY - offsetY;
            const maxX = window.innerWidth - this.element.offsetWidth - 10;
            const maxY = window.innerHeight - this.element.offsetHeight - 10;
            
            this.position.x = Math.max(10, Math.min(x, maxX));
            this.position.y = Math.max(10, Math.min(y, maxY));
            
            this.element.style.left = this.position.x + 'px';
            this.element.style.top = this.position.y + 'px';
            this.element.style.right = 'auto';
            this.element.style.bottom = 'auto';
        });
        
        document.addEventListener('mouseup', () => {
            isDragging = false;
            this.element.style.transition = '';
        });
    }

    initResizable() {
        if (!this.options.resizable) return;
        
        const resizer = document.createElement('div');
        resizer.className = 'window-resizer';
        this.element.appendChild(resizer);
        
        let isResizing = false;
        let startX = 0;
        let startY = 0;
        let startWidth = 0;
        let startHeight = 0;
        
        resizer.addEventListener('mousedown', (e) => {
            isResizing = true;
            startX = e.clientX;
            startY = e.clientY;
            startWidth = this.element.offsetWidth;
            startHeight = this.element.offsetHeight;
            this.element.style.transition = 'none';
            e.preventDefault();
        });
        
        document.addEventListener('mousemove', (e) => {
            if (!isResizing) return;
            
            const deltaX = e.clientX - startX;
            const deltaY = e.clientY - startY;
            
            const newWidth = Math.max(this.options.minWidth, startWidth + deltaX);
            const newHeight = Math.max(this.options.minHeight, startHeight + deltaY);
            
            this.element.style.width = newWidth + 'px';
            this.element.style.height = newHeight + 'px';
            
            this.options.width = newWidth;
            this.options.height = newHeight;
        });
        
        document.addEventListener('mouseup', () => {
            if (isResizing) {
                isResizing = false;
                this.element.style.transition = '';
                eventBus.emit('window:resize', { 
                    id: this.options.id,
                    width: this.options.width,
                    height: this.options.height
                });
            }
        });
        
        this.addResizeStyles();
    }

    addResizeStyles() {
        if (document.getElementById('window-resize-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'window-resize-styles';
        style.textContent = `
            .window-resizer {
                position: absolute;
                right: 0;
                bottom: 0;
                width: 16px;
                height: 16px;
                cursor: nwse-resize;
                background: transparent;
            }
            .window-resizer::before {
                content: '';
                position: absolute;
                right: 4px;
                bottom: 4px;
                width: 8px;
                height: 8px;
                border-right: 2px solid #ccc;
                border-bottom: 2px solid #ccc;
                opacity: 0.5;
            }
            .window-resizer:hover::before {
                opacity: 1;
                border-color: #6366f1;
            }
        `;
        document.head.appendChild(style);
    }

    open() {
        console.log('[BaseWindow] open called for:', this.options.id);
        this.isOpen = true;
        this.isMinimized = false;
        this.element.classList.add('open');
        this.element.style.display = 'flex';
        
        if (this.position.x === null) {
            this.centerWindow();
        }
        
        eventBus.emit('window:open', { id: this.options.id });
    }

    close() {
        this.isOpen = false;
        this.element.classList.remove('open');
        this.element.style.display = 'none';
        
        eventBus.emit('window:close', { id: this.options.id });
    }

    minimize() {
        this.isMinimized = true;
        this.element.style.display = 'none';
        
        eventBus.emit('window:minimize', { id: this.options.id });
    }

    centerWindow() {
        const x = (window.innerWidth - this.options.width) / 2;
        const y = (window.innerHeight - this.options.height) / 2;
        
        this.position.x = Math.max(10, x);
        this.position.y = Math.max(10, y);
        
        this.element.style.left = this.position.x + 'px';
        this.element.style.top = this.position.y + 'px';
        this.element.style.right = 'auto';
        this.element.style.bottom = 'auto';
    }

    setTitle(title) {
        this.options.title = title;
        const titleEl = this.element.querySelector('.window-title span');
        if (titleEl) titleEl.textContent = title;
    }

    destroy() {
        if (this.element) {
            this.element.remove();
            this.element = null;
        }
    }
}

export default BaseWindow;
