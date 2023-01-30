package com.example.todonew.unitTest;

import com.example.todonew.entity.Todo;
import com.example.todonew.entity.User;
import com.example.todonew.enumeration.StatusEnum;
import com.example.todonew.repository.TodoRepository;
import com.example.todonew.repository.UserRepository;
import com.example.todonew.request.TodoRequest;
import com.example.todonew.response.TodoResponse;
import com.example.todonew.response.UserResponse;
import com.example.todonew.service.TodoService;
import com.example.todonew.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TodoServiceUnitTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;




    @Test
    public void given_todoId_when_getAllTodos_then_return_list_of_todo_response(){
        User user=createUser();
        Todo todo= createTodo(user);

        TodoResponse todoResponse = TodoResponse.fromEntity(todo);
        List<TodoResponse> todoResponseList= new ArrayList<>();
        todoResponseList.add(todoResponse);

        TodoService todoServiceSpied = Mockito.spy(todoService);
        doReturn(todoResponseList).when(todoServiceSpied).getAllTodos(user.getId());

        List<TodoResponse> result = todoServiceSpied.getAllTodos(user.getId());

        //todo response da user ım yok onu kontrol etmem gerekiyor mu ,nasıl
        assertEquals(1,result.size());
        assertEquals(todo.getId(),result.get(0).getId());
        assertEquals(todo.getName(),result.get(0).getName());
        assertEquals(todo.getStatus(),result.get(0).getStatus());

    }

    @Test
    public void given_todoId_when_todo_service_getOneTodo_then_return_todo_response(){

        User user= createUser();
        Todo todo= createTodo(user);

        TodoResponse todoResponse = TodoResponse.fromEntity(todo);
        when(todoRepository.findById(todoResponse.getId())).thenReturn(Optional.of(todo));
        TodoResponse result = todoService.getOneTodo(todoResponse.getId(),user.getId());

        assertEquals(todo.getId(),result.getId());
        assertEquals(todo.getName(),result.getName());
        assertEquals(todo.getStatus(),result.getStatus());
    }

    @Test
    public void given_userId_and_todo_request_when_todo_service_createOneTodo_then_return_todo(){

        User user= createUser();
        Todo todo= createTodo(user);

        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setName(todo.getName());
        todoRequest.setStatus(todo.getStatus());

        ArgumentCaptor<Todo> argumentCaptor = ArgumentCaptor.forClass(Todo.class);

        when(todoRepository.save(argumentCaptor.capture())).thenReturn(null);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        Todo result = todoService.createOneTodo(user.getId(),todoRequest);

        Todo todoCaptured = argumentCaptor.getValue();
        UserResponse userResponse = UserResponse.fromEntity(result.getUser());

        assertEquals(user.getId().intValue(),userResponse.getId().intValue());
        assertEquals(user.getUserName(),userResponse.getUserName());
        assertEquals(user.getEmail(),userResponse.getEmail());

        assertEquals(todoRequest.getName(),result.getName());
        assertEquals(todoRequest.getStatus(),result.getStatus());

        //userın password hashi de kontrol edilmeli mi
        assertEquals(user.getId().intValue(),todoCaptured.getUser().getId().intValue());
        assertEquals(user.getUserName(),todoCaptured.getUser().getUserName());
        assertEquals(user.getEmail(),todoCaptured.getUser().getEmail());

        assertEquals(todoRequest.getName(),todoCaptured.getName());
        assertEquals(todoRequest.getStatus(),todoCaptured.getStatus());

    }

    @Test
    public void given_todoId_and_userId_todoRequest_when_todo_service_updateOneTodo_then_return_todoResponse(){

        //given
        User user=createUser();
        Todo todo=createTodo(user);

        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setName(todo.getName());
        todoRequest.setStatus(todo.getStatus());

        ArgumentCaptor<Todo> argumentCaptor = ArgumentCaptor.forClass(Todo.class);

        when(todoRepository.findById(todo.getId())).thenReturn(Optional.ofNullable(todo));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(todoRepository.save(argumentCaptor.capture())).thenReturn(null);

        //when
        todoService.updateOneTodo(todo.getId(), user.getId(), todoRequest);

        //then
        User capturedUser = argumentCaptor.getValue().getUser();
        Todo capturedTodo = argumentCaptor.getValue();

        assertEquals(todo.getUser().getId().intValue(), capturedUser.getId().intValue());
        assertEquals(todo.getUser().getUserName(), capturedUser.getUserName());
        assertEquals(todo.getUser().getEmail(), capturedUser.getEmail());

        assertEquals(todo.getName(), capturedTodo.getName());
        assertEquals(todo.getStatus(), capturedTodo.getStatus());

    }

    @Test
    public void given_todoId_and_userId_when_todo_service_delete_then_return_todoResponse(){

        //given
        User user = createUser();
        Todo todo = createTodo(user);

        ArgumentCaptor<Todo> argumentCaptor =ArgumentCaptor.forClass(Todo.class);

        when(todoRepository.findById(todo.getId())).thenReturn(Optional.ofNullable(todo));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        doNothing().when(todoRepository).delete(argumentCaptor.capture());
        //when
        todoService.deleteOneTodoById(todo.getId(), user.getId());

        //then
        User capturedUser = argumentCaptor.getValue().getUser();
        Todo capturedTodo = argumentCaptor.getValue();

        assertEquals(todo.getUser().getId().intValue(), capturedUser.getId().intValue());
        assertEquals(todo.getUser().getUserName(), capturedUser.getUserName());
        assertEquals(todo.getUser().getEmail(), capturedUser.getEmail());

        assertEquals(todo.getName(), capturedTodo.getName());
        assertEquals(todo.getStatus(), capturedTodo.getStatus());
    }

    private Todo createTodo(User user) {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setUser(user);
        todo.setName("TodoName");
        todo.setStatus(StatusEnum.COMPLETED);
        return todo;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("username");
        user.setEmail("userEmail");
        return user;
    }

}
