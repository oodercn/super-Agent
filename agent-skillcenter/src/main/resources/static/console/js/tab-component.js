// 通用Tab组件 - v3
// 支持所有页面的tab切换功能

// 标记是否已经初始化过事件委托
if (typeof window.tabComponentInitializedV3 === 'undefined') {
    window.tabComponentInitializedV3 = false;
}

// 初始化Tab组件
function initTabComponentV3() {
    console.log('[TabComponentV3] ========================================');
    console.log('[TabComponentV3] 初始化Tab组件');
    console.log('[TabComponentV3] ========================================');

    const tabButtons = document.querySelectorAll('.tab-btn');

    console.log('[TabComponentV3] 找到tab按钮数量:', tabButtons.length);

    if (tabButtons.length === 0) {
        console.log('[TabComponentV3] 没有找到tab按钮，跳过初始化');
        return;
    }

    // 使用事件委托，避免重复绑定
    if (!window.tabComponentInitializedV3) {
        document.addEventListener('click', function(e) {
            const button = e.target.closest('.tab-btn');
            if (!button) return;

            console.log('[TabComponentV3] 点击tab按钮:', button.textContent);

            // 获取tabId，优先从data-tab属性获取
            let tabId = button.getAttribute('data-tab');
            let useExternalHandler = false;

            // 如果没有data-tab属性，尝试从onclick属性解析
            if (!tabId) {
                const onclickAttr = button.getAttribute('onclick');
                if (onclickAttr) {
                    // 匹配 switchExecutionTab('xxx')
                    let match = onclickAttr.match(/switchExecutionTab\(['"]([^'"]+)['"]/);
                    if (match) {
                        tabId = match[1];
                        useExternalHandler = true;
                    }
                    // 匹配 switchTab('xxx', event)
                    if (!tabId) {
                        match = onclickAttr.match(/switchTab\(['"]([^'"]+)['"]/);
                        if (match) {
                            tabId = match[1];
                            useExternalHandler = true;
                        }
                    }
                }
            }

            console.log('[TabComponentV3] tabId:', tabId, 'useExternalHandler:', useExternalHandler);

            if (tabId && !useExternalHandler) {
                e.preventDefault();
                e.stopPropagation();
                handleTabSwitchV3(tabId, button);
            }
        });
        window.tabComponentInitializedV3 = true;
        console.log('[TabComponentV3] 事件委托已设置');
    }

    console.log('[TabComponentV3] Tab组件初始化完成');
}

// 切换Tab处理函数
function handleTabSwitchV3(tabId, clickedButton) {
    console.log('[TabComponentV3] handleTabSwitchV3被调用，tabId:', tabId);

    if (!tabId) {
        console.error('[TabComponentV3] tabId为空');
        return;
    }

    // 查找当前上下文中的tab按钮和内容
    const buttonContainer = clickedButton ? clickedButton.closest('.sharing-tabs, .execution-tabs, .admin-tabs, .group-tabs, [class*="tabs"]') : null;

    let tabButtons;

    if (buttonContainer) {
        tabButtons = buttonContainer.querySelectorAll('.tab-btn');
        console.log('[TabComponentV3] 在容器中找到tab按钮数量:', tabButtons.length);
    } else {
        tabButtons = document.querySelectorAll('.tab-btn');
    }

    // 更新按钮状态 - 根据按钮的 data-tab 属性或位置来匹配
    tabButtons.forEach((button) => {
        const buttonTabId = button.getAttribute('data-tab');
        if (buttonTabId) {
            // 有 data-tab 属性的按钮
            if (buttonTabId === tabId) {
                button.classList.add('active');
            } else {
                button.classList.remove('active');
            }
        } else {
            // 没有 data-tab 属性的按钮（使用 onclick 的）
            // 根据点击的按钮来判断
            if (button === clickedButton) {
                button.classList.add('active');
            } else {
                button.classList.remove('active');
            }
        }
    });

    // 隐藏所有tab内容
    const allTabContents = document.querySelectorAll('.tab-content');
    allTabContents.forEach((content) => {
        content.style.display = 'none';
    });

    // 显示目标内容
    let targetContent = document.getElementById(tabId + '-tab');
    console.log('[TabComponentV3] 尝试查找:', tabId + '-tab', '结果:', targetContent);

    if (!targetContent) {
        targetContent = document.getElementById(tabId + 'Tab');
        console.log('[TabComponentV3] 尝试查找:', tabId + 'Tab', '结果:', targetContent);
    }

    if (targetContent) {
        console.log('[TabComponentV3] 显示目标tab内容:', targetContent.id);
        targetContent.style.display = 'block';
    } else {
        console.error('[TabComponentV3] 未找到目标tab内容:', tabId);
    }
}

// 页面加载时自动初始化Tab组件
document.addEventListener('DOMContentLoaded', function() {
    console.log('[TabComponentV3] DOM加载完成，开始初始化Tab组件');
    initTabComponentV3();
});

// 导出函数供其他模块使用
if (typeof window !== 'undefined') {
    window.initTabComponentV3 = initTabComponentV3;
    window.handleTabSwitchV3 = handleTabSwitchV3;
}
