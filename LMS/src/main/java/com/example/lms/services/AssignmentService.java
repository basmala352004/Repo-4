package com.example.lms.services;
import com.example.lms.models.Assignment;
import com.example.lms.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }
    //submit assignment -> student
    public Assignment submitAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    //create assignment ->instructor
    public void createAssignment(Assignment assignment) {
        assignmentRepository.save(assignment);
    }

    // grading and feedback -> instructor
    public Assignment gradeAssignment(Integer assignmentId, double grade, String feedback) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (assignment != null) {
            assignment.setGrades(grade);
            assignment.setFeedback(feedback);
            return assignmentRepository.save(assignment);
        }
        return null;
    }
}
