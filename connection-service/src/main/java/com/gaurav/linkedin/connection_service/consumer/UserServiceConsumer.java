package com.gaurav.linkedin.connection_service.consumer;

import com.gaurav.linkedin.connection_service.service.ConnectionsService;
import com.gaurav.linkedin.user_service.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceConsumer {

    private final ConnectionsService connectionsService;
    @KafkaListener(topics = "user-created-topic")
    public void handleUserCreated(UserCreatedEvent userCreatedEvent){
        connectionsService.createPersonNode(userCreatedEvent);
    }
}
