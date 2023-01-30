package com.example.todonew.service;

import com.example.todonew.entity.Todo;
import com.example.todonew.entity.User;
import com.example.todonew.exception.BusinessException;
import com.example.todonew.exception.ErrorCode;
import com.example.todonew.repository.TodoRepository;
import com.example.todonew.repository.UserRepository;
import com.example.todonew.request.TodoRequest;
import com.example.todonew.response.TodoResponse;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoService {
    private TodoRepository todoRepository;
    private UserService userService;
    private UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserService userService, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public List<TodoResponse> getAllTodos(long userId){

        List<Todo> list=todoRepository.findByUserId(userId);
        return list.stream().map(t-> TodoResponse.fromEntity(t)).collect(Collectors.toList());///////////////////////////////////
    }

    public TodoResponse getOneTodo(Long todoId,long userId){


        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "Todo bulunamadı"));

        if (!todo.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.resource_missing, "yeterli yetki bulunmuyor");
        }

        return TodoResponse.fromEntity(todo);
    }

    public Todo createOneTodo(Long userId,TodoRequest body){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.forbidden,"ilk önce giriş yapılmalı"));

        Todo todo= new Todo();
        todo.setUser(user);
        todo.setName(body.getName());
        todo.setStatus(body.getStatus());
        todoRepository.save(todo);//önceden return todoRepository.save(todo)
        return todo;

    }
    public TodoResponse updateOneTodo(long todoId,long userId, TodoRequest body) {

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "Todo bulunamadı"));

        if (!todo.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.resource_missing, "yeterli yetki bulunmuyor");
        }

            todo.setName(body.getName());
            todo.setStatus(body.getStatus());
            todoRepository.save(todo);
            return TodoResponse.fromEntity(todo);
    }
    public void deleteOneTodoById(Long todoId,long userId){

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BusinessException(ErrorCode.resource_missing, "Todo bulunamadı"));

        if (!todo.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.resource_missing, "yeterli yetki bulunmuyor");
        }

        todoRepository.deleteById(todoId);
        //return TodoResponse.fromEntity(todo);

    }





}
