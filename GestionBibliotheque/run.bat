@echo off
chcp 65001 > nul
echo ============================================================
echo   COMPILATION ET LANCEMENT DE LA GESTION DE BIBLIOTHÈQUE
echo ============================================================

cd /d "%~dp0"

if not exist bin mkdir bin

echo Compilation des sources (Compatibilité Java 21+)...
javac --release 21 -encoding UTF-8 -d bin -cp "lib/mysql-connector-j-9.2.0.jar;src" src/utils/*.java src/database/*.java src/model/*.java src/repository/*.java src/service/*.java src/main/*.java

if %ERRORLEVEL% EQU 0 (
    echo [OK] Compilation réussie ! Lancement de l'application...
    echo.
    java -cp "bin;lib/mysql-connector-j-9.2.0.jar" main.Main
) else (
    echo [ERREUR] Échec de la compilation. Vérifiez votre installation Java.
)

pause
