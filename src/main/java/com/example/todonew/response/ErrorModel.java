package com.example.todonew.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorModel {

    private int statusCode;
    private String errorCode;
    private String message;

}

