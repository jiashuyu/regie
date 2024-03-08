package org.uchicago.regie.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.uchicago.regie.model.PrerequisiteEntity;

import java.util.List;

public interface PrerequisiteRepository extends CrudRepository<PrerequisiteEntity, Long> {

    @Query("SELECT prerequisite_id FROM course_prerequisites WHERE course_id = :courseId")
    List<Long> findPrerequisitesByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT course_id FROM course_prerequisites WHERE prerequisite_id = :prerequisiteId")
    List<Long> findCoursesForPrerequisite(@Param("prerequisiteId") Long prerequisiteId);

    @Query("SELECT COUNT(*) > 0 FROM course_prerequisites WHERE course_id = :courseId AND prerequisite_id = :prerequisiteId")
    boolean isPrerequisiteForCourse(@Param("courseId") Long courseId, @Param("prerequisiteId") Long prerequisiteId);

}
