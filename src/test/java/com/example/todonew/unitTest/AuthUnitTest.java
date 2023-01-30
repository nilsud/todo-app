package com.example.todonew.unitTest;


import com.example.todonew.entity.User;
import com.example.todonew.repository.UserRepository;
import com.example.todonew.request.LoginRequest;
import com.example.todonew.request.RegisterRequest;
import com.example.todonew.request.UserRequest;
import com.example.todonew.response.AuthResponse;
import com.example.todonew.security.JwtService;
import com.example.todonew.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthUnitTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;


    @Test
    public void given_register_request_when_auth_service_register_then_return_nothing(){

        //given
        User user = createUser();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(user.getUserName());
        registerRequest.setEmail(user.getEmail());
        registerRequest.setPassword("12345678");

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("$2a$10$5k99tvpc.Vi6fua8d9GOyOA2iyIDqgR.HQa1hHn1pZ9ajvdWlt3Um");
        when(userRepository.save(argumentCaptor.capture())).thenReturn(null);

        //when
        authService.register(registerRequest);

        //then
        User capturedUser = argumentCaptor.getValue();

        verify(userRepository).save(capturedUser);

        assertEquals(registerRequest.getUsername(),capturedUser.getUserName());
        assertEquals(registerRequest.getEmail(),capturedUser.getEmail());


    }

    @Test
    public void given_login_request_when_auth_service_then_return_auth_response(){

        //given
        User user = createUser();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword("12345678");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtService.createToken("1")).thenReturn( "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9."+
                "eyJpc3MiOiJ0b2RvLWFwcCIsImF1ZCI6InRvZG8tYXBwIiwic3ViIjoiMSIsImlhdCI6MTY1ODc0MzQyOSwiZXhwIjoxNjU5NjA3NDI5fQ."+
                "CPBFAJVHcJbnr0uHO4y_uqHoDMC4l3GTBOymAgz3LDvSt4qXursdNctfBoUpAJ16Eiy0uat-QWIvjb03edjD8Q");

        //when
       AuthResponse authResponse = authService.login(loginRequest);

       //Then
        assertEquals(1L,authResponse.getId());
        assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9."+
                "eyJpc3MiOiJ0b2RvLWFwcCIsImF1ZCI6InRvZG8tYXBwIiwic3ViIjoiMSIsImlhdCI6MTY1ODc0MzQyOSwiZXhwIjoxNjU5NjA3NDI5fQ."+
                "CPBFAJVHcJbnr0uHO4y_uqHoDMC4l3GTBOymAgz3LDvSt4qXursdNctfBoUpAJ16Eiy0uat-QWIvjb03edjD8Q", authResponse.getToken());
    }

    public User createUser(){
        User user = new User();
        user.setId(1L);
        user.setUserName("test");
        user.setEmail("test@mail.com");
        user.setPasswordHash("$2a$10$5k99tvpc.Vi6fua8d9GOyOA2iyIDqgR.HQa1hHn1pZ9ajvdWlt3Um");
        return user;
    }



}
