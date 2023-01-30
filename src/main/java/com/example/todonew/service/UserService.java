package com.example.todonew.service;

import com.example.todonew.entity.User;
import com.example.todonew.exception.BusinessException;
import com.example.todonew.exception.ErrorCode;
import com.example.todonew.repository.TodoRepository;
import com.example.todonew.repository.UserRepository;
import com.example.todonew.request.ResetPasswordRequest;
import com.example.todonew.request.UserRequest;
import com.example.todonew.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Long getAuthenticatedUserId() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")) {
            throw new BusinessException(ErrorCode.unauthorized, "Yeterli yetki bulunmuyor!");
        }
        return Long.parseLong(principal);
    }

    public Optional<User> getAuthenticatedUser() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")) {
            return Optional.empty();
        }
        return userRepository.findById(Long.parseLong(principal));
    }

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "Yeterli yetki bulunmuyor!"));

        return UserResponse.fromEntity(user);
    }

    public UserResponse updateUser(long userId, UserRequest body) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "Yeterli yetki bulunmuyor!"));


        user.setUserName(body.getUserName());
        user.setEmail(body.getEmail());
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    public ResponseEntity<UserResponse> resetPassword(long userId, ResetPasswordRequest body) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "Bulunamadı!"));

        if (!passwordEncoder.matches(body.getOldPassword(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.password_mismatch, "Şifre uyuşmuyor!");
        }

        user.setPasswordHash(passwordEncoder.encode(body.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(UserResponse.fromEntity(user));

    }


}
