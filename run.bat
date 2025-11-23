@echo off
setlocal enabledelayedexpansion

:: ============================================================
:: CONFIGURATION
:: ============================================================
set "SRC_DIR=src"
set "WEBAPP_DIR=webapp"
set "CLASSES_DIR=%WEBAPP_DIR%\WEB-INF\classes"
set "LIB_DIR=%WEBAPP_DIR%\WEB-INF\lib"

:: Server Paths (Relative)
set "JETTY_HOME=server\Jetty-11"
set "TOMCAT_HOME=server\Tomcat-11"

:: Classpaths (Quotes handled carefully)
set "CP_JETTY=%LIB_DIR%\*;%JETTY_HOME%\*"
set "CP_TOMCAT=%LIB_DIR%\*;%TOMCAT_HOME%\lib\*"
set "CP_RUN=%CLASSES_DIR%;%LIB_DIR%\*;%JETTY_HOME%\*"

:: URLs (Auto open browser)
set "URL_JETTY=http://localhost:8080"
set "URL_TOMCAT=http://localhost:8080/webapp"

:: ============================================================
:: MAIN MENU
:: ============================================================
:menu
cls
title SERVER_MANAGER_CONTROLLER
echo =================================================
echo    JAVA WEB SERVER MANAGER (Auto-Open Browser)
echo =================================================
echo 1. Run Jetty (Single Run)
echo 2. Run Jetty (Hot Reload - Watch Mode)
echo 3. Deploy to Tomcat (Build WAR ^& Auto Cleanup)
echo 4. Exit
echo =================================================
set /p choice="Select option [1-4]: "

if "%choice%"=="1" goto mode_jetty
if "%choice%"=="2" goto mode_hot
if "%choice%"=="3" goto mode_tomcat
if "%choice%"=="4" exit /b 0

goto menu

:: ============================================================
:: MODE: JETTY SINGLE RUN
:: ============================================================
:mode_jetty
call :clean
call :kill_port_8080
call :compile_jetty
if !errorlevel! equ 0 (
    echo [RUN] Starting Jetty...
    echo [INFO] Browser will open in 3 seconds...

    :: Open browser after 3s (run Asynchronous by start cmd)
    start "" cmd /c "timeout /t 3 >nul & start %URL_JETTY%"

    java -cp "%CP_RUN%" src.Main
)
pause
goto menu

:: ============================================================
:: MODE: JETTY HOT RELOAD (CORE LOGIC)
:: ============================================================
:mode_hot
call :clean
:: 1. Clean port before all
call :kill_port_8080

:: flag mark to just open browser at first time
set "FIRST_RUN=1"

:hot_loop
    cls
    title JETTY_WATCHER_ACTIVE
    echo =================================================
    echo [HOT RELOAD] Status: ACTIVE
    echo =================================================

    :: 2. Compile
    call :compile_jetty
    if !errorlevel! neq 0 (
        echo [ERROR] Compilation failed. Waiting for code fix...
        goto wait_for_change
    )

    :: 3. Start Server (DETACHED PROCESS)
    echo [START] Spawning Jetty Server in new window...
    start "JETTY_WORKER" /D "." cmd /c "java -cp "%CP_RUN%" src.Main"

    :: 3b. Open browser
    if "!FIRST_RUN!"=="1" (
        echo [INFO] First run detected. Opening browser in 3s...
        start "" cmd /c "timeout /t 3 >nul & start %URL_JETTY%"
        set "FIRST_RUN=0"
    )

    :wait_for_change
    echo.
    echo [WATCH] Watching '%SRC_DIR%' for changes (Ctrl+C to Stop)...

    :: 4. File System Watcher (PowerShell Hybrid)
    powershell -NoProfile -Command ^
        "$w = New-Object System.IO.FileSystemWatcher; $w.Path = '%SRC_DIR%'; $w.Filter = '*.java'; $w.IncludeSubdirectories = $true; $w.EnableRaisingEvents = $true; $w.WaitForChanged([System.IO.WatcherChangeTypes]::All)" >nul 2>&1

    echo [DETECT] Change detected! Reloading...

    :: 5. SAFE KILL (just kill Process at port 8080)
    call :kill_port_8080

    :: Loop lại
    goto hot_loop

:: ============================================================
:: MODE: TOMCAT DEPLOY (FIXED CLEANUP LOGIC)
:: ============================================================
:mode_tomcat
call :clean
call :compile_tomcat
if !errorlevel! neq 0 (
    echo [ERROR] Build failed.
    pause
    goto menu
)

:: SET ABSOLUTE PATH FOR CATALINA_HOME
if not exist "%TOMCAT_HOME%" (
    echo [ERROR] Tomcat directory not found at: %TOMCAT_HOME%
    pause
    goto menu
)

pushd "%TOMCAT_HOME%"
set "CATALINA_HOME=%CD%"
popd
:: ------------------------------------------------

echo [DEPLOY] Deploying to Tomcat at: %CATALINA_HOME%
set "TC_WEBAPPS=%CATALINA_HOME%\webapps"
set "WAR_NAME=webapp"

if exist "%TC_WEBAPPS%\%WAR_NAME%" rmdir /s /q "%TC_WEBAPPS%\%WAR_NAME%"
if exist "%TC_WEBAPPS%\%WAR_NAME%.war" del "%TC_WEBAPPS%\%WAR_NAME%.war"

echo [BUILD] Creating WAR...
pushd "%WEBAPP_DIR%"
jar -cf "%TC_WEBAPPS%\%WAR_NAME%.war" *
popd

echo [RUN] Starting Tomcat in a NEW WINDOW...
echo [INFO] Browser will open in 5 seconds...

:: Open browser
start "" cmd /c "timeout /t 5 >nul & start %URL_TOMCAT%"

:: Catch stop tomcat event
echo [INFO] Close the Tomcat window (or Ctrl+C inside it) to STOP and CLEANUP.
start "Tomcat_Server_Console" /WAIT cmd /c ""%CATALINA_HOME%\bin\catalina.bat" run"

:: Run command when tomcat terminal stop
call :cleanup_tomcat_artifacts

pause
goto menu

:: ============================================================
:: UTILITY FUNCTIONS
:: ============================================================

:clean
if exist "%CLASSES_DIR%" rmdir /s /q "%CLASSES_DIR%"
mkdir "%CLASSES_DIR%"
exit /b 0

:kill_port_8080
echo [STOP] Checking port 8080...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":8080" ^| findstr "LISTENING"') do (
    if "%%a" NEQ "0" (
        echo [KILL] Killing PID %%a...
        taskkill /F /PID %%a >nul 2>&1
    )
)
timeout /t 1 /nobreak >nul
exit /b 0

:compile_jetty
echo [COMPILE] Compiling Jetty sources...
dir /s /b "%SRC_DIR%\*.java" > sources.txt
javac -d "%CLASSES_DIR%" -cp "%CP_JETTY%" @sources.txt
set ERR=!errorlevel!
del sources.txt
exit /b !ERR!

:compile_tomcat
echo [COMPILE] Compiling Tomcat sources...
dir /s /b "%SRC_DIR%\*.java" | findstr /v "Main.java" > sources.txt
javac -d "%CLASSES_DIR%" -cp "%CP_TOMCAT%" @sources.txt
set ERR=!errorlevel!
del sources.txt
exit /b !ERR!

:cleanup_tomcat_artifacts
echo.
echo ------------------------------------------------
echo [CLEANUP] Cleaning up file WAR and folder...
if exist "%TC_WEBAPPS%\%WAR_NAME%" (
    rmdir /s /q "%TC_WEBAPPS%\%WAR_NAME%"
    echo [DEL] Folder %WAR_NAME% removed.
)
if exist "%TC_WEBAPPS%\%WAR_NAME%.war" (
    del "%TC_WEBAPPS%\%WAR_NAME%.war"
    echo [DEL] File %WAR_NAME%.war removed.
)
echo [CLEANUP] Finished.
echo ------------------------------------------------
exit /b 0
