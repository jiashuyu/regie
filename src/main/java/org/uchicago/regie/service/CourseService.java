package org.uchicago.regie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uchicago.regie.model.CourseEntity;
import org.uchicago.regie.repository.CourseRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Optional<CourseEntity> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<CourseEntity> getAllCoursesByDepartment(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId);
    }

    public List<CourseEntity> getCoursesWithOpenSlots() {
        return courseRepository.findCoursesWithOpenSlots();
    }

    public List<CourseEntity> searchCoursesByName(String name) {
        return courseRepository.findByNameContainingIgnoreCase(name);
    }

    public List<CourseEntity> getCoursesWithPrerequisites(Long courseId) {
        return courseRepository.findCourseWithPrerequisites(courseId);
    }

    public CourseEntity addCourse(CourseEntity course) {
        return courseRepository.save(course);
    }

    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    public List<CourseEntity> getAllCourses() {
        return StreamSupport.stream(courseRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public CourseEntity updateCourse(CourseEntity course) {
        return courseRepository.save(course);
    }

}
