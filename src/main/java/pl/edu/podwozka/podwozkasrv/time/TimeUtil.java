package pl.edu.podwozka.podwozkasrv.time;

import pl.edu.podwozka.podwozkasrv.config.Constants;

import java.time.Instant;
import java.time.LocalDateTime;

public final class TimeUtil {

    public static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(Constants.defaultZoneOffset).toInstant();
    }

    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, Constants.defaultZoneOffset);
    }

    private TimeUtil() {}
}
