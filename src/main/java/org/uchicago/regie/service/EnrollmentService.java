package org.uchicago.regie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.repository.EnrollmentRepository;
import org.uchicago.regie.repository.LabRepository;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final LabRepository labRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, LabRepository labRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.labRepository = labRepository;
    }

    public String getEnrollmentStatus(long studentId, long courseId) {
        return enrollmentRepository.getEnrollmentStatus(studentId, courseId);
    }

    @Transactional
    public void registerStudentForCourse(long studentId, long courseId, String status) {
        // Check if the student is already enrolled in the course
        String existingStatus = enrollmentRepository.getEnrollmentStatus(studentId, courseId);
        if (existingStatus == null) {
            // Proceed with course registration if not already enrolled
            EnrollmentEntity newEnrollment = new EnrollmentEntity(null, studentId, courseId, status);
            enrollmentRepository.save(newEnrollment);

            // Check for an associated lab and enroll the student if it exists
            labRepository.findByCourseId(courseId).ifPresent(lab -> {
                EnrollmentEntity labEnrollment = new EnrollmentEntity(null, studentId, lab.id(), "registered");
                enrollmentRepository.save(labEnrollment);
            });
        } else if ("dropped".equals(existingStatus) || "pending_approval".equals(existingStatus)) {
            // Update the existing enrollment to the new status.
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

//    @Transactional
//    public void dropStudentFromCourse(long studentId, long courseId) {
//        updateEnrollmentStatus(studentId, courseId, "dropped");
//    }

    @Transactional
    public void dropStudentFromCourse(long studentId, long courseId) {
        // First, drop the student from the course
        updateEnrollmentStatus(studentId, courseId, "dropped");

        // Then, check if there is an associated lab and drop the student from the lab
        labRepository.findByCourseId(courseId).ifPresent(lab -> {
            // Assuming the lab is treated as a separate course for enrollment purposes
            // and that there's a method in EnrollmentRepository to directly delete an enrollment
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
