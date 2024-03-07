package org.uchicago.regie.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("notifications")
public record NotificationEntity(
        @Id Long id,
        @Column("student_id") Long studentId,
        String type, // 'course_added', 'course_dropped', 'approval_request', 'approval_decision'
        String message,
        Boolean sent
) {
}
