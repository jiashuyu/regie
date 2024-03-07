package org.uchicago.regie.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "labs")
public record LabEntity(
        @Id Long id,
        @Column("course_id") Long courseId,
        @Column("lab_number") String labNumber
) {
}