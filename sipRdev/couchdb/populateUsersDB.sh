#!/bin/sh

# test user
curl -H 'Content-Type: application/json' -X POST http://127.0.0.1:5984/sipr -d '{"userName": "200", "sipPassword" : "12345"}'

# load users
for i in $(seq 10000 20000)
do
 curl -H 'Content-Type: application/json' -X POST http://127.0.0.1:5984/sipr -d '{"userName": "'$i'", "sipPassword" : "1234"}'
done