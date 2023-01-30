package com.example.todonew.controller;


import com.example.todonew.entity.Todo;
import com.example.todonew.request.TodoRequest;
import com.example.todonew.response.TodoResponse;
import com.example.todonew.service.TodoService;
import com.example.todonew.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping()
public class TodoController {
    private final TodoService todoService;
    private final UserService userService;


    @GetMapping("/todos/me")
    public List<TodoResponse> getAllMyTodos(){
        return todoService.getAllTodos(userService.getAuthenticatedUserId());
    }

    @GetMapping("/todo/{todoId}")
    public TodoResponse getOneTodo(@PathVariable Long todoId){
        return todoService.getOneTodo(todoId,userService.getAuthenticatedUserId());
   }

    @PostMapping("/todo")
    public Todo createOneTodo( @Valid @RequestBody TodoRequest body){
        return todoService.createOneTodo(userService.getAuthenticatedUserId(), body);
    }
    @PutMapping("/todo/{todoId}")
    public TodoResponse updateOneTodo(@PathVariable Long todoId,@Valid @RequestBody TodoRequest body){
         return todoService.updateOneTodo(todoId,userService.getAuthenticatedUserId(),body);
    }

    @DeleteMapping("/todo/{todoId}")
    public void deleteOneTodo(@PathVariable Long todoId){
        todoService.deleteOneTodoById(todoId, userService.getAuthenticatedUserId());

    }
}
