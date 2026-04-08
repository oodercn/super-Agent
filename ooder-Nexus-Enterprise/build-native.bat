@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   ApexOS v1.0.0 GraalVM Native Image
echo ========================================
echo.

echo [1/5] Checking environment...

where native-image >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] native-image not found
    echo Please install GraalVM and native-image
    pause
    exit /b 1
)

echo [OK] Native Image installed
echo.

echo [2/5] Setting up Visual Studio environment...

set "VCVARSALL="

if exist "E:\vs\VC\Auxiliary\Build\vcvarsall.bat" (
    set "VCVARSALL=E:\vs\VC\Auxiliary\Build\vcvarsall.bat"
)
if exist "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvarsall.bat" (
    set "VCVARSALL=C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvarsall.bat"
)
if exist "C:\Program Files\Microsoft Visual Studio\2022\Professional\VC\Auxiliary\Build\vcvarsall.bat" (
    set "VCVARSALL=C:\Program Files\Microsoft Visual Studio\2022\Professional\VC\Auxiliary\Build\vcvarsall.bat"
)
if exist "C:\Program Files\Microsoft Visual Studio\2022\Enterprise\VC\Auxiliary\Build\vcvarsall.bat" (
    set "VCVARSALL=C:\Program Files\Microsoft Visual Studio\2022\Enterprise\VC\Auxiliary\Build\vcvarsall.bat"
)
if exist "C:\Program Files\Microsoft Visual Studio\2022\BuildTools\VC\Auxiliary\Build\vcvarsall.bat" (
    set "VCVARSALL=C:\Program Files\Microsoft Visual Studio\2022\BuildTools\VC\Auxiliary\Build\vcvarsall.bat"
)

if "%VCVARSALL%"=="" (
    echo [ERROR] vcvarsall.bat not found
    echo Please install Visual Studio 2022 Build Tools
    pause
    exit /b 1
)

echo [OK] Found: %VCVARSALL%
call "%VCVARSALL%" x64
echo.

echo [3/5] Setting JAVA_HOME...

if exist "D:\graalvm\graalvm-jdk-21.0.2+13.1\bin\java.exe" (
    set "JAVA_HOME=D:\graalvm\graalvm-jdk-21.0.2+13.1"
    echo [OK] JAVA_HOME set to: %JAVA_HOME%
) else (
    echo [WARNING] JAVA_HOME not auto-detected
    echo Please set JAVA_HOME manually if build fails
)
echo.

echo [4/5] Cleaning...
call mvn clean -q
echo [OK] Done
echo.

echo [5/5] Building Native Image (this may take several minutes)...
echo.

call mvn -Pnative -DskipTests package

if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Build failed
    pause
    exit /b 1
)

echo.
echo Verifying...
if exist "target\apexos-native.exe" (
    echo [OK] Build successful!
    echo.
    echo ========================================
    echo   Build Complete
    echo ========================================
    echo.
    echo Output: target\apexos-native.exe
    echo.
    echo Run: target\apexos-native.exe
    echo.
) else (
    echo [ERROR] Output not found
    pause
    exit /b 1
)

pause
