package org.uchicago.regie.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.StudentEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test") // using a test profile that points to a test database
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Reset the ApplicationContext after each test method
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private StudentEntity savedStudent;

    @BeforeEach
    void setUp() {
        // Set up a test student before each test
        StudentEntity student = new StudentEntity(null, "test@example.com", true, "password123", "Test", "Student");
        savedStudent = studentRepository.save(student);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        studentRepository.delete(savedStudent);
    }

    @Test
    public void whenGetStudentByEmail_thenReturnsStudent() {
        StudentEntity foundStudent = studentRepository.getStudentByEmail(savedStudent.email());
        assertNotNull(foundStudent, "Should find student by email");
        assertEquals(savedStudent.email(), foundStudent.email(), "The emails should match");
    }

    @Test
    public void whenFindByEnabled_thenReturnStudents() {
        List<StudentEntity> enabledStudents = studentRepository.findByEnabled(true);
        assertFalse(enabledStudents.isEmpty(), "Should return enabled students");
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        boolean exists = studentRepository.existsByEmail(savedStudent.email());
        assertTrue(exists, "Student should exist by email");
    }

    @Test
    public void whenUpdateStudentEnabledStatus_thenStatusIsUpdated() {
        studentRepository.updateStudentEnabledStatus(savedStudent.email(), false);
        StudentEntity updatedStudent = studentRepository.getStudentByEmail(savedStudent.email());
        assertFalse(updatedStudent.enabled(), "The enabled status should be updated to false");
    }

    @Test
    public void whenUpdateStudentPassword_thenPasswordIsUpdated() {
        String newPassword = "newPassword123";
        studentRepository.updateStudentPassword(savedStudent.email(), newPassword);
        StudentEntity updatedStudent = studentRepository.getStudentByEmail(savedStudent.email());
        assertEquals(newPassword, updatedStudent.password(), "The password should be updated");
    }
}
