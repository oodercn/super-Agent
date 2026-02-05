/**
 * 移动端审批流程组件
 * 用于显示和处理审批流程
 */
ood.Class("ood.Mobile.OA.ApprovalFlow", ["ood.UI", "ood.absList"], {
    Instance: {
        Initialize: function() {
            this.constructor.upper.prototype.Initialize.call(this);
            this.initApprovalFeatures();
        },
        
        initApprovalFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-approval ood-mobile-component');
            this.bindTouchEvents();
        },
        
        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // ApprovalFlow组件的尺寸调整逻辑

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
            
            // 审批项点击事件
            container.on('click', '.ood-mobile-approval-item', function(e) {
                var item = ood(this);
                var index = parseInt(item.attr('data-index'));
                var approval = self._approvals[index];
                
                if (approval && !item.hasClass('ood-mobile-approval-item-disabled')) {
                    self.onApprovalClick(index, approval);
                }
            });
            
            // 操作按钮点击事件
            container.on('click', '.ood-mobile-approval-action', function(e) {
                e.stopPropagation();
                var button = ood(this);
                var action = button.attr('data-action');
                var item = button.closest('.ood-mobile-approval-item');
                var index = parseInt(item.attr('data-index'));
                var approval = self._approvals[index];
                
                if (approval && !item.hasClass('ood-mobile-approval-item-disabled')) {
                    self.onActionClick(index, approval, action);
                }
            });
            
            // 添加移动端触摸事件支持
            container.on('touchstart', '.ood-mobile-approval-item', function(e) {
                ood(this).addClass('ood-mobile-approval-item-active');
            });
            
            container.on('touchend', '.ood-mobile-approval-item', function(e) {
                ood(this).removeClass('ood-mobile-approval-item-active');
            });
            
            container.on('touchcancel', '.ood-mobile-approval-item', function(e) {
                ood(this).removeClass('ood-mobile-approval-item-active');
            });
            
            // 操作按钮触摸事件
            container.on('touchstart', '.ood-mobile-approval-action', function(e) {
                ood(this).addClass('ood-mobile-approval-action-active');
            });
            
            container.on('touchend', '.ood-mobile-approval-action', function(e) {
                ood(this).removeClass('ood-mobile-approval-action-active');
            });
            
            container.on('touchcancel', '.ood-mobile-approval-action', function(e) {
                ood(this).removeClass('ood-mobile-approval-action-active');
            });
        },
        
        setApprovals: function(approvals) {
            this._approvals = approvals || [];
            this.renderApprovals();
        },
        
        getApprovals: function() {
            return this._approvals || [];
        },
        
        renderApprovals: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            container.html('');
            
            for (var i = 0; i < this._approvals.length; i++) {
                var approval = this._approvals[i];
                var approvalElement = this.createApprovalElement(approval, i);
                container.append(approvalElement);
            }
        },
        
        createApprovalElement: function(approval, index) {
            var approvalEl = ood('<div class="ood-mobile-approval-item" data-index="' + index + '"></div>');
            
            // 标题和状态
            var header = ood('<div class="ood-mobile-approval-header"></div>');
            
            var title = ood('<div class="ood-mobile-approval-title">' + approval.title + '</div>');
            header.append(title);
            
            if (approval.status) {
                var status = ood('<div class="ood-mobile-approval-status ood-mobile-approval-status-' + approval.status + '">' + 
                    this.getStatusText(approval.status) + '</div>');
                header.append(status);
            }
            
            approvalEl.append(header);
            
            // 申请人信息
            if (approval.applicant) {
                var applicant = ood('<div class="ood-mobile-approval-applicant">' + approval.applicant + '</div>');
                approvalEl.append(applicant);
            }
            
            // 申请时间
            if (approval.applyTime) {
                var applyTime = ood('<div class="ood-mobile-approval-time">' + approval.applyTime + '</div>');
                approvalEl.append(applyTime);
            }
            
            // 审批类型
            if (approval.type) {
                var type = ood('<div class="ood-mobile-approval-type">' + approval.type + '</div>');
                approvalEl.append(type);
            }
            
            // 紧急程度
            if (approval.urgent) {
                var urgent = ood('<div class="ood-mobile-approval-urgent">紧急</div>');
                approvalEl.append(urgent);
            }
            
            // 操作按钮
            if (approval.status === 'pending' && !approval.disabled) {
                var actions = ood('<div class="ood-mobile-approval-actions"></div>');
                
                var approveBtn = ood('<button class="ood-mobile-approval-action ood-mobile-approval-action-approve" data-action="approve">同意</button>');
                var rejectBtn = ood('<button class="ood-mobile-approval-action ood-mobile-approval-action-reject" data-action="reject">拒绝</button>');
                
                actions.append(approveBtn);
                actions.append(rejectBtn);
                
                approvalEl.append(actions);
            }
            
            // 禁用状态
            if (approval.disabled) {
                approvalEl.addClass('ood-mobile-approval-item-disabled');
            }
            
            return approvalEl;
        },
        
        getStatusText: function(status) {
            var statusMap = {
                'pending': '待审批',
                'approved': '已同意',
                'rejected': '已拒绝',
                'cancelled': '已撤销'
            };
            return statusMap[status] || status;
        },
        
        onApprovalClick: function(index, approval) {
            var profile = this.get(0);
            
            if (profile.onApprovalClick) {
                profile.boxing().onApprovalClick(profile, index, approval);
            }
        },
        
        onActionClick: function(index, approval, action) {
            var profile = this.get(0);
            
            // 更新审批状态
            if (action === 'approve') {
                approval.status = 'approved';
            } else if (action === 'reject') {
                approval.status = 'rejected';
            }
            
            // 重新渲染该项
            var container = profile.getSubNode('CONTAINER');
            var item = container.find('.ood-mobile-approval-item[data-index="' + index + '"]');
            var newElement = this.createApprovalElement(approval, index);
            item.replaceWith(newElement);
            
            if (profile.onActionClick) {
                profile.boxing().onActionClick(profile, index, approval, action);
            }
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentApprovals = self.getApprovals();
                if (typeof index === 'undefined') {
                    currentApprovals = currentApprovals.concat(items);
                } else {
                    var insertIndex = isBefore ? index : index + 1;
                    currentApprovals.splice.apply(currentApprovals, [insertIndex, 0].concat(items));
                }
                
                self.setApprovals(currentApprovals);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentApprovals = self.getApprovals();
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var idx = parseInt(indices[i]);
                    if (idx >= 0 && idx < currentApprovals.length) {
                        currentApprovals.splice(idx, 1);
                    }
                }
                
                self.setApprovals(currentApprovals);
            });
        },
        
        clearItems: function() {
            return this.setApprovals([]);
        },
        
        getItems: function() {
            return this.getApprovals();
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
            className: 'ood-mobile-approval',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-approval-container'
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
            
            '.ood-mobile-approval-item': {
                'border-radius': 'var(--mobile-border-radius)',
                'border': '1px solid var(--mobile-border-color)',
                padding: 'var(--mobile-spacing-md)',
                'margin-bottom': 'var(--mobile-spacing-md)',
                cursor: 'pointer',
                transition: 'all 0.2s ease',
                'background-color': 'var(--mobile-bg-primary)',
                // 添加移动端触摸反馈样式
                '-webkit-tap-highlight-color': 'rgba(0, 0, 0, 0)',
                '-webkit-touch-callout': 'none',
                '-webkit-user-select': 'none'
            },
            
            '.ood-mobile-approval-item:hover': {
                'box-shadow': 'var(--mobile-shadow-light)'
            },
            
            // 添加移动端触摸状态样式
            '.ood-mobile-approval-item-active': {
                'box-shadow': 'var(--mobile-shadow-light)',
                'transform': 'scale(0.98)',
                'transition': 'all 0.1s ease'
            },
            
            '.ood-mobile-approval-item-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-approval-header': {
                display: 'flex',
                'justify-content': 'space-between',
                'align-items': 'flex-start',
                'margin-bottom': 'var(--mobile-spacing-sm)'
            },
            
            '.ood-mobile-approval-title': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'flex': 1
            },
            
            '.ood-mobile-approval-status': {
                'font-size': 'var(--mobile-font-xs)',
                'padding': '2px 8px',
                'border-radius': 'var(--mobile-border-radius)',
                'font-weight': '500'
            },
            
            '.ood-mobile-approval-status-pending': {
                'background-color': 'var(--mobile-warning-light)',
                color: 'var(--mobile-warning)'
            },
            
            '.ood-mobile-approval-status-approved': {
                'background-color': 'var(--mobile-success-light)',
                color: 'var(--mobile-success)'
            },
            
            '.ood-mobile-approval-status-rejected': {
                'background-color': 'var(--mobile-danger-light)',
                color: 'var(--mobile-danger)'
            },
            
            '.ood-mobile-approval-status-cancelled': {
                'background-color': 'var(--mobile-text-tertiary-light)',
                color: 'var(--mobile-text-tertiary)'
            },
            
            '.ood-mobile-approval-applicant': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-approval-time': {
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-text-tertiary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-approval-type': {
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-primary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-approval-urgent': {
                display: 'inline-block',
                'font-size': 'var(--mobile-font-xs)',
                'padding': '2px 6px',
                'border-radius': 'var(--mobile-border-radius)',
                'background-color': 'var(--mobile-danger)',
                color: 'white',
                'font-weight': '500'
            },
            
            '.ood-mobile-approval-actions': {
                display: 'flex',
                'justify-content': 'flex-end',
                'margin-top': 'var(--mobile-spacing-md)',
                'gap': 'var(--mobile-spacing-sm)'
            },
            
            '.ood-mobile-approval-action': {
                'border': 'none',
                'border-radius': 'var(--mobile-border-radius)',
                'padding': '6px 12px',
                'font-size': 'var(--mobile-font-sm)',
                cursor: 'pointer',
                transition: 'background-color 0.2s ease',
                // 添加移动端触摸反馈样式
                '-webkit-tap-highlight-color': 'rgba(0, 0, 0, 0)',
                '-webkit-touch-callout': 'none',
                '-webkit-user-select': 'none'
            },
            
            '.ood-mobile-approval-action-approve': {
                'background-color': 'var(--mobile-success)',
                color: 'white'
            },
            
            '.ood-mobile-approval-action-approve:hover': {
                'background-color': 'var(--mobile-success-dark)'
            },
            
            // 添加移动端触摸状态样式
            '.ood-mobile-approval-action-active': {
                'transform': 'scale(0.95)',
                'transition': 'all 0.1s ease'
            },
            
            '.ood-mobile-approval-action-reject': {
                'background-color': 'var(--mobile-danger)',
                color: 'white'
            },
            
            '.ood-mobile-approval-action-reject:hover': {
                'background-color': 'var(--mobile-danger-dark)'
            }
        },
        
        DataModel: {
            // 基础属性
            caption: {
                caption: '审批流程标题',
                ini: '审批流程',
                action: function(value) {
                    var profile = this;
                    profile.getRoot().attr('aria-label', value || '审批流程');
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
            
            // 审批数据
            approvals: {
                caption: '审批数据',
                ini: [
                    {
                        id: '1',
                        title: '请假申请',
                        applicant: '张三',
                        applyTime: '2025-09-14 09:30',
                        type: '请假',
                        status: 'pending',
                        urgent: false,
                        disabled: false
                    },
                    {
                        id: '2',
                        title: '出差申请',
                        applicant: '李四',
                        applyTime: '2025-09-13 14:20',
                        type: '出差',
                        status: 'approved',
                        urgent: true,
                        disabled: false
                    },
                    {
                        id: '3',
                        title: '报销申请',
                        applicant: '王五',
                        applyTime: '2025-09-12 11:15',
                        type: '报销',
                        status: 'rejected',
                        urgent: false,
                        disabled: false
                    }
                ],
                action: function(value) {
                    this.boxing().setApprovals(value);
                }
            },
            
            // 事件处理器
            onApprovalClick: {
                caption: '审批项点击事件处理器',
                ini: null
            },
            
            onActionClick: {
                caption: '审批操作点击事件处理器',
                ini: null
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
                profile.boxing().setApprovals(profile.properties.approvals);
            });
        }
    }
});