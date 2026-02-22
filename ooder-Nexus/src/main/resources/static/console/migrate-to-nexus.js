/**
 * Nexus UI Framework 迁移脚本
 * 自动将旧版页面迁移到 Nexus UI Framework v2.0
 * 
 * 使用方法:
 * 1. 在浏览器控制台运行
 * 2. 或在 Node.js 环境中运行
 */

// CSS 类名映射表
const classMapping = {
  // 布局类
  'container': 'nx-container',
  'nexus-container': 'nx-container',
  'sidebar': 'nx-page__sidebar',
  'nexus-sidebar': 'nx-page__sidebar',
  'main-content': 'nx-page__content',
  'nexus-main-content': 'nx-page__content',
  'nexus-page': 'nx-page',
  
  // 按钮类
  'btn': 'nx-btn',
  'nexus-btn': 'nx-btn',
  'btn-primary': 'nx-btn--primary',
  'nexus-btn-primary': 'nx-btn--primary',
  'btn-secondary': 'nx-btn--secondary',
  'nexus-btn-secondary': 'nx-btn--secondary',
  'btn-danger': 'nx-btn--danger',
  'btn-success': 'nx-btn--success',
  'btn-warning': 'nx-btn--warning',
  'btn-ghost': 'nx-btn--ghost',
  'btn-sm': 'nx-btn--sm',
  'btn-lg': 'nx-btn--lg',
  'btn-icon': 'nx-btn--icon',
  
  // 卡片类
  'card': 'nx-card',
  'nexus-card': 'nx-card',
  'status-card': 'nx-stat-card',
  'nexus-status-card': 'nx-stat-card',
  'stats-card': 'nx-card',
  'nexus-stats-card': 'nx-card',
  
  // 表格类
  'table': 'nx-table',
  'nexus-table': 'nx-table',
  'data-table': 'nx-table',
  'nexus-data-table': 'nx-table',
  
  // 表单类
  'form-group': 'nx-form-group',
  'nexus-form-group': 'nx-form-group',
  'form-control': 'nx-input',
  'form-input': 'nx-input',
  'nexus-input': 'nx-input',
  
  // 导航类
  'nav-menu': 'nx-nav',
  'nav-item': 'nx-nav__item',
  'nav-item--active': 'nx-nav__item--active',
  
  // 工具类
  'hidden': 'nx-hidden',
  'text-center': 'nx-text-center',
  'text-primary': 'nx-text-primary',
  'text-secondary': 'nx-text-secondary',
  'text-success': 'nx-text-success',
  'text-warning': 'nx-text-warning',
  'text-danger': 'nx-text-danger',
};

// HTML 属性映射
const attrMapping = {
  'data-theme-toggle': 'data-nx-theme-toggle',
  'data-sidebar-toggle': 'data-nx-sidebar-toggle',
};

/**
 * 迁移 HTML 内容
 * @param {string} html - 原始 HTML
 * @returns {string} - 迁移后的 HTML
 */
function migrateHTML(html) {
  let result = html;
  
  // 1. 替换 CSS 链接
  result = result.replace(
    /<link[^>]*href="[^"]*theme\.css"[^>]*>/g,
    '<link rel="stylesheet" href="../../css/nexus.css">'
  );
  result = result.replace(
    /<link[^>]*href="[^"]*mcpagent\/common\.css"[^>]*>/g,
    ''
  );
  result = result.replace(
    /<link[^>]*href="[^"]*styles\.css"[^>]*>/g,
    ''
  );
  result = result.replace(
    /<link[^>]*href="[^"]*dashboard\.css"[^>]*>/g,
    ''
  );
  
  // 2. 替换 JS 引用
  result = result.replace(
    /<script[^>]*src="[^"]*theme-manager\.js"[^>]*><\/script>/g,
    '<script src="../../js/nexus.js"></script>'
  );
  result = result.replace(
    /<script[^>]*src="[^"]*menu-loader\.js"[^>]*><\/script>/g,
    ''
  );
  result = result.replace(
    /<script[^>]*src="[^"]*common\.js"[^>]*><\/script>/g,
    ''
  );
  
  // 3. 替换类名
  for (const [oldClass, newClass] of Object.entries(classMapping)) {
    const regex = new RegExp(`class="([^"]*)\\b${oldClass}\\b([^"]*)"`, 'g');
    result = result.replace(regex, (match, before, after) => {
      return `class="${before}${newClass}${after}"`;
    });
  }
  
  // 4. 替换属性
  for (const [oldAttr, newAttr] of Object.entries(attrMapping)) {
    const regex = new RegExp(`\\b${oldAttr}\\b`, 'g');
    result = result.replace(regex, newAttr);
  }
  
  // 5. 添加面包屑（如果页面有标题）
  if (result.includes('<h1') && !result.includes('nx-breadcrumb')) {
    result = result.replace(
      /(<header class="nx-page__header">)/g,
      `$1\n          <nav class="nx-breadcrumb">\n            <span class="nx-breadcrumb__item">首页</span>\n            <span class="nx-breadcrumb__item nx-breadcrumb__item--active">当前页面</span>\n          </nav>`
    );
  }
  
  // 6. 替换主题切换按钮
  result = result.replace(
    /<button[^>]*class="[^"]*theme-toggle[^"]*"[^>]*>.*?<\/button>/gs,
    '<button class="nx-btn nx-btn--ghost nx-btn--icon" data-nx-theme-toggle>\n            <i class="ri-sun-line"></i>\n          </button>'
  );
  
  // 7. 添加侧边栏切换按钮
  if (!result.includes('data-nx-sidebar-toggle')) {
    result = result.replace(
      /(<header class="nx-page__header">[\s\S]*?<div class="nx-flex)/g,
      `$1 nx-items-center nx-gap-3">\n          <button class="nx-btn nx-btn--ghost nx-btn--icon" data-nx-sidebar-toggle>\n            <i class="ri-menu-line"></i>\n          </button>`
    );
  }
  
  return result;
}

/**
 * 检查页面是否需要迁移
 * @param {string} html - 页面 HTML
 * @returns {boolean}
 */
function needsMigration(html) {
  // 如果已经使用了 nexus.css，则不需要迁移
  if (html.includes('nexus.css') && !html.includes('theme.css')) {
    return false;
  }
  // 如果包含旧版类名，则需要迁移
  const oldClasses = ['theme.css', 'mcpagent/common.css', 'nexus-container', 'nexus-sidebar'];
  return oldClasses.some(cls => html.includes(cls));
}

// 导出函数
if (typeof module !== 'undefined' && module.exports) {
  module.exports = { migrateHTML, needsMigration, classMapping };
}

// 浏览器环境
if (typeof window !== 'undefined') {
  window.NexusMigrator = { migrateHTML, needsMigration, classMapping };
  console.log('Nexus Migrator 已加载，使用 NexusMigrator.migrateHTML(html) 迁移页面');
}
