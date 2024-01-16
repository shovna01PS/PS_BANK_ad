package com.sapient.PSBank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotExistsException extends RuntimeException{
    public CustomerNotExistsException(String message) {
        super(message);
    }
}
