/**
 * 登录页面脚本
 */

// 处理登录
function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    // 隐藏所有消息
    document.getElementById('error-message').style.display = 'none';
    document.getElementById('success-message').style.display = 'none';
    
    // 简单的登录验证
    if (username === 'admin' && password === 'admin123') {
        // 显示成功消息
        document.getElementById('success-message').style.display = 'block';
        
        // 模拟登录成功后跳转
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 1500);
    } else {
        // 显示错误消息
        document.getElementById('error-message').style.display = 'block';
    }
}

// 跳转到安装页面
function goToInstall() {
    window.location.href = 'install.html';
}

// 检查是否需要显示安装页面
document.addEventListener('DOMContentLoaded', function() {
    // 这里可以添加逻辑来检查系统是否已安装
    // 例如检查本地存储或发送API请求
});
