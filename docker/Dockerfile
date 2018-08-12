FROM tomcat:8.5.32-jre8-alpine

# Deploy the app
COPY target/podwozka-srv-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/podwozka-srv-0.0.1-SNAPSHOT.war

# Remove line below in order to disable debug configuration
ENV JPDA_ADDRESS 5005
CMD ["catalina.sh", "jpda", "run"]
EXPOSE 5005