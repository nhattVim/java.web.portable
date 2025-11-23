#!/bin/bash

set -e

MODE=$1 # jetty | tomcat | hot

SRC_DIR="src"
CLASSES_DIR="webapp/WEB-INF/classes"
LIB_DIR="webapp/WEB-INF/lib"

# Jetty Paths
JETTY_LIB_DIR="server/Jetty-11"

# Tomcat Paths
TOMCAT_HOME="server/Tomcat-11"
TOMCAT_WEBAPPS="$TOMCAT_HOME/webapps"
TOMCAT_LIB="$TOMCAT_HOME/lib"
TOMCAT_BIN="$TOMCAT_HOME/bin"
WAR_NAME="webapp"

# URLs
URL_JETTY="http://localhost:8080"
URL_TOMCAT="http://localhost:8080/$WAR_NAME"

# -----------------------------------
# 0. Check Dependencies
# -----------------------------------
command -v javac >/dev/null 2>&1 || {
    echo "javac is required but it's not installed. Aborting."
    exit 1
}

# -----------------------------------
# 1. Open browser
# -----------------------------------
open_browser_when_ready() {
    local url=$1
    local port=8080

    (
        echo "[INFO] Waiting for port $port to open..."

        # loop to check port (timeout 30s)
        local count=0
        while ! (echo >/dev/tcp/localhost/$port) >/dev/null 2>&1; do
            sleep 0.5
            count=$((count + 1))
            if [ $count -ge 60 ]; then # 60 * 0.5s = 30s
                echo "[WARN] Server took too long to start. Browser not opened."
                exit 1
            fi
        done

        echo "[INFO] Server is UP! Opening browser at $url..."

        if [[ "$OSTYPE" == "darwin"* ]]; then
            open "$url" # MacOS
        elif command -v xdg-open >/dev/null; then
            xdg-open "$url" # Linux
        elif command -v explorer.exe >/dev/null; then
            explorer.exe "$url" # WSL / Git Bash
        else
            echo "[WARN] No browser opener found."
        fi
    ) &
}

# -----------------------------------
# 2. Clean classes
# -----------------------------------
clean_classes() {
    echo "[CLEAN] Deleting old classes..."
    rm -rf "$CLASSES_DIR"/*
}

# -----------------------------------
# 3. Compile Functions
# -----------------------------------
compile_jetty() {
    echo "[COMPILE] Compiling for Jetty..."
    javac -d "$CLASSES_DIR" \
        -cp "$LIB_DIR/*:$JETTY_LIB_DIR/*" \
        $(find "$SRC_DIR" -name "*.java")
}

compile_tomcat() {
    echo "[COMPILE] Compiling for Tomcat..."
    javac -d "$CLASSES_DIR" \
        -cp "$LIB_DIR/*:$TOMCAT_LIB/*" \
        $(find "$SRC_DIR" -name "*.java" ! -name "Main.java")
}

# -----------------------------------
# 4. Run Functions
# -----------------------------------
run_jetty() {
    echo "[RUN] Starting Jetty Embedded..."

    open_browser_when_ready "$URL_JETTY"

    java -cp "$CLASSES_DIR:$LIB_DIR/*:$JETTY_LIB_DIR/*" src.Main
}

run_jetty_hot() {
    echo "[HOT RELOAD] Waiting for changes..."
    command -v entr >/dev/null 2>&1 || {
        echo "entr is required but it's not installed. Aborting."
        exit 1
    }

    open_browser_when_ready "$URL_JETTY"

    find src -name "*.java" | entr -r \
        /bin/bash -c "rm -rf $CLASSES_DIR/* \
        && echo '[RELOAD] Re-compiling...' \
        && javac -d $CLASSES_DIR -cp '$LIB_DIR/*:$JETTY_LIB_DIR/*' \$(find src -name '*.java') \
        && echo '[RELOAD] Restarting Server...' \
        && java -cp '$CLASSES_DIR:$LIB_DIR/*:$JETTY_LIB_DIR/*' src.Main"
}

run_tomcat() {
    echo "[DEPLOY] Deploying Tomcat..."

    rm -rf "$TOMCAT_WEBAPPS/$WAR_NAME" "$TOMCAT_WEBAPPS/$WAR_NAME.war"

    echo "[BUILD] Creating WAR file..."
    jar -cf "$TOMCAT_WEBAPPS/$WAR_NAME.war" -C webapp .
    echo "[INFO] Đã deploy $WAR_NAME.war vào $TOMCAT_WEBAPPS"

    cleanup_after_run() {
        echo ""
        echo "------------------------------------------------"
        echo "[CLEANUP] Cleaning up file WAR and folder..."
        rm -rf "$TOMCAT_WEBAPPS/$WAR_NAME" "$TOMCAT_WEBAPPS/$WAR_NAME.war"
        echo "[CLEANUP] Deleted WAR_NAME.war and WAR_NAME folder."
        echo "------------------------------------------------"
    }

    # - Cleanup after run ---------------
    trap cleanup_after_run EXIT
    # -----------------------------------

    # Run Tomcat
    chmod +x "$TOMCAT_BIN/catalina.sh"
    echo "[RUN] Tomcat is starting... Press Ctrl+C to stop."

    open_browser_when_ready "$URL_TOMCAT"

    "$TOMCAT_BIN/catalina.sh" run
}

# -----------------------------------
# MAIN SWITCH
# -----------------------------------
case "$MODE" in

jetty)
    clean_classes
    compile_jetty
    run_jetty
    ;;

hot)
    clean_classes
    run_jetty_hot
    ;;

tomcat)
    clean_classes
    compile_tomcat
    run_tomcat
    ;;

*)
    echo "-----------------------------------------------"
    echo "Usage:"
    echo "  ./run.sh jetty    -> Run Embedded Jetty (src.Main)"
    echo "  ./run.sh hot      -> Run Jetty with Hot Reload (cần entr)"
    echo "  ./run.sh tomcat   -> Build WAR and deploy to Tomcat Server"
    echo "-----------------------------------------------"
    exit 1
    ;;

esac
