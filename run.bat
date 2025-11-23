@echo off
setlocal enabledelayedexpansion

:: ============================================================
:: INTERNAL CALL HANDLER (Sub Process to check port)
:: ============================================================
if "%1"==":wait_and_open" goto :wait_and_open_impl

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

:: Classpaths
set "CP_JETTY=%LIB_DIR%\*;%JETTY_HOME%\*"
set "CP_TOMCAT=%LIB_DIR%\*;%TOMCAT_HOME%\lib\*"
set "CP_RUN=%CLASSES_DIR%;%LIB_DIR%\*;%JETTY_HOME%\*"

:: URLs
set "URL_JETTY=http://localhost:8080"
set "URL_TOMCAT=http://localhost:8080/webapp"

:: ============================================================
:: MAIN MENU (GUI MODE)
:: ============================================================
:menu
cls
:: Call GUI by PowerShell, return via ErrorLevel
call :show_gui_popup
set "SELECTION=%ERRORLEVEL%"

:: Input handle
if "%SELECTION%"=="1" goto mode_jetty
if "%SELECTION%"=="2" goto mode_hot
if "%SELECTION%"=="3" goto mode_tomcat
if "%SELECTION%"=="0" exit /b 0

:: Exit
exit /b 0

:: ============================================================
:: MODE: JETTY SINGLE RUN
:: ============================================================
:mode_jetty
call :clean
call :kill_port_8080
call :compile_jetty
if !errorlevel! equ 0 (
    echo [RUN] Starting Jetty...

    :: Call sub process to check port and open browser
    start /min "Browser_Waiter" cmd /c "call "%~f0" :wait_and_open %URL_JETTY%"

    java -cp "%CP_RUN%" src.Main
)
pause
goto menu

:: ============================================================
:: MODE: JETTY HOT RELOAD (CORE LOGIC)
:: ============================================================
:mode_hot
call :clean
call :kill_port_8080

set "FIRST_RUN=1"

:hot_loop
    cls
    title JETTY_WATCHER_ACTIVE
    echo =================================================
    echo [HOT RELOAD] Status: ACTIVE
    echo =================================================

    call :compile_jetty
    if !errorlevel! neq 0 (
        echo [ERROR] Compilation failed. Waiting for code fix...
        goto wait_for_change
    )

    echo [START] Spawning Jetty Server in new window...
    :: Start server in new terminal
    start "JETTY_WORKER" /D "." cmd /c "java -cp "%CP_RUN%" src.Main"

    :: Only check port and open browser on first run
    if "!FIRST_RUN!"=="1" (
        echo [INFO] Waiting for server port 8080...
        start /min "Browser_Waiter" cmd /c "call "%~f0" :wait_and_open %URL_JETTY%"
        set "FIRST_RUN=0"
    )

    :wait_for_change
    echo.
    echo [WATCH] Watching '%SRC_DIR%' for changes...
    echo [GUIDE] Press 'Q' to STOP and return to Menu.
    echo [GUIDE] Modify any .java file to RELOAD.

    :: PowerShell Script: Loop every 500ms to check file or key Q
    powershell -NoProfile -Command ^
        "$w = New-Object System.IO.FileSystemWatcher; $w.Path = '%SRC_DIR%'; $w.Filter = '*.java'; $w.IncludeSubdirectories = $true; $w.EnableRaisingEvents = $true; " ^
        "while($true) { " ^
        "   $r = $w.WaitForChanged([System.IO.WatcherChangeTypes]::All, 500); " ^
        "   if ($r.TimedOut -eq $false) { exit 100 } " ^
        "   if ([Console]::KeyAvailable) { " ^
        "       $k = [Console]::ReadKey($true); " ^
        "       if ($k.Key -eq 'Q') { exit 200 } " ^
        "   } " ^
        "}"

    :: Check exit code from PowerShell
    set "EXIT_CODE=%ERRORLEVEL%"

    :: If code 100 -> File changed -> Reload
    if "%EXIT_CODE%"=="100" (
        echo [DETECT] Change detected! Reloading...
        call :kill_port_8080
        goto hot_loop
    )

    :: If code 200 -> Press Q -> Exit to main menu
    if "%EXIT_CODE%"=="200" (
        echo [STOP] User requested stop.
        call :kill_port_8080
        goto menu
    )

    :: Retry if other exit code
    goto hot_loop

:: ============================================================
:: MODE: TOMCAT DEPLOY
:: ============================================================
:mode_tomcat
call :clean
call :compile_tomcat
if !errorlevel! neq 0 (
    echo [ERROR] Build failed.
    pause
    goto menu
)

if not exist "%TOMCAT_HOME%" (
    echo [ERROR] Tomcat directory not found at: %TOMCAT_HOME%
    pause
    goto menu
)

pushd "%TOMCAT_HOME%"
set "CATALINA_HOME=%CD%"
popd

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
echo [INFO] Waiting for Tomcat to listen on port 8080...

:: Check port for Tomcat
start /min "Browser_Waiter" cmd /c "call "%~f0" :wait_and_open %URL_TOMCAT%"

echo [INFO] Close the Tomcat window (or Ctrl+C inside it) to STOP and CLEANUP.
start "Tomcat_Server_Console" /WAIT cmd /c ""%CATALINA_HOME%\bin\catalina.bat" run"

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

:: ============================================================
:: POWERSHELL GUI POPUP FUNCTION (LARGE SIZE)
:: ============================================================
:show_gui_popup
set "PS_CMD=Add-Type -AssemblyName System.Windows.Forms; Add-Type -AssemblyName System.Drawing;"
set "PS_CMD=%PS_CMD% $f = New-Object System.Windows.Forms.Form; "
set "PS_CMD=%PS_CMD% $f.Text = 'Server Manager'; $f.StartPosition = 'CenterScreen'; $f.Width = 450; $f.Height = 320; $f.FormBorderStyle = 'FixedDialog'; $f.MaximizeBox = $false; "

:: Tạo Font chữ to (Size 14, Bold)
set "PS_CMD=%PS_CMD% $font = New-Object System.Drawing.Font('Segoe UI', 14, [System.Drawing.FontStyle]::Bold); "

:: Button 1: Jetty Single
set "PS_CMD=%PS_CMD% $b1 = New-Object System.Windows.Forms.Button; $b1.Text = 'Run Jetty (Single)'; $b1.Font = $font; "
set "PS_CMD=%PS_CMD% $b1.Top = 25; $b1.Left = 50; $b1.Width = 330; $b1.Height = 60; "
set "PS_CMD=%PS_CMD% $b1.Add_Click({ $host.SetShouldExit(1); $f.Close() }); $f.Controls.Add($b1); "

:: Button 2: Hot Reload
set "PS_CMD=%PS_CMD% $b2 = New-Object System.Windows.Forms.Button; $b2.Text = 'Run Jetty (Hot Reload)'; $b2.Font = $font; "
set "PS_CMD=%PS_CMD% $b2.Top = 100; $b2.Left = 50; $b2.Width = 330; $b2.Height = 60; "
set "PS_CMD=%PS_CMD% $b2.Add_Click({ $host.SetShouldExit(2); $f.Close() }); $f.Controls.Add($b2); "

:: Button 3: Tomcat
set "PS_CMD=%PS_CMD% $b3 = New-Object System.Windows.Forms.Button; $b3.Text = 'Deploy to Tomcat'; $b3.Font = $font; "
set "PS_CMD=%PS_CMD% $b3.Top = 175; $b3.Left = 50; $b3.Width = 330; $b3.Height = 60; "
set "PS_CMD=%PS_CMD% $b3.Add_Click({ $host.SetShouldExit(3); $f.Close() }); $f.Controls.Add($b3); "

:: Show Form
set "PS_CMD=%PS_CMD% $f.ShowDialog() | Out-Null; "

powershell -NoProfile -Command "%PS_CMD%"
exit /b %ERRORLEVEL%

:: ============================================================
:: IMPLEMENTATION: WAIT AND OPEN BROWSER
:: ============================================================
:wait_and_open_impl
set "TARGET_URL=%2"
echo Waiting for port 8080 to become active...
:: Loop 30 times, 1s/time (Timeout 30s)
for /L %%i in (1,1,30) do (
    netstat -an | find ":8080" | find "LISTENING" >nul
    if !errorlevel! equ 0 (
        echo Server UP! Opening browser...
        start "" "%TARGET_URL%"
        exit
    )
    timeout /t 1 >nul
)
echo Server start timeout (30s).
exit
