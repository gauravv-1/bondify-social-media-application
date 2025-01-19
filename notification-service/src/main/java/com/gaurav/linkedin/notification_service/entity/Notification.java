package com.gaurav.linkedin.notification_service.entity;

import com.gaurav.linkedin.connection_service.event.EventType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String userName;
    private String message;
    private Long senderId;
    private EventType eventType;
    private String userProfileUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
