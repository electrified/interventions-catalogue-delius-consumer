# interventions-catalogue-delius-consumer - ALPHA

This application is a Spring Boot app that consumes from an SQS queue, and performs CRUD operations on the Delius NSI tables.

It was written as an investigatory spike to prove out various concepts for the Interventions Catalogue work.

Concepts demonstrated:
- Consuming Avro JSON messages and updating an Oracle database schema
- Tables necessary to update to create NSI reference data
- SQS message consumption

Notes/Limitations
- A delete message sets the active/selectable flag to false
- Currently doesn't consider the version number of messages - applies them in the order received

## Running the application

To run the application, an Oracle database with the Delius schema is required.

Construct Oracle docker image Oracle 11 XE, using https://github.com/oracle/docker-images/tree/master/OracleDatabase/SingleInstance

Once you have a docker image, it can be started as follows. Replace `/home/ed/moj/delius_data` 
with a location where you would like to store the Oracle data. Place the Delius schema SQL file 
in this directory too.

```
docker run --name delius-database \
--shm-size=1g \
-p 1521:1521 -p 8088:8080 \
-e ORACLE_PWD=systempass \
-v /home/ed/moj/delius_data:/u01/app/oracle/oradata \
oracle/database:11.2.0.2-xe
```

Once you have a running container, get a shell on it, and use SQLPlus to create a user.
```
docker exec -it <container id> /bin/bash
sqlplus sys/systempass@//localhost:1521/XE as sysdba

CREATE USER DELIUS_APP_SCHEMA IDENTIFIED BY deliuspass;
GRANT CONNECT, CREATE SESSION, RESOURCE, DBA TO DELIUS_APP_SCHEMA;
```

Reconnect as the new user and run the SQL file to create the schema. (The unlicensed version of Flyway does not support Oracle 11)

The delius schema SQL file can be obtained from the [Community API](https://github.com/ministryofjustice/community-api/blob/master/delius.sql) repository.
```
sqlplus DELIUS_APP_SCHEMA/deliuspass@//localhost:1521/XE

@/u01/app/oracle/oradata/delius.sql

ALTER SESSION SET CURRENT_SCHEMA = DELIUS_APP;
```

Finally the application can be started:

```bash
./gradlew bootRun
```
