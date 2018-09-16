package pl.edu.podwozka.podwozkasrv.web.rest.exception;

import java.net.URI;

public final class ExceptionConstants {

    public static final String BASE_URL = "https://www.podwozka.edu.pl/problem/";

    public static final URI PROBLEM_WITH_MESSAGE = URI.create(BASE_URL + "problem-with-message");

    public static final URI LOGIN_ALREADY_USED = URI.create(BASE_URL + "/login-already-used");

    public static final URI EMAIL_ALREADY_USED = URI.create(BASE_URL + "/email-already-used");

    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_WITH_MESSAGE + "/invalid-password");

    private ExceptionConstants() {
    }
}
