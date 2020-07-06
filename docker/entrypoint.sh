#!/bin/sh

echo 'Do something'

#kill_jar() {
#  echo 'Received TERM'
#  kill "$(ps -ef | grep java | grep app | awk '{print $1}')"
#  wait $!
#  echo 'Process finished'
#}
#
#trap 'kill_jar' TERM INT
#
java -jar app.jar &
#
wait $!
#wait

#exec java -jar app.jar
