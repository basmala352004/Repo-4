package com.example.lms.services;

import com.example.lms.models.NotificationModel;
import com.example.lms.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {


    private final NotificationRepository notificationRepository;
// for bouns

    private final JavaMailSender mailSender;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    // Fetch unread notifications for a specific user
    public List<NotificationModel> fetchUnreadNotifications(Integer userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    // Fetch all notifications for a specific user
    public List<NotificationModel> fetchAllNotifications(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    // Send or save a new notification
    public void sendNotification(NotificationModel notification) {
        notificationRepository.save(notification);
    }

    public void markAsRead(Integer notificationID) {
        NotificationModel notification = notificationRepository.findById(notificationID).orElse(null);
        if (notification != null) {
            notification.setIsRead(true);  // Mark the notification as read
            notificationRepository.save(notification);  // Save the updated notification to the database
        }
    }

    // Bouns
// Send email notification
    public void sendEmailNotification(String recipientEmail, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

}