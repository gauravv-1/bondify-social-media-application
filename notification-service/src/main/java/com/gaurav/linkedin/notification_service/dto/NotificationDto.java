package com.gaurav.linkedin.notification_service.dto;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class NotificationDto {

    private String userName;
    private String message;
    private LocalDateTime createdAt;
}
