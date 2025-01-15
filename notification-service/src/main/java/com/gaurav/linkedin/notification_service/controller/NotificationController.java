package com.gaurav.linkedin.notification_service.controller;


import com.gaurav.linkedin.notification_service.dto.NotificationDto;
import com.gaurav.linkedin.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j

@RequestMapping("/core")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("getNotificationsByUserId/{userId}")
    public ResponseEntity<List<NotificationDto>> getAllNotificationsByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(notificationService.getAllNotificationsByUserId(userId), HttpStatus.OK);
    }
}
