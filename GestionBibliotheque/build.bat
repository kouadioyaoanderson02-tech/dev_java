@echo off
setlocal enabledelayedexpansion
chcp 65001 > nul
echo ============================================================
echo   COMPILATION DU PROJET GESTION DE BIBLIOTHÈQUE (JAVA 25)
echo ============================================================

if not exist bin (
    mkdir bin
)

echo Compilation des classes Java...
powershell -Command "javac -encoding UTF-8 -d bin -cp 'lib/mysql-connector-j-9.2.0.jar' (Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName)"

if %ERRORLEVEL% EQU 0 (
    echo [SUCCÈS] Compilation réussie sans aucune erreur !
) else (
    echo [ERREUR] Échec de la compilation.
)
pause
