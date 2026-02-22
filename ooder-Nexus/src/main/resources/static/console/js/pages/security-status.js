(function(global) {
    'use strict';

    var SecurityStatus = {
        init: function() {
            window.onPageInit = function() {
                // 页面特定的初始化代码
            };
        },

        refreshSecurityStatus: function() {
            alert('刷新安全状态...');
        },

        runSecurityScan: function() {
            alert('开始安全扫描...');
        }
    };

    SecurityStatus.init();

    global.refreshSecurityStatus = SecurityStatus.refreshSecurityStatus;
    global.runSecurityScan = SecurityStatus.runSecurityScan;

})(typeof window !== 'undefined' ? window : this);
