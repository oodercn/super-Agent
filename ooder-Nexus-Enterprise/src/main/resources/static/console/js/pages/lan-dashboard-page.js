(function(global) {
    'use strict';

    var LanDashboard = {
        init: function() {
            window.onPageInit = function() {
                console.log('LAN Dashboard 页面初始化完成');
                if (typeof init === 'function') {
                    init();
                }
            };
        },

        refreshData: function() {
            location.reload();
        },

        viewTopology: function() {
            window.location.href = '../nexus/network-topology.html';
        },

        viewBandwidthDetails: function() {
            window.location.href = 'bandwidth-monitor.html';
        },

        viewAllDevices: function() {
            window.location.href = 'network-devices.html';
        }
    };

    LanDashboard.init();

    global.refreshData = LanDashboard.refreshData;
    global.viewTopology = LanDashboard.viewTopology;
    global.viewBandwidthDetails = LanDashboard.viewBandwidthDetails;
    global.viewAllDevices = LanDashboard.viewAllDevices;

})(typeof window !== 'undefined' ? window : this);
