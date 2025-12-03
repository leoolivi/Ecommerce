package com.ecommerce.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Setting not found")
public class SettingNotFoundException extends Exception {
    public SettingNotFoundException(String msg) {
        super(msg);
    }
}
