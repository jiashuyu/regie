package org.uchicago.regie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.StudentEntity;
import org.uchicago.regie.repository.EnrollmentRepository;
import org.uchicago.regie.repository.StudentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStudentById() {
        Long studentId = 1L;
        Optional<StudentEntity> expectedStudent = Optional.of(new StudentEntity(studentId, "student@example.com", true, "password", "John", "Doe"));
        when(studentRepository.findById(studentId)).thenReturn(expectedStudent);

        Optional<StudentEntity> foundStudent = studentService.getStudentById(studentId);

        assertTrue(foundStudent.isPresent(), "Student should be found");
        assertEquals(expectedStudent.get().email(), foundStudent.get().email(), "Email should match");
    }

    @Test
    void getStudentByEmail() {
        String email = "student@example.com";
        StudentEntity expectedStudent = new StudentEntity(1L, email, true, "password", "John", "Doe");
        when(studentRepository.getStudentByEmail(email)).thenReturn(expectedStudent);

        StudentEntity foundStudent = studentService.getStudentByEmail(email);

        assertEquals(email, foundStudent.email(), "Email should match");
    }

    @Test
    void registerStudentForCourse() {
        long studentId = 1L, courseId = 1L;
        String status = "registered";
        EnrollmentEntity enrollment = new EnrollmentEntity(null, studentId, courseId, status);

        studentService.registerStudentForCourse(studentId, courseId, status);

        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void updateStudentEnrollmentStatus() {
        long studentId = 1L, courseId = 1L;
        String newStatus = "completed";

        studentService.updateStudentEnrollmentStatus(studentId, courseId, newStatus);

        verify(enrollmentRepository, times(1)).updateEnrollmentStatus(studentId, courseId, newStatus);
    }

    @Test
    void dropCourse() {
        long studentId = 1L, courseId = 1L;

        studentService.dropCourse(studentId, courseId);

        verify(enrollmentRepository, times(1)).deleteByStudentIdAndCourseId(studentId, courseId);
    }

    @Test
    void getStudentEnrollments() {
        long studentId = 1L;
        List<EnrollmentEntity> enrollments = Arrays.asList(new EnrollmentEntity(null, studentId, 2L, "registered"));
        when(enrollmentRepository.findByStudentId(studentId)).thenReturn(enrollments);

        List<EnrollmentEntity> foundEnrollments = studentService.getStudentEnrollments(studentId);

        assertFalse(foundEnrollments.isEmpty(), "Should return enrollments for the student");
    }

    @Test
    void createStudent() {
        StudentEntity student = new StudentEntity(null, "new@example.com", true, "password", "New", "Student");
        when(studentRepository.save(any(StudentEntity.class))).thenReturn(student);

        StudentEntity savedStudent = studentService.createStudent(student);

        assertNotNull(savedStudent, "Saved student should not be null");
    }

    @Test
    void updateStudentInformation() {
        StudentEntity student = new StudentEntity(1L, "update@example.com", true, "newPassword", "Updated", "Student");
        when(studentRepository.save(any(StudentEntity.class))).thenReturn(student);

        StudentEntity updatedStudent = studentService.updateStudentInformation(student);

        assertEquals("Updated", updatedStudent.firstName(), "First name should match the updated value");
    }

    @Test
    void deleteStudent() {
        long studentId = 1L;

        studentService.deleteStudent(studentId);

        verify(enrollmentRepository, times(1)).deleteByStudentId(studentId);
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void existsByEmail() {
        String email = "exists@example.com";
        when(studentRepository.existsByEmail(email)).thenReturn(true);

        boolean exists = studentService.existsByEmail(email);

        assertTrue(exists, "Student should exist by email");
    }

    @Test
    void getAllStudentsByEnabledStatus() {
        List<StudentEntity> students = Arrays.asList(new StudentEntity(1L, "enabled@example.com", true, "password", "Enabled", "Student"));
        when(studentRepository.findByEnabled(true)).thenReturn(students);

        List<StudentEntity> foundStudents = studentService.getAllStudentsByEnabledStatus(true);

        assertFalse(foundStudents.isEmpty(), "Should return enabled students");
    }

    @Test
    void updateStudentStatusByEmail() {
        String email = "status@update.com";
        boolean newStatus = false;

        doNothing().when(studentRepository).updateStudentEnabledStatus(email, newStatus);

        studentService.updateStudentStatusByEmail(email, newStatus);

        verify(studentRepository, times(1)).updateStudentEnabledStatus(email, newStatus);
    }
}
