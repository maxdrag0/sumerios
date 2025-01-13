@echo off
REM Instalar MariaDB desde el instalador .msi
echo Instalando MariaDB...
msiexec /i "%~dp0mariadb-11.6.2-winx64.msi" /quiet

REM Esperar unos segundos para asegurarse de que la instalación se complete
timeout /t 10

REM Copiar el archivo my.ini a la carpeta de MariaDB
echo Copiando archivo de configuración my.ini...
copy "%~dp0my.ini" "C:\Program Files\MariaDB 11.6\data\my.ini"

REM Crear la base de datos y el usuario necesario
echo Creando base de datos y configurando usuario...
"C:\Program Files\MariaDB 11.6\bin\mysql.exe" -u root -e "CREATE DATABASE IF NOT EXISTS sumerios; CREATE USER 'root'@'%' IDENTIFIED BY 'password'; GRANT ALL PRIVILEGES ON sumerios.* TO 'root'@'%'; FLUSH PRIVILEGES;"

REM Configurar MariaDB para aceptar conexiones remotas
echo Configuración completada. Reiniciando el servicio de MariaDB...
net stop MariaDB
net start MariaDB

REM Esperar para asegurarse de que el servicio se reinicie
timeout /t 5

echo Instalación y configuración completadas.
pause
