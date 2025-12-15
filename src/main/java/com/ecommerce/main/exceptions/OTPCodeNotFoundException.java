package com.ecommerce.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="OTP code not found")
public class OTPCodeNotFoundException extends Exception{
    public OTPCodeNotFoundException (String msg) {
        super(msg);
    }
}
