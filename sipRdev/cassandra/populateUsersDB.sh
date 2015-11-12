#!/usr/bin/env bash

DIRECTORY=`pwd`
sudo docker run -it --link sipRcassandra:cassandra -v $DIRECTORY/populateUsers.cql:/opt/populateUsers.cql --rm cassandra sh -c 'exec cqlsh "$CASSANDRA_PORT_9042_TCP_ADDR" -f /opt/populateUsers.cql'