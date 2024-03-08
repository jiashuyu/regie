package org.uchicago.regie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.uchicago.regie.model.CourseEntity;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.NotificationEntity;
import org.uchicago.regie.model.StudentEntity;
import org.uchicago.regie.service.CourseService;
import org.uchicago.regie.service.EnrollmentService;
import org.uchicago.regie.service.NotificationService;
import org.uchicago.regie.service.StudentService;

import java.util.List;

@Component
public class DevRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevRunner.class);

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final NotificationService notificationService;

    public DevRunner(StudentService studentService, EnrollmentService enrollmentService,
                     CourseService courseService, NotificationService notificationService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.notificationService = notificationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ------------ Demonstrate major functions in the REGIE system -------------
        // Create a new student
        StudentEntity student = studentService.createStudent(new StudentEntity(null, "jane.doe@example.com", true, "securePassword", "Jane", "Doe"));
        logger.info("Registered new student: " + student.email());

        // Create and list courses
        courseService.addCourse(new CourseEntity(null, 1L, "CS101", "Introduction to Computer Science", 40));
        courseService.addCourse(new CourseEntity(null, 1L, "CS102", "Data Structures and Algorithms", 30));

        // List all courses available
        List<CourseEntity> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            logger.info("No courses found.");
        } else {
            courses.forEach(course -> logger.info("Available Course: " + course.name() + " (" + course.courseNumber() + ")"));
        }

        // Check courses with open slots
        List<CourseEntity> openSlotCourses = courseService.getCoursesWithOpenSlots();
        if (openSlotCourses.isEmpty()) {
            logger.info("No courses with open slots found.");
        } else {
            openSlotCourses.forEach(course -> logger.info("Course with Open Slots: " + course.name()));
        }

        // Enrollment when prerequisites are not fulfilled
        long prereqCourseId = 2L;
        try {
            logger.info("Attempting to enroll student in Course ID " + prereqCourseId + " with prerequisites...");
            enrollmentService.registerStudentForCourse(1L, prereqCourseId, "registered", "Fall 2024");
        } catch (IllegalStateException e) {
            logger.info("Enrollment failed: " + e.getMessage());
        }

        // Enroll the student in a course
        long courseId = courseService.getAllCourses().get(0).id(); // Use the first course's ID
        enrollmentService.registerStudentForCourse(student.id(), courseId, "registered", "Fall 2024");
        logger.info("Enrolled student in course with ID: " + courseId);

        // Attempt to enroll in a course that exceeds the student's course load limit
        try {
            enrollmentService.registerStudentForCourse(student.id(), courseService.getAllCourses().get(1).id(), "registered", "Fall 2024");
        } catch (IllegalStateException e) {
            logger.info("Failed to enroll in additional course: " + e.getMessage());
        }

        // Update enrollment status to 'completed'
        enrollmentService.updateEnrollmentStatus(student.id(), courseId, "completed");
        logger.info("Updated student's enrollment status to 'completed' for course ID: " + courseId);

        // Create and send a notification
        NotificationEntity notification = notificationService.createNotification(new NotificationEntity(null, student.id(), "Welcome", "Welcome to the system, " + student.firstName() + "!", false));
        notificationService.markNotificationAsSent(notification.id());
        logger.info("Sent welcome notification to " + student.email());

        // Retrieve and display all notifications for the student
        List<NotificationEntity> notifications = notificationService.getAllNotificationsByStudentId(student.id());
        logger.info("Notifications for " + student.email() + ":");
        notifications.forEach(n -> logger.info(n.message()));

        // Retrieve and log all enrollments for the student
        List<EnrollmentEntity> enrollments = enrollmentService.getAllEnrollmentsByStudentId(student.id());
        logger.info("Enrollments for " + student.email() + ":");
        enrollments.forEach(enrollment -> logger.info("Course ID: " + enrollment.courseId() + ", Status: " + enrollment.status()));

        // Drop the course
        enrollmentService.dropStudentFromCourse(student.id(), courseId);
        logger.info("Dropped course for student ID: " + student.id() + ", Course ID: " + courseId);
    }
}
