package com.ahmetefe.backend.repository;

import com.ahmetefe.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long> {

    Optional<User> findByMailEqualsIgnoreCase(String mail);
    Optional<User> findByIdEquals(Long id);

}