FROM jboss/wildfly:13.0.0.Final

# User root user to install software
USER root

COPY target/podwozka-srv-0.0.1-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/podwozka-srv-0.0.1-SNAPSHOT.war

# Switch back to jboss user
USER jboss

# Expose the ports we're interested in
EXPOSE 8080
