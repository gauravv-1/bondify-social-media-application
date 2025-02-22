package com.gaurav.linkedin.connection_service.event;

import lombok.Builder;
import lombok.Data;

@Data

public class SendConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
    private String senderUserName;
    private EventType eventType;
    private String userProfileUrl;
}
