# interventions-catalogue-delius-consumer

```
Construct Oracle docker image Oracle 11 XE, using https://github.com/oracle/docker-images/tree/master/OracleDatabase/SingleInstance

docker run --name delius-database \
--shm-size=1g \
-p 1521:1521 -p 8088:8080 \
-e ORACLE_PWD=systempass \
-v /home/ed/moj/delius_data:/u01/app/oracle/oradata \
oracle/database:11.2.0.2-xe

docker exec -it efc0b96d8007 /bin/bash
sqlplus sys/systempass@//localhost:1521/XE as sysdba

CREATE USER DELIUS_APP_SCHEMA IDENTIFIED BY deliuspass;
GRANT CONNECT, CREATE SESSION, RESOURCE, DBA TO DELIUS_APP_SCHEMA;


sqlplus DELIUS_APP_SCHEMA/deliuspass@//localhost:1521/XE

@/u01/app/oracle/oradata/delius.sql

ALTER SESSION SET CURRENT_SCHEMA = DELIUS_APP;


```
