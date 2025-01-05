package com.gaurav.linkedin.user_service.event;

import lombok.Builder;
import lombok.Data;
@Data
public class UserCreatedEvent {
    Long userId;
    String name;

}
