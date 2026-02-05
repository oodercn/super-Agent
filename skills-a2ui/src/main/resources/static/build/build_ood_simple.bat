@echo off
:: build_ood_simple.bat
:: 简化版A2UI一键构建脚本

:: 设置编码为UTF-8，解决中文乱码问题
chcp 65001 >nul

:: 配置变量
set REL_PATH=..\
set COMPRESS_TOOL=%REL_PATH%build/tools/yuicompressor.jar
set OUT_PATH=runtime\
set OUT_ROOT=%REL_PATH%%OUT_PATH%

:: 检查Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java环境未找到，请确保已安装Java并配置环境变量
    pause
    exit /b 1
)

:: 创建输出目录
mkdir %OUT_PATH% 2>nul
mkdir %OUT_PATH%ood 2>nul
mkdir %OUT_PATH%ood\js 2>nul
mkdir %OUT_PATH%ood\js\UI 2>nul
mkdir %OUT_PATH%ood\js\Module 2>nul
mkdir %OUT_PATH%ood\Locale 2>nul
mkdir %OUT_PATH%ood\appearance 2>nul
mkdir %OUT_PATH%ood\iconfont 2>nul
mkdir %OUT_PATH%ood\css 2>nul

echo [INFO] A2UI构建脚本启动...
echo [INFO] 输出目录: %OUT_ROOT%

:: 复制资源文件
echo [INFO] 复制资源文件...
xcopy %REL_PATH%ood\js\Module\*.* %OUT_PATH%ood\js\Module\ /E /Y >nul
xcopy %REL_PATH%ood\appearance\*.* %OUT_PATH%ood\appearance\ /E /Y >nul
xcopy %REL_PATH%ood\Locale\*.* %OUT_PATH%ood\Locale\ /E /Y >nul
xcopy %REL_PATH%ood\iconfont\*.* %OUT_PATH%ood\iconfont\ /E /Y >nul
xcopy %REL_PATH%ood\css\*.* %OUT_PATH%ood\css\ /E /Y >nul

echo [INFO] 开始构建核心版本...

:: 核心文件列表
set CORE_FILES=
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\_begin.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\ood.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\APICaller.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\MQTT.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\DataBinder.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\Event.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\CSS.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\Dom.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\Template.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\DragDrop.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\Cookies.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\History.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\Tips.js
set CORE_FILES=%CORE_FILES% %REL_PATH%ood\js\_end.js

:: 合并核心文件
type nul > ood.js
for %%f in (%CORE_FILES%) do (
    type %%f >> ood.js
)

:: 压缩核心文件
echo [INFO] 压缩核心文件...
java -jar %COMPRESS_TOOL% -o %OUT_ROOT%ood\js\ood.js ood.js --charset utf-8
java -jar %COMPRESS_TOOL% -o %OUT_ROOT%ood\js\ood-debug.js ood.js --charset utf-8 --nomunge

:: 构建完整版本
echo [INFO] 开始构建完整版本...

:: 完整文件列表 - 简化版，仅包含主要文件
set ALL_FILES=%CORE_FILES%
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\Locale\en.js
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\Date.js
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\Module.js
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\XML.js
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\UI.js
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\UI\Block.js
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\UI\ButtonViews.js
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\UI\Dialog.js
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\UI\Input.js
set ALL_FILES=%ALL_FILES% %REL_PATH%ood\js\_end.js

:: 合并完整文件
type nul > ood-all.js
for %%f in (%ALL_FILES%) do (
    type %%f >> ood-all.js
)

:: 压缩完整文件
echo [INFO] 压缩完整文件...
java -jar %COMPRESS_TOOL% -o %OUT_ROOT%ood\js\ood-all.js ood-all.js --charset utf-8

:: 复制Coder.js
echo [INFO] 复制Coder.js...
copy %REL_PATH%ood\js\Coder.js %OUT_ROOT%ood\js\Coder.js >nul

:: 清理临时文件
del ood.js ood-all.js >nul

:: 输出构建结果
echo [INFO] A2UI构建完成！
echo [INFO] 输出目录: %OUT_ROOT%
echo [INFO] 核心版本: %OUT_ROOT%ood\js\ood.js
echo [INFO] 完整版本: %OUT_ROOT%ood\js\ood-all.js
echo [INFO] 调试版本: %OUT_ROOT%ood\js\ood-debug.js
echo [INFO] 按任意键退出...
pause >nul