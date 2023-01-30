package com.example.todonew;

import com.example.todonew.entity.Todo;
import com.example.todonew.enumeration.StatusEnum;
import com.example.todonew.helper.TokenHelper;
import com.example.todonew.request.TodoRequest;
import com.example.todonew.response.TodoResponse;
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
public class TodoControllerIT {

    private  final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                  value = {"/todoController/add_user.sql","/todoController/add_todo.sql"})
    public void given_token_and_todoId_when_getOneTodo_then_statusCodeIs200_return_todoResponse(){

        //Given
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer " + TokenHelper.user_token_with_id_1);//////

        //When
        final HttpEntity<String> request = new HttpEntity<>(null, requestHeaders);
        final ResponseEntity<TodoResponse> response = restTemplate.exchange("/todo/1"
                , HttpMethod.GET
                , request
                , new ParameterizedTypeReference<>() {});

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("test1",response.getBody().getName());
        assertEquals(StatusEnum.COMPLETED,response.getBody().getStatus());


    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = {"/todoController/add_user.sql","/todoController/add_todo.sql"})
    public void given_token_and_todoId_when_getOneTodoByNonExistingId_then_statusCodeIs404(){

        //Given
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer " + TokenHelper.user_token_with_id_1);//////

        //When
        final HttpEntity<String> request = new HttpEntity<>(null, requestHeaders);
        final ResponseEntity<TodoResponse> response = restTemplate.exchange("/todo/3"
                , HttpMethod.GET
                , request
                , new ParameterizedTypeReference<>() {});

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    @Sql (executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
            ,value = {"/todoController/add_user.sql", "/todoController/add_todo.sql"})
    public void given_authorization_token_and_todoRequest_when_createOneTodo_then_status_code_is_200_return_todoResponse(){

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+ TokenHelper.user_token_with_id_1);

        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setName("test1");
        todoRequest.setStatus(StatusEnum.COMPLETED);

        JsonNode requestBodyJson = objectMapper.valueToTree(todoRequest);

        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, requestHeaders);
        final ResponseEntity<Todo> response = restTemplate.exchange("/todo"
                , HttpMethod.POST
                , request
                , new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("test1", response.getBody().getName());
        assertEquals(StatusEnum.COMPLETED, response.getBody().getStatus());
    }




    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
            , value = {"/todoController/add_user.sql", "/todoController/add_todo.sql"})
    public void given_authorization_tokenId_and_todoRequest_when_updateOneTodo_then_status_code_is_200(){

        HttpHeaders requestHeaders =  new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+TokenHelper.user_token_with_id_1);


        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setName("test1");
        todoRequest.setStatus(StatusEnum.COMPLETED);

        JsonNode requestBodyJson = objectMapper.valueToTree(todoRequest);

        //When
        final HttpEntity<JsonNode> request = new HttpEntity<>(requestBodyJson, requestHeaders);
        final ResponseEntity<TodoResponse> response = restTemplate.exchange("/todo/1"
                , HttpMethod.PUT
                , request
                , new ParameterizedTypeReference<>() {
                });

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test1", response.getBody().getName());
        assertEquals(StatusEnum.COMPLETED, response.getBody().getStatus());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            value = {"/todoController/add_user.sql","/todoController/add_todo.sql"})
    public void given_authorizationToken_and_todoId_when_deleteOneTodo_then_statusCode_is_200(){

        //Given
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization","Bearer " + TokenHelper.user_token_with_id_1);//////

        //When
        final HttpEntity<String> request = new HttpEntity<>(null, requestHeaders);
        final ResponseEntity<TodoResponse> response = restTemplate.exchange("/todo/1"
                , HttpMethod.DELETE
                , request
                , new ParameterizedTypeReference<>() {});

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
    }



}
