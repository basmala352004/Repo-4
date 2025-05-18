package com.example.lms.controllers;

import com.example.lms.dtos.CourseDTO;
import com.example.lms.dtos.StudentDTO;
import com.example.lms.models.*;
import com.example.lms.repositories.UserRepository;
import com.example.lms.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final CourseService courseService;
    private final AttendanceService attendanceService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final TrackPerformanceService trackPerformanceService;
    private final AssignmentService assignmentService;
    private final QuizService quizService;

    @Autowired
    public StudentController(StudentService studentService,
                             CourseService courseService,
                             AttendanceService attendanceService,
                             NotificationService notificationService,
                             UserRepository userRepository,
                             TrackPerformanceService trackPerformanceService,
                             AssignmentService assignmentService,
                             QuizService quizService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.attendanceService = attendanceService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.trackPerformanceService = trackPerformanceService;
        this.assignmentService = assignmentService;
        this.quizService = quizService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<List<StudentDTO>> retrieveAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<StudentModel> retrieveStudentById(@PathVariable int id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/enrollCourse")
    public ResponseEntity<String> enrollCourse(@RequestParam("Course_id") long courseid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findByName(currentUserName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        studentService.enrollStudent(user.getId(), courseid);
        logger.info("Student {} enrolled in course {}", user.getId(), courseid);
        return ResponseEntity.ok("Student enrolled successfully");
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(value = "/assignments/{assignmentId}")
    public ResponseEntity<Map<String, Object>> getAssignmentGrades(@PathVariable Integer assignmentId) {
        Map<String, Object> assignmentGrades = trackPerformanceService.getAssignmentGrades(assignmentId);
        return ResponseEntity.ok(assignmentGrades);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(value = "/quizes/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuizGrades(@PathVariable long quizId) {
        Map<String, Object> quizGrades = trackPerformanceService.getQuizGrades(quizId);
        return ResponseEntity.ok(quizGrades);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/submitAssignment")
    public ResponseEntity<Assignment> submitAssignment(@RequestBody Assignment assignment) {
        Assignment submittedAssignment = assignmentService.submitAssignment(assignment);
        return ResponseEntity.ok(submittedAssignment);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/submitQuiz")
    public ResponseEntity<QuizModel> submitQuiz(@RequestBody QuizModel quiz) {
        QuizModel submittedQuiz = quizService.submitQuiz(quiz);
        return ResponseEntity.ok(submittedQuiz);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/displayCourses")
    public ResponseEntity<List<CourseDTO>> displayCourses() {
        List<CourseDTO> courses = courseService.displayCourses();
        return ResponseEntity.ok(courses);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/attend-lesson")
    public ResponseEntity<String> attendLesson(@RequestParam int studentId,
                                               @RequestParam Long lessonId,
                                               @RequestParam String otp) {
        return ResponseEntity.ok(attendanceService.attendLesson(studentId, lessonId, otp));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/sendByEmail")
    public String sendNotificationByEmail(@RequestBody Map<String, Object> payload) {
        Integer userId = (Integer) payload.get("userId");
        String type = (String) payload.get("type");
        String message = (String) payload.get("message");
        String timestampStr = (String) payload.get("timestamp");
        LocalDateTime timestamp = timestampStr != null ? LocalDateTime.parse(timestampStr) : LocalDateTime.now();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NotificationModel notification = new NotificationModel(user, type, message, timestamp);
        notificationService.sendNotification(notification);

        String subject = "Notification: " + type;
        String emailMessage = "You have a new notification:\n" + message;
        notificationService.sendEmailNotification(user.getEmail(), subject, emailMessage);

        return "Notification sent successfully!";
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{courseId}/materials")
    public ResponseEntity<List<String>> getCourseMaterials(@PathVariable Long courseId) {
        List<String> mediaFiles = courseService.getMediaFilesByCourseId(courseId);
        if (mediaFiles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mediaFiles);
    }
}