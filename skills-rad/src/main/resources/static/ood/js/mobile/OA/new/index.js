/**
 * 移动端OA办公组件库入口文件（规范版本）
 * 导出所有OA办公相关组件
 * 遵循OOD开发规范重构
 */

// 确保ood.Mobile.OA命名空间存在
if (typeof ood !== 'undefined' && ood.Mobile) {
    if (!ood.Mobile.OA) {
        ood.Mobile.OA = {};
    }
} else {
    console.warn('ood framework not ready, OA components may not work properly');
}

// 导入所有OA组件
// 组件会在各自文件中自动注册到ood.Class系统

// 导出组件列表
if (typeof ood !== 'undefined' && ood.Mobile && ood.Mobile.OA) {
    ood.Mobile.OA.Components = {
        Notice: 'ood.Mobile.OA.new.Notice',
        TodoList: 'ood.Mobile.OA.new.TodoList',
        // 其他组件将在后续添加
    };
    
    // 组件库信息
    ood.Mobile.OA.Info = {
        name: '移动端OA办公组件库（规范版本）',
        version: '1.0.0',
        description: '一套遵循OOD开发规范的移动端OA办公组件解决方案',
        author: 'OOD Team',
        created: '2025-09-18'
    };
}

console.log('移动端OA办公组件库（规范版本）加载完成');