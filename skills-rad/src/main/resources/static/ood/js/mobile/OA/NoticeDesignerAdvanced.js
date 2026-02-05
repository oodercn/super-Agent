/**
 * 移动端通知公告组件（高级设计器版本）
 * 展示更详细的事件处理和动作函数调用示例
 * 遵循OOD框架标准设计理念重构版本
 */
ood.Class("ood.Mobile.OA.NoticeDesignerAdvanced", 'ood.Module',{
    Instance:{
        initialize : function(){
            // 组件初始化
            this.constructor.upper.prototype.initialize.apply(this, arguments);
            this._notices = [];
            this._selectedNotice = null;
            this._dataSource = null; // 数据源关联
        },
        Dependencies:[],
        Required:[],
        properties : {
            // 组件基础属性
            width: '100%',
            height: 'auto',
            caption: '通知公告',
            // 数据关联属性
            dataSource: null, // 数据源配置
            dataField: 'notices', // 数据字段映射
            // 动作关联属性
            actions: {
                onClick: null,
                onStatusChange: null,
                onLoad: null,
                onError: null
            }
        },

        events:{
            // 通知点击事件
            onNoticeClick: function(profile, index, notice) {
                // 默认事件处理
                console.log("通知点击事件触发:", index, notice.title);
            },
            // 通知状态改变事件
            onNoticeStatusChange: function(profile, index, notice, status) {
                // 默认事件处理
                console.log("通知状态改变事件触发:", index, notice.title, status);
            },
            // 数据源改变事件
            onDataSourceChange: function(profile, dataSource) {
                // 数据源改变处理
                console.log("数据源改变事件触发:", dataSource);
            },
            // 数据加载完成事件
            onDataLoad: function(profile, data) {
                console.log("数据加载完成事件触发:", data);
            },
            // 数据加载错误事件
            onDataError: function(profile, error) {
                console.log("数据加载错误事件触发:", error);
            }
        },
        functions:{
            // 设置通知数据
            setNotices: function(notices) {
                this.properties.notices = notices || [];
                // 触发数据更新
                if (this._onNoticesChange) {
                    this._onNoticesChange(notices);
                }
                // 更新UI显示 - 使用 ood.UI.List 组件的内置方法
                if (this.noticeList) {
                    this.noticeList.setItems(this.getNotices());
                }
                
                // 执行关联动作 - 使用标准的 OOD 框架动作定义方式
                if (this.properties.actions && this.properties.actions.onLoad) {
                    // 使用标准的 OOD 框架动作定义方式
                    ood.pseudocode._callFunctions(this.properties.actions.onLoad, [{data: notices}], this.getModule());
                }
            },
            // 获取通知数据
            getNotices: function() {
                return this.properties.notices || [];
            },
            // 获取选中的通知
            getSelectedNotice: function() {
                return this._selectedNotice;
            },
            // 设置选中的通知
            setSelectedNotice: function(notice) {
                this._selectedNotice = notice;
            }
        },
        iniComponents : function(){
            // [[Code created by JDSEasy RAD Studio
            var host=this, children=[], properties={}, append=function(child){children.push(child.get(0));};
            ood.merge(properties, this.properties);

            // 创建APICaller实例 - 在iniComponents中预先初始化
            var noticeApiCaller = ood.create("ood.APICaller");
            noticeApiCaller.setHost(host, "noticeApiCaller");
            noticeApiCaller.setName("noticeApiCaller");
            // 设置默认配置
            noticeApiCaller.setQueryURL("/api/notices");
            noticeApiCaller.setQueryMethod("GET");
            noticeApiCaller.setProxyType("AJAX");
            noticeApiCaller.setRequestType("JSON");
            
            // 设置成功回调 - 使用标准的 OOD 框架动作定义方式
            noticeApiCaller.onData([
                {
                    "desc": "数据加载成功",
                    "type": "function",
                    "target": "setNotices",
                    "args": ["{temp$.okData}"]
                },
                {
                    "desc": "触发数据加载完成事件",
                    "type": "event",
                    "target": "onDataLoad",
                    "args": ["{temp$.okData}"]
                }
            ]);
            
            // 设置错误回调 - 使用标准的 OOD 框架动作定义方式
            noticeApiCaller.onError([
                {
                    "desc": "数据加载失败",
                    "type": "event",
                    "target": "onDataError",
                    "args": ["{temp$.koData}"]
                },
                {
                    "desc": "执行关联动作",
                    "type": "action",
                    "target": "actions.onError",
                    "args": [{error: "{temp$.koData}"}]
                }
            ]);
            
            // 将APICaller添加到组件中
            append(noticeApiCaller);

            // 创建 List 组件用于显示通知列表 - 使用 OOD 的低代码组件
            var noticeList = ood.create("ood.UI.List");
            noticeList.setHost(host, "noticeList");
            noticeList.setName("noticeList");
            noticeList.setLeft(0);
            noticeList.setTop(0);
            noticeList.setWidth("100%");
            noticeList.setHeight("100%");
            noticeList.setSelMode("single"); // 单选模式
            noticeList.setItems([]); // 初始空数据
            
            // 设置列表项选中事件 - 使用 List 组件支持的 onItemSelected 事件
            noticeList.onItemSelected([
                {
                    "desc": "设置选中通知",
                    "type": "function",
                    "target": "setSelectedNotice",
                    "args": ["{args[1]}"] // args[1] 是选中的项数据
                },
                {
                    "desc": "触发点击事件",
                    "type": "event",
                    "target": "onNoticeClick",
                    "args": ["{args[2]}", "{args[1]}"] // args[2] 是索引, args[1] 是项数据
                },
                {
                    "desc": "执行关联动作",
                    "type": "action",
                    "target": "actions.onClick",
                    "args": [{
                        index: "{args[2]}",
                        notice: "{args[1]}"
                    }]
                }
            ]);
            
            // 将 List 组件添加到组件中
            append(noticeList);

            return children;
            // ]]Code created by JDSEasy RAD Studio
        }
    } ,
    Static:{
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-notice',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-notice-container'
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
            
            '.ood-mobile-notice-item': {
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
            
            '.ood-mobile-notice-item:hover': {
                'box-shadow': 'var(--mobile-shadow-light)'
            },
            
            // 添加移动端触摸状态样式
            '.ood-mobile-notice-item-active': {
                'box-shadow': 'var(--mobile-shadow-light)',
                'transform': 'scale(0.98)',
                'transition': 'all 0.1s ease'
            },
            
            '.ood-mobile-notice-item-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-notice-title': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '600',
                color: 'var(--mobile-text-primary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-notice-important .ood-mobile-notice-title': {
                color: 'var(--mobile-danger)'
            },
            
            '.ood-mobile-notice-time': {
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-text-secondary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-notice-summary': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'line-height': '1.4'
            },
            
            '.ood-mobile-notice-read .ood-mobile-notice-title': {
                color: 'var(--mobile-text-secondary)'
            },
            
            '.ood-mobile-notice-read .ood-mobile-notice-summary': {
                color: 'var(--mobile-text-tertiary)'
            }
        }
    }
});