package org.uchicago.regie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.repository.CourseRepository;
import org.uchicago.regie.repository.EnrollmentRepository;
import org.uchicago.regie.repository.LabRepository;
import org.uchicago.regie.repository.PrerequisiteRepository;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final LabRepository labRepository;
    private final CourseRepository courseRepository;
    private final PrerequisiteRepository prerequisiteRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, LabRepository labRepository,
                             CourseRepository courseRepository, PrerequisiteRepository prerequisiteRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.labRepository = labRepository;
        this.courseRepository = courseRepository;
        this.prerequisiteRepository = prerequisiteRepository;
    }

    public String getEnrollmentStatus(long studentId, long courseId) {
        return enrollmentRepository.getEnrollmentStatus(studentId, courseId);
    }

    @Transactional
    public void registerStudentForCourse(long studentId, long courseId, String status, String quarter) {
        // check if the enrollment limit is reached:
        int currentEnrollmentCount = courseRepository.countEnrollmentsByCourseId(courseId);
        int maxEnrollment = courseRepository.findMaxEnrollmentByCourseId(courseId);
        if (currentEnrollmentCount >= maxEnrollment) {
            throw new IllegalStateException("Enrollment limit reached for this course.");
        }

        // Check if prerequisites are fulfilled:
        List<Long> prerequisiteCourseIds = prerequisiteRepository.findPrerequisitesByCourseId(courseId);
        boolean prerequisitesFulfilled = checkPrerequisitesFulfilled(studentId, prerequisiteCourseIds);
        if (!prerequisitesFulfilled) {
            throw new IllegalStateException("Prerequisites not fulfilled for this course.");
        }

        // Check if the student is already enrolled in 3 courses for the quarter
        int currentCourseLoad = enrollmentRepository.countEnrollmentsForStudentByQuarter(studentId, quarter);
        if (currentCourseLoad >= 3) {
            throw new IllegalStateException("Student is already enrolled in the maximum number of courses for the quarter.");
        }

        // Check if the student is already enrolled in the course
        String existingStatus = enrollmentRepository.getEnrollmentStatus(studentId, courseId);
        if (existingStatus == null) {
            // Proceed with course registration if not already enrolled
            EnrollmentEntity newEnrollment = new EnrollmentEntity(null, studentId, courseId, status, quarter);
            enrollmentRepository.save(newEnrollment);

            // Check for an associated lab and enroll the student if it exists
            labRepository.findByCourseId(courseId).ifPresent(lab -> {
                EnrollmentEntity labEnrollment = new EnrollmentEntity(null, studentId, lab.id(), "registered", quarter);
                enrollmentRepository.save(labEnrollment);
            });
        } else if ("dropped".equals(existingStatus) || "pending_approval".equals(existingStatus)) {
            // Update the existing enrollment to the new status.
            enrollmentRepository.updateEnrollmentStatus(studentId, courseId, status);
        }
    }

    private boolean checkPrerequisitesFulfilled(Long studentId, List<Long> prerequisiteCourseIds) {
        for (Long prerequisiteCourseId : prerequisiteCourseIds) {
            String status = enrollmentRepository.getEnrollmentStatus(studentId, prerequisiteCourseId);
            if (!"completed".equals(status)) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public void updateEnrollmentStatus(long studentId, long courseId, String newStatus) {
        enrollmentRepository.updateEnrollmentStatus(studentId, courseId, newStatus);
    }

    public List<EnrollmentEntity> getAllEnrollmentsByStudentId(long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<EnrollmentEntity> getAllEnrollmentsByCourseId(long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Transactional
    public void dropStudentFromCourse(long studentId, long courseId) {
        // First, drop the student from the course
        updateEnrollmentStatus(studentId, courseId, "dropped");

        // Then, check if there is an associated lab and drop the student from the lab
        labRepository.findByCourseId(courseId).ifPresent(lab -> {
            enrollmentRepository.deleteByStudentIdAndCourseId(studentId, lab.id());
        });
    }

    public List<EnrollmentEntity> findPendingApprovalEnrollments() {
        return enrollmentRepository.findPendingApprovals();
    }

    @Transactional
    public void deleteEnrollment(long studentId, long courseId) {
        enrollmentRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }
}
