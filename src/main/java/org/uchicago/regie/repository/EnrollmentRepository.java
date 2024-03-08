package org.uchicago.regie.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.EnrollmentEntity;

import java.util.List;

public interface EnrollmentRepository extends CrudRepository<EnrollmentEntity, Long> {

    @Modifying
    @Query("UPDATE enrollments SET status = :newStatus WHERE student_id = :studentId AND course_id = :courseId")
    void updateEnrollmentStatus(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("newStatus") String newStatus);

    @Query("SELECT status FROM enrollments WHERE student_id = :studentId AND course_id = :courseId")
    String getEnrollmentStatus(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query("SELECT * FROM enrollments WHERE student_id = :studentId")
    List<EnrollmentEntity> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT * FROM enrollments e WHERE e.course_id = :courseId")
    List<EnrollmentEntity> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(*) FROM enrollments WHERE course_id = :courseId")
    int countByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT * FROM enrollments WHERE status = 'pending_approval'")
    List<EnrollmentEntity> findPendingApprovals();

    @Modifying
    @Transactional
    @Query("DELETE FROM enrollments WHERE student_id = :studentId AND course_id = :courseId")
    void deleteByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Modifying
    @Transactional
    @Query("DELETE FROM enrollments WHERE student_id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(*) FROM enrollments WHERE student_id = :studentId AND status = 'registered' AND quarter = :quarter")
    int countEnrollmentsForStudentByQuarter(@Param("studentId") Long studentId, @Param("quarter") String quarter);

}
