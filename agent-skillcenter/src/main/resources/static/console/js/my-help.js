// 事件监听
document.addEventListener('DOMContentLoaded', function() {
    // 标签切换
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            // 移除所有标签的active类
            document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
            // 添加当前标签的active类
            this.classList.add('active');
            // 隐藏所有内容
            document.querySelectorAll('.tab-content').forEach(content => content.style.display = 'none');
            // 显示当前标签的内容
            const tabId = this.getAttribute('data-tab');
            document.getElementById(`${tabId}-tab`).style.display = 'block';
        });
    });
});
