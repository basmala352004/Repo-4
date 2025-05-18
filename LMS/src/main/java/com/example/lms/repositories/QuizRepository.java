package com.example.lms.repositories;

import com.example.lms.models.QuizModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<QuizModel, Long> {
    // Custom query methods can be added here if necessary
    Optional<QuizModel> findById(Long id);
}
