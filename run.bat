@echo off
setlocal
cd /d "%~dp0"
if not exist "build" mkdir "build"
javac --release 17 -encoding UTF-8 -d "build" -sourcepath "src" "src\restaurant\Main.java"
if errorlevel 1 exit /b 1
java -cp "build" restaurant.Main %*
exit /b %errorlevel%
