package com.example.MedicNote_Application.repository;

import com.example.MedicNote_Application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);  // ✅ Add this
    boolean existsByUsername(String username);       // ✅ For validation in register



}
