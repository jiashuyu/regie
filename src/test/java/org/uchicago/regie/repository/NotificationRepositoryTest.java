package org.uchicago.regie.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.NotificationEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test") // using a test profile that points to a test database
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Reset the ApplicationContext after each test method
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    private NotificationEntity savedNotification;

    @BeforeEach
    void setUp() {
        // Directly save a test notification using the repository
        NotificationEntity notification = new NotificationEntity(null, 1L, "course_added", "You have been enrolled in a course.", false);
        savedNotification = notificationRepository.save(notification);
    }

    @AfterEach
    void tearDown() {
        // Clean up by deleting all notifications
        notificationRepository.deleteAll();
    }

    @Test
    public void findByStudentId_ShouldReturnNotifications() {
        List<NotificationEntity> notifications = notificationRepository.findByStudentId(savedNotification.studentId());
        assertFalse(notifications.isEmpty(), "Should return notifications for the given student ID");
    }

    @Test
    public void findBySentFalse_ShouldReturnUnsentNotifications() {
        List<NotificationEntity> unsentNotifications = notificationRepository.findBySentFalse();
        assertTrue(unsentNotifications.stream().anyMatch(n -> !n.sent()), "Should return unsent notifications");
    }

    @Test
    public void findByType_ShouldReturnNotificationsOfType() {
        List<NotificationEntity> notifications = notificationRepository.findByType("course_added");
        assertTrue(notifications.stream().anyMatch(n -> "course_added".equals(n.type())), "Should return notifications of type 'course_added'");
    }

    @Test
    public void countByTypeAndStudentId_ShouldReturnCount() {
        int count = notificationRepository.countByTypeAndStudentId("course_added", savedNotification.studentId());
        assertTrue(count > 0, "Should return a count greater than 0 for existing type and student ID");
    }

    @Test
    public void deleteByStudentId_ShouldDeleteNotifications() {
        notificationRepository.deleteByStudentId(savedNotification.studentId());
        List<NotificationEntity> notifications = notificationRepository.findByStudentId(savedNotification.studentId());
        assertTrue(notifications.isEmpty(), "Should delete all notifications for the given student ID");
    }

    @Test
    public void markAsSent_ShouldUpdateNotificationToSent() {
        notificationRepository.markAsSent(savedNotification.id());
        Optional<NotificationEntity> updatedNotification = notificationRepository.findById(savedNotification.id());
        assertTrue(updatedNotification.isPresent() && updatedNotification.get().sent(), "Should mark the notification as sent");
    }
}
