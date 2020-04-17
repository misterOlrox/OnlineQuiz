package com.olrox.quiz.repository;

import com.olrox.quiz.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

}
