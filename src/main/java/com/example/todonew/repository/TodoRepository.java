package com.example.todonew.repository;

import com.example.todonew.entity.Todo;
import com.example.todonew.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {

    List<Todo> findByUserId(long userId);

    @Query("SELECT t FROM Todo t WHERE t.status='COMPLETED'")
    List<Todo> findByCompleted(Enum isCompleted);



}
