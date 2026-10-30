@echo off
rem Needs Java 21 installed. Downloads Gradle 8.8 once, then builds the mod.
cd /d "%~dp0"
if not exist ".gradle-dist\gradle-8.8\bin\gradle.bat" (
  echo Downloading Gradle 8.8 ...
  powershell -NoProfile -Command "New-Item -ItemType Directory -Force .gradle-dist | Out-Null; Invoke-WebRequest https://services.gradle.org/distributions/gradle-8.8-bin.zip -OutFile .gradle-dist\g.zip; Expand-Archive -Force .gradle-dist\g.zip .gradle-dist"
)
call ".gradle-dist\gradle-8.8\bin\gradle.bat" build
echo.
echo Done. The mod is in build\libs\ (file vanilla-sapling-trees-1.21.1-1.0.0.jar)
pause
