package com.example.todonew.request;

import com.example.todonew.enumeration.StatusEnum;
import com.example.todonew.validation.StatusValidation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data

public class TodoRequest {

    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
