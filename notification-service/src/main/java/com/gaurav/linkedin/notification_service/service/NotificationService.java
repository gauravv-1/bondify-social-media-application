package com.gaurav.linkedin.notification_service.service;


import com.gaurav.linkedin.notification_service.dto.NotificationDto;
import com.gaurav.linkedin.notification_service.entity.Notification;
import com.gaurav.linkedin.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;
    public List<NotificationDto> getAllNotificationsByUserId(Long userId) {

        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
                .map((element)->modelMapper.map(element,NotificationDto.class))
                .collect(Collectors.toList());

    }
}
