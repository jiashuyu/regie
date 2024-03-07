package org.uchicago.regie.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("students")
public record StudentEntity(
        @Id Long id,
        String email,
        boolean enabled,
        String password,
        @Column("first_name") String firstName,
        @Column("last_name") String lastName
) {
}