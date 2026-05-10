package com.example.Vibra.repository;

import com.example.Vibra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Para login
    Optional<User> findByEmail(String email);

    // Para validar duplicados
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}