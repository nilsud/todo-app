package com.example.todonew;

import com.example.todonew.request.LoginRequest;
import com.example.todonew.request.RegisterRequest;
import com.example.todonew.response.AuthResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AuthControllerIT {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    //@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,value = {"/authController/add_user.sql"})
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, value = {"/authController/remove_user.sql"})
    public void given_registerRequest_when_authController_register_then_statusCode200(){

        //Given
        HttpHeaders requestHeaders = new HttpHeaders();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("usertest1");
        registerRequest.setEmail("usertest@mail.com");
        registerRequest.setPassword("12345678");

        JsonNode requestBodyJson = objectMapper.valueToTree(registerRequest);

        //when
        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson,requestHeaders);
        final ResponseEntity<RegisterRequest> response = restTemplate.exchange("/auth/register"
                , HttpMethod.POST
                , request
                , new ParameterizedTypeReference<>() {
                });

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
            ,value = {"/authController/add_user.sql"})
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
            ,value = {"/authController/remove_user.sql"})
    public void given_loginRequest_when_authService_then_status_code_200_return_authResponse() {

        //Given
        HttpHeaders requestHeaders = new HttpHeaders();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("usertest@mail.com");
        loginRequest.setPassword("12345678");

        JsonNode requestBodyJson = objectMapper.valueToTree(loginRequest);

        //When
        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, requestHeaders);
        final ResponseEntity<AuthResponse> response = restTemplate.exchange("/auth/login"
                , HttpMethod.POST
                , request, new ParameterizedTypeReference<>() {
                });

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
    }
}
