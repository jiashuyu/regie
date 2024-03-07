package org.uchicago.regie.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.EnrollmentEntity;
import org.uchicago.regie.model.StudentEntity;
import org.uchicago.regie.repository.EnrollmentRepository;
import org.uchicago.regie.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Optional<StudentEntity> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public StudentEntity getStudentByEmail(String email) {
        return studentRepository.getStudentByEmail(email);
    }

    @Transactional
    public void registerStudentForCourse(long studentId, long courseId, String status) {
        EnrollmentEntity enrollmentEntity = new EnrollmentEntity(null, studentId, courseId, status);
        enrollmentRepository.save(enrollmentEntity);
    }

    @Transactional
    public void updateStudentEnrollmentStatus(long studentId, long courseId, String newStatus) {
        enrollmentRepository.updateEnrollmentStatus(studentId, courseId, newStatus);
    }

    @Transactional
    public void dropCourse(long studentId, long courseId) {
        enrollmentRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }

    public List<EnrollmentEntity> getStudentEnrollments(long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Transactional
    public StudentEntity createStudent(StudentEntity student) {
        return studentRepository.save(student);
    }

    @Transactional
    public StudentEntity updateStudentInformation(StudentEntity student) {
        studentRepository.save(student);
        return student;
    }

    @Transactional
    public void deleteStudent(long studentId) {
        // Delete the student's enrollments first to maintain referential integrity
        enrollmentRepository.deleteByStudentId(studentId);
        studentRepository.deleteById(studentId);
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    public List<StudentEntity> getAllStudentsByEnabledStatus(boolean enabled) {
        return studentRepository.findByEnabled(enabled);
    }

    @Transactional
    public void updateStudentStatusByEmail(String email, boolean enabled) {
        studentRepository.updateStudentEnabledStatus(email, enabled);
    }
}
