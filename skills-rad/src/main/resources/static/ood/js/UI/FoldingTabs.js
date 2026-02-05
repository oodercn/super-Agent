/**
 * FoldingTabs - 可折叠标签页组件
 * 基于CodeBuddy风格优化的现代化可折叠标签页实现
 *
 * @class ood.UI.FoldingTabs
 * @extends ood.UI.Tabs
 * @version 1.0.0
 * @author CodeBuddy
 * @description 提供可折叠的标签页功能，支持多种主题、响应式布局和可访问性
 */
ood.Class("ood.UI.FoldingTabs", "ood.UI.Tabs", {
    /**
     * 实例方法
     * @namespace Instance
     */
    Instance: {
        // 添加 iniProp 对象来存储默认值
        iniProp: {
            items: [
                {id: 'a', caption: 'tab1', message: "normal"},
                {id: 'b', caption: 'tab2', message: "with image", imageClass: "ri-image-line"},
                {id: 'c', caption: 'tab3', message: "height:100", height: 100},
                {
                    id: 'd',
                    caption: '$RAD.widgets.collapsible',
                    message: "with commands",
                    closeBtn: true,
                    optBtn: 'ood-uicmd-opt',
                    popBtn: true
                }
            ],
            value: 'a'
        },

        /**
         * 设置主题样式
         * @param {string} theme - 主题名称: 'light', 'dark', 'high-contrast'
         * @returns {Object} this - 支持链式调用
         */
        setTheme: function (theme) {
            return this.each(function (profile) {
                profile.properties.theme = theme;
                var root = profile.getRoot();

                // 应用主题类
                root.addClass('foldingtabs-themed')
                    .removeClass('foldingtabs-dark foldingtabs-hc');

                if (theme === 'dark') {
                    root.addClass('foldingtabs-dark');
                } else if (theme === 'high-contrast') {
                    root.addClass('foldingtabs-hc');
                }

                // 保存主题设置到本地存储
                localStorage.setItem('foldingtabs-theme', theme);
            });
        },


        /**
         * 增强可访问性支持
         * @returns {Object} this - 支持链式调用
         */
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNodes('ITEM');

                // 为容器添加ARIA属性
                this._setContainerAccessibility(root);

                // 为每个项目添加ARIA属性
                items.forEach(function (item, index) {
                    this._setItemAccessibility(profile, item);
                }, this);
            });
        },


        /**
         * 响应式布局调整
         * @returns {Object} this - 支持链式调用
         */
        adjustLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    viewportWidth = ood(document.body).cssSize().width;
                // 应用响应式布局类
                this._applyResponsiveClasses(root, viewportWidth);
            });
        },


        /**
         * 获取当前主题
         * @returns {string} 当前主题名称
         */
        getTheme: function () {
            var profile = this.get(0);
            return profile.properties.theme || localStorage.getItem('foldingtabs-theme') || 'light';
        },

        /**
         * 切换主题
         * @returns {Object} this - 支持链式调用
         */
        toggleTheme: function () {
            var currentTheme = this.getTheme();
            var nextTheme = currentTheme === 'light' ? 'dark' :
                (currentTheme === 'dark' ? 'high-contrast' : 'light');
            this.setTheme(nextTheme);
            return this;
        },


        /**
         * 更新UI值
         * @param {Object} box - 盒子对象
         * @param {string} value - 值
         * @param {Object} profile - 组件配置
         * @param {Object} item - 项目
         * @param {Event} event - 事件对象
         * @param {HTMLElement} source - 源元素
         * @param {number} checktype - 检查类型
         * @private
         */
        _updateUIValue: function (box, value, profile, item, event, source, checktype) {
            if (box.getUIValue() !== value) {
                box.setUIValue(value, null, null, 'click');

                // 触发选中事件
                if (box.get(0) && box.getUIValue() === value) {
                    box.onItemSelected(profile, item, event, source, checktype);
                }
            }
        },
        /**
         * 数据模型定义
         * @namespace DataModel
         */

        /**
         * 设置项目背景信息
         * @param {Object} item - 项目对象
         * @param {Object} properties - 组件属性
         * @private
         */
        _setBackgroundInfo: function (item, properties) {
            var t;
            item._bginfo = "";

            // 背景颜色
            if (t = item.panelBgClr || properties.panelBgClr) {
                item._bginfo += "background-color:" + t + ";";
            }

            // 背景图片
            if (t = item.panelBgImg || properties.panelBgImg) {
                item._bginfo += "background-image:url(" + ood.adjustRes(t) + ");";
            }

            // 背景位置
            if (t = item.panelBgImgPos || properties.panelBgImgPos) {
                item._bginfo += "background-position:" + t + ";";
            }

            // 背景重复
            if (t = item.panelBgImgRepeat || properties.panelBgImgRepeat) {
                item._bginfo += "background-repeat:" + t + ";";
            }

            // 背景附着
            if (t = item.panelBgImgAttachment || properties.panelBgImgAttachment) {
                item._bginfo += "background-attachment:" + t + ";";
            }
        },

        /**
         * 设置溢出样式
         * @param {Object} item - 项目对象
         * @param {Object} properties - 组件属性
         * @private
         */
        _setOverflowStyle: function (item, properties) {
            if (ood.isStr(item.overflow)) {
                item._overflow = item.overflow.indexOf(':') != -1 ?
                    (item.overflow) :
                    (item.overflow ? ("overflow:" + item.overflow) : "");
            } else if (ood.isStr(properties.overflow)) {
                item._overflow = properties.overflow.indexOf(':') != -1 ?
                    (properties.overflow) :
                    (properties.overflow ? ("overflow:" + properties.overflow) : "");
            }
        },


        /**
         * 设置项目可访问性属性
         * @param {Object} profile - 组件配置
         * @param {Object} item - 项目元素
         * @private
         */
        _setItemAccessibility: function (profile, item) {
            var itemId = profile.getItemIdByDom(item),
                isExpanded = profile.getItemByItemId(itemId)._show || false;

            // 设置标签属性
            item.attr({
                'role': 'tab',
                'aria-selected': isExpanded,
                'aria-controls': 'foldingtabs-panel-' + itemId,
                'aria-expanded': isExpanded
            });

            // 设置内容区域属性
            var body = profile.getSubNode('BODY', profile.getSubIdByItemId(itemId));
            if (body) {
                body.attr({
                    'id': 'foldingtabs-panel-' + itemId,
                    'role': 'tabpanel',
                    'aria-labelledby': 'foldingtabs-tab-' + itemId
                });
            }

            // 设置标题属性
            var title = profile.getSubNode('TITLE', profile.getSubIdByItemId(itemId));
            if (title) {
                title.attr({
                    'id': 'foldingtabs-tab-' + itemId,
                    'role': 'button',
                    'tabindex': '0'
                });
            }
        },


        /**
         * 设置容器可访问性属性
         * @param {Object} root - 根元素
         * @private
         */
        _setContainerAccessibility: function (root) {
            root.attr({
                'role': 'tablist',
                'aria-label': '可折叠标签页'
            });
        },

        /**
         * 初始化动画样式
         * @private
         */
        _initAnimationStyles: function () {
            if (!document.getElementById('ood-foldingtabs-animations')) {
                var style = document.createElement('style');
                style.id = 'ood-foldingtabs-animations';

                // 根据配置的过渡时间设置动画持续时间
                var duration = '0.3s';
                style.textContent = `
                    .ood-foldingtabs-body-open {
                        animation: ood-foldingtabs-open ${duration} ease-out;
                    }
                    
                    @keyframes ood-foldingtabs-open {
                        from { opacity: 0; transform: translateY(-10px); }
                        to { opacity: 1; transform: translateY(0); }
                    }
                `;
                document.head.appendChild(style);
            }
        },

        /**
         * 初始化主题
         * @param {Object} properties - 组件属性
         * @private
         */
        _initTheme: function (properties) {
            if (properties.theme) {
                this.setTheme(properties.theme);
            } else {
                // 从本地存储恢复主题
                var savedTheme = localStorage.getItem('foldingtabs-theme');
                if (savedTheme) {
                    this.setTheme(savedTheme);
                }
            }
        },

        /**
         * 应用响应式布局类
         * @param {Object} root - 根元素
         * @param {number} viewportWidth - 视口宽度
         * @private
         */
        _applyResponsiveClasses: function (root, viewportWidth) {
            // 移动设备布局 (< 768px)
            if (viewportWidth < 768) {
                root.addClass('foldingtabs-mobile');
            } else {
                root.removeClass('foldingtabs-mobile');
            }

            // 超小屏幕布局 (< 480px)
            if (viewportWidth < 480) {
                root.addClass('foldingtabs-tiny');
            } else {
                root.removeClass('foldingtabs-tiny');
            }
        },


        /**
         * 根据选择模式处理选择
         * @param {Object} profile - 组件配置
         * @param {Object} properties - 属性
         * @param {Object} item - 项目
         * @param {Object} box - 盒子对象
         * @param {Event} event - 事件对象
         * @param {HTMLElement} source - 源元素
         * @private
         */
        _handleSelectionByMode: function (profile, properties, item, box, event, source) {
            switch (properties.selMode) {
                // 多选模式
                case 'multi':
                    this._handleMultiSelection(profile, properties, item, box, event, source);
                    break;

                // 单选模式
                case 'single':
                    this._handleSingleSelection(profile, item, box, event, source);
                    break;
            }
        },

        /**
         * 处理多选模式
         * @param {Object} profile - 组件配置
         * @param {Object} properties - 属性
         * @param {Object} item - 项目
         * @param {Object} box - 盒子对象
         * @param {Event} event - 事件对象
         * @param {HTMLElement} source - 源元素
         * @private
         */
        _handleMultiSelection: function (profile, properties, item, box, event, source) {
            var value = box.getUIValue(),
                arr = value ? value.split(properties.valueSeparator) : [],
                checktype = 1;

            if (arr.length) {
                // 如果已选中则取消选中
                if (ood.arr.indexOf(arr, item.id) !== -1) {
                    ood.arr.removeValue(arr, item.id);
                    checktype = -1;
                }
                // 否则添加选中
                else {
                    arr.push(item.id);
                }

                // 排序并组合值
                arr.sort();
                value = arr.join(properties.valueSeparator);

                // 更新UI值
                this._updateUIValue(box, value, profile, item, event, source, checktype);
            }
        },

        /**
         * 处理单选模式
         * @param {Object} profile - 组件配置
         * @param {Object} item - 项目
         * @param {Object} box - 盒子对象
         * @param {Event} event - 事件对象
         * @param {HTMLElement} source - 源元素
         * @private
         */
        _handleSingleSelection: function (profile, item, box, event, source) {
            if (box.getUIValue() !== item.id) {
                box.setUIValue(item.id, null, null, 'click');

                // 触发选中事件
                if (box.get(0) && box.getUIValue() === item.id) {
                    box.onItemSelected(profile, item, event, source, 1);
                }
            }
        },

        _afterInsertItems: null
    },
    Static: {
        Templates: {
            tagName: 'div',
            style: '{_style};',
            BOX: {
                $order: 0,
                tagName: 'div',
                ITEMS: {
                    tagName: 'div',
                    text: "{items}"
                }
            },
            $submap: {
                items: {
                    ITEM: {
                        tagName: 'div',
                        className: 'ood-uiborder-flat ood-uiborder-radius {_checked} {_precheked} {itemClass} {disabled} {readonly} ood-foldingtabs-item',
                        style: '{_itemDisplay} {itemStyle} ',
                        HEAD: {
                            tagName: 'div',
                            style: "{_itemColor}",
                            className: 'ood-uibar {_checked} {_precheked} ood-foldingtabs-header',
                            TITLE: {
                                tabindex: '{_tabindex}',
                                TLEFT: {
                                    $order: 0,
                                    tagName: 'div',
                                    LTAGCMDS: {
                                        $order: 0,
                                        tagName: 'span',
                                        style: '{_ltagDisplay}',
                                        text: "{ltagCmds}"
                                    },
                                    TOGGLE: {
                                        $order: 1,
                                        className: 'oodfont {_tlgchecked} ood-foldingtabs-toggle',
                                        $fonticon: 'ood-uicmd-toggle'
                                    },
                                    ICON: {
                                        $order: 2,
                                        className: 'oodcon {imageClass} {picClass} ood-foldingtabs-icon',
                                        style: '{backgroundImage}{backgroundPosition}{backgroundSize}{backgroundRepeat}{iconFontSize}{imageDisplay}{iconStyle} {_iconColor}',
                                        text: '{iconFontCode}'
                                    },
                                    CAPTION: {
                                        $order: 3,
                                        className: "ood-title-node ood-foldingtabs-caption",
                                        style: "{_fontColor}",
                                        text: '{caption}'
                                    }
                                },
                                TRIGHT: {
                                    $order: 1,
                                    tagName: 'div',
                                    style: '{_capDisplay}',
                                    className: 'ood-foldingtabs-tright',
                                    MESSAGE: {
                                        $order: 0,
                                        className: 'ood-foldingtabs-message',
                                        text: '{message}'
                                    },
                                    CMDS: {
                                        $order: 2,
                                        RTAGCMDS: {
                                            $order: 0,
                                            tagName: 'span',
                                            style: '{_rtagDisplay}',
                                            className: 'ood-foldingtabs-tag-commands',
                                            text: "{rtagCmds}"
                                        },
                                        OPT: {
                                            $order: 1,
                                            className: 'oodfont {_opt}',
                                            $fonticon: 'ood-uicmd-opt',
                                            style: '{_opt}'
                                        },
                                        POP: {
                                            className: 'oodfont {popDisplay}',
                                            $fonticon: 'ood-uicmd-pop',
                                            style: '{popDisplay}',
                                            $order: 1
                                        },
                                        CLOSE: {
                                            className: 'oodfont',
                                            $fonticon: 'ood-uicmd-close',
                                            style: '{closeDisplay}',
                                            $order: 2
                                        }
                                    }
                                }
                            }
                        },
                        BODY: {
                            $order: 1,
                            tagName: 'div',
                            className: 'ood-uibase ood-foldingtabs-body',
                            BODYI: {
                                tagName: 'div',
                                className: 'ood-foldingtabs-body-inner',
                                PANEL: {
                                    tagName: 'div',
                                    style: '{_itemHeight};{_overflow};{_bginfo}',
                                    className: 'ood-uibase ood-uicontainer ood-foldingtabs-panel',
                                    text: ood.UI.$childTag
                                }
                            }
                        }
                    }
                },
                'items.ltagCmds': function (profile, template, v, tag, result) {
                    var me = arguments.callee,
                        map = me._m || (me._m = {'text': '.text', 'button': '.button', 'image': '.image'});
                    ood.UI.$doTemplate(profile, template, v, "items.tagCmds" + (map[v.type] || '.button'), result)
                },
                'items.rtagCmds': function (profile, template, v, tag, result) {
                    var me = arguments.callee,
                        map = me._m || (me._m = {'text': '.text', 'button': '.button', 'image': '.image'});
                    ood.UI.$doTemplate(profile, template, v, "items.tagCmds" + (map[v.type] || '.button'), result)
                },
                'items.tagCmds.text': ood.UI.$getTagCmdsTpl('text'),
                'items.tagCmds.button': ood.UI.$getTagCmdsTpl('button'),
                'items.tagCmds.image': ood.UI.$getTagCmdsTpl('image')
            }
        },
        /**
         * 外观样式定义 - CSS类映射
         * 设计器通过此映射将虚拟DOM结构转换为实际CSS类应用
         * @namespace Appearances
         */
        Appearances: {
            KEY: {
                padding: 'var(--ood-spacing-xs)'
            },
            BOX: {
                'box-shadow': 'var(--ood-shadow-sm)',
                'border-radius': 'var(--ood-radius-md)',
                'overflow': 'hidden',
                'background-color': 'var(--ood-bg)'
            },
            'LTAGCMDS, RTAGCMDS': {
                padding: 0,
                margin: 0,
                'vertical-align': 'middle'
            },
            ITEMS: {
                border: 0,
                position: 'relative',
                'background-color': 'var(--ood-bg)'
            },
            ITEM: {
                marginBottom: 'var(--ood-spacing-md)',
                padding: 0,
                position: 'relative',
                overflow: 'hidden',
                'border-radius': 'var(--ood-radius-md)',
                'box-shadow': 'var(--ood-shadow-sm)',
                transition: 'all var(--ood-transition-normal)',
                'background-color': 'var(--ood-bg-card)'
            },
            'HEAD, BODY, BODYI, PANEL': {
                position: 'relative'
            },
            CMDS: {
                padding: '.25em 0 0 .25em',
                'vertical-align': 'middle',
                position: 'relative'
            },
            BODY: {
                display: 'none',
                position: 'relative',
                'max-height': '0',
                opacity: '0',
                transition: 'all var(--transition-normal)',
                'border-radius': '0 0 var(--radius-md) var(--radius-md)'
            },
            BODYI: {
                padding: '0 .25em'
            },
            PANEL: {
                overflow: 'auto',
                padding: '1rem',
                'border-radius': '0 0 var(--radius-md) var(--radius-md)'
            },
            'ITEM-hover': {
                'box-shadow': 'var(--ood-shadow-md)',
                transform: 'translateY(-2px)',
                transition: 'all var(--ood-transition-normal)'
            },
            'ITEM-active, ITEM-checked': {},
            'ITEM-checked': {
                $order: 2
            },
            'ITEM-checked BODY': {
                $order: 2,
                display: 'block',
                'max-height': '800px',
                opacity: '1'
            },
            HEAD: {
                cursor: 'pointer',
                position: 'relative',
                overflow: 'hidden',
                'border-radius': 'var(--ood-radius-md) var(--ood-radius-md) 0 0',
                transition: 'all var(--ood-transition-fast)',
                'background-color': 'var(--ood-bg-header)',
                'border-bottom': '1px solid var(--ood-border)'
            },
            TITLE: {
                $order: 1,
                display: 'flex',
                'align-items': 'center',
                position: 'relative',
                'white-space': 'nowrap',
                overflow: 'hidden',
                padding: 'var(--ood-spacing-md)'
            },
            'CAPTION, MESSAGE': {
                padding: 'var(--ood-spacing-xs)',
                'vertical-align': 'middle'
            },
            MESSAGE: {
                'font-size': 'var(--ood-font-size-base)',
                color: 'var(--ood-text-muted)'
            },
            CAPTION: {
                'white-space': 'nowrap',
                flex: 1,
                'font-size': 'var(--ood-font-size-lg)',
                'font-weight': 500,
                color: 'var(--ood-text-heading)'
            },
            'ITEM-checked CAPTION': {
                $order: 2,
                'font-weight': '600',
                color: 'var(--ood-primary)'
            },
            TLEFT: {
                display: 'flex',
                'align-items': 'center',
                position: 'relative',
                'white-space': 'nowrap',
                overflow: 'hidden',
                flex: 1
            },
            TRIGHT: {
                display: 'flex',
                'align-items': 'center',
                position: 'relative',
                'white-space': 'nowrap',
                overflow: 'hidden',
                marginLeft: 'auto'
            },
            /* 现代化样式扩展 */
            '.ood-foldingtabs-toggle': {
                'font-size': '1.25rem',
                marginRight: '0.75rem',
                transition: 'transform var(--transition-normal)',
                color: 'var(--ood-text-secondary)'
            },
            '.ood-foldingtabs-toggle.-checked': {
                transform: 'rotate(90deg)',
                color: 'var(--ood-primary)'
            },
            '.ood-foldingtabs-icon': {
                'font-size': '1.25rem',
                marginRight: '0.75rem',
                transition: 'color var(--transition-fast)'
            },
            '.ood-foldingtabs-header:hover .ood-foldingtabs-icon': {
                color: 'var(--primary-color)'
            },
            '.ood-foldingtabs-caption': {
                transition: 'color var(--transition-fast)',
                'font-size': '1.1rem',
                'font-weight': 500
            },
            '.ood-foldingtabs-header:hover .ood-foldingtabs-caption': {
                color: 'var(--primary-color)'
            },
            '.ood-foldingtabs-body': {
                'border-radius': '0 0 var(--radius-md) var(--radius-md)',
                borderTop: 'none'
            },
            '.ood-foldingtabs-header': {
                'border-radius': 'var(--radius-md) var(--radius-md) 0 0',
                borderBottom: 'none'
            },
            '.ood-foldingtabs-item': {
                transition: 'all var(--transition-normal)'
            },
            '.ood-foldingtabs-item:hover': {
                'box-shadow': 'var(--shadow-md)',
                transform: 'translateY(-1px)'
            },
            '.ood-foldingtabs-panel': {
                'border-top': 'none'
            }
        },

        /**
         * 行为定义
         * @namespace Behaviors
         */
        Behaviors: {
            /**
             * 可拖动的元素键
             * @type {Array}
             */
            DraggableKeys: ['HEAD'],

            /**
             * 悬停效果元素
             * @type {Object}
             */
            HoverEffected: {
                OPT: 'OPT',
                CLOSE: 'CLOSE',
                POP: 'POP',
                ITEM: 'HEAD'
            },

            /**
             * 点击效果元素
             * @type {Object}
             */
            ClickEffected: {
                OPT: 'OPT',
                CLOSE: 'CLOSE',
                POP: 'POP',
                ITEM: 'HEAD'
            },

            /**
             * 项目集合行为
             * @type {Object}
             */
            ITEMS: {
                onMousedown: null,
                onDrag: null,
                onDragstop: null
            },

            /**
             * 头部行为
             * @type {Object}
             */
            HEAD: {
                /**
                 * 点击处理
                 * @param {Object} profile - 组件配置
                 * @param {Event} e - 事件对象
                 * @param {HTMLElement} src - 源元素
                 * @returns {boolean} 处理结果
                 */
                onClick: function (profile, e, src) {
                    // 只处理左键点击
                    if (ood.Event.getBtn(e) !== 'left') return;

                    var prop = profile.properties,
                        item = profile.getItemByDom(src),
                        box = profile.boxing();

                    // 项目不存在则退出
                    if (!item) return;

                    // 禁用状态检查
                    if (prop.disabled || item.disabled) return false;

                    // 只读状态检查
                    if (prop.readonly || item.readonly) return false;

                    // 聚焦标题
                    profile.getSubNode('TITLE').focus(true);

                    // 根据选择模式处理
                    profile.boxing()._handleSelectionByMode(profile, prop, item, box, e, src);
                },

                /**
                 * 键盘按下处理
                 * @param {Object} profile - 组件配置
                 * @param {Event} e - 事件对象
                 * @param {HTMLElement} src - 源元素
                 * @returns {boolean} 处理结果
                 */
                onKeydown: function (profile, e, src) {
                    var keys = ood.Event.getKey(e),
                        key = keys.key;

                    // 空格键或回车键触发点击
                    if (key === ' ' || key === 'enter') {
                        profile.getSubNode('HEAD', profile.getSubId(src)).onClick();
                        return false;
                    }
                }
            }
        },

        DataModel: {
            /**
             * 表达式
             * @type {Object}
             */
            expression: {
                ini: '',
                action: function () {
                    // 表达式变更时的处理
                }
            },

            /**
             * 图标颜色配置
             * @type {Object}
             */
            iconColors: null,

            /**
             * 项目颜色配置
             * @type {Object}
             */
            itemColors: null,

            /**
             * 字体颜色配置
             * @type {Object}
             */
            fontColors: null,

            /**
             * 自动字体颜色
             * @type {Object}
             */
            autoFontColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },

            /**
             * 自动图标颜色
             * @type {Object}
             */
            autoIconColor: {
                ini: true,
                action: function () {
                    this.boxing().refresh();
                }
            },

            /**
             * 自动项目颜色
             * @type {Object}
             */
            autoItemColor: {
                ini: false,
                action: function () {
                    this.boxing().refresh();
                }
            },

            /**
             * 水平边框宽度
             * @type {number}
             */
            $hborder: 0,

            /**
             * 垂直边框宽度
             * @type {number}
             */
            $vborder: 0,

            /**
             * 无面板模式
             * @type {boolean}
             */
            noPanel: null,

            /**
             * 无处理器模式
             * @type {boolean}
             */
            noHandler: null,

            /**
             * 水平对齐方式
             * @type {string}
             */
            HAlign: null,

            /**
             * 选择模式
             * @type {Object}
             */
            selMode: {
                ini: 'single',
                listbox: ['single', 'multi']
            },

            /**
             * 主题设置
             * @type {string}
             */
            theme: {
                ini: 'light',
                listbox: ['light', 'dark', 'high-contrast']
            },

            /**
             * 响应式布局
             * @type {boolean}
             */
            responsive: {
                ini: true
            },

            /**
             * 过渡动画持续时间
             * @type {string}
             */
            transitionDuration: {
                ini: 'normal',
                listbox: ['fast', 'normal', 'slow']
            },

            /**
             * 是否可折叠
             * @type {boolean}
             */
            collapsible: {
                ini: true
            },

            /**
             * 可访问性增强
             * @type {boolean}
             */
            enhancedAccessibility: {
                ini: true
            }
        },

        /**
         * 渲染触发器 - 组件渲染后执行初始化
         * @function RenderTrigger
         */
        RenderTrigger: function () {
            var self = this,
                pro = self.properties;

            // 添加动画样式
            self.boxing()._initAnimationStyles();

            // 初始化主题
            self.boxing()._initTheme(pro);

            // 初始化响应式设计
            if (pro.responsive !== false) {
                self.boxing().adjustLayout();
            }

            // 初始化可访问性
            self.boxing().enhanceAccessibility();
        },


        /**
         * 准备项目集合
         * @param {Object} profile - 组件配置
         * @param {Array} arr - 项目数组
         * @param {string} pid - 父ID
         * @returns {Array} 处理后的项目数组
         * @private
         */
        _prepareItems: function (profile, arr, pid) {
            // 设置第一个项目的预选中样式
            if (arr.length) {
                arr[0]._precheked = profile.getClass('ITEM', '-prechecked');
            }

            // 处理每个项目
            ood.arr.each(arr, function (item) {
                if (!item.index) {
                    item.index = ood.arr.indexOf(arr, item);
                }
                profile.boxing()._autoColor(item, item.index, profile.properties);
            });

            var obj = arguments.callee.upper.apply(this, arguments);
            var height = profile.$em2px(profile.boxing().getHeight());
            this._adjustPanelHeight(profile, height);
            // 调用父类方法
            return obj;
        },

        /**
         * 准备单个项目
         * @param {Object} profile - 组件配置
         * @param {Object} item - 项目对象
         * @param {Object} oitem - 原始项目对象
         * @param {string} pid - 父ID
         * @param {number} index - 索引
         * @param {number} len - 长度
         * @private
         */
        _prepareItem: function (profile, item, oitem, pid, index, len) {
            var dpn = 'display:none',
                p = profile.properties,
                t;

            // 设置显示属性
            item.closeDisplay = item.closeBtn ? '' : dpn;
            item.popDisplay = item.popBtn ? '' : dpn;
            item._opt = item.optBtn ? '' : dpn;
            item._itemDisplay = item.hidden ? dpn : '';

            // 应用自动颜色
            profile.boxing()._autoColor(item, index, p);

            // 设置高度
            if (item.height) {
                item._itemHeight = "height:" + profile.$forceu(item.height);
            }

            // 设置标签索引
            item._tabindex = p.tabindex;

            // 处理标题显示
            if (!item.caption) {
                item._capDisplay = dpn;
            }

            // 设置背景信息
            this._setBackgroundInfo(item, p);

            // 设置溢出处理
            this._setOverflowStyle(item, p);

            // 设置选中状态
            if (item._show) {
                item._checked = profile.getClass('ITEM', '-checked');
                item._tlgchecked = profile.getClass('TOGGLE', '-checked');
            }

            // 准备命令
            this._prepareCmds(profile, item);


        },

        /**
         * 设置项目背景信息
         * @param {Object} item - 项目对象
         * @param {Object} properties - 组件属性
         * @private
         */
        _setBackgroundInfo: function (item, properties) {
            var t;
            item._bginfo = "";

            // 背景颜色
            if (t = item.panelBgClr || properties.panelBgClr) {
                item._bginfo += "background-color:" + t + ";";
            }

            // 背景图片
            if (t = item.panelBgImg || properties.panelBgImg) {
                item._bginfo += "background-image:url(" + ood.adjustRes(t) + ");";
            }

            // 背景位置
            if (t = item.panelBgImgPos || properties.panelBgImgPos) {
                item._bginfo += "background-position:" + t + ";";
            }

            // 背景重复
            if (t = item.panelBgImgRepeat || properties.panelBgImgRepeat) {
                item._bginfo += "background-repeat:" + t + ";";
            }

            // 背景附着
            if (t = item.panelBgImgAttachment || properties.panelBgImgAttachment) {
                item._bginfo += "background-attachment:" + t + ";";
            }
        },

        /**
         * 设置溢出样式
         * @param {Object} item - 项目对象
         * @param {Object} properties - 组件属性
         * @private
         */
        _setOverflowStyle: function (item, properties) {
            if (ood.isStr(item.overflow)) {
                item._overflow = item.overflow.indexOf(':') != -1 ?
                    (item.overflow) :
                    (item.overflow ? ("overflow:" + item.overflow) : "");
            } else if (ood.isStr(properties.overflow)) {
                item._overflow = properties.overflow.indexOf(':') != -1 ?
                    (properties.overflow) :
                    (properties.overflow ? ("overflow:" + properties.overflow) : "");
            }
        },

        /**
         * 调整大小处理
         * @param {Object} profile - 组件配置
         * @param {number} width - 宽度
         * @param {number} height - 高度
         * @param {boolean} force - 是否强制调整
         * @param {string} key - 键值
         * @private
         */
        _onresize: function (profile, width, height, force, key) {
            // 强制重置尺寸缓存
            if (force) {
                profile._w = profile._h = null;
            }


            // 调整面板高度
            this._adjustPanelHeight(profile, height);

            // 调整面板宽度
            if (width && profile._w !== width) {
                this._adjustPanelWidth(profile, width);
            }

            // 响应式布局调整
            if (profile.properties.responsive !== false) {
                profile.boxing().adjustLayout();
            }
        },

        /**
         * 调整面板高度
         * @param {Object} profile - 组件配置
         * @param {number} height - 高度
         * @private
         */
        _adjustPanelHeight: function (profile, height) {
            var panelCount = profile.getSubNode("PANEL", true).size();
            if (!ood.isNumb(height)) {
                height = profile.$em2px(height);
            }

            profile.getSubNode("PANEL", true).each(function (panel) {
                // 计算标题栏占用的高度
                var headerHeight = (35 * panelCount) + 20;
                // 计算内容区域高度
                var contentHeight = profile.$addpx(height, 0 - headerHeight);
                // 设置面板高度
                panel.style.height = contentHeight;
            });
        },

        /**
         * 调整面板宽度
         * @param {Object} profile - 组件配置
         * @param {number} width - 宽度
         * @private
         */
        _adjustPanelWidth: function (profile, width) {
            profile._w = width;

            profile.getSubNode("PANEL", true).each(function (panel) {
                if (panel.offsetWidth) {
                    // 先设置为自动宽度
                    ood(panel).width('auto');

                    // 获取实际宽度
                    var panelWidth = ood(panel).width();
                    var prop = profile.properties;

                    // 单位转换
                    if (ood.$us(profile) > 0) {
                        panelWidth = profile.$px2em(panelWidth, panel) + 'em';
                    }

                    // 设置宽度
                    ood(panel).width(panelWidth);

                    // 调整内容宽度
                    ood.UI._adjustConW(profile, panel, panelWidth);
                } else {
                    var pxwidth = profile.$em2px(width);
                    ood(panel).width((pxwidth + 1) + "px");
                }
            });
        },

        /**
         * 增强可访问性支持
         * @returns {Object} this - 支持链式调用
         */
        enhanceAccessibility: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    items = profile.getSubNodes('ITEM');

                // 为容器添加ARIA属性
                this._setContainerAccessibility(root);

                // 为每个项目添加ARIA属性
                items.forEach(function (item, index) {
                    this._setItemAccessibility(profile, item);
                }, this);
            });
        },

        /**
         * 设置容器可访问性属性
         * @param {Object} root - 根元素
         * @private
         */
        _setContainerAccessibility: function (root) {
            root.attr({
                'role': 'tablist',
                'aria-label': '可折叠标签页'
            });
        },

        /**
         * 设置项目可访问性属性
         * @param {Object} profile - 组件配置
         * @param {Object} item - 项目元素
         * @private
         */
        _setItemAccessibility: function (profile, item) {
            var itemId = profile.getItemIdByDom(item),
                isExpanded = profile.getItemByItemId(itemId)._show || false;

            // 设置标签属性
            item.attr({
                'role': 'tab',
                'aria-selected': isExpanded,
                'aria-controls': 'foldingtabs-panel-' + itemId,
                'aria-expanded': isExpanded
            });

            // 设置内容区域属性
            var body = profile.getSubNode('BODY', profile.getSubIdByItemId(itemId));
            if (body) {
                body.attr({
                    'id': 'foldingtabs-panel-' + itemId,
                    'role': 'tabpanel',
                    'aria-labelledby': 'foldingtabs-tab-' + itemId
                });
            }

            // 设置标题属性
            var title = profile.getSubNode('TITLE', profile.getSubIdByItemId(itemId));
            if (title) {
                title.attr({
                    'id': 'foldingtabs-tab-' + itemId,
                    'role': 'button',
                    'tabindex': '0'
                });
            }
        },

        /**
         * 响应式布局调整
         * @returns {Object} this - 支持链式调用
         */
        adjustLayout: function () {
            return this.each(function (profile) {
                var root = profile.getRoot(),
                    viewportWidth = ood(document.body).cssSize().width;
                // 应用响应式布局类
                this._applyResponsiveClasses(root, viewportWidth);
            });
        },

        /**
         * 应用响应式布局类
         * @param {Object} root - 根元素
         * @param {number} viewportWidth - 视口宽度
         * @private
         */
        _applyResponsiveClasses: function (root, viewportWidth) {
            // 移动设备布局 (< 768px)
            if (viewportWidth < 768) {
                root.addClass('foldingtabs-mobile');
            } else {
                root.removeClass('foldingtabs-mobile');
            }

            // 超小屏幕布局 (< 480px)
            if (viewportWidth < 480) {
                root.addClass('foldingtabs-tiny');
            } else {
                root.removeClass('foldingtabs-tiny');
            }
        },
        _adjustScroll:
            null
    }
});
