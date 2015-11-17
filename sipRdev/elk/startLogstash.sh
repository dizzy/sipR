#!/usr/bin/env bash

DIRECTORY=`pwd`
sudo docker run -d -it -p 9999:9999 --name=sipRlogstash --link sipRelastic -v ./logstash-conf:/config-dir logstash logstash -f /config-dir/log
stash-syslog.conf