#!/usr/bin/env bash

sudo docker run -it --link sipRmysql:mysql -v $DIRECTORY/schema.sql:/opt/schema.sql \
    --rm mysql sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD" < /opt/schema.sql'