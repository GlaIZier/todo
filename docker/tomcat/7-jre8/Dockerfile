FROM tomcat:7-jre8
COPY app/* /usr/local/tomcat/webapps/
RUN mkdir /usr/local/tomcat/keys/
COPY keys/.keystore /usr/local/tomcat/keys/
COPY conf/* /usr/local/tomcat/conf/
RUN mkdir /usr/local/scripts/
COPY scripts/* /usr/local/scripts/
RUN find /usr/local/scripts -type f -exec chmod +x {} \;

EXPOSE 8443