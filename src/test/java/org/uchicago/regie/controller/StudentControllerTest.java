package org.uchicago.regie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.StudentEntity;
import org.uchicago.regie.service.EnrollmentService;
import org.uchicago.regie.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private EnrollmentService enrollmentService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registerStudent() throws Exception {
        StudentEntity student = new StudentEntity(null, "student@example.com", true, "password", "John", "Doe");
        when(studentService.createStudent(any(StudentEntity.class))).thenReturn(student);

        mockMvc.perform(post("/students/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@example.com"));
    }

    @Test
    void updateStudent() throws Exception {
        long studentId = 1L;
        StudentEntity student = new StudentEntity(studentId, "update@example.com", true, "newPassword", "Updated", "Student");
        when(studentService.updateStudentInformation(any(StudentEntity.class))).thenReturn(student);

        mockMvc.perform(put("/students/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("Updated"));
    }

    @Test
    void getStudentById() throws Exception {
        long studentId = 1L;
        Optional<StudentEntity> student = Optional.of(new StudentEntity(studentId, "student@example.com", true, "password", "John", "Doe"));
        when(studentService.getStudentById(studentId)).thenReturn(student);

        mockMvc.perform(get("/students/{studentId}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@example.com"));
    }

    @Test
    void deleteStudent() throws Exception {
        long studentId = 1L;
        doNothing().when(studentService).deleteStudent(studentId);

        mockMvc.perform(delete("/students/{studentId}", studentId))
                .andExpect(status().isOk());
    }

    @Test
    void enrollInCourse() throws Exception {
        long studentId = 1L;
        EnrollmentEntity enrollment = new EnrollmentEntity(null, studentId, 2L, "registered");
        doNothing().when(enrollmentService).registerStudentForCourse(studentId, enrollment.courseId(), enrollment.status());

        mockMvc.perform(post("/students/{studentId}/enrollments", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollment)))
                .andExpect(status().isOk());
    }

    @Test
    void getEnrollmentsForStudent() throws Exception {
        long studentId = 1L;
        List<EnrollmentEntity> enrollments = Arrays.asList(new EnrollmentEntity(1L, studentId, 2L, "registered"));
        when(enrollmentService.getAllEnrollmentsByStudentId(studentId)).thenReturn(enrollments);

        mockMvc.perform(get("/students/{studentId}/enrollments", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("registered"));
    }

    @Test
    void dropCourse() throws Exception {
        long studentId = 1L;
        long courseId = 2L;
        doNothing().when(enrollmentService).dropStudentFromCourse(studentId, courseId);

        mockMvc.perform(delete("/students/{studentId}/enrollments/{courseId}", studentId, courseId))
                .andExpect(status().isOk());
    }
}
