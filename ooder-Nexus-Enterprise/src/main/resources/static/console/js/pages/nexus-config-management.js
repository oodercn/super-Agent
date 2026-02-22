(function(global) {
    'use strict';

    var NexusConfigManagement = {
        init: function() {
            window.onPageInit = function() {
                console.log('配置管理页面初始化完成');
            };
        },

        saveConfig: function() {
            alert('配置保存成功！');
        },

        resetConfig: function() {
            if (confirm('确定要重置为默认配置吗？')) {
                alert('配置已重置为默认值');
            }
        }
    };

    NexusConfigManagement.init();

    global.saveConfig = NexusConfigManagement.saveConfig;
    global.resetConfig = NexusConfigManagement.resetConfig;

})(typeof window !== 'undefined' ? window : this);
