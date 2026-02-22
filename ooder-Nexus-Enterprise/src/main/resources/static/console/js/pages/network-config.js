(function(global) {
    'use strict';

    var NetworkConfig = {
        init: function() {
            window.onPageInit = function() {
                // 页面特定的初始化代码
            };
        },

        saveConfig: function() {
            alert('网络配置保存成功！');
        },

        resetConfig: function() {
            if (confirm('确定要重置网络配置吗？')) {
                alert('网络配置已重置为默认值');
            }
        }
    };

    NetworkConfig.init();

    global.saveConfig = NetworkConfig.saveConfig;
    global.resetConfig = NetworkConfig.resetConfig;

})(typeof window !== 'undefined' ? window : this);
