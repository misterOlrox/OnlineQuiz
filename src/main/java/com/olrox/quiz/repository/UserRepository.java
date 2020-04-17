package com.olrox.quiz.repository;

import com.olrox.quiz.entity.Role;
import com.olrox.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Optional<User> findFirstByRolesContaining(Role role);
}
