package org.uchicago.regie.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("courses")
public record CourseEntity(
        @Id Long id,
        @Column("department_id") Long departmentId,
        @Column("course_number") String courseNumber,
        String name,
        @Column("max_enrollment") int maxEnrollment
) {
}
