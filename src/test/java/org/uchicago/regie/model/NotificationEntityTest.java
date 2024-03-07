package org.uchicago.regie.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationEntityTest {

    @Test
    public void testNotificationEntityProperties() {
        Long id = 1L;
        Long studentId = 100L;
        String type = "course_added";
        String message = "You have been added to the course successfully.";
        Boolean sent = false;

        NotificationEntity notification = new NotificationEntity(id, studentId, type, message, sent);

        assertEquals(id, notification.id(), "The id should match the value provided to the constructor.");
        assertEquals(studentId, notification.studentId(), "The studentId should match the value provided to the constructor.");
        assertEquals(type, notification.type(), "The type should match the value provided to the constructor.");
        assertEquals(message, notification.message(), "The message should match the value provided to the constructor.");
        assertEquals(sent, notification.sent(), "The sent status should match the value provided to the constructor.");
    }

    @Test
    public void testNotificationSentStatus() {
        NotificationEntity sentNotification = new NotificationEntity(1L, 100L, "course_added", "Your course has been added.", true);
        NotificationEntity unsentNotification = new NotificationEntity(2L, 101L, "course_dropped", "Your course has been dropped.", false);

        assertTrue(sentNotification.sent(), "The sent status should be true for sent notifications.");
        assertFalse(unsentNotification.sent(), "The sent status should be false for unsent notifications.");
    }

}
