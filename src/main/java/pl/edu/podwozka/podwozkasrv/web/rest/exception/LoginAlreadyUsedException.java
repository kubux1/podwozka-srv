package pl.edu.podwozka.podwozkasrv.web.rest.exception;

public class LoginAlreadyUsedException extends BadRequestException {

    public LoginAlreadyUsedException() {
        super(ExceptionConstants.LOGIN_ALREADY_USED, "Login name already used!", "userManagement", "userexists");
    }
}
