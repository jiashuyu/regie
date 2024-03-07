package org.uchicago.regie.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uchicago.regie.model.CourseEntity;
import org.uchicago.regie.service.CourseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseEntity> getCourseById(@PathVariable Long id) {
        Optional<CourseEntity> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CourseEntity>> getAllCourses() {
        List<CourseEntity> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<CourseEntity>> getCoursesByDepartment(@PathVariable Long departmentId) {
        List<CourseEntity> courses = courseService.getAllCoursesByDepartment(departmentId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<CourseEntity> addCourse(@RequestBody CourseEntity courseEntity) {
        CourseEntity savedCourse = courseService.addCourse(courseEntity);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<CourseEntity> updateCourse(@RequestBody CourseEntity course) {
        CourseEntity updatedCourse = courseService.updateCourse(course);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/with-open-slots")
    public ResponseEntity<List<CourseEntity>> getCoursesWithOpenSlots() {
        List<CourseEntity> courses = courseService.getCoursesWithOpenSlots();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseEntity>> searchCoursesByName(@RequestParam String name) {
        List<CourseEntity> courses = courseService.searchCoursesByName(name);
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }

}

