package com.gaurav.linkedin.notification_service.consumer;

import com.gaurav.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import com.gaurav.linkedin.connection_service.event.SendConnectionRequestEvent;
import com.gaurav.linkedin.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        log.info("handle connections: handleSendConnectionRequest: {}", sendConnectionRequestEvent);
        String message =
                "You have received a connection request from "+sendConnectionRequestEvent.getSenderUserName();
        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message,sendConnectionRequestEvent.getSenderUserName());
    }



    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        log.info("handle connections: handleAcceptConnectionRequest: {}", acceptConnectionRequestEvent);
        String message =
                "Your connection request has been accepted by "+acceptConnectionRequestEvent.getSenderUserName();
        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message, acceptConnectionRequestEvent.getSenderUserName());
    }

}
