(function(global) {
    'use strict';

    var TrafficMonitor = {
        init: function() {
            window.onPageInit = function() {
            };
        },

        saveBandwidthSettings: function() {
            alert('带宽限制设置保存成功！');
        },

        resetBandwidthSettings: function() {
            if (confirm('确定要重置带宽限制设置吗？')) {
                alert('带宽限制设置已重置为默认值');
            }
        }
    };

    TrafficMonitor.init();

    global.saveBandwidthSettings = TrafficMonitor.saveBandwidthSettings;
    global.resetBandwidthSettings = TrafficMonitor.resetBandwidthSettings;

})(typeof window !== 'undefined' ? window : this);
