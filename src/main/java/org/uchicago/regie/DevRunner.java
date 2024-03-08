package org.uchicago.regie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.StudentEntity;
import org.uchicago.regie.service.EnrollmentService;
import org.uchicago.regie.service.StudentService;

import java.util.List;

@Component
public class DevRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevRunner.class);

    private StudentService studentService;
    private EnrollmentService enrollmentService;

    public DevRunner(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Create a new student
        StudentEntity student = studentService.createStudent(new StudentEntity(null, "john.doe@example.com", true, "password", "John", "Doe"));
        logger.info("Registered new student: " + student.email());

        // Enroll the student in a course
        long courseId = 1L; // Assuming a course with ID 1 exists
        enrollmentService.registerStudentForCourse(student.id(), courseId, "registered", "Spring 2024");
        logger.info("Enrolled student in course with ID: " + courseId);

        // Update enrollment status
        enrollmentService.updateEnrollmentStatus(student.id(), courseId, "completed");
        logger.info("Updated student's enrollment status to 'completed' for course ID: " + courseId);

        // Retrieve and display enrollments for the student
        List<EnrollmentEntity> enrollments = enrollmentService.getAllEnrollmentsByStudentId(student.id());
        logger.info("Enrollments for " + student.email() + ":");
        for (EnrollmentEntity enrollment : enrollments) {
            logger.info("Course ID: " + enrollment.courseId() + ", Status: " + enrollment.status());
        }
    }
}