

using local database:

service postgresql start

create database vuebato;

jdbc:postgresql://localhost:5432/vuebato

run sql from /sql/tables.sql

run without tomcat:

mvn spring-boot:run
npm run serve

localhost:8081/frontvuebato/

run with tomcat:

mvn clean install
backvuebato.war to tomcat (webapps)

npm run build
copy from dist to frontvuebato
in another tomcat, which runs on port 8081

to config tomcat:

tomcatFront/conf/server.xml

<Server port="8006" shutdown="SHUTDOWN">

<Connector port="8081" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8444" />

WEB-INF with file web.xml to ROOT and front

localhost:8081/frontvuebato/
