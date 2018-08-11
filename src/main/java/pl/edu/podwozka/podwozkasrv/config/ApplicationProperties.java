package pl.edu.podwozka.podwozkasrv.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@ComponentScan(basePackages = {"pl.edu.podwozka.podwozkasrv"})
public class ApplicationProperties {
}
