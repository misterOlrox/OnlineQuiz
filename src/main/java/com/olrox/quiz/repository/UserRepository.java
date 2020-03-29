package com.olrox.quiz.repository;

import com.olrox.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(
            value = "SELECT r.user_id FROM user_role r WHERE r.roles = 'ADMIN' limit 1",
            nativeQuery = true
    )
    Long findAdminId();
}
