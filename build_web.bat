@echo off
cd %~dp0
call gradlew web:build
pause
rmdir /s /q docs
echo D | xcopy /E web\build\dist\js\productionExecutable docs
pause
