package com.gaurav.linkedin.connection_service.event;

import lombok.Data;

@Data
public class AcceptConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
    private String senderUserName;
    private EventType eventType;
}
