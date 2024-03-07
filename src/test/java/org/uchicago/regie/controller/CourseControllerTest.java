package org.uchicago.regie.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.uchicago.regie.model.CourseEntity;
import org.uchicago.regie.service.CourseService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    void getCourseById() throws Exception {
        Long courseId = 1L;
        CourseEntity course = new CourseEntity(courseId, 1L, "CS101", "Introduction to Programming", 30);
        when(courseService.getCourseById(courseId)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/courses/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseId))
                .andExpect(jsonPath("$.name").value("Introduction to Programming"));
    }

    @Test
    void getAllCourses() throws Exception {
        List<CourseEntity> courses = Arrays.asList(new CourseEntity(1L, 1L, "CS101", "Introduction to Programming", 30));
        when(courseService.getAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Introduction to Programming"));
    }

    @Test
    void getCoursesByDepartment() throws Exception {
        Long departmentId = 1L;
        List<CourseEntity> courses = Arrays.asList(new CourseEntity(2L, departmentId, "CS102", "Data Structures", 25));
        when(courseService.getAllCoursesByDepartment(departmentId)).thenReturn(courses);

        mockMvc.perform(get("/courses/department/{departmentId}", departmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Data Structures"));
    }

    @Test
    void addCourse() throws Exception {
        CourseEntity newCourse = new CourseEntity(null, 1L, "CS103", "Algorithms", 25);
        CourseEntity savedCourse = new CourseEntity(3L, 1L, "CS103", "Algorithms", 25);
        when(courseService.addCourse(any(CourseEntity.class))).thenReturn(savedCourse);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"departmentId\":1,\"courseNumber\":\"CS103\",\"name\":\"Algorithms\",\"maxEnrollment\":25}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Algorithms"));
    }

    @Test
    void updateCourse() throws Exception {
        CourseEntity updatedCourse = new CourseEntity(1L, 1L, "CS101", "Intro to Programming Updated", 40);
        when(courseService.updateCourse(any(CourseEntity.class))).thenReturn(updatedCourse);

        mockMvc.perform(post("/courses/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"departmentId\":1,\"courseNumber\":\"CS101\",\"name\":\"Intro to Programming Updated\",\"maxEnrollment\":40}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Intro to Programming Updated"));
    }

    @Test
    void deleteCourse() throws Exception {
        Long courseId = 1L;
        doNothing().when(courseService).deleteCourseById(courseId);

        mockMvc.perform(delete("/courses/{id}", courseId))
                .andExpect(status().isOk());
    }

    @Test
    void getCoursesWithOpenSlots() throws Exception {
        List<CourseEntity> courses = Arrays.asList(new CourseEntity(1L, 1L, "CS104", "Networks", 40));
        when(courseService.getCoursesWithOpenSlots()).thenReturn(courses);

        mockMvc.perform(get("/courses/with-open-slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Networks"));
    }

    @Test
    void searchCoursesByName() throws Exception {
        String name = "programming";
        List<CourseEntity> courses = Arrays.asList(new CourseEntity(1L, 1L, "CS101", "Introduction to Programming", 30));
        when(courseService.searchCoursesByName(name)).thenReturn(courses);

        mockMvc.perform(get("/courses/search")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Introduction to Programming"));
    }
}
