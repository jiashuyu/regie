package org.uchicago.regie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.EnrollmentRequest;
import org.uchicago.regie.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerForCourse(@RequestBody EnrollmentRequest request) {
        enrollmentService.registerStudentForCourse(request.getStudentId(), request.getCourseId(), "registered", request.getQuarter());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<Void> updateEnrollmentStatus(@RequestBody EnrollmentRequest request) {
        enrollmentService.updateEnrollmentStatus(request.getStudentId(), request.getCourseId(), request.getNewStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentEntity>> getEnrollmentsByStudent(@PathVariable long studentId) {
        List<EnrollmentEntity> enrollments = enrollmentService.getAllEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentEntity>> getEnrollmentsByCourse(@PathVariable long courseId) {
        List<EnrollmentEntity> enrollments = enrollmentService.getAllEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @DeleteMapping("/drop")
    public ResponseEntity<Void> dropCourse(@RequestBody EnrollmentRequest request) {
        enrollmentService.dropStudentFromCourse(request.getStudentId(), request.getCourseId());
        return ResponseEntity.ok().build();
    }

}
