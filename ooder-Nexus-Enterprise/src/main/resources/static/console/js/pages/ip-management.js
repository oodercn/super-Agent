(function(global) {
    'use strict';

    var IpManagement = {
        usedIPs: [1, 10, 20, 30, 100, 101, 102, 103, 104, 105],
        reservedIPs: [2, 3, 4, 5, 6, 7, 8, 9],

        init: function() {
            window.onPageInit = function() {
                console.log('IP管理页面初始化完成');
                IpManagement.generateIPPool();
            };
        },

        generateIPPool: function() {
            var pool = document.getElementById('ip-pool');
            if (!pool) return;

            for (var i = 1; i <= 254; i++) {
                var block = document.createElement('div');
                block.className = 'ip-block';
                block.textContent = i;
                block.title = '192.168.1.' + i;

                if (IpManagement.reservedIPs.indexOf(i) !== -1) {
                    block.classList.add('reserved');
                } else if (IpManagement.usedIPs.indexOf(i) !== -1) {
                    block.classList.add('used');
                } else {
                    block.classList.add('available');
                }

                (function(ip) {
                    block.onclick = function() {
                        IpManagement.showIPDetails(ip);
                    };
                })(i);

                pool.appendChild(block);
            }
        },

        showIPDetails: function(ip) {
            alert('IP 地址: 192.168.1.' + ip);
        },

        showAddStaticIP: function() {
            alert('添加静态 IP 分配');
        },

        showAddStaticIPForm: function() {
            alert('添加静态 IP 功能开发中...');
        },

        saveIpRangeSettings: function() {
            alert('IP 范围设置保存成功！');
        },

        resetIpRangeSettings: function() {
            if (confirm('确定要重置 IP 范围设置吗？')) {
                alert('IP 范围设置已重置为默认值');
            }
        }
    };

    IpManagement.init();

    global.showIPDetails = IpManagement.showIPDetails;
    global.showAddStaticIP = IpManagement.showAddStaticIP;
    global.showAddStaticIPForm = IpManagement.showAddStaticIPForm;
    global.saveIpRangeSettings = IpManagement.saveIpRangeSettings;
    global.resetIpRangeSettings = IpManagement.resetIpRangeSettings;

})(typeof window !== 'undefined' ? window : this);
