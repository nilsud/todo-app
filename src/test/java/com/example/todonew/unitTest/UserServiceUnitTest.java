package com.example.todonew.unitTest;

import com.example.todonew.entity.Todo;
import com.example.todonew.entity.User;
import com.example.todonew.repository.UserRepository;
import com.example.todonew.request.ResetPasswordRequest;
import com.example.todonew.request.UserRequest;
import com.example.todonew.response.UserResponse;
import com.example.todonew.service.UserService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic;

    @BeforeClass
    public static void init() {
        securityContextHolderMockedStatic = mockStatic(SecurityContextHolder.class);
    }

    @AfterClass
    public static void close() {
        securityContextHolderMockedStatic.close();
    }


    @Test
    public void given_authorization_when_getAuthenticatedUserId_thenReturn_long_userId(){
        //Given
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("1");

        User user = createUser();

        //When
        Long userId = userService.getAuthenticatedUserId();

        //Then
        assertEquals(user.getId(), userId);
    }

    @Test
    public void given_authorization_when_getAuthenticatedUser_thenReturn_optionalUser(){
        //Given
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("1");

        User user = createUser();

        when(userRepository.findById(user.getId())).thenReturn((Optional<User>) authentication.getPrincipal());

        //When
        Optional<User> optionalUser = userService.getAuthenticatedUser();

        //Then
        assertEquals(user.getId(), optionalUser.get().getId());
        assertEquals(user.getId(), optionalUser.get().getUserName());
        assertEquals(user.getEmail(), optionalUser.get().getEmail());
    }

    @Test
    public void given_userId_when_getUser_thenReturn_userResponse(){
        //Given
        User user =createUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //When
        UserResponse userResponse = userService.getUser(user.getId());

        //Then
        assertEquals(user.getId(),userResponse.getId());
        assertEquals(user.getUserName(), userResponse.getUserName());
        assertEquals(user.getEmail(), userResponse.getEmail());


    }

    @Test
    public void given_userId_and_userRequest_when_updateUser_thenReturn_userResponse(){

        //Given
        User user =createUser();

        UserRequest userRequest = new UserRequest();
        userRequest.setUserName(user.getUserName());
        userRequest.setEmail(user.getEmail());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //When
        UserResponse userResponse = userService.updateUser(user.getId(), userRequest);

        //Then
        assertEquals(user.getId(),userResponse.getId());
        assertEquals(user.getUserName(), userResponse.getUserName());
        assertEquals(user.getEmail(), userResponse.getEmail());

    }

    @Test
    public void given_userId_and_resetPasswordRequest_when_resetPassword_thenReturn_userResponse(){
        //Given

        User user = createUser();
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setOldPassword("12345678");
        resetPasswordRequest.setNewPassword("newPassword");


        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(resetPasswordRequest.getOldPassword(), user.getPasswordHash())).thenReturn(true);
        when(passwordEncoder.encode(resetPasswordRequest.getNewPassword())).thenReturn(null);
        when(userRepository.save(any())).thenReturn(user);

        //When
        ResponseEntity<UserResponse> userResponseResponseEntity = userService.resetPassword(user.getId(), resetPasswordRequest);

        //Then
        assertEquals(user.getId(), userResponseResponseEntity.getBody().getId());
        assertEquals(user.getUserName(), userResponseResponseEntity.getBody().getUserName());
        assertEquals(user.getEmail(), userResponseResponseEntity.getBody().getEmail());



    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("username");
        user.setEmail("userEmail");
        user.setPasswordHash("$2a$10$5k99tvpc.Vi6fua8d9GOyOA2iyIDqgR.HQa1hHn1pZ9ajvdWlt3Um");
        return user;
    }

}
