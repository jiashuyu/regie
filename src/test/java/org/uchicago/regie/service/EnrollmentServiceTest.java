package org.uchicago.regie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.LabEntity;
import org.uchicago.regie.repository.EnrollmentRepository;
import org.uchicago.regie.repository.LabRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private LabRepository labRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Captor
    private ArgumentCaptor<EnrollmentEntity> enrollmentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEnrollmentStatus() {
        long studentId = 1L, courseId = 1L;
        String expectedStatus = "registered";
        when(enrollmentRepository.getEnrollmentStatus(studentId, courseId)).thenReturn(expectedStatus);

        String status = enrollmentService.getEnrollmentStatus(studentId, courseId);

        assertEquals(expectedStatus, status, "The enrollment status should match the expected value.");
    }

    @Test
    void registerStudentForCourse_NewEnrollment() {
        long studentId = 1L, courseId = 1L;
        String status = "registered";
        Optional<LabEntity> optionalLabEntity = Optional.of(new LabEntity(2L, courseId, "Lab 101"));

        when(enrollmentRepository.getEnrollmentStatus(studentId, courseId)).thenReturn(null);
        when(labRepository.findByCourseId(courseId)).thenReturn(optionalLabEntity);

        enrollmentService.registerStudentForCourse(studentId, courseId, status);

        // Verify the save method is called at least once without specifying the arguments
        verify(enrollmentRepository, atLeastOnce()).save(any(EnrollmentEntity.class));

        // If you specifically expect the save method to be called twice (once for the course enrollment, once for the lab enrollment),
        // then use times(2) instead of atLeastOnce().
        verify(enrollmentRepository, times(2)).save(any(EnrollmentEntity.class));
    }

    @Test
    void updateEnrollmentStatus() {
        long studentId = 1L, courseId = 1L;
        String newStatus = "dropped";

        enrollmentService.updateEnrollmentStatus(studentId, courseId, newStatus);

        verify(enrollmentRepository, times(1)).updateEnrollmentStatus(studentId, courseId, newStatus);
    }

    @Test
    void getAllEnrollmentsByStudentId() {
        long studentId = 1L;
        List<EnrollmentEntity> expected = Arrays.asList(new EnrollmentEntity(null, studentId, 2L, "registered"));
        when(enrollmentRepository.findByStudentId(studentId)).thenReturn(expected);

        List<EnrollmentEntity> enrollments = enrollmentService.getAllEnrollmentsByStudentId(studentId);

        assertFalse(enrollments.isEmpty(), "Should return enrollments for the student");
    }

    @Test
    void getAllEnrollmentsByCourseId() {
        long courseId = 1L;
        List<EnrollmentEntity> expected = Arrays.asList(new EnrollmentEntity(null, 1L, courseId, "registered"));
        when(enrollmentRepository.findByCourseId(courseId)).thenReturn(expected);

        List<EnrollmentEntity> enrollments = enrollmentService.getAllEnrollmentsByCourseId(courseId);

        assertFalse(enrollments.isEmpty(), "Should return enrollments for the course");
    }

    @Test
    void dropStudentFromCourse() {
        long studentId = 1L, courseId = 1L;

        enrollmentService.dropStudentFromCourse(studentId, courseId);

        verify(enrollmentRepository, times(1)).updateEnrollmentStatus(studentId, courseId, "dropped");
    }

    @Test
    void dropStudentFromCourse_WithAssociatedLab() {
        long studentId = 1L, courseId = 1L, labId = 2L;

        // Mock the scenario where a lab is associated with the course
        when(labRepository.findByCourseId(courseId)).thenReturn(Optional.of(new LabEntity(labId, courseId, "Lab 101")));

        // Execute the method under test
        enrollmentService.dropStudentFromCourse(studentId, courseId);

        // Verify the student's course enrollment status is updated to "dropped"
        verify(enrollmentRepository, times(1)).updateEnrollmentStatus(studentId, courseId, "dropped");

        // Verify the student is dropped from the associated lab as well
        verify(enrollmentRepository, times(1)).deleteByStudentIdAndCourseId(studentId, labId);
    }

    @Test
    void findPendingApprovalEnrollments() {
        List<EnrollmentEntity> expected = Arrays.asList(new EnrollmentEntity(null, 1L, 2L, "pending_approval"));
        when(enrollmentRepository.findPendingApprovals()).thenReturn(expected);

        List<EnrollmentEntity> enrollments = enrollmentService.findPendingApprovalEnrollments();

        assertFalse(enrollments.isEmpty(), "Should return pending approval enrollments");
    }

    @Test
    void deleteEnrollment() {
        long studentId = 1L, courseId = 1L;

        enrollmentService.deleteEnrollment(studentId, courseId);

        verify(enrollmentRepository, times(1)).deleteByStudentIdAndCourseId(studentId, courseId);
    }
}
