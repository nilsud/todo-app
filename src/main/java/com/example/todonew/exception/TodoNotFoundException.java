package com.example.todonew.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;


@ResponseStatus(HttpStatus.NOT_FOUND)

public class TodoNotFoundException extends RuntimeException{


    public TodoNotFoundException(String message) {
        super(message);

    }

}
