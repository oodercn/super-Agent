function renderNxPageHeader(options = {}) {
    const defaults = {
        title: '页面标题',
        subtitle: '',
        icon: 'ri-file-line',
        actions: ''
    };
    
    const config = { ...defaults, ...options };
    
    return `
        <div class="nx-page__overlay" id="page-overlay"></div>
        <aside class="nx-page__sidebar">
            <div class="nx-page__sidebar-toggle" id="sidebar-toggle">
                <i class="ri-arrow-left-s-line"></i>
            </div>
            <div class="nx-p-4 nx-mb-4 nx-page__sidebar-title">
                <h1 class="nx-text-lg nx-font-bold">
                    <i class="ri-server-line"></i> Apex OS
                </h1>
            </div>
            <ul class="nav-menu" id="nav-menu"></ul>
        </aside>
        
        <main class="nx-page__content">
            <header class="nx-page__header">
                <div class="nx-flex nx-items-center nx-gap-4">
                    <div class="nx-page__mobile-toggle" id="mobile-toggle">
                        <i class="ri-menu-line" style="font-size: 20px;"></i>
                    </div>
                    <div class="nx-page__title">
                        <h1><i class="${config.icon}"></i> ${config.title}</h1>
                        ${config.subtitle ? `<p>${config.subtitle}</p>` : ''}
                    </div>
                </div>
                <div class="nx-page__actions">
                    ${config.actions}
                    <div class="nx-page__theme-toggle" id="theme-toggle" title="切换主题">
                        <i class="ri-moon-line" id="theme-icon"></i>
                    </div>
                    <div class="nx-page__dropdown">
                        <div class="nx-page__user-menu" id="user-menu">
                            <div class="nx-page__user-avatar" id="user-avatar">U</div>
                            <div class="nx-page__user-info">
                                <span class="nx-page__user-name" id="user-name">加载中...</span>
                                <span class="nx-page__user-role" id="user-role"></span>
                            </div>
                            <i class="ri-arrow-down-s-line"></i>
                        </div>
                        <div class="nx-page__dropdown-menu" id="user-dropdown">
                            <div class="nx-page__dropdown-item" onclick="window.location.href='my-profile.html'">
                                <i class="ri-user-line"></i> 个人中心
                            </div>
                            <div class="nx-page__dropdown-item" onclick="window.location.href='my-capabilities.html'">
                                <i class="ri-puzzle-line"></i> 我的能力
                            </div>
                            <div class="nx-page__dropdown-item" onclick="window.location.href='my-scenes.html'">
                                <i class="ri-folder-line"></i> 我的场景
                            </div>
                            <div class="nx-page__dropdown-item nx-page__dropdown-item--danger" onclick="nxPage.logout()">
                                <i class="ri-logout-box-line"></i> 退出登录
                            </div>
                        </div>
                    </div>
                </div>
            </header>
            
            <div class="nx-page__main">
                <div class="nx-container">
    `;
}

function renderNxPageFooter() {
    return `
                </div>
            </div>
        </main>
    `;
}

function wrapNxPage(content, options = {}) {
    return `
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${options.title || '页面'} - Apex OS</title>
    <link rel="stylesheet" href="/console/css/remixicon/remixicon.css">
    <link rel="stylesheet" href="/console/css/nexus.css">
    <link rel="stylesheet" href="/console/css/nx-page.css">
    ${options.extraStyles || ''}
</head>
<body>
    <div class="nx-page">
        ${renderNxPageHeader(options)}
        ${content}
        ${renderNxPageFooter()}
    </div>
    
    <script src="/console/js/nexus.js"></script>
    <script src="/console/js/menu.js"></script>
    <script src="/console/js/nx-page.js"></script>
    <script src="/console/js/page-init.js" data-auto-init></script>
    ${options.extraScripts || ''}
</body>
</html>
    `;
}
