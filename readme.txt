

using local database:

service postgresql start

create database vuebato;

jdbc:postgresql://localhost:5432/vuebato

run sql from /sql/tables.sql

run without tomcat:

mvn spring-boot:run
npm install
npm run serve

localhost:8081/frontvuebato/

run with tomcat:

mvn clean install
backvuebato.war to tomcat (webapps)


for another tomcat, which runs on port 8081

in webapps create new folder frontvuebato
add WEB-INF from filesForTomcat to frontvuebato

npm install
npm run build
copy from dist to frontvuebato

to config tomcat:

tomcatFront/conf/server.xml

<Server port="8006" shutdown="SHUTDOWN">

<Connector port="8081" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8444" />

WEB-INF with file web.xml to ROOT and front

localhost:8081/frontvuebato/



for remote debug backvuebato on tomcat
run 
./catalina.sh jpda start

in Idea
Edit configuration
+
Remote JVM Debug

Name: Tomcat
Port: 8000     (by default tomcat listen 8000 port)
Host: localhost


To get application working from other devices change localhost to ip / url of tomcat with backvuebato

Vue.prototype.$api_url = "http://localhost:8080/backvuebato/";
axios.post('http://localhost:8080/backvuebato/login', data)

and here put ip/url of tomcat with frontvuebato
config.setAllowedOrigins(Arrays.asList("http://localhost:8081"));



