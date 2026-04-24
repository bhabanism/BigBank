@echo off
setlocal
rem Ensure PostgreSQL is up on localhost:5432 (no Docker in this script). See application.properties.
set "R=%~dp0"

start "BigBank API (8081)" /D "%R%bank-service" cmd /k mvnw.cmd spring-boot:run
start "BigBank shell (4200)" /D "%R%bank-web" cmd /k npm run run:all
REM start "BigBank shell (4200)" /D "%R%bank-web" cmd /k npm run start:shell
REM start "BigBank admin MFE (4201)" /D "%R%bank-web" cmd /k npm run start:admin-mfe

echo Launched: API, bank-shell, bank-admin-mfe in separate windows.
echo Shell http://localhost:4200/   API http://localhost:8081/
endlocal
