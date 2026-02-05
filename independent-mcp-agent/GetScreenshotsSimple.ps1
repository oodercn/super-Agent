# 简单浏览器截图脚本

# 设置参数
$baseUrl = "http://localhost:8091"
$screenshotsDir = "E:\ooder\ooder-public\super-agent\independent-mcp-agent\screenshots"

# 确保截图目录存在
if (-not (Test-Path $screenshotsDir)) {
    New-Item -ItemType Directory -Path $screenshotsDir -Force
}

# 创建角色目录
$roles = @("home", "lan", "enterprise")
foreach ($role in $roles) {
    $roleDir = Join-Path $screenshotsDir $role
    if (-not (Test-Path $roleDir)) {
        New-Item -ItemType Directory -Path $roleDir -Force
    }
}

# 页面配置
$pages = @(
    @{
        role = "home"
        pages = @(
            @{name = "dashboard"; url = "/console/pages/home/home-dashboard.html"},
            @{name = "device-list"; url = "/console/pages/home/device-list.html"},
            @{name = "device-control"; url = "/console/pages/home/device-control.html"},
            @{name = "security-status"; url = "/console/pages/home/security-status.html"}
        )
    },
    @{
        role = "lan"
        pages = @(
            @{name = "dashboard"; url = "/console/pages/lan/lan-dashboard.html"},
            @{name = "device-management"; url = "/console/pages/lan/device-details.html"},
            @{name = "network-topology"; url = "/console/pages/lan/network-status.html"},
            @{name = "security-monitoring"; url = "/console/pages/lan/network-settings.html"}
        )
    },
    @{
        role = "enterprise"
        pages = @(
            @{name = "dashboard"; url = "/console/pages/enterprise/enterprise-dashboard.html"},
            @{name = "analytics-dashboard"; url = "/console/pages/enterprise/enterprise-dashboard.html"},
            @{name = "multi-site-management"; url = "/console/pages/enterprise/enterprise-dashboard.html"},
            @{name = "advanced-security"; url = "/console/pages/enterprise/enterprise-dashboard.html"}
        )
    }
)

# 检查Chrome是否安装
$chromePath = "C:\Program Files\Google\Chrome\Application\chrome.exe"
if (-not (Test-Path $chromePath)) {
    Write-Host "Chrome浏览器未找到，请先安装Chrome浏览器" -ForegroundColor Red
    exit 1
}

# 检查ChromeDriver是否存在
$chromeDriverPath = "E:\ooder\ooder-public\super-agent\independent-mcp-agent\chromedriver.exe"
if (-not (Test-Path $chromeDriverPath)) {
    Write-Host "ChromeDriver未找到，正在下载..." -ForegroundColor Yellow
    
    # 下载ChromeDriver
    $chromeDriverUrl = "https://chromedriver.storage.googleapis.com/108.0.5359.71/chromedriver_win32.zip"
    $zipPath = "E:\ooder\ooder-public\super-agent\independent-mcp-agent\chromedriver.zip"
    
    try {
        Invoke-WebRequest -Uri $chromeDriverUrl -OutFile $zipPath
        Expand-Archive -Path $zipPath -DestinationPath "E:\ooder\ooder-public\super-agent\independent-mcp-agent"
        Remove-Item $zipPath -Force
        Write-Host "ChromeDriver下载并解压成功" -ForegroundColor Green
    }
    catch {
        Write-Host "ChromeDriver下载失败: $_" -ForegroundColor Red
        exit 1
    }
# 创建PowerShell脚本来使用ChromeDriver
$webdriverScript = @'
# 使用ChromeDriver获取截图
$chromeDriverPath = "E:\ooder\ooder-public\super-agent\independent-mcp-agent\chromedriver.exe"
$baseUrl = "http://localhost:8091"
$screenshotsDir = "E:\ooder\ooder-public\super-agent\independent-mcp-agent\screenshots"

# 导入必要的命名空间
Add-Type -AssemblyName System.Web

# 启动ChromeDriver
$chromeDriverProcess = Start-Process -FilePath $chromeDriverPath -PassThru -WindowStyle Hidden

# 等待ChromeDriver启动
Start-Sleep -Seconds 2

# 简单的HTTP客户端函数
function Invoke-ChromeDriverRequest {
    param(
        [string]$endpoint,
        [string]$method = "POST",
        [string]$body = ""
    )
    
    $url = "http://localhost:9515/$endpoint"
    $headers = @{"Content-Type" = "application/json"}
    
    try {
        if ($method -eq "POST") {
            return Invoke-RestMethod -Uri $url -Method POST -Headers $headers -Body $body -ErrorAction Stop
        } else {
            return Invoke-RestMethod -Uri $url -Method GET -Headers $headers -ErrorAction Stop
        }
    } catch {
        Write-Host "请求失败: $_" -ForegroundColor Red
        return $null
    }
}

# 启动会话
try {
    # 创建会话
    $sessionBody = '{"capabilities":{"alwaysMatch":{"browserName":"chrome","goog:chromeOptions":{"args":["--headless","--disable-gpu","--window-size=1920,1080"]}}}}'
    $sessionResponse = Invoke-ChromeDriverRequest -endpoint "session" -body $sessionBody
    
    if ($sessionResponse.sessionId) {
        $sessionId = $sessionResponse.sessionId
        Write-Host "成功创建浏览器会话: $sessionId" -ForegroundColor Green
        
        # 页面配置
        $pages = @(
            @{
                role = "home"
                pages = @(
                    @{name = "dashboard"; url = "/console/pages/home/home-dashboard.html"},
                    @{name = "device-list"; url = "/console/pages/home/device-list.html"},
                    @{name = "device-control"; url = "/console/pages/home/device-control.html"},
                    @{name = "security-status"; url = "/console/pages/home/security-status.html"}
                )
            },
            @{
                role = "lan"
                pages = @(
                    @{name = "dashboard"; url = "/console/pages/lan/lan-dashboard.html"},
                    @{name = "device-management"; url = "/console/pages/lan/device-details.html"},
                    @{name = "network-topology"; url = "/console/pages/lan/network-status.html"},
                    @{name = "security-monitoring"; url = "/console/pages/lan/network-settings.html"}
                )
            },
            @{
                role = "enterprise"
                pages = @(
                    @{name = "dashboard"; url = "/console/pages/enterprise/enterprise-dashboard.html"},
                    @{name = "analytics-dashboard"; url = "/console/pages/enterprise/enterprise-dashboard.html"},
                    @{name = "multi-site-management"; url = "/console/pages/enterprise/enterprise-dashboard.html"},
                    @{name = "advanced-security"; url = "/console/pages/enterprise/enterprise-dashboard.html"}
                )
            }
        )
        
        # 遍历所有页面
        foreach ($roleConfig in $pages) {
            $role = $roleConfig.role
            $rolePages = $roleConfig.pages
            $roleDir = Join-Path $screenshotsDir $role
            
            Write-Host "正在获取 $role 角色页面截图..." -ForegroundColor Cyan
            
            foreach ($page in $rolePages) {
                $pageName = $page.name
                $pageUrl = $baseUrl + $page.url
                $screenshotPath = Join-Path $roleDir "$pageName.png"
                
                try {
                    # 导航到页面
                    $navigateBody = '{"url":"' + $pageUrl + '"}'
                    Invoke-ChromeDriverRequest -endpoint "session/$sessionId/url" -body $navigateBody
                    
                    # 等待页面加载
                    Start-Sleep -Seconds 3
                    
                    # 获取截图
                    $screenshotResponse = Invoke-ChromeDriverRequest -endpoint "session/$sessionId/screenshot" -method "GET"
                    
                    if ($screenshotResponse.value) {
                        # 解码Base64截图
                        $screenshotBytes = [System.Convert]::FromBase64String($screenshotResponse.value)
                        [System.IO.File]::WriteAllBytes($screenshotPath, $screenshotBytes)
                        Write-Host "✓ 成功获取 $pageName 页面截图" -ForegroundColor Green
                    } else {
                        Write-Host "✗ 获取 $pageName 页面截图失败: 无响应" -ForegroundColor Red
                    }
                } catch {
                    Write-Host "✗ 获取 $pageName 页面截图失败: $_" -ForegroundColor Red
                }
            }
        }
        
        # 关闭会话
        Invoke-ChromeDriverRequest -endpoint "session/$sessionId" -method "DELETE"
        Write-Host "成功关闭浏览器会话" -ForegroundColor Green
    } else {
        Write-Host "创建浏览器会话失败" -ForegroundColor Red
    }
} catch {
    Write-Host "执行失败: $_" -ForegroundColor Red
} finally {
    # 停止ChromeDriver进程
    if ($chromeDriverProcess) {
        Stop-Process -Id $chromeDriverProcess.Id -Force -ErrorAction SilentlyContinue
    }
    Write-Host "所有操作完成" -ForegroundColor Green
}
'@

# 保存临时脚本
$tempScriptPath = "E:\ooder\ooder-public\super-agent\independent-mcp-agent\TempWebDriverScript.ps1"
$webdriverScript | Out-File -FilePath $tempScriptPath -Encoding UTF8

# 运行截图脚本
Write-Host "开始获取页面截图..." -ForegroundColor Cyan
& powershell.exe -ExecutionPolicy Bypass -File $tempScriptPath

# 清理临时脚本
Remove-Item $tempScriptPath -Force -ErrorAction SilentlyContinue

Write-Host "截图操作完成" -ForegroundColor Green
