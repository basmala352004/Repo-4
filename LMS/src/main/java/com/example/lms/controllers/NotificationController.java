package com.example.lms.controllers;
import com.example.lms.models.NotificationModel;
import com.example.lms.models.User;
import com.example.lms.repositories.UserRepository;
import com.example.lms.services.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Autowired
    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    // Fetch unread notifications for a specific user
    @GetMapping("/unread/{userId}")
    public List<NotificationModel> getUnreadNotifications(@PathVariable Integer userId) {
        return notificationService.fetchUnreadNotifications(userId);
    }
    // Fetch all notifications for a specific user
    @GetMapping("/all/{userId}")
    public List<NotificationModel> getAllNotifications(@PathVariable Integer userId) {
        return notificationService.fetchAllNotifications(userId);
    }
    // Send a new notification
    @PostMapping("/send")
    public String sendNotification(@RequestBody Map<String, Object> payload) {
        Integer userId = (Integer) payload.get("userId");
        String type = (String) payload.get("type");
        String message = (String) payload.get("message");
        String timestampStr = (String) payload.get("timestamp");
        LocalDateTime timestamp = timestampStr != null ? LocalDateTime.parse(timestampStr) : LocalDateTime.now();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        NotificationModel notification = new NotificationModel(user, type, message, timestamp);
        notificationService.sendNotification(notification);
        return "Notification sent successfully!";
    }
    // Mark a notification as read
    @PutMapping("/markAsRead/{notificationID}")
    public String markAsRead(@PathVariable Integer notificationID) {
        notificationService.markAsRead(notificationID);
        return "Notification marked as read!";
    }

    // for bouns
    // Send a new notification (and email)
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

}