package com.example.todonew.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;



    @Column(name= "email")
    private String email;


    @Column(name = "password_hash")
    private String passwordHash;


}




