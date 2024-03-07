package org.uchicago.regie.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.uchicago.regie.model.LabEntity;

import java.util.List;
import java.util.Optional;

public interface LabRepository extends CrudRepository<LabEntity, Long> {
    Optional<LabEntity> findByCourseId(Long courseId);

    // Find all labs for a specific department by joining with the courses table
    @Query("SELECT * FROM labs l JOIN courses c ON l.course_id = c.id WHERE c.department_id = :departmentId")
    List<LabEntity> findAllByDepartmentId(@Param("departmentId") Long departmentId);

    // Find all labs by lab number
    List<LabEntity> findByLabNumber(String labNumber);

    // Count how many labs are associated with a specific course
    @Query("SELECT COUNT(*) FROM labs WHERE course_id = :courseId")
    int countByCourseId(@Param("courseId") Long courseId);

    // Find all labs and order them by lab number
    List<LabEntity> findAllByOrderByLabNumberAsc();

}
