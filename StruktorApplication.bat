@echo off
if not exist setword.com goto error1
if not exist struktor\nul if not exist classes.jar goto error3
java | setword 1 > tmp.bat
call tmp.bat existjava
if not "%existjava%"=="Usage:" goto error2
:weiter
set CLASSPATH=%CLASSPATH%;.
echo loading ...
if exist classes.jar javaw -cp "classes.jar" struktor.StruktorApplication
if not exist classes.jar javaw struktor.StruktorApplication %1 %2
goto end
:error1
echo das kleine Hilfsprogramm "setword" wurde nicht gefunden !
echo aber vielleicht geht es ja trotzdem ...
goto weiter
:error2
echo Es wurde keine Java-Umgebung gefunden !
echo entweder ist sie nicht installiert oder der PATH wurde nicht
echo richtig gesetzt !
goto end
:error3
echo Struktor wurde nicht gefunden !
goto end
:end
del tmp.bat > nul
echo dieses Fenster kann nun geschlossen werden !