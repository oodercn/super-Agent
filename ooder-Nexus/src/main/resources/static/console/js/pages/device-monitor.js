(function(global) {
    'use strict';

    var DeviceMonitor = {
        init: function() {
            window.onPageInit = function() {
            };
        },

        refreshDevices: function() {
            alert('刷新设备列表...');
        }
    };

    DeviceMonitor.init();

    global.refreshDevices = DeviceMonitor.refreshDevices;

})(typeof window !== 'undefined' ? window : this);
