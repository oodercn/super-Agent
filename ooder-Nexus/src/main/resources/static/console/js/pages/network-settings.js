(function(global) {
    'use strict';

    var NetworkSettings = {
        init: function() {
            window.onPageInit = function() {
                console.log('网络设置页面初始化完成');
            };
        },

        saveSettings: function() {
            var password = document.getElementById('network-password').value;
            var confirmPassword = document.getElementById('confirm-password').value;
            if (password !== confirmPassword) {
                alert('密码不一致，请重新输入');
                return;
            }
            alert('网络设置保存成功！');
        },

        resetSettings: function() {
            if (confirm('确定要重置所有设置吗？')) {
                document.getElementById('network-name').value = 'HomeLAN';
                document.getElementById('network-mode').value = '5ghz';
                document.getElementById('channel').value = 'auto';
                document.getElementById('security-mode').value = 'wpa2';
                alert('设置已重置为默认值');
            }
        }
    };

    NetworkSettings.init();

    global.saveSettings = NetworkSettings.saveSettings;
    global.resetSettings = NetworkSettings.resetSettings;

})(typeof window !== 'undefined' ? window : this);
