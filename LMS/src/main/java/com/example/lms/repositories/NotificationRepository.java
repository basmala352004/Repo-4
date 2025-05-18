package com.example.lms.repositories;
import com.example.lms.models.NotificationModel;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationModel, Integer> {

    // Custom query to find unread notifications for a user
    List<NotificationModel> findByUserIdAndIsReadFalse(Integer userId);

    // Fetch all notifications for a user
    List<NotificationModel> findByUserId(Integer userId);
}


