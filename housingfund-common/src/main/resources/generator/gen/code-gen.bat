::If Not Exist %executable% (
::  mvn clean package
::)

REM set JAVA_OPTS=%JAVA_OPTS% -Xmx1024M -DloggerPath=conf/log4j.properties
set ags=generate -i %1 -l io.swagger.custom.JavaGen -o %USERPROFILE%\housingfund-gen-%date:~0,4%-%date:~5,2%-%date:~8,2%--%time:~0,2%-%time:~3,2%-%time:~6,2% -t %~p0\..\template

java %JAVA_OPTS% -jar %~p0swagger-codegen-cli.jar %ags%