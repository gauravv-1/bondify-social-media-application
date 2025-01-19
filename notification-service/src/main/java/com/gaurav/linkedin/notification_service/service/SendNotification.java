package com.gaurav.linkedin.notification_service.service;


import com.gaurav.linkedin.connection_service.event.EventType;
import com.gaurav.linkedin.notification_service.entity.Notification;
import com.gaurav.linkedin.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendNotification {

    private final NotificationRepository notificationRepository;

    public void send(Long userId, String message, String creatorName,
                     Long senderId, EventType eventType, String userProfileUrl) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUserId(userId);
        notification.setUserName(creatorName);
        notification.setSenderId(senderId);
        notification.setEventType(eventType);
        notification.setUserProfileUrl(userProfileUrl);

        notificationRepository.save(notification);
        log.info("Message :- {}",message);
        log.info("Notification saved for user: {}", userId);
    }
}
