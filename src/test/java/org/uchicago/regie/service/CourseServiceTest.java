package org.uchicago.regie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.uchicago.regie.model.CourseEntity;
import org.uchicago.regie.repository.CourseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCourseById() {
        Long courseId = 1L;
        CourseEntity course = new CourseEntity(courseId, 1L, "CS101", "Introduction to Programming", 30);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Optional<CourseEntity> foundCourse = courseService.getCourseById(courseId);

        assertTrue(foundCourse.isPresent(), "Course should be found");
        assertEquals(courseId, foundCourse.get().id(), "Course ID should match");
    }

    @Test
    void getAllCoursesByDepartment() {
        Long departmentId = 1L;
        List<CourseEntity> courses = Arrays.asList(
                new CourseEntity(1L, departmentId, "CS101", "Introduction to Programming", 30),
                new CourseEntity(2L, departmentId, "CS102", "Data Structures", 25)
        );
        when(courseRepository.findByDepartmentId(departmentId)).thenReturn(courses);

        List<CourseEntity> foundCourses = courseService.getAllCoursesByDepartment(departmentId);

        assertFalse(foundCourses.isEmpty(), "Should return courses for the given department ID");
        assertEquals(2, foundCourses.size(), "Should return two courses");
    }

    @Test
    void getCoursesWithOpenSlots() {
        List<CourseEntity> courses = Arrays.asList(new CourseEntity(1L, 1L, "CS103", "Algorithms", 30));
        when(courseRepository.findCoursesWithOpenSlots()).thenReturn(courses);

        List<CourseEntity> foundCourses = courseService.getCoursesWithOpenSlots();

        assertFalse(foundCourses.isEmpty(), "Should return courses with open slots");
    }

    @Test
    void searchCoursesByName() {
        String name = "programming";
        List<CourseEntity> courses = Arrays.asList(new CourseEntity(1L, 1L, "CS101", "Introduction to Programming", 30));
        when(courseRepository.findByNameContainingIgnoreCase(name)).thenReturn(courses);

        List<CourseEntity> foundCourses = courseService.searchCoursesByName(name);

        assertFalse(foundCourses.isEmpty(), "Should return courses containing name ignoring case");
    }

    @Test
    void getCoursesWithPrerequisites() {
        Long courseId = 1L;
        List<CourseEntity> courseWithPrerequisites = Arrays.asList(new CourseEntity(courseId, 1L, "CS102", "Data Structures", 25), null);
        when(courseRepository.findCourseWithPrerequisites(courseId)).thenReturn(courseWithPrerequisites);

        List<CourseEntity> foundCourses = courseService.getCoursesWithPrerequisites(courseId);

        assertFalse(foundCourses.isEmpty(), "Should return courses with their prerequisites");
    }

    @Test
    void addCourse() {
        CourseEntity course = new CourseEntity(null, 1L, "CS104", "Computer Networks", 30);
        when(courseRepository.save(course)).thenReturn(course);

        CourseEntity savedCourse = courseService.addCourse(course);

        assertNotNull(savedCourse, "Saved course should not be null");
    }

    @Test
    void deleteCourseById() {
        Long courseId = 1L;
        doNothing().when(courseRepository).deleteById(courseId);

        courseService.deleteCourseById(courseId);

        verify(courseRepository, times(1)).deleteById(courseId);
    }

    @Test
    void getAllCourses() {
        List<CourseEntity> courses = Arrays.asList(
                new CourseEntity(1L, 1L, "CS101", "Introduction to Programming", 30),
                new CourseEntity(2L, 1L, "CS102", "Data Structures", 25)
        );
        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseEntity> foundCourses = courseService.getAllCourses();

        assertFalse(foundCourses.isEmpty(), "Should return all courses");
        assertEquals(2, foundCourses.size(), "Should return two courses");
    }

    @Test
    void updateCourse() {
        CourseEntity course = new CourseEntity(1L, 1L, "CS101", "Introduction to Programming", 30);
        when(courseRepository.save(course)).thenReturn(course);

        CourseEntity updatedCourse = courseService.updateCourse(course);

        assertNotNull(updatedCourse, "Updated course should not be null");
    }
}
