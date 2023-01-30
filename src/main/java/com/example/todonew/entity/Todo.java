package com.example.todonew.entity;

import com.example.todonew.enumeration.StatusEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="todo")
@Data
@Getter
@Setter
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_id_seq")
    @SequenceGenerator(name = "todoseq", sequenceName = "todo_id_seq")
    @Column(name = "todo_id")
    private Long id;

    @Column(name = "todo_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false,updatable = false)
    private User user;








}
