#!/usr/bin/env bash

sudo docker run --name sipRkibana --link sipRelastic:elasticsearch -p 5601:5601 -d kibana:4.1