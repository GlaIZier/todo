FROM tomcat:7-jre8
COPY app/* /usr/local/tomcat/webapps/
RUN mkdir /usr/local/tomcat/keys/
COPY keys/.keystore /usr/local/tomcat/keys/
COPY conf/* /usr/local/tomcat/conf/

EXPOSE 8443