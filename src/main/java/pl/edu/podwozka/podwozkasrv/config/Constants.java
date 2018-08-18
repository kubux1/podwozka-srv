package pl.edu.podwozka.podwozkasrv.config;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.ZoneOffset;

/**
 * Application constants.
 */
public final class Constants {

    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new StdDateFormat());

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        return mapper;
    }

    public final static ZoneOffset defaultZoneOffset = ZoneOffset.UTC;

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String DEFAULT_LANGUAGE = "en";

    public static final String ANONYMOUS_USER = "anonymoususer";

    private Constants() {
    }
}
