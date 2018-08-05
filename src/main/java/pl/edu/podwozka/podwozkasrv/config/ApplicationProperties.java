package pl.edu.podwozka.podwozkasrv.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
}
