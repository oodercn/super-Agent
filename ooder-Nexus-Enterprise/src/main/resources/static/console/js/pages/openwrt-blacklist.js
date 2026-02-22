(function(global) {
    'use strict';

    var BlacklistManagement = {
        macBlacklist: [],
        domainBlacklist: [],
        editingMacIndex: -1,
        editingDomainIndex: -1,

        init: function() {
            var self = this;
            window.onPageInit = function() {
                console.log('黑名单管理页面初始化完成');
                self.loadData();
            };

            document.getElementById('bl-type').addEventListener('change', function() {
                var label = document.getElementById('bl-value-label');
                var input = document.getElementById('bl-value');
                if (this.value === 'mac') {
                    label.textContent = 'MAC地址';
                    input.placeholder = '如: 00:11:22:33:44:55';
                } else {
                    label.textContent = '域名';
                    input.placeholder = '如: example.com';
                }
            });
        },

        loadData: function() {
            document.getElementById('blacklistCount').textContent = this.macBlacklist.length + this.domainBlacklist.length;
            document.getElementById('macCount').textContent = this.macBlacklist.length;
            document.getElementById('domainCount').textContent = this.domainBlacklist.length;
            this.renderMacBlacklist();
            this.renderDomainBlacklist();
        },

        renderMacBlacklist: function() {
            var tbody = document.getElementById('macBlacklistTable');
            tbody.innerHTML = '';
            if (this.macBlacklist.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; padding: 20px;">暂无MAC黑名单</td></tr>';
                return;
            }
            this.macBlacklist.forEach(function(item, index) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + item.mac + '</td><td>' + item.note + '</td><td>' + item.addedAt + '</td><td><button class="nx-btn nx-btn--sm nx-btn--ghost" onclick="editMac(' + index + ')">编辑</button><button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeMac(' + index + ')">删除</button></td>';
                tbody.appendChild(row);
            });
        },

        renderDomainBlacklist: function() {
            var tbody = document.getElementById('domainBlacklistTable');
            tbody.innerHTML = '';
            if (this.domainBlacklist.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; padding: 20px;">暂无域名黑名单</td></tr>';
                return;
            }
            this.domainBlacklist.forEach(function(item, index) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + item.domain + '</td><td>' + item.note + '</td><td>' + item.addedAt + '</td><td><button class="nx-btn nx-btn--sm nx-btn--ghost" onclick="editDomain(' + index + ')">编辑</button><button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeDomain(' + index + ')">删除</button></td>';
                tbody.appendChild(row);
            });
        },

        showAddModal: function() {
            this.editingMacIndex = -1;
            this.editingDomainIndex = -1;
            document.getElementById('addModal').style.display = 'flex';
            document.getElementById('bl-modal-title').textContent = '添加黑名单';
            document.getElementById('bl-type').value = 'mac';
            document.getElementById('bl-value-label').textContent = 'MAC地址';
            document.getElementById('bl-value').value = '';
            document.getElementById('bl-value').placeholder = '如: 00:11:22:33:44:55';
            document.getElementById('bl-note').value = '';
        },

        editMac: function(index) {
            this.editingMacIndex = index;
            this.editingDomainIndex = -1;
            var item = this.macBlacklist[index];
            document.getElementById('addModal').style.display = 'flex';
            document.getElementById('bl-modal-title').textContent = '编辑MAC黑名单';
            document.getElementById('bl-type').value = 'mac';
            document.getElementById('bl-value-label').textContent = 'MAC地址';
            document.getElementById('bl-value').value = item.mac;
            document.getElementById('bl-value').placeholder = '如: 00:11:22:33:44:55';
            document.getElementById('bl-note').value = item.note;
        },

        editDomain: function(index) {
            this.editingMacIndex = -1;
            this.editingDomainIndex = index;
            var item = this.domainBlacklist[index];
            document.getElementById('addModal').style.display = 'flex';
            document.getElementById('bl-modal-title').textContent = '编辑域名黑名单';
            document.getElementById('bl-type').value = 'domain';
            document.getElementById('bl-value-label').textContent = '域名';
            document.getElementById('bl-value').value = item.domain;
            document.getElementById('bl-value').placeholder = '如: example.com';
            document.getElementById('bl-note').value = item.note;
        },

        closeAddModal: function() {
            document.getElementById('addModal').style.display = 'none';
            this.editingMacIndex = -1;
            this.editingDomainIndex = -1;
        },

        addBlacklistItem: function() {
            var type = document.getElementById('bl-type').value;
            var value = document.getElementById('bl-value').value.trim();
            var note = document.getElementById('bl-note').value.trim();

            if (!value) {
                alert('请输入值');
                return;
            }

            var now = new Date().toLocaleString('zh-CN');

            if (type === 'mac') {
                var itemData = { mac: value, note: note, addedAt: now };
                if (this.editingMacIndex >= 0) {
                    this.macBlacklist[this.editingMacIndex] = itemData;
                } else {
                    this.macBlacklist.push(itemData);
                }
            } else {
                var itemData = { domain: value, note: note, addedAt: now };
                if (this.editingDomainIndex >= 0) {
                    this.domainBlacklist[this.editingDomainIndex] = itemData;
                } else {
                    this.domainBlacklist.push(itemData);
                }
            }

            this.closeAddModal();
            this.loadData();
        },

        removeMac: function(index) {
            this.macBlacklist.splice(index, 1);
            this.loadData();
        },

        removeDomain: function(index) {
            this.domainBlacklist.splice(index, 1);
            this.loadData();
        }
    };

    BlacklistManagement.init();

    global.loadData = function() { BlacklistManagement.loadData(); };
    global.renderMacBlacklist = function() { BlacklistManagement.renderMacBlacklist(); };
    global.renderDomainBlacklist = function() { BlacklistManagement.renderDomainBlacklist(); };
    global.showAddModal = function() { BlacklistManagement.showAddModal(); };
    global.editMac = function(index) { BlacklistManagement.editMac(index); };
    global.editDomain = function(index) { BlacklistManagement.editDomain(index); };
    global.closeAddModal = function() { BlacklistManagement.closeAddModal(); };
    global.addBlacklistItem = function() { BlacklistManagement.addBlacklistItem(); };
    global.removeMac = function(index) { BlacklistManagement.removeMac(index); };
    global.removeDomain = function(index) { BlacklistManagement.removeDomain(index); };

})(typeof window !== 'undefined' ? window : this);
