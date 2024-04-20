@echo off
cd %~dp0
call gradlew compose:wasmJsBrowserDistribution
pause
rmdir /s /q web\src\jsMain\resources\race
echo D | xcopy /E compose\build\dist\wasmJs\productionExecutable web\src\jsMain\resources\race
rmdir /s /q docs\race
echo D | xcopy /E compose\build\dist\wasmJs\productionExecutable docs\race
pause
