package org.uchicago.regie.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("course_loads")
public record CourseLoadEntity(
        @Id @Column("student_id") Long studentId,
        @Column("current_load") int currentLoad,
        @Column("max_load") int maxLoad
) {
}
