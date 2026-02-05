/**
 * 移动端通知公告组件（规范版本）
 * 用于显示企业内部的通知、公告等信息
 * 遵循OOD开发规范重构
 */
ood.Class("ood.Mobile.OA.new.Notice", ["ood.UI", "ood.absList"], {
    Instance: {
        initialize: function() {
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
            this._notices = [];
            this._selectedNotice = null;
        },
        
        Dependencies: [],
        Required: [],
        
        // 移除properties定义，交由OOD底层实现
        // properties: {
        //     // 组件基础属性
        //     width: '100%',
        //     height: 'auto',
        //     caption: '通知公告'
        // },

        // 移除events中的内部函数实现
        // events: {
        //     // 通知点击事件
        //     onNoticeClick: function(profile, index, notice) {
        //         // 默认事件处理
        //     },
        //     // 通知状态改变事件
        //     onNoticeStatusChange: function(profile, index, notice, status) {
        //         // 默认事件处理
        //     },
        //     // 数据加载完成事件
        //     onDataLoad: function(profile, data) {
        //         // 数据加载完成处理
        //     },
        //     // 数据加载错误事件
        //     onDataError: function(profile, error) {
        //         // 数据加载错误处理
        //     }
        // },
        
        // 移除冗余的get/set方法
        functions: {
            // // 获取选中的通知
            // getSelectedNotice: function() {
            //     return this._selectedNotice;
            // },
            
            // // 设置选中的通知
            // setSelectedNotice: function(notice) {
            //     this._selectedNotice = notice;
            // }
        },
        
        iniComponents: function() {
            // [[Code created by EUSUI RAD Studio
            var host = this, children = [], properties = {}, append = function(child) {
                children.push(child.get(0));
            };
            ood.merge(properties, this.properties);
            
            // 创建容器组件
            var container = ood.create("ood.UI.Div");
            container.setHost(host, "container");
            container.setLeft(0);
            container.setTop(0);
            container.setWidth("100%");
            container.setHeight("100%");
            // 使用 KEY:VALUE 方式组合样式
            container.setCustomStyle({
                "KEY": {
                    "padding": "var(--mobile-spacing-md)"
                }
            });
            append(container);
            
            return children;
            // ]]Code created by EUSUI RAD Studio
        }
    },
    
    // 移除Static中的Templates和Appearances
    Static: {
        // Templates: {
        //     tagName: 'div',
        //     className: 'ood-mobile-notice',
        //     style: '{_style}',
        //     
        //     CONTAINER: {
        //         tagName: 'div',
        //         className: 'ood-mobile-notice-container'
        //     }
        // },
        // 
        // Appearances: {
        //     KEY: {
        //         position: 'relative',
        //         width: '100%',
        //         'background-color': 'var(--mobile-bg-primary)'
        //     },
        //     
        //     CONTAINER: {
        //         padding: 'var(--mobile-spacing-md)'
        //     },
        //     
        //     '.ood-mobile-notice-item': {
        //         'border-radius': 'var(--mobile-border-radius)',
        //         'border': '1px solid var(--mobile-border-color)',
        //         padding: 'var(--mobile-spacing-md)',
        //         'margin-bottom': 'var(--mobile-spacing-md)',
        //         cursor: 'pointer',
        //         transition: 'all 0.2s ease',
        //         'background-color': 'var(--mobile-bg-primary)',
        //         // 添加移动端触摸反馈样式
        //         '-webkit-tap-highlight-color': 'rgba(0, 0, 0, 0)',
        //         '-webkit-touch-callout': 'none',
        //         '-webkit-user-select': 'none'
        //     },
        //     
        //     '.ood-mobile-notice-item:hover': {
        //         'box-shadow': 'var(--mobile-shadow-light)'
        //     },
        //     
        //     // 添加移动端触摸状态样式
        //     '.ood-mobile-notice-item-active': {
        //         'box-shadow': 'var(--mobile-shadow-light)',
        //         'transform': 'scale(0.98)',
        //         'transition': 'all 0.1s ease'
        //     },
        //     
        //     '.ood-mobile-notice-item-disabled': {
        //         opacity: 0.5,
        //         cursor: 'not-allowed'
        //     },
        //     
        //     '.ood-mobile-notice-title': {
        //         'font-size': 'var(--mobile-font-md)',
        //         'font-weight': '600',
        //         color: 'var(--mobile-text-primary)',
        //         'margin-bottom': 'var(--mobile-spacing-xs)'
        //     },
        //     
        //     '.ood-mobile-notice-important .ood-mobile-notice-title': {
        //         color: 'var(--mobile-danger)'
        //     },
        //     
        //     '.ood-mobile-notice-time': {
        //         'font-size': 'var(--mobile-font-xs)',
        //         color: 'var(--mobile-text-secondary)',
        //         'margin-bottom': 'var(--mobile-spacing-xs)'
        //     },
        //     
        //     '.ood-mobile-notice-summary': {
        //         'font-size': 'var(--mobile-font-sm)',
        //         color: 'var(--mobile-text-secondary)',
        //         'line-height': '1.4'
        //     },
        //     
        //     '.ood-mobile-notice-read .ood-mobile-notice-title': {
        //         color: 'var(--mobile-text-secondary)'
        //     },
        //     
        //     '.ood-mobile-notice-read .ood-mobile-notice-summary': {
        //         color: 'var(--mobile-text-tertiary)'
        //     }
        // }
    }
});