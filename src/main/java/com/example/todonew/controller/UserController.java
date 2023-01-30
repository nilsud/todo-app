package com.example.todonew.controller;

import com.example.todonew.request.ResetPasswordRequest;
import com.example.todonew.request.UserRequest;
import com.example.todonew.response.UserResponse;
import com.example.todonew.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/me")
    public UserResponse getUser() {
        return userService.getUser(userService.getAuthenticatedUserId());
    }

    @PutMapping("/me")
    public UserResponse updateUser(@Valid @RequestBody UserRequest body) {
       return userService.updateUser(userService.getAuthenticatedUserId(), body);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<UserResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest body) {
        return userService.resetPassword(userService.getAuthenticatedUserId(), body);
    }


}
