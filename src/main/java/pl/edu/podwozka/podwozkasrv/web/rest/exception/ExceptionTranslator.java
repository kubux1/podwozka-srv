package pl.edu.podwozka.podwozkasrv.web.rest.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import pl.edu.podwozka.podwozkasrv.web.rest.util.HeaderUtil;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }
}
