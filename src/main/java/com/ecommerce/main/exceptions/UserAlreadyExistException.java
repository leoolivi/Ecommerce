package com.ecommerce.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason="User already exists")
public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}
