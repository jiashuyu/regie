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
        String quarter = "Spring 2024";

        EnrollmentEntity enrollment = new EnrollmentEntity(id, studentId, courseId, status, quarter);

        assertEquals(id, enrollment.id(), "The id should match the value provided to the constructor.");
        assertEquals(studentId, enrollment.studentId(), "The studentId should match the value provided to the constructor.");
        assertEquals(courseId, enrollment.courseId(), "The courseId should match the value provided to the constructor.");
        assertEquals(status, enrollment.status(), "The status should match the value provided to the constructor.");
    }

    @Test
    public void testEnrollmentStatusValidValues() {
        EnrollmentEntity registered = new EnrollmentEntity(1L, 100L, 200L, "registered", "Spring 2024");
        EnrollmentEntity dropped = new EnrollmentEntity(2L, 101L, 201L, "dropped", "Spring 2024");
        EnrollmentEntity pendingApproval = new EnrollmentEntity(3L, 102L, 202L, "pending_approval", "Spring 2024");

        assertAll("status",
                () -> assertEquals("registered", registered.status(), "Status should be 'registered'."),
                () -> assertEquals("dropped", dropped.status(), "Status should be 'dropped'."),
                () -> assertEquals("pending_approval", pendingApproval.status(), "Status should be 'pending_approval'.")
        );
    }

}

