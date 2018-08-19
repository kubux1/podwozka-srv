package pl.edu.podwozka.podwozkasrv.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import pl.edu.podwozka.podwozkasrv.config.Constants;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(Constants.SYSTEM_ACCOUNT);
    }
}
