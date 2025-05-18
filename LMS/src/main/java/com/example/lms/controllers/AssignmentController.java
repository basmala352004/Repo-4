package com.example.lms.controllers;

import com.example.lms.models.Assignment;
import com.example.lms.repositories.AssignmentRepository;
import com.example.lms.repositories.CourseRepository;
import com.example.lms.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    private   final AssignmentService assignmentService;
    private  final AssignmentRepository  assignmentRepository;


    // Constructor for dependency injection
    @Autowired
    public AssignmentController(AssignmentService assignmentService, AssignmentRepository assignmentRepository, CourseRepository courseRepository) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
    }


    //create assignment
    @PostMapping("/createAssignment")
    public ResponseEntity<String> createAssignment(@RequestBody Assignment assignment) {
        assignmentService.createAssignment(assignment);
        return ResponseEntity.ok("Assignment created successfully");
    }

    //submit assignment
    @PostMapping("/submit")
    public ResponseEntity<Assignment> submitAssignment(@RequestBody Assignment assignment) {
        Assignment submittedAssignment = assignmentService.submitAssignment(assignment);
        return ResponseEntity.ok(submittedAssignment);
    }
    //grade and feedback
    @PostMapping("/{assignmentId}/grade")
    public ResponseEntity<Assignment> gradeAssignment(
            @PathVariable Integer assignmentId,
            @RequestParam double grade,
            @RequestParam String feedback) {
        return ResponseEntity.ok(assignmentService.gradeAssignment(assignmentId, grade, feedback));
    }
    //fetch assignment details
    @GetMapping("/{assignmentId}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable Integer assignmentId) {
        return ResponseEntity.ok(assignmentRepository.findById(assignmentId).orElseThrow(() -> new RuntimeException("Assignment not found")));
    }
}
