/**
 * 移动端文件管理组件
 * 用于显示和管理企业文件
 */
ood.Class("ood.Mobile.OA.FileManager", ["ood.UI", "ood.absList"], {
    Instance: {
        Initialize: function() {
            this.constructor.upper.prototype.Initialize.call(this);
            this.initFileFeatures();
        },
        
        initFileFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-file ood-mobile-component');
            this.bindTouchEvents();
        },
        
        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // FileManager组件的尺寸调整逻辑

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
                    
                    // 设置文件列表的溢出滚动
                    var fileList = container.find('.ood-mobile-file-list');
                    if (!fileList.isEmpty()) {
                        fileList.css('height', 'calc(100% - 120px)'); // 减去工具栏的高度
                        fileList.css('overflow-y', 'auto');
                    }
                }
            }

            // 调整内部布局以适应新尺寸
            this.adjustLayout();
        },
        
        bindTouchEvents: function() {
            var self = this;
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            // 文件项点击事件
            container.on('click', '.ood-mobile-file-item', function(e) {
                var item = ood(this);
                var index = parseInt(item.attr('data-index'));
                var file = self._files[index];
                
                if (file && !item.hasClass('ood-mobile-file-item-disabled')) {
                    self.onFileClick(index, file);
                }
            });
            
            // 文件项长按事件（用于多选）
            container.on('touchstart', '.ood-mobile-file-item', function(e) {
                var item = ood(this);
                var touchStartTime = new Date().getTime();
                item.data('touchStartTime', touchStartTime);
            });
            
            container.on('touchend', '.ood-mobile-file-item', function(e) {
                var item = ood(this);
                var touchStartTime = item.data('touchStartTime');
                var touchEndTime = new Date().getTime();
                var touchDuration = touchEndTime - touchStartTime;
                
                // 长按500ms以上视为多选操作
                if (touchDuration > 500) {
                    var index = parseInt(item.attr('data-index'));
                    var file = self._files[index];
                    
                    if (file && !item.hasClass('ood-mobile-file-item-disabled')) {
                        self.toggleFileSelection(index, file);
                    }
                }
            });
            
            // 操作按钮点击事件
            container.on('click', '.ood-mobile-file-action', function(e) {
                e.stopPropagation();
                var button = ood(this);
                var action = button.attr('data-action');
                var item = button.closest('.ood-mobile-file-item');
                var index = parseInt(item.attr('data-index'));
                var file = self._files[index];
                
                if (file && !item.hasClass('ood-mobile-file-item-disabled')) {
                    self.onActionClick(index, file, action);
                }
            });
            
            // 添加移动端触摸事件支持
            container.on('touchstart', '.ood-mobile-file-item', function(e) {
                ood(this).addClass('ood-mobile-file-item-active');
            });
            
            container.on('touchend', '.ood-mobile-file-item', function(e) {
                // 只有在不是长按操作时才移除active类
                var item = ood(this);
                var touchStartTime = item.data('touchStartTime');
                var touchEndTime = new Date().getTime();
                var touchDuration = touchEndTime - touchStartTime;
                
                // 短按才移除active类
                if (touchDuration < 500) {
                    ood(this).removeClass('ood-mobile-file-item-active');
                }
            });
            
            container.on('touchcancel', '.ood-mobile-file-item', function(e) {
                ood(this).removeClass('ood-mobile-file-item-active');
            });
            
            // 操作按钮触摸事件
            container.on('touchstart', '.ood-mobile-file-action', function(e) {
                ood(this).addClass('ood-mobile-file-action-active');
            });
            
            container.on('touchend', '.ood-mobile-file-action', function(e) {
                ood(this).removeClass('ood-mobile-file-action-active');
            });
            
            container.on('touchcancel', '.ood-mobile-file-action', function(e) {
                ood(this).removeClass('ood-mobile-file-action-active');
            });
            
            // 工具栏按钮触摸事件
            container.on('touchstart', '.ood-mobile-file-toolbar-btn', function(e) {
                ood(this).addClass('ood-mobile-file-toolbar-btn-active');
            });
            
            container.on('touchend', '.ood-mobile-file-toolbar-btn', function(e) {
                ood(this).removeClass('ood-mobile-file-toolbar-btn-active');
            });
            
            container.on('touchcancel', '.ood-mobile-file-toolbar-btn', function(e) {
                ood(this).removeClass('ood-mobile-file-toolbar-btn-active');
            });
        },
        
        setFiles: function(files) {
            this._files = files || [];
            this._selectedFiles = []; // 重置选中文件
            this.renderFiles();
        },
        
        getFiles: function() {
            return this._files || [];
        },
        
        renderFiles: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            container.html('');
            
            // 渲染工具栏
            var toolbar = this.createToolbar();
            container.append(toolbar);
            
            // 渲染文件列表
            var fileList = this.createFileList();
            container.append(fileList);
        },
        
        createToolbar: function() {
            var toolbar = ood('<div class="ood-mobile-file-toolbar"></div>');
            
            // 搜索框
            var searchBox = ood('<input type="text" class="ood-mobile-file-search" placeholder="搜索文件">');
            toolbar.append(searchBox);
            
            // 操作按钮组
            var actions = ood('<div class="ood-mobile-file-toolbar-actions"></div>');
            
            var uploadBtn = ood('<button class="ood-mobile-file-toolbar-btn" data-action="upload">上传</button>');
            var newFolderBtn = ood('<button class="ood-mobile-file-toolbar-btn" data-action="newFolder">新建文件夹</button>');
            var selectBtn = ood('<button class="ood-mobile-file-toolbar-btn" data-action="select">选择</button>');
            
            actions.append(uploadBtn);
            actions.append(newFolderBtn);
            actions.append(selectBtn);
            
            toolbar.append(actions);
            
            return toolbar;
        },
        
        createFileList: function() {
            var list = ood('<div class="ood-mobile-file-list"></div>');
            
            for (var i = 0; i < this._files.length; i++) {
                var file = this._files[i];
                var fileElement = this.createFileElement(file, i);
                list.append(fileElement);
            }
            
            return list;
        },
        
        createFileElement: function(file, index) {
            var fileEl = ood('<div class="ood-mobile-file-item" data-index="' + index + '"></div>');
            
            // 文件图标
            var icon = ood('<div class="ood-mobile-file-icon ood-mobile-file-icon-' + (file.type || 'file') + '"></div>');
            fileEl.append(icon);
            
            // 文件信息容器
            var info = ood('<div class="ood-mobile-file-info"></div>');
            
            // 文件名
            if (file.name) {
                var name = ood('<div class="ood-mobile-file-name">' + file.name + '</div>');
                info.append(name);
            }
            
            // 文件大小和修改时间
            var meta = ood('<div class="ood-mobile-file-meta"></div>');
            if (file.size) {
                var size = ood('<span class="ood-mobile-file-size">' + this.formatFileSize(file.size) + '</span>');
                meta.append(size);
            }
            if (file.modifiedTime) {
                var time = ood('<span class="ood-mobile-file-time">' + file.modifiedTime + '</span>');
                meta.append(time);
            }
            info.append(meta);
            
            fileEl.append(info);
            
            // 操作按钮
            var actions = ood('<div class="ood-mobile-file-actions"></div>');
            
            var downloadBtn = ood('<button class="ood-mobile-file-action ood-mobile-file-action-download" data-action="download">下载</button>');
            var shareBtn = ood('<button class="ood-mobile-file-action ood-mobile-file-action-share" data-action="share">分享</button>');
            var deleteBtn = ood('<button class="ood-mobile-file-action ood-mobile-file-action-delete" data-action="delete">删除</button>');
            
            actions.append(downloadBtn);
            actions.append(shareBtn);
            actions.append(deleteBtn);
            
            fileEl.append(actions);
            
            // 选中状态
            if (this._selectedFiles.indexOf(index) !== -1) {
                fileEl.addClass('ood-mobile-file-item-selected');
            }
            
            // 禁用状态
            if (file.disabled) {
                fileEl.addClass('ood-mobile-file-item-disabled');
            }
            
            return fileEl;
        },
        
        formatFileSize: function(bytes) {
            if (bytes === 0) return '0 Bytes';
            var k = 1024;
            var sizes = ['Bytes', 'KB', 'MB', 'GB'];
            var i = Math.floor(Math.log(bytes) / Math.log(k));
            return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
        },
        
        toggleFileSelection: function(index, file) {
            var selectedIndex = this._selectedFiles.indexOf(index);
            
            if (selectedIndex === -1) {
                // 选中文件
                this._selectedFiles.push(index);
            } else {
                // 取消选中文件
                this._selectedFiles.splice(selectedIndex, 1);
            }
            
            // 更新UI
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            var item = container.find('.ood-mobile-file-item[data-index="' + index + '"]');
            
            if (selectedIndex === -1) {
                item.addClass('ood-mobile-file-item-selected');
            } else {
                item.removeClass('ood-mobile-file-item-selected');
            }
        },
        
        onFileClick: function(index, file) {
            var profile = this.get(0);
            
            if (profile.onFileClick) {
                profile.boxing().onFileClick(profile, index, file);
            }
        },
        
        onActionClick: function(index, file, action) {
            var profile = this.get(0);
            
            if (profile.onActionClick) {
                profile.boxing().onActionClick(profile, index, file, action);
            }
        },
        
        addFile: function(file) {
            this._files.push(file);
            this.renderFiles();
        },
        
        removeFile: function(index) {
            if (index < 0 || index >= this._files.length) return;
            
            this._files.splice(index, 1);
            this.renderFiles();
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentFiles = self.getFiles();
                if (typeof index === 'undefined') {
                    currentFiles = currentFiles.concat(items);
                } else {
                    var insertIndex = isBefore ? index : index + 1;
                    currentFiles.splice.apply(currentFiles, [insertIndex, 0].concat(items));
                }
                
                self.setFiles(currentFiles);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentFiles = self.getFiles();
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var idx = parseInt(indices[i]);
                    if (idx >= 0 && idx < currentFiles.length) {
                        currentFiles.splice(idx, 1);
                    }
                }
                
                self.setFiles(currentFiles);
            });
        },
        
        clearItems: function() {
            return this.setFiles([]);
        },
        
        getItems: function() {
            return this.getFiles();
        },
        
        getSelectedItems: function() {
            var selected = [];
            for (var i = 0; i < this._selectedFiles.length; i++) {
                var index = this._selectedFiles[i];
                if (index >= 0 && index < this._files.length) {
                    selected.push(this._files[index]);
                }
            }
            return selected;
        },
        
        selectItem: function(value) {
            // 根据文件ID选中文件
            for (var i = 0; i < this._files.length; i++) {
                if (this._files[i].id === value && this._selectedFiles.indexOf(i) === -1) {
                    this._selectedFiles.push(i);
                    var profile = this.get(0);
                    var container = profile.getSubNode('CONTAINER');
                    var item = container.find('.ood-mobile-file-item[data-index="' + i + '"]');
                    item.addClass('ood-mobile-file-item-selected');
                    break;
                }
            }
            return this;
        },
        
        unselectItem: function(value) {
            // 根据文件ID取消选中文件
            for (var i = 0; i < this._files.length; i++) {
                if (this._files[i].id === value) {
                    var selectedIndex = this._selectedFiles.indexOf(i);
                    if (selectedIndex !== -1) {
                        this._selectedFiles.splice(selectedIndex, 1);
                        var profile = this.get(0);
                        var container = profile.getSubNode('CONTAINER');
                        var item = container.find('.ood-mobile-file-item[data-index="' + i + '"]');
                        item.removeClass('ood-mobile-file-item-selected');
                    }
                    break;
                }
            }
            return this;
        }
    },
    
    Static: {
        Templates: {
            tagName: 'div',
            className: 'ood-mobile-file',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-file-container'
            }
        },
        
        Appearances: {
            KEY: {
                position: 'relative',
                width: '100%',
                height: '100%',
                'background-color': 'var(--mobile-bg-primary)'
            },
            
            CONTAINER: {
                height: '100%',
                position: 'relative'
            },
            
            '.ood-mobile-file-toolbar': {
                display: 'flex',
                'flex-direction': 'column',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)'
            },
            
            '.ood-mobile-file-search': {
                'padding': 'var(--mobile-spacing-sm)',
                'border': '1px solid var(--mobile-border-color)',
                'border-radius': 'var(--mobile-border-radius)',
                'font-size': 'var(--mobile-font-md)',
                'margin-bottom': 'var(--mobile-spacing-sm)'
            },
            
            '.ood-mobile-file-toolbar-actions': {
                display: 'flex',
                'justify-content': 'space-between'
            },
            
            '.ood-mobile-file-toolbar-btn': {
                'border': 'none',
                'border-radius': 'var(--mobile-border-radius)',
                'padding': '6px 12px',
                'font-size': 'var(--mobile-font-sm)',
                'background-color': 'var(--mobile-bg-secondary)',
                color: 'var(--mobile-text-primary)',
                cursor: 'pointer'
            },
            
            '.ood-mobile-file-list': {
                height: 'calc(100% - 120px)',
                'overflow-y': 'auto'
            },
            
            '.ood-mobile-file-item': {
                display: 'flex',
                'align-items': 'center',
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)',
                cursor: 'pointer',
                transition: 'background-color 0.2s ease'
            },
            
            '.ood-mobile-file-item:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            '.ood-mobile-file-item-selected': {
                'background-color': 'var(--mobile-primary-light)'
            },
            
            '.ood-mobile-file-item-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-file-icon': {
                width: '40px',
                height: '40px',
                'margin-right': 'var(--mobile-spacing-md)',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                'font-size': '20px'
            },
            
            '.ood-mobile-file-icon-folder': {
                color: '#FFA500'
            },
            
            '.ood-mobile-file-icon-doc': {
                color: '#007AFF'
            },
            
            '.ood-mobile-file-icon-pdf': {
                color: '#FF3B30'
            },
            
            '.ood-mobile-file-icon-image': {
                color: '#4CD964'
            },
            
            '.ood-mobile-file-icon-video': {
                color: '#FF9500'
            },
            
            '.ood-mobile-file-icon-audio': {
                color: '#5856D6'
            },
            
            '.ood-mobile-file-info': {
                flex: 1,
                'min-width': 0
            },
            
            '.ood-mobile-file-name': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '500',
                color: 'var(--mobile-text-primary)',
                'margin-bottom': 'var(--mobile-spacing-xs)',
                'white-space': 'nowrap',
                'overflow': 'hidden',
                'text-overflow': 'ellipsis'
            },
            
            '.ood-mobile-file-meta': {
                display: 'flex',
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-text-secondary)'
            },
            
            '.ood-mobile-file-size': {
                'margin-right': 'var(--mobile-spacing-sm)'
            },
            
            '.ood-mobile-file-actions': {
                display: 'flex',
                'flex-direction': 'column',
                'gap': '4px'
            },
            
            '.ood-mobile-file-action': {
                'border': 'none',
                'border-radius': 'var(--mobile-border-radius)',
                'padding': '4px 8px',
                'font-size': 'var(--mobile-font-xs)',
                cursor: 'pointer'
            },
            
            '.ood-mobile-file-action-download': {
                'background-color': 'var(--mobile-success)',
                color: 'white'
            },
            
            '.ood-mobile-file-action-share': {
                'background-color': 'var(--mobile-primary)',
                color: 'white'
            },
            
            '.ood-mobile-file-action-delete': {
                'background-color': 'var(--mobile-danger)',
                color: 'white'
            }
        },
        
        DataModel: {
            // 基础属性
            caption: {
                caption: '文件管理标题',
                ini: '文件管理',
                action: function(value) {
                    var profile = this;
                    profile.getRoot().attr('aria-label', value || '文件管理');
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
                ini: '100%'
            },
            
            // 文件数据
            files: {
                caption: '文件数据',
                ini: [
                    {
                        id: '1',
                        name: '项目计划书.docx',
                        type: 'doc',
                        size: 102400,
                        modifiedTime: '2025-09-14 10:30',
                        disabled: false
                    },
                    {
                        id: '2',
                        name: '产品需求文档.pdf',
                        type: 'pdf',
                        size: 204800,
                        modifiedTime: '2025-09-13 15:45',
                        disabled: false
                    },
                    {
                        id: '3',
                        name: '设计素材',
                        type: 'folder',
                        size: 0,
                        modifiedTime: '2025-09-12 09:15',
                        disabled: false
                    },
                    {
                        id: '4',
                        name: '会议照片.jpg',
                        type: 'image',
                        size: 512000,
                        modifiedTime: '2025-09-11 14:20',
                        disabled: false
                    }
                ],
                action: function(value) {
                    this.boxing().setFiles(value);
                }
            },
            
            // 事件处理器
            onFileClick: {
                caption: '文件点击事件处理器',
                ini: null
            },
            
            onActionClick: {
                caption: '文件操作点击事件处理器',
                ini: null
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
                profile.boxing().setFiles(profile.properties.files);
            });
        }
    }
});