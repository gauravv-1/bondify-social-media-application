package com.gaurav.linkedin.notification_service.dto;


import com.gaurav.linkedin.connection_service.event.EventType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class NotificationDto {

    private Long userId;
    private String userName;
    private String message;
    private LocalDateTime createdAt;
    private Long senderId;
    private EventType eventType;

}
