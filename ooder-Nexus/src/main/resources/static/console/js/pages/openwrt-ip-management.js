(function(global) {
    'use strict';

    var IPManagement = {
        dhcpLeases: [],
        staticIPs: [],
        editingStaticIPIndex: -1,

        init: function() {
            var self = this;
            window.onPageInit = function() {
                console.log('IP管理页面初始化完成');
                self.loadData();
            };
        },

        loadData: function() {
            document.getElementById('onlineDevices').textContent = this.dhcpLeases.length;
            document.getElementById('assignedIPs').textContent = this.dhcpLeases.length;
            document.getElementById('staticIPs').textContent = this.staticIPs.length;
            this.renderDhcpLeases();
            this.renderStaticIPs();
        },

        renderDhcpLeases: function() {
            var tbody = document.getElementById('dhcpLeasesTable');
            tbody.innerHTML = '';
            if (this.dhcpLeases.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 20px;">暂无DHCP租约</td></tr>';
                return;
            }
            this.dhcpLeases.forEach(function(lease, index) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + lease.hostname + '</td><td>' + lease.ip + '</td><td>' + lease.mac + '</td><td>' + lease.expires + '</td><td><button class="nx-btn nx-btn--sm nx-btn--secondary" onclick="makeStatic(' + index + ')">设为静态</button></td>';
                tbody.appendChild(row);
            });
        },

        renderStaticIPs: function() {
            var tbody = document.getElementById('staticIPsTable');
            tbody.innerHTML = '';
            if (this.staticIPs.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; padding: 20px;">暂无静态IP分配</td></tr>';
                return;
            }
            this.staticIPs.forEach(function(ip, index) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + ip.hostname + '</td><td>' + ip.ip + '</td><td>' + ip.mac + '</td><td><button class="nx-btn nx-btn--sm nx-btn--ghost" onclick="editStaticIP(' + index + ')">编辑</button><button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeStaticIP(' + index + ')">删除</button></td>';
                tbody.appendChild(row);
            });
        },

        showAddStaticIPModal: function() {
            this.editingStaticIPIndex = -1;
            document.getElementById('addStaticIPModal').style.display = 'flex';
            document.getElementById('ip-modal-title').textContent = '添加静态IP';
            document.getElementById('ip-hostname').value = '';
            document.getElementById('ip-address').value = '';
            document.getElementById('ip-mac').value = '';
        },

        editStaticIP: function(index) {
            this.editingStaticIPIndex = index;
            var ip = this.staticIPs[index];
            document.getElementById('addStaticIPModal').style.display = 'flex';
            document.getElementById('ip-modal-title').textContent = '编辑静态IP';
            document.getElementById('ip-hostname').value = ip.hostname;
            document.getElementById('ip-address').value = ip.ip;
            document.getElementById('ip-mac').value = ip.mac;
        },

        closeAddStaticIPModal: function() {
            document.getElementById('addStaticIPModal').style.display = 'none';
            this.editingStaticIPIndex = -1;
        },

        addStaticIP: function() {
            var hostname = document.getElementById('ip-hostname').value.trim();
            var ipAddress = document.getElementById('ip-address').value.trim();
            var mac = document.getElementById('ip-mac').value.trim();

            if (!hostname) {
                alert('请输入主机名');
                return;
            }

            var ipData = {
                hostname: hostname,
                ip: ipAddress || ('192.168.1.' + (100 + this.staticIPs.length)),
                mac: mac || '00:00:00:00:00:00'
            };

            if (this.editingStaticIPIndex >= 0) {
                this.staticIPs[this.editingStaticIPIndex] = ipData;
            } else {
                this.staticIPs.push(ipData);
            }

            this.closeAddStaticIPModal();
            this.loadData();
        },

        makeStatic: function(index) {
            var lease = this.dhcpLeases[index];
            this.staticIPs.push({
                hostname: lease.hostname,
                ip: lease.ip,
                mac: lease.mac
            });
            this.loadData();
        },

        removeStaticIP: function(index) {
            this.staticIPs.splice(index, 1);
            this.loadData();
        },

        refreshData: function() {
            this.loadData();
        }
    };

    IPManagement.init();

    global.loadData = function() { IPManagement.loadData(); };
    global.renderDhcpLeases = function() { IPManagement.renderDhcpLeases(); };
    global.renderStaticIPs = function() { IPManagement.renderStaticIPs(); };
    global.showAddStaticIPModal = function() { IPManagement.showAddStaticIPModal(); };
    global.editStaticIP = function(index) { IPManagement.editStaticIP(index); };
    global.closeAddStaticIPModal = function() { IPManagement.closeAddStaticIPModal(); };
    global.addStaticIP = function() { IPManagement.addStaticIP(); };
    global.makeStatic = function(index) { IPManagement.makeStatic(index); };
    global.removeStaticIP = function(index) { IPManagement.removeStaticIP(index); };
    global.refreshData = function() { IPManagement.refreshData(); };

})(typeof window !== 'undefined' ? window : this);
