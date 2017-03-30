@ECHO OFF

rem -- Set up
SETLOCAL

SET CHANGELOGPATH=database\liquibase
SET CHANGELOG=.\%CHANGELOGPATH%\changelog.xml
SET CHANGELOGTEMPLATE=.\%CHANGELOGPATH%\changelog.template.xml
SET CHANGELOGTMP=.\%CHANGELOGPATH%\changelog.tmp.xml

SET MAVENCHANGELOGPROPERTY=mvn.liquibase.changelog
SET MAVENPROFILE=dev-JAR

SET SCRIPTPATH=database\scripts
SET TYPESFILTERSCRIPT=.\%SCRIPTPATH%\sql.types.filter.py
SET TYPESFILTERJSON=.\%SCRIPTPATH%\filter.json
SET PKFIXSCRIPT=.\%SCRIPTPATH%\primary.keys.filter.py
SET APPENDSCRIPT=.\%SCRIPTPATH%\changelog.append.py

rem -- Go to project root
cd ..\..

rem -- Make temporary file for model-originated changes
copy %CHANGELOGTEMPLATE% %CHANGELOGTMP%

rem -- Generate and insert model-originated changes into temporary changelog
call mvn sql:execute liquibase:diff -D%MAVENCHANGELOGPROPERTY%=%CHANGELOGTMP% -P%MAVENPROFILE%

rem -- Filter changelog: replace DBMS-bound SQL types with generic (DBMS-unbound) ones
python %TYPESFILTERSCRIPT% %CHANGELOGTMP% %TYPESFILTERJSON%

rem -- Filter changelog: replace fixed PK names ("PRIMARY") with unique ones (PK_[table_name]); needed when diff is generated from MySQL databases
python %PKFIXSCRIPT% %CHANGELOGTMP%

rem -- Append filtered temporary changelog
python %APPENDSCRIPT% %CHANGELOGTMP% %CHANGELOG%

rem -- Delete temporary file
del %CHANGELOGTMP%
