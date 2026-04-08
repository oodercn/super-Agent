(function(global) {
'use strict';

var CapabilityDiscovery = {
    init: function() {
        console.log('[CapabilityDiscovery] Initializing...');
        DiscoveryCore.init();
    }
};

global.CapabilityDiscovery = CapabilityDiscovery;

document.addEventListener('DOMContentLoaded', function() {
    CapabilityDiscovery.init();
});

})(typeof window !== 'undefined' ? window : this);
