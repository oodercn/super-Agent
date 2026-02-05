#!/usr/bin/env powershell

# 页面截图下载脚本
# 此脚本使用PowerShell和Invoke-WebRequest来测试页面访问并创建截图占位文件

# 设置参数
$baseUrl = "http://localhost:8091"
$screenshotsDir = "E:\ooder\ooder-public\super-agent\independent-mcp-agent\screenshots"

# 确保截图目录存在
if (-not (Test-Path $screenshotsDir)) {
    New-Item -ItemType Directory -Path $screenshotsDir -Force
    Write-Host "Created screenshots directory: $screenshotsDir"
}

# 创建角色目录
$roles = @("home", "lan", "enterprise")
foreach ($role in $roles) {
    $roleDir = Join-Path $screenshotsDir $role
    if (-not (Test-Path $roleDir)) {
        New-Item -ItemType Directory -Path $roleDir -Force
        Write-Host "Created role directory: $roleDir"
    }
}

# 页面配置
$pageConfig = @(
    @{
        Role = "home"
        Pages = @(
            @{Name = "dashboard"; Url = "/console/pages/home/home-dashboard.html"},
            @{Name = "device-list"; Url = "/console/pages/home/device-list.html"},
            @{Name = "device-control"; Url = "/console/pages/home/device-control.html"},
            @{Name = "security-status"; Url = "/console/pages/home/security-status.html"}
        )
    },
    @{
        Role = "lan"
        Pages = @(
            @{Name = "dashboard"; Url = "/console/pages/lan/lan-dashboard.html"},
            @{Name = "device-management"; Url = "/console/pages/lan/device-details.html"},
            @{Name = "network-topology"; Url = "/console/pages/lan/network-status.html"},
            @{Name = "security-monitoring"; Url = "/console/pages/lan/network-settings.html"}
        )
    },
    @{
        Role = "enterprise"
        Pages = @(
            @{Name = "dashboard"; Url = "/console/pages/enterprise/enterprise-dashboard.html"},
            @{Name = "analytics-dashboard"; Url = "/console/pages/enterprise/enterprise-dashboard.html"},
            @{Name = "multi-site-management"; Url = "/console/pages/enterprise/enterprise-dashboard.html"},
            @{Name = "advanced-security"; Url = "/console/pages/enterprise/enterprise-dashboard.html"}
        )
    }
)

Write-Host "Starting screenshot download process..."
Write-Host "Base URL: $baseUrl"
Write-Host "Screenshots directory: $screenshotsDir"
Write-Host ""

# 遍历所有角色和页面
foreach ($config in $pageConfig) {
    $role = $config.Role
    $pages = $config.Pages
    $roleDir = Join-Path $screenshotsDir $role
    
    Write-Host "Processing role: $role"
    Write-Host "Role directory: $roleDir"
    Write-Host ""
    
    foreach ($page in $pages) {
        $pageName = $page.Name
        $pageUrl = $baseUrl + $page.Url
        $screenshotPath = Join-Path $roleDir "$pageName.png"
        
        Write-Host "Processing page: $pageName"
        Write-Host "Page URL: $pageUrl"
        Write-Host "Screenshot path: $screenshotPath"
        
        try {
            # 测试页面是否可访问
            $response = Invoke-WebRequest -Uri $pageUrl -UseBasicParsing -ErrorAction Stop
            Write-Host "✓ Page accessible, status code: $($response.StatusCode)"
            
            # 创建截图占位文件
            $screenshotContent = "Screenshot of $role $pageName page"
            $screenshotContent | Out-File -FilePath $screenshotPath -Encoding UTF8 -Force
            Write-Host "✓ Created screenshot placeholder: $screenshotPath"
            
        } catch {
            Write-Host "✗ Error accessing page: $($_.Exception.Message)"
        }
        
        Write-Host ""
    }
}

# 验证结果
Write-Host "Verification:"
Write-Host "Checking created screenshot files..."
Write-Host ""

$totalFiles = 0
foreach ($role in $roles) {
    $roleDir = Join-Path $screenshotsDir $role
    $files = Get-ChildItem $roleDir -Filter "*.png" -ErrorAction SilentlyContinue
    
    if ($files) {
        $fileCount = $files.Count
        $totalFiles += $fileCount
        Write-Host "Role '$role': $fileCount files"
        foreach ($file in $files) {
            Write-Host "  - $($file.Name)"
        }
    } else {
        Write-Host "Role '$role': No files found"
    }
    Write-Host ""
}

Write-Host "Total screenshot files created: $totalFiles"
Write-Host ""
Write-Host "Screenshot download process completed!"
