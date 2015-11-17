#!/usr/bin/env bash

sudo docker run --name=sipRelastic -d -p 9200:9200 -p 9300:9300 elasticsearch:1.7.3