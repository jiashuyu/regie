package org.uchicago.regie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.LabEntity;
import org.uchicago.regie.repository.CourseRepository;
import org.uchicago.regie.repository.EnrollmentRepository;
import org.uchicago.regie.repository.LabRepository;
import org.uchicago.regie.repository.PrerequisiteRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {


    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LabRepository labRepository;

    @Mock
    private PrerequisiteRepository prerequisiteRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

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
        String quarter = "Spring 2024";

        when(courseRepository.countEnrollmentsByCourseId(courseId)).thenReturn(0); // No existing enrollments
        when(courseRepository.findMaxEnrollmentByCourseId(courseId)).thenReturn(30); // Max enrollment not reached
        when(prerequisiteRepository.findPrerequisitesByCourseId(courseId)).thenReturn(Collections.emptyList()); // No prerequisites
        when(enrollmentRepository.countEnrollmentsForStudentByQuarter(studentId, quarter)).thenReturn(0); // Student not overloaded
        when(enrollmentRepository.getEnrollmentStatus(studentId, courseId)).thenReturn(null); // Not enrolled yet
        when(labRepository.findByCourseId(courseId)).thenReturn(Optional.of(new LabEntity(2L, courseId, "Lab 101"))); // Lab exists

        enrollmentService.registerStudentForCourse(studentId, courseId, status, quarter);

        verify(enrollmentRepository, times(2)).save(any(EnrollmentEntity.class)); // For both the course and the lab
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
        List<EnrollmentEntity> expected = Arrays.asList(new EnrollmentEntity(null, studentId, 2L, "registered", "Spring 2024"));
        when(enrollmentRepository.findByStudentId(studentId)).thenReturn(expected);

        List<EnrollmentEntity> enrollments = enrollmentService.getAllEnrollmentsByStudentId(studentId);

        assertFalse(enrollments.isEmpty(), "Should return enrollments for the student");
    }

    @Test
    void getAllEnrollmentsByCourseId() {
        long courseId = 1L;
        List<EnrollmentEntity> expected = Arrays.asList(new EnrollmentEntity(null, 1L, courseId, "registered", "Spring 2024"));
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
        List<EnrollmentEntity> expected = Arrays.asList(new EnrollmentEntity(null, 1L, 2L, "pending_approval", "Spring 2024"));
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
