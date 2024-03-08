package org.uchicago.regie.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnrollmentRequestTest {

    @Test
    public void testSetterAndGetterForStudentId() {
        long expectedStudentId = 123L;
        EnrollmentRequest request = new EnrollmentRequest(1L, 2L, "registered", "Spring 2024");
        request.setStudentId(expectedStudentId);

        assertEquals(expectedStudentId, request.getStudentId(), "The getter for studentId should return the value set by the setter.");
    }

    @Test
    public void testSetterAndGetterForCourseId() {
        long expectedCourseId = 456L;
        EnrollmentRequest request = new EnrollmentRequest(1L, 2L, "registered", "Spring 2024");
        request.setCourseId(expectedCourseId);

        assertEquals(expectedCourseId, request.getCourseId(), "The getter for courseId should return the value set by the setter.");
    }

    @Test
    public void testSetterAndGetterForNewStatus() {
        String expectedStatus = "registered";
        EnrollmentRequest request = new EnrollmentRequest(1L, 2L, "registered", "Spring 2024");
        request.setNewStatus(expectedStatus);

        assertEquals(expectedStatus, request.getNewStatus(), "The getter for newStatus should return the value set by the setter.");
    }
}

