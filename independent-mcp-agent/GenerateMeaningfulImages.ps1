# 生成有意义的PNG图片脚本
# 使用System.Drawing创建包含实际内容的图片

# 加载System.Drawing
Add-Type -AssemblyName System.Drawing

# 定义图片尺寸
$width = 800
$height = 600

# 定义颜色
$backgroundColor = [System.Drawing.Color]::White
$textColor = [System.Drawing.Color]::Black
$borderColor = [System.Drawing.Color]::LightGray
$headerColor = [System.Drawing.Color]::LightBlue

# 图片信息映射
$imageInfoMap = @{
    # Home用户图片
    "home\dashboard.png" = @{ Title = "Home Dashboard"; Description = "Home user main dashboard with device status" }
    "home\device-list.png" = @{ Title = "Device List"; Description = "List of devices for home user" }
    "home\device-control.png" = @{ Title = "Device Control"; Description = "Control interface for home devices" }
    "home\security-status.png" = @{ Title = "Security Status"; Description = "Home security monitoring status" }
    
    # LAN管理员图片
    "lan\dashboard.png" = @{ Title = "LAN Dashboard"; Description = "LAN admin main dashboard" }
    "lan\device-management.png" = @{ Title = "Device Management"; Description = "LAN device management interface" }
    "lan\network-topology.png" = @{ Title = "Network Topology"; Description = "LAN network topology view" }
    "lan\security-monitoring.png" = @{ Title = "Security Monitoring"; Description = "LAN security monitoring interface" }
    
    # Enterprise管理员图片
    "enterprise\dashboard.png" = @{ Title = "Enterprise Dashboard"; Description = "Enterprise main dashboard" }
    "enterprise\analytics-dashboard.png" = @{ Title = "Analytics Dashboard"; Description = "Enterprise analytics and reporting" }
    "enterprise\multi-site-management.png" = @{ Title = "Multi-site Management"; Description = "Enterprise multi-site management" }
    "enterprise\advanced-security.png" = @{ Title = "Advanced Security"; Description = "Enterprise advanced security settings" }
}

# 创建screenshots目录结构
$baseDir = "./screenshots"
if (-not (Test-Path $baseDir)) {
    New-Item -ItemType Directory -Path $baseDir -Force
}

@("home", "lan", "enterprise") | ForEach-Object {
    $dir = Join-Path $baseDir $_
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force
    }
}

# 生成图片
$imageInfoMap.GetEnumerator() | ForEach-Object {
    $relativePath = $_.Key
    $info = $_.Value
    $fullPath = Join-Path $baseDir $relativePath
    
    try {
        # 创建位图
        $bitmap = New-Object System.Drawing.Bitmap($width, $height)
        $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
        
        # 设置质量
        $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
        $graphics.TextRenderingHint = [System.Drawing.Text.TextRenderingHint]::AntiAliasGridFit
        
        # 绘制背景
        $graphics.Clear($backgroundColor)
        
        # 绘制边框
        $pen1 = New-Object System.Drawing.Pen($borderColor, 2)
        $graphics.DrawRectangle($pen1, 0, 0, $width - 1, $height - 1)
        $pen1.Dispose()
        
        # 绘制标题栏
        $headerBrush = New-Object System.Drawing.SolidBrush($headerColor)
        $graphics.FillRectangle($headerBrush, 0, 0, $width, 60)
        $headerBrush.Dispose()
        
        # 绘制标题
        $titleFont = New-Object System.Drawing.Font("Arial", 18, [System.Drawing.FontStyle]::Bold)
        $textBrush = New-Object System.Drawing.SolidBrush($textColor)
        $graphics.DrawString($info.Title, $titleFont, $textBrush, 20, 15)
        $titleFont.Dispose()
        
        # 绘制描述
        $descFont = New-Object System.Drawing.Font("Arial", 12)
        $graphics.DrawString($info.Description, $descFont, $textBrush, 20, 80)
        $descFont.Dispose()
        
        # 绘制示例内容框
        $pen2 = New-Object System.Drawing.Pen($borderColor, 1)
        $graphics.DrawRectangle($pen2, 20, 120, $width - 40, $height - 140)
        $pen2.Dispose()
        
        # 绘制示例内容
        $exampleFont = New-Object System.Drawing.Font("Arial", 10)
        $exampleText = "Example content for $($info.Title)`n`nThis is a placeholder image generated for documentation purposes.`nIn a real application, this would show the actual UI elements."
        $graphics.DrawString($exampleText, $exampleFont, $textBrush, 30, 140)
        $exampleFont.Dispose()
        $textBrush.Dispose()
        
        # 保存图片
        $bitmap.Save($fullPath, [System.Drawing.Imaging.ImageFormat]::Png)
        
        # 清理资源
        $graphics.Dispose()
        $bitmap.Dispose()
        
        Write-Host "Generated: $fullPath"
        
    } catch {
        Write-Host "Error generating ${fullPath}: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`nImage generation completed!" -ForegroundColor Green
