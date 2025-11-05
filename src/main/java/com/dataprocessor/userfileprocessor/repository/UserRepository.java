package com.dataprocessor.userfileprocessor.repository;

import com.dataprocessor.userfileprocessor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findBySource(String source);

    boolean existsByEmailAndSource(String email, String source);

    Optional<User> findByEmailAndSource(String email, String source);
}