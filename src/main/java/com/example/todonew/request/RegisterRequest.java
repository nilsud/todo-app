package com.example.todonew.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ToString
public class RegisterRequest {

    @Email(message = "Geçersiz e-posta!")
    @NotEmpty(message = "Email boş olamaz!")
    private String email;

    @Size(min = 6)
    @NotEmpty(message = "Şifre boş olamaz!")
    private String password;

    @NotEmpty(message = "İsim boş olamaz!")
    private String username;

    private Long id;

}

