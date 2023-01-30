package com.example.todonew.request;


import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class UserRequest {

    @NotEmpty(message = "username cannot be empty!")
    private String userName;

    private String email;


}
