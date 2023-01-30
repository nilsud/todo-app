package com.example.todonew.request;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@ToString
public class EmailRequest {

    @Email(message = "Geçersiz e-posta!")
    @NotEmpty(message = "E-posta boş olamaz!")
    private String email;

}
