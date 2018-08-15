package pl.edu.podwozka.podwozkasrv.web.rest;

import org.springframework.http.MediaType;
import pl.edu.podwozka.podwozkasrv.config.Constants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for testing REST controllers.
 * This class is package-private.
 */
class TestUtil {

    /** MediaType for JSON UTF8 */
    static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert
     * @return the JSON byte array
     * @throws IOException
     */
    static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return Constants.getDefaultObjectMapper().writeValueAsBytes(object);
    }
}
