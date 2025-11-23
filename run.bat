@echo off
setlocal enabledelayedexpansion

:: ==========================================
:: Config paths
:: ==========================================
set SRC_DIR=src
set CLASSES_DIR=webapp\WEB-INF\classes
set LIB_DIR=webapp\WEB-INF\lib
set JETTY_LIB_DIR=server\Jetty-11

:: ==========================================
:: Functions
:: ==========================================

:clean_classes
echo [CLEAN] Deleting old classes...
if exist "%CLASSES_DIR%" rmdir /s /q "%CLASSES_DIR%"
mkdir "%CLASSES_DIR%"
goto :eof

:compile_jetty
echo [COMPILE] Compiling for Jetty...
for /r %SRC_DIR% %%f in (*.java) do (
    javac -d "%CLASSES_DIR%" -cp "%LIB_DIR%\*;%JETTY_LIB_DIR%\*" "%%f"
)
goto :eof

:run_jetty
echo [RUN] Starting Jetty Embedded...
java -cp "%CLASSES_DIR%;%LIB_DIR%\*;%JETTY_LIB_DIR%\*" src.Main
goto :eof

:run_jetty_hot
echo [HOT RELOAD] Starting Jetty with automatic recompile...

:: Run PowerShell watcher
powershell -NoExit -Command ^
"function CompileJetty { " ^
"  Write-Host '[RECOMPILE] Compiling...' -ForegroundColor Cyan; " ^
"  Remove-Item -Recurse -Force '%CLASSES_DIR%\*' -ErrorAction SilentlyContinue; " ^
"  gci -Recurse '%SRC_DIR%' -Filter '*.java' | % { javac -d '%CLASSES_DIR%' -cp '%LIB_DIR%\*;%JETTY_LIB_DIR%\*' $_.FullName }; " ^
"}; " ^
"CompileJetty; " ^
"$fs = New-Object IO.FileSystemWatcher '%SRC_DIR%' -Property @{IncludeSubdirectories=$true;Filter='*.java'}; " ^
"$action = { CompileJetty; Stop-Process -Id $pid -Force }; " ^
"Register-ObjectEvent $fs Changed -Action $action; " ^
"Write-Host 'Watching source files for changes. Press Ctrl+C to exit.'; " ^
"while ($true) { Start-Sleep 1 }"

goto :eof

:: ==========================================
:: Menu
:: ==========================================
:menu
cls
echo ==========================================
echo         Java Server Runner
echo ==========================================
echo.
echo 1. Run Jetty Embedded
echo 2. Run Jetty Hot Reload
echo 0. Exit
echo.
set /p choice=Select option [0-2]:

if "%choice%"=="1" (
    call :clean_classes
    call :compile_jetty
    call :run_jetty
    goto :eof
)

if "%choice%"=="2" (
    call :run_jetty_hot
    goto :eof
)

if "%choice%"=="0" (
    echo Exiting...
    exit /b
)

echo Invalid option! Try again.
pause
goto menu
