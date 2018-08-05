FROM jboss/wildfly:13.0.0.Final

# User root user to install software
USER root

COPY target/podwozka-srv-0.0.1-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/podwozka-srv-0.0.1-SNAPSHOT.war

# Switch back to jboss user
USER jboss

# Add a management user named 'admin'
# Remove this line before application deploy
RUN /opt/jboss/wildfly/bin/add-user.sh -u admin -p admin --silent

# Expose an application port
EXPOSE 8080
# Debug port
EXPOSE 8787

# Start WildFly in standalone/debug mode and allow management from host
# Remove this line before application deploy
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "--debug"]