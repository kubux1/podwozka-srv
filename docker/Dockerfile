FROM tomcat:8.5.32-jre8-alpine

# Set the app as root
RUN rm -rf /usr/local/tomcat/webapps/ROOT
ENV MY_CONTEXT=ROOT
# Deploy the app
COPY target/*.war /usr/local/tomcat/webapps/${MY_CONTEXT}.war

# Remove line below in order to disable debug configuration
ENV JPDA_ADDRESS 5005
CMD ["catalina.sh", "jpda", "run"]
EXPOSE 5005