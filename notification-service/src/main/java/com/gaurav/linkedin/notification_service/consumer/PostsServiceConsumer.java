package com.gaurav.linkedin.notification_service.consumer;


import com.gaurav.linkedin.connection_service.event.EventType;
import com.gaurav.linkedin.notification_service.clients.ConnectionsClient;
import com.gaurav.linkedin.notification_service.clients.ConnectionsResponse;
import com.gaurav.linkedin.notification_service.clients.WrappedResponse;
import com.gaurav.linkedin.notification_service.dto.Person;
import com.gaurav.linkedin.notification_service.service.SendNotification;
import com.gaurav.linkedin.posts_service.event.PostCreatedEvent;
import com.gaurav.linkedin.posts_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Received postCreatedEvent: {}", postCreatedEvent);

        Long userId = postCreatedEvent.getCreatorId();
        WrappedResponse<Person> response = connectionsClient.getFirstConnections(userId);
        List<Person> connections = response.getData();
        log.info("Fetched connections: {}", connections);

        for (Person connection : connections) {
            sendNotification.send(
                    connection.getUserId(),
                    "Your connection " + postCreatedEvent.getCreatorUserName() + " has created a post, Check it out",
                    postCreatedEvent.getCreatorUserName(), postCreatedEvent.getCreatorId(), EventType.POST_EVENT
            );
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Sending notifications: handlePostLiked: {}", postLikedEvent);
        String message = "Your post has been liked by "+postLikedEvent.getLikedByUserName();

        sendNotification.send(postLikedEvent.getCreatorId(), message, postLikedEvent.getLikedByUserName(), postLikedEvent.getLikedByUserId(),EventType.POST_EVENT);
    }

}
