package com.example.lms;
import com.example.lms.models.NotificationModel;
import com.example.lms.repositories.NotificationRepository;
import com.example.lms.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
 class NotificationTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private NotificationModel notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        notification = new NotificationModel();
        notification.setNotificationID(1);
        notification.setMessage("Test notification");
        notification.setIsRead(false);
    }

    @Test
    void testFetchUnreadNotifications() {
        // Mock the repository method
        when(notificationRepository.findByUserIdAndIsReadFalse(1)).thenReturn(Arrays.asList(notification));

        // Call the service method
        List<NotificationModel> unreadNotifications = notificationService.fetchUnreadNotifications(1);

        // Assert the result
        assertNotNull(unreadNotifications);
        assertEquals(1, unreadNotifications.size());
        assertEquals("Test notification", unreadNotifications.get(0).getMessage());

        // Verify repository interaction
        verify(notificationRepository, times(1)).findByUserIdAndIsReadFalse(1);
    }

    @Test
    void testFetchAllNotifications() {
        // Mock the repository method
        when(notificationRepository.findByUserId(1)).thenReturn(Arrays.asList(notification));

        // Call the service method
        List<NotificationModel> allNotifications = notificationService.fetchAllNotifications(1);

        // Assert the result
        assertNotNull(allNotifications);
        assertEquals(1, allNotifications.size());
        assertEquals("Test notification", allNotifications.get(0).getMessage());

        // Verify repository interaction
        verify(notificationRepository, times(1)).findByUserId(1);
    }

    @Test
    void testSendNotification() {
        // Call the service method
        notificationService.sendNotification(notification);

        // Verify repository interaction
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testMarkAsRead() {
        // Mock the repository method to return the notification
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));

        // Call the service method
        notificationService.markAsRead(1);

        // Assert that the notification is marked as read
        assertTrue(notification.getIsRead());

        // Verify repository interaction
        verify(notificationRepository, times(1)).save(notification);
    }
}
