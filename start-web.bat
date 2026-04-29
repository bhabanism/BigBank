@echo off
setlocal
rem Ensure PostgreSQL is up on localhost:5432 (no Docker in this script). See application.properties.
set "R=%~dp0"

start "BigBank shell (4200)" /D "%R%bank-web" cmd /k ng serve bank-shell
start "BigBank admin MFE (4201)" /D "%R%bank-web" cmd /k ng serve bank-admin-mfe

echo Launched: bank-shell, bank-admin-mfe in separate windows.
echo Shell http://localhost:4200/
endlocal
