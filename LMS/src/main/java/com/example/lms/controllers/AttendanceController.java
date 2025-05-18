package com.example.lms.controllers;


import com.example.lms.models.AttendanceModel;
import com.example.lms.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/display-all-attendance")
    public ResponseEntity<List<AttendanceModel>> displayAllAttendance() {
        return ResponseEntity.ok(attendanceService.displayAllAttendance());
    }
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PostMapping("/display-lesson-attendance")
    public ResponseEntity<List<AttendanceModel>> displayLessonAttendance(@RequestParam long lessonId) {
        return ResponseEntity.ok(attendanceService.displayLessonAttendance(lessonId));
    }
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/attend-lesson")
    public ResponseEntity<String> attendLesson(@RequestParam int studentId, @RequestParam Long lessonId, @RequestParam String otp) {
        return ResponseEntity.ok(attendanceService.attendLesson(studentId, lessonId, otp));
    }
}
