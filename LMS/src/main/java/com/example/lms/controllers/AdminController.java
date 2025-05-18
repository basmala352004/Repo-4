package com.example.lms.controllers;

import com.example.lms.models.NotificationModel;
import com.example.lms.models.StudentModel;
import com.example.lms.models.User;
import com.example.lms.repositories.UserRepository;
import com.example.lms.services.CourseService;
import com.example.lms.services.NotificationService;
import com.example.lms.services.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@PreAuthorize("hasRole('ADMIN')")

@RestController
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CourseService courseService;

    UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, NotificationService notificationService, CourseService courseService, UserRepository userRepository) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.courseService = courseService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers(Authentication authentication) {

        return userService.getAllUsers();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentModel>> getEnrolledStudents(@PathVariable Long courseId) {
        List<StudentModel> students = courseService.getStudentsByCourseId(courseId);
        if (students.isEmpty()) {
            return ResponseEntity.status(404).body(null); // Return 404 if no students are enrolled
        }
        return ResponseEntity.ok(students); // Return the list of students enrolled in the course
    }

    @DeleteMapping("/admin/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


}
