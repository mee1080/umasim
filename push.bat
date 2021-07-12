@echo off
cd %~dp0
git add -A
git commit -m "commit %DATE% %TIME%"
git status
pause
git push
pause
