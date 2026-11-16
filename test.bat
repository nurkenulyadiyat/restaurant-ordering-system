@echo off
setlocal
cd /d "%~dp0"
if not exist "build" mkdir "build"
javac --release 17 -encoding UTF-8 -d "build" -sourcepath "src;tests" "tests\restaurant\ProjectTests.java"
if errorlevel 1 exit /b 1
java -cp "build" restaurant.ProjectTests
exit /b %errorlevel%
