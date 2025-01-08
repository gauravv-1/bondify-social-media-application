package com.gaurav.linkedin.user_service.event;

import lombok.Data;

@Data
public class ProfileUpdatedEvent {
    Long userId;
    Long instituteId;
}
