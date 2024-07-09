package org.andrelucs.examplerestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends AuthenticationServiceException implements Serializable {
    private static final long serialVersionUID = 1L;

    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
