package pl.edu.podwozka.podwozkasrv.time;

import pl.edu.podwozka.podwozkasrv.config.Constants;

import java.time.Instant;
import java.time.LocalDateTime;

public final class TimeUtil {

    public static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(Constants.DEFAULT_ZONE_OFFSET).toInstant();
    }

    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, Constants.DEFAULT_ZONE_OFFSET);
    }

    private TimeUtil() {
    }
}
