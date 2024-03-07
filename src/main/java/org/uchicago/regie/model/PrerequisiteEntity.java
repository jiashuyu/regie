package org.uchicago.regie.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("course_prerequisites")
public record PrerequisiteEntity(
        @Id Long id,
        @Column("course_id") Long courseId,
        @Column("prerequisite_id") Long prerequisiteId
) {
}