#!/usr/bin/env bash

DIRECTORY=`pwd`
sudo docker run -it --link sipRcassandra:cassandra -v $DIRECTORY/schema.cql:/opt/schema.cql --rm cassandra sh -c 'exec cqlsh "$CASSANDRA_PORT_9042_TCP_ADDR" -f /opt/schema.cql'