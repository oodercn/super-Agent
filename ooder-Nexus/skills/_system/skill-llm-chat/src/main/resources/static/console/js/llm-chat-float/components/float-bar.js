/**
 * 悬浮栏组件 - 催化入口
 * 包含菜单：待办、智能助手、IM消息
 */
import { eventBus } from '../core/event-bus.js';

export class FloatBar {
    constructor(container, options = {}) {
        this.container = container;
        this.options = {
            menus: [
                { id: 'todos', icon: 'ri-task-line', label: '待办', badge: 0 },
                { id: 'assistant', icon: 'ri-robot-line', label: '智能助手', badge: 0 },
                { id: 'im', icon: 'ri-message-3-line', label: 'IM消息', badge: 1 }
            ],
            ...options
        };
        
        this.isOpen = false;
        this.activeMenu = null;
        
        this.init();
    }

    init() {
        this.render();
        this.bindEvents();
    }

    render() {
        const badgeTotal = this.options.menus.reduce((sum, m) => sum + (m.badge || 0), 1);
        
        this.container.innerHTML = `
            <div class="float-bar ${this.isOpen ? 'open' : ''}" id="floatBar">
                <div class="float-bar-trigger" id="floatBarTrigger">
                    <div class="float-bar-icon">
                        <i class="ri-apps-line"></i>
                    </div>
                    ${badgeTotal > 1 ? `<span class="float-bar-badge">${badgeTotal}</span>` : ''}
                </div>
                <div class="float-bar-menu" id="floatBarMenu">
                    ${this.options.menus.map(menu => `
                        <div class="float-bar-menu-item ${this.activeMenu === menu.id ? 'active' : ''}" 
                             data-menu="${menu.id}">
                            <div class="menu-icon">
                                <i class="${menu.icon}"></i>
                            </div>
                            <div class="menu-label">${menu.label}</div>
                            ${menu.badge ? `<span class="menu-badge">${menu.badge}</span>` : ''}
                        </div>
                    `).join('')}
                </div>
            </div>
        `;
        
        this.addStyles();
    }

    addStyles() {
        if (document.getElementById('float-bar-styles')) return;
        
        const style = document.createElement('style');
        style.id = 'float-bar-styles';
        style.textContent = `
            .float-bar {
                position: fixed;
                bottom: 24px;
                right: 24px;
                z-index: 9999;
            }
            .float-bar-trigger {
                width: 60px;
                height: 60px;
                border-radius: 50%;
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                box-shadow: 0 4px 20px rgba(99, 102, 241, 0.4);
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            }
            .float-bar-trigger:hover {
                transform: scale(1.1);
                box-shadow: 0 6px 24px rgba(99, 102, 241, 0.5);
            }
            .float-bar-icon {
                color: white;
                font-size: 26px;
            }
            .float-bar-badge {
                position: absolute;
                top: -4px;
                right: -4px;
                background: #ef4444;
                color: white;
                font-size: 11px;
                min-width: 20px;
                height: 20px;
                border-radius: 10px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: 600;
                border: 2px solid white;
            }
            .float-bar-menu {
                position: absolute;
                bottom: 70px;
                right: 0;
                background: white;
                border-radius: 12px;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
                padding: 8px;
                min-width: 160px;
                opacity: 0;
                visibility: hidden;
                transform: translateY(10px);
                transition: all 0.2s ease;
            }
            .float-bar.open .float-bar-menu {
                opacity: 1;
                visibility: visible;
                transform: translateY(0);
            }
            .float-bar-menu-item {
                display: flex;
                align-items: center;
                gap: 12px;
                padding: 12px 16px;
                border-radius: 8px;
                cursor: pointer;
                transition: background 0.2s;
            }
            .float-bar-menu-item:hover {
                background: #f1f5f9;
            }
            .float-bar-menu-item.active {
                background: rgba(99, 102, 241, 0.1);
            }
            .menu-icon {
                width: 32px;
                height: 32px;
                border-radius: 50%;
                background: #f1f5f9;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #6366f1;
                font-size: 16px;
            }
            .float-bar-menu-item.active .menu-icon {
                background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
                color: white;
            }
            .menu-label {
                font-size: 14px;
                font-weight: 500;
                color: #334155;
            }
            .menu-badge {
                background: #ef4444;
                color: white;
                font-size: 11px;
                padding: 2px 6px;
                border-radius: 10px;
                font-weight: 600;
            }
        `;
        document.head.appendChild(style);
    }

    bindEvents() {
        const trigger = this.container.querySelector('#floatBarTrigger');
        const menuItems = this.container.querySelectorAll('.float-bar-menu-item');
        
        trigger.addEventListener('click', () => {
            this.toggle();
        });
        
        menuItems.forEach(item => {
            item.addEventListener('click', () => {
                const menuId = item.dataset.menu;
                this.selectMenu(menuId);
            });
        });
        
        document.addEventListener('click', (e) => {
            if (!e.target.closest('.float-bar') && this.isOpen) {
                this.close();
            }
        });
    }

    toggle() {
        this.isOpen = !this.isOpen;
        const bar = this.container.querySelector('#floatBar');
        bar.classList.toggle('open', this.isOpen);
        
        eventBus.emit('floatBar:toggle', { isOpen: this.isOpen });
    }

    open() {
        if (!this.isOpen) this.toggle();
    }

    close() {
        if (this.isOpen) this.toggle();
    }

    selectMenu(menuId) {
        console.log('[FloatBar] selectMenu called:', menuId);
        this.activeMenu = menuId;
        
        const menuItems = this.container.querySelectorAll('.float-bar-menu-item');
        menuItems.forEach(item => {
            item.classList.toggle('active', item.dataset.menu === menuId);
        });
        
        console.log('[FloatBar] Emitting floatBar:menuSelect event');
        eventBus.emit('floatBar:menuSelect', { menuId });
        this.close();
    }

    updateBadge(menuId, count) {
        const menu = this.options.menus.find(m => m.id === menuId);
        if (menu) {
            menu.badge = count;
            this.render();
        }
    }
}

export default FloatBar;
