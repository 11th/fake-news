#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/fake-news/vscale \
  target/fakenews-0.0.1-SNAPSHOT.jar \
  root@77.223.97.110:/home/eleventh/fakenews

echo 'Restart server...'

ssh -i ~/.ssh/fake-news/vscale root@77.223.97.110 << EOF

pgrep java | xargs kill -9
nohup java -jar fakenews-0.0.1-SNAPSHOT.jar > log.txt &

EOF

echo 'Goodbye...'
