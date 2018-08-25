package pl.edu.podwozka.podwozkasrv.web.rest.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Simple exception with a message, that returns an Internal Server Error code.
 */
public class InternalServerErrorException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(String message) {
        super(ExceptionConstants.PROBLEM_WITH_MESSAGE, message, Status.INTERNAL_SERVER_ERROR);
    }
}
