package org.uchicago.regie.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.NotificationEntity;

import java.util.List;

public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByStudentId(Long studentId);

    List<NotificationEntity> findBySentFalse();

    List<NotificationEntity> findByType(String type);

    @Query("SELECT COUNT(*) FROM notifications WHERE student_id = :studentId AND type = :type")
    int countByTypeAndStudentId(@Param("type") String type, @Param("studentId") Long studentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM notifications WHERE student_id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Modifying
    @Transactional
    @Query("UPDATE notifications SET sent = TRUE WHERE id = :notificationId")
    void markAsSent(@Param("notificationId") Long notificationId);

}
