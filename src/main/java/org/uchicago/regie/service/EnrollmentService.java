package org.uchicago.regie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.repository.EnrollmentRepository;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public String getEnrollmentStatus(long studentId, long courseId) {
        return enrollmentRepository.getEnrollmentStatus(studentId, courseId);
    }

    @Transactional
    public void registerStudentForCourse(long studentId, long courseId, String status) {
        // Check if the student is already enrolled in the course
        String existingStatus = enrollmentRepository.getEnrollmentStatus(studentId, courseId);
        if (existingStatus == null) {
            // Proceed with registration if not already enrolled
            EnrollmentEntity newEnrollment = new EnrollmentEntity(null, studentId, courseId, status);
            enrollmentRepository.save(newEnrollment);
        } else if ("dropped".equals(existingStatus) || "pending_approval".equals(existingStatus)) {
            // If the student had previously dropped the course or their enrollment was pending, they might want to re-enroll or change status.
            enrollmentRepository.updateEnrollmentStatus(studentId, courseId, status);
        }
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
        updateEnrollmentStatus(studentId, courseId, "dropped");
    }

    public List<EnrollmentEntity> findPendingApprovalEnrollments() {
        return enrollmentRepository.findPendingApprovals();
    }

    @Transactional
    public void deleteEnrollment(long studentId, long courseId) {
        enrollmentRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }
}
