package org.uchicago.regie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.EnrollmentRequest;
import org.uchicago.regie.service.EnrollmentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerForCourse() throws Exception {
        EnrollmentRequest request = new EnrollmentRequest(1L, 2L, "registered");

        mockMvc.perform(post("/enrollments/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(enrollmentService, times(1)).registerStudentForCourse(1L, 2L, "registered");
    }

    @Test
    void updateEnrollmentStatus() throws Exception {
        EnrollmentRequest request = new EnrollmentRequest(1L, 2L, "completed");

        mockMvc.perform(put("/enrollments/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(enrollmentService, times(1)).updateEnrollmentStatus(1L, 2L, "completed");
    }

    @Test
    void getEnrollmentsByStudent() throws Exception {
        List<EnrollmentEntity> enrollments = Arrays.asList(new EnrollmentEntity(1L, 1L, 2L, "registered"));
        when(enrollmentService.getAllEnrollmentsByStudentId(1L)).thenReturn(enrollments);

        mockMvc.perform(get("/enrollments/student/{studentId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("registered"));

        verify(enrollmentService, times(1)).getAllEnrollmentsByStudentId(1L);
    }

    @Test
    void getEnrollmentsByCourse() throws Exception {
        List<EnrollmentEntity> enrollments = Arrays.asList(new EnrollmentEntity(1L, 1L, 2L, "registered"));
        when(enrollmentService.getAllEnrollmentsByCourseId(2L)).thenReturn(enrollments);

        mockMvc.perform(get("/enrollments/course/{courseId}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("registered"));

        verify(enrollmentService, times(1)).getAllEnrollmentsByCourseId(2L);
    }

    @Test
    void dropCourse() throws Exception {
        EnrollmentRequest request = new EnrollmentRequest(1L, 2L, "registered");

        mockMvc.perform(delete("/enrollments/drop")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(enrollmentService, times(1)).dropStudentFromCourse(1L, 2L);
    }

}
