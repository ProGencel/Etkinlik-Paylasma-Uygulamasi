package com.ahmetefe.backend.repository;

import com.ahmetefe.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long> {

    Optional<User> findByMailIgnoreCase(String mail);

}