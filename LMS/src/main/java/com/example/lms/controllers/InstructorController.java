package com.example.lms.controllers;

import com.example.lms.models.*;

import com.example.lms.repositories.UserRepository;
import com.example.lms.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/instructor")
public class InstructorController

{

    private final CourseService courseService;
    private final LessonService lessonService;
    private final QuizService quizService;
    private final AssignmentService assignmentService;
    private final AttendanceService attendanceService;
    private final NotificationService notificationService;
    private final TrackPerformanceService trackPerformanceService;

     UserRepository userRepository;


    private static final String UPLOAD_DIRECTORY = "C:/uploads/";


    public InstructorController(CourseService courseService, LessonService lessonService,QuizService quizService,AssignmentService assignmentService,AttendanceService attendanceService,NotificationService notificationService,TrackPerformanceService trackPerformanceService ){
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.quizService = quizService;
        this.assignmentService = assignmentService;
        this.attendanceService = attendanceService;
        this.notificationService = notificationService;
        this.trackPerformanceService = trackPerformanceService;

    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/createCourse")
    public ResponseEntity<String> createCourse(@RequestBody CourseModel course) {
        if (course.getListLessons() == null) {
            course.setListLessons(new ArrayList<>());
        }
        if (course.getMediaFiles() == null) {
            course.setMediaFiles(new ArrayList<>());
        }

        courseService.createCourse(course);  // Create the course using the service
        return ResponseEntity.ok("Course created successfully");
    }
    // manages course content
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/{courseId}/update")
    public ResponseEntity<String> updateCourse(@PathVariable Long courseId, @RequestBody CourseModel updatedCourse) {
        courseService.updateCourseDetails(courseId, updatedCourse);
        return ResponseEntity.ok("Course updated successfully");
    }
    //removes students from courses.
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{courseId}/deleteStudent/{studentId}")
    public ResponseEntity<String> deleteEnrollStudent(@PathVariable Long courseId, @PathVariable Integer studentId) {
        courseService.deleteStudentFromCourse(courseId, studentId);
        return ResponseEntity.ok("Student deleted successfully");
    }
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{courseId}/deleteAllStudents")
    public ResponseEntity<String> deleteAllStudents(@PathVariable Long courseId) {
        courseService.deleteAllStudentsFromCourse(courseId);
        return ResponseEntity.ok("All students deleted successfully");
    }
    // can upload media files
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{courseId}/upload-media")
    public ResponseEntity<String> uploadMedia(@PathVariable String courseId, @RequestParam("file") MultipartFile file) {
        try {

            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists()&& !uploadDir.mkdirs()) {
                    return ResponseEntity.status(500).body("Failed to create upload directory.");
            }

            // Save the file to the Path
            String filePath = UPLOAD_DIRECTORY + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            // Add the file path
            courseService.addMediaFile(courseId, filePath);

            return ResponseEntity.ok("Media file uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File upload failed: " + e.getMessage());
        }

    }
    //quizses and assignments
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/createQuiz")

    public ResponseEntity<String> createQuiz(@RequestBody QuizModel quiz) {
        quizService.createQuiz(quiz);
        return ResponseEntity.ok("Quiz created successfully");
    }
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{quizId}/addQuestion")
    public ResponseEntity<String> addQuestion(@PathVariable Long quizId, @RequestBody QuestionModel question) {
        Optional<QuizModel> quiz = quizService.getQuizById(quizId);

        if (quiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Quiz with ID " + quizId + " not found.");
        }

        quizService.addQuestionToQuiz(quizId, question);
        return ResponseEntity.ok("Question added to quiz");
    }


    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{quizId}/grade")
    public ResponseEntity<QuizModel> gradeQuiz(
            @PathVariable long quizId,
            @RequestParam double grade,
            @RequestParam String feedback)
    {
        return ResponseEntity.ok(quizService.gradeQuiz(quizId, grade));
    }
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/createAssignment")
    public ResponseEntity<String> createAssignment(@RequestBody Assignment assignment) {
        assignmentService.createAssignment(assignment);
        return ResponseEntity.ok("Assignment created successfully");
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/{assignmentId}/grade")
    public ResponseEntity<Assignment> gradeAssignment(
            @PathVariable Integer assignmentId,
            @RequestParam double grade,
            @RequestParam String feedback) {
        return ResponseEntity.ok(assignmentService.gradeAssignment(assignmentId, grade, feedback));
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

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/{quizId}/randomQuestions")
    public ResponseEntity<List<QuestionModel>> getRandomQuestions(@PathVariable Long quizId, @RequestParam int numberOfQuestions) {
        Optional<QuizModel> quiz = quizService.getQuizById(quizId);

        if (quiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }

        List<QuestionModel> questions = quizService.getRandomQuestions(quizId, numberOfQuestions);
        return ResponseEntity.ok(questions);
    }
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PostMapping("/sendByEmail")
    public String sendNotificationByEmail(@RequestBody Map<String, Object> payload) {
        // Extract values from payload
        Integer userId = (Integer) payload.get("userId");
        String type = (String) payload.get("type");
        String message = (String) payload.get("message");
        String timestampStr = (String) payload.get("timestamp");
        LocalDateTime timestamp = timestampStr != null ? LocalDateTime.parse(timestampStr) : LocalDateTime.now();

        // Find the user by ID
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new notification object
        NotificationModel notification = new NotificationModel(user, type, message, timestamp);

        // Save notification to the database
        notificationService.sendNotification(notification);

        // Send email notification
        // Prepare the subject and message for the email
        String subject = "Notification: " + type; // You can customize the subject as needed
        String emailMessage = "You have a new notification:\n" + message; // Customize the email message

        // Send email using the service
        notificationService.sendEmailNotification(user.getEmail(), subject, emailMessage);

        return "Notification sent successfully!";
    }
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PostMapping(value = "/track/getPerformanceForCourses")
    public ResponseEntity<List<Map<String, Object>>> getPerformanceForCourses(
            @RequestBody TrackPerformanceController.PerformanceRequest request) {
        List<Map<String, Object>> performanceDetails = trackPerformanceService.getPerformanceForCourses(
                request.getCourseNames(), request.getLessonName());
        return ResponseEntity.ok(performanceDetails);
    }

    // Endpoint for fetching assignment grades and feedback
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping(value = "/getAssignmentGrades/{assignmentId}")
    public ResponseEntity<Map<String, Object>> getAssignmentGrades(@PathVariable Integer assignmentId) {
        Map<String, Object> assignmentGrades = trackPerformanceService.getAssignmentGrades(assignmentId);
        return ResponseEntity.ok(assignmentGrades);
    }
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping(value = "/getQuizGrades/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuizGrades(@PathVariable long quizId) {
        Map<String, Object> quizGrades = trackPerformanceService.getQuizGrades(quizId);
        return ResponseEntity.ok(quizGrades);
    }
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PostMapping("/generateOTP")
    public ResponseEntity<String> generateOTP(@RequestParam String otp, @RequestParam long lessonId) {
        return ResponseEntity.ok(lessonService.generateOTP(otp, lessonId));
    }
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentModel>> getEnrolledStudents(@PathVariable Long courseId) {
        List<StudentModel> students = courseService.getStudentsByCourseId(courseId);
        if (students.isEmpty()) {
            return ResponseEntity.status(404).body(null); // Return 404 if no students are enrolled
        }
        return ResponseEntity.ok(students); // Return the list of students enrolled in the course
    }



}
