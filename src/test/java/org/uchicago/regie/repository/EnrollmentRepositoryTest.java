package org.uchicago.regie.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.EnrollmentEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test") // using a test profile that points to a test database
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Reset the ApplicationContext after each test method
public class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    public void testUpdateEnrollmentStatus() {
        Long studentId = 1L;
        Long courseId = 1L;
        String newStatus = "completed";

        enrollmentRepository.updateEnrollmentStatus(studentId, courseId, newStatus);
        String status = enrollmentRepository.getEnrollmentStatus(studentId, courseId);
        assertEquals(newStatus, status, "The enrollment status should be updated.");
    }

    @Test
    public void testGetEnrollmentStatus() {
        Long studentId = 1L;
        Long courseId = 1L;

        String status = enrollmentRepository.getEnrollmentStatus(studentId, courseId);
        assertNotNull(status, "Should return an enrollment status.");
    }

    @Test
    public void testFindByStudentId() {
        Long studentId = 1L;

        List<EnrollmentEntity> enrollments = enrollmentRepository.findByStudentId(studentId);
        assertFalse(enrollments.isEmpty(), "Should find enrollments by student ID.");
    }

    @Test
    public void testFindByCourseId() {
        Long courseId = 1L;

        List<EnrollmentEntity> enrollments = enrollmentRepository.findByCourseId(courseId);
        assertFalse(enrollments.isEmpty(), "Should find enrollments by course ID.");
    }

    @Test
    public void testCountByCourseId() {
        Long courseId = 1L;

        int count = enrollmentRepository.countByCourseId(courseId);
        assertTrue(count > 0, "Should count enrollments by course ID.");
    }

    @Test
    public void testFindPendingApprovals() {
        List<EnrollmentEntity> pendingApprovals = enrollmentRepository.findPendingApprovals();
        assertFalse(pendingApprovals.isEmpty(), "Should find enrollments with status 'pending_approval'.");
    }

    @Test
    public void testDeleteByStudentIdAndCourseId() {
        Long studentId = 1L;
        Long courseId = 1L;

        enrollmentRepository.deleteByStudentIdAndCourseId(studentId, courseId);
        String status = enrollmentRepository.getEnrollmentStatus(studentId, courseId);
        assertNull(status, "The enrollment should be deleted.");
    }

    @Test
    public void testDeleteByStudentId() {
        Long studentId = 1L;

        enrollmentRepository.deleteByStudentId(studentId);
        List<EnrollmentEntity> enrollments = enrollmentRepository.findByStudentId(studentId);
        assertTrue(enrollments.isEmpty(), "All enrollments for the student should be deleted.");
    }
}
