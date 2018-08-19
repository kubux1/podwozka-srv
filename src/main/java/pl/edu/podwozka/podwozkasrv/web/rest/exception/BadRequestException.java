package pl.edu.podwozka.podwozkasrv.web.rest.exception;

import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BadRequestException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    @Getter
    private final String entityName;

    @Getter
    private final String errorKey;

    public BadRequestException(String defaultMessage, String entityName, String errorKey) {
        this(ExceptionConstants.PROBLEM_WITH_MESSAGE, defaultMessage, entityName, errorKey);
    }

    public BadRequestException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, Status.BAD_REQUEST, null, null, null,
                getParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    private static Map<String, Object> getParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
