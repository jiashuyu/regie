package org.uchicago.regie.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.CourseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void whenFindById_thenReturnCourse() {
        Long courseId = 1L;
        Optional<CourseEntity> found = courseRepository.findCourseById(courseId);

        assertTrue(found.isPresent(), "Course should be found by ID");
        assertEquals(courseId, found.get().id(), "Course ID should match the query parameter");
    }

    @Test
    public void whenFindByDepartmentId_thenReturnCourses() {
        Long departmentId = 1L;
        List<CourseEntity> courses = courseRepository.findByDepartmentId(departmentId);

        assertFalse(courses.isEmpty(), "Should find courses by department ID");
    }

    @Test
    public void whenFindCoursesWithOpenSlots_thenReturnCourses() {
        List<CourseEntity> courses = courseRepository.findCoursesWithOpenSlots();
        assertFalse(courses.isEmpty(), "Should find courses with open slots");
    }

    @Test
    public void whenFindByNameContainingIgnoreCase_thenReturnCourses() {
        String name = "java";
        List<CourseEntity> courses = courseRepository.findByNameContainingIgnoreCase(name);
        assertFalse(courses.isEmpty(), "Should find courses containing name ignoring case");
    }

}


