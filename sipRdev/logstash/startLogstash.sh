#!/usr/bin/env bash

DIRECTORY=`pwd`
sudo docker run -it -p 9999:9999 --name=sipRlogstash -v $DIRECTORY:/config-dir logstash logstash -f /config-dir/logstash-syslog.conf