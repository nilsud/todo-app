package com.example.todonew.response;

import com.example.todonew.entity.Todo;
import com.example.todonew.enumeration.StatusEnum;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class TodoResponse {

    private Long id;
    private String name;
    private StatusEnum status;


    public static TodoResponse fromEntity(Todo todo) {
        return new TodoResponseBuilder()
                .id(todo.getId())
                .name(todo.getName())
                .status(todo.getStatus())
                .build();
    }


}
