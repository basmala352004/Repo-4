package com.example.lms.services;

import com.example.lms.models.*;
import com.example.lms.repositories.QuizRepository;
import com.example.lms.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {


    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    // Method to create a new quiz
    public void createQuiz(QuizModel quiz) {
        quizRepository.save(quiz);
    }

    // Method to add a question to an existing quiz
    public void addQuestionToQuiz(Long quizId, QuestionModel question) {
        // Fetch the quiz from the repository
        Optional<QuizModel> quiz = quizRepository.findById(quizId);

        // If quiz doesn't exist, we can throw an exception or handle it differently
        if (quiz.isPresent()) {
            QuizModel existingQuiz = quiz.get();
            if (question.getId() == null) {
                questionRepository.save(question);  // Persist question first
            }

            // Set the quiz for the question
            question.setQuiz(existingQuiz);

            // Add the question to the quiz's question list
            existingQuiz.addQuestion(question);

            // Save the quiz (cascade will save question as well if it's new)
            quizRepository.save(existingQuiz);
        }
    }

    // Fetch random questions for a quiz
    public List<QuestionModel> getRandomQuestions(Long quizId, int numberOfQuestions) {
        Optional<QuizModel> quiz = quizRepository.findById(quizId);
        if (quiz.isPresent()) {
            List<QuestionModel> questions = quiz.get().getQuestions();
            Collections.shuffle(questions); // Shuffle the questions for randomness
            return questions.subList(0, Math.min(numberOfQuestions, questions.size())); // Select a random subset
        }
        return Collections.emptyList(); // Return an empty list if the quiz does not exist
    }
    public QuizModel gradeQuiz(long quizId, double grade) {
        QuizModel quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz != null) {
            quiz.setGrade(grade);
            return quizRepository.save(quiz);
        }
        return null;
    }
    public  QuizModel submitQuiz(QuizModel quiz) {
        return quizRepository.save(quiz);
    }
    // Fetch quiz by ID, returning Optional to handle quiz not found case
    public Optional<QuizModel> getQuizById(Long quizId) {
        return quizRepository.findById(quizId);
    }
}
