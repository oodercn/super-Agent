/**
 * 移动端通讯录组件
 * 用于显示和管理员工通讯录信息
 */
ood.Class("ood.Mobile.OA.ContactList", ["ood.UI", "ood.absList"], {
    Instance: {
        Initialize: function() {
            this.constructor.upper.prototype.Initialize.call(this);
            this.initContactFeatures();
        },
        
        initContactFeatures: function() {
            var profile = this.get(0);
            if (!profile) return;
            
            profile.getRoot().addClass('ood-mobile-contact ood-mobile-component');
            this.bindTouchEvents();
        },
        
        // 响应式调整大小事件处理
        _onresize: function(profile, width, height) {
            // ContactList组件的尺寸调整逻辑

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
                    var list = container.find('.ood-mobile-contact-list');
                    if (!list.isEmpty()) {
                        list.css('height', 'calc(100% - 100px)'); // 减去搜索框和索引栏的高度
                        list.css('overflow-y', 'auto');
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
            
            // 联系人项点击事件
            container.on('click', '.ood-mobile-contact-item', function(e) {
                var item = ood(this);
                var index = parseInt(item.attr('data-index'));
                var contact = self._contacts[index];
                
                if (contact && !item.hasClass('ood-mobile-contact-item-disabled')) {
                    self.onContactClick(index, contact);
                }
            });
            
            // 搜索框事件
            container.on('input', '.ood-mobile-contact-search', function(e) {
                var keyword = ood(this).val();
                self.filterContacts(keyword);
            });
            
            // 添加移动端触摸事件支持
            container.on('touchstart', '.ood-mobile-contact-item', function(e) {
                ood(this).addClass('ood-mobile-contact-item-active');
            });
            
            container.on('touchend', '.ood-mobile-contact-item', function(e) {
                ood(this).removeClass('ood-mobile-contact-item-active');
            });
            
            container.on('touchcancel', '.ood-mobile-contact-item', function(e) {
                ood(this).removeClass('ood-mobile-contact-item-active');
            });
        },
        
        setContacts: function(contacts) {
            this._contacts = contacts || [];
            this._filteredContacts = this._contacts.slice(); // 复制一份用于过滤
            this.renderContacts();
        },
        
        getContacts: function() {
            return this._contacts || [];
        },
        
        renderContacts: function() {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            
            container.html('');
            
            // 渲染搜索框
            var searchBox = this.createSearchBox();
            container.append(searchBox);
            
            // 渲染联系人列表
            var contactList = this.createContactList();
            container.append(contactList);
            
            // 渲染字母索引
            var indexBar = this.createIndexBar();
            container.append(indexBar);
        },
        
        createSearchBox: function() {
            var searchBox = ood('<div class="ood-mobile-contact-search-container"></div>');
            var searchInput = ood('<input type="text" class="ood-mobile-contact-search" placeholder="搜索联系人">');
            searchBox.append(searchInput);
            return searchBox;
        },
        
        createContactList: function() {
            var list = ood('<div class="ood-mobile-contact-list"></div>');
            
            // 按首字母分组显示联系人
            var groupedContacts = this.groupContactsByLetter();
            
            for (var letter in groupedContacts) {
                var letterGroup = ood('<div class="ood-mobile-contact-letter-group" data-letter="' + letter + '"></div>');
                
                var letterHeader = ood('<div class="ood-mobile-contact-letter-header">' + letter + '</div>');
                letterGroup.append(letterHeader);
                
                var items = groupedContacts[letter];
                for (var i = 0; i < items.length; i++) {
                    var contact = items[i];
                    var itemElement = this.createContactElement(contact, i);
                    letterGroup.append(itemElement);
                }
                
                list.append(letterGroup);
            }
            
            return list;
        },
        
        createContactElement: function(contact, index) {
            var contactEl = ood('<div class="ood-mobile-contact-item" data-index="' + index + '"></div>');
            
            // 头像
            var avatar = ood('<div class="ood-mobile-contact-avatar"></div>');
            if (contact.avatar) {
                avatar.css('background-image', 'url(' + contact.avatar + ')');
            } else {
                // 使用姓名首字母作为默认头像
                var initials = contact.name ? contact.name.charAt(0) : '?';
                avatar.text(initials);
            }
            contactEl.append(avatar);
            
            // 信息容器
            var info = ood('<div class="ood-mobile-contact-info"></div>');
            
            // 姓名
            if (contact.name) {
                var name = ood('<div class="ood-mobile-contact-name">' + contact.name + '</div>');
                info.append(name);
            }
            
            // 职位
            if (contact.position) {
                var position = ood('<div class="ood-mobile-contact-position">' + contact.position + '</div>');
                info.append(position);
            }
            
            // 部门
            if (contact.department) {
                var department = ood('<div class="ood-mobile-contact-department">' + contact.department + '</div>');
                info.append(department);
            }
            
            contactEl.append(info);
            
            // 禁用状态
            if (contact.disabled) {
                contactEl.addClass('ood-mobile-contact-item-disabled');
            }
            
            return contactEl;
        },
        
        createIndexBar: function() {
            var indexBar = ood('<div class="ood-mobile-contact-index-bar"></div>');
            
            // 生成A-Z字母索引
            for (var i = 65; i <= 90; i++) {
                var letter = String.fromCharCode(i);
                var indexItem = ood('<div class="ood-mobile-contact-index-item">' + letter + '</div>');
                indexBar.append(indexItem);
            }
            
            return indexBar;
        },
        
        groupContactsByLetter: function() {
            var grouped = {};
            
            for (var i = 0; i < this._filteredContacts.length; i++) {
                var contact = this._filteredContacts[i];
                var letter = contact.name ? contact.name.charAt(0).toUpperCase() : '#';
                
                // 简单处理，实际应该使用拼音库处理中文
                if (!letter.match(/[A-Z]/)) {
                    letter = '#';
                }
                
                if (!grouped[letter]) {
                    grouped[letter] = [];
                }
                grouped[letter].push(contact);
            }
            
            return grouped;
        },
        
        filterContacts: function(keyword) {
            if (!keyword) {
                this._filteredContacts = this._contacts.slice();
            } else {
                this._filteredContacts = this._contacts.filter(function(contact) {
                    return contact.name && contact.name.toLowerCase().indexOf(keyword.toLowerCase()) !== -1;
                });
            }
            this.renderContacts();
        },
        
        scrollToLetter: function(letter) {
            var profile = this.get(0);
            var container = profile.getSubNode('CONTAINER');
            var letterGroup = container.find('.ood-mobile-contact-letter-group[data-letter="' + letter + '"]');
            
            if (letterGroup.length > 0) {
                // 滚动到指定字母分组
                var offsetTop = letterGroup.offset().top;
                container.animate({ scrollTop: offsetTop }, 300);
            }
        },
        
        onContactClick: function(index, contact) {
            var profile = this.get(0);
            
            if (profile.onContactClick) {
                profile.boxing().onContactClick(profile, index, contact);
            }
        },
        
        addContact: function(contact) {
            this._contacts.push(contact);
            this._filteredContacts = this._contacts.slice();
            this.renderContacts();
        },
        
        removeContact: function(index) {
            if (index < 0 || index >= this._contacts.length) return;
            
            this._contacts.splice(index, 1);
            this._filteredContacts = this._contacts.slice();
            this.renderContacts();
        },
        
        // ood.absList 必需方法
        insertItems: function(items, index, isBefore) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(items)) items = [items];
                
                var currentContacts = self.getContacts();
                if (typeof index === 'undefined') {
                    currentContacts = currentContacts.concat(items);
                } else {
                    var insertIndex = isBefore ? index : index + 1;
                    currentContacts.splice.apply(currentContacts, [insertIndex, 0].concat(items));
                }
                
                self.setContacts(currentContacts);
            });
        },
        
        removeItems: function(indices) {
            var self = this;
            return this.each(function(profile) {
                if (!ood.isArr(indices)) indices = [indices];
                
                var currentContacts = self.getContacts();
                indices.sort(function(a, b) { return b - a; });
                
                for (var i = 0; i < indices.length; i++) {
                    var idx = parseInt(indices[i]);
                    if (idx >= 0 && idx < currentContacts.length) {
                        currentContacts.splice(idx, 1);
                    }
                }
                
                self.setContacts(currentContacts);
            });
        },
        
        clearItems: function() {
            return this.setContacts([]);
        },
        
        getItems: function() {
            return this.getContacts();
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
            className: 'ood-mobile-contact',
            style: '{_style}',
            
            CONTAINER: {
                tagName: 'div',
                className: 'ood-mobile-contact-container'
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
            
            '.ood-mobile-contact-search-container': {
                padding: 'var(--mobile-spacing-md)',
                'border-bottom': '1px solid var(--mobile-border-color)'
            },
            
            '.ood-mobile-contact-search': {
                width: '100%',
                'padding': 'var(--mobile-spacing-sm)',
                'border': '1px solid var(--mobile-border-color)',
                'border-radius': 'var(--mobile-border-radius)',
                'font-size': 'var(--mobile-font-md)'
            },
            
            '.ood-mobile-contact-list': {
                height: 'calc(100% - 60px)',
                'overflow-y': 'auto',
                padding: '0 var(--mobile-spacing-md)'
            },
            
            '.ood-mobile-contact-letter-group': {
                'margin-bottom': 'var(--mobile-spacing-lg)'
            },
            
            '.ood-mobile-contact-letter-header': {
                'font-size': 'var(--mobile-font-sm)',
                'font-weight': '600',
                color: 'var(--mobile-text-secondary)',
                'padding': 'var(--mobile-spacing-sm) 0',
                'border-bottom': '1px solid var(--mobile-border-color)'
            },
            
            '.ood-mobile-contact-item': {
                display: 'flex',
                'align-items': 'center',
                padding: 'var(--mobile-spacing-md) 0',
                'border-bottom': '1px solid var(--mobile-border-color)',
                cursor: 'pointer',
                transition: 'background-color 0.2s ease'
            },
            
            '.ood-mobile-contact-item:last-child': {
                'border-bottom': 'none'
            },
            
            '.ood-mobile-contact-item:hover': {
                'background-color': 'var(--mobile-bg-secondary)'
            },
            
            '.ood-mobile-contact-item-disabled': {
                opacity: 0.5,
                cursor: 'not-allowed'
            },
            
            '.ood-mobile-contact-avatar': {
                width: '40px',
                height: '40px',
                'border-radius': '50%',
                'background-color': 'var(--mobile-primary-light)',
                display: 'flex',
                'align-items': 'center',
                'justify-content': 'center',
                color: 'var(--mobile-primary)',
                'font-weight': '600',
                'margin-right': 'var(--mobile-spacing-md)',
                'background-size': 'cover',
                'background-position': 'center'
            },
            
            '.ood-mobile-contact-info': {
                flex: 1
            },
            
            '.ood-mobile-contact-name': {
                'font-size': 'var(--mobile-font-md)',
                'font-weight': '500',
                color: 'var(--mobile-text-primary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-contact-position': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-secondary)',
                'margin-bottom': 'var(--mobile-spacing-xs)'
            },
            
            '.ood-mobile-contact-department': {
                'font-size': 'var(--mobile-font-sm)',
                color: 'var(--mobile-text-tertiary)'
            },
            
            '.ood-mobile-contact-index-bar': {
                position: 'fixed',
                right: '10px',
                top: '50%',
                transform: 'translateY(-50%)',
                'z-index': 100,
                display: 'flex',
                'flex-direction': 'column',
                'align-items': 'center'
            },
            
            '.ood-mobile-contact-index-item': {
                'font-size': 'var(--mobile-font-xs)',
                color: 'var(--mobile-primary)',
                padding: '2px 4px',
                cursor: 'pointer'
            },
            
            '.ood-mobile-contact-index-item:hover': {
                'background-color': 'var(--mobile-bg-secondary)',
                'border-radius': 'var(--mobile-border-radius)'
            }
        },
        
        DataModel: {
            // 基础属性
            caption: {
                caption: '通讯录标题',
                ini: '通讯录',
                action: function(value) {
                    var profile = this;
                    profile.getRoot().attr('aria-label', value || '通讯录');
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
            
            // 联系人数据
            contacts: {
                caption: '联系人数据',
                ini: [
                    {
                        id: '1',
                        name: '张三',
                        position: '产品经理',
                        department: '产品部',
                        avatar: '',
                        phone: '13800138001',
                        email: 'zhangsan@company.com',
                        disabled: false
                    },
                    {
                        id: '2',
                        name: '李四',
                        position: '前端工程师',
                        department: '技术部',
                        avatar: '',
                        phone: '13800138002',
                        email: 'lisi@company.com',
                        disabled: false
                    },
                    {
                        id: '3',
                        name: '王五',
                        position: '设计师',
                        department: '设计部',
                        avatar: '',
                        phone: '13800138003',
                        email: 'wangwu@company.com',
                        disabled: false
                    }
                ],
                action: function(value) {
                    this.boxing().setContacts(value);
                }
            },
            
            // 事件处理器
            onContactClick: {
                caption: '联系人点击事件处理器',
                ini: null
            }
        },
        
        RenderTrigger: function() {
            var profile = this;
            ood.asyRun(function() {
                profile.boxing().Initialize();
                profile.boxing().setContacts(profile.properties.contacts);
            });
        }
    }
});