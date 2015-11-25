CREATE KEYSPACE sipr WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

USE sipr;

CREATE TABLE users (username text PRIMARY KEY, sip_pwd text);

CREATE TABLE registrations (username text, contact text, call_id text, cseq int, expires int, PRIMARY KEY (username, contact));

CREATE TABLE subscriptions (username text, contact text, call_id text, cseq int, expires int, type text, PRIMARY KEY (username, contact, type));

CREATE TABLE user_presence (username text PRIMARY KEY, presence text);