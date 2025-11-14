# Script para instalar la app Android con JAVA_HOME configurado
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"

Write-Host "Verificando dispositivos conectados..." -ForegroundColor Cyan
$devices = & "C:\Users\Delfos\AppData\Local\Android\Sdk\platform-tools\adb.exe" devices

if ($devices -match "device$") {
    Write-Host "Dispositivo encontrado. Instalando app..." -ForegroundColor Green
    .\gradlew.bat installDebug
} else {
    Write-Host "No hay dispositivos conectados." -ForegroundColor Yellow
    Write-Host "Por favor:" -ForegroundColor Yellow
    Write-Host "  1. Conecta un dispositivo Android por USB y habilita depuración USB" -ForegroundColor Yellow
    Write-Host "  2. O inicia un emulador desde Android Studio" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Luego ejecuta este script nuevamente." -ForegroundColor Yellow
}

