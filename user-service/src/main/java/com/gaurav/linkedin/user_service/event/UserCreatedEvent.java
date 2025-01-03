package com.gaurav.linkedin.user_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreatedEvent {
    Long userId;
    String name;

}
