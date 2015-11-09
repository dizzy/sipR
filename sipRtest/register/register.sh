#!/bin/bash

SUT=$1
DOMAIN=$2

CALLRATE=500
DURATION=3600
NUMCALLS=$[$DURATION * $CALLRATE]

sipp \
	${SUT} \
        -key domain ${DOMAIN} \
	-key expires 300 \
	-i `hostname -i` \
	-p 7022 \
	-r $[CALLRATE] \
	-l 100000000 \
	-m $[NUMCALLS] \
	-sf ./register.sipp.xml \
	-inf ./data.csv \
	-cp 28022 \
	-recv_timeout 60000 \
	-fd 60 \
        -trace_stat \
        -trace_screen 
