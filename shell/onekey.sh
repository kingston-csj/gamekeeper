#!/bin/bash
logName="console.log"
if [[ -f ${logName} ]]; then
  rm -rf ${logName}
fi
touch console.log
nohup java -jar gamekeeper-0.0.1-SNAPSHOT.jar > console.log &
tailf console.log
