package org.uchicago.regie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.uchicago.regie.model.NotificationEntity;
import org.uchicago.regie.repository.NotificationRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNotification() {
        NotificationEntity notification = new NotificationEntity(null, 1L, "course_added", "You have been enrolled in a course.", false);
        when(notificationRepository.save(notification)).thenReturn(notification);

        NotificationEntity savedNotification = notificationService.createNotification(notification);

        assertNotNull(savedNotification, "The saved notification should not be null");
    }

    @Test
    void getAllNotificationsByStudentId() {
        long studentId = 1L;
        List<NotificationEntity> notifications = Arrays.asList(new NotificationEntity(null, studentId, "course_added", "Notification message", false));
        when(notificationRepository.findByStudentId(studentId)).thenReturn(notifications);

        List<NotificationEntity> foundNotifications = notificationService.getAllNotificationsByStudentId(studentId);

        assertFalse(foundNotifications.isEmpty(), "Should return notifications for the given student ID");
    }

    @Test
    void getUnsentNotifications() {
        List<NotificationEntity> unsentNotifications = Arrays.asList(new NotificationEntity(null, 1L, "course_added", "Notification message", false));
        when(notificationRepository.findBySentFalse()).thenReturn(unsentNotifications);

        List<NotificationEntity> foundUnsentNotifications = notificationService.getUnsentNotifications();

        assertFalse(foundUnsentNotifications.isEmpty(), "Should return unsent notifications");
    }

    @Test
    void markNotificationAsSent() {
        long notificationId = 1L;
        doNothing().when(notificationRepository).markAsSent(notificationId);

        notificationService.markNotificationAsSent(notificationId);

        verify(notificationRepository, times(1)).markAsSent(notificationId);
    }

    @Test
    void getNotificationsByType() {
        String type = "course_added";
        List<NotificationEntity> notifications = Arrays.asList(new NotificationEntity(null, 1L, type, "Notification message", false));
        when(notificationRepository.findByType(type)).thenReturn(notifications);

        List<NotificationEntity> foundNotifications = notificationService.getNotificationsByType(type);

        assertFalse(foundNotifications.isEmpty(), "Should return notifications of the specified type");
    }

    @Test
    void deleteNotificationsByStudentId() {
        long studentId = 1L;
        doNothing().when(notificationRepository).deleteByStudentId(studentId);

        notificationService.deleteNotificationsByStudentId(studentId);

        verify(notificationRepository, times(1)).deleteByStudentId(studentId);
    }

    @Test
    void countNotificationsByTypeAndStudentId() {
        String type = "course_added";
        long studentId = 1L;
        int expectedCount = 1;
        when(notificationRepository.countByTypeAndStudentId(type, studentId)).thenReturn(expectedCount);

        int count = notificationService.countNotificationsByTypeAndStudentId(type, studentId);

        assertEquals(expectedCount, count, "Should return the correct count of notifications by type and student ID");
    }

    @Test
    void processAndSendUnsentNotifications() {
        List<NotificationEntity> unsentNotifications = Arrays.asList(new NotificationEntity(1L, 1L, "course_added", "Notification message", false));
        when(notificationRepository.findBySentFalse()).thenReturn(unsentNotifications);

        notificationService.processAndSendUnsentNotifications();

        verify(notificationRepository, times(1)).markAsSent(1L);
    }
}

