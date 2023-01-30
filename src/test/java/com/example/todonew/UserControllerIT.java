package com.example.todonew;


import com.example.todonew.helper.TokenHelper;
import com.example.todonew.request.ResetPasswordRequest;
import com.example.todonew.request.UserRequest;
import com.example.todonew.response.UserResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerIT {

    private  final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private TestRestTemplate restTemplate;



    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
            ,value = {"/userController/add_user.sql"})
    public void given_authorization_token_when_getUser_then_status_code_is_200_return_userResponse(){

        //Given
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+ TokenHelper.user_token_with_id_1);

        //When
        final HttpEntity<String> request = new HttpEntity<>(null, requestHeaders);
        final ResponseEntity<UserResponse> response = restTemplate.exchange("/user/me",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<UserResponse>() {});

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("usertest1", response.getBody().getUserName());
        assertEquals("usertest@mail.com", response.getBody().getEmail());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
            ,value = {"/userController/add_user.sql"})
    public void given_authorization_token_and_userRequest_when_updateUser_then_status_code_is_200_return_userResponse(){

        //Given
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+ TokenHelper.user_token_with_id_1);

        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("usertest1Update");
        userRequest.setEmail("usertestUpdate@mail.com");

        JsonNode requestBodyJson = objectMapper.valueToTree(userRequest);


        //When
        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, requestHeaders);
        final ResponseEntity<UserResponse> response = restTemplate.exchange("/user/me",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<UserResponse>() {});

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("usertest1Update", response.getBody().getUserName());
        assertEquals("usertestUpdate@mail.com", response.getBody().getEmail());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
            ,value = {"/userController/add_user.sql"})
    public void given_authorization_token_and_resetPasswordRequest_when_resetPassword_then_status_code_is_200(){

        //Given
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+ TokenHelper.user_token_with_id_1);

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setOldPassword("12345678");
        resetPasswordRequest.setNewPassword("newPassword");

        JsonNode requestBodyJson = objectMapper.valueToTree(resetPasswordRequest);


        //When
        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, requestHeaders);
        final ResponseEntity<UserResponse> response = restTemplate.exchange("/user/reset-password",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {});

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



}
