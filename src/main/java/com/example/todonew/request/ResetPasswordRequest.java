package com.example.todonew.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ToString
public class ResetPasswordRequest {

    @Size(min = 6)
    @NotEmpty(message = "Eski şifre boş olamaz!")
    private String oldPassword;

    @Size(min = 6)
    @NotEmpty(message = "Yeni şifre boş olamaz!")
    private String newPassword;

}

