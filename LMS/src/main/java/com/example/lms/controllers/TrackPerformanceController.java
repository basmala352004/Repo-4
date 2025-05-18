package com.example.lms.controllers;

import com.example.lms.services.TrackPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/performance")
public class TrackPerformanceController {


    private final TrackPerformanceService trackPerformanceService;

    @Autowired
    public TrackPerformanceController(TrackPerformanceService trackPerformanceService) {
        this.trackPerformanceService = trackPerformanceService;
    }

    // Endpoint for tracking performance based on attendance
    @PostMapping(value = "/track")
    public ResponseEntity<List<Map<String, Object>>> getPerformanceForCourses(
            @RequestBody PerformanceRequest request) {
        List<Map<String, Object>> performanceDetails = trackPerformanceService.getPerformanceForCourses(
                request.getCourseNames(), request.getLessonName());
        return ResponseEntity.ok(performanceDetails);
    }

    // Endpoint for fetching assignment grades and feedback
    @GetMapping(value = "/assignments/{assignmentId}")
    public ResponseEntity<Map<String, Object>> getAssignmentGrades(@PathVariable Integer assignmentId) {
        Map<String, Object> assignmentGrades = trackPerformanceService.getAssignmentGrades(assignmentId);
        return ResponseEntity.ok(assignmentGrades);
    }

    @GetMapping(value = "/quizes/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuizGrades(@PathVariable long quizId) {
        Map<String, Object> quizGrades = trackPerformanceService.getQuizGrades(quizId);
        return ResponseEntity.ok(quizGrades);
    }

    // Request class for tracking performance
    public static class PerformanceRequest {
        private List<String> courseNames;
        private String lessonName;

        // Getters and setters
        public List<String> getCourseNames() {
            return courseNames;
        }

        public void setCourseNames(List<String> courseNames) {
            this.courseNames = courseNames;
        }

        public String getLessonName() {
            return lessonName;
        }

        public void setLessonName(String lessonName) {
            this.lessonName = lessonName;
        }
    }
}
