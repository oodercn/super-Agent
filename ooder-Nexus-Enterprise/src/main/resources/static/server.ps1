using namespace System.Net
using namespace System.IO

# 创建HTTP监听器
$listener = New-Object HttpListener
$listener.Prefixes.Add('http://localhost:8000/')
$listener.Start()
Write-Host 'HTTP server started on http://localhost:8000'
Write-Host 'Serving files from: ' + (Get-Location).Path

# 处理请求
while ($listener.IsListening) {
    try {
        $context = $listener.GetContext()
        $request = $context.Request
        $response = $context.Response
        
        # 处理请求路径
        $path = $request.Url.LocalPath
        if ($path -eq '/') {
            $path = '/console/index.html'
        }
        
        # 构建文件路径
        $filePath = Join-Path (Get-Location).Path $path.TrimStart('/')
        
        # 检查文件是否存在
        if (Test-Path $filePath -PathType Leaf) {
            # 读取文件内容
            $content = Get-Content $filePath -Encoding Byte
            $response.ContentLength64 = $content.Length
            
            # 设置内容类型
            $ext = [IO.Path]::GetExtension($filePath)
            switch ($ext) {
                '.html' { $response.ContentType = 'text/html' }
                '.css' { $response.ContentType = 'text/css' }
                '.js' { $response.ContentType = 'application/javascript' }
                '.png' { $response.ContentType = 'image/png' }
                '.jpg' { $response.ContentType = 'image/jpeg' }
                '.gif' { $response.ContentType = 'image/gif' }
                default { $response.ContentType = 'application/octet-stream' }
            }
            
            # 发送响应
            $response.OutputStream.Write($content, 0, $content.Length)
            Write-Host 'Served: ' $path
        } else {
            # 文件不存在
            $response.StatusCode = 404
            $response.ContentType = 'text/plain'
            $content = [Text.Encoding]::UTF8.GetBytes('404 Not Found')
            $response.ContentLength64 = $content.Length
            $response.OutputStream.Write($content, 0, $content.Length)
            Write-Host '404: ' $path
        }
        
        $response.Close()
    } catch {
        Write-Host 'Error: ' $_.Exception.Message
    }
}
