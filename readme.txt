

using local database:

service postgresql start

create database vuebato;

jdbc:postgresql://localhost:5432/vuebato

run sql from /sql/tables.sql

mvn clean install
