package pl.edu.podwozka.podwozkasrv.web.rest.exception;

public class EmailAlreadyUsedException extends BadRequestException {

    public EmailAlreadyUsedException() {
        super(ExceptionConstants.EMAIL_ALREADY_USED, "Email address already used!", "userManagement", "userexists");
    }
}
