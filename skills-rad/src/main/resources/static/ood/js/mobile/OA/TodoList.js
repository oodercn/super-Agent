/**
 * 移动端待办事项组件
 * 用于显示和管理用户的待办任务
 */
ood.Class("ood.Mobile.OA.TodoList", ["ood.UI", "ood.absList"], {
    Instance: {
        Initialize: function() {
            this.constructor.upper.prototype.Initialize.call(this);
            this.initTodoFeatures();
        },
        
        initTodoFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-todo ood-mobile-component');
            this.bindTouchEvents();
        },
        
        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // TodoList组件的尺寸调整逻辑

            var prop = profile.properties,
                root = profile.getRoot(),
                container = profile.getSubNode('CONTAINER'),
                // 获取单位转换函数
                us = ood.$us(profile),
                adjustunit = function(v, emRate) {
                    return profile.$forceu(v, us > 0 ? 'em' : 'px', emRate);
                };

            // 如果提供了宽度，调整容器宽度
            if (width && width !== 'auto') {
                // 转换为像素值进行计算
                var pxWidth = profile.$px(width, null, true);
                if (pxWidth) {
                    root.css('width', adjustunit(pxWidth));
                    container.css('width', '100%');
                }
            }

            // 如果提供了高度，调整容器高度
            if (height && height !== 'auto') {
                var pxHeight = profile.$px(height, null, true);
                if (pxHeight) {
                    root.css('height', adjustunit(pxHeight));
                    container.css('height', '100%');
                    
                    // 设置溢出滚动
                    container.css('overflow-y', 'auto');
                }
            }

            // 调整内部布局以适应新尺寸
            this.adjustLayout();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 任务项点击事件
            container.on('click', '.ood-mobile-todo-item', function(e) {
                var item = ood(this);
                var index = parseInt(item.attr('data-index'));
                var todo = self._todos[index];
                
                if (todo && !item.hasClass('ood-mobile-todo-item-disabled')) {
                    self.onTodoClick(index, todo);
                }
            });
            
            // 完成状态切换
            container.on('click', '.ood-mobile-todo-checkbox', function(e) {
                e.stopPropagation();
                var item = ood(this).closest('.ood-mobile-todo-item');
                var index = parseInt(item.attr('data-index'));
                self.toggleComplete(index);
            });
            
            // 添加移动端触摸事件支持
            container.on('touchstart', '.ood-mobile-todo-item', function(e) {
                ood(this).addClass('ood-mobile-todo-item-active');
            });
            
            container.on('touchend', '.ood-mobile-todo-item', function(e) {
                ood(this).removeClass('ood-mobile-todo-item-active');
            });
            
            container.on('touchcancel', '.ood-mobile-todo-item', function(e) {
                ood(this).removeClass('ood-mobile-todo-item-active');
            });
            
            // 复选框触摸事件
            container.on('touchstart', '.ood-mobile-todo-checkbox', function(e) {
                ood(this).addClass('ood-mobile-todo-checkbox-active');
            });
            
            container.on('touchend', '.ood-mobile-todo-checkbox', function(e) {
                ood(this).removeClass('ood-mobile-todo-checkbox-active');
            });
            
            container.on('touchcancel', '.ood-mobile-todo-checkbox', function(e) {
                ood(this).removeClass('ood-mobile-todo-checkbox-active');
            });
        },
        
        setTodos: function(todos) {
            this._todos = todos || [];
            this.renderTodos();
        },
        
        getTodos: function() {
            return this._todos || [];
        },
        
        renderTodos: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            container.html('');
            
            for (var i = 0; i < this._todos.length; i++) {
                var todo = this._todos[i];
                var todoElement = this.createTodoElement(todo, i);
                container.append(todoElement);
            }
        },
        
        createTodoElement: function(todo, index) {
            var todoEl = ood('<div class="ood-mobile-todo-item" data-index="' + index + '"></div>');
            
            // 复选框
            var checkbox = ood('<div class="ood-mobile-todo-checkbox"></div>');
            if (todo.completed) {
                checkbox.addClass('ood-mobile-todo-checkbox-checked');
            }
            todoEl.append(checkbox);
            
            // 内容容器
            var content = ood('<div class="ood-mobile-todo-content"></div>');
            
            // 标题
            if (todo.title) {
                var title = ood('<div class="ood-mobile-todo-title">' + todo.title + '</div>');
                content.append(title);
            }
            
            // 截止时间
            if (todo.dueDate) {
                var dueDate = ood('<div class="ood-mobile-todo-due">' + todo.dueDate + '</div>');
                content.append(dueDate);
            }
            
            // 优先级
            if (todo.priority) {
                var priority = ood('<div class="ood-mobile-todo-priority ood-mobile-todo-priority-' + todo.priority + '">' + 
                    (todo.priority === 'high' ? '高' : todo.priority === 'medium' ? '中' : '低') + '</div>');
                content.append(priority);
            }
            
            todoEl.append(content);
            
            // 已完成状态
            if (todo.completed) {
                todoEl.addClass('ood-mobile-todo-completed');
            }
            
            // 禁用状态
            if (todo.disabled) {
                todoEl.addClass('ood-mobile-todo-item-disabled');
            }
            
            return todoEl;
        },
        
        toggleComplete: function(index) {
            if (index < 0 || index >= this._todos.length) return;
            
            var todo = this._todos[index];
            todo.completed = !todo.completed;
            
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            var item = container.find('.ood-mobile-todo-item[data-index="' + index + '"]');
            var checkbox = item.find('.ood-mobile-todo-checkbox');
            
            if (todo.completed) {
                item.addClass('ood-mobile-todo-completed');
                checkbox.addClass('ood-mobile-todo-checkbox-checked');
            } else {
                item.removeClass('ood-mobile-todo-completed');
                checkbox.removeClass('ood-mobile-todo-checkbox-checked');
            }
            
            // 触发完成状态改变事件
            this.onTodoCompleteChange(index, todo);
        },
        
        addTodo: function(todo) {
            this._todos.push(todo);
            this.renderTodos();
        },
        
        removeTodo: function(index) {
            if (index < 0 || index >= this._todos.length) return;
            
            this._todos.splice(index, 1);
            this.renderTodos();
        },
        
        onTodoClick: function(index, todo) {
            var profile = this.get(0);
            
            if (profile.onTodoClick) {
                profile.boxing().onTodoClick(profile, index, todo);
            }
        },
        
        onTodoCompleteChange: function(index, todo) {
            var profile = this.get(0);
            
            if (profile.onTodoCompleteChange) {
                profile.boxing().onTodoCompleteChange(profile, index, todo);
            }
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentTodos = self.getTodos();
                if (typeof index === 'undefined') {
                    currentTodos = currentTodos.concat(items);
                } else {
                    var insertIndex = isBefore ? index : index + 1;
                    currentTodos.splice.apply(currentTodos, [insertIndex, 0].concat(items));
                }
                
                self.setTodos(currentTodos);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentTodos = self.getTodos();
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var idx = parseInt(indices[i]);
                    if (idx >= 0 && idx < currentTodos.length) {
                        currentTodos.splice(idx, 1);
                    }
                }
                
                self.setTodos(currentTodos);
            });
        },
        
        clearItems: function() {
            return this.setTodos([]);
        },
        
        getItems: function() {
            return this.getTodos();
        },
        
        getSelectedItems: function() {
            return [];
        },
        
        selectItem: function(value) {
            return this;
        },
        
        unselectItem: function(value) {
            return this;
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-todo',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-todo-container'
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                'background-color': 'var(--mobile-bg-primary)'
            },
            
            CONTAINER: {
                padding: 'var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-todo-item': {
                display: 'flex',
                'align-items': 'flex-start',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                cursor: 'pointer',
                transition: 'background-color 0.2s ease',
                // 添加移动端触摸反馈样式
                '-webkit-tap-highlight-color': 'rgba(0, 0, 0, 0)',
                '-webkit-touch-callout': 'none',
                '-webkit-user-select': 'none'
            },
            
            '.ood-mobile-todo-item:last-child': {
                'border-bottom': 'none'
            },
            
            '.ood-mobile-todo-item:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            // 添加移动端触摸状态样式
            '.ood-mobile-todo-item-active': {
                'background-color': 'var(--mobile-bg-secondary)',
                'transform': 'scale(0.98)',
                'transition': 'all 0.1s ease'
            },
            
            '.ood-mobile-todo-item-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-todo-checkbox': {
                width: '20px',
                height: '20px',
                'border-radius': '50%',
                border: '2px solid var(--mobile-border-color)',
                'margin-right': 'var(--mobile-spacing-md)',
                'flex-shrink': 0,
                'margin-top': '2px'
            },
            
            '.ood-mobile-todo-checkbox-checked': {
                'background-color': 'var(--mobile-primary)',
                'border-color': 'var(--mobile-primary)',
                'position': 'relative'
            },
            
            '.ood-mobile-todo-checkbox-checked::after': {
                content: '""',
                position: 'absolute',
                left: '6px',
                top: '2px',
                width: '4px',
                height: '8px',
                'border-right': '2px solid white',
                'border-bottom': '2px solid white',
                transform: 'rotate(45deg)'
            },
            
            '.ood-mobile-todo-content': {
                flex: 1
            },
            
            '.ood-mobile-todo-title': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '500',
                color: 'var(--mobile-text-primary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-todo-completed .ood-mobile-todo-title': {
                'text-decoration': 'line-through',
                color: 'var(--mobile-text-secondary)'
            },
            
            '.ood-mobile-todo-due': {
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-text-secondary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-todo-priority': {
                display: 'inline-block',
                'font-size': 'var(--mobile-font-xs)',
                'padding': '2px 6px',
                'border-radius': 'var(--mobile-border-radius)',
                'font-weight': '500'
            },
            
            '.ood-mobile-todo-priority-high': {
                'background-color': 'var(--mobile-danger-light)',
                color: 'var(--mobile-danger)'
            },
            
            '.ood-mobile-todo-priority-medium': {
                'background-color': 'var(--mobile-warning-light)',
                color: 'var(--mobile-warning)'
            },
            
            '.ood-mobile-todo-priority-low': {
                'background-color': 'var(--mobile-success-light)',
                color: 'var(--mobile-success)'
            }
        },
        
        DataModel: {
            // 基础属性
            caption: {
                caption: '待办事项标题',
                ini: '待办事项',
                action: function(value) {
                    var profile = this;
                    profile.getRoot().attr('aria-label', value || '待办事项');
                }
            },
            
            width: {
                caption: '组件宽度',
                $spaceunit: 1,
                ini: '100%'
            },
            
            height: {
                caption: '组件高度',
                $spaceunit: 1,
                ini: 'auto'
            },
            
            // 待办事项数据
            todos: {
                caption: '待办事项数据',
                ini: [
                    {
                        id: '1',
                        title: '完成季度报告',
                        dueDate: '2025-09-20',
                        priority: 'high',
                        completed: false,
                        disabled: false
                    },
                    {
                        id: '2',
                        title: '准备会议材料',
                        dueDate: '2025-09-18',
                        priority: 'medium',
                        completed: true,
                        disabled: false
                    },
                    {
                        id: '3',
                        title: '回复客户邮件',
                        dueDate: '2025-09-15',
                        priority: 'low',
                        completed: false,
                        disabled: false
                    }
                ],
                action: function(value) {
                    this.boxing().setTodos(value);
                }
            },
            
            // 事件处理器
            onTodoClick: {
                caption: '待办事项点击事件处理器',
                ini: null
            },
            
            onTodoCompleteChange: {
                caption: '待办事项完成状态改变事件处理器',
                ini: null
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
                profile.boxing().setTodos(profile.properties.todos);
            });
        }
    }
});