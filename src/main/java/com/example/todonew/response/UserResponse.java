package com.example.todonew.response;


import com.example.todonew.entity.User;
import jdk.jshell.Snippet;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {

    private Long id;
    private String userName;
    private String email;

    public static UserResponse fromEntity(User user) {
        if(user == null)
            return null;
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
    }


}
