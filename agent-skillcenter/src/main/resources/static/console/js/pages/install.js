/**
 * 安装页面脚本
 */

// 开始安装
function startInstall() {
    // 显示进度条
    document.getElementById('progress-container').style.display = 'block';
    document.getElementById('button-group').style.display = 'none';
    
    // 模拟安装过程
    let progress = 0;
    const progressFill = document.getElementById('progress-fill');
    const progressText = document.getElementById('progress-text');
    
    const interval = setInterval(() => {
        progress += 5;
        progressFill.style.width = progress + '%';
        
        if (progress < 25) {
            progressText.textContent = '正在初始化系统...';
        } else if (progress < 50) {
            progressText.textContent = '正在配置数据库...';
        } else if (progress < 75) {
            progressText.textContent = '正在创建管理员账号...';
        } else {
            progressText.textContent = '正在完成安装...';
        }
        
        if (progress >= 100) {
            clearInterval(interval);
            setTimeout(() => {
                document.getElementById('progress-container').style.display = 'none';
                document.getElementById('success-message').style.display = 'block';
            }, 1000);
        }
    }, 200);
}

// 取消安装
function cancelInstall() {
    if (confirm('确定要取消安装吗？')) {
        window.location.href = 'index.html';
    }
}

// 点击成功页面的按钮跳转到登录页
document.addEventListener('DOMContentLoaded', function() {
    const successMessage = document.getElementById('success-message');
    if (successMessage) {
        const button = document.createElement('button');
        button.className = 'btn btn-primary';
        button.textContent = '进入系统';
        button.onclick = function() {
            window.location.href = 'login.html';
        };
        successMessage.appendChild(button);
    }
});
