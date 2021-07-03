@echo off
cd %~dp0
call gradlew web:build
pause
rmdir /s /q docs
echo D | xcopy web\build\distributions docs
pause
