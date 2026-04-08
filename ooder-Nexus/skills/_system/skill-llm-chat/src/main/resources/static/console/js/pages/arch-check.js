let checkResults = [];

async function initPage() {
    await loadSystemCheck();
}

async function loadSystemCheck() {
    const container = document.getElementById('check-result');
    container.innerHTML = '<div class="nx-flex nx-items-center nx-justify-center nx-p-8"><i class="ri-loader-4-line ri-spin"></i><span>加载中...</span></div>';
    
    try {
        const res = await fetch('/api/v1/arch-check/system');
        const result = await res.json();
        
        if (result.status === 'success' && result.data) {
            renderSystemCheck(result.data);
        } else {
            container.innerHTML = '<p class="nx-text-danger">加载失败</p>';
        }
    } catch (e) {
        container.innerHTML = '<p class="nx-text-danger">加载失败: ' + e.message + '</p>';
    }
}

function renderSystemCheck(data) {
    const container = document.getElementById('check-result');
    
    let html = `
        <div class="nx-grid nx-grid-cols-3 nx-mb-4">
            <div class="nx-stat-card">
                <div class="nx-stat-card__icon" style="background: var(--nx-success-light); color: var(--nx-success);"><i class="ri-checkbox-circle-line"></i></div>
                <div class="nx-stat-card__content">
                    <h4>通过</h4>
                    <p class="nx-stat-card__value" style="color: var(--nx-success);">${data.passed}</p>
                </div>
            </div>
            <div class="nx-stat-card">
                <div class="nx-stat-card__icon" style="background: var(--nx-danger-light); color: var(--nx-danger);"><i class="ri-error-warning-line"></i></div>
                <div class="nx-stat-card__content">
                    <h4>失败</h4>
                    <p class="nx-stat-card__value" style="color: var(--nx-danger);">${data.failed}</p>
                </div>
            </div>
            <div class="nx-stat-card">
                <div class="nx-stat-card__icon" style="background: var(--nx-primary-light); color: var(--nx-primary);"><i class="ri-shield-check-line"></i></div>
                <div class="nx-stat-card__content">
                    <h4>总检查项</h4>
                    <p class="nx-stat-card__value">${data.total}</p>
                </div>
            </div>
        </div>
        
        <div class="nx-table-container">
            <table class="nx-table">
                <thead>
                    <tr>
                        <th>检查项</th>
                        <th>描述</th>
                        <th>状态</th>
                        <th>结果</th>
                    </tr>
                </thead>
                <tbody>
    `;
    
    data.checks.forEach(check => {
        const statusIcon = check.status === 'pass' ? 
            '<i class="ri-checkbox-circle-line" style="color: var(--nx-success);"></i>' :
            check.status === 'fail' ? 
            '<i class="ri-error-warning-line" style="color: var(--nx-danger);"></i>' :
            '<i class="ri-loader-4-line ri-spin" style="color: var(--nx-warning);"></i>';
        
        const statusBadge = check.status === 'pass' ?
            '<span class="nx-badge nx-badge--success">通过</span>' :
            check.status === 'fail' ?
            '<span class="nx-badge nx-badge--danger">失败</span>' :
            '<span class="nx-badge nx-badge--warning">警告</span>';
        
        html += `
            <tr>
                <td><span class="nx-font-medium">${check.name}</span></td>
                <td>${check.description}</td>
                <td>${statusIcon}</td>
                <td>${check.message || statusBadge}</td>
            </tr>
        `;
    });
    
    html += '</tbody></table></div>';
    
    if (data.status === 'healthy') {
        html += `
            <div class="nx-mt-4 nx-p-4 nx-bg-success-light nx-rounded-lg">
                <div class="nx-flex nx-items-center nx-gap-2">
                    <i class="ri-checkbox-circle-line" style="color: var(--nx-success); font-size: 20px;"></i>
                    <span class="nx-font-medium" style="color: var(--nx-success);">${data.passed}/${data.total} 项检查全部通过，系统运行正常</span>
                </div>
            </div>
        `;
    } else if (data.failed > 0) {
        html += `
            <div class="nx-mt-4 nx-p-4 nx-bg-danger-light nx-rounded-lg">
                <div class="nx-flex nx-items-center nx-gap-2">
                    <i class="ri-error-warning-line" style="color: var(--nx-danger); font-size: 20px;"></i>
                    <span class="nx-font-medium" style="color: var(--nx-danger);">${data.failed} 项检查未通过，请检查相关配置</span>
                </div>
            </div>
        `;
    }
    
    container.innerHTML = html;
}

async function runAllChecks() {
    await loadSystemCheck();
}

document.addEventListener('DOMContentLoaded', initPage);
