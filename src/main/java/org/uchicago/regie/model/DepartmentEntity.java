package org.uchicago.regie.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("departments")
public record DepartmentEntity(
        @Id Long id,
        String name,
        @Column("chair_email") String chairEmail
) {
}