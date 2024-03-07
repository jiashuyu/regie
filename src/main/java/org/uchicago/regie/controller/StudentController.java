package org.uchicago.regie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.StudentEntity;
import org.uchicago.regie.service.EnrollmentService;
import org.uchicago.regie.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public StudentController(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/register")
    public ResponseEntity<StudentEntity> registerStudent(@RequestBody StudentEntity student) {
        StudentEntity newStudent = studentService.createStudent(student);
        return ResponseEntity.ok(newStudent);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentEntity> updateStudent(@PathVariable long studentId, @RequestBody StudentEntity student) {
        // Assuming studentService.updateStudentInformation handles finding and updating the student
        StudentEntity newStudent = new StudentEntity(studentId, student.email(), student.enabled(), student.password(), student.firstName(), student.lastName());
        StudentEntity updatedStudent = studentService.updateStudentInformation(newStudent);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentEntity> getStudentById(@PathVariable long studentId) {
        return studentService.getStudentById(studentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{studentId}/enrollments")
    public ResponseEntity<Void> enrollInCourse(@PathVariable long studentId, @RequestBody EnrollmentEntity enrollment) {
        enrollmentService.registerStudentForCourse(studentId, enrollment.courseId(), enrollment.status());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studentId}/enrollments")
    public ResponseEntity<List<EnrollmentEntity>> getEnrollmentsForStudent(@PathVariable long studentId) {
        List<EnrollmentEntity> enrollments = enrollmentService.getAllEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @DeleteMapping("/{studentId}/enrollments/{courseId}")
    public ResponseEntity<Void> dropCourse(@PathVariable long studentId, @PathVariable long courseId) {
        enrollmentService.dropStudentFromCourse(studentId, courseId);
        return ResponseEntity.ok().build();
    }

}

