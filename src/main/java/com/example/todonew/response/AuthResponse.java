package com.example.todonew.response;

import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponse {

    private Long id;
    private String token;

}
