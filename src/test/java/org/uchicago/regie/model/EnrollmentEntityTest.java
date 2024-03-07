package org.uchicago.regie.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnrollmentEntityTest {

    @Test
    public void testEnrollmentEntityProperties() {
        Long id = 1L;
        Long studentId = 100L;
        Long courseId = 200L;
        String status = "registered";

        EnrollmentEntity enrollment = new EnrollmentEntity(id, studentId, courseId, status);

        assertEquals(id, enrollment.id(), "The id should match the value provided to the constructor.");
        assertEquals(studentId, enrollment.studentId(), "The studentId should match the value provided to the constructor.");
        assertEquals(courseId, enrollment.courseId(), "The courseId should match the value provided to the constructor.");
        assertEquals(status, enrollment.status(), "The status should match the value provided to the constructor.");
    }

    @Test
    public void testEnrollmentStatusValidValues() {
        EnrollmentEntity registered = new EnrollmentEntity(1L, 100L, 200L, "registered");
        EnrollmentEntity dropped = new EnrollmentEntity(2L, 101L, 201L, "dropped");
        EnrollmentEntity pendingApproval = new EnrollmentEntity(3L, 102L, 202L, "pending_approval");

        assertAll("status",
                () -> assertEquals("registered", registered.status(), "Status should be 'registered'."),
                () -> assertEquals("dropped", dropped.status(), "Status should be 'dropped'."),
                () -> assertEquals("pending_approval", pendingApproval.status(), "Status should be 'pending_approval'.")
        );
    }

}

