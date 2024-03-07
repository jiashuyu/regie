package org.uchicago.regie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uchicago.regie.model.NotificationEntity;
import org.uchicago.regie.repository.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public NotificationEntity createNotification(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }

    public List<NotificationEntity> getAllNotificationsByStudentId(long studentId) {
        return notificationRepository.findByStudentId(studentId);
    }

    public List<NotificationEntity> getUnsentNotifications() {
        return notificationRepository.findBySentFalse();
    }

    @Transactional
    public void markNotificationAsSent(long notificationId) {
        notificationRepository.markAsSent(notificationId);
    }

    public List<NotificationEntity> getNotificationsByType(String type) {
        return notificationRepository.findByType(type);
    }

    @Transactional
    public void deleteNotificationsByStudentId(long studentId) {
        notificationRepository.deleteByStudentId(studentId);
    }

    public int countNotificationsByTypeAndStudentId(String type, long studentId) {
        return notificationRepository.countByTypeAndStudentId(type, studentId);
    }

    @Transactional
    public void processAndSendUnsentNotifications() {
        List<NotificationEntity> unsentNotifications = getUnsentNotifications();
        // Loop through each unsent notification and send it
        for (NotificationEntity notification : unsentNotifications) {
            // sendNotification(notification);
            markNotificationAsSent(notification.id());
        }
    }

}
