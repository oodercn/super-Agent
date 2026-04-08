export class TodoCard {

    static render(todo, onAction) {
        if (!todo) return '';
        const urgencyClass = todo.deadline && Date.now() > (todo.deadline || 0) ? 'urgent' : '';
        const typeIcons = {
            'INVITATION': 'ri-user-add-line',
            'APPROVAL': 'ri-checkbox-circle-line',
            'DELEGATION': 'ri-arrow-right-line',
            'SYSTEM': 'ri-notification-line',
            'REMINDER': 'ri-time-line',
            'TASK': 'ri-task-line'
        };

        const statusIcon = {
            'COMPLETED': { icon: 'ri-check-line', cls: 'done', text: '已完成' },
            'REJECTED': { icon: 'ri-close-line', cls: 'rejected', text: '已拒绝' },
            'PENDING': { icon: 'ri-time-line', cls: 'pending', text: '待处理' }
        };

        const status = statusIcon[todo.status] || statusIcon['PENDING'];
        const icon = typeIcons[todo.type] || 'ri-task-line';

        let actionsHtml = '';
        if (todo.status !== 'COMPLETED' && todo.status !== 'REJECTED') {
            actionsHtml = `
            <div class="todo-card-actions">
                <button class="todo-action accept" data-action="accept" data-todo-id="${todo.id}">
                    ✓ 接受
                </button>
                <button class="todo-action reject" data-action="reject" data-todo-id="${todo.id}">
                    ✗ 拒绝
                </button>
                ${todo.type === 'DELEGATION' ? `<button class="todo-action delegate" data-action="delegate" data-todo-id="${todo.id}">→ 转派</button>` : ''}
            </div>`;
        } else {
            actionsHtml = `<span class="todo-card-${status.cls}"><i class="${status.icon}"></i> ${status.text}</span>`;
        }

        return `
        <div class="todo-card ${urgencyClass}" data-todo-id="${todo.id}" data-type="${todo.type || 'TODO'}">
            <div class="todo-card-header">
                <span class="todo-card-type"><i class="${icon}"></i> ${todo.type || 'TODO'}</span>
                ${todo.deadline ? `<span class="todo-card-deadline ${urgencyClass}"><i class="ri-time-line"></i> ${new Date(todo.deadline).toLocaleDateString()}</span>` : ''}
            </div>
            <div class="todo-card-title">${TodoCard.escapeHtml(todo.title)}</div>
            ${todo.description ? `<div class="todo-card-desc">${TodoCard.escapeHtml(todo.description.substring(0, 200))}</div>` : ''}
            ${todo.fromUser ? `<div class="todo-card-meta">来自: ${TodoCard.escapeHtml(todo.fromUser)}</div>` : ''}
            ${actionsHtml}
        </div>`;
    }

    static bindActions(container, onAction) {
        if (!container) return;
        
        container.querySelectorAll('.todo-card-actions .todo-action').forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                const action = btn.dataset.action;
                const todoId = btn.dataset.todoId;
                if (onAction && action && todoId) onAction(action, todoId);
                
                btn.closest('.todo-card')?.classList.add('action-processing');
                btn.innerHTML = '<i class="ri-loader-4-line spin"></i> 处理中...';
            });
        });
    }

    static escapeHtml(str) {
        if (!str) return '';
        const div = document.createElement('div');
        div.textContent = str;
        return div.innerHTML;
    }

    static createFromMessage(message, options = {}) {
        return {
            id: 'todo-' + Date.now() + '-' + Math.random().toString(36).substr(2, 6),
            title: options.title || message.content?.substring(0, 60) || '待办事项',
            description: message.content || '',
            type: options.type || 'TASK',
            status: 'PENDING',
            fromUser: message.sender,
            deadline: options.deadline || null,
            createTime: Date.now(),
            sourceMessageId: message.messageId
        };
    }
}
