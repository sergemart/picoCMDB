@ECHO OFF

rem -- Set up
SETLOCAL

SET CHANGELOGPATH=database\liquibase
SET CHANGELOG=.\%CHANGELOGPATH%\changelog.xml
SET CHANGELOGTEMPLATE=.\%CHANGELOGPATH%\changelog.template.xml
SET CHANGELOGTMP=.\%CHANGELOGPATH%\changelog.tmp.xml

SET MAVENCHANGELOGPROPERTY=mvn.liquibase.changelog
SET MAVENPROFILE=dev-JAR
SET MAVENARGS=-D%MAVENCHANGELOGPROPERTY%=%CHANGELOGTMP% -P%MAVENPROFILE%

SET SCRIPTPATH=database\scripts
SET FILTERSCRIPT=.\%SCRIPTPATH%\sql.types.filter.py
SET FILTERJSON=.\%SCRIPTPATH%\filter.json
SET FILTERARGS=%CHANGELOGTMP% %FILTERJSON%
SET APPENDSCRIPT=.\%SCRIPTPATH%\changelog.append.py

rem -- Go to project root
cd ..\..

rem -- Make temporary file for model-originated changes
copy %CHANGELOGTEMPLATE% %CHANGELOGTMP%

rem -- Insert model-originated changes into temporary changelog
call mvn sql:execute liquibase:diff %MAVENARGS%

rem -- Filter changelog: replace DBMS-bound SQL types with generic (DBMS-unbound) ones
python %FILTERSCRIPT% %FILTERARGS%

rem -- Append filtered temporary changelog
python %APPENDSCRIPT% %CHANGELOGTMP% %CHANGELOG%

rem -- Clean up
del %CHANGELOGTMP%
