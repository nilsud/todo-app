package com.example.todonew.repository;

import com.example.todonew.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;




@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUserName(String userName);
    public User findByEmail(String email);
}
