package pl.edu.podwozka.podwozkasrv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PodwozkaSrvApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PodwozkaSrvApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PodwozkaSrvApplication.class, args);
    }
}
