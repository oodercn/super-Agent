(function(global) {
    'use strict';

    var BandwidthMonitor = {
        init: function() {
            window.onPageInit = function() {
                console.log('带宽监控页面初始化完成');
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

    BandwidthMonitor.init();

    global.saveBandwidthSettings = BandwidthMonitor.saveBandwidthSettings;
    global.resetBandwidthSettings = BandwidthMonitor.resetBandwidthSettings;

})(typeof window !== 'undefined' ? window : this);
