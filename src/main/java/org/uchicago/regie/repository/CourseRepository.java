package org.uchicago.regie.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.uchicago.regie.model.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<CourseEntity, Long> {

    Optional<CourseEntity> findCourseById(Long id);

    List<CourseEntity> findByDepartmentId(Long departmentId);

    @Query("SELECT * FROM courses WHERE courses.max_enrollment > (SELECT COUNT(*) FROM enrollments WHERE course_id = courses.id AND status = 'registered')")
    List<CourseEntity> findCoursesWithOpenSlots();

    List<CourseEntity> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM courses c LEFT JOIN course_prerequisites p ON c.id = p.course_id WHERE c.id = :courseId")
    List<CourseEntity> findCourseWithPrerequisites(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(*) FROM enrollments WHERE course_id = :courseId AND status = 'registered'")
    int countEnrollmentsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT max_enrollment FROM courses WHERE id = :courseId")
    int findMaxEnrollmentByCourseId(@Param("courseId") Long courseId);


}
