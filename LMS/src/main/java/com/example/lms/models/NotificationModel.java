package com.example.lms.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class NotificationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationID;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String type;
    @org.jetbrains.annotations.NotNull
    private String message;
    private LocalDateTime timestamp;
    private Boolean isRead;

    public NotificationModel() {}
    public NotificationModel(User user, String type, String message, LocalDateTime timestamp) {
        this.user = user;
        this.type = type;
        this.message = message;
        this.timestamp = (timestamp != null) ? timestamp : LocalDateTime.now();
        this.isRead = false;
    }

    // Getters and Setters
    public Integer getNotificationID() {
        return notificationID;
    }
    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public Boolean getIsRead() {
        return isRead;
    }
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
