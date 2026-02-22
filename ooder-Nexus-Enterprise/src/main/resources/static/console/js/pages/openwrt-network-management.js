(function(global) {
    'use strict';

    var NetworkManagement = {
        portForwardingRules: [],
        firewallRules: [],
        editingPortForwardingIndex: -1,
        editingFirewallRuleIndex: -1,

        init: function() {
            var self = this;
            window.onPageInit = function() {
                console.log('网络管理页面初始化完成');
                self.loadNetworkData();
            };
        },

        loadNetworkData: async function() {
            document.getElementById('portForwardingCount').textContent = this.portForwardingRules.length;
            document.getElementById('upnpCount').textContent = 0;
            document.getElementById('tunnelCount').textContent = 0;
            document.getElementById('firewallCount').textContent = this.firewallRules.length;
            this.renderPortForwarding();
            this.renderFirewall();
        },

        renderPortForwarding: function() {
            var tbody = document.getElementById('portForwardingTable');
            tbody.innerHTML = '';
            if (this.portForwardingRules.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 20px;">暂无端口映射规则</td></tr>';
                return;
            }
            this.portForwardingRules.forEach(function(rule, index) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + rule.name + '</td><td>' + rule.protocol + '</td><td>' + rule.externalPort + '</td><td>' + rule.internalIP + '</td><td>' + rule.internalPort + '</td><td><span class="nx-text-success">活跃</span></td><td><button class="nx-btn nx-btn--sm nx-btn--ghost" onclick="editPortForwarding(' + index + ')">编辑</button><button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removePortForwarding(' + index + ')">删除</button></td>';
                tbody.appendChild(row);
            });
        },

        renderFirewall: function() {
            var tbody = document.getElementById('firewallTable');
            tbody.innerHTML = '';
            if (this.firewallRules.length === 0) {
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; padding: 20px;">暂无防火墙规则</td></tr>';
                return;
            }
            this.firewallRules.forEach(function(rule, index) {
                var row = document.createElement('tr');
                row.innerHTML = '<td>' + rule.name + '</td><td>' + rule.sourceZone + '</td><td>' + rule.destZone + '</td><td>' + rule.protocol + '</td><td>' + rule.port + '</td><td>' + rule.action + '</td><td><button class="nx-btn nx-btn--sm nx-btn--ghost" onclick="editFirewallRule(' + index + ')">编辑</button><button class="nx-btn nx-btn--sm nx-btn--danger" onclick="removeFirewallRule(' + index + ')">删除</button></td>';
                tbody.appendChild(row);
            });
        },

        showPortForwardingModal: function() {
            this.editingPortForwardingIndex = -1;
            document.getElementById('portForwardingModal').style.display = 'flex';
            document.getElementById('pf-modal-title').textContent = '添加端口映射规则';
            document.getElementById('pf-name').value = '';
            document.getElementById('pf-protocol').value = 'TCP';
            document.getElementById('pf-external-port').value = '';
            document.getElementById('pf-internal-ip').value = '';
            document.getElementById('pf-internal-port').value = '';
        },

        editPortForwarding: function(index) {
            this.editingPortForwardingIndex = index;
            var rule = this.portForwardingRules[index];
            document.getElementById('portForwardingModal').style.display = 'flex';
            document.getElementById('pf-modal-title').textContent = '编辑端口映射规则';
            document.getElementById('pf-name').value = rule.name;
            document.getElementById('pf-protocol').value = rule.protocol;
            document.getElementById('pf-external-port').value = rule.externalPort;
            document.getElementById('pf-internal-ip').value = rule.internalIP;
            document.getElementById('pf-internal-port').value = rule.internalPort;
        },

        closePortForwardingModal: function() {
            document.getElementById('portForwardingModal').style.display = 'none';
            this.editingPortForwardingIndex = -1;
        },

        addPortForwarding: function() {
            var name = document.getElementById('pf-name').value.trim();
            if (!name) {
                alert('请输入规则名称');
                return;
            }
            var ruleData = {
                name: name,
                protocol: document.getElementById('pf-protocol').value,
                externalPort: document.getElementById('pf-external-port').value || '8080',
                internalIP: document.getElementById('pf-internal-ip').value || '192.168.1.100',
                internalPort: document.getElementById('pf-internal-port').value || '80'
            };

            if (this.editingPortForwardingIndex >= 0) {
                this.portForwardingRules[this.editingPortForwardingIndex] = ruleData;
            } else {
                this.portForwardingRules.push(ruleData);
            }
            this.closePortForwardingModal();
            this.loadNetworkData();
        },

        removePortForwarding: function(index) {
            this.portForwardingRules.splice(index, 1);
            this.loadNetworkData();
        },

        showFirewallModal: function() {
            this.editingFirewallRuleIndex = -1;
            document.getElementById('firewallModal').style.display = 'flex';
            document.getElementById('fw-modal-title').textContent = '添加防火墙规则';
            document.getElementById('fw-name').value = '';
            document.getElementById('fw-source-zone').value = 'lan';
            document.getElementById('fw-dest-zone').value = 'wan';
            document.getElementById('fw-protocol').value = 'TCP';
            document.getElementById('fw-port').value = '';
            document.getElementById('fw-action').value = 'ACCEPT';
        },

        editFirewallRule: function(index) {
            this.editingFirewallRuleIndex = index;
            var rule = this.firewallRules[index];
            document.getElementById('firewallModal').style.display = 'flex';
            document.getElementById('fw-modal-title').textContent = '编辑防火墙规则';
            document.getElementById('fw-name').value = rule.name;
            document.getElementById('fw-source-zone').value = rule.sourceZone;
            document.getElementById('fw-dest-zone').value = rule.destZone;
            document.getElementById('fw-protocol').value = rule.protocol;
            document.getElementById('fw-port').value = rule.port;
            document.getElementById('fw-action').value = rule.action;
        },

        closeFirewallModal: function() {
            document.getElementById('firewallModal').style.display = 'none';
            this.editingFirewallRuleIndex = -1;
        },

        addFirewallRule: function() {
            var name = document.getElementById('fw-name').value.trim();
            if (!name) {
                alert('请输入规则名称');
                return;
            }
            var ruleData = {
                name: name,
                sourceZone: document.getElementById('fw-source-zone').value,
                destZone: document.getElementById('fw-dest-zone').value,
                protocol: document.getElementById('fw-protocol').value,
                port: document.getElementById('fw-port').value || '80',
                action: document.getElementById('fw-action').value
            };

            if (this.editingFirewallRuleIndex >= 0) {
                this.firewallRules[this.editingFirewallRuleIndex] = ruleData;
            } else {
                this.firewallRules.push(ruleData);
            }
            this.closeFirewallModal();
            this.loadNetworkData();
        },

        removeFirewallRule: function(index) {
            this.firewallRules.splice(index, 1);
            this.loadNetworkData();
        }
    };

    NetworkManagement.init();

    global.loadNetworkData = function() { NetworkManagement.loadNetworkData(); };
    global.renderPortForwarding = function() { NetworkManagement.renderPortForwarding(); };
    global.renderFirewall = function() { NetworkManagement.renderFirewall(); };
    global.showPortForwardingModal = function() { NetworkManagement.showPortForwardingModal(); };
    global.editPortForwarding = function(index) { NetworkManagement.editPortForwarding(index); };
    global.closePortForwardingModal = function() { NetworkManagement.closePortForwardingModal(); };
    global.addPortForwarding = function() { NetworkManagement.addPortForwarding(); };
    global.removePortForwarding = function(index) { NetworkManagement.removePortForwarding(index); };
    global.showFirewallModal = function() { NetworkManagement.showFirewallModal(); };
    global.editFirewallRule = function(index) { NetworkManagement.editFirewallRule(index); };
    global.closeFirewallModal = function() { NetworkManagement.closeFirewallModal(); };
    global.addFirewallRule = function() { NetworkManagement.addFirewallRule(); };
    global.removeFirewallRule = function(index) { NetworkManagement.removeFirewallRule(index); };

})(typeof window !== 'undefined' ? window : this);
