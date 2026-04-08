/**
 * 执行管理页面 Tab 切换功能
 * 由于页面通过 AJAX 加载，需要将函数定义为全局函数
 */

// 执行管理页面专用的tab切换函数
function switchExecutionTab(tabId) {
    console.log('[ExecutionTab] 切换tab:', tabId);
    
    // 获取所有tab按钮
    const tabButtons = document.querySelectorAll('.execution-tabs .tab-btn');
    console.log('[ExecutionTab] 找到tab按钮数量:', tabButtons.length);
    
    // 获取所有tab内容
    const tabContents = document.querySelectorAll('.execution-content .tab-content');
    console.log('[ExecutionTab] 找到tab内容数量:', tabContents.length);
    
    // 移除所有按钮的active类
    tabButtons.forEach((button, index) => {
        console.log('[ExecutionTab] 移除active类，按钮', index);
        button.classList.remove('active');
    });
    
    // 为当前点击的按钮添加active类
    const clickedButton = event.target;
    console.log('[ExecutionTab] 点击的按钮:', clickedButton.textContent);
    clickedButton.classList.add('active');
    
    // 隐藏所有tab内容
    tabContents.forEach((content, index) => {
        console.log('[ExecutionTab] 隐藏tab内容', index, ':', content.id);
        content.style.display = 'none';
    });
    
    // 显示目标tab内容
    const targetContent = document.getElementById(tabId + '-tab');
    console.log('[ExecutionTab] 查找目标内容:', tabId + '-tab', '结果:', targetContent);
    
    if (targetContent) {
        console.log('[ExecutionTab] 显示目标tab内容:', targetContent.id);
        targetContent.style.display = 'block';
    } else {
        console.error('[ExecutionTab] 未找到目标tab内容:', tabId + '-tab');
    }
}

console.log('[ExecutionTab] execution-tabs.js 已加载，switchExecutionTab 函数已定义');
