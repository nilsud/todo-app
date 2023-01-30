package com.example.todonew.service;

import com.example.todonew.entity.User;
import com.example.todonew.exception.BusinessException;
import com.example.todonew.exception.ErrorCode;
import com.example.todonew.repository.UserRepository;
import com.example.todonew.request.LoginRequest;
import com.example.todonew.request.RegisterRequest;
import com.example.todonew.request.ResetPasswordRequest;
import com.example.todonew.response.AuthResponse;
import com.example.todonew.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequest body) {


        User existingUser = userRepository.findByEmail(body.getEmail());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.account_already_exists, "Email zaten bulunuyor!");
        }

        User user = new User();
        user.setUserName(body.getUsername());
        user.setEmail(body.getEmail());
        user.setPasswordHash(passwordEncoder.encode(body.getPassword()));
        userRepository.save(user);

    }

    public AuthResponse login(LoginRequest body) {
        User user = userRepository.findByEmail(body.getEmail());
        if (user == null) {
            throw new BusinessException(ErrorCode.account_missing, "Email bulunamadı!");
        }

        if (!passwordEncoder.matches(body.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.password_mismatch, "Şifre uyuşmuyor!");
        }
        return AuthResponse.builder()
                .id(user.getId())
                .token(jwtService.createToken(user.getId().toString()))
                .build();
    }


}

