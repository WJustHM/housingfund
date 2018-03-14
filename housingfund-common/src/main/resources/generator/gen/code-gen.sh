#!/bin/sh

#SCRIPT="$0"
WORKDIR=$(dirname $(dirname $_))
TEMPLATE="$WORKDIR/template"
JSON_FILE=$1
OUTPUT="$(echo ~)/housingfund-gen-$(date +%Y-%m-%d--%k-%M-%S)"
echo $OUTPUT
APPNAME="housingfund"
echo $WORKDIR
echo $JSON_FILE
while [ -h "$SCRIPT" ] ; do
  ls=`ls -ld "$SCRIPT"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    SCRIPT="$link"
  else
    SCRIPT=`dirname "$SCRIPT"`/"$link"
  fi
done
echo $SCRIPT
if [ ! -d "${APP_DIR}" ]; then
  APP_DIR=`dirname "$SCRIPT"`/..
  APP_DIR=`cd "${APP_DIR}"; pwd`
fi

executable="$WORKDIR/gen/swagger-codegen-cli.jar"

#if [ ! -f "$executable" ]
#then
#  mvn clean package
#fi

# if you've executed sbt assembly previously it will use that instead.
export JAVA_OPTS="${JAVA_OPTS} -XX:MaxPermSize=256M -Xmx1024M -DloggerPath=conf/log4j.properties"

ags="generate -t ${TEMPLATE} -i ${JSON_FILE} -l io.swagger.custom.JavaGen -o ${OUTPUT} -D appName=${APPNAME}"

java $JAVA_OPTS -jar $executable $ags
