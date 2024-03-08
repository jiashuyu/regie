package org.uchicago.regie.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("enrollments")
public record EnrollmentEntity(
        @Id Long id,
        @Column("student_id") Long studentId,
        @Column("course_id") Long courseId,
        String status, // 'registered', 'dropped', 'pending_approval'
        String quarter
) {
}